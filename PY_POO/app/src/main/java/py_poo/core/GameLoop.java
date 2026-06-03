package py_poo.core;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.DisplayMode;
import java.awt.Frame;
import java.awt.Graphics2D;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Window;

import com.entropyinteractive.Keyboard;
import com.entropyinteractive.Mouse;
import com.entropyinteractive.MouseWheel;

import py_poo.interfaces.JuegoLoopable;

public class GameLoop extends com.entropyinteractive.Game {
    private static GameLoop instancia;
    private JuegoLoopable videojuego;
    private static double deltaTime;
    private boolean isFullscreen;

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

    public void toggleFullscreen() {
        GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
        Window window = null;
        for (Frame f : Frame.getFrames()) {
            if (f.isVisible() && f.getWidth() > 100 && f.getHeight() > 100) {
                window = f;
                break;
            }
        }
        if (window == null) return;
        if (isFullscreen) {
            gd.setFullScreenWindow(null);
            window.setSize(Constantes.WIDTH, Constantes.HEIGHT);
            window.setVisible(true);
            window.toFront();
            isFullscreen = false;
        } else {
            DisplayMode dm = gd.getDisplayMode();
            gd.setFullScreenWindow(window);
            resizeCanvas(window, dm.getWidth(), dm.getHeight());
            isFullscreen = true;
        }
    }

    private void resizeCanvas(Window window, int w, int h) {
        for (Component c : ((Frame) window).getComponents()) {
            if (c instanceof java.awt.Canvas) {
                c.setSize(w, h);
                c.setPreferredSize(new Dimension(w, h));
            }
        }
        window.invalidate();
        window.validate();
    }

    public static void toggleFullscreenStatic() {
        if (instancia != null) {
            instancia.toggleFullscreen();
        }
    }

    public static boolean isFullscreen() {
        return instancia != null && instancia.isFullscreen;
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
            if (isFullscreen) {
                double sx = (double) getWidth() / Constantes.WIDTH;
                double sy = (double) getHeight() / Constantes.HEIGHT;
                double s = Math.min(sx, sy);
                g.translate(
                    (int) ((getWidth() - Constantes.WIDTH * s) / 2),
                    (int) ((getHeight() - Constantes.HEIGHT * s) / 2)
                );
                g.scale(s, s);
                g.setClip(0, 0, Constantes.WIDTH, Constantes.HEIGHT);
            }
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
        super.run(1.0 / fps);
    }
}
