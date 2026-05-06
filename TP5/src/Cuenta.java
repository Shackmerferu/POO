abstract class Cuenta{
    protected  int NroCuenta;
    protected String Titular;
    protected double Saldo;

    abstract void Depositar(int monto);
    abstract void Extraer(int monto);
    
    protected String consultarSaldo() {
    return "Su saldo disponible es: " + Saldo;
    }
    public void Transferir(int monto, int destino){

    }
}
