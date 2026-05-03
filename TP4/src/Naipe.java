public class Naipe {
    private String palo;
    private int numero;
    private static Naipe ultimoNaipe; 
    public Naipe(String palo, int numero){
        this.palo=palo;
        this.numero=numero;
        ultimoNaipe=this;
    }
    public void setPalo(String palo){
        this.palo=palo;
    }
    public void setNum(int numero){
        this.numero=numero;
    }
    public String getPalo(){
        return palo;
    }
    public int getNum(){
        return numero;
    }
     @Override
    public String toString() {
        return numero + " de " + palo;
    }
   
   public static Naipe ultiCard(){
    return ultimoNaipe;
   }
}
