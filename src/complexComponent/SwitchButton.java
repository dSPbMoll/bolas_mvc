package complexComponent;

import javax.swing.*;
import java.awt.*;

public class SwitchButton extends JToggleButton {
    public SwitchButton(){
        setPreferredSize(new Dimension(50,25));
        setContentAreaFilled(false);
        setFocusPainted(false);
        setBorderPainted(false);
        setMargin(new Insets(0,0,0,0));
    }

    @Override
    protected void paintComponent(Graphics g){
        Graphics2D g2=(Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);

        g2.setColor(isSelected()? Color.GREEN: Color.LIGHT_GRAY);
        g2.fillRoundRect(0,0,getWidth(),getHeight(), getHeight(), getHeight());

        int diameter=getHeight()-4;
        int x = isSelected()? getWidth()-diameter-2:2;
        g2.setColor(Color.WHITE);
        g2.fillOval(x,2,diameter,diameter);

        g2.setColor(Color.BLACK);
        g2.setFont(new Font("Arial", Font.BOLD, 12));
        FontMetrics fm = g2.getFontMetrics();
        String text = "AUTO";
        int textX = (getWidth() - fm.stringWidth(text)) / 2;
        int textY = (getHeight() + fm.getAscent() - fm.getDescent()) / 2;
        g2.drawString(text, textX, textY);

        g2.dispose();
    }
}
