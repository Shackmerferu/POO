package py_poo.spaceinvaders;

import py_poo.entities.ObjetoGrafico;
import java.awt.Graphics;

public class SegmentoEscudo extends ObjetoGrafico {
    
    // El contador de destrucción (2: Sano, 1: Dañado, 0: Destruido)
    private int estado; 

    private String[] spritesEstado;

    // Constructor: Necesita saber dónde nace y qué dimensiones tiene (20x10)
    public SegmentoEscudo(int x, int y) {
        super();
        this.estado = 2; // Nace sano
        
        // Inicializar las dimensiones individuales
        this.setDimension(new java.awt.Dimension(20, 10));
        this.setPunto(new java.awt.Point(x, y));
        spritesEstado[0] = "imagenes/Space Invaders/Invaders/space__0008_ShieldFull.png"; // Sano
        spritesEstado[1] = "imagenes/Space Invaders/Invaders/space__0009_ShieldDamaged.png"; // Dañado
        spritesEstado[2] = "imagenes/Space Invaders/Invaders/space__
        
        // ACÁ VAS A CARGAR TUS SPRITES EN EL ARREGLO
        // Ejemplo: spritesEstado[2] = "ruta/sano.png", spritesEstado[1] = "ruta/roto.png"
    }

    public void recibirDanio() {
        // ACÁ VA TU LÓGICA: Restar al estado y evaluar si llega a 0 para borrar
    }

    public void renderizar(Graphics g) {
        // ACÁ VA TU LÓGICA: Si estado > 0, dibujar el sprite que corresponda al índice
    }
}