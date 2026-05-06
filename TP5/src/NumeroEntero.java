public class NumeroEntero implements Multiplicable{
    protected int valor;

    public NumeroEntero(int valor){
        this.valor=valor;
    }
    @Override
    public void multiplicar(int x){
        valor=valor*x;
    }
    @Override
    public void mostrarResultado(){
        System.out.println(valor);
    }

}
