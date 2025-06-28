package com.hotelapi.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.hotelapi.dto.HotelResponse;
import com.hotelapi.dto.GenericResponse;
import com.hotelapi.service.HotelService;

@RestController
@RequestMapping("/api")
public class HotelController {

    @Autowired
    private HotelService hotelService;

    @PostMapping("/hotel")
    public ResponseEntity<GenericResponse> addHotel(@RequestBody HotelResponse dto) {
        GenericResponse response = hotelService.saveHotel(dto);
        return response.isSuccess()
                ? ResponseEntity.ok(response)
                : ResponseEntity.badRequest().body(response);
    }

    @PutMapping("/hotel/{id}")
    public ResponseEntity<GenericResponse> updateHotel(@PathVariable Long id, @RequestBody HotelResponse dto) {
        GenericResponse response = hotelService.updateHotel(id, dto);
        return response.isSuccess()
                ? ResponseEntity.ok(response)
                : ResponseEntity.status(404).body(response);
    }
}
