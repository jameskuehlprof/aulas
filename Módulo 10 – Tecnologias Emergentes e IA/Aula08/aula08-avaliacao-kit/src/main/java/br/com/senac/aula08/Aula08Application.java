package br.com.senac.aula08;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

/**
 * Aula 08 - Avaliacao Final: "TechFix Inteligente".
 *
 *   - Pagina:      http://localhost:8080/
 *   - Swagger UI:  http://localhost:8080/swagger-ui.html
 *   - Banco:       MySQL, schema "techfix" (root/admin) - ver application.properties
 */
@SpringBootApplication
@ConfigurationPropertiesScan
public class Aula08Application {

    public static void main(String[] args) {
        SpringApplication.run(Aula08Application.class, args);
    }
}
