package com.flimbis.amd_air.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LocationDto {
    private String type;
    private String subType;
    private String name;
    private String detailedName;
    private String iataCode;
    private Double relevance;
}
