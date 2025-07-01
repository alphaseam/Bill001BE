package com.hotelapi.dto;

import lombok.Data;
import java.util.List;

@Data
public class MobileBillRequest {
    private String customerName;
    private String mobileNumber;
    private List<Item> items;

    @Data
    public static class Item {
        private Long productId;
        private String productName;
        private int quantity;
        private double unitPrice;
        private Double discount; 
    }
}
