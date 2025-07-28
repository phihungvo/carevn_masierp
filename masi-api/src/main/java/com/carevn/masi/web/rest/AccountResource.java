package com.carevn.masi.web.rest;

import com.carevn.masi.dto.UserJWTDetail;
import com.carevn.masi.repository.AuthorityRepository;
import com.carevn.masi.repository.CompanyRepository;
import com.carevn.masi.repository.UserRepository;
import com.carevn.masi.service.MailService;
import com.carevn.masi.service.UserService;
import com.carevn.masi.service.dto.AccountStatus;
import com.carevn.masi.service.dto.AddSignatureDTO;
import com.carevn.masi.service.dto.AdminUserDTO;
import com.carevn.masi.service.dto.PasswordChangeDTO;
import com.carevn.masi.service.mapper.CompanyMapper;
import com.carevn.masi.service.web.client.EmployeeClient;
import com.carevn.masi.service.web.client.EmployeeDTO;
import com.carevn.masi.utils.SecurityUtils;
import com.carevn.masi.web.rest.errors.*;
import com.carevn.masi.web.rest.vm.KeyAndPasswordVM;
import com.carevn.masi.web.rest.vm.ManagedUserVM;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nimbusds.jose.jwk.source.ImmutableSecret;
import com.nimbusds.jose.util.Base64;
import jakarta.validation.Valid;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.oauth2.jwt.*;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import static com.carevn.masi.security.SecurityUtils.*;

/**
 * REST controller for managing the current user's account.
 */
@RestController
@RequestMapping("/api")
public class AccountResource {

    private final CompanyRepository companyRepository;
    private final CompanyMapper companyMapper;
    private final AuthorityRepository authorityRepository;
    @Value("${jhipster.security.authentication.jwt.token-validity-in-seconds-for-remember-me:0}")
    private long tokenValidityInSecondsForRememberMe;
    private final JwtEncoder jwtEncoder;
    @Value("${jhipster.security.authentication.jwt.token-validity-in-seconds:0}")
    private long tokenValidityInSeconds;

    public static class AccountResourceException extends RuntimeException {

        private AccountResourceException(String message) {
            super(message);
        }
    }

    private final Logger log = LoggerFactory.getLogger(AccountResource.class);

    private final UserRepository userRepository;

    private final UserService userService;

    private final MailService mailService;
    private final EmployeeClient employeeClient;

    private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!@#$%^&*()-_+=<>?";
    private static final Random RANDOM = new Random();

    @Value("${jhipster.security.authentication.jwt.base64-secret}")
    private String jwtKey;

    private JwtEncoder jwtEncoder(String jwtKey) {
        return new NimbusJwtEncoder(new ImmutableSecret<>(getSecretKey(jwtKey)));
    }

    private SecretKey getSecretKey(String jwtKey) {
        byte[] keyBytes = Base64.from(jwtKey).decode();
        return new SecretKeySpec(keyBytes, 0, keyBytes.length, com.carevn.masi.utils.SecurityUtils.JWT_ALGORITHM.getName());
    }

    private String genToken(UUID userId, Set<String> authorities, String company) {
        JwtEncoder encoder = jwtEncoder(jwtKey);

        var now = Instant.now();

        JwtClaimsSet claims = JwtClaimsSet
            .builder()
            .issuedAt(now)
            .expiresAt(now.plusSeconds(60 * 60 * 24))
            .subject(userId.toString())
            .claims(customClaim -> customClaim.put(AUTHORITIES_KEY, authorities))
            .claim("company", company)
            .build();
        JwsHeader jwsHeader = JwsHeader.with(com.carevn.masi.utils.SecurityUtils.JWT_ALGORITHM).build();
        return encoder.encode(JwtEncoderParameters.from(jwsHeader, claims)).getTokenValue();
    }


    public AccountResource(UserRepository userRepository, UserService userService, MailService mailService, EmployeeClient employeeClient, CompanyRepository companyRepository, CompanyMapper companyMapper, JwtEncoder jwtEncoder, AuthorityRepository authorityRepository) {
        this.userRepository = userRepository;
        this.userService = userService;
        this.mailService = mailService;
        this.employeeClient = employeeClient;
        this.companyRepository = companyRepository;
        this.companyMapper = companyMapper;
        this.jwtEncoder = jwtEncoder;
        this.authorityRepository = authorityRepository;
    }

    @PostMapping("/account/check")
    public Mono<List<AccountStatus>> checkAccount(@RequestBody List<UUID> userIds) {
        return userService.checkAccount(userIds).collectList();
    }

    @PostMapping("/account/update-role/{id}/{role}")
    public Mono<Void> checkAccount(@PathVariable UUID id, @PathVariable String role) {
        log.debug("Request to update role for user ID: {}, role: {}", id, role);
        if (role == null || role.trim().isEmpty()) {
            return Mono.error(new BadRequestAlertException("Role is required", "userManagement", "roleIsRequired"));
        }
        return userRepository.findAllMasiAuthorityByRole(role)
                .switchIfEmpty(Mono.error(new BadRequestAlertException("Role not found", "userManagement", "roleNotFound")))
                .flatMap(count -> {
                    if (count <= 0) {
                        return Mono.error(new BadRequestAlertException("Role is invalid", "userManagement", "roleIsInvalid"));
                    }
                    return userRepository.deleteUserAuthorities(id)
                            .then(userRepository.saveUserAuthority(id, role))
                            .doOnSuccess(unused -> log.info("Successfully updated role for user ID: {}", id))
                            .doOnError(e -> log.error("Failed to update role for user ID: {}", id, e));
                })
                .onErrorResume(e -> {
                    log.error("Error occurred during role update process", e);
                    return Mono.error(e);
                });
    }



    /**
     * {@code POST  /register} : register the user.
     *
     * @param managedUserVM the managed user View Model.
     * @throws InvalidPasswordException  {@code 400 (Bad Request)} if the password is incorrect.
     * @throws EmailAlreadyUsedException {@code 400 (Bad Request)} if the email is already used.
     * @throws LoginAlreadyUsedException {@code 400 (Bad Request)} if the login is already used.
     */
    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<AdminUserDTO> registerAccount(@Valid @RequestBody ManagedUserVM managedUserVM) {
        // create a temp password
        managedUserVM.setPassword(generateValidPassword());
        if (isPasswordLengthInvalid(managedUserVM.getPassword())) {
            throw new InvalidPasswordException();
        }
//        /.doOnSuccess(mailService::sendActivationEmail)
        return userService.registerUser(managedUserVM, managedUserVM.getPassword()).map(AdminUserDTO::new);
    }
    @GetMapping("/account/{id}")
    public Mono<AdminUserDTO> getAccount(@PathVariable UUID id) {
        return userService
            .getUserWithAuthorities(id)
            .map(AdminUserDTO::new);

    }

    @DeleteMapping("/account/{id}/toggle-active")
    public Mono<ResponseEntity<Void>> toggleActive(@PathVariable UUID id) {
        return userService.toggleUserActivation(id).thenReturn(ResponseEntity.noContent().build());
    }

    public record UpdateAccountRequest(
        @NotNull
        String username,
        @Size(min = ManagedUserVM.PASSWORD_MIN_LENGTH, max = ManagedUserVM.PASSWORD_MAX_LENGTH)
        String password,
        @Size(min = ManagedUserVM.PASSWORD_MIN_LENGTH, max = ManagedUserVM.PASSWORD_MAX_LENGTH)
        String repeatPassword
    ) {}

    @PatchMapping("/account/update/{id}")
    public Mono<AdminUserDTO> updateAccount(@PathVariable UUID id, @Valid @RequestBody UpdateAccountRequest updateAccountRequest) {
        if (!updateAccountRequest.password().equals(updateAccountRequest.repeatPassword())) {
            return Mono.error(new BadRequestAlertException("Passwords do not match", "userManagement", "passwordsDoNotMatch"));
        }
        return userService.updateUser(id, updateAccountRequest);
    }

    @GetMapping("/account/username/count/{id}")
    public Mono<Long> countByUserName(@PathVariable UUID id, @RequestParam String username) {
        return userRepository.findById(id).flatMap(user -> {
            return userRepository.countByUserName(username, user.getUserName())
                .flatMap(Mono::just);
        });
    }

    /**
     * {@code GET  /activate} : activate the registered user.
     *
     * @param key the activation key.
     * @throws RuntimeException {@code 500 (Internal Server Error)} if the user couldn't be activated.
     */
    @GetMapping("/activate")
    public Mono<Void> activateAccount(@RequestParam(value = "key") String key) {
        return userService
            .activateRegistration(key)
            .switchIfEmpty(Mono.error(new AccountResourceException("No user was found for this activation key")))
            .then();
    }

    /**
     * {@code GET  /account} : get the current user.
     *
     * @return the current user.
     * @throws RuntimeException {@code 500 (Internal Server Error)} if the user couldn't be returned.
     */
    @GetMapping("/account")
    public Mono<AdminUserDTO> getAccount() {
        return
            userService
                .getUserWithAuthorities()
                .doOnError(e -> log.error("Error getting user", e))
                .map(AdminUserDTO::new)
                .flatMap(user -> {
                    if (Objects.nonNull(user.getEmployeeId())) {
                        return employeeClient.getEmployee(user.getEmployeeId())
                            .map(employeeDTO -> {
                                user.setWorkspace(employeeDTO.getWorkspace());
                                return user;
                            })
                            .onErrorReturn(user)
                            .then(Mono.just(user));
                    }
                    return Mono.just(user);
                })
                .flatMap(user -> {
                    if (user.getCompanyJson() != null) {
                        try {
                            List<String> companyIds = new ObjectMapper().readValue(
                                user.getCompanyJson().asString(),
                                new TypeReference<List<String>>() {}
                            );
                            if (companyIds.isEmpty()){
                                companyIds.add(user.getCompanyId());
                            }
                            return companyRepository.findAllByNormalizedName(companyIds)
                                .map(companyMapper::toDto)
                                .collectList()
                                .map(companies -> {
                                    if (companies == null)
                                        return user;
                                    user.setCompanies(companies);
                                    return user;
                                });
                        } catch (JsonProcessingException e) {
                            return Mono.just(user);
                        }
                    }
                    return Mono.just(user);
                })
                .switchIfEmpty(Mono.error(new AccountResourceException("User could not be found")));
    }

    /**
     * {@code POST  /account} : update the current user information.
     *
     * @param userDTO the current user information.
     * @throws EmailAlreadyUsedException {@code 400 (Bad Request)} if the email is already used.
     * @throws RuntimeException          {@code 500 (Internal Server Error)} if the user login wasn't found.
     */
    @PostMapping("/account")
    public Mono<Void> saveAccount(@Valid @RequestBody AdminUserDTO userDTO) {
        return SecurityUtils.getUserJWTDetail()
            .map(UserJWTDetail::getUserId)
            .switchIfEmpty(Mono.error(new AccountResourceException("Current user login not found")))
            .flatMap(userLogin ->
                userRepository
                    .findOneByEmailIgnoreCase(userDTO.getEmail())
                    .filter(existingUser -> {
                        assert existingUser.getId() != null;
                        return !existingUser.getId().equals(userLogin);
                    })
                    .hasElement()
                    .flatMap(emailExists -> {
                        if (emailExists) {
                            return Mono.error(new EmailAlreadyUsedException());
                        }
                        return userRepository.findById(userLogin);
                    })
            )
            .switchIfEmpty(Mono.error(new AccountResourceException("User could not be found")))
            .flatMap(user ->
                {
                    var addSignatureDTO = new AddSignatureDTO();
                    addSignatureDTO.setSignatureId(userDTO.getSignatureId());
                    addSignatureDTO.setSignatureName(userDTO.getSignatureFileName());
                    return userService.updateMySignature(addSignatureDTO).then(userService.updateUser(
                        userDTO.getFirstName(),
                        userDTO.getLastName(),
                        userDTO.getEmail(),
                        userDTO.getLangKey(),
                        userDTO.getImageUrl()
                    ));
                }
            );
    }

    /**
     * {@code POST  /account/change-password} : changes the current user's password.
     *
     * @param passwordChangeDto current and new password.
     * @throws InvalidPasswordException {@code 400 (Bad Request)} if the new password is incorrect.
     */
    @PostMapping(path = "/account/change-password")
    public Mono<Void> changePassword(@RequestBody PasswordChangeDTO passwordChangeDto) {
        if (isPasswordLengthInvalid(passwordChangeDto.getNewPassword())) {
            throw new InvalidPasswordException();
        }
        return userService.changePassword(passwordChangeDto.getCurrentPassword(), passwordChangeDto.getNewPassword());
    }

    /**
     * {@code POST   /account/reset-password/init} : Send an email to reset the password of the user.
     *
     * @param mail the mail of the user.
     */
    @PostMapping(path = "/account/reset-password/init")
    public Mono<Void> requestPasswordReset(@RequestBody String mail) {
        return userService
            .requestPasswordReset(mail)
            .doOnSuccess(user -> {
                if (Objects.nonNull(user)) {
                    mailService.sendPasswordResetMail(user);
                } else {
                    // Pretend the request has been successful to prevent checking which emails really exist
                    // but log that an invalid attempt has been made
                    log.warn("Password reset requested for non existing mail");
                }
            })
            .then();
    }

    /**
     * {@code POST   /account/reset-password/finish} : Finish to reset the password of the user.
     *
     * @param keyAndPassword the generated key and the new password.
     * @throws InvalidPasswordException {@code 400 (Bad Request)} if the password is incorrect.
     * @throws RuntimeException         {@code 500 (Internal Server Error)} if the password could not be reset.
     */
    @PostMapping(path = "/account/reset-password/finish")
    public Mono<Void> finishPasswordReset(@RequestBody KeyAndPasswordVM keyAndPassword) {
        if (isPasswordLengthInvalid(keyAndPassword.getNewPassword())) {
            throw new InvalidPasswordException();
        }
        return userService
            .completePasswordReset(keyAndPassword.getNewPassword(), keyAndPassword.getKey())
            .switchIfEmpty(Mono.error(new AccountResourceException("No user was found for this reset key")))
            .then();
    }

    private static boolean isPasswordLengthInvalid(String password) {
        return (
            StringUtils.isEmpty(password) ||
                password.length() < ManagedUserVM.PASSWORD_MIN_LENGTH ||
                password.length() > ManagedUserVM.PASSWORD_MAX_LENGTH
        );
    }



    public static String generateValidPassword() {

        int passwordLength = RANDOM.nextInt(ManagedUserVM.PASSWORD_MAX_LENGTH - ManagedUserVM.PASSWORD_MIN_LENGTH + 1) + ManagedUserVM.PASSWORD_MIN_LENGTH;
        StringBuilder password = new StringBuilder(passwordLength);

        for (int i = 0; i < passwordLength; i++) {
            int index = RANDOM.nextInt(CHARACTERS.length());
            password.append(CHARACTERS.charAt(index));
        }

        return password.toString();
    }

    @PatchMapping("/account/update-companies")
    public Mono<ResponseEntity<AdminUserDTO>> updateCompanies(@Valid @RequestBody List<String> companies) {
        return SecurityUtils.getUserJWTDetail()
            .map(UserJWTDetail::getUserId)
            .switchIfEmpty(Mono.error(new AccountResourceException("Current user login not found")))
            .flatMap(userId -> userService.updateUserCompanies(userId, companies))
            .map(user -> ResponseEntity.ok().body(new AdminUserDTO(user)))
            .doOnError(e -> log.error("Error updating companies: ", e));
    }

    public record UpdateCurrentCompanyRequest(
        @NotNull
        String companyId
    ) {}

    @PatchMapping("/account/{id}/update-companies")
    public Mono<ResponseEntity<AdminUserDTO>> updateCompaniesForUser(@Valid @RequestBody List<String> companies, @PathVariable UUID id) {
        return SecurityUtils.getUserJWTDetail()
            .switchIfEmpty(Mono.error(new AccountResourceException("Current user login not found")))
            .flatMap(userId -> userService.updateUserCompanies(id, companies))
            .map(user -> ResponseEntity.ok().body(new AdminUserDTO(user)))
            .doOnError(e -> log.error("Error updating companies: ", e));
    }


//    private Mono<String> getJwtToken() {
//        return ReactiveSecurityContextHolder.getContext()
//            .map(SecurityContext::getAuthentication)
//            .map(authentication -> {
//                if (authentication instanceof JwtAuthenticationToken authenticationToken) {
//                    return authenticationToken.getToken().getTokenValue();
//                }
//                return genToken();
//            })
//            .onErrorReturn(genToken())
//            .switchIfEmpty(Mono.just(genToken()));
//    }

    @PatchMapping("/account/update-current-company")
    public Mono<ResponseEntity<AuthenticateController.JWTToken>> updateCurrentCompany(@Valid @RequestBody UpdateCurrentCompanyRequest request) {
    return SecurityUtils.getUserJWTDetail()
        .map(UserJWTDetail::getUserId)
        .switchIfEmpty(Mono.error(new AccountResourceException("Current user login not found")))
        .flatMap(userId -> userService.updateCurrentCompany(userId, request.companyId))
        .map(AdminUserDTO::new)
        .flatMap(a -> {
            return userService.getUserWithAuthorities(a.getId()).map(AdminUserDTO::new).flatMap(user -> {
                Authentication authentication = new UsernamePasswordAuthenticationToken(user.getId(), null, user.getAuthorities().stream().map(SimpleGrantedAuthority::new).collect(Collectors.toSet()));
                Map<String, Object> otherClaims = new HashMap<>();
                otherClaims.put(COMPANY_CLAIM_KEY, user.getCompanyId());

                if (Objects.nonNull(user.getEmployeeId())) {
                    return employeeClient.getEmployee(user.getEmployeeId())
                        .defaultIfEmpty(new EmployeeDTO())
                        .flatMap(employeeDTO -> {
                            var token = "";
                            user.setWorkspace(employeeDTO.getWorkspace());
                            if (Objects.nonNull(user.getWorkspace())) {
                                otherClaims.put(GROUP_CLAIM_KEY, String.valueOf(user.getWorkspace().getNormalizedName()));
                                otherClaims.put(GROUP_ID_CLAIM_KEY, user.getWorkspace().getId());
                            }
                            token = createToken(authentication, true, otherClaims);
                            var jwtToken2 = new AuthenticateController.JWTToken(token, user.getAuthorities());
                            HttpHeaders httpHeaders = new HttpHeaders();
                            httpHeaders.setBearerAuth(token);
                            return Mono.just(new ResponseEntity<>(jwtToken2, httpHeaders, HttpStatus.OK));
                        });
                }
                else{
                    var token = createToken(authentication, false, otherClaims);
                    var jwtToken2 = new AuthenticateController.JWTToken(token, user.getAuthorities());
                    HttpHeaders httpHeaders = new HttpHeaders();
                    httpHeaders.setBearerAuth(token);
                    return Mono.just(new ResponseEntity<>(jwtToken2, httpHeaders, HttpStatus.OK));
                }
            });
        })
        .doOnError(e -> log.error("Error updating current company: ", e));
    }

    public String createToken(Authentication authentication, boolean rememberMe, Map<String, Object> otherClaims) {
        String authorities = authentication.getAuthorities().stream()
            .map(GrantedAuthority::getAuthority)
            .filter(x -> x.startsWith("ROLE_"))
            .collect(Collectors.joining(" "));
        Instant now = Instant.now();
        Instant validity;
        validity = now.plus(this.tokenValidityInSeconds, ChronoUnit.SECONDS);


        // @formatter:off
        JwtClaimsSet claims = JwtClaimsSet.builder()
            .issuedAt(now)
            .expiresAt(validity)
            .subject(authentication.getName())
            .claim(AUTHORITIES_KEY, authorities)
            .claims(stringObjectMap-> stringObjectMap.putAll(otherClaims))
            .build();

        JwsHeader jwsHeader = JwsHeader.with(JWT_ALGORITHM).build();
        return this.jwtEncoder.encode(JwtEncoderParameters.from(jwsHeader, claims)).getTokenValue();
    }

}
