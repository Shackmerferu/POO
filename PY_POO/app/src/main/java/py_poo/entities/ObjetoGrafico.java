package py_poo.entities;

import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;


public abstract class ObjetoGrafico {
    protected BufferedImage sprite;
    protected Dimension dimension;
    protected Point punto;


    public ObjetoGrafico() {}

    public ObjetoGrafico(String sprite){
        try {
            this.sprite = ImageIO.read(getClass().getClassLoader().getResourceAsStream(sprite));
            this.dimension = new Dimension(this.sprite.getWidth(), this.sprite.getHeight());
            this.punto = new Point(0,0);
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public ObjetoGrafico(String sprite, Dimension dimension, Point punto){
        try {
            this.sprite = ImageIO.read(getClass().getClassLoader().getResourceAsStream(sprite));
            this.dimension = dimension;
            this.punto = punto;
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
    
    public void desaparecer(){}

    public void display(Graphics2D g) {
        g.drawImage(sprite, (int) this.getX(), (int) this.getY(), null);
    }

    public void setDimension(Dimension dimension){
        this.dimension = dimension;
    }

    public void setPunto(Point punto){
        this.punto = punto;
    }

    public int getWidth() {
        return (int)dimension.getWidth();
    }

    public int getHeight() {
        return (int)dimension.getHeight();
    }

    public double getX() {
        return punto.getX();
    }

    public void setX(double x) {
        punto.setLocation(x, punto.getY());
    }

    public double getY() {
        return punto.getY();
    }

    public void setY(double y) {punto.setLocation(punto.getX(), y);}
}
