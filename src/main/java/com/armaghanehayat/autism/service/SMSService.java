package com.armaghanehayat.autism.service;

import com.armaghanehayat.autism.config.ApplicationProperties;
import com.armaghanehayat.autism.domain.Giver;
import com.armaghanehayat.autism.domain.User;
import com.armaghanehayat.autism.service.dto.BulkSendSmsDto;
import com.armaghanehayat.autism.service.dto.SendSmsDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Locale;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSource;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;
import tech.jhipster.config.JHipsterProperties;

/**
 * Service for sending emails.
 * <p>
 * We use the {@link Async} annotation to send emails asynchronously.
 */
@Service
public class SMSService {

    private final Logger log = LoggerFactory.getLogger(SMSService.class);

    private static final String SEND_SMS_END_POINT = "SendSMS";
    private static final String SEND_BULK_SMS_END_POINT = "SendSimpleSMS";

    private final ApplicationProperties.SMS smsProperties;

    private static final ObjectMapper mapper = new ObjectMapper();

    public SMSService(ApplicationProperties applicationProperties) {
        this.smsProperties = applicationProperties.getSms();
    }

    @Async
    public void sendSmsToGiver(String phoneNumber, String content) {
        if (smsProperties.isEnable()) {
            RestTemplate restTemplate = new RestTemplate();
            SendSmsDto SendSmsDto = new SendSmsDto(
                smsProperties.getUsername(),
                smsProperties.getPassword(),
                smsProperties.getFrom(),
                phoneNumber,
                content
            );
            restTemplate.postForEntity(smsProperties.getSendSmsUrl() + SEND_SMS_END_POINT, SendSmsDto, String.class);
        }
    }
}
