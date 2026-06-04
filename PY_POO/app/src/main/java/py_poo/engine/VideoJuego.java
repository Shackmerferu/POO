package py_poo.engine;

import java.util.ArrayList;
import java.util.List;

import py_poo.core.GameLoop;
import py_poo.entities.ObjetoGrafico;
import py_poo.input.InputManager;
import py_poo.interfaces.JuegoLoopable;
import py_poo.loderunner.Nivel;
import py_poo.ranking.RankingManager;

public abstract class VideoJuego implements JuegoLoopable {
    protected String Nombre;
    protected boolean Activo;
    protected EstadoJuego estado;
    protected List<Integer> Puntuacion;
    protected Nivel NivelActual;
    protected List<ObjetoGrafico> Entidades;
    private int ResX;
    private int ResY;
    protected boolean Fullscreen;
    protected List<Jugador> Jugador;
    protected String Resultado;
    protected RankingManager rankingManager = new RankingManager();
    protected String nombreJugadorPrincipal;
    protected InputManager input;
    protected boolean soundEnabled = true;
    private boolean lastCtrlState;
    private boolean lastBackslashState;
    private boolean lastPauseState;

    public void iniciar() {
        this.Activo = true;
        this.estado = EstadoJuego.MENU;
        this.Entidades = new ArrayList<>();
        this.Puntuacion = new ArrayList<>();
        this.Jugador = new ArrayList<>();
        iniciapuntaje(null, null);
    }

    public void actualizar() {
        if (!Activo) {
            return;
        }
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
                getResultado();
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

        boolean ctrlNow = input.isCtrlPressed();
        if (ctrlNow && !lastCtrlState) {
            soundEnabled = !soundEnabled;
            System.out.println("Sonido: " + (soundEnabled ? "ON" : "OFF"));
        }
        lastCtrlState = ctrlNow;

        boolean backslashNow = input.isBackslashPressed();
        if (backslashNow && !lastBackslashState) {
            GameLoop.toggleFullscreenStatic();
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

    public void renderizar(java.awt.Graphics g) {
        for (ObjetoGrafico entidad : Entidades) {
            entidad.display(g);
        }

    }

    public abstract String getGanador();

    public abstract String getPerdedor();

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
