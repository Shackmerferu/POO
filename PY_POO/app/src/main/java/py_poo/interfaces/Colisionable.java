package py_poo.interfaces;

import java.awt.Rectangle;

import py_poo.entities.ObjetoGrafico;

public interface Colisionable {
    void colisionar(ObjetoGrafico entidad);
    Object getBounds();
    public Rectangle getcolision();
    public Void setcolision(int Dimension);
}