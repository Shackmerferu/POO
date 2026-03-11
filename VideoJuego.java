import java.time.LocalTime;

public class VideoJuego{
    private int cantidadDeVideoJuegosCreados;
    private String nombre;
    private LocalTime fechaDeLanzamiento;
    private double precio;

    public void jugar(){System.out.println("A jugar!!!");}
    
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    @Override
    public String toString() {
        return "Name:" + nombre + "Cantidad:" + cantidadDeVideoJuegosCreados + "Lanzamiento:" + fechaDeLanzamiento + "Precio(ARS):" + precio;
    }

    public String getNombre() {
        return nombre;
    }
    public void setFechaDeLanzamiento(LocalTime fechaDeLanzamiento) {
        this.fechaDeLanzamiento = fechaDeLanzamiento;
    }

    public LocalTime getFechaDeLanzamiento() {
        return fechaDeLanzamiento;
    }

}