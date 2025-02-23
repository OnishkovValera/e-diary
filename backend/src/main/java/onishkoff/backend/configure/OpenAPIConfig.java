package onishkoff.backend.configure;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


import java.util.List;

@Configuration
public class OpenAPIConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI().servers(
                List.of(
                        new Server().url("https://localhost:8080")
                )
        ).info(
                new Info().title("API Documentation").version("1.0")
        );
    }
}
