package com.hotelapi.service.impl;

import com.hotelapi.dto.GenericResponse;
import com.hotelapi.dto.HotelDto;
import com.hotelapi.dto.HotelResponse;
import com.hotelapi.entity.Hotel;
import com.hotelapi.entity.Product;
import com.hotelapi.exception.DatabaseException;
import com.hotelapi.exception.InvalidInputException;
import com.hotelapi.exception.NoDataFoundException;
import com.hotelapi.exception.ResourceNotFoundException;
import com.hotelapi.repository.HotelRepository;
import com.hotelapi.repository.ProductRepository;
import com.hotelapi.service.HotelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class HotelServiceImpl implements HotelService {

    @Autowired
    private HotelRepository hotelRepository;

    @Autowired
    private ProductRepository productRepository;

    @Override
    public GenericResponse<HotelResponse> saveHotel(HotelDto dto) {
        // Validate required fields
        if (dto.getHotelName() == null || dto.getHotelName().trim().isEmpty()) {
            throw new InvalidInputException("Hotel name is required");
        }
        if (dto.getOwnerName() == null || dto.getOwnerName().trim().isEmpty()) {
            throw new InvalidInputException("Owner name is required");
        }
        if (dto.getMobile() == null || dto.getMobile().trim().isEmpty()) {
            throw new InvalidInputException("Mobile number is required");
        }
        if (dto.getEmail() == null || dto.getEmail().trim().isEmpty()) {
            throw new InvalidInputException("Email is required");
        }
        if (dto.getAddress() == null || dto.getAddress().trim().isEmpty()) {
            throw new InvalidInputException("Address is required");
        }
        
        if (hotelRepository.existsByMobile(dto.getMobile().trim())) {
            throw new InvalidInputException("Mobile number already exists");
        }

        Hotel hotel = Hotel.builder()
                .hotelName(dto.getHotelName())
                .ownerName(dto.getOwnerName())
                .mobile(dto.getMobile())
                .email(dto.getEmail())
                .address(dto.getAddress())
                .gstNumber(dto.getGstNumber())
                .hotelType(dto.getHotelType())
                .isActive(dto.getIsActive())
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        Hotel saved = hotelRepository.save(hotel);
        HotelResponse response = mapToResponse(saved);
        return GenericResponse.ok("Hotel saved successfully", response);
    }

    @Override
    public GenericResponse<HotelResponse> updateHotel(Long id, HotelDto dto) {
        Hotel hotel = hotelRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Hotel not found with ID: " + id));

        // Validate required fields
        if (dto.getHotelName() == null || dto.getHotelName().trim().isEmpty()) {
            throw new InvalidInputException("Hotel name is required");
        }
        if (dto.getOwnerName() == null || dto.getOwnerName().trim().isEmpty()) {
            throw new InvalidInputException("Owner name is required");
        }
        if (dto.getMobile() == null || dto.getMobile().trim().isEmpty()) {
            throw new InvalidInputException("Mobile number is required");
        }
        if (dto.getEmail() == null || dto.getEmail().trim().isEmpty()) {
            throw new InvalidInputException("Email is required");
        }
        if (dto.getAddress() == null || dto.getAddress().trim().isEmpty()) {
            throw new InvalidInputException("Address is required");
        }

        // Check if mobile number has changed and if the new one already exists
        String existingMobile = hotel.getMobile();
        String newMobile = dto.getMobile().trim();
        
        if (existingMobile == null || !existingMobile.equals(newMobile)) {
            if (hotelRepository.existsByMobile(newMobile)) {
                throw new InvalidInputException("Mobile number already exists");
            }
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

        Hotel updated = hotelRepository.save(hotel);
        HotelResponse response = mapToResponse(updated);
        return GenericResponse.ok("Hotel updated successfully", response);
    }

    @Override
    public GenericResponse<HotelResponse> patchHotel(Long id, HotelDto dto) {
        Hotel hotel = hotelRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Hotel not found with ID: " + id));

        if (dto.getHotelName() != null) hotel.setHotelName(dto.getHotelName());
        if (dto.getOwnerName() != null) hotel.setOwnerName(dto.getOwnerName());

        if (dto.getMobile() != null) {
            String existingMobile = hotel.getMobile();
            String newMobile = dto.getMobile();
            
            if (existingMobile == null || !existingMobile.equals(newMobile)) {
                if (hotelRepository.existsByMobile(newMobile)) {
                    throw new InvalidInputException("Mobile number already exists");
                }
                hotel.setMobile(newMobile);
            }
        }

        if (dto.getEmail() != null) hotel.setEmail(dto.getEmail());
        if (dto.getAddress() != null) hotel.setAddress(dto.getAddress());
        if (dto.getGstNumber() != null) hotel.setGstNumber(dto.getGstNumber());
        if (dto.getHotelType() != null) hotel.setHotelType(dto.getHotelType());
        if (dto.getIsActive() != null) hotel.setIsActive(dto.getIsActive());

        hotel.setUpdatedAt(LocalDateTime.now());

        Hotel updated = hotelRepository.save(hotel);
        HotelResponse response = mapToResponse(updated);
        return GenericResponse.ok("Hotel partially updated successfully", response);
    }

    @Override
    public GenericResponse<HotelResponse> getHotelById(Long id) {
        Hotel hotel = hotelRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Hotel not found with ID: " + id));

        HotelResponse response = mapToResponse(hotel);
        return GenericResponse.ok("Hotel fetched successfully", response);
    }

    @Override
    public GenericResponse<List<HotelResponse>> getHotelsByUser(String userId) {
        List<Hotel> hotels = hotelRepository.findByOwnerName(userId);

        if (hotels.isEmpty()) {
            throw new NoDataFoundException("No hotels found for user: " + userId);
        }

        List<HotelResponse> responses = hotels.stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());

        return GenericResponse.ok("Hotels fetched successfully", responses);
    }

    @Override
    public GenericResponse<List<HotelResponse>> getAllHotels() {
        List<Hotel> hotels = hotelRepository.findAll();

        List<HotelResponse> responses = hotels.stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());

        return GenericResponse.ok("All hotels fetched successfully", responses);
    }

    @Override
    public GenericResponse<String> deleteHotelById(Long id) {
        Hotel hotel = hotelRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Hotel not found with ID: " + id));

        // Check if hotel has any associated products
        List<Product> associatedProducts = productRepository.findByHotelHotelId(id);
        if (!associatedProducts.isEmpty()) {
            throw new InvalidInputException("Cannot delete hotel. Hotel has " + associatedProducts.size() + 
                    " associated product(s). Please delete or transfer all products before deleting the hotel.");
        }

        hotelRepository.delete(hotel);
        return GenericResponse.ok("Hotel deleted successfully", null);
    }

    @Override
    @Transactional
    public GenericResponse<String> forceDeleteHotelById(Long id) {
        try {
            Hotel hotel = hotelRepository.findById(id)
                    .orElseThrow(() -> new ResourceNotFoundException("Hotel not found with ID: " + id));

            // Get associated products count for response message
            List<Product> associatedProducts = productRepository.findByHotelHotelId(id);
            int productCount = associatedProducts.size();

            // Delete all associated products first using repository method
            if (!associatedProducts.isEmpty()) {
                productRepository.deleteAllInBatch(associatedProducts);
                productRepository.flush(); // Ensure products are deleted before hotel deletion
            }

            // Now delete the hotel
            hotelRepository.delete(hotel);
            hotelRepository.flush(); // Ensure deletion is completed
            
            String message = productCount == 0 ? 
                "Hotel deleted successfully" : 
                "Hotel and " + productCount + " associated product(s) deleted successfully";
            
            return GenericResponse.ok(message, null);
            
        } catch (Exception e) {
            throw new DatabaseException("Failed to force delete hotel with ID: " + id + ". Error: " + e.getMessage(), e);
        }
    }

    private HotelResponse mapToResponse(Hotel hotel) {
        HotelResponse response = new HotelResponse();
        response.setHotelId(hotel.getHotelId());
        response.setHotelName(hotel.getHotelName());
        response.setOwnerName(hotel.getOwnerName());
        response.setMobile(hotel.getMobile());
        response.setEmail(hotel.getEmail());
        response.setAddress(hotel.getAddress());
        response.setGstNumber(hotel.getGstNumber());
        response.setHotelType(hotel.getHotelType());
        response.setIsActive(hotel.getIsActive());
        return response;
    }
}
