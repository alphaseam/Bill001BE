package com.hotelapi;

import com.hotelapi.dto.GenericResponse;
import com.hotelapi.dto.HotelResponse;
import com.hotelapi.entity.Hotel;
import com.hotelapi.repository.HotelRepository;
import com.hotelapi.service.HotelService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class HotelServiceTest {

    @Mock private HotelRepository hotelRepository;

    @InjectMocks private HotelService hotelService;

    private HotelResponse sampleDto;

    @BeforeEach
    void setUp() {
        sampleDto = new HotelResponse();
        sampleDto.setHotelName("Taj");
        sampleDto.setOwnerName("Ratan Tata");
        sampleDto.setMobile("9876543210");
        sampleDto.setEmail("taj@example.com");
        sampleDto.setAddress("Mumbai");
        sampleDto.setGstNumber("27ABCDE1234F1Z5");
        sampleDto.setHotelType("5-star");
        sampleDto.setIsActive(true);
    }

    @Test
    void testSaveHotel_Success() {
        when(hotelRepository.existsByMobile("9876543210")).thenReturn(false);

        GenericResponse response = hotelService.saveHotel(sampleDto);
        //System.out.println(response);
        assertTrue(response.isSuccess());
        assertEquals("Hotel saved successfully", response.getMessage());
        verify(hotelRepository).save(any(Hotel.class));
    }

    @Test
    void testSaveHotel_DuplicateMobile() {
        when(hotelRepository.existsByMobile("9876543210")).thenReturn(true);

        GenericResponse response = hotelService.saveHotel(sampleDto);
        //System.out.println(response);

        assertFalse(response.isSuccess());
        assertEquals("Mobile number already exists", response.getMessage());
        verify(hotelRepository, never()).save(any());
    }

    @Test
    void testUpdateHotel_Success() {
        Hotel hotel = new Hotel();
        hotel.setMobile("9876543210");
        when(hotelRepository.findById(1L)).thenReturn(Optional.of(hotel));

        GenericResponse response = hotelService.updateHotel(1L, sampleDto);
        //System.out.println(response);

        assertTrue(response.isSuccess());
        assertEquals("Hotel updated successfully", response.getMessage());
        verify(hotelRepository).save(hotel);
    }

    @Test
    void testUpdateHotel_HotelNotFound() {
        when(hotelRepository.findById(1L)).thenReturn(Optional.empty());

        GenericResponse response = hotelService.updateHotel(1L, sampleDto);

        assertFalse(response.isSuccess());
        assertEquals("Hotel not found", response.getMessage());
        verify(hotelRepository, never()).save(any());
    }

    @Test
    void testUpdateHotel_DuplicateMobileWhenChanged() {
        Hotel hotel = new Hotel();
        hotel.setMobile("1111111111");

        when(hotelRepository.findById(1L)).thenReturn(Optional.of(hotel));
        when(hotelRepository.existsByMobile("9876543210")).thenReturn(true);

        GenericResponse response = hotelService.updateHotel(1L, sampleDto);

        assertFalse(response.isSuccess());
        assertEquals("Mobile number already exists", response.getMessage());
        verify(hotelRepository, never()).save(any());
    }

    @Test
    void testUpdateHotel_SameMobileNoConflict() {
        Hotel hotel = new Hotel();
        hotel.setMobile("9876543210");

        when(hotelRepository.findById(1L)).thenReturn(Optional.of(hotel));

        GenericResponse response = hotelService.updateHotel(1L, sampleDto);

        assertTrue(response.isSuccess());
        assertEquals("Hotel updated successfully", response.getMessage());
        verify(hotelRepository).save(hotel);
    }

    @Test
    void testSaveHotel_FieldMapping() {
        when(hotelRepository.existsByMobile("9876543210")).thenReturn(false);
        ArgumentCaptor<Hotel> captor = ArgumentCaptor.forClass(Hotel.class);

        hotelService.saveHotel(sampleDto);

        verify(hotelRepository).save(captor.capture());
        Hotel saved = captor.getValue();

        assertEquals("Taj", saved.getHotelName());
        assertEquals("Ratan Tata", saved.getOwnerName());
        assertEquals("9876543210", saved.getMobile());
    }

    @Test
    void testUpdateHotel_FieldsUpdatedCorrectly() {
        Hotel hotel = new Hotel();
        hotel.setMobile("9876543210");

        when(hotelRepository.findById(1L)).thenReturn(Optional.of(hotel));

        hotelService.updateHotel(1L, sampleDto);

        assertEquals("Taj", hotel.getHotelName());
        assertEquals("Ratan Tata", hotel.getOwnerName());
    }

    @Test
    void testSaveHotel_SaveCalledOnce() {
        when(hotelRepository.existsByMobile("9876543210")).thenReturn(false);

        hotelService.saveHotel(sampleDto);

        verify(hotelRepository, times(1)).save(any());
    }

    @Test
    void testUpdateHotel_NoSaveWhenMissing() {
        when(hotelRepository.findById(99L)).thenReturn(Optional.empty());

        hotelService.updateHotel(99L, sampleDto);

        verify(hotelRepository, never()).save(any());
    }
}

