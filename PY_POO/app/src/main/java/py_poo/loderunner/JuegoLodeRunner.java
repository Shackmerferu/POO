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
import py_poo.collision.CollisionManager;
import py_poo.core.Constantes;
import py_poo.core.GameLoop;
import py_poo.engine.EstadoJuego;
import py_poo.engine.Jugador;
import py_poo.engine.VideoJuego;
import py_poo.entities.Agujero;
import py_poo.entities.Escalera;
import py_poo.entities.Moneda;
import py_poo.entities.ParticulaLadrillo;
import py_poo.entities.Puerta;
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
    private int tiempoNivel;
    private BufferedImage fondo;
    private FXPlayer fxPlayer;
    private boolean musicaIniciada;

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
        this.musicaIniciada = false;
        CargadorRecursos cr = new CargadorRecursos();
        this.fondo = cr.cargarImagen("imagenes/Lode Runner/fondo negro.png");

        fxPlayer = new FXPlayer();
        fxPlayer.cargarSonidoRecurso("punto", "sonidos/punto.wav");
        fxPlayer.cargarSonidoRecurso("paleta", "sonidos/paleta.wav");
        fxPlayer.cargarSonidoRecurso("empieza", "sonidos/Empieza.wav");
        fxPlayer.cargarSonidoRecurso("soundtrack", "sonidos/SoundTrack.wav");
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

        if (estado == EstadoJuego.JUGANDO && heroe != null) {
            g.setFont(new Font("Consolas", Font.BOLD, 18));
            g.setColor(Color.WHITE);
            g.drawString("NIVEL " + (nivelIdx + 1), 10, 25);
            g.drawString("ORO: " + heroe.getOroRecolectado() + "/" + heroe.getNivelOroTotal(), 10, 48);
            g.drawString("VIDAS: " + heroe.getVidas(), 10, 71);
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

            var top = new py_poo.ranking.RankingManager().cargarDetalleTop("Lode%", 10);
            g.setFont(new Font("Consolas", Font.PLAIN, 14));
            g.setColor(Color.WHITE);
            int y = 290;
            if (top == null || top.isEmpty()) {
                g.drawString("Aún no hay puntajes.", Constantes.WIDTH / 2 - 80, y);
            } else {
                for (int i = 0; i < top.size(); i++) {
                    var entry = top.get(i);
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
        String snd = "FX:" + (soundFxEnabled ? "ON" : "OFF") + " MUS:" + (musicEnabled ? "ON" : "OFF")
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
        Jugador.clear();
        Jugador.add(new Jugador(nombreJugadorPrincipal));
        estado = EstadoJuego.JUGANDO;

        musicaIniciada = false;
        cargarNivelActual();
    }

    private void cargarNivelActual() {
        if (nivelIdx >= niveles.size()) {
            estado = EstadoJuego.VICTORIA;
            finalizar(EstadoJuego.VICTORIA, "Ganaste todos los niveles!");
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

        heroe = new Recolector(tx, ty, nivel.getTile_size());
        heroe.setInputManager(input);
        heroe.setNivel(nivel);
        heroe.setNivelOroTotal(nivel.totalOro);
        Entidades.clear();
        guardias = new ArrayList<>();
        for (var l : nivel.ladrillos)
            Entidades.add(l);
        for (var l : nivel.ladrillosIrrompibles)
            Entidades.add(l);
        for (var e : nivel.escaleras)
            Entidades.add(e);
        for (var b : nivel.barras)
            Entidades.add(b);
        for (var m : nivel.monedas)
            Entidades.add(m);
        for (int[] sp : nivel.spawnGuardias) {
            Guardia g = new Guardia(sp[0], sp[1], nivel.getTile_size());
            g.setHeroe(heroe);
            g.setNivel(nivel);
            guardias.add(g);
            Entidades.add(g);
        }
        Entidades.add(heroe);
    }
    @Override
    protected void actualizarLogicaJuego() {
        if (this.estado == EstadoJuego.MENU) {
            if (menu.isConfigMode()) {
                menu.actualizarConfig();
                configManager.guardar();
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
                musicaIniciada = false;
            }
            return;
        }
        if (this.estado == EstadoJuego.JUGANDO) {
            if (soundEnabled && musicEnabled) {
                if (!musicaIniciada) {
                    fxPlayer.repetir("soundtrack");
                    musicaIniciada = true;
                }
            } else if (musicaIniciada) {
                fxPlayer.detener("soundtrack");
                musicaIniciada = false;
            }
            if (heroe == null) return;
            Nivel nivelActual = (Nivel) this.NivelActual;
            if (nivelActual == null) return;

            heroe.mover();
            if (camara != null) {
                camara.seguirJugador(heroe, nivelActual);
            }
            if (soundEnabled && soundFxEnabled && heroe.cavoEsteFrame()) {
                fxPlayer.reproducir("paleta");
            }
            tiempoNivel++;
            nivelActual.actualizar();

            for (Guardia g : guardias) {
                if (g != null) g.mover();
            }

            // JUGADOR vs monedas
            for (var it = nivelActual.monedas.iterator(); it.hasNext();) {
                var m = it.next();
                if (!m.isRecolectada() && collisionManager.colisiona(heroe, m)) {
                    m.recolectar();
                    heroe.recogerOro();
                    puntosJ1 += 100;
                    if (soundEnabled && soundFxEnabled) fxPlayer.reproducir("punto");
                }
            }

            // GUARDIAS vs oro
            for (Guardia g : guardias) {
                if (g == null || g.isCargandoOro()) continue;
                for (var m : nivelActual.monedas) {
                    if (!m.isRecolectada() && collisionManager.colisiona(g, m)) {
                        m.recolectar();
                        g.setMonedaCargada(m);
                        break;
                    }
                }
            }

            // JUGADOR vs guardias
            for (Guardia g : guardias) {
                if (g == null) continue;
                if (collisionManager.colisiona(g, heroe)) {
                    if (g.enAgujero()) continue;
                    boolean puedeBajar = input.isDownPressed() || input.isSPressed();
                    boolean heroearriba = heroe.getY() + heroe.getHeight() <= g.getY() + g.getHeight() + 5;
                    boolean hayAgujeroAbierto = false;
                    for (Agujero a : nivelActual.agujeros) {
                        if (a.isAbierto()) { hayAgujeroAbierto = true; break; }
                    }
                    if (heroearriba && hayAgujeroAbierto && !puedeBajar) {
                        heroe.setY(g.getY() - heroe.getHeight());
                    } else {
                        heroe.perderVida();
                        if (soundEnabled && soundFxEnabled) fxPlayer.reproducir("empieza");
                        if (heroe.getVidas() <= 0) {
                            if (!rankingRegistrado) {
                                rankingManager.agregarPuntaje(nombreJugadorPrincipal, "Lode Runner", nivelIdx + 1, puntosJ1);
                                if (menu != null) menu.recargarRanking();
                                rankingRegistrado = true;
                            }
                            heroe.desaparecer();
                            this.estado = EstadoJuego.GAME_OVER;
                            fxPlayer.detener("soundtrack");
                            return;
                        } else {
                            int vidasGuardadas = heroe.getVidas();
                            nivelActual.finalizarNivel();
                            cargarNivelActual();
                            heroe.setVidas(vidasGuardadas);
                            return;
                        }
                    }
                }
            }

            // GUARDIAS vs agujeros
            for (Guardia g : guardias) {
                if (g == null) continue;
                if (g.enAgujero()) {
                    boolean enAlgunAgujero = false;
                    for (Agujero a : nivelActual.agujeros) {
                        if (collisionManager.colisiona(a, g)) {
                            enAlgunAgujero = true;
                            if (a.getTiempoRestante() < 90) {
                                g.setY(g.getY() - Guardia.VELOCIDAD);
                                if (!collisionManager.colisiona(a, g)) {
                                    g.enAgujero(false);
                                    g.getIA().reaparecer();
                                }
                            }
                            break;
                        }
                    }
                    if (!enAlgunAgujero) {
                        soltarOroGuardia(g, nivelActual);
                        g.reaparecer();
                        puntosJ1 += 200;
                    }
                } else {
                    for (Agujero a : nivelActual.agujeros) {
                        if (collisionManager.colisiona(a, g)) {
                            if (g.isCargandoOro()) {
                                soltarOroGuardia(g, nivelActual);
                            }
                            g.enAgujero(true);
                            g.setCayendo(false);
                            g.getIA().atrapar();
                            break;
                        }
                    }
                }
            }

            // Sincronizar entidades dinámicas de Nivel al renderer
            for (Agujero a : nivelActual.agujeros) {
                if (!Entidades.contains(a)) Entidades.add(0, a);
            }
            for (ParticulaLadrillo p : nivelActual.particulas) {
                if (!Entidades.contains(p)) Entidades.add(p);
            }
            for (Escalera e : nivelActual.escaleras) {
                if (!Entidades.contains(e)) Entidades.add(e);
            }
            Puerta puerta = nivelActual.puertaSalida;
            if (puerta != null && !Entidades.contains(puerta)) {
                Entidades.add(puerta);
            }
            Entidades.removeIf(e ->
                (e instanceof ParticulaLadrillo && !nivelActual.particulas.contains(e))
                || (e instanceof Agujero && !nivelActual.agujeros.contains(e))
                || (e instanceof Escalera && !nivelActual.escaleras.contains(e))
                || (e instanceof Puerta && e != nivelActual.puertaSalida));

            // Escalera de escape
            if (heroe.nivelCompleto() && !nivelActual.escapeLadderActiva) {
                nivelActual.activarEscape();
            }

            if (nivelActual.escapeLadderActiva) {
                int htx = (int)((heroe.getX() + nivelActual.getTile_size() / 2) / nivelActual.getTile_size());
                int hty = (int)((heroe.getY() + nivelActual.getTile_size() / 2) / nivelActual.getTile_size());
                if (htx == nivelActual.escapeLadderX && hty == nivelActual.escapeLadderY) {
                    int bonusTiempo = Math.max(0, (nivelActual.tiempoLimite * 60 - tiempoNivel) / 6);
                    puntosJ1 += 500 + bonusTiempo;
                    heroe.setVidas(heroe.getVidas() + 1);
                    if (soundEnabled && soundFxEnabled) fxPlayer.reproducir("empieza");
                    nivelActual.finalizarNivel();
                    nivelIdx++;
                    if (nivelIdx >= niveles.size()) {
                        rankingManager.agregarPuntaje(nombreJugadorPrincipal, "Lode Runner", nivelIdx + 1, puntosJ1);
                        if (menu != null) menu.recargarRanking();
                        estado = EstadoJuego.VICTORIA;
                        fxPlayer.detener("soundtrack");
                        finalizar(EstadoJuego.VICTORIA, "Ganaste todos los niveles!");
                        return;
                    }
                    cargarNivelActual();
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

    private void soltarOroGuardia(Guardia g, Nivel nivel) {
        if (!g.isCargandoOro()) return;
        int tx = g.getTileX();
        int ty = g.getTileY();
        Moneda suelta = new Moneda(tx, ty, nivel.getTile_size());
        nivel.monedas.add(suelta);
        Entidades.add(suelta);
        g.setMonedaCargada(null);
    }

    public void setNombreJugador(String nombre) {
        this.nombreJugadorPrincipal = nombre;
    }

    @Override
    protected void reiniciar() {
        fxPlayer.detener("soundtrack");
        musicaIniciada = false;
        super.reiniciar();
    }
}
