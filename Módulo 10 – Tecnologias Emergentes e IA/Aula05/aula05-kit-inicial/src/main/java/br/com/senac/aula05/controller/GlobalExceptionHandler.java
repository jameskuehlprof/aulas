package br.com.senac.aula05.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import br.com.senac.aula05.ia.IaException;

/**
 * Converte excecoes em respostas JSON padronizadas (RFC 7807 - ProblemDetail).
 *
 * Regra de ouro vista na Aula 04: o usuario da API nunca ve stack trace.
 * Ele ve um status HTTP coerente e uma mensagem que explica o que fazer.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(IaException.class)
    public ProblemDetail erroDeIa(IaException e) {
        // 401/429 do provedor viram 502 (Bad Gateway) para o nosso cliente:
        // o problema esta "atras" da nossa API, nao no cliente.
        HttpStatus status = switch (e.getStatusHttp()) {
            case 400 -> HttpStatus.BAD_REQUEST;
            case 401, 403, 429, 500, 502, 503, 529 -> HttpStatus.BAD_GATEWAY;
            default -> HttpStatus.INTERNAL_SERVER_ERROR;
        };
        ProblemDetail pd = ProblemDetail.forStatusAndDetail(status, e.getMessage());
        pd.setTitle("Falha ao consultar o provedor de IA");
        pd.setProperty("statusDoProvedor", e.getStatusHttp());
        return pd;
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ProblemDetail validacao(MethodArgumentNotValidException e) {
        ProblemDetail pd = ProblemDetail.forStatus(HttpStatus.BAD_REQUEST);
        pd.setTitle("Dados de entrada invalidos");
        pd.setDetail(e.getBindingResult().getFieldErrors().stream()
                .map(f -> f.getField() + ": " + f.getDefaultMessage())
                .reduce((a, b) -> a + "; " + b).orElse("erro de validacao"));
        return pd;
    }
}
