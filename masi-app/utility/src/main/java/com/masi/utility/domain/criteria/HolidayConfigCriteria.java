package com.masi.utility.domain.criteria;

import com.masi.utility.domain.enumeration.CalenderType;
import com.masi.utility.domain.enumeration.HolidayType;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Date;
import java.util.Objects;
import java.util.Optional;

import lombok.*;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.masi.utility.domain.HolidayConfig} entity. This class is used
 * in {@link com.masi.utility.web.rest.HolidayConfigResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /holiday-configs?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@Data
@ParameterObject
@EqualsAndHashCode
@SuppressWarnings("common-java:DuplicatedBlocks")
public class HolidayConfigCriteria implements Serializable, Criteria {

    /**
     * Class for filtering HolidayType
     */
    public static class HolidayTypeFilter extends Filter<HolidayType> {

        public HolidayTypeFilter() {}

        public HolidayTypeFilter(HolidayTypeFilter filter) {
            super(filter);
        }

        @Override
        public HolidayTypeFilter copy() {
            return new HolidayTypeFilter(this);
        }
    }

    /**
     * Class for filtering CalenderType
     */
    public static class CalenderTypeFilter extends Filter<CalenderType> {

        public CalenderTypeFilter() {}

        public CalenderTypeFilter(CalenderTypeFilter filter) {
            super(filter);
        }

        @Override
        public CalenderTypeFilter copy() {
            return new CalenderTypeFilter(this);
        }
    }

    @Serial
    private static final long serialVersionUID = 1L;

    private UUIDFilter id;

    private StringFilter name;

    private HolidayTypeFilter type;

    private LocalDate fixedDate; // only month and day not year
    private LocalDate fixedFromDate;
    private LocalDate fixedToDate;

    private CalenderTypeFilter calenderType;

    private LocalDateFilter date;

    private StringFilter description;

    private ZonedDateTimeFilter createdAt;

    private ZonedDateTimeFilter updatedAt;

    private Boolean distinct;

    public HolidayConfigCriteria() {}

    public HolidayConfigCriteria(HolidayConfigCriteria other) {
        this.id = other.optionalId().map(UUIDFilter::copy).orElse(null);
        this.name = other.optionalName().map(StringFilter::copy).orElse(null);
        this.type = other.optionalType().map(HolidayTypeFilter::copy).orElse(null);
        this.calenderType = other.optionalCalenderType().map(CalenderTypeFilter::copy).orElse(null);
        this.date = other.optionalDate().map(LocalDateFilter::copy).orElse(null);
        this.description = other.optionalDescription().map(StringFilter::copy).orElse(null);
        this.createdAt = other.optionalCreatedAt().map(ZonedDateTimeFilter::copy).orElse(null);
        this.updatedAt = other.optionalUpdatedAt().map(ZonedDateTimeFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public HolidayConfigCriteria copy() {
        return new HolidayConfigCriteria(this);
    }

    public Optional<UUIDFilter> optionalId() {
        return Optional.ofNullable(id);
    }

    public UUIDFilter id() {
        if (id == null) {
            setId(new UUIDFilter());
        }
        return id;
    }

    public Optional<StringFilter> optionalName() {
        return Optional.ofNullable(name);
    }

    public StringFilter name() {
        if (name == null) {
            setName(new StringFilter());
        }
        return name;
    }

    public Optional<HolidayTypeFilter> optionalType() {
        return Optional.ofNullable(type);
    }

    public HolidayTypeFilter type() {
        if (type == null) {
            setType(new HolidayTypeFilter());
        }
        return type;
    }

    public Optional<CalenderTypeFilter> optionalCalenderType() {
        return Optional.ofNullable(calenderType);
    }

    public CalenderTypeFilter calenderType() {
        if (calenderType == null) {
            setCalenderType(new CalenderTypeFilter());
        }
        return calenderType;
    }

    public Optional<LocalDateFilter> optionalDate() {
        return Optional.ofNullable(date);
    }

    public LocalDateFilter date() {
        if (date == null) {
            setDate(new LocalDateFilter());
        }
        return date;
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

    public Optional<ZonedDateTimeFilter> optionalCreatedAt() {
        return Optional.ofNullable(createdAt);
    }

    public ZonedDateTimeFilter createdAt() {
        if (createdAt == null) {
            setCreatedAt(new ZonedDateTimeFilter());
        }
        return createdAt;
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

    public Optional<Boolean> optionalDistinct() {
        return Optional.ofNullable(distinct);
    }

    public Boolean distinct() {
        if (distinct == null) {
            setDistinct(true);
        }
        return distinct;
    }

}
