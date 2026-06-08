package py_poo.engine;

import java.util.ArrayList;
import java.util.List;

import py_poo.config.ConfigManager;
import py_poo.core.GameLoop;
import py_poo.entities.ObjetoGrafico;
import py_poo.input.InputManager;
import py_poo.interfaces.JuegoLoopable;
import py_poo.loderunner.Nivel;
import py_poo.ranking.RankingManager;

// clase padre que utilizamos para los 3 juegos
public abstract class VideoJuego implements JuegoLoopable {


    // ayuda memoria
    // Usamos 'protected' para que los juegos hijos puedan leer y modificar estas variables directamente.
    protected String Nombre;
    protected boolean Activo;                 // Si es false, el GameLoop deja de actualizar este juego
    protected EstadoJuego estado;             // Máquina de estados: MENU, JUGANDO, PAUSA, GAME_OVER
    protected List<Integer> Puntuacion;
    protected Nivel NivelActual;
    protected List<ObjetoGrafico> Entidades;  // Lista  de todo lo que se dibuja en pantalla

    // Configuraciones gráficas y de hardware
    private int ResX;
    private int ResY;
    protected boolean Fullscreen;
    protected List<Jugador> Jugador;
    protected String Resultado;

    // Herramientas utilizadas en los juegos (Bases de datos, Teclado, Sonido, Cámara)
    protected RankingManager rankingManager = new RankingManager();
    protected String nombreJugadorPrincipal;
    protected InputManager input;
    protected boolean soundEnabled = true;
    protected boolean soundFxEnabled = true;
    protected boolean musicEnabled = true;
    protected ConfigManager configManager = new ConfigManager();
    protected Camara camara;

    // "Anti-rebotes" para teclas globales (Evita que presionar Pausa una vez parpadee el juego 60 veces)
    private boolean lastBackslashState;
    private boolean lastPauseState;
    private boolean lastQState;
    private boolean lastMState;


    public void iniciar() {
        this.Activo = true;
        this.estado = EstadoJuego.MENU; // Todos los juegos arrancan en su propio menú
        this.Entidades = new ArrayList<>();
        this.Puntuacion = new ArrayList<>();
        this.Jugador = new ArrayList<>();
        iniciapuntaje(null, null);

        this.camara = new Camara();

        // Carga las configuraciones guardadas en disco (Sonido y Pantalla completa)
        configManager.cargar();
        this.soundEnabled = configManager.isSoundEnabled();
        this.soundFxEnabled = configManager.isSoundFxEnabled();
        this.musicEnabled = configManager.isMusicEnabled();

        // Si el jugador había dejado el juego en pantalla completa, lo restaura
        if (configManager.isFullscreen() && !GameLoop.isFullscreen()) {
            GameLoop.toggleFullscreenStatic();
        }
    }


    public void actualizar() {
        if (!Activo) { return; } // Si el juego se cerró, corta la ejecución por seguridad

        manejarControlesGlobales(); // Lee las teclas que funcionan en cualquier momento (Pausa, ESC, Volumen)

        // MÁQUINA DE ESTADOS: Dependiendo de en qué pantalla estemos, delega el trabajo
        // al método abstracto 'actualizarLogicaJuego()' que cada juego hijo programó a su manera.
        switch (estado) {
            case MENU:
            case JUGANDO:
            case PAUSA:
            case GAME_OVER:
                actualizarLogicaJuego();
                break;
            case VICTORIA:
                getResultado();
                break;
        }
    }


    private void manejarControlesGlobales() {
        if (input == null) return;

        // TECLA P: Pausa / Reanudar
        boolean pNow = input.isPPressed();
        if (pNow && !lastPauseState && (estado == EstadoJuego.JUGANDO || estado == EstadoJuego.PAUSA)) {
            pausa();
        }
        lastPauseState = pNow;

        // TECLA ESC: Volver al menú principal del juego actual
        if (input.isEscapePressed()) {
            if (estado == EstadoJuego.JUGANDO || estado == EstadoJuego.PAUSA) {
                reiniciar();
            }
        }

        // TECLA Q: Mutear Efectos
        boolean qNow = input.isQPressed();
        if (qNow && !lastQState) {
            soundFxEnabled = !soundFxEnabled;
            System.out.println("Efectos de sonido: " + (soundFxEnabled ? "ON" : "OFF"));
        }
        lastQState = qNow;

        // TECLA M: Mutear Música
        boolean mNow = input.isMPressed();
        if (mNow && !lastMState) {
            musicEnabled = !musicEnabled;
            System.out.println("Música: " + (musicEnabled ? "ON" : "OFF"));
        }
        lastMState = mNow;

        // TECLA \ : Alternar Pantalla Completa
        boolean backslashNow = input.isBackslashPressed();
        if (backslashNow && !lastBackslashState) {
            GameLoop.toggleFullscreenStatic();
            configManager.setFullscreen(GameLoop.isFullscreen());
            configManager.guardar(); // Guarda la preferencia en el archivo de config
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

        //  Guarda automáticamente los puntajes en SQLite al terminar
        if (Jugador!=null && Puntuacion!=null){
            for (int i = 0; i < Jugador.size(); i++) {
                String nombre=Jugador.get(i).getNombre();
                int puntos= i< Puntuacion.size() ? Puntuacion.get(i):0;
                rankingManager.agregarPuntaje(nombre, this.Nombre, getNivelActual(), puntos);
            }
        }

        // Limpieza de memoria
        if (this.NivelActual != null) {
            this.NivelActual.finalizarNivel();
        }
        if (this.Entidades != null) {
            this.Entidades.clear(); // Borra todos los enemigos/pelotas de la RAM
        }
    }


    protected void pausa() {
        if (!Activo) return;
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
    public abstract String getGanador();
    public abstract String getPerdedor();

    protected void reiniciar() {
        resetPuntaje();
        this.estado = EstadoJuego.MENU;
        this.Activo = true;
        this.Resultado = null;

        if (Entidades != null) Entidades.clear();
        if (NivelActual != null) {
            NivelActual.finalizarNivel();
            NivelActual = null;
        }
        iniciar();
    }


    public void cargarNivel() {
        if (Entidades != null) Entidades.clear();
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


    public void renderizar(java.awt.Graphics g) {
        boolean usarCamara = camara != null && (estado == EstadoJuego.JUGANDO || estado == EstadoJuego.PAUSA || estado == EstadoJuego.GAME_OVER);

        if (usarCamara) {

            g.translate(-camara.getX(), -camara.getY());
        }

        // Pinta todo los objetos
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
            if (J1 != null) Puntuacion.add(0);
            if (J2 != null) Puntuacion.add(0);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void sumarPunto(int id, int Puntaje) {
        Puntuacion.set(id, Puntuacion.get(id) + Puntaje);
    }

    public void resetPuntaje() {
        Puntuacion.clear();
    }

    // --- GETTERS Y SETTERS COMPLEMENTARIOS ---
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