public class Prueba extends PruebaIncompleta {

    @Override
    public void texto2() {
        System.out.println("prueba de");
    }

    @Override
    public void texto3() {
        System.out.println("redefinición de métodos");
    }

    @Override
    public void texto4() {}

    public static void main(String[] args) {
        Prueba p = new Prueba();
        p.mensaje();
    }
}

