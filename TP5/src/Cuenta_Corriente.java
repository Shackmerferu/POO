public class Cuenta_Corriente  extends Cuenta{
    private double limite=5000;
    public Cuenta_Corriente (Cliente cliente, double Saldo){
    super(cliente,Saldo);
    System.out.println("Cuenta Corriente de " + cliente.getNombre());
   }
    void Depositar(int monto){
    this.Saldo=Saldo+monto;
   }
   void Extraer(int monto){
    if(monto<=this.Saldo){
        this.Saldo -= monto;
        System.out.println("Extracción exitosa (usando saldo propio).");
    }else if(monto<=(this.Saldo+this.limite)){
        this.Saldo= this.Saldo-monto;
        System.out.println("Extracción con descubierto. Saldo: " + Saldo);
        System.out.println("Te quedan " + (limite + Saldo) + " de crédito.");
    }else{
     System.out.println("Límite excedido.");
    }
}
}
