package a_pong;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class Paddle
        implements Thing, KeyListener {
    private static final int HEIGHT = 10;
    private static final int WIDTH = 100;

    private int cx;
    private int cy;
    private Color body = Color.DARK_GRAY;

    private final Screen screen;

    public static Paddle create(Screen screen) {
        return new Paddle(screen);
    }

    private Paddle(Screen screen) {
        this.screen = screen;
        screen.add(this);
    }

    public synchronized Paddle set(Color color) {
        body = color;
        return this;
    }

    public synchronized Paddle setXY(int x0, int y0) {
        cx = x0;
        cy = y0;
        return this;
    }

    public synchronized Color getColor() {
        return body;
    }

    public synchronized void paint(Graphics2D g) {
        if (body != null) {
            g.setColor(body);
            g.fillRect(cx - WIDTH / 2, cy - HEIGHT / 2, WIDTH, HEIGHT);
        }
    }

    public synchronized boolean hit(int cx, int cy0, int cy2) {
        if (cx < this.cx - WIDTH / 2)
            return false;
        if (cx > this.cx + WIDTH / 2)
            return false;
        if (cy0 < this.cy && cy2 >= this.cy)
            return true;
        if (cy0 > this.cy && cy2 <= this.cy)
            return true;
        return false;
    }

    @Override
    public synchronized void keyTyped(KeyEvent e) {
    }

    @Override
    public synchronized void keyPressed(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_LEFT:
                cx = Math.max(WIDTH / 2, cx - 15);
                break;
            case KeyEvent.VK_RIGHT:
                cx = Math.min(cx + 15, screen.getWidth() - WIDTH / 2);
                break;
        }
        screen.paint();
    }

    @Override
    public synchronized void keyReleased(KeyEvent e) {
    }
}
