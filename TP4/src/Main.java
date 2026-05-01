

public class Main {
    public static void main(String[] args){

        VideoJuego Jueguito = new VideoJuego("carlos duty", 0);
        VideoJuego Jueguito2 = new VideoJuego("el principe de los serpa", 500);
        VideoJuego Jueguito3 = new VideoJuego("zendaya", 200);
    
        System.out.println(Jueguito);
        System.out.println(Jueguito2);
        System.out.println(Jueguito3);
        System.out.println("Total de juegos creados: " + VideoJuego.getCantidadDeVideoJuegosCreados());
    
    }
}