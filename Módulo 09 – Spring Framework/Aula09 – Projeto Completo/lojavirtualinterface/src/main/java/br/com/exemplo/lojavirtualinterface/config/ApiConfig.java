package br.com.exemplo.lojavirtualinterface.config;

/**
 * Endereco da API lojavirtual (projeto Spring Boot do Modulo 09, pasta
 * irma desta) e credenciais do usuario de teste usado nas chamadas
 * autenticadas (POST/PUT/DELETE - ver SecurityConfig do lojavirtual:
 * GET e publico, escrita exige HTTP Basic).
 *
 * A API continua na porta 8080 (padrao do Spring Boot, sem nenhuma
 * alteracao) - e a mesma porta que o Swagger UI e a collection do
 * Postman ja usam, entao elas continuam funcionando normalmente. Por
 * isso ESTE projeto (a interface grafica) precisa rodar em um Tomcat
 * numa porta DIFERENTE (ex.: 8081) - ver o passo a passo desta aula
 * para como configurar isso no Eclipse (Servers view > porta HTTP).
 *
 * Credenciais em texto puro aqui so' porque e' o mesmo usuario de teste
 * fixo criado pelo data.sql do lojavirtual (Aula 08) - nunca faca isso
 * com credenciais reais; numa aplicacao de verdade isso viria de uma
 * tela de login + variavel de ambiente/secret manager, nao de uma
 * constante no codigo.
 */
public final class ApiConfig {

    public static final String BASE_URL = "http://localhost:8080";

    public static final String USUARIO = "admin";
    public static final String SENHA = "123456";

    private ApiConfig() {
    }
}
