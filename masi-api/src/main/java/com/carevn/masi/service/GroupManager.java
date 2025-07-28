package com.carevn.masi.service;

import com.carevn.masi.domain.Group;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Set;
import java.util.UUID;

@Service
public class GroupManager {
    Logger log = LoggerFactory.getLogger(GroupManager.class);

    @Getter
    private final GroupService groupService;
    private final UserService userService;
    private final AuthorityService authorityService;

    public GroupManager(GroupService groupService, UserService userService, AuthorityService authorityService) {
        this.groupService = groupService;
        this.userService = userService;
        this.authorityService = authorityService;
    }

    public Mono<List<Group>> getAll() {
        log.debug("REST request to get all groups");
        return groupService.findAll().collectList();
    }

    public Mono<Void> addAuthorityToGroup(UUID groupId, Set<String> authorities) {
        log.debug("REST request to add authorities to group {}",groupId);
        return groupService.getActiveGroupById(groupId)
            .hasElement()
            .flatMap(hasElement -> {
                if (Boolean.TRUE.equals(hasElement)) {
                    return groupService.addAuthority(groupId, authorities);
                } else {
                    return Mono.error(new RuntimeException("Group not found"));
                }
            })
            .doOnError(e -> log.error("Error while adding authorities to group:", e));
    }

    public Mono<Void> removeAuthorityFromGroup(UUID groupId, Set<String> authorities) {
        log.debug("REST request to remove authorities from group {}",groupId);
        return groupService.removeAuthorities(groupId, authorities)
            .doOnError(e -> log.error("Error while removing authorities from group:", e));
    }

    public Mono<Void> addUserToGroup(UUID groupId, Set<UUID> userIds) {
        log.debug("REST request to add users to group {}",groupId);
        return groupService.getActiveGroupById(groupId)
            .hasElement()
            .flatMap(hasElement -> {
                if (Boolean.TRUE.equals(hasElement)) {
                    return groupService.addUser(groupId, userIds);
                } else {
                    return Mono.error(new RuntimeException("Group not found"));
                }
            })
            .doOnError(e -> log.error("Error while adding authorities to group:", e));
    }






    public Mono<Void> removeUserFromGroup(UUID groupId, Set<UUID> userIds) {
        log.debug("REST request to remove users from group {}",groupId);
        return groupService.removeUserFromGroup(groupId, userIds)
            .doOnError(e -> log.error("Error while removing user from group:", e));
    }
}
