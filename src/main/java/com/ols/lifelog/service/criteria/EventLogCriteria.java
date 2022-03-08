package com.ols.lifelog.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.BooleanFilter;
import tech.jhipster.service.filter.DoubleFilter;
import tech.jhipster.service.filter.Filter;
import tech.jhipster.service.filter.FloatFilter;
import tech.jhipster.service.filter.IntegerFilter;
import tech.jhipster.service.filter.LocalDateFilter;
import tech.jhipster.service.filter.LongFilter;
import tech.jhipster.service.filter.StringFilter;
import tech.jhipster.service.filter.UUIDFilter;

/**
 * Criteria class for the {@link com.ols.lifelog.domain.EventLog} entity. This class is used
 * in {@link com.ols.lifelog.web.rest.EventLogResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /event-logs?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class EventLogCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private UUIDFilter uuid;

    private StringFilter name;

    private StringFilter detail;

    private LocalDateFilter createdDate;

    private LocalDateFilter updatedDate;

    private LongFilter userId;

    private LongFilter tagsId;

    private LongFilter eventLogBookId;

    private LongFilter eventLogTypeId;

    private Boolean distinct;

    public EventLogCriteria() {}

    public EventLogCriteria(EventLogCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.uuid = other.uuid == null ? null : other.uuid.copy();
        this.name = other.name == null ? null : other.name.copy();
        this.detail = other.detail == null ? null : other.detail.copy();
        this.createdDate = other.createdDate == null ? null : other.createdDate.copy();
        this.updatedDate = other.updatedDate == null ? null : other.updatedDate.copy();
        this.userId = other.userId == null ? null : other.userId.copy();
        this.tagsId = other.tagsId == null ? null : other.tagsId.copy();
        this.eventLogBookId = other.eventLogBookId == null ? null : other.eventLogBookId.copy();
        this.eventLogTypeId = other.eventLogTypeId == null ? null : other.eventLogTypeId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public EventLogCriteria copy() {
        return new EventLogCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public LongFilter id() {
        if (id == null) {
            id = new LongFilter();
        }
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public UUIDFilter getUuid() {
        return uuid;
    }

    public UUIDFilter uuid() {
        if (uuid == null) {
            uuid = new UUIDFilter();
        }
        return uuid;
    }

    public void setUuid(UUIDFilter uuid) {
        this.uuid = uuid;
    }

    public StringFilter getName() {
        return name;
    }

    public StringFilter name() {
        if (name == null) {
            name = new StringFilter();
        }
        return name;
    }

    public void setName(StringFilter name) {
        this.name = name;
    }

    public StringFilter getDetail() {
        return detail;
    }

    public StringFilter detail() {
        if (detail == null) {
            detail = new StringFilter();
        }
        return detail;
    }

    public void setDetail(StringFilter detail) {
        this.detail = detail;
    }

    public LocalDateFilter getCreatedDate() {
        return createdDate;
    }

    public LocalDateFilter createdDate() {
        if (createdDate == null) {
            createdDate = new LocalDateFilter();
        }
        return createdDate;
    }

    public void setCreatedDate(LocalDateFilter createdDate) {
        this.createdDate = createdDate;
    }

    public LocalDateFilter getUpdatedDate() {
        return updatedDate;
    }

    public LocalDateFilter updatedDate() {
        if (updatedDate == null) {
            updatedDate = new LocalDateFilter();
        }
        return updatedDate;
    }

    public void setUpdatedDate(LocalDateFilter updatedDate) {
        this.updatedDate = updatedDate;
    }

    public LongFilter getUserId() {
        return userId;
    }

    public LongFilter userId() {
        if (userId == null) {
            userId = new LongFilter();
        }
        return userId;
    }

    public void setUserId(LongFilter userId) {
        this.userId = userId;
    }

    public LongFilter getTagsId() {
        return tagsId;
    }

    public LongFilter tagsId() {
        if (tagsId == null) {
            tagsId = new LongFilter();
        }
        return tagsId;
    }

    public void setTagsId(LongFilter tagsId) {
        this.tagsId = tagsId;
    }

    public LongFilter getEventLogBookId() {
        return eventLogBookId;
    }

    public LongFilter eventLogBookId() {
        if (eventLogBookId == null) {
            eventLogBookId = new LongFilter();
        }
        return eventLogBookId;
    }

    public void setEventLogBookId(LongFilter eventLogBookId) {
        this.eventLogBookId = eventLogBookId;
    }

    public LongFilter getEventLogTypeId() {
        return eventLogTypeId;
    }

    public LongFilter eventLogTypeId() {
        if (eventLogTypeId == null) {
            eventLogTypeId = new LongFilter();
        }
        return eventLogTypeId;
    }

    public void setEventLogTypeId(LongFilter eventLogTypeId) {
        this.eventLogTypeId = eventLogTypeId;
    }

    public Boolean getDistinct() {
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
        final EventLogCriteria that = (EventLogCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(uuid, that.uuid) &&
            Objects.equals(name, that.name) &&
            Objects.equals(detail, that.detail) &&
            Objects.equals(createdDate, that.createdDate) &&
            Objects.equals(updatedDate, that.updatedDate) &&
            Objects.equals(userId, that.userId) &&
            Objects.equals(tagsId, that.tagsId) &&
            Objects.equals(eventLogBookId, that.eventLogBookId) &&
            Objects.equals(eventLogTypeId, that.eventLogTypeId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, uuid, name, detail, createdDate, updatedDate, userId, tagsId, eventLogBookId, eventLogTypeId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "EventLogCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (uuid != null ? "uuid=" + uuid + ", " : "") +
            (name != null ? "name=" + name + ", " : "") +
            (detail != null ? "detail=" + detail + ", " : "") +
            (createdDate != null ? "createdDate=" + createdDate + ", " : "") +
            (updatedDate != null ? "updatedDate=" + updatedDate + ", " : "") +
            (userId != null ? "userId=" + userId + ", " : "") +
            (tagsId != null ? "tagsId=" + tagsId + ", " : "") +
            (eventLogBookId != null ? "eventLogBookId=" + eventLogBookId + ", " : "") +
            (eventLogTypeId != null ? "eventLogTypeId=" + eventLogTypeId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
