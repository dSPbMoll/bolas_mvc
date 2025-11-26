package balls.view;

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
    private RangeSlider ballSizeSlider;
    private RangeSlider ballSpeedSlider;
    private Image backgoundImage;

    public ControlPanel(View view) {
        this.view = view;
        this.FIRE_BUTTON = new JButton("Disparar Bola");
        this.AUTO_BUTTON = new SwitchButton();
        this.PLAY_BUTTON = new JButton("▶");
        this.PAUSE_BUTTON = new JButton("||");
        this.RESTART_BUTTON = new JButton("◯");
        setOpaque(false);

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

        this.ballSizeSlider = new RangeSlider("Ball Size", 10,30);
        gbc.gridy = 2;
        //ballSizeSlider.setOpaque(false);
        add(ballSizeSlider, gbc);

        this.ballSpeedSlider = new RangeSlider("Ball Speed", 0,10);
        gbc.gridy = 3;
        //ballSpeedSlider.setOpaque(false);
        add(ballSpeedSlider, gbc);
    }

    // ----------------------------- GETTERS & SETTERS -----------------------------

    public JButton getFIRE_BUTTON() {
        return this.FIRE_BUTTON;
    }

    public int getMinBallSpeedSliderValue() {
        return ballSpeedSlider.getMinTextFieldValue();
    }

    public int getMaxBallSpeedSliderValue() {
        return ballSpeedSlider.getMaxTextFieldValue();
    }

    public int getMinBallSizeSliderValue() {
        return ballSizeSlider.getMinTextFieldValue();
    }

    public int getMaxBallSizeSliderValue() {
        return ballSizeSlider.getMaxTextFieldValue();
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
