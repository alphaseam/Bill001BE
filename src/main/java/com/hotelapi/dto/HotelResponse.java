package com.hotelapi.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class HotelResponse {
    private String hotelName;
    private String ownerName;
    private String mobile;
    private String email;
    private String address;
    private String gstNumber;
    private String hotelType;
    private Boolean isActive;
}
