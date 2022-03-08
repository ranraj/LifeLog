package com.ols.lifelog.web.rest;

import com.ols.lifelog.domain.Tags;
import com.ols.lifelog.repository.TagsRepository;
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
 * REST controller for managing {@link com.ols.lifelog.domain.Tags}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class TagsResource {

    private final Logger log = LoggerFactory.getLogger(TagsResource.class);

    private static final String ENTITY_NAME = "tags";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final TagsRepository tagsRepository;

    public TagsResource(TagsRepository tagsRepository) {
        this.tagsRepository = tagsRepository;
    }

    /**
     * {@code POST  /tags} : Create a new tags.
     *
     * @param tags the tags to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new tags, or with status {@code 400 (Bad Request)} if the tags has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/tags")
    public ResponseEntity<Tags> createTags(@RequestBody Tags tags) throws URISyntaxException {
        log.debug("REST request to save Tags : {}", tags);
        if (tags.getId() != null) {
            throw new BadRequestAlertException("A new tags cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Tags result = tagsRepository.save(tags);
        return ResponseEntity
            .created(new URI("/api/tags/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /tags/:id} : Updates an existing tags.
     *
     * @param id the id of the tags to save.
     * @param tags the tags to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated tags,
     * or with status {@code 400 (Bad Request)} if the tags is not valid,
     * or with status {@code 500 (Internal Server Error)} if the tags couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/tags/{id}")
    public ResponseEntity<Tags> updateTags(@PathVariable(value = "id", required = false) final Long id, @RequestBody Tags tags)
        throws URISyntaxException {
        log.debug("REST request to update Tags : {}, {}", id, tags);
        if (tags.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, tags.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!tagsRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Tags result = tagsRepository.save(tags);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, tags.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /tags/:id} : Partial updates given fields of an existing tags, field will ignore if it is null
     *
     * @param id the id of the tags to save.
     * @param tags the tags to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated tags,
     * or with status {@code 400 (Bad Request)} if the tags is not valid,
     * or with status {@code 404 (Not Found)} if the tags is not found,
     * or with status {@code 500 (Internal Server Error)} if the tags couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/tags/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Tags> partialUpdateTags(@PathVariable(value = "id", required = false) final Long id, @RequestBody Tags tags)
        throws URISyntaxException {
        log.debug("REST request to partial update Tags partially : {}, {}", id, tags);
        if (tags.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, tags.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!tagsRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Tags> result = tagsRepository
            .findById(tags.getId())
            .map(existingTags -> {
                if (tags.getName() != null) {
                    existingTags.setName(tags.getName());
                }
                if (tags.getDescription() != null) {
                    existingTags.setDescription(tags.getDescription());
                }

                return existingTags;
            })
            .map(tagsRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, tags.getId().toString())
        );
    }

    /**
     * {@code GET  /tags} : get all the tags.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of tags in body.
     */
    @GetMapping("/tags")
    public List<Tags> getAllTags() {
        log.debug("REST request to get all Tags");
        return tagsRepository.findAll();
    }

    /**
     * {@code GET  /tags/:id} : get the "id" tags.
     *
     * @param id the id of the tags to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the tags, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/tags/{id}")
    public ResponseEntity<Tags> getTags(@PathVariable Long id) {
        log.debug("REST request to get Tags : {}", id);
        Optional<Tags> tags = tagsRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(tags);
    }

    /**
     * {@code DELETE  /tags/:id} : delete the "id" tags.
     *
     * @param id the id of the tags to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/tags/{id}")
    public ResponseEntity<Void> deleteTags(@PathVariable Long id) {
        log.debug("REST request to delete Tags : {}", id);
        tagsRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
