package com.hotelapi.service;

import com.hotelapi.dto.BillDto;
import com.hotelapi.entity.Bill;

import java.util.List;

public interface BillService {

    Bill createBill(BillDto dto);

    Bill getBillById(Long id);

    List<Bill> getAllBills();

    Bill updateBill(Long id, BillDto dto);

    void deleteBill(Long id);
}
