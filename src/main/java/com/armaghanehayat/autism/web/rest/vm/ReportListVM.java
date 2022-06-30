package com.armaghanehayat.autism.web.rest.vm;

import com.armaghanehayat.autism.domain.enumeration.Account;
import com.armaghanehayat.autism.domain.enumeration.HelpType;
import java.time.Instant;

public class ReportListVM {

    private Long giverId;
    private String giverName;
    private String giverFamily;
    private String giverPhoneNumber;
    private Boolean isCash;
    private Long amount;
    private Instant donationDate;
    private HelpType helpType;
    private String province;
    private String city;
    private Account account;

    public ReportListVM() {}

    public ReportListVM(
        Long giverId,
        String giverName,
        String giverFamily,
        String giverPhoneNumber,
        Boolean isCash,
        Long amount,
        Instant donationDate,
        HelpType helpType,
        String province,
        String city,
        Account account
    ) {
        this.giverId = giverId;
        this.giverName = giverName;
        this.giverFamily = giverFamily;
        this.giverPhoneNumber = giverPhoneNumber;
        this.isCash = isCash;
        this.amount = amount;
        this.donationDate = donationDate;
        this.helpType = helpType;
        this.province = province;
        this.city = city;
        this.account = account;
    }

    public Long getGiverId() {
        return giverId;
    }

    public void setGiverId(Long giverId) {
        this.giverId = giverId;
    }

    public String getGiverName() {
        return giverName;
    }

    public void setGiverName(String giverName) {
        this.giverName = giverName;
    }

    public String getGiverFamily() {
        return giverFamily;
    }

    public void setGiverFamily(String giverFamily) {
        this.giverFamily = giverFamily;
    }

    public String getGiverPhoneNumber() {
        return giverPhoneNumber;
    }

    public void setGiverPhoneNumber(String giverPhoneNumber) {
        this.giverPhoneNumber = giverPhoneNumber;
    }

    public Boolean getCash() {
        return isCash;
    }

    public void setCash(Boolean cash) {
        isCash = cash;
    }

    public Long getAmount() {
        return amount;
    }

    public void setAmount(Long amount) {
        this.amount = amount;
    }

    public Instant getDonationDate() {
        return donationDate;
    }

    public void setDonationDate(Instant donationDate) {
        this.donationDate = donationDate;
    }

    public HelpType getHelpType() {
        return helpType;
    }

    public void setHelpType(HelpType helpType) {
        this.helpType = helpType;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }
}
