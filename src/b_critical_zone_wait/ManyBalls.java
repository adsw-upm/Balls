package b_critical_zone_wait;

import java.util.Random;

public class ManyBalls {
    private static final String TITLE = "critical zone (wait)";
    private static final int N_BALLS = 7;

    private static final Random random = new Random();

    public static void main(String[] args)
            throws InterruptedException {
        Monitor monitor = new Monitor();

        Screen screen = new Screen(TITLE, 500, 500);

        for (int i = 0; i < N_BALLS; i++) {
            Runnable ball = MonBall.create(screen, monitor)
                    .setV(getV(), getV());
            Thread thread = new Thread(ball);
            thread.start();
            Thread.sleep(1000); // milisegundos
        }
    }

    private static int getV() {
        int v = 5 + random.nextInt(20);
        if (Math.random() < 0.3)
            return -v;
        else
            return v;
    }

}
