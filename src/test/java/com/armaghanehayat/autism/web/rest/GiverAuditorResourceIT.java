package com.armaghanehayat.autism.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.armaghanehayat.autism.IntegrationTest;
import com.armaghanehayat.autism.domain.Giver;
import com.armaghanehayat.autism.domain.GiverAuditor;
import com.armaghanehayat.autism.domain.User;
import com.armaghanehayat.autism.repository.GiverAuditorRepository;
import com.armaghanehayat.autism.service.criteria.GiverAuditorCriteria;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
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
 * Integration tests for the {@link GiverAuditorResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class GiverAuditorResourceIT {

    private static final String DEFAULT_FIELD_NAME = "AAAAAAAAAA";
    private static final String UPDATED_FIELD_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_OLD_VALUE = "AAAAAAAAAA";
    private static final String UPDATED_OLD_VALUE = "BBBBBBBBBB";

    private static final String DEFAULT_NEW_VALUE = "AAAAAAAAAA";
    private static final String UPDATED_NEW_VALUE = "BBBBBBBBBB";

    private static final Instant DEFAULT_CHANGE_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CHANGE_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/giver-auditors";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private GiverAuditorRepository giverAuditorRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restGiverAuditorMockMvc;

    private GiverAuditor giverAuditor;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static GiverAuditor createEntity(EntityManager em) {
        GiverAuditor giverAuditor = new GiverAuditor()
            .fieldName(DEFAULT_FIELD_NAME)
            .oldValue(DEFAULT_OLD_VALUE)
            .newValue(DEFAULT_NEW_VALUE)
            .changeDate(DEFAULT_CHANGE_DATE);
        return giverAuditor;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static GiverAuditor createUpdatedEntity(EntityManager em) {
        GiverAuditor giverAuditor = new GiverAuditor()
            .fieldName(UPDATED_FIELD_NAME)
            .oldValue(UPDATED_OLD_VALUE)
            .newValue(UPDATED_NEW_VALUE)
            .changeDate(UPDATED_CHANGE_DATE);
        return giverAuditor;
    }

    @BeforeEach
    public void initTest() {
        giverAuditor = createEntity(em);
    }

    @Test
    @Transactional
    void createGiverAuditor() throws Exception {
        int databaseSizeBeforeCreate = giverAuditorRepository.findAll().size();
        // Create the GiverAuditor
        restGiverAuditorMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(giverAuditor)))
            .andExpect(status().isCreated());

        // Validate the GiverAuditor in the database
        List<GiverAuditor> giverAuditorList = giverAuditorRepository.findAll();
        assertThat(giverAuditorList).hasSize(databaseSizeBeforeCreate + 1);
        GiverAuditor testGiverAuditor = giverAuditorList.get(giverAuditorList.size() - 1);
        assertThat(testGiverAuditor.getFieldName()).isEqualTo(DEFAULT_FIELD_NAME);
        assertThat(testGiverAuditor.getOldValue()).isEqualTo(DEFAULT_OLD_VALUE);
        assertThat(testGiverAuditor.getNewValue()).isEqualTo(DEFAULT_NEW_VALUE);
        assertThat(testGiverAuditor.getChangeDate()).isEqualTo(DEFAULT_CHANGE_DATE);
    }

    @Test
    @Transactional
    void createGiverAuditorWithExistingId() throws Exception {
        // Create the GiverAuditor with an existing ID
        giverAuditor.setId(1L);

        int databaseSizeBeforeCreate = giverAuditorRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restGiverAuditorMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(giverAuditor)))
            .andExpect(status().isBadRequest());

        // Validate the GiverAuditor in the database
        List<GiverAuditor> giverAuditorList = giverAuditorRepository.findAll();
        assertThat(giverAuditorList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkFieldNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = giverAuditorRepository.findAll().size();
        // set the field null
        giverAuditor.setFieldName(null);

        // Create the GiverAuditor, which fails.

        restGiverAuditorMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(giverAuditor)))
            .andExpect(status().isBadRequest());

        List<GiverAuditor> giverAuditorList = giverAuditorRepository.findAll();
        assertThat(giverAuditorList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkOldValueIsRequired() throws Exception {
        int databaseSizeBeforeTest = giverAuditorRepository.findAll().size();
        // set the field null
        giverAuditor.setOldValue(null);

        // Create the GiverAuditor, which fails.

        restGiverAuditorMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(giverAuditor)))
            .andExpect(status().isBadRequest());

        List<GiverAuditor> giverAuditorList = giverAuditorRepository.findAll();
        assertThat(giverAuditorList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkNewValueIsRequired() throws Exception {
        int databaseSizeBeforeTest = giverAuditorRepository.findAll().size();
        // set the field null
        giverAuditor.setNewValue(null);

        // Create the GiverAuditor, which fails.

        restGiverAuditorMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(giverAuditor)))
            .andExpect(status().isBadRequest());

        List<GiverAuditor> giverAuditorList = giverAuditorRepository.findAll();
        assertThat(giverAuditorList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkChangeDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = giverAuditorRepository.findAll().size();
        // set the field null
        giverAuditor.setChangeDate(null);

        // Create the GiverAuditor, which fails.

        restGiverAuditorMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(giverAuditor)))
            .andExpect(status().isBadRequest());

        List<GiverAuditor> giverAuditorList = giverAuditorRepository.findAll();
        assertThat(giverAuditorList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllGiverAuditors() throws Exception {
        // Initialize the database
        giverAuditorRepository.saveAndFlush(giverAuditor);

        // Get all the giverAuditorList
        restGiverAuditorMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(giverAuditor.getId().intValue())))
            .andExpect(jsonPath("$.[*].fieldName").value(hasItem(DEFAULT_FIELD_NAME)))
            .andExpect(jsonPath("$.[*].oldValue").value(hasItem(DEFAULT_OLD_VALUE)))
            .andExpect(jsonPath("$.[*].newValue").value(hasItem(DEFAULT_NEW_VALUE)))
            .andExpect(jsonPath("$.[*].changeDate").value(hasItem(DEFAULT_CHANGE_DATE.toString())));
    }

    @Test
    @Transactional
    void getGiverAuditor() throws Exception {
        // Initialize the database
        giverAuditorRepository.saveAndFlush(giverAuditor);

        // Get the giverAuditor
        restGiverAuditorMockMvc
            .perform(get(ENTITY_API_URL_ID, giverAuditor.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(giverAuditor.getId().intValue()))
            .andExpect(jsonPath("$.fieldName").value(DEFAULT_FIELD_NAME))
            .andExpect(jsonPath("$.oldValue").value(DEFAULT_OLD_VALUE))
            .andExpect(jsonPath("$.newValue").value(DEFAULT_NEW_VALUE))
            .andExpect(jsonPath("$.changeDate").value(DEFAULT_CHANGE_DATE.toString()));
    }

    @Test
    @Transactional
    void getGiverAuditorsByIdFiltering() throws Exception {
        // Initialize the database
        giverAuditorRepository.saveAndFlush(giverAuditor);

        Long id = giverAuditor.getId();

        defaultGiverAuditorShouldBeFound("id.equals=" + id);
        defaultGiverAuditorShouldNotBeFound("id.notEquals=" + id);

        defaultGiverAuditorShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultGiverAuditorShouldNotBeFound("id.greaterThan=" + id);

        defaultGiverAuditorShouldBeFound("id.lessThanOrEqual=" + id);
        defaultGiverAuditorShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllGiverAuditorsByFieldNameIsEqualToSomething() throws Exception {
        // Initialize the database
        giverAuditorRepository.saveAndFlush(giverAuditor);

        // Get all the giverAuditorList where fieldName equals to DEFAULT_field_NAME
        defaultGiverAuditorShouldBeFound("fieldName.equals=" + DEFAULT_FIELD_NAME);

        // Get all the giverAuditorList where fieldName equals to UPDATED_field_NAME
        defaultGiverAuditorShouldNotBeFound("fieldName.equals=" + UPDATED_FIELD_NAME);
    }

    @Test
    @Transactional
    void getAllGiverAuditorsByFieldNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        giverAuditorRepository.saveAndFlush(giverAuditor);

        // Get all the giverAuditorList where fiedlName not equals to DEFAULT_FIEDL_NAME
        defaultGiverAuditorShouldNotBeFound("fieldName.notEquals=" + DEFAULT_FIELD_NAME);

        // Get all the giverAuditorList where fiedlName not equals to UPDATED_FIEDL_NAME
        defaultGiverAuditorShouldBeFound("fieldName.notEquals=" + UPDATED_FIELD_NAME);
    }

    @Test
    @Transactional
    void getAllGiverAuditorsByFieldNameIsInShouldWork() throws Exception {
        // Initialize the database
        giverAuditorRepository.saveAndFlush(giverAuditor);

        // Get all the giverAuditorList where fiedlName in DEFAULT_FIEDL_NAME or UPDATED_FIEDL_NAME
        defaultGiverAuditorShouldBeFound("fieldName.in=" + DEFAULT_FIELD_NAME + "," + UPDATED_FIELD_NAME);

        // Get all the giverAuditorList where fiedlName equals to UPDATED_FIEDL_NAME
        defaultGiverAuditorShouldNotBeFound("fieldName.in=" + UPDATED_FIELD_NAME);
    }

    @Test
    @Transactional
    void getAllGiverAuditorsByFieldNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        giverAuditorRepository.saveAndFlush(giverAuditor);

        // Get all the giverAuditorList where fiedlName is not null
        defaultGiverAuditorShouldBeFound("fieldName.specified=true");

        // Get all the giverAuditorList where fiedlName is null
        defaultGiverAuditorShouldNotBeFound("fieldName.specified=false");
    }

    @Test
    @Transactional
    void getAllGiverAuditorsByFieldNameContainsSomething() throws Exception {
        // Initialize the database
        giverAuditorRepository.saveAndFlush(giverAuditor);

        // Get all the giverAuditorList where fiedlName contains DEFAULT_FIEDL_NAME
        defaultGiverAuditorShouldBeFound("fieldName.contains=" + DEFAULT_FIELD_NAME);

        // Get all the giverAuditorList where fiedlName contains UPDATED_FIEDL_NAME
        defaultGiverAuditorShouldNotBeFound("fieldName.contains=" + UPDATED_FIELD_NAME);
    }

    @Test
    @Transactional
    void getAllGiverAuditorsByFieldNameNotContainsSomething() throws Exception {
        // Initialize the database
        giverAuditorRepository.saveAndFlush(giverAuditor);

        // Get all the giverAuditorList where fiedlName does not contain DEFAULT_FIEDL_NAME
        defaultGiverAuditorShouldNotBeFound("fieldName.doesNotContain=" + UPDATED_FIELD_NAME);

        // Get all the giverAuditorList where fiedlName does not contain UPDATED_FIEDL_NAME
        defaultGiverAuditorShouldBeFound("fieldName.doesNotContain=" + UPDATED_FIELD_NAME);
    }

    @Test
    @Transactional
    void getAllGiverAuditorsByOldValueIsEqualToSomething() throws Exception {
        // Initialize the database
        giverAuditorRepository.saveAndFlush(giverAuditor);

        // Get all the giverAuditorList where oldValue equals to DEFAULT_OLD_VALUE
        defaultGiverAuditorShouldBeFound("oldValue.equals=" + DEFAULT_OLD_VALUE);

        // Get all the giverAuditorList where oldValue equals to UPDATED_OLD_VALUE
        defaultGiverAuditorShouldNotBeFound("oldValue.equals=" + UPDATED_OLD_VALUE);
    }

    @Test
    @Transactional
    void getAllGiverAuditorsByOldValueIsNotEqualToSomething() throws Exception {
        // Initialize the database
        giverAuditorRepository.saveAndFlush(giverAuditor);

        // Get all the giverAuditorList where oldValue not equals to DEFAULT_OLD_VALUE
        defaultGiverAuditorShouldNotBeFound("oldValue.notEquals=" + DEFAULT_OLD_VALUE);

        // Get all the giverAuditorList where oldValue not equals to UPDATED_OLD_VALUE
        defaultGiverAuditorShouldBeFound("oldValue.notEquals=" + UPDATED_OLD_VALUE);
    }

    @Test
    @Transactional
    void getAllGiverAuditorsByOldValueIsInShouldWork() throws Exception {
        // Initialize the database
        giverAuditorRepository.saveAndFlush(giverAuditor);

        // Get all the giverAuditorList where oldValue in DEFAULT_OLD_VALUE or UPDATED_OLD_VALUE
        defaultGiverAuditorShouldBeFound("oldValue.in=" + DEFAULT_OLD_VALUE + "," + UPDATED_OLD_VALUE);

        // Get all the giverAuditorList where oldValue equals to UPDATED_OLD_VALUE
        defaultGiverAuditorShouldNotBeFound("oldValue.in=" + UPDATED_OLD_VALUE);
    }

    @Test
    @Transactional
    void getAllGiverAuditorsByOldValueIsNullOrNotNull() throws Exception {
        // Initialize the database
        giverAuditorRepository.saveAndFlush(giverAuditor);

        // Get all the giverAuditorList where oldValue is not null
        defaultGiverAuditorShouldBeFound("oldValue.specified=true");

        // Get all the giverAuditorList where oldValue is null
        defaultGiverAuditorShouldNotBeFound("oldValue.specified=false");
    }

    @Test
    @Transactional
    void getAllGiverAuditorsByOldValueContainsSomething() throws Exception {
        // Initialize the database
        giverAuditorRepository.saveAndFlush(giverAuditor);

        // Get all the giverAuditorList where oldValue contains DEFAULT_OLD_VALUE
        defaultGiverAuditorShouldBeFound("oldValue.contains=" + DEFAULT_OLD_VALUE);

        // Get all the giverAuditorList where oldValue contains UPDATED_OLD_VALUE
        defaultGiverAuditorShouldNotBeFound("oldValue.contains=" + UPDATED_OLD_VALUE);
    }

    @Test
    @Transactional
    void getAllGiverAuditorsByOldValueNotContainsSomething() throws Exception {
        // Initialize the database
        giverAuditorRepository.saveAndFlush(giverAuditor);

        // Get all the giverAuditorList where oldValue does not contain DEFAULT_OLD_VALUE
        defaultGiverAuditorShouldNotBeFound("oldValue.doesNotContain=" + DEFAULT_OLD_VALUE);

        // Get all the giverAuditorList where oldValue does not contain UPDATED_OLD_VALUE
        defaultGiverAuditorShouldBeFound("oldValue.doesNotContain=" + UPDATED_OLD_VALUE);
    }

    @Test
    @Transactional
    void getAllGiverAuditorsByNewValueIsEqualToSomething() throws Exception {
        // Initialize the database
        giverAuditorRepository.saveAndFlush(giverAuditor);

        // Get all the giverAuditorList where newValue equals to DEFAULT_NEW_VALUE
        defaultGiverAuditorShouldBeFound("newValue.equals=" + DEFAULT_NEW_VALUE);

        // Get all the giverAuditorList where newValue equals to UPDATED_NEW_VALUE
        defaultGiverAuditorShouldNotBeFound("newValue.equals=" + UPDATED_NEW_VALUE);
    }

    @Test
    @Transactional
    void getAllGiverAuditorsByNewValueIsNotEqualToSomething() throws Exception {
        // Initialize the database
        giverAuditorRepository.saveAndFlush(giverAuditor);

        // Get all the giverAuditorList where newValue not equals to DEFAULT_NEW_VALUE
        defaultGiverAuditorShouldNotBeFound("newValue.notEquals=" + DEFAULT_NEW_VALUE);

        // Get all the giverAuditorList where newValue not equals to UPDATED_NEW_VALUE
        defaultGiverAuditorShouldBeFound("newValue.notEquals=" + UPDATED_NEW_VALUE);
    }

    @Test
    @Transactional
    void getAllGiverAuditorsByNewValueIsInShouldWork() throws Exception {
        // Initialize the database
        giverAuditorRepository.saveAndFlush(giverAuditor);

        // Get all the giverAuditorList where newValue in DEFAULT_NEW_VALUE or UPDATED_NEW_VALUE
        defaultGiverAuditorShouldBeFound("newValue.in=" + DEFAULT_NEW_VALUE + "," + UPDATED_NEW_VALUE);

        // Get all the giverAuditorList where newValue equals to UPDATED_NEW_VALUE
        defaultGiverAuditorShouldNotBeFound("newValue.in=" + UPDATED_NEW_VALUE);
    }

    @Test
    @Transactional
    void getAllGiverAuditorsByNewValueIsNullOrNotNull() throws Exception {
        // Initialize the database
        giverAuditorRepository.saveAndFlush(giverAuditor);

        // Get all the giverAuditorList where newValue is not null
        defaultGiverAuditorShouldBeFound("newValue.specified=true");

        // Get all the giverAuditorList where newValue is null
        defaultGiverAuditorShouldNotBeFound("newValue.specified=false");
    }

    @Test
    @Transactional
    void getAllGiverAuditorsByNewValueContainsSomething() throws Exception {
        // Initialize the database
        giverAuditorRepository.saveAndFlush(giverAuditor);

        // Get all the giverAuditorList where newValue contains DEFAULT_NEW_VALUE
        defaultGiverAuditorShouldBeFound("newValue.contains=" + DEFAULT_NEW_VALUE);

        // Get all the giverAuditorList where newValue contains UPDATED_NEW_VALUE
        defaultGiverAuditorShouldNotBeFound("newValue.contains=" + UPDATED_NEW_VALUE);
    }

    @Test
    @Transactional
    void getAllGiverAuditorsByNewValueNotContainsSomething() throws Exception {
        // Initialize the database
        giverAuditorRepository.saveAndFlush(giverAuditor);

        // Get all the giverAuditorList where newValue does not contain DEFAULT_NEW_VALUE
        defaultGiverAuditorShouldNotBeFound("newValue.doesNotContain=" + DEFAULT_NEW_VALUE);

        // Get all the giverAuditorList where newValue does not contain UPDATED_NEW_VALUE
        defaultGiverAuditorShouldBeFound("newValue.doesNotContain=" + UPDATED_NEW_VALUE);
    }

    @Test
    @Transactional
    void getAllGiverAuditorsByChangeDateIsEqualToSomething() throws Exception {
        // Initialize the database
        giverAuditorRepository.saveAndFlush(giverAuditor);

        // Get all the giverAuditorList where changeDate equals to DEFAULT_CHANGE_DATE
        defaultGiverAuditorShouldBeFound("changeDate.equals=" + DEFAULT_CHANGE_DATE);

        // Get all the giverAuditorList where changeDate equals to UPDATED_CHANGE_DATE
        defaultGiverAuditorShouldNotBeFound("changeDate.equals=" + UPDATED_CHANGE_DATE);
    }

    @Test
    @Transactional
    void getAllGiverAuditorsByChangeDateIsNotEqualToSomething() throws Exception {
        // Initialize the database
        giverAuditorRepository.saveAndFlush(giverAuditor);

        // Get all the giverAuditorList where changeDate not equals to DEFAULT_CHANGE_DATE
        defaultGiverAuditorShouldNotBeFound("changeDate.notEquals=" + DEFAULT_CHANGE_DATE);

        // Get all the giverAuditorList where changeDate not equals to UPDATED_CHANGE_DATE
        defaultGiverAuditorShouldBeFound("changeDate.notEquals=" + UPDATED_CHANGE_DATE);
    }

    @Test
    @Transactional
    void getAllGiverAuditorsByChangeDateIsInShouldWork() throws Exception {
        // Initialize the database
        giverAuditorRepository.saveAndFlush(giverAuditor);

        // Get all the giverAuditorList where changeDate in DEFAULT_CHANGE_DATE or UPDATED_CHANGE_DATE
        defaultGiverAuditorShouldBeFound("changeDate.in=" + DEFAULT_CHANGE_DATE + "," + UPDATED_CHANGE_DATE);

        // Get all the giverAuditorList where changeDate equals to UPDATED_CHANGE_DATE
        defaultGiverAuditorShouldNotBeFound("changeDate.in=" + UPDATED_CHANGE_DATE);
    }

    @Test
    @Transactional
    void getAllGiverAuditorsByChangeDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        giverAuditorRepository.saveAndFlush(giverAuditor);

        // Get all the giverAuditorList where changeDate is not null
        defaultGiverAuditorShouldBeFound("changeDate.specified=true");

        // Get all the giverAuditorList where changeDate is null
        defaultGiverAuditorShouldNotBeFound("changeDate.specified=false");
    }

    @Test
    @Transactional
    void getAllGiverAuditorsByAuditorIsEqualToSomething() throws Exception {
        // Initialize the database
        giverAuditorRepository.saveAndFlush(giverAuditor);
        User auditor = UserResourceIT.createEntity(em);
        em.persist(auditor);
        em.flush();
        giverAuditor.setAuditor(auditor);
        giverAuditorRepository.saveAndFlush(giverAuditor);
        Long auditorId = auditor.getId();

        // Get all the giverAuditorList where auditor equals to auditorId
        defaultGiverAuditorShouldBeFound("auditorId.equals=" + auditorId);

        // Get all the giverAuditorList where auditor equals to (auditorId + 1)
        defaultGiverAuditorShouldNotBeFound("auditorId.equals=" + (auditorId + 1));
    }

    @Test
    @Transactional
    void getAllGiverAuditorsByGiverIsEqualToSomething() throws Exception {
        // Initialize the database
        giverAuditorRepository.saveAndFlush(giverAuditor);
        Giver giver = GiverResourceIT.createEntity(em);
        em.persist(giver);
        em.flush();
        giverAuditor.setGiver(giver);
        giverAuditorRepository.saveAndFlush(giverAuditor);
        Long giverId = giver.getId();

        // Get all the giverAuditorList where giver equals to giverId
        defaultGiverAuditorShouldBeFound("giverId.equals=" + giverId);

        // Get all the giverAuditorList where giver equals to (giverId + 1)
        defaultGiverAuditorShouldNotBeFound("giverId.equals=" + (giverId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultGiverAuditorShouldBeFound(String filter) throws Exception {
        restGiverAuditorMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(giverAuditor.getId().intValue())))
            .andExpect(jsonPath("$.[*].fieldName").value(hasItem(DEFAULT_FIELD_NAME)))
            .andExpect(jsonPath("$.[*].oldValue").value(hasItem(DEFAULT_OLD_VALUE)))
            .andExpect(jsonPath("$.[*].newValue").value(hasItem(DEFAULT_NEW_VALUE)))
            .andExpect(jsonPath("$.[*].changeDate").value(hasItem(DEFAULT_CHANGE_DATE.toString())));

        // Check, that the count call also returns 1
        restGiverAuditorMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultGiverAuditorShouldNotBeFound(String filter) throws Exception {
        restGiverAuditorMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restGiverAuditorMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingGiverAuditor() throws Exception {
        // Get the giverAuditor
        restGiverAuditorMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewGiverAuditor() throws Exception {
        // Initialize the database
        giverAuditorRepository.saveAndFlush(giverAuditor);

        int databaseSizeBeforeUpdate = giverAuditorRepository.findAll().size();

        // Update the giverAuditor
        GiverAuditor updatedGiverAuditor = giverAuditorRepository.findById(giverAuditor.getId()).get();
        // Disconnect from session so that the updates on updatedGiverAuditor are not directly saved in db
        em.detach(updatedGiverAuditor);
        updatedGiverAuditor
            .fieldName(UPDATED_FIELD_NAME)
            .oldValue(UPDATED_OLD_VALUE)
            .newValue(UPDATED_NEW_VALUE)
            .changeDate(UPDATED_CHANGE_DATE);

        restGiverAuditorMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedGiverAuditor.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedGiverAuditor))
            )
            .andExpect(status().isOk());

        // Validate the GiverAuditor in the database
        List<GiverAuditor> giverAuditorList = giverAuditorRepository.findAll();
        assertThat(giverAuditorList).hasSize(databaseSizeBeforeUpdate);
        GiverAuditor testGiverAuditor = giverAuditorList.get(giverAuditorList.size() - 1);
        assertThat(testGiverAuditor.getFieldName()).isEqualTo(UPDATED_FIELD_NAME);
        assertThat(testGiverAuditor.getOldValue()).isEqualTo(UPDATED_OLD_VALUE);
        assertThat(testGiverAuditor.getNewValue()).isEqualTo(UPDATED_NEW_VALUE);
        assertThat(testGiverAuditor.getChangeDate()).isEqualTo(UPDATED_CHANGE_DATE);
    }

    @Test
    @Transactional
    void putNonExistingGiverAuditor() throws Exception {
        int databaseSizeBeforeUpdate = giverAuditorRepository.findAll().size();
        giverAuditor.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restGiverAuditorMockMvc
            .perform(
                put(ENTITY_API_URL_ID, giverAuditor.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(giverAuditor))
            )
            .andExpect(status().isBadRequest());

        // Validate the GiverAuditor in the database
        List<GiverAuditor> giverAuditorList = giverAuditorRepository.findAll();
        assertThat(giverAuditorList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchGiverAuditor() throws Exception {
        int databaseSizeBeforeUpdate = giverAuditorRepository.findAll().size();
        giverAuditor.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restGiverAuditorMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(giverAuditor))
            )
            .andExpect(status().isBadRequest());

        // Validate the GiverAuditor in the database
        List<GiverAuditor> giverAuditorList = giverAuditorRepository.findAll();
        assertThat(giverAuditorList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamGiverAuditor() throws Exception {
        int databaseSizeBeforeUpdate = giverAuditorRepository.findAll().size();
        giverAuditor.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restGiverAuditorMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(giverAuditor)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the GiverAuditor in the database
        List<GiverAuditor> giverAuditorList = giverAuditorRepository.findAll();
        assertThat(giverAuditorList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateGiverAuditorWithPatch() throws Exception {
        // Initialize the database
        giverAuditorRepository.saveAndFlush(giverAuditor);

        int databaseSizeBeforeUpdate = giverAuditorRepository.findAll().size();

        // Update the giverAuditor using partial update
        GiverAuditor partialUpdatedGiverAuditor = new GiverAuditor();
        partialUpdatedGiverAuditor.setId(giverAuditor.getId());

        partialUpdatedGiverAuditor.oldValue(UPDATED_OLD_VALUE).newValue(UPDATED_NEW_VALUE);

        restGiverAuditorMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedGiverAuditor.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedGiverAuditor))
            )
            .andExpect(status().isOk());

        // Validate the GiverAuditor in the database
        List<GiverAuditor> giverAuditorList = giverAuditorRepository.findAll();
        assertThat(giverAuditorList).hasSize(databaseSizeBeforeUpdate);
        GiverAuditor testGiverAuditor = giverAuditorList.get(giverAuditorList.size() - 1);
        assertThat(testGiverAuditor.getFieldName()).isEqualTo(DEFAULT_FIELD_NAME);
        assertThat(testGiverAuditor.getOldValue()).isEqualTo(UPDATED_OLD_VALUE);
        assertThat(testGiverAuditor.getNewValue()).isEqualTo(UPDATED_NEW_VALUE);
        assertThat(testGiverAuditor.getChangeDate()).isEqualTo(DEFAULT_CHANGE_DATE);
    }

    @Test
    @Transactional
    void fullUpdateGiverAuditorWithPatch() throws Exception {
        // Initialize the database
        giverAuditorRepository.saveAndFlush(giverAuditor);

        int databaseSizeBeforeUpdate = giverAuditorRepository.findAll().size();

        // Update the giverAuditor using partial update
        GiverAuditor partialUpdatedGiverAuditor = new GiverAuditor();
        partialUpdatedGiverAuditor.setId(giverAuditor.getId());

        partialUpdatedGiverAuditor
            .fieldName(UPDATED_FIELD_NAME)
            .oldValue(UPDATED_OLD_VALUE)
            .newValue(UPDATED_NEW_VALUE)
            .changeDate(UPDATED_CHANGE_DATE);

        restGiverAuditorMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedGiverAuditor.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedGiverAuditor))
            )
            .andExpect(status().isOk());

        // Validate the GiverAuditor in the database
        List<GiverAuditor> giverAuditorList = giverAuditorRepository.findAll();
        assertThat(giverAuditorList).hasSize(databaseSizeBeforeUpdate);
        GiverAuditor testGiverAuditor = giverAuditorList.get(giverAuditorList.size() - 1);
        assertThat(testGiverAuditor.getFieldName()).isEqualTo(UPDATED_FIELD_NAME);
        assertThat(testGiverAuditor.getOldValue()).isEqualTo(UPDATED_OLD_VALUE);
        assertThat(testGiverAuditor.getNewValue()).isEqualTo(UPDATED_NEW_VALUE);
        assertThat(testGiverAuditor.getChangeDate()).isEqualTo(UPDATED_CHANGE_DATE);
    }

    @Test
    @Transactional
    void patchNonExistingGiverAuditor() throws Exception {
        int databaseSizeBeforeUpdate = giverAuditorRepository.findAll().size();
        giverAuditor.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restGiverAuditorMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, giverAuditor.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(giverAuditor))
            )
            .andExpect(status().isBadRequest());

        // Validate the GiverAuditor in the database
        List<GiverAuditor> giverAuditorList = giverAuditorRepository.findAll();
        assertThat(giverAuditorList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchGiverAuditor() throws Exception {
        int databaseSizeBeforeUpdate = giverAuditorRepository.findAll().size();
        giverAuditor.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restGiverAuditorMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(giverAuditor))
            )
            .andExpect(status().isBadRequest());

        // Validate the GiverAuditor in the database
        List<GiverAuditor> giverAuditorList = giverAuditorRepository.findAll();
        assertThat(giverAuditorList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamGiverAuditor() throws Exception {
        int databaseSizeBeforeUpdate = giverAuditorRepository.findAll().size();
        giverAuditor.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restGiverAuditorMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(giverAuditor))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the GiverAuditor in the database
        List<GiverAuditor> giverAuditorList = giverAuditorRepository.findAll();
        assertThat(giverAuditorList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteGiverAuditor() throws Exception {
        // Initialize the database
        giverAuditorRepository.saveAndFlush(giverAuditor);

        int databaseSizeBeforeDelete = giverAuditorRepository.findAll().size();

        // Delete the giverAuditor
        restGiverAuditorMockMvc
            .perform(delete(ENTITY_API_URL_ID, giverAuditor.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<GiverAuditor> giverAuditorList = giverAuditorRepository.findAll();
        assertThat(giverAuditorList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
