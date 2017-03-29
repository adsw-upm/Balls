package d_narrow_bridge;

import javax.swing.*;
import java.awt.*;
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
        panel.setPreferredSize(new Dimension(width, height));
        frame.getContentPane().add(panel, BorderLayout.CENTER);
        frame.pack();
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

    public void paint() {
        frame.repaint();
    }

    public int getWidth() {
        return panel.getWidth();
    }

    public int getHeight() {
        return panel.getHeight();
    }

    public interface Thing {
        void paint(Graphics2D g);
    }

    private class MyJPanel
            extends JPanel {
        @Override
        public void paint(Graphics g) {
            int width = panel.getWidth();
            int height = panel.getHeight();

            g.setColor(Color.WHITE);
            g.fillRect(0, 0, width, height);

            int h3 = height / 3;
            g.setColor(Color.GREEN);
            g.fillRect(0, h3, width, h3);

            synchronized (thingList) {
                for (Thing thing : thingList)
                    thing.paint((Graphics2D) g);
            }
        }
    }
}
