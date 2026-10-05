public class PrincipalNotif 
{
    public static void main(String[] args) {
        Email email = new Email("joao@email.com");
        email.exibirDestinatario();
        email.enviar();
        System.out.println();
        WhatsApp whatsapp = new WhatsApp("(47)99999-9999");
        whatsapp.exibirDestinatario();
        whatsapp.enviar();
        System.out.println();
        SMS S = new SMS("(47)99999-9999");
        S.exibirDestinatario();
        S.enviar();
    }}