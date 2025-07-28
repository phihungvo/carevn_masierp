package com.carevn.masi.web.rest;

import com.carevn.masi.domain.Authority;
import com.carevn.masi.dto.ApiResponse;
import com.carevn.masi.repository.AuthorityRepository;
import com.carevn.masi.repository.UserRepository;
import com.carevn.masi.security.AuthoritiesConstants;
import com.carevn.masi.service.UserService;
import com.carevn.masi.service.dto.UserDTO;

import java.util.*;

import com.carevn.masi.utils.StringUtils;
import io.swagger.v3.oas.annotations.Operation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.http.ResponseEntity;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.ForwardedHeaderUtils;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import tech.jhipster.web.util.PaginationUtil;

@RestController
@RequestMapping("/api")
public class PublicUserResource {

    private static final List<String> ALLOWED_ORDERED_PROPERTIES = Collections.unmodifiableList(
        Arrays.asList("id", "login", "firstName", "lastName", "email", "activated", "langKey"));

    private final Logger log = LoggerFactory.getLogger(PublicUserResource.class);

    private final UserService userService;
    private final UserRepository userRepository;
    private final AuthorityRepository authorityRepository;

    public PublicUserResource(UserService userService, UserRepository userRepository, AuthorityRepository authorityRepository) {
        this.userService = userService;
        this.userRepository = userRepository;
        this.authorityRepository = authorityRepository;
    }

    /**
     * {@code GET /users} : get all users with only public information - calling
     * this method is allowed for anyone.
     *
     * @param request  a {@link ServerHttpRequest} request.
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body
     * all users.
     */
    @GetMapping("/users")
    public Mono<ResponseEntity<Flux<UserDTO>>> getAllPublicUsers(
        ServerHttpRequest request,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get all public User names");
        if (!onlyContainsAllowedProperties(pageable)) {
            return Mono.just(ResponseEntity.badRequest().build());
        }

        return userService
            .countManagedUsers()
            .map(total -> new PageImpl<>(new ArrayList<>(), pageable, total))
            .map(page -> PaginationUtil.generatePaginationHttpHeaders(
                ForwardedHeaderUtils.adaptFromForwardedHeaders(request.getURI(), request.getHeaders()),
                page))
            .map(headers -> ResponseEntity.ok().headers(headers).body(userService.getAllPublicUsers(pageable)));
    }

    public record UserQuery(List<UUID> ids) {
    }

    /**
     * {{@code @Method} POST /user}
     * @param pageable
     * @return a list of users
     **/
    @PostMapping("/users/list")
    public Mono<ResponseEntity<Flux<UserDTO>>> getUsers(
        ServerHttpRequest request,
        @ParameterObject Pageable pageable,
        @RequestBody(required = false) UserQuery query) {
        return userService
            .countManagedUsers()
            .map(total -> new PageImpl<>(new ArrayList<>(), pageable, total))
            .map(page -> ResponseEntity.ok().body(userService.getAllPublicUsers(pageable, query)));
    }


    private boolean onlyContainsAllowedProperties(Pageable pageable) {
        return pageable.getSort().stream().map(Sort.Order::getProperty).allMatch(ALLOWED_ORDERED_PROPERTIES::contains);
    }

    /**
     * Gets a list of all roles.
     *
     * @return a string list of all roles.
     */
    @Operation(summary = "Get a list of all roles and permissions")
    @GetMapping("/authorities")
    public Mono<ApiResponse<Authority>> getAuthorities(
        @RequestParam(required = false, defaultValue = "PERMISSION") String type,
        @RequestParam(required = false, defaultValue = "false") boolean onlyUserAuthorities,
        @RequestParam(required = false, defaultValue = "") String search, @ParameterObject Pageable pageable) {
        if (pageable == null) {
            pageable = Pageable.ofSize(10000).withPage(1);
        }
        var queryType = "PERMISSION.%";
        if ("ROLE".equals(type)) {
            queryType = "ROLE%";
        }
        search = "%" + search.replace("'", "''") + "%";
        return ApiResponse.from(authorityRepository.findByDescriptionLike(search, queryType, pageable.getPageSize(), pageable.getOffset()).collectList(), authorityRepository.countByDescriptionLike(search, queryType));
    }


    public record Payload(String authority) {
    }

    @PostMapping("/my-account/authority")
    public Mono<ResponseEntity<Object>> addToMyAuthority(@RequestBody Payload authority) {
        System.out.println("Adding authority: " + authority);
        return com.carevn.masi.utils.SecurityUtils.getUserJWTDetail().flatMap(user -> {
            return userRepository.saveUserAuthority(user.getUserId(), authority.authority()).then(Mono.fromCallable(() -> ResponseEntity.ok().build()));
        });

    }

    @DeleteMapping("my-account/authority")

    public Mono<ResponseEntity<Object>> removeAuthority(@RequestBody Payload authority) {
        return com.carevn.masi.utils.SecurityUtils.getUserJWTDetail().flatMap(user -> {
            return userRepository.deleteUserAuthority(user.getUserId(), authority.authority()).then(Mono.fromCallable(() -> ResponseEntity.ok().build()));
        });
    }

    @GetMapping("account/authority")
    public Mono<ResponseEntity<List<String>>> getMyAuthority() {
        return com.carevn.masi.utils.SecurityUtils.getUserJWTDetail().flatMap(user -> {
            return userRepository.findUserAuthorities(user.getUserId()).collectList().map(authorities -> {
                return ResponseEntity.ok().body(authorities);
            });
        });
    }

    /**
     * {@code SEARCH /users/_search/:query} : search for the User corresponding to
     * the query.
     *
     * @param query the query to search.
     * @return the result of the search.
     */
    @GetMapping("/users/_search/{query}")
    public Mono<List<UserDTO>> search(@PathVariable("query") String query) {
        return userService.getAllPublicUsers(null).collectList();
    }
}
