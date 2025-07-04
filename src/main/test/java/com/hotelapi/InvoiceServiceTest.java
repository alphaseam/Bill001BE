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

    /**
     * Setup method runs before each test. It:
     * - Ensures the `invoices` directory exists.
     * - Initializes `InvoiceService` with the test repository and output path.
     * - Creates a mock `Bill` object with customer and bill items.
     */
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


     // Clean-up method that deletes the generated invoice file after each test.

    @AfterEach
    void cleanUp() {
        Path pdf = Paths.get("invoices", "Invoice_INV-1001.pdf");
        if (pdf.toFile().exists()) {
            pdf.toFile().delete();
        }
    }

    /**
     * Test case to verify successful invoice PDF generation.
     * It checks that the returned file path is correct and the file exists.
     */
    @Test
    void testGenerateInvoicePdf_success() throws Exception {
        when(billRepository.findById(1L)).thenReturn(Optional.of(mockBill));

        String path = invoiceService.generateInvoicePdf(1L);

        assertNotNull(path, "PDF path should not be null");
        assertTrue(path.endsWith(".pdf"), "PDF path should end with .pdf");

        File generated = new File(path);
        assertTrue(generated.exists(), "Generated PDF file should exist");

        verify(billRepository, times(1)).findById(1L);
    }


     // Test case to ensure exception is thrown when the bill is not found.

    @Test
    void testGenerateInvoicePdf_billNotFound() {
        when(billRepository.findById(1L)).thenReturn(Optional.empty());

        Exception exception = assertThrows(Exception.class,
                () -> invoiceService.generateInvoicePdf(1L));
        assertEquals("Bill not found with ID: 1", exception.getMessage());
    }


     // Verifies that the PDF file is created and is not empty.

    @Test
    void testGeneratedPdf_fileExists() throws Exception {
        when(billRepository.findById(1L)).thenReturn(Optional.of(mockBill));

        String path = invoiceService.generateInvoicePdf(1L);
        File file = new File(path);

        assertTrue(file.exists(), "PDF file should exist after generation");
        assertTrue(file.length() > 0, "PDF file should not be empty");
    }


     // Confirms that the generated PDF has the correct filename format.

    @Test
    void testGenerateInvoicePdf_fileHasCorrectName() throws Exception {
        when(billRepository.findById(1L)).thenReturn(Optional.of(mockBill));

        String path = invoiceService.generateInvoicePdf(1L);
        assertTrue(path.contains("Invoice_INV-1001.pdf"), "Should contain correct invoice filename");
    }


     // Verifies that the bill repository is called exactly once.

    @Test
    void testRepositoryCalledOnce() throws Exception {
        when(billRepository.findById(1L)).thenReturn(Optional.of(mockBill));
        invoiceService.generateInvoicePdf(1L);
        verify(billRepository, times(1)).findById(1L);
    }

    /**
     * Tests the scenario where the bill has a zero discount.
     * Ensures that PDF is still generated successfully.
     */
    @Test
    void testGenerateInvoicePdf_handlesZeroDiscount() throws Exception {
        mockBill.setDiscount(0.0);
        when(billRepository.findById(1L)).thenReturn(Optional.of(mockBill));

        String path = invoiceService.generateInvoicePdf(1L);
        File file = new File(path);
        assertTrue(file.exists(), "PDF should exist with zero discount");
    }


     // Tests PDF generation with multiple bill items.

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
        assertTrue(path.endsWith(".pdf"), "Should generate PDF for multiple items");
    }


     // Confirms the generated PDF file is stored inside the `invoices/` directory.

    @Test
    void testInvoiceDirectoryPrefix() throws Exception {
        when(billRepository.findById(1L)).thenReturn(Optional.of(mockBill));

        String path = invoiceService.generateInvoicePdf(1L);
        assertTrue(path.startsWith("invoices/") || path.startsWith("invoices\\"), "Path should be under invoices/");
    }


     // Validates that the customer name in the mock bill entity is correctly set.

    @Test
    void testCustomerNameInBillEntity() {
        assertEquals("Test Customer", mockBill.getCustomer().getName());
    }


      // Verifies that the total amount calculation is correct.

    @Test
    void testTotalCalculationIntegrity() {
        double expectedTotal = mockBill.getSubtotal() + mockBill.getTax() - mockBill.getDiscount();
        assertEquals(expectedTotal, mockBill.getTotal(), "Total amount calculation should be correct");
    }
}
