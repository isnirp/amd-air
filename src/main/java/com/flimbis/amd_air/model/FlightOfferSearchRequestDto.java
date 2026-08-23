package com.flimbis.amd_air.model;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class FlightOfferSearchRequestDto {
    @NotBlank
    private String origin;
    @NotBlank
    private String destination;
    @NotBlank
    @Pattern(regexp = "\\d{4}-\\d{2}-\\d{2}")
    private String departureDate;
    @Pattern(regexp = "\\d{4}-\\d{2}-\\d{2}")
    private String returnDate;
    @Min(1)
    @Max(9)
    private int adults = 1;
    @Min(0)
    @Max(9)
    private int children = 0;
    @Min(0)
    @Max(9)
    private int infants = 0;
    private String travelClass; // ECONOMY, PREMIUM_ECONOMY, BUSINESS, FIRST
    private String currencyCode = "USD";
    private boolean nonStop = false;
}
