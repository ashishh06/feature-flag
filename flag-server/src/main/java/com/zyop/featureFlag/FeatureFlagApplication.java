package com.zyop.featureFlag;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.TimeZone;

@SpringBootApplication
public class FeatureFlagApplication {

	public static void main(String[] args) {
		// Set JVM timezone to UTC before Spring Boot starts
		// This prevents "invalid value for parameter TimeZone" errors with PostgreSQL
		TimeZone.setDefault(TimeZone.getTimeZone("UTC"));

		SpringApplication.run(FeatureFlagApplication.class, args);
	}
}
