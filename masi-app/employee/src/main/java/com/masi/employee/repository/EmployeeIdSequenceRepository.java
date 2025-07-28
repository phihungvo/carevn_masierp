package com.masi.employee.repository;

import com.masi.employee.domain.EmployeeIdSequence;
import com.masi.employee.domain.enumeration.Gender;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.annotation.Id;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Modifying;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

@SuppressWarnings("unused")
@Repository
public interface EmployeeIdSequenceRepository
        extends ReactiveCrudRepository<EmployeeIdSequence, Long>, EmployeeIdSequenceRepositoryInternal {
    @Override
    <S extends EmployeeIdSequence> Mono<S> save(S entity);

    Mono<EmployeeIdSequence> findFirstByGenderAndWorkspaceId(Gender gender, String workspaceId);

    @Query("SELECT * FROM employee_id_sequence WHERE workspace_id = :workspaceId limit 1")
    Flux<EmployeeIdSequence> findAllByWorkspaceId(String workspaceId);

    @Query("UPDATE employee_id_sequence SET current_sequence = current_sequence + 1 where gender = :gender and workspace_id = :workspaceId RETURNING current_sequence")
    Mono<Integer> getAndIncrease(Gender gender, String workspaceId);

    @Override
    Flux<EmployeeIdSequence> findAll();

    @Query("SELECT * FROM employee_id_sequence  where gender = :gender and workspace_id = :workspaceId limit 1")
    Mono<EmployeeIdSequence> getFirstByGenderAndWorkspace(Gender gender , String workspaceId);


    @Override
    Mono<EmployeeIdSequence> findById(Long id);

    @Override
    Mono<Void> deleteById(Long id);
}

interface EmployeeIdSequenceRepositoryInternal {
    <S extends EmployeeIdSequence> Mono<S> save(S entity);

    Flux<EmployeeIdSequence> findAllBy(Pageable pageable);

    Flux<EmployeeIdSequence> findAll();

    Mono<EmployeeIdSequence> findById(Long id);
    // this is not supported at the moment because of
    // https://github.com/jhipster/generator-jhipster/issues/18269
    // Flux<EmployeeIdSequence> findAllBy(Pageable pageable, Criteria criteria);
}
