package com.ols.lifelog.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Type;

/**
 * A EventLog.
 */
@Entity
@Table(name = "event_log")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class EventLog implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Type(type = "uuid-char")
    @Column(name = "uuid", length = 36)
    private UUID uuid;

    @Column(name = "name")
    private String name;

    @NotNull
    @Column(name = "detail", nullable = false)
    private String detail;

    @Column(name = "created_date")
    private LocalDate createdDate;

    @Column(name = "updated_date")
    private LocalDate updatedDate;

    @ManyToOne
    private User user;

    @ManyToMany
    @JoinTable(
        name = "rel_event_log__tags",
        joinColumns = @JoinColumn(name = "event_log_id"),
        inverseJoinColumns = @JoinColumn(name = "tags_id")
    )
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "eventLogs" }, allowSetters = true)
    private Set<Tags> tags = new HashSet<>();

    @ManyToOne
    @JsonIgnoreProperties(value = { "eventLogs", "user" }, allowSetters = true)
    private EventLogBook eventLogBook;

    @OneToMany(mappedBy = "eventLog")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "eventLog" }, allowSetters = true)
    private Set<EventLogType> eventLogTypes = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public EventLog id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public UUID getUuid() {
        return this.uuid;
    }

    public EventLog uuid(UUID uuid) {
        this.setUuid(uuid);
        return this;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    public String getName() {
        return this.name;
    }

    public EventLog name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDetail() {
        return this.detail;
    }

    public EventLog detail(String detail) {
        this.setDetail(detail);
        return this;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public LocalDate getCreatedDate() {
        return this.createdDate;
    }

    public EventLog createdDate(LocalDate createdDate) {
        this.setCreatedDate(createdDate);
        return this;
    }

    public void setCreatedDate(LocalDate createdDate) {
        this.createdDate = createdDate;
    }

    public LocalDate getUpdatedDate() {
        return this.updatedDate;
    }

    public EventLog updatedDate(LocalDate updatedDate) {
        this.setUpdatedDate(updatedDate);
        return this;
    }

    public void setUpdatedDate(LocalDate updatedDate) {
        this.updatedDate = updatedDate;
    }

    public User getUser() {
        return this.user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public EventLog user(User user) {
        this.setUser(user);
        return this;
    }

    public Set<Tags> getTags() {
        return this.tags;
    }

    public void setTags(Set<Tags> tags) {
        this.tags = tags;
    }

    public EventLog tags(Set<Tags> tags) {
        this.setTags(tags);
        return this;
    }

    public EventLog addTags(Tags tags) {
        this.tags.add(tags);
        tags.getEventLogs().add(this);
        return this;
    }

    public EventLog removeTags(Tags tags) {
        this.tags.remove(tags);
        tags.getEventLogs().remove(this);
        return this;
    }

    public EventLogBook getEventLogBook() {
        return this.eventLogBook;
    }

    public void setEventLogBook(EventLogBook eventLogBook) {
        this.eventLogBook = eventLogBook;
    }

    public EventLog eventLogBook(EventLogBook eventLogBook) {
        this.setEventLogBook(eventLogBook);
        return this;
    }

    public Set<EventLogType> getEventLogTypes() {
        return this.eventLogTypes;
    }

    public void setEventLogTypes(Set<EventLogType> eventLogTypes) {
        if (this.eventLogTypes != null) {
            this.eventLogTypes.forEach(i -> i.setEventLog(null));
        }
        if (eventLogTypes != null) {
            eventLogTypes.forEach(i -> i.setEventLog(this));
        }
        this.eventLogTypes = eventLogTypes;
    }

    public EventLog eventLogTypes(Set<EventLogType> eventLogTypes) {
        this.setEventLogTypes(eventLogTypes);
        return this;
    }

    public EventLog addEventLogType(EventLogType eventLogType) {
        this.eventLogTypes.add(eventLogType);
        eventLogType.setEventLog(this);
        return this;
    }

    public EventLog removeEventLogType(EventLogType eventLogType) {
        this.eventLogTypes.remove(eventLogType);
        eventLogType.setEventLog(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof EventLog)) {
            return false;
        }
        return id != null && id.equals(((EventLog) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "EventLog{" +
            "id=" + getId() +
            ", uuid='" + getUuid() + "'" +
            ", name='" + getName() + "'" +
            ", detail='" + getDetail() + "'" +
            ", createdDate='" + getCreatedDate() + "'" +
            ", updatedDate='" + getUpdatedDate() + "'" +
            "}";
    }
}
