package br.com.senac.aula05.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.senac.aula05.dto.Dtos.ArenaRequest;
import br.com.senac.aula05.dto.Dtos.ArenaResponse;
import br.com.senac.aula05.dto.Dtos.ChatRequest;
import br.com.senac.aula05.dto.Dtos.EmailCobrancaRequest;
import br.com.senac.aula05.dto.Dtos.ResumoRequest;
import br.com.senac.aula05.ia.RespostaIA;
import br.com.senac.aula05.service.ChatService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

/**
 * Endpoints dos Exercicios 1, 2 e 3 + bonus Arena.
 * Teste tudo pelo Swagger: http://localhost:8080/swagger-ui.html
 */
@RestController
@RequestMapping("/api")
@Tag(name = "1. Chat, Resumo e E-mail", description = "Exercicios 1, 2 e 3 da Aula 05")
public class ChatController {

    private final ChatService service;

    public ChatController(ChatService service) {
        this.service = service;
    }

    @PostMapping("/chat")
    @Operation(summary = "Exercicio 1 - pergunta unica para o provedor escolhido")
    public RespostaIA chat(@Valid @RequestBody ChatRequest req) {
        return service.chat(req.provedor(), req.mensagem());
    }

    @PostMapping("/resumo")
    @Operation(summary = "Exercicio 2 - resume um texto em N linhas")
    public RespostaIA resumo(@Valid @RequestBody ResumoRequest req) {
        return service.resumir(req.provedor(), req.texto(), req.linhas());
    }

    @PostMapping("/email-cobranca")
    @Operation(summary = "Exercicio 3 - gera um e-mail de cobranca a partir de 4 dados")
    public RespostaIA emailCobranca(@Valid @RequestBody EmailCobrancaRequest req) {
        return service.emailCobranca(req.provedor(), req.nomeCliente(), req.valor(), req.prazo(), req.tom());
    }

    @PostMapping("/arena")
    @Operation(summary = "Bonus - mesma pergunta para Claude, ChatGPT e Gemini em paralelo")
    public ArenaResponse arena(@Valid @RequestBody ArenaRequest req) {
        return service.arena(req.mensagem());
    }
}
