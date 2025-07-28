package com.carevn.masi.repository;

import com.carevn.masi.constants.AuthoritiesConstants;
import com.carevn.masi.domain.Authority;
import com.carevn.masi.domain.Group;
import com.carevn.masi.domain.User;

import com.carevn.masi.service.dto.BasicSearchQuery;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Modifying;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Collection;
import java.util.Set;
import java.util.UUID;

import com.carevn.masi.domain.User;

@Repository
public interface GroupRepository extends ReactiveCrudRepository<Group, UUID>, GroupRepositoryInternal {

    @Query("INSERT INTO masi_group_user (group_id, user_id) VALUES (:groupId, :userId)")
    Mono<Void> addUserToGroup(UUID groupId, UUID userId);

    @Query("DELETE FROM masi_group_user WHERE group_id = :groupId AND user_id = :userId")
    Mono<Void> removeUserFromGroup(UUID groupId, UUID userId);

    @Query("DELETE FROM masi_group_user WHERE group_id = :groupId")
    Mono<Void> removeAllUserFromGroup(UUID groupId);

    @Query("SELECT masi_group_authority.group_authority FROM masi_group_user join masi_group_authority on masi_group_user.group_id = masi_group_authority.group_id where masi_group_user.user_id = :userId")
    Flux<String> findUserGroupAuthority(UUID userId);

    @Query("SELECT masi_authority.* FROM masi_authority join masi_group_authority on masi_authority.name = masi_group_authority.group_authority where masi_group_authority.group_id = :groupId")
    Flux<Authority> findAllByAuthoritiesByGroupId(UUID groupId);

    @Query("DELETE FROM masi_group_authority WHERE group_id = :groupId")
    Mono<Void> removeAllAuthorityFromGroup(UUID groupId);

    @Query("INSERT INTO masi_group_authority (group_id, group_authority) VALUES (:groupId, :authority)")
    Mono<Void> addAuthorityToGroup(UUID groupId, String authority);

    @Query("DELETE FROM masi_group_authority WHERE group_id = :groupId AND group_authority = :authority")
    Mono<Void> removeAuthorityFromGroup(UUID groupId, String authority);

    Mono<Group> findByActivatedIsTrueAndId(UUID id);

    @Query("SELECT masi_group.* FROM masi_group join masi_group_user on masi_group.id = masi_group_user.group_id where masi_group_user.user_id = :userId limit 1")
    Mono<Group> findFirstByUserId(UUID userId);

    @Query("SELECT group_authority FROM masi_group_authority where masi_group_authority.group_id = :groupId")
    Flux<String> findAuthoritiesByGroupId(UUID groupId);

    @Query("SELECT u.* FROM masi_user u join masi_user_authority ua on u.id = ua.user_id join masi_group_user gu on u.id = gu.user_id join masi_group g on gu.group_id = g.id"
        + " WHERE u.company_id = :companyId AND (g.normalized_name = :groupIdentifier or g.id::varchar = :groupIdentifier) " +
        "and ua.user_authority = '" + AuthoritiesConstants.DEPARTMENT_MANAGER + "'and u.activated = true limit 1")
    Mono<User> findDepartmentManager(String companyId, String groupIdentifier);

    @Modifying
    @Query("DELETE FROM masi_group_user WHERE user_id = :userId")
    Mono<Void> deleteRemoveAllUserGroup(UUID userId);


    @Query("SELECT masi_group.* FROM masi_group join masi_group_user on masi_group.id = masi_group_user.group_id where masi_group_user.user_id = :userId")
    Flux<Group> getGroupsByUserId(UUID userId);


}

interface GroupRepositoryInternal {

    Flux<Group> findAllBy(Pageable pageable);

    Flux<Group> findAll();

    Mono<Group> findByUserId(UUID userId);

    Flux<Group> findAllByQuery(BasicSearchQuery query, Pageable pageable);

    Mono<Long> countAllByQuery(BasicSearchQuery query);

    Mono<Group> getById(UUID id);

    Mono<Void> clearCache();

    Mono<Void> clearCache(UUID id);
}
