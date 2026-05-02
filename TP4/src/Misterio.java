public class Misterio {
    private final String nombre;
    private static Misterio misterioso;

    private Misterio(String nombre) {
        this.nombre = nombre;
        System.out.println("Mi nombre es: " + this.nombre);
    }

    public static Misterio getInstancia(String nombre) {
        /*
        genera una carga del mismo objeto una vez que se llamo por primera vez , en este caso al cargar el chavo del 8 
        se genera un estado de ese objeto y cuando vuelve a ser llamado genera un nuevo estado del mismo objeto por eso 
        se llama "..."
        */
        
        if (misterioso == null) {
            misterioso = new Misterio(nombre);
        } else {
            System.out.println("...");
        }
        return misterioso;
    }

    public String getNombre() {
        return nombre;
    }

    public static void main(String[] args) {
        Misterio ramon = Misterio.getInstancia("Don Ramon");
        Misterio chavito = Misterio.getInstancia("El Chavo del Ocho");

        System.out.println(ramon.getNombre());
        System.out.println(chavito.getNombre());
    }
}