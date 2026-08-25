package com.flimbis.amd_air.model;

import com.amadeus.resources.Location;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface FlightMapper {
    @Mapping(target = "type", source = "type")
    @Mapping(target = "subType", source = "subType")
    @Mapping(target = "name", source = "name")
    @Mapping(target = "detailedName", source = "detailedName")
    @Mapping(target = "iataCode", source = "iataCode")
    LocationDto toLocationDto(Location location);
}
