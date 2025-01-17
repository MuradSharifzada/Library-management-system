package com.librarymanagementsystem.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenApi() {

        return new OpenAPI()
                .info(new Info()
                        .title("Library Management System API")
                        .version("1.0")
                        .description("This API provides functionalities to efficiently manage a library system," +
                                " including operations for handling books, members, authors, categories, and book orders."
                        )

                        .contact(new Contact()
                                .name("Murad Sharifzada")
                                .email("muradsharifzada@gmail.com")
                        ));
    }
}
