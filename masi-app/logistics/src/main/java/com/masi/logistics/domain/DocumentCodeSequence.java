package com.masi.logistics.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.Serial;
import java.io.Serializable;
import java.util.UUID;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.domain.Persistable;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

/**
 * A DocumentCodeSequence.
 */
@Data
@Table("document_code_sequence")
@JsonIgnoreProperties(value = {"new"})
@SuppressWarnings("common-java:DuplicatedBlocks")
public class DocumentCodeSequence implements Serializable, Persistable<UUID> {

    @Serial
    private static final long serialVersionUID = 7239845540710584320L;

    @Id
    @Column("id")
    private UUID id;

    @Column("company")
    private String company;

    @Column("current_sequence")
    private Integer currentSequence;

    @Column("java_format")
    private String javaFormat;

    @Column("document_type")
    private String documentType;

    @Transient
    private boolean isPersisted;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public DocumentCodeSequence id(UUID id) {
        this.setId(id);
        return this;
    }

    public DocumentCodeSequence company(String company) {
        this.setCompany(company);
        return this;
    }

    public DocumentCodeSequence currentSequence(Integer currentSequence) {
        this.setCurrentSequence(currentSequence);
        return this;
    }

    public DocumentCodeSequence javaFormat(String javaFormat) {
        this.setJavaFormat(javaFormat);
        return this;
    }

    public DocumentCodeSequence documentType(String documentType) {
        this.setDocumentType(documentType);
        return this;
    }

    @Transient
    @Override
    public boolean isNew() {
        return !this.isPersisted;
    }

    public DocumentCodeSequence setIsPersisted() {
        this.isPersisted = true;
        return this;
    }

    public String getNextAndIncrement() {
        this.setIsPersisted();
        return String.format(javaFormat, currentSequence++);
    }

    public String getNext() {
        return String.format(javaFormat, currentSequence);
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

}
