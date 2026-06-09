package py_poo.entities;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;

import py_poo.collision.Hitbox;


public abstract class ObjetoGrafico {


    protected BufferedImage sprite;
    protected Dimension dimension;
    protected Point punto;
    protected Hitbox hitbox;


    protected boolean paraEliminar = false;

    // 3 contructores para
    public ObjetoGrafico() {
        this.punto = new Point(0, 0);
    }


    public ObjetoGrafico(String sprite) {
        try {

            this.sprite = ImageIO.read(getClass().getClassLoader().getResourceAsStream(sprite));
            // Calcula automáticamente la dimensión en base al tamaño real del PNG
            this.dimension = new Dimension(this.sprite.getWidth(), this.sprite.getHeight());
            this.punto = new Point(0, 0);
            // Crea el Hitbox del mismo tamaño que la imagen
            this.hitbox = new Hitbox(0, 0, (int) dimension.getWidth(), (int) dimension.getHeight());
        } catch (Exception e) {
            System.out.println(e); // Si falla, avisa por consola en vez de crashear el juego
        }
    }

    // 3. Constructor completo (Imagen + Tamaño forzado + Posición inicial)
    public ObjetoGrafico(String sprite, Dimension dimension, Point punto) {
        try {
            this.sprite = ImageIO.read(getClass().getClassLoader().getResourceAsStream(sprite));
            this.dimension = dimension;
            this.punto = punto;
            this.hitbox = new Hitbox(punto.x, punto.y, (int) dimension.getWidth(), (int) dimension.getHeight());
        } catch (Exception e) {
            System.out.println(e);
        }
    }


    public BufferedImage getSprite() {
        return sprite;
    }

    // Permite cambiar la "Skin" (imagen) del objeto a mitad del juego
    public void setSprite(String sprite) {
        try {
            this.sprite = ImageIO.read(getClass().getClassLoader().getResourceAsStream(sprite));
        } catch (Exception e) {
            System.out.println(e);
        }
    }


    public void desaparecer() {}

    // Dibuja la imagen en pantalla en su posición actual
    public void display(java.awt.Graphics g) {
        g.drawImage(sprite, (int) this.getX(), (int) this.getY(), null);
    }

    //  Al cambiar el tamaño del objeto, también ajusta el tamaño de su caja de colisión.
    public void setDimension(Dimension dimension) {
        this.dimension = dimension;
        if (this.hitbox != null) {
            this.hitbox.setDimension((int) dimension.getWidth(), (int) dimension.getHeight());
        }
    }

    //  Al cambiar la posición directa, mueve también la caja de colisión.
    public void setPunto(Point punto) {
        this.punto = punto;
        if (this.hitbox != null) {
            this.hitbox.setPosicion(punto.x, punto.y);
        }
    }

    public int getWidth() {
        return (int) dimension.getWidth();
    }

    public int getHeight() {
        return (int) dimension.getHeight();
    }

    public double getX() {
        return punto.getX();
    }

    // Mueve al objeto horizontalmente y arrastra su Hitbox con él
    public void setX(double x) {
        punto.setLocation(x, punto.getY());
        if (this.hitbox != null) {
            this.hitbox.setPosicion((int) x, (int) getY());
        }
    }

    public double getY() {
        return punto.getY();
    }

    // Mueve al objeto verticalmente y arrastra su Hitbox con él
    public void setY(double y) {
        punto.setLocation(punto.getX(), y);
        if (this.hitbox != null) {
            this.hitbox.setPosicion((int) getX(), (int) y);
        }
    }

    // forma en la que traba la colision

    public Hitbox getHitbox() {
        return hitbox;
    }

    // Devuelve un rectángulo matemático para calcular choques (AABB Collision)
    public Rectangle getBounds() {
        if (hitbox != null) {
            return hitbox.getBounds();
        }

        return new Rectangle((int) getX(), (int) getY(), getWidth(), getHeight());
    }

    public Point getPunto() {
        return punto;
    }

    public boolean isParaEliminar() {
        return paraEliminar;
    }

 // señala a quien va a eliminar
    public void marcarParaEliminar() {
        this.paraEliminar = true;
    }


    public void actualizar() {}
}