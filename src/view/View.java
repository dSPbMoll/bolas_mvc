package view;

import javax.swing.*;
import controller.Controller;
import model.Ball;

import java.awt.*;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * This class represents the window that the final user will see and interact with
 */
public class View extends JFrame {
    private final Controller controller;
    private final ControlPanel controlPanel;
    private final Viewer viewer;
    private final DataPanel dataPanel;
    private final Color lightBlueColor = new Color(199, 228, 238);

    public View(Controller controller) {
        this.controller = controller;
        this.controlPanel = new ControlPanel(this);
        this.viewer = new Viewer(this);
        this.dataPanel = new DataPanel(this);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800,600);

        JPanel content = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        JPanel leftPanel = new JPanel(new BorderLayout());

        viewer.getThread().start();

        // --- Left Panel (left) ---
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.VERTICAL;
        gbc.weightx = 0;
        gbc.weighty = 1.0;
        //leftPanel.setBackground();
        content.add(leftPanel, gbc);

        // --- Control Panel (top-left) ---
        addFireButtonListener(e -> addBall());
        leftPanel.add(controlPanel, BorderLayout.CENTER);

        // --- Data Panel (bot-left) ---
        setBackground(Color.GREEN);
        leftPanel.add(dataPanel, BorderLayout.SOUTH);

        // --- Viewer (right) ---
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        content.add(viewer, gbc);

        add(content);
        setLocationRelativeTo(null);
        setVisible(true);
    }
    public Color getLightBlueColor() {
        return this.lightBlueColor;
    }
    public void addBall() {
        controller.addBall();
        System.out.println("Funciono");
    }
    public void addRoom(int x, int y, int width, int height) {
        controller.addRoom(x, y, width, height);
    }
    public CopyOnWriteArrayList<Ball> getAllBalls() {
        return controller.getAllBalls();
    }
    public int getViewerWidth() {
        return viewer.getWidth();
    }
    public int getViewerHeight() {
        return viewer.getHeight();
    }
    // public void startViewer() {
        //viewer.startViewer();
    //}
    public void addFireButtonListener(ActionListener listener) {
        controlPanel.getFIRE_BUTTON().addActionListener(listener);
    }
}
