package br.com.senac.aula05;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

/**
 * Aula 05 - Arena de IA Generativa (Claude x ChatGPT x Gemini).
 *
 * Sobe em http://localhost:8080
 *   - Pagina Arena (chat web): http://localhost:8080/
 *   - Swagger UI:              http://localhost:8080/swagger-ui.html
 */
@SpringBootApplication
@ConfigurationPropertiesScan
public class Aula05Application {

    public static void main(String[] args) {
        SpringApplication.run(Aula05Application.class, args);
    }
}
