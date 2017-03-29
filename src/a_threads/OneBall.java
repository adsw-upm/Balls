package a_threads;

import java.io.IOException;
import java.util.Random;

public class OneBall {
    private static final String TITLE = "one ball";

    private static final Random random = new Random();

    public static void main(String[] args)
            throws IOException, InterruptedException {
        Screen screen = new Screen(TITLE, 500, 500);

        Ball ball = Ball.create(screen)
                .setV(getV(), getV());
        ball.run();
    }

    private static int getV() {
        int v = 5 + random.nextInt(20);
        if (Math.random() < 0.3)
            return -v;
        else
            return v;
    }

}
