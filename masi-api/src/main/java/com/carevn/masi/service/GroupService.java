package com.carevn.masi.service;

import com.carevn.masi.domain.Authority;
import com.carevn.masi.domain.Group;
import com.carevn.masi.domain.User;
import com.carevn.masi.repository.GroupRepository;
import com.carevn.masi.repository.UserRepository;
import com.carevn.masi.service.dto.BasicSearchQuery;
import com.carevn.masi.service.dto.CreateGroupRequest;
import com.carevn.masi.utils.SecurityUtils;
import com.carevn.masi.utils.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class GroupService {
    private final Logger log = LoggerFactory.getLogger(GroupService.class);

    private final GroupRepository groupRepository;
    private final UserRepository userRepository;

    public GroupService(GroupRepository groupRepository, UserRepository userRepository) {
        this.groupRepository = groupRepository;
        this.userRepository = userRepository;
    }

    @Transactional(readOnly = true)
    public Mono<User> findDepartmentManager(String groupIdentifier) {
        return SecurityUtils.getUserJWTDetail()
            .flatMap(user -> groupRepository.findDepartmentManager(user.getCompanyId(), groupIdentifier));
    }

    @Transactional
    public Mono<Void> removeAllUserFromGroup(UUID groupId) {
        return groupRepository.removeAllUserFromGroup(groupId);
    }

    @Transactional(readOnly = true)
    public Mono<Long> countByQuery(BasicSearchQuery query) {
        return groupRepository.countAllByQuery(query);
    }

    @Transactional(readOnly = true)
    public Flux<Group> findAllByQuery(BasicSearchQuery query, Pageable pageable) {
        return groupRepository.findAllByQuery(query, pageable);
    }

    @Transactional
    public Mono<Group> save(CreateGroupRequest createGroupRequest) {
        Group group = new Group();
        group.setName(createGroupRequest.getName());
        group.setDescription(createGroupRequest.getDescription());
        group.setActivated(true);

        group.setNormalizedName(StringUtils.normalizeName(createGroupRequest.getName()));
        group.setCreatedBy("system");
        group.setCreatedDate(Instant.now());
        return SecurityUtils.getUserJWTDetail()
            .flatMap(user -> {
                group.setCompanyId(user.getCompanyId());
                group.setWorkspaceId(user.getCompanyId());
                return groupRepository.save(group);
            })
            .flatMap(savedGroup -> {
                return this.addAuthority(savedGroup.getId(), new HashSet<>(createGroupRequest.getAuthorities()))
                    .then(groupRepository.clearCache(savedGroup.getId()))
                    .then(Mono.just(savedGroup));
            });
    }

    public Mono<Group> getById(UUID id) {
        return groupRepository.getById(id).flatMap(group -> {
            return userRepository.findUsersByGroupId(id)
                .collect(Collectors.toSet())
                .zipWith(groupRepository.findAllByAuthoritiesByGroupId(id).collect(Collectors.toSet()))
                .map(tuple -> {
                    group.setUsers(tuple.getT1());
                    group.setAuthorities(tuple.getT2());
                    return group;
                }).then(Mono.just(group));
        });
    }

    @Transactional
    public Flux<Group> findAll() {
        Flux<Group> groups = groupRepository.findAll();
        log.debug("Fetching all groups");
        return groups.doOnError(e -> log.error("Error while fetching groups", e));
    }

    @Transactional
    public Mono<Void> addAuthority(UUID groupId, Set<String> authorities) {

        return Flux.fromIterable(authorities)
            .flatMap(authority -> {
                log.debug("Adding authority {} to group {}", authority, groupId);
                return groupRepository.addAuthorityToGroup(groupId, authority);
            })
            .then(groupRepository.clearCache(groupId));
    }

    @Transactional
    public Mono<Void> removeAuthorities(UUID groupId, Set<String> authorities) {
        return Flux.fromIterable(authorities)
            .flatMap(authority -> {
                log.debug("Removing authority {} from group {}", authority, groupId);
                return groupRepository.removeAuthorityFromGroup(groupId, authority);
            })
            .then(groupRepository.clearCache(groupId));
    }

    public Mono<Void> addUser(UUID groupId, Set<UUID> userIds) {

        return Flux.fromIterable(userIds)
            .flatMap(userId -> groupRepository.addUserToGroup(groupId, userId))
            .then(groupRepository.clearCache(groupId));
    }

    public Mono<Void> addToUser(Set<UUID> groupIds, UUID userId) {
        return groupRepository.findAllById(groupIds)
            .flatMap(groups -> {
                return groupRepository.addUserToGroup(groups.getId(), userId);
            }).then();
    }

    public Mono<Void> removeFromUser(Set<UUID> groupIds, UUID userId) {
        return Flux.fromIterable(groupIds)
            .flatMap(groupId -> groupRepository.removeUserFromGroup(groupId, userId))
            .then(groupRepository.clearCache(userId));
    }
    public Mono<Void> removeAllGroupUserOfGroup( UUID userId) {
        return groupRepository.deleteRemoveAllUserGroup(userId)
            .then(groupRepository.clearCache(userId));
    }
    public Mono<Void> removeUserFromGroup(UUID groupId, Set<UUID> userIds) {
        log.debug("Removing user {} from group {}", userIds, groupId);
        return Flux.fromIterable(userIds)
            .flatMap(userId -> groupRepository.removeUserFromGroup(groupId, userId))
            .then(groupRepository.clearCache(groupId));
    }


    @Transactional
    public Mono<Group> updateGroup(CreateGroupRequest createGroupRequest) {
        return groupRepository.findById(createGroupRequest.getId())
            .flatMap(group -> {
                group.setName(createGroupRequest.getName());
                group.setDescription(createGroupRequest.getDescription());
                group.setNormalizedName(StringUtils.normalizeName(createGroupRequest.getName()));
                return groupRepository.save(group.setIsPersisted());
            }).flatMap(savedGroup -> {
                return groupRepository.removeAllAuthorityFromGroup(savedGroup.getId())
                    .then(this.addAuthority(savedGroup.getId(), new HashSet<>(createGroupRequest.getAuthorities())))
                    .then(groupRepository.clearCache(savedGroup.getId()))
                    .then(Mono.just(savedGroup));
            });
    }

    public Mono<Group> getGroupIdByUserId(UUID userId) {
        return groupRepository.findFirstByUserId(userId)
            .switchIfEmpty(Mono.just(new Group()));
    }

    public Mono<Group> getActiveGroupById(UUID id) {
        return groupRepository.findByActivatedIsTrueAndId(id);
    }

    public Mono<Void> deleteGroup(UUID groupId) {
        return groupRepository.deleteById(groupId);
    }

    public Mono<List<Group> > getGroupsByUserId(UUID userId) {
        return groupRepository.getGroupsByUserId(userId).collectList();
    }
}
