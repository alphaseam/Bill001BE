package com.hotelapi;

import com.hotelapi.dto.BillDto;
import com.hotelapi.dto.BillItemDto;
import com.hotelapi.dto.WhatsAppMessageRequest;
import com.hotelapi.entity.*;
import com.hotelapi.exception.BillNotFoundException;
import com.hotelapi.exception.InvalidInputException;
import com.hotelapi.repository.*;
import com.hotelapi.service.InvoiceService;
import com.hotelapi.service.WhatsAppService;
import com.hotelapi.service.impl.BillServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class BillServiceImplTest {

    @InjectMocks
    private BillServiceImpl billService;

    @Mock
    private BillRepository billRepository;

    @Mock
    private BillItemRepository billItemRepository;

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private InvoiceService invoiceService;

    @Mock
    private WhatsAppService whatsAppService;

    private Customer customer;
    private Product product;
    private BillDto billDto;
    private BillItemDto itemDto;


     // Set up test data and mock dependencies before each test.

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        customer = Customer.builder()
                .id(1L)
                .name("John Doe")
                .mobile("9999999999")
                .build();

        product = Product.builder()
                .id(101L)
                .productName("Paneer Butter Masala")
                .price(200.0)
                .build();

        itemDto = new BillItemDto(101L, 2, 200.0);

        billDto = new BillDto(
                1L,                      // Customer ID
                List.of(itemDto),        // List of bill items
                50.0,                    // Discount
                null, null, null, null,  // subtotal, tax, total, date
                "Test remarks"           // Remarks
        );
    }

    /**
     * Test successful creation of a bill.
     * Verifies that the bill is saved, invoice is generated, and WhatsApp notification is sent.
     */
    @Test
    void testCreateBill_Success() throws Exception {
        when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));
        when(productRepository.findById(101L)).thenReturn(Optional.of(product));
        when(billRepository.save(any())).thenAnswer(i -> i.getArgument(0));
        when(invoiceService.generateInvoicePdf(anyLong())).thenReturn("/invoice.pdf");

        Bill saved = billService.createBill(billDto);

        assertNotNull(saved);
        assertEquals(customer, saved.getCustomer());
        assertEquals(1, saved.getItems().size());
        verify(whatsAppService).sendMessage(any(WhatsAppMessageRequest.class));
    }

    /**
     * Test creation of a bill with an invalid customer ID.
     * Expects InvalidInputException with "Customer not found" message.
     */
    @Test
    void testCreateBill_InvalidCustomer_ThrowsException() {
        when(customerRepository.findById(1L)).thenReturn(Optional.empty());

        InvalidInputException ex = assertThrows(InvalidInputException.class, () -> {
            billService.createBill(billDto);
        });

        assertTrue(ex.getMessage().contains("Customer not found"));
    }

    /**
     * Test creation of a bill with an invalid product ID.
     * Expects InvalidInputException with "Product not found" message.
     */
    @Test
    void testCreateBill_InvalidProduct_ThrowsException() {
        when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));
        when(productRepository.findById(101L)).thenReturn(Optional.empty());

        InvalidInputException ex = assertThrows(InvalidInputException.class, () -> {
            billService.createBill(billDto);
        });

        assertTrue(ex.getMessage().contains("Product not found"));
    }

    /**
     * Test fetching a bill by its ID when it exists.
     * Asserts that the returned bill is correct.
     */
    @Test
    void testGetBillById_Success() {
        Bill bill = new Bill();
        bill.setId(1L);
        when(billRepository.findById(1L)).thenReturn(Optional.of(bill));

        Bill result = billService.getBillById(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
    }

    /**
     * Test fetching a bill by ID when it does not exist.
     * Expects a BillNotFoundException.
     */
    @Test
    void testGetBillById_NotFound() {
        when(billRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(BillNotFoundException.class, () -> billService.getBillById(1L));
    }

    /**
     * Test retrieving all bills from the repository.
     * Asserts that the returned list matches the mock data.
     */
    @Test
    void testGetAllBills() {
        List<Bill> bills = List.of(new Bill(), new Bill());
        when(billRepository.findAll()).thenReturn(bills);

        List<Bill> result = billService.getAllBills();

        assertEquals(2, result.size());
    }

    /**
     * Test updating an existing bill successfully.
     * Verifies that updated values are saved properly.
     */
    @Test
    void testUpdateBill_Success() {
        Bill existingBill = new Bill();
        existingBill.setId(1L);
        existingBill.setDiscount(0.0);
        existingBill.setSubtotal(0.0);
        existingBill.setTax(0.0);
        existingBill.setTotal(0.0);

        when(billRepository.findById(1L)).thenReturn(Optional.of(existingBill));
        when(billRepository.save(any())).thenAnswer(i -> i.getArgument(0));

        BillDto updateDto = new BillDto();
        updateDto.setDiscount(20.0);
        updateDto.setSubtotal(500.0);
        updateDto.setTax(60.0);
        updateDto.setTotal(540.0);

        Bill updated = billService.updateBill(1L, updateDto);

        assertEquals(20.0, updated.getDiscount());
        assertEquals(500.0, updated.getSubtotal());
        assertEquals(60.0, updated.getTax());
        assertEquals(540.0, updated.getTotal());
    }

    /**
     * Test updating a bill that doesn't exist.
     * Expects a BillNotFoundException to be thrown.
     */
    @Test
    void testUpdateBill_NotFound() {
        when(billRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(BillNotFoundException.class, () -> {
            billService.updateBill(1L, new BillDto());
        });
    }

    /**
     * Test deleting a bill by ID when it exists.
     * Ensures that delete is called on the repository.
     */
    @Test
    void testDeleteBill_Success() {
        Bill bill = new Bill();
        bill.setId(1L);
        when(billRepository.findById(1L)).thenReturn(Optional.of(bill));

        billService.deleteBill(1L);

        verify(billRepository).delete(bill);
    }

    /**
     * Test deleting a bill by ID when it doesn't exist.
     * Expects a BillNotFoundException.
     */
    @Test
    void testDeleteBill_NotFound() {
        when(billRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(BillNotFoundException.class, () -> billService.deleteBill(1L));
    }
}
