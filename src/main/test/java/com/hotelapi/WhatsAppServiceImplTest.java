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

@ExtendWith(MockitoExtension.class)
class WhatsAppServiceImplTest {

    @Mock
    private TwilioConfig twilioConfig;

    @Mock
    private WhatsAppLogRepository logRepository;

    @Mock
    private MessageCreator messageCreator;

    @Mock
    private Message mockMessage;

    @InjectMocks
    private WhatsAppServiceImpl whatsAppService;

    private WhatsAppMessageRequest request;

    // Initializes the test data and lenient config
    @BeforeEach
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

    // Test: Successful WhatsApp message sending
    @Test
    void testSendMessage_success() {
        when(logRepository.existsByBillIdAndStatus(101L, "SENT")).thenReturn(false);
        when(mockMessage.getStatus()).thenReturn(Message.Status.DELIVERED); // Simulate success

        try (MockedStatic<Message> mockedStatic = mockStatic(Message.class)) {
            mockedStatic.when(() ->
                    Message.creator(any(PhoneNumber.class), any(PhoneNumber.class), anyString())
            ).thenReturn(messageCreator);

            when(messageCreator.create()).thenReturn(mockMessage);

            boolean result = whatsAppService.sendMessage(request);

            assertTrue(result);
            verify(logRepository).save(argThat(log ->
                    log.getCustomerName().equals("Ravi Sharma") &&
                            log.getStatus().equals("SENT")
            ));
        }
    }

    // Test: Message already sent should return false and not send again
    @Test
    void testSendMessage_alreadySent_returnsFalse() {
        when(logRepository.existsByBillIdAndStatus(101L, "SENT")).thenReturn(true);
        boolean result = whatsAppService.sendMessage(request);
        assertFalse(result);
        verify(logRepository, never()).save(any());
    }

    // Test: Message send fails (Twilio returns FAILED status)
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

            assertFalse(result);
            verify(logRepository).save(argThat(log -> log.getStatus().equals("FAILED")));
        }
    }

    // Test: Exception thrown while sending message should be logged as failure
    @Test
    void testSendMessage_twilioThrowsException_logsFailure() {
        when(logRepository.existsByBillIdAndStatus(101L, "SENT")).thenReturn(false);

        try (MockedStatic<Message> mockedStatic = mockStatic(Message.class)) {
            mockedStatic.when(() ->
                    Message.creator(any(PhoneNumber.class), any(PhoneNumber.class), anyString())
            ).thenReturn(messageCreator);

            when(messageCreator.create()).thenThrow(new RuntimeException("Twilio error"));

            boolean result = whatsAppService.sendMessage(request);

            assertFalse(result);
            verify(logRepository).save(argThat(log ->
                    log.getStatus().equals("FAILED") &&
                            log.getError().contains("Twilio error")
            ));
        }
    }

    // Test: Format plain 10-digit number
    @Test
    void testFormatIndianNumber_withPlainNumber() {
        String result = invokeFormat("9876543210");
        assertEquals("+919876543210", result);
    }

    // Test: Format number with +91 prefix
    @Test
    void testFormatIndianNumber_with91Prefix() {
        String result = invokeFormat("+919876543210");
        assertEquals("+919876543210", result);
    }

    // Test: Format number with 0 prefix
    @Test
    void testFormatIndianNumber_with0Prefix() {
        String result = invokeFormat("09876543210");
        assertEquals("+919876543210", result);
    }

    // Test: Format number with other country code
    @Test
    void testFormatIndianNumber_withOtherCountryCode() {
        String result = invokeFormat("+441234567890");
        assertEquals("+441234567890", result);
    }

    // Test: Format number with spaces
    @Test
    void testFormatIndianNumber_withSpaces() {
        String result = invokeFormat(" 98765 43210 ");
        assertEquals("+919876543210", result);
    }

    // Test: Format empty input
    @Test
    void testFormatIndianNumber_emptyInput() {
        String result = invokeFormat("");
        assertEquals("", result);
    }

    // Test: Format null input
    @Test
    void testFormatIndianNumber_nullInput() {
        String result = invokeFormat(null);
        assertEquals("", result);
    }

    // Helper method to invoke private phone number formatting logic via reflection
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
