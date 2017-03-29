package c_readers_writers;

public class RW_Monitor {
    private static final int MAX_READERS_WAITING = 3;
    private int nReaders = 0;
    private boolean writing = false;

    private int waitingWriters = 0;
    private int waitingReaders = 0;
    private boolean vaciando = false;

    public synchronized int getNReaders() {
        return nReaders;
    }

    public synchronized int getNWriters() {
        if (writing)
            return 1;
        else
            return 0;
    }

    public synchronized void openReading() {
        while (vaciando)
            waiting();

        waitingReaders++;
        if (waitingReaders > MAX_READERS_WAITING)
            vaciando = true;
        while (canRead() == false)
            waiting();
        waitingReaders--;
        nReaders++;
        invariant();
    }

    private boolean canRead() {
        if (writing) return false;
        if (vaciando) return true;
        if (waitingWriters > 0) return false;
        return true;
    }

    public synchronized void closeReading() {
        nReaders--;
        if (nReaders == 0)
            vaciando = false;
        notifyAll();
        invariant();
    }

    public synchronized void openWriting() {
        waitingWriters++;
        while (canWrite() == false)
            waiting();
        waitingWriters--;
        writing = true;
        invariant();
    }

    private boolean canWrite() {
        if (writing) return false;
        if (nReaders > 0) return false;
        if (vaciando) return false;
        return true;
    }

    public synchronized void closeWriting() {
        writing = false;
        notifyAll();
        invariant();
    }

    private void invariant() {
        if (getNReaders() > 0 && getNWriters() > 0)
            System.err.println("ERROR");
        if (getNWriters() > 1)
            System.err.println("ERROR");
    }

    private synchronized void waiting() {
        try {
            wait();
        } catch (InterruptedException ignored) {
        }
    }
}
