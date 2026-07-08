package py_poo.collision;

import java.util.List;

import py_poo.entities.ObjetoGrafico;

// Gestiona la detección de colisiones entre objetos gráficos del juego.
// Proporciona métodos para verificar colisiones entre dos objetos o entre todos los objetos de una lista.
public class CollisionManager {
    // Verifica colisiones entre todos los pares de entidades en la lista.
    // Si dos entidades colisionan, ambas son marcadas como desaparecidas.
    // NOTA: Este método no se usa actualmente en la lógica principal del juego;
    // las colisiones se manejan individualmente en JuegoLodeRunner.
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

    // Retorna true si los rectángulos delimitadores de dos objetos gráficos se intersectan,
    // indicando que están colisionando. Usa el método intersects de Rectangle.
    public boolean colisiona(ObjetoGrafico a, ObjetoGrafico b) {
        return a.getBounds().intersects(b.getBounds());
    }
}
