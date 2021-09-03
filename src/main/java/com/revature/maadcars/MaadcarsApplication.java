package com.revature.maadcars;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class MaadcarsApplication {
	private static final Logger logger = LoggerFactory.getLogger(MaadcarsApplication.class);

	public static void main(String[] args) {
		logger.trace("Logging started.");
		SpringApplication.run(MaadcarsApplication.class, args);

	}


}

