package com.masi.sale.repository;

import com.masi.sale.domain.Customer;
import com.masi.sale.domain.CustomerCodeFormats;
import com.masi.sale.service.dto.CustomerRO;

import java.time.LocalDate;
import java.util.UUID;

import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Spring Data R2DBC repository for the Customer entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CustomerRepository extends ReactiveCrudRepository<Customer, UUID>, CustomerRepositoryInternal {
    Flux<Customer> findAllBy(Pageable pageable);

    @Override
    <S extends Customer> Mono<S> save(S entity);

    @Override
    Flux<Customer> findAll();

    @Override
    Mono<Customer> findById(UUID id);

    @Query("SELECT * FROM customer WHERE id = :id AND is_deleted = false AND company = :company")
    Mono<Customer> findByIdAndIsDeleted(UUID id, String company);

    @Override
    Mono<Void> deleteById(UUID id);

    @Query("SELECT * FROM customer WHERE customer_code = :customerCode AND company = :company and is_deleted = false")
    Mono<Customer> findByIsDeletedAndCustomerCodeAndCompany(String customerCode, String company);

    @Query("SELECT COUNT(c) FROM customer c WHERE c.customer_code = :customerCode AND c.is_deleted = false")
    Mono<Long> countByIsDeletedAndCustomerCode(String customerCode);

    Mono<Customer> findFirstByTaxCodeAndIsDeletedIsFalseAndCompany(String taxCode, String company);

    @Query("SELECT * FROM customer_code_formats WHERE company = :company")
    Flux<CustomerCodeFormats> findTemPlateByCompany(String company);

    @Query(
        """
            SELECT * FROM customer WHERE
            birthday >= :fromDate AND birthday <= :toDate
            AND customer_owner = :employee_id
            """
    )
    Flux<Customer> findAllByBirthday(LocalDate fromDate, LocalDate toDate, UUID employee_id);

    @Query(
        """
            SELECT COUNT(*) FROM customer  WHERE birthday >= :fromDate AND birthday <= :toDate
            AND customer_owner = :employee_id
            """
    )
    Mono<Long> countAllByFilter(LocalDate fromDate, LocalDate toDate, UUID employee_id);
}

interface CustomerRepositoryInternal {
    <S extends Customer> Mono<S> save(S entity);

    Flux<Customer> findAllBy(Pageable pageable);

    Flux<Customer> findAll();

    Mono<Customer> findById(UUID id);

    // this is not supported at the moment because of https://github.com/jhipster/generator-jhipster/issues/18269
    // Flux<Customer> findAllBy(Pageable pageable, Criteria criteria);
    Flux<Customer> findAllByFilter(CustomerRO ro, Pageable pageable);

    Mono<Long> countAllByFilter(CustomerRO ro);
}
