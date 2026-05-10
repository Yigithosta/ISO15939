package view;

import model.Dimension;
import model.MeasurementSession;
import model.Scenario;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.plaf.basic.BasicProgressBarUI;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Cursor;
import java.util.ArrayList;
import java.util.List;

/**
 * Step 5 – Analyse
 * Shows:
 *   5a. Dimension-based weighted averages (JProgressBar)
 *   5b. Radar chart (bonus, Graphics2D)
 *   5c. Gap analysis (worst dimension)
 */
public class AnalysePanel extends JPanel {

    private final MainFrame mainFrame;
    private final JPanel    contentPanel = new JPanel();

    public AnalysePanel(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);
        buildUI();
    }

    private void buildUI() {
        add(buildHeader(), BorderLayout.NORTH);

        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBackground(Color.WHITE);
        contentPanel.setBorder(new EmptyBorder(15, 30, 15, 30));

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

        JLabel title = new JLabel("Step 5: Analyse");
        title.setFont(new Font("SansSerif", Font.BOLD, 22));
        title.setForeground(new Color(33, 102, 172));

        JLabel sub = new JLabel("Weighted averages, radar chart, and gap analysis for the measurement session.");
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

        List<Dimension> dims   = scenario.getDimensions();
        List<Double>    scores = new ArrayList<>();
        for (Dimension d : dims) scores.add(d.calculateScore());

        // --- 5a: Progress bars ---
        contentPanel.add(sectionLabel("5a.  Dimension-Based Weighted Averages"));
        contentPanel.add(Box.createVerticalStrut(10));
        build5a(dims, scores);
        contentPanel.add(Box.createVerticalStrut(24));

        // --- 5b: Radar chart (Bonus) ---
        contentPanel.add(sectionLabel("5b.  Radar Chart  (Bonus)"));
        contentPanel.add(Box.createVerticalStrut(10));
        build5b(dims, scores);
        contentPanel.add(Box.createVerticalStrut(24));

        // --- 5c: Gap analysis ---
        contentPanel.add(sectionLabel("5c.  Gap Analysis"));
        contentPanel.add(Box.createVerticalStrut(10));
        build5c(dims, scores);
        contentPanel.add(Box.createVerticalStrut(20));

        contentPanel.revalidate();
        contentPanel.repaint();
    }

    // -----------------------------------------------------------------------
    // 5a
    // -----------------------------------------------------------------------
    private void build5a(List<Dimension> dims, List<Double> scores) {
        for (int i = 0; i < dims.size(); i++) {
            Dimension dim   = dims.get(i);
            double    score = scores.get(i);
            Color     color = scoreColor(score);

            JPanel row = new JPanel(new BorderLayout(12, 0));
            row.setBackground(Color.WHITE);
            row.setAlignmentX(Component.LEFT_ALIGNMENT);
            row.setMaximumSize(new java.awt.Dimension(Integer.MAX_VALUE, 36));

            JLabel name = new JLabel(dim.getName());
            name.setFont(new Font("SansSerif", Font.BOLD, 13));
            name.setPreferredSize(new java.awt.Dimension(170, 28));
            row.add(name, BorderLayout.WEST);

            JProgressBar bar = new JProgressBar(0, 100);
            bar.setValue((int)(score / 5.0 * 100));
            bar.setStringPainted(true);
            bar.setString(String.format("%.2f / 5.0", score));
            bar.setFont(new Font("SansSerif", Font.BOLD, 12));
            bar.setForeground(color);
            bar.setBackground(new Color(230, 235, 245));
            bar.setUI(new BasicProgressBarUI());   // force cross-platform rendering
            bar.setForeground(color);              // re-apply after setUI
            row.add(bar, BorderLayout.CENTER);

            contentPanel.add(row);
            contentPanel.add(Box.createVerticalStrut(8));
        }
    }

    // -----------------------------------------------------------------------
    // 5b
    // -----------------------------------------------------------------------
    private void build5b(List<Dimension> dims, List<Double> scores) {
        List<String> names = new ArrayList<>();
        for (Dimension d : dims) names.add(d.getName());

        RadarChartPanel radar = new RadarChartPanel(names, scores);
        radar.setPreferredSize(new java.awt.Dimension(420, 360));
        radar.setMaximumSize(new java.awt.Dimension(500, 400));

        JPanel wrap = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        wrap.setBackground(Color.WHITE);
        wrap.setAlignmentX(Component.LEFT_ALIGNMENT);
        wrap.setMaximumSize(new java.awt.Dimension(Integer.MAX_VALUE, 410));
        wrap.add(radar);
        contentPanel.add(wrap);
    }

    // -----------------------------------------------------------------------
    // 5c
    // -----------------------------------------------------------------------
    private void build5c(List<Dimension> dims, List<Double> scores) {
        // Identify worst dimension
        int worstIdx = 0;
        for (int i = 1; i < scores.size(); i++) {
            if (scores.get(i) < scores.get(worstIdx)) worstIdx = i;
        }
        Dimension worst = dims.get(worstIdx);
        double    score = scores.get(worstIdx);
        double    gap   = 5.0 - score;

        String qualityLevel;
        if      (score >= 4.5) qualityLevel = "Excellent";
        else if (score >= 3.5) qualityLevel = "Good";
        else if (score >= 2.5) qualityLevel = "Needs Improvement";
        else                   qualityLevel = "Poor";

        Color levelColor = scoreColor(score);

        JPanel box = new JPanel();
        box.setLayout(new BoxLayout(box, BoxLayout.Y_AXIS));
        box.setBackground(new Color(255, 249, 235));
        box.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(240, 180, 60), 1),
            new EmptyBorder(16, 22, 16, 22)
        ));
        box.setAlignmentX(Component.LEFT_ALIGNMENT);
        box.setMaximumSize(new java.awt.Dimension(Integer.MAX_VALUE, 200));

        addGapRow(box, "Dimension:",     worst.getName(),                      Color.BLACK);
        addGapRow(box, "Score:",         String.format("%.2f / 5.0", score),  levelColor);
        addGapRow(box, "Gap to 5.0:",    String.format("%.2f", gap),           new Color(180, 40, 40));
        addGapRow(box, "Quality Level:", qualityLevel,                          levelColor);
        box.add(Box.createVerticalStrut(10));

        JLabel msg = new JLabel(
            "<html><i>This dimension has the lowest score and requires the most improvement.</i></html>"
        );
        msg.setFont(new Font("SansSerif", Font.ITALIC, 13));
        msg.setForeground(new Color(120, 90, 0));
        msg.setAlignmentX(Component.LEFT_ALIGNMENT);
        box.add(msg);

        contentPanel.add(box);
    }

    private void addGapRow(JPanel parent, String label, String value, Color valueColor) {
        JPanel row = new JPanel(new FlowLayout(FlowLayout.LEFT, 6, 2));
        row.setBackground(new Color(255, 249, 235));
        row.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel lbl = new JLabel(label);
        lbl.setFont(new Font("SansSerif", Font.BOLD, 13));
        lbl.setPreferredSize(new java.awt.Dimension(130, 22));

        JLabel val = new JLabel(value);
        val.setFont(new Font("SansSerif", Font.BOLD, 14));
        val.setForeground(valueColor);

        row.add(lbl);
        row.add(val);
        parent.add(row);
    }

    // -----------------------------------------------------------------------
    // Utility
    // -----------------------------------------------------------------------
    private JLabel sectionLabel(String text) {
        JLabel l = new JLabel(text);
        l.setFont(new Font("SansSerif", Font.BOLD, 15));
        l.setForeground(new Color(33, 102, 172));
        l.setAlignmentX(Component.LEFT_ALIGNMENT);
        return l;
    }

    private Color scoreColor(double score) {
        if      (score >= 4.5) return new Color(56, 161, 68);
        else if (score >= 3.5) return new Color(33, 120, 210);
        else if (score >= 2.5) return new Color(220, 130, 0);
        else                   return new Color(200, 50, 50);
    }

    private JPanel buildNav() {
        JPanel p = new JPanel(new FlowLayout(FlowLayout.RIGHT, 20, 12));
        p.setBackground(new Color(245, 247, 252));
        p.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, new Color(220, 225, 235)));

        JButton back = ProfilePanel.makeButton("← Back", new Color(180, 190, 210), Color.WHITE);
        back.addActionListener(e -> mainFrame.goToStep(3));

        JButton newSession = ProfilePanel.makeButton("New Session", new Color(76, 175, 80), Color.WHITE);
        newSession.addActionListener(e -> mainFrame.resetSession());

        p.add(back);
        p.add(newSession);
        return p;
    }
}
