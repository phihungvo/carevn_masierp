package com.carevn.masi.domain;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.io.Serializable;
import java.util.Objects;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.domain.Persistable;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

/**
 * An authority (a security role) used by Spring Security.
 */
@Data
@Table("masi_authority")
@Builder
@AllArgsConstructor
public class Authority implements Serializable, Persistable<String> {

    private static final long serialVersionUID = 1L;

    @NotNull
    @Size(max = 50)
    @Id
    private String name;

    @Column("description")
    private String description;

    @Column("action")
    private String action;

    @Column("resource")
    private String resource;

    public Authority() {
        // Empty constructor needed for Jackson.
    }

    public Authority(String name) {
        this.name = name;
    }

    @Override
    public String getId() {
        return name;
    }

    @Override
    public boolean isNew() {
        return true;
    }

    public static long getSerialversionuid() {
        return serialVersionUID;
    }

}
