package py_poo.entities;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;


public abstract class ObjetoGrafico {


    protected BufferedImage sprite;
    // bounds unifica posicion (x, y) y tamaño (ancho, alto) en un solo objeto
    protected Rectangle bounds;
    protected boolean paraEliminar = false;

    // Constructor vacio, inicializa bounds en 0
    public ObjetoGrafico() {
        this.bounds = new Rectangle(0, 0, 0, 0);
    }

    // Constructor con imagen; calcula el tamaño automaticamente del PNG
    public ObjetoGrafico(String sprite) {
        try {
            this.sprite = ImageIO.read(getClass().getClassLoader().getResourceAsStream(sprite));
            this.bounds = new Rectangle(0, 0, this.sprite.getWidth(), this.sprite.getHeight());
        } catch (Exception e) {
            System.out.println(e); // Si falla, avisa por consola en vez de crashear el juego
        }
    }

    // Constructor completo (imagen + tamaño forzado + posicion inicial)
    public ObjetoGrafico(String sprite, Dimension dimension, Point punto) {
        try {
            this.sprite = ImageIO.read(getClass().getClassLoader().getResourceAsStream(sprite));
            this.bounds = new Rectangle(punto.x, punto.y, dimension.width, dimension.height);
        } catch (Exception e) {
            System.out.println(e);
        }
    }


    public BufferedImage getSprite() {
        return sprite;
    }

    // Permite cambiar la "skin" (imagen) del objeto a mitad del juego
    public void setSprite(String sprite) {
        try {
            this.sprite = ImageIO.read(getClass().getClassLoader().getResourceAsStream(sprite));
        } catch (Exception e) {
            System.out.println(e);
        }
    }


    public void desaparecer() {}

    // Dibuja la imagen en pantalla en su posicion actual
    public void display(Graphics g) {
        g.drawImage(sprite, (int) this.getX(), (int) this.getY(), null);
    }

    // Cambia el tamaño del objeto
    public void setDimension(Dimension dimension) {
        this.bounds.setSize(dimension.width, dimension.height);
    }

    // Cambia la posicion del objeto
    public void setPunto(Point punto) {
        this.bounds.setLocation(punto.x, punto.y);
    }

    public int getWidth() {
        return bounds.width;
    }

    public int getHeight() {
        return bounds.height;
    }

    public double getX() {
        return bounds.x;
    }

    // Mueve al objeto horizontalmente
    public void setX(double x) {
        bounds.x = (int) x;
    }

    public double getY() {
        return bounds.y;
    }

    // Mueve al objeto verticalmente
    public void setY(double y) {
        bounds.y = (int) y;
    }

    // Devuelve un rectangulo matematico para calcular choques (AABB Collision)
    public Rectangle getBounds() {
        return bounds;
    }

    public Point getPunto() {
        return bounds.getLocation();
    }

    public boolean isParaEliminar() {
        return paraEliminar;
    }

    // Señala a quien va a eliminar
    public void marcarParaEliminar() {
        this.paraEliminar = true;
    }


    public void actualizar() {}
}
