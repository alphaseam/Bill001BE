package com.hotelapi.service;

import com.hotelapi.config.TwilioConfig;
import com.hotelapi.dto.WhatsAppMessageRequest;
import com.hotelapi.entity.WhatsAppLog;
import com.hotelapi.repository.WhatsAppLogRepository;
import com.hotelapi.util.WhatsAppUtil;
import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Slf4j
@Service
public class WhatsAppService {

    private final TwilioConfig config;
    private final WhatsAppLogRepository logRepository;

    public WhatsAppService(@Qualifier("twilioConfig") TwilioConfig config, WhatsAppLogRepository logRepository) {
        this.config = config;
        this.logRepository = logRepository;
    }

    @PostConstruct
    public void init() {
        Twilio.init(config.getAccountSid(), config.getAuthToken());
    }

    public boolean sendMessage(WhatsAppMessageRequest request) {
        if (logRepository.existsByBillIdAndStatus(request.getBillId(), "SENT")) {
            log.warn("Message already sent for bill ID {}", request.getBillId());
            return false;
        }

        String messageBody = WhatsAppUtil.buildMessage(request);

        boolean isSuccess = false;
        String errorMessage = null;

        try {
            Message message = Message.creator(
                    new PhoneNumber("whatsapp:" + request.getCustomerPhone()),
                    new PhoneNumber(config.getWhatsappFrom()),
                    messageBody
            ).create();

            log.info("Twilio Message SID: {}", message.getSid());
            isSuccess = message.getStatus() != Message.Status.FAILED;

        } catch (Exception e) {
            errorMessage = e.getMessage();
            log.error("Twilio message send failed: {}", errorMessage);
        }

        WhatsAppLog logEntry = WhatsAppLog.builder()
                .billId(request.getBillId())
                .customerName(request.getCustomerName())
                .customerPhone(request.getCustomerPhone())
                .message(messageBody)
                .status(isSuccess ? "SENT" : "FAILED")
                .error(errorMessage)
                .timestamp(LocalDateTime.now())
                .build();

        logRepository.save(logEntry);

        return isSuccess;
    }
}
