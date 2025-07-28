package com.masi.employee.repository;

import com.masi.employee.domain.EmployeeChangeLog;
import java.util.UUID;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Spring Data R2DBC repository for the EmployeeChangeLog entity.
 */
@SuppressWarnings("unused")
@Repository
public interface EmployeeChangeLogRepository extends ReactiveCrudRepository<EmployeeChangeLog, UUID>, EmployeeChangeLogRepositoryInternal {
    Flux<EmployeeChangeLog> findAllBy(Pageable pageable);

    @Override
    <S extends EmployeeChangeLog> Mono<S> save(S entity);

    @Override
    Flux<EmployeeChangeLog> findAll();

    @Override
    Mono<EmployeeChangeLog> findById(UUID id);

    @Override
    Mono<Void> deleteById(UUID id);


    Mono<Long> countAllByEmployeeId(UUID employeeId);

}

interface EmployeeChangeLogRepositoryInternal {
    <S extends EmployeeChangeLog> Mono<S> save(S entity);

    Flux<EmployeeChangeLog> findAllBy(Pageable pageable);

    Flux<EmployeeChangeLog> findAll();

    Mono<EmployeeChangeLog> findById(UUID id);
    // this is not supported at the moment because of https://github.com/jhipster/generator-jhipster/issues/18269
    // Flux<EmployeeChangeLog> findAllBy(Pageable pageable, Criteria criteria);
    Flux<EmployeeChangeLog> findAllByEmployeeIdOrderByChangeDateDesc(UUID employeeId, Pageable pageable);
    Mono<Long> countAllByEmployeeIdOrderByChangeDateDesc(UUID employeeId);

}
