package py_poo.pong;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.util.List;

import py_poo.config.KeyBindings;
import py_poo.core.Constantes;
import py_poo.input.InputManager;
import py_poo.ranking.RankingManager;
import py_poo.ranking.RankingManager.RankingEntry;
import py_poo.ui.MenuPrincipal;

public class MenuPong extends MenuPrincipal {

    // VARIABLES DEL MENÚ
    private InputManager input;         // Lee el teclado
    private int seleccion;              // Guarda en qué opción del menú principal estamos parados

    // Variables para la pantalla de Configuración de Teclas
    private boolean configMode;         // Si es 'true', estamos en la pantalla de teclas. Si es 'false', en el menú normal.
    private int configSelected;         // En qué opción de configuración estamos parados
    private int configActionIndex = -1; // Qué acción específica estamos reasignando (-1 significa que ninguna por ahora)
    private long lastConfigKeyTime;     // Temporizador para evitar "rebotes" (que una tecla se presione dos veces por error)

    // Variables para la Base de Datos
    private RankingManager rankingManager;      // El gestor de SQLite
    private List<RankingEntry> topRanking;      // Lista temporal que guarda el Top 10 para dibujarlo en pantalla

    // ─── SKIN Y PISTA MUSICAL (descomentar cuando haya assets alternativos) ───
    // private int skinPaletas = 0;
    // private int pistaMusical = 0;

    //  CONSTRUCTOR
    public MenuPong(InputManager input, Object mouse) {
        super("Pong", "Menú Principal", Color.BLACK, "Jugar", "Salir"); // Configuración heredada de MenuPrincipal
        this.input = input;
        this.seleccion = 0; // Arranca por defecto en la opción 0 ("1 JUGADOR")

        // Conecta a la base de datos y trae el top 10
        this.rankingManager = new RankingManager();
        // El "Pong%" le dice a SQL: "Tráeme todo lo que empiece con la palabra Pong (vs IA, vs J2, etc.)"
        this.topRanking = rankingManager.cargarDetalleTop("Pong%", 10);
    }

    //  GETTERS Y SETTERS BÁSICOS
    public int getSeleccion() {
        return seleccion;
    }

    public void setSeleccion(int seleccion) {
        this.seleccion = seleccion;
    }

    public boolean isConfigMode() {
        return configMode;
    }

    // Activa o desactiva la pantalla de configuración y resetea sus valores
    public void setConfigMode(boolean configMode) {
        this.configMode = configMode;
        configSelected = 0;       // Vuelve a la primera opción
        configActionIndex = -1;   // No hay tecla seleccionada para cambiar
        lastConfigKeyTime = System.currentTimeMillis(); // Guarda la hora exacta en la que entramos
    }

    //  CONFIGURACIÓN DE TECLAS
    public void actualizarConfig() {
        long now = System.currentTimeMillis();

        // 1. Si el jugador seleccionó una acción y estamos esperando que presione la tecla nueva:
        if (configActionIndex >= 0) {
            // con la que acabamos de elegir la opción.
            if (now - lastConfigKeyTime < 120) return;

            // Recorre todas las teclas posibles (0 a 255)
            for (int code = 0; code < 256; code++) {
                if (input.isKeyPressed(code)) { // Si encuentra la que tocaste...
                    // ...la guarda en la configuración oficial del juego
                    KeyBindings.set(KeyBindings.getActionNames()[configActionIndex], code);
                    lastConfigKeyTime = now;
                    configActionIndex = -1; // Termina la espera
                    break;
                }
            }
            return;
        }

        // 2. Si no esta esperando una tecla, puede ir arriba o abajo en el menu
        if (input.isMenuUpPressed() || input.isWPressed()) {
            configSelected = Math.max(0, configSelected - 1); // Sube pero no pasa del 0
        }
        if (input.isMenuDownPressed() || input.isSPressed()) {
            String[] actions = KeyBindings.getActionNames();
            configSelected = Math.min(actions.length, configSelected + 1); // Baja hasta la opción "VOLVER"
        }

        // 3. Al presionar ENTER en el menú de config
        if (input.isEnterPressed()) {
            String[] actions = KeyBindings.getActionNames();
            if (configSelected == actions.length) {
                // Si estaba parado en la última opción ("VOLVER"), sale del modo config
                configMode = false;
            } else {
                // Si eligió una acción, activa el "modo espera" para leer la nueva tecla
                configActionIndex = configSelected;
                lastConfigKeyTime = now;
            }
        }
    }

    // DIBUJA LA PANTALLA DE CONFIGURACIÓN
    public void dibujarConfig(Graphics g) {
        // Dibuja un fondo semitransparente oscuro
        g.setColor(new Color(0, 0, 0, 200));
        g.fillRect(0, 0, 800, 600);

        // Título
        g.setFont(new Font("Consolas", Font.BOLD, 28));
        g.setColor(Color.CYAN);
        g.drawString("CONFIGURAR TECLAS", 220, 60);

        String[] actions = KeyBindings.getActionNames();
        g.setFont(new Font("Consolas", Font.PLAIN, 16));

        // Dibuja la lista de acciones configurables
        for (int i = 0; i < actions.length; i++) {
            int y = 110 + i * 35; // Calcula en qué pixel (Y) dibujar la opción para que queden en lista

            // Dibuja la flechita amarilla ">" si estamos parados sobre esta opción
            if (i == configSelected) {
                g.setColor(Color.YELLOW);
                g.drawString("> ", 180, y);
            } else {
                g.setColor(Color.WHITE);
            }

            // Reemplaza guiones bajos por espacios para que se lea mejor
            String label = actions[i].replace("_", " ");
            String key = KeyBindings.keyName(KeyBindings.get(actions[i]));

            // Si está esperando que toquemos una tecla nueva, lo pinta de verde
            if (configActionIndex == i) {
                g.setColor(Color.GREEN);
                g.drawString(label + ": [ PRESIONA UNA TECLA ]", 210, y);
            } else {
                g.drawString(label + ": " + key, 210, y);
            }
        }

        // Dibuja el botón "VOLVER" al final de la lista
        int y = 110 + actions.length * 35;
        if (configSelected == actions.length) {
            g.setColor(Color.YELLOW);
            g.drawString("> ", 180, y);
        } else {
            g.setColor(Color.WHITE);
        }
        g.drawString("VOLVER", 210, y);

        // Instrucciones abajo de todo
        g.setFont(new Font("Consolas", Font.PLAIN, 12));
        g.setColor(Color.GRAY);
        g.drawString("Flechas: mover  |  Enter: seleccionar / cambiar  |  Esc: salir", 180, 580);
    }


    public void actualizar() {
    }

    // --- DIBUJAR PANTALLA PRINCIPAL ---
    public void dibujar(Graphics g) {

        // Si estamos en la pantalla de teclas, derivamos el dibujo al otro método y cortamos acá
        if (isConfigMode()) {
            dibujarConfig(g);
            return;
        }

        // Fondo
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, Constantes.WIDTH, Constantes.HEIGHT);

        // Título del juego
        g.setFont(new Font("Consolas", Font.BOLD, 45));
        g.setColor(Color.GREEN);
        g.drawString("ARCADE PONG", 260, 100);

        // Lista de opciones
        String[] opciones = {"1 JUGADOR (VS IA)", "2 JUGADORES", "CONFIG", "SALIR"};
        g.setFont(new Font("Consolas", Font.PLAIN, 20));
        for (int i = 0; i < opciones.length; i++) {
            if (i == seleccion) {
                g.setColor(Color.YELLOW);
                g.drawString("> " + opciones[i], 100, 200 + i * 35); // Seleccionado con flechita
            } else {
                g.setColor(Color.WHITE);
                g.drawString("  " + opciones[i], 100, 200 + i * 35); // No seleccionado
            }
        }

        // Textos de los controles
        g.setFont(new Font("Consolas", Font.PLAIN, 14));
        g.setColor(Color.GRAY);
        g.drawString("W/S o Flechas para mover | ENTER para seleccionar", 100, 420);
        g.drawString("Controles: W/S (J1)  |  Flechas Arriba/Abajo (J2)", 100, 440);

        // dibuja el ranking
        g.setFont(new Font("Consolas", Font.BOLD, 22));
        g.setColor(Color.CYAN);
        g.drawString("--- TOP 10 RANKING ---", 450, 180);

        g.setFont(new Font("Consolas", Font.PLAIN, 14));
        g.setColor(Color.WHITE);

        // Si la base de datos está vacía
        if (topRanking == null || topRanking.isEmpty()) {
            g.drawString("Aún no hay puntajes.", 450, 220);
        } else {
            // Si hay datos, los dibujamos uno debajo del otro
            int y = 220; // Altura inicial del primer puntaje
            for (int i = 0; i < topRanking.size(); i++) {
                RankingEntry entry = topRanking.get(i);

                // Limpia la palabra Pong, dejando solo "vs IA [11-5] (Ganó)"
                String detalle = entry.juego().replace("Pong", "").trim();

                // Por si el detalle quedó vacío por algún motivo, muestra los puntos crudos
                if (detalle.isEmpty()) {
                    detalle = entry.puntaje() + " pts";
                }

                // Arma ranking con este formato : "1. German  vs IA [11-5] (Ganó)"
                String texto = String.format("%d. %s  %s", (i + 1), entry.jugador(), detalle);
                g.drawString(texto, 450, y);
                y += 20; // Baja 20 píxeles para el siguiente renglón
            }
        }
    }

    // ─── Getters para skin y pista musical ───
    // public int getSkinPaletas() { return skinPaletas; }
    // public int getPistaMusical() { return pistaMusical; }

    // ═══════════════════════════════════════════════════════════════
    // CONFIGURACIÓN COMPLETA (descomentar cuando haya assets alternativos)
    // Reemplazar los métodos dibujarConfig() y actualizarConfig()
    // existentes por estos que incluyen Skin y Pista Musical.
    // ═══════════════════════════════════════════════════════════════
    // private final String[] opcionesConfig = {
    //     "Skin Paletas", "Pista Musical",
    //     "Configurar Teclas", "RESET VALORES", "VOLVER"
    // };
    //
    // public void dibujarConfig(Graphics g) {
    //     g.setColor(new Color(0, 0, 0, 200));
    //     g.fillRect(0, 0, 800, 600);
    //     g.setFont(new Font("Consolas", Font.BOLD, 28));
    //     g.setColor(Color.CYAN);
    //     g.drawString("CONFIGURACIÓN", 280, 60);
    //     g.setFont(new Font("Consolas", Font.PLAIN, 18));
    //     for (int i = 0; i < opcionesConfig.length; i++) {
    //         int y = 110 + i * 40;
    //         if (i == configSelected) {
    //             g.setColor(Color.YELLOW);
    //             g.drawString("> ", 150, y);
    //         } else {
    //             g.setColor(Color.WHITE);
    //         }
    //         String extra = "";
    //         if (i == 0) extra = skinPaletas == 0 ? " [ORIGINAL]" : " [ALTERNATIVA]";
    //         else if (i == 1) extra = pistaMusical == 0 ? " [ORIGINAL]" : " [TEMA 2]";
    //         g.drawString(opcionesConfig[i] + extra, 180, y);
    //     }
    //     g.setFont(new Font("Consolas", Font.PLAIN, 12));
    //     g.setColor(Color.GRAY);
    //     g.drawString("Flechas: mover  |  Enter: cambiar  |  Esc: volver", 180, 560);
    // }
    //
    // public void actualizarConfig() {
    //     long now = System.currentTimeMillis();
    //     if (configActionIndex >= 0) {
    //         if (now - lastConfigKeyTime < 120) return;
    //         for (int code = 0; code < 256; code++) {
    //             if (input.isKeyPressed(code)) {
    //                 KeyBindings.set(KeyBindings.getActionNames()[0], code);
    //                 lastConfigKeyTime = now;
    //                 configActionIndex = -1;
    //                 break;
    //             }
    //         }
    //         return;
    //     }
    //     if (now - lastConfigKeyTime > 120) {
    //         if (input.isMenuUpPressed() || input.isWPressed()) {
    //             configSelected--;
    //             if (configSelected < 0) configSelected = opcionesConfig.length - 1;
    //             lastConfigKeyTime = now;
    //         }
    //         if (input.isMenuDownPressed() || input.isSPressed()) {
    //             configSelected++;
    //             if (configSelected >= opcionesConfig.length) configSelected = 0;
    //             lastConfigKeyTime = now;
    //         }
    //     }
    //     if (input.isEnterPressed() && (now - lastConfigKeyTime > 150)) {
    //         lastConfigKeyTime = now;
    //         switch (configSelected) {
    //             case 0: skinPaletas = (skinPaletas + 1) % 2; break;
    //             case 1: pistaMusical = (pistaMusical + 1) % 2; break;
    //             case 2: configActionIndex = 0; break;
    //             case 3: skinPaletas = 0; pistaMusical = 0; break;
    //             case 4: configMode = false; break;
    //         }
    //     }
    // }
}