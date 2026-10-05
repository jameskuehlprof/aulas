package br.com.senac.aula06.visao;

import java.io.IOException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpTimeoutException;
import java.time.Duration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * HttpClient nativo + Jackson + tratamento de erros - o mesmo ProviderBase
 * da Aula 05, agora reaproveitado pelos provedores de visao.
 */
public abstract class HttpJsonBase implements VisaoProvider {

    protected final Logger log = LoggerFactory.getLogger(getClass());
    protected final ObjectMapper mapper;
    private final HttpClient client;
    private final Duration timeout;

    protected HttpJsonBase(ObjectMapper mapper, int timeoutSegundos) {
        this.mapper = mapper;
        this.timeout = Duration.ofSeconds(timeoutSegundos);
        this.client = HttpClient.newBuilder().connectTimeout(Duration.ofSeconds(10)).build();
    }

    protected String chave(String variavel) {
        String v = System.getenv(variavel);
        if (v == null || v.isBlank()) {
            throw new VisaoException(500, "Variavel de ambiente " + variavel
                    + " nao configurada (Run > Run Configurations > Environment).");
        }
        return v;
    }

    protected JsonNode postJson(HttpRequest.Builder builder, Object corpo) {
        try {
            HttpRequest request = builder.timeout(timeout)
                    .header("content-type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(mapper.writeValueAsString(corpo)))
                    .build();
            HttpResponse<String> response = enviarComRetry(request);
            if (response.statusCode() == 200) {
                return mapper.readTree(response.body());
            }
            log.error("[{}] HTTP {} -> {}", nome(), response.statusCode(), response.body());
            throw new VisaoException(response.statusCode(), mensagem(response.statusCode()));
        } catch (IOException e) {
            throw new VisaoException(502, "Falha de rede ao chamar " + nome() + ": " + e.getMessage());
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new VisaoException(500, "Chamada interrompida");
        }
    }

    /**
     * Status HTTP considerados temporarios: vale a pena tentar de novo.
     * 429 = limite/rate limit; 500/502/503/529 = provedor sobrecarregado
     * ou instavel (comum em modelos recem-lancados ou no free tier).
     */
    private static final java.util.Set<Integer> RETRYAVEIS = java.util.Set.of(429, 500, 502, 503, 529);

    /** Envia a requisicao com 1 retry em timeout e ate 2 retries (backoff) em status transitorio. */
    private HttpResponse<String> enviarComRetry(HttpRequest request) throws IOException, InterruptedException {
        HttpResponse<String> response = null;
        for (int tentativa = 1; tentativa <= 3; tentativa++) {
            try {
                response = client.send(request, HttpResponse.BodyHandlers.ofString());
            } catch (HttpTimeoutException e) {
                log.warn("[{}] timeout na tentativa {}, tentando de novo...", nome(), tentativa);
                continue;
            }
            if (response.statusCode() != 200 && RETRYAVEIS.contains(response.statusCode()) && tentativa < 3) {
                long esperaMs = 500L * tentativa; // backoff simples: 500ms, depois 1000ms
                log.warn("[{}] HTTP {} na tentativa {}, tentando de novo em {} ms...",
                        nome(), response.statusCode(), tentativa, esperaMs);
                Thread.sleep(esperaMs);
                continue;
            }
            return response;
        }
        // ultima tentativa (timeout na 3a vez): deixa estourar para o catch do metodo chamador
        return client.send(request, HttpResponse.BodyHandlers.ofString());
    }

    private String mensagem(int status) {
        return switch (status) {
            case 400 -> "Requisicao invalida para " + nome() + " (imagem corrompida ou JSON errado?).";
            case 401, 403 -> "Chave invalida ou API nao ativada para " + nome() + ".";
            case 404 -> "Modelo/endpoint nao encontrado em " + nome() + ".";
            case 413 -> "Imagem grande demais para " + nome() + ".";
            case 429 -> "Limite de uso ou credito esgotado em " + nome() + ".";
            case 500, 502, 503, 529 -> "Provedor " + nome() + " indisponivel. Tente de novo.";
            default -> "Erro inesperado (" + status + ") em " + nome() + ".";
        };
    }

    /**
     * Modelos multimodais as vezes devolvem o JSON dentro de ```json ... ```.
     * Este metodo limpa isso antes do parse - ESSENCIAL no Desafio (recibo).
     */
    protected static String limparJson(String texto) {
        String t = texto.trim();
        if (t.startsWith("```")) {
            t = t.replaceFirst("^```[a-zA-Z]*\\s*", "").replaceFirst("\\s*```$", "");
        }
        int ini = t.indexOf('{'), fim = t.lastIndexOf('}');
        int iniArr = t.indexOf('['), fimArr = t.lastIndexOf(']');
        if (iniArr >= 0 && (ini < 0 || iniArr < ini)) {
            return t.substring(iniArr, fimArr + 1);
        }
        return ini >= 0 && fim > ini ? t.substring(ini, fim + 1) : t;
    }
}
