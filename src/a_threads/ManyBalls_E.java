package a_threads;

import java.util.Random;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class ManyBalls_E {
    private static final String TITLE = "many balls";
    private static final int N_BALLS = 23;

    private static final Random random = new Random();

    public static void main(String[] args) {
        Screen screen = new Screen(TITLE, 500, 500);

        int nThreads = 5;
        Executor executor = Executors.newFixedThreadPool(nThreads);

        for (int i = 0; i < N_BALLS; i++) {
            Runnable ball = Ball.create(screen)
                    .setV(getV(), getV());
            executor.execute(ball);
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
