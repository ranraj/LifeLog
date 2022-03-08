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
 * Criteria class for the {@link com.ols.lifelog.domain.EventLogBook} entity. This class is used
 * in {@link com.ols.lifelog.web.rest.EventLogBookResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /event-log-books?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class EventLogBookCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private UUIDFilter uuid;

    private LocalDateFilter createdDate;

    private LocalDateFilter updatedDate;

    private StringFilter name;

    private StringFilter description;

    private BooleanFilter archieved;

    private LongFilter eventLogId;

    private LongFilter userId;

    private Boolean distinct;

    public EventLogBookCriteria() {}

    public EventLogBookCriteria(EventLogBookCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.uuid = other.uuid == null ? null : other.uuid.copy();
        this.createdDate = other.createdDate == null ? null : other.createdDate.copy();
        this.updatedDate = other.updatedDate == null ? null : other.updatedDate.copy();
        this.name = other.name == null ? null : other.name.copy();
        this.description = other.description == null ? null : other.description.copy();
        this.archieved = other.archieved == null ? null : other.archieved.copy();
        this.eventLogId = other.eventLogId == null ? null : other.eventLogId.copy();
        this.userId = other.userId == null ? null : other.userId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public EventLogBookCriteria copy() {
        return new EventLogBookCriteria(this);
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

    public StringFilter getDescription() {
        return description;
    }

    public StringFilter description() {
        if (description == null) {
            description = new StringFilter();
        }
        return description;
    }

    public void setDescription(StringFilter description) {
        this.description = description;
    }

    public BooleanFilter getArchieved() {
        return archieved;
    }

    public BooleanFilter archieved() {
        if (archieved == null) {
            archieved = new BooleanFilter();
        }
        return archieved;
    }

    public void setArchieved(BooleanFilter archieved) {
        this.archieved = archieved;
    }

    public LongFilter getEventLogId() {
        return eventLogId;
    }

    public LongFilter eventLogId() {
        if (eventLogId == null) {
            eventLogId = new LongFilter();
        }
        return eventLogId;
    }

    public void setEventLogId(LongFilter eventLogId) {
        this.eventLogId = eventLogId;
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
        final EventLogBookCriteria that = (EventLogBookCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(uuid, that.uuid) &&
            Objects.equals(createdDate, that.createdDate) &&
            Objects.equals(updatedDate, that.updatedDate) &&
            Objects.equals(name, that.name) &&
            Objects.equals(description, that.description) &&
            Objects.equals(archieved, that.archieved) &&
            Objects.equals(eventLogId, that.eventLogId) &&
            Objects.equals(userId, that.userId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, uuid, createdDate, updatedDate, name, description, archieved, eventLogId, userId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "EventLogBookCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (uuid != null ? "uuid=" + uuid + ", " : "") +
            (createdDate != null ? "createdDate=" + createdDate + ", " : "") +
            (updatedDate != null ? "updatedDate=" + updatedDate + ", " : "") +
            (name != null ? "name=" + name + ", " : "") +
            (description != null ? "description=" + description + ", " : "") +
            (archieved != null ? "archieved=" + archieved + ", " : "") +
            (eventLogId != null ? "eventLogId=" + eventLogId + ", " : "") +
            (userId != null ? "userId=" + userId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
