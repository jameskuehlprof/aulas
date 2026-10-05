package br.com.senac.exercicio1.service;

import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.Duration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.senac.exercicio1.dto.IdadeResponse;

/**
 * Exercicio 1 - "Consumindo uma API publica".
 *
 * Implementa, passo a passo, exatamente o que o enunciado pede:
 *   1) Criar o HttpClient e o HttpRequest
 *   2) Enviar a requisicao com .send()
 *   3) Imprimir o status code e o corpo da resposta
 *   4) Extrair apenas o campo "age" do JSON usando Jackson ObjectMapper
 *
 * A diferenca para a versao "so console" (PrimeiraChamadaHttp.java, da Aula
 * 04) e que aqui a logica mora dentro de um @Service do Spring, chamado por
 * um @RestController - assim o mesmo codigo fica disponivel tambem via
 * Swagger UI, alem de continuar imprimindo tudo no console (System.out),
 * igual ao exercicio original.
 */
@Service
public class AgifyService {

    private static final String API_URL = "https://api.agify.io?name=%s";

    // O Spring Boot ja cria e configura um ObjectMapper (bean) sozinho -
    // aqui so pedimos para ele injetar essa instancia pronta (em vez de
    // fazer "new ObjectMapper()" na mao, como no exemplo so-console).
    private final ObjectMapper objectMapper;

    @Autowired
    public AgifyService(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public IdadeResponse consultarIdade(String nome) throws IOException, InterruptedException {

        // 1) HttpClient - o "telefone" que faz a ligacao
        HttpClient client = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(5))
                .build();

        String nomeCodificado = URLEncoder.encode(nome, StandardCharsets.UTF_8);

        // 1) HttpRequest - metodo, URL (sem headers, a agify.io e publica e nao pede chave)
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(String.format(API_URL, nomeCodificado)))
                .timeout(Duration.ofSeconds(10))
                .GET()
                .build();

        // 2) .send() - envia e espera a resposta chegar como String
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        // 3) Imprime status code e corpo bruto no console, como pede o enunciado
        System.out.println("Status HTTP: " + response.statusCode());
        System.out.println("Corpo bruto: " + response.body());

        Integer idadePrevista = null;
        if (response.statusCode() == 200) {
            // 4) Jackson ObjectMapper - extrai so o campo "age" do JSON
            JsonNode json = objectMapper.readTree(response.body());
            if (json.hasNonNull("age")) {
                idadePrevista = json.get("age").asInt();
            }
        }

        return new IdadeResponse(nome, idadePrevista, response.statusCode(), response.body());
    }
}
