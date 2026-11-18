package com.flimbis.amd_air.flight;

import com.amadeus.Params;
import com.amadeus.exceptions.ResponseException;
import com.amadeus.referencedata.Locations;
import com.amadeus.resources.FlightOfferSearch;
import com.amadeus.resources.Location;
import com.flimbis.amd_air.common.AmdConnect;
import com.flimbis.amd_air.model.FlightMapper;
import com.flimbis.amd_air.model.FlightOfferSearchRequestDto;
import com.flimbis.amd_air.model.LocationDto;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@Slf4j
@Service
public class FlightSearchService {
    private final AmdConnect amd;
    private final FlightMapper mapper;

    public FlightSearchService(AmdConnect amdConnect, FlightMapper flightMapper) {
        this.amd = amdConnect;
        this.mapper = flightMapper;
    }

    public List<LocationDto> searchLocation(@NonNull String keyword) throws ResponseException {
        log.info("searching location: {}", keyword);

        Location[] locations = amd.getAmadeus().referenceData.locations.get(Params
                .with("keyword", keyword)
                .and("subType", Locations.AIRPORT));

        return Arrays.stream(locations)
                .map(mapper::toLocationDto)
                .toList();
    }

    // Step 1: search for flights with Flight Offers Search
    public FlightOfferSearch[] searchFlightOffers(FlightOfferSearchRequestDto request, Integer offerCount) throws ResponseException {
        log.info("step1: Flight Offers Search > searching flight offers for origin: {} and destination: {}",
                request.getOrigin(), request.getDestination());


        Params params = Params.with("originLocationCode", request.getOrigin())
                .and("destinationLocationCode", request.getDestination())
                .and("departureDate", request.getDepartureDate())
                .and("adults", String.valueOf(request.getAdults()))
                .and("max", getMaxOffers(offerCount));

        return amd.getAmadeus().shopping.flightOffersSearch.get(params);
    }

    private int getMaxOffers(Integer offerCount) {
        int maxOffersDefault = 5;
        int maxOffers = Objects.requireNonNullElse(offerCount, maxOffersDefault);
        return maxOffers != 0? maxOffers: maxOffersDefault;
    }
}
