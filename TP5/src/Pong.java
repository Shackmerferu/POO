public class Pong extends VideoJuego{
    public Pong(){
        super("Pongo", 1000, "JuanMa", "+18");
        System.out.println("Se crea un objeto de tipo Pong");
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
