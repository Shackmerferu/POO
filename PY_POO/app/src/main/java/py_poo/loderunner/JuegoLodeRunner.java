package py_poo.loderunner;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import py_poo.audio.FXPlayer;
import py_poo.core.Constantes;
import py_poo.engine.VideoJuego;
import py_poo.engine.EstadoJuego;
import py_poo.engine.Jugador;
import py_poo.engine.VideoJuego;
import py_poo.input.InputManager;
import py_poo.interfaces.EventResult;
import py_poo.interfaces.GameEvent;
import py_poo.interfaces.GameEventListener;
import py_poo.utils.CargadorRecursos;
import py_poo.ranking.RankingManager;
import py_poo.ranking.RankingManager.RankingEntry;

public class JuegoLodeRunner extends VideoJuego implements GameEventListener {

    public JuegoLodeRunner() {
        super("Lode Runner", Constantes.WIDTH, Constantes.HEIGHT);
        this.Nombre = "Lode Runner";
    }

    private InputManager input;
    private MenuLodeRunner menu;
    private Recolector recolector;
    private List<Guardia> guardias;
    private List<Nivel> niveles;
    private int nivelIdx;
    private int puntosJ1;
    private boolean rankingRegistrado;
    private int tiempoNivel;
    private BufferedImage fondo;
    private FXPlayer fxPlayer;

    // VARIABLES DE RANKING AGREGADAS
    private RankingManager rankingManager;
    private List<RankingEntry> topRankingLodeRunner;

    @Override
    public void iniciar() {
        // TRUCO DE MEMORIA: Guardamos la elección antes de que el motor reinicie
        int viejoVolumen = (this.menu != null) ? this.menu.getVolumenIndex() : 0;

        super.iniciar();
        this.input = new InputManager();
        super.input = this.input;
        this.menu = new MenuLodeRunner(input, null);

        // Le devolvemos la memoria al menú nuevo
        this.menu.setVolumenIndex(viejoVolumen);

        this.rankingManager = new RankingManager();

        this.puntosJ1 = 0;
        this.rankingRegistrado = false;
        this.estado = EstadoJuego.MENU;

        CargadorRecursos cr = new CargadorRecursos();
        this.fondo = cr.cargarImagen("imagenes/Lode Runner/fondo negro.png");

        this.fxPlayer = new FXPlayer();
        this.fxPlayer.cargarSonidoRecurso("punto", "sonidos/punto.wav");
        this.fxPlayer.cargarSonidoRecurso("paleta", "sonidos/paleta.wav");
        this.fxPlayer.cargarSonidoRecurso("Empieza", "sonidos/Empieza.wav");
        this.fxPlayer.cargarSonidoRecurso("CancionFondoLodeRunner", "sonidos/LodeRunner/CancionFondoLodeRunner.wav");

        // Asignamos el volumen inicial de la memoria a los efectos
        String volumenEfectos = this.menu.getVolumenString();
        this.fxPlayer.setVolumen("punto", volumenEfectos);
        this.fxPlayer.setVolumen("paleta", volumenEfectos);
        this.fxPlayer.setVolumen("Empieza", volumenEfectos);
    }

    @Override
    protected void reiniciar() {
        if (fxPlayer != null) {
            fxPlayer.detener("CancionFondoLodeRunner");
        }
        super.reiniciar();
    }

    public void pause() {
        estado = EstadoJuego.PAUSA;
    }

    @Override
    public void renderizar(Graphics g) {
        Graphics2D g2d = (g instanceof Graphics2D) ? (Graphics2D) g : null;
        if (g2d != null) {
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        }
        if (this.estado == EstadoJuego.MENU && menu != null) {
            menu.dibujar(g);
            return;
        }
        if (fondo != null) {
            g.drawImage(fondo, 0, 0, Constantes.WIDTH, Constantes.HEIGHT, null);
        } else {
            g.setColor(Color.BLACK);
            g.fillRect(0, 0, Constantes.WIDTH, Constantes.HEIGHT);
        }

        super.renderizar(g);

        if (estado == EstadoJuego.JUGANDO && recolector != null) {
            g.setFont(new Font("Consolas", Font.BOLD, 18));
            g.setColor(Color.WHITE);
            g.drawString("NIVEL " + (nivelIdx + 1), 10, 25);
            g.drawString("ORO: " + recolector.getOroRecolectado() + "/" + recolector.getNivelOroTotal(), 10, 48);
            g.drawString("VIDAS: " + recolector.getVidas(), 10, 71);
            g.drawString("PUNTOS: " + puntosJ1, 10, 94);
            if (NivelActual != null) {
                int restante = Math.max(0, NivelActual.tiempoLimite * 60 - tiempoNivel) / 60;
                g.drawString("TIEMPO: " + restante + "s", 10, 117);
            } else {
                g.drawString("TIEMPO: --", 10, 117);
            }
        }

        if (estado == EstadoJuego.PAUSA) {
            g.setFont(new Font("Consolas", Font.BOLD, 48));
            g.setColor(Color.YELLOW);
            g.drawString("PAUSA", Constantes.WIDTH / 2 - 80, Constantes.HEIGHT / 2);
            g.setFont(new Font("Consolas", Font.PLAIN, 20));
            g.setColor(Color.GRAY);
            g.drawString("P = reanudar  |  ESC = menu", Constantes.WIDTH / 2 - 160, Constantes.HEIGHT / 2 + 50);
        }

        if (estado == EstadoJuego.VICTORIA) {
            g.setFont(new Font("Consolas", Font.BOLD, 40));
            g.setColor(new Color(80, 255, 80));
            g.drawString("¡VICTORIA!", Constantes.WIDTH / 2 - 130, 120);

            g.setFont(new Font("Consolas", Font.BOLD, 24));
            g.setColor(Color.WHITE);
            g.drawString("Puntaje: " + puntosJ1, Constantes.WIDTH / 2 - 80, 180);
            g.drawString("Niveles completados: " + niveles.size(), Constantes.WIDTH / 2 - 130, 210);

            g.setFont(new Font("Consolas", Font.BOLD, 18));
            g.setColor(Color.CYAN);
            g.drawString("--- TOP 10 ---", Constantes.WIDTH / 2 - 70, 260);

            g.setFont(new Font("Consolas", Font.PLAIN, 14));
            g.setColor(Color.WHITE);
            int y = 290;
            if (topRankingLodeRunner == null || topRankingLodeRunner.isEmpty()) {
                g.drawString("Aún no hay puntajes.", Constantes.WIDTH / 2 - 80, y);
            } else {
                for (int i = 0; i < topRankingLodeRunner.size(); i++) {
                    var entry = topRankingLodeRunner.get(i);
                    String texto = String.format("%d. %s  N%d  %d pts", (i + 1), entry.jugador(), entry.Nivel(), entry.puntaje());
                    g.drawString(texto, Constantes.WIDTH / 2 - 150, y);
                    y += 22;
                }
            }

            g.setFont(new Font("Consolas", Font.PLAIN, 18));
            g.setColor(Color.GRAY);
            g.drawString("ENTER = volver al menú", Constantes.WIDTH / 2 - 120, Constantes.HEIGHT - 80);
        }

        if (estado == EstadoJuego.GAME_OVER) {
            g.setFont(new Font("Consolas", Font.BOLD, 40));
            g.setColor(new Color(255, 80, 80));
            g.drawString("GAME OVER", Constantes.WIDTH / 2 - 140, 120);

            g.setFont(new Font("Consolas", Font.BOLD, 24));
            g.setColor(Color.WHITE);
            g.drawString("Puntaje: " + puntosJ1, Constantes.WIDTH / 2 - 80, 180);
            g.drawString("Nivel: " + (nivelIdx + 1), Constantes.WIDTH / 2 - 60, 210);

            g.setFont(new Font("Consolas", Font.BOLD, 18));
            g.setColor(Color.CYAN);
            g.drawString("--- TOP 10 ---", Constantes.WIDTH / 2 - 70, 260);

            g.setFont(new Font("Consolas", Font.PLAIN, 14));
            g.setColor(Color.WHITE);
            int y = 290;
            if (topRankingLodeRunner == null || topRankingLodeRunner.isEmpty()) {
                g.drawString("Aún no hay puntajes.", Constantes.WIDTH / 2 - 80, y);
            } else {
                for (int i = 0; i < topRankingLodeRunner.size(); i++) {
                    var entry = topRankingLodeRunner.get(i);
                    String texto = String.format("%d. %s  N%d  %d pts", (i + 1), entry.jugador(), entry.Nivel(), entry.puntaje());
                    g.drawString(texto, Constantes.WIDTH / 2 - 150, y);
                    y += 22;
                }
            }

            g.setFont(new Font("Consolas", Font.PLAIN, 18));
            g.setColor(Color.GRAY);
            g.drawString("ENTER = volver al menú", Constantes.WIDTH / 2 - 120, Constantes.HEIGHT - 80);
        }

        g.setFont(new Font("Consolas", Font.PLAIN, 12));
        g.setColor(new Color(255, 255, 255, 100));
        String snd = "FX:" + (soundFxEnabled ? "ON" : "OFF") + " MUS:" + (soundEnabled ? "ON" : "OFF")
                + " Q=FX M=MUS CTRL=GLOBAL \\=FULL";
        g.drawString(snd, 10, Constantes.HEIGHT - 10);
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
        tiempoNivel = 0;

        String jugadorAsegurado = (nombreJugadorPrincipal != null && !nombreJugadorPrincipal.isBlank()) ? nombreJugadorPrincipal : "Jugador 1";
        Jugador.clear();
        Jugador.add(new Jugador(jugadorAsegurado));
        estado = EstadoJuego.JUGANDO;

        // ESTRUCTURA FIJA Y SEGURA: Igual que en Pong y Space Invaders
        if (soundEnabled) {
            fxPlayer.detener("CancionFondoLodeRunner");
            fxPlayer.repetir("CancionFondoLodeRunner");
            fxPlayer.setVolumen("CancionFondoLodeRunner", menu.getVolumenString());
        }

        cargarNivelActual();
    }

    private void cargarNivelActual() {
        if (nivelIdx >= niveles.size()) {
            estado = EstadoJuego.VICTORIA;
            if (estado == EstadoJuego.VICTORIA) {
                if (input.isEnterPressed()) {
                    if (fxPlayer != null) {
                        fxPlayer.detener("CancionFondoLodeRunner");
                    }
                    if (menu != null) {
                        menu.recargarRanking();
                    }
                    this.estado = EstadoJuego.MENU;
                }
            }
            return;
        }

        Nivel nivel = niveles.get(nivelIdx);
        tiempoNivel = 0;
        nivel.cargar();
        this.NivelActual = nivel;

        int tx = nivel.spawnRecolectorX;
        int ty = nivel.spawnRecolectorY;
        if (tx == 0 && ty == 0) {
            tx = 1;
            ty = 1;
        }

        recolector = new Recolector(tx, ty, nivel.getTile_size());
        recolector.setGameEventListener(this);
        recolector.setInputManager(input);
        recolector.setNivel(nivel);
        recolector.setNivelOroTotal(nivel.totalOro);
        String skin = menu.getSkinPersonaje() == 0 ? "original" : "alternativo";
        recolector.setSkin(skin);
        Entidades.clear();
        guardias = new ArrayList<>();
        for (var l : nivel.ladrillos) Entidades.add(l);
        for (var l : nivel.ladrillosIrrompibles) Entidades.add(l);
        for (var e : nivel.escaleras) Entidades.add(e);
        for (var b : nivel.barras) Entidades.add(b);
        for (var m : nivel.monedas) Entidades.add(m);

        for (int[] sp : nivel.spawnGuardias) {
            Guardia g = new Guardia(sp[0], sp[1], nivel.getTile_size());
            g.setRecolector(recolector);
            g.setNivel(nivel);
            g.setSkin(skin);
            guardias.add(g);
            Entidades.add(g);
        }
        recolector.setGuardias(guardias);
        Entidades.add(recolector);
    }

    @Override
    protected void actualizarLogicaJuego() {
        if (estado == EstadoJuego.MENU) {
            if (menu.isConfigMode()) {
                menu.actualizarConfig();

                // Actualizamos música y efectos dinámicamente en el menú
                String volActualizado = this.menu.getVolumenString();
                this.fxPlayer.setVolumen("punto", volActualizado);
                this.fxPlayer.setVolumen("paleta", volActualizado);
                this.fxPlayer.setVolumen("Empieza", volActualizado);
                this.fxPlayer.setVolumen("CancionFondoLodeRunner", volActualizado);

                configManager.guardar();
                return;
            }
            if (input.isMenuUpPressed() || input.isWPressed()) menu.navegarMainMenu(-1);
            if (input.isMenuDownPressed() || input.isSPressed()) menu.navegarMainMenu(1);
            if (input.isEnterPressed()) {
                if (menu.getSeleccion() == 2) {
                    VideoJuego.terminarJuego();
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

        if (estado == EstadoJuego.PAUSA) {
            if (input.isPPressed()) {
                estado = EstadoJuego.JUGANDO;
                if (soundEnabled) {
                    fxPlayer.repetir("CancionFondoLodeRunner");
                    fxPlayer.setVolumen("CancionFondoLodeRunner", menu.getVolumenString());
                }
            } else if (input.isEscapePressed()) {
                estado = EstadoJuego.MENU;
                fxPlayer.detener("CancionFondoLodeRunner");
            }
            return;
        }

        if (estado == EstadoJuego.GAME_OVER || estado == EstadoJuego.VICTORIA) {
            if (input.isEnterPressed()) {
                estado = EstadoJuego.MENU;
                fxPlayer.detener("CancionFondoLodeRunner");
            }
            return;
        }

        if (estado != EstadoJuego.JUGANDO || recolector == null || NivelActual == null) return;

        Nivel nivelActual = (Nivel) this.NivelActual;

        recolector.mover();
        if (camara != null) camara.seguirJugador(recolector, nivelActual);

        nivelActual.actualizar();
        tiempoNivel++;

        for (Guardia g : guardias) {
            if (g != null) g.mover();
        }

        recolector.verificarCaidaEnAgujero();
        recolector.recolectarMonedas();
        recolector.verificarColisionGuardias();

        for (Guardia g : guardias) {
            if (g == null) continue;
            g.intentarRecolectarOro();
            if (g.manejarColisionAgujero(nivelActual.agujeros, guardias)) {
                puntosJ1 += 200;
            }
        }

        nivelActual.sincronizarEntidades(Entidades);

        if (recolector.nivelCompleto() && !nivelActual.escapeLadderActiva) {
            nivelActual.activarEscape();
        }

        if (nivelActual.escapeLadderActiva) {
            int rtx = (int) ((recolector.getX() + nivelActual.getTile_size() / 2) / nivelActual.getTile_size());
            int rty = (int) ((recolector.getY() + nivelActual.getTile_size() / 2) / nivelActual.getTile_size());
            if (rtx == nivelActual.escapeLadderX && rty == nivelActual.escapeLadderY) {
                int bonusTiempo = Math.max(0, (nivelActual.tiempoLimite * 60 - tiempoNivel) / 6);
                puntosJ1 += 500 + bonusTiempo;
                recolector.setVidas(recolector.getVidas() + 1);
                if (soundEnabled && soundFxEnabled) fxPlayer.reproducir("empieza");
                nivelActual.finalizarNivel();
                nivelIdx++;

                if (nivelIdx >= niveles.size()) {
                    String jugador = (nombreJugadorPrincipal != null && !nombreJugadorPrincipal.isBlank()) ? nombreJugadorPrincipal : "Jugador 1";
                    rankingManager.agregarPuntaje(jugador, "Lode Runner", nivelIdx, puntosJ1);
                    if (menu != null) menu.recargarRanking();

                    topRankingLodeRunner = rankingManager.cargarDetalleTop("Lode%", 10);

                    estado = EstadoJuego.VICTORIA;
                    fxPlayer.detener("CancionFondoLodeRunner");
                    return;
                }
                cargarNivelActual();
            }
        }
    }

    @Override
    public EventResult onEvent(GameEvent event) {
        switch (event) {
            case HERO_DEATH:
                if (recolector.getVidas() <= 0) {
                    if (!rankingRegistrado) {
                        String jugador = (nombreJugadorPrincipal != null && !nombreJugadorPrincipal.isBlank()) ? nombreJugadorPrincipal : "Jugador 1";
                        rankingManager.agregarPuntaje(jugador, "Lode Runner", nivelIdx + 1, puntosJ1);
                        if (menu != null) menu.recargarRanking();
                        rankingRegistrado = true;
                    }
                    recolector.desaparecer();
                    topRankingLodeRunner = rankingManager.cargarDetalleTop("Lode%", 10);
                    estado = EstadoJuego.GAME_OVER;
                    fxPlayer.detener("CancionFondoLodeRunner");
                    return EventResult.GAME_OVER;
                } else {
                    if (soundEnabled && soundFxEnabled) fxPlayer.reproducir("empieza");
                    Nivel nivelActual = (Nivel) this.NivelActual;
                    int vidasGuardadas = recolector.getVidas();
                    nivelActual.finalizarNivel();
                    cargarNivelActual();
                    recolector.setVidas(vidasGuardadas);
                    return EventResult.CONTINUE;
                }
            case COIN_COLLECTED:
                puntosJ1 += 100;
                if (soundEnabled && soundFxEnabled) fxPlayer.reproducir("punto");
                return EventResult.CONTINUE;
            case DIG:
                if (soundEnabled && soundFxEnabled) fxPlayer.reproducir("paleta");
                return EventResult.CONTINUE;
        }
        return EventResult.CONTINUE;
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