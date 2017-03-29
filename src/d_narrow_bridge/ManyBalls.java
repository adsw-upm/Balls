package d_narrow_bridge;

import java.util.Random;

public class ManyBalls {
    private static final String TITLE = "narrow bridge";
    private static final int N_BALLS = 8;

    private static final Random random = new Random();

    public static void main(String[] args)
            throws InterruptedException {
        NB_Monitor monitor = new NB_Monitor();

        Screen screen = new Screen(TITLE, 500, 500);

        for (int i = 0; i < N_BALLS; i++) {
            Runnable ball = NB_Ball.create(screen, monitor)
                    .setV(getV(), getV());
            Thread thread = new Thread(ball);
            thread.start();
            Thread.sleep(1000); // milisegundos
        }
    }

    private static int getV() {
        return 5 + random.nextInt(30);
    }

}
