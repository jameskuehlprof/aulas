package br.com.senac.aula05.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Le as propriedades "ia.*" do application.properties.
 *
 * Boa pratica mostrada em aula: nome do modelo, limite de tokens e timeout
 * ficam em configuracao (facil de trocar sem recompilar), enquanto as CHAVES
 * ficam em variaveis de ambiente (nunca em arquivo versionado no Git).
 */
@ConfigurationProperties(prefix = "ia")
public record IaProperties(
        Provedor claude,
        Provedor openai,
        Provedor gemini,
        int maxTokens,
        int timeoutSegundos,
        Historico historico) {

    public record Provedor(String modelo) {}

    public record Historico(int tamanho) {}
}
