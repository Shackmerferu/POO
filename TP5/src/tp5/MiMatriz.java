package tp5;

public class MiMatriz implements Multiplicable{
    @SuppressWarnings("FieldMayBeFinal")
    private int[][] valor;
    
    public MiMatriz(int[][] valor) {
        this.valor = valor;
    }

    @Override
    public void multiplicar(int n) {
        for (int[] valor1 : valor) {
            for (int f = 0; f < valor1.length; f++) {
                valor1[f] *= n;
            }
        }
    }

    @Override
    public void mostrarResultado() {
        for (int[] valor1 : valor) {
            for (int f = 0; f < valor1.length; f++) {
                System.out.println("Resultado matriz:" + valor1[f]);
            }
        }
    }
}
