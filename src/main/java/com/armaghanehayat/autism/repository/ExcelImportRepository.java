package com.armaghanehayat.autism.repository;

import com.armaghanehayat.autism.domain.ExcelImport;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the ExcelImport entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ExcelImportRepository extends JpaRepository<ExcelImport, Long> {}
