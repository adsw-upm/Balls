package d_narrow_bridge;

import java.awt.*;
import java.util.Random;

public class NB_Ball
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

    private final NB_Monitor monitor;

    public static NB_Ball create(Screen screen, NB_Monitor monitor) {
        return new NB_Ball(screen, monitor);
    }

    private NB_Ball(Screen screen, NB_Monitor monitor) {
        this.screen = screen;
        this.monitor = monitor;
        screen.add(this);
    }

    public synchronized NB_Ball setColor(Color color) {
        body = color;
        return this;
    }

    public synchronized NB_Ball setXY(int x0, int y0) {
        cx = x0;
        cy = y0;
        return this;
    }

    public synchronized NB_Ball setV(int vx, int vy) {
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

        while (running) {
            int width = screen.getWidth();
            int height = screen.getHeight();

            long t = System.currentTimeMillis();
            double dt = (t - time) / 100.0;

            // nueva posicion al cabo de un tiempo dt
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

            int hN = height / 3;
            int hS = height * 2 / 3;
            if (cy < hN && ny >= hN)
                monitor.entraN();
            if (cy > hN && ny <= hN)
                monitor.saleN();
            if (cy < hS && ny >= hS)
                monitor.saleS();
            if (cy > hS && ny <= hS)
                monitor.entraS();

            // me muevo al nuevo lugar
            setXY(nx, ny);
            setV(nvx, nvy);
            screen.paint();

            time = System.currentTimeMillis();
            nap(delta);
        }
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
