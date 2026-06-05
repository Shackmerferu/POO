package py_poo.engine;

import py_poo.core.Constantes;
import py_poo.entities.ObjetoGrafico;
import py_poo.loderunner.Nivel;

public class Camara {
    private int X;
    private int Y;

    public int getX() { return X; }
    public int getY() { return Y; }

    public void mover() {
    }

    public void seguirJugador(ObjetoGrafico obj, Nivel nivel) {
        int objetivoX = (int)obj.getX() - Constantes.WIDTH / 2 + obj.getWidth() / 2;
        int objetivoY = (int)obj.getY() - Constantes.HEIGHT / 2 + obj.getHeight() / 2;

        if (nivel != null) {
            int maxX = Math.max(0, nivel.getAnchoPixels() - Constantes.WIDTH);
            int maxY = Math.max(0, nivel.getAltoPixels() - Constantes.HEIGHT);
            objetivoX = Math.max(0, Math.min(objetivoX, maxX));
            objetivoY = Math.max(0, Math.min(objetivoY, maxY));
        }

        X = objetivoX;
        Y = objetivoY;
    }
}
