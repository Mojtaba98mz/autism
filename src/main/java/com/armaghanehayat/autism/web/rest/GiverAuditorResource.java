package com.armaghanehayat.autism.web.rest;

import com.armaghanehayat.autism.domain.GiverAuditor;
import com.armaghanehayat.autism.repository.GiverAuditorRepository;
import com.armaghanehayat.autism.service.GiverAuditorQueryService;
import com.armaghanehayat.autism.service.GiverAuditorService;
import com.armaghanehayat.autism.service.criteria.GiverAuditorCriteria;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.armaghanehayat.autism.domain.GiverAuditor}.
 */
@RestController
@RequestMapping("/api")
public class GiverAuditorResource {

    private final Logger log = LoggerFactory.getLogger(GiverAuditorResource.class);

    private static final String ENTITY_NAME = "giverAuditor";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final GiverAuditorService giverAuditorService;

    private final GiverAuditorRepository giverAuditorRepository;

    private final GiverAuditorQueryService giverAuditorQueryService;

    public GiverAuditorResource(
        GiverAuditorService giverAuditorService,
        GiverAuditorRepository giverAuditorRepository,
        GiverAuditorQueryService giverAuditorQueryService
    ) {
        this.giverAuditorService = giverAuditorService;
        this.giverAuditorRepository = giverAuditorRepository;
        this.giverAuditorQueryService = giverAuditorQueryService;
    }

    /**
     * {@code POST  /giver-auditors} : Create a new giverAuditor.
     *
     * @param giverAuditor the giverAuditor to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new giverAuditor, or with status {@code 400 (Bad Request)} if the giverAuditor has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/giver-auditors")
    public ResponseEntity<GiverAuditor> createGiverAuditor(@Valid @RequestBody GiverAuditor giverAuditor) throws URISyntaxException {
        log.debug("REST request to save GiverAuditor : {}", giverAuditor);
        if (giverAuditor.getId() != null) {
            throw new BadRequestAlertException("A new giverAuditor cannot already have an ID", ENTITY_NAME, "idexists");
        }
        GiverAuditor result = giverAuditorService.save(giverAuditor);
        return ResponseEntity
            .created(new URI("/api/giver-auditors/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /giver-auditors/:id} : Updates an existing giverAuditor.
     *
     * @param id the id of the giverAuditor to save.
     * @param giverAuditor the giverAuditor to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated giverAuditor,
     * or with status {@code 400 (Bad Request)} if the giverAuditor is not valid,
     * or with status {@code 500 (Internal Server Error)} if the giverAuditor couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/giver-auditors/{id}")
    public ResponseEntity<GiverAuditor> updateGiverAuditor(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody GiverAuditor giverAuditor
    ) throws URISyntaxException {
        log.debug("REST request to update GiverAuditor : {}, {}", id, giverAuditor);
        if (giverAuditor.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, giverAuditor.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!giverAuditorRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        GiverAuditor result = giverAuditorService.save(giverAuditor);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, giverAuditor.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /giver-auditors/:id} : Partial updates given fields of an existing giverAuditor, field will ignore if it is null
     *
     * @param id the id of the giverAuditor to save.
     * @param giverAuditor the giverAuditor to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated giverAuditor,
     * or with status {@code 400 (Bad Request)} if the giverAuditor is not valid,
     * or with status {@code 404 (Not Found)} if the giverAuditor is not found,
     * or with status {@code 500 (Internal Server Error)} if the giverAuditor couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/giver-auditors/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<GiverAuditor> partialUpdateGiverAuditor(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody GiverAuditor giverAuditor
    ) throws URISyntaxException {
        log.debug("REST request to partial update GiverAuditor partially : {}, {}", id, giverAuditor);
        if (giverAuditor.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, giverAuditor.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!giverAuditorRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<GiverAuditor> result = giverAuditorService.partialUpdate(giverAuditor);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, giverAuditor.getId().toString())
        );
    }

    /**
     * {@code GET  /giver-auditors} : get all the giverAuditors.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of giverAuditors in body.
     */
    @GetMapping("/giver-auditors")
    public ResponseEntity<List<GiverAuditor>> getAllGiverAuditors(GiverAuditorCriteria criteria, Pageable pageable) {
        log.debug("REST request to get GiverAuditors by criteria: {}", criteria);
        Page<GiverAuditor> page = giverAuditorQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /giver-auditors/count} : count all the giverAuditors.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/giver-auditors/count")
    public ResponseEntity<Long> countGiverAuditors(GiverAuditorCriteria criteria) {
        log.debug("REST request to count GiverAuditors by criteria: {}", criteria);
        return ResponseEntity.ok().body(giverAuditorQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /giver-auditors/:id} : get the "id" giverAuditor.
     *
     * @param id the id of the giverAuditor to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the giverAuditor, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/giver-auditors/{id}")
    public ResponseEntity<GiverAuditor> getGiverAuditor(@PathVariable Long id) {
        log.debug("REST request to get GiverAuditor : {}", id);
        Optional<GiverAuditor> giverAuditor = giverAuditorService.findOne(id);
        return ResponseUtil.wrapOrNotFound(giverAuditor);
    }

    /**
     * {@code DELETE  /giver-auditors/:id} : delete the "id" giverAuditor.
     *
     * @param id the id of the giverAuditor to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/giver-auditors/{id}")
    public ResponseEntity<Void> deleteGiverAuditor(@PathVariable Long id) {
        log.debug("REST request to delete GiverAuditor : {}", id);
        giverAuditorService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
