package py_poo.spaceinvaders;

import java.awt.Graphics;
import java.util.HashMap;

import py_poo.audio.FXPlayer;
import py_poo.core.Constantes;
import py_poo.core.GameLoop;
import py_poo.engine.EstadoJuego;
import py_poo.engine.VideoJuego;
import py_poo.entities.ObjetoGrafico;
import py_poo.input.InputManager;
import py_poo.ranking.RankingManager;

public class JuegoSpaceInvaders extends VideoJuego {
    private FXPlayer fxPlayer;
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
    private int puntaje = 0;
    private int contadorDisparos = 0;
    private SegmentoEscudo SegmentoEscudo = null;
    private NivelSpaceInvaders nivel = new NivelSpaceInvaders();
    private RankingManager rankingManager;
    private boolean rankingRegistrado;

    private void registrarRankingFinal() {
        if (rankingRegistrado) {
            return;
        }
        String jugador = (nombreJugadorPrincipal != null && !nombreJugadorPrincipal.isBlank()) ? nombreJugadorPrincipal : "Toni";
    
        rankingManager.agregarPuntaje(jugador, "Space Invaders", (this.nivelDeFlota + 1), this.puntaje);
    
        rankingRegistrado = true;
    }

    @Override
    public void iniciar() {
        super.iniciar();
        this.fxPlayer = new FXPlayer();
        this.input = new InputManager();
        
        //Precarga de efectos de sonido
        this.fxPlayer.cargarSonidoRecurso("Laser", "sonidos/SpaceInvader/Laser.wav");
        this.fxPlayer.cargarSonidoRecurso("LaserFlota", "sonidos/SpaceInvader/LaserFlota.wav");
        this.fxPlayer.cargarSonidoRecurso("ExplosionNaves", "sonidos/SpaceInvader/ExplosionNaves.wav");
        this.fxPlayer.cargarSonidoRecurso("ExplosionFlota", "sonidos/SpaceInvader/ExplosionFlota.wav");
        this.fxPlayer.cargarSonidoRecurso("PlatoVolador", "sonidos/SpaceInvader/PlatoVolador.wav");
        this.fxPlayer.cargarSonidoRecurso("GameOver", "sonidos/SpaceInvader/GameOver.wav");
        this.fxPlayer.cargarSonidoRecurso("CancionSpaceInvaders", "sonidos/SpaceInvader/CancionSpaceInvaders.wav");
        
      
        this.fxPlayer.setVolumen("CancionSpaceInvaders", "medio");
        this.menu = new MenuSpaceInvaders(this.input, this);
        
        this.menu.setVisible(true);
        this.rankingManager = new RankingManager();
        this.rankingRegistrado = false;
        this.estado = EstadoJuego.MENU;
    }
    
   @Override
protected void actualizarLogicaJuego() {
    //Estado 1 Menu Principal
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
        //Estado 2 Pausa
    } else if (this.estado == EstadoJuego.PAUSA) {
        if (input.isEnterPressed()) { 
            this.estado = EstadoJuego.JUGANDO;
        }
        return; 
        //Estado 3 Jugando
    } else if (this.estado == EstadoJuego.JUGANDO) {
        if (input.isPPressed()) {
            this.estado = EstadoJuego.PAUSA;
            return;
        }
       //Movimiento de la nave
        if (navecita != null) {
            int limiteDerecho = Constantes.WIDTH - navecita.getWidth(); 

            if (input.isLeftPressed() && navecita.getX() > 0) {
                navecita.setX(navecita.getX() - 5);
            }
            if (input.isRightPressed() && navecita.getX() < limiteDerecho) {
                navecita.setX(navecita.getX() + 5);
            }
        }
        //disparo Nave
        if (input.isSpacePressed()) {
           if (disparo == null || disparo.isParaEliminar()) { 
                disparo = navecita.Disparar();
                this.contadorDisparos++;
                Entidades.add(disparo);

                if (this.menu.isSonidoActivado()) {
                    this.fxPlayer.reproducir("Laser");
                }
            }
        }
        //Animacion de Flota
        if (flotaE != null) {
            for (Enemigo bicho : flotaE.values()) {
                bicho.actualizacionAnimacion(); 
            }
        }

       //Implementar 3 niveles de velocidad de flota//
       //Movimiento de flota
       int delay=0;
        if(this.menu != null){
            int velocidad=this.menu.getVelocidad();
            if(velocidad == 0){
                delay=80; // bajo
            }else if(velocidad== 1){
                delay=50; //normal
            }else{
                delay=20; //mas rapido
            }
        }
       
        
        long tiempoActualFlota = System.currentTimeMillis();
        if (tiempoActualFlota - ultimoMovimientoFlota > delay) {
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
                
               
                //derrota imnmediata sin importar las vidas, invasion
                if (navecita != null && bicho.getY() + bicho.getHeight() >= navecita.getY()) {
                    this.navecita = null;
                    this.Resultado = "No compa, nos entraron los bichos, Corre Wachin";
                    if (puntaje > 5000) {
                        this.Resultado = "¡Vivir solo cuesta vida!";
                    }
                    this.estado = EstadoJuego.GAME_OVER;
                    registrarRankingFinal();
                    break; 
                }
            }   
            ultimoMovimientoFlota = tiempoActualFlota;
        }  
        //Nave Nodriza desarrollo completo
        if (this.platoVolador == null) {
            if (Math.random() < 0.0001) { 
                this.platoVolador = new NaveNodriza();
                Entidades.add(this.platoVolador);
            }
        } else {
            if (this.platoVolador.isParaEliminar()) {
                this.platoVolador = null;
            }
        }
            //Tiros de flota bichos
        for (Enemigo bicho : flotaE.values()) {
            if (!bicho.isParaEliminar() && Math.random() < 0.0001) {
                int centroX = (int) bicho.getX() + (bicho.getWidth() / 2); 
                int origenY = (int) bicho.getY() + bicho.getHeight();
                String laserEnemigoSkin = skinsLaserEnemigo[menu.getSkinProyectiles()];
                Laser disparoEnemigo = new Laser(centroX, origenY, 4, laserEnemigoSkin);
                if (this.menu.isSonidoActivado()) {
                    this.fxPlayer.reproducir("LaserFlota");
                }
                Entidades.add(disparoEnemigo);  
            }
        }
            //Matriz de colisiones
        for (int i = 0; i < Entidades.size(); i++) {
            ObjetoGrafico entidad = Entidades.get(i);
            if (entidad instanceof Laser) {
                Laser laser = (Laser) entidad;
                
                if (laser.getVelocidad() < 0 && !laser.isParaEliminar()) {
                    if (platoVolador != null && !platoVolador.isParaEliminar() && laser.getBounds().intersects(platoVolador.getBounds())) {
                        Entidades.add(new Murido((int)platoVolador.getX(), (int)platoVolador.getY(), 1));
                        platoVolador.marcarParaEliminar();
                        this.puntaje += this.platoVolador.puntaje(this.contadorDisparos);
                        laser.marcarParaEliminar();
                        platoVolador = null; 
                    }
                    for (Enemigo bicho : flotaE.values()) {
                        if (!bicho.isParaEliminar() && laser.getBounds().intersects(bicho.getBounds())) {
                            Entidades.add(new Murido((int)bicho.getX(), (int)bicho.getY(), 1));
                            bicho.marcarParaEliminar();
                            if (this.menu.isSonidoActivado()) {
                                 this.fxPlayer.reproducir("ExplosionFlota");
                            }
                            if (bicho instanceof EnemigoA) puntaje += 30;
                            else if (bicho instanceof EnemigoB) puntaje += 20;
                            else if (bicho instanceof EnemigoC) puntaje += 10;
                            laser.marcarParaEliminar();
                            break;
                        }
                    }
                
                } else if (laser.getVelocidad() > 0 && !laser.isParaEliminar()) {
                    if (navecita != null && laser.getBounds().intersects(navecita.getBounds())) {
                        Entidades.add(new Murido((int)navecita.getX(), (int)navecita.getY(), 2));
                        laser.marcarParaEliminar();
                        navecita.recibirDanio(1);
                        if (navecita.getVidas() > 0) {
                            navecita.setX(380);
                            navecita.setY(500);
                            if (this.menu.isSonidoActivado()) {
                                 this.fxPlayer.reproducir("ExplosionNaves");
                            }
                        } else {
                            this.navecita = null;
                            this.Resultado = "Te re moriste cumpa";
                            if (puntaje > 5000) {
                                this.Resultado = "¡El futuro llego hace rato!";
                            }
                            this.estado = EstadoJuego.GAME_OVER;
                            registrarRankingFinal();

                            if (this.fxPlayer != null) {
                                 this.fxPlayer.detener("CancionSpaceInvaders");
                                 if (this.menu.isSonidoActivado()) {
                                     this.fxPlayer.reproducir("GameOver");
                                }
                            }
                        }
                        break;
                    }
                }
                
                if (!laser.isParaEliminar()) {
                    for (int a = 0 ; a < Entidades.size(); a++) {
                        ObjetoGrafico posibleSegmento = Entidades.get(a);
                        if (posibleSegmento instanceof SegmentoEscudo) {
                            SegmentoEscudo seg = (SegmentoEscudo) posibleSegmento;
                            if (!seg.isParaEliminar() && laser.getBounds().intersects(seg.getBounds())) {
                                seg.recibirDanio();
                                laser.marcarParaEliminar();
                                break; 
                            }
                        }
                    }
                }
            }
        }
        //Limpieza de flota en hashmap
        if (flotaE != null) {
            flotaE.values().removeIf(bicho -> bicho.isParaEliminar());
            if (flotaE.isEmpty()) {
                navecita.agregarVida(1);
                this.nivelDeFlota++; 
                nivel.generarOleadas(this.flotaE, Entidades, this.nivelDeFlota, menu.getSkinInvasores());
            }
        }

        for (int a = Entidades.size() - 1; a >= 0; a--) {
            ObjetoGrafico entidadLimpieza = Entidades.get(a);
            entidadLimpieza.actualizar();                                     
            
            if (entidadLimpieza.isParaEliminar()) {
                Entidades.remove(a); 
            }
        }

        if(input.isEscapePressed()){
            if (this.fxPlayer != null) {
                    this.fxPlayer.detener("CancionSpaceInvaders");
                }
            this.estado= EstadoJuego.MENU;
        }
        //Estado 4 Game over
    } else if (this.estado == EstadoJuego.GAME_OVER) {
            if (input.isEnterPressed()) {
                if (this.fxPlayer != null) {
                    this.fxPlayer.detener("CancionSpaceInvaders");
                }
                if (this.menu != null) {
                     this.menu.recargarRanking(); 
                }
                this.estado = EstadoJuego.MENU;
            }
    }
}

    public void pause() {
        estado = EstadoJuego.PAUSA;
    }

    @Override
    public void renderizar(Graphics g) {
        if (this.estado == EstadoJuego.MENU) {
            if (menu != null) {
                ((MenuSpaceInvaders) menu).dibujar(g); 
            }
        } else if (this.estado == EstadoJuego.JUGANDO) {
            super.renderizar(g); 
            if (navecita != null) {
                g.setColor(java.awt.Color.WHITE);
                g.setFont(new java.awt.Font("Arial", java.awt.Font.BOLD, 20));
                g.drawString("Puntaje: " + puntaje, 630, 580);
                g.drawString("Vidas x " + navecita.getVidas(), 20, 580);
            }
            
        } if (this.estado == EstadoJuego.PAUSA) {
            g.setColor(new java.awt.Color(0, 0, 0, 150));
            g.fillRect(0, 0, Constantes.WIDTH, Constantes.HEIGHT);
            g.setColor(java.awt.Color.WHITE);
            g.setFont(new java.awt.Font("Arial", java.awt.Font.BOLD, 40));
            g.drawString("PAUSA", 350, 300);
        } if (this.estado == EstadoJuego.GAME_OVER) {
            g.setColor(new java.awt.Color(0, 0, 0, 150));
            g.fillRect(0, 0, Constantes.WIDTH, Constantes.HEIGHT);
            
            g.setColor(java.awt.Color.RED);
            g.setFont(new java.awt.Font("Arial", java.awt.Font.BOLD, 50));
            int anchoTitulo = g.getFontMetrics().stringWidth("Fin del Juego");
            g.drawString("Fin del Juego", (Constantes.WIDTH - anchoTitulo) / 2, 130);
            
            g.setColor(java.awt.Color.WHITE);
            g.setFont(new java.awt.Font("Arial", java.awt.Font.BOLD, 26));
            int anchoResultado = g.getFontMetrics().stringWidth(this.Resultado);
            g.drawString(this.Resultado, (Constantes.WIDTH - anchoResultado) / 2, 240);
            
            g.setColor(java.awt.Color.YELLOW);
            g.setFont(new java.awt.Font("Arial", java.awt.Font.BOLD, 24));
            int anchoPuntaje = g.getFontMetrics().stringWidth("Puntaje Final: " + puntaje);
            g.drawString("Puntaje Final: " + puntaje, (Constantes.WIDTH - anchoPuntaje) / 2, 340);

            g.setColor(java.awt.Color.LIGHT_GRAY);
            g.setFont(new java.awt.Font("Arial", java.awt.Font.PLAIN, 18));
            int anchoSalir = g.getFontMetrics().stringWidth("Presiona ENTER para volver al menú");
            g.drawString("Presiona ENTER para volver al menú", (Constantes.WIDTH - anchoSalir) / 2, 400);
        }
    }
   
    private String[] skinsNave = {
        "imagenes/Space Invaders/Invaders/normal.png",
        "imagenes/Space Invaders/Invaders/moderno.png" // Skin alternativa
    };

    private String[] skinsLaser = {
        "imagenes/Space Invaders/Projectiles/Projectile_Player.png",
        "imagenes/Space Invaders/Projectiles/ProjectileB_1.png" // Skin alternativa
    };

    private String[] skinsLaserEnemigo = {
        "imagenes/Space Invaders/Projectiles/ProjectileA_1.png",
        "imagenes/Space Invaders/Projectiles/ProjectileB_4.png" // Skin alternativa
    };

    @Override
    protected void crearPartida() {
        this.Entidades.clear();
        this.disparo = null;
        this.puntaje = 0;
        this.platoVolador = null;

        String naveSkin = skinsNave[menu.getSkinNave()];
        String laserSkin = skinsLaser[menu.getSkinProyectiles()];

        this.navecita = new NaveJugador(380, 500, naveSkin, laserSkin);
        Entidades.add(navecita);
        this.rankingRegistrado = false;
        this.nivelDeFlota = 0;
        this.flotaE = new HashMap<>(); 
        nivel.generarOleadas(this.flotaE, Entidades, this.nivelDeFlota, menu.getSkinInvasores());
        Escudo e1 = new Escudo(150, 420);
        Escudo e2 = new Escudo(320, 420);
        Escudo e3 = new Escudo(490, 420);
        Escudo e4 = new Escudo(660, 420);
        for (SegmentoEscudo seg : e1.getSegmentos()) Entidades.add(seg);
        for (SegmentoEscudo seg : e2.getSegmentos()) Entidades.add(seg);
        for (SegmentoEscudo seg : e3.getSegmentos()) Entidades.add(seg);        
        for (SegmentoEscudo seg : e4.getSegmentos()) Entidades.add(seg);
        this.estado = EstadoJuego.JUGANDO;
        if (this.menu.isSonidoActivado()) {
            this.fxPlayer.repetir("CancionSpaceInvaders");
        }
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