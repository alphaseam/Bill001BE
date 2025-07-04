package com.hotelapi.service.impl;

import com.hotelapi.config.TwilioConfig;
import com.hotelapi.dto.WhatsAppMessageRequest;
import com.hotelapi.entity.WhatsAppLog;
import com.hotelapi.repository.WhatsAppLogRepository;
import com.hotelapi.service.WhatsAppService;
import com.hotelapi.util.WhatsAppUtil;
import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

/**
 * Service implementation for sending WhatsApp messages using Twilio.
 */
@Slf4j
@Service
public class WhatsAppServiceImpl implements WhatsAppService {

    private final TwilioConfig config;
    private final WhatsAppLogRepository logRepository;

    public WhatsAppServiceImpl(
            @Qualifier("twilioConfig") TwilioConfig config,
            WhatsAppLogRepository logRepository
    ) {
        this.config = config;
        this.logRepository = logRepository;
    }

    @PostConstruct
    public void init() {
        Twilio.init(config.getAccountSid(), config.getAuthToken());
    }

    @Override
    public boolean sendMessage(WhatsAppMessageRequest request) {
        if (logRepository.existsByBillIdAndStatus(request.getBillId(), "SENT")) {
            log.warn("WhatsApp message already sent for bill ID: {}", request.getBillId());
            return false;
        }

        String formattedPhone = formatIndianMobileNumber(request.getCustomerPhone());
        String messageBody = WhatsAppUtil.buildMessage(request);
        boolean success = false;
        String error = null;

        try {
            Message message = Message.creator(
                    new PhoneNumber("whatsapp:" + formattedPhone),
                    new PhoneNumber(config.getWhatsappFrom()),
                    messageBody
            ).create();

            log.info("Twilio WhatsApp Message sent: SID = {}", message.getSid());
            success = message.getStatus() != Message.Status.FAILED;

        } catch (Exception e) {
            error = e.getMessage();
            log.error("WhatsApp message sending failed: {}", error);
        }

        logRepository.save(
                WhatsAppLog.builder()
                        .billId(request.getBillId())
                        .customerName(request.getCustomerName())
                        .customerPhone(formattedPhone)
                        .message(messageBody)
                        .status(success ? "SENT" : "FAILED")
                        .error(error)
                        .timestamp(LocalDateTime.now())
                        .build()
        );

        return success;
    }

    /**
     * Formats the mobile number to ensure it has +91 country code by default.
     */
    private String formatIndianMobileNumber(String mobileNumber) {
        if (mobileNumber == null || mobileNumber.trim().isEmpty()) return "";

        mobileNumber = mobileNumber.trim().replaceAll("\\s+", "");

        if (mobileNumber.startsWith("+91")) {
            return mobileNumber;
        } else if (mobileNumber.startsWith("+")) {
            return mobileNumber; // Other country code
        } else if (mobileNumber.startsWith("0")) {
            mobileNumber = mobileNumber.substring(1);
        }
        return "+91" + mobileNumber;
    }
}
