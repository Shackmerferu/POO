import java.time.LocalTime;
import java.util.Date;
public class juego {
    private int cantidaddejuegoscreado;
    private String Nombre;
    protected LocalTime fechadelanzamiento;
    private double precio;

    public void jugar(){

    }
    public String toString(){
        return "Videojuego: " + Nombre +
                ", Fecha de lanzamiento: " + fechadelanzamiento +
                ", Precio: " + precio;
    }

    public void setNombre(String nombre) {
        Nombre = nombre;
    }

    public String getNombre() {
        return Nombre;
    }

    public void setFechadelanzamiento(LocalTime fechadelanzamiento) {
        this.fechadelanzamiento = fechadelanzamiento;
    }

    public LocalTime getFechadelanzamiento() {
        return fechadelanzamiento;
    }
    juego(double precio){
    this.precio=precio;
    }
    juego(int precio){
        this.precio=precio;
    }

    juego(String precio){
        this.precio = Double.parseDouble(precio);
    }

}
