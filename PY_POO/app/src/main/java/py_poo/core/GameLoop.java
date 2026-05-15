package py_poo.core;

import py_poo.engine.EstadoJuego;
import py_poo.engine.VideoJuego;

public class GameLoop extends com.entropyinteractive.GameLoop {
    private EstadoJuego Estado; 
    private VideoJuego Videojuego;

    public void setEstado(EstadoJuego Estado) {
        this.Estado = Estado;
    }

    public EstadoJuego getEstado() {
        return Estado;
    }

    public void setVideoJuego(VideoJuego Videojuego) {
        this.Videojuego = Videojuego;
    }

    public VideoJuego getVideoJuego() {
        return Videojuego;
    }

    public void startup() {
    }

    
    public void shutdown() {
    }

    protected void update(double delta) {
    }

    public void draw() {
    }
}
