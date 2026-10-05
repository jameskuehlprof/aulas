public class Email extends Notificacao {
    public Email(String destinatario) {
        super(destinatario);
    }
    @Override
    public void enviar() {
        System.out.println("Enviando e-mail...");
    }
}