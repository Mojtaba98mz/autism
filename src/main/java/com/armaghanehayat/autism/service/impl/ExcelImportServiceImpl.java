package com.armaghanehayat.autism.service.impl;

import com.armaghanehayat.autism.domain.ExcelImport;
import com.armaghanehayat.autism.repository.ExcelImportRepository;
import com.armaghanehayat.autism.service.ExcelImportService;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link ExcelImport}.
 */
@Service
@Transactional
public class ExcelImportServiceImpl implements ExcelImportService {

    private final Logger log = LoggerFactory.getLogger(ExcelImportServiceImpl.class);

    private final ExcelImportRepository excelImportRepository;

    public ExcelImportServiceImpl(ExcelImportRepository excelImportRepository) {
        this.excelImportRepository = excelImportRepository;
    }

    @Override
    public ExcelImport save(ExcelImport excelImport) {
        log.debug("Request to save ExcelImport : {}", excelImport);
        return excelImportRepository.save(excelImport);
    }

    @Override
    public Optional<ExcelImport> partialUpdate(ExcelImport excelImport) {
        log.debug("Request to partially update ExcelImport : {}", excelImport);

        return excelImportRepository
            .findById(excelImport.getId())
            .map(
                existingExcelImport -> {
                    if (excelImport.getExcel() != null) {
                        existingExcelImport.setExcel(excelImport.getExcel());
                    }
                    if (excelImport.getExcelContentType() != null) {
                        existingExcelImport.setExcelContentType(excelImport.getExcelContentType());
                    }

                    return existingExcelImport;
                }
            )
            .map(excelImportRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ExcelImport> findAll() {
        log.debug("Request to get all ExcelImports");
        return excelImportRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ExcelImport> findOne(Long id) {
        log.debug("Request to get ExcelImport : {}", id);
        return excelImportRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete ExcelImport : {}", id);
        excelImportRepository.deleteById(id);
    }
}
