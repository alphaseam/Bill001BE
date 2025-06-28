package com.hotelapi.repository;

import com.hotelapi.dto.ProductSalesReportResponse;
import com.hotelapi.entity.Order;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends CrudRepository<Order, Long> {

    @Query("""
        SELECT new com.hotelapi.dto.ProductSalesReportResponse(
            o.product.id,
            o.product.name,
            SUM(o.quantity),
            SUM(o.totalPrice),
            o.product.category
        )
        FROM Order o
        WHERE MONTH(o.createdAt) = :month
          AND YEAR(o.createdAt) = :year
          AND o.status = 'COMPLETED'
        GROUP BY o.product.id, o.product.name, o.product.category
    """)
    List<ProductSalesReportResponse> getMonthlyProductSales(
        @Param("month") int month,
        @Param("year") int year
    );
}
