package com.hotelapi.controller;

import com.hotelapi.dto.GenericResponse;
import com.hotelapi.dto.HotelDto;
import com.hotelapi.dto.HotelResponse;
import com.hotelapi.service.HotelService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@Tag(name = "Hotel", description = "Hotel creation, update, delete, and retrieval operations")
public class HotelController {

    @Autowired
    private HotelService hotelService;

    @PostMapping("/hotel")
    @Operation(summary = "Add a new hotel", description = "Create and save a new hotel in the system")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Hotel added successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid hotel data")
    })
    public ResponseEntity<GenericResponse<HotelResponse>> addHotel(@RequestBody HotelDto dto) {
        GenericResponse<HotelResponse> response = hotelService.saveHotel(dto);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/hotel/{id}")
    @Operation(summary = "Update an existing hotel", description = "Update full details of an existing hotel using its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Hotel updated successfully"),
            @ApiResponse(responseCode = "404", description = "Hotel not found")
    })
    public ResponseEntity<GenericResponse<HotelResponse>> updateHotel(@PathVariable Long id, @RequestBody HotelDto dto) {
        GenericResponse<HotelResponse> response = hotelService.updateHotel(id, dto);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/hotel/{id}")
    @Operation(summary = "Partially update hotel", description = "Update selected fields of a hotel using its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Hotel partially updated successfully"),
            @ApiResponse(responseCode = "404", description = "Hotel not found")
    })
    public ResponseEntity<GenericResponse<HotelResponse>> patchHotel(@PathVariable Long id, @RequestBody HotelDto dto) {
        GenericResponse<HotelResponse> response = hotelService.patchHotel(id, dto);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/hotel/{id}")
    @Operation(summary = "Get hotel by ID", description = "Retrieve hotel details by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Hotel retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Hotel not found")
    })
    public ResponseEntity<GenericResponse<HotelResponse>> getHotelById(@PathVariable Long id) {
        GenericResponse<HotelResponse> response = hotelService.getHotelById(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/hotel/user/{userId}")
    @Operation(summary = "Get all hotels by user ID (owner name)", description = "Fetch all hotels belonging to a specific user (owner name)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Hotels retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "No hotels found for the user")
    })
    public ResponseEntity<GenericResponse<List<HotelResponse>>> getHotelsByUser(@PathVariable String userId) {
        GenericResponse<List<HotelResponse>> response = hotelService.getHotelsByUser(userId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/hotel/all")
    @Operation(summary = "Get all hotels", description = "Retrieve all hotel records")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "All hotels retrieved successfully")
    })
    public ResponseEntity<GenericResponse<List<HotelResponse>>> getAllHotels() {
        GenericResponse<List<HotelResponse>> response = hotelService.getAllHotels();
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/hotel/{id}")
    @Operation(summary = "Delete hotel by ID", description = "Remove a hotel from the system using its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Hotel deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Hotel not found")
    })
    public ResponseEntity<GenericResponse<String>> deleteHotel(@PathVariable Long id) {
        GenericResponse<String> response = hotelService.deleteHotelById(id);
        return ResponseEntity.ok(response);
    }
}
