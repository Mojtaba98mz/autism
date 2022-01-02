package com.armaghanehayat.autism.service;

import com.armaghanehayat.autism.domain.Ceremony;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link Ceremony}.
 */
public interface CeremonyService {
    /**
     * Save a ceremony.
     *
     * @param ceremony the entity to save.
     * @return the persisted entity.
     */
    Ceremony save(Ceremony ceremony);

    /**
     * Partially updates a ceremony.
     *
     * @param ceremony the entity to update partially.
     * @return the persisted entity.
     */
    Optional<Ceremony> partialUpdate(Ceremony ceremony);

    /**
     * Get all the ceremonies.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<Ceremony> findAll(Pageable pageable);

    /**
     * Get the "id" ceremony.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Ceremony> findOne(Long id);

    /**
     * Delete the "id" ceremony.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
