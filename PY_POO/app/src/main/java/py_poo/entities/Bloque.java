package py_poo.entities;

public abstract class Bloque extends ObjetoGrafico {
    protected boolean destruible;
    protected int valor;

    public int getValor() {
        return valor;
    }

    public void recoger() {
    }
}
