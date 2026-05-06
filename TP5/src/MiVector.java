public class MiVector implements Multiplicable {
    protected int[] vector;
    public MiVector(int[] vector){
        this.vector=vector;
    }
    @Override
    public void multiplicar(int x){
        for(int i=0; i<vector.length;i++){
            vector[i]=vector[i]*x;
        }
    }
     @Override
    public void mostrarResultado(){
      System.out.print("[");
    for(int i = 0; i < vector.length; i++){
        System.out.print(vector[i]);
        if(i < vector.length - 1){
            System.out.print(", ");
        }
    }
    System.out.println("]");
    }
}
