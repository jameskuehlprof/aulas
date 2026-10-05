package br.com.exemplo.lojavirtualinterface.filter;

import java.io.IOException;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebFilter;

/**
 * Garante UTF-8 em toda requisição/resposta desta aplicação, para os
 * acentos do português não ficarem corrompidos (ex.: "Relatório"
 * aparecendo como "RelatÃ³rio").
 *
 * Sem este filtro, dois pontos ficam por conta do valor padrão do
 * servidor (que pode não ser UTF-8, dependendo da configuração do
 * Tomcat/JVM no Eclipse):
 *
 *   1) Leitura de parâmetros de request (formulários com nome[],
 *      categoria, cliente etc.) — sem request.setCharacterEncoding("UTF-8")
 *      ANTES de qualquer getParameter(...), o container pode decodificar
 *      o corpo do POST com outro charset, corrompendo nomes com acento
 *      já no momento de salvar (ex.: "Informática" virando "InformÃ¡tica"
 *      no banco, via API).
 *
 *   2) Escrita da resposta (HTML das JSPs) — cada JSP já declara
 *      pageEncoding="UTF-8"/contentType="...charset=UTF-8" na diretiva
 *      page, mas forçar response.setCharacterEncoding("UTF-8") aqui,
 *      antes de qualquer forward, é a forma mais robusta de garantir que
 *      o cabeçalho Content-Type realmente saia com charset=UTF-8 em
 *      TODA resposta (incluindo erro.jsp, redirects etc.), sem depender
 *      só da diretiva de cada página.
 *
 * @WebFilter("/*") aplica a TODAS as URLs desta aplicação — mesmo padrão
 * de anotação (sem entrada em web.xml) já usado nos Servlets do projeto.
 */
@WebFilter("/*")
public class CodificacaoFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");

        chain.doFilter(request, response);
    }
}
