package com.carevn.masi.service;

import com.carevn.masi.repository.CompanyRepository;
import com.carevn.masi.service.dto.CompanyDTO;
import com.carevn.masi.service.dto.CompanyQuery;
import com.carevn.masi.service.mapper.CompanyMapper;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Implementation for managing {@link com.carevn.masi.domain.Company}.
 */
@Service
@Transactional
public class CompanyService {

    private static final Logger log = LoggerFactory.getLogger(CompanyService.class);

    private final CompanyRepository companyRepository;

    private final CompanyMapper companyMapper;


    public CompanyService(
        CompanyRepository companyRepository,
        CompanyMapper companyMapper
    ) {
        this.companyRepository = companyRepository;
        this.companyMapper = companyMapper;
    }

    /**
     * Save a company.
     *
     * @param companyDTO the entity to save.
     * @return the persisted entity.
     */
    public Mono<CompanyDTO> save(CompanyDTO companyDTO) {
        log.debug("Request to save Company : {}", companyDTO);
        return companyRepository.save(companyMapper.toEntity(companyDTO)).map(companyMapper::toDto);
    }

    /**
     * Update a company.
     *
     * @param companyDTO the entity to save.
     * @return the persisted entity.
     */
    public Mono<CompanyDTO> update(CompanyDTO companyDTO) {
        log.debug("Request to update Company : {}", companyDTO);
        return companyRepository
            .save(companyMapper.toEntity(companyDTO).setIsPersisted())
            .map(companyMapper::toDto);
    }

    /**
     * Partially update a company.
     *
     * @param companyDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Mono<CompanyDTO> partialUpdate(CompanyDTO companyDTO) {
        log.debug("Request to partially update Company : {}", companyDTO);

        return companyRepository
            .findById(companyDTO.getId())
            .map(existingCompany -> {
                companyMapper.partialUpdate(existingCompany, companyDTO);
                existingCompany.setIsPersisted();
                return existingCompany;
            })
            .flatMap(companyRepository::save)
            .flatMap(Mono::just)
            .map(companyMapper::toDto);
    }

    public Mono<CompanyDTO> activate(UUID id) {
        log.debug("Request to activate Company : {}", id);
        return companyRepository
            .findById(id)
            .map(company -> {
                company.setIsActivated(true);
                company.setIsPersisted();
                return company;
            })
            .flatMap(companyRepository::save)
            .flatMap(Mono::just)
            .map(companyMapper::toDto);
    }

    public Mono<CompanyDTO> deactivate(UUID id) {
        log.debug("Request to deactivate Company : {}", id);
        return companyRepository
            .findById(id)
            .map(company -> {
                company.setIsActivated(false);
                company.setIsPersisted();
                return company;
            })
            .flatMap(companyRepository::save)
            .flatMap(Mono::just)
            .map(companyMapper::toDto);
    }

    /**
     * Get all the companies.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Flux<CompanyDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Companies");
        return companyRepository.findAllBy(pageable).map(companyMapper::toDto);
    }

    /**
     * Returns the number of companies available.
     * @return the number of entities in the database.
     *
     */
    public Mono<Long> countAll() {
        return companyRepository.count();
    }



    /**
     * Get one company by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Mono<CompanyDTO> findOne(UUID id) {
        log.debug("Request to get Company : {}", id);
        return companyRepository.findById(id).map(companyMapper::toDto);
    }

    /**
     * Delete the company by id.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    public Mono<Void> delete(UUID id) {
        log.debug("Request to delete Company : {}", id);
        return companyRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public Flux<CompanyDTO> findAllByQuery(Pageable pageable, CompanyQuery query) {
        log.debug("Request to get all Companies by query");
        return companyRepository.findAllByQuery(pageable, query).map(companyMapper::toDto);
    }

    @Transactional(readOnly = true)
    public Mono<Long> countByQuery(CompanyQuery query) {
        log.debug("Request to count Companies by query");
        return companyRepository.countByQuery(query);
    }

}
