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
    private Laser disparo = null;
    private long ultimoMovimientoFlota = 0;
    private NaveNodriza platoVolador = null;
    private int nivelDeFlota = 0;
    private NivelSpaceInvaders nivel = new NivelSpaceInvaders();




    
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
                menu.setSeleccion(Math.min(2, menu.getSeleccion() + 1));
            }
            if (input.isEnterPressed()) {
              
               int opcionActual = menu.getSeleccion();
                
                if (opcionActual == 0) {
                    
                    crearPartida();
                    this.estado = EstadoJuego.JUGANDO; 
                } 
                else if (opcionActual == 1) {
                   
                    menu.setConfigMode(true);
                } 
                else if (opcionActual == 2) {
                  
                    GameLoop.terminarJuego();
                }
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
               if (disparo == null || disparo.isParaEliminar()) { 
                    disparo = navecita.Disparar();
                    Entidades.add(disparo);
                }
            }
            long tiempoActualFlota = System.currentTimeMillis();
            if(tiempoActualFlota - ultimoMovimientoFlota > 35){
             
            
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
                if (navecita != null && bicho.getY() + bicho.getHeight() >= navecita.getY()) {
                            this.navecita = null;
                            this.Resultado = "¡Invasión alienígena! Game Over.";
                            this.estado = EstadoJuego.GAME_OVER;
                            break; 
                        }
            }  
               ultimoMovimientoFlota = tiempoActualFlota;
            }  
            if (this.platoVolador == null) {
            
                if (Math.random() < 0.002) { 
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
                        if (platoVolador != null && !platoVolador.isParaEliminar() && laser.getBounds().intersects(platoVolador.getBounds())) {
                                Entidades.add(new Murido((int)platoVolador.getX(), (int)platoVolador.getY(), 1));
                                platoVolador.marcarParaEliminar();
                                laser.marcarParaEliminar();
                                platoVolador = null; 
                            }
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
                            navecita.recibirDanio(1);
                            if(navecita.getVidas()>0){
                                navecita.setX(380);
                                navecita.setY(500);
                            }else{
                            this.navecita=null;
                            this.Resultado= "Te re moriste cumpa";
                            this.estado= EstadoJuego.MENU;
                            }
                            break;
                        }
                    }
                } 

            }
        
            

            if (flotaE != null) {
                flotaE.values().removeIf(bicho -> bicho.isParaEliminar());
                if(flotaE.isEmpty()){
                    navecita.agregarVida(1);
                    this.nivelDeFlota++; 
                    nivel.generarOleadas(this.flotaE, Entidades, this.nivelDeFlota);
                }
            }

           
            for (int i = Entidades.size() - 1; i >= 0; i--) {
                ObjetoGrafico entidad = Entidades.get(i);
                entidad.actualizar();                                     
                
                if (entidad.isParaEliminar()) {
                    Entidades.remove(i);
                }
                
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
        } else if (this.estado == EstadoJuego.JUGANDO) {
            super.renderizar(g); 
            if (navecita != null) {
                g.setColor(java.awt.Color.WHITE);
                g.setFont(new java.awt.Font("Arial", java.awt.Font.BOLD, 20));
                
                
                g.drawString("Vidas x " + navecita.getVidas(), 20, 580);
            }
        }
    }
   

    @Override
    protected void crearPartida() {
        this.Entidades.clear();
        this.disparo = null;
        this.navecita = new NaveJugador(380, 500);
        Entidades.add(navecita);
        
        this.nivelDeFlota = 0;
        this.flotaE = new HashMap<>(); 
        
       
        nivel.generarOleadas(this.flotaE, Entidades, this.nivelDeFlota);
        
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
