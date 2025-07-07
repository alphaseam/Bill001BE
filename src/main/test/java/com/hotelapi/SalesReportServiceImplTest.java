package com.hotelapi;

import com.hotelapi.dto.GenericResponse;
import com.hotelapi.dto.ProductSalesReportResponse;
import com.hotelapi.dto.SalesReportResponse;
import com.hotelapi.exception.BadRequestException;
import com.hotelapi.repository.OrderRepository;
import com.hotelapi.service.impl.SalesReportServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class SalesReportServiceImplTest {

    @Mock
    private OrderRepository orderRepository;

    @InjectMocks
    private SalesReportServiceImpl salesReportService;

    private BigDecimal dummyRevenue;
    private Long dummyTransactions;
    private List<ProductSalesReportResponse> dummyProductList;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        dummyRevenue = new BigDecimal("15000.50");
        dummyTransactions = 25L;
        dummyProductList = List.of(new ProductSalesReportResponse(
                1L, "Burger", 100L, 5000.00, "Food"
        ));
    }

    // 1. Monthly Product Sales - Valid Input
    @Test
    void testGetMonthlyProductSalesReport_Success() {
        when(orderRepository.getMonthlyProductSales(6, 2025)).thenReturn(dummyProductList);

        GenericResponse<List<ProductSalesReportResponse>> response =
                salesReportService.getMonthlyProductSalesReport(6, 2025);

        assertTrue(response.isSuccess());
        assertEquals("Product-wise monthly sales report generated", response.getMessage());
        assertEquals(1, response.getData().size());
        verify(orderRepository).getMonthlyProductSales(6, 2025);
    }

    // 2. Monthly Product Sales - Null Month
    @Test
    void testGetMonthlyProductSalesReport_NullMonth_ThrowsException() {
        assertThrows(BadRequestException.class, () -> salesReportService.getMonthlyProductSalesReport(null, 2025));
    }

    // 3. Monthly Product Sales - Null Year
    @Test
    void testGetMonthlyProductSalesReport_NullYear_ThrowsException() {
        assertThrows(BadRequestException.class, () -> salesReportService.getMonthlyProductSalesReport(6, null));
    }

    // 4. Monthly Product Sales - Invalid Month
    @Test
    void testGetMonthlyProductSalesReport_InvalidMonth_ThrowsException() {
        assertThrows(BadRequestException.class, () -> salesReportService.getMonthlyProductSalesReport(13, 2025));
    }

    // 5. Daily Sales - Valid Dates
    @Test
    void testGetDailySales_Success() {
        LocalDate from = LocalDate.of(2025, 7, 1);
        LocalDate to = LocalDate.of(2025, 7, 5);

        when(orderRepository.getTotalRevenueBetweenDates(from, to)).thenReturn(dummyRevenue);
        when(orderRepository.getTotalTransactionsBetweenDates(from, to)).thenReturn(dummyTransactions);

        SalesReportResponse response = salesReportService.getDailySales("2025-07-01", "2025-07-05");

        assertEquals(dummyRevenue, response.getTotalRevenue());
        assertEquals(dummyTransactions, response.getTotalTransactions());
    }

    // 6. Daily Sales - Invalid Date Format
    @Test
    void testGetDailySales_InvalidDateFormat_ThrowsException() {
        assertThrows(BadRequestException.class, () -> salesReportService.getDailySales("2025-15-01", "2025-07-05"));
    }

    // 7. Monthly Sales - Valid Year
    @Test
    void testGetMonthlySales_Success() {
        LocalDate from = LocalDate.of(2025, 1, 1);
        LocalDate to = LocalDate.of(2025, 12, 31);

        when(orderRepository.getTotalRevenueBetweenDates(from, to)).thenReturn(dummyRevenue);
        when(orderRepository.getTotalTransactionsBetweenDates(from, to)).thenReturn(dummyTransactions);

        SalesReportResponse response = salesReportService.getMonthlySales(2025);

        assertEquals(dummyRevenue, response.getTotalRevenue());
        assertEquals(dummyTransactions, response.getTotalTransactions());
    }

    // 8. Monthly Sales - Null Year (Default to current year)
    @Test
    void testGetMonthlySales_NullYear_UsesCurrentYear() {
        int year = LocalDate.now().getYear();
        LocalDate from = LocalDate.of(year, 1, 1);
        LocalDate to = LocalDate.of(year, 12, 31);

        when(orderRepository.getTotalRevenueBetweenDates(from, to)).thenReturn(dummyRevenue);
        when(orderRepository.getTotalTransactionsBetweenDates(from, to)).thenReturn(dummyTransactions);

        SalesReportResponse response = salesReportService.getMonthlySales(null);

        assertEquals(dummyRevenue, response.getTotalRevenue());
        assertEquals(dummyTransactions, response.getTotalTransactions());
    }

    // 9. Yearly Sales - Valid Range
    @Test
    void testGetYearlySales_Success() {
        LocalDate from = LocalDate.of(2020, 1, 1);
        LocalDate to = LocalDate.of(2025, 12, 31);

        when(orderRepository.getTotalRevenueBetweenDates(from, to)).thenReturn(dummyRevenue);
        when(orderRepository.getTotalTransactionsBetweenDates(from, to)).thenReturn(dummyTransactions);

        SalesReportResponse response = salesReportService.getYearlySales(2020, 2025);

        assertEquals(dummyRevenue, response.getTotalRevenue());
        assertEquals(dummyTransactions, response.getTotalTransactions());
    }

    // 10. Yearly Sales - Null Years (Default to current)
    @Test
    void testGetYearlySales_NullYears_UseCurrent() {
        int year = LocalDate.now().getYear();
        LocalDate from = LocalDate.of(year, 1, 1);
        LocalDate to = LocalDate.of(year, 12, 31);

        when(orderRepository.getTotalRevenueBetweenDates(from, to)).thenReturn(dummyRevenue);
        when(orderRepository.getTotalTransactionsBetweenDates(from, to)).thenReturn(dummyTransactions);

        SalesReportResponse response = salesReportService.getYearlySales(null, null);

        assertEquals(dummyRevenue, response.getTotalRevenue());
        assertEquals(dummyTransactions, response.getTotalTransactions());
    }

    // 11. Daily Sales - Null Inputs (Use today’s date)
    @Test
    void testGetDailySales_NullDates_UseToday() {
        LocalDate today = LocalDate.now();

        when(orderRepository.getTotalRevenueBetweenDates(today, today)).thenReturn(dummyRevenue);
        when(orderRepository.getTotalTransactionsBetweenDates(today, today)).thenReturn(dummyTransactions);

        SalesReportResponse response = salesReportService.getDailySales(null, null);

        assertEquals(dummyRevenue, response.getTotalRevenue());
        assertEquals(dummyTransactions, response.getTotalTransactions());
    }

    // 12. Daily Sales - Revenue or Transactions Null Should Default to Zero
    @Test
    void testGetDailySales_NullValues_DefaultToZero() {
        LocalDate from = LocalDate.of(2025, 7, 1);
        LocalDate to = LocalDate.of(2025, 7, 5);

        when(orderRepository.getTotalRevenueBetweenDates(from, to)).thenReturn(null);
        when(orderRepository.getTotalTransactionsBetweenDates(from, to)).thenReturn(null);

        SalesReportResponse response = salesReportService.getDailySales("2025-07-01", "2025-07-05");

        assertEquals(BigDecimal.ZERO, response.getTotalRevenue());
        assertEquals(0L, response.getTotalTransactions());
    }
}

