package com.armaghanehayat.autism.pojo;

public class InvalidPhoneNumber {

    private String name;
    private String family;
    private String phoneNumber;
    private String reason;

    public InvalidPhoneNumber() {}

    public InvalidPhoneNumber(String name, String family, String phoneNumber, String reason) {
        this.name = name;
        this.family = family;
        this.phoneNumber = phoneNumber;
        this.reason = reason;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFamily() {
        return family;
    }

    public void setFamily(String family) {
        this.family = family;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }
}
