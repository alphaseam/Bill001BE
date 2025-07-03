package com.hotelapi.repository;

import com.hotelapi.entity.Hotel;
import com.hotelapi.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    Optional<Product> findByProductNameAndHotel(String productName, Hotel hotel);

    Optional<Product> findByProductCodeAndHotel(String productCode, Hotel hotel);

    // Added for getting all products by hotel ID
    List<Product> findByHotelHotelId(Long hotelId);
}
