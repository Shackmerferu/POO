public class MiMatriz implements Multiplicable {
    protected int[][] matriz;
    public MiMatriz(int[][] matriz){
        this.matriz=matriz;
    }
     @Override
    public void multiplicar(int x){
        for(int i=0; i<matriz.length;i++){
            for(int j=0;j<matriz[i].length;j++){
            matriz[i][j]=matriz[i][j]*x;
        }
        }
    }
     @Override
    public void mostrarResultado(){
         for(int i = 0; i < matriz.length; i++){
        System.out.print("[");
        for(int j = 0; j < matriz[i].length; j++){
            System.out.print(matriz[i][j]);
            if(j < matriz[i].length - 1){
                System.out.print(", ");
            }
        }
        System.out.println("]");
    }
    }
}
