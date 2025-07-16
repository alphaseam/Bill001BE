package com.hotelapi.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * BillStatsDto represents bill statistics for analytics
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "DTO representing bill statistics")
public class BillStatsDto {

    @Schema(description = "Type of statistics", example = "monthly")
    private String type;

    @Schema(description = "Total number of bills")
    private Long totalBills;

    @Schema(description = "Total revenue")
    private Double totalRevenue;

    @Schema(description = "Average bill amount")
    private Double averageBillAmount;

    @Schema(description = "Period label", example = "2024-01")
    private String period;

    @Schema(description = "Detailed statistics by period or product")
    private List<StatItem> details;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema(description = "Individual stat item")
    public static class StatItem {
        @Schema(description = "Label for the stat", example = "Paneer Tikka")
        private String label;

        @Schema(description = "Count of bills/items")
        private Long count;

        @Schema(description = "Total amount")
        private Double amount;

        @Schema(description = "Period (for time-based stats)", example = "2024-01-15")
        private String period;
    }
}
