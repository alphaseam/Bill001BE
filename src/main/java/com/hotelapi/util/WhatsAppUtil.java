package com.hotelapi.util;

import com.hotelapi.dto.WhatsAppMessageRequest;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.experimental.UtilityClass;

@Schema(description = "Utility class for WhatsApp message formatting and link generation")
@UtilityClass
public class WhatsAppUtil {

    @Schema(description = "Builds the message to be sent over WhatsApp")
    public static String buildMessage(WhatsAppMessageRequest request) {
        StringBuilder sb = new StringBuilder();

        sb.append("Hello ").append(request.getCustomerName()).append(",\n\n")
          .append(" Your bill is ready (ID: ").append(request.getBillId()).append(")\n");

        if (request.getMessageContent() != null && !request.getMessageContent().isEmpty()) {
            sb.append(request.getMessageContent()).append("\n");
        }

        if (request.getBillPdfUrl() != null && !request.getBillPdfUrl().isEmpty()) {
            sb.append("\n View your bill PDF: ").append(request.getBillPdfUrl());
        }

        sb.append("\n\nThank you for choosing our service!\n Hotel Management");

        return sb.toString();
    }

    @Schema(description = "for shortlink")
    public static String shortenUrl(String url) {
        // Placeholder for URL shortener logic
        return url;
    }
}
