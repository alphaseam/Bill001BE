package com.hotelapi.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import lombok.Builder;

@Schema(description = "DTO for WhatsApp message request")
@Data
@Builder
public class WhatsAppMessageRequest {

    @Schema(description = "Customer's name", example = "Ravi Sharma")
    @NotBlank(message = "Customer name is required")
    private String customerName;

    @Schema(description = "Customer's 10-digit WhatsApp number (without country code)", example = "9876543210")
    @NotBlank(message = "Customer phone number is required")
    @Pattern(regexp = "^[0-9]{10}$", message = "Phone number must be 10 digits")
    private String customerPhone;

    @Schema(description = "Bill ID associated with the message", example = "101")
    private Long billId;

    @Schema(description = "URL to the bill PDF (optional)", example = "https://yourdomain.com/bill/101.pdf")
    private String billPdfUrl;

    @Schema(description = "Text message content to be sent", example = "Thank you for dining with us. Your bill total is ₹850.")
    private String messageContent;
}
