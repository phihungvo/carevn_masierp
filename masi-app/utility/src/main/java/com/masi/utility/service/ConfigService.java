package com.masi.utility.service;

import com.carevn.masi.utils.SecurityUtils;
import com.masi.utility.domain.criteria.ConfigCriteria;
import com.masi.utility.repository.ConfigRepository;
import com.masi.utility.service.dto.ConfigDTO;
import com.masi.utility.service.mapper.ConfigMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Implementation for managing {@link com.masi.utility.domain.Config}.
 */
@Service
@Transactional
public class ConfigService {

    private static final Logger LOG = LoggerFactory.getLogger(ConfigService.class);

    private final ConfigRepository configRepository;

    private final ConfigMapper configMapper;

    public ConfigService(ConfigRepository configRepository, ConfigMapper configMapper) {
        this.configRepository = configRepository;
        this.configMapper = configMapper;
    }

    /**
     * Save a config.
     *
     * @param configDTO the entity to save.
     * @return the persisted entity.
     */
    public Mono<ConfigDTO> save(ConfigDTO configDTO) {
        LOG.debug("Request to save Config : {}", configDTO);
        return configRepository.save(configMapper.toEntity(configDTO)).map(configMapper::toDto);
    }

    /**
     * Update a config.
     *
     * @param configDTO the entity to save.
     * @return the persisted entity.
     */
    public Mono<ConfigDTO> update(ConfigDTO configDTO) {
        LOG.debug("Request to update Config : {}", configDTO);
        return configRepository.save(configMapper.toEntity(configDTO)).map(configMapper::toDto);
    }

    /**
     * Partially update a config.
     *
     * @param configDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Mono<ConfigDTO> partialUpdate(ConfigDTO configDTO) {
        LOG.debug("Request to partially update Config : {}", configDTO);

        return configRepository
                .findById(configDTO.getId())
                .map(existingConfig -> {
                    configDTO.applyChange(existingConfig);

                    return existingConfig;
                })
                .flatMap(configRepository::save)
                .map(configMapper::toDto);
    }

    /**
     * Find configs by Criteria.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Flux<ConfigDTO> findByCriteria(ConfigCriteria criteria, Pageable pageable) {
        LOG.debug("Request to get all Configs by Criteria");
        return configRepository.findByCriteria(criteria, pageable).map(configMapper::toDto);
    }

    /**
     * Find the count of configs by criteria.
     *
     * @param criteria filtering criteria
     * @return the count of configs
     */
    public Mono<Long> countByCriteria(ConfigCriteria criteria) {
        LOG.debug("Request to get the count of all Configs by Criteria");
        return configRepository.countByCriteria(criteria);
    }

    /**
     * Returns the number of configs available.
     *
     * @return the number of entities in the database.
     */
    public Mono<Long> countAll() {
        return configRepository.count();
    }

    /**
     * Get one config by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Mono<ConfigDTO> findOne(Long id) {
        LOG.debug("Request to get Config : {}", id);
        return configRepository.findById(id).map(configMapper::toDto);
    }

    @Transactional(readOnly = true)
    public Mono<ConfigDTO> findByKey(String key) {
        LOG.debug("Request to get Config by key : {}", key);
        return SecurityUtils.getUserJWTDetail().flatMap(user -> configRepository.findOneByKeyAndCompany(key, user.getCompanyId())).map(configMapper::toDto);
    }

    public Mono<ConfigDTO> createConfig(ConfigDTO configDTO) {
        LOG.debug("Request to create Config : {}", configDTO);
        return SecurityUtils.getUserJWTDetail().flatMap(user -> {
            configDTO.setUpdatedBy(user.getUserId().toString());
            configDTO.setCompany(user.getCompanyId());
            var entity = configMapper.toEntity(configDTO);
            return configRepository.save(entity).map(configMapper::toDto);
        });
    }

    /**
     * Delete the config by id.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    public Mono<Void> delete(Long id) {
        LOG.debug("Request to delete Config : {}", id);
        return configRepository.deleteById(id);
    }
}
