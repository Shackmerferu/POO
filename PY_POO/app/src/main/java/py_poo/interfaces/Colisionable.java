package py_poo.interfaces;

import py_poo.entities.Entidad;

public interface Colisionable {
    void colisionar(Entidad entidad);
    Object getBounds();
}
