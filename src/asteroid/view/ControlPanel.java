package asteroid.view;

import asteroid.dto.EntityParamsDto;
import helpers.complexComponent.RangeSlider;
import helpers.complexComponent.SwitchButton;

import javax.swing.*;
import java.awt.*;

public class ControlPanel extends JPanel {
    private final View view;
    private final JButton FIRE_BUTTON;
    private final JButton PLAY_BUTTON;
    private final JButton PAUSE_BUTTON;
    private final JButton RESTART_BUTTON;
    private final SwitchButton AUTO_BUTTON;
    private RangeSlider sizeSlider;
    private RangeSlider speedSlider;

    public ControlPanel(View view) {
        this.view = view;
        this.FIRE_BUTTON = new JButton("Fire Asteroid");
        this.AUTO_BUTTON = new SwitchButton();
        this.PLAY_BUTTON = new JButton("▶");
        this.PAUSE_BUTTON = new JButton("||");
        this.RESTART_BUTTON = new JButton("◯");
        setOpaque(false);

        setButtonIcons();
        buildLayout();
    }

    // ---------------------------------- LAYOUT BUILDING ----------------------------------

    private void buildLayout() {
        setLayout(new GridBagLayout());
        setBackground(view.getLightBlueColor());

        buildReproductionPanel();
        buildFirePanel();
        buildSliders();
    }

    public void setButtonIcons(){
        ImageIcon iconPlay = new ImageIcon("src/img/play.png");
        Image scaledIconPlay = iconPlay.getImage().getScaledInstance(24,24,Image.SCALE_SMOOTH);
        PLAY_BUTTON.setIcon(new ImageIcon(scaledIconPlay));

        ImageIcon iconPause = new ImageIcon("src/img/pause.png");
        Image scaledIconPause = iconPause.getImage().getScaledInstance(24,24,Image.SCALE_SMOOTH);
        PAUSE_BUTTON.setIcon(new ImageIcon(scaledIconPause));

        ImageIcon iconRestart = new ImageIcon("src/img/restart.png");
        Image scaledIconRestart = iconRestart.getImage().getScaledInstance(24,24,Image.SCALE_SMOOTH);
        RESTART_BUTTON.setIcon(new ImageIcon(scaledIconRestart));

        PLAY_BUTTON.setText("");
        PAUSE_BUTTON.setText("");
        RESTART_BUTTON.setText("");
    }

    private void buildReproductionPanel() {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6,6,6,6);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        gbc.anchor = GridBagConstraints.CENTER;

        JPanel reproductionPanel = new JPanel();
        reproductionPanel.setOpaque(false);
        reproductionPanel.setLayout(new GridLayout(1,3,2,2));
        reproductionPanel.add(PLAY_BUTTON);
        reproductionPanel.add(PAUSE_BUTTON);
        reproductionPanel.add(RESTART_BUTTON);
        add(reproductionPanel, gbc);
    }

    private void buildFirePanel() {
        JPanel firePanel = new JPanel();
        firePanel.setLayout(new GridLayout(1, 2, 2, 2));
        firePanel.setBackground(view.getLightBlueColor());
        firePanel.add(FIRE_BUTTON);
        firePanel.add(AUTO_BUTTON);
        AUTO_BUTTON.setSelected(false);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6,6,6,6);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.gridy=1;
        firePanel.setOpaque(false);
        add(firePanel, gbc);
    }

    private void buildSliders() {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6,6,6,6);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        gbc.anchor = GridBagConstraints.CENTER;

        this.sizeSlider = new RangeSlider(this, "Asteroid Size", 10,30);
        gbc.gridy = 2;
        //asteroidSizeSlider.setOpaque(false);
        add(sizeSlider, gbc);

        this.speedSlider = new RangeSlider(this, "Asteroid Speed", 0,10);
        gbc.gridy = 3;
        //asteroidSpeedSlider.setOpaque(false);
        add(speedSlider, gbc);
    }

    // ----------------------------- GETTERS & SETTERS -----------------------------

    public JButton getFIRE_BUTTON() {
        return this.FIRE_BUTTON;
    }

    public int getMinAsteroidSpeedSliderValue() {
        return speedSlider.getMinTextFieldValue();
    }

    public int getMaxAsteroidSpeedSliderValue() {
        return speedSlider.getMaxTextFieldValue();
    }

    public int getMinAsteroidSizeSliderValue() {
        return sizeSlider.getMinTextFieldValue();
    }

    public int getMaxAsteroidSizeSliderValue() {
        return sizeSlider.getMaxTextFieldValue();
    }

    public JButton getPlayButton() {
        return this.PLAY_BUTTON;
    }

    public JButton getPauseButton() {
        return this.PAUSE_BUTTON;
    }

    public JButton getRestartButton() {
        return this.RESTART_BUTTON;
    }

    public JToggleButton getAutoButton(){ return this.AUTO_BUTTON; }

    // =================================== LINKING METHODS ===================================

    public void sendNewEntityParamsToLifeGenerator() {
        view.sendNewEntityParamsToLifeGenerator(
                new EntityParamsDto(
                        sizeSlider.getMinTextFieldValue(),
                        sizeSlider.getMaxTextFieldValue(),
                        speedSlider.getMinTextFieldValue(),
                        speedSlider.getMaxTextFieldValue()
                )
        );
    }
}
