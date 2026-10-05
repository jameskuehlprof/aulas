package br.com.exemplo.lojavirtual.dto;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Formato PADRONIZADO de erro devolvido pela API inteira. Em vez de cada
 * situação de erro devolver um corpo diferente (ou o stack trace cru do
 * Spring), toda a aplicação passa a responder erros neste mesmo formato —
 * o que facilita muito a vida de quem consome a API.
 */
public class ErroRespostaDTO {

    private LocalDateTime timestamp = LocalDateTime.now();
    private int status;
    private String erro;
    private String mensagem;
    private List<String> detalhes;

    public ErroRespostaDTO() {
    }

    public ErroRespostaDTO(int status, String erro, String mensagem) {
        this.status = status;
        this.erro = erro;
        this.mensagem = mensagem;
    }

    public ErroRespostaDTO(int status, String erro, String mensagem, List<String> detalhes) {
        this(status, erro, mensagem);
        this.detalhes = detalhes;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getErro() {
        return erro;
    }

    public void setErro(String erro) {
        this.erro = erro;
    }

    public String getMensagem() {
        return mensagem;
    }

    public void setMensagem(String mensagem) {
        this.mensagem = mensagem;
    }

    public List<String> getDetalhes() {
        return detalhes;
    }

    public void setDetalhes(List<String> detalhes) {
        this.detalhes = detalhes;
    }
}
