package com.zyop.featureFlag;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class FeatureFlagApplication {

	public static void main(String[] args) {
		SpringApplication.run(FeatureFlagApplication.class, args);
	}

//	@Bean
//    CommandLineRunner seedTestFlag(FlagRepository flagRepository) {
//		return args -> {
//			flagRepository.save(new Flag("checkout-redesign", "New Checkout Page", true));
//		};
//	}
}
