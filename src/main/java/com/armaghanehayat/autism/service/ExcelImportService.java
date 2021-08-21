package com.armaghanehayat.autism.service;

import com.armaghanehayat.autism.domain.ExcelImport;
import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link ExcelImport}.
 */
public interface ExcelImportService {
    /**
     * Save a excelImport.
     *
     * @param excelImport the entity to save.
     * @return the persisted entity.
     */
    ExcelImport save(ExcelImport excelImport);

    /**
     * Partially updates a excelImport.
     *
     * @param excelImport the entity to update partially.
     * @return the persisted entity.
     */
    Optional<ExcelImport> partialUpdate(ExcelImport excelImport);

    /**
     * Get all the excelImports.
     *
     * @return the list of entities.
     */
    List<ExcelImport> findAll();

    /**
     * Get the "id" excelImport.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<ExcelImport> findOne(Long id);

    /**
     * Delete the "id" excelImport.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
