package com.carevn.masi.service;

import com.carevn.masi.config.Constants;
import com.carevn.masi.domain.Authority;
import com.carevn.masi.domain.User;
import com.carevn.masi.dto.UserJWTDetail;
import com.carevn.masi.repository.AuthorityRepository;
import com.carevn.masi.repository.CompanyRepository;
import com.carevn.masi.repository.GroupRepository;
import com.carevn.masi.repository.UserRepository;
import com.carevn.masi.security.AuthoritiesConstants;
import com.carevn.masi.security.SecurityUtils;
import com.carevn.masi.service.dto.*;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

import com.carevn.masi.web.rest.AccountResource;
import com.carevn.masi.web.rest.PublicUserResource;
import com.carevn.masi.web.rest.errors.BadRequestAlertException;
import com.nimbusds.jose.jwk.source.ImmutableSecret;
import com.nimbusds.jose.util.Base64;
import io.r2dbc.postgresql.codec.Json;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.support.NotFoundException;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;
import tech.jhipster.security.RandomUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import static com.carevn.masi.security.SecurityUtils.AUTHORITIES_KEY;
import static com.carevn.masi.security.SecurityUtils.JWT_ALGORITHM;

/**
 * Service class for managing users.
 */
@Service
public class UserService {

    private final Logger log = LoggerFactory.getLogger(UserService.class);

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final AuthorityRepository authorityRepository;

    private final CompanyRepository companyRepository;

    private final GroupRepository groupRepository;
    private final  StreamBridge streamBridge;

    public UserService(
        UserRepository userRepository,
        PasswordEncoder passwordEncoder,
        AuthorityRepository authorityRepository,
        CompanyRepository companyRepository, GroupRepository groupRepository, StreamBridge streamBridge) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authorityRepository = authorityRepository;
        this.companyRepository = companyRepository;
        this.groupRepository = groupRepository;
        this.streamBridge = streamBridge;
    }

    private void sendUserStatusChange(User user) {
        UserStatusChangeDto userStatusChangeDto = new UserStatusChangeDto();
        userStatusChangeDto.setId(user.getId());
        userStatusChangeDto.setActive(user.isActivated());
        log.info("Sending user status change: {}", userStatusChangeDto);
        Mono.fromRunnable(() -> {
            try {
                streamBridge.send("userStatusChange-out-0", userStatusChangeDto, MediaType.APPLICATION_JSON);
            } catch (Exception e) {
                log.error("Error sending user status change", e);
            }
        }).subscribeOn(Schedulers.boundedElastic()).subscribe();
}

    @Transactional
    public Mono<User> activateRegistration(String key) {
        log.debug("Activating user for activation key {}", key);
        return userRepository
            .findOneByActivationKey(key)
            .flatMap(user -> {
                // activate given user for the registration key.
                user.setActivated(true);
                user.setActivationKey(null);
                return saveUser(user);
            })
            .doOnNext(user -> log.debug("Activated user: {}", user));
    }

    @Transactional
    public Mono<Void> toggleUserActivation(UUID id) {
        return userRepository.findById(id)
            .flatMap(user -> {
                user.setActivated(!user.isActivated());
                return saveUser(user).map(u -> {
                    sendUserStatusChange(u);
                    return u;
                }).then();
            })
            .then();
    }

    @Transactional
    public Mono<User> updateMySignature(AddSignatureDTO dto) {
        return com.carevn.masi.utils.SecurityUtils.getUserJWTDetail()
            .flatMap(userJwt -> {
                return userRepository.findById(userJwt.getUserId())
                    .flatMap(user -> {
                        user.setSignatureId(dto.getSignatureId());
                        user.setSignatureFileName(dto.getSignatureName());
                        return userRepository.updateSignature(user).then(Mono.just(user));
                    });
            });
    }

    @Transactional
    public Mono<User> completePasswordReset(String newPassword, String key) {
        log.debug("Reset user password for reset key {}", key);
        return userRepository
            .findOneByResetKey(key)
            .filter(user -> user.getResetDate().isAfter(Instant.now().minus(1, ChronoUnit.DAYS)))
            .publishOn(Schedulers.boundedElastic())
            .map(user -> {
                user.setPassword(passwordEncoder.encode(newPassword));
                user.setResetKey(null);
                user.setResetDate(null);
                return user;
            })
            .flatMap(this::saveUser);
    }

    public Mono<User> getById(UUID id) {
        return userRepository.findById(id)
            .flatMap(user -> companyRepository.findByIdentifier(user.getCompanyId()).map(company -> {
                user.setCompany(company);
                return user;
            }).then(Mono.just(user)));
    }

    @Transactional
    public Mono<User> requestPasswordReset(String mail) {
        return userRepository
            .findOneByEmailIgnoreCase(mail)
            .filter(User::isActivated)
            .publishOn(Schedulers.boundedElastic())
            .map(user -> {
                user.setResetKey(RandomUtil.generateResetKey());
                user.setResetDate(Instant.now());
                return user;
            })
            .flatMap(this::saveUser);
    }

    public Mono<User> updateAuth(UUID id, String userName, String password) {
        return userRepository
            .findById(id)
            .map(user -> {
                if (password != null)
                    user.setPassword(passwordEncoder.encode(password));
                if (userName != null)
                    user.setUserName(userName);
                return user;
            })
            .flatMap(this::saveUser);
    }

    @Transactional
    public Mono<User> registerUser(AdminUserDTO userDTO, String password) {
        if (StringUtils.isBlank(userDTO.getEmail())) {
            userDTO.setEmail(UUID.randomUUID().toString() + "@carevn.com");
        }
        return com.carevn.masi.utils.SecurityUtils.getUserJWTDetail().flatMap(login -> {
            return
                userRepository.findById(userDTO.getId())
                    .flatMap(user -> {
                        return updateAuth(user.getId(), userDTO.getUserName(), password);
                    }).switchIfEmpty(
                        Mono.defer(() -> {
                            return userRepository
                                .findOneByUserName(userDTO.getUserName())
                                .flatMap(existingUser -> {
                                    if (!existingUser.isActivated()) {
                                        return userRepository.delete(existingUser);
                                    } else {
                                        return Mono.error(new UsernameAlreadyUsedException());
                                    }
                                })
                                .publishOn(Schedulers.boundedElastic())
                                .then(
                                    Mono.fromCallable(() -> {
                                        User newUser = new User();
                                        newUser.setId(userDTO.getEmployeeId());
                                        newUser.setEmployeeId(userDTO.getEmployeeId());
                                        String encryptedPassword = passwordEncoder.encode(password);
                                        newUser.setUserName(userDTO.getUserName());
                                        // new user gets initially a generated password
                                        newUser.setPassword(encryptedPassword);
                                        newUser.setFirstName(userDTO.getFirstName());
                                        newUser.setLastName(userDTO.getLastName());
                                        if (userDTO.getEmail() != null) {
                                            newUser.setEmail(userDTO.getEmail());
                                        }
                                        newUser.setCompanyId(login.getCompanyId());
                                        newUser.setCompanyJson(Json.of("[\"" + login.getCompanyId() + "\"]"));
                                        newUser.setImageUrl(userDTO.getImageUrl());
                                        newUser.setLangKey(userDTO.getLangKey());
                                        // new user is not active
                                        newUser.setActivated(true);
                                        // new user gets registration key
                                        newUser.setActivationKey(RandomUtil.generateActivationKey());
                                        return newUser;
                                    }))
                                .flatMap(newUser -> {
                                    Set<Authority> authorities = new HashSet<>();
                                    return authorityRepository
                                        .findById(AuthoritiesConstants.USER)
                                        .map(authorities::add)
                                        .thenReturn(newUser)
                                        .doOnNext(user -> {
                                            sendUserStatusChange(user);
                                            user.setAuthorities(authorities);
                                        })
                                        .flatMap(user -> saveUser(user, true))
                                        .doOnNext(user -> log.debug("Created Information for User: {}", user));
                                });
                        })
                    );
        });

    }

    @Transactional
    public Mono<User> createUser(AdminUserDTO userDTO) {
        User user = new User();
        user.setEmployeeId(userDTO.getEmployeeId());
        user.setId(userDTO.getEmployeeId());
        user.setUserName(userDTO.getUserName());
        user.setFirstName(userDTO.getFirstName());
        user.setLastName(userDTO.getLastName());
        user.setSuperAdmin(Boolean.TRUE.equals(userDTO.getSuperAdmin()));
        if (userDTO.getEmail() != null) {
            user.setEmail(userDTO.getEmail());
        }
        user.setImageUrl(userDTO.getImageUrl());
        if (userDTO.getLangKey() == null) {
            user.setLangKey(Constants.DEFAULT_LANGUAGE); // default language
        } else {
            user.setLangKey(userDTO.getLangKey());
        }
        return Flux
            .fromIterable(userDTO.getAuthorities() != null ? userDTO.getAuthorities() : new HashSet<>())
            .flatMap(authorityRepository::findById)
            .doOnNext(authority -> user.getAuthorities().add(authority))
            .then(Mono.just(user))
            .publishOn(Schedulers.boundedElastic())
            .map(newUser -> {
                String encryptedPassword = passwordEncoder.encode(RandomUtil.generatePassword());
                newUser.setPassword(encryptedPassword);
                newUser.setResetKey(RandomUtil.generateResetKey());
                newUser.setResetDate(Instant.now());
                newUser.setActivated(true);
                sendUserStatusChange(newUser);
                return newUser;
            })
            .flatMap(this::saveUser)
            .doOnNext(user1 -> log.debug("Created Information for User: {}", user1));
    }

    /**
     * Update all information for a specific user, and return the modified user.
     *
     * @param userDTO user to update.
     * @return updated user.
     */
    @Transactional
    public Mono<AdminUserDTO> updateUser(AdminUserDTO userDTO) {
        return userRepository
            .findById(userDTO.getId())
            .flatMap(user -> {
                user.setUserName(userDTO.getUserName());
                user.setFirstName(userDTO.getFirstName());
                user.setLastName(userDTO.getLastName());
                if (userDTO.getEmail() != null) {
                    user.setEmail(userDTO.getEmail());
                }
                user.setImageUrl(userDTO.getImageUrl());
                user.setActivated(userDTO.isActivated());
                user.setLangKey(userDTO.getLangKey());
                Set<Authority> managedAuthorities = user.getAuthorities();
                managedAuthorities.clear();
                return userRepository
                    .deleteUserAuthorities(user.getId())
                    .thenMany(Flux.fromIterable(userDTO.getAuthorities()))
                    .flatMap(authorityRepository::findById)
                    .map(managedAuthorities::add)
                    .then(Mono.just(user));
            })
            .flatMap(this::saveUser)
            .doOnNext(user -> log.debug("Changed Information for User: {}", user))
            .map(AdminUserDTO::new);
    }

    public Mono<AdminUserDTO> updateUser(UUID id, AccountResource.UpdateAccountRequest request) {
        return userRepository
            .findById(id)
            .switchIfEmpty(Mono.error(new NotFoundException("User not found")))
            .flatMap(u -> {
                return userRepository.countByUserName(request.username(), u.getUserName())
                    .flatMap(count -> {
                        if (count > 0) {
                            return Mono.error(new BadRequestAlertException("Username already in use", "userManagement", "USERNAME_EXISTED"));
                        }
                        u.setUserName(request.username());
                        u.setPassword(passwordEncoder.encode(request.password()));
                        u.setIsPersisted();
                        return userRepository.save(u).map(AdminUserDTO::new);
                    });
            });
    }

    @Transactional(readOnly = true)
    public Mono<User> getUserWithAuthorities(UUID id) {
        return userRepository.findOneWithAuthoritiesById(id);
    }

    @Transactional
    public Mono<Void> deleteUser(String userName) {
        return userRepository
            .findOneByUserName(userName)
            .flatMap(user -> userRepository.delete(user).thenReturn(user))
            .doOnNext(user -> {
                user.setActivated(false);
                sendUserStatusChange(user);
                log.debug("Deleted User: {}", user);
            })
            .then();
    }

    /**
     * Update basic information (first name, last name, email, language) for the
     * current user.
     *
     * @param firstName first name of user.
     * @param lastName  last name of user.
     * @param email     email id of user.
     * @param langKey   language key.
     * @param imageUrl  image URL of user.
     * @return a completed {@link Mono}.
     */
    @Transactional
    public Mono<Void> updateUser(String firstName, String lastName, String email, String langKey, String imageUrl) {
        return com.carevn.masi.utils.SecurityUtils
            .getUserJWTDetail().map(UserJWTDetail::getUserId)
            .flatMap(userRepository::findById)
            .flatMap(user -> {
                user.setFirstName(firstName);
                user.setLastName(lastName);
                if (email != null) {
                    user.setEmail(email);
                }
                user.setLangKey(langKey);
                user.setImageUrl(imageUrl);
                return saveUser(user);
            })
            .doOnNext(user -> log.debug("Changed Information for User: {}", user))
            .then();
    }

    @Transactional
    public Mono<User> saveUser(User user, boolean isNew) {
        return com.carevn.masi.utils.SecurityUtils.getUserJWTDetail()
            .flatMap(login -> {
                if (user.getCreatedBy() == null) {
                    user.setCreatedBy(String.valueOf(login.getUserId()));
                }
//                user.setCompanyId(login.getCompanyId());
                user.setLastModifiedBy(String.valueOf(login.getUserId()));
                if (!isNew) {
                    user.setIsPersisted();
                }
                // Saving the relationship can be done in an entity callback
                // once https://github.com/spring-projects/spring-data-r2dbc/issues/215 is done
                return userRepository
                    .save(user)
                    .flatMap(savedUser -> {
                        if (CollectionUtils.isEmpty(savedUser.getAuthorities())) {
                            return Mono.just(savedUser);
                        }
                        sendUserStatusChange(savedUser);
                        return Flux
                            .fromIterable(user.getAuthorities())
                            .flatMap(authority -> userRepository.saveUserAuthority(savedUser.getId(),
                                authority.getName()))
                            .then(Mono.just(savedUser));
                    });
            })
            .doOnError(e -> log.error("Error saving user", e));
    }

    @Transactional
    public Mono<User> saveUser(User user) {
        return this.saveUser(user, false);
    }

    @Transactional
    public Mono<Void> changePassword(String currentClearTextPassword, String newPassword) {
        return SecurityUtils
            .getCurrentUserLogin()
            .flatMap(userRepository::findOneByUserName)
            .publishOn(Schedulers.boundedElastic())
            .map(user -> {
                String currentEncryptedPassword = user.getPassword();
                if (!passwordEncoder.matches(currentClearTextPassword, currentEncryptedPassword)) {
                    throw new InvalidPasswordException();
                }
                String encryptedPassword = passwordEncoder.encode(newPassword);
                user.setPassword(encryptedPassword);
                return user;
            })
            .flatMap(this::saveUser)
            .doOnNext(user -> log.debug("Changed password for User: {}", user))
            .then();
    }

    @Transactional(readOnly = true)
    public Flux<AdminUserDTO> getAllManagedUsers(Pageable pageable) {
        return userRepository.findAllWithAuthorities(pageable).map(AdminUserDTO::new);
    }

    @Transactional(readOnly = true)
    public Flux<UserDTO> getAllPublicUsers(Pageable pageable) {
        return userRepository.findAllByIdNotNullAndActivatedIsTrue(pageable).map(UserDTO::new);
    }

    @Transactional(readOnly = true)
    public Flux<UserDTO> getAllPublicUsers(Pageable pageable, PublicUserResource.UserQuery query) {
        var ids = query.ids();
        if (ids.isEmpty()) {
            return userRepository.findAllByIdNotNullAndActivatedIsTrue(pageable).map(UserDTO::new);
        }
        return userRepository.findAllByIdInAndActivatedIsTrue(pageable, query.ids()).map(UserDTO::new);
    }

    @Transactional(readOnly = true)
    public Mono<Long> countManagedUsers() {
        return userRepository.count();
    }

    @Transactional(readOnly = true)
    public Mono<User> getUserWithAuthoritiesByUserName(String userName) {
        return userRepository.findOneWithAuthoritiesByUserName(userName);
    }

    @Transactional(readOnly = true)
    public Mono<User> getUserWithAuthorities() {
        return com.carevn.masi.utils.SecurityUtils.getUserJWTDetail()
            .flatMap(uid -> {
                System.out.println("uid: " + uid);
                return Mono.just(uid.getUserId());
            })
            .flatMap(userRepository::findOneWithAuthoritiesById)
            .flatMap(user -> {
                return groupRepository.findAuthoritiesByGroupId(user.getId()).collectList().map(authorities -> {
                    user.addAuthority(authorities.stream().map(a -> {
                        Authority authority = new Authority();
                        authority.setName(a);
                        return authority;
                    }).collect(Collectors.toList()));
                    return user;
                }).then(Mono.just(user));
            })
            .flatMap(user -> companyRepository.findByIdentifier(user.getCompanyId()).map(company -> {
                user.setCompany(company);
                return user;
            }).then(Mono.just(user)));
    }

    @Transactional(readOnly = true)
    public Mono<User> getActivatedUserById(UUID id) {
        return userRepository.findByActivatedIsTrueAndId(id);
    }

    /**
     * Not activated users should be automatically deleted after 3 days.
     * <p>
     * This is scheduled to get fired everyday, at 01:00 (am).
     */
    @Scheduled(cron = "0 0 1 * * ?", zone = "Asia/Ho_Chi_Minh")
    public void removeNotActivatedUsers() {
        removeNotActivatedUsersReactively().blockLast();
    }

    @Transactional
    public Flux<User> removeNotActivatedUsersReactively() {
        return userRepository
            .findAllByActivatedIsFalseAndActivationKeyIsNotNullAndCreatedDateBefore(
                LocalDateTime.ofInstant(Instant.now().minus(3, ChronoUnit.DAYS), ZoneOffset.UTC))
            .flatMap(user -> userRepository.delete(user).thenReturn(user))
            .doOnNext(user -> log.debug("Deleted User: {}", user));
    }

    /**
     * Gets a list of all the authorities.
     *
     * @return a list of all the authorities.
     */
    @Transactional(readOnly = true)
    public Flux<String> getAuthorities() {
        return authorityRepository.findAll().map(Authority::getName);
    }

    public Flux<AccountStatus> checkAccount(List<UUID> userIds) {
        return userRepository.findAllById(userIds)
            .map(user -> {
                if (user.isActivated()) {
                    return AccountStatus.builder().id(user.getId()).status(AccountStatus.Status.ACTIVE).build();
                } else {
                    return AccountStatus.builder().id(user.getId()).status(AccountStatus.Status.INACTIVE).build();
                }
            });
    }

    @Transactional
    public Mono<User> updateUserCompanies(UUID userId, List<String> companies) {
        if (companies == null) {
            return Mono.error(new IllegalArgumentException("Companies list cannot be null"));
        }

        return userRepository.findById(userId)
            .switchIfEmpty(Mono.error(new NotFoundException("User not found")))
            .flatMap(user -> {
                try {
                    String jsonStr = new ObjectMapper().writeValueAsString(companies);
                    user.setCompanyJson(Json.of(jsonStr));
                    user.setIsPersisted();
                    return userRepository.save(user)
                        .flatMap(savedUser -> userRepository.findOneWithAuthoritiesById(savedUser.getId()));
                } catch (JsonProcessingException e) {
                    return Mono.error(new RuntimeException("Error processing JSON", e));
                }
            })
            .doOnSuccess(user -> log.debug("Updated companies for user: {}", user.getId()));
    }

    @Transactional
    public Mono<User> updateCurrentCompany(UUID userId, String companyId) {
        return userRepository.findById(userId)
            .switchIfEmpty(Mono.error(new BadRequestAlertException("User could not be found", "userManagement", "userNotFound")))
            .flatMap(user -> {
                if (user.getCompanyJson() == null) {
                    return Mono.error(new BadRequestAlertException(
                        "User has no companies assigned",
                        "userManagement",
                        "noCompanies"
                    ));
                }

                try {
                    List<String> userCompanies = new ObjectMapper().readValue(
                        user.getCompanyJson().asString(),
                        new TypeReference<List<String>>() {}
                    );

                    if (!userCompanies.contains(companyId)) {
                        return Mono.error(new BadRequestAlertException(
                            "Company not assigned to user",
                            "userManagement",
                            "invalidCompany"
                        ));
                    }

                    user.setCompanyId(companyId);
                    return saveUser(user);

                } catch (JsonProcessingException e) {
                    return Mono.error(new BadRequestAlertException(
                        "Invalid company data",
                        "userManagement",
                        "invalidData"
                    ));
                }
            })
            .doOnSuccess(user -> log.debug("Updated current company for user: {}", user.getId()));
    }
}
