package com.masi.logistics.service;

import com.masi.logistics.repository.UomGroupDetailsRepository;
import com.masi.logistics.repository.UomGroupRepository;
import com.masi.logistics.service.dto.UomDTO;
import com.masi.logistics.service.dto.UomGroupDTO;
import com.masi.logistics.service.dto.UomGroupDetailsDTO;
import com.masi.logistics.service.mapper.UomGroupDetailsMapper;
import com.masi.logistics.service.mapper.UomGroupMapper;

import lombok.AllArgsConstructor;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Implementation for managing
 * {@link com.masi.logistics.domain.UomGroup}.
 */
@Service
@Transactional
public class UomGroupService {

    private static final Logger log = LoggerFactory.getLogger(UomGroupService.class);

    private final UomGroupRepository uomGroupRepository;
    private final UomGroupDetailsRepository uomGroupDetailsRepository;

    private final UomGroupMapper uomGroupMapper;
    private final UomGroupDetailsMapper uomGroupDetailsMapper;

    public UomGroupService(UomGroupRepository uomGroupRepository, UomGroupDetailsRepository uomGroupDetailsRepository, UomGroupMapper uomGroupMapper, UomGroupDetailsMapper uomGroupDetailsMapper) {
        this.uomGroupRepository = uomGroupRepository;
        this.uomGroupDetailsRepository = uomGroupDetailsRepository;
        this.uomGroupMapper = uomGroupMapper;
        this.uomGroupDetailsMapper = uomGroupDetailsMapper;
    }

    /**
     * Save a uomGroup.
     *
     * @param uomGroupDTO the entity to save.
     * @return the persisted entity.
     */
    public Mono<UomGroupDTO> save(UomGroupDTO uomGroupDTO) {
        log.debug("Request to save UomGroup : {}", uomGroupDTO);
        return uomGroupRepository.save(uomGroupMapper.toEntity(uomGroupDTO))
            .flatMap(uomGroup -> {
                List<UomGroupDetailsDTO> detailsDTOs = uomGroupDTO.getUomGroupDetailsDTOs();
                detailsDTOs.forEach(detailsDTO -> detailsDTO.setUomGroupId(uomGroup.getId()));
                return uomGroupDetailsRepository.saveAll(detailsDTOs.stream()
                        .map(uomGroupDetailsMapper::toEntity)
                        .peek(details -> details.setUomGroup(uomGroup))
                        .collect(Collectors.toList()))
                    .collectList()
                    .map(savedDetails -> {
                        uomGroup.setUomGroupDetails(new HashSet<>(savedDetails));
                        return uomGroup;
                    });
            })
            .map(uomGroupMapper::toDto);
    }

    /**
     * Update a uomGroup.
     *
     * @param uomGroupDTO the entity to save.
     * @return the persisted entity.
     */
    public Mono<UomGroupDTO> update(UomGroupDTO uomGroupDTO) {
        log.debug("Request to update UomGroup : {}", uomGroupDTO);
        return uomGroupRepository.save(uomGroupMapper.toEntity(uomGroupDTO).setIsPersisted())
                .map(uomGroupMapper::toDto);
    }

    /**
     * Partially update a uomGroup.
     *
     * @param uomGroupDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Mono<UomGroupDTO> partialUpdate(UomGroupDTO uomGroupDTO) {
        log.debug("Request to partially update UomGroup : {}", uomGroupDTO);

        return uomGroupRepository
            .findById(uomGroupDTO.getId())
            .flatMap(existingUomGroup -> uomGroupDetailsRepository
                .deleteAllByUomGroupId(existingUomGroup.getId(), existingUomGroup.getCompany(), uomGroupDTO.getUpdateBy())
                .then(Mono.defer(() -> {

                    List<UomGroupDetailsDTO> detailsDTOs = uomGroupDTO.getUomGroupDetailsDTOs();
                    detailsDTOs.forEach(detailsDTO -> detailsDTO.setUomGroupId(existingUomGroup.getId()));
                    return uomGroupDetailsRepository.saveAll(detailsDTOs.stream()
                            .map(uomGroupDetailsMapper::toEntity)
                            .peek(details -> details.setUomGroup(existingUomGroup))
                            .collect(Collectors.toList()))
                        .collectList()
                        .flatMap(savedDetails -> {
                            existingUomGroup.setUomGroupDetails(new HashSet<>(savedDetails));
                            uomGroupMapper.partialUpdate(existingUomGroup, uomGroupDTO);
                            return uomGroupRepository.save(existingUomGroup.setIsPersisted()).map(uomGroupMapper::toDto);
                        });
                })));
    }



    /**
     * Get all the uomGroups.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Flux<UomGroupDTO> findAll(Pageable pageable) {
        log.debug("Request to get all UomGroups");
        return uomGroupRepository.findAllBy(pageable).map(uomGroupMapper::toDto);
    }

    /**
     * Returns the number of uomGroups available.
     *
     * @return the number of entities in the database.
     *
     */
    public Mono<Long> countAll() {
        return uomGroupRepository.count();
    }

    /**
     * Get one uomGroup by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Mono<UomGroupDTO> findOne(UUID id, String company) {
        log.debug("Request to get UomGroup : {}", id);
        return uomGroupRepository.findById(id).map(uomGroupMapper::toDto)
            .flatMap(uom -> uomGroupDetailsRepository.findAllByUomGroupId(id, company).collectList()
                .map(uomGroupDetailsMapper::toDto).flatMap(detail -> {
                    var listUom = detail.stream().map(UomGroupDetailsDTO::getBaseUom).collect(Collectors.toSet());
                    uom.setListUomOfGroup(new HashSet<>(listUom));
                    uom.setUomGroupDetailsDTOs(new ArrayList<>(detail));
                    return Mono.just(uom);
                }));
    }

    /**
     * Delete the uomGroup by id.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    /**
     * Delete the uomGroup by id.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    public Mono<Void> delete(UUID id, String company, String deletedBy) {
        log.debug("Request to delete UomGroup : {}", id);
        // remove details:
        return uomGroupDetailsRepository.deleteAllByUomGroupId(id, company, deletedBy)
            .then(uomGroupRepository.deleteById(id, company, deletedBy));
    }


}
