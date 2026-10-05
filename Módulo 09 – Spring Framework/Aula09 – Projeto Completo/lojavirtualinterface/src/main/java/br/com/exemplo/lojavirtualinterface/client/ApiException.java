package br.com.exemplo.lojavirtualinterface.client;

/**
 * Erro ao chamar a API lojavirtual: tanto erro "de negocio" (400, 404,
 * respondido pelo GlobalExceptionHandler da API) quanto erro de
 * infraestrutura (API fora do ar, timeout). status = 0 identifica esse
 * segundo caso, sem codigo HTTP nenhum.
 */
public class ApiException extends Exception {

    private static final long serialVersionUID = 1L;

    private final int status;

    public ApiException(int status, String mensagem) {
        super(mensagem);
        this.status = status;
    }

    public int getStatus() {
        return status;
    }
}
