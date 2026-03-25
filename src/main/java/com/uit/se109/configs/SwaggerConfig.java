package com.uit.se109.configs;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springdoc.core.customizers.OpenApiCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {
  private SecurityScheme createAPIKeyScheme() {
    return new SecurityScheme().type(SecurityScheme.Type.HTTP).bearerFormat("JWT").scheme("bearer");
  }

  @Bean
  OpenAPI openApi() {
    return new OpenAPI()
        .addSecurityItem(new SecurityRequirement().addList("bearerAuth"))
        .components(new Components().addSecuritySchemes("bearerAuth", createAPIKeyScheme()));
  }

  @Bean
  OpenApiCustomizer operationIdCustomizer() {
    return openApi -> {
      openApi
          .getPaths()
          .values()
          .forEach(
              (item) -> {
                item.readOperations()
                    .forEach(
                        operation -> {
                          String operationId = operation.getOperationId();
                          if (operationId != null) {
                            String entityName =
                                operation.getTags().isEmpty()
                                    ? "Unknown"
                                    : operation.getTags().get(0).replaceAll("\\s+", "");
                            operation.setOperationId(operationId.replace("{Resource}", entityName));
                          }
                        });
              });
    };
  }
}
