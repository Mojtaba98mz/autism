package com.armaghanehayat.autism.service;

import com.armaghanehayat.autism.domain.Giver;
import com.armaghanehayat.autism.domain.User;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link Giver}.
 */
public interface GiverService {
    /**
     * Save a giver.
     *
     * @param giver the entity to save.
     * @return the persisted entity.
     */
    Giver save(Giver giver, Boolean isNew);

    /**
     * Partially updates a giver.
     *
     * @param giver the entity to update partially.
     * @return the persisted entity.
     */
    Optional<Giver> partialUpdate(Giver giver);

    /**
     * Get all the givers.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<Giver> findAll(Pageable pageable);

    /**
     * Get the "id" giver.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Giver> findOne(Long id);

    /**
     * Delete the "id" giver.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    boolean disableEnable(Long id);

    /**
     * Get all the givers supporter.
     *
     * @return the list of entities.
     */
    List<User> findAllGiversSupporters();

    Optional<Giver> findByPhoneNumber(String phoneNumber);

    List<Giver> filterByGiverName(String filter);
}
