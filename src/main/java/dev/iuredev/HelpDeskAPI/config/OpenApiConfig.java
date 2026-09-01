package dev.iuredev.HelpDeskAPI.config;

import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI helpDeskOpenApi() {
        return new OpenAPI()
                .info(new Info()
                        .title("Help Desk API")
                        .version("1.0.0")
                        .description("API REST para gerenciamento de chamados de suporte técnico.")
                        .contact(new Contact()
                                .name("Iure Xavier")
                                .url("https://github.com/iuxie")))
                .externalDocs(new ExternalDocumentation()
                        .description("Repositório do projeto")
                        .url("https://github.com/iuxie/help-desk-api"));
    }
}
