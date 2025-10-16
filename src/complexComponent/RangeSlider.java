package complexComponent;

import javax.swing.*;

public class RangeSlider extends JPanel {
    private JSlider minSlider;
    private JSlider maxSlider;

    public RangeSlider() {
        this.minSlider = new JSlider();
        this.maxSlider = new JSlider();
    }
}
