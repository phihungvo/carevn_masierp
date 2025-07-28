package com.masi.sale.repository;

import ch.qos.logback.core.testUtil.MockInitialContext;
import com.masi.sale.domain.ContractMaterial;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

import com.masi.sale.domain.ContractMaterialFull;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Modifying;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Spring Data R2DBC repository for the ContractProduct entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ContractMaterialRepository extends ReactiveCrudRepository<ContractMaterial, UUID>, ContractMaterialRepositoryInternal {
    Flux<ContractMaterial> findAllBy(Pageable pageable);

    @Override
    <S extends ContractMaterial> Mono<S> save(S entity);

    @Override
    Flux<ContractMaterial> findAll();

    @Override
    Mono<ContractMaterial> findById(UUID id);

    @Override
    Mono<Void> deleteById(UUID id);

    @Query("""
            SELECT c.*, m.name AS material_name,m.item_id AS item_id
                                FROM material m
                                 JOIN contract_material c ON c.id_material = m.id
                                 WHERE c.is_deleted = false AND c.id_contract = :idContract
                     """)
    Flux<ContractMaterialFull> findAllByIdContract(UUID idContract);
    @Query("""
            SELECT c.*, m.name AS material_name,m.item_id AS item_id
                                FROM material m
                                 JOIN contract_material c ON c.id_material = m.id
                                 WHERE c.is_deleted = false AND c.order_id = :orderId
            """)
    Flux<ContractMaterialFull> findAllByOrderId(UUID orderId);

    @Query("UPDATE contract_material SET is_deleted = true WHERE id_contract = :idContract")
    Mono<Void> deleteByIdContract(UUID idContract);

    @Modifying
    @Query("UPDATE contract_material SET order_id = :orderId WHERE id IN (:contractMaterialUse)")
    Mono<Integer> updateOrderById(Collection<UUID> contractMaterialUse, UUID orderId);

    @Modifying
    @Query("UPDATE contract_material SET  order_Id = :orderId WHERE id IN (:contractMaterialUse)")
    Mono<Integer> updateManufactureById(Collection<UUID> contractMaterialUse, UUID orderId, Boolean isOrder);

    @Modifying
    @Query("UPDATE contract_material SET manufacture_order_id = :manufacture_order_id  WHERE id IN (:contractMaterialUse)")
    Mono<Integer> updateManufactureById(Collection<UUID> contractMaterialUse, UUID manufactureOrderId);

    @Modifying
    @Query("UPDATE contract_material SET manufacture_order_id = :manufacture_order_id AND order_Id = :orderId WHERE id IN (:contractMaterialUse)")
    Mono<Integer> updateManufactureById(Collection<UUID> contractMaterialUse, UUID manufactureOrderId, UUID orderId);

    @Modifying
    @Query("UPDATE contract_material SET manufacture_order_id = NULL WHERE manufacture_order_id = :idManufacture")
    Mono<Integer> deleteAllByManufactureById(UUID idManufacture);

    @Modifying
    @Query("UPDATE contract_material SET order_id = NULL WHERE order_id = :orderId")
    Mono<Void> removeOrderIdByOrderId(UUID orderId);

    @Query("""
             SELECT c.*,
                           m.name AS material_name,
                           m.item_id AS item_id,
                           o.order_code AS order_code,
                           o.date_order AS date_order,
                           o.status AS status_order,
                           o.quantity AS quantity_order,
                           con.status AS status_contract,
                           con.contract_name AS contract_name,
                           cust.customer_code AS customer_code,
                           cust.company_name AS company_name
                    FROM material m
                    JOIN contract_material c ON c.id_material = m.id
                    LEFT JOIN masi_order o ON c.order_id = o.id
                    LEFT JOIN contract con ON c.id_contract = con.id
                    LEFT JOIN customer cust ON con.customer_id = cust.id
            				WHERE c.is_deleted = false AND c.order_id In (:contractIds)
           
            """)
    Flux<ContractMaterialFull> findAllByIdContractIn(List<UUID> contractIds);
}

interface ContractMaterialRepositoryInternal {
    <S extends ContractMaterial> Mono<S> save(S entity);

    Flux<ContractMaterial> findAllBy(Pageable pageable);

    Flux<ContractMaterial> findAll();

    Mono<ContractMaterial> findById(UUID id);

    Flux<ContractMaterial> findAllByIds(Collection<UUID> ids);
    // this is not supported at the moment because of https://github.com/jhipster/generator-jhipster/issues/18269
    // Flux<ContractProduct> findAllBy(Pageable pageable, Criteria criteria);
}
