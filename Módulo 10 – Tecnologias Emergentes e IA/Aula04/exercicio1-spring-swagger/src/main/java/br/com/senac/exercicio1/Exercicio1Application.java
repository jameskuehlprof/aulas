package br.com.senac.exercicio1;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Modulo 10 - Exercicio 1: Consumindo uma API publica.
 *
 * Ponto de entrada da aplicacao Spring Boot. Ao rodar esta classe (ou
 * "mvn spring-boot:run"), sobe um servidor web embutido na porta 8080 com:
 *   - o endpoint REST definido em br.com.senac.exercicio1.controller.AgifyController
 *   - o Swagger UI, gerado automaticamente, em http://localhost:8080/swagger-ui.html
 */
@SpringBootApplication
public class Exercicio1Application {

    public static void main(String[] args) {
        SpringApplication.run(Exercicio1Application.class, args);
    }
}
