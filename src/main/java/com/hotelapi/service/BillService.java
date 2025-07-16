package com.hotelapi.service;

import com.hotelapi.dto.BillDto;
import com.hotelapi.dto.BillStatsDto;
import com.hotelapi.dto.MobileBillRequest;
import com.hotelapi.dto.MobileBillResponse;
import com.hotelapi.entity.Bill;

import java.util.List;

public interface BillService {

    Bill createBill(BillDto dto);

    Bill getBillById(Long id);

    List<Bill> getAllBills();

    Bill updateBill(Long id, BillDto dto);

    void deleteBill(Long id);

    List<Bill> getBillsByUserId(Long userId);

    List<Bill> getBillsByProductName(String productName);

    List<Bill> getBillsByDateRange(Long userId, String from, String to);

    BillStatsDto getBillStats(String type);

    // New method for mobile bill creation
    MobileBillResponse createMobileBill(MobileBillRequest request);
}
