package com.masi.sale.service;

import com.carevn.masi.utils.SecurityUtils;
import com.masi.sale.domain.RequestApproval;
import com.masi.sale.domain.query.RequestApprovalCriteria;
import com.masi.sale.repository.RequestApprovalRepository;
import com.masi.sale.service.dto.*;
import com.masi.sale.service.mapper.RequestApprovalMapper;
import com.masi.sale.service.web.client.EmployeeClient;
import com.masi.sale.web.rest.errors.BadRequestAlertException;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.ZonedDateTime;
import java.util.*;
import java.util.function.Function;

/**
 * Service Implementation for managing
 * {@link com.masi.sale.domain.RequestApproval}.
 */
@Service
@Transactional
public class RequestApprovalService {

    private static final Logger log = LoggerFactory.getLogger(RequestApprovalService.class);

    private final RequestApprovalRepository requestApprovalRepository;

    private final RequestApprovalMapper requestApprovalMapper;
    private final EmployeeClient employeeClient;

    public RequestApprovalService(RequestApprovalRepository requestApprovalRepository,
                                  RequestApprovalMapper requestApprovalMapper, EmployeeClient employeeClient) {
        this.requestApprovalRepository = requestApprovalRepository;
        this.requestApprovalMapper = requestApprovalMapper;
        this.employeeClient = employeeClient;
    }

    @FunctionalInterface
    public interface SetReviewAsync<T extends Reviewable> {
        void apply(T docs, RequestApprovalDTO type);
    }

    public <T extends Reviewable> Mono<Void> getReviewsByType(List<T> docs, String type, SetReviewAsync<T> setReviewAsync) {
        Map<UUID, T> reviewableMap = new HashMap<>();
        docs.forEach(reviewable -> {
            reviewableMap.put(reviewable.getDocumentId(), reviewable);
        });
        return requestApprovalRepository.findByDocumentIdInAndTypeAndIsDeletedIsFalse(reviewableMap.keySet(), type).map(requestApprovalMapper::toDto)
            .collectList()
            .flatMap(requestApprovals -> {
                Map<UUID, List<RequestApprovalDTO>> requestApprovalMap = new HashMap<>();
                requestApprovals.forEach(requestApproval -> {
                    requestApprovalMap.putIfAbsent(requestApproval.getEmployeeId(), new ArrayList<>());
                    requestApprovalMap.get(requestApproval.getEmployeeId()).add(requestApproval);
                    Reviewable reviewable = reviewableMap.get(requestApproval.getDocumentId());
                    // call setReviewAsync
                    setReviewAsync.apply((T) reviewable, requestApproval);
                });
                return employeeClient.getEmployeesByListIds(new ArrayList<>(requestApprovalMap.keySet()))
                    .collectList()
                    .flatMap(employees -> {
                        employees.forEach(employee -> {
                            List<RequestApprovalDTO> l1 = requestApprovalMap.get(employee.getId());
                            if (l1 != null) {
                                l1.forEach(requestApproval -> {
                                    requestApproval.setEmployee(employee);
                                });
                            }
                        });
                        return Mono.empty();
                    })

                    .then(Mono.empty());
            }).doOnError(throwable -> {
                log.error("Error when resolveReviews", throwable);
            }).then(Mono.empty());

    }

    public Mono<Void> resolveReviews(Collection<? extends Reviewable> reviewables) {
        Map<UUID, Reviewable> reviewableMap = new HashMap<>();
        reviewables.forEach(reviewable -> {
            reviewableMap.put(reviewable.getDocumentId(), reviewable);

        });
        return requestApprovalRepository.findByDocumentIdInAndTypeIsNullAndIsDeletedIsFalse(reviewableMap.keySet())
            .map(requestApprovalMapper::toDto)
            .collectList()
            .flatMap(requestApprovals -> {
                // map betweem employeeId list requestApprovals
                Map<UUID, List<RequestApprovalDTO>> requestApprovalMap = new HashMap<>();
                requestApprovals.forEach(requestApproval -> {
                    requestApprovalMap.putIfAbsent(requestApproval.getEmployeeId(), new ArrayList<>());
                    requestApprovalMap.get(requestApproval.getEmployeeId()).add(requestApproval);
                    Reviewable reviewable = reviewableMap.get(requestApproval.getDocumentId());
                    if (reviewable != null) {
                        reviewable.addReview(requestApproval);
                    }
                });
                return employeeClient.getEmployeesByListIds(new ArrayList<>(requestApprovalMap.keySet()))
                    .collectList()
                    .flatMap(employees -> {
                        employees.forEach(employee -> {
                            List<RequestApprovalDTO> l1 = requestApprovalMap.get(employee.getId());
                            if (l1 != null) {
                                l1.forEach(requestApproval -> {
                                    requestApproval.setEmployee(employee);
                                });
                            }
                        });
                        return Mono.empty();
                    })

                    .then(Mono.empty());
            }).doOnError(throwable -> {
                log.error("Error when resolveReviews", throwable);
            }).then(Mono.empty());
    }

    /**
     * Save a requestApproval.
     *
     * @param requestApprovalDTO the entity to save.
     * @return the persisted entity.
     */
    public Mono<RequestApprovalDTO> save(RequestApprovalDTO requestApprovalDTO) {
        log.debug("Request to save RequestApproval : {}", requestApprovalDTO);
        return requestApprovalRepository.save(requestApprovalMapper.toEntity(requestApprovalDTO))
            .map(requestApprovalMapper::toDto);
    }

    public Mono<RequestApprovalDTO> requestReview(CreateReviewRequest createReviewRequest) {
        return requestReview(createReviewRequest, null);
    }

    public Mono<RequestApprovalDTO> requestReview(CreateReviewRequest createReviewRequest, String type) {
        log.info("Request to requestReview : {}", createReviewRequest);
        Mono<Void> deleteMono;
        if (StringUtils.isNotBlank(type)) {
            deleteMono = requestApprovalRepository.deleteAllByDocumentIdAndType(
                createReviewRequest.getDocumentId(),
                type
            );
        } else {
            deleteMono = requestApprovalRepository.deleteAllByDocumentIdAndTypeIsNull(
                createReviewRequest.getDocumentId()
            );
        }
        return deleteMono.then(createReviewRequest.toSetReviewAsync(type)
            .flatMap(requestApprovals -> {
                return Flux.fromIterable(requestApprovals)
                    .flatMap(requestApproval -> {
                        return requestApprovalRepository.save(requestApproval)
                            .map(requestApprovalMapper::toDto);
                    })
                    .collectList()
                    .map(requestApprovalDTOS -> requestApprovalDTOS.get(0));
            }));
    }

    public Mono<Boolean> isAllApproved(UUID documentId) {
        return requestApprovalRepository.countByDocumentIdAndResult(documentId, false).map(count -> count == 0);
    }

    public Mono<Boolean> isOneRejected(UUID documentId) {
        return requestApprovalRepository.countByDocumentIdAndResult(documentId, false).map(count -> count > 0);
    }

    public Mono<RequestApprovalDTO> handleReview(UpdateReview updateReview) {
        return handleReview(updateReview, null);
    }

    public Mono<RequestApprovalDTO> handleReview(UpdateReview updateReview, String type) {
        return SecurityUtils.getUserJWTDetail().flatMap(user -> {
            Mono<RequestApproval> requestApprovalMono;
            if (StringUtils.isNotBlank(type)) {
                requestApprovalMono = requestApprovalRepository.findFirstByDocumentIdAndEmployeeIdAndTypeAndIsDeletedIsFalse(
                    updateReview.getDocumentId(), user.getUserId(), type);
            } else {
                requestApprovalMono = requestApprovalRepository
                    .findFirstByDocumentIdAndEmployeeId(updateReview.getDocumentId(), user.getUserId());
            }

            return requestApprovalMono
                .flatMap(requestApproval -> {
                    if (requestApproval.getResult() != null) {
                        return Mono.error(new BadRequestAlertException("This request has been approved or rejected",
                            "RequestApproval", "requestApprovalHasBeenApprovedOrRejected"));
                    }
                    // không có rejectNote thì là approved :))
                    requestApproval.setResult(updateReview.getIsApproved());
                    if (updateReview.getIsApproved() && StringUtils.isEmpty(updateReview.getApprovedSign())) {
                        return Mono.error(new BadRequestAlertException("Approved sign is required",
                            "RequestApproval", "approvedSignIsRequired"));
                    }
                    if (!updateReview.getIsApproved() && StringUtils.isEmpty(updateReview.getRejectNote())) {
                        return Mono.error(new BadRequestAlertException("Reject note is required", "RequestApproval",
                            "rejectNoteIsRequired"));
                    }
                    requestApproval.setApprovedSign(updateReview.getApprovedSign());
                    requestApproval.setApprovedSignName(updateReview.getApprovedSignName());
                    requestApproval.setRejectNote(updateReview.getRejectNote());
                    requestApproval.setUpdatedAt(ZonedDateTime.now());
                    return requestApprovalRepository.save(requestApproval.setIsPersisted())
                        .map(requestApprovalMapper::toDto);
                })
                .doOnError(throwable -> {
                    log.error("Error when handleReview", throwable);
                })
                .switchIfEmpty(Mono.error(new BadRequestAlertException("RequestApproval not found",
                    "RequestApproval", "requestApprovalNotFound")));

        });
    }

    public Flux<RequestApprovalDTO> findByDocumentId(UUID documentId) {
        return requestApprovalRepository.findByDocumentIdAndIsDeletedIsFalse(documentId).map(requestApprovalMapper::toDto);
    }

    public Flux<RequestApprovalDTO> findByDocumentId(UUID documentId, String type) {
        return requestApprovalRepository.findByDocumentIdAndTypeAndIsDeletedIsFalse(documentId, type).map(requestApprovalMapper::toDto);
    }


    public Flux<RequestApprovalDTO> findByDocumentIdIn(Collection<UUID> documentIds) {
        return requestApprovalRepository.findByDocumentIdInAndTypeIsNullAndIsDeletedIsFalse(documentIds).map(requestApprovalMapper::toDto);
    }

    public Mono<List<DocumentReviewMap>> findReviewMapByDocumentIds(Collection<UUID> documentIds) {
        return requestApprovalRepository.findByDocumentIdInAndTypeIsNullAndIsDeletedIsFalse(documentIds)
            .collectMultimap(
                RequestApproval::getDocumentId,
                Function.identity())
            .map(map -> {
                List<DocumentReviewMap> result = new ArrayList<>();
                map.forEach((k, v) -> {
                    DocumentReviewMap documentReviewMap = new DocumentReviewMap();
                    documentReviewMap.setDocumentId(k);
                    documentReviewMap.setRequestApprovals(requestApprovalMapper.toDto(new ArrayList<>(v)));
                    result.add(documentReviewMap);
                });
                return result;
            });
    }

    /**
     * Update a requestApproval.
     *
     * @param requestApprovalDTO the entity to save.
     * @return the persisted entity.
     */
    public Mono<RequestApprovalDTO> update(RequestApprovalDTO requestApprovalDTO) {
        log.debug("Request to update RequestApproval : {}", requestApprovalDTO);
        return requestApprovalRepository
            .save(requestApprovalMapper.toEntity(requestApprovalDTO).setIsPersisted())
            .map(requestApprovalMapper::toDto);
    }

    /**
     * Partially update a requestApproval.
     *
     * @param requestApprovalDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Mono<RequestApprovalDTO> partialUpdate(RequestApprovalDTO requestApprovalDTO) {
        log.debug("Request to partially update RequestApproval : {}", requestApprovalDTO);

        return requestApprovalRepository
            .findById(requestApprovalDTO.getId())
            .map(existingRequestApproval -> {
                requestApprovalMapper.partialUpdate(existingRequestApproval, requestApprovalDTO);

                return existingRequestApproval;
            })
            .flatMap(requestApprovalRepository::save)
            .map(requestApprovalMapper::toDto);
    }

    /**
     * Find requestApprovals by Criteria.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Flux<RequestApprovalDTO> findByCriteria(RequestApprovalCriteria criteria, Pageable pageable) {
        log.debug("Request to get all RequestApprovals by Criteria");
        return requestApprovalRepository.findByCriteria(criteria, pageable).map(requestApprovalMapper::toDto);
    }

    /**
     * Find the count of requestApprovals by criteria.
     *
     * @param criteria filtering criteria
     * @return the count of requestApprovals
     */
    public Mono<Long> countByCriteria(RequestApprovalCriteria criteria) {
        log.debug("Request to get the count of all RequestApprovals by Criteria");
        return requestApprovalRepository.countByCriteria(criteria);
    }

    /**
     * Returns the number of requestApprovals available.
     *
     * @return the number of entities in the database.
     */
    public Mono<Long> countAll() {
        return requestApprovalRepository.count();
    }

    /**
     * Get one requestApproval by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Mono<RequestApprovalDTO> findOne(UUID id) {
        log.debug("Request to get RequestApproval : {}", id);
        return requestApprovalRepository.findById(id).map(requestApprovalMapper::toDto);
    }

    /**
     * Delete the requestApproval by id.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    public Mono<Void> delete(UUID id) {
        log.debug("Request to delete RequestApproval : {}", id);
        return requestApprovalRepository.deleteById(id);
    }
}
