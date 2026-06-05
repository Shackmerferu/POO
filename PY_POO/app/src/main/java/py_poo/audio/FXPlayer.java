package py_poo.audio;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;

public class FXPlayer {
    private Map<String, Clip> sonido;
    private static int volumen;
    private boolean mute;

    public FXPlayer() {
        this.sonido = new HashMap<>();
        this.mute = false;
    }

    public void cargarSonido(String nombre, String ruta) {
        try {
            java.net.URL url = getClass().getClassLoader().getResource(ruta);
            if (url == null) {
                System.err.println("No se pudo encontrar el recurso de sonido: " + ruta);
                return;
            }
            AudioInputStream audio = AudioSystem.getAudioInputStream(url);
            Clip clip = AudioSystem.getClip();
            clip.open(audio);
            sonido.put(nombre, clip);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void cargarSonidoRecurso(String nombre, String resourcePath) {
        try {
            InputStream is = getClass().getClassLoader().getResourceAsStream(resourcePath);
            if (is == null) {
                System.err.println("Recurso de sonido no encontrado: " + resourcePath);
                return;
            }
            AudioInputStream audio = AudioSystem.getAudioInputStream(is);
            Clip clip = AudioSystem.getClip();
            clip.open(audio);
            sonido.put(nombre, clip);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void reproducir(String nombre) {
        Clip clip = sonido.get(nombre);
        if (clip != null) {
            clip.setFramePosition(0);
            clip.start();
        }
    }

    public void detener(String nombre) {
        Clip clip = sonido.get(nombre);
        if (clip != null) {
            clip.stop();
        }
    }

    public void setVolumen(String nombre, String nivel) {
        Clip clip = sonido.get(nombre);

        if (clip != null) {
            // Buscamos el control de volumen de ese clip
            FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);

            // Configuramos los 3 niveles
            switch (nivel.toLowerCase()) {
                case "bajo":
                    gainControl.setValue(-30.0f); // Reduce mucho el volumen
                    break;
                case "medio":
                    gainControl.setValue(-10.0f); // Reduce la mitad del volumen
                    break;
                case "fuerte":
                default:
                    gainControl.setValue(0.0f);   // Volumen normal/máximo del archivo
                    break;
            }
        }
    }


    public void mutear() {
        mute = !mute;
        float gain = mute ? -80.0f : 0.0f;
        for (Clip clip : sonido.values()) {
            if (clip != null && clip.isOpen()) {
                try {
                    FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
                    gainControl.setValue(gain);
                } catch (IllegalArgumentException e) {
                    // algunos clips puede que no soporten MASTER_GAIN
                }
            }
        }
    }
    public void repetir(String nombre) {
        Clip clip = sonido.get(nombre);
        if (clip != null) {
            clip.loop(Clip.LOOP_CONTINUOUSLY);
        }
    }
}
