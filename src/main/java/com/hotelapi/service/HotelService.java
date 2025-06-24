package com.hotelapi.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hotelapi.dto.HotelDto;
import com.hotelapi.dto.ResponseDto;
import com.hotelapi.entity.Hotel;
import com.hotelapi.repository.HotelRepository;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class HotelService {

    @Autowired
    private HotelRepository hotelRepository;

    public ResponseDto saveHotel(HotelDto dto) {
        if (hotelRepository.existsByMobile(dto.getMobile())) {
            return new ResponseDto("Mobile number already exists", false);
        }

        Hotel hotel = new Hotel();
        hotel.setHotelName(dto.getHotelName());
        hotel.setOwnerName(dto.getOwnerName());
        hotel.setMobile(dto.getMobile());
        hotel.setEmail(dto.getEmail());
        hotel.setAddress(dto.getAddress());
        hotel.setGstNumber(dto.getGstNumber());
        hotel.setHotelType(dto.getHotelType());
        hotel.setIsActive(dto.getIsActive());
        hotel.setCreatedAt(LocalDateTime.now());
        hotel.setUpdatedAt(LocalDateTime.now());

        hotelRepository.save(hotel);
        return new ResponseDto("Hotel saved successfully", true);
    }

    public ResponseDto updateHotel(Long id, HotelDto dto) {
        Optional<Hotel> optionalHotel = hotelRepository.findById(id);
        if (optionalHotel.isEmpty()) {
            return new ResponseDto("Hotel not found", false);
        }

        Hotel hotel = optionalHotel.get();

        if (!hotel.getMobile().equals(dto.getMobile()) &&
                hotelRepository.existsByMobile(dto.getMobile())) {
            return new ResponseDto("Mobile number already exists", false);
        }

        hotel.setHotelName(dto.getHotelName());
        hotel.setOwnerName(dto.getOwnerName());
        hotel.setMobile(dto.getMobile());
        hotel.setEmail(dto.getEmail());
        hotel.setAddress(dto.getAddress());
        hotel.setGstNumber(dto.getGstNumber());
        hotel.setHotelType(dto.getHotelType());
        hotel.setIsActive(dto.getIsActive());
        hotel.setUpdatedAt(LocalDateTime.now());

        hotelRepository.save(hotel);
        return new ResponseDto("Hotel updated successfully", true);
    }
}
