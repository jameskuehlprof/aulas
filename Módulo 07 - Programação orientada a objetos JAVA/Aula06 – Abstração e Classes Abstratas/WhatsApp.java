public class WhatsApp extends Notificacao {
    public WhatsApp(String destinatario) {
        super(destinatario);
    }
    @Override
    public void enviar() {
        System.out.println("Enviando mensagem pelo WhatsApp...");
    }
}