package com.armaghanehayat.autism.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.armaghanehayat.autism.IntegrationTest;
import com.armaghanehayat.autism.domain.City;
import com.armaghanehayat.autism.domain.Donation;
import com.armaghanehayat.autism.domain.Giver;
import com.armaghanehayat.autism.domain.GiverAuditor;
import com.armaghanehayat.autism.domain.Province;
import com.armaghanehayat.autism.domain.User;
import com.armaghanehayat.autism.repository.GiverRepository;
import com.armaghanehayat.autism.service.criteria.GiverCriteria;
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
 * Integration tests for the {@link GiverResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class GiverResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_FAMILY = "AAAAAAAAAA";
    private static final String UPDATED_FAMILY = "BBBBBBBBBB";

    private static final String DEFAULT_PHONE_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_PHONE_NUMBER = "BBBBBBBBBB";

    private static final String DEFAULT_CODE = "AAAAAAAAAA";
    private static final String UPDATED_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_ADDRESS = "AAAAAAAAAA";
    private static final String UPDATED_ADDRESS = "BBBBBBBBBB";

    private static final Instant DEFAULT_ABSORB_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_ABSORB_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/givers";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private GiverRepository giverRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restGiverMockMvc;

    private Giver giver;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Giver createEntity(EntityManager em) {
        Giver giver = new Giver()
            .name(DEFAULT_NAME)
            .family(DEFAULT_FAMILY)
            .phoneNumber(DEFAULT_PHONE_NUMBER)
            .code(DEFAULT_CODE)
            .address(DEFAULT_ADDRESS)
            .absorbDate(DEFAULT_ABSORB_DATE);
        return giver;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Giver createUpdatedEntity(EntityManager em) {
        Giver giver = new Giver()
            .name(UPDATED_NAME)
            .family(UPDATED_FAMILY)
            .phoneNumber(UPDATED_PHONE_NUMBER)
            .code(UPDATED_CODE)
            .address(UPDATED_ADDRESS)
            .absorbDate(UPDATED_ABSORB_DATE);
        return giver;
    }

    @BeforeEach
    public void initTest() {
        giver = createEntity(em);
    }

    @Test
    @Transactional
    void createGiver() throws Exception {
        int databaseSizeBeforeCreate = giverRepository.findAll().size();
        // Create the Giver
        restGiverMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(giver)))
            .andExpect(status().isCreated());

        // Validate the Giver in the database
        List<Giver> giverList = giverRepository.findAll();
        assertThat(giverList).hasSize(databaseSizeBeforeCreate + 1);
        Giver testGiver = giverList.get(giverList.size() - 1);
        assertThat(testGiver.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testGiver.getFamily()).isEqualTo(DEFAULT_FAMILY);
        assertThat(testGiver.getPhoneNumber()).isEqualTo(DEFAULT_PHONE_NUMBER);
        assertThat(testGiver.getCode()).isEqualTo(DEFAULT_CODE);
        assertThat(testGiver.getAddress()).isEqualTo(DEFAULT_ADDRESS);
        assertThat(testGiver.getAbsorbDate()).isEqualTo(DEFAULT_ABSORB_DATE);
    }

    @Test
    @Transactional
    void createGiverWithExistingId() throws Exception {
        // Create the Giver with an existing ID
        giver.setId(1L);

        int databaseSizeBeforeCreate = giverRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restGiverMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(giver)))
            .andExpect(status().isBadRequest());

        // Validate the Giver in the database
        List<Giver> giverList = giverRepository.findAll();
        assertThat(giverList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = giverRepository.findAll().size();
        // set the field null
        giver.setName(null);

        // Create the Giver, which fails.

        restGiverMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(giver)))
            .andExpect(status().isBadRequest());

        List<Giver> giverList = giverRepository.findAll();
        assertThat(giverList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkFamilyIsRequired() throws Exception {
        int databaseSizeBeforeTest = giverRepository.findAll().size();
        // set the field null
        giver.setFamily(null);

        // Create the Giver, which fails.

        restGiverMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(giver)))
            .andExpect(status().isBadRequest());

        List<Giver> giverList = giverRepository.findAll();
        assertThat(giverList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkPhoneNumberIsRequired() throws Exception {
        int databaseSizeBeforeTest = giverRepository.findAll().size();
        // set the field null
        giver.setPhoneNumber(null);

        // Create the Giver, which fails.

        restGiverMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(giver)))
            .andExpect(status().isBadRequest());

        List<Giver> giverList = giverRepository.findAll();
        assertThat(giverList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCodeIsRequired() throws Exception {
        int databaseSizeBeforeTest = giverRepository.findAll().size();
        // set the field null
        giver.setCode(null);

        // Create the Giver, which fails.

        restGiverMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(giver)))
            .andExpect(status().isBadRequest());

        List<Giver> giverList = giverRepository.findAll();
        assertThat(giverList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllGivers() throws Exception {
        // Initialize the database
        giverRepository.saveAndFlush(giver);

        // Get all the giverList
        restGiverMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(giver.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].family").value(hasItem(DEFAULT_FAMILY)))
            .andExpect(jsonPath("$.[*].phoneNumber").value(hasItem(DEFAULT_PHONE_NUMBER)))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE)))
            .andExpect(jsonPath("$.[*].address").value(hasItem(DEFAULT_ADDRESS)))
            .andExpect(jsonPath("$.[*].absorbDate").value(hasItem(DEFAULT_ABSORB_DATE.toString())));
    }

    @Test
    @Transactional
    void getGiver() throws Exception {
        // Initialize the database
        giverRepository.saveAndFlush(giver);

        // Get the giver
        restGiverMockMvc
            .perform(get(ENTITY_API_URL_ID, giver.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(giver.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.family").value(DEFAULT_FAMILY))
            .andExpect(jsonPath("$.phoneNumber").value(DEFAULT_PHONE_NUMBER))
            .andExpect(jsonPath("$.code").value(DEFAULT_CODE))
            .andExpect(jsonPath("$.address").value(DEFAULT_ADDRESS))
            .andExpect(jsonPath("$.absorbDate").value(DEFAULT_ABSORB_DATE.toString()));
    }

    @Test
    @Transactional
    void getGiversByIdFiltering() throws Exception {
        // Initialize the database
        giverRepository.saveAndFlush(giver);

        Long id = giver.getId();

        defaultGiverShouldBeFound("id.equals=" + id);
        defaultGiverShouldNotBeFound("id.notEquals=" + id);

        defaultGiverShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultGiverShouldNotBeFound("id.greaterThan=" + id);

        defaultGiverShouldBeFound("id.lessThanOrEqual=" + id);
        defaultGiverShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllGiversByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        giverRepository.saveAndFlush(giver);

        // Get all the giverList where name equals to DEFAULT_NAME
        defaultGiverShouldBeFound("name.equals=" + DEFAULT_NAME);

        // Get all the giverList where name equals to UPDATED_NAME
        defaultGiverShouldNotBeFound("name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllGiversByNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        giverRepository.saveAndFlush(giver);

        // Get all the giverList where name not equals to DEFAULT_NAME
        defaultGiverShouldNotBeFound("name.notEquals=" + DEFAULT_NAME);

        // Get all the giverList where name not equals to UPDATED_NAME
        defaultGiverShouldBeFound("name.notEquals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllGiversByNameIsInShouldWork() throws Exception {
        // Initialize the database
        giverRepository.saveAndFlush(giver);

        // Get all the giverList where name in DEFAULT_NAME or UPDATED_NAME
        defaultGiverShouldBeFound("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME);

        // Get all the giverList where name equals to UPDATED_NAME
        defaultGiverShouldNotBeFound("name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllGiversByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        giverRepository.saveAndFlush(giver);

        // Get all the giverList where name is not null
        defaultGiverShouldBeFound("name.specified=true");

        // Get all the giverList where name is null
        defaultGiverShouldNotBeFound("name.specified=false");
    }

    @Test
    @Transactional
    void getAllGiversByNameContainsSomething() throws Exception {
        // Initialize the database
        giverRepository.saveAndFlush(giver);

        // Get all the giverList where name contains DEFAULT_NAME
        defaultGiverShouldBeFound("name.contains=" + DEFAULT_NAME);

        // Get all the giverList where name contains UPDATED_NAME
        defaultGiverShouldNotBeFound("name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllGiversByNameNotContainsSomething() throws Exception {
        // Initialize the database
        giverRepository.saveAndFlush(giver);

        // Get all the giverList where name does not contain DEFAULT_NAME
        defaultGiverShouldNotBeFound("name.doesNotContain=" + DEFAULT_NAME);

        // Get all the giverList where name does not contain UPDATED_NAME
        defaultGiverShouldBeFound("name.doesNotContain=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllGiversByFamilyIsEqualToSomething() throws Exception {
        // Initialize the database
        giverRepository.saveAndFlush(giver);

        // Get all the giverList where family equals to DEFAULT_FAMILY
        defaultGiverShouldBeFound("family.equals=" + DEFAULT_FAMILY);

        // Get all the giverList where family equals to UPDATED_FAMILY
        defaultGiverShouldNotBeFound("family.equals=" + UPDATED_FAMILY);
    }

    @Test
    @Transactional
    void getAllGiversByFamilyIsNotEqualToSomething() throws Exception {
        // Initialize the database
        giverRepository.saveAndFlush(giver);

        // Get all the giverList where family not equals to DEFAULT_FAMILY
        defaultGiverShouldNotBeFound("family.notEquals=" + DEFAULT_FAMILY);

        // Get all the giverList where family not equals to UPDATED_FAMILY
        defaultGiverShouldBeFound("family.notEquals=" + UPDATED_FAMILY);
    }

    @Test
    @Transactional
    void getAllGiversByFamilyIsInShouldWork() throws Exception {
        // Initialize the database
        giverRepository.saveAndFlush(giver);

        // Get all the giverList where family in DEFAULT_FAMILY or UPDATED_FAMILY
        defaultGiverShouldBeFound("family.in=" + DEFAULT_FAMILY + "," + UPDATED_FAMILY);

        // Get all the giverList where family equals to UPDATED_FAMILY
        defaultGiverShouldNotBeFound("family.in=" + UPDATED_FAMILY);
    }

    @Test
    @Transactional
    void getAllGiversByFamilyIsNullOrNotNull() throws Exception {
        // Initialize the database
        giverRepository.saveAndFlush(giver);

        // Get all the giverList where family is not null
        defaultGiverShouldBeFound("family.specified=true");

        // Get all the giverList where family is null
        defaultGiverShouldNotBeFound("family.specified=false");
    }

    @Test
    @Transactional
    void getAllGiversByFamilyContainsSomething() throws Exception {
        // Initialize the database
        giverRepository.saveAndFlush(giver);

        // Get all the giverList where family contains DEFAULT_FAMILY
        defaultGiverShouldBeFound("family.contains=" + DEFAULT_FAMILY);

        // Get all the giverList where family contains UPDATED_FAMILY
        defaultGiverShouldNotBeFound("family.contains=" + UPDATED_FAMILY);
    }

    @Test
    @Transactional
    void getAllGiversByFamilyNotContainsSomething() throws Exception {
        // Initialize the database
        giverRepository.saveAndFlush(giver);

        // Get all the giverList where family does not contain DEFAULT_FAMILY
        defaultGiverShouldNotBeFound("family.doesNotContain=" + DEFAULT_FAMILY);

        // Get all the giverList where family does not contain UPDATED_FAMILY
        defaultGiverShouldBeFound("family.doesNotContain=" + UPDATED_FAMILY);
    }

    @Test
    @Transactional
    void getAllGiversByPhoneNumberIsEqualToSomething() throws Exception {
        // Initialize the database
        giverRepository.saveAndFlush(giver);

        // Get all the giverList where phoneNumber equals to DEFAULT_PHONE_NUMBER
        defaultGiverShouldBeFound("phoneNumber.equals=" + DEFAULT_PHONE_NUMBER);

        // Get all the giverList where phoneNumber equals to UPDATED_PHONE_NUMBER
        defaultGiverShouldNotBeFound("phoneNumber.equals=" + UPDATED_PHONE_NUMBER);
    }

    @Test
    @Transactional
    void getAllGiversByPhoneNumberIsNotEqualToSomething() throws Exception {
        // Initialize the database
        giverRepository.saveAndFlush(giver);

        // Get all the giverList where phoneNumber not equals to DEFAULT_PHONE_NUMBER
        defaultGiverShouldNotBeFound("phoneNumber.notEquals=" + DEFAULT_PHONE_NUMBER);

        // Get all the giverList where phoneNumber not equals to UPDATED_PHONE_NUMBER
        defaultGiverShouldBeFound("phoneNumber.notEquals=" + UPDATED_PHONE_NUMBER);
    }

    @Test
    @Transactional
    void getAllGiversByPhoneNumberIsInShouldWork() throws Exception {
        // Initialize the database
        giverRepository.saveAndFlush(giver);

        // Get all the giverList where phoneNumber in DEFAULT_PHONE_NUMBER or UPDATED_PHONE_NUMBER
        defaultGiverShouldBeFound("phoneNumber.in=" + DEFAULT_PHONE_NUMBER + "," + UPDATED_PHONE_NUMBER);

        // Get all the giverList where phoneNumber equals to UPDATED_PHONE_NUMBER
        defaultGiverShouldNotBeFound("phoneNumber.in=" + UPDATED_PHONE_NUMBER);
    }

    @Test
    @Transactional
    void getAllGiversByPhoneNumberIsNullOrNotNull() throws Exception {
        // Initialize the database
        giverRepository.saveAndFlush(giver);

        // Get all the giverList where phoneNumber is not null
        defaultGiverShouldBeFound("phoneNumber.specified=true");

        // Get all the giverList where phoneNumber is null
        defaultGiverShouldNotBeFound("phoneNumber.specified=false");
    }

    @Test
    @Transactional
    void getAllGiversByPhoneNumberContainsSomething() throws Exception {
        // Initialize the database
        giverRepository.saveAndFlush(giver);

        // Get all the giverList where phoneNumber contains DEFAULT_PHONE_NUMBER
        defaultGiverShouldBeFound("phoneNumber.contains=" + DEFAULT_PHONE_NUMBER);

        // Get all the giverList where phoneNumber contains UPDATED_PHONE_NUMBER
        defaultGiverShouldNotBeFound("phoneNumber.contains=" + UPDATED_PHONE_NUMBER);
    }

    @Test
    @Transactional
    void getAllGiversByPhoneNumberNotContainsSomething() throws Exception {
        // Initialize the database
        giverRepository.saveAndFlush(giver);

        // Get all the giverList where phoneNumber does not contain DEFAULT_PHONE_NUMBER
        defaultGiverShouldNotBeFound("phoneNumber.doesNotContain=" + DEFAULT_PHONE_NUMBER);

        // Get all the giverList where phoneNumber does not contain UPDATED_PHONE_NUMBER
        defaultGiverShouldBeFound("phoneNumber.doesNotContain=" + UPDATED_PHONE_NUMBER);
    }

    @Test
    @Transactional
    void getAllGiversByCodeIsEqualToSomething() throws Exception {
        // Initialize the database
        giverRepository.saveAndFlush(giver);

        // Get all the giverList where code equals to DEFAULT_CODE
        defaultGiverShouldBeFound("code.equals=" + DEFAULT_CODE);

        // Get all the giverList where code equals to UPDATED_CODE
        defaultGiverShouldNotBeFound("code.equals=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    void getAllGiversByCodeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        giverRepository.saveAndFlush(giver);

        // Get all the giverList where code not equals to DEFAULT_CODE
        defaultGiverShouldNotBeFound("code.notEquals=" + DEFAULT_CODE);

        // Get all the giverList where code not equals to UPDATED_CODE
        defaultGiverShouldBeFound("code.notEquals=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    void getAllGiversByCodeIsInShouldWork() throws Exception {
        // Initialize the database
        giverRepository.saveAndFlush(giver);

        // Get all the giverList where code in DEFAULT_CODE or UPDATED_CODE
        defaultGiverShouldBeFound("code.in=" + DEFAULT_CODE + "," + UPDATED_CODE);

        // Get all the giverList where code equals to UPDATED_CODE
        defaultGiverShouldNotBeFound("code.in=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    void getAllGiversByCodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        giverRepository.saveAndFlush(giver);

        // Get all the giverList where code is not null
        defaultGiverShouldBeFound("code.specified=true");

        // Get all the giverList where code is null
        defaultGiverShouldNotBeFound("code.specified=false");
    }

    @Test
    @Transactional
    void getAllGiversByCodeContainsSomething() throws Exception {
        // Initialize the database
        giverRepository.saveAndFlush(giver);

        // Get all the giverList where code contains DEFAULT_CODE
        defaultGiverShouldBeFound("code.contains=" + DEFAULT_CODE);

        // Get all the giverList where code contains UPDATED_CODE
        defaultGiverShouldNotBeFound("code.contains=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    void getAllGiversByCodeNotContainsSomething() throws Exception {
        // Initialize the database
        giverRepository.saveAndFlush(giver);

        // Get all the giverList where code does not contain DEFAULT_CODE
        defaultGiverShouldNotBeFound("code.doesNotContain=" + DEFAULT_CODE);

        // Get all the giverList where code does not contain UPDATED_CODE
        defaultGiverShouldBeFound("code.doesNotContain=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    void getAllGiversByAddressIsEqualToSomething() throws Exception {
        // Initialize the database
        giverRepository.saveAndFlush(giver);

        // Get all the giverList where address equals to DEFAULT_ADDRESS
        defaultGiverShouldBeFound("address.equals=" + DEFAULT_ADDRESS);

        // Get all the giverList where address equals to UPDATED_ADDRESS
        defaultGiverShouldNotBeFound("address.equals=" + UPDATED_ADDRESS);
    }

    @Test
    @Transactional
    void getAllGiversByAddressIsNotEqualToSomething() throws Exception {
        // Initialize the database
        giverRepository.saveAndFlush(giver);

        // Get all the giverList where address not equals to DEFAULT_ADDRESS
        defaultGiverShouldNotBeFound("address.notEquals=" + DEFAULT_ADDRESS);

        // Get all the giverList where address not equals to UPDATED_ADDRESS
        defaultGiverShouldBeFound("address.notEquals=" + UPDATED_ADDRESS);
    }

    @Test
    @Transactional
    void getAllGiversByAddressIsInShouldWork() throws Exception {
        // Initialize the database
        giverRepository.saveAndFlush(giver);

        // Get all the giverList where address in DEFAULT_ADDRESS or UPDATED_ADDRESS
        defaultGiverShouldBeFound("address.in=" + DEFAULT_ADDRESS + "," + UPDATED_ADDRESS);

        // Get all the giverList where address equals to UPDATED_ADDRESS
        defaultGiverShouldNotBeFound("address.in=" + UPDATED_ADDRESS);
    }

    @Test
    @Transactional
    void getAllGiversByAddressIsNullOrNotNull() throws Exception {
        // Initialize the database
        giverRepository.saveAndFlush(giver);

        // Get all the giverList where address is not null
        defaultGiverShouldBeFound("address.specified=true");

        // Get all the giverList where address is null
        defaultGiverShouldNotBeFound("address.specified=false");
    }

    @Test
    @Transactional
    void getAllGiversByAddressContainsSomething() throws Exception {
        // Initialize the database
        giverRepository.saveAndFlush(giver);

        // Get all the giverList where address contains DEFAULT_ADDRESS
        defaultGiverShouldBeFound("address.contains=" + DEFAULT_ADDRESS);

        // Get all the giverList where address contains UPDATED_ADDRESS
        defaultGiverShouldNotBeFound("address.contains=" + UPDATED_ADDRESS);
    }

    @Test
    @Transactional
    void getAllGiversByAddressNotContainsSomething() throws Exception {
        // Initialize the database
        giverRepository.saveAndFlush(giver);

        // Get all the giverList where address does not contain DEFAULT_ADDRESS
        defaultGiverShouldNotBeFound("address.doesNotContain=" + DEFAULT_ADDRESS);

        // Get all the giverList where address does not contain UPDATED_ADDRESS
        defaultGiverShouldBeFound("address.doesNotContain=" + UPDATED_ADDRESS);
    }

    @Test
    @Transactional
    void getAllGiversByAbsorbDateIsEqualToSomething() throws Exception {
        // Initialize the database
        giverRepository.saveAndFlush(giver);

        // Get all the giverList where absorbDate equals to DEFAULT_ABSORB_DATE
        defaultGiverShouldBeFound("absorbDate.equals=" + DEFAULT_ABSORB_DATE);

        // Get all the giverList where absorbDate equals to UPDATED_ABSORB_DATE
        defaultGiverShouldNotBeFound("absorbDate.equals=" + UPDATED_ABSORB_DATE);
    }

    @Test
    @Transactional
    void getAllGiversByAbsorbDateIsNotEqualToSomething() throws Exception {
        // Initialize the database
        giverRepository.saveAndFlush(giver);

        // Get all the giverList where absorbDate not equals to DEFAULT_ABSORB_DATE
        defaultGiverShouldNotBeFound("absorbDate.notEquals=" + DEFAULT_ABSORB_DATE);

        // Get all the giverList where absorbDate not equals to UPDATED_ABSORB_DATE
        defaultGiverShouldBeFound("absorbDate.notEquals=" + UPDATED_ABSORB_DATE);
    }

    @Test
    @Transactional
    void getAllGiversByAbsorbDateIsInShouldWork() throws Exception {
        // Initialize the database
        giverRepository.saveAndFlush(giver);

        // Get all the giverList where absorbDate in DEFAULT_ABSORB_DATE or UPDATED_ABSORB_DATE
        defaultGiverShouldBeFound("absorbDate.in=" + DEFAULT_ABSORB_DATE + "," + UPDATED_ABSORB_DATE);

        // Get all the giverList where absorbDate equals to UPDATED_ABSORB_DATE
        defaultGiverShouldNotBeFound("absorbDate.in=" + UPDATED_ABSORB_DATE);
    }

    @Test
    @Transactional
    void getAllGiversByAbsorbDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        giverRepository.saveAndFlush(giver);

        // Get all the giverList where absorbDate is not null
        defaultGiverShouldBeFound("absorbDate.specified=true");

        // Get all the giverList where absorbDate is null
        defaultGiverShouldNotBeFound("absorbDate.specified=false");
    }

    @Test
    @Transactional
    void getAllGiversByProvinceIsEqualToSomething() throws Exception {
        // Initialize the database
        giverRepository.saveAndFlush(giver);
        Province province = ProvinceResourceIT.createEntity(em);
        em.persist(province);
        em.flush();
        giver.setProvince(province);
        giverRepository.saveAndFlush(giver);
        Long provinceId = province.getId();

        // Get all the giverList where province equals to provinceId
        defaultGiverShouldBeFound("provinceId.equals=" + provinceId);

        // Get all the giverList where province equals to (provinceId + 1)
        defaultGiverShouldNotBeFound("provinceId.equals=" + (provinceId + 1));
    }

    @Test
    @Transactional
    void getAllGiversByCityIsEqualToSomething() throws Exception {
        // Initialize the database
        giverRepository.saveAndFlush(giver);
        City city = CityResourceIT.createEntity(em);
        em.persist(city);
        em.flush();
        giver.setCity(city);
        giverRepository.saveAndFlush(giver);
        Long cityId = city.getId();

        // Get all the giverList where city equals to cityId
        defaultGiverShouldBeFound("cityId.equals=" + cityId);

        // Get all the giverList where city equals to (cityId + 1)
        defaultGiverShouldNotBeFound("cityId.equals=" + (cityId + 1));
    }

    @Test
    @Transactional
    void getAllGiversByDonationIsEqualToSomething() throws Exception {
        // Initialize the database
        giverRepository.saveAndFlush(giver);
        Donation donation = DonationResourceIT.createEntity(em);
        em.persist(donation);
        em.flush();
        giver.addDonation(donation);
        giverRepository.saveAndFlush(giver);
        Long donationId = donation.getId();

        // Get all the giverList where donation equals to donationId
        defaultGiverShouldBeFound("donationId.equals=" + donationId);

        // Get all the giverList where donation equals to (donationId + 1)
        defaultGiverShouldNotBeFound("donationId.equals=" + (donationId + 1));
    }

    @Test
    @Transactional
    void getAllGiversByGiverauditorIsEqualToSomething() throws Exception {
        // Initialize the database
        giverRepository.saveAndFlush(giver);
        GiverAuditor giverauditor = GiverAuditorResourceIT.createEntity(em);
        em.persist(giverauditor);
        em.flush();
        giver.addGiverauditor(giverauditor);
        giverRepository.saveAndFlush(giver);
        Long giverauditorId = giverauditor.getId();

        // Get all the giverList where giverauditor equals to giverauditorId
        defaultGiverShouldBeFound("giverauditorId.equals=" + giverauditorId);

        // Get all the giverList where giverauditor equals to (giverauditorId + 1)
        defaultGiverShouldNotBeFound("giverauditorId.equals=" + (giverauditorId + 1));
    }

    @Test
    @Transactional
    void getAllGiversByAbsorbantIsEqualToSomething() throws Exception {
        // Initialize the database
        giverRepository.saveAndFlush(giver);
        User absorbant = UserResourceIT.createEntity(em);
        em.persist(absorbant);
        em.flush();
        giver.setAbsorbant(absorbant);
        giverRepository.saveAndFlush(giver);
        Long absorbantId = absorbant.getId();

        // Get all the giverList where absorbant equals to absorbantId
        defaultGiverShouldBeFound("absorbantId.equals=" + absorbantId);

        // Get all the giverList where absorbant equals to (absorbantId + 1)
        defaultGiverShouldNotBeFound("absorbantId.equals=" + (absorbantId + 1));
    }

    @Test
    @Transactional
    void getAllGiversBySupporterIsEqualToSomething() throws Exception {
        // Initialize the database
        giverRepository.saveAndFlush(giver);
        User supporter = UserResourceIT.createEntity(em);
        em.persist(supporter);
        em.flush();
        giver.setSupporter(supporter);
        giverRepository.saveAndFlush(giver);
        Long supporterId = supporter.getId();

        // Get all the giverList where supporter equals to supporterId
        defaultGiverShouldBeFound("supporterId.equals=" + supporterId);

        // Get all the giverList where supporter equals to (supporterId + 1)
        defaultGiverShouldNotBeFound("supporterId.equals=" + (supporterId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultGiverShouldBeFound(String filter) throws Exception {
        restGiverMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(giver.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].family").value(hasItem(DEFAULT_FAMILY)))
            .andExpect(jsonPath("$.[*].phoneNumber").value(hasItem(DEFAULT_PHONE_NUMBER)))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE)))
            .andExpect(jsonPath("$.[*].address").value(hasItem(DEFAULT_ADDRESS)))
            .andExpect(jsonPath("$.[*].absorbDate").value(hasItem(DEFAULT_ABSORB_DATE.toString())));

        // Check, that the count call also returns 1
        restGiverMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultGiverShouldNotBeFound(String filter) throws Exception {
        restGiverMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restGiverMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingGiver() throws Exception {
        // Get the giver
        restGiverMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewGiver() throws Exception {
        // Initialize the database
        giverRepository.saveAndFlush(giver);

        int databaseSizeBeforeUpdate = giverRepository.findAll().size();

        // Update the giver
        Giver updatedGiver = giverRepository.findById(giver.getId()).get();
        // Disconnect from session so that the updates on updatedGiver are not directly saved in db
        em.detach(updatedGiver);
        updatedGiver
            .name(UPDATED_NAME)
            .family(UPDATED_FAMILY)
            .phoneNumber(UPDATED_PHONE_NUMBER)
            .code(UPDATED_CODE)
            .address(UPDATED_ADDRESS)
            .absorbDate(UPDATED_ABSORB_DATE);

        restGiverMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedGiver.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedGiver))
            )
            .andExpect(status().isOk());

        // Validate the Giver in the database
        List<Giver> giverList = giverRepository.findAll();
        assertThat(giverList).hasSize(databaseSizeBeforeUpdate);
        Giver testGiver = giverList.get(giverList.size() - 1);
        assertThat(testGiver.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testGiver.getFamily()).isEqualTo(UPDATED_FAMILY);
        assertThat(testGiver.getPhoneNumber()).isEqualTo(UPDATED_PHONE_NUMBER);
        assertThat(testGiver.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testGiver.getAddress()).isEqualTo(UPDATED_ADDRESS);
        assertThat(testGiver.getAbsorbDate()).isEqualTo(UPDATED_ABSORB_DATE);
    }

    @Test
    @Transactional
    void putNonExistingGiver() throws Exception {
        int databaseSizeBeforeUpdate = giverRepository.findAll().size();
        giver.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restGiverMockMvc
            .perform(
                put(ENTITY_API_URL_ID, giver.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(giver))
            )
            .andExpect(status().isBadRequest());

        // Validate the Giver in the database
        List<Giver> giverList = giverRepository.findAll();
        assertThat(giverList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchGiver() throws Exception {
        int databaseSizeBeforeUpdate = giverRepository.findAll().size();
        giver.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restGiverMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(giver))
            )
            .andExpect(status().isBadRequest());

        // Validate the Giver in the database
        List<Giver> giverList = giverRepository.findAll();
        assertThat(giverList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamGiver() throws Exception {
        int databaseSizeBeforeUpdate = giverRepository.findAll().size();
        giver.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restGiverMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(giver)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Giver in the database
        List<Giver> giverList = giverRepository.findAll();
        assertThat(giverList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateGiverWithPatch() throws Exception {
        // Initialize the database
        giverRepository.saveAndFlush(giver);

        int databaseSizeBeforeUpdate = giverRepository.findAll().size();

        // Update the giver using partial update
        Giver partialUpdatedGiver = new Giver();
        partialUpdatedGiver.setId(giver.getId());

        partialUpdatedGiver.name(UPDATED_NAME).phoneNumber(UPDATED_PHONE_NUMBER).code(UPDATED_CODE).absorbDate(UPDATED_ABSORB_DATE);

        restGiverMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedGiver.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedGiver))
            )
            .andExpect(status().isOk());

        // Validate the Giver in the database
        List<Giver> giverList = giverRepository.findAll();
        assertThat(giverList).hasSize(databaseSizeBeforeUpdate);
        Giver testGiver = giverList.get(giverList.size() - 1);
        assertThat(testGiver.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testGiver.getFamily()).isEqualTo(DEFAULT_FAMILY);
        assertThat(testGiver.getPhoneNumber()).isEqualTo(UPDATED_PHONE_NUMBER);
        assertThat(testGiver.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testGiver.getAddress()).isEqualTo(DEFAULT_ADDRESS);
        assertThat(testGiver.getAbsorbDate()).isEqualTo(UPDATED_ABSORB_DATE);
    }

    @Test
    @Transactional
    void fullUpdateGiverWithPatch() throws Exception {
        // Initialize the database
        giverRepository.saveAndFlush(giver);

        int databaseSizeBeforeUpdate = giverRepository.findAll().size();

        // Update the giver using partial update
        Giver partialUpdatedGiver = new Giver();
        partialUpdatedGiver.setId(giver.getId());

        partialUpdatedGiver
            .name(UPDATED_NAME)
            .family(UPDATED_FAMILY)
            .phoneNumber(UPDATED_PHONE_NUMBER)
            .code(UPDATED_CODE)
            .address(UPDATED_ADDRESS)
            .absorbDate(UPDATED_ABSORB_DATE);

        restGiverMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedGiver.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedGiver))
            )
            .andExpect(status().isOk());

        // Validate the Giver in the database
        List<Giver> giverList = giverRepository.findAll();
        assertThat(giverList).hasSize(databaseSizeBeforeUpdate);
        Giver testGiver = giverList.get(giverList.size() - 1);
        assertThat(testGiver.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testGiver.getFamily()).isEqualTo(UPDATED_FAMILY);
        assertThat(testGiver.getPhoneNumber()).isEqualTo(UPDATED_PHONE_NUMBER);
        assertThat(testGiver.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testGiver.getAddress()).isEqualTo(UPDATED_ADDRESS);
        assertThat(testGiver.getAbsorbDate()).isEqualTo(UPDATED_ABSORB_DATE);
    }

    @Test
    @Transactional
    void patchNonExistingGiver() throws Exception {
        int databaseSizeBeforeUpdate = giverRepository.findAll().size();
        giver.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restGiverMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, giver.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(giver))
            )
            .andExpect(status().isBadRequest());

        // Validate the Giver in the database
        List<Giver> giverList = giverRepository.findAll();
        assertThat(giverList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchGiver() throws Exception {
        int databaseSizeBeforeUpdate = giverRepository.findAll().size();
        giver.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restGiverMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(giver))
            )
            .andExpect(status().isBadRequest());

        // Validate the Giver in the database
        List<Giver> giverList = giverRepository.findAll();
        assertThat(giverList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamGiver() throws Exception {
        int databaseSizeBeforeUpdate = giverRepository.findAll().size();
        giver.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restGiverMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(giver)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Giver in the database
        List<Giver> giverList = giverRepository.findAll();
        assertThat(giverList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteGiver() throws Exception {
        // Initialize the database
        giverRepository.saveAndFlush(giver);

        int databaseSizeBeforeDelete = giverRepository.findAll().size();

        // Delete the giver
        restGiverMockMvc
            .perform(delete(ENTITY_API_URL_ID, giver.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Giver> giverList = giverRepository.findAll();
        assertThat(giverList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
