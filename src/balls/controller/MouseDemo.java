package balls.controller;

import javax.swing.*;
import java.awt.event.*;

public class MouseDemo {
    public static void main(String[] args) {
        JFrame frame = new JFrame("Demo Mouse");
        JPanel panel = new JPanel();

        // Listener que detecta el movimiento del mouse
        panel.addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                int x = e.getX();
                int y = e.getY();
                System.out.println("Mouse en: (" + x + ", " + y + ")");
            }
        });

        frame.add(panel);
        frame.setSize(400, 300);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
}

