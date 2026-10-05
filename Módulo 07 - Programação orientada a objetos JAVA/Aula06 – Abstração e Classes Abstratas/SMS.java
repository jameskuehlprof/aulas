public class SMS extends Notificacao {
    public SMS(String destinatario) {
        super(destinatario);
    }
    @Override
    public void enviar() {
        System.out.println("Enviando SMS...");
    }
}