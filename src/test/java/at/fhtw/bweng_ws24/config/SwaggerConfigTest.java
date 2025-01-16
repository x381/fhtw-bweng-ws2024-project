package at.fhtw.bweng_ws24.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.junit.jupiter.api.Test;
import org.springdoc.core.models.GroupedOpenApi;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class SwaggerConfigTest {

    @Test
    public void testPublicApiBeanConfiguration() {
        // Arrange
        SwaggerConfig swaggerConfig = new SwaggerConfig();

        // Act
        GroupedOpenApi groupedOpenApi = swaggerConfig.publicApi();

        // Assert
        assertEquals("public-api", groupedOpenApi.getGroup());
    }

    @Test
    public void testCustomOpenAPIBeanConfiguration() {
        // Arrange
        SwaggerConfig swaggerConfig = new SwaggerConfig();

        // Act
        OpenAPI openAPI = swaggerConfig.customOpenAPI();

        // Assert
        Info info = openAPI.getInfo();
        assertEquals("User API", info.getTitle());
        assertEquals("1.0.0", info.getVersion());

        SecurityScheme securityScheme = openAPI.getComponents().getSecuritySchemes().get("bearerAuth");
        assertEquals(SecurityScheme.Type.HTTP, securityScheme.getType());
        assertEquals("bearer", securityScheme.getScheme());
        assertEquals("JWT", securityScheme.getBearerFormat());
    }
}