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
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Cursor;
import javax.swing.SwingConstants;
import java.util.List;

/**
 * Step 4 – Collect Data
 * Shows raw values and automatically computed 1–5 scores for each metric.
 */
public class CollectPanel extends JPanel {

    private final MainFrame mainFrame;
    private final JPanel    contentPanel = new JPanel();

    public CollectPanel(MainFrame mainFrame) {
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

        JLabel title = new JLabel("Step 4: Collect Data");
        title.setFont(new Font("SansSerif", Font.BOLD, 22));
        title.setForeground(new Color(33, 102, 172));

        JLabel sub = new JLabel("Raw data values and automatically calculated scores (1–5) per metric.");
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

        JLabel formulaNote = new JLabel(
            "<html><i>Score formula — Higher is better: 1+(v-min)/(max-min)×4 &nbsp;|&nbsp; Lower is better: 5-(v-min)/(max-min)×4 &nbsp;(rounded to nearest 0.5)</i></html>"
        );
        formulaNote.setFont(new Font("SansSerif", Font.PLAIN, 12));
        formulaNote.setForeground(new Color(120, 130, 150));
        formulaNote.setAlignmentX(Component.LEFT_ALIGNMENT);
        contentPanel.add(formulaNote);
        contentPanel.add(Box.createVerticalStrut(16));

        for (Dimension dim : scenario.getDimensions()) {
            JLabel dimLabel = new JLabel(
                dim.getName() + "   (Coefficient: " + dim.getCoefficient() + ")"
            );
            dimLabel.setFont(new Font("SansSerif", Font.BOLD, 14));
            dimLabel.setForeground(new Color(33, 102, 172));
            dimLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
            contentPanel.add(dimLabel);
            contentPanel.add(Box.createVerticalStrut(5));

            List<Metric> metrics = dim.getMetrics();
            String[] cols = {"Metric", "Direction", "Range", "Value", "Score (1–5)", "Coeff / Unit"};
            Object[][] data = buildCollectData(metrics);

            contentPanel.add(buildTableWidget(data, cols, metrics));
            contentPanel.add(Box.createVerticalStrut(16));
        }

        contentPanel.revalidate();
        contentPanel.repaint();
    }

    private Object[][] buildCollectData(List<Metric> metrics) {
        Object[][] data = new Object[metrics.size()][6];
        for (int i = 0; i < metrics.size(); i++) {
            Metric m = metrics.get(i);
            data[i][0] = m.getName();
            data[i][1] = m.getDirectionText();
            data[i][2] = m.getRangeText();
            data[i][3] = m.getValueText();
            data[i][4] = m.calculateScore();
            data[i][5] = m.getCoefficient() + " / " + m.getUnit();
        }
        return data;
    }

    private JPanel buildTableWidget(Object[][] data, String[] cols, List<Metric> metrics) {
        DefaultTableModel model = new DefaultTableModel(data, cols) {
            public boolean isCellEditable(int r, int c) { return false; }
        };

        JTable table = PlanPanel.createStyledTable(model, metrics.size());

        // Score column – colour-coded by value
        int scoreCol = 4;
        table.getColumnModel().getColumn(scoreCol).setCellRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable t, Object v,
                    boolean sel, boolean focus, int row, int col) {
                Component c = super.getTableCellRendererComponent(t, v, sel, focus, row, col);
                if (!sel) {
                    double score = (v instanceof Number) ? ((Number) v).doubleValue() : 0.0;
                    if      (score >= 4.5) c.setBackground(new Color(200, 240, 200));
                    else if (score >= 3.0) c.setBackground(new Color(255, 250, 200));
                    else                   c.setBackground(new Color(255, 220, 220));
                    c.setForeground(Color.BLACK);
                }
                ((JLabel) c).setHorizontalAlignment(SwingConstants.CENTER);
                ((JLabel) c).setFont(new Font("SansSerif", Font.BOLD, 13));
                return c;
            }
        });

        JPanel wrapper = new JPanel(new BorderLayout());
        wrapper.setBackground(Color.WHITE);
        wrapper.setAlignmentX(Component.LEFT_ALIGNMENT);
        wrapper.add(table.getTableHeader(), BorderLayout.NORTH);
        wrapper.add(table, BorderLayout.CENTER);
        int h = table.getRowHeight() * metrics.size() + 28;
        wrapper.setMaximumSize(new java.awt.Dimension(Integer.MAX_VALUE, h));
        wrapper.setPreferredSize(new java.awt.Dimension(750, h));
        return wrapper;
    }

    private JPanel buildNav() {
        JPanel p = new JPanel(new FlowLayout(FlowLayout.RIGHT, 20, 12));
        p.setBackground(new Color(245, 247, 252));
        p.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, new Color(220, 225, 235)));

        JButton back = ProfilePanel.makeButton("← Back", new Color(180, 190, 210), Color.WHITE);
        back.addActionListener(e -> mainFrame.goToStep(2));

        JButton next = ProfilePanel.makeButton("Analyse →", new Color(33, 102, 172), Color.WHITE);
        next.addActionListener(e -> mainFrame.goToStep(4));

        p.add(back);
        p.add(next);
        return p;
    }
}
