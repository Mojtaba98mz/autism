package com.armaghanehayat.autism.web.rest;

import com.armaghanehayat.autism.domain.CeremonyUser;
import com.armaghanehayat.autism.repository.CeremonyUserRepository;
import com.armaghanehayat.autism.service.CeremonyUserQueryService;
import com.armaghanehayat.autism.service.CeremonyUserService;
import com.armaghanehayat.autism.service.criteria.CeremonyUserCriteria;
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
 * REST controller for managing {@link com.armaghanehayat.autism.domain.CeremonyUser}.
 */
@RestController
@RequestMapping("/api")
public class CeremonyUserResource {

    private final Logger log = LoggerFactory.getLogger(CeremonyUserResource.class);

    private static final String ENTITY_NAME = "ceremonyUser";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CeremonyUserService ceremonyUserService;

    private final CeremonyUserRepository ceremonyUserRepository;

    private final CeremonyUserQueryService ceremonyUserQueryService;

    public CeremonyUserResource(
        CeremonyUserService ceremonyUserService,
        CeremonyUserRepository ceremonyUserRepository,
        CeremonyUserQueryService ceremonyUserQueryService
    ) {
        this.ceremonyUserService = ceremonyUserService;
        this.ceremonyUserRepository = ceremonyUserRepository;
        this.ceremonyUserQueryService = ceremonyUserQueryService;
    }

    /**
     * {@code POST  /ceremony-users} : Create a new ceremonyUser.
     *
     * @param ceremonyUser the ceremonyUser to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new ceremonyUser, or with status {@code 400 (Bad Request)} if the ceremonyUser has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/ceremony-users")
    public ResponseEntity<CeremonyUser> createCeremonyUser(@Valid @RequestBody CeremonyUser ceremonyUser) throws URISyntaxException {
        log.debug("REST request to save CeremonyUser : {}", ceremonyUser);
        if (ceremonyUser.getId() != null) {
            throw new BadRequestAlertException("A new ceremonyUser cannot already have an ID", ENTITY_NAME, "idexists");
        }
        CeremonyUser result = ceremonyUserService.save(ceremonyUser);
        return ResponseEntity
            .created(new URI("/api/ceremony-users/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /ceremony-users/:id} : Updates an existing ceremonyUser.
     *
     * @param id the id of the ceremonyUser to save.
     * @param ceremonyUser the ceremonyUser to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated ceremonyUser,
     * or with status {@code 400 (Bad Request)} if the ceremonyUser is not valid,
     * or with status {@code 500 (Internal Server Error)} if the ceremonyUser couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/ceremony-users/{id}")
    public ResponseEntity<CeremonyUser> updateCeremonyUser(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody CeremonyUser ceremonyUser
    ) throws URISyntaxException {
        log.debug("REST request to update CeremonyUser : {}, {}", id, ceremonyUser);
        if (ceremonyUser.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, ceremonyUser.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!ceremonyUserRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        CeremonyUser result = ceremonyUserService.save(ceremonyUser);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, ceremonyUser.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /ceremony-users/:id} : Partial updates given fields of an existing ceremonyUser, field will ignore if it is null
     *
     * @param id the id of the ceremonyUser to save.
     * @param ceremonyUser the ceremonyUser to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated ceremonyUser,
     * or with status {@code 400 (Bad Request)} if the ceremonyUser is not valid,
     * or with status {@code 404 (Not Found)} if the ceremonyUser is not found,
     * or with status {@code 500 (Internal Server Error)} if the ceremonyUser couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/ceremony-users/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<CeremonyUser> partialUpdateCeremonyUser(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody CeremonyUser ceremonyUser
    ) throws URISyntaxException {
        log.debug("REST request to partial update CeremonyUser partially : {}, {}", id, ceremonyUser);
        if (ceremonyUser.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, ceremonyUser.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!ceremonyUserRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<CeremonyUser> result = ceremonyUserService.partialUpdate(ceremonyUser);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, ceremonyUser.getId().toString())
        );
    }

    /**
     * {@code GET  /ceremony-users} : get all the ceremonyUsers.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of ceremonyUsers in body.
     */
    @GetMapping("/ceremony-users")
    public ResponseEntity<List<CeremonyUser>> getAllCeremonyUsers(CeremonyUserCriteria criteria, Pageable pageable) {
        log.debug("REST request to get CeremonyUsers by criteria: {}", criteria);
        Page<CeremonyUser> page = ceremonyUserQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /ceremony-users/count} : count all the ceremonyUsers.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/ceremony-users/count")
    public ResponseEntity<Long> countCeremonyUsers(CeremonyUserCriteria criteria) {
        log.debug("REST request to count CeremonyUsers by criteria: {}", criteria);
        return ResponseEntity.ok().body(ceremonyUserQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /ceremony-users/:id} : get the "id" ceremonyUser.
     *
     * @param id the id of the ceremonyUser to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the ceremonyUser, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/ceremony-users/{id}")
    public ResponseEntity<CeremonyUser> getCeremonyUser(@PathVariable Long id) {
        log.debug("REST request to get CeremonyUser : {}", id);
        Optional<CeremonyUser> ceremonyUser = ceremonyUserService.findOne(id);
        return ResponseUtil.wrapOrNotFound(ceremonyUser);
    }

    /**
     * {@code DELETE  /ceremony-users/:id} : delete the "id" ceremonyUser.
     *
     * @param id the id of the ceremonyUser to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/ceremony-users/{id}")
    public ResponseEntity<Void> deleteCeremonyUser(@PathVariable Long id) {
        log.debug("REST request to delete CeremonyUser : {}", id);
        ceremonyUserService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
