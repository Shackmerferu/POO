package tp5;

public class MiVector implements Multiplicable{
    @SuppressWarnings("FieldMayBeFinal")
    private int[] valor;
    
    public MiVector(int[] valor) {
        this.valor = valor;
    }

    @SuppressWarnings("unused")
    @Override
    public void multiplicar(int n) {
        for(int valores:valor){
            valores *=n;
        }
    }

    @Override
    public void mostrarResultado() {
        for (int valores : valor) {
            System.out.println("Resultado vector:" + valores);            
        }   
    }
}
