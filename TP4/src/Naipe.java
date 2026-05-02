enum Palo {
    CORAZONES, DIAMANTES, TREBOLES, PICAS, COMODIN
}

public class Naipe {
    private final Palo palo;
    private final short valor;
    private static Naipe ultimo;

    public Naipe(Palo palo, short valor) {
        this.palo = palo;
        this.valor = valor;
        ultimo = this;
    }

    public Palo getPalo() { return palo; }
    public int getValor() { return valor; }

    public static Naipe getUltimo() { return ultimo; }

    @Override
    public String toString() {
        if (palo == Palo.COMODIN) return "COMODIN";
        return valor + " de " + palo;
    }
}