package py_poo.pong;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;

import py_poo.engine.EstadoJuego;
import py_poo.engine.VideoJuego;
import py_poo.input.InputManager;

public class JuegoPong extends VideoJuego {
    private boolean OpJuego;
    private InputManager input;
    private MenuPong menu;
    private Paleta paleta1;
    private Paleta paleta2;
    public void setOpJuego(boolean opJuego) {
        this.OpJuego = opJuego;
    }

  @Override
    public void iniciar() {
        
        super.iniciar(); 
        
     
        this.input = new InputManager(); 
        
   
        this.menu = new MenuPong(input, null); 
        
        
        this.estado = EstadoJuego.MENU;
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
        if(estado == EstadoJuego.JUGANDO){
            if(paleta1 != null) {
                paleta1.dibujar(g);
            }
            if(paleta2 != null) {
                paleta2.dibujar(g);
            }
        }
    }
    @Override
    protected void crearPartida() {
        
    }
    @Override
    public String getGanador(){
        return Nombre;

    }
    @Override
    public String getPerdedor(){
        return Nombre;

    }
   @Override
 protected void actualizarLogicaJuego() {
    if (this.estado == EstadoJuego.MENU) {
            if (input.isEnterPressed()) {
                crearPartida(); 
            }
            return; 
    }
    if (this.estado == EstadoJuego.JUGANDO) {
            if (paleta1 != null) paleta1.Mover();
            if (paleta2 != null) paleta2.Mover();
            
        }
    }
 }

