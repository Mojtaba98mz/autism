package com.armaghanehayat.autism.domain.enumeration;

/**
 * The HelpType enumeration.
 */
public enum HelpType {
    BERENJ("برنج"),
    ROGHAN("روغن"),
    LEBAS("لباس"),
    DARMAN("درمان"),
    KHEIRATENAN("خیرات نان"),
    KAFARE("کفاره"),
    FETRIE("فطریه"),
    GOOSHTEGHORBANI("گوشت قربانی"),
    SAYER("سایر");

    private final String value;

    HelpType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
