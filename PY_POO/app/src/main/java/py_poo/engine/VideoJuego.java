package py_poo.engine;

import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.ArrayList;
import java.util.List;

import com.entropyinteractive.JGame;
import com.entropyinteractive.Keyboard;
import com.entropyinteractive.Mouse;
import com.entropyinteractive.MouseWheel;

import py_poo.config.ConfigManager;
import py_poo.core.Constantes;
import py_poo.entities.ObjetoGrafico;
import py_poo.input.InputManager;
import py_poo.loderunner.Nivel;
import py_poo.ranking.RankingManager;

public abstract class VideoJuego extends JGame {
    private static VideoJuego instancia;
    private static double deltaTime;
    private boolean isFullscreen;

    protected String Nombre;
    protected boolean Activo;
    protected EstadoJuego estado;
    protected List<Integer> Puntuacion;
    protected Nivel NivelActual;
    protected List<ObjetoGrafico> Entidades;
    protected boolean Fullscreen;
    protected List<Jugador> Jugador;
    protected String Resultado;
    protected RankingManager rankingManager = new RankingManager();
    protected String nombreJugadorPrincipal;
    protected InputManager input;
    protected boolean soundEnabled = true;
    protected boolean soundFxEnabled = true;
    protected boolean musicEnabled = true;
    protected ConfigManager configManager = new ConfigManager();
    protected Camara camara;

    private boolean lastBackslashState;
    private boolean lastPauseState;
    private boolean lastQState;
    private boolean lastMState;

    public VideoJuego(String title, int width, int height) {
        super(title, width, height);
        instancia = this;
    }

    public static double getDeltaTime() {
        return deltaTime;
    }

    public static Keyboard getTeclado() {
        return instancia != null ? instancia.getKeyboard() : null;
    }

    public static Mouse getRaton() {
        return instancia != null ? instancia.getMouse() : null;
    }

    public static MouseWheel getRuedaRaton() {
        return instancia != null ? instancia.getMouseWheel() : null;
    }

    public static void terminarJuego() {
        if (instancia != null) {
            instancia.stop();
        }
    }

    public void toggleFullscreen() {
        GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
        Frame window = null;
        for (Frame f : Frame.getFrames()) {
            if (f.isVisible() && f.getWidth() > 100 && f.getHeight() > 100) {
                window = f;
                break;
            }
        }
        if (window == null) return;
        if (isFullscreen) {
            gd.setFullScreenWindow(null);
            window.setSize(Constantes.WIDTH, Constantes.HEIGHT);
            window.setLocationRelativeTo(null);
            isFullscreen = false;
        } else {
            gd.setFullScreenWindow(window);
            isFullscreen = true;
        }
    }

    public static void toggleFullscreenStatic() {
        if (instancia != null) {
            instancia.toggleFullscreen();
        }
    }

    public static boolean isFullscreen() {
        return instancia != null && instancia.isFullscreen;
    }

    @Override
    public void gameStartup() {
        iniciar();
        for (Frame f : Frame.getFrames()) {
            if (f.isVisible()) {
                f.addWindowListener(new WindowListener() {
                    public void windowClosing(WindowEvent e) {
                        terminarJuego();
                    }
                    public void windowOpened(WindowEvent e) {}
                    public void windowClosed(WindowEvent e) {}
                    public void windowIconified(WindowEvent e) {}
                    public void windowDeiconified(WindowEvent e) {}
                    public void windowActivated(WindowEvent e) {}
                    public void windowDeactivated(WindowEvent e) {}
                });
                break;
            }
        }
    }

    @Override
    public void gameUpdate(double delta) {
        deltaTime = delta;
        actualizar();
    }

    @Override
    public void gameDraw(Graphics2D g) {
        double sx = (double) getWidth() / Constantes.WIDTH;
        double sy = (double) getHeight() / Constantes.HEIGHT;
        g.scale(sx, sy);
        renderizar(g);
    }

    @Override
    public void gameShutdown() {
        finalizar();
    }

    public void run(int fps) {
        super.run(1.0 / fps);
    }

    public void iniciar() {
        this.Activo = true;
        this.estado = EstadoJuego.MENU;
        this.Entidades = new ArrayList<>();
        this.Puntuacion = new ArrayList<>();
        this.Jugador = new ArrayList<>();
        iniciapuntaje(null, null);

        this.camara = new Camara();

        configManager.cargar();
        this.soundEnabled = configManager.isSoundEnabled();
        this.soundFxEnabled = configManager.isSoundFxEnabled();
        this.musicEnabled = configManager.isMusicEnabled();

        if (configManager.isFullscreen() && !VideoJuego.isFullscreen()) {
            VideoJuego.toggleFullscreenStatic();
        }
    }

    public void actualizar() {
        if (!Activo) { return; }

        manejarControlesGlobales();

        switch (estado) {
            case MENU:
                actualizarLogicaJuego();
                break;
            case JUGANDO:
                actualizarLogicaJuego();
                break;
            case PAUSA:
                actualizarLogicaJuego();
            case GAME_OVER:
                actualizarLogicaJuego();
                break;
            case VICTORIA:
                actualizarLogicaJuego();
                break;
        }
    }

    private void manejarControlesGlobales() {
        if (input == null) return;

        boolean pNow = input.isPPressed();
        if (pNow && !lastPauseState && (estado == EstadoJuego.JUGANDO || estado == EstadoJuego.PAUSA)) {
            pausa();
        }
        lastPauseState = pNow;

        if (input.isEscapePressed()) {
            if (estado == EstadoJuego.JUGANDO || estado == EstadoJuego.PAUSA) {
                reiniciar();
            }
        }

        boolean qNow = input.isQPressed();
        if (qNow && !lastQState) {
            soundFxEnabled = !soundFxEnabled;
            System.out.println("Efectos de sonido: " + (soundFxEnabled ? "ON" : "OFF"));
        }
        lastQState = qNow;

        boolean mNow = input.isMPressed();
        if (mNow && !lastMState) {
            musicEnabled = !musicEnabled;
            System.out.println("Música: " + (musicEnabled ? "ON" : "OFF"));
        }
        lastMState = mNow;

        boolean backslashNow = input.isBackslashPressed();
        if (backslashNow && !lastBackslashState) {
            VideoJuego.toggleFullscreenStatic();
            configManager.setFullscreen(VideoJuego.isFullscreen());
            configManager.guardar();
        }
        lastBackslashState = backslashNow;
    }

    public void finalizar() {
        finalizar(EstadoJuego.GAME_OVER, "Juego cerro repentinamente");
    }

    protected void finalizar(EstadoJuego estadoFinal, String resultado) {
        this.Activo = false;
        this.estado = estadoFinal;
        this.Resultado = resultado;

        if (Jugador!=null && Puntuacion!=null){
            for (int i = 0; i < Jugador.size(); i++) {
                String nombre=Jugador.get(i).getNombre();
                int puntos= i< Puntuacion.size() ? Puntuacion.get(i):0;
                rankingManager.agregarPuntaje(nombre,this.Nombre,getNivelActual(),puntos);
            }
        }

        if (this.NivelActual != null) {
            this.NivelActual.finalizarNivel();
        }
        if (this.Entidades != null) {
            this.Entidades.clear();
        }
    }

    protected void pausa() {
        if (!Activo)
            return;
        if (this.estado == EstadoJuego.JUGANDO) {
            this.estado = EstadoJuego.PAUSA;
            System.out.println("JUEGO EN PAUSA");
        } else if (this.estado == EstadoJuego.PAUSA) {
            this.estado = EstadoJuego.JUGANDO;
            System.out.println("JUEGO REANUDADO");
        }
    }


    protected void crearPartida() {
        Entidades.clear();
        iniciapuntaje(null, null);
        renderizar(null);
        this.estado = EstadoJuego.JUGANDO;

    }

    protected abstract void actualizarLogicaJuego();

    protected void reiniciar() {

        resetPuntaje();

        this.estado = EstadoJuego.MENU;
        this.Activo = true;
        this.Resultado = null;

        if (Entidades != null) {
            Entidades.clear();
        }
        if (NivelActual != null) {
            NivelActual.finalizarNivel();
            NivelActual = null;
        }
        iniciar();

    }

    public void cargarNivel() {

        if (Entidades != null) {
            Entidades.clear();
        }

        if (NivelActual == null) {
            System.out.println("ERROR NO HAY MAS NIVEL PAPA");
            return;
        }

        NivelActual.cargar();

        this.estado = EstadoJuego.JUGANDO;
        this.Activo = true;
        System.out.println("Nivel cargado: " + NivelActual.toString());
    }

    public String getResultado() {
        return Resultado;
    }

    public void renderizar(Graphics g) {
        boolean usarCamara = camara != null && (estado == EstadoJuego.JUGANDO || estado == EstadoJuego.PAUSA || estado == EstadoJuego.GAME_OVER);
        if (usarCamara) {
            g.translate(-camara.getX(), -camara.getY());
        }

        for (ObjetoGrafico entidad : Entidades) {
            entidad.display(g);
        }
        if (usarCamara) {
            g.translate(camara.getX(), camara.getY());
        }
    }

    public List<Integer> getpuntaje() {
        return Puntuacion;
    }

    public void iniciapuntaje(Jugador J1, Jugador J2) {
        try {
            if (J1 != null) {
                Puntuacion.add(0);
            }
            if (J2 != null) {
                Puntuacion.add(0);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void sumarPunto( int id, int Puntaje){
        Puntuacion.set(id, Puntuacion.get(id) + Puntaje);
    }

    public void resetPuntaje() {
        Puntuacion.clear();
    }

    public void setNombreJugador(String nombre) {
        this.nombreJugadorPrincipal = nombre;
    }

    public void setNombreJuego(String nombre) {
        this.Nombre = nombre;
    }

    public String getNombreJuego() {
        return Nombre;
    }

    protected int getNivelActual() {
        return NivelActual != null ? NivelActual.getNumero() : 1;
    }
}
