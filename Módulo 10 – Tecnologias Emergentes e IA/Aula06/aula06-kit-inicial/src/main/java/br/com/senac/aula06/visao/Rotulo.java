package br.com.senac.aula06.visao;

/**
 * Um rotulo ("label") identificado na imagem, com a confianca de 0 a 1.
 * Ex.: ("cachorro", 0.97), ("grama", 0.81).
 */
public record Rotulo(String nome, double confianca) {}
