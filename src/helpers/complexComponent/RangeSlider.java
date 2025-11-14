package helpers.complexComponent;

import javax.swing.*;
import java.awt.*;

/**
 * This class represents a complex component which mainly contains two sliders. Additionaly,
 * it has a title for the object and two TextFields for visualizing the selected number
 */

public class RangeSlider extends JPanel {
    private final JLabel titleLabel;
    private final JLabel minLabel;
    private final JLabel maxLabel;
    private final JTextField minTextField;
    private final JTextField maxTextField;
    private final JSlider minSlider;
    private final JSlider maxSlider;

    public RangeSlider(String title, int minValue, int maxValue) {
        super(new GridBagLayout());
        this.titleLabel = new JLabel(title);
        minSlider = new JSlider(minValue, maxValue);
        maxSlider = new JSlider(minValue, maxValue);
        this.minLabel = new JLabel("FROM");
        this.maxLabel = new JLabel("TO");
        this.minTextField = new JTextField();
        this.maxTextField = new JTextField();
        setMinimumSize(new Dimension(230, 100));

        // Tick configuration for the sliders
        int majorTick = Math.max(1, (maxValue - minValue) / 5); // 5 divisiones por defecto
        int minorTick = Math.max(1, majorTick / 2);
        maxSlider.setMajorTickSpacing(majorTick);
        maxSlider.setMinorTickSpacing(minorTick);
        maxSlider.setPaintTicks(true);
        maxSlider.setPaintLabels(true);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(4, 4, 4, 4);
        gbc.fill = GridBagConstraints.BOTH;

        //Title
        add(this.titleLabel, gbc);

        //Info Bar
        gbc.gridy = 1;
        gbc.gridx = 0;
        gbc.weightx = 0;
        add(minLabel, gbc);

        gbc.gridx = 1;
        gbc.weightx = 0.5;
        minTextField.setText(String.valueOf(minSlider.getValue()));
        add(minTextField, gbc);

        gbc.gridx = 2;
        gbc.weightx = 1;
        add(maxLabel, gbc);

        gbc.gridx = 3;
        gbc.weightx = 0.5;
        maxTextField.setText(String.valueOf(maxSlider.getValue()));
        add(maxTextField, gbc);

        // Min slider
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 4;
        gbc.weighty = 0.5;
        minSlider.setMinimumSize(new Dimension(170, 0));
        minSlider.addChangeListener(e -> minTextField.setText(String.valueOf(minSlider.getValue())));
        add(minSlider, gbc);

        // Max slider
        gbc.gridy = 3;
        gbc.weighty = 0.5;
        maxSlider.setMinimumSize(new Dimension(170, 0));
        maxSlider.addChangeListener(e -> maxTextField.setText(String.valueOf(maxSlider.getValue())));
        add(maxSlider, gbc);
    }
    public JSlider getMinSlider() {
        return minSlider;
    }
    public JSlider getMaxSlider() {
        return maxSlider;
    }
    public JLabel getMinLabel() {
        return minLabel;
    }
    public JLabel getMaxLabel() {
        return maxLabel;
    }
    public int getMinTextFieldValue() {
        return Integer.parseInt(this.minTextField.getText());
    }
    public int getMaxTextFieldValue() {
        return Integer.parseInt(this.maxTextField.getText());
    }

}