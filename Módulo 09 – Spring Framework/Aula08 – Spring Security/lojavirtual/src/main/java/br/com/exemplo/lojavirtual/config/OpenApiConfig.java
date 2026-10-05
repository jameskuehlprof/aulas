package br.com.exemplo.lojavirtual.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuração do Swagger/OpenAPI (springdoc-openapi). Depois de rodar a
 * aplicação, a documentação interativa fica disponível em:
 *
 *   http://localhost:8080/swagger-ui.html   (interface visual)
 *   http://localhost:8080/v3/api-docs       (JSON puro da especificação)
 *
 * Isso substitui a antiga prática de manter uma "coleção do Postman"
 * desatualizada à parte: a documentação nasce dos próprios Controllers e
 * DTOs (com a ajuda das anotações @Tag, @Operation etc.), então tende a
 * ficar sempre sincronizada com o código.
 */
@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI lojavirtualOpenAPI() {
        return new OpenAPI().info(new Info()
                .title("API lojavirtual")
                .description("API REST do projeto incremental do Módulo 09 — Spring Framework e Spring Boot")
                .version("v1")
                .contact(new Contact().name("Turma Entra21 — Módulo 09")));
    }
}
