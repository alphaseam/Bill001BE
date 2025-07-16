package com.hotelapi.repository;

import com.hotelapi.entity.Bill;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.List;
import java.time.LocalDateTime;

@Repository
public interface BillRepository extends JpaRepository<Bill, Long> {

    Optional<Bill> findByBillNumber(String billNumber);
List<Bill> findByUserId(Long userId);

    List<Bill> findByCreatedAtBetweenAndUserId(LocalDateTime start, LocalDateTime end, Long userId);

    List<Bill> findByItems_ItemNameContainingIgnoreCase(String productName);

}
