package com.armaghanehayat.autism.service;

import com.armaghanehayat.autism.domain.CeremonyUser;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link CeremonyUser}.
 */
public interface CeremonyUserService {
    /**
     * Save a ceremonyUser.
     *
     * @param ceremonyUser the entity to save.
     * @return the persisted entity.
     */
    CeremonyUser save(CeremonyUser ceremonyUser);

    /**
     * Partially updates a ceremonyUser.
     *
     * @param ceremonyUser the entity to update partially.
     * @return the persisted entity.
     */
    Optional<CeremonyUser> partialUpdate(CeremonyUser ceremonyUser);

    /**
     * Get all the ceremonyUsers.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<CeremonyUser> findAll(Pageable pageable);

    /**
     * Get the "id" ceremonyUser.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<CeremonyUser> findOne(Long id);

    /**
     * Delete the "id" ceremonyUser.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    List<CeremonyUser> filterByName(String filter);
}
