import java.time.LocalTime;

abstract class VideoJuego {
    protected String nombre;
    protected LocalTime fechaDeLanzamiento;
    protected String editorial;
    protected double precio;
    protected String pegi;

    public VideoJuego(String nombre, double precio, String editorial,String pegi){
        this.nombre=nombre;
        this.precio=precio;
        this.editorial=editorial;
        this.fechaDeLanzamiento = LocalTime.now();
        this.pegi=pegi;
    }
    public VideoJuego(String nombre , int precio,  String editorial,String pegi){
        this.precio=precio;
        this.fechaDeLanzamiento = LocalTime.now();
        this.pegi=pegi;
        this.editorial=editorial;
    }
     public VideoJuego(String nombre , String precio,  String editorial,String pegi){
        this.precio = Double.parseDouble(precio);
        this.fechaDeLanzamiento = LocalTime.now();
        this.pegi=pegi;
        this.editorial=editorial;
    }
    abstract void jugar();
    abstract String getNombre();
    @Override
    public String toString() {
        return "Name:" + nombre + " Lanzamiento:" + this.fechaDeLanzamiento + " Precio(ARS):" + this.precio +"| Editorial: "+ this.editorial +"Pegi"+ this.pegi;
    }
}
