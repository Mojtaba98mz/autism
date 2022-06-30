package com.armaghanehayat.autism.web.rest.vm;

import com.armaghanehayat.autism.domain.City;
import com.armaghanehayat.autism.domain.Province;
import com.armaghanehayat.autism.domain.enumeration.Account;
import com.armaghanehayat.autism.domain.enumeration.HelpType;
import java.time.Instant;

public class ReportVM {

    private Long id;
    private Boolean isCash;
    private Long amountFrom;
    private Long amountTo;
    private Instant fromDate;
    private Instant toDate;
    private HelpType helpType;
    private Province province;
    private City city;
    private Account account;

    public ReportVM() {}

    public ReportVM(
        Long id,
        Boolean isCash,
        Long amountFrom,
        Long amountTo,
        Instant fromDate,
        Instant toDate,
        HelpType helpType,
        Province province,
        City city,
        Account account
    ) {
        this.id = id;
        this.isCash = isCash;
        this.amountFrom = amountFrom;
        this.amountTo = amountTo;
        this.fromDate = fromDate;
        this.toDate = toDate;
        this.helpType = helpType;
        this.province = province;
        this.city = city;
        this.account = account;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Boolean getIsCash() {
        return isCash;
    }

    public void setIsCash(Boolean isCash) {
        this.isCash = isCash;
    }

    public Long getAmountFrom() {
        return amountFrom;
    }

    public void setAmountFrom(Long amountFrom) {
        this.amountFrom = amountFrom;
    }

    public Long getAmountTo() {
        return amountTo;
    }

    public void setAmountTo(Long amountTo) {
        this.amountTo = amountTo;
    }

    public Instant getFromDate() {
        return fromDate;
    }

    public void setFromDate(Instant fromDate) {
        this.fromDate = fromDate;
    }

    public Instant getToDate() {
        return toDate;
    }

    public void setToDate(Instant toDate) {
        this.toDate = toDate;
    }

    public HelpType getHelpType() {
        return helpType;
    }

    public void setHelpType(HelpType helpType) {
        this.helpType = helpType;
    }

    public Province getProvince() {
        return province;
    }

    public void setProvince(Province province) {
        this.province = province;
    }

    public City getCity() {
        return city;
    }

    public void setCity(City city) {
        this.city = city;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }
}
