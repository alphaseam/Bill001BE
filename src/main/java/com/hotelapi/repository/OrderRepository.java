package com.hotelapi.repository;

import com.hotelapi.dto.ProductSalesReportResponse;
import com.hotelapi.entity.Order;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface OrderRepository extends CrudRepository<Order, Long> {

    @Query("""
        SELECT new com.hotelapi.dto.ProductSalesReportResponse(
            o.product.id,
            o.product.productName,
            SUM(o.quantity),
            SUM(o.totalPrice),
            o.product.category
        )
        FROM Order o
        WHERE MONTH(o.createdAt) = :month
          AND YEAR(o.createdAt) = :year
          AND o.status = 'COMPLETED'
        GROUP BY o.product.id, o.product.productName, o.product.category
    """)
    List<ProductSalesReportResponse> getMonthlyProductSales(
            @Param("month") int month,
            @Param("year") int year
    );

    @Query("""
        SELECT SUM(o.totalPrice) 
        FROM Order o 
        WHERE o.createdAt BETWEEN :from AND :to
          AND o.status = 'COMPLETED'
    """)
    BigDecimal getTotalRevenueBetweenDates(
            @Param("from") LocalDate from,
            @Param("to") LocalDate to
    );

    @Query("""
        SELECT COUNT(o.id) 
        FROM Order o 
        WHERE o.createdAt BETWEEN :from AND :to
          AND o.status = 'COMPLETED'
    """)
    Long getTotalTransactionsBetweenDates(
            @Param("from") LocalDate from,
            @Param("to") LocalDate to
    );
}
