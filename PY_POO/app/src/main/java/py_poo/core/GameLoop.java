package py_poo.core;

import java.awt.Graphics2D;
import com.entropyinteractive.Keyboard;
import com.entropyinteractive.Mouse;
import com.entropyinteractive.MouseWheel;

import py_poo.interfaces.JuegoLoopable;

public class GameLoop extends com.entropyinteractive.JGame {
    private static GameLoop instancia;
    private JuegoLoopable videojuego;
    private static double deltaTime;

    public GameLoop(String title, int width, int height) {
        super(title, width, height);
        instancia = this;
    }

    public void setVideoJuego(JuegoLoopable vj) {
        this.videojuego = vj;
    }

    public JuegoLoopable getVideoJuego() {
        return videojuego;
    }

    public static double getDeltaTime() {
        return deltaTime;
    }

    public static Keyboard getTeclado() {
        return instancia != null ? instancia.getKeyboard() : null;
    }

    public static Mouse getRaton() {
        return instancia != null ? instancia.getMouse() : null;
    }

    public static MouseWheel getRuedaRaton() {
        return instancia != null ? instancia.getMouseWheel() : null;
    }

    public static void terminarJuego() {
        if (instancia != null) {
            instancia.stop();
        }
    }

    @Override
    public void gameStartup() {
        if (videojuego != null) {
            videojuego.iniciar();
        }
    }

    @Override
    public void gameUpdate(double delta) {
        deltaTime = delta;
        if (videojuego != null) {
            videojuego.actualizar();
        }
    }

    @Override
    public void gameDraw(Graphics2D g) {
        if (videojuego != null) {
            videojuego.renderizar(g);
        }
    }

    @Override
    public void gameShutdown() {
        if (videojuego != null) {
            videojuego.finalizar();
        }
    }
    
public void run(int fps) {
    super.run(fps); 
}
/*
    public void run() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'run'");
    }
        */
}
