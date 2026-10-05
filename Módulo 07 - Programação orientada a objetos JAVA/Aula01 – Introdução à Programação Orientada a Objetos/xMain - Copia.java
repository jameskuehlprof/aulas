public class Mainx {
    public static void cadastrarProduto(String nome,
                                        double preco,
                                        int quantidade){
        Produto produto = new Produto();
        produto.nome = nome;
        produto.preco = preco;
        produto.quantidade = quantidade;
        System.out.println(produto.nome);
        System.out.println(produto.preco);
        System.out.println(produto.quantidade);
    }
}