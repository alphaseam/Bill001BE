package com.hotelapi.service.impl;

import com.hotelapi.dto.GenericResponse;
import com.hotelapi.dto.ProductSalesReportResponse;
import com.hotelapi.dto.SalesReportResponse;
import com.hotelapi.exception.BadRequestException;
import com.hotelapi.repository.OrderRepository;
import com.hotelapi.service.SalesReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SalesReportServiceImpl implements SalesReportService {

    private final OrderRepository orderRepository;

    /**
     * Provides product-wise monthly sales report
     * @param month month for which report is needed (1-12)
     * @param year year for which report is needed
     * @return GenericResponse with a list of product sales data
     */
    @Override
    public GenericResponse<List<ProductSalesReportResponse>> getMonthlyProductSalesReport(Integer month, Integer year) {
        // validate required parameters
        if (month == null || year == null) {
            throw new BadRequestException("Both 'month' and 'year' are required.");
        }
        if (month < 1 || month > 12) {
            throw new BadRequestException("Month must be between 1 and 12.");
        }

        // query the repository for the aggregated product sales
        List<ProductSalesReportResponse> reportList = orderRepository.getMonthlyProductSales(month, year);

        // wrap in GenericResponse
        return new GenericResponse<>("Product-wise monthly sales report generated", true, reportList);
    }

    /**
     * Generates daily sales summary between given dates
     * @param fromDate start date in yyyy-MM-dd format
     * @param toDate end date in yyyy-MM-dd format
     * @return SalesReportResponse with revenue and transactions
     */
    @Override
    public SalesReportResponse getDailySales(String fromDate, String toDate) {
        LocalDate from;
        LocalDate to;

        // parse dates safely, default to today if null
        try {
            from = (fromDate != null) ? LocalDate.parse(fromDate) : LocalDate.now();
            to = (toDate != null) ? LocalDate.parse(toDate) : LocalDate.now();
        } catch (DateTimeParseException e) {
            throw new BadRequestException("Invalid date format. Use yyyy-MM-dd");
        }

        // query total revenue
        BigDecimal totalRevenue = orderRepository.getTotalRevenueBetweenDates(from, to);
        // query total transactions
        Long totalTransactions = orderRepository.getTotalTransactionsBetweenDates(from, to);

        // build and return a report DTO
        return SalesReportResponse.builder()
                .totalRevenue(totalRevenue != null ? totalRevenue : BigDecimal.ZERO)
                .totalTransactions(totalTransactions != null ? totalTransactions : 0L)
                .bestSellingProduct("Pending")  // you can add logic later to determine top product
                .topCustomer("Pending")         // you can add logic later to determine top customer
                .build();
    }

    /**
     * Generates monthly sales summary for the given year
     * @param year the year to calculate the report for
     * @return SalesReportResponse with revenue and transactions
     */
    @Override
    public SalesReportResponse getMonthlySales(Integer year) {
        if (year == null) {
            year = LocalDate.now().getYear(); // default to current year if not provided
        }

        // build date range for the whole year
        LocalDate from = LocalDate.of(year, 1, 1);
        LocalDate to = LocalDate.of(year, 12, 31);

        // query total revenue
        BigDecimal totalRevenue = orderRepository.getTotalRevenueBetweenDates(from, to);
        // query total transactions
        Long totalTransactions = orderRepository.getTotalTransactionsBetweenDates(from, to);

        return SalesReportResponse.builder()
                .totalRevenue(totalRevenue != null ? totalRevenue : BigDecimal.ZERO)
                .totalTransactions(totalTransactions != null ? totalTransactions : 0L)
                .bestSellingProduct("Pending")
                .topCustomer("Pending")
                .build();
    }

    /**
     * Generates yearly sales summary between the given year range
     * @param fromYear start year
     * @param toYear end year
     * @return SalesReportResponse with revenue and transactions
     */
    @Override
    public SalesReportResponse getYearlySales(Integer fromYear, Integer toYear) {
        // default to current year if not provided
        int startYear = (fromYear != null) ? fromYear : LocalDate.now().getYear();
        int endYear = (toYear != null) ? toYear : LocalDate.now().getYear();

        // build date range for those years
        LocalDate from = LocalDate.of(startYear, 1, 1);
        LocalDate to = LocalDate.of(endYear, 12, 31);

        // query total revenue
        BigDecimal totalRevenue = orderRepository.getTotalRevenueBetweenDates(from, to);
        // query total transactions
        Long totalTransactions = orderRepository.getTotalTransactionsBetweenDates(from, to);

        return SalesReportResponse.builder()
                .totalRevenue(totalRevenue != null ? totalRevenue : BigDecimal.ZERO)
                .totalTransactions(totalTransactions != null ? totalTransactions : 0L)
                .bestSellingProduct("Pending")
                .topCustomer("Pending")
                .build();
    }
}
