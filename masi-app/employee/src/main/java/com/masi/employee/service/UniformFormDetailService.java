package com.masi.employee.service;

import com.masi.employee.repository.UniformFormDetailRepository;
import com.masi.employee.repository.UniformOrderProcessRepository;
import com.masi.employee.service.dto.UniformFormDetailDTO;
import com.masi.employee.service.mapper.UniformFormDetailMapper;

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
 * {@link com.masi.employee.domain.UniformFormDetail}.
 */
@Service
@Transactional
public class UniformFormDetailService {

    private static final Logger log = LoggerFactory.getLogger(UniformFormDetailService.class);

    private final UniformFormDetailRepository uniformFormDetailRepository;
    private final UniformOrderProcessRepository uniformOrderProcessRepository;

    private final UniformFormDetailMapper uniformFormDetailMapper;

    public UniformFormDetailService(
            UniformFormDetailRepository uniformFormDetailRepository,
            UniformFormDetailMapper uniformFormDetailMapper,
            UniformOrderProcessRepository uniformOrderProcessRepository) {
        this.uniformFormDetailRepository = uniformFormDetailRepository;
        this.uniformOrderProcessRepository = uniformOrderProcessRepository;
        this.uniformFormDetailMapper = uniformFormDetailMapper;
    }

    /**
     * Save a uniformFormDetail.
     *
     * @param uniformFormDetailDTO the entity to save.
     * @return the persisted entity.
     */
    public Mono<UniformFormDetailDTO> save(UniformFormDetailDTO uniformFormDetailDTO) {
        log.debug("Request to save UniformFormDetail : {}", uniformFormDetailDTO);
        return uniformFormDetailRepository.save(uniformFormDetailMapper.toEntity(uniformFormDetailDTO))
                .map(uniformFormDetailMapper::toDto);
    }

    /**
     * Update a uniformFormDetail.
     *
     * @param uniformFormDetailDTO the entity to save.
     * @return the persisted entity.
     */
    public Mono<UniformFormDetailDTO> update(UniformFormDetailDTO uniformFormDetailDTO) {
        log.debug("Request to update UniformFormDetail : {}", uniformFormDetailDTO);
        return uniformFormDetailRepository
                .save(uniformFormDetailMapper.toEntity(uniformFormDetailDTO).setIsPersisted())
                .map(uniformFormDetailMapper::toDto);
    }

    /**
     * Partially update a uniformFormDetail.
     *
     * @param uniformFormDetailDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Mono<UniformFormDetailDTO> partialUpdate(UniformFormDetailDTO uniformFormDetailDTO) {
        log.debug("Request to partially update UniformFormDetail : {}", uniformFormDetailDTO);

        return uniformFormDetailRepository
                .findById(uniformFormDetailDTO.getId())
                .map(existingUniformFormDetail -> {
                    uniformFormDetailMapper.partialUpdate(existingUniformFormDetail, uniformFormDetailDTO);

                    return existingUniformFormDetail;
                })
                .flatMap(uniformFormDetailRepository::save)
                .map(uniformFormDetailMapper::toDto);
    }

    /**
     * Get all the uniformFormDetails.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Flux<UniformFormDetailDTO> findAll(Pageable pageable) {
        log.debug("Request to get all UniformFormDetails");
        return uniformFormDetailRepository.findAllBy(pageable).map(uniformFormDetailMapper::toDto);
    }

    /**
     * Returns the number of uniformFormDetails available.
     *
     * @return the number of entities in the database.
     *
     */
    public Mono<Long> countAll() {
        return uniformFormDetailRepository.count();
    }

    /**
     * Get one uniformFormDetail by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Mono<UniformFormDetailDTO> findOne(UUID id) {
        log.debug("Request to get UniformFormDetail : {}", id);
        return uniformFormDetailRepository.findById(id).map(uniformFormDetailMapper::toDto);
    }

    /**
     * Delete the uniformFormDetail by id.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    public Mono<Void> delete(UUID id) {
        log.debug("Request to delete UniformFormDetail : {}", id);
        return uniformFormDetailRepository.deleteById(id);
    }

    // save all
    public Flux<UniformFormDetailDTO> saveAll(List<UniformFormDetailDTO> uniformFormDetailDTO) {
        log.debug("Request to saveAll UniformFormDetail : {}", uniformFormDetailDTO);
        return uniformFormDetailRepository
                .saveAll(uniformFormDetailMapper.toEntity(uniformFormDetailDTO))
                .map(uniformFormDetailMapper::toDto);
    }

    // remove and create new by uniform order id
    public Flux<UniformFormDetailDTO> deleteAndCreateByUniformOrderId(UUID id, String deleteBy,
            List<UniformFormDetailDTO> uniformFormDetailDTOs) {
        log.debug("Request to deleteAndCreateByUniformOrderId : {}", id);
        return uniformFormDetailRepository.deleteByUniformOrder(id, deleteBy).thenReturn(saveAll(uniformFormDetailDTOs))
                .flatMapMany(Flux::from);
    }

    public Flux<UniformFormDetailDTO> findAllByUniformOrderId(UUID id) {
        return uniformFormDetailRepository.findAllByUniformOrderId(id).map(uniformFormDetailMapper::toDto);
    }

}
