package com.ols.lifelog.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.ols.lifelog.IntegrationTest;
import com.ols.lifelog.domain.EventLog;
import com.ols.lifelog.domain.EventLogBook;
import com.ols.lifelog.domain.User;
import com.ols.lifelog.repository.EventLogBookRepository;
import com.ols.lifelog.service.criteria.EventLogBookCriteria;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;
import java.util.Random;
import java.util.UUID;
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
 * Integration tests for the {@link EventLogBookResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class EventLogBookResourceIT {

    private static final UUID DEFAULT_UUID = UUID.randomUUID();
    private static final UUID UPDATED_UUID = UUID.randomUUID();

    private static final LocalDate DEFAULT_CREATED_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_CREATED_DATE = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_CREATED_DATE = LocalDate.ofEpochDay(-1L);

    private static final LocalDate DEFAULT_UPDATED_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_UPDATED_DATE = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_UPDATED_DATE = LocalDate.ofEpochDay(-1L);

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final Boolean DEFAULT_ARCHIEVED = false;
    private static final Boolean UPDATED_ARCHIEVED = true;

    private static final String ENTITY_API_URL = "/api/event-log-books";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private EventLogBookRepository eventLogBookRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restEventLogBookMockMvc;

    private EventLogBook eventLogBook;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static EventLogBook createEntity(EntityManager em) {
        EventLogBook eventLogBook = new EventLogBook()
            .uuid(DEFAULT_UUID)
            .createdDate(DEFAULT_CREATED_DATE)
            .updatedDate(DEFAULT_UPDATED_DATE)
            .name(DEFAULT_NAME)
            .description(DEFAULT_DESCRIPTION)
            .archieved(DEFAULT_ARCHIEVED);
        return eventLogBook;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static EventLogBook createUpdatedEntity(EntityManager em) {
        EventLogBook eventLogBook = new EventLogBook()
            .uuid(UPDATED_UUID)
            .createdDate(UPDATED_CREATED_DATE)
            .updatedDate(UPDATED_UPDATED_DATE)
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .archieved(UPDATED_ARCHIEVED);
        return eventLogBook;
    }

    @BeforeEach
    public void initTest() {
        eventLogBook = createEntity(em);
    }

    @Test
    @Transactional
    void createEventLogBook() throws Exception {
        int databaseSizeBeforeCreate = eventLogBookRepository.findAll().size();
        // Create the EventLogBook
        restEventLogBookMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(eventLogBook)))
            .andExpect(status().isCreated());

        // Validate the EventLogBook in the database
        List<EventLogBook> eventLogBookList = eventLogBookRepository.findAll();
        assertThat(eventLogBookList).hasSize(databaseSizeBeforeCreate + 1);
        EventLogBook testEventLogBook = eventLogBookList.get(eventLogBookList.size() - 1);
        assertThat(testEventLogBook.getUuid()).isEqualTo(DEFAULT_UUID);
        assertThat(testEventLogBook.getCreatedDate()).isEqualTo(DEFAULT_CREATED_DATE);
        assertThat(testEventLogBook.getUpdatedDate()).isEqualTo(DEFAULT_UPDATED_DATE);
        assertThat(testEventLogBook.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testEventLogBook.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testEventLogBook.getArchieved()).isEqualTo(DEFAULT_ARCHIEVED);
    }

    @Test
    @Transactional
    void createEventLogBookWithExistingId() throws Exception {
        // Create the EventLogBook with an existing ID
        eventLogBook.setId(1L);

        int databaseSizeBeforeCreate = eventLogBookRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restEventLogBookMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(eventLogBook)))
            .andExpect(status().isBadRequest());

        // Validate the EventLogBook in the database
        List<EventLogBook> eventLogBookList = eventLogBookRepository.findAll();
        assertThat(eventLogBookList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = eventLogBookRepository.findAll().size();
        // set the field null
        eventLogBook.setName(null);

        // Create the EventLogBook, which fails.

        restEventLogBookMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(eventLogBook)))
            .andExpect(status().isBadRequest());

        List<EventLogBook> eventLogBookList = eventLogBookRepository.findAll();
        assertThat(eventLogBookList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkDescriptionIsRequired() throws Exception {
        int databaseSizeBeforeTest = eventLogBookRepository.findAll().size();
        // set the field null
        eventLogBook.setDescription(null);

        // Create the EventLogBook, which fails.

        restEventLogBookMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(eventLogBook)))
            .andExpect(status().isBadRequest());

        List<EventLogBook> eventLogBookList = eventLogBookRepository.findAll();
        assertThat(eventLogBookList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllEventLogBooks() throws Exception {
        // Initialize the database
        eventLogBookRepository.saveAndFlush(eventLogBook);

        // Get all the eventLogBookList
        restEventLogBookMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(eventLogBook.getId().intValue())))
            .andExpect(jsonPath("$.[*].uuid").value(hasItem(DEFAULT_UUID.toString())))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(DEFAULT_CREATED_DATE.toString())))
            .andExpect(jsonPath("$.[*].updatedDate").value(hasItem(DEFAULT_UPDATED_DATE.toString())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].archieved").value(hasItem(DEFAULT_ARCHIEVED.booleanValue())));
    }

    @Test
    @Transactional
    void getEventLogBook() throws Exception {
        // Initialize the database
        eventLogBookRepository.saveAndFlush(eventLogBook);

        // Get the eventLogBook
        restEventLogBookMockMvc
            .perform(get(ENTITY_API_URL_ID, eventLogBook.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(eventLogBook.getId().intValue()))
            .andExpect(jsonPath("$.uuid").value(DEFAULT_UUID.toString()))
            .andExpect(jsonPath("$.createdDate").value(DEFAULT_CREATED_DATE.toString()))
            .andExpect(jsonPath("$.updatedDate").value(DEFAULT_UPDATED_DATE.toString()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.archieved").value(DEFAULT_ARCHIEVED.booleanValue()));
    }

    @Test
    @Transactional
    void getEventLogBooksByIdFiltering() throws Exception {
        // Initialize the database
        eventLogBookRepository.saveAndFlush(eventLogBook);

        Long id = eventLogBook.getId();

        defaultEventLogBookShouldBeFound("id.equals=" + id);
        defaultEventLogBookShouldNotBeFound("id.notEquals=" + id);

        defaultEventLogBookShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultEventLogBookShouldNotBeFound("id.greaterThan=" + id);

        defaultEventLogBookShouldBeFound("id.lessThanOrEqual=" + id);
        defaultEventLogBookShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllEventLogBooksByUuidIsEqualToSomething() throws Exception {
        // Initialize the database
        eventLogBookRepository.saveAndFlush(eventLogBook);

        // Get all the eventLogBookList where uuid equals to DEFAULT_UUID
        defaultEventLogBookShouldBeFound("uuid.equals=" + DEFAULT_UUID);

        // Get all the eventLogBookList where uuid equals to UPDATED_UUID
        defaultEventLogBookShouldNotBeFound("uuid.equals=" + UPDATED_UUID);
    }

    @Test
    @Transactional
    void getAllEventLogBooksByUuidIsNotEqualToSomething() throws Exception {
        // Initialize the database
        eventLogBookRepository.saveAndFlush(eventLogBook);

        // Get all the eventLogBookList where uuid not equals to DEFAULT_UUID
        defaultEventLogBookShouldNotBeFound("uuid.notEquals=" + DEFAULT_UUID);

        // Get all the eventLogBookList where uuid not equals to UPDATED_UUID
        defaultEventLogBookShouldBeFound("uuid.notEquals=" + UPDATED_UUID);
    }

    @Test
    @Transactional
    void getAllEventLogBooksByUuidIsInShouldWork() throws Exception {
        // Initialize the database
        eventLogBookRepository.saveAndFlush(eventLogBook);

        // Get all the eventLogBookList where uuid in DEFAULT_UUID or UPDATED_UUID
        defaultEventLogBookShouldBeFound("uuid.in=" + DEFAULT_UUID + "," + UPDATED_UUID);

        // Get all the eventLogBookList where uuid equals to UPDATED_UUID
        defaultEventLogBookShouldNotBeFound("uuid.in=" + UPDATED_UUID);
    }

    @Test
    @Transactional
    void getAllEventLogBooksByUuidIsNullOrNotNull() throws Exception {
        // Initialize the database
        eventLogBookRepository.saveAndFlush(eventLogBook);

        // Get all the eventLogBookList where uuid is not null
        defaultEventLogBookShouldBeFound("uuid.specified=true");

        // Get all the eventLogBookList where uuid is null
        defaultEventLogBookShouldNotBeFound("uuid.specified=false");
    }

    @Test
    @Transactional
    void getAllEventLogBooksByCreatedDateIsEqualToSomething() throws Exception {
        // Initialize the database
        eventLogBookRepository.saveAndFlush(eventLogBook);

        // Get all the eventLogBookList where createdDate equals to DEFAULT_CREATED_DATE
        defaultEventLogBookShouldBeFound("createdDate.equals=" + DEFAULT_CREATED_DATE);

        // Get all the eventLogBookList where createdDate equals to UPDATED_CREATED_DATE
        defaultEventLogBookShouldNotBeFound("createdDate.equals=" + UPDATED_CREATED_DATE);
    }

    @Test
    @Transactional
    void getAllEventLogBooksByCreatedDateIsNotEqualToSomething() throws Exception {
        // Initialize the database
        eventLogBookRepository.saveAndFlush(eventLogBook);

        // Get all the eventLogBookList where createdDate not equals to DEFAULT_CREATED_DATE
        defaultEventLogBookShouldNotBeFound("createdDate.notEquals=" + DEFAULT_CREATED_DATE);

        // Get all the eventLogBookList where createdDate not equals to UPDATED_CREATED_DATE
        defaultEventLogBookShouldBeFound("createdDate.notEquals=" + UPDATED_CREATED_DATE);
    }

    @Test
    @Transactional
    void getAllEventLogBooksByCreatedDateIsInShouldWork() throws Exception {
        // Initialize the database
        eventLogBookRepository.saveAndFlush(eventLogBook);

        // Get all the eventLogBookList where createdDate in DEFAULT_CREATED_DATE or UPDATED_CREATED_DATE
        defaultEventLogBookShouldBeFound("createdDate.in=" + DEFAULT_CREATED_DATE + "," + UPDATED_CREATED_DATE);

        // Get all the eventLogBookList where createdDate equals to UPDATED_CREATED_DATE
        defaultEventLogBookShouldNotBeFound("createdDate.in=" + UPDATED_CREATED_DATE);
    }

    @Test
    @Transactional
    void getAllEventLogBooksByCreatedDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        eventLogBookRepository.saveAndFlush(eventLogBook);

        // Get all the eventLogBookList where createdDate is not null
        defaultEventLogBookShouldBeFound("createdDate.specified=true");

        // Get all the eventLogBookList where createdDate is null
        defaultEventLogBookShouldNotBeFound("createdDate.specified=false");
    }

    @Test
    @Transactional
    void getAllEventLogBooksByCreatedDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        eventLogBookRepository.saveAndFlush(eventLogBook);

        // Get all the eventLogBookList where createdDate is greater than or equal to DEFAULT_CREATED_DATE
        defaultEventLogBookShouldBeFound("createdDate.greaterThanOrEqual=" + DEFAULT_CREATED_DATE);

        // Get all the eventLogBookList where createdDate is greater than or equal to UPDATED_CREATED_DATE
        defaultEventLogBookShouldNotBeFound("createdDate.greaterThanOrEqual=" + UPDATED_CREATED_DATE);
    }

    @Test
    @Transactional
    void getAllEventLogBooksByCreatedDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        eventLogBookRepository.saveAndFlush(eventLogBook);

        // Get all the eventLogBookList where createdDate is less than or equal to DEFAULT_CREATED_DATE
        defaultEventLogBookShouldBeFound("createdDate.lessThanOrEqual=" + DEFAULT_CREATED_DATE);

        // Get all the eventLogBookList where createdDate is less than or equal to SMALLER_CREATED_DATE
        defaultEventLogBookShouldNotBeFound("createdDate.lessThanOrEqual=" + SMALLER_CREATED_DATE);
    }

    @Test
    @Transactional
    void getAllEventLogBooksByCreatedDateIsLessThanSomething() throws Exception {
        // Initialize the database
        eventLogBookRepository.saveAndFlush(eventLogBook);

        // Get all the eventLogBookList where createdDate is less than DEFAULT_CREATED_DATE
        defaultEventLogBookShouldNotBeFound("createdDate.lessThan=" + DEFAULT_CREATED_DATE);

        // Get all the eventLogBookList where createdDate is less than UPDATED_CREATED_DATE
        defaultEventLogBookShouldBeFound("createdDate.lessThan=" + UPDATED_CREATED_DATE);
    }

    @Test
    @Transactional
    void getAllEventLogBooksByCreatedDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        eventLogBookRepository.saveAndFlush(eventLogBook);

        // Get all the eventLogBookList where createdDate is greater than DEFAULT_CREATED_DATE
        defaultEventLogBookShouldNotBeFound("createdDate.greaterThan=" + DEFAULT_CREATED_DATE);

        // Get all the eventLogBookList where createdDate is greater than SMALLER_CREATED_DATE
        defaultEventLogBookShouldBeFound("createdDate.greaterThan=" + SMALLER_CREATED_DATE);
    }

    @Test
    @Transactional
    void getAllEventLogBooksByUpdatedDateIsEqualToSomething() throws Exception {
        // Initialize the database
        eventLogBookRepository.saveAndFlush(eventLogBook);

        // Get all the eventLogBookList where updatedDate equals to DEFAULT_UPDATED_DATE
        defaultEventLogBookShouldBeFound("updatedDate.equals=" + DEFAULT_UPDATED_DATE);

        // Get all the eventLogBookList where updatedDate equals to UPDATED_UPDATED_DATE
        defaultEventLogBookShouldNotBeFound("updatedDate.equals=" + UPDATED_UPDATED_DATE);
    }

    @Test
    @Transactional
    void getAllEventLogBooksByUpdatedDateIsNotEqualToSomething() throws Exception {
        // Initialize the database
        eventLogBookRepository.saveAndFlush(eventLogBook);

        // Get all the eventLogBookList where updatedDate not equals to DEFAULT_UPDATED_DATE
        defaultEventLogBookShouldNotBeFound("updatedDate.notEquals=" + DEFAULT_UPDATED_DATE);

        // Get all the eventLogBookList where updatedDate not equals to UPDATED_UPDATED_DATE
        defaultEventLogBookShouldBeFound("updatedDate.notEquals=" + UPDATED_UPDATED_DATE);
    }

    @Test
    @Transactional
    void getAllEventLogBooksByUpdatedDateIsInShouldWork() throws Exception {
        // Initialize the database
        eventLogBookRepository.saveAndFlush(eventLogBook);

        // Get all the eventLogBookList where updatedDate in DEFAULT_UPDATED_DATE or UPDATED_UPDATED_DATE
        defaultEventLogBookShouldBeFound("updatedDate.in=" + DEFAULT_UPDATED_DATE + "," + UPDATED_UPDATED_DATE);

        // Get all the eventLogBookList where updatedDate equals to UPDATED_UPDATED_DATE
        defaultEventLogBookShouldNotBeFound("updatedDate.in=" + UPDATED_UPDATED_DATE);
    }

    @Test
    @Transactional
    void getAllEventLogBooksByUpdatedDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        eventLogBookRepository.saveAndFlush(eventLogBook);

        // Get all the eventLogBookList where updatedDate is not null
        defaultEventLogBookShouldBeFound("updatedDate.specified=true");

        // Get all the eventLogBookList where updatedDate is null
        defaultEventLogBookShouldNotBeFound("updatedDate.specified=false");
    }

    @Test
    @Transactional
    void getAllEventLogBooksByUpdatedDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        eventLogBookRepository.saveAndFlush(eventLogBook);

        // Get all the eventLogBookList where updatedDate is greater than or equal to DEFAULT_UPDATED_DATE
        defaultEventLogBookShouldBeFound("updatedDate.greaterThanOrEqual=" + DEFAULT_UPDATED_DATE);

        // Get all the eventLogBookList where updatedDate is greater than or equal to UPDATED_UPDATED_DATE
        defaultEventLogBookShouldNotBeFound("updatedDate.greaterThanOrEqual=" + UPDATED_UPDATED_DATE);
    }

    @Test
    @Transactional
    void getAllEventLogBooksByUpdatedDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        eventLogBookRepository.saveAndFlush(eventLogBook);

        // Get all the eventLogBookList where updatedDate is less than or equal to DEFAULT_UPDATED_DATE
        defaultEventLogBookShouldBeFound("updatedDate.lessThanOrEqual=" + DEFAULT_UPDATED_DATE);

        // Get all the eventLogBookList where updatedDate is less than or equal to SMALLER_UPDATED_DATE
        defaultEventLogBookShouldNotBeFound("updatedDate.lessThanOrEqual=" + SMALLER_UPDATED_DATE);
    }

    @Test
    @Transactional
    void getAllEventLogBooksByUpdatedDateIsLessThanSomething() throws Exception {
        // Initialize the database
        eventLogBookRepository.saveAndFlush(eventLogBook);

        // Get all the eventLogBookList where updatedDate is less than DEFAULT_UPDATED_DATE
        defaultEventLogBookShouldNotBeFound("updatedDate.lessThan=" + DEFAULT_UPDATED_DATE);

        // Get all the eventLogBookList where updatedDate is less than UPDATED_UPDATED_DATE
        defaultEventLogBookShouldBeFound("updatedDate.lessThan=" + UPDATED_UPDATED_DATE);
    }

    @Test
    @Transactional
    void getAllEventLogBooksByUpdatedDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        eventLogBookRepository.saveAndFlush(eventLogBook);

        // Get all the eventLogBookList where updatedDate is greater than DEFAULT_UPDATED_DATE
        defaultEventLogBookShouldNotBeFound("updatedDate.greaterThan=" + DEFAULT_UPDATED_DATE);

        // Get all the eventLogBookList where updatedDate is greater than SMALLER_UPDATED_DATE
        defaultEventLogBookShouldBeFound("updatedDate.greaterThan=" + SMALLER_UPDATED_DATE);
    }

    @Test
    @Transactional
    void getAllEventLogBooksByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        eventLogBookRepository.saveAndFlush(eventLogBook);

        // Get all the eventLogBookList where name equals to DEFAULT_NAME
        defaultEventLogBookShouldBeFound("name.equals=" + DEFAULT_NAME);

        // Get all the eventLogBookList where name equals to UPDATED_NAME
        defaultEventLogBookShouldNotBeFound("name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllEventLogBooksByNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        eventLogBookRepository.saveAndFlush(eventLogBook);

        // Get all the eventLogBookList where name not equals to DEFAULT_NAME
        defaultEventLogBookShouldNotBeFound("name.notEquals=" + DEFAULT_NAME);

        // Get all the eventLogBookList where name not equals to UPDATED_NAME
        defaultEventLogBookShouldBeFound("name.notEquals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllEventLogBooksByNameIsInShouldWork() throws Exception {
        // Initialize the database
        eventLogBookRepository.saveAndFlush(eventLogBook);

        // Get all the eventLogBookList where name in DEFAULT_NAME or UPDATED_NAME
        defaultEventLogBookShouldBeFound("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME);

        // Get all the eventLogBookList where name equals to UPDATED_NAME
        defaultEventLogBookShouldNotBeFound("name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllEventLogBooksByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        eventLogBookRepository.saveAndFlush(eventLogBook);

        // Get all the eventLogBookList where name is not null
        defaultEventLogBookShouldBeFound("name.specified=true");

        // Get all the eventLogBookList where name is null
        defaultEventLogBookShouldNotBeFound("name.specified=false");
    }

    @Test
    @Transactional
    void getAllEventLogBooksByNameContainsSomething() throws Exception {
        // Initialize the database
        eventLogBookRepository.saveAndFlush(eventLogBook);

        // Get all the eventLogBookList where name contains DEFAULT_NAME
        defaultEventLogBookShouldBeFound("name.contains=" + DEFAULT_NAME);

        // Get all the eventLogBookList where name contains UPDATED_NAME
        defaultEventLogBookShouldNotBeFound("name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllEventLogBooksByNameNotContainsSomething() throws Exception {
        // Initialize the database
        eventLogBookRepository.saveAndFlush(eventLogBook);

        // Get all the eventLogBookList where name does not contain DEFAULT_NAME
        defaultEventLogBookShouldNotBeFound("name.doesNotContain=" + DEFAULT_NAME);

        // Get all the eventLogBookList where name does not contain UPDATED_NAME
        defaultEventLogBookShouldBeFound("name.doesNotContain=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllEventLogBooksByDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        eventLogBookRepository.saveAndFlush(eventLogBook);

        // Get all the eventLogBookList where description equals to DEFAULT_DESCRIPTION
        defaultEventLogBookShouldBeFound("description.equals=" + DEFAULT_DESCRIPTION);

        // Get all the eventLogBookList where description equals to UPDATED_DESCRIPTION
        defaultEventLogBookShouldNotBeFound("description.equals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllEventLogBooksByDescriptionIsNotEqualToSomething() throws Exception {
        // Initialize the database
        eventLogBookRepository.saveAndFlush(eventLogBook);

        // Get all the eventLogBookList where description not equals to DEFAULT_DESCRIPTION
        defaultEventLogBookShouldNotBeFound("description.notEquals=" + DEFAULT_DESCRIPTION);

        // Get all the eventLogBookList where description not equals to UPDATED_DESCRIPTION
        defaultEventLogBookShouldBeFound("description.notEquals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllEventLogBooksByDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        eventLogBookRepository.saveAndFlush(eventLogBook);

        // Get all the eventLogBookList where description in DEFAULT_DESCRIPTION or UPDATED_DESCRIPTION
        defaultEventLogBookShouldBeFound("description.in=" + DEFAULT_DESCRIPTION + "," + UPDATED_DESCRIPTION);

        // Get all the eventLogBookList where description equals to UPDATED_DESCRIPTION
        defaultEventLogBookShouldNotBeFound("description.in=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllEventLogBooksByDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        eventLogBookRepository.saveAndFlush(eventLogBook);

        // Get all the eventLogBookList where description is not null
        defaultEventLogBookShouldBeFound("description.specified=true");

        // Get all the eventLogBookList where description is null
        defaultEventLogBookShouldNotBeFound("description.specified=false");
    }

    @Test
    @Transactional
    void getAllEventLogBooksByDescriptionContainsSomething() throws Exception {
        // Initialize the database
        eventLogBookRepository.saveAndFlush(eventLogBook);

        // Get all the eventLogBookList where description contains DEFAULT_DESCRIPTION
        defaultEventLogBookShouldBeFound("description.contains=" + DEFAULT_DESCRIPTION);

        // Get all the eventLogBookList where description contains UPDATED_DESCRIPTION
        defaultEventLogBookShouldNotBeFound("description.contains=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllEventLogBooksByDescriptionNotContainsSomething() throws Exception {
        // Initialize the database
        eventLogBookRepository.saveAndFlush(eventLogBook);

        // Get all the eventLogBookList where description does not contain DEFAULT_DESCRIPTION
        defaultEventLogBookShouldNotBeFound("description.doesNotContain=" + DEFAULT_DESCRIPTION);

        // Get all the eventLogBookList where description does not contain UPDATED_DESCRIPTION
        defaultEventLogBookShouldBeFound("description.doesNotContain=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllEventLogBooksByArchievedIsEqualToSomething() throws Exception {
        // Initialize the database
        eventLogBookRepository.saveAndFlush(eventLogBook);

        // Get all the eventLogBookList where archieved equals to DEFAULT_ARCHIEVED
        defaultEventLogBookShouldBeFound("archieved.equals=" + DEFAULT_ARCHIEVED);

        // Get all the eventLogBookList where archieved equals to UPDATED_ARCHIEVED
        defaultEventLogBookShouldNotBeFound("archieved.equals=" + UPDATED_ARCHIEVED);
    }

    @Test
    @Transactional
    void getAllEventLogBooksByArchievedIsNotEqualToSomething() throws Exception {
        // Initialize the database
        eventLogBookRepository.saveAndFlush(eventLogBook);

        // Get all the eventLogBookList where archieved not equals to DEFAULT_ARCHIEVED
        defaultEventLogBookShouldNotBeFound("archieved.notEquals=" + DEFAULT_ARCHIEVED);

        // Get all the eventLogBookList where archieved not equals to UPDATED_ARCHIEVED
        defaultEventLogBookShouldBeFound("archieved.notEquals=" + UPDATED_ARCHIEVED);
    }

    @Test
    @Transactional
    void getAllEventLogBooksByArchievedIsInShouldWork() throws Exception {
        // Initialize the database
        eventLogBookRepository.saveAndFlush(eventLogBook);

        // Get all the eventLogBookList where archieved in DEFAULT_ARCHIEVED or UPDATED_ARCHIEVED
        defaultEventLogBookShouldBeFound("archieved.in=" + DEFAULT_ARCHIEVED + "," + UPDATED_ARCHIEVED);

        // Get all the eventLogBookList where archieved equals to UPDATED_ARCHIEVED
        defaultEventLogBookShouldNotBeFound("archieved.in=" + UPDATED_ARCHIEVED);
    }

    @Test
    @Transactional
    void getAllEventLogBooksByArchievedIsNullOrNotNull() throws Exception {
        // Initialize the database
        eventLogBookRepository.saveAndFlush(eventLogBook);

        // Get all the eventLogBookList where archieved is not null
        defaultEventLogBookShouldBeFound("archieved.specified=true");

        // Get all the eventLogBookList where archieved is null
        defaultEventLogBookShouldNotBeFound("archieved.specified=false");
    }

    @Test
    @Transactional
    void getAllEventLogBooksByEventLogIsEqualToSomething() throws Exception {
        // Initialize the database
        eventLogBookRepository.saveAndFlush(eventLogBook);
        EventLog eventLog;
        if (TestUtil.findAll(em, EventLog.class).isEmpty()) {
            eventLog = EventLogResourceIT.createEntity(em);
            em.persist(eventLog);
            em.flush();
        } else {
            eventLog = TestUtil.findAll(em, EventLog.class).get(0);
        }
        em.persist(eventLog);
        em.flush();
        eventLogBook.addEventLog(eventLog);
        eventLogBookRepository.saveAndFlush(eventLogBook);
        Long eventLogId = eventLog.getId();

        // Get all the eventLogBookList where eventLog equals to eventLogId
        defaultEventLogBookShouldBeFound("eventLogId.equals=" + eventLogId);

        // Get all the eventLogBookList where eventLog equals to (eventLogId + 1)
        defaultEventLogBookShouldNotBeFound("eventLogId.equals=" + (eventLogId + 1));
    }

    @Test
    @Transactional
    void getAllEventLogBooksByUserIsEqualToSomething() throws Exception {
        // Initialize the database
        eventLogBookRepository.saveAndFlush(eventLogBook);
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
        eventLogBook.setUser(user);
        eventLogBookRepository.saveAndFlush(eventLogBook);
        Long userId = user.getId();

        // Get all the eventLogBookList where user equals to userId
        defaultEventLogBookShouldBeFound("userId.equals=" + userId);

        // Get all the eventLogBookList where user equals to (userId + 1)
        defaultEventLogBookShouldNotBeFound("userId.equals=" + (userId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultEventLogBookShouldBeFound(String filter) throws Exception {
        restEventLogBookMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(eventLogBook.getId().intValue())))
            .andExpect(jsonPath("$.[*].uuid").value(hasItem(DEFAULT_UUID.toString())))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(DEFAULT_CREATED_DATE.toString())))
            .andExpect(jsonPath("$.[*].updatedDate").value(hasItem(DEFAULT_UPDATED_DATE.toString())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].archieved").value(hasItem(DEFAULT_ARCHIEVED.booleanValue())));

        // Check, that the count call also returns 1
        restEventLogBookMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultEventLogBookShouldNotBeFound(String filter) throws Exception {
        restEventLogBookMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restEventLogBookMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingEventLogBook() throws Exception {
        // Get the eventLogBook
        restEventLogBookMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewEventLogBook() throws Exception {
        // Initialize the database
        eventLogBookRepository.saveAndFlush(eventLogBook);

        int databaseSizeBeforeUpdate = eventLogBookRepository.findAll().size();

        // Update the eventLogBook
        EventLogBook updatedEventLogBook = eventLogBookRepository.findById(eventLogBook.getId()).get();
        // Disconnect from session so that the updates on updatedEventLogBook are not directly saved in db
        em.detach(updatedEventLogBook);
        updatedEventLogBook
            .uuid(UPDATED_UUID)
            .createdDate(UPDATED_CREATED_DATE)
            .updatedDate(UPDATED_UPDATED_DATE)
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .archieved(UPDATED_ARCHIEVED);

        restEventLogBookMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedEventLogBook.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedEventLogBook))
            )
            .andExpect(status().isOk());

        // Validate the EventLogBook in the database
        List<EventLogBook> eventLogBookList = eventLogBookRepository.findAll();
        assertThat(eventLogBookList).hasSize(databaseSizeBeforeUpdate);
        EventLogBook testEventLogBook = eventLogBookList.get(eventLogBookList.size() - 1);
        assertThat(testEventLogBook.getUuid()).isEqualTo(UPDATED_UUID);
        assertThat(testEventLogBook.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
        assertThat(testEventLogBook.getUpdatedDate()).isEqualTo(UPDATED_UPDATED_DATE);
        assertThat(testEventLogBook.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testEventLogBook.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testEventLogBook.getArchieved()).isEqualTo(UPDATED_ARCHIEVED);
    }

    @Test
    @Transactional
    void putNonExistingEventLogBook() throws Exception {
        int databaseSizeBeforeUpdate = eventLogBookRepository.findAll().size();
        eventLogBook.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEventLogBookMockMvc
            .perform(
                put(ENTITY_API_URL_ID, eventLogBook.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(eventLogBook))
            )
            .andExpect(status().isBadRequest());

        // Validate the EventLogBook in the database
        List<EventLogBook> eventLogBookList = eventLogBookRepository.findAll();
        assertThat(eventLogBookList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchEventLogBook() throws Exception {
        int databaseSizeBeforeUpdate = eventLogBookRepository.findAll().size();
        eventLogBook.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEventLogBookMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(eventLogBook))
            )
            .andExpect(status().isBadRequest());

        // Validate the EventLogBook in the database
        List<EventLogBook> eventLogBookList = eventLogBookRepository.findAll();
        assertThat(eventLogBookList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamEventLogBook() throws Exception {
        int databaseSizeBeforeUpdate = eventLogBookRepository.findAll().size();
        eventLogBook.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEventLogBookMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(eventLogBook)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the EventLogBook in the database
        List<EventLogBook> eventLogBookList = eventLogBookRepository.findAll();
        assertThat(eventLogBookList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateEventLogBookWithPatch() throws Exception {
        // Initialize the database
        eventLogBookRepository.saveAndFlush(eventLogBook);

        int databaseSizeBeforeUpdate = eventLogBookRepository.findAll().size();

        // Update the eventLogBook using partial update
        EventLogBook partialUpdatedEventLogBook = new EventLogBook();
        partialUpdatedEventLogBook.setId(eventLogBook.getId());

        partialUpdatedEventLogBook.archieved(UPDATED_ARCHIEVED);

        restEventLogBookMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedEventLogBook.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedEventLogBook))
            )
            .andExpect(status().isOk());

        // Validate the EventLogBook in the database
        List<EventLogBook> eventLogBookList = eventLogBookRepository.findAll();
        assertThat(eventLogBookList).hasSize(databaseSizeBeforeUpdate);
        EventLogBook testEventLogBook = eventLogBookList.get(eventLogBookList.size() - 1);
        assertThat(testEventLogBook.getUuid()).isEqualTo(DEFAULT_UUID);
        assertThat(testEventLogBook.getCreatedDate()).isEqualTo(DEFAULT_CREATED_DATE);
        assertThat(testEventLogBook.getUpdatedDate()).isEqualTo(DEFAULT_UPDATED_DATE);
        assertThat(testEventLogBook.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testEventLogBook.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testEventLogBook.getArchieved()).isEqualTo(UPDATED_ARCHIEVED);
    }

    @Test
    @Transactional
    void fullUpdateEventLogBookWithPatch() throws Exception {
        // Initialize the database
        eventLogBookRepository.saveAndFlush(eventLogBook);

        int databaseSizeBeforeUpdate = eventLogBookRepository.findAll().size();

        // Update the eventLogBook using partial update
        EventLogBook partialUpdatedEventLogBook = new EventLogBook();
        partialUpdatedEventLogBook.setId(eventLogBook.getId());

        partialUpdatedEventLogBook
            .uuid(UPDATED_UUID)
            .createdDate(UPDATED_CREATED_DATE)
            .updatedDate(UPDATED_UPDATED_DATE)
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .archieved(UPDATED_ARCHIEVED);

        restEventLogBookMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedEventLogBook.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedEventLogBook))
            )
            .andExpect(status().isOk());

        // Validate the EventLogBook in the database
        List<EventLogBook> eventLogBookList = eventLogBookRepository.findAll();
        assertThat(eventLogBookList).hasSize(databaseSizeBeforeUpdate);
        EventLogBook testEventLogBook = eventLogBookList.get(eventLogBookList.size() - 1);
        assertThat(testEventLogBook.getUuid()).isEqualTo(UPDATED_UUID);
        assertThat(testEventLogBook.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
        assertThat(testEventLogBook.getUpdatedDate()).isEqualTo(UPDATED_UPDATED_DATE);
        assertThat(testEventLogBook.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testEventLogBook.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testEventLogBook.getArchieved()).isEqualTo(UPDATED_ARCHIEVED);
    }

    @Test
    @Transactional
    void patchNonExistingEventLogBook() throws Exception {
        int databaseSizeBeforeUpdate = eventLogBookRepository.findAll().size();
        eventLogBook.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEventLogBookMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, eventLogBook.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(eventLogBook))
            )
            .andExpect(status().isBadRequest());

        // Validate the EventLogBook in the database
        List<EventLogBook> eventLogBookList = eventLogBookRepository.findAll();
        assertThat(eventLogBookList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchEventLogBook() throws Exception {
        int databaseSizeBeforeUpdate = eventLogBookRepository.findAll().size();
        eventLogBook.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEventLogBookMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(eventLogBook))
            )
            .andExpect(status().isBadRequest());

        // Validate the EventLogBook in the database
        List<EventLogBook> eventLogBookList = eventLogBookRepository.findAll();
        assertThat(eventLogBookList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamEventLogBook() throws Exception {
        int databaseSizeBeforeUpdate = eventLogBookRepository.findAll().size();
        eventLogBook.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEventLogBookMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(eventLogBook))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the EventLogBook in the database
        List<EventLogBook> eventLogBookList = eventLogBookRepository.findAll();
        assertThat(eventLogBookList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteEventLogBook() throws Exception {
        // Initialize the database
        eventLogBookRepository.saveAndFlush(eventLogBook);

        int databaseSizeBeforeDelete = eventLogBookRepository.findAll().size();

        // Delete the eventLogBook
        restEventLogBookMockMvc
            .perform(delete(ENTITY_API_URL_ID, eventLogBook.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<EventLogBook> eventLogBookList = eventLogBookRepository.findAll();
        assertThat(eventLogBookList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
