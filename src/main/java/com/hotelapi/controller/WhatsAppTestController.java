// For testing purpose
package com.hotelapi.controller;

import com.hotelapi.dto.WhatsAppMessageRequest;
import com.hotelapi.service.WhatsAppService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/whatsapp")
@RequiredArgsConstructor
@Tag(name = "WhatsApp Test Controller", description = "For manually testing WhatsApp message sending")
public class WhatsAppTestController {

    private final WhatsAppService whatsAppService;

    @Operation(summary = "Test WhatsApp message send")
    @PostMapping("/send")
    public ResponseEntity<String> sendWhatsAppMessage(@RequestBody WhatsAppMessageRequest messageRequest) {
        boolean result = whatsAppService.sendMessage(messageRequest);
        return result ?
                ResponseEntity.ok("WhatsApp message sent successfully") :
                ResponseEntity.status(500).body("Failed to send WhatsApp message");
    }
}
