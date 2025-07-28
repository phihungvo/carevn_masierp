package com.carevn.masi.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.NotNull;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.domain.Persistable;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@EqualsAndHashCode(callSuper = true)
@Data
@Table("masi_group")
public class Group extends AbstractAuditingEntity<UUID> implements Persistable<UUID> {

    @Id
    private UUID id = UUID.randomUUID();

    @Column("name")
    private String name="";

    @Column("description")
    private String description;

    @NotNull
    @NotNull
    @Column("activated")
    private boolean activated = false;

    @Column("workspace_id")
    private String workspaceId;

    @Column("normalized_name")
    private String normalizedName = "";

    @Column("company_id")
    private String companyId;

    @Transient
    private Set<User> users = new HashSet<>();

    @Transient
    private Set<Authority> authorities = new HashSet<>();

    @Transient
    private boolean isPersisted;

    public Group setIsPersisted() {
        this.isPersisted = true;
        return this;
    }
    @Transient
    @Override
    public boolean isNew() {
        return !this.isPersisted;
    }

    public Group() {
        // Empty constructor needed for Jackson.
    }


    public Group(UUID id, String name, String description, boolean activated, String workspaceId) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.activated = activated;
        this.workspaceId = workspaceId;
    }


}
