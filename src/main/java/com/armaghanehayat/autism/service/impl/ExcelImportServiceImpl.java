package com.armaghanehayat.autism.service.impl;

import com.armaghanehayat.autism.domain.Donation;
import com.armaghanehayat.autism.domain.ExcelImport;
import com.armaghanehayat.autism.domain.Giver;
import com.armaghanehayat.autism.domain.User;
import com.armaghanehayat.autism.repository.ExcelImportRepository;
import com.armaghanehayat.autism.service.ExcelImportService;
import com.armaghanehayat.autism.service.GiverService;
import com.armaghanehayat.autism.service.UserService;
import com.armaghanehayat.autism.util.JalaliDateTime;
import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.Instant;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
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

    private final GiverService giverService;

    private final UserService userService;

    public ExcelImportServiceImpl(ExcelImportRepository excelImportRepository, GiverService giverService, UserService userService) {
        this.excelImportRepository = excelImportRepository;
        this.giverService = giverService;
        this.userService = userService;
    }

    @Override
    @Transactional
    public ExcelImport save(ExcelImport excelImport) {
        log.debug("Request to save ExcelImport : {}", excelImport);
        InputStream fis = new ByteArrayInputStream(excelImport.getExcel());
        HSSFWorkbook wb = null;
        try {
            wb = new HSSFWorkbook(fis);
        } catch (IOException e) {
            e.printStackTrace();
        }
        HSSFSheet sheet = wb.getSheetAt(0);
        for (Row row : sheet) {
            if (row.getRowNum() < 2) continue;
            Giver giver = new Giver();
            Donation donation = new Donation();
            giver.setName(row.getCell(2).getStringCellValue());
            giver.setFamily(row.getCell(3).getStringCellValue());
            giver.setPhoneNumber(row.getCell(4).getStringCellValue());
            if (row.getCell(5) != null) {
                donation.setAmount(Long.valueOf(row.getCell(5).getStringCellValue()));
                donation.setDonationDate(JalaliDateTime.jalaliToGregorianWithoutTime(row.getCell(6).getStringCellValue()));
                Set<Donation> donations = new HashSet<>();
                donations.add(donation);
                giver.setDonations(donations);
            }
            Optional<Giver> byPhoneNumber = giverService.findByPhoneNumber(giver.getPhoneNumber());
            if (byPhoneNumber.isEmpty()) giverService.save(giver, true);
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
