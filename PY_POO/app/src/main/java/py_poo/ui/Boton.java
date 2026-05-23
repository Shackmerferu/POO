package py_poo.ui;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;

public class Boton {
    private final String texto;
    private final int x;
    private final int y;
    private final int width;
    private final int height;
    private final Runnable action;
    private boolean seleccionado;

    public Boton(String texto, int x, int y, int width, int height, Runnable action) {
        this.texto = texto;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.action = action;
    }

    public void click() {
        if (action != null) {
            action.run();
        }
    }

    public boolean contains(int mx, int my) {
        return mx >= x && mx <= x + width && my >= y && my <= y + height;
    }

    public void setSeleccionado(boolean seleccionado) {
        this.seleccionado = seleccionado;
    }

    public void renderizar(Graphics g) {
        Color fondo = seleccionado ? new Color(0x4A90E2) : new Color(0x303030);
        Color borde = seleccionado ? Color.WHITE : new Color(0x909090);
        g.setColor(fondo);
        g.fillRoundRect(x, y, width, height, 24, 24);
        g.setColor(borde);
        g.drawRoundRect(x, y, width, height, 24, 24);

        Font fuente = g.getFont().deriveFont(Font.BOLD, 26f);
        g.setFont(fuente);
        g.setColor(Color.WHITE);
        int textWidth = g.getFontMetrics().stringWidth(texto);
        int textHeight = g.getFontMetrics().getAscent();
        g.drawString(texto, x + (width - textWidth) / 2, y + (height + textHeight) / 2 - 6);
    }
}
