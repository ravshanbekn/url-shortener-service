package com.urlshortenerservice;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
@EnableJpaRepositories
@OpenAPIDefinition(info = @Info(
        title = "Url Shortener Service",
        version = "1.0",
        description = "Url Shortener Service API"
))
public class UrlShortenerServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(UrlShortenerServiceApplication.class, args);
    }
}
