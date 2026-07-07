package py_poo.spaceinvaders;

import py_poo.entities.ObjetoGrafico;

public class SegmentoEscudo extends ObjetoGrafico {
    
    private int estado; // 2: Sano, 1: Dañado, 0: Destruido
    private String archivoSano;
    private String archivoDaniado;

    
    public SegmentoEscudo(int x, int y, String archivoSano, String archivoDaniado) {
        super();
        this.estado = 2; 
        this.archivoSano = archivoSano;
        this.archivoDaniado = archivoDaniado;
        
        this.setBounds(new java.awt.Rectangle(x, y, 20, 10));
        
        actualizarSprite();
    }

    private void actualizarSprite() {
      
        
        if (this.estado == 2) {
            this.setSprite("imagenes/Space Invaders/Invaders/escudo/no daniado/" + this.archivoSano);
        } else if (this.estado == 1) {
            this.setSprite("imagenes/Space Invaders/Invaders/escudo/daniado/" + this.archivoDaniado);
        }
    }

    public void recibirDanio() {
        if (this.estado > 0) {
            this.estado--; 
            
            if (this.estado == 0) {
                this.marcarParaEliminar(); 
            } else {
                actualizarSprite(); 
            }
        }
    }
    
    public int getEstado() {
        return this.estado;
    }
}