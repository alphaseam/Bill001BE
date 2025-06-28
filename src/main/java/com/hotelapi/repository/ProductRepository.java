package com.hotelapi.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.hotelapi.entity.Hotel;
import com.hotelapi.entity.Product;


@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    Optional<Product> findByNameAndHotel(String name, Hotel hotel);


}

