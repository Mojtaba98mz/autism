package com.armaghanehayat.autism.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.armaghanehayat.autism.IntegrationTest;
import com.armaghanehayat.autism.domain.Ceremony;
import com.armaghanehayat.autism.domain.CeremonyUser;
import com.armaghanehayat.autism.repository.CeremonyUserRepository;
import com.armaghanehayat.autism.service.criteria.CeremonyUserCriteria;
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
 * Integration tests for the {@link CeremonyUserResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class CeremonyUserResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_FAMILY = "AAAAAAAAAA";
    private static final String UPDATED_FAMILY = "BBBBBBBBBB";

    private static final String DEFAULT_PHONE_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_PHONE_NUMBER = "BBBBBBBBBB";

    private static final String DEFAULT_HOME_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_HOME_NUMBER = "BBBBBBBBBB";

    private static final String DEFAULT_ADDRESS = "AAAAAAAAAA";
    private static final String UPDATED_ADDRESS = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/ceremony-users";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private CeremonyUserRepository ceremonyUserRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCeremonyUserMockMvc;

    private CeremonyUser ceremonyUser;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CeremonyUser createEntity(EntityManager em) {
        CeremonyUser ceremonyUser = new CeremonyUser()
            .name(DEFAULT_NAME)
            .family(DEFAULT_FAMILY)
            .phoneNumber(DEFAULT_PHONE_NUMBER)
            .homeNumber(DEFAULT_HOME_NUMBER)
            .address(DEFAULT_ADDRESS);
        return ceremonyUser;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CeremonyUser createUpdatedEntity(EntityManager em) {
        CeremonyUser ceremonyUser = new CeremonyUser()
            .name(UPDATED_NAME)
            .family(UPDATED_FAMILY)
            .phoneNumber(UPDATED_PHONE_NUMBER)
            .homeNumber(UPDATED_HOME_NUMBER)
            .address(UPDATED_ADDRESS);
        return ceremonyUser;
    }

    @BeforeEach
    public void initTest() {
        ceremonyUser = createEntity(em);
    }

    @Test
    @Transactional
    void createCeremonyUser() throws Exception {
        int databaseSizeBeforeCreate = ceremonyUserRepository.findAll().size();
        // Create the CeremonyUser
        restCeremonyUserMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(ceremonyUser)))
            .andExpect(status().isCreated());

        // Validate the CeremonyUser in the database
        List<CeremonyUser> ceremonyUserList = ceremonyUserRepository.findAll();
        assertThat(ceremonyUserList).hasSize(databaseSizeBeforeCreate + 1);
        CeremonyUser testCeremonyUser = ceremonyUserList.get(ceremonyUserList.size() - 1);
        assertThat(testCeremonyUser.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testCeremonyUser.getFamily()).isEqualTo(DEFAULT_FAMILY);
        assertThat(testCeremonyUser.getPhoneNumber()).isEqualTo(DEFAULT_PHONE_NUMBER);
        assertThat(testCeremonyUser.getHomeNumber()).isEqualTo(DEFAULT_HOME_NUMBER);
        assertThat(testCeremonyUser.getAddress()).isEqualTo(DEFAULT_ADDRESS);
    }

    @Test
    @Transactional
    void createCeremonyUserWithExistingId() throws Exception {
        // Create the CeremonyUser with an existing ID
        ceremonyUser.setId(1L);

        int databaseSizeBeforeCreate = ceremonyUserRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restCeremonyUserMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(ceremonyUser)))
            .andExpect(status().isBadRequest());

        // Validate the CeremonyUser in the database
        List<CeremonyUser> ceremonyUserList = ceremonyUserRepository.findAll();
        assertThat(ceremonyUserList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = ceremonyUserRepository.findAll().size();
        // set the field null
        ceremonyUser.setName(null);

        // Create the CeremonyUser, which fails.

        restCeremonyUserMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(ceremonyUser)))
            .andExpect(status().isBadRequest());

        List<CeremonyUser> ceremonyUserList = ceremonyUserRepository.findAll();
        assertThat(ceremonyUserList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkFamilyIsRequired() throws Exception {
        int databaseSizeBeforeTest = ceremonyUserRepository.findAll().size();
        // set the field null
        ceremonyUser.setFamily(null);

        // Create the CeremonyUser, which fails.

        restCeremonyUserMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(ceremonyUser)))
            .andExpect(status().isBadRequest());

        List<CeremonyUser> ceremonyUserList = ceremonyUserRepository.findAll();
        assertThat(ceremonyUserList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkPhoneNumberIsRequired() throws Exception {
        int databaseSizeBeforeTest = ceremonyUserRepository.findAll().size();
        // set the field null
        ceremonyUser.setPhoneNumber(null);

        // Create the CeremonyUser, which fails.

        restCeremonyUserMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(ceremonyUser)))
            .andExpect(status().isBadRequest());

        List<CeremonyUser> ceremonyUserList = ceremonyUserRepository.findAll();
        assertThat(ceremonyUserList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllCeremonyUsers() throws Exception {
        // Initialize the database
        ceremonyUserRepository.saveAndFlush(ceremonyUser);

        // Get all the ceremonyUserList
        restCeremonyUserMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(ceremonyUser.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].family").value(hasItem(DEFAULT_FAMILY)))
            .andExpect(jsonPath("$.[*].phoneNumber").value(hasItem(DEFAULT_PHONE_NUMBER)))
            .andExpect(jsonPath("$.[*].homeNumber").value(hasItem(DEFAULT_HOME_NUMBER)))
            .andExpect(jsonPath("$.[*].address").value(hasItem(DEFAULT_ADDRESS)));
    }

    @Test
    @Transactional
    void getCeremonyUser() throws Exception {
        // Initialize the database
        ceremonyUserRepository.saveAndFlush(ceremonyUser);

        // Get the ceremonyUser
        restCeremonyUserMockMvc
            .perform(get(ENTITY_API_URL_ID, ceremonyUser.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(ceremonyUser.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.family").value(DEFAULT_FAMILY))
            .andExpect(jsonPath("$.phoneNumber").value(DEFAULT_PHONE_NUMBER))
            .andExpect(jsonPath("$.homeNumber").value(DEFAULT_HOME_NUMBER))
            .andExpect(jsonPath("$.address").value(DEFAULT_ADDRESS));
    }

    @Test
    @Transactional
    void getCeremonyUsersByIdFiltering() throws Exception {
        // Initialize the database
        ceremonyUserRepository.saveAndFlush(ceremonyUser);

        Long id = ceremonyUser.getId();

        defaultCeremonyUserShouldBeFound("id.equals=" + id);
        defaultCeremonyUserShouldNotBeFound("id.notEquals=" + id);

        defaultCeremonyUserShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultCeremonyUserShouldNotBeFound("id.greaterThan=" + id);

        defaultCeremonyUserShouldBeFound("id.lessThanOrEqual=" + id);
        defaultCeremonyUserShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllCeremonyUsersByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        ceremonyUserRepository.saveAndFlush(ceremonyUser);

        // Get all the ceremonyUserList where name equals to DEFAULT_NAME
        defaultCeremonyUserShouldBeFound("name.equals=" + DEFAULT_NAME);

        // Get all the ceremonyUserList where name equals to UPDATED_NAME
        defaultCeremonyUserShouldNotBeFound("name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllCeremonyUsersByNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        ceremonyUserRepository.saveAndFlush(ceremonyUser);

        // Get all the ceremonyUserList where name not equals to DEFAULT_NAME
        defaultCeremonyUserShouldNotBeFound("name.notEquals=" + DEFAULT_NAME);

        // Get all the ceremonyUserList where name not equals to UPDATED_NAME
        defaultCeremonyUserShouldBeFound("name.notEquals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllCeremonyUsersByNameIsInShouldWork() throws Exception {
        // Initialize the database
        ceremonyUserRepository.saveAndFlush(ceremonyUser);

        // Get all the ceremonyUserList where name in DEFAULT_NAME or UPDATED_NAME
        defaultCeremonyUserShouldBeFound("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME);

        // Get all the ceremonyUserList where name equals to UPDATED_NAME
        defaultCeremonyUserShouldNotBeFound("name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllCeremonyUsersByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        ceremonyUserRepository.saveAndFlush(ceremonyUser);

        // Get all the ceremonyUserList where name is not null
        defaultCeremonyUserShouldBeFound("name.specified=true");

        // Get all the ceremonyUserList where name is null
        defaultCeremonyUserShouldNotBeFound("name.specified=false");
    }

    @Test
    @Transactional
    void getAllCeremonyUsersByNameContainsSomething() throws Exception {
        // Initialize the database
        ceremonyUserRepository.saveAndFlush(ceremonyUser);

        // Get all the ceremonyUserList where name contains DEFAULT_NAME
        defaultCeremonyUserShouldBeFound("name.contains=" + DEFAULT_NAME);

        // Get all the ceremonyUserList where name contains UPDATED_NAME
        defaultCeremonyUserShouldNotBeFound("name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllCeremonyUsersByNameNotContainsSomething() throws Exception {
        // Initialize the database
        ceremonyUserRepository.saveAndFlush(ceremonyUser);

        // Get all the ceremonyUserList where name does not contain DEFAULT_NAME
        defaultCeremonyUserShouldNotBeFound("name.doesNotContain=" + DEFAULT_NAME);

        // Get all the ceremonyUserList where name does not contain UPDATED_NAME
        defaultCeremonyUserShouldBeFound("name.doesNotContain=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllCeremonyUsersByFamilyIsEqualToSomething() throws Exception {
        // Initialize the database
        ceremonyUserRepository.saveAndFlush(ceremonyUser);

        // Get all the ceremonyUserList where family equals to DEFAULT_FAMILY
        defaultCeremonyUserShouldBeFound("family.equals=" + DEFAULT_FAMILY);

        // Get all the ceremonyUserList where family equals to UPDATED_FAMILY
        defaultCeremonyUserShouldNotBeFound("family.equals=" + UPDATED_FAMILY);
    }

    @Test
    @Transactional
    void getAllCeremonyUsersByFamilyIsNotEqualToSomething() throws Exception {
        // Initialize the database
        ceremonyUserRepository.saveAndFlush(ceremonyUser);

        // Get all the ceremonyUserList where family not equals to DEFAULT_FAMILY
        defaultCeremonyUserShouldNotBeFound("family.notEquals=" + DEFAULT_FAMILY);

        // Get all the ceremonyUserList where family not equals to UPDATED_FAMILY
        defaultCeremonyUserShouldBeFound("family.notEquals=" + UPDATED_FAMILY);
    }

    @Test
    @Transactional
    void getAllCeremonyUsersByFamilyIsInShouldWork() throws Exception {
        // Initialize the database
        ceremonyUserRepository.saveAndFlush(ceremonyUser);

        // Get all the ceremonyUserList where family in DEFAULT_FAMILY or UPDATED_FAMILY
        defaultCeremonyUserShouldBeFound("family.in=" + DEFAULT_FAMILY + "," + UPDATED_FAMILY);

        // Get all the ceremonyUserList where family equals to UPDATED_FAMILY
        defaultCeremonyUserShouldNotBeFound("family.in=" + UPDATED_FAMILY);
    }

    @Test
    @Transactional
    void getAllCeremonyUsersByFamilyIsNullOrNotNull() throws Exception {
        // Initialize the database
        ceremonyUserRepository.saveAndFlush(ceremonyUser);

        // Get all the ceremonyUserList where family is not null
        defaultCeremonyUserShouldBeFound("family.specified=true");

        // Get all the ceremonyUserList where family is null
        defaultCeremonyUserShouldNotBeFound("family.specified=false");
    }

    @Test
    @Transactional
    void getAllCeremonyUsersByFamilyContainsSomething() throws Exception {
        // Initialize the database
        ceremonyUserRepository.saveAndFlush(ceremonyUser);

        // Get all the ceremonyUserList where family contains DEFAULT_FAMILY
        defaultCeremonyUserShouldBeFound("family.contains=" + DEFAULT_FAMILY);

        // Get all the ceremonyUserList where family contains UPDATED_FAMILY
        defaultCeremonyUserShouldNotBeFound("family.contains=" + UPDATED_FAMILY);
    }

    @Test
    @Transactional
    void getAllCeremonyUsersByFamilyNotContainsSomething() throws Exception {
        // Initialize the database
        ceremonyUserRepository.saveAndFlush(ceremonyUser);

        // Get all the ceremonyUserList where family does not contain DEFAULT_FAMILY
        defaultCeremonyUserShouldNotBeFound("family.doesNotContain=" + DEFAULT_FAMILY);

        // Get all the ceremonyUserList where family does not contain UPDATED_FAMILY
        defaultCeremonyUserShouldBeFound("family.doesNotContain=" + UPDATED_FAMILY);
    }

    @Test
    @Transactional
    void getAllCeremonyUsersByPhoneNumberIsEqualToSomething() throws Exception {
        // Initialize the database
        ceremonyUserRepository.saveAndFlush(ceremonyUser);

        // Get all the ceremonyUserList where phoneNumber equals to DEFAULT_PHONE_NUMBER
        defaultCeremonyUserShouldBeFound("phoneNumber.equals=" + DEFAULT_PHONE_NUMBER);

        // Get all the ceremonyUserList where phoneNumber equals to UPDATED_PHONE_NUMBER
        defaultCeremonyUserShouldNotBeFound("phoneNumber.equals=" + UPDATED_PHONE_NUMBER);
    }

    @Test
    @Transactional
    void getAllCeremonyUsersByPhoneNumberIsNotEqualToSomething() throws Exception {
        // Initialize the database
        ceremonyUserRepository.saveAndFlush(ceremonyUser);

        // Get all the ceremonyUserList where phoneNumber not equals to DEFAULT_PHONE_NUMBER
        defaultCeremonyUserShouldNotBeFound("phoneNumber.notEquals=" + DEFAULT_PHONE_NUMBER);

        // Get all the ceremonyUserList where phoneNumber not equals to UPDATED_PHONE_NUMBER
        defaultCeremonyUserShouldBeFound("phoneNumber.notEquals=" + UPDATED_PHONE_NUMBER);
    }

    @Test
    @Transactional
    void getAllCeremonyUsersByPhoneNumberIsInShouldWork() throws Exception {
        // Initialize the database
        ceremonyUserRepository.saveAndFlush(ceremonyUser);

        // Get all the ceremonyUserList where phoneNumber in DEFAULT_PHONE_NUMBER or UPDATED_PHONE_NUMBER
        defaultCeremonyUserShouldBeFound("phoneNumber.in=" + DEFAULT_PHONE_NUMBER + "," + UPDATED_PHONE_NUMBER);

        // Get all the ceremonyUserList where phoneNumber equals to UPDATED_PHONE_NUMBER
        defaultCeremonyUserShouldNotBeFound("phoneNumber.in=" + UPDATED_PHONE_NUMBER);
    }

    @Test
    @Transactional
    void getAllCeremonyUsersByPhoneNumberIsNullOrNotNull() throws Exception {
        // Initialize the database
        ceremonyUserRepository.saveAndFlush(ceremonyUser);

        // Get all the ceremonyUserList where phoneNumber is not null
        defaultCeremonyUserShouldBeFound("phoneNumber.specified=true");

        // Get all the ceremonyUserList where phoneNumber is null
        defaultCeremonyUserShouldNotBeFound("phoneNumber.specified=false");
    }

    @Test
    @Transactional
    void getAllCeremonyUsersByPhoneNumberContainsSomething() throws Exception {
        // Initialize the database
        ceremonyUserRepository.saveAndFlush(ceremonyUser);

        // Get all the ceremonyUserList where phoneNumber contains DEFAULT_PHONE_NUMBER
        defaultCeremonyUserShouldBeFound("phoneNumber.contains=" + DEFAULT_PHONE_NUMBER);

        // Get all the ceremonyUserList where phoneNumber contains UPDATED_PHONE_NUMBER
        defaultCeremonyUserShouldNotBeFound("phoneNumber.contains=" + UPDATED_PHONE_NUMBER);
    }

    @Test
    @Transactional
    void getAllCeremonyUsersByPhoneNumberNotContainsSomething() throws Exception {
        // Initialize the database
        ceremonyUserRepository.saveAndFlush(ceremonyUser);

        // Get all the ceremonyUserList where phoneNumber does not contain DEFAULT_PHONE_NUMBER
        defaultCeremonyUserShouldNotBeFound("phoneNumber.doesNotContain=" + DEFAULT_PHONE_NUMBER);

        // Get all the ceremonyUserList where phoneNumber does not contain UPDATED_PHONE_NUMBER
        defaultCeremonyUserShouldBeFound("phoneNumber.doesNotContain=" + UPDATED_PHONE_NUMBER);
    }

    @Test
    @Transactional
    void getAllCeremonyUsersByHomeNumberIsEqualToSomething() throws Exception {
        // Initialize the database
        ceremonyUserRepository.saveAndFlush(ceremonyUser);

        // Get all the ceremonyUserList where homeNumber equals to DEFAULT_HOME_NUMBER
        defaultCeremonyUserShouldBeFound("homeNumber.equals=" + DEFAULT_HOME_NUMBER);

        // Get all the ceremonyUserList where homeNumber equals to UPDATED_HOME_NUMBER
        defaultCeremonyUserShouldNotBeFound("homeNumber.equals=" + UPDATED_HOME_NUMBER);
    }

    @Test
    @Transactional
    void getAllCeremonyUsersByHomeNumberIsNotEqualToSomething() throws Exception {
        // Initialize the database
        ceremonyUserRepository.saveAndFlush(ceremonyUser);

        // Get all the ceremonyUserList where homeNumber not equals to DEFAULT_HOME_NUMBER
        defaultCeremonyUserShouldNotBeFound("homeNumber.notEquals=" + DEFAULT_HOME_NUMBER);

        // Get all the ceremonyUserList where homeNumber not equals to UPDATED_HOME_NUMBER
        defaultCeremonyUserShouldBeFound("homeNumber.notEquals=" + UPDATED_HOME_NUMBER);
    }

    @Test
    @Transactional
    void getAllCeremonyUsersByHomeNumberIsInShouldWork() throws Exception {
        // Initialize the database
        ceremonyUserRepository.saveAndFlush(ceremonyUser);

        // Get all the ceremonyUserList where homeNumber in DEFAULT_HOME_NUMBER or UPDATED_HOME_NUMBER
        defaultCeremonyUserShouldBeFound("homeNumber.in=" + DEFAULT_HOME_NUMBER + "," + UPDATED_HOME_NUMBER);

        // Get all the ceremonyUserList where homeNumber equals to UPDATED_HOME_NUMBER
        defaultCeremonyUserShouldNotBeFound("homeNumber.in=" + UPDATED_HOME_NUMBER);
    }

    @Test
    @Transactional
    void getAllCeremonyUsersByHomeNumberIsNullOrNotNull() throws Exception {
        // Initialize the database
        ceremonyUserRepository.saveAndFlush(ceremonyUser);

        // Get all the ceremonyUserList where homeNumber is not null
        defaultCeremonyUserShouldBeFound("homeNumber.specified=true");

        // Get all the ceremonyUserList where homeNumber is null
        defaultCeremonyUserShouldNotBeFound("homeNumber.specified=false");
    }

    @Test
    @Transactional
    void getAllCeremonyUsersByHomeNumberContainsSomething() throws Exception {
        // Initialize the database
        ceremonyUserRepository.saveAndFlush(ceremonyUser);

        // Get all the ceremonyUserList where homeNumber contains DEFAULT_HOME_NUMBER
        defaultCeremonyUserShouldBeFound("homeNumber.contains=" + DEFAULT_HOME_NUMBER);

        // Get all the ceremonyUserList where homeNumber contains UPDATED_HOME_NUMBER
        defaultCeremonyUserShouldNotBeFound("homeNumber.contains=" + UPDATED_HOME_NUMBER);
    }

    @Test
    @Transactional
    void getAllCeremonyUsersByHomeNumberNotContainsSomething() throws Exception {
        // Initialize the database
        ceremonyUserRepository.saveAndFlush(ceremonyUser);

        // Get all the ceremonyUserList where homeNumber does not contain DEFAULT_HOME_NUMBER
        defaultCeremonyUserShouldNotBeFound("homeNumber.doesNotContain=" + DEFAULT_HOME_NUMBER);

        // Get all the ceremonyUserList where homeNumber does not contain UPDATED_HOME_NUMBER
        defaultCeremonyUserShouldBeFound("homeNumber.doesNotContain=" + UPDATED_HOME_NUMBER);
    }

    @Test
    @Transactional
    void getAllCeremonyUsersByAddressIsEqualToSomething() throws Exception {
        // Initialize the database
        ceremonyUserRepository.saveAndFlush(ceremonyUser);

        // Get all the ceremonyUserList where address equals to DEFAULT_ADDRESS
        defaultCeremonyUserShouldBeFound("address.equals=" + DEFAULT_ADDRESS);

        // Get all the ceremonyUserList where address equals to UPDATED_ADDRESS
        defaultCeremonyUserShouldNotBeFound("address.equals=" + UPDATED_ADDRESS);
    }

    @Test
    @Transactional
    void getAllCeremonyUsersByAddressIsNotEqualToSomething() throws Exception {
        // Initialize the database
        ceremonyUserRepository.saveAndFlush(ceremonyUser);

        // Get all the ceremonyUserList where address not equals to DEFAULT_ADDRESS
        defaultCeremonyUserShouldNotBeFound("address.notEquals=" + DEFAULT_ADDRESS);

        // Get all the ceremonyUserList where address not equals to UPDATED_ADDRESS
        defaultCeremonyUserShouldBeFound("address.notEquals=" + UPDATED_ADDRESS);
    }

    @Test
    @Transactional
    void getAllCeremonyUsersByAddressIsInShouldWork() throws Exception {
        // Initialize the database
        ceremonyUserRepository.saveAndFlush(ceremonyUser);

        // Get all the ceremonyUserList where address in DEFAULT_ADDRESS or UPDATED_ADDRESS
        defaultCeremonyUserShouldBeFound("address.in=" + DEFAULT_ADDRESS + "," + UPDATED_ADDRESS);

        // Get all the ceremonyUserList where address equals to UPDATED_ADDRESS
        defaultCeremonyUserShouldNotBeFound("address.in=" + UPDATED_ADDRESS);
    }

    @Test
    @Transactional
    void getAllCeremonyUsersByAddressIsNullOrNotNull() throws Exception {
        // Initialize the database
        ceremonyUserRepository.saveAndFlush(ceremonyUser);

        // Get all the ceremonyUserList where address is not null
        defaultCeremonyUserShouldBeFound("address.specified=true");

        // Get all the ceremonyUserList where address is null
        defaultCeremonyUserShouldNotBeFound("address.specified=false");
    }

    @Test
    @Transactional
    void getAllCeremonyUsersByAddressContainsSomething() throws Exception {
        // Initialize the database
        ceremonyUserRepository.saveAndFlush(ceremonyUser);

        // Get all the ceremonyUserList where address contains DEFAULT_ADDRESS
        defaultCeremonyUserShouldBeFound("address.contains=" + DEFAULT_ADDRESS);

        // Get all the ceremonyUserList where address contains UPDATED_ADDRESS
        defaultCeremonyUserShouldNotBeFound("address.contains=" + UPDATED_ADDRESS);
    }

    @Test
    @Transactional
    void getAllCeremonyUsersByAddressNotContainsSomething() throws Exception {
        // Initialize the database
        ceremonyUserRepository.saveAndFlush(ceremonyUser);

        // Get all the ceremonyUserList where address does not contain DEFAULT_ADDRESS
        defaultCeremonyUserShouldNotBeFound("address.doesNotContain=" + DEFAULT_ADDRESS);

        // Get all the ceremonyUserList where address does not contain UPDATED_ADDRESS
        defaultCeremonyUserShouldBeFound("address.doesNotContain=" + UPDATED_ADDRESS);
    }

    @Test
    @Transactional
    void getAllCeremonyUsersByCeremonyIsEqualToSomething() throws Exception {
        // Initialize the database
        ceremonyUserRepository.saveAndFlush(ceremonyUser);
        Ceremony ceremony = CeremonyResourceIT.createEntity(em);
        em.persist(ceremony);
        em.flush();
        ceremonyUser.addCeremony(ceremony);
        ceremonyUserRepository.saveAndFlush(ceremonyUser);
        Long ceremonyId = ceremony.getId();

        // Get all the ceremonyUserList where ceremony equals to ceremonyId
        defaultCeremonyUserShouldBeFound("ceremonyId.equals=" + ceremonyId);

        // Get all the ceremonyUserList where ceremony equals to (ceremonyId + 1)
        defaultCeremonyUserShouldNotBeFound("ceremonyId.equals=" + (ceremonyId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultCeremonyUserShouldBeFound(String filter) throws Exception {
        restCeremonyUserMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(ceremonyUser.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].family").value(hasItem(DEFAULT_FAMILY)))
            .andExpect(jsonPath("$.[*].phoneNumber").value(hasItem(DEFAULT_PHONE_NUMBER)))
            .andExpect(jsonPath("$.[*].homeNumber").value(hasItem(DEFAULT_HOME_NUMBER)))
            .andExpect(jsonPath("$.[*].address").value(hasItem(DEFAULT_ADDRESS)));

        // Check, that the count call also returns 1
        restCeremonyUserMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultCeremonyUserShouldNotBeFound(String filter) throws Exception {
        restCeremonyUserMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restCeremonyUserMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingCeremonyUser() throws Exception {
        // Get the ceremonyUser
        restCeremonyUserMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewCeremonyUser() throws Exception {
        // Initialize the database
        ceremonyUserRepository.saveAndFlush(ceremonyUser);

        int databaseSizeBeforeUpdate = ceremonyUserRepository.findAll().size();

        // Update the ceremonyUser
        CeremonyUser updatedCeremonyUser = ceremonyUserRepository.findById(ceremonyUser.getId()).get();
        // Disconnect from session so that the updates on updatedCeremonyUser are not directly saved in db
        em.detach(updatedCeremonyUser);
        updatedCeremonyUser
            .name(UPDATED_NAME)
            .family(UPDATED_FAMILY)
            .phoneNumber(UPDATED_PHONE_NUMBER)
            .homeNumber(UPDATED_HOME_NUMBER)
            .address(UPDATED_ADDRESS);

        restCeremonyUserMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedCeremonyUser.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedCeremonyUser))
            )
            .andExpect(status().isOk());

        // Validate the CeremonyUser in the database
        List<CeremonyUser> ceremonyUserList = ceremonyUserRepository.findAll();
        assertThat(ceremonyUserList).hasSize(databaseSizeBeforeUpdate);
        CeremonyUser testCeremonyUser = ceremonyUserList.get(ceremonyUserList.size() - 1);
        assertThat(testCeremonyUser.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testCeremonyUser.getFamily()).isEqualTo(UPDATED_FAMILY);
        assertThat(testCeremonyUser.getPhoneNumber()).isEqualTo(UPDATED_PHONE_NUMBER);
        assertThat(testCeremonyUser.getHomeNumber()).isEqualTo(UPDATED_HOME_NUMBER);
        assertThat(testCeremonyUser.getAddress()).isEqualTo(UPDATED_ADDRESS);
    }

    @Test
    @Transactional
    void putNonExistingCeremonyUser() throws Exception {
        int databaseSizeBeforeUpdate = ceremonyUserRepository.findAll().size();
        ceremonyUser.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCeremonyUserMockMvc
            .perform(
                put(ENTITY_API_URL_ID, ceremonyUser.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(ceremonyUser))
            )
            .andExpect(status().isBadRequest());

        // Validate the CeremonyUser in the database
        List<CeremonyUser> ceremonyUserList = ceremonyUserRepository.findAll();
        assertThat(ceremonyUserList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchCeremonyUser() throws Exception {
        int databaseSizeBeforeUpdate = ceremonyUserRepository.findAll().size();
        ceremonyUser.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCeremonyUserMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(ceremonyUser))
            )
            .andExpect(status().isBadRequest());

        // Validate the CeremonyUser in the database
        List<CeremonyUser> ceremonyUserList = ceremonyUserRepository.findAll();
        assertThat(ceremonyUserList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamCeremonyUser() throws Exception {
        int databaseSizeBeforeUpdate = ceremonyUserRepository.findAll().size();
        ceremonyUser.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCeremonyUserMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(ceremonyUser)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the CeremonyUser in the database
        List<CeremonyUser> ceremonyUserList = ceremonyUserRepository.findAll();
        assertThat(ceremonyUserList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateCeremonyUserWithPatch() throws Exception {
        // Initialize the database
        ceremonyUserRepository.saveAndFlush(ceremonyUser);

        int databaseSizeBeforeUpdate = ceremonyUserRepository.findAll().size();

        // Update the ceremonyUser using partial update
        CeremonyUser partialUpdatedCeremonyUser = new CeremonyUser();
        partialUpdatedCeremonyUser.setId(ceremonyUser.getId());

        partialUpdatedCeremonyUser
            .name(UPDATED_NAME)
            .family(UPDATED_FAMILY)
            .phoneNumber(UPDATED_PHONE_NUMBER)
            .homeNumber(UPDATED_HOME_NUMBER);

        restCeremonyUserMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCeremonyUser.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCeremonyUser))
            )
            .andExpect(status().isOk());

        // Validate the CeremonyUser in the database
        List<CeremonyUser> ceremonyUserList = ceremonyUserRepository.findAll();
        assertThat(ceremonyUserList).hasSize(databaseSizeBeforeUpdate);
        CeremonyUser testCeremonyUser = ceremonyUserList.get(ceremonyUserList.size() - 1);
        assertThat(testCeremonyUser.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testCeremonyUser.getFamily()).isEqualTo(UPDATED_FAMILY);
        assertThat(testCeremonyUser.getPhoneNumber()).isEqualTo(UPDATED_PHONE_NUMBER);
        assertThat(testCeremonyUser.getHomeNumber()).isEqualTo(UPDATED_HOME_NUMBER);
        assertThat(testCeremonyUser.getAddress()).isEqualTo(DEFAULT_ADDRESS);
    }

    @Test
    @Transactional
    void fullUpdateCeremonyUserWithPatch() throws Exception {
        // Initialize the database
        ceremonyUserRepository.saveAndFlush(ceremonyUser);

        int databaseSizeBeforeUpdate = ceremonyUserRepository.findAll().size();

        // Update the ceremonyUser using partial update
        CeremonyUser partialUpdatedCeremonyUser = new CeremonyUser();
        partialUpdatedCeremonyUser.setId(ceremonyUser.getId());

        partialUpdatedCeremonyUser
            .name(UPDATED_NAME)
            .family(UPDATED_FAMILY)
            .phoneNumber(UPDATED_PHONE_NUMBER)
            .homeNumber(UPDATED_HOME_NUMBER)
            .address(UPDATED_ADDRESS);

        restCeremonyUserMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCeremonyUser.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCeremonyUser))
            )
            .andExpect(status().isOk());

        // Validate the CeremonyUser in the database
        List<CeremonyUser> ceremonyUserList = ceremonyUserRepository.findAll();
        assertThat(ceremonyUserList).hasSize(databaseSizeBeforeUpdate);
        CeremonyUser testCeremonyUser = ceremonyUserList.get(ceremonyUserList.size() - 1);
        assertThat(testCeremonyUser.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testCeremonyUser.getFamily()).isEqualTo(UPDATED_FAMILY);
        assertThat(testCeremonyUser.getPhoneNumber()).isEqualTo(UPDATED_PHONE_NUMBER);
        assertThat(testCeremonyUser.getHomeNumber()).isEqualTo(UPDATED_HOME_NUMBER);
        assertThat(testCeremonyUser.getAddress()).isEqualTo(UPDATED_ADDRESS);
    }

    @Test
    @Transactional
    void patchNonExistingCeremonyUser() throws Exception {
        int databaseSizeBeforeUpdate = ceremonyUserRepository.findAll().size();
        ceremonyUser.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCeremonyUserMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, ceremonyUser.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(ceremonyUser))
            )
            .andExpect(status().isBadRequest());

        // Validate the CeremonyUser in the database
        List<CeremonyUser> ceremonyUserList = ceremonyUserRepository.findAll();
        assertThat(ceremonyUserList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchCeremonyUser() throws Exception {
        int databaseSizeBeforeUpdate = ceremonyUserRepository.findAll().size();
        ceremonyUser.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCeremonyUserMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(ceremonyUser))
            )
            .andExpect(status().isBadRequest());

        // Validate the CeremonyUser in the database
        List<CeremonyUser> ceremonyUserList = ceremonyUserRepository.findAll();
        assertThat(ceremonyUserList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamCeremonyUser() throws Exception {
        int databaseSizeBeforeUpdate = ceremonyUserRepository.findAll().size();
        ceremonyUser.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCeremonyUserMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(ceremonyUser))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the CeremonyUser in the database
        List<CeremonyUser> ceremonyUserList = ceremonyUserRepository.findAll();
        assertThat(ceremonyUserList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteCeremonyUser() throws Exception {
        // Initialize the database
        ceremonyUserRepository.saveAndFlush(ceremonyUser);

        int databaseSizeBeforeDelete = ceremonyUserRepository.findAll().size();

        // Delete the ceremonyUser
        restCeremonyUserMockMvc
            .perform(delete(ENTITY_API_URL_ID, ceremonyUser.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<CeremonyUser> ceremonyUserList = ceremonyUserRepository.findAll();
        assertThat(ceremonyUserList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
