package com.hotelapi.config;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Schema(description = "Configuration properties for WhatsApp integration")
@Configuration
@Getter
public class WhatsAppConfig {

    @Schema(description = "WhatsApp API base URL", example = "https://api.whatsapp.com/v1/messages")
    @Value("${whatsapp.api.base-url}")
    private String baseUrl;

    @Schema(description = "Authorization token for WhatsApp API")
    @Value("${whatsapp.api.token}")
    private String authToken;

    @Schema(description = "Sender ID or phone number used to send messages")
    @Value("${whatsapp.api.sender-id}")
    private String senderId;
}
