package com.armaghanehayat.autism.web.rest;

import com.armaghanehayat.autism.domain.Ceremony;
import com.armaghanehayat.autism.repository.CeremonyRepository;
import com.armaghanehayat.autism.service.CeremonyQueryService;
import com.armaghanehayat.autism.service.CeremonyService;
import com.armaghanehayat.autism.service.criteria.CeremonyCriteria;
import com.armaghanehayat.autism.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.Instant;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
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
 * REST controller for managing {@link com.armaghanehayat.autism.domain.Ceremony}.
 */
@RestController
@RequestMapping("/api")
public class CeremonyResource {

    private final Logger log = LoggerFactory.getLogger(CeremonyResource.class);

    private static final String ENTITY_NAME = "ceremony";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CeremonyService ceremonyService;

    private final CeremonyRepository ceremonyRepository;

    private final CeremonyQueryService ceremonyQueryService;

    public CeremonyResource(
        CeremonyService ceremonyService,
        CeremonyRepository ceremonyRepository,
        CeremonyQueryService ceremonyQueryService
    ) {
        this.ceremonyService = ceremonyService;
        this.ceremonyRepository = ceremonyRepository;
        this.ceremonyQueryService = ceremonyQueryService;
    }

    /**
     * {@code POST  /ceremonies} : Create a new ceremony.
     *
     * @param ceremony the ceremony to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new ceremony, or with status {@code 400 (Bad Request)} if the ceremony has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/ceremonies")
    public ResponseEntity<Ceremony> createCeremony(@RequestBody Ceremony ceremony) throws URISyntaxException {
        log.debug("REST request to save Ceremony : {}", ceremony);
        if (ceremony.getId() != null) {
            throw new BadRequestAlertException("A new ceremony cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ceremony.setGivenDate(Instant.now());
        Ceremony result = ceremonyService.save(ceremony);
        return ResponseEntity
            .created(new URI("/api/ceremonies/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /ceremonies/:id} : Updates an existing ceremony.
     *
     * @param id the id of the ceremony to save.
     * @param ceremony the ceremony to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated ceremony,
     * or with status {@code 400 (Bad Request)} if the ceremony is not valid,
     * or with status {@code 500 (Internal Server Error)} if the ceremony couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/ceremonies/{id}")
    public ResponseEntity<Ceremony> updateCeremony(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody Ceremony ceremony
    ) throws URISyntaxException {
        log.debug("REST request to update Ceremony : {}, {}", id, ceremony);
        if (ceremony.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, ceremony.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!ceremonyRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Ceremony result = ceremonyService.save(ceremony);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, ceremony.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /ceremonies/:id} : Partial updates given fields of an existing ceremony, field will ignore if it is null
     *
     * @param id the id of the ceremony to save.
     * @param ceremony the ceremony to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated ceremony,
     * or with status {@code 400 (Bad Request)} if the ceremony is not valid,
     * or with status {@code 404 (Not Found)} if the ceremony is not found,
     * or with status {@code 500 (Internal Server Error)} if the ceremony couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/ceremonies/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<Ceremony> partialUpdateCeremony(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody Ceremony ceremony
    ) throws URISyntaxException {
        log.debug("REST request to partial update Ceremony partially : {}, {}", id, ceremony);
        if (ceremony.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, ceremony.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!ceremonyRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Ceremony> result = ceremonyService.partialUpdate(ceremony);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, ceremony.getId().toString())
        );
    }

    /**
     * {@code GET  /ceremonies} : get all the ceremonies.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of ceremonies in body.
     */
    @GetMapping("/ceremonies")
    public ResponseEntity<List<Ceremony>> getAllCeremonies(CeremonyCriteria criteria, Pageable pageable) {
        log.debug("REST request to get Ceremonies by criteria: {}", criteria);
        Page<Ceremony> page = ceremonyQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /ceremonies/count} : count all the ceremonies.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/ceremonies/count")
    public ResponseEntity<Long> countCeremonies(CeremonyCriteria criteria) {
        log.debug("REST request to count Ceremonies by criteria: {}", criteria);
        return ResponseEntity.ok().body(ceremonyQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /ceremonies/:id} : get the "id" ceremony.
     *
     * @param id the id of the ceremony to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the ceremony, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/ceremonies/{id}")
    public ResponseEntity<Ceremony> getCeremony(@PathVariable Long id) {
        log.debug("REST request to get Ceremony : {}", id);
        Optional<Ceremony> ceremony = ceremonyService.findOne(id);
        return ResponseUtil.wrapOrNotFound(ceremony);
    }

    /**
     * {@code DELETE  /ceremonies/:id} : delete the "id" ceremony.
     *
     * @param id the id of the ceremony to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/ceremonies/{id}")
    public ResponseEntity<Void> deleteCeremony(@PathVariable Long id) {
        log.debug("REST request to delete Ceremony : {}", id);
        ceremonyService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
