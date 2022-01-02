package com.armaghanehayat.autism.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.armaghanehayat.autism.IntegrationTest;
import com.armaghanehayat.autism.domain.Ceremony;
import com.armaghanehayat.autism.domain.CeremonyUser;
import com.armaghanehayat.autism.repository.CeremonyRepository;
import com.armaghanehayat.autism.service.criteria.CeremonyCriteria;
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
 * Integration tests for the {@link CeremonyResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class CeremonyResourceIT {

    private static final Long DEFAULT_AMOUNT = 1L;
    private static final Long UPDATED_AMOUNT = 2L;
    private static final Long SMALLER_AMOUNT = 1L - 1L;

    private static final Instant DEFAULT_GIVEN_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_GIVEN_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final byte[] DEFAULT_RECEIPT = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_RECEIPT = TestUtil.createByteArray(1, "1");
    private static final String DEFAULT_RECEIPT_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_RECEIPT_CONTENT_TYPE = "image/png";

    private static final String ENTITY_API_URL = "/api/ceremonies";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private CeremonyRepository ceremonyRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCeremonyMockMvc;

    private Ceremony ceremony;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Ceremony createEntity(EntityManager em) {
        Ceremony ceremony = new Ceremony()
            .amount(DEFAULT_AMOUNT)
            .givenDate(DEFAULT_GIVEN_DATE)
            .description(DEFAULT_DESCRIPTION)
            .receipt(DEFAULT_RECEIPT)
            .receiptContentType(DEFAULT_RECEIPT_CONTENT_TYPE);
        return ceremony;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Ceremony createUpdatedEntity(EntityManager em) {
        Ceremony ceremony = new Ceremony()
            .amount(UPDATED_AMOUNT)
            .givenDate(UPDATED_GIVEN_DATE)
            .description(UPDATED_DESCRIPTION)
            .receipt(UPDATED_RECEIPT)
            .receiptContentType(UPDATED_RECEIPT_CONTENT_TYPE);
        return ceremony;
    }

    @BeforeEach
    public void initTest() {
        ceremony = createEntity(em);
    }

    @Test
    @Transactional
    void createCeremony() throws Exception {
        int databaseSizeBeforeCreate = ceremonyRepository.findAll().size();
        // Create the Ceremony
        restCeremonyMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(ceremony)))
            .andExpect(status().isCreated());

        // Validate the Ceremony in the database
        List<Ceremony> ceremonyList = ceremonyRepository.findAll();
        assertThat(ceremonyList).hasSize(databaseSizeBeforeCreate + 1);
        Ceremony testCeremony = ceremonyList.get(ceremonyList.size() - 1);
        assertThat(testCeremony.getAmount()).isEqualTo(DEFAULT_AMOUNT);
        assertThat(testCeremony.getGivenDate()).isEqualTo(DEFAULT_GIVEN_DATE);
        assertThat(testCeremony.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testCeremony.getReceipt()).isEqualTo(DEFAULT_RECEIPT);
        assertThat(testCeremony.getReceiptContentType()).isEqualTo(DEFAULT_RECEIPT_CONTENT_TYPE);
    }

    @Test
    @Transactional
    void createCeremonyWithExistingId() throws Exception {
        // Create the Ceremony with an existing ID
        ceremony.setId(1L);

        int databaseSizeBeforeCreate = ceremonyRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restCeremonyMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(ceremony)))
            .andExpect(status().isBadRequest());

        // Validate the Ceremony in the database
        List<Ceremony> ceremonyList = ceremonyRepository.findAll();
        assertThat(ceremonyList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllCeremonies() throws Exception {
        // Initialize the database
        ceremonyRepository.saveAndFlush(ceremony);

        // Get all the ceremonyList
        restCeremonyMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(ceremony.getId().intValue())))
            .andExpect(jsonPath("$.[*].amount").value(hasItem(DEFAULT_AMOUNT.intValue())))
            .andExpect(jsonPath("$.[*].givenDate").value(hasItem(DEFAULT_GIVEN_DATE.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].receiptContentType").value(hasItem(DEFAULT_RECEIPT_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].receipt").value(hasItem(Base64Utils.encodeToString(DEFAULT_RECEIPT))));
    }

    @Test
    @Transactional
    void getCeremony() throws Exception {
        // Initialize the database
        ceremonyRepository.saveAndFlush(ceremony);

        // Get the ceremony
        restCeremonyMockMvc
            .perform(get(ENTITY_API_URL_ID, ceremony.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(ceremony.getId().intValue()))
            .andExpect(jsonPath("$.amount").value(DEFAULT_AMOUNT.intValue()))
            .andExpect(jsonPath("$.givenDate").value(DEFAULT_GIVEN_DATE.toString()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.receiptContentType").value(DEFAULT_RECEIPT_CONTENT_TYPE))
            .andExpect(jsonPath("$.receipt").value(Base64Utils.encodeToString(DEFAULT_RECEIPT)));
    }

    @Test
    @Transactional
    void getCeremoniesByIdFiltering() throws Exception {
        // Initialize the database
        ceremonyRepository.saveAndFlush(ceremony);

        Long id = ceremony.getId();

        defaultCeremonyShouldBeFound("id.equals=" + id);
        defaultCeremonyShouldNotBeFound("id.notEquals=" + id);

        defaultCeremonyShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultCeremonyShouldNotBeFound("id.greaterThan=" + id);

        defaultCeremonyShouldBeFound("id.lessThanOrEqual=" + id);
        defaultCeremonyShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllCeremoniesByAmountIsEqualToSomething() throws Exception {
        // Initialize the database
        ceremonyRepository.saveAndFlush(ceremony);

        // Get all the ceremonyList where amount equals to DEFAULT_AMOUNT
        defaultCeremonyShouldBeFound("amount.equals=" + DEFAULT_AMOUNT);

        // Get all the ceremonyList where amount equals to UPDATED_AMOUNT
        defaultCeremonyShouldNotBeFound("amount.equals=" + UPDATED_AMOUNT);
    }

    @Test
    @Transactional
    void getAllCeremoniesByAmountIsNotEqualToSomething() throws Exception {
        // Initialize the database
        ceremonyRepository.saveAndFlush(ceremony);

        // Get all the ceremonyList where amount not equals to DEFAULT_AMOUNT
        defaultCeremonyShouldNotBeFound("amount.notEquals=" + DEFAULT_AMOUNT);

        // Get all the ceremonyList where amount not equals to UPDATED_AMOUNT
        defaultCeremonyShouldBeFound("amount.notEquals=" + UPDATED_AMOUNT);
    }

    @Test
    @Transactional
    void getAllCeremoniesByAmountIsInShouldWork() throws Exception {
        // Initialize the database
        ceremonyRepository.saveAndFlush(ceremony);

        // Get all the ceremonyList where amount in DEFAULT_AMOUNT or UPDATED_AMOUNT
        defaultCeremonyShouldBeFound("amount.in=" + DEFAULT_AMOUNT + "," + UPDATED_AMOUNT);

        // Get all the ceremonyList where amount equals to UPDATED_AMOUNT
        defaultCeremonyShouldNotBeFound("amount.in=" + UPDATED_AMOUNT);
    }

    @Test
    @Transactional
    void getAllCeremoniesByAmountIsNullOrNotNull() throws Exception {
        // Initialize the database
        ceremonyRepository.saveAndFlush(ceremony);

        // Get all the ceremonyList where amount is not null
        defaultCeremonyShouldBeFound("amount.specified=true");

        // Get all the ceremonyList where amount is null
        defaultCeremonyShouldNotBeFound("amount.specified=false");
    }

    @Test
    @Transactional
    void getAllCeremoniesByAmountIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        ceremonyRepository.saveAndFlush(ceremony);

        // Get all the ceremonyList where amount is greater than or equal to DEFAULT_AMOUNT
        defaultCeremonyShouldBeFound("amount.greaterThanOrEqual=" + DEFAULT_AMOUNT);

        // Get all the ceremonyList where amount is greater than or equal to UPDATED_AMOUNT
        defaultCeremonyShouldNotBeFound("amount.greaterThanOrEqual=" + UPDATED_AMOUNT);
    }

    @Test
    @Transactional
    void getAllCeremoniesByAmountIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        ceremonyRepository.saveAndFlush(ceremony);

        // Get all the ceremonyList where amount is less than or equal to DEFAULT_AMOUNT
        defaultCeremonyShouldBeFound("amount.lessThanOrEqual=" + DEFAULT_AMOUNT);

        // Get all the ceremonyList where amount is less than or equal to SMALLER_AMOUNT
        defaultCeremonyShouldNotBeFound("amount.lessThanOrEqual=" + SMALLER_AMOUNT);
    }

    @Test
    @Transactional
    void getAllCeremoniesByAmountIsLessThanSomething() throws Exception {
        // Initialize the database
        ceremonyRepository.saveAndFlush(ceremony);

        // Get all the ceremonyList where amount is less than DEFAULT_AMOUNT
        defaultCeremonyShouldNotBeFound("amount.lessThan=" + DEFAULT_AMOUNT);

        // Get all the ceremonyList where amount is less than UPDATED_AMOUNT
        defaultCeremonyShouldBeFound("amount.lessThan=" + UPDATED_AMOUNT);
    }

    @Test
    @Transactional
    void getAllCeremoniesByAmountIsGreaterThanSomething() throws Exception {
        // Initialize the database
        ceremonyRepository.saveAndFlush(ceremony);

        // Get all the ceremonyList where amount is greater than DEFAULT_AMOUNT
        defaultCeremonyShouldNotBeFound("amount.greaterThan=" + DEFAULT_AMOUNT);

        // Get all the ceremonyList where amount is greater than SMALLER_AMOUNT
        defaultCeremonyShouldBeFound("amount.greaterThan=" + SMALLER_AMOUNT);
    }

    @Test
    @Transactional
    void getAllCeremoniesByGivenDateIsEqualToSomething() throws Exception {
        // Initialize the database
        ceremonyRepository.saveAndFlush(ceremony);

        // Get all the ceremonyList where givenDate equals to DEFAULT_GIVEN_DATE
        defaultCeremonyShouldBeFound("givenDate.equals=" + DEFAULT_GIVEN_DATE);

        // Get all the ceremonyList where givenDate equals to UPDATED_GIVEN_DATE
        defaultCeremonyShouldNotBeFound("givenDate.equals=" + UPDATED_GIVEN_DATE);
    }

    @Test
    @Transactional
    void getAllCeremoniesByGivenDateIsNotEqualToSomething() throws Exception {
        // Initialize the database
        ceremonyRepository.saveAndFlush(ceremony);

        // Get all the ceremonyList where givenDate not equals to DEFAULT_GIVEN_DATE
        defaultCeremonyShouldNotBeFound("givenDate.notEquals=" + DEFAULT_GIVEN_DATE);

        // Get all the ceremonyList where givenDate not equals to UPDATED_GIVEN_DATE
        defaultCeremonyShouldBeFound("givenDate.notEquals=" + UPDATED_GIVEN_DATE);
    }

    @Test
    @Transactional
    void getAllCeremoniesByGivenDateIsInShouldWork() throws Exception {
        // Initialize the database
        ceremonyRepository.saveAndFlush(ceremony);

        // Get all the ceremonyList where givenDate in DEFAULT_GIVEN_DATE or UPDATED_GIVEN_DATE
        defaultCeremonyShouldBeFound("givenDate.in=" + DEFAULT_GIVEN_DATE + "," + UPDATED_GIVEN_DATE);

        // Get all the ceremonyList where givenDate equals to UPDATED_GIVEN_DATE
        defaultCeremonyShouldNotBeFound("givenDate.in=" + UPDATED_GIVEN_DATE);
    }

    @Test
    @Transactional
    void getAllCeremoniesByGivenDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        ceremonyRepository.saveAndFlush(ceremony);

        // Get all the ceremonyList where givenDate is not null
        defaultCeremonyShouldBeFound("givenDate.specified=true");

        // Get all the ceremonyList where givenDate is null
        defaultCeremonyShouldNotBeFound("givenDate.specified=false");
    }

    @Test
    @Transactional
    void getAllCeremoniesByDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        ceremonyRepository.saveAndFlush(ceremony);

        // Get all the ceremonyList where description equals to DEFAULT_DESCRIPTION
        defaultCeremonyShouldBeFound("description.equals=" + DEFAULT_DESCRIPTION);

        // Get all the ceremonyList where description equals to UPDATED_DESCRIPTION
        defaultCeremonyShouldNotBeFound("description.equals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllCeremoniesByDescriptionIsNotEqualToSomething() throws Exception {
        // Initialize the database
        ceremonyRepository.saveAndFlush(ceremony);

        // Get all the ceremonyList where description not equals to DEFAULT_DESCRIPTION
        defaultCeremonyShouldNotBeFound("description.notEquals=" + DEFAULT_DESCRIPTION);

        // Get all the ceremonyList where description not equals to UPDATED_DESCRIPTION
        defaultCeremonyShouldBeFound("description.notEquals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllCeremoniesByDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        ceremonyRepository.saveAndFlush(ceremony);

        // Get all the ceremonyList where description in DEFAULT_DESCRIPTION or UPDATED_DESCRIPTION
        defaultCeremonyShouldBeFound("description.in=" + DEFAULT_DESCRIPTION + "," + UPDATED_DESCRIPTION);

        // Get all the ceremonyList where description equals to UPDATED_DESCRIPTION
        defaultCeremonyShouldNotBeFound("description.in=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllCeremoniesByDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        ceremonyRepository.saveAndFlush(ceremony);

        // Get all the ceremonyList where description is not null
        defaultCeremonyShouldBeFound("description.specified=true");

        // Get all the ceremonyList where description is null
        defaultCeremonyShouldNotBeFound("description.specified=false");
    }

    @Test
    @Transactional
    void getAllCeremoniesByDescriptionContainsSomething() throws Exception {
        // Initialize the database
        ceremonyRepository.saveAndFlush(ceremony);

        // Get all the ceremonyList where description contains DEFAULT_DESCRIPTION
        defaultCeremonyShouldBeFound("description.contains=" + DEFAULT_DESCRIPTION);

        // Get all the ceremonyList where description contains UPDATED_DESCRIPTION
        defaultCeremonyShouldNotBeFound("description.contains=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllCeremoniesByDescriptionNotContainsSomething() throws Exception {
        // Initialize the database
        ceremonyRepository.saveAndFlush(ceremony);

        // Get all the ceremonyList where description does not contain DEFAULT_DESCRIPTION
        defaultCeremonyShouldNotBeFound("description.doesNotContain=" + DEFAULT_DESCRIPTION);

        // Get all the ceremonyList where description does not contain UPDATED_DESCRIPTION
        defaultCeremonyShouldBeFound("description.doesNotContain=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllCeremoniesByCeremonyUserIsEqualToSomething() throws Exception {
        // Initialize the database
        ceremonyRepository.saveAndFlush(ceremony);
        CeremonyUser ceremonyUser = CeremonyUserResourceIT.createEntity(em);
        em.persist(ceremonyUser);
        em.flush();
        ceremony.setCeremonyUser(ceremonyUser);
        ceremonyRepository.saveAndFlush(ceremony);
        Long ceremonyUserId = ceremonyUser.getId();

        // Get all the ceremonyList where ceremonyUser equals to ceremonyUserId
        defaultCeremonyShouldBeFound("ceremonyUserId.equals=" + ceremonyUserId);

        // Get all the ceremonyList where ceremonyUser equals to (ceremonyUserId + 1)
        defaultCeremonyShouldNotBeFound("ceremonyUserId.equals=" + (ceremonyUserId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultCeremonyShouldBeFound(String filter) throws Exception {
        restCeremonyMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(ceremony.getId().intValue())))
            .andExpect(jsonPath("$.[*].amount").value(hasItem(DEFAULT_AMOUNT.intValue())))
            .andExpect(jsonPath("$.[*].givenDate").value(hasItem(DEFAULT_GIVEN_DATE.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].receiptContentType").value(hasItem(DEFAULT_RECEIPT_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].receipt").value(hasItem(Base64Utils.encodeToString(DEFAULT_RECEIPT))));

        // Check, that the count call also returns 1
        restCeremonyMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultCeremonyShouldNotBeFound(String filter) throws Exception {
        restCeremonyMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restCeremonyMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingCeremony() throws Exception {
        // Get the ceremony
        restCeremonyMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewCeremony() throws Exception {
        // Initialize the database
        ceremonyRepository.saveAndFlush(ceremony);

        int databaseSizeBeforeUpdate = ceremonyRepository.findAll().size();

        // Update the ceremony
        Ceremony updatedCeremony = ceremonyRepository.findById(ceremony.getId()).get();
        // Disconnect from session so that the updates on updatedCeremony are not directly saved in db
        em.detach(updatedCeremony);
        updatedCeremony
            .amount(UPDATED_AMOUNT)
            .givenDate(UPDATED_GIVEN_DATE)
            .description(UPDATED_DESCRIPTION)
            .receipt(UPDATED_RECEIPT)
            .receiptContentType(UPDATED_RECEIPT_CONTENT_TYPE);

        restCeremonyMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedCeremony.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedCeremony))
            )
            .andExpect(status().isOk());

        // Validate the Ceremony in the database
        List<Ceremony> ceremonyList = ceremonyRepository.findAll();
        assertThat(ceremonyList).hasSize(databaseSizeBeforeUpdate);
        Ceremony testCeremony = ceremonyList.get(ceremonyList.size() - 1);
        assertThat(testCeremony.getAmount()).isEqualTo(UPDATED_AMOUNT);
        assertThat(testCeremony.getGivenDate()).isEqualTo(UPDATED_GIVEN_DATE);
        assertThat(testCeremony.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testCeremony.getReceipt()).isEqualTo(UPDATED_RECEIPT);
        assertThat(testCeremony.getReceiptContentType()).isEqualTo(UPDATED_RECEIPT_CONTENT_TYPE);
    }

    @Test
    @Transactional
    void putNonExistingCeremony() throws Exception {
        int databaseSizeBeforeUpdate = ceremonyRepository.findAll().size();
        ceremony.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCeremonyMockMvc
            .perform(
                put(ENTITY_API_URL_ID, ceremony.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(ceremony))
            )
            .andExpect(status().isBadRequest());

        // Validate the Ceremony in the database
        List<Ceremony> ceremonyList = ceremonyRepository.findAll();
        assertThat(ceremonyList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchCeremony() throws Exception {
        int databaseSizeBeforeUpdate = ceremonyRepository.findAll().size();
        ceremony.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCeremonyMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(ceremony))
            )
            .andExpect(status().isBadRequest());

        // Validate the Ceremony in the database
        List<Ceremony> ceremonyList = ceremonyRepository.findAll();
        assertThat(ceremonyList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamCeremony() throws Exception {
        int databaseSizeBeforeUpdate = ceremonyRepository.findAll().size();
        ceremony.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCeremonyMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(ceremony)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Ceremony in the database
        List<Ceremony> ceremonyList = ceremonyRepository.findAll();
        assertThat(ceremonyList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateCeremonyWithPatch() throws Exception {
        // Initialize the database
        ceremonyRepository.saveAndFlush(ceremony);

        int databaseSizeBeforeUpdate = ceremonyRepository.findAll().size();

        // Update the ceremony using partial update
        Ceremony partialUpdatedCeremony = new Ceremony();
        partialUpdatedCeremony.setId(ceremony.getId());

        partialUpdatedCeremony
            .amount(UPDATED_AMOUNT)
            .description(UPDATED_DESCRIPTION)
            .receipt(UPDATED_RECEIPT)
            .receiptContentType(UPDATED_RECEIPT_CONTENT_TYPE);

        restCeremonyMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCeremony.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCeremony))
            )
            .andExpect(status().isOk());

        // Validate the Ceremony in the database
        List<Ceremony> ceremonyList = ceremonyRepository.findAll();
        assertThat(ceremonyList).hasSize(databaseSizeBeforeUpdate);
        Ceremony testCeremony = ceremonyList.get(ceremonyList.size() - 1);
        assertThat(testCeremony.getAmount()).isEqualTo(UPDATED_AMOUNT);
        assertThat(testCeremony.getGivenDate()).isEqualTo(DEFAULT_GIVEN_DATE);
        assertThat(testCeremony.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testCeremony.getReceipt()).isEqualTo(UPDATED_RECEIPT);
        assertThat(testCeremony.getReceiptContentType()).isEqualTo(UPDATED_RECEIPT_CONTENT_TYPE);
    }

    @Test
    @Transactional
    void fullUpdateCeremonyWithPatch() throws Exception {
        // Initialize the database
        ceremonyRepository.saveAndFlush(ceremony);

        int databaseSizeBeforeUpdate = ceremonyRepository.findAll().size();

        // Update the ceremony using partial update
        Ceremony partialUpdatedCeremony = new Ceremony();
        partialUpdatedCeremony.setId(ceremony.getId());

        partialUpdatedCeremony
            .amount(UPDATED_AMOUNT)
            .givenDate(UPDATED_GIVEN_DATE)
            .description(UPDATED_DESCRIPTION)
            .receipt(UPDATED_RECEIPT)
            .receiptContentType(UPDATED_RECEIPT_CONTENT_TYPE);

        restCeremonyMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCeremony.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCeremony))
            )
            .andExpect(status().isOk());

        // Validate the Ceremony in the database
        List<Ceremony> ceremonyList = ceremonyRepository.findAll();
        assertThat(ceremonyList).hasSize(databaseSizeBeforeUpdate);
        Ceremony testCeremony = ceremonyList.get(ceremonyList.size() - 1);
        assertThat(testCeremony.getAmount()).isEqualTo(UPDATED_AMOUNT);
        assertThat(testCeremony.getGivenDate()).isEqualTo(UPDATED_GIVEN_DATE);
        assertThat(testCeremony.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testCeremony.getReceipt()).isEqualTo(UPDATED_RECEIPT);
        assertThat(testCeremony.getReceiptContentType()).isEqualTo(UPDATED_RECEIPT_CONTENT_TYPE);
    }

    @Test
    @Transactional
    void patchNonExistingCeremony() throws Exception {
        int databaseSizeBeforeUpdate = ceremonyRepository.findAll().size();
        ceremony.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCeremonyMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, ceremony.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(ceremony))
            )
            .andExpect(status().isBadRequest());

        // Validate the Ceremony in the database
        List<Ceremony> ceremonyList = ceremonyRepository.findAll();
        assertThat(ceremonyList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchCeremony() throws Exception {
        int databaseSizeBeforeUpdate = ceremonyRepository.findAll().size();
        ceremony.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCeremonyMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(ceremony))
            )
            .andExpect(status().isBadRequest());

        // Validate the Ceremony in the database
        List<Ceremony> ceremonyList = ceremonyRepository.findAll();
        assertThat(ceremonyList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamCeremony() throws Exception {
        int databaseSizeBeforeUpdate = ceremonyRepository.findAll().size();
        ceremony.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCeremonyMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(ceremony)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Ceremony in the database
        List<Ceremony> ceremonyList = ceremonyRepository.findAll();
        assertThat(ceremonyList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteCeremony() throws Exception {
        // Initialize the database
        ceremonyRepository.saveAndFlush(ceremony);

        int databaseSizeBeforeDelete = ceremonyRepository.findAll().size();

        // Delete the ceremony
        restCeremonyMockMvc
            .perform(delete(ENTITY_API_URL_ID, ceremony.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Ceremony> ceremonyList = ceremonyRepository.findAll();
        assertThat(ceremonyList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
