package a_pong;

import java.awt.*;

public class Ball
        implements Thing, Runnable {
    private static final int R = 10;
    private int cx, cy;
    private Color body = Color.CYAN;

    private final Screen screen;
    private Paddle paddle;

    private double vx, vy;
    private volatile boolean running;

    public static Ball create(Screen screen) {
        return new Ball(screen);
    }

    private Ball(Screen screen) {
        this.screen = screen;
        screen.add(this);
    }

    public synchronized Ball set(Color color) {
        body = color;
        return this;
    }

    public synchronized Ball setXY(int x0, int y0) {
        cx = x0;
        cy = y0;
        return this;
    }

    public synchronized Ball setV(double vx, double vy) {
        this.vx = vx;
        this.vy = vy;
        return this;
    }

    public synchronized Ball setPaddle(Paddle paddle) {
        this.paddle = paddle;
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
            int W = screen.getWidth();
            int H = screen.getHeight();

            long t = System.currentTimeMillis();
            double dt = (t - time) / 100.0;

            int nx = (int) (cx + vx * dt);
            int ny = (int) (cy + vy * dt);
            double nvx = vx;
            double nvy = vy;

            if (nx < 0) {
                nx = 0;
                nvx = Math.abs(vx);
            }
            if (ny < 0) {
                ny = 0;
                nvy = Math.abs(vy);
            }
            if (nx > W) {
                nx = W;
                nvx = -Math.abs(vx);
            }
            if (ny > H) {
                ny = H;
                nvy = -Math.abs(vy);
            }
            if (paddle != null && paddle.hit(nx, cy, ny)) {
                ny = cy;
                nvy = -vy;
            }

            setXY(nx, ny);
            setV(nvx, nvy);
            screen.paint();

            time = System.currentTimeMillis();
            nap(delta);
        }

        screen.remove(this);
        screen.paint();
    }

    private void nap(long delta) {
        try {
            Thread.sleep(delta);
        } catch (InterruptedException ignored) {
        }
    }

    public synchronized void paint(Graphics2D g) {
        if (body != null) {
            g.setColor(body);
            g.fillOval(cx - R, cy - R, 2 * R, 2 * R);
        }
        g.setColor(Color.BLACK);
        g.drawOval(cx - R, cy - R, 2 * R, 2 * R);
    }
}
