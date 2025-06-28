package com.hotelapi.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.hotelapi.dto.HotelResponse;
import com.hotelapi.dto.GenericResponse;
import com.hotelapi.service.HotelService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

@RestController
@RequestMapping("/api")
@Tag(name = "Hotel", description = "Hotel creation and update operations")
public class HotelController {

    @Autowired
    private HotelService hotelService;

    @PostMapping("/hotel")
    @Operation(summary = "Add a new hotel", description = "Create and save a new hotel in the system")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Hotel added successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid hotel data")
    })
    public ResponseEntity<GenericResponse> addHotel(@RequestBody HotelResponse dto) {
        GenericResponse response = hotelService.saveHotel(dto);
        return response.isSuccess()
                ? ResponseEntity.ok(response)
                : ResponseEntity.badRequest().body(response);
    }

    @PutMapping("/hotel/{id}")
    @Operation(summary = "Update an existing hotel", description = "Update details of an existing hotel using its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Hotel updated successfully"),
            @ApiResponse(responseCode = "404", description = "Hotel not found")
    })
    public ResponseEntity<GenericResponse> updateHotel(@PathVariable Long id, @RequestBody HotelResponse dto) {
        GenericResponse response = hotelService.updateHotel(id, dto);
        return response.isSuccess()
                ? ResponseEntity.ok(response)
                : ResponseEntity.status(404).body(response);
    }
}
