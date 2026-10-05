package br.com.senac.aula04;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Aula 04 - Slide 6 "Primeira Chamada HTTP em Java"
 *
 * Demonstração em sala: consumir uma API pública GRATUITA e SEM AUTENTICAÇÃO
 * (agify.io - a mesma API já usada no Exercício 1 do docx de exercícios).
 *
 * Objetivo didático: mostrar o fluxo completo (HttpClient -> HttpRequest ->
 * .send() -> HttpResponse -> Jackson) antes de complicar com autenticação.
 *
 * Dependência Maven necessária (pom.xml):
 *   <dependency>
 *     <groupId>com.fasterxml.jackson.core</groupId>
 *     <artifactId>jackson-databind</artifactId>
 *     <version>2.17.0</version>
 *   </dependency>
 */
public class PrimeiraChamadaHttp {

    public static void main(String[] args) throws Exception {
        String nome = "joao"; // troque pelo nome que quiser testar ao vivo

        // 1) HttpClient - o "telefone" que faz a ligação
        HttpClient client = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(5))
                .build();

        // 2) HttpRequest - método, URL, headers (aqui não precisa de nenhum)
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://api.agify.io?name=" + nome))
                .GET()
                .build();

        // 3) .send() - envia e espera a resposta chegar como String
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        System.out.println("Status HTTP: " + response.statusCode());
        System.out.println("Corpo bruto: " + response.body());

        // 4) Jackson - extrai só o campo que interessa, sem parsear tudo na mão
        if (response.statusCode() == 200) {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode json = mapper.readTree(response.body());
            int idadePrevista = json.get("age").asInt();
            System.out.println("Idade prevista para '" + nome + "': " + idadePrevista + " anos");
        } else {
            System.out.println("A API não respondeu com sucesso (status " + response.statusCode() + ")");
        }
    }
}
