package com.masi.logistics.repository;

import com.masi.logistics.domain.InventoriesCheckDetail;
import com.masi.logistics.domain.InventoriesCheckDetailMapItem;
import com.masi.logistics.domain.criteria.InventoriesCheckDetailCriteria;
import java.util.UUID;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Spring Data R2DBC repository for the InventoriesCheckDetail entity.
 */
@SuppressWarnings("unused")
@Repository
public interface InventoriesCheckDetailRepository
    extends ReactiveCrudRepository<InventoriesCheckDetail, UUID>, InventoriesCheckDetailRepositoryInternal {
    Flux<InventoriesCheckDetail> findAllBy(Pageable pageable);

    @Override
    <S extends InventoriesCheckDetail> Mono<S> save(S entity);

    @Override
    Flux<InventoriesCheckDetail> findAll();

    @Override
    Mono<InventoriesCheckDetail> findById(UUID id);

    @Override
    Mono<Void> deleteById(UUID id);

    @Query("UPDATE inventories_check_detail SET is_deleted = :isDeleted WHERE inventories_check_id = :id")
    Flux<InventoriesCheckDetail> deleteByInventoriesCheckId(UUID id, boolean isDeleted);

    @Query("""

            SELECT icd.*,
                   i.name AS item_name,
                   i.code AS item_code,
                   u.name AS item_unit_name,
                   ic.id as item_category_id,
                   ic.code as item_category_code,
                   ic."name" as item_category_name
            FROM inventories_check_detail icd
            LEFT JOIN item i ON icd.item_id = i.id
            LEFT JOIN uom u ON i.uom_id = u.id
            left join item_category ic on ic.id = i.item_category_id\s
            WHERE icd.inventories_check_id = :id AND icd.is_deleted = false
             ORDER BY i.code ASC;

""")
    Flux<InventoriesCheckDetailMapItem> findAllByInventoriesCheckId(UUID id);
}

interface InventoriesCheckDetailRepositoryInternal {
    <S extends InventoriesCheckDetail> Mono<S> save(S entity);

    Flux<InventoriesCheckDetail> findAllBy(Pageable pageable);

    Flux<InventoriesCheckDetail> findAll();

    Mono<InventoriesCheckDetail> findById(UUID id);
    // this is not supported at the moment because of https://github.com/jhipster/generator-jhipster/issues/18269
    // Flux<InventoriesCheckDetail> findAllBy(Pageable pageable, Criteria criteria);
    Flux<InventoriesCheckDetail> findByCriteria(InventoriesCheckDetailCriteria criteria, Pageable pageable);

    Mono<Long> countByCriteria(InventoriesCheckDetailCriteria criteria);
}
