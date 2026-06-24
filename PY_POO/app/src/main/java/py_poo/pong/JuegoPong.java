package py_poo.pong;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;

import py_poo.audio.FXPlayer;
import py_poo.collision.CollisionManager;
import py_poo.core.Constantes;
import py_poo.core.GameLoop;
import py_poo.engine.EstadoJuego;
import py_poo.engine.VideoJuego;
import py_poo.input.InputManager;
import py_poo.utils.CargadorRecursos;

public class JuegoPong extends VideoJuego {

    public JuegoPong() {
        super("Pong", Constantes.WIDTH, Constantes.HEIGHT);
    }

    private boolean OpJuego;
    private InputManager input;
    private MenuPong menu;
    private Paleta paleta1;
    private Paleta paleta2;
    private PelotaPong pelota;
    private CollisionManager collisionManager;
    private FXPlayer fxPlayer;
    private int puntosJ1;         // puntaje jugador 1
    private int puntosJ2;         // puntaje jugador 2
    private int PUNTOS_MAX = 11;  // configurable desde Launcher
    private BufferedImage fondo;  // fondo escalado a 800x600
    private boolean modoIA;       // true = vs IA, false = 2 jugadores
    private IA_Pong ia;
    private boolean rankingRegistrado;

    public void setOpJuego(boolean opJuego) {
        this.OpJuego = opJuego;
    }

    public void setPuntosMax(int puntos) {
        this.PUNTOS_MAX = puntos;
    }

    @Override
    public void iniciar() {
        super.iniciar();
        this.input = new InputManager();
        super.input = this.input;
        this.menu = new MenuPong(input, null);
        this.collisionManager = new CollisionManager();

        // Inicia el sonido y carga los archivos
        this.fxPlayer = new FXPlayer();
        this.fxPlayer.cargarSonidoRecurso("rebote", "sonidos/paleta.wav");
        this.fxPlayer.cargarSonidoRecurso("punto", "sonidos/punto.wav");
        this.fxPlayer.cargarSonidoRecurso("inicio", "sonidos/Empieza.wav");
        this.fxPlayer.cargarSonidoRecurso("fondo", "sonidos/SoundTrack.wav");
        this.fxPlayer.setVolumen("fondo", "bajo");

        this.puntosJ1 = 0;
        this.puntosJ2 = 0;
        this.rankingRegistrado = false;
        this.estado = EstadoJuego.MENU; // Empieza en el menú

        CargadorRecursos cr = new CargadorRecursos();
        this.fondo = cr.cargarImagen("imagenes/Pong/Fondo Pong.png");
    }

    public void pause() {
        estado = EstadoJuego.PAUSA;
    }

    @Override
    public void renderizar(Graphics g) {
        super.renderizar(g);
        Graphics2D g2d = (g instanceof Graphics2D) ? (Graphics2D) g : null;
        if (g2d != null) {
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        }

        // Renderizado del menú principal
        if (this.estado == EstadoJuego.MENU && menu != null) {
            menu.dibujar(g);
        }

        // Renderizado de la mesa de juego
        if (estado == EstadoJuego.JUGANDO || estado == EstadoJuego.PAUSA || estado == EstadoJuego.GAME_OVER) {
            if (fondo != null) {
                g.drawImage(fondo, 0, 0, Constantes.WIDTH, Constantes.HEIGHT, null);
            } else {
                g.setColor(Color.BLACK);
                g.fillRect(0, 0, Constantes.WIDTH, Constantes.HEIGHT);
            }

            if (paleta1 != null) paleta1.dibujar(g);
            if (paleta2 != null) paleta2.dibujar(g);
            if (pelota != null) pelota.display(g);

            g.setFont(new Font("Consolas", Font.BOLD, 36));
            g.setColor(Color.WHITE);
            g.drawString(String.valueOf(puntosJ1), Constantes.WIDTH / 4, 50);
            g.drawString(String.valueOf(puntosJ2), 3 * Constantes.WIDTH / 4, 50);

            if (estado == EstadoJuego.PAUSA) {
                g.setFont(new Font("Consolas", Font.BOLD, 48));
                g.setColor(Color.YELLOW);
                g.drawString("PAUSA", Constantes.WIDTH / 2 - 80, Constantes.HEIGHT / 2);
                g.setFont(new Font("Consolas", Font.PLAIN, 20));
                g.setColor(Color.GRAY);
                g.drawString("P = reanudar  |  ESC = menu", Constantes.WIDTH / 2 - 160, Constantes.HEIGHT / 2 + 50);
            }

            if (estado == EstadoJuego.GAME_OVER) {
                g.setFont(new Font("Consolas", Font.BOLD, 48));
                g.setColor(Color.YELLOW);
                String ganador;
                if (puntosJ1 >= PUNTOS_MAX) {
                    ganador = "JUGADOR 1";
                } else {
                    ganador = modoIA ? "IA" : "JUGADOR 2";
                }
                g.drawString("GANADOR: " + ganador, Constantes.WIDTH / 2 - 200, Constantes.HEIGHT / 2);
                g.setFont(new Font("Consolas", Font.PLAIN, 20));
                g.setColor(Color.GRAY);
                g.drawString("Presiona ENTER para volver al menu", Constantes.WIDTH / 2 - 160, Constantes.HEIGHT / 2 + 50);
            }
        }

        g.setFont(new Font("Consolas", Font.PLAIN, 12));
        g.setColor(new Color(255, 255, 255, 100));
        g.drawString((soundEnabled ? "SONIDO:ON" : "SONIDO:OFF") + "  |  \\ = pantalla completa", 10, Constantes.HEIGHT - 10);
    }

    @Override
    protected void crearPartida() {
        paleta1 = new Paleta(input, 1);
        paleta1.ResetearPOS();
        paleta2 = new Paleta(input, 2);
        paleta2.ResetearPOS();

        // Aplicamos las skins desde las variables persistentes del menú
        int skinJ1 = menu.getSkinPaleta1();
        if (skinJ1 == 1) {
            paleta1.setSprite("imagenes/Pong/Paleta 1-2.png");
        } else if (skinJ1 == 2) {
            paleta1.setSprite("imagenes/Pong/Paleta 1-3.png");
        }

        int skinJ2 = menu.getSkinPaleta2();
        if (skinJ2 == 1) {
            paleta2.setSprite("imagenes/Pong/Paleta 2-2.png");
        } else if (skinJ2 == 2) {
            paleta2.setSprite("imagenes/Pong/Paleta 2-3.png");
        }

        pelota = new PelotaPong();
        puntosJ1 = 0;
        puntosJ2 = 0;
        rankingRegistrado = false;
        this.estado = EstadoJuego.JUGANDO;

        if (modoIA) {
            ia = new IA_Pong(pelota, paleta2, 4);
        } else {
            ia = null;
        }

        if (soundEnabled) {
            fxPlayer.reproducir("inicio");
            fxPlayer.detener("fondo");
            fxPlayer.repetir("fondo");
        }
    }

    @Override
    public String getGanador() { return Nombre; }

    @Override
    public String getPerdedor() { return Nombre; }

    @Override
    protected void actualizarLogicaJuego() {


        //  Estado: MENÚ PRINCIPAL
        if (this.estado == EstadoJuego.MENU) {
            if (menu.isConfigMode()) {
                menu.actualizarConfig();
                return;
            }
            if (input.isMenuUpPressed() || input.isWPressed()) {
                menu.navegarMainMenu(-1);
            }
            if (input.isMenuDownPressed() || input.isSPressed()) {
                menu.navegarMainMenu(1);
            }
            if (input.isEnterPressed()) {
                if (menu.getSeleccion() == 3) {
                    if (fxPlayer != null) fxPlayer.detener("fondo");
                    GameLoop.terminarJuego();
                    return;
                }
                if (menu.getSeleccion() == 2) {
                    menu.setConfigMode(true);
                    return;
                }
                modoIA = (menu.getSeleccion() == 0);
                crearPartida();
            }
        }
        //  Estado: FIN DE PARTIDA
        else if (this.estado == EstadoJuego.GAME_OVER) {
            if (input.isEnterPressed()) {
                if (fxPlayer != null) {
                    fxPlayer.detener("fondo"); // Detiene la pista musical de forma efectiva
                }
                if (menu != null) {
                    menu.recargarRanking(); // Refresca los nuevos puntajes desde la base de datos
                }
                this.estado = EstadoJuego.MENU; // Cambia el estado sin destruir la instancia del menú
            }
        }
        //  Estado: EN PARTIDA
        else if (this.estado == EstadoJuego.JUGANDO) {
            if (paleta1 != null) paleta1.Mover();
            if (modoIA) {
                if (ia != null) ia.calcularMovimiento();
            } else {
                if (paleta2 != null) paleta2.Mover();
            }

            if (pelota != null) {
                pelota.mover();
                pelota.rebotarParedes();

                if (collisionManager.colisiona(pelota, paleta1)) {
                    pelota.rebotarPaleta(paleta1);
                    if (soundEnabled) fxPlayer.reproducir("rebote");
                }
                if (collisionManager.colisiona(pelota, paleta2)) {
                    pelota.rebotarPaleta(paleta2);
                    if (soundEnabled) fxPlayer.reproducir("rebote");
                }
                if (pelota.salioIzquierda()) {
                    puntosJ2++;
                    if (soundEnabled) fxPlayer.reproducir("punto");
                    paleta1.ResetearPOS();
                    paleta2.ResetearPOS();
                    pelota.reiniciar(false);
                }
                if (pelota.salioDerecha()) {
                    puntosJ1++;
                    if (soundEnabled) fxPlayer.reproducir("punto");
                    if (modoIA && ia != null) ia.incrementarDificultad();
                    paleta1.ResetearPOS();
                    paleta2.ResetearPOS();
                    pelota.reiniciar(true);
                }
                if (puntosJ1 >= PUNTOS_MAX || puntosJ2 >= PUNTOS_MAX) {
                    estado = EstadoJuego.GAME_OVER;
                    registrarRankingFinal();
                }
            }
        }
    }

    private void registrarRankingFinal() {
        if (rankingRegistrado) {
            return;
        }
        if (nombreJugadorPrincipal == null || nombreJugadorPrincipal.isBlank()) {
            return;
        }
        String resultadoStr = (puntosJ1 >= PUNTOS_MAX) ? "(Ganó)" : "(Perdió)";
        String modoStr = modoIA ? "vs IA" : "vs J2";
        String nombreJuegoDetalle = "Pong " + modoStr + " [" + puntosJ1 + "-" + puntosJ2 + "] " + resultadoStr;
        rankingManager.agregarPuntaje(nombreJugadorPrincipal, nombreJuegoDetalle, getNivelActual(), puntosJ1);
        rankingRegistrado = true;
    }

       @Override
    protected void reiniciar() {
        if (fxPlayer != null) {
            fxPlayer.detener("fondo");
        }
        this.puntosJ1 = 0;
        this.puntosJ2 = 0;
        this.rankingRegistrado = false;
        this.estado = EstadoJuego.MENU;
    }
}