package py_poo.entities;

public abstract class Personaje extends ObjetoGrafico {
    protected int vidas;
    protected int direccion;

    public void mover() {
    }

    public void recibirDanio(int cantidad) {
        this.vidas -= cantidad;
    }

    public int getVidas() {
        return vidas;
    }

    public void setVidas(int vidas) {
        this.vidas = vidas;
    }

    public void agregarVida(int cantidad) {
        this.vidas += cantidad;
    }
}
