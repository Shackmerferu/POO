public abstract class Cuenta{
    protected  int NroCuenta;
    protected Cliente Titular;
    protected double Saldo;
    private static int contadorCuentas = 1000;
    public Cuenta(Cliente Titular, double Saldo){
        this.NroCuenta=generarSiguienteNumero();
        this.Titular=Titular;
        this.Saldo=Saldo;
    }
    abstract void Depositar(int monto);
    abstract void Extraer(int monto);
    private static int generarSiguienteNumero() {
        return ++contadorCuentas;
    }
    protected String consultarSaldo() {
    return "Su saldo disponible es: " + Saldo;
    }
    public void Transferir(int monto, Cuenta destino){
       this.Extraer(monto);
       destino.Depositar(monto);
        System.out.println(">>> Se transfirieron " + monto + " de " + 
                       this.Titular.getNombre() + " a " + 
                       destino.getTitular().getNombre());
    }
    public Cliente getTitular(){
        return this.Titular;
    }
    public int getNroCuenta() {
        return this.NroCuenta;
    }

}
