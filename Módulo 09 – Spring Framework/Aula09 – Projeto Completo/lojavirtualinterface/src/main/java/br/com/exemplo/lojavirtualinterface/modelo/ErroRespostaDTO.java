package br.com.exemplo.lojavirtualinterface.modelo;

import java.util.List;

/**
 * Espelha br.com.exemplo.lojavirtual.dto.ErroRespostaDTO (API). Usado
 * pelo ApiClient para extrair uma mensagem legível quando a API responde
 * um erro (400/404/...).
 */
public class ErroRespostaDTO {

    private int status;
    private String erro;
    private String mensagem;
    private List<String> detalhes;

    public ErroRespostaDTO() {
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
