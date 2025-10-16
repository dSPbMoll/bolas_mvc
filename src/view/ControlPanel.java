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

    public ControlPanel(View view) {
        this.view = view;

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10,10,10,10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weighty = 1.0;
        gbc.gridx = 0;
        gbc.anchor = GridBagConstraints.CENTER;
        setBackground(view.getLightBlueColor());

        // Reproduction Panel
        JPanel reproductionPanel = new JPanel();
        this.PLAY_BUTTON = new JButton("▶");

        gbc.gridy = 0;
        add(PLAY_BUTTON, gbc);

        this.PAUSE_BUTTON = new JButton("||");
        gbc.gridy = 1;
        add(PAUSE_BUTTON, gbc);

        this.RESTART_BUTTON = new JButton("◯");
        gbc.gridy = 2;
        add(RESTART_BUTTON, gbc);

        this.FIRE_BUTTON = new JButton("Disparar Bola");
        gbc.gridx = 1;
        gbc.gridy = 0;
        add(FIRE_BUTTON, gbc);

        this.ballSizeSlider = new RangeSlider(1,5);
        gbc.gridx = 2;
        add(ballSizeSlider, gbc);

        setPreferredSize(new Dimension(150, 100));
    }
    public JButton getFIRE_BUTTON() {
        return this.FIRE_BUTTON;
    }
}
