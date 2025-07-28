package com.masi.utility.domain.criteria;

import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;

import lombok.Getter;
import lombok.Setter;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.masi.utility.domain.NotificationRecipient} entity. This class is used
 * in {@link com.masi.utility.web.rest.NotificationRecipientResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /notification-recipients?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@Setter
@Getter
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class NotificationRecipientCriteria implements Serializable, Criteria {

    @Serial
    private static final long serialVersionUID = 1L;

    private UUIDFilter id;

    private UUIDFilter notificationId;

    private UUIDFilter recipientId;

    private BooleanFilter read;

    private ZonedDateTimeFilter readAt;


    private Boolean distinct;

    public NotificationRecipientCriteria() {}

    public NotificationRecipientCriteria(NotificationRecipientCriteria other) {
        this.id = other.optionalId().map(UUIDFilter::copy).orElse(null);
        this.notificationId = other.optionalNotificationId().map(UUIDFilter::copy).orElse(null);
        this.recipientId = other.optionalRecipientId().map(UUIDFilter::copy).orElse(null);
        this.read = other.optionalRead().map(BooleanFilter::copy).orElse(null);
        this.readAt = other.optionalReadAt().map(ZonedDateTimeFilter::copy).orElse(null);
        this.notificationId = other.optionalNotificationId().map(UUIDFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public NotificationRecipientCriteria copy() {
        return new NotificationRecipientCriteria(this);
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


    public Optional<UUIDFilter> optionalRecipientId() {
        return Optional.ofNullable(recipientId);
    }

    public UUIDFilter recipientId() {
        if (recipientId == null) {
            setRecipientId(new UUIDFilter());
        }
        return recipientId;
    }

    public Optional<BooleanFilter> optionalRead() {
        return Optional.ofNullable(read);
    }

    public BooleanFilter read() {
        if (read == null) {
            setRead(new BooleanFilter());
        }
        return read;
    }

    public Optional<ZonedDateTimeFilter> optionalReadAt() {
        return Optional.ofNullable(readAt);
    }

    public ZonedDateTimeFilter readAt() {
        if (readAt == null) {
            setReadAt(new ZonedDateTimeFilter());
        }
        return readAt;
    }

    public Optional<UUIDFilter> optionalNotificationId() {
        return Optional.ofNullable(notificationId);
    }

    public UUIDFilter notificationId() {
        if (notificationId == null) {
            setNotificationId(new UUIDFilter());
        }
        return notificationId;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final NotificationRecipientCriteria that = (NotificationRecipientCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(notificationId, that.notificationId) &&
            Objects.equals(recipientId, that.recipientId) &&
            Objects.equals(read, that.read) &&
            Objects.equals(readAt, that.readAt) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, notificationId, recipientId, read, readAt, notificationId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "NotificationRecipientCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalNotificationId().map(f -> "notificationId=" + f + ", ").orElse("") +
            optionalRecipientId().map(f -> "recipientId=" + f + ", ").orElse("") +
            optionalRead().map(f -> "read=" + f + ", ").orElse("") +
            optionalReadAt().map(f -> "readAt=" + f + ", ").orElse("") +
            optionalNotificationId().map(f -> "notificationId=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
