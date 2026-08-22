package com.flimbis.amd_air;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class AmdAirApplication {

	public static void main(String[] args) {
		SpringApplication.run(AmdAirApplication.class, args);
	}

}


// todo flow
// - step 1: search for flights with Flight Offers Search
// - step 2: confirm the availability and price with Flight Offers Price
// - step 3: create the reservation with Flight Create Orders