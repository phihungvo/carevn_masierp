package com.carevn.masi.security;

import com.carevn.masi.domain.Authority;
import com.carevn.masi.domain.Group;
import com.carevn.masi.domain.User;
import com.carevn.masi.repository.GroupRepository;
import com.carevn.masi.repository.UserRepository;

import java.util.*;

import org.hibernate.validator.internal.constraintvalidators.hv.EmailValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Authenticate a user from the database.
 */
@Component("userDetailsService")
public class DomainUserDetailsService implements ReactiveUserDetailsService {

    private final Logger log = LoggerFactory.getLogger(DomainUserDetailsService.class);

    private final UserRepository userRepository;

    private final GroupRepository groupRepository;

    public DomainUserDetailsService(UserRepository userRepository, GroupRepository groupRepository) {
        this.userRepository = userRepository;
        this.groupRepository = groupRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public Mono<UserDetails> findByUsername(final String userName) {
        log.debug("Authenticating {}", userName);

        if (new EmailValidator().isValid(userName, null)) {
            return userRepository
                .findOneWithAuthoritiesByEmailIgnoreCase(userName)
                .doOnError(e -> log.error("Error getting user by email", e))
                .switchIfEmpty(Mono.error(new UsernameNotFoundException("User with email " + userName + " was not found in the database")))
                .flatMap(user -> createSpringSecurityUser(userName, user).doOnError(e -> log.error("Error creating spring security user", e)));
        }

        return userRepository
            .findOneWithAuthoritiesByUserName(userName)
            .flatMap(a->{
                log.info("User found: {}", a);
                return Mono.just(a);
            })
            .doOnError(e -> log.error("Error getting user by username", e))
            .switchIfEmpty(Mono.defer(() ->{
                log.error("User not found: {}", userName);
                return  Mono.error(new UsernameNotFoundException("User " + userName + " was not found in the database"));
            }))
            .flatMap(user -> createSpringSecurityUser(userName, user).doOnError(e -> log.error("Error creating spring security user", e)));
    }

    private Mono<org.springframework.security.core.userdetails.User> createSpringSecurityUser(String lowercaseUserName, User user) {
        if (!user.isActivated()) {
            throw new UserNotActivatedException("User " + lowercaseUserName + " was not activated");
        }
        Collection<SimpleGrantedAuthority> grantedAuthorities = new HashSet<>(user
            .getAuthorities()
            .stream()
            .map(Authority::getName)
            .map(SimpleGrantedAuthority::new)
            .toList());

        return
            getGroupAuthorities(user)
                .collectList()
                .map(grantedAuthorities::addAll)
                .map(ignored -> {
                    return new org.springframework.security.core.userdetails.User(user.getEmployeeId().toString(), user.getPassword(), grantedAuthorities);
                });
    }

    private Flux<SimpleGrantedAuthority> getGroupAuthorities(User user) {
        return groupRepository
            .findFirstByUserId(user.getId())
            .flatMap(group -> groupRepository.findAuthoritiesByGroupId(group.getId()).collectList())
            .flatMapMany(Flux::fromIterable)
            .map(SimpleGrantedAuthority::new)
            .doOnError(e -> log.error("Error getting group authorities", e))
            .switchIfEmpty(Flux.empty());
    }
}
