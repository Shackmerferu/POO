package py_poo.collision;

import java.util.List;

import py_poo.entities.ObjetoGrafico;

public class CollisionManager {
    public void verificarColisiones(List<ObjetoGrafico> entidades) {
        for (int i = 0; i < entidades.size(); i++) {
            for (int j = i + 1; j < entidades.size(); j++) {
                if (colisiona(entidades.get(i), entidades.get(j))) {
                    entidades.get(i).desaparecer();
                    entidades.get(j).desaparecer();
                }
            }
        }
    }

    public boolean colisiona(ObjetoGrafico a, ObjetoGrafico b) {
        return a.getBounds().intersects(b.getBounds());
    }
}
