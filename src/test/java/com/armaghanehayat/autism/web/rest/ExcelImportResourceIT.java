package com.armaghanehayat.autism.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.armaghanehayat.autism.IntegrationTest;
import com.armaghanehayat.autism.domain.ExcelImport;
import com.armaghanehayat.autism.repository.ExcelImportRepository;
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
 * Integration tests for the {@link ExcelImportResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ExcelImportResourceIT {

    private static final byte[] DEFAULT_EXCEL = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_EXCEL = TestUtil.createByteArray(1, "1");
    private static final String DEFAULT_EXCEL_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_EXCEL_CONTENT_TYPE = "image/png";

    private static final String ENTITY_API_URL = "/api/excel-imports";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ExcelImportRepository excelImportRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restExcelImportMockMvc;

    private ExcelImport excelImport;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ExcelImport createEntity(EntityManager em) {
        ExcelImport excelImport = new ExcelImport().excel(DEFAULT_EXCEL).excelContentType(DEFAULT_EXCEL_CONTENT_TYPE);
        return excelImport;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ExcelImport createUpdatedEntity(EntityManager em) {
        ExcelImport excelImport = new ExcelImport().excel(UPDATED_EXCEL).excelContentType(UPDATED_EXCEL_CONTENT_TYPE);
        return excelImport;
    }

    @BeforeEach
    public void initTest() {
        excelImport = createEntity(em);
    }

    @Test
    @Transactional
    void createExcelImport() throws Exception {
        int databaseSizeBeforeCreate = excelImportRepository.findAll().size();
        // Create the ExcelImport
        restExcelImportMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(excelImport)))
            .andExpect(status().isCreated());

        // Validate the ExcelImport in the database
        List<ExcelImport> excelImportList = excelImportRepository.findAll();
        assertThat(excelImportList).hasSize(databaseSizeBeforeCreate + 1);
        ExcelImport testExcelImport = excelImportList.get(excelImportList.size() - 1);
        assertThat(testExcelImport.getExcel()).isEqualTo(DEFAULT_EXCEL);
        assertThat(testExcelImport.getExcelContentType()).isEqualTo(DEFAULT_EXCEL_CONTENT_TYPE);
    }

    @Test
    @Transactional
    void createExcelImportWithExistingId() throws Exception {
        // Create the ExcelImport with an existing ID
        excelImport.setId(1L);

        int databaseSizeBeforeCreate = excelImportRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restExcelImportMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(excelImport)))
            .andExpect(status().isBadRequest());

        // Validate the ExcelImport in the database
        List<ExcelImport> excelImportList = excelImportRepository.findAll();
        assertThat(excelImportList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllExcelImports() throws Exception {
        // Initialize the database
        excelImportRepository.saveAndFlush(excelImport);

        // Get all the excelImportList
        restExcelImportMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(excelImport.getId().intValue())))
            .andExpect(jsonPath("$.[*].excelContentType").value(hasItem(DEFAULT_EXCEL_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].excel").value(hasItem(Base64Utils.encodeToString(DEFAULT_EXCEL))));
    }

    @Test
    @Transactional
    void getExcelImport() throws Exception {
        // Initialize the database
        excelImportRepository.saveAndFlush(excelImport);

        // Get the excelImport
        restExcelImportMockMvc
            .perform(get(ENTITY_API_URL_ID, excelImport.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(excelImport.getId().intValue()))
            .andExpect(jsonPath("$.excelContentType").value(DEFAULT_EXCEL_CONTENT_TYPE))
            .andExpect(jsonPath("$.excel").value(Base64Utils.encodeToString(DEFAULT_EXCEL)));
    }

    @Test
    @Transactional
    void getNonExistingExcelImport() throws Exception {
        // Get the excelImport
        restExcelImportMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewExcelImport() throws Exception {
        // Initialize the database
        excelImportRepository.saveAndFlush(excelImport);

        int databaseSizeBeforeUpdate = excelImportRepository.findAll().size();

        // Update the excelImport
        ExcelImport updatedExcelImport = excelImportRepository.findById(excelImport.getId()).get();
        // Disconnect from session so that the updates on updatedExcelImport are not directly saved in db
        em.detach(updatedExcelImport);
        updatedExcelImport.excel(UPDATED_EXCEL).excelContentType(UPDATED_EXCEL_CONTENT_TYPE);

        restExcelImportMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedExcelImport.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedExcelImport))
            )
            .andExpect(status().isOk());

        // Validate the ExcelImport in the database
        List<ExcelImport> excelImportList = excelImportRepository.findAll();
        assertThat(excelImportList).hasSize(databaseSizeBeforeUpdate);
        ExcelImport testExcelImport = excelImportList.get(excelImportList.size() - 1);
        assertThat(testExcelImport.getExcel()).isEqualTo(UPDATED_EXCEL);
        assertThat(testExcelImport.getExcelContentType()).isEqualTo(UPDATED_EXCEL_CONTENT_TYPE);
    }

    @Test
    @Transactional
    void putNonExistingExcelImport() throws Exception {
        int databaseSizeBeforeUpdate = excelImportRepository.findAll().size();
        excelImport.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restExcelImportMockMvc
            .perform(
                put(ENTITY_API_URL_ID, excelImport.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(excelImport))
            )
            .andExpect(status().isBadRequest());

        // Validate the ExcelImport in the database
        List<ExcelImport> excelImportList = excelImportRepository.findAll();
        assertThat(excelImportList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchExcelImport() throws Exception {
        int databaseSizeBeforeUpdate = excelImportRepository.findAll().size();
        excelImport.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restExcelImportMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(excelImport))
            )
            .andExpect(status().isBadRequest());

        // Validate the ExcelImport in the database
        List<ExcelImport> excelImportList = excelImportRepository.findAll();
        assertThat(excelImportList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamExcelImport() throws Exception {
        int databaseSizeBeforeUpdate = excelImportRepository.findAll().size();
        excelImport.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restExcelImportMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(excelImport)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ExcelImport in the database
        List<ExcelImport> excelImportList = excelImportRepository.findAll();
        assertThat(excelImportList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateExcelImportWithPatch() throws Exception {
        // Initialize the database
        excelImportRepository.saveAndFlush(excelImport);

        int databaseSizeBeforeUpdate = excelImportRepository.findAll().size();

        // Update the excelImport using partial update
        ExcelImport partialUpdatedExcelImport = new ExcelImport();
        partialUpdatedExcelImport.setId(excelImport.getId());

        partialUpdatedExcelImport.excel(UPDATED_EXCEL).excelContentType(UPDATED_EXCEL_CONTENT_TYPE);

        restExcelImportMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedExcelImport.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedExcelImport))
            )
            .andExpect(status().isOk());

        // Validate the ExcelImport in the database
        List<ExcelImport> excelImportList = excelImportRepository.findAll();
        assertThat(excelImportList).hasSize(databaseSizeBeforeUpdate);
        ExcelImport testExcelImport = excelImportList.get(excelImportList.size() - 1);
        assertThat(testExcelImport.getExcel()).isEqualTo(UPDATED_EXCEL);
        assertThat(testExcelImport.getExcelContentType()).isEqualTo(UPDATED_EXCEL_CONTENT_TYPE);
    }

    @Test
    @Transactional
    void fullUpdateExcelImportWithPatch() throws Exception {
        // Initialize the database
        excelImportRepository.saveAndFlush(excelImport);

        int databaseSizeBeforeUpdate = excelImportRepository.findAll().size();

        // Update the excelImport using partial update
        ExcelImport partialUpdatedExcelImport = new ExcelImport();
        partialUpdatedExcelImport.setId(excelImport.getId());

        partialUpdatedExcelImport.excel(UPDATED_EXCEL).excelContentType(UPDATED_EXCEL_CONTENT_TYPE);

        restExcelImportMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedExcelImport.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedExcelImport))
            )
            .andExpect(status().isOk());

        // Validate the ExcelImport in the database
        List<ExcelImport> excelImportList = excelImportRepository.findAll();
        assertThat(excelImportList).hasSize(databaseSizeBeforeUpdate);
        ExcelImport testExcelImport = excelImportList.get(excelImportList.size() - 1);
        assertThat(testExcelImport.getExcel()).isEqualTo(UPDATED_EXCEL);
        assertThat(testExcelImport.getExcelContentType()).isEqualTo(UPDATED_EXCEL_CONTENT_TYPE);
    }

    @Test
    @Transactional
    void patchNonExistingExcelImport() throws Exception {
        int databaseSizeBeforeUpdate = excelImportRepository.findAll().size();
        excelImport.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restExcelImportMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, excelImport.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(excelImport))
            )
            .andExpect(status().isBadRequest());

        // Validate the ExcelImport in the database
        List<ExcelImport> excelImportList = excelImportRepository.findAll();
        assertThat(excelImportList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchExcelImport() throws Exception {
        int databaseSizeBeforeUpdate = excelImportRepository.findAll().size();
        excelImport.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restExcelImportMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(excelImport))
            )
            .andExpect(status().isBadRequest());

        // Validate the ExcelImport in the database
        List<ExcelImport> excelImportList = excelImportRepository.findAll();
        assertThat(excelImportList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamExcelImport() throws Exception {
        int databaseSizeBeforeUpdate = excelImportRepository.findAll().size();
        excelImport.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restExcelImportMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(excelImport))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ExcelImport in the database
        List<ExcelImport> excelImportList = excelImportRepository.findAll();
        assertThat(excelImportList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteExcelImport() throws Exception {
        // Initialize the database
        excelImportRepository.saveAndFlush(excelImport);

        int databaseSizeBeforeDelete = excelImportRepository.findAll().size();

        // Delete the excelImport
        restExcelImportMockMvc
            .perform(delete(ENTITY_API_URL_ID, excelImport.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<ExcelImport> excelImportList = excelImportRepository.findAll();
        assertThat(excelImportList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
