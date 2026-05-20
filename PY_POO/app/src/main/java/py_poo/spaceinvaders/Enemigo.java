package py_poo.spaceinvaders;

import py_poo.entities.Personaje;
import py_poo.interfaces.Armado;

public abstract class Enemigo extends Personaje implements Armado {
    public void moverEnFormacion() {
    }

    @Override
    public void Disparar() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'Disparar'");
    }
}
