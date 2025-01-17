package com.librarymanagementsystem.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenApi() {

        return new OpenAPI()
                .info(new Info()
                        .title("**Library Management System API**")
                        .version("1.0")
                        .description("""
                                **This API provides functionalities to efficiently manage a library system**, including:
                                - Operations for handling **books**, **members**, **authors**, **categories**, and **book orders**.
                                - Supports **CRUD operations**, **pagination**, and **search capabilities**.
                                                                
                                ---
                                                                
                                **Contact Information**:
                                - **Name**: Murad Sharifzada
                                - **Email**: [muradsharifzada@gmail.com](mailto:muradsharifzada@gmail.com)
                                - **LinkedIn**: [Murad Sharifzada - LinkedIn](https://linkedin.com/in/muradsharifzada)
                                """
                        ));
    }
}
