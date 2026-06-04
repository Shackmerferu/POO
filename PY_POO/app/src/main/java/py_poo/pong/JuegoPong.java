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
    private boolean OpJuego;
    private InputManager input;
    private MenuPong menu;
    private Paleta paleta1;
    private Paleta paleta2;
    private PelotaPong pelota;
    private CollisionManager collisionManager;
    private FXPlayer fxPlayer;
    private int puntosJ1;         //puntaje jugador 1
    private int puntosJ2;         //puntaje jugador 2
    private int PUNTOS_MAX = 11; // configurable desde Launcher
    private BufferedImage fondo; // fondo escalado a 800x600
    private boolean modoIA; // true = vs IA, false = 2 jugadores
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

        // incia el sonido y carga los sonidos
        this.fxPlayer = new FXPlayer();
        this.fxPlayer.cargarSonido("rebote", "sonidos/paleta.wav");
        this.fxPlayer.cargarSonido("punto", "sonidos/punto.wav");
        this.fxPlayer.cargarSonido("inicio", "sonidos/Empieza.wav");
        this.fxPlayer.cargarSonido("fondo", "sonidos/SoundTrack.wav");
        this.fxPlayer.setVolumen("fondo", "bajo");

        // seteamos valores
        this.puntosJ1 = 0;
        this.puntosJ2 = 0;
        this.rankingRegistrado = false;
        this.estado = EstadoJuego.MENU; // empieza en el menu

        //cargamos la imagen de la chancha
        CargadorRecursos cr = new CargadorRecursos();
        this.fondo = cr.cargarImagen("imagenes/Pong/Fondo Pong.png");
    }

    public void pause() {
        estado = EstadoJuego.PAUSA;
    }
    // Metodos de Renderizados(dibujamos a 60fps)
    @Override
    public void renderizar(Graphics g) {
        super.renderizar(g);
        Graphics2D g2d = (g instanceof Graphics2D) ? (Graphics2D) g : null;
        if (g2d != null) {
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON); // suaviza bordes
            g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON); // suaviza texto
        }
        //estamos en el menu lo dibujamos
        if (this.estado == EstadoJuego.MENU && menu != null) {
            menu.dibujar(g);
        }
        // Si estamos en el juego dibujamos, tabien si esta en pasusa o si termina, se dibuja la cancha

        if (estado == EstadoJuego.JUGANDO || estado == EstadoJuego.PAUSA || estado == EstadoJuego.GAME_OVER) {
            if (fondo != null) {
                g.drawImage(fondo, 0, 0, Constantes.WIDTH, Constantes.HEIGHT, null);
            } else {
                g.setColor(Color.BLACK);
                g.fillRect(0, 0, Constantes.WIDTH, Constantes.HEIGHT);
            }

            // si ya existen las entidades ñas dibujamos

            if (paleta1 != null) paleta1.dibujar(g);
            if (paleta2 != null) paleta2.dibujar(g);
            if (pelota != null) pelota.display(g);

            // se dibuja los marcadores de puntaje
            g.setFont(new Font("Consolas", Font.BOLD, 36));
            g.setColor(Color.WHITE);
            g.drawString(String.valueOf(puntosJ1), Constantes.WIDTH / 4, 50);
            g.drawString(String.valueOf(puntosJ2), 3 * Constantes.WIDTH / 4, 50);

            // se dibuja el cartel pausa al medio
            if (estado == EstadoJuego.PAUSA) {
                g.setFont(new Font("Consolas", Font.BOLD, 48));
                g.setColor(Color.YELLOW);
                g.drawString("PAUSA", Constantes.WIDTH / 2 - 80, Constantes.HEIGHT / 2);
                g.setFont(new Font("Consolas", Font.PLAIN, 20));
                g.setColor(Color.GRAY);
                g.drawString("P = reanudar  |  ESC = menu", Constantes.WIDTH / 2 - 160, Constantes.HEIGHT / 2 + 50);
            }
                // dibujamos cartel de que perdio
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
        //se muestra un cartelito de las configuraciones que puede cambiar mientras juega y con que teclas
        g.setFont(new Font("Consolas", Font.PLAIN, 12));
        g.setColor(new Color(255, 255, 255, 100));
        g.drawString((soundEnabled ? "SONIDO:ON" : "SONIDO:OFF") + "  |  \\ = pantalla completa", 10, Constantes.HEIGHT - 10);
    }
    // todos los metodos para crear partida(todo para crear un partido nuevo)
    @Override
    protected void crearPartida() {
        paleta1 = new Paleta(input, 1);
        paleta1.ResetearPOS();
        paleta2 = new Paleta(input, 2);
        paleta2.ResetearPOS();
        pelota = new PelotaPong();
        puntosJ1 = 0;
        puntosJ2 = 0;
        rankingRegistrado = false;
        this.estado = EstadoJuego.JUGANDO;
        //elige jugar con el bot
        if (modoIA) {
            ia = new IA_Pong(pelota, paleta2, 1);
        } else {
            ia = null;
        }
        //sonido de inicio y de la hinchada
        if (soundEnabled) fxPlayer.reproducir("inicio");
        if (soundEnabled) fxPlayer.repetir("fondo");
    }
    // obtenemos el nombre, ya sea que gano o perdio para anotarlo en el ranking
    @Override
    public String getGanador() {
        return Nombre;
    }

    @Override
    public String getPerdedor() {
        return Nombre;
    }

    @Override
    protected void actualizarLogicaJuego() {
        // aca empezamos a ver como se comporta el menu
        if (this.estado == EstadoJuego.MENU) {
            if (menu.isConfigMode()) {
                menu.actualizarConfig();
                return;
            }
            ///  aca nos movemos por el menu y por las opciones
            if (input.isMenuUpPressed() || input.isWPressed()) {
                menu.setSeleccion(Math.max(0, menu.getSeleccion() - 1));
            }
            if (input.isMenuDownPressed() || input.isSPressed()) {
                menu.setSeleccion(Math.min(3, menu.getSeleccion() + 1));
            }
            // enter elige la opcion
            if (input.isEnterPressed()) {
                if (menu.getSeleccion() == 3)
                {
                    if (fxPlayer != null) fxPlayer.detener("fondo");// pone salir apaga la musica
                    GameLoop.terminarJuego(); // vuelve al Launcher
                    return;
                }
                if (menu.getSeleccion() == 2) {
                    menu.setConfigMode(true);
                    return;
                }
                modoIA = (menu.getSeleccion() == 0); // opcion 0 = vs IA, opcion 1 = 2 jugadores
                crearPartida();
            }
            return;
        }
        // termina el partido
        if (this.estado == EstadoJuego.GAME_OVER) {
            if (input.isEnterPressed()) {
                this.estado = EstadoJuego.MENU;
            }
            return;
        }
            // si esta en juego (logica)
        if (this.estado == EstadoJuego.JUGANDO) {
            //movimiento J1
            if (paleta1 != null) paleta1.Mover();
            //movimiento J2 o BOT
            if (modoIA) {
                if (ia != null) ia.calcularMovimiento();
            } else {
                if (paleta2 != null) paleta2.Mover();
            }
            // se mueve la pelota y fija si colisiona
            if (pelota != null) {
                pelota.mover();
                pelota.rebotarParedes();
                // colision J1
                if (collisionManager.colisiona(pelota, paleta1)) {
                    pelota.rebotarPaleta(paleta1);
                    if (soundEnabled) fxPlayer.reproducir("rebote");
                }
                // Colision J1
                if (collisionManager.colisiona(pelota, paleta2)) {
                    pelota.rebotarPaleta(paleta2);
                    if (soundEnabled) fxPlayer.reproducir("rebote");
                }
                // sale la pelota del lado que recibio el gol
                if (pelota.salioIzquierda()) {
                    puntosJ2++;
                    if (soundEnabled) fxPlayer.reproducir("punto");
                    paleta1.ResetearPOS();
                    paleta2.ResetearPOS();
                    pelota.reiniciar(false); // sirve hacia la izquierda (perdedor)
                }
                // sale la pelota del lado que recibio el gol
                if (pelota.salioDerecha()) {
                    puntosJ1++;
                    if (soundEnabled) fxPlayer.reproducir("punto");
                    if (modoIA && ia != null) ia.incrementarDificultad(); // IA mas rapida con cada punto
                    paleta1.ResetearPOS();
                    paleta2.ResetearPOS();
                    pelota.reiniciar(true); // sirve hacia la derecha (perdedor)
                }
                //alguien alcazo los puntos para ganar
                if (puntosJ1 >= PUNTOS_MAX || puntosJ2 >= PUNTOS_MAX) {
                    estado = EstadoJuego.GAME_OVER;
                    registrarRankingFinal();//lo guardamos en BD

                }
            }
        }
    }
//guardamos los puntajes
    private void registrarRankingFinal() {
        //evita que se repita el resultado
        if (rankingRegistrado) {
            return;
        }
        if (nombreJugadorPrincipal == null || nombreJugadorPrincipal.isBlank()) {
            return;
        }
        // arma el texto que se muestra en el ranking del menu
        String resultadoStr = (puntosJ1 >= PUNTOS_MAX) ? "(Ganó)" : "(Perdió)";
        String modoStr = modoIA ? "vs IA" : "vs J2";
        String nombreJuegoDetalle = "Pong " + modoStr + " [" + puntosJ1 + "-" + puntosJ2 + "] " + resultadoStr;
        //Llama al Manager para ejecutar el INSERT INTO en SQLite
        rankingManager.agregarPuntaje(nombreJugadorPrincipal, nombreJuegoDetalle, getNivelActual(), puntosJ1);
        rankingRegistrado = true;
    }

}
