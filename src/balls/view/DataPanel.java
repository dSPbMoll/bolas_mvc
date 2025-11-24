package balls.view;

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
        Object[][] data = {{"FPS:", "0"}, {"Render Time:", "0"}, {"Ball Count:", "0"}};

        tableModel = new DefaultTableModel(data, columnNames);
        table = new JTable(tableModel);

        TableColumnModel columnModel = table.getColumnModel();
        columnModel.getColumn(0).setPreferredWidth(100);
        columnModel.getColumn(1).setPreferredWidth(50);

        DefaultTableCellRenderer rightRenderer = new DefaultTableCellRenderer();
        rightRenderer.setHorizontalAlignment(SwingConstants.RIGHT);
        columnModel.getColumn(1).setCellRenderer(rightRenderer);

        table.setShowGrid(false);
        table.setBackground(view.getLightBlueColor());

        add(table);
    }

    // ---------------------------------- TABLE INFO UPDATING ----------------------------------

    public void updateFps(int fps) {
        tableModel.setValueAt(fps, 0, 1);
    }

    public void updateRenderTime(double renderTime) {
        tableModel.setValueAt(renderTime + " ms", 1, 1);
    }

    public void updateBallCount(int ballCount) {
        tableModel.setValueAt(ballCount, 2, 1);
    }


}
