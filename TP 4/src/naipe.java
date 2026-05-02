public class naipe {
    private int numero;
    private String palo;
    private static naipe ultimonaipe;


public naipe(int numero, String palo) {
    this.numero = numero;
    this.palo   = palo;
    ultimonaipe = this; // marca el ultimo
}

public static naipe getUltimoNaipe() {
    return ultimonaipe;
}


public int getNumero() {
    return numero; }

public String getPalo()   {
    return palo;
}


public String toString() {
    return numero + " de " + palo;
}

}