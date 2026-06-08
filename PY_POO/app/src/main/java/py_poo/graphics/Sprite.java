package py_poo.graphics;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;

public class Sprite {
    private BufferedImage imagen;

    public Sprite(BufferedImage imagen) {
        this.imagen = imagen;
    }

    public Sprite(String ruta) {
        try{
        this.imagen = ImageIO.read(getClass().getClassLoader().getResourceAsStream(ruta));
        }catch(Exception e){
            System.err.println("No se pudo cargar la imagen en: " + ruta);
            e.printStackTrace();
        }
    }

    public BufferedImage getImagen() {
        return imagen;
    }

    public void dibujar(Graphics g, int x, int y) {
        if (imagen != null) {
            g.drawImage(imagen, x, y, null);
        }
    }

    public void dibujar(Graphics g, int x, int y, int ancho, int alto) {
        if (imagen != null) {
            g.drawImage(imagen, x, y, ancho, alto, null);
        }
    }

    public int getWidth() {
        return imagen != null ? imagen.getWidth() : 0;
    }

    public int getHeight() {
        return imagen != null ? imagen.getHeight() : 0;
    }
}
