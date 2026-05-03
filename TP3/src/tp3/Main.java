package tp3;


public class Main {
    public static void main(String[] args){

    VideoJuego jueguito = new VideoJuego("carlos duty", 0);
    VideoJuego jueguito2 = new VideoJuego("el principe de los serpa", 500);
    VideoJuego jueguito3 = new VideoJuego("zendaya", 200);

    // Example usage to avoid "never read" warning
    System.out.println(jueguito);
    System.out.println(jueguito2);
    System.out.println(jueguito3);        
    System.out.println(VideoJuego.getCantidadDeVideoJuegosCreados());
    
    }
}