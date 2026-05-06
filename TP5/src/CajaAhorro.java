public class CajaAhorro extends Cuenta{
   public CajaAhorro(Cliente cliente, double Saldo){
    super(cliente,Saldo);
    System.out.println("Caja de Ahorro de " + cliente.getNombre());
   }
    void Depositar(int monto){
    this.Saldo=Saldo+monto;
   }
   void Extraer(int monto){
    if(monto<=this.Saldo){
        this.Saldo= Saldo-monto;
    }else{
        System.out.println("Saldo insuficiente para realizar esta operacion");
    }
   }
}
