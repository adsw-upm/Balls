package d_narrow_bridge;

import java.util.ArrayList;
import java.util.List;

public class NB_Monitor {
    private int nNS = 0;
    private int nSN = 0;
    private List<Thread> queue = new ArrayList<>();

    public synchronized void entraN() {
        queue.add(Thread.currentThread());
        while (cocheEntraNorte() == false)
            waiting();
        queue.remove(0);
        nNS++;
    }

    private boolean cocheEntraNorte() {
        // requisito inexcusable
        if (nSN > 0) return false;

        // politica opcional
        Thread me = Thread.currentThread();
        if (!queue.get(0).equals(me)) return false;

        return true;
    }

    public synchronized void entraS() {
        queue.add(Thread.currentThread());
        while (cocheEntraSur() == false)
            waiting();
        queue.remove(0);
        nSN++;
    }

    private boolean cocheEntraSur() {
        // requisito inexcusable
        if (nNS > 0) return false;

        // politica opcional
        Thread me = Thread.currentThread();
        if (!queue.get(0).equals(me)) return false;

        return true;
    }

    public synchronized void saleN() {
        nSN--;
        notifyAll();
    }

    public synchronized void saleS() {
        nNS--;
        notifyAll();
    }

    private synchronized void waiting() {
        try {
            wait();
        } catch (InterruptedException ignored) {
        }
    }
}
