package view;

import javax.swing.*;
import java.awt.*;
import java.util.List;

/**
 * Bonus – Radar (spider) chart drawn with Java 2D Graphics.
 * Renders one polygon per set of dimension scores on a 5-level grid.
 */
public class RadarChartPanel extends JPanel {

    private static final double MAX_VALUE   = 5.0;
    private static final Color  FILL_COLOR  = new Color(33, 102, 172, 55);
    private static final Color  LINE_COLOR  = new Color(33, 102, 172);
    private static final Color  GRID_COLOR  = new Color(210, 218, 235);
    private static final Color  LABEL_COLOR = new Color(40, 55, 80);
    private static final Color  SCORE_COLOR = new Color(33, 102, 172);

    private final List<String> labels;
    private final List<Double> values;

    public RadarChartPanel(List<String> labels, List<Double> values) {
        this.labels = labels;
        this.values = values;
        setBackground(Color.WHITE);
        setOpaque(true);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        int n = labels.size();
        if (n < 3) return;

        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,  RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        int w  = getWidth();
        int h  = getHeight();
        int cx = w / 2;
        int cy = h / 2;
        int radius = Math.min(w, h) / 2 - 70;

        drawGrid(g2, n, cx, cy, radius);
        drawAxes(g2, n, cx, cy, radius);
        drawDataPolygon(g2, n, cx, cy, radius);
        drawLabels(g2, n, cx, cy, radius);

        g2.dispose();
    }

    /** Concentric grid polygons (1 per score level). */
    private void drawGrid(Graphics2D g2, int n, int cx, int cy, int radius) {
        g2.setColor(GRID_COLOR);
        g2.setStroke(new BasicStroke(0.8f));

        for (int level = 1; level <= 5; level++) {
            double r = radius * level / 5.0;
            int[] xs = new int[n], ys = new int[n];
            for (int i = 0; i < n; i++) {
                double angle = angle(i, n);
                xs[i] = cx + (int)(r * Math.cos(angle));
                ys[i] = cy - (int)(r * Math.sin(angle));
            }
            g2.drawPolygon(xs, ys, n);

            // Level number
            g2.setColor(new Color(160, 170, 185));
            g2.setFont(new Font("SansSerif", Font.PLAIN, 10));
            g2.drawString(String.valueOf(level), cx + 3, cy - (int) r + 12);
            g2.setColor(GRID_COLOR);
        }
    }

    /** Lines from center to each vertex. */
    private void drawAxes(Graphics2D g2, int n, int cx, int cy, int radius) {
        g2.setColor(new Color(190, 200, 220));
        g2.setStroke(new BasicStroke(1.0f));
        for (int i = 0; i < n; i++) {
            double angle = angle(i, n);
            int ex = cx + (int)(radius * Math.cos(angle));
            int ey = cy - (int)(radius * Math.sin(angle));
            g2.drawLine(cx, cy, ex, ey);
        }
    }

    /** Filled polygon for the actual dimension scores. */
    private void drawDataPolygon(Graphics2D g2, int n, int cx, int cy, int radius) {
        int[] xs = new int[n], ys = new int[n];
        for (int i = 0; i < n; i++) {
            double r     = radius * values.get(i) / MAX_VALUE;
            double angle = angle(i, n);
            xs[i] = cx + (int)(r * Math.cos(angle));
            ys[i] = cy - (int)(r * Math.sin(angle));
        }

        g2.setColor(FILL_COLOR);
        g2.fillPolygon(xs, ys, n);

        g2.setColor(LINE_COLOR);
        g2.setStroke(new BasicStroke(2.2f));
        g2.drawPolygon(xs, ys, n);

        // Vertex dots
        g2.setColor(LINE_COLOR);
        for (int i = 0; i < n; i++) {
            g2.fillOval(xs[i] - 5, ys[i] - 5, 10, 10);
        }
    }

    /** Dimension names and scores around the perimeter. */
    private void drawLabels(Graphics2D g2, int n, int cx, int cy, int radius) {
        int labelRadius = radius + 38;
        for (int i = 0; i < n; i++) {
            double angle = angle(i, n);
            int lx = cx + (int)(labelRadius * Math.cos(angle));
            int ly = cy - (int)(labelRadius * Math.sin(angle));

            String label = labels.get(i);
            String score = String.format("(%.1f)", values.get(i));

            g2.setFont(new Font("SansSerif", Font.BOLD, 12));
            g2.setColor(LABEL_COLOR);
            FontMetrics fm = g2.getFontMetrics();
            g2.drawString(label, lx - fm.stringWidth(label) / 2,
                                 ly - fm.getDescent());

            g2.setFont(new Font("SansSerif", Font.PLAIN, 11));
            g2.setColor(SCORE_COLOR);
            FontMetrics fm2 = g2.getFontMetrics();
            g2.drawString(score, lx - fm2.stringWidth(score) / 2,
                                 ly + fm2.getAscent() - fm2.getDescent() + 2);
        }
    }

    /** Angle for dimension i: starts at top (PI/2), goes clockwise. */
    private static double angle(int i, int n) {
        return Math.PI / 2.0 + 2.0 * Math.PI * i / n;
    }
}
