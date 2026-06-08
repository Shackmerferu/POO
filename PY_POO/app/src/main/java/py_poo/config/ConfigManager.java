package py_poo.config;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

public class ConfigManager {
    private static final String ARCHIVO = "configuracion.txt"; //archivo mapeo de configuracion de teclas , sonido y atributos 

    private float volumen;
    private boolean fullscreen;
    private boolean soundEnabled;
    private boolean soundFxEnabled;
    private boolean musicEnabled;
    private Map<String, Integer> keyBindings;

    public ConfigManager() {
        this.volumen = 100;
        this.fullscreen = false;
        this.soundEnabled = true;
        this.soundFxEnabled = true;
        this.musicEnabled = true;
        this.keyBindings = new HashMap<>();
    }

    public void cargar() {//se carga el archivo
        File f = new File(ARCHIVO);
        if (!f.exists()) {
            guardar();
            return;
        }
        try (BufferedReader br = new BufferedReader(new FileReader(f))) { //se lee linea por linea
            String linea;
            while ((linea = br.readLine()) != null) {
                linea = linea.trim();
                if (linea.isEmpty() || linea.startsWith("#")) continue; //si en la linea detectamos # estamos ante un parametro
                int idx = linea.indexOf('=');//si en la linea detectamos = estamos ante un valor de parametro
                if (idx < 0) continue;
                String key = linea.substring(0, idx).trim();//guardamos el parametro
                String val = linea.substring(idx + 1).trim();//guardamos el valor
                switch (key) {
                    case "volumen":
                        try { volumen = Float.parseFloat(val); } catch (NumberFormatException e) {}
                        break;
                    case "fullscreen":
                        fullscreen = Boolean.parseBoolean(val);
                        break;
                    case "soundEnabled":
                        soundEnabled = Boolean.parseBoolean(val);
                        break;
                    case "soundFxEnabled":
                        soundFxEnabled = Boolean.parseBoolean(val);
                        break;
                    case "musicEnabled":
                        musicEnabled = Boolean.parseBoolean(val);
                        break;
                    default:
                        if (key.startsWith("key_")) {
                            try {
                                keyBindings.put(key.substring(4), Integer.parseInt(val));
                            } catch (NumberFormatException e) {}
                        }
                        break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        aplicarKeyBindings();
    }

    public void guardar() {//guardamos en configuracion para posterior uso
        try (PrintWriter pw = new PrintWriter(new FileWriter(ARCHIVO))) {
            pw.println("volumen=" + volumen);
            pw.println("fullscreen=" + fullscreen);
            pw.println("soundEnabled=" + soundEnabled);
            pw.println("soundFxEnabled=" + soundFxEnabled);
            pw.println("musicEnabled=" + musicEnabled);
            for (String accion : KeyBindings.getActionNames()) {
                int codigo = KeyBindings.get(accion);
                pw.println("key_" + accion + "=" + codigo);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void aplicarKeyBindings() {//se aplica los cambios de teclas dinamicamente
        for (Map.Entry<String, Integer> entry : keyBindings.entrySet()) {
            KeyBindings.set(entry.getKey(), entry.getValue());
        }
    }

    public float getVolumen() { return volumen; }
    public void setVolumen(float v) { this.volumen = v; }
    public boolean isFullscreen() { return fullscreen; }
    public void setFullscreen(boolean v) { this.fullscreen = v; }
    public boolean isSoundEnabled() { return soundEnabled; }
    public void setSoundEnabled(boolean v) { this.soundEnabled = v; }
    public boolean isSoundFxEnabled() { return soundFxEnabled; }
    public void setSoundFxEnabled(boolean v) { this.soundFxEnabled = v; }
    public boolean isMusicEnabled() { return musicEnabled; }
    public void setMusicEnabled(boolean v) { this.musicEnabled = v; }

    public void leer() { cargar(); }
    public void escribir() { guardar(); }
}
