package b_critical_zone_nowait;

public class Monitor {
    private boolean ocupado = false;

    public synchronized boolean isOcupado() {
        if (ocupado)
            return true;
        ocupado = true;
        return false;
    }

    public synchronized void libera() {
        ocupado = false;
    }
}
