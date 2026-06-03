package py_poo.loderunner;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import py_poo.config.KeyBindings;
import py_poo.core.Constantes;
import py_poo.input.InputManager;
import py_poo.ui.MenuPrincipal;
import py_poo.engine.EstadoJuego;
import py_poo.engine.VideoJuego;
import py_poo.entities.Agujero;
import py_poo.entities.Barra;
import py_poo.entities.Escalera;
import py_poo.entities.Ladrillo;
import py_poo.entities.Moneda;
import py_poo.entities.Personaje;

public class JuegoLodeRunner extends VideoJuego {

    private InputManager input;
    private MenuLodeRunner menu;

    private Personaje heroe;
    private List<Personaje> guardias;
    private List<Nivel> niveles;
    private int nivelIndex;

    private int puntaje;
    private boolean nivelCompletado;
    private boolean esperandoReinicio;
    private int tiempoEspera;

    private static final int ANIMACION_FRAME_SPEED = 8;
    private static final int VISOR_TILES = 12;
    private static final int VISOR_PIXELS = VISOR_TILES * 40;
    private int frameCounter;
    private boolean enTransicion;
    private float camX, camY;

    private Random rand = new Random();

    public JuegoLodeRunner() {
        this.Nombre = "Lode Runner";
    }

    @Override
    public void iniciar() {
        super.iniciar();
        this.input = new InputManager();
        super.input = this.input;
        this.menu = new MenuLodeRunner(input, null);
        this.puntaje = 0;
        this.estado = EstadoJuego.MENU;
        this.enTransicion = false;
        this.esperandoReinicio = false;
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
                    GameLoop.terminarJuego(); // vuelve al Launcher
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
        if (estado == EstadoJuego.JUGANDO) {
            actualizarJuego();
        
        }
        if (estado == EstadoJuego.GAME_OVER || estado == EstadoJuego.VICTORIA) {
            actualizarGameOver();
        }

    }
    private void actualizarMenu() {
        if (input.isEnterPressed() && !enTransicion) {
            enTransicion = true;
            crearPartida();
        }
    }

    private void actualizarGameOver() {
        if (input.isEnterPressed()) {
            reiniciar();
        }
    }

    @Override
    protected void crearPartida() {
        super.crearPartida();
        this.puntaje = 0;
        this.nivelIndex = 0;
        this.niveles = new ArrayList<>();
        niveles.add(new Nivel1());
        niveles.add(new Nivel2());
        niveles.add(new Nivel3());

        this.guardias = new ArrayList<>();
        this.estado = EstadoJuego.JUGANDO;
        this.nivelCompletado = false;
        this.enTransicion = false;
        this.esperandoReinicio = false;

        cargarNivelActual();
    }

    private void cargarNivelActual() {
        if (nivelIndex >= niveles.size()) {
            estado = EstadoJuego.VICTORIA;
            finalizar(EstadoJuego.VICTORIA, "Ganaste todos los niveles!");
            return;
        }

        Nivel nivel = niveles.get(nivelIndex);
        nivel.cargar();
        this.NivelActual = nivel;

        int tx = nivel.spawnRecolectorX;
        int ty = nivel.spawnRecolectorY;
        if (tx == 0 && ty == 0) {
            tx = 1; ty = 1;
        }
        heroe = new Recolector(tx, ty, nivel.getTile_size());

        heroe.setNivelOroTotal(nivel.totalOro);

        Entidades.clear();
        Entidades.add(heroe);

        guardias.clear();
        for (int[] sp : nivel.spawnGuardias) {
            Guardia g = new Guardia(sp[0], sp[1], nivel.getTile_size());
            guardias.add(g);
            Entidades.add(g);
        }

        for (Moneda m : nivel.monedas) Entidades.add(m);
        for (Escalera e : nivel.escaleras) Entidades.add(e);
        for (Barra b : nivel.barras) Entidades.add(b);
        for (Ladrillo l : nivel.ladrillos) Entidades.add(l);
        for (Ladrillo l : nivel.ladrillosIrrompibles) Entidades.add(l);
        nivelCompletado = false;
        System.out.println("Nivel " + (nivelIndex + 1) + " cargado. Oro total: " + nivel.totalOro);
    }

    private void reiniciarNivel() {
        Nivel nivel = niveles.get(nivelIndex);
        nivel.finalizarNivel();
        nivel.cargar();

        int tx = nivel.spawnRecolectorX;
        int ty = nivel.spawnRecolectorY;
        heroe = new Recolector(tx, ty, nivel.getTile_size());
        heroe.setNivelOroTotal(nivel.totalOro);
        heroe.setVidas(Recolector.VIDAS_INICIALES);

        Entidades.clear();
        Entidades.add(heroe);

        guardias.clear();
        for (int[] sp : nivel.spawnGuardias) {
            Guardia g = new Guardia(sp[0], sp[1], nivel.getTile_size());
            guardias.add(g);
            Entidades.add(g);
        }

        for (Moneda m : nivel.monedas) Entidades.add(m);
        for (Escalera e : nivel.escaleras) Entidades.add(e);
        for (Barra b : nivel.barras) Entidades.add(b);
        for (Ladrillo l : nivel.ladrillos) Entidades.add(l);
        for (Ladrillo l : nivel.ladrillosIrrompibles) Entidades.add(l);
        estado = EstadoJuego.JUGANDO;
    }

    private void actualizarJuego() {
        if (heroe == null || NivelActual == null) return;

        frameCounter++;
        heroe.actualizar();
        for (Guardia g : guardias) g.actualizar();
        NivelActual.actualizar();

        if (heroe.getVidas() <= 0) {
            estado = EstadoJuego.GAME_OVER;
            finalizar(EstadoJuego.GAME_OVER, "Te quedaste sin vidas!");
            return;
        }

        manejarInputHeroe();
        aplicarGravedad();
        aplicarMovimientoGuardias();
        verificarRecoleccionOro();
        verificarColisionesGuardias();
        verificarAgujeros();
        verificarNivelCompleto();
        actualizarCamara();
    }

    
    

    @Override
    public String getGanador() {
        return nombreJugadorPrincipal;
    }

    @Override
    public String getPerdedor() {
        return nombreJugadorPrincipal;
    }

    private float clamp(float v, float min, float max) {
        return Math.max(min, Math.min(max, v));
    }

    public void setNombreJugador(String nombre) {
        this.nombreJugadorPrincipal = nombre;
    }
}
