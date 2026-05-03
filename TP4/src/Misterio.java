public class Misterio {
    private String nombre;
    private static Misterio misterioso;
    private Misterio(String nombre) {
        this.nombre = nombre;
        System.out.println("Mi nombre es: " + this.nombre);
    }
    public static Misterio getInstancia(String nombre) {
        if (misterioso == null){
            misterioso = new Misterio(nombre);
        }
        else{
            System.out.println("Ya existe una instancia de Misterio, no se puede crear otra.");
        }
        return misterioso;
    }
    public String getNombre(){
        return nombre;
    }
    public static void main(String[] args) {
        Misterio chavito = Misterio.getInstancia("El Chavo del Ocho");
                Misterio ramon = Misterio.getInstancia("Don Ramon");
        System.out.println(ramon.getNombre());
        System.out.println(chavito.getNombre());
    }
}

