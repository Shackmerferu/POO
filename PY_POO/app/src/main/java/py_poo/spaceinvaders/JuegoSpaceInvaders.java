package py_poo.spaceinvaders;

import java.awt.Graphics;
import java.util.HashMap;

import py_poo.core.GameLoop;
import py_poo.engine.EstadoJuego;
import py_poo.engine.VideoJuego;
import py_poo.entities.ObjetoGrafico;
import py_poo.input.InputManager;
import py_poo.ui.MenuPrincipal;
import py_poo.core.Constantes;
public class JuegoSpaceInvaders extends VideoJuego {
    private InputManager input;
    private MenuSpaceInvaders menu;
    private NaveJugador navecita;
    private HashMap<String, Enemigo> flotaE;
    private int direccionflotaX = 2; 
    private int velocidadflotaY = 15;
    private long ultimoDisparo = 0;
    private long ultimoMovimientoFlota = 0;
    private NaveNodriza platoVolador = null;
    
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
            if (menu.isConfigMode()) {
                menu.actualizarConfig();
                return;
            }

            if (input.isMenuUpPressed() || input.isWPressed()) {
                menu.setSeleccion(Math.max(0, menu.getSeleccion() - 1));
            }
            if (input.isMenuDownPressed() || input.isSPressed()) {
                menu.setSeleccion(Math.min(3, menu.getSeleccion() + 1));
            }
            if (input.isEnterPressed()) {
                if (menu.getSeleccion() == 3) {
                    GameLoop.terminarJuego(); 
                    return;
                }
                if (menu.getSeleccion() == 2) {
                    menu.setConfigMode(true);
                    return;
                }
            
                crearPartida();
            }
            return;
        }

      
        if (this.estado == EstadoJuego.JUGANDO) {
            
           
            if (navecita != null) {
                
                int limiteDerecho = Constantes.WIDTH - navecita.getWidth(); 

                if (input.isLeftPressed() && navecita.getX() > 0) {
                    navecita.setX(navecita.getX() - 5);
                }
                if (input.isRightPressed() && navecita.getX() < limiteDerecho) {
                    navecita.setX(navecita.getX() + 5);
                }
            }

            
            if (input.isSpacePressed()) {
                long tiempoActual = System.currentTimeMillis();
                if (tiempoActual - ultimoDisparo > 400) { 
                    Laser nuevoDisparo = navecita.Disparar();
                    Entidades.add(nuevoDisparo);
                    ultimoDisparo = tiempoActual;
                }
            }
            long tiempoActualFlota = System.currentTimeMillis();
            if(tiempoActualFlota - ultimoMovimientoFlota > 50){
             
            
            boolean tocaronBorde = false;
           
            for (Enemigo bicho : flotaE.values()) {
                if (direccionflotaX > 0 && bicho.getX() + bicho.getWidth() >= Constantes.WIDTH) {
                    tocaronBorde = true;
                    break;
                }
                if (direccionflotaX < 0 && bicho.getX() <= 0) {
                    tocaronBorde = true;
                    break;
                }
            }

            if (tocaronBorde) {
                direccionflotaX = direccionflotaX * -1;
            }

            for (Enemigo bicho : flotaE.values()) {
                bicho.setX(bicho.getX() + direccionflotaX); 
                if (tocaronBorde) {
                    bicho.setY(bicho.getY() + velocidadflotaY); 
                }
            }  
               ultimoMovimientoFlota = tiempoActualFlota;
            }  
            if (this.platoVolador == null) {
            
                if (Math.random() < 0.0002) { 
                    this.platoVolador = new NaveNodriza();
                    Entidades.add(this.platoVolador);
                }
            } else {
              
                if (this.platoVolador.isParaEliminar()) {
                    this.platoVolador = null;
                }
            }
            for (Enemigo bicho : flotaE.values()) {
            if(!bicho.isParaEliminar() && Math.random() < 0.0002) {
                    int centroX = (int) bicho.getX() + (bicho.getWidth() / 2); 
                    int origenY = (int) bicho.getY() + bicho.getHeight();
                    Laser disparoEnemigo = new Laser(centroX, origenY, 5, "imagenes/Space Invaders/Projectiles/ProjectileA_1.png");
                    Entidades.add(disparoEnemigo);  
                }
            }
            for(int i=0; i<Entidades.size(); i++){
              ObjetoGrafico entidad = Entidades.get(i);
                if (entidad instanceof Laser){
                    Laser laser = (Laser) entidad;
                    if (laser.getVelocidad()<0 && !laser.isParaEliminar()){
                            for(Enemigo bicho : flotaE.values()){
                                if (!bicho.isParaEliminar() && laser.getBounds().intersects(bicho.getBounds())){
                                    Entidades.add(new Murido((int)bicho.getX(), (int)bicho.getY(), 1));
                                    bicho.marcarParaEliminar();
                                    laser.marcarParaEliminar();
                                    break;
                                }
                            }
                    }else if(laser.getVelocidad()>0 && !laser.isParaEliminar()){
                        if(laser.getBounds().intersects(navecita.getBounds())){
                            Entidades.add(new Murido((int)navecita.getX(), (int)navecita.getY(), 2));
                            laser.marcarParaEliminar();
                            this.navecita=null;
                            this.Resultado= "Te re moriste cumpa";
                            this.estado= EstadoJuego.GAME_OVER;
                            break;
                        }
                    }
                } 

                }

            }
           
            if (flotaE != null) {
                flotaE.values().removeIf(bicho -> bicho.isParaEliminar());
            }

           
            for (int i = Entidades.size() - 1; i >= 0; i--) {
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
       this.flotaE = new HashMap<>(); 
        
        int filas = 4;
        int columnas = 10;
        
        for (int i = 0; i < filas; i++) {
            for (int j = 0; j < columnas; j++) {
                int posX = 50 + (j * 50); 
                int posY = 50 + (i * 40);
                
                Enemigo bicho = null; 
                if (i == 0) {
                    bicho = new EnemigoA(posX, posY); 
                } else if (i == 1 || i == 2) {
                    bicho = new EnemigoB(posX, posY); 
                } else {
                    bicho = new EnemigoC(posX, posY); 
                }
              
                String clave = i + "," + j; 
                
              
                flotaE.put(clave, bicho);
                Entidades.add(bicho);
            }
        }
       
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
