package com.masi.sale.service;

import com.masi.sale.domain.PurchaseRequestFile;
import com.masi.sale.repository.PurchaseRequestFileRepository;
import com.masi.sale.service.dto.PurchaseRequestFileDTO;
import com.masi.sale.service.dto.PurchaseRequestFileUploadDTO;
import com.masi.sale.service.mapper.PurchaseRequestFileMapper;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Implementation for managing
 * {@link com.masi.sale.domain.PurchaseRequestFile}.
 */
@Service
public class PurchaseRequestFileService {

    private static final Logger log = LoggerFactory.getLogger(PurchaseRequestFileService.class);

    private final PurchaseRequestFileRepository purchaseRequestFileRepository;

    private final PurchaseRequestFileMapper purchaseRequestFileMapper;


    public PurchaseRequestFileService(PurchaseRequestFileRepository purchaseRequestFileRepository, PurchaseRequestFileMapper purchaseRequestFileMapper) {
        this.purchaseRequestFileRepository = purchaseRequestFileRepository;
        this.purchaseRequestFileMapper = purchaseRequestFileMapper;
    }




    public Flux<PurchaseRequestFileDTO> saveAll(List<String> fileId, UUID purchaseRequestId) {
        return Flux.fromIterable(fileId).flatMap(id -> {
            PurchaseRequestFile entity = new PurchaseRequestFile();
            entity.setId(UUID.randomUUID());
            entity.setPurchaseRequestId(purchaseRequestId);
            entity.setFilePath(id);
            return purchaseRequestFileRepository.save(entity);
        }).map(purchaseRequestFileMapper::toDto);
    }

   


    public Flux<PurchaseRequestFileDTO> findByPurchaseRequestId(UUID purchaseRequestId) {
        return purchaseRequestFileRepository.findAllByPurchaseRequestId(purchaseRequestId).map(purchaseRequestFileMapper::toDto);
    }

    /**
     * Update a purchaseRequestFile.
     *
     * @param purchaseRequestFileDTO the entity to save.
     * @return the persisted entity.
     */
    public Mono<PurchaseRequestFileDTO> update(PurchaseRequestFileDTO purchaseRequestFileDTO) {
        log.debug("Request to update PurchaseRequestFile : {}", purchaseRequestFileDTO);
        return purchaseRequestFileRepository.save(purchaseRequestFileMapper.toEntity(purchaseRequestFileDTO).setIsPersisted()).map(purchaseRequestFileMapper::toDto);
    }


    /**
     * Partially update a purchaseRequestFile.
     *
     * @param purchaseRequestFileDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Mono<PurchaseRequestFileDTO> partialUpdate(PurchaseRequestFileDTO purchaseRequestFileDTO) {
        log.debug("Request to partially update PurchaseRequestFile : {}", purchaseRequestFileDTO);

        return purchaseRequestFileRepository.findById(purchaseRequestFileDTO.getId()).map(existingPurchaseRequestFile -> {
            purchaseRequestFileMapper.partialUpdate(existingPurchaseRequestFile, purchaseRequestFileDTO);

            return existingPurchaseRequestFile;
        }).flatMap(purchaseRequestFileRepository::save).map(purchaseRequestFileMapper::toDto);
    }

    /**
     * Get all the purchaseRequestFiles.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Flux<PurchaseRequestFileDTO> findAll(Pageable pageable) {
        log.debug("Request to get all PurchaseRequestFiles");
        return purchaseRequestFileRepository.findAllBy(pageable).map(purchaseRequestFileMapper::toDto);
    }

    /**
     * Returns the number of purchaseRequestFiles available.
     *
     * @return the number of entities in the database.
     */
    public Mono<Long> countAll() {
        return purchaseRequestFileRepository.count();
    }

    /**
     * Get one purchaseRequestFile by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Mono<PurchaseRequestFileDTO> findOne(UUID id) {
        log.debug("Request to get PurchaseRequestFile : {}", id);
        return purchaseRequestFileRepository.findById(id).map(purchaseRequestFileMapper::toDto);
    }

    /**
     * Delete the purchaseRequestFile by id.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    public Mono<Void> delete(UUID id) {
        log.debug("Request to delete PurchaseRequestFile : {}", id);
        return purchaseRequestFileRepository.deleteById(id);
    }
}
