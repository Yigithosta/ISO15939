package view;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

/**
 * Top panel showing wizard progress: step circles + names with completion marks.
 */
public class StepIndicatorPanel extends JPanel {

    private static final Color COLOR_ACTIVE    = new Color(33, 102, 172);
    private static final Color COLOR_DONE      = new Color(76, 175, 80);
    private static final Color COLOR_INACTIVE  = new Color(200, 210, 230);
    private static final Color COLOR_TEXT_INACTIVE = new Color(150, 160, 180);

    private final String[] stepNames;
    private int     currentStep = 0;
    private boolean[] completed;

    public StepIndicatorPanel(String[] stepNames) {
        this.stepNames = stepNames;
        this.completed = new boolean[stepNames.length];
        setBackground(new Color(240, 245, 255));
        setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, new Color(200, 210, 230)));
        setPreferredSize(new Dimension(0, 60));
        setLayout(new FlowLayout(FlowLayout.CENTER, 8, 15));
        rebuild();
    }

    /** Update the indicator. Steps before currentStep that are flagged in completed[] show check marks. */
    public void update(int current, boolean[] completedFlags) {
        this.currentStep = current;
        this.completed   = completedFlags.clone();
        rebuild();
    }

    private void rebuild() {
        removeAll();
        for (int i = 0; i < stepNames.length; i++) {
            if (i > 0) {
                JLabel arrow = new JLabel("→");
                arrow.setForeground(COLOR_TEXT_INACTIVE);
                arrow.setFont(new Font("SansSerif", Font.PLAIN, 14));
                add(arrow);
            }
            add(buildStepWidget(i));
        }
        revalidate();
        repaint();
    }

    private JPanel buildStepWidget(int i) {
        JPanel p = new JPanel(new FlowLayout(FlowLayout.CENTER, 4, 0));
        p.setOpaque(false);

        // Circle badge
        JLabel badge = new JLabel();
        badge.setPreferredSize(new Dimension(26, 26));
        badge.setHorizontalAlignment(SwingConstants.CENTER);
        badge.setFont(new Font("SansSerif", Font.BOLD, 11));
        badge.setOpaque(true);

        // Name label
        JLabel name = new JLabel((i + 1) + ". " + stepNames[i]);

        if (completed[i] && i != currentStep) {
            badge.setText("✓");
            badge.setBackground(COLOR_DONE);
            badge.setForeground(Color.WHITE);
            name.setFont(new Font("SansSerif", Font.PLAIN, 13));
            name.setForeground(COLOR_DONE);
        } else if (i == currentStep) {
            badge.setText(String.valueOf(i + 1));
            badge.setBackground(COLOR_ACTIVE);
            badge.setForeground(Color.WHITE);
            name.setFont(new Font("SansSerif", Font.BOLD, 13));
            name.setForeground(COLOR_ACTIVE);
        } else {
            badge.setText(String.valueOf(i + 1));
            badge.setBackground(COLOR_INACTIVE);
            badge.setForeground(new Color(100, 110, 130));
            name.setFont(new Font("SansSerif", Font.PLAIN, 13));
            name.setForeground(COLOR_TEXT_INACTIVE);
        }

        badge.setBorder(BorderFactory.createLineBorder(badge.getBackground().darker(), 1));
        p.add(badge);
        p.add(name);
        return p;
    }
}
