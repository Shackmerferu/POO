public class Main{
    public static void main(String[] args) {

        Pong pong = new Pong();
        PiedraPapelTijera ppt = new PiedraPapelTijera();
        Dados dados = new Dados();

        System.out.println("----- Pong -----");
        System.out.println(pong.getNombre());
        System.out.println(pong);
        pong.jugar();

        System.out.println("----- Piedra Papel Tijera -----");
        System.out.println(ppt.getNombre());
        System.out.println(ppt);
        ppt.jugar();

        System.out.println("----- Dados -----");
        System.out.println(dados.getNombre());
        System.out.println(dados);
        dados.jugar();
    }
}