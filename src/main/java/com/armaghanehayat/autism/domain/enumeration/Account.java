package com.armaghanehayat.autism.domain.enumeration;

/**
 * The Account enumeration.
 */
public enum Account {
    SADERAT("صادرات"),
    MELLI("ملی"),
    GHAVAMIN("قوامین");

    private final String value;

    Account(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
