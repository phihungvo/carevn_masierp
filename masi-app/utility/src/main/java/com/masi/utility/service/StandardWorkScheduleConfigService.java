package com.masi.utility.service;

import com.masi.utility.domain.StandardWorkScheduleConfig;
import com.masi.utility.domain.criteria.StandardWorkScheduleConfigCriteria;
import com.masi.utility.repository.StandardWorkScheduleConfigRepository;
import com.masi.utility.service.dto.StandardWorkScheduleConfigDTO;
import com.masi.utility.service.dto.WorkScheduleQuery;
import com.masi.utility.service.mapper.StandardWorkScheduleConfigMapper;

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
 * Service Implementation for managing {@link com.masi.utility.domain.StandardWorkScheduleConfig}.
 */
@Service
@Transactional
public class StandardWorkScheduleConfigService {

    private static final Logger LOG = LoggerFactory.getLogger(StandardWorkScheduleConfigService.class);

    private final StandardWorkScheduleConfigRepository standardWorkScheduleConfigRepository;

    private final StandardWorkScheduleConfigMapper standardWorkScheduleConfigMapper;
    private final StandardWorkScheduleConfig.StandardWorkScheduleConfigBuilder DEFAULT_STANDARD_WORK_SCHEDULE_CONFIG = StandardWorkScheduleConfig.builder()
        .workHours(8)
        .numberOfShifts(2); // sáng/chiều

    public StandardWorkScheduleConfigService(
        StandardWorkScheduleConfigRepository standardWorkScheduleConfigRepository,
        StandardWorkScheduleConfigMapper standardWorkScheduleConfigMapper
    ) {
        this.standardWorkScheduleConfigRepository = standardWorkScheduleConfigRepository;
        this.standardWorkScheduleConfigMapper = standardWorkScheduleConfigMapper;
    }

    public Mono<List<StandardWorkScheduleConfigDTO>> findByQuery(WorkScheduleQuery query) {
        var standardWorkScheduleConfigCriteria = new StandardWorkScheduleConfigCriteria();
        standardWorkScheduleConfigCriteria.company().setEquals(query.getCompany());
        standardWorkScheduleConfigCriteria.departmentType().setEquals(query.getWorkspaceType());
        return standardWorkScheduleConfigRepository.findByCriteria(standardWorkScheduleConfigCriteria, null)
            .map(standardWorkScheduleConfigMapper::toDto)
            .collectList().flatMap(standardWorkScheduleConfigDTOS -> {
                var returnResult = new java.util.ArrayList<StandardWorkScheduleConfigDTO>(standardWorkScheduleConfigDTOS);
                List<StandardWorkScheduleConfig> dayNotHave = new java.util.ArrayList<>();
                for (int i = 1; i <= 7; i++) {
                    int finalI = i;
                    boolean isHave = standardWorkScheduleConfigDTOS.stream().anyMatch(config -> config.getDayOfWeek().equals(finalI));
                    if (!isHave) {
                        var data = DEFAULT_STANDARD_WORK_SCHEDULE_CONFIG
                            .id(UUID.randomUUID())
                            .dayOfWeek(i)
                            .name("Giờ làm việc thứ " + (i + 1))
                            .departmentType(query.getWorkspaceType())
                            .company(query.getCompany())
                            .build();
                        dayNotHave.add(data);
                        returnResult.add(standardWorkScheduleConfigMapper.toDto(data));
                    }
                }
                return standardWorkScheduleConfigRepository.saveAll(dayNotHave)
                    .collectList().then(Mono.just(returnResult));
            });
    }

    /**
     * Save a standardWorkScheduleConfig.
     *
     * @param standardWorkScheduleConfigDTO the entity to save.
     * @return the persisted entity.
     */
    public Mono<StandardWorkScheduleConfigDTO> save(StandardWorkScheduleConfigDTO standardWorkScheduleConfigDTO) {
        LOG.debug("Request to save StandardWorkScheduleConfig : {}", standardWorkScheduleConfigDTO);
        return standardWorkScheduleConfigRepository
            .save(standardWorkScheduleConfigMapper.toEntity(standardWorkScheduleConfigDTO))
            .map(standardWorkScheduleConfigMapper::toDto);
    }

    /**
     * Update a standardWorkScheduleConfig.
     *
     * @param standardWorkScheduleConfigDTO the entity to save.
     * @return the persisted entity.
     */
    public Mono<StandardWorkScheduleConfigDTO> update(StandardWorkScheduleConfigDTO standardWorkScheduleConfigDTO) {
        LOG.debug("Request to update StandardWorkScheduleConfig : {}", standardWorkScheduleConfigDTO);
        return standardWorkScheduleConfigRepository
            .save(standardWorkScheduleConfigMapper.toEntity(standardWorkScheduleConfigDTO).setIsPersisted())
            .map(standardWorkScheduleConfigMapper::toDto);
    }

    /**
     * Partially update a standardWorkScheduleConfig.
     *
     * @param standardWorkScheduleConfigDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Mono<StandardWorkScheduleConfigDTO> partialUpdate(StandardWorkScheduleConfigDTO standardWorkScheduleConfigDTO) {
        LOG.debug("Request to partially update StandardWorkScheduleConfig : {}", standardWorkScheduleConfigDTO);

        return standardWorkScheduleConfigRepository
            .findById(standardWorkScheduleConfigDTO.getId())
            .map(existingStandardWorkScheduleConfig -> {
                standardWorkScheduleConfigMapper.partialUpdate(existingStandardWorkScheduleConfig, standardWorkScheduleConfigDTO);

                return existingStandardWorkScheduleConfig;
            })
            .flatMap(standardWorkScheduleConfigRepository::save)
            .map(standardWorkScheduleConfigMapper::toDto);
    }

    /**
     * Find standardWorkScheduleConfigs by Criteria.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Flux<StandardWorkScheduleConfigDTO> findByCriteria(StandardWorkScheduleConfigCriteria criteria, Pageable pageable) {
        LOG.debug("Request to get all StandardWorkScheduleConfigs by Criteria");
        return standardWorkScheduleConfigRepository.findByCriteria(criteria, pageable).map(standardWorkScheduleConfigMapper::toDto);
    }

    /**
     * Find the count of standardWorkScheduleConfigs by criteria.
     *
     * @param criteria filtering criteria
     * @return the count of standardWorkScheduleConfigs
     */
    public Mono<Long> countByCriteria(StandardWorkScheduleConfigCriteria criteria) {
        LOG.debug("Request to get the count of all StandardWorkScheduleConfigs by Criteria");
        return standardWorkScheduleConfigRepository.countByCriteria(criteria);
    }

    /**
     * Returns the number of standardWorkScheduleConfigs available.
     *
     * @return the number of entities in the database.
     */
    public Mono<Long> countAll() {
        return standardWorkScheduleConfigRepository.count();
    }

    /**
     * Get one standardWorkScheduleConfig by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Mono<StandardWorkScheduleConfigDTO> findOne(UUID id) {
        LOG.debug("Request to get StandardWorkScheduleConfig : {}", id);
        return standardWorkScheduleConfigRepository.findById(id).map(standardWorkScheduleConfigMapper::toDto);
    }

    /**
     * Delete the standardWorkScheduleConfig by id.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    public Mono<Void> delete(UUID id) {
        LOG.debug("Request to delete StandardWorkScheduleConfig : {}", id);
        return standardWorkScheduleConfigRepository.deleteById(id);
    }
}
