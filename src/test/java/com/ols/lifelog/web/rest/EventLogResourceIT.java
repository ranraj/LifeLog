package com.ols.lifelog.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.ols.lifelog.IntegrationTest;
import com.ols.lifelog.domain.EventLog;
import com.ols.lifelog.domain.EventLogBook;
import com.ols.lifelog.domain.EventLogType;
import com.ols.lifelog.domain.Tags;
import com.ols.lifelog.domain.User;
import com.ols.lifelog.repository.EventLogRepository;
import com.ols.lifelog.service.EventLogService;
import com.ols.lifelog.service.criteria.EventLogCriteria;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link EventLogResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class EventLogResourceIT {

    private static final UUID DEFAULT_UUID = UUID.randomUUID();
    private static final UUID UPDATED_UUID = UUID.randomUUID();

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DETAIL = "AAAAAAAAAA";
    private static final String UPDATED_DETAIL = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_CREATED_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_CREATED_DATE = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_CREATED_DATE = LocalDate.ofEpochDay(-1L);

    private static final LocalDate DEFAULT_UPDATED_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_UPDATED_DATE = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_UPDATED_DATE = LocalDate.ofEpochDay(-1L);

    private static final String ENTITY_API_URL = "/api/event-logs";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private EventLogRepository eventLogRepository;

    @Mock
    private EventLogRepository eventLogRepositoryMock;

    @Mock
    private EventLogService eventLogServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restEventLogMockMvc;

    private EventLog eventLog;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static EventLog createEntity(EntityManager em) {
        EventLog eventLog = new EventLog()
            .uuid(DEFAULT_UUID)
            .name(DEFAULT_NAME)
            .detail(DEFAULT_DETAIL)
            .createdDate(DEFAULT_CREATED_DATE)
            .updatedDate(DEFAULT_UPDATED_DATE);
        return eventLog;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static EventLog createUpdatedEntity(EntityManager em) {
        EventLog eventLog = new EventLog()
            .uuid(UPDATED_UUID)
            .name(UPDATED_NAME)
            .detail(UPDATED_DETAIL)
            .createdDate(UPDATED_CREATED_DATE)
            .updatedDate(UPDATED_UPDATED_DATE);
        return eventLog;
    }

    @BeforeEach
    public void initTest() {
        eventLog = createEntity(em);
    }

    @Test
    @Transactional
    void createEventLog() throws Exception {
        int databaseSizeBeforeCreate = eventLogRepository.findAll().size();
        // Create the EventLog
        restEventLogMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(eventLog)))
            .andExpect(status().isCreated());

        // Validate the EventLog in the database
        List<EventLog> eventLogList = eventLogRepository.findAll();
        assertThat(eventLogList).hasSize(databaseSizeBeforeCreate + 1);
        EventLog testEventLog = eventLogList.get(eventLogList.size() - 1);
        assertThat(testEventLog.getUuid()).isEqualTo(DEFAULT_UUID);
        assertThat(testEventLog.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testEventLog.getDetail()).isEqualTo(DEFAULT_DETAIL);
        assertThat(testEventLog.getCreatedDate()).isEqualTo(DEFAULT_CREATED_DATE);
        assertThat(testEventLog.getUpdatedDate()).isEqualTo(DEFAULT_UPDATED_DATE);
    }

    @Test
    @Transactional
    void createEventLogWithExistingId() throws Exception {
        // Create the EventLog with an existing ID
        eventLog.setId(1L);

        int databaseSizeBeforeCreate = eventLogRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restEventLogMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(eventLog)))
            .andExpect(status().isBadRequest());

        // Validate the EventLog in the database
        List<EventLog> eventLogList = eventLogRepository.findAll();
        assertThat(eventLogList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkDetailIsRequired() throws Exception {
        int databaseSizeBeforeTest = eventLogRepository.findAll().size();
        // set the field null
        eventLog.setDetail(null);

        // Create the EventLog, which fails.

        restEventLogMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(eventLog)))
            .andExpect(status().isBadRequest());

        List<EventLog> eventLogList = eventLogRepository.findAll();
        assertThat(eventLogList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllEventLogs() throws Exception {
        // Initialize the database
        eventLogRepository.saveAndFlush(eventLog);

        // Get all the eventLogList
        restEventLogMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(eventLog.getId().intValue())))
            .andExpect(jsonPath("$.[*].uuid").value(hasItem(DEFAULT_UUID.toString())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].detail").value(hasItem(DEFAULT_DETAIL)))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(DEFAULT_CREATED_DATE.toString())))
            .andExpect(jsonPath("$.[*].updatedDate").value(hasItem(DEFAULT_UPDATED_DATE.toString())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllEventLogsWithEagerRelationshipsIsEnabled() throws Exception {
        when(eventLogServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restEventLogMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(eventLogServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllEventLogsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(eventLogServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restEventLogMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(eventLogServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    @Transactional
    void getEventLog() throws Exception {
        // Initialize the database
        eventLogRepository.saveAndFlush(eventLog);

        // Get the eventLog
        restEventLogMockMvc
            .perform(get(ENTITY_API_URL_ID, eventLog.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(eventLog.getId().intValue()))
            .andExpect(jsonPath("$.uuid").value(DEFAULT_UUID.toString()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.detail").value(DEFAULT_DETAIL))
            .andExpect(jsonPath("$.createdDate").value(DEFAULT_CREATED_DATE.toString()))
            .andExpect(jsonPath("$.updatedDate").value(DEFAULT_UPDATED_DATE.toString()));
    }

    @Test
    @Transactional
    void getEventLogsByIdFiltering() throws Exception {
        // Initialize the database
        eventLogRepository.saveAndFlush(eventLog);

        Long id = eventLog.getId();

        defaultEventLogShouldBeFound("id.equals=" + id);
        defaultEventLogShouldNotBeFound("id.notEquals=" + id);

        defaultEventLogShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultEventLogShouldNotBeFound("id.greaterThan=" + id);

        defaultEventLogShouldBeFound("id.lessThanOrEqual=" + id);
        defaultEventLogShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllEventLogsByUuidIsEqualToSomething() throws Exception {
        // Initialize the database
        eventLogRepository.saveAndFlush(eventLog);

        // Get all the eventLogList where uuid equals to DEFAULT_UUID
        defaultEventLogShouldBeFound("uuid.equals=" + DEFAULT_UUID);

        // Get all the eventLogList where uuid equals to UPDATED_UUID
        defaultEventLogShouldNotBeFound("uuid.equals=" + UPDATED_UUID);
    }

    @Test
    @Transactional
    void getAllEventLogsByUuidIsNotEqualToSomething() throws Exception {
        // Initialize the database
        eventLogRepository.saveAndFlush(eventLog);

        // Get all the eventLogList where uuid not equals to DEFAULT_UUID
        defaultEventLogShouldNotBeFound("uuid.notEquals=" + DEFAULT_UUID);

        // Get all the eventLogList where uuid not equals to UPDATED_UUID
        defaultEventLogShouldBeFound("uuid.notEquals=" + UPDATED_UUID);
    }

    @Test
    @Transactional
    void getAllEventLogsByUuidIsInShouldWork() throws Exception {
        // Initialize the database
        eventLogRepository.saveAndFlush(eventLog);

        // Get all the eventLogList where uuid in DEFAULT_UUID or UPDATED_UUID
        defaultEventLogShouldBeFound("uuid.in=" + DEFAULT_UUID + "," + UPDATED_UUID);

        // Get all the eventLogList where uuid equals to UPDATED_UUID
        defaultEventLogShouldNotBeFound("uuid.in=" + UPDATED_UUID);
    }

    @Test
    @Transactional
    void getAllEventLogsByUuidIsNullOrNotNull() throws Exception {
        // Initialize the database
        eventLogRepository.saveAndFlush(eventLog);

        // Get all the eventLogList where uuid is not null
        defaultEventLogShouldBeFound("uuid.specified=true");

        // Get all the eventLogList where uuid is null
        defaultEventLogShouldNotBeFound("uuid.specified=false");
    }

    @Test
    @Transactional
    void getAllEventLogsByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        eventLogRepository.saveAndFlush(eventLog);

        // Get all the eventLogList where name equals to DEFAULT_NAME
        defaultEventLogShouldBeFound("name.equals=" + DEFAULT_NAME);

        // Get all the eventLogList where name equals to UPDATED_NAME
        defaultEventLogShouldNotBeFound("name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllEventLogsByNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        eventLogRepository.saveAndFlush(eventLog);

        // Get all the eventLogList where name not equals to DEFAULT_NAME
        defaultEventLogShouldNotBeFound("name.notEquals=" + DEFAULT_NAME);

        // Get all the eventLogList where name not equals to UPDATED_NAME
        defaultEventLogShouldBeFound("name.notEquals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllEventLogsByNameIsInShouldWork() throws Exception {
        // Initialize the database
        eventLogRepository.saveAndFlush(eventLog);

        // Get all the eventLogList where name in DEFAULT_NAME or UPDATED_NAME
        defaultEventLogShouldBeFound("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME);

        // Get all the eventLogList where name equals to UPDATED_NAME
        defaultEventLogShouldNotBeFound("name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllEventLogsByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        eventLogRepository.saveAndFlush(eventLog);

        // Get all the eventLogList where name is not null
        defaultEventLogShouldBeFound("name.specified=true");

        // Get all the eventLogList where name is null
        defaultEventLogShouldNotBeFound("name.specified=false");
    }

    @Test
    @Transactional
    void getAllEventLogsByNameContainsSomething() throws Exception {
        // Initialize the database
        eventLogRepository.saveAndFlush(eventLog);

        // Get all the eventLogList where name contains DEFAULT_NAME
        defaultEventLogShouldBeFound("name.contains=" + DEFAULT_NAME);

        // Get all the eventLogList where name contains UPDATED_NAME
        defaultEventLogShouldNotBeFound("name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllEventLogsByNameNotContainsSomething() throws Exception {
        // Initialize the database
        eventLogRepository.saveAndFlush(eventLog);

        // Get all the eventLogList where name does not contain DEFAULT_NAME
        defaultEventLogShouldNotBeFound("name.doesNotContain=" + DEFAULT_NAME);

        // Get all the eventLogList where name does not contain UPDATED_NAME
        defaultEventLogShouldBeFound("name.doesNotContain=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllEventLogsByDetailIsEqualToSomething() throws Exception {
        // Initialize the database
        eventLogRepository.saveAndFlush(eventLog);

        // Get all the eventLogList where detail equals to DEFAULT_DETAIL
        defaultEventLogShouldBeFound("detail.equals=" + DEFAULT_DETAIL);

        // Get all the eventLogList where detail equals to UPDATED_DETAIL
        defaultEventLogShouldNotBeFound("detail.equals=" + UPDATED_DETAIL);
    }

    @Test
    @Transactional
    void getAllEventLogsByDetailIsNotEqualToSomething() throws Exception {
        // Initialize the database
        eventLogRepository.saveAndFlush(eventLog);

        // Get all the eventLogList where detail not equals to DEFAULT_DETAIL
        defaultEventLogShouldNotBeFound("detail.notEquals=" + DEFAULT_DETAIL);

        // Get all the eventLogList where detail not equals to UPDATED_DETAIL
        defaultEventLogShouldBeFound("detail.notEquals=" + UPDATED_DETAIL);
    }

    @Test
    @Transactional
    void getAllEventLogsByDetailIsInShouldWork() throws Exception {
        // Initialize the database
        eventLogRepository.saveAndFlush(eventLog);

        // Get all the eventLogList where detail in DEFAULT_DETAIL or UPDATED_DETAIL
        defaultEventLogShouldBeFound("detail.in=" + DEFAULT_DETAIL + "," + UPDATED_DETAIL);

        // Get all the eventLogList where detail equals to UPDATED_DETAIL
        defaultEventLogShouldNotBeFound("detail.in=" + UPDATED_DETAIL);
    }

    @Test
    @Transactional
    void getAllEventLogsByDetailIsNullOrNotNull() throws Exception {
        // Initialize the database
        eventLogRepository.saveAndFlush(eventLog);

        // Get all the eventLogList where detail is not null
        defaultEventLogShouldBeFound("detail.specified=true");

        // Get all the eventLogList where detail is null
        defaultEventLogShouldNotBeFound("detail.specified=false");
    }

    @Test
    @Transactional
    void getAllEventLogsByDetailContainsSomething() throws Exception {
        // Initialize the database
        eventLogRepository.saveAndFlush(eventLog);

        // Get all the eventLogList where detail contains DEFAULT_DETAIL
        defaultEventLogShouldBeFound("detail.contains=" + DEFAULT_DETAIL);

        // Get all the eventLogList where detail contains UPDATED_DETAIL
        defaultEventLogShouldNotBeFound("detail.contains=" + UPDATED_DETAIL);
    }

    @Test
    @Transactional
    void getAllEventLogsByDetailNotContainsSomething() throws Exception {
        // Initialize the database
        eventLogRepository.saveAndFlush(eventLog);

        // Get all the eventLogList where detail does not contain DEFAULT_DETAIL
        defaultEventLogShouldNotBeFound("detail.doesNotContain=" + DEFAULT_DETAIL);

        // Get all the eventLogList where detail does not contain UPDATED_DETAIL
        defaultEventLogShouldBeFound("detail.doesNotContain=" + UPDATED_DETAIL);
    }

    @Test
    @Transactional
    void getAllEventLogsByCreatedDateIsEqualToSomething() throws Exception {
        // Initialize the database
        eventLogRepository.saveAndFlush(eventLog);

        // Get all the eventLogList where createdDate equals to DEFAULT_CREATED_DATE
        defaultEventLogShouldBeFound("createdDate.equals=" + DEFAULT_CREATED_DATE);

        // Get all the eventLogList where createdDate equals to UPDATED_CREATED_DATE
        defaultEventLogShouldNotBeFound("createdDate.equals=" + UPDATED_CREATED_DATE);
    }

    @Test
    @Transactional
    void getAllEventLogsByCreatedDateIsNotEqualToSomething() throws Exception {
        // Initialize the database
        eventLogRepository.saveAndFlush(eventLog);

        // Get all the eventLogList where createdDate not equals to DEFAULT_CREATED_DATE
        defaultEventLogShouldNotBeFound("createdDate.notEquals=" + DEFAULT_CREATED_DATE);

        // Get all the eventLogList where createdDate not equals to UPDATED_CREATED_DATE
        defaultEventLogShouldBeFound("createdDate.notEquals=" + UPDATED_CREATED_DATE);
    }

    @Test
    @Transactional
    void getAllEventLogsByCreatedDateIsInShouldWork() throws Exception {
        // Initialize the database
        eventLogRepository.saveAndFlush(eventLog);

        // Get all the eventLogList where createdDate in DEFAULT_CREATED_DATE or UPDATED_CREATED_DATE
        defaultEventLogShouldBeFound("createdDate.in=" + DEFAULT_CREATED_DATE + "," + UPDATED_CREATED_DATE);

        // Get all the eventLogList where createdDate equals to UPDATED_CREATED_DATE
        defaultEventLogShouldNotBeFound("createdDate.in=" + UPDATED_CREATED_DATE);
    }

    @Test
    @Transactional
    void getAllEventLogsByCreatedDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        eventLogRepository.saveAndFlush(eventLog);

        // Get all the eventLogList where createdDate is not null
        defaultEventLogShouldBeFound("createdDate.specified=true");

        // Get all the eventLogList where createdDate is null
        defaultEventLogShouldNotBeFound("createdDate.specified=false");
    }

    @Test
    @Transactional
    void getAllEventLogsByCreatedDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        eventLogRepository.saveAndFlush(eventLog);

        // Get all the eventLogList where createdDate is greater than or equal to DEFAULT_CREATED_DATE
        defaultEventLogShouldBeFound("createdDate.greaterThanOrEqual=" + DEFAULT_CREATED_DATE);

        // Get all the eventLogList where createdDate is greater than or equal to UPDATED_CREATED_DATE
        defaultEventLogShouldNotBeFound("createdDate.greaterThanOrEqual=" + UPDATED_CREATED_DATE);
    }

    @Test
    @Transactional
    void getAllEventLogsByCreatedDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        eventLogRepository.saveAndFlush(eventLog);

        // Get all the eventLogList where createdDate is less than or equal to DEFAULT_CREATED_DATE
        defaultEventLogShouldBeFound("createdDate.lessThanOrEqual=" + DEFAULT_CREATED_DATE);

        // Get all the eventLogList where createdDate is less than or equal to SMALLER_CREATED_DATE
        defaultEventLogShouldNotBeFound("createdDate.lessThanOrEqual=" + SMALLER_CREATED_DATE);
    }

    @Test
    @Transactional
    void getAllEventLogsByCreatedDateIsLessThanSomething() throws Exception {
        // Initialize the database
        eventLogRepository.saveAndFlush(eventLog);

        // Get all the eventLogList where createdDate is less than DEFAULT_CREATED_DATE
        defaultEventLogShouldNotBeFound("createdDate.lessThan=" + DEFAULT_CREATED_DATE);

        // Get all the eventLogList where createdDate is less than UPDATED_CREATED_DATE
        defaultEventLogShouldBeFound("createdDate.lessThan=" + UPDATED_CREATED_DATE);
    }

    @Test
    @Transactional
    void getAllEventLogsByCreatedDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        eventLogRepository.saveAndFlush(eventLog);

        // Get all the eventLogList where createdDate is greater than DEFAULT_CREATED_DATE
        defaultEventLogShouldNotBeFound("createdDate.greaterThan=" + DEFAULT_CREATED_DATE);

        // Get all the eventLogList where createdDate is greater than SMALLER_CREATED_DATE
        defaultEventLogShouldBeFound("createdDate.greaterThan=" + SMALLER_CREATED_DATE);
    }

    @Test
    @Transactional
    void getAllEventLogsByUpdatedDateIsEqualToSomething() throws Exception {
        // Initialize the database
        eventLogRepository.saveAndFlush(eventLog);

        // Get all the eventLogList where updatedDate equals to DEFAULT_UPDATED_DATE
        defaultEventLogShouldBeFound("updatedDate.equals=" + DEFAULT_UPDATED_DATE);

        // Get all the eventLogList where updatedDate equals to UPDATED_UPDATED_DATE
        defaultEventLogShouldNotBeFound("updatedDate.equals=" + UPDATED_UPDATED_DATE);
    }

    @Test
    @Transactional
    void getAllEventLogsByUpdatedDateIsNotEqualToSomething() throws Exception {
        // Initialize the database
        eventLogRepository.saveAndFlush(eventLog);

        // Get all the eventLogList where updatedDate not equals to DEFAULT_UPDATED_DATE
        defaultEventLogShouldNotBeFound("updatedDate.notEquals=" + DEFAULT_UPDATED_DATE);

        // Get all the eventLogList where updatedDate not equals to UPDATED_UPDATED_DATE
        defaultEventLogShouldBeFound("updatedDate.notEquals=" + UPDATED_UPDATED_DATE);
    }

    @Test
    @Transactional
    void getAllEventLogsByUpdatedDateIsInShouldWork() throws Exception {
        // Initialize the database
        eventLogRepository.saveAndFlush(eventLog);

        // Get all the eventLogList where updatedDate in DEFAULT_UPDATED_DATE or UPDATED_UPDATED_DATE
        defaultEventLogShouldBeFound("updatedDate.in=" + DEFAULT_UPDATED_DATE + "," + UPDATED_UPDATED_DATE);

        // Get all the eventLogList where updatedDate equals to UPDATED_UPDATED_DATE
        defaultEventLogShouldNotBeFound("updatedDate.in=" + UPDATED_UPDATED_DATE);
    }

    @Test
    @Transactional
    void getAllEventLogsByUpdatedDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        eventLogRepository.saveAndFlush(eventLog);

        // Get all the eventLogList where updatedDate is not null
        defaultEventLogShouldBeFound("updatedDate.specified=true");

        // Get all the eventLogList where updatedDate is null
        defaultEventLogShouldNotBeFound("updatedDate.specified=false");
    }

    @Test
    @Transactional
    void getAllEventLogsByUpdatedDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        eventLogRepository.saveAndFlush(eventLog);

        // Get all the eventLogList where updatedDate is greater than or equal to DEFAULT_UPDATED_DATE
        defaultEventLogShouldBeFound("updatedDate.greaterThanOrEqual=" + DEFAULT_UPDATED_DATE);

        // Get all the eventLogList where updatedDate is greater than or equal to UPDATED_UPDATED_DATE
        defaultEventLogShouldNotBeFound("updatedDate.greaterThanOrEqual=" + UPDATED_UPDATED_DATE);
    }

    @Test
    @Transactional
    void getAllEventLogsByUpdatedDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        eventLogRepository.saveAndFlush(eventLog);

        // Get all the eventLogList where updatedDate is less than or equal to DEFAULT_UPDATED_DATE
        defaultEventLogShouldBeFound("updatedDate.lessThanOrEqual=" + DEFAULT_UPDATED_DATE);

        // Get all the eventLogList where updatedDate is less than or equal to SMALLER_UPDATED_DATE
        defaultEventLogShouldNotBeFound("updatedDate.lessThanOrEqual=" + SMALLER_UPDATED_DATE);
    }

    @Test
    @Transactional
    void getAllEventLogsByUpdatedDateIsLessThanSomething() throws Exception {
        // Initialize the database
        eventLogRepository.saveAndFlush(eventLog);

        // Get all the eventLogList where updatedDate is less than DEFAULT_UPDATED_DATE
        defaultEventLogShouldNotBeFound("updatedDate.lessThan=" + DEFAULT_UPDATED_DATE);

        // Get all the eventLogList where updatedDate is less than UPDATED_UPDATED_DATE
        defaultEventLogShouldBeFound("updatedDate.lessThan=" + UPDATED_UPDATED_DATE);
    }

    @Test
    @Transactional
    void getAllEventLogsByUpdatedDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        eventLogRepository.saveAndFlush(eventLog);

        // Get all the eventLogList where updatedDate is greater than DEFAULT_UPDATED_DATE
        defaultEventLogShouldNotBeFound("updatedDate.greaterThan=" + DEFAULT_UPDATED_DATE);

        // Get all the eventLogList where updatedDate is greater than SMALLER_UPDATED_DATE
        defaultEventLogShouldBeFound("updatedDate.greaterThan=" + SMALLER_UPDATED_DATE);
    }

    @Test
    @Transactional
    void getAllEventLogsByUserIsEqualToSomething() throws Exception {
        // Initialize the database
        eventLogRepository.saveAndFlush(eventLog);
        User user;
        if (TestUtil.findAll(em, User.class).isEmpty()) {
            user = UserResourceIT.createEntity(em);
            em.persist(user);
            em.flush();
        } else {
            user = TestUtil.findAll(em, User.class).get(0);
        }
        em.persist(user);
        em.flush();
        eventLog.setUser(user);
        eventLogRepository.saveAndFlush(eventLog);
        Long userId = user.getId();

        // Get all the eventLogList where user equals to userId
        defaultEventLogShouldBeFound("userId.equals=" + userId);

        // Get all the eventLogList where user equals to (userId + 1)
        defaultEventLogShouldNotBeFound("userId.equals=" + (userId + 1));
    }

    @Test
    @Transactional
    void getAllEventLogsByTagsIsEqualToSomething() throws Exception {
        // Initialize the database
        eventLogRepository.saveAndFlush(eventLog);
        Tags tags;
        if (TestUtil.findAll(em, Tags.class).isEmpty()) {
            tags = TagsResourceIT.createEntity(em);
            em.persist(tags);
            em.flush();
        } else {
            tags = TestUtil.findAll(em, Tags.class).get(0);
        }
        em.persist(tags);
        em.flush();
        eventLog.addTags(tags);
        eventLogRepository.saveAndFlush(eventLog);
        Long tagsId = tags.getId();

        // Get all the eventLogList where tags equals to tagsId
        defaultEventLogShouldBeFound("tagsId.equals=" + tagsId);

        // Get all the eventLogList where tags equals to (tagsId + 1)
        defaultEventLogShouldNotBeFound("tagsId.equals=" + (tagsId + 1));
    }

    @Test
    @Transactional
    void getAllEventLogsByEventLogBookIsEqualToSomething() throws Exception {
        // Initialize the database
        eventLogRepository.saveAndFlush(eventLog);
        EventLogBook eventLogBook;
        if (TestUtil.findAll(em, EventLogBook.class).isEmpty()) {
            eventLogBook = EventLogBookResourceIT.createEntity(em);
            em.persist(eventLogBook);
            em.flush();
        } else {
            eventLogBook = TestUtil.findAll(em, EventLogBook.class).get(0);
        }
        em.persist(eventLogBook);
        em.flush();
        eventLog.setEventLogBook(eventLogBook);
        eventLogRepository.saveAndFlush(eventLog);
        Long eventLogBookId = eventLogBook.getId();

        // Get all the eventLogList where eventLogBook equals to eventLogBookId
        defaultEventLogShouldBeFound("eventLogBookId.equals=" + eventLogBookId);

        // Get all the eventLogList where eventLogBook equals to (eventLogBookId + 1)
        defaultEventLogShouldNotBeFound("eventLogBookId.equals=" + (eventLogBookId + 1));
    }

    @Test
    @Transactional
    void getAllEventLogsByEventLogTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        eventLogRepository.saveAndFlush(eventLog);
        EventLogType eventLogType;
        if (TestUtil.findAll(em, EventLogType.class).isEmpty()) {
            eventLogType = EventLogTypeResourceIT.createEntity(em);
            em.persist(eventLogType);
            em.flush();
        } else {
            eventLogType = TestUtil.findAll(em, EventLogType.class).get(0);
        }
        em.persist(eventLogType);
        em.flush();
        eventLog.addEventLogType(eventLogType);
        eventLogRepository.saveAndFlush(eventLog);
        Long eventLogTypeId = eventLogType.getId();

        // Get all the eventLogList where eventLogType equals to eventLogTypeId
        defaultEventLogShouldBeFound("eventLogTypeId.equals=" + eventLogTypeId);

        // Get all the eventLogList where eventLogType equals to (eventLogTypeId + 1)
        defaultEventLogShouldNotBeFound("eventLogTypeId.equals=" + (eventLogTypeId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultEventLogShouldBeFound(String filter) throws Exception {
        restEventLogMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(eventLog.getId().intValue())))
            .andExpect(jsonPath("$.[*].uuid").value(hasItem(DEFAULT_UUID.toString())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].detail").value(hasItem(DEFAULT_DETAIL)))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(DEFAULT_CREATED_DATE.toString())))
            .andExpect(jsonPath("$.[*].updatedDate").value(hasItem(DEFAULT_UPDATED_DATE.toString())));

        // Check, that the count call also returns 1
        restEventLogMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultEventLogShouldNotBeFound(String filter) throws Exception {
        restEventLogMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restEventLogMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingEventLog() throws Exception {
        // Get the eventLog
        restEventLogMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewEventLog() throws Exception {
        // Initialize the database
        eventLogRepository.saveAndFlush(eventLog);

        int databaseSizeBeforeUpdate = eventLogRepository.findAll().size();

        // Update the eventLog
        EventLog updatedEventLog = eventLogRepository.findById(eventLog.getId()).get();
        // Disconnect from session so that the updates on updatedEventLog are not directly saved in db
        em.detach(updatedEventLog);
        updatedEventLog
            .uuid(UPDATED_UUID)
            .name(UPDATED_NAME)
            .detail(UPDATED_DETAIL)
            .createdDate(UPDATED_CREATED_DATE)
            .updatedDate(UPDATED_UPDATED_DATE);

        restEventLogMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedEventLog.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedEventLog))
            )
            .andExpect(status().isOk());

        // Validate the EventLog in the database
        List<EventLog> eventLogList = eventLogRepository.findAll();
        assertThat(eventLogList).hasSize(databaseSizeBeforeUpdate);
        EventLog testEventLog = eventLogList.get(eventLogList.size() - 1);
        assertThat(testEventLog.getUuid()).isEqualTo(UPDATED_UUID);
        assertThat(testEventLog.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testEventLog.getDetail()).isEqualTo(UPDATED_DETAIL);
        assertThat(testEventLog.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
        assertThat(testEventLog.getUpdatedDate()).isEqualTo(UPDATED_UPDATED_DATE);
    }

    @Test
    @Transactional
    void putNonExistingEventLog() throws Exception {
        int databaseSizeBeforeUpdate = eventLogRepository.findAll().size();
        eventLog.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEventLogMockMvc
            .perform(
                put(ENTITY_API_URL_ID, eventLog.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(eventLog))
            )
            .andExpect(status().isBadRequest());

        // Validate the EventLog in the database
        List<EventLog> eventLogList = eventLogRepository.findAll();
        assertThat(eventLogList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchEventLog() throws Exception {
        int databaseSizeBeforeUpdate = eventLogRepository.findAll().size();
        eventLog.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEventLogMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(eventLog))
            )
            .andExpect(status().isBadRequest());

        // Validate the EventLog in the database
        List<EventLog> eventLogList = eventLogRepository.findAll();
        assertThat(eventLogList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamEventLog() throws Exception {
        int databaseSizeBeforeUpdate = eventLogRepository.findAll().size();
        eventLog.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEventLogMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(eventLog)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the EventLog in the database
        List<EventLog> eventLogList = eventLogRepository.findAll();
        assertThat(eventLogList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateEventLogWithPatch() throws Exception {
        // Initialize the database
        eventLogRepository.saveAndFlush(eventLog);

        int databaseSizeBeforeUpdate = eventLogRepository.findAll().size();

        // Update the eventLog using partial update
        EventLog partialUpdatedEventLog = new EventLog();
        partialUpdatedEventLog.setId(eventLog.getId());

        partialUpdatedEventLog.uuid(UPDATED_UUID).detail(UPDATED_DETAIL);

        restEventLogMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedEventLog.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedEventLog))
            )
            .andExpect(status().isOk());

        // Validate the EventLog in the database
        List<EventLog> eventLogList = eventLogRepository.findAll();
        assertThat(eventLogList).hasSize(databaseSizeBeforeUpdate);
        EventLog testEventLog = eventLogList.get(eventLogList.size() - 1);
        assertThat(testEventLog.getUuid()).isEqualTo(UPDATED_UUID);
        assertThat(testEventLog.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testEventLog.getDetail()).isEqualTo(UPDATED_DETAIL);
        assertThat(testEventLog.getCreatedDate()).isEqualTo(DEFAULT_CREATED_DATE);
        assertThat(testEventLog.getUpdatedDate()).isEqualTo(DEFAULT_UPDATED_DATE);
    }

    @Test
    @Transactional
    void fullUpdateEventLogWithPatch() throws Exception {
        // Initialize the database
        eventLogRepository.saveAndFlush(eventLog);

        int databaseSizeBeforeUpdate = eventLogRepository.findAll().size();

        // Update the eventLog using partial update
        EventLog partialUpdatedEventLog = new EventLog();
        partialUpdatedEventLog.setId(eventLog.getId());

        partialUpdatedEventLog
            .uuid(UPDATED_UUID)
            .name(UPDATED_NAME)
            .detail(UPDATED_DETAIL)
            .createdDate(UPDATED_CREATED_DATE)
            .updatedDate(UPDATED_UPDATED_DATE);

        restEventLogMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedEventLog.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedEventLog))
            )
            .andExpect(status().isOk());

        // Validate the EventLog in the database
        List<EventLog> eventLogList = eventLogRepository.findAll();
        assertThat(eventLogList).hasSize(databaseSizeBeforeUpdate);
        EventLog testEventLog = eventLogList.get(eventLogList.size() - 1);
        assertThat(testEventLog.getUuid()).isEqualTo(UPDATED_UUID);
        assertThat(testEventLog.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testEventLog.getDetail()).isEqualTo(UPDATED_DETAIL);
        assertThat(testEventLog.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
        assertThat(testEventLog.getUpdatedDate()).isEqualTo(UPDATED_UPDATED_DATE);
    }

    @Test
    @Transactional
    void patchNonExistingEventLog() throws Exception {
        int databaseSizeBeforeUpdate = eventLogRepository.findAll().size();
        eventLog.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEventLogMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, eventLog.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(eventLog))
            )
            .andExpect(status().isBadRequest());

        // Validate the EventLog in the database
        List<EventLog> eventLogList = eventLogRepository.findAll();
        assertThat(eventLogList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchEventLog() throws Exception {
        int databaseSizeBeforeUpdate = eventLogRepository.findAll().size();
        eventLog.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEventLogMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(eventLog))
            )
            .andExpect(status().isBadRequest());

        // Validate the EventLog in the database
        List<EventLog> eventLogList = eventLogRepository.findAll();
        assertThat(eventLogList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamEventLog() throws Exception {
        int databaseSizeBeforeUpdate = eventLogRepository.findAll().size();
        eventLog.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEventLogMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(eventLog)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the EventLog in the database
        List<EventLog> eventLogList = eventLogRepository.findAll();
        assertThat(eventLogList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteEventLog() throws Exception {
        // Initialize the database
        eventLogRepository.saveAndFlush(eventLog);

        int databaseSizeBeforeDelete = eventLogRepository.findAll().size();

        // Delete the eventLog
        restEventLogMockMvc
            .perform(delete(ENTITY_API_URL_ID, eventLog.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<EventLog> eventLogList = eventLogRepository.findAll();
        assertThat(eventLogList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
