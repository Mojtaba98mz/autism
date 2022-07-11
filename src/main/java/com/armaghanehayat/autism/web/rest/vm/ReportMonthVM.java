package com.armaghanehayat.autism.web.rest.vm;

import com.armaghanehayat.autism.domain.City;
import com.armaghanehayat.autism.domain.Province;
import com.armaghanehayat.autism.domain.enumeration.Account;
import com.armaghanehayat.autism.domain.enumeration.HelpType;
import java.time.Instant;

public class ReportMonthVM {

    private Instant fromDate;
    private Instant toDate;

    public ReportMonthVM() {}

    public ReportMonthVM(Instant fromDate, Instant toDate) {
        this.fromDate = fromDate;
        this.toDate = toDate;
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
}
