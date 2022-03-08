package com.ols.lifelog.service;

import com.ols.lifelog.domain.EventLogBook;
import com.ols.lifelog.repository.EventLogBookRepository;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link EventLogBook}.
 */
@Service
@Transactional
public class EventLogBookService {

    private final Logger log = LoggerFactory.getLogger(EventLogBookService.class);

    private final EventLogBookRepository eventLogBookRepository;

    public EventLogBookService(EventLogBookRepository eventLogBookRepository) {
        this.eventLogBookRepository = eventLogBookRepository;
    }

    /**
     * Save a eventLogBook.
     *
     * @param eventLogBook the entity to save.
     * @return the persisted entity.
     */
    public EventLogBook save(EventLogBook eventLogBook) {
        log.debug("Request to save EventLogBook : {}", eventLogBook);
        return eventLogBookRepository.save(eventLogBook);
    }

    /**
     * Partially update a eventLogBook.
     *
     * @param eventLogBook the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<EventLogBook> partialUpdate(EventLogBook eventLogBook) {
        log.debug("Request to partially update EventLogBook : {}", eventLogBook);

        return eventLogBookRepository
            .findById(eventLogBook.getId())
            .map(existingEventLogBook -> {
                if (eventLogBook.getUuid() != null) {
                    existingEventLogBook.setUuid(eventLogBook.getUuid());
                }
                if (eventLogBook.getCreatedDate() != null) {
                    existingEventLogBook.setCreatedDate(eventLogBook.getCreatedDate());
                }
                if (eventLogBook.getUpdatedDate() != null) {
                    existingEventLogBook.setUpdatedDate(eventLogBook.getUpdatedDate());
                }
                if (eventLogBook.getName() != null) {
                    existingEventLogBook.setName(eventLogBook.getName());
                }
                if (eventLogBook.getDescription() != null) {
                    existingEventLogBook.setDescription(eventLogBook.getDescription());
                }
                if (eventLogBook.getArchieved() != null) {
                    existingEventLogBook.setArchieved(eventLogBook.getArchieved());
                }

                return existingEventLogBook;
            })
            .map(eventLogBookRepository::save);
    }

    /**
     * Get all the eventLogBooks.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<EventLogBook> findAll() {
        log.debug("Request to get all EventLogBooks");
        return eventLogBookRepository.findAll();
    }

    /**
     * Get one eventLogBook by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<EventLogBook> findOne(Long id) {
        log.debug("Request to get EventLogBook : {}", id);
        return eventLogBookRepository.findById(id);
    }

    /**
     * Delete the eventLogBook by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete EventLogBook : {}", id);
        eventLogBookRepository.deleteById(id);
    }
}
