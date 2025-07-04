package com.hotelapi;

import com.hotelapi.dto.GenericResponse;
import com.hotelapi.dto.ProductSalesReportResponse;
import com.hotelapi.exception.BadRequestException;
import com.hotelapi.repository.OrderRepository;
import com.hotelapi.service.SalesReportService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

// Enable Mockito support with JUnit 5
@ExtendWith(MockitoExtension.class)
class SalesReportServiceTest {

    // Inject mocked dependencies into SalesReportService
    @InjectMocks
    private SalesReportService salesReportService;

    // Mock OrderRepository dependency
    @Mock
    private OrderRepository orderRepository;

    // Reusable mock report data
    private List<ProductSalesReportResponse> mockReportList;

    // Setup reusable mock data before each test
    @BeforeEach
    void setUp() {
        ProductSalesReportResponse report1 = new ProductSalesReportResponse(1L, "Burger", 100L, 5000.0, "Food");
        ProductSalesReportResponse report2 = new ProductSalesReportResponse(2L, "Fries", 200L, 3000.0, "Snacks");
        mockReportList = Arrays.asList(report1, report2);
    }

    // Test: Valid input should return correct data
    @Test
    void testGetMonthlyReport_Success() {
        when(orderRepository.getMonthlyProductSales(6, 2025)).thenReturn(mockReportList);

        GenericResponse<List<ProductSalesReportResponse>> response = salesReportService.getMonthlyProductSalesReport(6, 2025);

        assertTrue(response.isSuccess());
        assertEquals("Product-wise monthly sales report generated", response.getMessage());
        assertEquals(2, response.getData().size());
        verify(orderRepository, times(1)).getMonthlyProductSales(6, 2025);
    }

    // Test: Null month should throw BadRequestException
    @Test
    void testGetMonthlyReport_NullMonth() {
        Exception ex = assertThrows(BadRequestException.class, () -> {
            salesReportService.getMonthlyProductSalesReport(null, 2025);
        });

        assertEquals("Both 'month' and 'year' are required.", ex.getMessage());
    }

    // Test: Null year should throw BadRequestException
    @Test
    void testGetMonthlyReport_NullYear() {
        Exception ex = assertThrows(BadRequestException.class, () -> {
            salesReportService.getMonthlyProductSalesReport(6, null);
        });

        assertEquals("Both 'month' and 'year' are required.", ex.getMessage());
    }

    // Test: Invalid month = 0 should throw BadRequestException
    @Test
    void testGetMonthlyReport_InvalidMonthZero() {
        Exception ex = assertThrows(BadRequestException.class, () -> {
            salesReportService.getMonthlyProductSalesReport(0, 2025);
        });

        assertEquals("Month must be between 1 and 12.", ex.getMessage());
    }

    // Test: Invalid month > 12 should throw BadRequestException
    @Test
    void testGetMonthlyReport_InvalidMonthGreaterThan12() {
        Exception ex = assertThrows(BadRequestException.class, () -> {
            salesReportService.getMonthlyProductSalesReport(13, 2025);
        });

        assertEquals("Month must be between 1 and 12.", ex.getMessage());
    }

    // Test: Empty result list should still return a successful response
    @Test
    void testGetMonthlyReport_EmptyData() {
        when(orderRepository.getMonthlyProductSales(6, 2025)).thenReturn(Collections.emptyList());

        GenericResponse<List<ProductSalesReportResponse>> response = salesReportService.getMonthlyProductSalesReport(6, 2025);

        assertTrue(response.isSuccess());
        assertNotNull(response.getData());
        assertEquals(0, response.getData().size());
    }

    // Test: Verify response content matches expected mock data
    @Test
    void testGetMonthlyReport_ResponseContainsCorrectData() {
        when(orderRepository.getMonthlyProductSales(6, 2025)).thenReturn(mockReportList);

        GenericResponse<List<ProductSalesReportResponse>> response = salesReportService.getMonthlyProductSalesReport(6, 2025);

        ProductSalesReportResponse report = response.getData().get(0);
        assertEquals(1L, report.getProductId());
        assertEquals("Burger", report.getProductName());
    }

    // Test: Verify that repository method is called exactly once
    @Test
    void testGetMonthlyReport_RepositoryMethodCalledOnce() {
        when(orderRepository.getMonthlyProductSales(5, 2024)).thenReturn(mockReportList);

        salesReportService.getMonthlyProductSalesReport(5, 2024);

        verify(orderRepository, times(1)).getMonthlyProductSales(5, 2024);
    }

    // Test: Negative month should throw BadRequestException
    @Test
    void testGetMonthlyReport_NegativeMonth() {
        Exception ex = assertThrows(BadRequestException.class, () -> {
            salesReportService.getMonthlyProductSalesReport(-3, 2024);
        });

        assertEquals("Month must be between 1 and 12.", ex.getMessage());
    }

    // Test: Edge case month values (1 and 12) should work correctly
    @Test
    void testGetMonthlyReport_BoundaryMonth_1And12() {
        when(orderRepository.getMonthlyProductSales(1, 2025)).thenReturn(mockReportList);
        when(orderRepository.getMonthlyProductSales(12, 2025)).thenReturn(mockReportList);

        GenericResponse<List<ProductSalesReportResponse>> janResponse = salesReportService.getMonthlyProductSalesReport(1, 2025);
        GenericResponse<List<ProductSalesReportResponse>> decResponse = salesReportService.getMonthlyProductSalesReport(12, 2025);

        assertEquals(2, janResponse.getData().size());
        assertEquals(2, decResponse.getData().size());
    }
}
