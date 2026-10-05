package br.com.senac.aula07.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import br.com.senac.aula07.dto.Dtos.PerguntaResponse;
import br.com.senac.aula07.dto.Dtos.UsuarioRequest;
import br.com.senac.aula07.dto.Dtos.UsuarioResponse;
import br.com.senac.aula07.service.AtendimentoService;
import br.com.senac.aula07.service.UsuarioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/usuarios")
@Tag(name = "1. Usuarios", description = "Cadastro e historico")
public class UsuarioController {

    private final UsuarioService usuarios;
    private final AtendimentoService atendimento;

    public UsuarioController(UsuarioService usuarios, AtendimentoService atendimento) {
        this.usuarios = usuarios;
        this.atendimento = atendimento;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Cadastra um usuario")
    public UsuarioResponse cadastrar(@Valid @RequestBody UsuarioRequest req) {
        return usuarios.cadastrar(req);
    }

    @GetMapping
    @Operation(summary = "Lista os usuarios")
    public List<UsuarioResponse> listar() {
        return usuarios.listar();
    }

    @GetMapping("/{id}/historico")
    @Operation(summary = "Exercicio 2 - historico de perguntas e respostas do usuario (mais recente primeiro)")
    public List<PerguntaResponse> historico(@PathVariable Long id) {
        return atendimento.historico(id);
    }
}
