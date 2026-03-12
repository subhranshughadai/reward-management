package com.charter.rewardmanagement.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration class for OpenAPI documentation.
 * Defines API information for the Rewards API.
 */
@Configuration
public class SwaggerConfig {

    /**
     * Creates the OpenAPI configuration bean.
     *
     * @return OpenAPI instance with API details
     */
    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Rewards API")
                        .version("1.0")
                        .description("Customer Rewards Calculation API"));
    }
}