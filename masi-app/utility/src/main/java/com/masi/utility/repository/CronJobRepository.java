package com.masi.utility.repository;

import com.masi.utility.domain.CronJob;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.ZonedDateTime;

/**
 * Spring Data R2DBC repository for the CronJob entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CronJobRepository extends ReactiveCrudRepository<CronJob, Long>, CronJobRepositoryInternal {
    Flux<CronJob> findAllBy(Pageable pageable);

    @Override
    <S extends CronJob> Mono<S> save(S entity);

    @Override
    Flux<CronJob> findAll();

    @Override
    Mono<CronJob> findById(Long id);

    @Override
    Mono<Void> deleteById(Long id);

    @Query( """
                    SELECT * FROM cron_job
                        WHERE next_run < :before
                            AND  enabled = true
                            AND (last_run IS NULL OR last_run < :before - interval '1 minute')
            """ )
    Flux<CronJob> findNextCronJobs(ZonedDateTime before);
}

interface CronJobRepositoryInternal {
    <S extends CronJob> Mono<S> save(S entity);

    Flux<CronJob> findAllBy(Pageable pageable);

    Flux<CronJob> findAll();

    Mono<CronJob> findById(Long id);
    // this is not supported at the moment because of https://github.com/jhipster/generator-jhipster/issues/18269
    // Flux<CronJob> findAllBy(Pageable pageable, Criteria criteria);
}
