package br.com.senac.aula06;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

/**
 * Aula 06 - Visao Computacional em Java Web.
 *
 * Sobe em http://localhost:8080
 *   - Pagina de testes (upload): http://localhost:8080/
 *   - Swagger UI:                http://localhost:8080/swagger-ui.html
 */
@SpringBootApplication
@ConfigurationPropertiesScan
public class Aula06Application {

    public static void main(String[] args) {
        SpringApplication.run(Aula06Application.class, args);
    }
}
