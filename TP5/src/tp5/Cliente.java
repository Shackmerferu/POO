package tp5;

public class Cliente {
    private final String Nombre;
    private final String Apellido;
    private final String Direccion;

    public Cliente(String nombre,String apellido,String direccion){
        this.Nombre=nombre;
        this.Apellido=apellido;
        this.Direccion=direccion;
    };

            
    public String getnombre(){
        return this.Nombre;
    }
    public String getapellido(){
        return this.Apellido;
    }
    public String getdireccion(){
        return this.Direccion;
    }
    
}
