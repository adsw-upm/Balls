package a_pong;

import java.awt.*;
import java.io.IOException;
import java.util.Random;

/**
 * Created by jose on 09-Mar-16.
 */
public class Game {
    private static final String TITLE = "PING PONG";
    private static final int N_BALLS = 2;

    private static final Random random = new Random();

    public static void main(String[] args)
            throws IOException, InterruptedException {
        Screen screen = new Screen(TITLE, 500, 500);
        Thread.sleep(1000);

        Paddle paddle = Paddle.create(screen)
                .set(Color.DARK_GRAY)
                .setXY(250, 100);
        screen.setKeyListener(paddle);

        for (int i = 0; i < N_BALLS; i++) {
            Runnable ball = Ball.create(screen)
                    .set(getColor())
                    .setV(getV(), getV())
                    .setPaddle(paddle);
            Thread thread = new Thread(ball);
            thread.start();
        }
    }

    private static Color getColor() {
        return new Color(random.nextInt(255), random.nextInt(255), random.nextInt(255));
    }

    private static int getV() {
        int v = 5 + (int) (10 * Math.random());
        if (Math.random() < 0.3)
            return -v;
        else
            return v;
    }

}
