package c_readers_writers;

import java.awt.*;
import java.io.IOException;
import java.util.Random;

public class ManyBalls {
    private static final String TITLE = "Readers-Writers (19.4.2016)";

    private static final Random random = new Random();

    public static final Color READER = Color.BLUE;
    public static final Color WRITER = Color.RED;
    public static final int N_READERS = 5;
    public static final int N_WRITERS = 3;

    public static void main(String[] args)
            throws IOException, InterruptedException {
        RW_Monitor monitor = new RW_Monitor();

        Screen screen = new Screen(TITLE, 500, 500);
        Thread.sleep(1000);

        for (int i = 0; i < N_READERS; i++) {
            RW_Ball.create(screen, monitor)
                    .setColor(READER)
                    .setV(getV(), getV())
                    .start();
        }
        for (int i = 0; i < N_WRITERS; i++) {
            RW_Ball.create(screen, monitor)
                    .setColor(WRITER)
                    .setV(getV(), getV())
                    .start();
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
