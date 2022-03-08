package com.ols.lifelog.web.rest;

import com.ols.lifelog.domain.EventLogType;
import com.ols.lifelog.repository.EventLogTypeRepository;
import com.ols.lifelog.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.ols.lifelog.domain.EventLogType}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class EventLogTypeResource {

    private final Logger log = LoggerFactory.getLogger(EventLogTypeResource.class);

    private static final String ENTITY_NAME = "eventLogType";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final EventLogTypeRepository eventLogTypeRepository;

    public EventLogTypeResource(EventLogTypeRepository eventLogTypeRepository) {
        this.eventLogTypeRepository = eventLogTypeRepository;
    }

    /**
     * {@code POST  /event-log-types} : Create a new eventLogType.
     *
     * @param eventLogType the eventLogType to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new eventLogType, or with status {@code 400 (Bad Request)} if the eventLogType has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/event-log-types")
    public ResponseEntity<EventLogType> createEventLogType(@RequestBody EventLogType eventLogType) throws URISyntaxException {
        log.debug("REST request to save EventLogType : {}", eventLogType);
        if (eventLogType.getId() != null) {
            throw new BadRequestAlertException("A new eventLogType cannot already have an ID", ENTITY_NAME, "idexists");
        }
        EventLogType result = eventLogTypeRepository.save(eventLogType);
        return ResponseEntity
            .created(new URI("/api/event-log-types/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /event-log-types/:id} : Updates an existing eventLogType.
     *
     * @param id the id of the eventLogType to save.
     * @param eventLogType the eventLogType to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated eventLogType,
     * or with status {@code 400 (Bad Request)} if the eventLogType is not valid,
     * or with status {@code 500 (Internal Server Error)} if the eventLogType couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/event-log-types/{id}")
    public ResponseEntity<EventLogType> updateEventLogType(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody EventLogType eventLogType
    ) throws URISyntaxException {
        log.debug("REST request to update EventLogType : {}, {}", id, eventLogType);
        if (eventLogType.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, eventLogType.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!eventLogTypeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        EventLogType result = eventLogTypeRepository.save(eventLogType);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, eventLogType.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /event-log-types/:id} : Partial updates given fields of an existing eventLogType, field will ignore if it is null
     *
     * @param id the id of the eventLogType to save.
     * @param eventLogType the eventLogType to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated eventLogType,
     * or with status {@code 400 (Bad Request)} if the eventLogType is not valid,
     * or with status {@code 404 (Not Found)} if the eventLogType is not found,
     * or with status {@code 500 (Internal Server Error)} if the eventLogType couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/event-log-types/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<EventLogType> partialUpdateEventLogType(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody EventLogType eventLogType
    ) throws URISyntaxException {
        log.debug("REST request to partial update EventLogType partially : {}, {}", id, eventLogType);
        if (eventLogType.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, eventLogType.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!eventLogTypeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<EventLogType> result = eventLogTypeRepository
            .findById(eventLogType.getId())
            .map(existingEventLogType -> {
                if (eventLogType.getName() != null) {
                    existingEventLogType.setName(eventLogType.getName());
                }
                if (eventLogType.getTemplate() != null) {
                    existingEventLogType.setTemplate(eventLogType.getTemplate());
                }

                return existingEventLogType;
            })
            .map(eventLogTypeRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, eventLogType.getId().toString())
        );
    }

    /**
     * {@code GET  /event-log-types} : get all the eventLogTypes.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of eventLogTypes in body.
     */
    @GetMapping("/event-log-types")
    public List<EventLogType> getAllEventLogTypes() {
        log.debug("REST request to get all EventLogTypes");
        return eventLogTypeRepository.findAll();
    }

    /**
     * {@code GET  /event-log-types/:id} : get the "id" eventLogType.
     *
     * @param id the id of the eventLogType to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the eventLogType, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/event-log-types/{id}")
    public ResponseEntity<EventLogType> getEventLogType(@PathVariable Long id) {
        log.debug("REST request to get EventLogType : {}", id);
        Optional<EventLogType> eventLogType = eventLogTypeRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(eventLogType);
    }

    /**
     * {@code DELETE  /event-log-types/:id} : delete the "id" eventLogType.
     *
     * @param id the id of the eventLogType to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/event-log-types/{id}")
    public ResponseEntity<Void> deleteEventLogType(@PathVariable Long id) {
        log.debug("REST request to delete EventLogType : {}", id);
        eventLogTypeRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
