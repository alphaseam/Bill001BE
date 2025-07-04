package com.hotelapi;

import com.hotelapi.dto.GenericResponse;
import com.hotelapi.dto.HotelResponse;
import com.hotelapi.entity.Hotel;
import com.hotelapi.repository.HotelRepository;
import com.hotelapi.service.HotelService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class HotelServiceTest {

    @Mock
    private HotelRepository hotelRepository;

    @InjectMocks
    private HotelService hotelService;

    private HotelResponse sampleDto;

    /**
     * Set up reusable test data before each test case.
     * Prepares a sample HotelResponse DTO for testing create/update operations.
     */
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

    /**
     * Test saving a hotel when the mobile number does not already exist.
     * Verifies that the save is successful and repository save is called.
     */
    @Test
    void testSaveHotel_Success() {
        when(hotelRepository.existsByMobile("9876543210")).thenReturn(false);

        GenericResponse response = hotelService.saveHotel(sampleDto);

        assertTrue(response.isSuccess());
        assertEquals("Hotel saved successfully", response.getMessage());
        verify(hotelRepository).save(any(Hotel.class));
    }

    /**
     * Test saving a hotel when the mobile number already exists.
     * Expects an error message and save should not be called.
     */
    @Test
    void testSaveHotel_DuplicateMobile() {
        when(hotelRepository.existsByMobile("9876543210")).thenReturn(true);

        GenericResponse response = hotelService.saveHotel(sampleDto);

        assertFalse(response.isSuccess());
        assertEquals("Mobile number already exists", response.getMessage());
        verify(hotelRepository, never()).save(any());
    }

    /**
     * Test updating an existing hotel successfully.
     * Ensures updated values are saved and response is successful.
     */
    @Test
    void testUpdateHotel_Success() {
        Hotel hotel = new Hotel();
        hotel.setMobile("9876543210");

        when(hotelRepository.findById(1L)).thenReturn(Optional.of(hotel));

        GenericResponse response = hotelService.updateHotel(1L, sampleDto);

        assertTrue(response.isSuccess());
        assertEquals("Hotel updated successfully", response.getMessage());
        verify(hotelRepository).save(hotel);
    }

    /**
     * Test updating a hotel with a non-existing ID.
     * Expects a failure response and no save operation.
     */
    @Test
    void testUpdateHotel_HotelNotFound() {
        when(hotelRepository.findById(1L)).thenReturn(Optional.empty());

        GenericResponse response = hotelService.updateHotel(1L, sampleDto);

        assertFalse(response.isSuccess());
        assertEquals("Hotel not found", response.getMessage());
        verify(hotelRepository, never()).save(any());
    }

    /**
     * Test updating a hotel where mobile number has changed but already exists for another hotel.
     * Expects failure due to duplicate mobile.
     */
    @Test
    void testUpdateHotel_DuplicateMobileWhenChanged() {
        Hotel hotel = new Hotel();
        hotel.setMobile("1111111111"); // Different from sampleDto

        when(hotelRepository.findById(1L)).thenReturn(Optional.of(hotel));
        when(hotelRepository.existsByMobile("9876543210")).thenReturn(true); // SampleDto's mobile

        GenericResponse response = hotelService.updateHotel(1L, sampleDto);

        assertFalse(response.isSuccess());
        assertEquals("Mobile number already exists", response.getMessage());
        verify(hotelRepository, never()).save(any());
    }

    /**
     * Test updating a hotel where the mobile number remains unchanged.
     * Expect update to proceed without duplicate conflict.
     */
    @Test
    void testUpdateHotel_SameMobileNoConflict() {
        Hotel hotel = new Hotel();
        hotel.setMobile("9876543210"); // Same as sampleDto

        when(hotelRepository.findById(1L)).thenReturn(Optional.of(hotel));

        GenericResponse response = hotelService.updateHotel(1L, sampleDto);

        assertTrue(response.isSuccess());
        assertEquals("Hotel updated successfully", response.getMessage());
        verify(hotelRepository).save(hotel);
    }


     // Verify that the fields from DTO are mapped properly into the Hotel entity before saving.

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


     // Test to ensure update operation maps all fields from DTO correctly.

    @Test
    void testUpdateHotel_FieldsUpdatedCorrectly() {
        Hotel hotel = new Hotel();
        hotel.setMobile("9876543210");

        when(hotelRepository.findById(1L)).thenReturn(Optional.of(hotel));

        hotelService.updateHotel(1L, sampleDto);

        assertEquals("Taj", hotel.getHotelName());
        assertEquals("Ratan Tata", hotel.getOwnerName());
    }


     // Ensure that the repository save method is called only once during hotel save.

    @Test
    void testSaveHotel_SaveCalledOnce() {
        when(hotelRepository.existsByMobile("9876543210")).thenReturn(false);

        hotelService.saveHotel(sampleDto);

        verify(hotelRepository, times(1)).save(any());
    }


     // Ensure that update operation does not invoke save if hotel with the given ID is not found.

    @Test
    void testUpdateHotel_NoSaveWhenMissing() {
        when(hotelRepository.findById(99L)).thenReturn(Optional.empty());

        hotelService.updateHotel(99L, sampleDto);

        verify(hotelRepository, never()).save(any());
    }
}
