import java.util.*;

class Produto {
    private String nome;
    private double preco;

    public Produto(String nome, double preco) {
        this.nome = nome;
        this.preco = preco;
    }

    public String getNome() {
        return nome;
    }

    public double getPreco() {
        return preco;
    }

    @Override
    public String toString() {
        return nome + " - R$" + preco;
    }
}

public class OrdenadorProdutos {

    public static List<Produto> ordenarPorPreco(List<Produto> produtos) {
        if (produtos == null || produtos.isEmpty()) {
            System.out.println("Lista nula ou vazia. Nada a ordenar.");
            return Collections.emptyList();
        }

        produtos.sort(Comparator.comparingDouble(Produto::getPreco));
        return produtos;
    }

    public static void main(String[] args) {
        // Teste com lista nula
        List<Produto> listaNula = null;
        System.out.println("Teste lista nula: " + ordenarPorPreco(listaNula));

        // Teste com lista vazia
        List<Produto> listaVazia = new ArrayList<>();
        System.out.println("Teste lista vazia: " + ordenarPorPreco(listaVazia));

        // Teste com lista preenchida
        List<Produto> lista = new ArrayList<>();
        lista.add(new Produto("Notebook", 3500.00));
        lista.add(new Produto("Mouse", 80.00));
        lista.add(new Produto("Monitor", 1200.00));

        System.out.println("Lista ordenada: " + ordenarPorPreco(lista));
    }
}
