package com.carevn.masi.web.rest;

import com.carevn.masi.domain.Group;
import com.carevn.masi.domain.enumerate.GenericAction;
import com.carevn.masi.dto.ApiResponse;
import com.carevn.masi.dto.Response;
import com.carevn.masi.service.GroupManager;
import com.carevn.masi.service.dto.BasicSearchQuery;
import com.carevn.masi.service.dto.CreateGroupRequest;
import com.carevn.masi.service.dto.GroupAuthorityDTO;
import com.carevn.masi.service.dto.GroupUserDTO;
import io.swagger.v3.oas.annotations.Operation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.*;

@RestController
@RequestMapping("/api/groups")
public class GroupResource {

    private final Logger log = LoggerFactory.getLogger(GroupResource.class);

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final GroupManager groupManager;

    public GroupResource(GroupManager groupManager) {
        this.groupManager = groupManager;
    }

//    @GetMapping("")
//    public Mono<List<Group>> getAllGroups() {
//        return groupManager.getAll();
//    }

    @Operation(summary = "add or remove authorities to/from group")
    @PostMapping("/authorities")
    public Mono<ResponseEntity<Response>> manageAuthorities(@RequestParam("action") GenericAction action, @RequestBody GroupAuthorityDTO groupAuthorityDTO) {
        return switch (action) {
            case ADD ->
                groupManager.addAuthorityToGroup(groupAuthorityDTO.getGroupId(), groupAuthorityDTO.getAuthorities())
                    .then(Mono.just(ResponseEntity.ok().body(Response.success("Authorities added successfully", null))))
                    .onErrorResume(e -> Mono.just(ResponseEntity.badRequest().body(Response.error("Error while adding authorities", e))));
            case REMOVE ->
                groupManager.removeAuthorityFromGroup(groupAuthorityDTO.getGroupId(), groupAuthorityDTO.getAuthorities())
                    .then(Mono.just(ResponseEntity.ok().body(Response.success("Authorities removed successfully", null)))
                        .onErrorResume(e -> Mono.just(ResponseEntity.badRequest().body(Response.error("Error while removing authorities", e)))));
            case REPLACE -> groupManager.removeAuthorityFromGroup(groupAuthorityDTO.getGroupId(), new HashSet<>())
                .then(groupManager.addAuthorityToGroup(groupAuthorityDTO.getGroupId(), groupAuthorityDTO.getAuthorities()))
                .then(Mono.just(ResponseEntity.ok().body(Response.success("Authorities replaced successfully", null)))
                    .onErrorResume(e -> Mono.just(ResponseEntity.badRequest().body(Response.error("Error while replacing authorities", e)))));

        };
    }

    @Operation(summary = "get all groups")
    @GetMapping("")
    public Mono<ApiResponse<Group>>
    findAll(@ParameterObject Pageable pageable,
            @ParameterObject BasicSearchQuery query
    ) {
        return ApiResponse.from(groupManager.getGroupService().findAllByQuery(query, pageable), groupManager.getGroupService().countByQuery(query));

    }


    @Operation(summary = "create a new group")
    @PostMapping("")
    public Mono<ResponseEntity<Response>> createGroup(@RequestBody CreateGroupRequest createGroupRequest) {
        return groupManager.getGroupService().save(createGroupRequest)
            .map(group -> ResponseEntity.ok().body(Response.success("Group created successfully", group)))
            .onErrorResume(e -> Mono.just(ResponseEntity.badRequest().body(Response.error("Error while creating group", e))));
    }

    @Operation(summary = "delete a group")
    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Response>> deleteGroup(@PathVariable("id") UUID id) {
        return groupManager.getGroupService().deleteGroup(id)
            .then(Mono.just(ResponseEntity.ok().body(Response.success("Group deleted successfully", null))))
            .onErrorResume(e -> Mono.just(ResponseEntity.badRequest().body(Response.error("Error while deleting group", e))));
    }

    @Operation(summary = "get a group by id")
    @GetMapping("/{id}")
    public Mono<ResponseEntity<Group>> getGroup(@PathVariable("id") UUID id) {
        return groupManager.getGroupService().getById(id)
            .map(ResponseEntity.ok()::body)
            .switchIfEmpty(Mono.just(ResponseEntity.notFound().build()));
    }

    @Operation(summary = "full update a group")
    @PutMapping("/{id}")
    public Mono<ResponseEntity<Response>> updateGroup(@PathVariable("id") UUID id, @RequestBody CreateGroupRequest createGroupRequest) {
        createGroupRequest.setId(id);
        return groupManager.getGroupService().updateGroup(createGroupRequest)
            .map(group -> ResponseEntity.ok().body(Response.success("Group updated successfully", group)))
            .onErrorResume(e -> Mono.just(ResponseEntity.badRequest().body(Response.error("Error while updating group", e))));
    }


    @GetMapping("/{identifier}/manager")
    @Transactional(readOnly = true)

    public Mono<ResponseEntity<Map<String, Object>>> getDepartmentManager(@PathVariable("identifier") String groupIdentifier) {
        return groupManager.getGroupService().findDepartmentManager(groupIdentifier)
            .map(user -> {
                Map<String, Object> map = new HashMap<>();
                map.put("id", user.getId());
                map.put("fullName", user.getFullName());
                return ResponseEntity.ok().body(map);
            })
            .switchIfEmpty(Mono.just(ResponseEntity.notFound().build()));
    }

    @PostMapping("/users")
    @Operation(summary = "add or remove users to/from group")
    public Mono<ResponseEntity<Response>> manageUsers(@RequestParam("action") GenericAction action, @RequestBody GroupUserDTO groupUserDTO) {
        log.debug("REST request to manage users in group : {} action : {}", groupUserDTO.getGroupId(), action);
        return switch (action) {
            case ADD -> groupManager.addUserToGroup(groupUserDTO.getGroupId(), groupUserDTO.getUserIds())
                .then(Mono.just(ResponseEntity.ok().body(Response.success("Users added successfully", null))))
                .onErrorResume(e -> Mono.just(ResponseEntity.badRequest().body(Response.error("Error while adding users", e))));
            case REMOVE -> groupManager.removeUserFromGroup(groupUserDTO.getGroupId(), groupUserDTO.getUserIds())
                .then(Mono.just(ResponseEntity.ok().body(Response.success("Users removed successfully", null))))
                .onErrorResume(e -> Mono.just(ResponseEntity.badRequest().body(Response.error("Error while removing users", e))));
            case REPLACE -> groupManager.getGroupService().removeAllUserFromGroup(groupUserDTO.getGroupId())
                .then(groupManager.addUserToGroup(groupUserDTO.getGroupId(), groupUserDTO.getUserIds()))
                .then(Mono.just(ResponseEntity.ok().body(Response.success("Users replaced successfully", null)))
                    .onErrorResume(e -> Mono.just(ResponseEntity.badRequest().body(Response.error("Error while replacing users", e)))));
        };
    }

}
