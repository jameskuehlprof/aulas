package br.com.senac.aula05.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.senac.aula05.dto.Dtos.AtendimentoRequest;
import br.com.senac.aula05.dto.Dtos.AtendimentoResponse;
import br.com.senac.aula05.ia.MensagemChat;
import br.com.senac.aula05.service.AtendimentoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

/**
 * DESAFIO - assistente de atendimento com personalidade + historico por sessao.
 *
 * O "sessaoId" e qualquer texto escolhido pelo cliente (ex.: "joao-123").
 * Mensagens com o mesmo sessaoId compartilham o historico.
 */
@RestController
@RequestMapping("/api/atendimento")
@Tag(name = "2. Atendimento (Desafio)", description = "Assistente com personalidade e memoria de conversa")
public class AtendimentoController {

    private final AtendimentoService service;

    public AtendimentoController(AtendimentoService service) {
        this.service = service;
    }

    @PostMapping("/{sessaoId}")
    @Operation(summary = "Envia uma mensagem para a assistente 'Lia' dentro de uma sessao")
    public AtendimentoResponse enviar(@PathVariable String sessaoId,
                                      @Valid @RequestBody AtendimentoRequest req) {
        return service.atender(sessaoId, req.provedor(), req.mensagem());
    }

    @GetMapping("/{sessaoId}/historico")
    @Operation(summary = "Mostra o historico guardado da sessao (prova de que a IA 'lembra')")
    public List<MensagemChat> historico(@PathVariable String sessaoId) {
        return service.historico(sessaoId);
    }

    @DeleteMapping("/{sessaoId}")
    @Operation(summary = "Encerra a sessao e apaga o historico")
    public ResponseEntity<Void> encerrar(@PathVariable String sessaoId) {
        service.encerrar(sessaoId);
        return ResponseEntity.noContent().build();
    }
}
