package com.masi.logistics.service;

import com.carevn.masi.dto.DocumentReviewMap;
import com.carevn.masi.dto.Reviewable;
import com.carevn.masi.utils.SecurityUtils;
import com.masi.logistics.domain.RequestApproval;
import com.masi.logistics.domain.criteria.RequestApprovalCriteria;
import com.masi.logistics.repository.RequestApprovalRepository;
import com.masi.logistics.service.dto.CreateReviewRequest;
import com.masi.logistics.service.dto.RequestApprovalDTO;
import com.masi.logistics.service.dto.UpdateReview;
import com.masi.logistics.service.mapper.RequestApprovalMapper;
import com.masi.logistics.service.web.client.EmployeeClient;
import com.masi.logistics.web.rest.errors.BadRequestAlertException;
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
 * Service Implementation for managing {@link com.masi.logistics.domain.RequestApproval}.
 */
@Service
@Transactional
public class RequestApprovalService {

    private static final Logger log = LoggerFactory.getLogger(RequestApprovalService.class);

    private final RequestApprovalRepository requestApprovalRepository;

    private final RequestApprovalMapper requestApprovalMapper;
    private final EmployeeClient employeeClient;

    public RequestApprovalService(RequestApprovalRepository requestApprovalRepository, RequestApprovalMapper requestApprovalMapper, EmployeeClient employeeClient) {
        this.requestApprovalRepository = requestApprovalRepository;
        this.requestApprovalMapper = requestApprovalMapper;
        this.employeeClient = employeeClient;
    }

    public Mono<Void> resolveReviews(Collection<? extends Reviewable<RequestApprovalDTO>> reviewables) {
        Map<UUID, Reviewable<RequestApprovalDTO>> reviewableMap = new HashMap<>();
        reviewables.forEach(reviewable -> {
            reviewableMap.put(reviewable.getDocumentId(), reviewable);
        });
        return requestApprovalRepository.findByDocumentIdIn(reviewableMap.keySet())
            .map(requestApprovalMapper::toDto)
            .collectList()
            .flatMap(requestApprovals -> {
                // map betweem employeeId list requestApprovals
                Map<UUID, List<RequestApprovalDTO>> requestApprovalMap = new HashMap<>();
                requestApprovals.forEach(requestApproval -> {
                    requestApprovalMap.putIfAbsent(requestApproval.getEmployeeId(), new ArrayList<>());
                    requestApprovalMap.get(requestApproval.getEmployeeId()).add(requestApproval);
                    Reviewable<RequestApprovalDTO> reviewable = reviewableMap.get(requestApproval.getDocumentId());
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
        return requestApprovalRepository.save(requestApprovalMapper.toEntity(requestApprovalDTO)).map(requestApprovalMapper::toDto);
    }



    public Mono<Boolean> isAllApproved(UUID documentId) {
        return requestApprovalRepository.countByDocumentIdAndResult(documentId, false).map(count -> count == 0);
    }

    public Mono<Boolean> isOneRejected(UUID documentId) {
        return requestApprovalRepository.countByDocumentIdAndResult(documentId, false).map(count -> count > 0);
    }



    public Flux<RequestApprovalDTO> findByDocumentId(UUID documentId) {
        return requestApprovalRepository.findByDocumentIdAndIsDeleted(documentId,false).map(requestApprovalMapper::toDto);
    }

    public Flux<RequestApprovalDTO> findByDocumentIdAndGroup(UUID documentId, String group) {
        return requestApprovalRepository.findByDocumentIdAndIsDeletedAndGroup(documentId, false, group).map(requestApprovalMapper::toDto);
    }

    public Flux<RequestApprovalDTO> findByDocumentIdIn(Collection<UUID> documentIds) {
        return requestApprovalRepository.findByDocumentIdIn(documentIds).map(requestApprovalMapper::toDto);
    }

    public Mono<List<DocumentReviewMap<RequestApprovalDTO>>> findReviewMapByDocumentIds(Collection<UUID> documentIds) {
        return requestApprovalRepository.findByDocumentIdIn(documentIds)
            .collectMultimap(
                RequestApproval::getDocumentId,
                Function.identity())
            .map(map -> {
                List<DocumentReviewMap<RequestApprovalDTO>> result = new ArrayList<>();
                map.forEach((k, v) -> {
                    DocumentReviewMap<RequestApprovalDTO> documentReviewMap = new DocumentReviewMap<RequestApprovalDTO>();
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

    public Mono<RequestApprovalDTO> requestReview(CreateReviewRequest createReviewRequest) {
        log.info("Request to requestReview : {}", createReviewRequest);
        return requestApprovalRepository.deleteAllByDocumentId(createReviewRequest.getDocumentId())
                .then(createReviewRequest.toSetReviewAsync())
                .flatMapMany(Flux::fromIterable)
                .flatMap(requestApproval ->
                        requestApprovalRepository.save(requestApproval)
                                .map(requestApprovalMapper::toDto)
                )
                .collectList()
                .filter(requestApprovalDTOS -> !requestApprovalDTOS.isEmpty())
                .map(requestApprovalDTOS -> requestApprovalDTOS.get(0));
    }


    public Mono<RequestApprovalDTO> handleReview(UpdateReview updateReview) {
        return SecurityUtils.getUserJWTDetail().flatMap(user -> {
            return requestApprovalRepository.findFirstByDocumentIdAndEmployeeIdAndIsDeleted(updateReview.getDocumentId(), user.getUserId(),false)
                    .flatMap(requestApproval -> {
                        if (requestApproval.getResult() != null) {
                            return Mono.error(new BadRequestAlertException("This request has been approved or rejected", "RequestApproval", "requestApprovalHasBeenApprovedOrRejected"));
                        }
                        // không có rejectNote thì là approved :))
                        requestApproval.setResult(updateReview.getIsApproved());
                        if (updateReview.getIsApproved() && StringUtils.isEmpty(updateReview.getApprovedSign())) {
                            return Mono.error(new BadRequestAlertException("Approved sign is required", "RequestApproval", "approvedSignIsRequired"));
                        }
                        if (!updateReview.getIsApproved() && StringUtils.isEmpty(updateReview.getRejectNote())) {
                            return Mono.error(new BadRequestAlertException("Reject note is required", "RequestApproval", "rejectNoteIsRequired"));
                        }
                        requestApproval.setApprovedSign(updateReview.getApprovedSign());
                        requestApproval.setApprovedSignName(updateReview.getApprovedSignName());
                        requestApproval.setRejectNote(updateReview.getRejectNote());
                        requestApproval.setUpdatedAt(ZonedDateTime.now());
                        return requestApprovalRepository.save(requestApproval.setIsPersisted()).map(requestApprovalMapper::toDto);
                    })
                    .doOnError(throwable -> {
                        log.error("Error when handleReview", throwable);
                    })
                    .switchIfEmpty(Mono.error(new BadRequestAlertException("RequestApproval not found", "RequestApproval", "requestApprovalNotFound")));

        });
    }

    public Mono<Void> updateCleanAgain(UUID documentId) {
        return requestApprovalRepository.updateCleanAgain(documentId);
    }

    public Mono<List<RequestApprovalDTO>> fetchRequestApprovalsWithEmployees(List<UUID> inventoryIds) {
        if (inventoryIds.isEmpty()) {
            return Mono.just(Collections.emptyList());
        }
        return requestApprovalRepository.findAllByDocumentIdsAndIsDeletedIsFalse(inventoryIds)
                .map(requestApprovalMapper::toDto)
                .collectList()
                .flatMap(requestApprovals -> {
                    List<UUID> employeeIds = requestApprovals.stream()
                            .map(RequestApprovalDTO::getEmployeeId)
                            .distinct()
                            .toList();

                    return employeeClient.getEmployeesByListIds(employeeIds)
                            .collectList()
                            .map(employees -> {
                                requestApprovals.forEach(requestApproval ->
                                        employees.stream()
                                                .filter(employee -> employee.getId().equals(requestApproval.getEmployeeId()))
                                                .findFirst()
                                                .ifPresent(requestApproval::setEmployee));
                                return requestApprovals;
                            });
                });
    }
}
