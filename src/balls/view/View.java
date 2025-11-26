package balls.view;

import javax.swing.*;
import balls.controller.Controller;
import balls.dto.Position;
import balls.model.Ball;
import balls.model.Room;

import java.awt.*;
import java.util.ArrayList;
import java.util.concurrent.CopyOnWriteArrayList;

import static java.lang.Thread.sleep;

/**
 * This class represents the window that the final user will see and interact with
 */
public class View extends JFrame {
    private final Controller controller;
    private final ControlPanel controlPanel;
    private final Viewer viewer;
    private final DataPanel dataPanel;
    private final Color lightBlueColor = new Color(199, 228, 238);
    private Timer autoTimer;

    public View(Controller controller) {
        this.controller = controller;
        this.controlPanel = new ControlPanel(this);
        this.viewer = new Viewer(this);
        this.dataPanel = new DataPanel(this);

        buildWindow();

        viewer.setFocusable(true);
        viewer.requestFocusInWindow();

    }

    // ---------------------------------- WINDOW BUILDING ----------------------------------

    private void buildWindow() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800,600);

        Container content = this.getContentPane();
        content.setLayout(new GridBagLayout());

        buildLeftPanel(content);
        buildViewer(content);

        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void buildLeftPanel(Container content) {
        JPanel leftPanel = new JPanel(new GridBagLayout());
        leftPanel.setBackground(getLightBlueColor());

        buildControlPanel(leftPanel);
        buildDataPanel(leftPanel);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.VERTICAL;
        gbc.weightx = 0;
        gbc.weighty = 1;
        content.add(leftPanel, gbc);
    }

    private void buildControlPanel(JPanel lp) {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.NORTH;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weighty = 1;
        lp.add(controlPanel, gbc);

        addFireButtonListener();
        addAutoListener();
        addPlayListener();
        addPauseListener();
        addRestartListener();
    }

    private void buildDataPanel(JPanel lp) {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridy = 1;
        gbc.weighty = 0;
        gbc.anchor = GridBagConstraints.SOUTH;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        lp.add(dataPanel, gbc);
    }

    private void buildViewer(Container content) {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.weightx = 3.0;
        gbc.fill = GridBagConstraints.BOTH;
        content.add(viewer, gbc);
    }

    // ---------------------------------- LISTENERS ----------------------------------

    public void addFireButtonListener() {
        controlPanel.getFIRE_BUTTON().addActionListener(e->{
            if (!viewer.getRunning()){
                JOptionPane.showMessageDialog(
                        this, "Dale a play antes de disparar una bola",
                        "Aviso",
                        JOptionPane.WARNING_MESSAGE
                );
            } else{
                addBall();
            }
        });

    }

    private void addAutoListener(){
        controlPanel.getAutoButton().addActionListener(e->{
            if(controlPanel.getAutoButton().isSelected()){
                if(autoTimer!=null){
                    autoTimer.stop();
                }
                autoTimer=new Timer(2000, ev-> controller.addBall());
                autoTimer.start();
            } else if (autoTimer!=null) {
                autoTimer.stop();
            }
        });
    }

    private void addPlayListener() {
        controlPanel.getPlayButton().addActionListener(e -> {
            if (viewer.getThread() == null) {
                viewer.startViewer();
                controller.setPaused(false);
                controller.startPlayerThread();
            }
        });
    }

    private void addPauseListener() {
        controlPanel.getPauseButton().addActionListener(e -> {
            if (viewer.getThread() != null) {
                viewer.pauseViewer();
                controller.setPaused(true);
                controller.stopPlayerThread();
            }
        });
    }

    private void addRestartListener(){
        controlPanel.getRestartButton().addActionListener(e-> {
            stopAllBalls();
            getAllBalls().clear();
            //getAllRooms().clear();
            viewer.restartViewer();
        });
    }

    // ---------------------------------- GETTERS & SETTERS ----------------------------------

    public Color getLightBlueColor() {
        return this.lightBlueColor;
    }

    // ---------------------------------- LINKING METHODS ----------------------------------

    public void addBall() {
        controller.addBall();
    }

    //public void addRoom(Position position, Dimension size) {
        //controller.addRoom(position, size);
    //}

    public ArrayList<Ball> getAllBalls() {
        return controller.getAllBalls();
    }

    public int getViewerWidth() {
        return viewer.getWidth();
    }

    public int getViewerHeight() {
        return viewer.getHeight();
    }

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

    //public ArrayList<Room> getAllRooms() {
        //return controller.getAllRooms();
    //}

    // ------------- DATA PANEL

    public void updateFPS(int fps) {
        this.dataPanel.updateFps(fps);
    }

    public void updateRenderTime(double renderTime) {
        this.dataPanel.updateRenderTime(renderTime);
    }

    public void updateBallCount(int ballCount) {
        this.dataPanel.updateBallCount(ballCount);
    }

    public void stopAllBalls(){
        controller.stopAllBalls();
    }

    // ------------- PLAYER

    public Dimension getPlayerPosition() {
        return controller.getPlayerPosition();
    }

    public Dimension getPlayerSize() {
        return this.controller.getPlayerSize();
    }

    public void setPlayerMovingUp(boolean b) {
        controller.setPlayerMovingUp(b);
    }

    public void setPlayerMovingLeft(boolean b) {
        controller.setPlayerMovingLeft(b);
    }

    public void setPlayerMovingRight(boolean b) {
        controller.setPlayerMovingRight(b);
    }

    public void setPlayerMovingDown(boolean b) {
        controller.setPlayerMovingDown(b);
    }

    public Dimension getCursorPositionInViewer() {
        return viewer.getCursorPosition();
    }

    public double getPlayerRotationAngle() {
        return controller.getPlayerRotationAngle();
    }
}
