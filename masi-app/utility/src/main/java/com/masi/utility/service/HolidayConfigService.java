package com.masi.utility.service;

import com.masi.utility.domain.HolidayConfig;
import com.masi.utility.domain.criteria.HolidayConfigCriteria;
import com.masi.utility.domain.enumeration.HolidayType;
import com.masi.utility.repository.HolidayConfigRepository;
import com.masi.utility.service.dto.HolidayConfigDTO;
import com.masi.utility.service.mapper.HolidayConfigMapper;

import java.time.LocalDate;
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
 * Service Implementation for managing {@link com.masi.utility.domain.HolidayConfig}.
 */
@Service
@Transactional
public class HolidayConfigService {

    private static final Logger LOG = LoggerFactory.getLogger(HolidayConfigService.class);

    private final HolidayConfigRepository holidayConfigRepository;

    private final HolidayConfigMapper holidayConfigMapper;

    public HolidayConfigService(HolidayConfigRepository holidayConfigRepository, HolidayConfigMapper holidayConfigMapper) {
        this.holidayConfigRepository = holidayConfigRepository;
        this.holidayConfigMapper = holidayConfigMapper;
    }


    public Mono<HolidayConfigDTO> save(HolidayConfigDTO holidayConfigDTO) {
        HolidayConfigCriteria criteria = new HolidayConfigCriteria();
        criteria.type().setEquals(holidayConfigDTO.getType());
        criteria.calenderType().setEquals(holidayConfigDTO.getCalenderType());
        if (holidayConfigDTO.getType() == HolidayType.FLEXIBLE) {
            criteria.date().setEquals(holidayConfigDTO.getDate());
        } else {
            criteria.setFixedDate(holidayConfigDTO.getDate());
        }
        return holidayConfigRepository.findByCriteria(criteria, Pageable.ofSize(1))
            .collectList()
            .flatMap(holidayConfigs -> {
                if (holidayConfigs.isEmpty()) {
                    return holidayConfigRepository
                        .save(holidayConfigMapper.toEntity(holidayConfigDTO))
                        .map(holidayConfigMapper::toDto);
                } else {
                    holidayConfigDTO.applyChanges(holidayConfigs.get(0));
                    return Mono.just(holidayConfigMapper.toDto(holidayConfigs.get(0)));
                }
            });
    }

    /**
     * Find holidayConfigs by Criteria.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Flux<HolidayConfigDTO> findByCriteria(HolidayConfigCriteria criteria, Pageable pageable) {
        LOG.debug("Request to get all HolidayConfigs by Criteria");
        return holidayConfigRepository.findByCriteria(criteria, pageable).map(holidayConfigMapper::toDto);
    }

    /**
     * Find the count of holidayConfigs by criteria.
     *
     * @param criteria filtering criteria
     * @return the count of holidayConfigs
     */
    public Mono<Long> countByCriteria(HolidayConfigCriteria criteria) {
        LOG.debug("Request to get the count of all HolidayConfigs by Criteria");
        return holidayConfigRepository.countByCriteria(criteria);
    }

    /**
     * Get one holidayConfig by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Mono<HolidayConfigDTO> findOne(UUID id) {
        LOG.debug("Request to get HolidayConfig : {}", id);
        return holidayConfigRepository.findById(id).map(holidayConfigMapper::toDto);
    }

    /**
     * Delete the holidayConfig by id.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    public Mono<Void> delete(UUID id) {
        LOG.debug("Request to delete HolidayConfig : {}", id);
        return holidayConfigRepository.deleteById(id);
    }

    public Mono<List<HolidayConfigDTO>> findByMonth(int month, int year) {
        HolidayConfigCriteria criteria = new HolidayConfigCriteria();
        criteria.setFixedToDate(LocalDate.of(year, month, 1).plusMonths(1).minusDays(1));
        criteria.setFixedFromDate(LocalDate.of(year, month, 1));
        return holidayConfigRepository.findByCriteria(criteria, null).map(holidayConfigMapper::toDto).collectList();

    }
}
