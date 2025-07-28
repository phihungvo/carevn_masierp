package com.masi.sale.repository;

import com.masi.sale.domain.TemplateContract;

import java.util.List;
import java.util.UUID;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Spring Data R2DBC repository for the TemplateContract entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TemplateContractRepository extends ReactiveCrudRepository<TemplateContract, UUID>, TemplateContractRepositoryInternal {
    Flux<TemplateContract> findAllBy(Pageable pageable);

    @Override
    <S extends TemplateContract> Mono<S> save(S entity);

    @Override
    Flux<TemplateContract> findAll();

    @Override
    Mono<TemplateContract> findById(UUID id);

    @Override
    Mono<Void> deleteById(UUID id);

    @Query("SELECT * FROM template_contract WHERE company = :company")
    Flux<TemplateContract> findAllByCompany(String company);

}

interface TemplateContractRepositoryInternal {
    <S extends TemplateContract> Mono<S> save(S entity);

    Flux<TemplateContract> findAllBy(Pageable pageable);

    Flux<TemplateContract> findAll();

    Mono<TemplateContract> findById(UUID id);
    // this is not supported at the moment because of https://github.com/jhipster/generator-jhipster/issues/18269
    // Flux<TemplateContract> findAllBy(Pageable pageable, Criteria criteria);
}
