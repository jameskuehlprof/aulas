package br.com.senac.aula07;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

/**
 * Aula 07 - Sistema de Atendimento Inteligente (Spring Boot + JPA + IA).
 *
 *   - Pagina:      http://localhost:8080/
 *   - Swagger UI:  http://localhost:8080/swagger-ui.html
 *
 * Banco padrao: MySQL (crie antes: CREATE DATABASE atendimento CHARACTER SET utf8mb4;
 * usuario/senha em application.properties). Sem MySQL instalado? Rode com
 * --spring.profiles.active=h2 (banco em memoria, sem instalar nada; console em
 * http://localhost:8080/h2-console, JDBC URL jdbc:h2:mem:atendimento, user sa).
 */
@SpringBootApplication
@ConfigurationPropertiesScan
public class Aula07Application {

    public static void main(String[] args) {
        SpringApplication.run(Aula07Application.class, args);
    }
}
