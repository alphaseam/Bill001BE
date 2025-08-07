package com.hotelapi.service;

import com.hotelapi.dto.GenericResponse;
import com.hotelapi.dto.HotelDto;
import com.hotelapi.dto.HotelResponse;

import java.util.List;

public interface HotelService {

    GenericResponse<HotelResponse> saveHotel(HotelDto hotelDto);

    GenericResponse<HotelResponse> updateHotel(Long id, HotelDto hotelDto);

    GenericResponse<HotelResponse> getHotelById(Long id);  // JIRA-001

    GenericResponse<List<HotelResponse>> getHotelsByUser(String userId); // JIRA-002

    GenericResponse<List<HotelResponse>> getAllHotels(); // JIRA-003  

    GenericResponse<String> deleteHotelById(Long id); // JIRA-004

    GenericResponse<String> forceDeleteHotelById(Long id); // Force delete with cascading product deletion

    GenericResponse<HotelResponse> patchHotel(Long id, HotelDto hotelDto); // JIRA-005
}
