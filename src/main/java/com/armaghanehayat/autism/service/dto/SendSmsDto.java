package com.armaghanehayat.autism.service.dto;

public class SendSmsDto {

    private String username;
    private String password;
    private String to;
    private String from;
    private String text;

    public SendSmsDto(String username, String password, String from, String to, String text) {
        this.username = username;
        this.password = password;
        this.from = from;
        this.to = to;
        this.text = text;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getFrom() {
        return from;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
