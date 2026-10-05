package br.com.exemplo.lojavirtual.exception;

/**
 * Exceção lançada sempre que um recurso (Produto, Cliente, etc.) buscado por
 * id não existe. É uma RuntimeException (não checada) para não poluir as
 * assinaturas dos métodos do Service com "throws".
 *
 * Quem transforma esta exceção em uma resposta HTTP 404 é o
 * GlobalExceptionHandler — o Controller não trata nada disso.
 */
public class RecursoNaoEncontradoException extends RuntimeException {
	private static final long serialVersionUID = 1L;
    public RecursoNaoEncontradoException(String mensagem) {
        super(mensagem);
    }
}
