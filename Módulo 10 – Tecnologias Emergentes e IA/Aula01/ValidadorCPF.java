public class ValidadorCPF {

    public static boolean validarCPF(String cpf) {
        // Remove caracteres não numéricos
        cpf = cpf.replaceAll("\\D", "");

        // Verifica se tem 11 dígitos
        if (cpf.length() != 11) return false;

        // Verifica se todos os dígitos são iguais (ex: 11111111111)
        if (cpf.matches("(\\d)\\1{10}")) return false;

        try {
            // Cálculo do primeiro dígito verificador
            int soma = 0;
            for (int i = 0; i < 9; i++) {
                soma += (cpf.charAt(i) - '0') * (10 - i);
            }
            int primeiroDigito = 11 - (soma % 11);
            if (primeiroDigito >= 10) primeiroDigito = 0;

            // Cálculo do segundo dígito verificador
            soma = 0;
            for (int i = 0; i < 10; i++) {
                soma += (cpf.charAt(i) - '0') * (11 - i);
            }
            int segundoDigito = 11 - (soma % 11);
            if (segundoDigito >= 10) segundoDigito = 0;

            // Verifica se os dígitos calculados batem com os informados
            return (primeiroDigito == (cpf.charAt(9) - '0')) &&
                   (segundoDigito == (cpf.charAt(10) - '0'));

        } catch (Exception e) {
            return false;
        }
    }

    // Exemplo de uso
    public static void main(String[] args) {
        System.out.println(validarCPF("00413822931")); // true
        System.out.println(validarCPF("11111111111")); // false
        System.out.println(validarCPF("12345678909")); // false
    }
}