package it.unitn.healthcore;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @class HealthcoreApplication
 * @brief Main class for the HealthCore application.
 * This class serves as the entry point for the Spring Boot application.
 * It is responsible for bootstrapping the application and starting the embedded server.
 * @author HealthCore Team
 * @version 1.0.0
 * @date 2026-06-11
 */
@SpringBootApplication
public class HealthcoreApplication {

	public static void main(String[] args) {
		SpringApplication.run(HealthcoreApplication.class, args);

	}

}
