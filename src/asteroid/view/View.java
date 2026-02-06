package asteroid.view;

import javax.swing.*;
import asteroid.controller.GameController;
import asteroid.controller.entity.EntityType;
import asteroid.dto.BodyDto;
import asteroid.dto.EntityParamsDto;
import asteroid.dto.ShipMovementDto;
import asteroid.view.renderable.Renderable;
import config.player.ControlConfig;
import config.simulation.WorldConfig;
import helpers.CardinalDirection;

import java.awt.*;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.HashMap;

import static java.lang.Thread.sleep;

/**
 * This class represents the window that the final user will see and interact with
 */
public class View extends JFrame {
    private final GameController controller;
    private final ControlPanel controlPanel;
    private final Viewer viewer;
    private final DataPanel dataPanel;
    private final Color lightBlueColor = new Color(199, 228, 238);
    private Timer autoTimer;
    private Image backgroundImage;

    public View(GameController controller,
                HashMap<ControlConfig, String> playerControlConfigs,
                HashMap<WorldConfig, Integer> worldConfigs) {

        this.controller = controller;
        this.controlPanel = new ControlPanel(this);
        this.viewer = new Viewer(this, playerControlConfigs, worldConfigs);
        this.dataPanel = new DataPanel(this);

        buildWindow(worldConfigs);

        viewer.setFocusable(true);
        viewer.requestFocusInWindow();

    }

    // ---------------------------------- WINDOW BUILDING ----------------------------------

    private void buildWindow(HashMap<WorldConfig, Integer> worldConfig) {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(worldConfig.get(WorldConfig.WIDTH), worldConfig.get(WorldConfig.WIDTH));

        ImagePanel content=new ImagePanel("src/img/galaxy4.jpg");
        content.setLayout(new GridBagLayout());
        setContentPane(content);

        buildLeftPanel(content);
        buildViewer(content);

        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void buildLeftPanel(Container content) {
        JPanel leftPanel = new JPanel(new GridBagLayout());
        leftPanel.setOpaque(false);

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
        controlPanel.setOpaque(false);
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
        dataPanel.setOpaque(false);
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
                        this, "Dale a play antes de disparar un asteroide",
                        "Aviso",
                        JOptionPane.WARNING_MESSAGE
                );
            } else{
                controller.generateEntityInLifeGenerator();
            }
        });

    }

    private void addAutoListener(){
        controlPanel.getAutoButton().addActionListener(e -> {
            controller.setRunningLifeGenerator(controlPanel.getAutoButton().isSelected());
        });
    }

    private void addPlayListener() {
        controlPanel.getPlayButton().addActionListener(e -> {
            if (viewer.getThread() == null) {
                viewer.startViewer();
                controller.startLifeGenerator();
                controller.startAllMovingBodies();
                viewer.startAllRenderableSS();
            }
        });
    }

    private void addPauseListener() {
        controlPanel.getPauseButton().addActionListener(e -> {
            if (viewer.getThread() != null) {
                viewer.pauseViewer();
                controller.stopLifeGenerator();
                controller.stopAllMovingBodies();
                viewer.stopAllRenderableSS();
            }
        });
    }

    private void addRestartListener(){
        controlPanel.getRestartButton().addActionListener(e-> {
            if (!viewer.getRunning()){
                JOptionPane.showMessageDialog(
                        this, "Dale a play antes de hacer un restart",
                        "Aviso",
                        JOptionPane.WARNING_MESSAGE
                );
            } else{
                controller.stopAllMovingBodies();
                deleteAllEntities();
                viewer.restartViewer();
            }
        });
    }

    // ---------------------------------- GETTERS & SETTERS ----------------------------------

    public Color getLightBlueColor() {
        return this.lightBlueColor;
    }

    // ---------------------------------- LINKING METHODS ----------------------------------

    public ArrayList<BodyDto> getAllBodyDtosByType(EntityType type) {
        return controller.getAllBodyDtosByType(type);
    }

    public int getViewerWidth() {
        return viewer.getWidth();
    }

    public int getViewerHeight() {
        return viewer.getHeight();
    }

    public int getMinAsteroidSpeedSliderValue() {
        return controlPanel.getMinAsteroidSpeedSliderValue();
    }

    public int getMaxAsteroidSpeedSliderValue() {
        return controlPanel.getMaxAsteroidSpeedSliderValue();
    }

    public int getMinAsteroidSizeSliderValue() {
        return controlPanel.getMinAsteroidSizeSliderValue();
    }

    public int getMaxAsteroidSizeSliderValue() {
        return controlPanel.getMaxAsteroidSizeSliderValue();
    }

    public void addRenderable(Renderable renderable) {
        this.viewer.addRenderable(renderable);
    }

    public void deleteRenderable(EntityType type, long entityId) {
        viewer.deleteRenderable(type, entityId);
    }

    public void deleteAllRenderables() {
        viewer.deleteAllRenderables();
    }

    public void sendNewEntityParamsToLifeGenerator(EntityParamsDto params) {
        controller.sendNewEntityParamsToLifeGenerator(params);
    }

    // ------------- DATA PANEL

    public void updateFPS(int fps) {
        this.dataPanel.updateFps(fps);
    }

    public void updateRenderTime(double renderTime) {
        this.dataPanel.updateRenderTime(renderTime);
    }

    public void updateAsteroidCount(int asteroidCount) {
        this.dataPanel.updateAsteroidCount(asteroidCount);
    }

    public void stopAllMovingBodies(){
        controller.stopAllMovingBodies();
    }

    public void deleteAllEntities() {
        controller.deleteAllEntities();
    }

    // ------------- PLAYER

    public void setPlayerMoving(int entityId, boolean b, CardinalDirection direction) {
        controller.setPlayerMoving(entityId, b, direction);
    }

    public void setPlayerShooting(long playerEntityId, boolean b) {
        controller.setPlayerShooting(playerEntityId, b);
    }

    public void calcRotationAngleOfPlayer(Point2D.Double mouseP, int entityId) {
        controller.calcRotationAngleOfPlayer(mouseP, 1);
    }

    public ShipMovementDto getShipMovementDtoOfPlayer(long entityId) {
        return controller.getShipMovementDtoOfPlayer(entityId);
    }
}
