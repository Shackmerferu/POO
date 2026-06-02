package py_poo.utils;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import javax.imageio.ImageIO;

public class CargadorRecursos {

    public BufferedImage cargarImagen(String ruta) {
        InputStream is = getClass().getClassLoader().getResourceAsStream(ruta);
        if (is == null) {
            System.err.println("Recurso no encontrado: " + ruta);
            return null;
        }
        try {
            return ImageIO.read(is);
        } catch (IOException e) {
            System.err.println("Error al leer imagen: " + ruta);
            return null;
        }
    }

    public void cargarSonido() {
    }
}
