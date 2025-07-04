package com.hotelapi.dto;

import lombok.Data;

@Data
public class HotelDto {
    private String hotelName;
    private String ownerName;
    private String mobile;
    private String email;
    private String address;
    private String gstNumber;
    private String hotelType;
    private Boolean isActive;
}
