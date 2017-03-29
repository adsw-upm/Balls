package a_threads;

import java.awt.*;
import java.util.Random;

public class Ball
        implements Screen.Thing, Runnable {
    private static final Random random = new Random();
    private final Screen screen;

    private int cx;
    private int cy;
    private int r = 10;
    private Color body = new Color(random.nextInt(255), random.nextInt(255), random.nextInt(255));

    private int vx;
    private int vy;
    private volatile boolean running;

    public static Ball create(Screen screen) {
        return new Ball(screen);
    }

    private Ball(Screen screen) {
        this.screen = screen;
        screen.add(this);
    }

    public synchronized Ball setColor(Color color) {
        body = color;
        return this;
    }

    public synchronized Ball setXY(int x0, int y0) {
        cx = x0;
        cy = y0;
        return this;
    }

    public synchronized Ball setV(int vx, int vy) {
        this.vx = vx;
        this.vy = vy;
        return this;
    }

    public synchronized void quit() {
        running = false;
    }

    @Override
    public void run() {
        double v = Math.sqrt(vx * vx + vy * vy);
        long delta = v < 100 ? 100 : (long) (10 * 100 / v);
        running = true;
        long time = System.currentTimeMillis();

        int nSteps = 200 + (int) (200 * Math.random());
        for (int i = 0; i < nSteps; i++) {
            int width = screen.getWidth();
            int height = screen.getHeight();

            long t = System.currentTimeMillis();
            double dt = (t - time) / 100.0;

            // nueva posición al cabo de un tiempo dt
            int nx = (int) (cx + vx * dt);
            int ny = (int) (cy + vy * dt);
            int nvx = vx;
            int nvy = vy;

            // si se sale del cuadro, rebota
            if (nx < r) {
                nx = r;
                if (nvx < 0)
                    nvx = -nvx;
            } else if (nx > width - r) {
                nx = width - r;
                if (nvx > 0)
                    nvx = -nvx;
            }
            if (ny < r) {
                ny = r;
                if (nvy < 0)
                    nvy = -nvy;
            } else if (ny > height - r) {
                ny = height - r;
                if (nvy > 0)
                    nvy = -nvy;
            }

            // me muevo al nuevo lugar
            setXY(nx, ny);
            setV(nvx, nvy);
            screen.paint();

            time = System.currentTimeMillis();
            nap(delta);
        }

        screen.remove(this);
        screen.paint();
    }

    private void nap(long ms) {
        try {
            Thread.sleep(ms);
        } catch (InterruptedException ignored) {
        }
    }

    public synchronized void paint(Graphics2D g) {
        if (body != null) {
            g.setColor(body);
            g.fillOval(cx - r, cy - r, 2 * r, 2 * r);
        }
        g.setColor(Color.BLACK);
        g.drawOval(cx - r, cy - r, 2 * r, 2 * r);
    }
}
