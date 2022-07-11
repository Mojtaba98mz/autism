package com.armaghanehayat.autism.web.rest.vm;

import com.armaghanehayat.autism.domain.enumeration.Account;
import com.armaghanehayat.autism.domain.enumeration.HelpType;
import java.time.Instant;

public class ReportMonthListVM {

    private String username;

    private Long amount;
    private Instant donationDate;

    public ReportMonthListVM() {}

    public ReportMonthListVM(Long amount, Instant donationDate) {
        this.amount = amount;
        this.donationDate = donationDate;
    }

    public ReportMonthListVM(String username, Long amount, Instant donationDate) {
        this.username = username;
        this.amount = amount;
        this.donationDate = donationDate;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
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
}
