package br.com.exemplo.lojavirtualinterface.client;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.Base64;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import br.com.exemplo.lojavirtualinterface.config.ApiConfig;
import br.com.exemplo.lojavirtualinterface.modelo.ErroRespostaDTO;

/**
 * Cliente HTTP para a API REST do lojavirtual. E' a UNICA classe deste
 * projeto que sabe falar HTTP/JSON com a API - os Servlets nunca chamam
 * java.net.http diretamente, do mesmo jeito que, no sistemacad
 * (Modulo 08), so' o DAO falava SQL direto com o banco. Muda a "fonte
 * da verdade": la' era um banco de dados local, aqui e' uma API REST.
 *
 *   Servlet  --chama-->  ApiClient  --HTTP/JSON-->  API lojavirtual (Spring Boot)
 *
 * Metodos GET nao exigem autenticacao (a API deixa GET publico);
 * POST/PUT/DELETE enviam HTTP Basic com o usuario de ApiConfig, porque a
 * API exige um usuario autenticado para qualquer escrita (SecurityConfig
 * da Aula 8).
 */
public final class ApiClient {

    private static final HttpClient HTTP = HttpClient.newBuilder()
            .connectTimeout(Duration.ofSeconds(5))
            .build();

    private static final ObjectMapper JSON = new ObjectMapper()
            .registerModule(new JavaTimeModule());

    private ApiClient() {
    }

    public static <T> T get(String caminho, Class<T> tipoResposta) throws ApiException {
        return desserializar(enviar("GET", caminho, null, false), tipoResposta);
    }

    public static <T> T get(String caminho, TypeReference<T> tipoResposta) throws ApiException {
        return desserializar(enviar("GET", caminho, null, false), tipoResposta);
    }

    public static <T> T post(String caminho, Object requestBody, Class<T> tipoResposta) throws ApiException {
        return desserializar(enviar("POST", caminho, requestBody, true), tipoResposta);
    }

    public static <T> T put(String caminho, Object requestBody, Class<T> tipoResposta) throws ApiException {
        return desserializar(enviar("PUT", caminho, requestBody, true), tipoResposta);
    }

    public static void delete(String caminho) throws ApiException {
        enviar("DELETE", caminho, null, true);
    }

    // ------------------------------------------------------------------

    private static String enviar(String metodo, String caminho, Object requestBody, boolean autenticado)
            throws ApiException {
        try {
            HttpRequest.Builder builder = HttpRequest.newBuilder()
                    .uri(URI.create(ApiConfig.BASE_URL + caminho))
                    .timeout(Duration.ofSeconds(10))
                    .header("Accept", "application/json");

            if (autenticado) {
                builder.header("Authorization", "Basic " + credenciais());
            }

            if (requestBody != null) {
                String json = JSON.writeValueAsString(requestBody);
                builder.header("Content-Type", "application/json")
                        .method(metodo, HttpRequest.BodyPublishers.ofString(json, StandardCharsets.UTF_8));
            } else {
                builder.method(metodo, HttpRequest.BodyPublishers.noBody());
            }

            HttpResponse<String> resposta = HTTP.send(builder.build(),
                    HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8));

            if (resposta.statusCode() >= 200 && resposta.statusCode() < 300) {
                return resposta.body();
            }

            throw new ApiException(resposta.statusCode(), mensagemDeErro(resposta.statusCode(), resposta.body()));

        } catch (IOException e) {
            throw new ApiException(0, "Não foi possível conectar à API lojavirtual em " + ApiConfig.BASE_URL
                    + ". Verifique se ela está em execução (Run As > Spring Boot App).");
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new ApiException(0, "Requisição para a API foi interrompida.");
        }
    }

    private static String mensagemDeErro(int status, String corpoResposta) {
        try {
            ErroRespostaDTO erro = JSON.readValue(corpoResposta, ErroRespostaDTO.class);
            if (erro.getDetalhes() != null && !erro.getDetalhes().isEmpty()) {
                return erro.getMensagem() + " (" + String.join("; ", erro.getDetalhes()) + ")";
            }
            return erro.getMensagem() != null ? erro.getMensagem() : erro.getErro();
        } catch (Exception e) {
            // Corpo nao veio no formato ErroRespostaDTO (ex.: 401 do Spring
            // Security tem um corpo diferente) - cai para uma mensagem generica.
            return (status == 401)
                    ? "Não autorizado pela API (usuário/senha inválidos em ApiConfig)."
                    : "A API respondeu com status " + status + ".";
        }
    }

    private static String credenciais() {
        String texto = ApiConfig.USUARIO + ":" + ApiConfig.SENHA;
        return Base64.getEncoder().encodeToString(texto.getBytes(StandardCharsets.UTF_8));
    }

    private static <T> T desserializar(String json, Class<T> tipo) throws ApiException {
        if (tipo == Void.class || json == null || json.isBlank()) {
            return null;
        }
        try {
            return JSON.readValue(json, tipo);
        } catch (IOException e) {
            throw new ApiException(0, "A API respondeu em um formato inesperado: " + e.getMessage());
        }
    }

    private static <T> T desserializar(String json, TypeReference<T> tipo) throws ApiException {
        try {
            return JSON.readValue(json, tipo);
        } catch (IOException e) {
            throw new ApiException(0, "A API respondeu em um formato inesperado: " + e.getMessage());
        }
    }
}
