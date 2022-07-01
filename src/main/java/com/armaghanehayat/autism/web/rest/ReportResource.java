package com.armaghanehayat.autism.web.rest;

import com.armaghanehayat.autism.domain.City;
import com.armaghanehayat.autism.domain.Donation;
import com.armaghanehayat.autism.domain.Giver;
import com.armaghanehayat.autism.domain.Province;
import com.armaghanehayat.autism.domain.enumeration.Account;
import com.armaghanehayat.autism.domain.enumeration.HelpType;
import com.armaghanehayat.autism.repository.CityRepository;
import com.armaghanehayat.autism.repository.GiverRepository;
import com.armaghanehayat.autism.repository.ProvinceRepository;
import com.armaghanehayat.autism.service.SMSService;
import com.armaghanehayat.autism.web.rest.vm.ReportListVM;
import com.armaghanehayat.autism.web.rest.vm.ReportVM;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * REST controller for managing {@link Donation}.
 */
@RestController
@RequestMapping("/api")
public class ReportResource {

    private final GiverRepository giverRepository;
    private final ProvinceRepository provinceRepository;
    private final CityRepository cityRepository;
    private final SMSService smsService;

    public ReportResource(
        GiverRepository giverRepository,
        ProvinceRepository provinceRepository,
        CityRepository cityRepository,
        SMSService smsService
    ) {
        this.giverRepository = giverRepository;
        this.provinceRepository = provinceRepository;
        this.cityRepository = cityRepository;
        this.smsService = smsService;
    }

    @PostMapping("/reports")
    public ResponseEntity<List<ReportListVM>> getReport(@RequestBody ReportVM reportVM) {
        if (reportVM.getAmountFrom() == null) reportVM.setAmountFrom(0L);
        if (reportVM.getAmountTo() == null) reportVM.setAmountTo(Long.MAX_VALUE);
        if (reportVM.getFromDate() == null) reportVM.setFromDate(ZonedDateTime.now().minusYears(20).toInstant());
        if (reportVM.getToDate() == null) reportVM.setToDate(ZonedDateTime.now().plusYears(20).toInstant());
        ArrayList<HelpType> helpTypes = new ArrayList<>();
        if (reportVM.getHelpType() == null) {
            helpTypes.add(HelpType.BERENJ);
            helpTypes.add(HelpType.ROGHAN);
            helpTypes.add(HelpType.LEBAS);
            helpTypes.add(HelpType.DARMAN);
            helpTypes.add(HelpType.KHEIRATENAN);
            helpTypes.add(HelpType.KAFARE);
            helpTypes.add(HelpType.FETRIE);
            helpTypes.add(HelpType.GOOSHTEGHORBANI);
            helpTypes.add(HelpType.SAYER);
        } else {
            helpTypes.add(reportVM.getHelpType());
        }
        ArrayList<Account> accounts = new ArrayList<>();
        if (reportVM.getAccount() == null) {
            accounts.add(Account.GHAVAMIN);
            accounts.add(Account.MELLI);
            accounts.add(Account.SADERAT);
        } else {
            accounts.add(reportVM.getAccount());
        }
        ArrayList<Long> provinces = new ArrayList<>();
        if (reportVM.getProvince() == null) {
            provinces.addAll(provinceRepository.findAll().stream().map(Province::getId).collect(Collectors.toList()));
        } else {
            provinces.add(reportVM.getProvince().getId());
        }
        ArrayList<Long> cities = new ArrayList<>();
        if (reportVM.getCity() == null) {
            cities.addAll(cityRepository.findAll().stream().map(City::getId).collect(Collectors.toList()));
        } else {
            cities.add(reportVM.getCity().getId());
        }
        return ResponseEntity
            .ok()
            .body(
                giverRepository.getReport(
                    reportVM.getIsCash(),
                    reportVM.getAmountFrom(),
                    reportVM.getAmountTo(),
                    reportVM.getFromDate(),
                    reportVM.getToDate(),
                    helpTypes,
                    provinces,
                    cities,
                    accounts
                )
            );
    }

    @GetMapping("/reports/send/{content}")
    public void getCourseReport(@PathVariable String content) {
        List<String> phoneNumbers = giverRepository.findAllPhoneNumbers();
        for (String phoneNumber : phoneNumbers) {
            smsService.sendSmsToGiver(phoneNumber, content);
        }
    }
}
