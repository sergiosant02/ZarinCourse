package com.learning.api.v1.stocks.configs;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.ExternalDocumentation;


@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI documentation() {
        return new OpenAPI()
                // name of your API project
                .info(new Info().title("Stocks API")
                        .description("Sample Java Spring Boot API using MongoDB Atlas")
                        // this version follows semantic versioning
                        // at the time this codelab was created, the project had been updated to 2.0.0 from 1.0.0
                        // there are breaking changes from the old code base, therefore we increment the major integer from 1 -> 2
                        .version("1.0.0")
                        .contact(new Contact().name("SOME_NAME").url("SOME_URL")))
                .externalDocs(new ExternalDocumentation()
                        .description("Stocks API Documentation")
                        .url("https://github.com/zarinlo/sample-springboot-api#readme"));
    }
}