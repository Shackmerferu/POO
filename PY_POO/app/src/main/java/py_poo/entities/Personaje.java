package py_poo.entities;

import java.awt.Dimension;
import java.awt.Point;
import java.util.List;

public abstract class Personaje extends ObjetoGrafico {
    protected int vidas;
    protected int direccion;
    private List<ObjetoGrafico> entidadesEscena;

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

    public void setEntidadesEscena(List<ObjetoGrafico> entidadesEscena) {
        this.entidadesEscena = entidadesEscena;
    }

    public void crearExplosion(int x, int y) {
        if (entidadesEscena == null) {
            return;
        }

        ObjetoGrafico explosion = new ObjetoGrafico() {
            private final long creadoEn = System.currentTimeMillis();

            {
                setDimension(new Dimension(30, 30));
                setPunto(new Point(x, y));
                setSprite(getRutaExplosion());
            }

            @Override
            public void actualizar() {
                if (System.currentTimeMillis() - creadoEn > 150) {
                    marcarParaEliminar();
                }
            }
        };

        entidadesEscena.add(explosion);
    }

    protected String getRutaExplosion() {
        return null;
    }
}
