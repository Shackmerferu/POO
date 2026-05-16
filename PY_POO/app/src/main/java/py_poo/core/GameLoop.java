package py_poo.core;

import java.awt.Graphics2D;

import py_poo.engine.EstadoJuego;
import py_poo.engine.VideoJuego;

public class GameLoop extends com.entropyinteractive.Game {
    public GameLoop(String title, int width, int height) {
        super(title, width, height);
        //TODO Auto-generated constructor stub
    }

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
    
    @Override
    public void gameStartup() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'gameStartup'");
    }

    @Override
    public void gameUpdate(double var1) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'gameUpdate'");
    }

    @Override
    public void gameDraw(Graphics2D var1) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'gameDraw'");
    }

    @Override
    public void gameShutdown() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'gameShutdown'");
    }
}
