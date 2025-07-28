package com.masi.employee.service;

import com.masi.employee.domain.DocumentSequence;
import com.masi.employee.repository.DocumentSequenceRepository;
import com.masi.employee.service.dto.DocumentSequenceDTO;
import com.masi.employee.service.mapper.DocumentSequenceMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Implementation for managing
 * {@link com.masi.employee.domain.DocumentSequence}.
 */
@Service
@Transactional
public class DocumentSequenceService {

    private static final Logger log = LoggerFactory.getLogger(DocumentSequenceService.class);

    private final DocumentSequenceRepository documentSequenceRepository;

    private final DocumentSequenceMapper documentSequenceMapper;

    public DocumentSequenceService(DocumentSequenceRepository documentSequenceRepository,
            DocumentSequenceMapper documentSequenceMapper) {
        this.documentSequenceRepository = documentSequenceRepository;
        this.documentSequenceMapper = documentSequenceMapper;
    }

    /**
     * Save a documentSequence.
     *
     * @param documentSequenceDTO the entity to save.
     * @return the persisted entity.
     */
    public Mono<DocumentSequenceDTO> save(DocumentSequenceDTO documentSequenceDTO) {
        log.debug("Request to save DocumentSequence : {}", documentSequenceDTO);
        return documentSequenceRepository.save(documentSequenceMapper.toEntity(documentSequenceDTO))
                .map(documentSequenceMapper::toDto);
    }

    /**
     * Update a documentSequence.
     *
     * @param documentSequenceDTO the entity to save.
     * @return the persisted entity.
     */
    public Mono<DocumentSequenceDTO> update(DocumentSequenceDTO documentSequenceDTO) {
        log.debug("Request to update DocumentSequence : {}", documentSequenceDTO);
        return documentSequenceRepository.save(documentSequenceMapper.toEntity(documentSequenceDTO))
                .map(documentSequenceMapper::toDto);
    }

    /**
     * Partially update a documentSequence.
     *
     * @param documentSequenceDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Mono<DocumentSequenceDTO> partialUpdate(DocumentSequenceDTO documentSequenceDTO) {
        log.debug("Request to partially update DocumentSequence : {}", documentSequenceDTO);

        return documentSequenceRepository
                .findById(documentSequenceDTO.getId())
                .map(existingDocumentSequence -> {
                    documentSequenceMapper.partialUpdate(existingDocumentSequence, documentSequenceDTO);

                    return existingDocumentSequence;
                })
                .flatMap(documentSequenceRepository::save)
                .map(documentSequenceMapper::toDto);
    }

    /**
     * Get all the documentSequences.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Flux<DocumentSequenceDTO> findAll() {
        log.debug("Request to get all DocumentSequences");
        return documentSequenceRepository.findAll().map(documentSequenceMapper::toDto);
    }

    /**
     * Returns the number of documentSequences available.
     *
     * @return the number of entities in the database.
     *
     */
    public Mono<Long> countAll() {
        return documentSequenceRepository.count();
    }

    /**
     * Get one documentSequence by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Mono<DocumentSequenceDTO> findOne(Long id) {
        log.debug("Request to get DocumentSequence : {}", id);
        return documentSequenceRepository.findById(id).map(documentSequenceMapper::toDto);
    }

    /**
     * Delete the documentSequence by id.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    public Mono<Void> delete(Long id) {
        log.debug("Request to delete DocumentSequence : {}", id);
        return documentSequenceRepository.deleteById(id);
    }

    public Mono<Integer> findOrInitSequence(String entityName, String company) {
        return documentSequenceRepository
                .findByEntityNameAndCompanyAndIsActiveIsTrue(entityName, company)
                .flatMap(sequence -> {
                    sequence.setCurrentSequence(sequence.getCurrentSequence() + 1);
                    return documentSequenceRepository.save(sequence).map(DocumentSequence::getCurrentSequence);
                })
                .switchIfEmpty(
                        documentSequenceRepository.save(new DocumentSequence().initNewSequence(entityName, company))
                                .map(DocumentSequence::getCurrentSequence));
    }
}
