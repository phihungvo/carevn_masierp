package com.carevn.masi.repository;

import static org.springframework.data.relational.core.query.Criteria.where;
import static org.springframework.data.relational.core.query.Query.query;

import com.carevn.masi.domain.Authority;
import com.carevn.masi.domain.User;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.commons.beanutils.BeanComparator;
import org.springframework.data.domain.*;
import org.springframework.data.r2dbc.convert.R2dbcConverter;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.data.relational.core.query.Update;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple2;
import reactor.util.function.Tuple3;
import reactor.util.function.Tuples;

/**
 * Spring Data R2DBC repository for the {@link User} entity.
 */
@Repository
public interface UserRepository extends R2dbcRepository<User, UUID>, UserRepositoryInternal {
    Mono<User> findOneByActivationKey(String activationKey);

    Flux<User> findAllByActivatedIsFalseAndActivationKeyIsNotNullAndCreatedDateBefore(LocalDateTime dateTime);

    Mono<User> findOneByResetKey(String resetKey);

    Mono<User> findOneByEmailIgnoreCase(String email);

    Mono<User> findOneByUserName(String userName);

    Flux<User> findAllByIdNotNull(Pageable pageable);

    Flux<User> findAllByIdNotNullAndActivatedIsTrue(Pageable pageable);

    Flux<User> findAllByIdInAndActivatedIsTrue(Pageable pageable, List<UUID> ids);

    Mono<User> findByActivatedIsTrueAndId(UUID id);

    Mono<Long> count();

    @Query("SELECT * FROM masi_user WHERE id IN (SELECT user_id FROM masi_group_user WHERE group_id = :groupId)")
    Flux<User> findUsersByGroupId(UUID groupId);

    @Query("INSERT INTO masi_user_authority VALUES(:userId, :authority)")
    Mono<Void> saveUserAuthority(UUID userId, String authority);

    @Query("DELETE FROM masi_user_authority")
    Mono<Void> deleteAllUserAuthorities();

    @Query("DELETE FROM masi_user_authority WHERE user_id = :userId")
    Mono<Void> deleteUserAuthorities(UUID userId);

    @Query("SELECT user_authority FROM masi_user_authority  WHERE user_id = :userId")
    Flux<String> findUserAuthorities(UUID userId);

    @Query("DELETE FROM masi_user_authority WHERE user_id = :userId AND authority = :authority")
    Mono<Void> deleteUserAuthority(UUID userId, String authority);

    @Query("SELECT * FROM masi_user WHERE id = :id")
    Mono<User> findById(UUID id);

    @Query("SELECT masi_user.* FROM masi_user join masi_user_authority on masi_user.id = masi_user_authority.user_id where masi_user_authority.user_authority = :authority limit :limit offset :offset")
    Flux<User> findAllByAuthority(String authority,int limit, long offset);

    @Query("select Count(*) as count FROM masi_user_authority where user_authority = :authority")
    Mono<Long> countByAuthority(String authority);

    @Query("SELECT COUNT(user_name) FROM masi_user WHERE user_name = :newUsername AND user_name != :oldUsername")
    Mono<Long> countByUserName(String newUsername, String oldUsername);

    @Query("SELECT COUNT(name) FROM masi_authority WHERE name = :role")
    Mono<Long> findAllMasiAuthorityByRole(String role);


}

interface DeleteExtended<T> {
    Mono<Void> delete(T user);
}

interface UserRepositoryInternal extends DeleteExtended<User> {
    Mono<User> findOneWithAuthoritiesByUserName(String userName);

    Mono<User> findOneWithAuthoritiesById(UUID userId);

    Mono<User> findOneWithAuthoritiesByEmailIgnoreCase(String email);

    Flux<User> findAllWithAuthorities(Pageable pageable);

    Mono<User> saveUser(User user);

    Mono<Void> updateSignature(User user);
}

class UserRepositoryInternalImpl implements UserRepositoryInternal {

    private final DatabaseClient db;
    private final R2dbcEntityTemplate r2dbcEntityTemplate;
    private final R2dbcConverter r2dbcConverter;

    public UserRepositoryInternalImpl(DatabaseClient db, R2dbcEntityTemplate r2dbcEntityTemplate,
                                      R2dbcConverter r2dbcConverter) {
        this.db = db;
        this.r2dbcEntityTemplate = r2dbcEntityTemplate;
        this.r2dbcConverter = r2dbcConverter;
    }

    @Override
    public Mono<User> findOneWithAuthoritiesByUserName(String userName) {
        return findOneWithAuthoritiesBy("user_name", userName);
    }

    @Override
    public Mono<User> findOneWithAuthoritiesById(UUID id) {
        return findOneWithAuthoritiesBy("id", id);
    }

    @Override
    public Mono<User> findOneWithAuthoritiesByEmailIgnoreCase(String email) {
        return findOneWithAuthoritiesBy("email", email);
    }

    @Override
    public Flux<User> findAllWithAuthorities(Pageable pageable) {
        String property = pageable.getSort().stream().map(Sort.Order::getProperty).findFirst().orElse("id");
        String direction = String.valueOf(
            pageable.getSort().stream().map(Sort.Order::getDirection).findFirst().orElse(Sort.DEFAULT_DIRECTION));
        long page = pageable.getPageNumber();
        long size = pageable.getPageSize();

        return db
            .sql("SELECT * FROM masi_user u LEFT JOIN masi_user_authority ua ON u.id=ua.user_id")
            .map((row, metadata) -> Tuples.of(r2dbcConverter.read(User.class, row, metadata),
                Optional.ofNullable(row.get("authority_name", String.class))))
            .all()
            .groupBy(t -> t.getT1().getUserName())
            .flatMap(l -> l.collectList().map(t -> updateUserWithAuthorities(t.get(0).getT1(), t)))
            .sort(
                Sort.Direction.fromString(direction) == Sort.DEFAULT_DIRECTION
                    ? new BeanComparator<>(property)
                    : new BeanComparator<>(property).reversed())
            .skip(page * size)
            .take(size);
    }

    @Override
    public Mono<Void> delete(User user) {
        return db
            .sql("DELETE FROM masi_user_authority WHERE user_id = :userId")
            .bind("userId", user.getId())
            .then()
            .then(r2dbcEntityTemplate.delete(User.class).matching(query(where("id").is(user.getId()))).all()
                .then());
    }

    @Override
    public Mono<Void> updateSignature(User user) {
        assert user.getId() != null;
        return db
            .sql("UPDATE masi_user SET signature_id = :signatureId, signature_file_name = :signatureFileName WHERE id = :id")
            .bind("signatureId", user.getSignatureId())
            .bind("signatureFileName", user.getSignatureFileName())
            .bind("id", user.getId())
            .fetch()
            .rowsUpdated()
            .then();
    }

    @Override
    public Mono<User> saveUser(User user) {
        if (user.getId() == null) {
            user.setId(UUID.randomUUID());
        }
        return r2dbcEntityTemplate.insert(User.class).using(user)
            .thenMany(Flux.fromIterable(user.getAuthorities()))
            .flatMap(authority -> saveUserAuthority(user.getId(), authority.getName()))
            .then(Mono.just(user));
    }

    private Mono<Void> saveUserAuthority(UUID userId, String authority) {
        return db.sql("INSERT INTO masi_user_authority VALUES(:userId, :authority)")
            .bind("userId", userId)
            .bind("authority", authority)
            .then();
    }

    private Mono<User> findOneWithAuthoritiesBy(String fieldName, Object fieldValue) {
        return db
            .sql("SELECT * FROM masi_user u " +
                "LEFT JOIN masi_user_authority ua ON u.id=ua.user_id " +
                "LEFT JOIN masi_group_user ug ON u.id=ug.user_id " +
                "LEFT JOIN masi_group_authority ga ON ug.group_id=ga.group_id " +
                "WHERE u." + fieldName + " = :" + fieldName)
            .bind(fieldName, fieldValue)
            .map((row, metadata) -> Tuples.of(
                r2dbcConverter.read(User.class, row, metadata),
                Optional.ofNullable(row.get("user_authority", String.class)),
                Optional.ofNullable(row.get("group_authority", String.class))))
            .all()
            .collectList()
            .filter(l -> !l.isEmpty())
            .map(l -> updateUserWithAllAuthorities(l.get(0).getT1(), l));
    }

    private User updateUserWithAuthorities(User user, List<Tuple2<User, Optional<String>>> tuples) {
        user.setAuthorities(
            tuples
                .stream()
                .filter(t -> t.getT2().isPresent())
                .map(t -> {
                    Authority authority = new Authority();
                    authority.setName(t.getT2().orElseThrow());
                    return authority;
                })
                .collect(Collectors.toSet()));

        return user;
    }

    private User updateUserWithAllAuthorities(User user,
                                              List<Tuple3<User, Optional<String>, Optional<String>>> tuples) {
        user.setAuthorities(
            tuples
                .stream()
                .flatMap(t -> Stream.of(t.getT2(), t.getT3()))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .map(authorityName -> {
                    Authority authority = new Authority();
                    authority.setName(authorityName);
                    return authority;
                })
                .collect(Collectors.toSet()));

        return user;
    }
}
