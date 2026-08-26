package com.flimbis.amd_air.flight;

import com.amadeus.Amadeus;
import com.amadeus.Params;
import com.amadeus.ReferenceData;
import com.amadeus.Shopping;
import com.amadeus.referencedata.Locations;
import com.amadeus.resources.FlightOfferSearch;
import com.amadeus.resources.Location;
import com.amadeus.shopping.FlightOffersSearch;
import com.flimbis.amd_air.common.AmdConnect;
import com.flimbis.amd_air.model.FlightMapper;
import com.flimbis.amd_air.model.FlightOfferSearchRequestDto;
import com.flimbis.amd_air.model.LocationDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class FlightSearchServiceTest {

    private FlightSearchService service;
    @Mock
    private AmdConnect amd;
    @Mock
    private FlightMapper mapper;

    @BeforeEach
    void setUp() {
        service = new FlightSearchService(amd, mapper);
    }

    @Test
    void testSearchLocation_whenValidKeyword_shouldReturnLocation() throws Exception {
        String locationKeyword = "LON";

        Amadeus amadeus = mock(Amadeus.class);
        amadeus.referenceData = mock(ReferenceData.class);
        amadeus.referenceData.locations = mock(Locations.class);
        when(amd.getAmadeus()).thenReturn(amadeus);

        Location location = mock(Location.class);
        when(amadeus.referenceData.locations.get(Params
                .with("keyword", locationKeyword)
                .and("subType", Locations.AIRPORT))).thenReturn(new Location[]{location});

        LocationDto dto = LocationDto.builder()
                .iataCode("LHR")
                .name("LONDON HEATHROW")
                .build();
        when(mapper.toLocationDto(location)).thenReturn(dto);

        List<LocationDto> result = service.searchLocation(locationKeyword);

        assertEquals(1, result.size());
        assertEquals(dto, result.get(0));
    }

    @Test
    void testSearchFlightOffers_whenValidRequest_shouldReturnFLightOffers() throws Exception {
        FlightOfferSearchRequestDto request = new FlightOfferSearchRequestDto();
        request.setOrigin("JFK");
        request.setDestination("LON");
        request.setDepartureDate("2026-09-01");
        request.setAdults(2);

        Amadeus amadeus = mock(Amadeus.class);
        amadeus.shopping = mock(Shopping.class);
        amadeus.shopping.flightOffersSearch = mock(FlightOffersSearch.class);
        when(amd.getAmadeus()).thenReturn(amadeus);

        Params params = Params.with("originLocationCode", request.getOrigin())
                .and("destinationLocationCode", request.getDestination())
                .and("departureDate", request.getDepartureDate())
                .and("adults", String.valueOf(request.getAdults()))
                .and("max", 5);

        FlightOfferSearch offer = mock(FlightOfferSearch.class);
        FlightOfferSearch[] offers = new FlightOfferSearch[]{offer};
        when(amadeus.shopping.flightOffersSearch.get(params)).thenReturn(offers);

        FlightOfferSearch[] result = service.searchFlightOffers(request);

        assertEquals(1, result.length);
        assertEquals(offer, result[0]);
    }
}
