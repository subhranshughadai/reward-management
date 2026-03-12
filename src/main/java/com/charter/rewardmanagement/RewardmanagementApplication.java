package com.charter.rewardmanagement;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

/**
 * Main class for the Reward Management Spring Boot application.
 */
@EnableCaching
@SpringBootApplication
public class RewardmanagementApplication {

	/**
	 * Application entry point.
	 *
	 * @param args command line arguments
	 */
	public static void main(String[] args) {
		SpringApplication.run(RewardmanagementApplication.class, args);
	}

}