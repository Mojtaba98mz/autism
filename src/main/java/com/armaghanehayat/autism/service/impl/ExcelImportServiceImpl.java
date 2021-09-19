package com.armaghanehayat.autism.service.impl;

import com.armaghanehayat.autism.domain.ExcelImport;
import com.armaghanehayat.autism.repository.ExcelImportRepository;
import com.armaghanehayat.autism.service.ExcelImportService;
import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Optional;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
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
        InputStream fis = new ByteArrayInputStream(excelImport.getExcel());
        //creating workbook instance that refers to .xls file
        XSSFWorkbook wb = null;
        try {
            wb = new XSSFWorkbook(fis);
        } catch (IOException e) {
            e.printStackTrace();
        }
        //creating a Sheet object to retrieve the object
        XSSFSheet sheet = wb.getSheetAt(0);
        //evaluating cell type
        FormulaEvaluator formulaEvaluator = wb.getCreationHelper().createFormulaEvaluator();
        for (Row row : sheet) { //iteration over row using for each loop
            for (Cell cell : row) { //iteration over cell using for each loop
                /* switch (formulaEvaluator.evaluateInCell(cell).getCellType()) {
                    case Cell.CELL_TYPE_NUMERIC:   //field that represents numeric cell type
//getting the value of the cell as a number
                        System.out.print(cell.getNumericCellValue() + "\t\t");
                        break;
                    case Cell.CELL_TYPE_STRING:    //field that represents string cell type
//getting the value of the cell as a string
                        System.out.print(cell.getStringCellValue() + "\t\t");
                        break;
                }*/
                System.out.println();
            }
            System.out.println();
        }
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
