package com.ols.lifelog.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.ols.lifelog.IntegrationTest;
import com.ols.lifelog.domain.EventLogType;
import com.ols.lifelog.repository.EventLogTypeRepository;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link EventLogTypeResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class EventLogTypeResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_TEMPLATE = "AAAAAAAAAA";
    private static final String UPDATED_TEMPLATE = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/event-log-types";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private EventLogTypeRepository eventLogTypeRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restEventLogTypeMockMvc;

    private EventLogType eventLogType;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static EventLogType createEntity(EntityManager em) {
        EventLogType eventLogType = new EventLogType().name(DEFAULT_NAME).template(DEFAULT_TEMPLATE);
        return eventLogType;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static EventLogType createUpdatedEntity(EntityManager em) {
        EventLogType eventLogType = new EventLogType().name(UPDATED_NAME).template(UPDATED_TEMPLATE);
        return eventLogType;
    }

    @BeforeEach
    public void initTest() {
        eventLogType = createEntity(em);
    }

    @Test
    @Transactional
    void createEventLogType() throws Exception {
        int databaseSizeBeforeCreate = eventLogTypeRepository.findAll().size();
        // Create the EventLogType
        restEventLogTypeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(eventLogType)))
            .andExpect(status().isCreated());

        // Validate the EventLogType in the database
        List<EventLogType> eventLogTypeList = eventLogTypeRepository.findAll();
        assertThat(eventLogTypeList).hasSize(databaseSizeBeforeCreate + 1);
        EventLogType testEventLogType = eventLogTypeList.get(eventLogTypeList.size() - 1);
        assertThat(testEventLogType.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testEventLogType.getTemplate()).isEqualTo(DEFAULT_TEMPLATE);
    }

    @Test
    @Transactional
    void createEventLogTypeWithExistingId() throws Exception {
        // Create the EventLogType with an existing ID
        eventLogType.setId(1L);

        int databaseSizeBeforeCreate = eventLogTypeRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restEventLogTypeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(eventLogType)))
            .andExpect(status().isBadRequest());

        // Validate the EventLogType in the database
        List<EventLogType> eventLogTypeList = eventLogTypeRepository.findAll();
        assertThat(eventLogTypeList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllEventLogTypes() throws Exception {
        // Initialize the database
        eventLogTypeRepository.saveAndFlush(eventLogType);

        // Get all the eventLogTypeList
        restEventLogTypeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(eventLogType.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].template").value(hasItem(DEFAULT_TEMPLATE)));
    }

    @Test
    @Transactional
    void getEventLogType() throws Exception {
        // Initialize the database
        eventLogTypeRepository.saveAndFlush(eventLogType);

        // Get the eventLogType
        restEventLogTypeMockMvc
            .perform(get(ENTITY_API_URL_ID, eventLogType.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(eventLogType.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.template").value(DEFAULT_TEMPLATE));
    }

    @Test
    @Transactional
    void getNonExistingEventLogType() throws Exception {
        // Get the eventLogType
        restEventLogTypeMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewEventLogType() throws Exception {
        // Initialize the database
        eventLogTypeRepository.saveAndFlush(eventLogType);

        int databaseSizeBeforeUpdate = eventLogTypeRepository.findAll().size();

        // Update the eventLogType
        EventLogType updatedEventLogType = eventLogTypeRepository.findById(eventLogType.getId()).get();
        // Disconnect from session so that the updates on updatedEventLogType are not directly saved in db
        em.detach(updatedEventLogType);
        updatedEventLogType.name(UPDATED_NAME).template(UPDATED_TEMPLATE);

        restEventLogTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedEventLogType.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedEventLogType))
            )
            .andExpect(status().isOk());

        // Validate the EventLogType in the database
        List<EventLogType> eventLogTypeList = eventLogTypeRepository.findAll();
        assertThat(eventLogTypeList).hasSize(databaseSizeBeforeUpdate);
        EventLogType testEventLogType = eventLogTypeList.get(eventLogTypeList.size() - 1);
        assertThat(testEventLogType.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testEventLogType.getTemplate()).isEqualTo(UPDATED_TEMPLATE);
    }

    @Test
    @Transactional
    void putNonExistingEventLogType() throws Exception {
        int databaseSizeBeforeUpdate = eventLogTypeRepository.findAll().size();
        eventLogType.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEventLogTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, eventLogType.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(eventLogType))
            )
            .andExpect(status().isBadRequest());

        // Validate the EventLogType in the database
        List<EventLogType> eventLogTypeList = eventLogTypeRepository.findAll();
        assertThat(eventLogTypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchEventLogType() throws Exception {
        int databaseSizeBeforeUpdate = eventLogTypeRepository.findAll().size();
        eventLogType.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEventLogTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(eventLogType))
            )
            .andExpect(status().isBadRequest());

        // Validate the EventLogType in the database
        List<EventLogType> eventLogTypeList = eventLogTypeRepository.findAll();
        assertThat(eventLogTypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamEventLogType() throws Exception {
        int databaseSizeBeforeUpdate = eventLogTypeRepository.findAll().size();
        eventLogType.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEventLogTypeMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(eventLogType)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the EventLogType in the database
        List<EventLogType> eventLogTypeList = eventLogTypeRepository.findAll();
        assertThat(eventLogTypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateEventLogTypeWithPatch() throws Exception {
        // Initialize the database
        eventLogTypeRepository.saveAndFlush(eventLogType);

        int databaseSizeBeforeUpdate = eventLogTypeRepository.findAll().size();

        // Update the eventLogType using partial update
        EventLogType partialUpdatedEventLogType = new EventLogType();
        partialUpdatedEventLogType.setId(eventLogType.getId());

        partialUpdatedEventLogType.name(UPDATED_NAME);

        restEventLogTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedEventLogType.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedEventLogType))
            )
            .andExpect(status().isOk());

        // Validate the EventLogType in the database
        List<EventLogType> eventLogTypeList = eventLogTypeRepository.findAll();
        assertThat(eventLogTypeList).hasSize(databaseSizeBeforeUpdate);
        EventLogType testEventLogType = eventLogTypeList.get(eventLogTypeList.size() - 1);
        assertThat(testEventLogType.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testEventLogType.getTemplate()).isEqualTo(DEFAULT_TEMPLATE);
    }

    @Test
    @Transactional
    void fullUpdateEventLogTypeWithPatch() throws Exception {
        // Initialize the database
        eventLogTypeRepository.saveAndFlush(eventLogType);

        int databaseSizeBeforeUpdate = eventLogTypeRepository.findAll().size();

        // Update the eventLogType using partial update
        EventLogType partialUpdatedEventLogType = new EventLogType();
        partialUpdatedEventLogType.setId(eventLogType.getId());

        partialUpdatedEventLogType.name(UPDATED_NAME).template(UPDATED_TEMPLATE);

        restEventLogTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedEventLogType.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedEventLogType))
            )
            .andExpect(status().isOk());

        // Validate the EventLogType in the database
        List<EventLogType> eventLogTypeList = eventLogTypeRepository.findAll();
        assertThat(eventLogTypeList).hasSize(databaseSizeBeforeUpdate);
        EventLogType testEventLogType = eventLogTypeList.get(eventLogTypeList.size() - 1);
        assertThat(testEventLogType.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testEventLogType.getTemplate()).isEqualTo(UPDATED_TEMPLATE);
    }

    @Test
    @Transactional
    void patchNonExistingEventLogType() throws Exception {
        int databaseSizeBeforeUpdate = eventLogTypeRepository.findAll().size();
        eventLogType.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEventLogTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, eventLogType.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(eventLogType))
            )
            .andExpect(status().isBadRequest());

        // Validate the EventLogType in the database
        List<EventLogType> eventLogTypeList = eventLogTypeRepository.findAll();
        assertThat(eventLogTypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchEventLogType() throws Exception {
        int databaseSizeBeforeUpdate = eventLogTypeRepository.findAll().size();
        eventLogType.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEventLogTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(eventLogType))
            )
            .andExpect(status().isBadRequest());

        // Validate the EventLogType in the database
        List<EventLogType> eventLogTypeList = eventLogTypeRepository.findAll();
        assertThat(eventLogTypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamEventLogType() throws Exception {
        int databaseSizeBeforeUpdate = eventLogTypeRepository.findAll().size();
        eventLogType.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEventLogTypeMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(eventLogType))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the EventLogType in the database
        List<EventLogType> eventLogTypeList = eventLogTypeRepository.findAll();
        assertThat(eventLogTypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteEventLogType() throws Exception {
        // Initialize the database
        eventLogTypeRepository.saveAndFlush(eventLogType);

        int databaseSizeBeforeDelete = eventLogTypeRepository.findAll().size();

        // Delete the eventLogType
        restEventLogTypeMockMvc
            .perform(delete(ENTITY_API_URL_ID, eventLogType.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<EventLogType> eventLogTypeList = eventLogTypeRepository.findAll();
        assertThat(eventLogTypeList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
