package com.armaghanehayat.autism.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Properties specific to Autism.
 * <p>
 * Properties are configured in the {@code application.yml} file.
 * See {@link tech.jhipster.config.JHipsterProperties} for a good example.
 */
@ConfigurationProperties(prefix = "application", ignoreUnknownFields = false)
public class ApplicationProperties {

    private final ApplicationProperties.SMS sms = new ApplicationProperties.SMS();

    public ApplicationProperties.SMS getSms() {
        return this.sms;
    }

    public static class SMS {

        private boolean enable = false;
        private String username = "";
        private String password = "";
        private String sendSmsUrl = "";
        private String from = "";

        public SMS() {}

        public boolean isEnable() {
            return enable;
        }

        public void setEnable(boolean enable) {
            this.enable = enable;
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

        public String getSendSmsUrl() {
            return sendSmsUrl;
        }

        public void setSendSmsUrl(String sendSmsUrl) {
            this.sendSmsUrl = sendSmsUrl;
        }

        public String getFrom() {
            return from;
        }

        public void setFrom(String from) {
            this.from = from;
        }
    }
}
