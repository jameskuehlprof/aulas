package br.com.senac.exercicio1.controller;

import java.io.IOException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.com.senac.exercicio1.dto.IdadeResponse;
import br.com.senac.exercicio1.service.AgifyService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;

/**
 * Exercicio 1 - "Consumindo uma API publica".
 *
 * Expoe a chamada feita pelo AgifyService como um endpoint REST, para poder
 * ser testado direto pelo navegador ou pelo Swagger UI, sem precisar de
 * Postman nem de rodar uma classe com "main" pelo Eclipse.
 *
 * Depois de rodar a aplicacao (mvn spring-boot:run), testar em:
 *   - Swagger UI:     http://localhost:8080/swagger-ui.html
 *   - Chamada direta: http://localhost:8080/api/idade?nome=joao
 */
@RestController
@RequestMapping("/api/idade")
@Tag(name = "Idade (agify.io)", description = "Exercicio 1 - consumindo a API publica agify.io com HttpClient + Jackson")
public class AgifyController {

    private final AgifyService agifyService;

    public AgifyController(AgifyService agifyService) {
        this.agifyService = agifyService;
    }

    @GetMapping
    @Operation(
            summary = "Consulta a idade prevista para um nome",
            description = "Faz uma requisicao GET para https://api.agify.io usando HttpClient nativo, "
                    + "imprime status code e corpo bruto no console do servidor, e extrai o campo "
                    + "'age' do JSON de resposta usando Jackson ObjectMapper."
    )
    public ResponseEntity<IdadeResponse> consultarIdade(
            @Parameter(description = "Nome para consultar a idade prevista", example = "joao")
            @RequestParam(defaultValue = "joao") String nome) {

        try {
            IdadeResponse resposta = agifyService.consultarIdade(nome);

            if (resposta.getAge() == null) {
                // A API respondeu, mas nao com status 200 (ou sem o campo "age")
                return ResponseEntity.status(HttpStatus.BAD_GATEWAY).body(resposta);
            }
            return ResponseEntity.ok(resposta);

        } catch (IOException e) {
            // Falha de rede/conexao ao chamar a api.agify.io
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
                    .body(new IdadeResponse(nome, null, 0, "Erro de conexao: " + e.getMessage()));

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new IdadeResponse(nome, null, 0, "Requisicao interrompida"));
        }
    }
}
