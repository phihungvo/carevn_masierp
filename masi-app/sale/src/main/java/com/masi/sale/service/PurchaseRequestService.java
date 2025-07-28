package com.masi.sale.service;

import com.masi.sale.domain.PurchaseRequest;
import com.masi.sale.domain.enumeration.PurchaseRequestStatus;
import com.masi.sale.repository.PurchaseRequestRepository;
import com.masi.sale.service.dto.*;
import com.masi.sale.service.mapper.PurchaseRequestMapper;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import com.masi.sale.service.web.client.FileClient;
import com.masi.sale.web.rest.errors.BadRequestAlertException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.io.InputStreamResource;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Implementation for managing {@link com.masi.sale.domain.PurchaseRequest}.
 */
@Service
@Transactional
public class PurchaseRequestService {

    private static final Logger log = LoggerFactory.getLogger(PurchaseRequestService.class);

    private final PurchaseRequestRepository purchaseRequestRepository;

    private final PurchaseRequestMapper purchaseRequestMapper;
    private final PurchaseRequestFileService purchaseRequestFileService;


    private final PurchaseDeliveryService purchaseDeliveryService;
    private final FileClient fileClient;


    public PurchaseRequestService(PurchaseRequestRepository purchaseRequestRepository, PurchaseRequestMapper purchaseRequestMapper
        , PurchaseRequestFileService purchaseRequestFileService, PurchaseDeliveryService purchaseDeliveryService,
                                  FileClient fileClient) {
        this.purchaseRequestRepository = purchaseRequestRepository;
        this.purchaseRequestMapper = purchaseRequestMapper;
        this.purchaseRequestFileService = purchaseRequestFileService;
        this.purchaseDeliveryService = purchaseDeliveryService;
        this.fileClient = fileClient;
    }

    /**
     * Save a purchaseRequest.
     *
     * @param dto the entity to save.
     * @return the persisted entity.
     */
    public Mono<PurchaseRequestDTO> save(PurchaseRequestDTO dto) {
        PurchaseRequest entity = dto.toEntity();
        entity.setRequestStatus(PurchaseRequestStatus.NEW);
        return purchaseRequestRepository.save(entity)
            .map(purchaseRequestMapper::toDto)
            .flatMap(savedDto -> {
                if (dto.getFiles() != null && !dto.getFiles().isEmpty()) {
                    return purchaseRequestFileService.saveAll(dto.getFiles(), savedDto.getId()).collectList().map(savedFiles -> savedDto);
                }
                return Mono.just(savedDto);
            })
            .flatMap(
                savedDto -> purchaseDeliveryService.save(
                        PurchaseDeliveryDTO.builder().
                            id(UUID.randomUUID()).
                            purchaseRequestId(savedDto.getId())
                            .deliveried(0f)
                            .waitingDelivery(savedDto.getQuantity())
                            .build())
                    .then(Mono.just(savedDto))
            );
    }

    /**
     * Update a purchaseRequest.
     *
     * @param purchaseRequestDTO the entity to save.
     * @return the persisted entity.
     */
    public Mono<PurchaseRequestDTO> update(PurchaseRequestDTO purchaseRequestDTO) {
        log.debug("Request to update PurchaseRequest : {}", purchaseRequestDTO);
        return purchaseRequestRepository
            .save(purchaseRequestMapper.toEntity(purchaseRequestDTO).setIsPersisted())
            .map(purchaseRequestMapper::toDto);
    }

    /**
     * Partially update a purchaseRequest.
     *
     * @param purchaseRequestDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Mono<PurchaseRequestDTO> partialUpdate(PurchaseRequestDTO purchaseRequestDTO) {
        log.debug("Request to partially update PurchaseRequest : {}", purchaseRequestDTO);

        return purchaseRequestRepository
            .findById(purchaseRequestDTO.getId())
            .<PurchaseRequest>handle((existingPurchaseRequest, sink) -> {
                List<PurchaseRequestStatus> allowedStatuses = Arrays.asList(PurchaseRequestStatus.NEW, PurchaseRequestStatus.REJECTED, PurchaseRequestStatus.WAITING_APPROVAL);
                if (!allowedStatuses.contains(existingPurchaseRequest.getRequestStatus())) {
                    sink.error(new BadRequestAlertException("Purchase Request with status " + existingPurchaseRequest.getRequestStatus() + " cannot be full updated", "PurchaseRequest", "status"));
                    return;
                }
                purchaseRequestDTO.applyChangesToEntity(existingPurchaseRequest);
                sink.next(existingPurchaseRequest.setIsPersisted());
            })
            .flatMap(purchaseRequestRepository::save)
            .map(purchaseRequestMapper::toDto)
            .flatMap(savedDto -> {
                if (purchaseRequestDTO.getFiles() != null && !purchaseRequestDTO.getFiles().isEmpty()) {
                    return purchaseRequestFileService.saveAll(purchaseRequestDTO.getFiles(), savedDto.getId()).collectList().map(savedFiles -> {
                        return savedDto;
                    });
                }
                return Mono.just(savedDto);
            });
    }

    @Transactional(readOnly = true)
    public Flux<PurchaseRequestDTO> findAllByQuery(PurchaseRequestQueryDTO queryDTO, Pageable pageable) {
        return purchaseRequestRepository.findAllByQuery(queryDTO, pageable).map(PurchaseRequest::toDTO);
    }

    @Transactional(readOnly = true)
    public Mono<Long> countAllByQuery(PurchaseRequestQueryDTO queryDTO) {
        return purchaseRequestRepository.countAllByQuery(queryDTO);
    }

    /**
     * Get all the purchaseRequests.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Flux<PurchaseRequestDTO> findAll(Pageable pageable) {
        log.debug("Request to get all PurchaseRequests");
        return purchaseRequestRepository.findAllBy(pageable).map(purchaseRequestMapper::toDto);
    }

    public Mono<Float> calculateRemaining(UUID reqId) {
        return purchaseDeliveryService.findOneByPurchaseRequestId(reqId).zipWith(findOne(reqId), (purchaseDeliveryDTO, purchaseRequestDTO) -> purchaseRequestDTO.getTotalPrice() - purchaseDeliveryDTO.getDeliveried());
    }

    /**
     * Returns the number of purchaseRequests available.
     *
     * @return the number of entities in the database.
     */
    public Mono<Long> countAll() {
        return purchaseRequestRepository.count();
    }

    /**
     * Get one purchaseRequest by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Mono<PurchaseRequestDTO> findOne(UUID id) {
        log.debug("Request to get PurchaseRequest : {}", id);
        Mono<PurchaseRequestDTO> dto = purchaseRequestRepository.getFirstByIdAndIsDeletedFalse(id).map(purchaseRequestMapper::toDto);
        return dto.
            switchIfEmpty(Mono.error(new BadRequestAlertException("Entity not found", "PurchaseRequest", "idnotfound")))
            .flatMap(purchaseRequestDTO -> purchaseDeliveryService.findOneByPurchaseRequestId(id)
                .switchIfEmpty(Mono.just(new PurchaseDeliveryDTO()))
                .map(purchaseDeliveryDTO -> {
                    purchaseRequestDTO.setPurchaseDelivery(purchaseDeliveryDTO);
                    return purchaseRequestDTO;
                })).flatMap(purchaseRequestDTO -> purchaseRequestFileService.findByPurchaseRequestId(id)
                .map(e -> UUID.fromString(e.getFilePath()))
                .collectList()
                .flatMap(fileDtos -> {
                    return fileClient.getFileAttachmentsByListIds(fileDtos).collectList().map(fileAttachments -> {
                        purchaseRequestDTO.setPurchaseRequestFiles(fileAttachments);
                        return purchaseRequestDTO;
                    });
                }));
    }

    /**
     * Delete the purchaseRequest by id.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    public Mono<Void> delete(UUID id) {
        log.debug("Request to delete PurchaseRequest : {}", id);
        return purchaseRequestRepository.findById(id)
            .<PurchaseRequest>handle((purchaseRequest, sink) -> {
                List<PurchaseRequestStatus> allowedStatuses = Arrays.asList(PurchaseRequestStatus.NEW, PurchaseRequestStatus.REJECTED, PurchaseRequestStatus.WAITING_APPROVAL);
                if (!allowedStatuses.contains(purchaseRequest.getRequestStatus())) {
                    sink.error(new BadRequestAlertException("Purchase Request with status " + purchaseRequest.getRequestStatus() + " cannot be deleted", "PurchaseRequest", "status"));
                    return;
                }
                purchaseRequest.setIsDeleted(true);
                purchaseRequest.setUpdatedDate(ZonedDateTime.now());
                sink.next(purchaseRequest.setIsPersisted());
            })
            .flatMap(purchaseRequestRepository::save)
            .then();
    }
}
