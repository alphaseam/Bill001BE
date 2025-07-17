package com.hotelapi;

import com.hotelapi.entity.*;
import com.hotelapi.repository.BillRepository;
import com.hotelapi.service.InvoiceService;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class InvoiceServiceTest {

    @Mock
    private BillRepository billRepository;

    private InvoiceService invoiceService;

    private Bill mockBill;

    // Set up the mock Bill and InvoiceService before each test
    @BeforeEach
    void setup() {
        File invoiceDir = new File("invoices");
        if (!invoiceDir.exists()) {
            invoiceDir.mkdirs();
        }

        invoiceService = new InvoiceService(billRepository, "invoices");

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

    // Delete generated PDF file after each test
    @AfterEach
    void cleanUp() {
        Path pdf = Paths.get("invoices", "Invoice_INV-1001.pdf");
        if (pdf.toFile().exists()) {
            pdf.toFile().delete();
        }
    }

    // Test for successful invoice PDF generation
    @Test
    void testGenerateInvoicePdf_success() throws Exception {
        when(billRepository.findById(1L)).thenReturn(Optional.of(mockBill));
        String path = invoiceService.generateInvoicePdf(1L);
        assertNotNull(path);
        assertTrue(path.endsWith(".pdf"));
        assertTrue(new File(path).exists());
        verify(billRepository, times(1)).findById(1L);
    }

    // Test when bill is not found, should throw exception
    @Test
    void testGenerateInvoicePdf_billNotFound() {
        when(billRepository.findById(1L)).thenReturn(Optional.empty());
        Exception exception = assertThrows(Exception.class, () -> invoiceService.generateInvoicePdf(1L));
        assertEquals("Bill not found with ID: 1", exception.getMessage());
    }

    // Test that generated PDF file exists and is not empty
    @Test
    void testGeneratedPdf_fileExists() throws Exception {
        when(billRepository.findById(1L)).thenReturn(Optional.of(mockBill));
        String path = invoiceService.generateInvoicePdf(1L);
        assertTrue(new File(path).exists());
        assertTrue(new File(path).length() > 0);
    }

    // Test generated file name is as expected
    @Test
    void testGenerateInvoicePdf_fileHasCorrectName() throws Exception {
        when(billRepository.findById(1L)).thenReturn(Optional.of(mockBill));
        String path = invoiceService.generateInvoicePdf(1L);
        assertTrue(path.contains("Invoice_INV-1001.pdf"));
    }

    // Test repository is called exactly once
    @Test
    void testRepositoryCalledOnce() throws Exception {
        when(billRepository.findById(1L)).thenReturn(Optional.of(mockBill));
        invoiceService.generateInvoicePdf(1L);
        verify(billRepository, times(1)).findById(1L);
    }

    // Test PDF generation when discount is zero
    @Test
    void testGenerateInvoicePdf_handlesZeroDiscount() throws Exception {
        mockBill.setDiscount(0.0);
        when(billRepository.findById(1L)).thenReturn(Optional.of(mockBill));
        String path = invoiceService.generateInvoicePdf(1L);
        assertTrue(new File(path).exists());
    }

    // Test PDF generation with multiple bill items
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

    // Test that generated path starts with invoices directory
    @Test
    void testInvoiceDirectoryPrefix() throws Exception {
        when(billRepository.findById(1L)).thenReturn(Optional.of(mockBill));
        String path = invoiceService.generateInvoicePdf(1L);
        assertTrue(path.startsWith("invoices/") || path.startsWith("invoices\\"));
    }

    // Test customer name in mock bill is correct
    @Test
    void testCustomerNameInBillEntity() {
        assertEquals("Test Customer", mockBill.getCustomer().getName());
    }

    // Test that total is calculated as subtotal + tax - discount
    @Test
    void testTotalCalculationIntegrity() {
        double expectedTotal = mockBill.getSubtotal() + mockBill.getTax() - mockBill.getDiscount();
        assertEquals(expectedTotal, mockBill.getTotal());
    }

    // Test PDF generation with no bill items
    @Test
    void testGenerateInvoicePdf_withEmptyBillItems() throws Exception {
        mockBill.setItems(List.of());
        when(billRepository.findById(1L)).thenReturn(Optional.of(mockBill));
        String path = invoiceService.generateInvoicePdf(1L);
        assertTrue(new File(path).exists());
    }

    // Test long customer name is handled gracefully
    @Test
    void testGenerateInvoicePdf_withLongCustomerName() throws Exception {
        Customer longNameCustomer = new Customer();
        longNameCustomer.setName("Very Long Customer Name That Should Still Work Properly In PDF Without Breaking Layout");
        mockBill.setCustomer(longNameCustomer);
        when(billRepository.findById(1L)).thenReturn(Optional.of(mockBill));
        String path = invoiceService.generateInvoicePdf(1L);
        assertTrue(new File(path).exists());
    }

    // Test directory is created if not already present
    @Test
    void testDirectoryCreatedIfNotExist() throws Exception {
        File dir = new File("invoices");
        if (dir.exists()) {
            dir.delete(); // delete to check auto-creation
        }
        when(billRepository.findById(1L)).thenReturn(Optional.of(mockBill));
        String path = invoiceService.generateInvoicePdf(1L);
        assertTrue(new File(path).exists());
        assertTrue(new File("invoices").exists());
    }

    // Test generated PDF has a reasonable minimum size
    @Test
    void testPdfHasMinimumSize_whenItemPresent() throws Exception {
        when(billRepository.findById(1L)).thenReturn(Optional.of(mockBill));
        String path = invoiceService.generateInvoicePdf(1L);
        assertTrue(new File(path).length() > 100);
    }
}
