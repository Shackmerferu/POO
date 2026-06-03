package py_poo.spaceinvaders;

import java.awt.Graphics;

import py_poo.engine.EstadoJuego;
import py_poo.engine.VideoJuego;
import py_poo.entities.ObjetoGrafico;
import py_poo.input.InputManager;

public class JuegoSpaceInvaders extends VideoJuego {
    private InputManager input;
    private MenuSpaceInvaders menu;
    private NaveJugador navecita;
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
    menu.actualizar(); 
    if (input.isEnterPressed()) {
        int op = menu.getSeleccion();
        if (op == 0) {
            crearPartida(); 
        } else if (op == 1) {
            System.out.println("Opciones... (por hacer)");
        } else if (op == 2) {
            System.exit(0); 
        }
    }
    return;
    }   
    if (this.estado == EstadoJuego.JUGANDO){
        if (navecita!= null){
            if(input.isLeftPressed()){
                navecita.setX(navecita.getX()-5);
            }
            if(input.isRightPressed()){
                navecita.setX(navecita.getX()+5);
            }
            if(input.isSpacePressed()){
                navecita.Disparar();
            }
        }
    }
    for(int i=Entidades.size()-1; i>=0; i--){
        ObjetoGrafico entidad = Entidades.get(i);
        entidad.actualizar();
        if (entidad.isParaEliminar()) {
                    Entidades.remove(i);
                }
    }
       
 }
    
    public void pause(){
        estado = EstadoJuego.PAUSA;
    }
    @Override
    public void renderizar(Graphics g){
      
        if (this.estado == EstadoJuego.MENU) {
            if (menu != null) {
               
                ((MenuSpaceInvaders) menu).dibujar(g); 
            }
        } 
        
        else if (this.estado == EstadoJuego.JUGANDO) {
            super.renderizar(g); 
        }
    }
   

    @Override
    protected void crearPartida() {
        this.navecita= new NaveJugador(380,500);
        Entidades.add(navecita);
        this.estado = EstadoJuego.JUGANDO;
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
