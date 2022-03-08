package com.ols.lifelog.web.rest;

import com.ols.lifelog.domain.EventLogBook;
import com.ols.lifelog.repository.EventLogBookRepository;
import com.ols.lifelog.service.EventLogBookQueryService;
import com.ols.lifelog.service.EventLogBookService;
import com.ols.lifelog.service.criteria.EventLogBookCriteria;
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
 * REST controller for managing {@link com.ols.lifelog.domain.EventLogBook}.
 */
@RestController
@RequestMapping("/api")
public class EventLogBookResource {

    private final Logger log = LoggerFactory.getLogger(EventLogBookResource.class);

    private static final String ENTITY_NAME = "eventLogBook";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final EventLogBookService eventLogBookService;

    private final EventLogBookRepository eventLogBookRepository;

    private final EventLogBookQueryService eventLogBookQueryService;

    public EventLogBookResource(
        EventLogBookService eventLogBookService,
        EventLogBookRepository eventLogBookRepository,
        EventLogBookQueryService eventLogBookQueryService
    ) {
        this.eventLogBookService = eventLogBookService;
        this.eventLogBookRepository = eventLogBookRepository;
        this.eventLogBookQueryService = eventLogBookQueryService;
    }

    /**
     * {@code POST  /event-log-books} : Create a new eventLogBook.
     *
     * @param eventLogBook the eventLogBook to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new eventLogBook, or with status {@code 400 (Bad Request)} if the eventLogBook has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/event-log-books")
    public ResponseEntity<EventLogBook> createEventLogBook(@Valid @RequestBody EventLogBook eventLogBook) throws URISyntaxException {
        log.debug("REST request to save EventLogBook : {}", eventLogBook);
        if (eventLogBook.getId() != null) {
            throw new BadRequestAlertException("A new eventLogBook cannot already have an ID", ENTITY_NAME, "idexists");
        }
        EventLogBook result = eventLogBookService.save(eventLogBook);
        return ResponseEntity
            .created(new URI("/api/event-log-books/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /event-log-books/:id} : Updates an existing eventLogBook.
     *
     * @param id the id of the eventLogBook to save.
     * @param eventLogBook the eventLogBook to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated eventLogBook,
     * or with status {@code 400 (Bad Request)} if the eventLogBook is not valid,
     * or with status {@code 500 (Internal Server Error)} if the eventLogBook couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/event-log-books/{id}")
    public ResponseEntity<EventLogBook> updateEventLogBook(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody EventLogBook eventLogBook
    ) throws URISyntaxException {
        log.debug("REST request to update EventLogBook : {}, {}", id, eventLogBook);
        if (eventLogBook.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, eventLogBook.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!eventLogBookRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        EventLogBook result = eventLogBookService.save(eventLogBook);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, eventLogBook.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /event-log-books/:id} : Partial updates given fields of an existing eventLogBook, field will ignore if it is null
     *
     * @param id the id of the eventLogBook to save.
     * @param eventLogBook the eventLogBook to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated eventLogBook,
     * or with status {@code 400 (Bad Request)} if the eventLogBook is not valid,
     * or with status {@code 404 (Not Found)} if the eventLogBook is not found,
     * or with status {@code 500 (Internal Server Error)} if the eventLogBook couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/event-log-books/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<EventLogBook> partialUpdateEventLogBook(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody EventLogBook eventLogBook
    ) throws URISyntaxException {
        log.debug("REST request to partial update EventLogBook partially : {}, {}", id, eventLogBook);
        if (eventLogBook.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, eventLogBook.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!eventLogBookRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<EventLogBook> result = eventLogBookService.partialUpdate(eventLogBook);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, eventLogBook.getId().toString())
        );
    }

    /**
     * {@code GET  /event-log-books} : get all the eventLogBooks.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of eventLogBooks in body.
     */
    @GetMapping("/event-log-books")
    public ResponseEntity<List<EventLogBook>> getAllEventLogBooks(EventLogBookCriteria criteria) {
        log.debug("REST request to get EventLogBooks by criteria: {}", criteria);
        List<EventLogBook> entityList = eventLogBookQueryService.findByCriteria(criteria);
        return ResponseEntity.ok().body(entityList);
    }

    /**
     * {@code GET  /event-log-books/count} : count all the eventLogBooks.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/event-log-books/count")
    public ResponseEntity<Long> countEventLogBooks(EventLogBookCriteria criteria) {
        log.debug("REST request to count EventLogBooks by criteria: {}", criteria);
        return ResponseEntity.ok().body(eventLogBookQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /event-log-books/:id} : get the "id" eventLogBook.
     *
     * @param id the id of the eventLogBook to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the eventLogBook, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/event-log-books/{id}")
    public ResponseEntity<EventLogBook> getEventLogBook(@PathVariable Long id) {
        log.debug("REST request to get EventLogBook : {}", id);
        Optional<EventLogBook> eventLogBook = eventLogBookService.findOne(id);
        return ResponseUtil.wrapOrNotFound(eventLogBook);
    }

    /**
     * {@code DELETE  /event-log-books/:id} : delete the "id" eventLogBook.
     *
     * @param id the id of the eventLogBook to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/event-log-books/{id}")
    public ResponseEntity<Void> deleteEventLogBook(@PathVariable Long id) {
        log.debug("REST request to delete EventLogBook : {}", id);
        eventLogBookService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
