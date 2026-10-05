package br.com.senac.aula06.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

/** Propriedades "visao.*" do application.properties. */
@ConfigurationProperties(prefix = "visao")
public record VisaoProperties(
        Provedor gemini,
        Provedor claude,
        Provedor openai,
        String provedorPadrao,
        int maxTokens,
        int timeoutSegundos) {

    public record Provedor(String modelo) {}
}
