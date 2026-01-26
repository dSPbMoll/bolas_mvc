package helpers.complexComponent;

import asteroid.view.ControlPanel;

import javax.swing.*;
import java.awt.*;

/**
 * Complex component which mainly contains two sliders. Additionaly,
 * it has a title for the object and two TextFields for visualizing the selected number
 */

public class RangeSlider extends JPanel {
    private final ControlPanel parent;
    private final JLabel titleLabel;
    private final JLabel minLabel;
    private final JLabel maxLabel;
    private final int MIN_VALUE_OF_SLIDERS;
    private final int MAX_VALUE_OF_SLIDERS;
    private final JTextField minTextField;
    private final JTextField maxTextField;
    private final JSlider minSlider;
    private final JSlider maxSlider;
    private boolean updating;

    public RangeSlider(ControlPanel parent, String title, int minValue, int maxValue) {
        super(new GridBagLayout());
        this.parent = parent;
        this.titleLabel = new JLabel(title);
        this.MIN_VALUE_OF_SLIDERS = minValue;
        this.MAX_VALUE_OF_SLIDERS = maxValue;

        this.minLabel = new JLabel("FROM");
        this.maxLabel = new JLabel("TO");
        this.minTextField = new JTextField();
        this.maxTextField = new JTextField();
        this.minSlider = new JSlider(minValue, maxValue);
        this.maxSlider = new JSlider(minValue, maxValue);
        this.updating = false;
        setMinimumSize(new Dimension(230, 100));

        buildLayout();
        setSlidersConfiguration();
        addListeners();
    }

    // =============================== GETTERS & SETTERS =============================

    public int getMinTextFieldValue() {
        return Integer.parseInt(this.minTextField.getText());
    }

    public int getMaxTextFieldValue() {
        return Integer.parseInt(this.maxTextField.getText());
    }

    // ================================ LAYOUT BUILDING ================================

    private void buildLayout() {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(4, 4, 4, 4);
        gbc.fill = GridBagConstraints.BOTH;

        buildInfoBar();
        buildSliders();
    }

    private void buildInfoBar() {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(4, 4, 4, 4);
        gbc.fill = GridBagConstraints.BOTH;

        //Info Bar
        add(this.titleLabel, gbc);

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
    }

    private void buildSliders() {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(4, 4, 4, 4);
        gbc.fill = GridBagConstraints.BOTH;

        // Min slider
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 4;
        gbc.weightx = 0.5;
        gbc.weighty = 0.5;
        minSlider.setMinimumSize(new Dimension(170, 0));
        add(minSlider, gbc);

        // Max slider
        gbc.gridy = 3;
        gbc.weighty = 0.5;
        maxSlider.setMinimumSize(new Dimension(170, 0));
        add(maxSlider, gbc);
    }

    private void setSlidersConfiguration() {
        // Tick configuration for the sliders
        int majorTick = Math.max(1, (MAX_VALUE_OF_SLIDERS - MIN_VALUE_OF_SLIDERS) / 5); // 5 divisions by default
        int minorTick = Math.max(1, majorTick / 2);

        setSliderConfiguration(minSlider, minorTick, majorTick);
        setSliderConfiguration(maxSlider, minorTick, majorTick);
    }

    private void setSliderConfiguration(JSlider slider, int minorTick, int majorTick) {
        slider.setMajorTickSpacing(majorTick);
        slider.setMinorTickSpacing(minorTick);
        slider.setPaintTicks(true);
        slider.setPaintLabels(true);
    }

    // =============================== LISTENERS =============================

    private void addListeners() {
        addSliderListeners();
        addTextFieldListeners();
    }

    private void addSliderListeners() {
        addSliderListener(minSlider, minTextField);
        addSliderListener(maxSlider, maxTextField);
    }

    private void addSliderListener(JSlider slider, JTextField textField) {
        slider.addChangeListener(e -> {
            if (updating) return;

            int sliderValue = slider.getValue();
            updating = true;
            textField.setText(String.valueOf(sliderValue));
            updating = false;
            parent.sendNewEntityParamsToLifeGenerator();
        });
    }

    private void addTextFieldListeners() {
        addTextFieldListener(minTextField, minSlider);
        addTextFieldListener(maxTextField, maxSlider);
    }

    private void addTextFieldListener(JTextField textField, JSlider slider) {
        textField.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            @Override
            public void insertUpdate(javax.swing.event.DocumentEvent e) {
                contentChanged(textField, slider);
            }

            @Override
            public void removeUpdate(javax.swing.event.DocumentEvent e) {
                contentChanged(textField, slider);
            }

            @Override
            public void changedUpdate(javax.swing.event.DocumentEvent e) {
            }

            private void contentChanged(JTextField TF, JSlider slider) {
                if (updating) return;
                try {
                    if (TF.getText().isEmpty()) return;
                    int newContent = Integer.parseInt(TF.getText());
                    if (newContent >= 0 && newContent <= 255 ) {

                        updating = true;
                        slider.setValue(newContent);
                        updating = false;
                        parent.sendNewEntityParamsToLifeGenerator();
                    }
                } catch (Exception e) {
                    System.err.println(e);
                }

            }
        });
    }
}