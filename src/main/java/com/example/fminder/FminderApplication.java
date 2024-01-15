package com.example.fminder;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class FminderApplication {

	public static void main(String[] args) {
		SpringApplication.run(FminderApplication.class, args);
	}

}
