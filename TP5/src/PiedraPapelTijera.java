public class PiedraPapelTijera extends VideoJuego {
        public PiedraPapelTijera(){
        super("Tijera", 12000, "JuanMe", "No se que es pegi");
        System.out.println("Se crea un objeto de tipo PiedraPapelTijera");
    }
    @Override
    void jugar(){
        System.out.println("Vamo a jugar cra");

    }
    @Override
    String getNombre(){
        return this.nombre;
    }
}


