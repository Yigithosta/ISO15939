package view;

import model.Dimension;
import model.MeasurementSession;
import model.Metric;
import model.Scenario;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.FlowLayout;
import java.awt.BorderLayout;
import java.util.List;

/**
 * Step 3 – Plan Measurement (read-only)
 * Displays a table of dimensions and metrics for the selected scenario.
 */
public class PlanPanel extends JPanel {

    private final MainFrame mainFrame;
    private final JPanel    contentPanel = new JPanel();

    public PlanPanel(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);
        buildUI();
    }

    private void buildUI() {
        add(buildHeader(), BorderLayout.NORTH);

        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBackground(Color.WHITE);
        contentPanel.setBorder(new EmptyBorder(15, 25, 15, 25));

        JScrollPane scroll = new JScrollPane(contentPanel);
        scroll.setBorder(null);
        scroll.getViewport().setBackground(Color.WHITE);
        scroll.getVerticalScrollBar().setUnitIncrement(16);
        add(scroll, BorderLayout.CENTER);
        add(buildNav(), BorderLayout.SOUTH);
    }

    private JPanel buildHeader() {
        JPanel p = new JPanel(new BorderLayout());
        p.setBackground(Color.WHITE);
        p.setBorder(new EmptyBorder(20, 25, 5, 25));

        JLabel title = new JLabel("Step 3: Plan Measurement");
        title.setFont(new Font("SansSerif", Font.BOLD, 22));
        title.setForeground(new Color(33, 102, 172));

        JLabel sub = new JLabel("Review the quality dimensions and metrics for the selected scenario (read-only).");
        sub.setFont(new Font("SansSerif", Font.PLAIN, 13));
        sub.setForeground(new Color(100, 110, 130));

        p.add(title, BorderLayout.NORTH);
        p.add(sub,   BorderLayout.SOUTH);
        return p;
    }

    public void refresh(MeasurementSession session) {
        contentPanel.removeAll();

        Scenario scenario = session.getScenario();
        if (scenario == null) return;

        // Session info
        JLabel info = new JLabel(String.format(
            "Scenario: %s  |  Mode: %s  |  Type: %s Quality",
            scenario.getName(), session.getMode(), session.getQualityType()
        ));
        info.setFont(new Font("SansSerif", Font.ITALIC, 13));
        info.setForeground(new Color(100, 110, 130));
        info.setAlignmentX(Component.LEFT_ALIGNMENT);
        contentPanel.add(info);
        contentPanel.add(Box.createVerticalStrut(18));

        for (Dimension dim : scenario.getDimensions()) {
            // Dimension heading
            JLabel dimLabel = new JLabel(
                dim.getName() + "   (Coefficient: " + dim.getCoefficient() + ")"
            );
            dimLabel.setFont(new Font("SansSerif", Font.BOLD, 14));
            dimLabel.setForeground(new Color(33, 102, 172));
            dimLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
            contentPanel.add(dimLabel);
            contentPanel.add(Box.createVerticalStrut(5));

            // Metrics table
            List<Metric> metrics = dim.getMetrics();
            String[] cols   = {"Metric", "Coefficient", "Direction", "Range", "Unit"};
            Object[][] data = buildPlanData(metrics);

            contentPanel.add(buildTableWidget(data, cols, metrics.size()));
            contentPanel.add(Box.createVerticalStrut(16));
        }

        contentPanel.revalidate();
        contentPanel.repaint();
    }

    private Object[][] buildPlanData(List<Metric> metrics) {
        Object[][] data = new Object[metrics.size()][5];
        for (int i = 0; i < metrics.size(); i++) {
            Metric m  = metrics.get(i);
            data[i][0] = m.getName();
            data[i][1] = m.getCoefficient();
            data[i][2] = m.getDirectionText();
            data[i][3] = m.getRangeText();
            data[i][4] = m.getUnit();
        }
        return data;
    }

    private JPanel buildTableWidget(Object[][] data, String[] cols, int rowCount) {
        DefaultTableModel model = new DefaultTableModel(data, cols) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        JTable table = createStyledTable(model, rowCount);

        JPanel wrapper = new JPanel(new BorderLayout());
        wrapper.setBackground(Color.WHITE);
        wrapper.setAlignmentX(Component.LEFT_ALIGNMENT);
        wrapper.add(table.getTableHeader(), BorderLayout.NORTH);
        wrapper.add(table, BorderLayout.CENTER);
        int h = table.getRowHeight() * rowCount + 28;
        wrapper.setMaximumSize(new java.awt.Dimension(Integer.MAX_VALUE, h));
        wrapper.setPreferredSize(new java.awt.Dimension(700, h));
        return wrapper;
    }

    static JTable createStyledTable(DefaultTableModel model, int rowCount) {
        JTable table = new JTable(model);
        table.setFont(new Font("SansSerif", Font.PLAIN, 13));
        table.setRowHeight(28);
        table.setShowGrid(true);
        table.setGridColor(new Color(220, 225, 235));
        table.setIntercellSpacing(new java.awt.Dimension(1, 1));
        table.setSelectionBackground(new Color(200, 220, 255));

        // Header styling
        table.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 13));
        table.getTableHeader().setBackground(new Color(33, 102, 172));
        table.getTableHeader().setForeground(Color.WHITE);
        table.getTableHeader().setReorderingAllowed(false);

        // Alternating row renderer
        DefaultTableCellRenderer altRenderer = new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable t, Object v,
                    boolean sel, boolean focus, int row, int col) {
                Component c = super.getTableCellRendererComponent(t, v, sel, focus, row, col);
                if (!sel) c.setBackground(row % 2 == 0 ? Color.WHITE : new Color(245, 248, 255));
                return c;
            }
        };
        for (int i = 0; i < model.getColumnCount(); i++) {
            table.getColumnModel().getColumn(i).setCellRenderer(altRenderer);
        }
        return table;
    }

    private JPanel buildNav() {
        JPanel p = new JPanel(new FlowLayout(FlowLayout.RIGHT, 20, 12));
        p.setBackground(new Color(245, 247, 252));
        p.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, new Color(220, 225, 235)));

        JButton back = ProfilePanel.makeButton("← Back", new Color(180, 190, 210), Color.WHITE);
        back.addActionListener(e -> mainFrame.goToStep(1));

        JButton next = ProfilePanel.makeButton("Next →", new Color(33, 102, 172), Color.WHITE);
        next.addActionListener(e -> mainFrame.goToStep(3));

        p.add(back);
        p.add(next);
        return p;
    }
}
