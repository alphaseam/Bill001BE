package com.hotelapi.service;

import com.hotelapi.config.WhatsAppConfig;
import com.hotelapi.dto.WhatsAppMessageDto;
import com.hotelapi.entity.WhatsAppLog;
import com.hotelapi.repository.WhatsAppLogRepository;
import com.hotelapi.util.WhatsAppUtil;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;

@Schema(description = "Service to send WhatsApp messages and log status")
@Service
@RequiredArgsConstructor
@Slf4j
public class WhatsAppService {

    private final WhatsAppConfig config;
    private final WhatsAppLogRepository logRepository;
    private final RestTemplate restTemplate = new RestTemplate(); // Can be replaced with WebClient

    public boolean sendMessage(WhatsAppMessageDto dto) {
        //  Prevent duplicate message for same bill
        if (logRepository.existsByBillIdAndStatus(dto.getBillId(), "SENT")) {
            log.warn("WhatsApp message already sent for billId: {}", dto.getBillId());
            return false;
        }

        String apiUrl = config.getBaseUrl();
        String sender = config.getSenderId();
        String token = config.getAuthToken();

        String formattedMessage = WhatsAppUtil.buildMessage(dto);

        var requestBody = new WhatsAppApiRequest(sender, dto.getCustomerPhone(), formattedMessage);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(token);

        HttpEntity<Object> requestEntity = new HttpEntity<>(requestBody, headers);

        boolean isSuccess = false;
        String errorMessage = null;

        try {
            ResponseEntity<String> response = restTemplate.postForEntity(apiUrl, requestEntity, String.class);
            isSuccess = response.getStatusCode().is2xxSuccessful();
            log.info("WhatsApp message response: {}", response.getBody());
        } catch (Exception ex) {
            errorMessage = ex.getMessage();
            log.error("Failed to send WhatsApp message for billId {}: {}", dto.getBillId(), errorMessage);
        }

        //Log message status
        WhatsAppLog logEntry = WhatsAppLog.builder()
                .billId(dto.getBillId())
                .customerName(dto.getCustomerName())
                .customerPhone(dto.getCustomerPhone())
                .message(formattedMessage)
                .status(isSuccess ? "SENT" : "FAILED")
                .error(errorMessage)
                .timestamp(LocalDateTime.now())
                .build();

        logRepository.save(logEntry);

        return isSuccess;
    }

    // Internal DTO to match WhatsApp API request structure
    @Schema(description = "Internal DTO to structure WhatsApp API request")
    private record WhatsAppApiRequest(
            @Schema(description = "Sender ID") String sender,
            @Schema(description = "Recipient number") String recipient,
            @Schema(description = "Message content") String message
    ) {}
}
