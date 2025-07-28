package com.carevn.masi.repository;

import com.carevn.masi.domain.Authority;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Spring Data R2DBC repository for the {@link Authority} entity.
 */
public interface AuthorityRepository extends R2dbcRepository<Authority, String> {

    //    "unaccent(%s) ilike unaccent('%%%s%%')
    @Query("SELECT * FROM masi_authority WHERE unaccent(description) iLIKE unaccent(:name) and name ilike :type limit :limit offset :offset")
    Flux<Authority> findByDescriptionLike(String name,String type, int limit, Long offset);

    @Query("SELECT COUNT(*) FROM masi_authority WHERE unaccent(description) LIKE unaccent(:name)  and name ilike :type")
    Mono<Long> countByDescriptionLike(String name,String type);
}
