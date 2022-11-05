package com.example.businessLogic;

import com.example.businessLogic.dtos.lessor.LessorPostDto;
import com.example.businessLogic.services.interfaces.LessorService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class BusinessLogicApplication {

	public static void main(String[] args) {
		SpringApplication.run(BusinessLogicApplication.class, args);
	}

	@Bean
	CommandLineRunner run(LessorService lessorService) {
		return args -> {
			lessorService.create(new LessorPostDto("lessorA", "lessor1@email.com", "+380987654561"));
			lessorService.create(new LessorPostDto("lessorB", "lessor2@email.com", "+380987654562"));
			lessorService.create(new LessorPostDto("lessorC", "lessor3@email.com", "+380987654563"));
		};
	}

}
