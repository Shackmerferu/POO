package py_poo.spaceinvaders;


import py_poo.entities.ObjetoGrafico;
public class Murido extends ObjetoGrafico {
    private long creadoEn;
    public Murido(int X, int Y, int tipo) {
        super();
        this.setDimension(new java.awt.Dimension(30, 30));
        this.setPunto(new java.awt.Point(X, Y));
        
        if (tipo == 1) {
            this.setSprite("imagenes/Space Invaders/Invaders/space__0009_EnemyExplosion.png");
        } else if (tipo ==2) {
            this.setSprite("imagenes/Space Invaders/Invaders/space__0010_PlayerExplosion.png");
        }
        this.creadoEn = System.currentTimeMillis();
    }
    @Override
    public void actualizar() {
        long tiempoActual = System.currentTimeMillis()-creadoEn;
        if (tiempoActual > 150) {
           this.marcarParaEliminar();
        }
    }
    
}
