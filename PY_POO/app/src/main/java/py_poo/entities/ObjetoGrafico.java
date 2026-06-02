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

    public ObjetoGrafico() {
        this.punto = new Point(0, 0);
    }

    public ObjetoGrafico(String sprite) {
        try {
            this.sprite = ImageIO.read(getClass().getClassLoader().getResourceAsStream(sprite));
            this.dimension = new Dimension(this.sprite.getWidth(), this.sprite.getHeight());
            this.punto = new Point(0, 0);
            this.hitbox = new Hitbox(0, 0, (int) dimension.getWidth(), (int) dimension.getHeight());
        } catch (Exception e) {
            System.out.println(e);
        }
    }

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

    public void setSprite(String sprite) {
        try {
            this.sprite = ImageIO.read(getClass().getClassLoader().getResourceAsStream(sprite));
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public void desaparecer() {}

    public void display(java.awt.Graphics g) {
        g.drawImage(sprite, (int) this.getX(), (int) this.getY(), null);
    }

    public void setDimension(Dimension dimension) {
        this.dimension = dimension;
        if (this.hitbox != null) {
            this.hitbox.setDimension((int) dimension.getWidth(), (int) dimension.getHeight());
        }
    }

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

    public void setX(double x) {
        punto.setLocation(x, punto.getY());
        if (this.hitbox != null) {
            this.hitbox.setPosicion((int) x, (int) getY());
        }
    }

    public double getY() {
        return punto.getY();
    }

    public void setY(double y) {
        punto.setLocation(punto.getX(), y);
        if (this.hitbox != null) {
            this.hitbox.setPosicion((int) getX(), (int) y);
        }
    }

    public Hitbox getHitbox() {
        return hitbox;
    }

    public Rectangle getBounds() {
        if (hitbox != null) {
            return hitbox.getBounds();
        }
        return new Rectangle((int) getX(), (int) getY(), getWidth(), getHeight());
    }
}
