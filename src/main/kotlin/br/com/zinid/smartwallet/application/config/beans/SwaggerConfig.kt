package br.com.zinid.smartwallet.application.config.beans

import io.swagger.v3.oas.models.Components
import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.oas.models.media.BooleanSchema
import io.swagger.v3.oas.models.media.Schema
import io.swagger.v3.oas.models.media.StringSchema
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class SwaggerConfig {

    @Bean
    fun userQueryParametersSerialization() = OpenAPI()
        .components(
            Components()
                .addSchemas(
                    "ParameterMap", Schema<Map<String, String>>()
                        .addProperty("year_month", StringSchema().example("2023-09"))
                        .addProperty("payment_type", StringSchema().example("credit"))
                        .addProperty("tags", StringSchema().example("obra,gasolina"))
                        .addProperty("installments_only", BooleanSchema().example(false))
                        .addProperty("essential", BooleanSchema().example(false))
                        .addProperty("monthly_subscription", BooleanSchema().example(false))

                )
        )
}