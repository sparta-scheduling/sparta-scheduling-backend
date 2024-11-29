package com.sparta.spartascheduling;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class SpartaSchedulingApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpartaSchedulingApplication.class, args);
	}

}
