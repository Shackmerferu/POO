

package tp1;

public class Contacto {
    private String nombre;
    private String numero;


     Contacto(String nombre,String numero){
        this.nombre=nombre;
        this.numero=numero;
    }

    String getNombre(){
         return nombre;
    }
    String getNumero(){
        return numero;
    }

    void setNombre(String nombre){
         this.nombre=nombre;
    }

     void setNumero(String numero) {
        this.numero = numero;
    }
}
