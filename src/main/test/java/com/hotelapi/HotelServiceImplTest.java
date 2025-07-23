package com.hotelapi;

import com.hotelapi.dto.GenericResponse;
import com.hotelapi.dto.HotelDto;
import com.hotelapi.dto.HotelResponse;
import com.hotelapi.entity.Hotel;
import com.hotelapi.exception.InvalidInputException;
import com.hotelapi.exception.NoDataFoundException;
import com.hotelapi.exception.ResourceNotFoundException;
import com.hotelapi.repository.HotelRepository;
import com.hotelapi.service.impl.HotelServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class HotelServiceImplTest {

    @Mock
    private HotelRepository hotelRepository;

    @InjectMocks
    private HotelServiceImpl hotelService;

    private Hotel hotel;
    private HotelDto dto;

    @BeforeEach
    void setUp() {
        dto = new HotelDto();
        dto.setHotelName("Test Hotel");
        dto.setOwnerName("John Doe");
        dto.setMobile("9999999999");
        dto.setEmail("test@hotel.com");
        dto.setAddress("Test City");
        dto.setGstNumber("GST123");
        dto.setHotelType("Luxury");
        dto.setIsActive(true);

        hotel = Hotel.builder()
                .hotelId(1L)
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
    }

    // Test saving a new hotel successfully
    @Test
    void saveHotel_success() {
        when(hotelRepository.existsByMobile(dto.getMobile())).thenReturn(false);
        when(hotelRepository.save(any())).thenReturn(hotel);

        GenericResponse<HotelResponse> response = hotelService.saveHotel(dto);

        assertEquals("Hotel saved successfully", response.getMessage());
        assertEquals(dto.getHotelName(), response.getData().getHotelName());
    }

    // Test saving a hotel with duplicate mobile number
    @Test
    void saveHotel_duplicateMobile() {
        when(hotelRepository.existsByMobile(dto.getMobile())).thenReturn(true);
        assertThrows(InvalidInputException.class, () -> hotelService.saveHotel(dto));
    }

    // Test updating a hotel successfully
    @Test
    void updateHotel_success() {
        when(hotelRepository.findById(1L)).thenReturn(Optional.of(hotel));
        when(hotelRepository.save(any())).thenReturn(hotel);

        GenericResponse<HotelResponse> response = hotelService.updateHotel(1L, dto);

        assertEquals("Hotel updated successfully", response.getMessage());
        assertEquals(dto.getMobile(), response.getData().getMobile());
    }

    // Test updating a hotel with non-existing ID
    @Test
    void updateHotel_notFound() {
        when(hotelRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> hotelService.updateHotel(1L, dto));
    }

    // Test fetching a hotel by ID successfully
    @Test
    void getHotelById_success() {
        when(hotelRepository.findById(1L)).thenReturn(Optional.of(hotel));

        GenericResponse<HotelResponse> response = hotelService.getHotelById(1L);
        assertEquals("Hotel fetched successfully", response.getMessage());
    }

    // Test fetching hotels by owner name (user) successfully
    @Test
    void getHotelsByUser_found() {
        when(hotelRepository.findByOwnerName("John Doe")).thenReturn(Collections.singletonList(hotel));

        GenericResponse<List<HotelResponse>> response = hotelService.getHotelsByUser("John Doe");
        assertEquals(1, response.getData().size());
    }

    // Test fetching hotels by owner name (user) returns no data
    @Test
    void getHotelsByUser_noData() {
        when(hotelRepository.findByOwnerName("Unknown")).thenReturn(Collections.emptyList());

        assertThrows(NoDataFoundException.class, () -> hotelService.getHotelsByUser("Unknown"));
    }

    // Test fetching all hotels successfully
    @Test
    void getAllHotels_success() {
        when(hotelRepository.findAll()).thenReturn(Collections.singletonList(hotel));

        GenericResponse<List<HotelResponse>> response = hotelService.getAllHotels();
        assertEquals(1, response.getData().size());
    }

    // Test deleting a hotel by ID successfully
    @Test
    void deleteHotelById_success() {
        when(hotelRepository.findById(1L)).thenReturn(Optional.of(hotel));
        doNothing().when(hotelRepository).delete(hotel);

        GenericResponse<String> response = hotelService.deleteHotelById(1L);
        assertEquals("Hotel deleted successfully", response.getMessage());
    }

    // Test partially updating a hotel successfully
    @Test
    void patchHotel_success() {
        when(hotelRepository.findById(1L)).thenReturn(Optional.of(hotel));
        when(hotelRepository.save(any())).thenReturn(hotel);

        dto.setHotelName("Updated Name");
        dto.setMobile("8888888888");

        GenericResponse<HotelResponse> response = hotelService.patchHotel(1L, dto);
        assertEquals("Hotel partially updated successfully", response.getMessage());
        assertEquals("Updated Name", response.getData().getHotelName());
    }

    // Test partial update with a duplicate mobile number
    @Test
    void patchHotel_duplicateMobile() {
        when(hotelRepository.findById(1L)).thenReturn(Optional.of(hotel));
        dto.setMobile("8888888888");
        when(hotelRepository.existsByMobile("8888888888")).thenReturn(true);

        assertThrows(InvalidInputException.class, () -> hotelService.patchHotel(1L, dto));
    }
}
