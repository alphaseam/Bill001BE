package com.hotelapi.service;

import com.hotelapi.dto.WhatsAppMessageRequest;

public interface WhatsAppService {
    boolean sendMessage(WhatsAppMessageRequest request);
}
