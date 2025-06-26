package com.hotelapi.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Schema(description = "Log of WhatsApp message delivery status for each bill")
@Entity
@Table(name = "whatsapp_logs")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WhatsAppLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "Unique identifier for the WhatsApp log", example = "1")
    private Long id;

    @Schema(description = "Customer's name", example = "Ravi Sharma")
    private String customerName;

    @Schema(description = "Customer's WhatsApp number", example = "+919876543210")
    private String customerPhone;

    @Schema(description = "Bill ID linked to the message", example = "101")
    private Long billId;

    @Schema(description = "Status of the WhatsApp message", example = "SENT")
    private String status;

    @Schema(description = "Message content that was sent", example = "Thank you for your visit. Bill total: ₹850")
    @Column(length = 1000)
    private String message;

    @Schema(description = "Error message if the message failed to send", example = "Invalid phone number format")
    private String error;

    @Schema(description = "Timestamp when the message was sent or failed", example = "2025-06-26T14:45:00")
    private LocalDateTime timestamp;
}
