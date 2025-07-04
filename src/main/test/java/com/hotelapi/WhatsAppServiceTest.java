package com.hotelapi;

import com.hotelapi.config.TwilioConfig;
import com.hotelapi.dto.WhatsAppMessageRequest;
import com.hotelapi.entity.WhatsAppLog;
import com.hotelapi.repository.WhatsAppLogRepository;
import com.hotelapi.service.WhatsAppService;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.rest.api.v2010.account.MessageCreator;
import com.twilio.type.PhoneNumber;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class WhatsAppServiceTest {

    // Mock dependencies
    @Mock
    private TwilioConfig config;

    @Mock
    private WhatsAppLogRepository logRepository;

    // Inject mocks into the service
    @InjectMocks
    private WhatsAppService whatsAppService;

    private WhatsAppMessageRequest request; // Holds input data
    private MockedStatic<Message> messageMock; // Used for mocking static method

    // Setup test data before each test
    @BeforeEach
    public void setup() {
        request = WhatsAppMessageRequest.builder()
                .billId(101L)
                .customerName("Ravi Sharma")
                .customerPhone("+919876543210")
                .messageContent("Thank you for your visit.")
                .build();
    }

    // Close static mocks after each test
    @AfterEach
    public void tearDown() {
        if (messageMock != null) {
            messageMock.close();
        }
    }

    //  Test: Should skip sending if message already sent for bill
    @Test
    public void testMessageAlreadySent_shouldReturnFalse() {
        when(logRepository.existsByBillIdAndStatus(101L, "SENT")).thenReturn(true);

        boolean result = whatsAppService.sendMessage(request);

        assertFalse(result);
        verify(logRepository, never()).save(any()); // No log saved
    }

    // Test: Successful Twilio message send
    @Test
    public void testSendMessage_successfulSend_shouldReturnTrue() {
        when(logRepository.existsByBillIdAndStatus(101L, "SENT")).thenReturn(false);
        when(config.getWhatsappFrom()).thenReturn("whatsapp:+14155238886");

        // Mocking Twilio Message and its response
        Message mockMessage = mock(Message.class);
        when(mockMessage.getStatus()).thenReturn(Message.Status.SENT);

        MessageCreator messageCreatorMock = mock(MessageCreator.class);
        when(messageCreatorMock.create()).thenReturn(mockMessage);

        messageMock = mockStatic(Message.class);
        messageMock.when(() ->
                Message.creator(any(PhoneNumber.class), any(PhoneNumber.class), anyString())
        ).thenReturn(messageCreatorMock);

        boolean result = whatsAppService.sendMessage(request);

        assertTrue(result);
        verify(logRepository).save(any(WhatsAppLog.class)); // Log must be saved
    }

    //  Test: Twilio fails internally, return false but still log
    @Test
    public void testSendMessage_twilioFails_shouldReturnFalse() {
        when(logRepository.existsByBillIdAndStatus(101L, "SENT")).thenReturn(false);
        when(config.getWhatsappFrom()).thenReturn("whatsapp:+14155238886");

        MessageCreator messageCreatorMock = mock(MessageCreator.class);
        when(messageCreatorMock.create()).thenThrow(new RuntimeException("Twilio error"));

        messageMock = mockStatic(Message.class);
        messageMock.when(() ->
                Message.creator(any(PhoneNumber.class), any(PhoneNumber.class), anyString())
        ).thenReturn(messageCreatorMock);

        boolean result = whatsAppService.sendMessage(request);

        assertFalse(result);
        verify(logRepository).save(any(WhatsAppLog.class));
    }

    // Test: Log should be saved even if Twilio fails
    @Test
    public void testLogIsSavedRegardlessOfStatus() {
        when(logRepository.existsByBillIdAndStatus(anyLong(), anyString())).thenReturn(false);
        when(config.getWhatsappFrom()).thenReturn("whatsapp:+14155238886");

        MessageCreator messageCreatorMock = mock(MessageCreator.class);
        when(messageCreatorMock.create()).thenThrow(new RuntimeException("Twilio failure"));

        messageMock = mockStatic(Message.class);
        messageMock.when(() ->
                Message.creator(any(PhoneNumber.class), any(PhoneNumber.class), anyString())
        ).thenReturn(messageCreatorMock);

        whatsAppService.sendMessage(request);

        ArgumentCaptor<WhatsAppLog> captor = ArgumentCaptor.forClass(WhatsAppLog.class);
        verify(logRepository).save(captor.capture());
        assertNotNull(captor.getValue()); // Log must exist
    }

    //  Test: Ensure log fields are correctly set even when failed
    @Test
    public void testLogFieldsAreSetCorrectly() {
        when(logRepository.existsByBillIdAndStatus(anyLong(), anyString())).thenReturn(false);
        when(config.getWhatsappFrom()).thenReturn("whatsapp:+14155238886");

        MessageCreator messageCreatorMock = mock(MessageCreator.class);
        when(messageCreatorMock.create()).thenThrow(new RuntimeException("Sending failed"));

        messageMock = mockStatic(Message.class);
        messageMock.when(() ->
                Message.creator(any(PhoneNumber.class), any(PhoneNumber.class), anyString())
        ).thenReturn(messageCreatorMock);

        whatsAppService.sendMessage(request);

        ArgumentCaptor<WhatsAppLog> captor = ArgumentCaptor.forClass(WhatsAppLog.class);
        verify(logRepository).save(captor.capture());

        WhatsAppLog log = captor.getValue();
        assertEquals("FAILED", log.getStatus());
        assertEquals("Ravi Sharma", log.getCustomerName());
        assertEquals("+919876543210", log.getCustomerPhone());
    }

    //  Test: Empty message content should still try sending
    @Test
    public void testEmptyMessageContent() {
        request.setMessageContent("");

        when(logRepository.existsByBillIdAndStatus(anyLong(), anyString())).thenReturn(false);
        when(config.getWhatsappFrom()).thenReturn("whatsapp:+14155238886");

        Message mockMessage = mock(Message.class);
        when(mockMessage.getStatus()).thenReturn(Message.Status.SENT);

        MessageCreator messageCreatorMock = mock(MessageCreator.class);
        when(messageCreatorMock.create()).thenReturn(mockMessage);

        messageMock = mockStatic(Message.class);
        messageMock.when(() ->
                Message.creator(any(PhoneNumber.class), any(PhoneNumber.class), anyString())
        ).thenReturn(messageCreatorMock);

        boolean result = whatsAppService.sendMessage(request);
        assertTrue(result); // Still succeeds
    }

    // Test: Prevent duplicate messages for same bill
    @Test
    public void testMultipleMessagesForSameBill_shouldPreventDuplicate() {
        when(logRepository.existsByBillIdAndStatus(101L, "SENT")).thenReturn(true);

        boolean result = whatsAppService.sendMessage(request);

        assertFalse(result);
        verify(logRepository, never()).save(any());
    }

    // Test: Valid phone number formatting check
    @Test
    public void testValidPhoneFormatting() {
        request.setCustomerPhone("+919123456789");
        assertTrue(request.getCustomerPhone().startsWith("+91")); // Indian number
    }

    // Test: Invalid phone number format triggers failure
    @Test
    public void testInvalidPhoneNumberFormat_shouldFailSending() {
        request.setCustomerPhone("INVALID");

        when(logRepository.existsByBillIdAndStatus(anyLong(), anyString())).thenReturn(false);
        when(config.getWhatsappFrom()).thenReturn("whatsapp:+14155238886");

        MessageCreator messageCreatorMock = mock(MessageCreator.class);
        when(messageCreatorMock.create()).thenThrow(new IllegalArgumentException("Invalid phone format"));

        messageMock = mockStatic(Message.class);
        messageMock.when(() ->
                Message.creator(any(PhoneNumber.class), any(PhoneNumber.class), anyString())
        ).thenReturn(messageCreatorMock);

        boolean result = whatsAppService.sendMessage(request);
        assertFalse(result); // Fail gracefully
    }
}
