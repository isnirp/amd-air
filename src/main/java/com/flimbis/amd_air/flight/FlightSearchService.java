package com.flimbis.amd_air.flight;

import com.amadeus.Params;
import com.amadeus.exceptions.ResponseException;
import com.amadeus.referencedata.Locations;
import com.amadeus.resources.Location;
import com.flimbis.amd_air.common.AmdConnect;
import com.flimbis.amd_air.model.FlightMapper;
import com.flimbis.amd_air.model.LocationDto;
import lombok.NonNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
public class FlightSearchService {
    private static final Logger logger = LoggerFactory.getLogger(FlightSearchService.class);
    private final AmdConnect amd;
    private final FlightMapper mapper;

    public FlightSearchService(AmdConnect amdConnect, FlightMapper flightMapper) {
        this.amd = amdConnect;
        this.mapper = flightMapper;
    }

    public List<LocationDto> searchLocation(@NonNull String keyword) throws ResponseException {
        logger.info("searching location: {}", keyword);

        Location[] locations = amd.getAmadeus().referenceData.locations.get(Params
                .with("keyword", keyword)
                .and("subType", Locations.AIRPORT));

        return Arrays.stream(locations)
                .map(mapper::toLocationDto)
                .toList();
    }
}
