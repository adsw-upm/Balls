package c_readers_writers;

public class MonitorBloques {
    private int nReaders = 0;
    private boolean writing = false;
    private int waitingReaders = 0;
    private int waitingWriters = 0;
    private int maxReadersWaiting = 3;
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
        if (waitingReaders >= maxReadersWaiting)
            vaciando = true;
        while (canRead() == false) {
            waiting();
        }
        waitingReaders--;
        nReaders++;
    }

    private boolean canRead() {
        if (writing) return false;

        if (vaciando) return true;
        if (waitingWriters > 0) return false;
        return true;
    }

    public synchronized void closeReading() {
        notifyAll();
        nReaders--;
        if (nReaders == 0)
            vaciando = false;
    }

    public synchronized void openWriting() {
        waitingWriters++;
        while (canWrite() == false)
            waiting();
        waitingWriters--;
        writing = true;
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
    }

    private synchronized void waiting() {
        try {
            wait();
        } catch (InterruptedException ignored) {
        }
    }
}
