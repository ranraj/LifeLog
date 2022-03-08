package com.ols.lifelog.service;

import com.ols.lifelog.domain.EventLog;
import com.ols.lifelog.repository.EventLogRepository;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link EventLog}.
 */
@Service
@Transactional
public class EventLogService {

    private final Logger log = LoggerFactory.getLogger(EventLogService.class);

    private final EventLogRepository eventLogRepository;

    public EventLogService(EventLogRepository eventLogRepository) {
        this.eventLogRepository = eventLogRepository;
    }

    /**
     * Save a eventLog.
     *
     * @param eventLog the entity to save.
     * @return the persisted entity.
     */
    public EventLog save(EventLog eventLog) {
        log.debug("Request to save EventLog : {}", eventLog);
        return eventLogRepository.save(eventLog);
    }

    /**
     * Partially update a eventLog.
     *
     * @param eventLog the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<EventLog> partialUpdate(EventLog eventLog) {
        log.debug("Request to partially update EventLog : {}", eventLog);

        return eventLogRepository
            .findById(eventLog.getId())
            .map(existingEventLog -> {
                if (eventLog.getUuid() != null) {
                    existingEventLog.setUuid(eventLog.getUuid());
                }
                if (eventLog.getName() != null) {
                    existingEventLog.setName(eventLog.getName());
                }
                if (eventLog.getDetail() != null) {
                    existingEventLog.setDetail(eventLog.getDetail());
                }
                if (eventLog.getCreatedDate() != null) {
                    existingEventLog.setCreatedDate(eventLog.getCreatedDate());
                }
                if (eventLog.getUpdatedDate() != null) {
                    existingEventLog.setUpdatedDate(eventLog.getUpdatedDate());
                }

                return existingEventLog;
            })
            .map(eventLogRepository::save);
    }

    /**
     * Get all the eventLogs.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<EventLog> findAll() {
        log.debug("Request to get all EventLogs");
        return eventLogRepository.findAllWithEagerRelationships();
    }

    /**
     * Get all the eventLogs with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<EventLog> findAllWithEagerRelationships(Pageable pageable) {
        return eventLogRepository.findAllWithEagerRelationships(pageable);
    }

    /**
     * Get one eventLog by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<EventLog> findOne(Long id) {
        log.debug("Request to get EventLog : {}", id);
        return eventLogRepository.findOneWithEagerRelationships(id);
    }

    /**
     * Delete the eventLog by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete EventLog : {}", id);
        eventLogRepository.deleteById(id);
    }
}
