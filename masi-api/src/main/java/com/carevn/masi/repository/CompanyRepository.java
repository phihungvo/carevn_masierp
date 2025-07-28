package com.carevn.masi.repository;

import com.carevn.masi.constants.AuthoritiesConstants;
import com.carevn.masi.domain.Company;

import java.util.List;
import java.util.UUID;

import com.carevn.masi.service.dto.CompanyQuery;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Modifying;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import com.carevn.masi.domain.User;
/**
 * Spring Data R2DBC repository for the Company entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CompanyRepository extends ReactiveCrudRepository<Company, UUID>, CompanyRepositoryInternal {
    Flux<Company> findAllBy(Pageable pageable);

    @Override
    <S extends Company> Mono<S> save(S entity);

    @Override
    Flux<Company> findAll();

    @Query("SELECT * from company where normalized_name in (:normalizedName)")
    Flux<Company> findAllByNormalizedName(List<String> normalizedName);

    @Query("SELECT  * from  company where normalized_name = :unique")
    Mono<Company> findByIdentifier(String unique);

    @Override
    Mono<Company> findById(UUID id);

    @Override
    Mono<Void> deleteById(UUID id);

    @Query("SELECT u.* FROM masi_user u join masi_user_authority ua on u.id = ua.user_id where ua.user_authority = '" +
        AuthoritiesConstants.DIRECTOR+
        "'and u.activated = true and u.company_id = :companyId limit 1")
    Mono<User> findCompanyDirector(String companyId);

    @Modifying
    @Query("Update masi_user set company_id = :companyId where id = :userId")
    Mono<Void> addUserToCompany(String companyId, UUID userId);

}

interface CompanyRepositoryInternal {
    <S extends Company> Mono<S> save(S entity);

    Flux<Company> findAllBy(Pageable pageable);

    Flux<Company> findAll();

    Mono<Company> findById(UUID id);

    Flux<Company> findAllByQuery(Pageable pageable, CompanyQuery query);

    Mono<Long> countByQuery(CompanyQuery query);
    // this is not supported at the moment because of https://github.com/jhipster/generator-jhipster/issues/18269
    // Flux<Company> findAllBy(Pageable pageable, Criteria criteria);
}
