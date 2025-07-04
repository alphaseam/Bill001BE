package com.hotelapi.service;

import com.hotelapi.dto.ProductSalesReportResponse;
import com.hotelapi.dto.GenericResponse;
import com.hotelapi.dto.SalesReportResponse;

import java.util.List;

public interface SalesReportService {

    GenericResponse<List<ProductSalesReportResponse>> getMonthlyProductSalesReport(Integer month, Integer year);

    SalesReportResponse getDailySales(String fromDate, String toDate);

    SalesReportResponse getMonthlySales(Integer year);

    SalesReportResponse getYearlySales(Integer fromYear, Integer toYear);
}
