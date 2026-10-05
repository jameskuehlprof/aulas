package br.com.exemplo.lojavirtual.exception;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import br.com.exemplo.lojavirtual.dto.ErroRespostaDTO;

/**
 * @RestControllerAdvice: intercepta exceções lançadas por QUALQUER
 * Controller da aplicação e as transforma em respostas HTTP padronizadas.
 * É aqui, e só aqui, que o "corpo de erro" da API é decidido.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    // 404 — recurso não encontrado
    @ExceptionHandler(RecursoNaoEncontradoException.class)
    public ResponseEntity<ErroRespostaDTO> tratarNaoEncontrado(RecursoNaoEncontradoException ex) {
        ErroRespostaDTO erro = new ErroRespostaDTO(
                HttpStatus.NOT_FOUND.value(), "Não encontrado", ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(erro);
    }

    // 400 — dados inválidos (@Valid falhou em algum campo do @RequestBody)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErroRespostaDTO> tratarValidacao(MethodArgumentNotValidException ex) {
        List<String> detalhes = ex.getBindingResult().getFieldErrors().stream()
                .map(fe -> fe.getField() + ": " + fe.getDefaultMessage())
                .toList();
        ErroRespostaDTO erro = new ErroRespostaDTO(
                HttpStatus.BAD_REQUEST.value(), "Dados inválidos",
                "Um ou mais campos não passaram na validação", detalhes);
        return ResponseEntity.badRequest().body(erro);
    }

    // 400 — regra de negócio violada (ex.: CPF duplicado)
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErroRespostaDTO> tratarRegraDeNegocio(IllegalArgumentException ex) {
        ErroRespostaDTO erro = new ErroRespostaDTO(
                HttpStatus.BAD_REQUEST.value(), "Regra de negócio violada", ex.getMessage());
        return ResponseEntity.badRequest().body(erro);
    }

    // 500 — qualquer outro erro inesperado; nunca mais expomos stack trace cru
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErroRespostaDTO> tratarErroGenerico(Exception ex) {
        ErroRespostaDTO erro = new ErroRespostaDTO(
                HttpStatus.INTERNAL_SERVER_ERROR.value(), "Erro interno",
                "Ocorreu um erro inesperado. Tente novamente mais tarde.");
        return ResponseEntity.internalServerError().body(erro);
    }
}
