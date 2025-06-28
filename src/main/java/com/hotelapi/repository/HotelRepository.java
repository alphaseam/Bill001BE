package com.hotelapi.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.hotelapi.entity.Hotel;


@Repository
public interface HotelRepository extends JpaRepository<Hotel, Long> {
	
    boolean existsByMobile(String mobile);

    Optional<Hotel> findByMobile(String mobile);
}