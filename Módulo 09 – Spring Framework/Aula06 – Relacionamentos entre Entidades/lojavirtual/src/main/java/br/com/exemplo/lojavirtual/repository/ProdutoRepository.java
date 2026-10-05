package br.com.exemplo.lojavirtual.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.exemplo.lojavirtual.model.Produto;

/**
 * Antes (Aulas 2 e 3): uma classe @Repository com uma List em memória e
 * métodos escritos à mão (findAll, findById, save, deleteById).
 *
 * Agora: basta uma INTERFACE que estende JpaRepository<Produto, Long>.
 *   Produto -> a entidade;
 *   Long    -> o tipo do id.
 *
 * O Spring Data JPA gera a implementação automaticamente, em tempo de
 * execução — findAll(), findById(), save() e deleteById() já vêm prontos,
 * agora conversando de verdade com o banco MySQL (não mais com uma lista).
 *
 * findByNomeContainingIgnoreCase é uma QUERY DERIVADA: o Spring Data lê o
 * NOME do método e monta o SQL sozinho (aqui, um "LIKE %nome%" que ignora
 * maiúsculas/minúsculas) — sem escrever nenhuma linha de SQL.
 */
public interface ProdutoRepository extends JpaRepository<Produto, Long> {

    List<Produto> findByNomeContainingIgnoreCase(String nome);
}
