package com.armaghanehayat.autism.web.rest;

import com.armaghanehayat.autism.domain.Giver;
import com.armaghanehayat.autism.domain.User;
import com.armaghanehayat.autism.repository.GiverRepository;
import com.armaghanehayat.autism.service.GiverQueryService;
import com.armaghanehayat.autism.service.GiverService;
import com.armaghanehayat.autism.service.UserService;
import com.armaghanehayat.autism.service.criteria.GiverCriteria;
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
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.armaghanehayat.autism.domain.Giver}.
 */
@RestController
@RequestMapping("/api")
public class GiverResource {

    private final Logger log = LoggerFactory.getLogger(GiverResource.class);

    private static final String ENTITY_NAME = "giver";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final GiverService giverService;

    private final GiverRepository giverRepository;

    private final GiverQueryService giverQueryService;

    private final UserService userService;

    public GiverResource(
        GiverService giverService,
        GiverRepository giverRepository,
        GiverQueryService giverQueryService,
        UserService userService
    ) {
        this.giverService = giverService;
        this.giverRepository = giverRepository;
        this.giverQueryService = giverQueryService;
        this.userService = userService;
    }

    /**
     * {@code POST  /givers} : Create a new giver.
     *
     * @param giver the giver to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new giver, or with status {@code 400 (Bad Request)} if the giver has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/givers")
    public ResponseEntity<Giver> createGiver(@Valid @RequestBody Giver giver) throws URISyntaxException {
        log.debug("REST request to save Giver : {}", giver);
        if (giver.getId() != null) {
            throw new BadRequestAlertException("A new giver cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Giver result = giverService.save(giver);
        return ResponseEntity
            .created(new URI("/api/givers/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /givers/:id} : Updates an existing giver.
     *
     * @param id the id of the giver to save.
     * @param giver the giver to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated giver,
     * or with status {@code 400 (Bad Request)} if the giver is not valid,
     * or with status {@code 500 (Internal Server Error)} if the giver couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/givers/{id}")
    public ResponseEntity<Giver> updateGiver(@PathVariable(value = "id", required = false) final Long id, @Valid @RequestBody Giver giver)
        throws URISyntaxException {
        log.debug("REST request to update Giver : {}, {}", id, giver);
        if (giver.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, giver.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!giverRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Giver result = giverService.save(giver);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, giver.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /givers/:id} : Partial updates given fields of an existing giver, field will ignore if it is null
     *
     * @param id the id of the giver to save.
     * @param giver the giver to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated giver,
     * or with status {@code 400 (Bad Request)} if the giver is not valid,
     * or with status {@code 404 (Not Found)} if the giver is not found,
     * or with status {@code 500 (Internal Server Error)} if the giver couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/givers/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<Giver> partialUpdateGiver(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody Giver giver
    ) throws URISyntaxException {
        log.debug("REST request to partial update Giver partially : {}, {}", id, giver);
        if (giver.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, giver.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!giverRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Giver> result = giverService.partialUpdate(giver);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, giver.getId().toString())
        );
    }

    /**
     * {@code GET  /givers} : get all the givers.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of givers in body.
     */
    @GetMapping("/givers")
    public ResponseEntity<List<Giver>> getAllGivers(GiverCriteria criteria, Pageable pageable) {
        log.debug("REST request to get Givers by criteria: {}", criteria);
        Page<Giver> page = giverQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /givers/count} : count all the givers.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/givers/count")
    public ResponseEntity<Long> countGivers(GiverCriteria criteria) {
        log.debug("REST request to count Givers by criteria: {}", criteria);
        return ResponseEntity.ok().body(giverQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /givers/:id} : get the "id" giver.
     *
     * @param id the id of the giver to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the giver, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/givers/{id}")
    public ResponseEntity<Giver> getGiver(@PathVariable Long id) {
        log.debug("REST request to get Giver : {}", id);
        Optional<Giver> giver = giverService.findOne(id);
        return ResponseUtil.wrapOrNotFound(giver);
    }

    /**
     * {@code DELETE  /givers/:id} : delete the "id" giver.
     *
     * @param id the id of the giver to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/givers/{id}")
    public ResponseEntity<Void> deleteGiver(@PathVariable Long id) {
        log.debug("REST request to delete Giver : {}", id);
        giverService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code GET  /givers/supporters} : get all the givers.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of givers in body.
     */
    @GetMapping("/givers/supporters")
    public ResponseEntity<List<User>> getAllGiversSupporters() {
        log.debug("REST request to get GiversSupporters ");
        List<User> allGiversSupporters = giverService.findAllGiversSupporters();
        return ResponseEntity.ok().body(allGiversSupporters);
    }

    @GetMapping("/givers/assign/{supporterId}/{giverId}")
    public ResponseEntity<Void> assign(@PathVariable Long supporterId, @PathVariable Long giverId) {
        log.debug("REST request to assign giver to supporter ");
        Optional<Giver> giverOptional = giverService.findOne(giverId);
        Optional<User> supporterOptional = userService.getById(supporterId);
        if (giverOptional.isPresent() && supporterOptional.isPresent()) {
            Giver giver = giverOptional.get();
            User user = supporterOptional.get();
            giver.setSupporter(user);
            giverRepository.save(giver);
        }
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, supporterId.toString()))
            .build();
    }

    @GetMapping("/givers/unAssign/{supporterId}/{giverId}")
    public ResponseEntity<Void> unAssign(@PathVariable Long supporterId, @PathVariable Long giverId) {
        log.debug("REST request to unAssign giver to supporter ");
        Optional<Giver> giverOptional = giverService.findOne(giverId);
        Optional<User> supporterOptional = userService.getById(1l);
        if (giverOptional.isPresent() && supporterOptional.isPresent()) {
            Giver giver = giverOptional.get();
            User user = supporterOptional.get();
            giver.setSupporter(user);
            giverRepository.save(giver);
        }
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, supporterId.toString()))
            .build();
    }
}
