package com.masi.logistics.service;

import com.carevn.masi.utils.CSV.CSVUtils;
import com.carevn.masi.utils.SecurityUtils;
import com.masi.logistics.domain.Item;
import com.masi.logistics.domain.criteria.ItemCriteria;
import com.masi.logistics.domain.enumeration.ItemType;
import com.masi.logistics.repository.ItemRepository;
import com.masi.logistics.service.dto.ItemDTO;
import com.masi.logistics.service.exportDTO.*;
import com.masi.logistics.service.mapper.InventoriesMapper;
import com.masi.logistics.service.mapper.ItemMapper;
import java.util.UUID;

import com.masi.logistics.web.rest.errors.BadRequestAlertException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Implementation for managing {@link com.masi.logistics.domain.Item}.
 */
@Service
@Transactional
public class ItemService {

    private static final Logger log = LoggerFactory.getLogger(ItemService.class);

    private final ItemRepository itemRepository;

    private final ItemMapper itemMapper;

    public ItemService(ItemRepository itemRepository, ItemMapper itemMapper) {
        this.itemRepository = itemRepository;
        this.itemMapper = itemMapper;
    }

    /**
     * Save a item.
     *
     * @param itemDTO the entity to save.
     * @return the persisted entity.
     */
    public Mono<ItemDTO> save(ItemDTO itemDTO) {
        log.debug("Request to save Item : {}", itemDTO);
        return SecurityUtils.getUserJWTDetail().flatMap(user -> {
            return itemRepository.countByCodeAndCompanyAndIsDeletedIsFalse(itemDTO.getCode(), user.getCompanyId())
                .flatMap(count -> {
                    if (count > 0) {
                        return Mono.error(new BadRequestAlertException("Item code "+ itemDTO.getCode() +" already exists", "Item", "CODE_EXISTS"));
                    }
                    return itemRepository.save(itemMapper.toEntity(itemDTO)).map(itemMapper::toDto);
                });
        });
    }

    /**
     * Update a item.
     *
     * @param itemDTO the entity to save.
     * @return the persisted entity.
     */
    public Mono<ItemDTO> update(ItemDTO itemDTO) {
        log.debug("Request to update Item : {}", itemDTO);
        return itemRepository.save(itemMapper.toEntity(itemDTO).setIsPersisted()).map(itemMapper::toDto);
    }

    /**
     * Partially update a item.
     *
     * @param itemDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Mono<ItemDTO> partialUpdate(ItemDTO itemDTO) {
        log.debug("Request to partially update Item : {}", itemDTO);

        return SecurityUtils.getUserJWTDetail().flatMap(user -> {
            return itemRepository
                .findById(itemDTO.getId())
                .map(existingItem -> {
                    itemDTO.setCode(existingItem.getCode());
                    itemMapper.partialUpdate(existingItem, itemDTO);
                    existingItem.setIsPersisted();
                    return existingItem;
                })
                .flatMap(itemRepository::save)
                .map(itemMapper::toDto);
        });
    }

    /**
     * Find items by Criteria.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Flux<ItemDTO> findByCriteria(ItemCriteria criteria, Pageable pageable) {
        log.debug("Request to get all Items by Criteria");
        return itemRepository.findByCriteria(criteria, pageable)
                .collectList()
                .flatMapMany(es -> {
                    log.debug("Item : {}", es);
                    return Flux.fromIterable(es).map(itemMapper::toDto);
                });
    }

    /**
     * Find the count of items by criteria.
     * @param criteria filtering criteria
     * @return the count of items
     */
    public Mono<Long> countByCriteria(ItemCriteria criteria) {
        log.debug("Request to get the count of all Items by Criteria");
        return itemRepository.countByCriteria(criteria);
    }

    /**
     * Returns the number of items available.
     * @return the number of entities in the database.
     *
     */
    public Mono<Long> countAll() {
        return itemRepository.count();
    }

    /**
     * Get one item by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Mono<ItemDTO> findOne(UUID id, String company) {
        log.debug("Request to get Item : {}", id);
        return itemRepository.findById(id, company).map(itemMapper::toDto);
    }

    public Mono<ItemDTO> getItemWrap(String company, String itemType) {
        log.debug("Request to get Item : {}", itemType);
        return itemRepository.findByCreatedByAndCompany(itemType, company)
                .map(itemMapper::toDto);
    }

    /**
     * Delete the item by id.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    public Mono<Void> delete(UUID id, String updatedBy, String company) {
        log.debug("Request to delete Item : {}", id);
        return itemRepository.deleteById(id, updatedBy, company);
    }

    public Mono<ItemDTO> changeIsActive(UUID id, boolean b) {
        log.debug("Request to change isActive Factories : {}", id);
        return itemRepository.findById(id)
                .map(factories -> {
                    factories.setIsActive(b);
                    factories.setIsPersisted();
                    return factories;
                })
                .flatMap(itemRepository::save)
                .map(itemMapper::toDto);
    }


    public Mono<byte[]> exportRecordsAsCSV(ItemCriteria criteria, Pageable pageable) {
        log.debug("Request to export Inventories as CSV");

        return this.findByCriteria(criteria, null)
                .map(items_DTO_Export::new)
                .collectList()
                .flatMap(dtoList -> {
                    log.debug("Converting Inventories list to CSV");

                    return CSVUtils.convertListToExcel(dtoList);
                });
    }


    public Mono<ItemDTO> findByIdOrCreateWhenIsExist(Float proteinPercentageApply) {
        log.debug("Request to get Item by proteinPercentageApply : {}", proteinPercentageApply);

        Float proteinPercentageApplyFormat = Math.round(proteinPercentageApply * 100) / 100.0f;

        return itemRepository.findByProteinPercentageApply(proteinPercentageApplyFormat)
                .switchIfEmpty(Mono.defer(() -> {
                    Item item = new Item();
                    item.setId(UUID.randomUUID());
                    item.setCode("BOT_CA_" + proteinPercentageApplyFormat);
                    item.setName("Bột cá " + proteinPercentageApplyFormat + "% đạm");
                    item.setUomId(UUID.fromString("0c3cf5fe-d928-4f80-a49d-de3062352ea1"));
                    item.setCompany("KIM_LONG");
                    item.setCreatedBy("ADMIN");
                    item.setItemCategoryId(UUID.fromString("de80e071-8409-48d2-b864-f489048d074d"));
                    item.setVatRate(10f);
                    item.setUnitPrice(0f);
                    item.setIsSeparation(false);
                    item.setSupplierId(UUID.fromString("ac2a78a2-0648-4e72-af9d-5558e61c2acc"));
                    item.setItemType(ItemType.ITEM);
                    item.setIsActive(true);
                    item.setIsDeleted(false);
                    item.setItemTypeId(UUID.fromString("d1a485cb-bee5-4e7d-96c8-df23d8d0e19a"));
                    item.setIsSeparation(false);
                    item.setPercentProtein(proteinPercentageApplyFormat);

                    return itemRepository.save(item);
                }))
                .map(itemMapper::toDto);
    }

}
