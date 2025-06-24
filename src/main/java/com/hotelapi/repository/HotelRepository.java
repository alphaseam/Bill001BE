package com.hotelapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.hotelapi.entity.Hotel;


@Repository
public interface HotelRepository extends JpaRepository<Hotel, Long> {
	
    boolean existsByMobile(String mobile);
}