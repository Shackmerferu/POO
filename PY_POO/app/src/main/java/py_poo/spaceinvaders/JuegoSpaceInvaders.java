package py_poo.spaceinvaders;

import java.awt.Graphics;

import py_poo.engine.EstadoJuego;
import py_poo.engine.VideoJuego;
import py_poo.input.InputManager;
import py_poo.pong.MenuPong;

public class JuegoSpaceInvaders extends VideoJuego {
    private InputManager input;
    private MenuSpaceInvaders menu;
    
    @Override
    public void iniciar() {
        super.iniciar();
        
        this.input = new InputManager();
        
        this.menu = new MenuSpaceInvaders(this.input, this);
        
        this.menu.setVisible(true);
        
        this.estado = EstadoJuego.MENU;
    }
    
    @Override
    protected void actualizarLogicaJuego() {
    if (this.estado == EstadoJuego.MENU) {
            if (input.isEnterPressed()) {
                crearPartida(); 
            }
            return; 
        }
       
 }
    
    public void pause(){
        estado = EstadoJuego.PAUSA;
    }
    @Override
    public void renderizar(Graphics g){
        super.renderizar(g);
        if (this.estado == EstadoJuego.JUGANDO) {

        }
    }
   

    @Override
    protected void crearPartida() {
        
        throw new UnsupportedOperationException("Unimplemented method 'crearPartida'");
    }

    @Override
    public String getGanador() {
        return Nombre;

    }

    @Override
    public String getPerdedor() {
        return Nombre;

    }
}
