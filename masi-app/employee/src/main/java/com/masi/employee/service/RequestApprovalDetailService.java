package com.masi.employee.service;

import com.carevn.masi.dto.DocumentReviewedEvent;
import com.masi.employee.domain.RequestApprovalDetail;
import com.masi.employee.domain.criteria.RequestApprovalDetailCriteria;
import com.masi.employee.repository.RequestApprovalDetailRepository;
import com.masi.employee.service.dto.DocumentReviewDTO;
import com.masi.employee.service.dto.RequestApprovalDetailDTO;
import com.masi.employee.service.mapper.RequestApprovalDetailMapper;

import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Implementation for managing {@link com.masi.employee.domain.RequestApprovalDetail}.
 */
@Service
@Transactional
public class RequestApprovalDetailService {

    private static final Logger LOG = LoggerFactory.getLogger(RequestApprovalDetailService.class);

    private final RequestApprovalDetailRepository requestApprovalDetailRepository;

    private final RequestApprovalDetailMapper requestApprovalDetailMapper;
    private final StreamBridge streamBridge;

    public RequestApprovalDetailService(RequestApprovalDetailRepository requestApprovalDetailRepository, RequestApprovalDetailMapper requestApprovalDetailMapper, StreamBridge streamBridge) {
        this.requestApprovalDetailRepository = requestApprovalDetailRepository;
        this.requestApprovalDetailMapper = requestApprovalDetailMapper;
        this.streamBridge = streamBridge;
    }

    /**
     * Save a requestApprovalDetail.
     *
     * @param requestApprovalDetailDTO the entity to save.
     * @return the persisted entity.
     */
    public Mono<RequestApprovalDetailDTO> save(RequestApprovalDetailDTO requestApprovalDetailDTO) {
        LOG.debug("Request to save RequestApprovalDetail : {}", requestApprovalDetailDTO);
        return requestApprovalDetailRepository.save(requestApprovalDetailMapper.toEntity(requestApprovalDetailDTO)).map(requestApprovalDetailMapper::toDto);
    }

    /**
     * Update a requestApprovalDetail.
     *
     * @param requestApprovalDetailDTO the entity to save.
     * @return the persisted entity.
     */
    public Mono<RequestApprovalDetailDTO> update(RequestApprovalDetailDTO requestApprovalDetailDTO) {
        LOG.debug("Request to update RequestApprovalDetail : {}", requestApprovalDetailDTO);
        return requestApprovalDetailRepository.save(requestApprovalDetailMapper.toEntity(requestApprovalDetailDTO).setIsPersisted()).map(requestApprovalDetailMapper::toDto);
    }

    public Mono<Void> handleReview(DocumentReviewDTO documentReviewDTO) {
        return requestApprovalDetailRepository.findById(documentReviewDTO.getId()).map(documentReviewDTO::applyTO).flatMap(requestApprovalDetailRepository::save).flatMap(requestApprovalDetail -> {
            return handleReview(requestApprovalDetail.getDocumentId()).then();
        }).then();
    }

    private Mono<Void> handleReview(UUID documentId) {
        return requestApprovalDetailRepository.findAllByDocumentId(documentId).collectList().flatMap(requestApprovalDetails -> {
            if (requestApprovalDetails.isEmpty()) {
                return Mono.empty();
            }
            var builder = DocumentReviewedEvent.builder().entityName(requestApprovalDetails.get(0).getEntityName()).documentId(requestApprovalDetails.get(0).getDocumentId().toString());

            var isAllApproved = requestApprovalDetails.stream().allMatch(RequestApprovalDetail::getIsApproved);
            if (isAllApproved) {
                LOG.info("All request approval details are approved for document {}", documentId);
                builder.isApproved(true);
                streamBridge.send(DocumentReviewedEvent.EVENT_NAME, builder.build(), MediaType.APPLICATION_JSON);
                return Mono.empty();
            }
            var isAnyRejected = requestApprovalDetails.stream().anyMatch(requestApprovalDetail -> !requestApprovalDetail.getIsApproved());
            if (isAnyRejected) {
                LOG.info("Some request approval details are rejected for document {}", documentId);
                builder.isApproved(false);
                streamBridge.send(DocumentReviewedEvent.EVENT_NAME, builder.build(), MediaType.APPLICATION_JSON);
                return Mono.empty();
            }
            return Mono.empty();
        }).then();
    }

    /**
     * Partially update a requestApprovalDetail.
     *
     * @param requestApprovalDetailDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Mono<RequestApprovalDetailDTO> partialUpdate(RequestApprovalDetailDTO requestApprovalDetailDTO) {
        LOG.debug("Request to partially update RequestApprovalDetail : {}", requestApprovalDetailDTO);

        return requestApprovalDetailRepository.findById(requestApprovalDetailDTO.getId()).map(existingRequestApprovalDetail -> {
            requestApprovalDetailMapper.partialUpdate(existingRequestApprovalDetail, requestApprovalDetailDTO);

            return existingRequestApprovalDetail;
        }).flatMap(requestApprovalDetailRepository::save).map(requestApprovalDetailMapper::toDto);
    }

    /**
     * Find requestApprovalDetails by Criteria.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Flux<RequestApprovalDetailDTO> findByCriteria(RequestApprovalDetailCriteria criteria, Pageable pageable) {
        LOG.debug("Request to get all RequestApprovalDetails by Criteria");
        return requestApprovalDetailRepository.findByCriteria(criteria, pageable).map(requestApprovalDetailMapper::toDto);
    }

    /**
     * Find the count of requestApprovalDetails by criteria.
     *
     * @param criteria filtering criteria
     * @return the count of requestApprovalDetails
     */
    public Mono<Long> countByCriteria(RequestApprovalDetailCriteria criteria) {
        LOG.debug("Request to get the count of all RequestApprovalDetails by Criteria");
        return requestApprovalDetailRepository.countByCriteria(criteria);
    }

    /**
     * Returns the number of requestApprovalDetails available.
     *
     * @return the number of entities in the database.
     */
    public Mono<Long> countAll() {
        return requestApprovalDetailRepository.count();
    }

    /**
     * Get one requestApprovalDetail by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Mono<RequestApprovalDetailDTO> findOne(UUID id) {
        LOG.debug("Request to get RequestApprovalDetail : {}", id);
        return requestApprovalDetailRepository.findById(id).map(requestApprovalDetailMapper::toDto);
    }

    /**
     * Delete the requestApprovalDetail by id.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    public Mono<Void> delete(UUID id) {
        LOG.debug("Request to delete RequestApprovalDetail : {}", id);
        return requestApprovalDetailRepository.deleteById(id);
    }
}
