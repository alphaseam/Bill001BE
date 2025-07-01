package com.hotelapi;

import com.hotelapi.entity.*;
import com.hotelapi.repository.BillRepository;
import com.hotelapi.service.InvoiceService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.File;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class InvoiceServiceTest {

    @Mock
    private BillRepository billRepository;

    @InjectMocks
    private InvoiceService invoiceService;

    private Bill mockBill;

    @BeforeEach
    void setup() {
        Customer customer = new Customer();
        customer.setName("Test Customer");
        customer.setMobile("9876543210");

        BillItem item = new BillItem();
        item.setItemName("Room Rent");
        item.setQuantity(2);
        item.setUnitPrice(1000.0);
        item.setDiscount(100.0);
        item.setTotal(1900.0);

        mockBill = new Bill();
        mockBill.setId(1L);
        mockBill.setBillNumber("INV-1001");
        mockBill.setCustomer(customer);
        mockBill.setItems(List.of(item));
        mockBill.setCreatedAt(LocalDateTime.now());
        mockBill.setSubtotal(2000.0);
        mockBill.setTax(180.0);
        mockBill.setDiscount(100.0);
        mockBill.setTotal(2080.0);
    }

    @Test
    void testGenerateInvoicePdf_success() throws Exception {
        when(billRepository.findById(1L)).thenReturn(Optional.of(mockBill));

        String path = invoiceService.generateInvoicePdf(1L);

        assertNotNull(path);
        assertTrue(path.endsWith(".pdf"));
        assertTrue(new File(path).exists());
        verify(billRepository, times(1)).findById(1L);
    }

    @Test
    void testGenerateInvoicePdf_billNotFound() {
        when(billRepository.findById(1L)).thenReturn(Optional.empty());

        Exception exception = assertThrows(Exception.class, () -> invoiceService.generateInvoicePdf(1L));
        assertEquals("Bill not found with ID: 1", exception.getMessage());
    }

    @Test
    void testGeneratedPdf_fileExists() throws Exception {
        when(billRepository.findById(1L)).thenReturn(Optional.of(mockBill));

        String path = invoiceService.generateInvoicePdf(1L);
        File file = new File(path);

        assertTrue(file.exists());
        assertTrue(file.length() > 0);
    }

    @Test
    void testGenerateInvoicePdf_fileHasCorrectName() throws Exception {
        when(billRepository.findById(1L)).thenReturn(Optional.of(mockBill));

        String path = invoiceService.generateInvoicePdf(1L);

        assertTrue(path.contains("Invoice_INV-1001.pdf"));
    }

    @Test
    void testRepositoryCalledOnce() throws Exception {
        when(billRepository.findById(1L)).thenReturn(Optional.of(mockBill));

        invoiceService.generateInvoicePdf(1L);

        verify(billRepository, times(1)).findById(1L);
    }

    @Test
    void testGenerateInvoicePdf_handlesZeroDiscount() throws Exception {
        mockBill.setDiscount(0.0);
        when(billRepository.findById(1L)).thenReturn(Optional.of(mockBill));

        String path = invoiceService.generateInvoicePdf(1L);

        assertTrue(new File(path).exists());
    }

    @Test
    void testGenerateInvoicePdf_handlesMultipleItems() throws Exception {
        BillItem extraItem = new BillItem();
        extraItem.setItemName("Food");
        extraItem.setQuantity(1);
        extraItem.setUnitPrice(500.0);
        extraItem.setDiscount(0.0);
        extraItem.setTotal(500.0);

        mockBill.setItems(List.of(mockBill.getItems().get(0), extraItem));
        when(billRepository.findById(1L)).thenReturn(Optional.of(mockBill));

        String path = invoiceService.generateInvoicePdf(1L);

        assertTrue(path.endsWith(".pdf"));
    }

    @Test
    void testInvoiceDirectoryPrefix() throws Exception {
        when(billRepository.findById(1L)).thenReturn(Optional.of(mockBill));

        String path = invoiceService.generateInvoicePdf(1L);

        assertTrue(path.startsWith("invoices/"));
    }

    @Test
    void testCustomerNameInBillEntity() {

        assertEquals("Test Customer", mockBill.getCustomer().getName());
    }

    @Test
    void testTotalCalculationIntegrity() {
        double expectedTotal = mockBill.getSubtotal() + mockBill.getTax() - mockBill.getDiscount();
        assertEquals(expectedTotal, mockBill.getTotal());
    }
}


