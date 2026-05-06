public class PiedraPapelTijera extends VideoJuego {
        public PiedraPapelTijera(){
        super("Tijera", 12000, "JuanMe", "+12");
        System.out.println("Se crea un objeto de tipo PiedraPapelTijera");
    }
    @Override
    void jugar(){
        String[] opciones = {"piedra", "papel", "tijera"};
        int indice= (int) (Math.random() * opciones.length);
        this.resultado= opciones[indice]; 
        System.out.println("Vamo a jugar cra");

    }
    @Override
    String getNombre(){
        return this.nombre;
    }
}


