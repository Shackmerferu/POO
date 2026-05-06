public class Cliente {
    private String nombre;
    private String apellido;
    private String Direccion;
    public Cliente(String nombre, String apellido, String Direccion){
        this.nombre=nombre;
        this.apellido=apellido;
        this.Direccion=Direccion;
    }
    public String getNombre(){
        return this.nombre;
    }
    public String getApellido(){
        return this.apellido;
    }
    public String getDireccion(){
        return this.Direccion;
    }
}
    