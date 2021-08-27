package com.armaghanehayat.autism.service;

import com.armaghanehayat.autism.domain.Province;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link Province}.
 */
public interface ProvinceService {
    /**
     * Save a province.
     *
     * @param province the entity to save.
     * @return the persisted entity.
     */
    Province save(Province province);

    /**
     * Partially updates a province.
     *
     * @param province the entity to update partially.
     * @return the persisted entity.
     */
    Optional<Province> partialUpdate(Province province);

    /**
     * Get all the provinces.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<Province> findAll(Pageable pageable);
    /**
     * Get all the Province where Giver is {@code null}.
     *
     * @return the {@link List} of entities.
     */
    //    List<Province> findAllWhereGiverIsNull();

    /**
     * Get the "id" province.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Province> findOne(Long id);

    /**
     * Delete the "id" province.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
