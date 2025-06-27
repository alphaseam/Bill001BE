package com.hotelapi.repository;

import com.hotelapi.entity.WhatsAppLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface WhatsAppLogRepository extends JpaRepository<WhatsAppLog, Long> {

    List<WhatsAppLog> findByBillId(Long billId);

    List<WhatsAppLog> findByCustomerPhone(String customerPhone);

    boolean existsByBillIdAndStatus(Long billId, String status);
}
