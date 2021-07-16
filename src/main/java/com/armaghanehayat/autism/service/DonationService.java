package com.armaghanehayat.autism.service;

import com.armaghanehayat.autism.domain.Donation;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link Donation}.
 */
public interface DonationService {
    /**
     * Save a donation.
     *
     * @param donation the entity to save.
     * @return the persisted entity.
     */
    Donation save(Donation donation);

    /**
     * Partially updates a donation.
     *
     * @param donation the entity to update partially.
     * @return the persisted entity.
     */
    Optional<Donation> partialUpdate(Donation donation);

    /**
     * Get all the donations.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<Donation> findAll(Pageable pageable);

    /**
     * Get the "id" donation.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Donation> findOne(Long id);

    /**
     * Delete the "id" donation.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
