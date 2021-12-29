package com.armaghanehayat.autism.web.rest;

import com.armaghanehayat.autism.domain.ExcelImport;
import com.armaghanehayat.autism.pojo.InvalidPhoneNumber;
import com.armaghanehayat.autism.repository.ExcelImportRepository;
import com.armaghanehayat.autism.service.ExcelImportService;
import com.armaghanehayat.autism.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.armaghanehayat.autism.domain.ExcelImport}.
 */
@RestController
@RequestMapping("/api")
public class ExcelImportResource {

    private final Logger log = LoggerFactory.getLogger(ExcelImportResource.class);

    private static final String ENTITY_NAME = "excelImport";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ExcelImportService excelImportService;

    private final ExcelImportRepository excelImportRepository;

    public ExcelImportResource(ExcelImportService excelImportService, ExcelImportRepository excelImportRepository) {
        this.excelImportService = excelImportService;
        this.excelImportRepository = excelImportRepository;
    }

    /**
     * {@code POST  /excel-imports} : Create a new excelImport.
     *
     * @param excelImport the excelImport to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new excelImport, or with status {@code 400 (Bad Request)} if the excelImport has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/excel-imports")
    public ResponseEntity<List<InvalidPhoneNumber>> createExcelImport(@Valid @RequestBody ExcelImport excelImport)
        throws URISyntaxException {
        log.debug("REST request to save ExcelImport : {}", excelImport);
        if (excelImport.getId() != null) {
            throw new BadRequestAlertException("A new excelImport cannot already have an ID", ENTITY_NAME, "idexists");
        }
        List<InvalidPhoneNumber> result = excelImportService.save(excelImport);
        return ResponseEntity.ok().body(result);
    }

    /**
     * {@code PUT  /excel-imports/:id} : Updates an existing excelImport.
     *
     * @param id the id of the excelImport to save.
     * @param excelImport the excelImport to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated excelImport,
     * or with status {@code 400 (Bad Request)} if the excelImport is not valid,
     * or with status {@code 500 (Internal Server Error)} if the excelImport couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/excel-imports/{id}")
    public ResponseEntity<List<InvalidPhoneNumber>> updateExcelImport(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody ExcelImport excelImport
    ) throws URISyntaxException {
        log.debug("REST request to update ExcelImport : {}, {}", id, excelImport);
        if (excelImport.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, excelImport.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!excelImportRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        List<InvalidPhoneNumber> result = excelImportService.save(excelImport);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, excelImport.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /excel-imports/:id} : Partial updates given fields of an existing excelImport, field will ignore if it is null
     *
     * @param id the id of the excelImport to save.
     * @param excelImport the excelImport to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated excelImport,
     * or with status {@code 400 (Bad Request)} if the excelImport is not valid,
     * or with status {@code 404 (Not Found)} if the excelImport is not found,
     * or with status {@code 500 (Internal Server Error)} if the excelImport couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/excel-imports/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<ExcelImport> partialUpdateExcelImport(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody ExcelImport excelImport
    ) throws URISyntaxException {
        log.debug("REST request to partial update ExcelImport partially : {}, {}", id, excelImport);
        if (excelImport.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, excelImport.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!excelImportRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ExcelImport> result = excelImportService.partialUpdate(excelImport);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, excelImport.getId().toString())
        );
    }

    /**
     * {@code GET  /excel-imports} : get all the excelImports.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of excelImports in body.
     */
    @GetMapping("/excel-imports")
    public List<ExcelImport> getAllExcelImports() {
        log.debug("REST request to get all ExcelImports");
        return excelImportService.findAll();
    }

    /**
     * {@code GET  /excel-imports/:id} : get the "id" excelImport.
     *
     * @param id the id of the excelImport to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the excelImport, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/excel-imports/{id}")
    public ResponseEntity<ExcelImport> getExcelImport(@PathVariable Long id) {
        log.debug("REST request to get ExcelImport : {}", id);
        Optional<ExcelImport> excelImport = excelImportService.findOne(id);
        return ResponseUtil.wrapOrNotFound(excelImport);
    }

    /**
     * {@code DELETE  /excel-imports/:id} : delete the "id" excelImport.
     *
     * @param id the id of the excelImport to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/excel-imports/{id}")
    public ResponseEntity<Void> deleteExcelImport(@PathVariable Long id) {
        log.debug("REST request to delete ExcelImport : {}", id);
        excelImportService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
