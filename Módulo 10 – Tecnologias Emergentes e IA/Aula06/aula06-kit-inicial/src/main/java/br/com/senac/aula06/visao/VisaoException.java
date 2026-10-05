package br.com.senac.aula06.visao;

/** Erro amigavel de visao computacional (mesma ideia da IaException da Aula 05). */
public class VisaoException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    private final int statusHttp;

    public VisaoException(int statusHttp, String mensagem) {
        super(mensagem);
        this.statusHttp = statusHttp;
    }

    public int getStatusHttp() {
        return statusHttp;
    }
}
