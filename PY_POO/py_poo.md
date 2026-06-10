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
   boolean abierto
   Ladrillo ladrilloAsociado
   float progreso
   int tiempoRestante
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
   boolean repitiendo
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
   int valor
}
class Camara {
  + Camara() 
  - int Y
  - int X
  + seguirJugador(ObjetoGrafico, Nivel) void
   int Y
   int X
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
   boolean soundEnabled
   boolean soundFxEnabled
   boolean musicEnabled
   boolean fullscreen
   float volumen
}
class Constantes {
  + Constantes() 
}
class Enemigo {
  + Enemigo(int, int, Animacion) 
  + display(Graphics) void
  + actualizacionAnimacion() void
   int puntos
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
   SegmentoEscudo[] segmentos
}
class EstadoJuego {
<<enumeration>>
  MENU
  JUGANDO
  PAUSA
  GAME_OVER
  VICTORIA
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
   Keyboard teclado
   JuegoLoopable videoJuego
   MouseWheel ruedaRaton
   Mouse raton
}
class Guardia {
  + Guardia(int, int, int) 
  - boolean enEscalera
  - boolean enBarra
  - boolean enAire
  - boolean cayendo
  - Nivel nivel
  - String skin
  - Recolector heroe
  - Moneda monedaCargada
  + moverDerecha() void
  + actualizar() void
  + enAgujero() boolean
  + moverAbajo() void
  + enAgujero(boolean) void
  + intentarRecolectarOro() void
  - cargarAnimaciones() void
  + iniciarEscape(int) void
  - hayAgujeroSeguro(int, int) boolean
  + manejarColisionAgujero(List~Agujero~, List~Guardia~) boolean
  + display(Graphics) void
  - aplicarGravedad() void
  - tieneSoporte(int, int) boolean
  - cargarAnimacion(CargadorRecursos, String, String, int, long) Animacion?
  + moverIzquierda() void
  + soltarMoneda() void
  - detectarPlataforma() void
  + moverArriba() void
  + reaparecer() void
  + mover() void
   boolean cargandoOro
   Nivel nivel
   IA_Guardia IA
   boolean cayendo
   boolean enEscalera
   int tileY
   int tileX
   boolean enAire
   Recolector heroe
   boolean enBarra
   String skin
   Moneda monedaCargada
}
class Hitbox {
  + Hitbox(int, int, int, int) 
  - int x
  - int width
  - int height
  - int y
  + setPosicion(int, int) void
  + setDimension(int, int) void
   int y
   Rectangle bounds
   int x
   int height
   int width
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
   int TIEMPO_ESCAPE
   boolean persiguiendo
   Comportamiento estado
   int tiempoAtrapado
   int TIEMPO_ESPERA_ESCAPE
   boolean saliendo
}
class IA_Pong {
  + IA_Pong(PelotaPong, Paleta, int) 
  - int dificultad
  + incrementarDificultad() void
  + calcularMovimiento() void
   int dificultad
}
class InputManager {
  + InputManager() 
  - long lastEnterTime
  - long lastMenuUpTime
  - long lastMenuDownTime
  - long COOLDOWN_MS
  + isKeyPressed(int) boolean
  + upPressed() boolean
  + downPressed() boolean
  + leftPressed() boolean
  + rightPressed() boolean
  + enterPressed() boolean
  + spacePressed() boolean
  + escapePressed() boolean
  + qPressed() boolean
  + mPressed() boolean
  + pPressed() boolean
  + digPressed() boolean
  + wPressed() boolean
  + sPressed() boolean
  + menuUpPressed() boolean
  + menuDownPressed() boolean
  + backslashPressed() boolean
  + ctrlPressed() boolean
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
   String nombreJugador
   String ganador
   String perdedor
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
  - Paleta paleta1
  - Paleta paleta2
  - PelotaPong pelota
  - int PUNTOS_MAX
  + renderizar(Graphics) void
  - registrarRankingFinal() void
  + iniciar() void
  + pause() void
  # crearPartida() void
  # reiniciar() void
  # actualizarLogicaJuego() void
}
class JuegoSpaceInvaders {
  + JuegoSpaceInvaders() 
  # actualizarLogicaJuego() void
  + iniciar() void
  + renderizar(Graphics) void
  + pause() void
  - registrarRankingFinal() void
  # crearPartida() void
   String ganador
   String perdedor
}
class Jugador {
  + Jugador(String) 
  - String Nombre
  + getid() int
   String Nombre
}
class KeyBindings {
  + KeyBindings() 
  + get(String) int
  + set(String, int) void
  + keyName(int) String
   String[] actionNames
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
   Estado estado
   boolean roto
   boolean irrompible
}
class Laser {
  + Laser(int, int, int, String) 
  - int velocidad
  + actualizar() void
   int velocidad
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
   boolean configMode
   int skinPersonaje
   String[] configActions
   int seleccion
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
   int skinPaleta2
   boolean configMode
   int skinPaleta1
   int seleccion
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
   boolean configMode
   String[] configActions
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
   boolean visible
   boolean configMode
   int skinNave
   int velocidad
   int seleccion
   int skinProyectiles
   boolean sonidoActivado
   int skinInvasores
}
class Moneda {
  + Moneda(int, int, int) 
  - boolean recolectada
  + actualizar() void
  + recoger() void
  - cargarAnimacion() void
  + display(Graphics) void
  + recolectar() void
   boolean recolectada
}
class MouseManager {
  + MouseManager() 
   int y
   int x
   boolean leftPressed
   boolean rightPressed
   boolean middlePressed
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
  # int tile_size
  # int Numero
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
   int Numero
   int altoMapa
   int anchoMapa
   int anchoPixels
   int altoPixels
   int tile_size
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
   Hitbox hitbox
   Point punto
   BufferedImage sprite
   Rectangle bounds
   int width
   boolean paraEliminar
   double y
   int height
   double x
   Dimension dimension
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
   boolean activo
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
   int vidas
}
class Puerta {
  + Puerta(int, int, int) 
  - boolean visible
  + ocultar() void
  + display(Graphics) void
  - cargarSprite() void
  + mostrar() void
   boolean visible
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
   List~Integer~ puntajes
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
   boolean cayendo
   int tileY
   int oroRecolectado
   int tileX
   List~Guardia~ guardias
   InputManager inputManager
   Nivel nivel
   boolean enEscalera
   boolean enAire
   boolean enBarra
   String skin
   GameEventListener gameEventListener
   int nivelOroTotal
}
class SegmentoEscudo {
  + SegmentoEscudo(int, int, String, String) 
  - int estado
  - actualizarSprite() void
  + recibirDanio() void
   int estado
}
class Sprite {
  + Sprite(BufferedImage) 
  + Sprite(String) 
  - BufferedImage imagen
  + dibujar(Graphics, int, int, int, int) void
  + dibujar(Graphics, int, int) void
   BufferedImage imagen
   int height
   int width
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
   String nombreJuego
   String perdedor
   String nombreJugador
   String ganador
   String Resultado
   int NivelActual
}
class Game {
}
class JFrame {
}

Agujero  --|>  Bloque : extender
Agujero "1" --> "ladrilloAsociado 1" Ladrillo : asociar
Animacion "1" *--> "frames *" Sprite : contener
App  ..>  Launcher : iniciar
Bala  ..|>  Movible : implementar
Bala  --|>  ObjetoGrafico : extender
Barra  --|>  Bloque : extender
Barra  ..>  CargadorRecursos : cargar recursos
Barra  ..>  Sprite : cargar
Barra "1" *--> "spriteBarra 1" Sprite : tener
Bloque  --|>  ObjetoGrafico : extender
Enemigo "1" *--> "animacion 1" Animacion : tener
Enemigo  --|>  Personaje : extender
EnemigoA  ..>  Animacion : crear
EnemigoA  ..|>  Armado : implementar
EnemigoA  --|>  Enemigo : extender
EnemigoA  ..>  Laser : disparar
EnemigoA  ..>  Sprite : cargar
EnemigoB  ..>  Animacion : crear
EnemigoB  --|>  Enemigo : extender
EnemigoB  ..>  Sprite : cargar
EnemigoC  ..>  Animacion : crear
EnemigoC  --|>  Enemigo : extender
EnemigoC  ..>  Sprite : cargar
Escalera "1" *--> "animacion 1" Animacion : tener
Escalera  ..>  Animacion : crear
Escalera  --|>  Bloque : extender
Escalera  ..>  CargadorRecursos : cargar recursos
Escalera  ..>  Sprite : cargar
Escudo  --|>  ObjetoGrafico : extender
Escudo "1" *--> "segmentos *" SegmentoEscudo : contener
Escudo  ..>  SegmentoEscudo : crear
GameLoop "1" *--> "videojuego 1" JuegoLoopable : ejecutar
GameLoop  --|>  Game : extender
Guardia "1" *--> "animCaminando 1" Animacion : tener
Guardia  ..>  Animacion : crear
Guardia  ..>  CargadorRecursos : cargar recursos
Guardia "1" *--> "ia 1" IA_Guardia : tener
Guardia  ..>  IA_Guardia : crear
Guardia  ..>  Moneda : cargar
Guardia "1" *--> "monedaCargada 1" Moneda : llevar
Guardia "1" --> "nivel 1" Nivel : conocer
Guardia  --|>  Personaje : extender
Guardia "1" --> "heroe 1" Recolector : perseguir
Guardia  ..>  Sprite : cargar
IA_Pong "1" *--> "paleta 1" Paleta : controlar
IA_Pong "1" *--> "pelota 1" PelotaPong : seguir
JuegoLodeRunner  ..>  CargadorRecursos : cargar recursos
JuegoLodeRunner  ..>  FXPlayer : reproducir sonido
JuegoLodeRunner "1" *--> "fxPlayer 1" FXPlayer : tener
JuegoLodeRunner  ..|>  GameEventListener : implementar
JuegoLodeRunner "1" *--> "guardias *" Guardia : gestionar
JuegoLodeRunner  ..>  Guardia : crear
JuegoLodeRunner  ..>  InputManager : configurar
JuegoLodeRunner "1" *--> "input 1" InputManager : usar
JuegoLodeRunner  ..>  Jugador : crear
JuegoLodeRunner "1" *--> "menu 1" MenuLodeRunner : tener
JuegoLodeRunner  ..>  MenuLodeRunner : crear
JuegoLodeRunner "1" *--> "niveles *" Nivel : gestionar
JuegoLodeRunner  ..>  Nivel1 : crear
JuegoLodeRunner  ..>  Nivel2 : crear
JuegoLodeRunner  ..>  Nivel3 : crear
JuegoLodeRunner  ..>  RankingManager : gestionar puntajes
JuegoLodeRunner "1" *--> "heroe 1" Recolector : tener
JuegoLodeRunner  ..>  Recolector : crear
JuegoLodeRunner  --|>  VideoJuego : extender
JuegoPong  ..>  CargadorRecursos : cargar recursos
JuegoPong  ..>  CollisionManager : detectar colisiones
JuegoPong "1" *--> "collisionManager 1" CollisionManager : tener
JuegoPong  ..>  FXPlayer : reproducir sonido
JuegoPong "1" *--> "fxPlayer 1" FXPlayer : tener
JuegoPong  ..>  IA_Pong : crear
JuegoPong "1" *--> "ia 1" IA_Pong : tener
JuegoPong "1" *--> "input 1" InputManager : usar
JuegoPong  ..>  InputManager : configurar
JuegoPong "1" *--> "menu 1" MenuPong : tener
JuegoPong  ..>  MenuPong : crear
JuegoPong  ..>  Paleta : crear
JuegoPong "1" *--> "paleta1 1" Paleta : tener
JuegoPong "1" *--> "paleta2 1" Paleta : tener
JuegoPong  ..>  PelotaPong : crear
JuegoPong "1" *--> "pelota 1" PelotaPong : tener
JuegoPong  --|>  VideoJuego : extender
JuegoSpaceInvaders "1" *--> "flotaE *" Enemigo : gestionar
JuegoSpaceInvaders  ..>  Escudo : crear
JuegoSpaceInvaders "1" *--> "fxPlayer 1" FXPlayer : tener
JuegoSpaceInvaders  ..>  FXPlayer : reproducir sonido
JuegoSpaceInvaders  ..>  InputManager : configurar
JuegoSpaceInvaders "1" *--> "input 1" InputManager : usar
JuegoSpaceInvaders  ..>  Laser : crear
JuegoSpaceInvaders "1" *--> "disparo 1" Laser : tener
JuegoSpaceInvaders  ..>  MenuSpaceInvaders : crear
JuegoSpaceInvaders "1" *--> "menu 1" MenuSpaceInvaders : tener
JuegoSpaceInvaders  ..>  Murido : crear explosion
JuegoSpaceInvaders  ..>  NaveJugador : crear
JuegoSpaceInvaders "1" *--> "navecita 1" NaveJugador : tener
JuegoSpaceInvaders  ..>  NaveNodriza : crear
JuegoSpaceInvaders "1" *--> "platoVolador 1" NaveNodriza : tener
JuegoSpaceInvaders  ..>  NivelSpaceInvaders : crear
JuegoSpaceInvaders "1" *--> "nivel 1" NivelSpaceInvaders : tener
JuegoSpaceInvaders "1" *--> "rankingManager 1" RankingManager : tener
JuegoSpaceInvaders  ..>  RankingManager : gestionar puntajes
JuegoSpaceInvaders "1" *--> "SegmentoEscudo 1" SegmentoEscudo : tener
JuegoSpaceInvaders  --|>  VideoJuego : extender
Ladrillo  ..>  Animacion : crear
Ladrillo "1" *--> "animNormal 1" Animacion : tener
Ladrillo  --|>  Bloque : extender
Ladrillo  ..>  CargadorRecursos : cargar recursos
Ladrillo  ..>  Sprite : cargar
Laser  --|>  Bala : extender
Launcher  ..>  CargadorRecursos : cargar recursos
Launcher "1" *--> "recursos 1" CargadorRecursos : usar
Launcher  ..>  GameLoop : crear
Launcher  ..>  JuegoLodeRunner : crear
Launcher  ..>  JuegoPong : crear
Launcher  ..>  JuegoSpaceInvaders : crear
Launcher  --|>  JFrame : extender
MenuLodeRunner  --|>  MenuPrincipal : extender
MenuLodeRunner "1" *--> "rankingManager 1" RankingManager : tener
MenuLodeRunner  ..>  RankingManager : gestionar puntajes
MenuPong "1" *--> "input 1" InputManager : usar
MenuPong  --|>  MenuPrincipal : extender
MenuPong "1" *--> "rankingManager 1" RankingManager : tener
MenuPong  ..>  RankingManager : gestionar puntajes
MenuPrincipal "1" *--> "input 1" InputManager : usar
MenuPrincipal  --|>  JFrame : extender
MenuSpaceInvaders "1" *--> "juego 1" JuegoSpaceInvaders : tener
MenuSpaceInvaders  --|>  MenuPrincipal : extender
MenuSpaceInvaders "1" *--> "rankingManager 1" RankingManager : tener
MenuSpaceInvaders  ..>  RankingManager : gestionar puntajes
Moneda  ..>  Animacion : crear
Moneda "1" *--> "animacion 1" Animacion : tener
Moneda  --|>  Bloque : extender
Moneda  ..>  CargadorRecursos : cargar recursos
Moneda  ..>  Sprite : cargar
Murido  --|>  ObjetoGrafico : extender
NaveJugador  ..|>  Armado : implementar
NaveJugador  ..>  Laser : disparar
NaveJugador  --|>  Personaje : extender
NaveNodriza  --|>  ObjetoGrafico : extender
Nivel "1" *--> "agujeros *" Agujero : contener
Nivel  ..>  Agujero : crear
Nivel  ..>  Barra : crear
Nivel "1" *--> "barras *" Barra : contener
Nivel  ..>  Escalera : crear
Nivel "1" *--> "escaleras *" Escalera : contener
Nivel "1" *--> "ladrillos *" Ladrillo : contener
Nivel  ..>  Ladrillo : crear
Nivel  ..>  Moneda : crear
Nivel "1" *--> "monedas *" Moneda : contener
Nivel "1" *--> "Entidades *" ObjetoGrafico : contener
Nivel  ..>  ParticulaLadrillo : crear
Nivel "1" *--> "particulas *" ParticulaLadrillo : contener
Nivel "1" *--> "puertaSalida 1" Puerta : tener
Nivel  ..>  Puerta : crear
Nivel1  --|>  Nivel : extender
Nivel2  --|>  Nivel : extender
Nivel3  --|>  Nivel : extender
NivelSpaceInvaders  ..>  EnemigoA : crear
NivelSpaceInvaders  ..>  EnemigoB : crear
NivelSpaceInvaders  ..>  EnemigoC : crear
ObjetoGrafico "1" *--> "hitbox 1" Hitbox : tener
ObjetoGrafico  ..>  Hitbox : crear
Paleta "1" *--> "input 1" InputManager : usar
Paleta  ..|>  Movible : implementar
Paleta  --|>  ObjetoGrafico : extender
ParticulaLadrillo  ..>  Animacion : crear
ParticulaLadrillo "1" *--> "animacion 1" Animacion : tener
ParticulaLadrillo  ..>  CargadorRecursos : cargar recursos
ParticulaLadrillo  --|>  ObjetoGrafico : extender
ParticulaLadrillo  ..>  Sprite : cargar
PelotaPong  --|>  ObjetoGrafico : extender
Personaje  --|>  ObjetoGrafico : extender
Puerta  ..>  CargadorRecursos : cargar recursos
Puerta  --|>  ObjetoGrafico : extender
Puerta "1" *--> "spritePuerta 1" Sprite : tener
Puerta  ..>  Sprite : cargar
Recolector  ..>  Animacion : crear
Recolector "1" *--> "animParado 1" Animacion : tener
Recolector  ..>  CargadorRecursos : cargar recursos
Recolector "1" *--> "listener 1" GameEventListener : notificar
Recolector "1" --> "guardias *" Guardia : conocer
Recolector "1" *--> "input 1" InputManager : usar
Recolector "1" --> "nivel 1" Nivel : conocer
Recolector  --|>  Personaje : extender
Recolector  ..>  Sprite : cargar
SegmentoEscudo  --|>  ObjetoGrafico : extender
SpriteSheet  ..>  Sprite : crear
SpriteSheet "1" *--> "sprites *" Sprite : contener
VideoJuego "1" *--> "camara 1" Camara : tener
VideoJuego  ..>  Camara : crear
VideoJuego "1" *--> "configManager 1" ConfigManager : tener
VideoJuego  ..>  ConfigManager : crear
VideoJuego "1" *--> "estado 1" EstadoJuego : tener
VideoJuego "1" *--> "input 1" InputManager : usar
VideoJuego  ..|>  JuegoLoopable : implementar
VideoJuego "1" *--> "Jugador *" Jugador : tener
VideoJuego "1" *--> "NivelActual 1" Nivel : tener
VideoJuego "1" *--> "Entidades *" ObjetoGrafico : contener
VideoJuego  ..>  RankingManager : gestionar puntajes
VideoJuego "1" *--> "rankingManager 1" RankingManager : tener
