package com.hotelapi.service;

import com.hotelapi.dto.ProductSalesReportDto;
import com.hotelapi.dto.ResponseDto;
import com.hotelapi.exception.BadRequestException;
import com.hotelapi.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SalesReportService {

    private final OrderRepository orderRepository;

    public ResponseDto<List<ProductSalesReportDto>> getMonthlyProductSalesReport(Integer month, Integer year) {

        if (month == null || year == null) {
            throw new BadRequestException("Both 'month' and 'year' are required.");
        }

        if (month < 1 || month > 12) {
            throw new BadRequestException("Month must be between 1 and 12.");
        }

        List<ProductSalesReportDto> reportList = orderRepository.getMonthlyProductSales(month, year);

        return new ResponseDto<>("Product-wise monthly sales report generated", true, reportList);
    }
}
