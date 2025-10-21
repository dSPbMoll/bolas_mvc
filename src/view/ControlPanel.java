package view;

import complexComponent.RangeSlider;

import javax.swing.*;
import java.awt.*;

public class ControlPanel extends JPanel {
    private final View view;
    private final JButton FIRE_BUTTON;
    private final JButton PLAY_BUTTON;
    private final JButton PAUSE_BUTTON;
    private final JButton RESTART_BUTTON;
    private RangeSlider ballSizeSlider;
    private RangeSlider ballSpeedSlider;

    public ControlPanel(View view) {
        this.view = view;

        setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6,6,6,6);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.CENTER;
        setBackground(view.getLightBlueColor());

        // Reproduction Panel
        JPanel reproductionPanel = new JPanel();
        reproductionPanel.setLayout(new GridLayout(1,3,2,2));
        this.PLAY_BUTTON = new JButton("▶");
        this.PAUSE_BUTTON = new JButton("||");
        this.RESTART_BUTTON = new JButton("◯");
        reproductionPanel.add(PLAY_BUTTON);
        reproductionPanel.add(PAUSE_BUTTON);
        reproductionPanel.add(RESTART_BUTTON);
        add(reproductionPanel, gbc);

        this.FIRE_BUTTON = new JButton("Disparar Bola");
        gbc.gridy = 1;
        add(FIRE_BUTTON, gbc);

        this.ballSizeSlider = new RangeSlider("Ball Size", 10,30);
        gbc.gridy = 2;
        add(ballSizeSlider, gbc);

        this.ballSpeedSlider = new RangeSlider("Ball Speed", 0,10);
        gbc.gridy = 3;
        add(ballSpeedSlider, gbc);
    }
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
}
