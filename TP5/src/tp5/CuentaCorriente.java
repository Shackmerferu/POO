package tp5;

public class CuentaCorriente extends Cuenta {

    private final double descubierto;

    public CuentaCorriente(Cliente titular, double saldoInicial, double descubierto) {
        super(titular, saldoInicial);
        this.descubierto = descubierto;
    }

    @Override
    public void depositar(double monto) {
        if (monto > 0) {
            saldo += monto;
        }
    }

    @Override
    public void extraer(double monto) {
        if (monto > 0 && saldo + descubierto >= monto) {
            saldo -= monto;
        }
    }

    @Override
    protected double consultarSaldo() {
        return saldo;
    }
}