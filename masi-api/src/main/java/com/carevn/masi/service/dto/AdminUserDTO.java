package com.carevn.masi.service.dto;

import com.carevn.masi.config.Constants;
import com.carevn.masi.domain.Authority;
import com.carevn.masi.domain.User;
import com.carevn.masi.utils.PgJsonObjectDeserializer;
import com.carevn.masi.utils.PgJsonObjectSerializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.r2dbc.postgresql.codec.Json;
import jakarta.validation.constraints.*;
import lombok.*;

import java.io.Serializable;
import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

/**
 * A DTO representing a user, with his authorities.
 */
@Data
public class AdminUserDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private UUID id;

    @NotBlank
    @Pattern(regexp = Constants.LOGIN_REGEX)
    @Size(min = 1, max = 50)
    private String userName;

    @Size(max = 50)
    private String firstName;

    @Size(max = 50)
    private String lastName;

    @Email
    @Size(min = 5, max = 254)
    private String email;

    private WorkspaceDTO workspace;

    @Size(max = 256)
    private String imageUrl;

    private boolean activated = false;

    @Size(min = 2, max = 10)
    private String langKey;

    private String createdBy;

    private Instant createdDate;

    private String lastModifiedBy;

    private Instant lastModifiedDate;

    private Set<String> authorities;

    private Boolean isSuperAdmin;

    private CompanyDTO company;
    private String companyId;

    @JsonSerialize(using = PgJsonObjectSerializer.class)
    @JsonDeserialize(using = PgJsonObjectDeserializer.class)
    private Json companyJson;

    @NotNull(message = "Employee ID is required")
    private UUID employeeId;

    private String signatureId;

    private String signatureContentType;

    private String signatureFileName;

    private List<CompanyDTO> companies;

    public AdminUserDTO() {
        // Empty constructor needed for Jackson.
    }

    public AdminUserDTO(User user) {
        this.id = user.getId();
        this.userName = user.getUserName();
        this.firstName = user.getFirstName();
        this.lastName = user.getLastName();
        this.email = user.getEmail();
        this.activated = user.isActivated();
        this.imageUrl = user.getImageUrl();
        this.langKey = user.getLangKey();
        this.createdBy = user.getCreatedBy();
        this.createdDate = user.getCreatedDate();
        this.lastModifiedBy = user.getLastModifiedBy();
        this.lastModifiedDate = user.getLastModifiedDate();
        this.authorities = user.getAuthorities().stream().map(Authority::getName).collect(Collectors.toSet());
        this.isSuperAdmin = user.getSuperAdmin();
        this.employeeId = user.getEmployeeId();
        this.companyId = user.getCompanyId();
        this.signatureId = user.getSignatureId();
        this.signatureFileName = user.getSignatureFileName();
        this.companyJson = user.getCompanyJson();
        if (Objects.nonNull(user.getCompany())) {
            this.company = new CompanyDTO();
            this.company.setNormalizedName(user.getCompanyId());
            this.company.setName(user.getCompany().getName());
            this.company.setId(user.getCompany().getId());
        }
        this.companies = user.getCompanies();
    }


    public Boolean getSuperAdmin() {
        return isSuperAdmin;
    }

    public void setSuperAdmin(Boolean superAdmin) {
        isSuperAdmin = superAdmin;
    }

}
