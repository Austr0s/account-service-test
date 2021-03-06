package io.accountservice.test.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;


/**
 * OpenAPI3 configuration class
 * 
 * @author Austr0s
 */
@Configuration
public class OpenApiConfig {
	@Bean
	public OpenAPI customOpenAPI() {
		return new OpenAPI().components(new Components()).info(new Info().title("Account Application API")
				.description("This is a sample Spring Boot RESTfull service using springdoc-openapi and OpenAPI 3."));
	}
}
