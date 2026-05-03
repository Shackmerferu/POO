package tp5;

public class NumeroEntero implements Multiplicable{
    private int valor;
    
    public NumeroEntero(int valor){
        this.valor = valor;
    }

    @Override
    public void multiplicar(int n) {
        this.valor = this.valor * n; 
    }

    @Override
    public void mostrarResultado() {
        System.out.println("Resultado num entero:" + valor);
    }

    public int getvalor(){
        return this.valor;
    }
}
