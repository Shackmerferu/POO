package py_poo.loderunner;

import java.awt.Graphics;

import py_poo.engine.EstadoJuego;
import py_poo.engine.VideoJuego;
import py_poo.input.InputManager;
import py_poo.pong.MenuPong;

public class JuegoLodeRunner extends VideoJuego {
   private InputManager input;
    private MenuLodeRunner menu;
    
    @Override
    public void iniciar() {
        super.iniciar(); 
        
     
        this.input = new InputManager();
        super.input = this.input;
    
        this.menu = new MenuLodeRunner(input, null); 
        
        
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
     //Si estamos jugando, acá va la física    
 }
    
    public void pause(){
        estado = EstadoJuego.PAUSA;
    }
    @Override
    public void renderizar(Graphics g){
        super.renderizar(g);
        if (this.estado == EstadoJuego.MENU && menu != null) {
            menu.dibujar(g); 
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
