package com.masi.employee.repository;

import com.masi.employee.domain.Uniform;
import com.masi.employee.domain.UniformStock;
import com.masi.employee.service.reports.ExportImportReport;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Spring Data R2DBC repository for the UniformStock entity.
 */
@SuppressWarnings("unused")
@Repository
public interface UniformStockRepository
        extends ReactiveCrudRepository<UniformStock, UUID>, UniformStockRepositoryInternal {
    Flux<UniformStock> findAllBy(Pageable pageable);

    @Query("SELECT * FROM uniform_stock entity WHERE entity.uniform_id = :id")
    Flux<UniformStock> findByUniform(UUID id);

    Mono<UniformStock> findOneByUniformIdAndCompany(UUID uniformId, String company);

    @Query("SELECT * FROM uniform_stock entity WHERE entity.uniform_id IS NULL")
    Flux<UniformStock> findAllWhereUniformIsNull();

    @Query("SELECT * FROM uniform_stock entity WHERE entity.uniform_id IN (:listUniformId) AND entity.warehouse_id = :warehouseId AND delete_at IS NULL AND delete_by IS NULL AND company = :company")
    Flux<UniformStock> getListUniformStockNotDeleteByListUniformId(List<UUID> listUniformId, String company, UUID warehouseId);

    @Override
    <S extends UniformStock> Mono<S> save(S entity);

    @Override
    Flux<UniformStock> findAll();

    @Override
    Mono<UniformStock> findById(UUID id);

    @Query("SELECT * FROM uniform_stock entity WHERE entity.company = :company AND delete_at IS NULL AND delete_by IS NULL AND id = :id")
    Mono<UniformStock> findByIdAndCompany(UUID id, String company);

    @Query("""
            SELECT u.id ,u.name ,SUM(us.stock) as total,'STOCK' as "type"
              FROM uniform u
              LEFT JOIN uniform_stock us ON u.id = us.uniform_id
              WHERE u.company = :company
              GROUP BY u.id, u.name, "type"
              ORDER  BY u."name" ASC
              LIMIT  :limit
              OFFSET :offset
                  """)
    Flux<ExportImportReport> findAllStock(int limit, long offset, String company);

    @Query("""
            SELECT count(uu.id) as total
            FROM(
              SELECT u.id ,u.name, 'STOCK' as "type"
              FROM uniform u
              LEFT JOIN uniform_stock us ON u.id = us.uniform_id
              WHERE u.company = :company
              GROUP BY u.id, u.name, "type"
            ) as uu
                  """)
    Mono<Long> countAllStock(String company);

    @Override
    Mono<Void> deleteById(UUID id);

}

interface UniformStockRepositoryInternal {
    <S extends UniformStock> Mono<S> save(S entity);

    Flux<UniformStock> findAllBy(Pageable pageable);

    Flux<UniformStock> findAll();

    Flux<UniformStock> findAllByCompany(String company);

    Mono<UniformStock> findById(UUID id);

    Flux<UniformStock> findAllByQuery(Pageable pageable, String company, String status);

    Mono<Long> countAllByQuery(String company, String status);

    // this is not supported at the moment because of
    // https://github.com/jhipster/generator-jhipster/issues/18269
    // Flux<UniformStock> findAllBy(Pageable pageable, Criteria criteria);
}
