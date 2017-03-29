package b_critical_zone_wait;

public class Monitor {
    private boolean ocupado = false;

    public synchronized void entrar() {
        while (ocupado)
            waiting();
        ocupado = true;
    }

    public synchronized void salir() {
        notifyAll();
        ocupado = false;
    }

    private void waiting() {
        try {
            wait();
        } catch (InterruptedException ignored) {
        }
    }
}
