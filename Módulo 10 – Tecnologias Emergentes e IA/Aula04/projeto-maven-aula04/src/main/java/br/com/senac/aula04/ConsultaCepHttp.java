package br.com.senac.aula04;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.Scanner;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Aula 04 - Exemplo extra: consulta de CEP com a API pública ViaCEP.
 *
 * Mesma ideia do PrimeiraChamadaHttp (GET, sem autenticacao), mas com um
 * detalhe importante para reforcar o topico "tratar erros como profissional"
 * dos exercicios: a ViaCEP responde com status 200 MESMO quando o CEP nao
 * existe - o erro vem dentro do JSON ({"erro": true}), nao no status HTTP.
 * Isso mostra pra turma que confiar so no status code as vezes nao basta.
 *
 * Endpoint: https://viacep.com.br/ws/{cep}/json/
 */
public class ConsultaCepHttp {

    private static final String URL_BASE = "https://viacep.com.br/ws/%s/json/";

    public static void main(String[] args) throws Exception {
        String cep;
        if (args.length > 0) {
            cep = args[0];
        } else {
            Scanner scanner = new Scanner(System.in);
            System.out.print("Digite o CEP (somente numeros, ex.: 01310930): ");
            cep = scanner.nextLine().trim();
            scanner.close();
        }

        HttpClient client = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(5))
                .build();

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(String.format(URL_BASE, cep)))
                .timeout(Duration.ofSeconds(10))
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        System.out.println("Status HTTP: " + response.statusCode());

        if (response.statusCode() != 200) {
            System.out.println("A API nao respondeu com sucesso (status " + response.statusCode() + ")");
            return;
        }

        ObjectMapper mapper = new ObjectMapper();
        JsonNode json = mapper.readTree(response.body());

        // Detalhe importante: CEP invalido/inexistente ainda vem com status 200,
        // so muda o conteudo do JSON (campo "erro": true).
        if (json.has("erro") && json.get("erro").asBoolean()) {
            System.out.println("CEP '" + cep + "' nao encontrado (a API respondeu 200, mas o corpo indica erro).");
            return;
        }

        System.out.println("--- Endereco encontrado ---");
        System.out.println("Logradouro: " + json.path("logradouro").asText());
        System.out.println("Bairro:     " + json.path("bairro").asText());
        System.out.println("Cidade/UF:  " + json.path("localidade").asText() + "/" + json.path("uf").asText());
    }
}
