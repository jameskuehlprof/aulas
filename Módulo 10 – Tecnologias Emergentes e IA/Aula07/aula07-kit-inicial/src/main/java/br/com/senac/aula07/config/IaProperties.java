package br.com.senac.aula07.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Propriedades "ia.*" do application.properties.
 *
 * Boa pratica: modelo, provedor, limites e cache ficam em configuracao;
 * as CHAVES ficam em variaveis de ambiente (nunca em arquivo versionado).
 */
@ConfigurationProperties(prefix = "ia")
public record IaProperties(
        String provedor,
        Provedor claude,
        Provedor openai,
        Provedor gemini,
        int maxTokens,
        int timeoutSegundos,
        Historico historico,
        Cache cache) {

    public record Provedor(String modelo) {}

    public record Historico(int tamanho) {}

    public record Cache(boolean ativo, int validadeMinutos) {}
}
