public class Produto {
    // Atributos privados
    private String nome;
    private double preco;
    // Getter do nome
    public String getNome() {
        return nome;
    }
    // Setter do nome
    public void setNome(String nome) {
        this.nome = nome;
    }
    // Getter do preço
    public double getPreco() {
        return preco;
    }
    // Setter do preço
    public void setPreco(double preco) {
        if (preco >= 0) {
            this.preco = preco;
        } else {
            System.out.println("O preço não pode ser negativo.");
        }
    }}