package tp5;

import java.time.LocalTime;
import java.time.temporal.ChronoUnit;

public abstract class VideoJuego{

    private static int cantidadDeVideoJuegosCreados = 0;
    protected LocalTime fechaDeLanzamiento;
    protected double precio;
    protected String editorial;
    protected String nombre;
    protected short pegi;

    public abstract void jugar();
    public abstract String getNombre();
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    @Override
    public String toString() {
        return "Name:" + nombre + " Lanzamiento:" + this.fechaDeLanzamiento + " Precio(ARS):" + this.precio;
    }   
    //Es polimorfismo estatico
    public VideoJuego(String nombre , double precio, double pegi) {
        this.precio = precio;
        this.nombre = nombre;
        this.fechaDeLanzamiento = LocalTime.now().truncatedTo(ChronoUnit.SECONDS);
        cantidadDeVideoJuegosCreados++;
        this.pegi = (short) pegi;
    }

    public VideoJuego(String nombre, int precio, int pegi) {
        this.nombre = nombre;
        this.precio = precio;
        this.fechaDeLanzamiento = LocalTime.now().truncatedTo(ChronoUnit.SECONDS);
        this.pegi = (short) pegi; 
        cantidadDeVideoJuegosCreados++;
    }

    public VideoJuego(String nombre, String precio, String pegi) {
        this.nombre = nombre;
        this.precio = Double.parseDouble(precio);
        this.fechaDeLanzamiento = LocalTime.now().truncatedTo(ChronoUnit.SECONDS);
        this.pegi = Short.decode(pegi); 
        cantidadDeVideoJuegosCreados++;
    }

    public static int getCantidadDeVideoJuegosCreados() {
        return cantidadDeVideoJuegosCreados;
    }



    public void setFechaDeLanzamiento(LocalTime fechaDeLanzamiento) {
        this.fechaDeLanzamiento = fechaDeLanzamiento;
    }

    public LocalTime getFechaDeLanzamiento() {
        return fechaDeLanzamiento;
    }
}