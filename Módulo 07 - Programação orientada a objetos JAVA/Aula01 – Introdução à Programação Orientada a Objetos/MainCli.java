public class MainCli {
    public static void main(String[] args) {
        Cliente c1 = new Cliente();
        c1.nome = "James";
        c1.email = "james.kuehl@prof.sc.senac.br";
        c1.cidade = "Timbó";                
        System.out.println("======== Cliente 1 ========");
        System.out.println("Nome: " + c1.nome);
        System.out.println("E-mail " + c1.email);
        System.out.println("Cidade: " + c1.cidade);
        System.out.println();
        
    }
}