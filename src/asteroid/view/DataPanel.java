package asteroid.view;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;
import java.awt.*;

public class DataPanel extends JPanel {
    private View view;
    private JTable table;
    private DefaultTableModel tableModel;

    public DataPanel(View view) {
        this.view = view;
        buildLayout();
    }

    // --------------------------------- LAYOUT BUILDING ---------------------------------

    private void buildLayout() {
        setLayout(new BorderLayout());

        String[] columnNames = {"titulo", "Valor"};
        Object[][] data = {{"FPS:", "0"}, {"Render Time:", "0"}, {"Asteroid Count:", "0"}};

        tableModel = new DefaultTableModel(data, columnNames);
        table = new JTable(tableModel);


        DefaultTableCellRenderer renderer = new DefaultTableCellRenderer();
        renderer.setOpaque(false);
        table.getColumnModel().getColumn(0).setCellRenderer(renderer);

        TableColumnModel columnModel = table.getColumnModel();
        columnModel.getColumn(0).setPreferredWidth(100);
        columnModel.getColumn(1).setPreferredWidth(50);

        DefaultTableCellRenderer rightRenderer = new DefaultTableCellRenderer();
        rightRenderer.setOpaque(false);
        rightRenderer.setHorizontalAlignment(SwingConstants.RIGHT);
        columnModel.getColumn(1).setCellRenderer(rightRenderer);


        table.setForeground(Color.white);
        table.setOpaque(false);
        table.setShowGrid(false);
        //table.setBackground(view.getLightBlueColor());

        add(table);
    }

    // ---------------------------------- TABLE INFO UPDATING ----------------------------------

    public void updateFps(int fps) {
        tableModel.setValueAt(fps, 0, 1);
    }

    public void updateRenderTime(double renderTime) {
        tableModel.setValueAt(renderTime + " ms", 1, 1);
    }

    public void updateAsteroidCount(int asteroidCount) {
        tableModel.setValueAt(asteroidCount, 2, 1);
    }


}
