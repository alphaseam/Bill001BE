package com.hotelapi.util;

import com.hotelapi.dto.WhatsAppMessageDto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.experimental.UtilityClass;

@Schema(description = "Utility class for WhatsApp message formatting and link generation")
@UtilityClass
public class WhatsAppUtil {

    @Schema(description = "Builds the message to be sent over WhatsApp")
    public static String buildMessage(WhatsAppMessageDto dto) {
        StringBuilder sb = new StringBuilder();

        sb.append("Hello ").append(dto.getCustomerName()).append(",\n\n")
          .append(" Your bill is ready (ID: ").append(dto.getBillId()).append(")\n");

        if (dto.getMessageContent() != null && !dto.getMessageContent().isEmpty()) {
            sb.append(dto.getMessageContent()).append("\n");
        }

        if (dto.getBillPdfUrl() != null && !dto.getBillPdfUrl().isEmpty()) {
            sb.append("\n View your bill PDF: ").append(dto.getBillPdfUrl());
        }

        sb.append("\n\nThank you for choosing our service!\n Hotel Management");

        return sb.toString();
    }

    @Schema(description = "for shortlink")
    public static String shortenUrl(String url) {
        //need to check for shot links
        return url;
    }
}
