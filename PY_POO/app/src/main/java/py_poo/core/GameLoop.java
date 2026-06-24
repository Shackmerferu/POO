package py_poo.core;

import com.entropyinteractive.Keyboard;
import com.entropyinteractive.Mouse;
import com.entropyinteractive.MouseWheel;

import py_poo.engine.VideoJuego;

public class GameLoop {
    public static double getDeltaTime() {
        return VideoJuego.getDeltaTime();
    }

    public static Keyboard getTeclado() {
        return VideoJuego.getTeclado();
    }

    public static Mouse getRaton() {
        return VideoJuego.getRaton();
    }

    public static MouseWheel getRuedaRaton() {
        return VideoJuego.getRuedaRaton();
    }

    public static void terminarJuego() {
        VideoJuego.terminarJuego();
    }

    public static void toggleFullscreenStatic() {
        VideoJuego.toggleFullscreenStatic();
    }

    public static boolean isFullscreen() {
        return VideoJuego.isFullscreen();
    }
}
