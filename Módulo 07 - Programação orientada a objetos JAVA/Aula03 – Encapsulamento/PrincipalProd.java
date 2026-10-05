public class PrincipalProd {
    public static void main(String[] args) {
        // Criando um objeto Produto
        Produto produto = new Produto();
        // Utilizando os setters
        produto.setNome("Notebook");
        produto.setPreco(3500.00);
        // Exibindo os dados utilizando os getters
        System.out.println("Nome: " + produto.getNome());
        System.out.println("Preço: R$ " + produto.getPreco());
    }
}