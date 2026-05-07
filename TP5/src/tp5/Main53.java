package tp5;
import java.util.Vector;

public class Main53 {
    public static void main(String[] args) {
        Vector<VideoJuego> juegos = new Vector<>();

        juegos.add(new Dados("Dados",100,(short)1));
        juegos.add(new PiedraPapelTijeras("PPT",0,(short)1));

        for (VideoJuego v : juegos) {
            v.jugar();
            v.mostrarResultado();
        }
    }
}
