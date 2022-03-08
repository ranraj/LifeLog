package com.ols.lifelog.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A EventLogType.
 */
@Entity
@Table(name = "event_log_type")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class EventLogType implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "template")
    private String template;

    @ManyToOne
    @JsonIgnoreProperties(value = { "user", "tags", "eventLogBook", "eventLogTypes" }, allowSetters = true)
    private EventLog eventLog;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public EventLogType id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public EventLogType name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTemplate() {
        return this.template;
    }

    public EventLogType template(String template) {
        this.setTemplate(template);
        return this;
    }

    public void setTemplate(String template) {
        this.template = template;
    }

    public EventLog getEventLog() {
        return this.eventLog;
    }

    public void setEventLog(EventLog eventLog) {
        this.eventLog = eventLog;
    }

    public EventLogType eventLog(EventLog eventLog) {
        this.setEventLog(eventLog);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof EventLogType)) {
            return false;
        }
        return id != null && id.equals(((EventLogType) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "EventLogType{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", template='" + getTemplate() + "'" +
            "}";
    }
}
