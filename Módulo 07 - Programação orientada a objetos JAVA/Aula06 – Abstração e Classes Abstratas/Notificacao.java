public abstract class Notificacao {
    protected String destinatario;
    public Notificacao(String destinatario) {
        this.destinatario = destinatario;
    }
    public void exibirDestinatario() {
        System.out.println("Destinatário: " + destinatario);
    }
    public abstract void enviar();
}
