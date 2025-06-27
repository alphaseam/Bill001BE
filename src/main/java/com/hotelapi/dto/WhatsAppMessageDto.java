package com.hotelapi.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.Builder;

@Schema(description = "DTO for WhatsApp message request")
@Data
@Builder
public class WhatsAppMessageDto {

    @Schema(description = "Customer's name", example = "Ravi Sharma")
    private String customerName;

    @Schema(description = "Customer's WhatsApp number with country code", example = "+919876543210")
    private String customerPhone;

    @Schema(description = "Bill ID associated with the message", example = "101")
    private Long billId;

    @Schema(description = "URL to the bill PDF (optional)", example = "https://yourdomain.com/bill/101.pdf")
    private String billPdfUrl;

    @Schema(description = "Text message content to be sent", example = "Thank you for dining with us. Your bill total is ₹850.")
    private String messageContent;
}
