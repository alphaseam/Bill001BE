package com.hotelapi.dto;

import lombok.Data;
import java.time.LocalDate;
import java.util.List;

@Data
public class MobileBillResponse {
    private String message;
    private Long billId;
    private double totalAmount;
    private LocalDate billDate;
    private List<ItemSummary> items;

    @Data
    public static class ItemSummary {
        private String productName;
        private int quantity;
        private double total;
    }
}
