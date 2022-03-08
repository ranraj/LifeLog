package com.ols.lifelog.web.rest;

import com.ols.lifelog.domain.EventLog;
import com.ols.lifelog.repository.EventLogRepository;
import com.ols.lifelog.service.EventLogQueryService;
import com.ols.lifelog.service.EventLogService;
import com.ols.lifelog.service.criteria.EventLogCriteria;
import com.ols.lifelog.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.ols.lifelog.domain.EventLog}.
 */
@RestController
@RequestMapping("/api")
public class EventLogResource {

    private final Logger log = LoggerFactory.getLogger(EventLogResource.class);

    private static final String ENTITY_NAME = "eventLog";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final EventLogService eventLogService;

    private final EventLogRepository eventLogRepository;

    private final EventLogQueryService eventLogQueryService;

    public EventLogResource(
        EventLogService eventLogService,
        EventLogRepository eventLogRepository,
        EventLogQueryService eventLogQueryService
    ) {
        this.eventLogService = eventLogService;
        this.eventLogRepository = eventLogRepository;
        this.eventLogQueryService = eventLogQueryService;
    }

    /**
     * {@code POST  /event-logs} : Create a new eventLog.
     *
     * @param eventLog the eventLog to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new eventLog, or with status {@code 400 (Bad Request)} if the eventLog has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/event-logs")
    public ResponseEntity<EventLog> createEventLog(@Valid @RequestBody EventLog eventLog) throws URISyntaxException {
        log.debug("REST request to save EventLog : {}", eventLog);
        if (eventLog.getId() != null) {
            throw new BadRequestAlertException("A new eventLog cannot already have an ID", ENTITY_NAME, "idexists");
        }
        EventLog result = eventLogService.save(eventLog);
        return ResponseEntity
            .created(new URI("/api/event-logs/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /event-logs/:id} : Updates an existing eventLog.
     *
     * @param id the id of the eventLog to save.
     * @param eventLog the eventLog to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated eventLog,
     * or with status {@code 400 (Bad Request)} if the eventLog is not valid,
     * or with status {@code 500 (Internal Server Error)} if the eventLog couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/event-logs/{id}")
    public ResponseEntity<EventLog> updateEventLog(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody EventLog eventLog
    ) throws URISyntaxException {
        log.debug("REST request to update EventLog : {}, {}", id, eventLog);
        if (eventLog.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, eventLog.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!eventLogRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        EventLog result = eventLogService.save(eventLog);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, eventLog.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /event-logs/:id} : Partial updates given fields of an existing eventLog, field will ignore if it is null
     *
     * @param id the id of the eventLog to save.
     * @param eventLog the eventLog to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated eventLog,
     * or with status {@code 400 (Bad Request)} if the eventLog is not valid,
     * or with status {@code 404 (Not Found)} if the eventLog is not found,
     * or with status {@code 500 (Internal Server Error)} if the eventLog couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/event-logs/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<EventLog> partialUpdateEventLog(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody EventLog eventLog
    ) throws URISyntaxException {
        log.debug("REST request to partial update EventLog partially : {}, {}", id, eventLog);
        if (eventLog.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, eventLog.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!eventLogRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<EventLog> result = eventLogService.partialUpdate(eventLog);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, eventLog.getId().toString())
        );
    }

    /**
     * {@code GET  /event-logs} : get all the eventLogs.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of eventLogs in body.
     */
    @GetMapping("/event-logs")
    public ResponseEntity<List<EventLog>> getAllEventLogs(EventLogCriteria criteria) {
        log.debug("REST request to get EventLogs by criteria: {}", criteria);
        List<EventLog> entityList = eventLogQueryService.findByCriteria(criteria);
        return ResponseEntity.ok().body(entityList);
    }

    /**
     * {@code GET  /event-logs/count} : count all the eventLogs.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/event-logs/count")
    public ResponseEntity<Long> countEventLogs(EventLogCriteria criteria) {
        log.debug("REST request to count EventLogs by criteria: {}", criteria);
        return ResponseEntity.ok().body(eventLogQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /event-logs/:id} : get the "id" eventLog.
     *
     * @param id the id of the eventLog to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the eventLog, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/event-logs/{id}")
    public ResponseEntity<EventLog> getEventLog(@PathVariable Long id) {
        log.debug("REST request to get EventLog : {}", id);
        Optional<EventLog> eventLog = eventLogService.findOne(id);
        return ResponseUtil.wrapOrNotFound(eventLog);
    }

    /**
     * {@code DELETE  /event-logs/:id} : delete the "id" eventLog.
     *
     * @param id the id of the eventLog to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/event-logs/{id}")
    public ResponseEntity<Void> deleteEventLog(@PathVariable Long id) {
        log.debug("REST request to delete EventLog : {}", id);
        eventLogService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
