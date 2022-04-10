package com.armaghanehayat.autism.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.armaghanehayat.autism.IntegrationTest;
import com.armaghanehayat.autism.domain.Donation;
import com.armaghanehayat.autism.domain.Giver;
import com.armaghanehayat.autism.domain.enumeration.Account;
import com.armaghanehayat.autism.domain.enumeration.HelpType;
import com.armaghanehayat.autism.repository.DonationRepository;
import com.armaghanehayat.autism.service.criteria.DonationCriteria;
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
import org.springframework.util.Base64Utils;

/**
 * Integration tests for the {@link DonationResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class DonationResourceIT {

    private static final Boolean DEFAULT_IS_CASH = false;
    private static final Boolean UPDATED_IS_CASH = true;

    private static final Long DEFAULT_AMOUNT = 1L;
    private static final Long UPDATED_AMOUNT = 2L;
    private static final Long SMALLER_AMOUNT = 1L - 1L;

    private static final Instant DEFAULT_DONATION_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_DONATION_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final HelpType DEFAULT_HELP_TYPE = HelpType.BERENJ;
    private static final HelpType UPDATED_HELP_TYPE = HelpType.ROGHAN;

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final byte[] DEFAULT_RECEIPT = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_RECEIPT = TestUtil.createByteArray(1, "1");
    private static final String DEFAULT_RECEIPT_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_RECEIPT_CONTENT_TYPE = "image/png";

    private static final Account DEFAULT_ACCOUNT = Account.MELLI;
    private static final Account UPDATED_ACCOUNT = Account.GHAVAMIN;

    private static final Instant DEFAULT_REGISTER_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_REGISTER_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/donations";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private DonationRepository donationRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restDonationMockMvc;

    private Donation donation;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Donation createEntity(EntityManager em) {
        Donation donation = new Donation()
            .isCash(DEFAULT_IS_CASH)
            .amount(DEFAULT_AMOUNT)
            .donationDate(DEFAULT_DONATION_DATE)
            .helpType(DEFAULT_HELP_TYPE)
            .description(DEFAULT_DESCRIPTION)
            .receipt(DEFAULT_RECEIPT)
            .receiptContentType(DEFAULT_RECEIPT_CONTENT_TYPE)
            .account(DEFAULT_ACCOUNT)
            .registerDate(DEFAULT_REGISTER_DATE);
        return donation;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Donation createUpdatedEntity(EntityManager em) {
        Donation donation = new Donation()
            .isCash(UPDATED_IS_CASH)
            .amount(UPDATED_AMOUNT)
            .donationDate(UPDATED_DONATION_DATE)
            .helpType(UPDATED_HELP_TYPE)
            .description(UPDATED_DESCRIPTION)
            .receipt(UPDATED_RECEIPT)
            .receiptContentType(UPDATED_RECEIPT_CONTENT_TYPE)
            .account(UPDATED_ACCOUNT)
            .registerDate(UPDATED_REGISTER_DATE);
        return donation;
    }

    @BeforeEach
    public void initTest() {
        donation = createEntity(em);
    }

    @Test
    @Transactional
    void createDonation() throws Exception {
        int databaseSizeBeforeCreate = donationRepository.findAll().size();
        // Create the Donation
        restDonationMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(donation)))
            .andExpect(status().isCreated());

        // Validate the Donation in the database
        List<Donation> donationList = donationRepository.findAll();
        assertThat(donationList).hasSize(databaseSizeBeforeCreate + 1);
        Donation testDonation = donationList.get(donationList.size() - 1);
        assertThat(testDonation.getIsCash()).isEqualTo(DEFAULT_IS_CASH);
        assertThat(testDonation.getAmount()).isEqualTo(DEFAULT_AMOUNT);
        assertThat(testDonation.getDonationDate()).isEqualTo(DEFAULT_DONATION_DATE);
        assertThat(testDonation.getHelpType()).isEqualTo(DEFAULT_HELP_TYPE);
        assertThat(testDonation.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testDonation.getReceipt()).isEqualTo(DEFAULT_RECEIPT);
        assertThat(testDonation.getReceiptContentType()).isEqualTo(DEFAULT_RECEIPT_CONTENT_TYPE);
        assertThat(testDonation.getAccount()).isEqualTo(DEFAULT_ACCOUNT);
        assertThat(testDonation.getRegisterDate()).isEqualTo(DEFAULT_REGISTER_DATE);
    }

    @Test
    @Transactional
    void createDonationWithExistingId() throws Exception {
        // Create the Donation with an existing ID
        donation.setId(1L);

        int databaseSizeBeforeCreate = donationRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restDonationMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(donation)))
            .andExpect(status().isBadRequest());

        // Validate the Donation in the database
        List<Donation> donationList = donationRepository.findAll();
        assertThat(donationList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllDonations() throws Exception {
        // Initialize the database
        donationRepository.saveAndFlush(donation);

        // Get all the donationList
        restDonationMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(donation.getId().intValue())))
            .andExpect(jsonPath("$.[*].isCash").value(hasItem(DEFAULT_IS_CASH.booleanValue())))
            .andExpect(jsonPath("$.[*].amount").value(hasItem(DEFAULT_AMOUNT.intValue())))
            .andExpect(jsonPath("$.[*].donationDate").value(hasItem(DEFAULT_DONATION_DATE.toString())))
            .andExpect(jsonPath("$.[*].helpType").value(hasItem(DEFAULT_HELP_TYPE.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].receiptContentType").value(hasItem(DEFAULT_RECEIPT_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].receipt").value(hasItem(Base64Utils.encodeToString(DEFAULT_RECEIPT))))
            .andExpect(jsonPath("$.[*].account").value(hasItem(DEFAULT_ACCOUNT.toString())))
            .andExpect(jsonPath("$.[*].registerDate").value(hasItem(DEFAULT_REGISTER_DATE.toString())));
    }

    @Test
    @Transactional
    void getDonation() throws Exception {
        // Initialize the database
        donationRepository.saveAndFlush(donation);

        // Get the donation
        restDonationMockMvc
            .perform(get(ENTITY_API_URL_ID, donation.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(donation.getId().intValue()))
            .andExpect(jsonPath("$.isCash").value(DEFAULT_IS_CASH.booleanValue()))
            .andExpect(jsonPath("$.amount").value(DEFAULT_AMOUNT.intValue()))
            .andExpect(jsonPath("$.donationDate").value(DEFAULT_DONATION_DATE.toString()))
            .andExpect(jsonPath("$.helpType").value(DEFAULT_HELP_TYPE.toString()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.receiptContentType").value(DEFAULT_RECEIPT_CONTENT_TYPE))
            .andExpect(jsonPath("$.receipt").value(Base64Utils.encodeToString(DEFAULT_RECEIPT)))
            .andExpect(jsonPath("$.account").value(DEFAULT_ACCOUNT.toString()))
            .andExpect(jsonPath("$.registerDate").value(DEFAULT_REGISTER_DATE.toString()));
    }

    @Test
    @Transactional
    void getDonationsByIdFiltering() throws Exception {
        // Initialize the database
        donationRepository.saveAndFlush(donation);

        Long id = donation.getId();

        defaultDonationShouldBeFound("id.equals=" + id);
        defaultDonationShouldNotBeFound("id.notEquals=" + id);

        defaultDonationShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultDonationShouldNotBeFound("id.greaterThan=" + id);

        defaultDonationShouldBeFound("id.lessThanOrEqual=" + id);
        defaultDonationShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllDonationsByIsCashIsEqualToSomething() throws Exception {
        // Initialize the database
        donationRepository.saveAndFlush(donation);

        // Get all the donationList where isCash equals to DEFAULT_IS_CASH
        defaultDonationShouldBeFound("isCash.equals=" + DEFAULT_IS_CASH);

        // Get all the donationList where isCash equals to UPDATED_IS_CASH
        defaultDonationShouldNotBeFound("isCash.equals=" + UPDATED_IS_CASH);
    }

    @Test
    @Transactional
    void getAllDonationsByIsCashIsNotEqualToSomething() throws Exception {
        // Initialize the database
        donationRepository.saveAndFlush(donation);

        // Get all the donationList where isCash not equals to DEFAULT_IS_CASH
        defaultDonationShouldNotBeFound("isCash.notEquals=" + DEFAULT_IS_CASH);

        // Get all the donationList where isCash not equals to UPDATED_IS_CASH
        defaultDonationShouldBeFound("isCash.notEquals=" + UPDATED_IS_CASH);
    }

    @Test
    @Transactional
    void getAllDonationsByIsCashIsInShouldWork() throws Exception {
        // Initialize the database
        donationRepository.saveAndFlush(donation);

        // Get all the donationList where isCash in DEFAULT_IS_CASH or UPDATED_IS_CASH
        defaultDonationShouldBeFound("isCash.in=" + DEFAULT_IS_CASH + "," + UPDATED_IS_CASH);

        // Get all the donationList where isCash equals to UPDATED_IS_CASH
        defaultDonationShouldNotBeFound("isCash.in=" + UPDATED_IS_CASH);
    }

    @Test
    @Transactional
    void getAllDonationsByIsCashIsNullOrNotNull() throws Exception {
        // Initialize the database
        donationRepository.saveAndFlush(donation);

        // Get all the donationList where isCash is not null
        defaultDonationShouldBeFound("isCash.specified=true");

        // Get all the donationList where isCash is null
        defaultDonationShouldNotBeFound("isCash.specified=false");
    }

    @Test
    @Transactional
    void getAllDonationsByAmountIsEqualToSomething() throws Exception {
        // Initialize the database
        donationRepository.saveAndFlush(donation);

        // Get all the donationList where amount equals to DEFAULT_AMOUNT
        defaultDonationShouldBeFound("amount.equals=" + DEFAULT_AMOUNT);

        // Get all the donationList where amount equals to UPDATED_AMOUNT
        defaultDonationShouldNotBeFound("amount.equals=" + UPDATED_AMOUNT);
    }

    @Test
    @Transactional
    void getAllDonationsByAmountIsNotEqualToSomething() throws Exception {
        // Initialize the database
        donationRepository.saveAndFlush(donation);

        // Get all the donationList where amount not equals to DEFAULT_AMOUNT
        defaultDonationShouldNotBeFound("amount.notEquals=" + DEFAULT_AMOUNT);

        // Get all the donationList where amount not equals to UPDATED_AMOUNT
        defaultDonationShouldBeFound("amount.notEquals=" + UPDATED_AMOUNT);
    }

    @Test
    @Transactional
    void getAllDonationsByAmountIsInShouldWork() throws Exception {
        // Initialize the database
        donationRepository.saveAndFlush(donation);

        // Get all the donationList where amount in DEFAULT_AMOUNT or UPDATED_AMOUNT
        defaultDonationShouldBeFound("amount.in=" + DEFAULT_AMOUNT + "," + UPDATED_AMOUNT);

        // Get all the donationList where amount equals to UPDATED_AMOUNT
        defaultDonationShouldNotBeFound("amount.in=" + UPDATED_AMOUNT);
    }

    @Test
    @Transactional
    void getAllDonationsByAmountIsNullOrNotNull() throws Exception {
        // Initialize the database
        donationRepository.saveAndFlush(donation);

        // Get all the donationList where amount is not null
        defaultDonationShouldBeFound("amount.specified=true");

        // Get all the donationList where amount is null
        defaultDonationShouldNotBeFound("amount.specified=false");
    }

    @Test
    @Transactional
    void getAllDonationsByAmountIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        donationRepository.saveAndFlush(donation);

        // Get all the donationList where amount is greater than or equal to DEFAULT_AMOUNT
        defaultDonationShouldBeFound("amount.greaterThanOrEqual=" + DEFAULT_AMOUNT);

        // Get all the donationList where amount is greater than or equal to UPDATED_AMOUNT
        defaultDonationShouldNotBeFound("amount.greaterThanOrEqual=" + UPDATED_AMOUNT);
    }

    @Test
    @Transactional
    void getAllDonationsByAmountIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        donationRepository.saveAndFlush(donation);

        // Get all the donationList where amount is less than or equal to DEFAULT_AMOUNT
        defaultDonationShouldBeFound("amount.lessThanOrEqual=" + DEFAULT_AMOUNT);

        // Get all the donationList where amount is less than or equal to SMALLER_AMOUNT
        defaultDonationShouldNotBeFound("amount.lessThanOrEqual=" + SMALLER_AMOUNT);
    }

    @Test
    @Transactional
    void getAllDonationsByAmountIsLessThanSomething() throws Exception {
        // Initialize the database
        donationRepository.saveAndFlush(donation);

        // Get all the donationList where amount is less than DEFAULT_AMOUNT
        defaultDonationShouldNotBeFound("amount.lessThan=" + DEFAULT_AMOUNT);

        // Get all the donationList where amount is less than UPDATED_AMOUNT
        defaultDonationShouldBeFound("amount.lessThan=" + UPDATED_AMOUNT);
    }

    @Test
    @Transactional
    void getAllDonationsByAmountIsGreaterThanSomething() throws Exception {
        // Initialize the database
        donationRepository.saveAndFlush(donation);

        // Get all the donationList where amount is greater than DEFAULT_AMOUNT
        defaultDonationShouldNotBeFound("amount.greaterThan=" + DEFAULT_AMOUNT);

        // Get all the donationList where amount is greater than SMALLER_AMOUNT
        defaultDonationShouldBeFound("amount.greaterThan=" + SMALLER_AMOUNT);
    }

    @Test
    @Transactional
    void getAllDonationsByDonationDateIsEqualToSomething() throws Exception {
        // Initialize the database
        donationRepository.saveAndFlush(donation);

        // Get all the donationList where donationDate equals to DEFAULT_DONATION_DATE
        defaultDonationShouldBeFound("donationDate.equals=" + DEFAULT_DONATION_DATE);

        // Get all the donationList where donationDate equals to UPDATED_DONATION_DATE
        defaultDonationShouldNotBeFound("donationDate.equals=" + UPDATED_DONATION_DATE);
    }

    @Test
    @Transactional
    void getAllDonationsByDonationDateIsNotEqualToSomething() throws Exception {
        // Initialize the database
        donationRepository.saveAndFlush(donation);

        // Get all the donationList where donationDate not equals to DEFAULT_DONATION_DATE
        defaultDonationShouldNotBeFound("donationDate.notEquals=" + DEFAULT_DONATION_DATE);

        // Get all the donationList where donationDate not equals to UPDATED_DONATION_DATE
        defaultDonationShouldBeFound("donationDate.notEquals=" + UPDATED_DONATION_DATE);
    }

    @Test
    @Transactional
    void getAllDonationsByDonationDateIsInShouldWork() throws Exception {
        // Initialize the database
        donationRepository.saveAndFlush(donation);

        // Get all the donationList where donationDate in DEFAULT_DONATION_DATE or UPDATED_DONATION_DATE
        defaultDonationShouldBeFound("donationDate.in=" + DEFAULT_DONATION_DATE + "," + UPDATED_DONATION_DATE);

        // Get all the donationList where donationDate equals to UPDATED_DONATION_DATE
        defaultDonationShouldNotBeFound("donationDate.in=" + UPDATED_DONATION_DATE);
    }

    @Test
    @Transactional
    void getAllDonationsByDonationDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        donationRepository.saveAndFlush(donation);

        // Get all the donationList where donationDate is not null
        defaultDonationShouldBeFound("donationDate.specified=true");

        // Get all the donationList where donationDate is null
        defaultDonationShouldNotBeFound("donationDate.specified=false");
    }

    @Test
    @Transactional
    void getAllDonationsByHelpTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        donationRepository.saveAndFlush(donation);

        // Get all the donationList where helpType equals to DEFAULT_HELP_TYPE
        defaultDonationShouldBeFound("helpType.equals=" + DEFAULT_HELP_TYPE);

        // Get all the donationList where helpType equals to UPDATED_HELP_TYPE
        defaultDonationShouldNotBeFound("helpType.equals=" + UPDATED_HELP_TYPE);
    }

    @Test
    @Transactional
    void getAllDonationsByHelpTypeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        donationRepository.saveAndFlush(donation);

        // Get all the donationList where helpType not equals to DEFAULT_HELP_TYPE
        defaultDonationShouldNotBeFound("helpType.notEquals=" + DEFAULT_HELP_TYPE);

        // Get all the donationList where helpType not equals to UPDATED_HELP_TYPE
        defaultDonationShouldBeFound("helpType.notEquals=" + UPDATED_HELP_TYPE);
    }

    @Test
    @Transactional
    void getAllDonationsByHelpTypeIsInShouldWork() throws Exception {
        // Initialize the database
        donationRepository.saveAndFlush(donation);

        // Get all the donationList where helpType in DEFAULT_HELP_TYPE or UPDATED_HELP_TYPE
        defaultDonationShouldBeFound("helpType.in=" + DEFAULT_HELP_TYPE + "," + UPDATED_HELP_TYPE);

        // Get all the donationList where helpType equals to UPDATED_HELP_TYPE
        defaultDonationShouldNotBeFound("helpType.in=" + UPDATED_HELP_TYPE);
    }

    @Test
    @Transactional
    void getAllDonationsByHelpTypeIsNullOrNotNull() throws Exception {
        // Initialize the database
        donationRepository.saveAndFlush(donation);

        // Get all the donationList where helpType is not null
        defaultDonationShouldBeFound("helpType.specified=true");

        // Get all the donationList where helpType is null
        defaultDonationShouldNotBeFound("helpType.specified=false");
    }

    @Test
    @Transactional
    void getAllDonationsByDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        donationRepository.saveAndFlush(donation);

        // Get all the donationList where description equals to DEFAULT_DESCRIPTION
        defaultDonationShouldBeFound("description.equals=" + DEFAULT_DESCRIPTION);

        // Get all the donationList where description equals to UPDATED_DESCRIPTION
        defaultDonationShouldNotBeFound("description.equals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllDonationsByDescriptionIsNotEqualToSomething() throws Exception {
        // Initialize the database
        donationRepository.saveAndFlush(donation);

        // Get all the donationList where description not equals to DEFAULT_DESCRIPTION
        defaultDonationShouldNotBeFound("description.notEquals=" + DEFAULT_DESCRIPTION);

        // Get all the donationList where description not equals to UPDATED_DESCRIPTION
        defaultDonationShouldBeFound("description.notEquals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllDonationsByDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        donationRepository.saveAndFlush(donation);

        // Get all the donationList where description in DEFAULT_DESCRIPTION or UPDATED_DESCRIPTION
        defaultDonationShouldBeFound("description.in=" + DEFAULT_DESCRIPTION + "," + UPDATED_DESCRIPTION);

        // Get all the donationList where description equals to UPDATED_DESCRIPTION
        defaultDonationShouldNotBeFound("description.in=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllDonationsByDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        donationRepository.saveAndFlush(donation);

        // Get all the donationList where description is not null
        defaultDonationShouldBeFound("description.specified=true");

        // Get all the donationList where description is null
        defaultDonationShouldNotBeFound("description.specified=false");
    }

    @Test
    @Transactional
    void getAllDonationsByDescriptionContainsSomething() throws Exception {
        // Initialize the database
        donationRepository.saveAndFlush(donation);

        // Get all the donationList where description contains DEFAULT_DESCRIPTION
        defaultDonationShouldBeFound("description.contains=" + DEFAULT_DESCRIPTION);

        // Get all the donationList where description contains UPDATED_DESCRIPTION
        defaultDonationShouldNotBeFound("description.contains=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllDonationsByDescriptionNotContainsSomething() throws Exception {
        // Initialize the database
        donationRepository.saveAndFlush(donation);

        // Get all the donationList where description does not contain DEFAULT_DESCRIPTION
        defaultDonationShouldNotBeFound("description.doesNotContain=" + DEFAULT_DESCRIPTION);

        // Get all the donationList where description does not contain UPDATED_DESCRIPTION
        defaultDonationShouldBeFound("description.doesNotContain=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllDonationsByAccountIsEqualToSomething() throws Exception {
        // Initialize the database
        donationRepository.saveAndFlush(donation);

        // Get all the donationList where account equals to DEFAULT_ACCOUNT
        defaultDonationShouldBeFound("account.equals=" + DEFAULT_ACCOUNT);

        // Get all the donationList where account equals to UPDATED_ACCOUNT
        defaultDonationShouldNotBeFound("account.equals=" + UPDATED_ACCOUNT);
    }

    @Test
    @Transactional
    void getAllDonationsByAccountIsNotEqualToSomething() throws Exception {
        // Initialize the database
        donationRepository.saveAndFlush(donation);

        // Get all the donationList where account not equals to DEFAULT_ACCOUNT
        defaultDonationShouldNotBeFound("account.notEquals=" + DEFAULT_ACCOUNT);

        // Get all the donationList where account not equals to UPDATED_ACCOUNT
        defaultDonationShouldBeFound("account.notEquals=" + UPDATED_ACCOUNT);
    }

    @Test
    @Transactional
    void getAllDonationsByAccountIsInShouldWork() throws Exception {
        // Initialize the database
        donationRepository.saveAndFlush(donation);

        // Get all the donationList where account in DEFAULT_ACCOUNT or UPDATED_ACCOUNT
        defaultDonationShouldBeFound("account.in=" + DEFAULT_ACCOUNT + "," + UPDATED_ACCOUNT);

        // Get all the donationList where account equals to UPDATED_ACCOUNT
        defaultDonationShouldNotBeFound("account.in=" + UPDATED_ACCOUNT);
    }

    @Test
    @Transactional
    void getAllDonationsByAccountIsNullOrNotNull() throws Exception {
        // Initialize the database
        donationRepository.saveAndFlush(donation);

        // Get all the donationList where account is not null
        defaultDonationShouldBeFound("account.specified=true");

        // Get all the donationList where account is null
        defaultDonationShouldNotBeFound("account.specified=false");
    }

    @Test
    @Transactional
    void getAllDonationsByRegisterDateIsEqualToSomething() throws Exception {
        // Initialize the database
        donationRepository.saveAndFlush(donation);

        // Get all the donationList where registerDate equals to DEFAULT_REGISTER_DATE
        defaultDonationShouldBeFound("registerDate.equals=" + DEFAULT_REGISTER_DATE);

        // Get all the donationList where registerDate equals to UPDATED_REGISTER_DATE
        defaultDonationShouldNotBeFound("registerDate.equals=" + UPDATED_REGISTER_DATE);
    }

    @Test
    @Transactional
    void getAllDonationsByRegisterDateIsNotEqualToSomething() throws Exception {
        // Initialize the database
        donationRepository.saveAndFlush(donation);

        // Get all the donationList where registerDate not equals to DEFAULT_REGISTER_DATE
        defaultDonationShouldNotBeFound("registerDate.notEquals=" + DEFAULT_REGISTER_DATE);

        // Get all the donationList where registerDate not equals to UPDATED_REGISTER_DATE
        defaultDonationShouldBeFound("registerDate.notEquals=" + UPDATED_REGISTER_DATE);
    }

    @Test
    @Transactional
    void getAllDonationsByRegisterDateIsInShouldWork() throws Exception {
        // Initialize the database
        donationRepository.saveAndFlush(donation);

        // Get all the donationList where registerDate in DEFAULT_REGISTER_DATE or UPDATED_REGISTER_DATE
        defaultDonationShouldBeFound("registerDate.in=" + DEFAULT_REGISTER_DATE + "," + UPDATED_REGISTER_DATE);

        // Get all the donationList where registerDate equals to UPDATED_REGISTER_DATE
        defaultDonationShouldNotBeFound("registerDate.in=" + UPDATED_REGISTER_DATE);
    }

    @Test
    @Transactional
    void getAllDonationsByRegisterDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        donationRepository.saveAndFlush(donation);

        // Get all the donationList where registerDate is not null
        defaultDonationShouldBeFound("registerDate.specified=true");

        // Get all the donationList where registerDate is null
        defaultDonationShouldNotBeFound("registerDate.specified=false");
    }

    @Test
    @Transactional
    void getAllDonationsByGiverIsEqualToSomething() throws Exception {
        // Initialize the database
        donationRepository.saveAndFlush(donation);
        Giver giver;
        if (TestUtil.findAll(em, Giver.class).isEmpty()) {
            giver = GiverResourceIT.createEntity(em);
            em.persist(giver);
            em.flush();
        } else {
            giver = TestUtil.findAll(em, Giver.class).get(0);
        }
        em.persist(giver);
        em.flush();
        donation.setGiver(giver);
        donationRepository.saveAndFlush(donation);
        Long giverId = giver.getId();

        // Get all the donationList where giver equals to giverId
        defaultDonationShouldBeFound("giverId.equals=" + giverId);

        // Get all the donationList where giver equals to (giverId + 1)
        defaultDonationShouldNotBeFound("giverId.equals=" + (giverId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultDonationShouldBeFound(String filter) throws Exception {
        restDonationMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(donation.getId().intValue())))
            .andExpect(jsonPath("$.[*].isCash").value(hasItem(DEFAULT_IS_CASH.booleanValue())))
            .andExpect(jsonPath("$.[*].amount").value(hasItem(DEFAULT_AMOUNT.intValue())))
            .andExpect(jsonPath("$.[*].donationDate").value(hasItem(DEFAULT_DONATION_DATE.toString())))
            .andExpect(jsonPath("$.[*].helpType").value(hasItem(DEFAULT_HELP_TYPE.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].receiptContentType").value(hasItem(DEFAULT_RECEIPT_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].receipt").value(hasItem(Base64Utils.encodeToString(DEFAULT_RECEIPT))))
            .andExpect(jsonPath("$.[*].account").value(hasItem(DEFAULT_ACCOUNT.toString())))
            .andExpect(jsonPath("$.[*].registerDate").value(hasItem(DEFAULT_REGISTER_DATE.toString())));

        // Check, that the count call also returns 1
        restDonationMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultDonationShouldNotBeFound(String filter) throws Exception {
        restDonationMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restDonationMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingDonation() throws Exception {
        // Get the donation
        restDonationMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewDonation() throws Exception {
        // Initialize the database
        donationRepository.saveAndFlush(donation);

        int databaseSizeBeforeUpdate = donationRepository.findAll().size();

        // Update the donation
        Donation updatedDonation = donationRepository.findById(donation.getId()).get();
        // Disconnect from session so that the updates on updatedDonation are not directly saved in db
        em.detach(updatedDonation);
        updatedDonation
            .isCash(UPDATED_IS_CASH)
            .amount(UPDATED_AMOUNT)
            .donationDate(UPDATED_DONATION_DATE)
            .helpType(UPDATED_HELP_TYPE)
            .description(UPDATED_DESCRIPTION)
            .receipt(UPDATED_RECEIPT)
            .receiptContentType(UPDATED_RECEIPT_CONTENT_TYPE)
            .account(UPDATED_ACCOUNT)
            .registerDate(UPDATED_REGISTER_DATE);

        restDonationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedDonation.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedDonation))
            )
            .andExpect(status().isOk());

        // Validate the Donation in the database
        List<Donation> donationList = donationRepository.findAll();
        assertThat(donationList).hasSize(databaseSizeBeforeUpdate);
        Donation testDonation = donationList.get(donationList.size() - 1);
        assertThat(testDonation.getIsCash()).isEqualTo(UPDATED_IS_CASH);
        assertThat(testDonation.getAmount()).isEqualTo(UPDATED_AMOUNT);
        assertThat(testDonation.getDonationDate()).isEqualTo(UPDATED_DONATION_DATE);
        assertThat(testDonation.getHelpType()).isEqualTo(UPDATED_HELP_TYPE);
        assertThat(testDonation.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testDonation.getReceipt()).isEqualTo(UPDATED_RECEIPT);
        assertThat(testDonation.getReceiptContentType()).isEqualTo(UPDATED_RECEIPT_CONTENT_TYPE);
        assertThat(testDonation.getAccount()).isEqualTo(UPDATED_ACCOUNT);
        assertThat(testDonation.getRegisterDate()).isEqualTo(UPDATED_REGISTER_DATE);
    }

    @Test
    @Transactional
    void putNonExistingDonation() throws Exception {
        int databaseSizeBeforeUpdate = donationRepository.findAll().size();
        donation.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDonationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, donation.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(donation))
            )
            .andExpect(status().isBadRequest());

        // Validate the Donation in the database
        List<Donation> donationList = donationRepository.findAll();
        assertThat(donationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchDonation() throws Exception {
        int databaseSizeBeforeUpdate = donationRepository.findAll().size();
        donation.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDonationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(donation))
            )
            .andExpect(status().isBadRequest());

        // Validate the Donation in the database
        List<Donation> donationList = donationRepository.findAll();
        assertThat(donationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamDonation() throws Exception {
        int databaseSizeBeforeUpdate = donationRepository.findAll().size();
        donation.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDonationMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(donation)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Donation in the database
        List<Donation> donationList = donationRepository.findAll();
        assertThat(donationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateDonationWithPatch() throws Exception {
        // Initialize the database
        donationRepository.saveAndFlush(donation);

        int databaseSizeBeforeUpdate = donationRepository.findAll().size();

        // Update the donation using partial update
        Donation partialUpdatedDonation = new Donation();
        partialUpdatedDonation.setId(donation.getId());

        partialUpdatedDonation
            .isCash(UPDATED_IS_CASH)
            .donationDate(UPDATED_DONATION_DATE)
            .receipt(UPDATED_RECEIPT)
            .receiptContentType(UPDATED_RECEIPT_CONTENT_TYPE)
            .account(UPDATED_ACCOUNT)
            .registerDate(UPDATED_REGISTER_DATE);

        restDonationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDonation.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedDonation))
            )
            .andExpect(status().isOk());

        // Validate the Donation in the database
        List<Donation> donationList = donationRepository.findAll();
        assertThat(donationList).hasSize(databaseSizeBeforeUpdate);
        Donation testDonation = donationList.get(donationList.size() - 1);
        assertThat(testDonation.getIsCash()).isEqualTo(UPDATED_IS_CASH);
        assertThat(testDonation.getAmount()).isEqualTo(DEFAULT_AMOUNT);
        assertThat(testDonation.getDonationDate()).isEqualTo(UPDATED_DONATION_DATE);
        assertThat(testDonation.getHelpType()).isEqualTo(DEFAULT_HELP_TYPE);
        assertThat(testDonation.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testDonation.getReceipt()).isEqualTo(UPDATED_RECEIPT);
        assertThat(testDonation.getReceiptContentType()).isEqualTo(UPDATED_RECEIPT_CONTENT_TYPE);
        assertThat(testDonation.getAccount()).isEqualTo(UPDATED_ACCOUNT);
        assertThat(testDonation.getRegisterDate()).isEqualTo(UPDATED_REGISTER_DATE);
    }

    @Test
    @Transactional
    void fullUpdateDonationWithPatch() throws Exception {
        // Initialize the database
        donationRepository.saveAndFlush(donation);

        int databaseSizeBeforeUpdate = donationRepository.findAll().size();

        // Update the donation using partial update
        Donation partialUpdatedDonation = new Donation();
        partialUpdatedDonation.setId(donation.getId());

        partialUpdatedDonation
            .isCash(UPDATED_IS_CASH)
            .amount(UPDATED_AMOUNT)
            .donationDate(UPDATED_DONATION_DATE)
            .helpType(UPDATED_HELP_TYPE)
            .description(UPDATED_DESCRIPTION)
            .receipt(UPDATED_RECEIPT)
            .receiptContentType(UPDATED_RECEIPT_CONTENT_TYPE)
            .account(UPDATED_ACCOUNT)
            .registerDate(UPDATED_REGISTER_DATE);

        restDonationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDonation.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedDonation))
            )
            .andExpect(status().isOk());

        // Validate the Donation in the database
        List<Donation> donationList = donationRepository.findAll();
        assertThat(donationList).hasSize(databaseSizeBeforeUpdate);
        Donation testDonation = donationList.get(donationList.size() - 1);
        assertThat(testDonation.getIsCash()).isEqualTo(UPDATED_IS_CASH);
        assertThat(testDonation.getAmount()).isEqualTo(UPDATED_AMOUNT);
        assertThat(testDonation.getDonationDate()).isEqualTo(UPDATED_DONATION_DATE);
        assertThat(testDonation.getHelpType()).isEqualTo(UPDATED_HELP_TYPE);
        assertThat(testDonation.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testDonation.getReceipt()).isEqualTo(UPDATED_RECEIPT);
        assertThat(testDonation.getReceiptContentType()).isEqualTo(UPDATED_RECEIPT_CONTENT_TYPE);
        assertThat(testDonation.getAccount()).isEqualTo(UPDATED_ACCOUNT);
        assertThat(testDonation.getRegisterDate()).isEqualTo(UPDATED_REGISTER_DATE);
    }

    @Test
    @Transactional
    void patchNonExistingDonation() throws Exception {
        int databaseSizeBeforeUpdate = donationRepository.findAll().size();
        donation.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDonationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, donation.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(donation))
            )
            .andExpect(status().isBadRequest());

        // Validate the Donation in the database
        List<Donation> donationList = donationRepository.findAll();
        assertThat(donationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchDonation() throws Exception {
        int databaseSizeBeforeUpdate = donationRepository.findAll().size();
        donation.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDonationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(donation))
            )
            .andExpect(status().isBadRequest());

        // Validate the Donation in the database
        List<Donation> donationList = donationRepository.findAll();
        assertThat(donationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamDonation() throws Exception {
        int databaseSizeBeforeUpdate = donationRepository.findAll().size();
        donation.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDonationMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(donation)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Donation in the database
        List<Donation> donationList = donationRepository.findAll();
        assertThat(donationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteDonation() throws Exception {
        // Initialize the database
        donationRepository.saveAndFlush(donation);

        int databaseSizeBeforeDelete = donationRepository.findAll().size();

        // Delete the donation
        restDonationMockMvc
            .perform(delete(ENTITY_API_URL_ID, donation.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Donation> donationList = donationRepository.findAll();
        assertThat(donationList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
