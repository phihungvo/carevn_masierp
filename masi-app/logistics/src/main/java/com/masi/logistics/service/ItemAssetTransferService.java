package com.masi.logistics.service;

import com.carevn.masi.dto.UserJWTDetail;
import com.carevn.masi.utils.CSV.CSVUtils;
import com.carevn.masi.utils.SecurityUtils;
import com.masi.logistics.domain.AssetTransferDetails;
import com.masi.logistics.domain.ItemAssetTransfer;
import com.masi.logistics.domain.criteria.AssetTransferDetailsCriteria;
import com.masi.logistics.domain.criteria.ItemAssetTransferCriteria;
import com.masi.logistics.domain.enumeration.StatusEntity;
import com.masi.logistics.repository.*;
import com.masi.logistics.service.dto.ItemAssetTransferDTO;
import com.masi.logistics.service.exportDTO.ItemAssetTransferExportDTO;
import com.masi.logistics.service.mapper.AssetTransferDetailsMapper;
import com.masi.logistics.service.mapper.ItemAssetTransferMapper;

import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import tech.jhipster.service.filter.BooleanFilter;
import tech.jhipster.service.filter.StringFilter;
import tech.jhipster.service.filter.UUIDFilter;

/**
 * Service Implementation for managing {@link com.masi.logistics.domain.ItemAssetTransfer}.
 */
@AllArgsConstructor
@Service
@Transactional
public class ItemAssetTransferService {

    private static final Logger log = LoggerFactory.getLogger(ItemAssetTransferService.class);

    private final ItemAssetTransferRepository itemAssetTransferRepository;

    private final ItemAssetTransferMapper itemAssetTransferMapper;
    private final AssetTransferDetailsRepository assetTransferDetailsRepository;
    private final AssetTransferDetailsMapper assetTransferDetailsMapper;

    private final DocumentCodeSequenceService documentCodeSequenceService;
    private final InventoriesRepository inventoriesRepository;
    private final InventoriesStorageRepository inventoriesStorageRepository;
    private final ItemInfoRepository itemInfoRepository;


    /**
     * Save a itemAssetTransfer.
     *
     * @param itemAssetTransferDTO the entity to save.
     * @return the persisted entity.
     */
    public Mono<ItemAssetTransferDTO> save(ItemAssetTransferDTO itemAssetTransferDTO) {
        log.debug("Request to save ItemAssetTransfer: {}", itemAssetTransferDTO);
        itemAssetTransferDTO.setStatus(StatusEntity.NEW);
        return documentCodeSequenceService.makeSureDocumentCodeSequenceExist(ItemAssetTransfer.ENTITY_NAME, "%04d")
                .then(documentCodeSequenceService.getByDocumentType(ItemAssetTransfer.ENTITY_NAME))
                .flatMap(sequence -> {
                    LocalDate currentDate = LocalDate.now();
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMyy");
                    String formattedDate = currentDate.format(formatter);
                    String nextCode = sequence.getNextAndIncrement();
                    String code = "MTS" + formattedDate + "/" + nextCode;

                    itemAssetTransferDTO.setCode(code);
                    var entity = itemAssetTransferMapper.toEntity(itemAssetTransferDTO);
                    return documentCodeSequenceService.updateSequence(sequence)
                            .then(itemAssetTransferRepository.save(entity)
                                    .map(itemAssetTransferMapper::toDto)
                                    .flatMap(savedDTO -> {
                                                if (itemAssetTransferDTO.getAssetTransferDetailsDTOS() == null) {
                                                    itemAssetTransferDTO.setAssetTransferDetailsDTOS(new ArrayList<>());
                                                }
                                                itemAssetTransferDTO.getAssetTransferDetailsDTOS()
                                                        .forEach(detail -> detail.setItemAssetTransferId(savedDTO.getId()));

                                                var detailEntities = assetTransferDetailsMapper.toEntity(
                                                        new ArrayList<>(itemAssetTransferDTO.getAssetTransferDetailsDTOS())
                                                );

                                                return assetTransferDetailsRepository.saveAll(detailEntities)
                                                        .collectList()
                                                        .map(assetTransferDetailsMapper::toDto)
                                                        .flatMap(detailDTOs -> {
                                                            savedDTO.setAssetTransferDetailsDTOS(new ArrayList<>(detailDTOs));
//                                        return inventoriesRepository.findById(savedDTO.getInventoriesStorageId())
//                                            .doOnNext(inventories -> {
//                                                if (inventories.getDepartment() != null) {
//                                                    try {
//                                                        savedDTO.setDepartment(String.valueOf(itemAssetTransferDTO.getToDepartmentId()));
//                                                    } catch (Exception e) {
//                                                        log.debug("Error mapping inventories to DTO", e);
//                                                    }
//                                                }
//                                            })
//                                            .thenReturn(savedDTO);

                                                            return Mono.just(savedDTO);
                                                        })

                                                        ;
                                            }
                                    ));

                });

    }

    /**
     * Update a itemAssetTransfer.
     *
     * @param itemAssetTransferDTO the entity to save.
     * @return the persisted entity.
     */
    public Mono<ItemAssetTransferDTO> update(ItemAssetTransferDTO itemAssetTransferDTO) {
        log.debug("Request to update ItemAssetTransfer : {}", itemAssetTransferDTO);
        return itemAssetTransferRepository
                .save(itemAssetTransferMapper.toEntity(itemAssetTransferDTO).setIsPersisted())
                .map(itemAssetTransferMapper::toDto);
    }

    /**
     * Partially update a itemAssetTransfer.
     *
     * @param itemAssetTransferDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Mono<ItemAssetTransferDTO> partialUpdate(ItemAssetTransferDTO itemAssetTransferDTO) {
        log.debug("Request to partially update ItemAssetTransfer : {}", itemAssetTransferDTO);

        return SecurityUtils.getUserJWTDetail().flatMap(login -> {
            return itemAssetTransferRepository
                    .findByIdAndCompany(itemAssetTransferDTO.getId(), login.getCompanyId())
                    .map(existingItemAssetTransfer -> {
                        itemAssetTransferMapper.partialUpdate(existingItemAssetTransfer, itemAssetTransferDTO);
                        existingItemAssetTransfer.setIsPersisted();
                        return existingItemAssetTransfer;
                    })
                    .flatMap(itemAssetTransferRepository::save)
                    .map(itemAssetTransferMapper::toDto)
                    .flatMap(it -> {
                        if (itemAssetTransferDTO.getAssetTransferDetailsDTOS() == null) {
                            itemAssetTransferDTO.setAssetTransferDetailsDTOS(new ArrayList<>());
                        }
                        itemAssetTransferDTO.getAssetTransferDetailsDTOS().forEach(itemAssetTransferDetailDTO -> {
                            itemAssetTransferDetailDTO.setItemAssetTransferId(it.getId());
                        });
                        var detailEntity = assetTransferDetailsMapper.toEntity(new ArrayList<>(itemAssetTransferDTO.getAssetTransferDetailsDTOS()));
                        return assetTransferDetailsRepository.deleteByItemAssetTransferId(it.getId(), it.getCompany())
                                .flatMap(delete -> {
                                    return assetTransferDetailsRepository.saveAll(detailEntity)
                                            .collectList()
                                            .map(assetTransferDetailsMapper::toDto)
                                            .flatMap(listDetails -> {
                                                it.setAssetTransferDetailsDTOS(new ArrayList<>(listDetails));
                                                return Mono.just(it);
                                            });
                                });
                    });
        });
    }

    /**
     * Find itemAssetTransfers by Criteria.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Flux<ItemAssetTransferDTO> findByCriteria(ItemAssetTransferCriteria criteria, Pageable pageable) {
        log.debug("Request to get all ItemAssetTransfers by Criteria");
        return SecurityUtils.getUserJWTDetail().flatMapMany(user -> {
            setDefaultItemAssetTransferCriteria(criteria, user);
            return itemAssetTransferRepository.findByCriteria(criteria, pageable)
                    .map(itemAssetTransferMapper::toDto)
                    .collectList()
                    .flatMapMany(itemAssetTransfers -> {
                        var listIds = itemAssetTransfers.stream().map(ItemAssetTransferDTO::getId).toList();
                        var assetTransferDetailsCriteria = getAssetTransferDetailsCriteriaByItemAssetTransferIdIn(listIds, user.getCompanyId());
                        return assetTransferDetailsRepository.findByCriteria(assetTransferDetailsCriteria, null)
                                .collectList()
                                .flatMapMany(assetTransferDetails -> {
                                    itemAssetTransfers.forEach(itemAssetTransferDTO -> {
                                        var details = assetTransferDetails.stream().filter(assetTransferDetail -> listIds.contains(assetTransferDetail.getItemAssetTransferId())).toList();
                                        itemAssetTransferDTO.setAssetTransferDetailsDTOS(new ArrayList<>(assetTransferDetailsMapper.toDto(details)));
                                    });
                                    return Flux.fromIterable(itemAssetTransfers);
                                });
                    })

                    ;
        });
    }

    private static void setDefaultItemAssetTransferCriteria(ItemAssetTransferCriteria criteria, UserJWTDetail user) {
        StringFilter company = new StringFilter();
        company.setEquals(user.getCompanyId());
        criteria.setCompany(company);
        BooleanFilter isDeleted = new BooleanFilter();
        isDeleted.setEquals(false);
        criteria.setIsDeleted(isDeleted);
    }

    private AssetTransferDetailsCriteria getAssetTransferDetailsCriteriaByItemAssetTransferIdIn(List<UUID> listIds, String companyId) {
        var criteria = new AssetTransferDetailsCriteria();
        UUIDFilter itemAssetTransferId = new UUIDFilter();
        itemAssetTransferId.setIn(listIds);
        criteria.setItemAssetTransferId(itemAssetTransferId);
        StringFilter company = new StringFilter();
        company.setEquals(companyId);
        criteria.setCompany(company);
        BooleanFilter isDeleted = new BooleanFilter();
        isDeleted.setEquals(false);
        criteria.setIsDeleted(isDeleted);
        return criteria;
    }

    /**
     * Find the count of itemAssetTransfers by criteria.
     *
     * @param criteria filtering criteria
     * @return the count of itemAssetTransfers
     */
    public Mono<Long> countByCriteria(ItemAssetTransferCriteria criteria) {
        log.debug("Request to get the count of all ItemAssetTransfers by Criteria");
        return SecurityUtils.getUserJWTDetail().flatMap(user -> {
            setDefaultItemAssetTransferCriteria(criteria, user);
            return itemAssetTransferRepository.countByCriteria(criteria);
        });
    }

    /**
     * Returns the number of itemAssetTransfers available.
     *
     * @return the number of entities in the database.
     */
    public Mono<Long> countAll() {
        return itemAssetTransferRepository.count();
    }

    /**
     * Get one itemAssetTransfer by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Mono<ItemAssetTransferDTO> findOne(UUID id) {
        log.debug("Request to get ItemAssetTransfer : {}", id);
        return SecurityUtils.getUserJWTDetail().flatMap(user -> {
            return itemAssetTransferRepository.findByIdAndCompany(id, user.getCompanyId())
                    .map(itemAssetTransferMapper::toDto)
                    .flatMap(it -> {
                        var assetTransferDetailsCriteria = getAssetTransferDetailsCriteriaByItemAssetTransferIdIn(List.of(it.getId()), user.getCompanyId());
                        return assetTransferDetailsRepository.findByCriteria(assetTransferDetailsCriteria, null)
                                .collectList()
                                .map(assetTransferDetails -> {
                                    it.setAssetTransferDetailsDTOS(new ArrayList<>(assetTransferDetailsMapper.toDto(assetTransferDetails)));
                                    return it;
                                });
                    });
        });
    }

    @Transactional(readOnly = true)
    public Flux<ItemAssetTransferDTO> findOneByItemInfo(UUID inventoriesStorageId) {
        log.debug("Request to get ItemAssetTransfer: {}", inventoriesStorageId);
        return SecurityUtils.getUserJWTDetail()
                .flatMapMany(user -> itemAssetTransferRepository.findAllByInventoriesStorageId(inventoriesStorageId)
                        .map(itemAssetTransferMapper::toDto)
                        .flatMap(itemAssetTransferDTO -> {
                            var assetTransferDetailsCriteria = getAssetTransferDetailsCriteriaByItemAssetTransferIdIn(
                                    List.of(itemAssetTransferDTO.getId()),
                                    user.getCompanyId()
                            );
                            return assetTransferDetailsRepository.findByCriteria(assetTransferDetailsCriteria, null)
                                    .collectList()
                                    .map(details -> {
                                        itemAssetTransferDTO.setAssetTransferDetailsDTOS(
                                                new ArrayList<>(assetTransferDetailsMapper.toDto(details))
                                        );
                                        return itemAssetTransferDTO;
                                    });
                        })
                );
    }


    /**
     * Delete the itemAssetTransfer by id.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    public Mono<Void> delete(UUID id) {
        log.debug("Request to delete ItemAssetTransfer : {}", id);
        return SecurityUtils.getUserJWTDetail().flatMap(user -> {
            return itemAssetTransferRepository.findByIdAndCompany(id, user.getCompanyId())
                    .flatMap(itemAssetTransfer -> {
                        itemAssetTransfer.setIsDeleted(true);
                        itemAssetTransfer.deletedAt(ZonedDateTime.now());
                        itemAssetTransfer.deletedBy(user.getUserId().toString());
                        itemAssetTransfer.setIsPersisted();
                        return itemAssetTransferRepository.save(itemAssetTransfer);
                    }).then(assetTransferDetailsRepository.deleteByItemAssetTransferId(id, user.getCompanyId()).then());
        });
    }

    // export

    public Mono<byte[]> exportRecordsAsCSV(ItemAssetTransferCriteria criteria, Pageable pageable) {
        log.debug("Request to export ItemAssetDepreciation as CSV");
        return this.findByCriteria(criteria, pageable)
                .map(ItemAssetTransferExportDTO::new)
                .collectList()
                .flatMap(dtoList -> {
                    log.debug("Converting Inventories list to CSV");
                    return CSVUtils.convertListToExcel(dtoList);
                });
    }

    public Mono<Void> setStatus(UUID id, StatusEntity status) {
        return itemAssetTransferRepository.findByIdBrier(id)
                .flatMap(e -> {
                    e.setStatus(status);
                    e.setIsPersisted();
                    return itemAssetTransferRepository.save(e).then();
                });
    }

    public Mono<Void> handleConfirm(UUID id) {
        return itemAssetTransferRepository.findByIdBrier(id)
                .flatMap(e -> {
                    if (e == null) {
                        return Mono.empty();
                    }
                    return assetTransferDetailsRepository.findByItemAssetTransferId(id)
                            .flatMap(assetTransferDetails ->
                                    inventoriesStorageRepository.changeDepartmentByIds(
                                                    assetTransferDetails.getInventoriesStorageId(),
                                                    String.valueOf(e.getToDepartmentId())
                                            )
                                            .flatMap(a -> {
                                                log.debug("Change user by id");
                                                return itemInfoRepository.changeUserByIds(assetTransferDetails.getInventoriesStorageId(), assetTransferDetails.getEmployeeToId());
                                            })
                            )

                            .then();
                });
    }

}
