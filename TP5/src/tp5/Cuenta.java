package tp5;

public abstract class Cuenta {
    protected int nroCuenta;
    protected Cliente titular;
    protected double saldo;

    private static int contador = 1;

    public Cuenta(Cliente titular, double saldo) {
        if (titular == null) {
            throw new IllegalArgumentException("Titular nulo");
        }
        if (saldo < 0) {
            throw new IllegalArgumentException("Saldo inicial inválido");
        }
        this.nroCuenta = contador++;
        this.titular = titular;
        this.saldo = saldo;
    }

    public abstract void depositar(double monto);

    public abstract void extraer(double monto);

    protected abstract double consultarSaldo();

    public final void transferir(double monto, Cuenta destino) {
        if (destino == null) {
            throw new IllegalArgumentException("Cuenta destino nula");
        }
        if (monto <= 0) {
            throw new IllegalArgumentException("Monto inválido");
        }
        this.extraer(monto);
        destino.depositar(monto);
    }

    public Cliente getTitular() {
        return titular;
    }
}