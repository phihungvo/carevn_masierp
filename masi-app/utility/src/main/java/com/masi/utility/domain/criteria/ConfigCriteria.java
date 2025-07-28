package com.masi.utility.domain.criteria;

import com.masi.utility.domain.enumeration.DataType;
import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.masi.utility.domain.Config} entity. This class is used
 * in {@link com.masi.utility.web.rest.ConfigResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /configs?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ConfigCriteria implements Serializable, Criteria {

    /**
     * Class for filtering DataType
     */
    public static class DataTypeFilter extends Filter<DataType> {

        public DataTypeFilter() {}

        public DataTypeFilter(DataTypeFilter filter) {
            super(filter);
        }

        @Override
        public DataTypeFilter copy() {
            return new DataTypeFilter(this);
        }
    }

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter key;

    private StringFilter description;

    private StringFilter value;

    private DataTypeFilter type;

    private ZonedDateTimeFilter updatedAt;

    private StringFilter updatedBy;

    private StringFilter company;

    private Boolean distinct;

    public ConfigCriteria() {}

    public ConfigCriteria(ConfigCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.key = other.optionalKey().map(StringFilter::copy).orElse(null);
        this.description = other.optionalDescription().map(StringFilter::copy).orElse(null);
        this.value = other.optionalValue().map(StringFilter::copy).orElse(null);
        this.type = other.optionalType().map(DataTypeFilter::copy).orElse(null);
        this.updatedAt = other.optionalUpdatedAt().map(ZonedDateTimeFilter::copy).orElse(null);
        this.updatedBy = other.optionalUpdatedBy().map(StringFilter::copy).orElse(null);
        this.company = other.optionalCompany().map(StringFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public ConfigCriteria copy() {
        return new ConfigCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public Optional<LongFilter> optionalId() {
        return Optional.ofNullable(id);
    }

    public LongFilter id() {
        if (id == null) {
            setId(new LongFilter());
        }
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getKey() {
        return key;
    }

    public Optional<StringFilter> optionalKey() {
        return Optional.ofNullable(key);
    }

    public StringFilter key() {
        if (key == null) {
            setKey(new StringFilter());
        }
        return key;
    }

    public void setKey(StringFilter key) {
        this.key = key;
    }

    public StringFilter getDescription() {
        return description;
    }

    public Optional<StringFilter> optionalDescription() {
        return Optional.ofNullable(description);
    }

    public StringFilter description() {
        if (description == null) {
            setDescription(new StringFilter());
        }
        return description;
    }

    public void setDescription(StringFilter description) {
        this.description = description;
    }

    public StringFilter getValue() {
        return value;
    }

    public Optional<StringFilter> optionalValue() {
        return Optional.ofNullable(value);
    }

    public StringFilter value() {
        if (value == null) {
            setValue(new StringFilter());
        }
        return value;
    }

    public void setValue(StringFilter value) {
        this.value = value;
    }

    public DataTypeFilter getType() {
        return type;
    }

    public Optional<DataTypeFilter> optionalType() {
        return Optional.ofNullable(type);
    }

    public DataTypeFilter type() {
        if (type == null) {
            setType(new DataTypeFilter());
        }
        return type;
    }

    public void setType(DataTypeFilter type) {
        this.type = type;
    }

    public ZonedDateTimeFilter getUpdatedAt() {
        return updatedAt;
    }

    public Optional<ZonedDateTimeFilter> optionalUpdatedAt() {
        return Optional.ofNullable(updatedAt);
    }

    public ZonedDateTimeFilter updatedAt() {
        if (updatedAt == null) {
            setUpdatedAt(new ZonedDateTimeFilter());
        }
        return updatedAt;
    }

    public void setUpdatedAt(ZonedDateTimeFilter updatedAt) {
        this.updatedAt = updatedAt;
    }

    public StringFilter getUpdatedBy() {
        return updatedBy;
    }

    public Optional<StringFilter> optionalUpdatedBy() {
        return Optional.ofNullable(updatedBy);
    }

    public StringFilter updatedBy() {
        if (updatedBy == null) {
            setUpdatedBy(new StringFilter());
        }
        return updatedBy;
    }

    public void setUpdatedBy(StringFilter updatedBy) {
        this.updatedBy = updatedBy;
    }

    public StringFilter getCompany() {
        return company;
    }

    public Optional<StringFilter> optionalCompany() {
        return Optional.ofNullable(company);
    }

    public StringFilter company() {
        if (company == null) {
            setCompany(new StringFilter());
        }
        return company;
    }

    public void setCompany(StringFilter company) {
        this.company = company;
    }

    public Boolean getDistinct() {
        return distinct;
    }

    public Optional<Boolean> optionalDistinct() {
        return Optional.ofNullable(distinct);
    }

    public Boolean distinct() {
        if (distinct == null) {
            setDistinct(true);
        }
        return distinct;
    }

    public void setDistinct(Boolean distinct) {
        this.distinct = distinct;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final ConfigCriteria that = (ConfigCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(key, that.key) &&
            Objects.equals(description, that.description) &&
            Objects.equals(value, that.value) &&
            Objects.equals(type, that.type) &&
            Objects.equals(updatedAt, that.updatedAt) &&
            Objects.equals(updatedBy, that.updatedBy) &&
            Objects.equals(company, that.company) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, key, description, value, type, updatedAt, updatedBy, company, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ConfigCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalKey().map(f -> "key=" + f + ", ").orElse("") +
            optionalDescription().map(f -> "description=" + f + ", ").orElse("") +
            optionalValue().map(f -> "value=" + f + ", ").orElse("") +
            optionalType().map(f -> "type=" + f + ", ").orElse("") +
            optionalUpdatedAt().map(f -> "updatedAt=" + f + ", ").orElse("") +
            optionalUpdatedBy().map(f -> "updatedBy=" + f + ", ").orElse("") +
            optionalCompany().map(f -> "company=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
