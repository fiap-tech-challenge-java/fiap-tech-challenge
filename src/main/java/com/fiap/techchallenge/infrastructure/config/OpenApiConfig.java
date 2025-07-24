package com.fiap.techchallenge.infrastructure.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        final String securitySchemeName = "bearerAuth"; // Mantém o nome do esquema de segurança

        return new OpenAPI()
                .info(new Info().title("FIAP_TC1").version("1.0.0")
                        .description("API to manage users (restaurant owner and customer)"))

                .components(new Components().addSecuritySchemes(securitySchemeName, new SecurityScheme()
                        .name(securitySchemeName).type(SecurityScheme.Type.HTTP).scheme("bearer").bearerFormat("JWT")
                        .description("Insert the JWT token obtained from the /auth/login endpoint in the format: Bearer {token}")));
    }
}