public class Dados extends VideoJuego {
        public Dados(){
        super("Dadin", 120, "JuanPi", "No se que es pegi");
        System.out.println("Se crea un objeto de tipo Dados");
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


