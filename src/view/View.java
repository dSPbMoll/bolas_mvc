package view;

import javax.swing.*;
import controller.Controller;
import dto.Position;
import model.Ball;
import model.Room;

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

        GridBagConstraints gbc = new GridBagConstraints();
        GridBagConstraints leftPanelGbc = new GridBagConstraints();
        JPanel content = new JPanel(new GridBagLayout());
        JPanel leftPanel = new JPanel(new GridBagLayout());

        //viewer.getThread().start();

        // --- Left Panel (left) ---
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 0;
        gbc.weighty = 1;
        leftPanel.setBackground(getLightBlueColor());

        // --- Control Panel (top-left) ---
        leftPanelGbc.gridx = 0;
        leftPanelGbc.gridy = 0;
        leftPanelGbc.fill = GridBagConstraints.HORIZONTAL;
        leftPanelGbc.weightx = 0;
        leftPanelGbc.weighty = 0;
        addFireButtonListener(e -> addBall());
        leftPanel.add(controlPanel, leftPanelGbc);

        addPlayListener();
        addPauseListener();

        //Space-filling panel
        JPanel spaceFillingPanel = new JPanel(new GridBagLayout());
        leftPanelGbc.gridy = 1;
        leftPanelGbc.weighty = 1;
        leftPanelGbc.fill = GridBagConstraints.VERTICAL;
        leftPanel.add(spaceFillingPanel, leftPanelGbc);

        // --- Data Panel (bot-left) ---
        leftPanelGbc.gridy = 2;
        leftPanelGbc.weighty = 0;
        leftPanelGbc.fill = GridBagConstraints.HORIZONTAL;
        leftPanel.add(dataPanel, leftPanelGbc);

        content.add(leftPanel, gbc);

        // --- Viewer (right) ---
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.weightx = 3.0;
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
    }
    public void addRoom(Position position, Dimension size) {
        controller.addRoom(position, size);
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
    public int getMinBallSpeedSliderValue() {
        return controlPanel.getMinBallSpeedSliderValue();
    }
    public int getMaxBallSpeedSliderValue() {
        return controlPanel.getMaxBallSpeedSliderValue();
    }
    public int getMinBallSizeSliderValue() {
        return controlPanel.getMinBallSizeSliderValue();
    }
    public int getMaxBallSizeSliderValue() {
        return controlPanel.getMaxBallSizeSliderValue();
    }
    public void addFireButtonListener(ActionListener listener) {
        controlPanel.getFIRE_BUTTON().addActionListener(listener);
    }
    public ArrayList<Room> getAllRooms() {
        return controller.getAllRooms();
    }

    private void addPlayListener() {
        controlPanel.getPlayButton().addActionListener(e -> {

            Thread viewerThread = viewer.getThread();

            if (viewerThread.isAlive() && viewer.getIsRunning() == false) {
                viewer.setIsRunning(true);

            } else if (!viewerThread.isAlive()) {
                viewer.setIsRunning(true);
                viewerThread.start();
            }
        });
    }
    private void addPauseListener() {
        controlPanel.getPauseButton().addActionListener(e -> {

            viewer.setIsRunning(false);

        });
    }
}
