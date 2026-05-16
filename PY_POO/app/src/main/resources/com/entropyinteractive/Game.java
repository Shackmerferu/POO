package com.entropyinteractive;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Component;
import java.awt.Frame;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferStrategy;

public abstract class Game extends GameLoop {
   private Frame frame;
   private Canvas canvas;
   private BufferStrategy buffer;
   private static Keyboard keyboard;
   private static Mouse mouse;
   private static MouseWheel mouseWheel;

   public Game(String title, int width, int height) {
      this.frame = new Frame(title);
      this.frame.setResizable(false);
      this.canvas = new Canvas();
      this.canvas.setIgnoreRepaint(true);
      this.frame.add(this.canvas);
      this.canvas.setSize(width, height);
      this.frame.pack();
      this.frame.setLocationRelativeTo((Component)null);
      this.frame.setVisible(true);
      this.canvas.createBufferStrategy(2);
      this.buffer = this.canvas.getBufferStrategy();
      keyboard = new Keyboard();
      mouse = new Mouse();
      mouseWheel = new MouseWheel();
      this.canvas.addKeyListener(keyboard);
      this.canvas.addMouseListener(mouse);
      this.canvas.addMouseMotionListener(mouse);
      this.canvas.addMouseWheelListener(mouseWheel);
      this.canvas.requestFocus();
   }

   public int getWidth() {
      return this.canvas.getWidth();
   }

   public int getHeight() {
      return this.canvas.getHeight();
   }

   public String getTitle() {
      return this.frame.getTitle();
   }

   public static Keyboard getKeyboard() {
      return keyboard;
   }

   public static Mouse getMouse() {
      return mouse;
   }

   public static MouseWheel getMouseWheel() {
      return mouseWheel;
   }

   public void startup() {
      this.gameStartup();
   }

   public void update(double delta) {
      keyboard.update();
      mouse.update();
      mouseWheel.update();
      this.gameUpdate(delta);
   }

   public void draw() {
      Graphics2D g = (Graphics2D)this.buffer.getDrawGraphics();
      g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
      g.setColor(Color.BLACK);
      g.fillRect(0, 0, this.canvas.getWidth(), this.canvas.getHeight());
      this.gameDraw(g);
      this.buffer.show();
      g.dispose();
   }

   public void shutdown() {
      this.gameShutdown();
      this.frame.dispose();
   }

   public abstract void gameStartup();

   public abstract void gameUpdate(double var1);

   public abstract void gameDraw(Graphics2D var1);

   public abstract void gameShutdown();
}
