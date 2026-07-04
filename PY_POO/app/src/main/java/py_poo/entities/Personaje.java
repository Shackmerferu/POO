package py_poo.entities;

import java.awt.Dimension;
import java.awt.Point;

public abstract class Personaje extends ObjetoGrafico {
    protected int vidas;
    protected int direccion;

    public void mover() {
    }

    public int getVidas() {
        return this.vidas;
    }

    public void recibirDanio(int cantidad) {
        this.vidas -= cantidad;
    }

    public void setVidas(int vidas) {
        this.vidas = vidas;
    }

    public void agregarVida(int cantidad) {
        this.vidas += cantidad;
    }

    public abstract ObjetoGrafico crearExplosion(int x, int y);

    protected ObjetoGrafico crearExplosionConRuta(int x, int y, String ruta) {
        return new ObjetoGrafico() {
            private final long creadoEn = System.currentTimeMillis();

            {
                setDimension(new Dimension(30, 30));
                setPunto(new Point(x, y));
                setSprite(ruta);
            }

            @Override
            public void actualizar() {
                if (System.currentTimeMillis() - creadoEn > 150) {
                    marcarParaEliminar();
                }
            }
        };
    }
}
