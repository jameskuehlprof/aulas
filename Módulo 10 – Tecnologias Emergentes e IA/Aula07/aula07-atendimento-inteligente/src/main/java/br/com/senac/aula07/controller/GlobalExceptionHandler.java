package br.com.senac.aula07.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import br.com.senac.aula07.ia.IaException;
import br.com.senac.aula07.service.UsuarioService.ConflitoException;

/** Erros viram ProblemDetail (RFC 7807). Nunca stack trace para o cliente. */
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(RecursoNaoEncontradoException.class)
    public ProblemDetail naoEncontrado(RecursoNaoEncontradoException e) {
        return ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, e.getMessage());
    }

    @ExceptionHandler(ConflitoException.class)
    public ProblemDetail conflito(ConflitoException e) {
        return ProblemDetail.forStatusAndDetail(HttpStatus.CONFLICT, e.getMessage());
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

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ProblemDetail jsonInvalido(HttpMessageNotReadableException e) {
        ProblemDetail pd = ProblemDetail.forStatus(HttpStatus.BAD_REQUEST);
        pd.setTitle("JSON invalido");
        pd.setDetail("Nao foi possivel ler o corpo da requisicao. Confira o JSON e a codificacao (UTF-8).");
        return pd;
    }

    /**
     * Normalmente a IaException e tratada no Service (fallback). Se escapar
     * (ex.: provedor mal configurado), ainda assim vira uma resposta clara.
     */
    @ExceptionHandler(IaException.class)
    public ProblemDetail ia(IaException e) {
        ProblemDetail pd = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_GATEWAY, e.getMessage());
        pd.setTitle("Falha ao consultar o provedor de IA");
        pd.setProperty("statusDoProvedor", e.getStatusHttp());
        return pd;
    }
}
