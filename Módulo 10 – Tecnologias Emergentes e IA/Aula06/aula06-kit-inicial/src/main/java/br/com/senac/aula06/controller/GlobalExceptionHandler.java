package br.com.senac.aula06.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.multipart.support.MissingServletRequestPartException;

import br.com.senac.aula06.visao.VisaoException;

/** Erros viram ProblemDetail (RFC 7807) com mensagem util - nunca stack trace. */
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(VisaoException.class)
    public ProblemDetail visao(VisaoException e) {
        HttpStatus status = switch (e.getStatusHttp()) {
            case 400 -> HttpStatus.BAD_REQUEST;
            case 413 -> HttpStatus.PAYLOAD_TOO_LARGE;
            case 415 -> HttpStatus.UNSUPPORTED_MEDIA_TYPE;
            case 501 -> HttpStatus.NOT_IMPLEMENTED;
            case 401, 403, 429, 500, 502, 503, 529 -> HttpStatus.BAD_GATEWAY;
            default -> HttpStatus.INTERNAL_SERVER_ERROR;
        };
        ProblemDetail pd = ProblemDetail.forStatusAndDetail(status, e.getMessage());
        pd.setTitle("Falha na analise da imagem");
        pd.setProperty("statusDoProvedor", e.getStatusHttp());
        return pd;
    }

    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public ProblemDetail grande(MaxUploadSizeExceededException e) {
        ProblemDetail pd = ProblemDetail.forStatus(HttpStatus.PAYLOAD_TOO_LARGE);
        pd.setTitle("Imagem muito grande");
        pd.setDetail("Limite de 8 MB por imagem. Reduza a resolucao da foto antes de enviar.");
        return pd;
    }

    @ExceptionHandler(MissingServletRequestPartException.class)
    public ProblemDetail faltando(MissingServletRequestPartException e) {
        ProblemDetail pd = ProblemDetail.forStatus(HttpStatus.BAD_REQUEST);
        pd.setTitle("Arquivo nao enviado");
        pd.setDetail("Envie o campo '" + e.getRequestPartName() + "' como arquivo (multipart/form-data).");
        return pd;
    }
}
