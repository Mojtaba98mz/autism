package com.armaghanehayat.autism.service;

import com.armaghanehayat.autism.domain.GiverAuditor;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link GiverAuditor}.
 */
public interface GiverAuditorService {
    /**
     * Save a giverAuditor.
     *
     * @param giverAuditor the entity to save.
     * @return the persisted entity.
     */
    GiverAuditor save(GiverAuditor giverAuditor);

    /**
     * Partially updates a giverAuditor.
     *
     * @param giverAuditor the entity to update partially.
     * @return the persisted entity.
     */
    Optional<GiverAuditor> partialUpdate(GiverAuditor giverAuditor);

    /**
     * Get all the giverAuditors.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<GiverAuditor> findAll(Pageable pageable);

    /**
     * Get the "id" giverAuditor.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<GiverAuditor> findOne(Long id);

    /**
     * Delete the "id" giverAuditor.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
