package com.masi.sale.repository;

import com.masi.sale.domain.Order;
import com.masi.sale.service.dto.OrderQueryDTO;

import java.time.LocalDate;
import java.util.UUID;

import com.masi.sale.service.mapper.ContractFileMapper;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Spring Data R2DBC repository for the Order entity.
 */
@SuppressWarnings("unused")
@Repository
public interface OrderRepository extends ReactiveCrudRepository<Order, UUID>, OrderRepositoryInternal {
    Flux<Order> findAllBy(Pageable pageable);

    @Override
    <S extends Order> Mono<S> save(S entity);

    @Override
    Flux<Order> findAll();

    @Override
    Mono<Order> findById(UUID id);


    @Override
    Mono<Void> deleteById(UUID id);


    @Query("""
                SELECT c.* 
                FROM masi_order c 
                WHERE
        c.is_deleted = false 
                  AND (
                      ((:deliveryDate IS NULL OR (c.delivery_term_to BETWEEN :deliveryDate AND :expectedReceiveDate)) OR 
                        (:expectedReceiveDate IS NULL OR (c.delivery_term_from BETWEEN :deliveryDate AND :expectedReceiveDate)))
                  )
            """)
    Flux<Order> findAllByDate(LocalDate deliveryDate, LocalDate expectedReceiveDate);
}

interface OrderRepositoryInternal {
    <S extends Order> Mono<S> save(S entity);

    Flux<Order> findAllBy(Pageable pageable);

    Flux<Order> findAll();

    Mono<Order> findById(UUID id);

    Flux<Order> findAllByQuery(OrderQueryDTO dto, Pageable pageable);
    Mono<Long> countAllByQuery(OrderQueryDTO dto);
    Mono<Order> findByIdAndIsDeletedIsFalse(UUID id);

    // this is not supported at the moment because of https://github.com/jhipster/generator-jhipster/issues/18269
    // Flux<Order> findAllBy(Pageable pageable, Criteria criteria);
}
