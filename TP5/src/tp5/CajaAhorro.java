package tp5;

public class CajaAhorro extends Cuenta {

    public CajaAhorro(Cliente titular, double saldo) {
        super(titular, saldo);
    }

    @Override
    public void depositar(double monto) {
        if (monto <= 0) {
            throw new IllegalArgumentException("Monto inválido");
        }
        saldo += monto;
    }

    @Override
    public void extraer(double monto) {
        if (monto <= 0) {
            throw new IllegalArgumentException("Monto inválido");
        }
        if (saldo < monto) {
            throw new IllegalStateException("Saldo insuficiente");
        }
        saldo -= monto;
    }

    @Override
    protected double consultarSaldo() { return saldo; }
}
