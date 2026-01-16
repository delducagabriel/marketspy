package com.marketspy.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class MarketspyApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(MarketspyApiApplication.class, args);
	}

}
