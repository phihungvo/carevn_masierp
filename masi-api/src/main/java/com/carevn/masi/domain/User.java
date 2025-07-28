package com.carevn.masi.domain;

import com.carevn.masi.config.Constants;
import com.carevn.masi.service.dto.CompanyDTO;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.r2dbc.postgresql.codec.Json;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.io.Serializable;
import java.time.Instant;
import java.util.*;

import lombok.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.domain.Persistable;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

/**
 * A user.
 */
@Data
@Table("masi_user")
public class User extends AbstractAuditingEntity<UUID> implements Serializable, Persistable<UUID> {

    private static final long serialVersionUID = 1L;

    @Id
    private UUID id;

    @NotNull
    @Pattern(regexp = Constants.LOGIN_REGEX)
    @Size(min = 1, max = 50)
    @Column("user_name")
    private String userName;

    @JsonIgnore
    @NotNull
    @Size(min = 60, max = 60)
    @Column("password_hash")
    private String password;

    @Size(max = 50)
    @Column("first_name")
    private String firstName;

    @Size(max = 50)
    @Column("last_name")
    private String lastName;

    @Email
    @Size(min = 5, max = 254)
    private String email;

    @NotNull
    private boolean activated = false;

    @Size(min = 2, max = 10)
    @Column("lang_key")
    private String langKey;

    @Size(max = 256)
    @Column("image_url")
    private String imageUrl;

    @Size(max = 20)
    @Column("activation_key")
    @JsonIgnore
    private String activationKey;

    @Size(max = 20)
    @Column("reset_key")
    @JsonIgnore
    private String resetKey;

    @Column("reset_date")
    private Instant resetDate = null;

    @Column("is_super_admin")
    private Boolean isSuperAdmin = false;

    @Column("employee_id")
    private UUID employeeId;

    @JsonIgnore
    @Transient
    private Set<Authority> authorities = new HashSet<>();

    public void addAuthority(Collection<Authority> authorities) {
        if (this.authorities == null) {
            this.authorities = new HashSet<>();
        }
        this.authorities.addAll(authorities);
    }

    @JsonIgnore
    @Transient
    private Set<Group> groups = new HashSet<>();

    @Column("company_id")
    private String companyId;

    @Transient
    private Company company;

    @Column("company_json")
    private Json companyJson;

    @Column("signature_id")
    private String signatureId;

    @Column("signature_file_name")
    private String signatureFileName;

    @Transient
    private List<CompanyDTO> companies;

    private boolean isPersisted() {
        return id != null;
    }

    @Transient
    private boolean isPersisted;

    @Transient
    @Override
    public boolean isNew() {
        return !this.isPersisted;
    }

    public User setIsPersisted() {
        this.isPersisted = true;
        return this;
    }

    public String getFullName() {
        return lastName + " " + firstName;
    }

    public Boolean getSuperAdmin() {
        return isSuperAdmin;
    }

    public void setSuperAdmin(Boolean superAdmin) {
        isSuperAdmin = superAdmin;
    }

    public static long getSerialversionuid() {
        return serialVersionUID;
    }

    public User toBrief() {
        var user = new User();
        user.setId(id);
        user.setFirstName(firstName);
        user.setLastName(lastName);
        return user;
    }

}
