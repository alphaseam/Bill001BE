package com.hotelapi.service.impl;

import com.hotelapi.dto.GenericResponse;
import com.hotelapi.dto.ProductSalesReportResponse;
import com.hotelapi.dto.SalesReportResponse;
import com.hotelapi.exception.BadRequestException;
import com.hotelapi.repository.OrderRepository;
import com.hotelapi.service.SalesReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SalesReportServiceImpl implements SalesReportService {

    private final OrderRepository orderRepository;

    @Override
    public GenericResponse<List<ProductSalesReportResponse>> getMonthlyProductSalesReport(Integer month, Integer year) {
        if (month == null || year == null) {
            throw new BadRequestException("Both 'month' and 'year' are required.");
        }

        if (month < 1 || month > 12) {
            throw new BadRequestException("Month must be between 1 and 12.");
        }

        List<ProductSalesReportResponse> reportList = orderRepository.getMonthlyProductSales(month, year);

        return new GenericResponse<>("Product-wise monthly sales report generated", true, reportList);
    }

    @Override
    public SalesReportResponse getDailySales(String fromDate, String toDate) {
        // TODO: implement aggregation logic for daily sales
        return new SalesReportResponse();
    }

    @Override
    public SalesReportResponse getMonthlySales(Integer year) {
        // TODO: implement aggregation logic for monthly sales
        return new SalesReportResponse();
    }

    @Override
    public SalesReportResponse getYearlySales(Integer fromYear, Integer toYear) {
        // TODO: implement aggregation logic for yearly sales
        return new SalesReportResponse();
    }
}
