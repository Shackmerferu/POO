package py_poo.loderunner;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import py_poo.collision.CollisionManager;
import py_poo.core.Constantes;
import py_poo.core.GameLoop;
import py_poo.engine.EstadoJuego;
import py_poo.engine.VideoJuego;
import py_poo.entities.Agujero;
import py_poo.input.InputManager;
import py_poo.utils.CargadorRecursos;

public class JuegoLodeRunner extends VideoJuego {

    private InputManager input;
    private MenuLodeRunner menu;
    private CollisionManager collisionManager;
    private Recolector heroe;
    private List<Guardia> guardias;
    private List<Nivel> niveles;
    private int nivelIdx;
    private int puntosJ1;
    private boolean rankingRegistrado;
    private BufferedImage fondo;

    public JuegoLodeRunner() {
        this.Nombre = "Lode Runner";
    }

    @Override
    public void iniciar() {
        super.iniciar();
        this.input = new InputManager();
        super.input = this.input;
        this.menu = new MenuLodeRunner(input, null);
        this.collisionManager = new CollisionManager();
        this.puntosJ1 = 0;
        this.rankingRegistrado = false;
        this.estado = EstadoJuego.MENU;
        CargadorRecursos cr = new CargadorRecursos();
        this.fondo = cr.cargarImagen("imagenes/Lode Runner/fondo negro.png");
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
        if (this.estado == EstadoJuego.MENU && menu != null) {
            menu.dibujar(g);
        }
        if (estado == EstadoJuego.JUGANDO || estado == EstadoJuego.PAUSA || estado == EstadoJuego.GAME_OVER) {
            if (fondo != null) {
                g.drawImage(fondo, 0, 0, Constantes.WIDTH, Constantes.HEIGHT, null);
            } else {
                g.setColor(Color.BLACK);
                g.fillRect(0, 0, Constantes.WIDTH, Constantes.HEIGHT);
            }

            if (heroe != null)
                heroe.display(g);
            if (guardias != null)
                for (Guardia gu : guardias)
                    gu.display(g);

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
                if (puntosJ1 >= 0) {
                    ganador = "JUGADOR 1";
                } else {
                    ganador = "JUGADOR 2";
                }
                g.drawString("GANADOR: " + ganador, Constantes.WIDTH / 2 - 200, Constantes.HEIGHT / 2);
                g.setFont(new Font("Consolas", Font.PLAIN, 20));
                g.setColor(Color.GRAY);
                g.drawString("Presiona ENTER para volver al menu", Constantes.WIDTH / 2 - 160,
                        Constantes.HEIGHT / 2 + 50);
            }
        }

        g.setFont(new Font("Consolas", Font.PLAIN, 12));
        g.setColor(new Color(255, 255, 255, 100));
        g.drawString((soundEnabled ? "SONIDO:ON" : "SONIDO:OFF") + "  |  \\ = pantalla completa", 10,
                Constantes.HEIGHT - 10);
    }

    @Override
    protected void crearPartida() {
        niveles = new ArrayList<>();
        niveles.add(new Nivel1());
        niveles.add(new Nivel2());
        niveles.add(new Nivel3());

        nivelIdx = 0;
        puntosJ1 = 0;
        rankingRegistrado = false;
        estado = EstadoJuego.JUGANDO;

        cargarNivelActual();
    }

    private void cargarNivelActual() {
        if (nivelIdx >= niveles.size()) {
            estado = EstadoJuego.VICTORIA;
            finalizar(EstadoJuego.VICTORIA, "Ganaste todos los niveles!");
            return;
        }

        Nivel nivel = niveles.get(nivelIdx);
        nivel.cargar();
        this.NivelActual = nivel;

        int tx = nivel.spawnRecolectorX;
        int ty = nivel.spawnRecolectorY;
        if (tx == 0 && ty == 0) {
            tx = 1;
            ty = 1;
        }

        heroe = new Recolector(tx, ty, nivel.getTile_size());

        Entidades.clear();
        Entidades.add(heroe);

        guardias = new ArrayList<>();
        for (int[] sp : nivel.spawnGuardias) {
            Guardia g = new Guardia(sp[0], sp[1], nivel.getTile_size());
            guardias.add(g);
            Entidades.add(g);
        }

        for (var m : nivel.monedas)
            Entidades.add(m);
        for (var e : nivel.escaleras)
            Entidades.add(e);
        for (var b : nivel.barras)
            Entidades.add(b);
        for (var l : nivel.ladrillos)
            Entidades.add(l);
        for (var l : nivel.ladrillosIrrompibles)
            Entidades.add(l);
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
                if (menu.getSeleccion() == 2) {
                    GameLoop.terminarJuego();
                    return;
                }
                if (menu.getSeleccion() == 1) {
                    menu.setConfigMode(true);
                    return;
                }
                crearPartida();
            }
            return;
        }

        if (this.estado == EstadoJuego.GAME_OVER) {
            if (input.isEnterPressed()) {
                this.estado = EstadoJuego.MENU;
            }
            return;
        }

        if (this.estado == EstadoJuego.JUGANDO) {
            if (heroe != null)
                heroe.mover();
            Nivel nivelActual = (Nivel) this.NivelActual;
            for (Guardia g : guardias) {
                if (g != null)
                    g.mover();
                if (collisionManager.colisiona(g, heroe)) {
                    boolean heroearriba = heroe.getY() + heroe.getHeight() <= g.getY() + g.getHeight() + 5;
                    boolean hayAgujeroAbierto = false;
                    for (Agujero a : nivelActual.agujeros) {
                        if (a.isAbierto()) {
                            hayAgujeroAbierto = true;
                            break;
                        }
                        if (heroearriba && hayAgujeroAbierto) {
                            heroe.setY(g.getY() - heroe.getHeight());
                        } else {
                            heroe.perderVida();
                            if (heroe.getVidas() <= 0) {
                                heroe.desaparecer();
                                this.estado = EstadoJuego.GAME_OVER;
                            }
                        }
                        if (collisionManager.colisiona(a, g)) {
                            if(a.getTiempoRestante()<5000){
                                g.moverArriba();
                                g.mover();
                            }
                        }

                    }

                }
            }
        }
    }

    @Override
    public String getGanador() {
        return nombreJugadorPrincipal;
    }

    @Override
    public String getPerdedor() {
        return nombreJugadorPrincipal;
    }

    public void setNombreJugador(String nombre) {
        this.nombreJugadorPrincipal = nombre;
    }
}
