package com.hotelapi;

import com.hotelapi.config.TwilioConfig;
import com.hotelapi.dto.WhatsAppMessageRequest;
import com.hotelapi.repository.WhatsAppLogRepository;
import com.hotelapi.service.impl.WhatsAppServiceImpl;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.rest.api.v2010.account.MessageCreator;
import com.twilio.type.PhoneNumber;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.MockedStatic;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class) // Enables Mockito annotations
class WhatsAppServiceImplTest {

    @Mock // Mock Twilio configuration
    private TwilioConfig twilioConfig;

    @Mock // Mock WhatsApp log repository
    private WhatsAppLogRepository logRepository;

    @Mock // Mock Twilio message creator
    private MessageCreator messageCreator;

    @Mock // Mock Twilio message object
    private Message mockMessage;

    @InjectMocks // Inject mocks into WhatsAppServiceImpl
    private WhatsAppServiceImpl whatsAppService;

    private WhatsAppMessageRequest request;

    @BeforeEach // Setup test data before each test
    void setUp() {
        request = WhatsAppMessageRequest.builder()
                .customerName("Ravi Sharma")
                .customerPhone("9876543210")
                .billId(101L)
                .messageContent("Your bill total is ₹850.")
                .billPdfUrl("http://localhost:8080/bill/101.pdf")
                .build();

        lenient().when(twilioConfig.getWhatsappFrom()).thenReturn("whatsapp:+14155238886");
    }

    // Test sending WhatsApp message successfully
    @Test
    void testSendMessage_success() {
        when(logRepository.existsByBillIdAndStatus(101L, "SENT")).thenReturn(false);
        when(mockMessage.getStatus()).thenReturn(Message.Status.DELIVERED);

        try (MockedStatic<Message> mockedStatic = mockStatic(Message.class)) {
            mockedStatic.when(() ->
                    Message.creator(any(PhoneNumber.class), any(PhoneNumber.class), anyString())
            ).thenReturn(messageCreator);

            when(messageCreator.create()).thenReturn(mockMessage);

            boolean result = whatsAppService.sendMessage(request);

            assertTrue(result); // Message should be sent successfully
            verify(logRepository).save(argThat(log ->
                    log.getCustomerName().equals("Ravi Sharma") &&
                            log.getStatus().equals("SENT")
            ));
        }
    }

    // Test skipping message if already sent
    @Test
    void testSendMessage_alreadySent_returnsFalse() {
        when(logRepository.existsByBillIdAndStatus(101L, "SENT")).thenReturn(true);

        boolean result = whatsAppService.sendMessage(request);

        assertFalse(result); // No duplicate message should be sent
        verify(logRepository, never()).save(any()); // No log saved
    }

    // Test when Twilio returns FAILED status
    @Test
    void testSendMessage_twilioFails_logsFailure() {
        when(logRepository.existsByBillIdAndStatus(101L, "SENT")).thenReturn(false);
        when(mockMessage.getStatus()).thenReturn(Message.Status.FAILED);

        try (MockedStatic<Message> mockedStatic = mockStatic(Message.class)) {
            mockedStatic.when(() ->
                    Message.creator(any(PhoneNumber.class), any(PhoneNumber.class), anyString())
            ).thenReturn(messageCreator);

            when(messageCreator.create()).thenReturn(mockMessage);

            boolean result = whatsAppService.sendMessage(request);

            assertFalse(result); // Should return false on failure
            verify(logRepository).save(argThat(log -> log.getStatus().equals("FAILED")));
        }
    }

    // Test when Twilio throws an exception
    @Test
    void testSendMessage_twilioThrowsException_logsFailure() {
        when(logRepository.existsByBillIdAndStatus(101L, "SENT")).thenReturn(false);

        try (MockedStatic<Message> mockedStatic = mockStatic(Message.class)) {
            mockedStatic.when(() ->
                    Message.creator(any(PhoneNumber.class), any(PhoneNumber.class), anyString())
            ).thenReturn(messageCreator);

            when(messageCreator.create()).thenThrow(new RuntimeException("Twilio error"));

            boolean result = whatsAppService.sendMessage(request);

            assertFalse(result); // Should handle exception gracefully
            verify(logRepository).save(argThat(log ->
                    log.getStatus().equals("FAILED") &&
                            log.getError().contains("Twilio error")
            ));
        }
    }

    // Test formatting plain Indian number
    @Test
    void testFormatIndianNumber_withPlainNumber() {
        String result = invokeFormat("9876543210");
        assertEquals("+919876543210", result);
    }

    // Test formatting number already with +91
    @Test
    void testFormatIndianNumber_with91Prefix() {
        String result = invokeFormat("+919876543210");
        assertEquals("+919876543210", result);
    }

    // Test formatting number with leading 0
    @Test
    void testFormatIndianNumber_with0Prefix() {
        String result = invokeFormat("09876543210");
        assertEquals("+919876543210", result);
    }

    // Test formatting number with other country code
    @Test
    void testFormatIndianNumber_withOtherCountryCode() {
        String result = invokeFormat("+441234567890");
        assertEquals("+441234567890", result);
    }

    // Test formatting number with spaces
    @Test
    void testFormatIndianNumber_withSpaces() {
        String result = invokeFormat(" 98765 43210 ");
        assertEquals("+919876543210", result);
    }

    // Test formatting when input is empty
    @Test
    void testFormatIndianNumber_emptyInput() {
        String result = invokeFormat("");
        assertEquals("", result);
    }

    // Test formatting when input is null
    @Test
    void testFormatIndianNumber_nullInput() {
        String result = invokeFormat(null);
        assertEquals("", result);
    }

    // Utility method to call private formatIndianMobileNumber method
    private String invokeFormat(String number) {
        try {
            var method = WhatsAppServiceImpl.class.getDeclaredMethod("formatIndianMobileNumber", String.class);
            method.setAccessible(true);
            return (String) method.invoke(whatsAppService, number);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
