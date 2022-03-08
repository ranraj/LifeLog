package com.ols.lifelog.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Tags.
 */
@Entity
@Table(name = "tags")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Tags implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;

    @ManyToMany(mappedBy = "tags")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "user", "tags", "eventLogBook", "eventLogTypes" }, allowSetters = true)
    private Set<EventLog> eventLogs = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Tags id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public Tags name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return this.description;
    }

    public Tags description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Set<EventLog> getEventLogs() {
        return this.eventLogs;
    }

    public void setEventLogs(Set<EventLog> eventLogs) {
        if (this.eventLogs != null) {
            this.eventLogs.forEach(i -> i.removeTags(this));
        }
        if (eventLogs != null) {
            eventLogs.forEach(i -> i.addTags(this));
        }
        this.eventLogs = eventLogs;
    }

    public Tags eventLogs(Set<EventLog> eventLogs) {
        this.setEventLogs(eventLogs);
        return this;
    }

    public Tags addEventLog(EventLog eventLog) {
        this.eventLogs.add(eventLog);
        eventLog.getTags().add(this);
        return this;
    }

    public Tags removeEventLog(EventLog eventLog) {
        this.eventLogs.remove(eventLog);
        eventLog.getTags().remove(this);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Tags)) {
            return false;
        }
        return id != null && id.equals(((Tags) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Tags{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", description='" + getDescription() + "'" +
            "}";
    }
}
