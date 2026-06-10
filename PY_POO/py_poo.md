classDiagram
direction BT
class Agujero {
  + Agujero(int, int, Ladrillo) 
  + Agujero(int, int) 
  - Ladrillo ladrilloAsociado
  - boolean abierto
  + actualizar() void
  + abrir() void
  + display(Graphics) void
  + cerrar() void
   - boolean abierto
   - Ladrillo ladrilloAsociado
   - float progreso
   - int tiempoRestante
}
class Animacion {
  + Animacion(List~Sprite~, long) 
  - boolean repitiendo
  + reiniciar() void
  + obtenerFrame() Sprite
  + termino() boolean
  + dibujar(Graphics, int, int) void
  + actualizar() void
  + dibujar(Graphics, int, int, int, int) void
   - boolean repitiendo
}
class App {
  + App() 
  + main(String[]) void
}
class Armado {
<<Interface>>
  + Disparar() Bala
}
class Bala {
  + Bala() 
  + Mover() void
  + impactar() void
}
class Barra {
  + Barra(int, int, int) 
  + deslizar() void
  + colgar() void
  + display(Graphics) void
  - cargarSprite() void
}
class Bloque {
  + Bloque() 
  # int valor
  + recoger() void
  + destruir() void
   - int valor
}
class Camara {
  + Camara() 
  - int Y
  - int X
  + seguirJugador(ObjetoGrafico, Nivel) void
   - int Y
   - int X
}
class CargadorRecursos {
  + CargadorRecursos() 
  + cargarImagen(String) BufferedImage
  + cargarSonido() void
}
class CollisionManager {
  + CollisionManager() 
  + verificarColisiones(List~ObjetoGrafico~) void
  + colisiona(ObjetoGrafico, ObjetoGrafico) boolean
}
class ConfigManager {
  + ConfigManager() 
  - float volumen
  - boolean soundFxEnabled
  - boolean fullscreen
  - boolean musicEnabled
  - boolean soundEnabled
  + leer() void
  + cargar() void
  + escribir() void
  + guardar() void
  - aplicarKeyBindings() void
   - boolean soundEnabled
   - boolean soundFxEnabled
   - boolean musicEnabled
   - boolean fullscreen
   - float volumen
}
class Constantes {
  + Constantes() 
}
class Enemigo {
  + Enemigo(int, int, Animacion) 
  + display(Graphics) void
  + actualizacionAnimacion() void
   - int puntos
}
class EnemigoA {
  + EnemigoA(int, int, int) 
  + Disparar() Bala
  - animacionFlota(int) Animacion
}
class EnemigoB {
  + EnemigoB(int, int, int) 
  - animacionFlota(int) Animacion
}
class EnemigoC {
  + EnemigoC(int, int, int) 
  - animacionFlota(int) Animacion
}
class Escalera {
  + Escalera(int, int, int) 
  + display(Graphics) void
  - cargarAnimacion() void
}
class Escudo {
  + Escudo(int, int) 
  - SegmentoEscudo[] segmentos
   - SegmentoEscudo[] segmentos
}
class EstadoJuego {
<<enumeration>>
  + EstadoJuego() 
  + valueOf(String) EstadoJuego
  + values() EstadoJuego[]
}
class FXPlayer {
  + FXPlayer() 
  + repetir(String) void
  + setVolumen(String, String) void
  + mutear() void
  + cargarSonidoRecurso(String, String) void
  + reproducir(String) void
  + detener(String) void
}
class GameEventListener {
<<Interface>>
  + onCoinCollected() void
  + onGameOver() void
  + onHeroDeath() void
  + onDig() void
}
class GameLoop {
  + GameLoop(String, int, int) 
  - boolean isFullscreen
  - double deltaTime
  + toggleFullscreenStatic() void
  + terminarJuego() void
  + toggleFullscreen() void
  + gameStartup() void
  + gameShutdown() void
  + gameUpdate(double) void
  + gameDraw(Graphics2D) void
  + run(int) void
   - Keyboard? teclado
   - JuegoLoopable videoJuego
   - MouseWheel? ruedaRaton
   - boolean isFullscreen
   - double deltaTime
   - Mouse? raton
}
class Guardia {
  + Guardia(int, int, int) 
  + static final double VELOCIDAD
  - IA_Guardia ia
  - Recolector heroe
  - Nivel nivel
  - boolean enAgujero
  - boolean enEscalera
  - boolean enBarra
  - boolean cayendo
  - Moneda monedaCargada
  - boolean enAire
  - int tileSize
  - int spawnTileX
  - int spawnTileY
  - String skin
  - int tiempoEsperaEscape
  - int contadorAtascado
  - Animacion animCaminando
  - Animacion animAtrapado
  - Animacion animEscalera
  - Animacion animBarra
  - Animacion animCayendo
  + moverDerecha() void
  + actualizar() void
  + moverAbajo() void
  + intentarRecolectarOro() void
  - cargarAnimaciones() void
  + iniciarEscape(int) void
  - hayAgujeroSeguro(int, int) boolean
  + manejarColisionAgujero(List~Agujero~, List~Guardia~) boolean
  + display(Graphics) void
  - aplicarGravedad() void
  - tieneSoporte(int, int) boolean
  - cargarAnimacion(CargadorRecursos, String, String, int, long) Animacion
  + moverIzquierda() void
  + soltarMoneda() void
  - detectarPlataforma() void
  + moverArriba() void
  + reaparecer() void
  + mover() void
  + getTileX() int
  + getTileY() int
  + getIA() IA_Guardia
  + isEnEscalera() boolean
  + isEnBarra() boolean
  + isCayendo() boolean
  + isCargandoOro() boolean
  + isEnAire() boolean
  + enAgujero() boolean
  + enAgujero(boolean) void
  + setEnEscalera(boolean) void
  + setEnBarra(boolean) void
  + setCayendo(boolean) void
  + setEnAire(boolean) void
  + setSkin(String) void
  + getSkin() String
  + setHeroe(Recolector) void
  + setNivel(Nivel) void
  + getMonedaCargada() Moneda
  + setMonedaCargada(Moneda) void
}
class Hitbox {
  + Hitbox(int, int, int, int) 
  - int x
  - int width
  - int height
  - int y
  + setPosicion(int, int) void
  + setDimension(int, int) void
   - int y
   - Rectangle bounds
   - int x
   - int height
   - int width
}
class IA_Guardia {
  + IA_Guardia() 
  - int TIEMPO_ESCAPE
  - int tiempoAtrapado
  - Comportamiento estado
  - int TIEMPO_ESPERA_ESCAPE
  + atrapar() void
  + salir() void
  + reaparecer() void
  + incrementarTiempoAtrapado() void
  + reanimar() void
  + cambiarAPersecucion() void
  - calcularVagar(int, int, int, int, boolean, boolean, boolean, boolean, boolean, boolean) int
  + calcularMovimiento(int, int, int, int, boolean, boolean, boolean, boolean, boolean, boolean) int
  - calcularPersecucion(int, int, int, int, boolean, boolean, boolean, boolean, boolean, boolean) int
   - int TIEMPO_ESCAPE
   - boolean persiguiendo
   - Comportamiento estado
   - int tiempoAtrapado
   - int TIEMPO_ESPERA_ESCAPE
   - boolean saliendo
}
class IA_Pong {
  + IA_Pong(PelotaPong, Paleta, int) 
  - int dificultad
  + incrementarDificultad() void
  + calcularMovimiento() void
   - int dificultad
}
class InputManager {
  + InputManager() 
  + isKeyPressed(int) boolean
   - boolean menuUpPressed
   - boolean rightPressed
   - boolean QPressed
   - boolean PPressed
   - boolean digPressed
   - boolean WPressed
   - boolean menuDownPressed
   - boolean leftPressed
   - boolean escapePressed
   - boolean upPressed
   - boolean downPressed
   - boolean backslashPressed
   - boolean MPressed
   - boolean enterPressed
   - boolean SPressed
   - boolean spacePressed
   - boolean ctrlPressed
}
class JuegoLodeRunner {
  + JuegoLodeRunner() 
  + onCoinCollected() void
  - cargarNivelActual() void
  - gestionarMusica() void
  + renderizar(Graphics) void
  + onHeroDeath() void
  # reiniciar() void
  + iniciar() void
  + onGameOver() void
  + pause() void
  + onDig() void
  # actualizarLogicaJuego() void
  # crearPartida() void
   - String nombreJugador
   - String ganador
   - String perdedor
}
class JuegoLoopable {
<<Interface>>
  + actualizar() void
  + finalizar() void
  + iniciar() void
  + renderizar(Graphics) void
}
class JuegoPong {
  + JuegoPong() 
  - boolean OpJuego
  + renderizar(Graphics) void
  - registrarRankingFinal() void
  + iniciar() void
  + pause() void
  # crearPartida() void
  # reiniciar() void
  # actualizarLogicaJuego() void
   - boolean OpJuego
   - String perdedor
   - int puntosMax
   - String ganador
}
class JuegoSpaceInvaders {
  + JuegoSpaceInvaders() 
  # actualizarLogicaJuego() void
  + iniciar() void
  + renderizar(Graphics) void
  + pause() void
  - registrarRankingFinal() void
  # crearPartida() void
   - String ganador
   - String perdedor
}
class Jugador {
  + Jugador(String) 
  - String Nombre
  + getid() int
   - String Nombre
}
class KeyBindings {
  + KeyBindings() 
  + get(String) int
  + set(String, int) void
  + keyName(int) String
   - String[] actionNames
}
class Ladrillo {
  + Ladrillo(int, int, int, boolean) 
  - boolean irrompible
  - Estado estado
  + actualizar() void
  + iniciarBreaking() void
  + iniciarRegen() void
  + display(Graphics) void
  - cargarAnimaciones() void
   - Estado estado
   - boolean roto
   - boolean irrompible
}
class Laser {
  + Laser(int, int, int, String) 
  - int velocidad
  + actualizar() void
   - int velocidad
}
class Launcher {
  + Launcher() 
  - buildCard(GameEntry, boolean, int) JPanel
  - hline() JSeparator
  - scrollToFocused() void
  - darkCheck(String, boolean) JCheckBox
  - openSession() void
  - crearJuego(String) VideoJuego?
  - dialog(String) JDialog
  - hover(JLabel, Color, Color, Consumer~MouseEvent~) MouseAdapter
  - buildDetailBar() JPanel
  - rebuildCarousel() void
  - buildTopBar() JPanel
  - switchTab(String) void
  - darkPanel() JPanel
  - darkField() JTextField
  - launchGame() void
  - openGameConfig() void
  - openAddGame() void
  - styledLabel(String, Font, Color) JLabel
  - updateSessionLabel() void
  - buildDialogBtn(String, boolean, ActionListener) RoundBtn
  - removeSelected() void
  - paintRnd(Graphics2D, JComponent, Color, Color, float, int) void
  - openGlobalSettings() void
  - round(int, int, int, int, int) Float
  - updateDetailBar() void
  - buildActionBtn(String, boolean) RoundBtn
  - addFormRow(JPanel, String, JComponent) void
  - styleScrollBar(JScrollPane) void
  - darkPanel(LayoutManager) JPanel
  - aa(Graphics) Graphics2D
  - buildBody() JPanel
  - clearFocus() void
  - buildBottomBar() JPanel
  - darkCombo(String[]) JComboBox~String~
}
class MenuLodeRunner {
  + MenuLodeRunner(InputManager, Object) 
  - int seleccion
  - int skinPersonaje
  + dibujarConfig(Graphics) void
  + dibujar(Graphics) void
  + actualizarConfig() void
  + actualizar() void
  + navegarMainMenu(int) boolean
  + recargarRanking() void
   - boolean configMode
   - int skinPersonaje
   - String[] configActions
   - int seleccion
}
class MenuPong {
  + MenuPong(InputManager, Object) 
  - boolean configMode
  - int seleccion
  + actualizar() void
  + recargarRanking() void
  + actualizarConfig() void
  + dibujar(Graphics) void
  + dibujarConfig(Graphics) void
   - int skinPaleta2
   - boolean configMode
   - int skinPaleta1
   - int seleccion
}
class MenuPrincipal {
  + MenuPrincipal(String, String, Color, String, String) 
  # boolean configMode
  # String[] configActions
  + actualizarConfig() void
  + dibujarConfig(Graphics) void
  + actualizar() void
  - guardarConfiguracion() void
  - reiniciarDefaults() void
  - obtenerDefault(String) int
  + renderizar() void
   - boolean configMode
   - String[] configActions
}
class MenuSpaceInvaders {
  + MenuSpaceInvaders(InputManager, JuegoSpaceInvaders) 
  - boolean sonidoActivado
  - int seleccion
  - int skinProyectiles
  - int skinNave
  - int velocidad
  - boolean configMode
  - int skinInvasores
  + navegarMainMenu(int) boolean
  + dibujarConfig(Graphics) void
  + actualizarConfig() void
  + recargarRanking() void
  + dibujar(Graphics) void
  + actualizar() void
   - boolean visible
   - boolean configMode
   - int skinNave
   - int velocidad
   - int seleccion
   - int skinProyectiles
   - boolean sonidoActivado
   - int skinInvasores
}
class Moneda {
  + Moneda(int, int, int) 
  - boolean recolectada
  + actualizar() void
  + recoger() void
  - cargarAnimacion() void
  + display(Graphics) void
  + recolectar() void
   - boolean recolectada
}
class MouseManager {
  + MouseManager() 
   - int y
   - int x
   - boolean leftPressed
   - boolean rightPressed
   - boolean middlePressed
}
class Movible {
<<Interface>>
  + Mover() void
}
class Murido {
  + Murido(int, int, int) 
  + actualizar() void
}
class NaveJugador {
  + NaveJugador(int, int, String, String) 
  + display(Graphics) void
  + Disparar() Laser
}
class NaveNodriza {
  + NaveNodriza() 
  + puntaje(int) int
  + actualizar() void
}
class Nivel {
  + Nivel() 
  + Nivel(int, String[]) 
  + static final char VACIO
  + static final char LADRILLO
  + static final char LADRILLO_IRROMPIBLE
  + static final char ESCALERA
  + static final char BARRA
  + static final char MONEDA
  + static final char AGUJERO
  + static final char GUARDIA
  + static final char RECOLECTOR
  + static final char PUERTA
  # int Numero
  # String[] Mapa
  # char[][] mapa
  # int tile_size
  # List~ObjetoGrafico~ Entidades
  # List~Ladrillo~ ladrillos
  # List~Ladrillo~ ladrillosIrrompibles
  # List~Escalera~ escaleras
  # List~Barra~ barras
  # List~Moneda~ monedas
  # List~Agujero~ agujeros
  # List~ParticulaLadrillo~ particulas
  # int escapeLadderX
  # int escapeLadderY
  # boolean escapeLadderActiva
  # int spawnRecolectorX
  # int spawnRecolectorY
  # Puerta puertaSalida
  # int totalOro
  + int tiempoLimite
  + getTile(int, int) char
  + esEscalera(int, int) boolean
  + esBarra(int, int) boolean
  + actualizar() void
  + finalizarNivel() void
  + esSolido(int, int) boolean
  + esMoneda(int, int) boolean
  + renderizar() void
  + sincronizarEntidades(List~ObjetoGrafico~) void
  + getMonedaEn(int, int) Moneda
  + setTile(int, int, char) void
  + cavarEn(int, int) boolean
  + esLadrilloCavable(int, int) boolean
  + cargar() void
  + agregarEntidad(ObjetoGrafico) void
  + activarEscape() void
  + esVacio(int, int) boolean
  + getNumero() int
  + getTile_size() int
  + getAnchoMapa() int
  + getAltoMapa() int
  + getAnchoPixels() int
  + getAltoPixels() int
}
class Nivel1 {
  + Nivel1() 
}
class Nivel2 {
  + Nivel2() 
}
class Nivel3 {
  + Nivel3() 
}
class NivelSpaceInvaders {
  + NivelSpaceInvaders() 
  + generarOleadas(HashMap~String, Enemigo~, List~ObjetoGrafico~, int, int) void
}
class ObjetoGrafico {
  + ObjetoGrafico(String, Dimension, Point) 
  + ObjetoGrafico(String) 
  + ObjetoGrafico() 
  # BufferedImage sprite
  # Point punto
  # Hitbox hitbox
  # Dimension dimension
  # boolean paraEliminar
  + display(Graphics) void
  + actualizar() void
  + marcarParaEliminar() void
  + desaparecer() void
   - Hitbox hitbox
   - Point punto
   - BufferedImage sprite
   - Rectangle bounds
   - int width
   - boolean paraEliminar
   - double y
   - int height
   - double x
   - Dimension dimension
}
class Paleta {
  + Paleta(InputManager, int) 
  + ResetearPOS() void
  + Mover() void
  + dibujar(Graphics) void
}
class ParticulaLadrillo {
  + ParticulaLadrillo(int, int, int) 
  - boolean activo
  + display(Graphics) void
  - cargarAnimacion() void
  + actualizar() void
   - boolean activo
}
class PelotaPong {
  + PelotaPong() 
  + aumentarVelocidad() void
  + salioIzquierda() boolean
  + mover() void
  + salioDerecha() boolean
  + rebotarParedes() void
  + reiniciar() void
  + rebotarPaleta(Paleta) void
  + reiniciar(boolean) void
  + display(Graphics) void
}
class Personaje {
  + Personaje() 
  # int vidas
  + agregarVida(int) void
  + mover() void
  + recibirDanio(int) void
   - int vidas
}
class Puerta {
  + Puerta(int, int, int) 
  - boolean visible
  + ocultar() void
  + display(Graphics) void
  - cargarSprite() void
  + mostrar() void
   - boolean visible
}
class RankingManager {
  + RankingManager() 
  + RankingManager(String) 
  - List~Integer~ puntajes
  + cargarPuntajesTop(String, int) List~Integer~
  + cargarRanking() void
  - crearCarpetaSiNoExiste(String) void
  + guardarRanking() void
  + cargarDetalleTop(String, int) List~RankingEntry~
  - inicializarTabla() void
  + agregarPuntaje(String, String, int, int) void
  + agregarPuntaje() void
   - List~Integer~ puntajes
}
class Recolector {
  + Recolector(int, int, int) 
  - int oroRecolectado
  - boolean enAire
  - int nivelOroTotal
  - int tileY
  - boolean cayendo
  - boolean enBarra
  - List~Guardia~ guardias
  - Nivel nivel
  - boolean enEscalera
  - int tileX
  - String skin
  + moverIzquierda() void
  + moverArriba() void
  - hayAgujeroSeguro(int, int) boolean
  - tieneSoporte(int, int) boolean
  + verificarCaidaEnAgujero() void
  - aplicarGravedad() void
  - hayGuardiaEnAgujero(Agujero) boolean
  - cargarAnimaciones() void
  + moverDerecha() void
  + cavoEsteFrame() boolean
  + cavarDerecha() void
  + verificarColisionGuardias() void
  + recogerOro() void
  + moverAbajo() void
  + cavarIzquierda() void
  + recolectarMonedas() void
  + display(Graphics) void
  - hayGuardiaEnTile(int, int) boolean
  + perderVida() void
  + actualizar() void
  - detectarPlataforma() void
  + nivelCompleto() boolean
  + reiniciarPosicion() void
  + estaEnAgujero() boolean
  - cargarAnimacion(CargadorRecursos, String, String, int, long) Animacion?
  + mover() void
   - boolean cayendo
   - int tileY
   - int oroRecolectado
   - int tileX
   - List~Guardia~ guardias
   - InputManager inputManager
   - Nivel nivel
   - boolean enEscalera
   - boolean enAire
   - boolean enBarra
   - String skin
   - GameEventListener gameEventListener
   - int nivelOroTotal
}
class SegmentoEscudo {
  + SegmentoEscudo(int, int, String, String) 
  - int estado
  - actualizarSprite() void
  + recibirDanio() void
   - int estado
}
class Sprite {
  + Sprite(BufferedImage) 
  + Sprite(String) 
  - BufferedImage imagen
  + dibujar(Graphics, int, int, int, int) void
  + dibujar(Graphics, int, int) void
   - BufferedImage imagen
   - int height
   - int width
}
class SpriteSheet {
  + SpriteSheet(BufferedImage, int, int) 
  + SpriteSheet(List~BufferedImage~) 
  + obtenerSprite(int) Sprite
  + size() int
}
class VideoJuego {
  + VideoJuego() 
  # Nivel NivelActual
  # String Resultado
  + actualizar() void
  # actualizarLogicaJuego() void
  + cargarNivel() void
  + renderizar(Graphics) void
  + finalizar() void
  # reiniciar() void
  # finalizar(EstadoJuego, String) void
  + resetPuntaje() void
  + sumarPunto(int, int) void
  + iniciar() void
  # crearPartida() void
  # pausa() void
  + iniciapuntaje(Jugador, Jugador) void
  - manejarControlesGlobales() void
  + getpuntaje() List~Integer~
   - String nombreJuego
   - String perdedor
   - String nombreJugador
   - String ganador
   - String Resultado
   - int NivelActual
}

Agujero  --|>  Bloque 
Agujero "1" *--> "ladrilloAsociado 1" Ladrillo 
Animacion "1" *--> "frames *" Sprite 
App  ..>  Launcher : «crea»
Bala  ..>  Movible 
Bala  --|>  ObjetoGrafico 
Barra  --|>  Bloque 
Barra  ..>  CargadorRecursos : «crea»
Barra  ..>  Sprite : «crea»
Barra "1" *--> "spriteBarra 1" Sprite 
Bloque  --|>  ObjetoGrafico 
Enemigo "1" *--> "animacion 1" Animacion 
Enemigo  --|>  Personaje 
EnemigoA  ..>  Animacion : «crea»
EnemigoA  ..>  Armado 
EnemigoA  --|>  Enemigo 
EnemigoA  ..>  Laser : «crea»
EnemigoA  ..>  Sprite : «crea»
EnemigoB  ..>  Animacion : «crea»
EnemigoB  --|>  Enemigo 
EnemigoB  ..>  Sprite : «crea»
EnemigoC  ..>  Animacion : «crea»
EnemigoC  --|>  Enemigo 
EnemigoC  ..>  Sprite : «crea»
Escalera "1" *--> "animacion 1" Animacion 
Escalera  ..>  Animacion : «crea»
Escalera  --|>  Bloque 
Escalera  ..>  CargadorRecursos : «crea»
Escalera  ..>  Sprite : «crea»
Escudo  --|>  ObjetoGrafico 
Escudo "1" *--> "segmentos *" SegmentoEscudo 
Escudo  ..>  SegmentoEscudo : «crea»
GameLoop "1" *--> "videojuego 1" JuegoLoopable 
Guardia "1" *--> "animCaminando 1" Animacion 
Guardia  ..>  Animacion : «crea»
Guardia  ..>  CargadorRecursos : «crea»
Guardia "1" *--> "ia 1" IA_Guardia 
Guardia  ..>  IA_Guardia : «crea»
Guardia  ..>  Moneda : «crea»
Guardia "1" *--> "monedaCargada 1" Moneda 
Guardia "1" *--> "nivel 1" Nivel 
Guardia  --|>  Personaje 
Guardia "1" *--> "heroe 1" Recolector 
Guardia  ..>  Sprite : «crea»
IA_Pong "1" *--> "paleta 1" Paleta 
IA_Pong "1" *--> "pelota 1" PelotaPong 
JuegoLodeRunner  ..>  CargadorRecursos : «crea»
JuegoLodeRunner  ..>  FXPlayer : «crea»
JuegoLodeRunner "1" *--> "fxPlayer 1" FXPlayer 
JuegoLodeRunner  ..>  GameEventListener 
JuegoLodeRunner "1" *--> "guardias *" Guardia 
JuegoLodeRunner  ..>  Guardia : «crea»
JuegoLodeRunner  ..>  InputManager : «crea»
JuegoLodeRunner "1" *--> "input 1" InputManager 
JuegoLodeRunner  ..>  Jugador : «crea»
JuegoLodeRunner "1" *--> "menu 1" MenuLodeRunner 
JuegoLodeRunner  ..>  MenuLodeRunner : «crea»
JuegoLodeRunner "1" *--> "niveles *" Nivel 
JuegoLodeRunner  ..>  Nivel1 : «crea»
JuegoLodeRunner  ..>  Nivel2 : «crea»
JuegoLodeRunner  ..>  Nivel3 : «crea»
JuegoLodeRunner  ..>  RankingManager : «crea»
JuegoLodeRunner "1" *--> "heroe 1" Recolector 
JuegoLodeRunner  ..>  Recolector : «crea»
JuegoLodeRunner  --|>  VideoJuego 
JuegoPong  ..>  CargadorRecursos : «crea»
JuegoPong  ..>  CollisionManager : «crea»
JuegoPong "1" *--> "collisionManager 1" CollisionManager 
JuegoPong  ..>  FXPlayer : «crea»
JuegoPong "1" *--> "fxPlayer 1" FXPlayer 
JuegoPong  ..>  IA_Pong : «crea»
JuegoPong "1" *--> "ia 1" IA_Pong 
JuegoPong "1" *--> "input 1" InputManager 
JuegoPong  ..>  InputManager : «crea»
JuegoPong "1" *--> "menu 1" MenuPong 
JuegoPong  ..>  MenuPong : «crea»
JuegoPong  ..>  Paleta : «crea»
JuegoPong "1" *--> "paleta1 1" Paleta 
JuegoPong  ..>  PelotaPong : «crea»
JuegoPong "1" *--> "pelota 1" PelotaPong 
JuegoPong  --|>  VideoJuego 
JuegoSpaceInvaders "1" *--> "flotaE *" Enemigo 
JuegoSpaceInvaders  ..>  Escudo : «crea»
JuegoSpaceInvaders "1" *--> "fxPlayer 1" FXPlayer 
JuegoSpaceInvaders  ..>  FXPlayer : «crea»
JuegoSpaceInvaders  ..>  InputManager : «crea»
JuegoSpaceInvaders "1" *--> "input 1" InputManager 
JuegoSpaceInvaders  ..>  Laser : «crea»
JuegoSpaceInvaders "1" *--> "disparo 1" Laser 
JuegoSpaceInvaders  ..>  MenuSpaceInvaders : «crea»
JuegoSpaceInvaders "1" *--> "menu 1" MenuSpaceInvaders 
JuegoSpaceInvaders  ..>  Murido : «crea»
JuegoSpaceInvaders  ..>  NaveJugador : «crea»
JuegoSpaceInvaders "1" *--> "navecita 1" NaveJugador 
JuegoSpaceInvaders  ..>  NaveNodriza : «crea»
JuegoSpaceInvaders "1" *--> "platoVolador 1" NaveNodriza 
JuegoSpaceInvaders  ..>  NivelSpaceInvaders : «crea»
JuegoSpaceInvaders "1" *--> "nivel 1" NivelSpaceInvaders 
JuegoSpaceInvaders "1" *--> "rankingManager 1" RankingManager 
JuegoSpaceInvaders  ..>  RankingManager : «crea»
JuegoSpaceInvaders "1" *--> "SegmentoEscudo 1" SegmentoEscudo 
JuegoSpaceInvaders  --|>  VideoJuego 
Ladrillo  ..>  Animacion : «crea»
Ladrillo "1" *--> "animNormal 1" Animacion 
Ladrillo  --|>  Bloque 
Ladrillo  ..>  CargadorRecursos : «crea»
Ladrillo  ..>  Sprite : «crea»
Laser  --|>  Bala 
Launcher  ..>  CargadorRecursos : «crea»
Launcher "1" *--> "recursos 1" CargadorRecursos 
Launcher  ..>  GameLoop : «crea»
Launcher  ..>  JuegoLodeRunner : «crea»
Launcher  ..>  JuegoPong : «crea»
Launcher  ..>  JuegoSpaceInvaders : «crea»
MenuLodeRunner  --|>  MenuPrincipal 
MenuLodeRunner "1" *--> "rankingManager 1" RankingManager 
MenuLodeRunner  ..>  RankingManager : «crea»
MenuPong "1" *--> "input 1" InputManager 
MenuPong  --|>  MenuPrincipal 
MenuPong "1" *--> "rankingManager 1" RankingManager 
MenuPong  ..>  RankingManager : «crea»
MenuPrincipal "1" *--> "input 1" InputManager 
MenuSpaceInvaders "1" *--> "juego 1" JuegoSpaceInvaders 
MenuSpaceInvaders  --|>  MenuPrincipal 
MenuSpaceInvaders "1" *--> "rankingManager 1" RankingManager 
MenuSpaceInvaders  ..>  RankingManager : «crea»
Moneda  ..>  Animacion : «crea»
Moneda "1" *--> "animacion 1" Animacion 
Moneda  --|>  Bloque 
Moneda  ..>  CargadorRecursos : «crea»
Moneda  ..>  Sprite : «crea»
Murido  --|>  ObjetoGrafico 
NaveJugador  ..>  Armado 
NaveJugador  ..>  Laser : «crea»
NaveJugador  --|>  Personaje 
NaveNodriza  --|>  ObjetoGrafico 
Nivel "1" *--> "agujeros *" Agujero 
Nivel  ..>  Agujero : «crea»
Nivel  ..>  Barra : «crea»
Nivel "1" *--> "barras *" Barra 
Nivel  ..>  Escalera : «crea»
Nivel "1" *--> "escaleras *" Escalera 
Nivel "1" *--> "ladrillos *" Ladrillo 
Nivel  ..>  Ladrillo : «crea»
Nivel  ..>  Moneda : «crea»
Nivel "1" *--> "monedas *" Moneda 
Nivel "1" *--> "Entidades *" ObjetoGrafico 
Nivel  ..>  ParticulaLadrillo : «crea»
Nivel "1" *--> "particulas *" ParticulaLadrillo 
Nivel "1" *--> "puertaSalida 1" Puerta 
Nivel  ..>  Puerta : «crea»
Nivel1  --|>  Nivel 
Nivel2  --|>  Nivel 
Nivel3  --|>  Nivel 
NivelSpaceInvaders  ..>  EnemigoA : «crea»
NivelSpaceInvaders  ..>  EnemigoB : «crea»
NivelSpaceInvaders  ..>  EnemigoC : «crea»
ObjetoGrafico "1" *--> "hitbox 1" Hitbox 
ObjetoGrafico  ..>  Hitbox : «crea»
Paleta "1" *--> "input 1" InputManager 
Paleta  ..>  Movible 
Paleta  --|>  ObjetoGrafico 
ParticulaLadrillo  ..>  Animacion : «crea»
ParticulaLadrillo "1" *--> "animacion 1" Animacion 
ParticulaLadrillo  ..>  CargadorRecursos : «crea»
ParticulaLadrillo  --|>  ObjetoGrafico 
ParticulaLadrillo  ..>  Sprite : «crea»
PelotaPong  --|>  ObjetoGrafico 
Personaje  --|>  ObjetoGrafico 
Puerta  ..>  CargadorRecursos : «crea»
Puerta  --|>  ObjetoGrafico 
Puerta "1" *--> "spritePuerta 1" Sprite 
Puerta  ..>  Sprite : «crea»
Recolector  ..>  Animacion : «crea»
Recolector "1" *--> "animParado 1" Animacion 
Recolector  ..>  CargadorRecursos : «crea»
Recolector "1" *--> "listener 1" GameEventListener 
Recolector "1" *--> "guardias *" Guardia 
Recolector "1" *--> "input 1" InputManager 
Recolector "1" *--> "nivel 1" Nivel 
Recolector  --|>  Personaje 
Recolector  ..>  Sprite : «crea»
SegmentoEscudo  --|>  ObjetoGrafico 
SpriteSheet  ..>  Sprite : «crea»
SpriteSheet "1" *--> "sprites *" Sprite 
VideoJuego "1" *--> "camara 1" Camara 
VideoJuego  ..>  Camara : «crea»
VideoJuego "1" *--> "configManager 1" ConfigManager 
VideoJuego  ..>  ConfigManager : «crea»
VideoJuego "1" *--> "estado 1" EstadoJuego 
VideoJuego "1" *--> "input 1" InputManager 
VideoJuego  ..>  JuegoLoopable 
VideoJuego "1" *--> "Jugador *" Jugador 
VideoJuego "1" *--> "NivelActual 1" Nivel 
VideoJuego "1" *--> "Entidades *" ObjetoGrafico 
VideoJuego  ..>  RankingManager : «crea»
VideoJuego "1" *--> "rankingManager 1" RankingManager 
