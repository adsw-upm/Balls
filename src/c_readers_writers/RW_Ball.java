package c_readers_writers;

import java.awt.*;
import java.util.Random;

public class RW_Ball
        extends Thread
        implements Screen.Thing {
    private static final Random random = new Random();
    private final Screen screen;

    private int cx;
    private int cy;
    private int r = 10;
    private Color body = new Color(random.nextInt(255), random.nextInt(255), random.nextInt(255));

    private int vx;
    private int vy;
    private volatile boolean running;

    private final RW_Monitor monitor;
    private int nWritersIn;

    public static RW_Ball create(Screen screen, RW_Monitor monitor) {
        return new RW_Ball(screen, monitor);
    }

    private RW_Ball(Screen screen, RW_Monitor monitor) {
        this.screen = screen;
        this.monitor = monitor;
        screen.add(this);
    }

    public synchronized RW_Ball setColor(Color color) {
        body = color;
        return this;
    }

    public synchronized RW_Ball setXY(int x0, int y0) {
        cx = x0;
        cy = y0;
        return this;
    }

    public synchronized RW_Ball setV(int vx, int vy) {
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

            if (cy < height / 2 && ny >= height / 2)
                getIn();

            if (cy > height / 2 && ny <= height / 2)
                getOut();

            // me muevo al nuevo lugar
            setXY(nx, ny);
            setV(nvx, nvy);
            screen.paint();

            time = System.currentTimeMillis();
            waiting(delta);
        }

        screen.remove(this);
        screen.paint();
    }

    private void waiting(long delta) {
        try {
            Thread.sleep(delta);
        } catch (InterruptedException ignored) {
        }
    }

    private void getIn() {
        if (body == ManyBalls.READER)
            monitor.openReading();
        if (body == ManyBalls.WRITER)
            monitor.openWriting();
    }

    private void getOut() {
        if (body == ManyBalls.READER)
            monitor.closeReading();
        if (body == ManyBalls.WRITER)
            monitor.closeWriting();
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
