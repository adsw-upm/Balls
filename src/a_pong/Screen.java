package a_pong;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyListener;
import java.util.ArrayList;

/**
 * Dibujos en pantalla.
 *
 * @author jam
 * @version 26.1.2015
 */
public class Screen {
    private final JFrame frame;
    private final JPanel panel;
    private final ArrayList<Thing> thingList = new ArrayList<Thing>();

    public Screen(String title, int width, int height) {
        frame = new JFrame(title);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        panel = new MyJPanel();
        frame.setSize(10 + width, 30 + height);
        frame.getContentPane().add(panel, BorderLayout.CENTER);
        frame.setVisible(true);
    }

    public void add(Thing thing) {
        synchronized (thingList) {
            thingList.add(thing);
        }
    }

    public void remove(Thing thing) {
        synchronized (thingList) {
            thingList.remove(thing);
        }
    }

    public void reset() {
        synchronized (thingList) {
            thingList.clear();
        }
    }

    public void setKeyListener(KeyListener listener) {
        frame.addKeyListener(listener);
    }

    public void paint() {
        frame.repaint();
    }

    public int getWidth() {
        return panel.getWidth();
    }

    public int getHeight() {
        return panel.getHeight();
    }

    private class MyJPanel
            extends JPanel {
        @Override
        public void paint(Graphics g) {
            g.setColor(Color.WHITE);
            g.fillRect(0, 0, getWidth(), getHeight());
            synchronized (thingList) {
                for (Thing thing : thingList)
                    thing.paint((Graphics2D) g);
            }
        }
    }
}
