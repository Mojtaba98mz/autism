package com.armaghanehayat.autism.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.armaghanehayat.autism.IntegrationTest;
import com.armaghanehayat.autism.domain.City;
import com.armaghanehayat.autism.domain.Giver;
import com.armaghanehayat.autism.domain.Province;
import com.armaghanehayat.autism.repository.ProvinceRepository;
import com.armaghanehayat.autism.service.criteria.ProvinceCriteria;
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
 * Integration tests for the {@link ProvinceResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ProvinceResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_EN_NAME = "AAAAAAAAAA";
    private static final String UPDATED_EN_NAME = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/provinces";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ProvinceRepository provinceRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restProvinceMockMvc;

    private Province province;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Province createEntity(EntityManager em) {
        Province province = new Province().name(DEFAULT_NAME).enName(DEFAULT_EN_NAME);
        return province;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Province createUpdatedEntity(EntityManager em) {
        Province province = new Province().name(UPDATED_NAME).enName(UPDATED_EN_NAME);
        return province;
    }

    @BeforeEach
    public void initTest() {
        province = createEntity(em);
    }

    @Test
    @Transactional
    void createProvince() throws Exception {
        int databaseSizeBeforeCreate = provinceRepository.findAll().size();
        // Create the Province
        restProvinceMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(province)))
            .andExpect(status().isCreated());

        // Validate the Province in the database
        List<Province> provinceList = provinceRepository.findAll();
        assertThat(provinceList).hasSize(databaseSizeBeforeCreate + 1);
        Province testProvince = provinceList.get(provinceList.size() - 1);
        assertThat(testProvince.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testProvince.getEnName()).isEqualTo(DEFAULT_EN_NAME);
    }

    @Test
    @Transactional
    void createProvinceWithExistingId() throws Exception {
        // Create the Province with an existing ID
        province.setId(1L);

        int databaseSizeBeforeCreate = provinceRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restProvinceMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(province)))
            .andExpect(status().isBadRequest());

        // Validate the Province in the database
        List<Province> provinceList = provinceRepository.findAll();
        assertThat(provinceList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = provinceRepository.findAll().size();
        // set the field null
        province.setName(null);

        // Create the Province, which fails.

        restProvinceMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(province)))
            .andExpect(status().isBadRequest());

        List<Province> provinceList = provinceRepository.findAll();
        assertThat(provinceList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkEnNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = provinceRepository.findAll().size();
        // set the field null
        province.setEnName(null);

        // Create the Province, which fails.

        restProvinceMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(province)))
            .andExpect(status().isBadRequest());

        List<Province> provinceList = provinceRepository.findAll();
        assertThat(provinceList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllProvinces() throws Exception {
        // Initialize the database
        provinceRepository.saveAndFlush(province);

        // Get all the provinceList
        restProvinceMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(province.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].enName").value(hasItem(DEFAULT_EN_NAME)));
    }

    @Test
    @Transactional
    void getProvince() throws Exception {
        // Initialize the database
        provinceRepository.saveAndFlush(province);

        // Get the province
        restProvinceMockMvc
            .perform(get(ENTITY_API_URL_ID, province.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(province.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.enName").value(DEFAULT_EN_NAME));
    }

    @Test
    @Transactional
    void getProvincesByIdFiltering() throws Exception {
        // Initialize the database
        provinceRepository.saveAndFlush(province);

        Long id = province.getId();

        defaultProvinceShouldBeFound("id.equals=" + id);
        defaultProvinceShouldNotBeFound("id.notEquals=" + id);

        defaultProvinceShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultProvinceShouldNotBeFound("id.greaterThan=" + id);

        defaultProvinceShouldBeFound("id.lessThanOrEqual=" + id);
        defaultProvinceShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllProvincesByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        provinceRepository.saveAndFlush(province);

        // Get all the provinceList where name equals to DEFAULT_NAME
        defaultProvinceShouldBeFound("name.equals=" + DEFAULT_NAME);

        // Get all the provinceList where name equals to UPDATED_NAME
        defaultProvinceShouldNotBeFound("name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllProvincesByNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        provinceRepository.saveAndFlush(province);

        // Get all the provinceList where name not equals to DEFAULT_NAME
        defaultProvinceShouldNotBeFound("name.notEquals=" + DEFAULT_NAME);

        // Get all the provinceList where name not equals to UPDATED_NAME
        defaultProvinceShouldBeFound("name.notEquals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllProvincesByNameIsInShouldWork() throws Exception {
        // Initialize the database
        provinceRepository.saveAndFlush(province);

        // Get all the provinceList where name in DEFAULT_NAME or UPDATED_NAME
        defaultProvinceShouldBeFound("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME);

        // Get all the provinceList where name equals to UPDATED_NAME
        defaultProvinceShouldNotBeFound("name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllProvincesByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        provinceRepository.saveAndFlush(province);

        // Get all the provinceList where name is not null
        defaultProvinceShouldBeFound("name.specified=true");

        // Get all the provinceList where name is null
        defaultProvinceShouldNotBeFound("name.specified=false");
    }

    @Test
    @Transactional
    void getAllProvincesByNameContainsSomething() throws Exception {
        // Initialize the database
        provinceRepository.saveAndFlush(province);

        // Get all the provinceList where name contains DEFAULT_NAME
        defaultProvinceShouldBeFound("name.contains=" + DEFAULT_NAME);

        // Get all the provinceList where name contains UPDATED_NAME
        defaultProvinceShouldNotBeFound("name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllProvincesByNameNotContainsSomething() throws Exception {
        // Initialize the database
        provinceRepository.saveAndFlush(province);

        // Get all the provinceList where name does not contain DEFAULT_NAME
        defaultProvinceShouldNotBeFound("name.doesNotContain=" + DEFAULT_NAME);

        // Get all the provinceList where name does not contain UPDATED_NAME
        defaultProvinceShouldBeFound("name.doesNotContain=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllProvincesByEnNameIsEqualToSomething() throws Exception {
        // Initialize the database
        provinceRepository.saveAndFlush(province);

        // Get all the provinceList where enName equals to DEFAULT_EN_NAME
        defaultProvinceShouldBeFound("enName.equals=" + DEFAULT_EN_NAME);

        // Get all the provinceList where enName equals to UPDATED_EN_NAME
        defaultProvinceShouldNotBeFound("enName.equals=" + UPDATED_EN_NAME);
    }

    @Test
    @Transactional
    void getAllProvincesByEnNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        provinceRepository.saveAndFlush(province);

        // Get all the provinceList where enName not equals to DEFAULT_EN_NAME
        defaultProvinceShouldNotBeFound("enName.notEquals=" + DEFAULT_EN_NAME);

        // Get all the provinceList where enName not equals to UPDATED_EN_NAME
        defaultProvinceShouldBeFound("enName.notEquals=" + UPDATED_EN_NAME);
    }

    @Test
    @Transactional
    void getAllProvincesByEnNameIsInShouldWork() throws Exception {
        // Initialize the database
        provinceRepository.saveAndFlush(province);

        // Get all the provinceList where enName in DEFAULT_EN_NAME or UPDATED_EN_NAME
        defaultProvinceShouldBeFound("enName.in=" + DEFAULT_EN_NAME + "," + UPDATED_EN_NAME);

        // Get all the provinceList where enName equals to UPDATED_EN_NAME
        defaultProvinceShouldNotBeFound("enName.in=" + UPDATED_EN_NAME);
    }

    @Test
    @Transactional
    void getAllProvincesByEnNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        provinceRepository.saveAndFlush(province);

        // Get all the provinceList where enName is not null
        defaultProvinceShouldBeFound("enName.specified=true");

        // Get all the provinceList where enName is null
        defaultProvinceShouldNotBeFound("enName.specified=false");
    }

    @Test
    @Transactional
    void getAllProvincesByEnNameContainsSomething() throws Exception {
        // Initialize the database
        provinceRepository.saveAndFlush(province);

        // Get all the provinceList where enName contains DEFAULT_EN_NAME
        defaultProvinceShouldBeFound("enName.contains=" + DEFAULT_EN_NAME);

        // Get all the provinceList where enName contains UPDATED_EN_NAME
        defaultProvinceShouldNotBeFound("enName.contains=" + UPDATED_EN_NAME);
    }

    @Test
    @Transactional
    void getAllProvincesByEnNameNotContainsSomething() throws Exception {
        // Initialize the database
        provinceRepository.saveAndFlush(province);

        // Get all the provinceList where enName does not contain DEFAULT_EN_NAME
        defaultProvinceShouldNotBeFound("enName.doesNotContain=" + DEFAULT_EN_NAME);

        // Get all the provinceList where enName does not contain UPDATED_EN_NAME
        defaultProvinceShouldBeFound("enName.doesNotContain=" + UPDATED_EN_NAME);
    }

    @Test
    @Transactional
    void getAllProvincesByCityIsEqualToSomething() throws Exception {
        // Initialize the database
        provinceRepository.saveAndFlush(province);
        City city = CityResourceIT.createEntity(em);
        em.persist(city);
        em.flush();
        province.addCity(city);
        provinceRepository.saveAndFlush(province);
        Long cityId = city.getId();

        // Get all the provinceList where city equals to cityId
        defaultProvinceShouldBeFound("cityId.equals=" + cityId);

        // Get all the provinceList where city equals to (cityId + 1)
        defaultProvinceShouldNotBeFound("cityId.equals=" + (cityId + 1));
    }

    @Test
    @Transactional
    void getAllProvincesByGiverIsEqualToSomething() throws Exception {
        // Initialize the database
        provinceRepository.saveAndFlush(province);
        Giver giver = GiverResourceIT.createEntity(em);
        em.persist(giver);
        em.flush();
        province.addGiver(giver);
        provinceRepository.saveAndFlush(province);
        Long giverId = giver.getId();

        // Get all the provinceList where giver equals to giverId
        defaultProvinceShouldBeFound("giverId.equals=" + giverId);

        // Get all the provinceList where giver equals to (giverId + 1)
        defaultProvinceShouldNotBeFound("giverId.equals=" + (giverId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultProvinceShouldBeFound(String filter) throws Exception {
        restProvinceMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(province.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].enName").value(hasItem(DEFAULT_EN_NAME)));

        // Check, that the count call also returns 1
        restProvinceMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultProvinceShouldNotBeFound(String filter) throws Exception {
        restProvinceMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restProvinceMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingProvince() throws Exception {
        // Get the province
        restProvinceMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewProvince() throws Exception {
        // Initialize the database
        provinceRepository.saveAndFlush(province);

        int databaseSizeBeforeUpdate = provinceRepository.findAll().size();

        // Update the province
        Province updatedProvince = provinceRepository.findById(province.getId()).get();
        // Disconnect from session so that the updates on updatedProvince are not directly saved in db
        em.detach(updatedProvince);
        updatedProvince.name(UPDATED_NAME).enName(UPDATED_EN_NAME);

        restProvinceMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedProvince.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedProvince))
            )
            .andExpect(status().isOk());

        // Validate the Province in the database
        List<Province> provinceList = provinceRepository.findAll();
        assertThat(provinceList).hasSize(databaseSizeBeforeUpdate);
        Province testProvince = provinceList.get(provinceList.size() - 1);
        assertThat(testProvince.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testProvince.getEnName()).isEqualTo(UPDATED_EN_NAME);
    }

    @Test
    @Transactional
    void putNonExistingProvince() throws Exception {
        int databaseSizeBeforeUpdate = provinceRepository.findAll().size();
        province.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restProvinceMockMvc
            .perform(
                put(ENTITY_API_URL_ID, province.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(province))
            )
            .andExpect(status().isBadRequest());

        // Validate the Province in the database
        List<Province> provinceList = provinceRepository.findAll();
        assertThat(provinceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchProvince() throws Exception {
        int databaseSizeBeforeUpdate = provinceRepository.findAll().size();
        province.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProvinceMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(province))
            )
            .andExpect(status().isBadRequest());

        // Validate the Province in the database
        List<Province> provinceList = provinceRepository.findAll();
        assertThat(provinceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamProvince() throws Exception {
        int databaseSizeBeforeUpdate = provinceRepository.findAll().size();
        province.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProvinceMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(province)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Province in the database
        List<Province> provinceList = provinceRepository.findAll();
        assertThat(provinceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateProvinceWithPatch() throws Exception {
        // Initialize the database
        provinceRepository.saveAndFlush(province);

        int databaseSizeBeforeUpdate = provinceRepository.findAll().size();

        // Update the province using partial update
        Province partialUpdatedProvince = new Province();
        partialUpdatedProvince.setId(province.getId());

        partialUpdatedProvince.name(UPDATED_NAME);

        restProvinceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedProvince.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedProvince))
            )
            .andExpect(status().isOk());

        // Validate the Province in the database
        List<Province> provinceList = provinceRepository.findAll();
        assertThat(provinceList).hasSize(databaseSizeBeforeUpdate);
        Province testProvince = provinceList.get(provinceList.size() - 1);
        assertThat(testProvince.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testProvince.getEnName()).isEqualTo(DEFAULT_EN_NAME);
    }

    @Test
    @Transactional
    void fullUpdateProvinceWithPatch() throws Exception {
        // Initialize the database
        provinceRepository.saveAndFlush(province);

        int databaseSizeBeforeUpdate = provinceRepository.findAll().size();

        // Update the province using partial update
        Province partialUpdatedProvince = new Province();
        partialUpdatedProvince.setId(province.getId());

        partialUpdatedProvince.name(UPDATED_NAME).enName(UPDATED_EN_NAME);

        restProvinceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedProvince.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedProvince))
            )
            .andExpect(status().isOk());

        // Validate the Province in the database
        List<Province> provinceList = provinceRepository.findAll();
        assertThat(provinceList).hasSize(databaseSizeBeforeUpdate);
        Province testProvince = provinceList.get(provinceList.size() - 1);
        assertThat(testProvince.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testProvince.getEnName()).isEqualTo(UPDATED_EN_NAME);
    }

    @Test
    @Transactional
    void patchNonExistingProvince() throws Exception {
        int databaseSizeBeforeUpdate = provinceRepository.findAll().size();
        province.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restProvinceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, province.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(province))
            )
            .andExpect(status().isBadRequest());

        // Validate the Province in the database
        List<Province> provinceList = provinceRepository.findAll();
        assertThat(provinceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchProvince() throws Exception {
        int databaseSizeBeforeUpdate = provinceRepository.findAll().size();
        province.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProvinceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(province))
            )
            .andExpect(status().isBadRequest());

        // Validate the Province in the database
        List<Province> provinceList = provinceRepository.findAll();
        assertThat(provinceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamProvince() throws Exception {
        int databaseSizeBeforeUpdate = provinceRepository.findAll().size();
        province.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProvinceMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(province)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Province in the database
        List<Province> provinceList = provinceRepository.findAll();
        assertThat(provinceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteProvince() throws Exception {
        // Initialize the database
        provinceRepository.saveAndFlush(province);

        int databaseSizeBeforeDelete = provinceRepository.findAll().size();

        // Delete the province
        restProvinceMockMvc
            .perform(delete(ENTITY_API_URL_ID, province.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Province> provinceList = provinceRepository.findAll();
        assertThat(provinceList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
