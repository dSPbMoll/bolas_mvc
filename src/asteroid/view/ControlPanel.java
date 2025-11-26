package asteroid.view;

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
    private RangeSlider asteroidSizeSlider;
    private RangeSlider asteroidSpeedSlider;

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

        this.asteroidSizeSlider = new RangeSlider("Asteroid Size", 10,30);
        gbc.gridy = 2;
        //asteroidSizeSlider.setOpaque(false);
        add(asteroidSizeSlider, gbc);

        this.asteroidSpeedSlider = new RangeSlider("Asteroid Speed", 0,10);
        gbc.gridy = 3;
        //asteroidSpeedSlider.setOpaque(false);
        add(asteroidSpeedSlider, gbc);
    }

    // ----------------------------- GETTERS & SETTERS -----------------------------

    public JButton getFIRE_BUTTON() {
        return this.FIRE_BUTTON;
    }

    public int getMinAsteroidSpeedSliderValue() {
        return asteroidSpeedSlider.getMinTextFieldValue();
    }

    public int getMaxAsteroidSpeedSliderValue() {
        return asteroidSpeedSlider.getMaxTextFieldValue();
    }

    public int getMinAsteroidSizeSliderValue() {
        return asteroidSizeSlider.getMinTextFieldValue();
    }

    public int getMaxAsteroidSizeSliderValue() {
        return asteroidSizeSlider.getMaxTextFieldValue();
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
}
