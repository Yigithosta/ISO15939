package view;

import model.Scenario;
import model.ScenarioRepository;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.List;

/**
 * Step 2 – Define Quality Dimensions
 * Three-stage selection: quality type → mode → scenario.
 * All selections use JRadioButton (enforces mutual exclusivity).
 */
public class DefinePanel extends JPanel {

    private final MainFrame mainFrame;

    // 2a: Quality Type
    private final ButtonGroup typeGroup   = new ButtonGroup();
    private final JRadioButton productBtn = new JRadioButton("Product Quality");
    private final JRadioButton processBtn = new JRadioButton("Process Quality");

    // 2b: Mode
    private final ButtonGroup modeGroup     = new ButtonGroup();
    private final JRadioButton healthBtn    = new JRadioButton("Health");
    private final JRadioButton educationBtn = new JRadioButton("Education");

    // 2c: Scenario (rebuilt dynamically)
    private ButtonGroup     scenarioGroup;
    private JRadioButton[]  scenarioButtons = new JRadioButton[0];
    private final JPanel    scenarioPanel   = new JPanel();

    public DefinePanel(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);
        buildUI();
    }

    private void buildUI() {
        add(buildHeader(), BorderLayout.NORTH);

        JPanel content = new JPanel();
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        content.setBackground(Color.WHITE);
        content.setBorder(new EmptyBorder(15, 30, 15, 30));

        // --- 2a ---
        content.add(sectionLabel("2a.  Quality Type"));
        content.add(Box.createVerticalStrut(6));
        content.add(buildTypePanel());
        content.add(Box.createVerticalStrut(18));

        // --- 2b ---
        content.add(sectionLabel("2b.  Mode Selection"));
        content.add(Box.createVerticalStrut(6));
        content.add(buildModePanel());
        content.add(Box.createVerticalStrut(18));

        // --- 2c ---
        content.add(sectionLabel("2c.  Scenario Selection"));
        content.add(Box.createVerticalStrut(6));
        scenarioPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 20, 4));
        scenarioPanel.setBackground(Color.WHITE);
        scenarioPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        content.add(scenarioPanel);

        JScrollPane scroll = new JScrollPane(content);
        scroll.setBorder(null);
        scroll.getViewport().setBackground(Color.WHITE);
        add(scroll, BorderLayout.CENTER);
        add(buildNav(), BorderLayout.SOUTH);

        updateScenarioList();
    }

    private JPanel buildHeader() {
        JPanel p = new JPanel(new BorderLayout());
        p.setBackground(Color.WHITE);
        p.setBorder(new EmptyBorder(20, 25, 5, 25));

        JLabel title = new JLabel("Step 2: Define Quality Dimensions");
        title.setFont(new Font("SansSerif", Font.BOLD, 22));
        title.setForeground(new Color(33, 102, 172));

        JLabel sub = new JLabel("Select the scope and context of the measurement session.");
        sub.setFont(new Font("SansSerif", Font.PLAIN, 13));
        sub.setForeground(new Color(100, 110, 130));

        p.add(title, BorderLayout.NORTH);
        p.add(sub,   BorderLayout.SOUTH);
        return p;
    }

    private JPanel buildTypePanel() {
        style(productBtn, "Software product characteristics: performance, security, usability, reliability");
        style(processBtn, "Development process characteristics: sprint efficiency, code quality, team collaboration");

        typeGroup.add(productBtn);
        typeGroup.add(processBtn);
        productBtn.setSelected(true);

        JPanel p = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 4));
        p.setBackground(Color.WHITE);
        p.setAlignmentX(Component.LEFT_ALIGNMENT);
        p.add(productBtn);
        p.add(processBtn);
        return p;
    }

    private JPanel buildModePanel() {
        style(healthBtn,    "Health management system scenarios (ready-made dataset)");
        style(educationBtn, "Education LMS system scenarios (ready-made dataset)");

        modeGroup.add(healthBtn);
        modeGroup.add(educationBtn);
        healthBtn.setSelected(true);

        ActionListener modeChange = e -> updateScenarioList();
        healthBtn.addActionListener(modeChange);
        educationBtn.addActionListener(modeChange);

        JPanel p = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 4));
        p.setBackground(Color.WHITE);
        p.setAlignmentX(Component.LEFT_ALIGNMENT);
        p.add(healthBtn);
        p.add(educationBtn);
        return p;
    }

    private void updateScenarioList() {
        String mode = educationBtn.isSelected() ? "Education" : "Health";
        List<String> names = ScenarioRepository.getScenarioNames(mode);

        scenarioPanel.removeAll();
        scenarioGroup   = new ButtonGroup();
        scenarioButtons = new JRadioButton[names.size()];

        for (int i = 0; i < names.size(); i++) {
            JRadioButton btn = new JRadioButton(names.get(i));
            style(btn, "Select this scenario for measurement");
            if (i == 0) btn.setSelected(true);
            scenarioGroup.add(btn);
            scenarioButtons[i] = btn;
            scenarioPanel.add(btn);
        }
        scenarioPanel.revalidate();
        scenarioPanel.repaint();
    }

    private JPanel buildNav() {
        JPanel p = new JPanel(new FlowLayout(FlowLayout.RIGHT, 20, 12));
        p.setBackground(new Color(245, 247, 252));
        p.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, new Color(220, 225, 235)));

        JButton back = ProfilePanel.makeButton("← Back", new Color(180, 190, 210), Color.WHITE);
        back.addActionListener(e -> mainFrame.goToStep(0));

        JButton next = ProfilePanel.makeButton("Next →", new Color(33, 102, 172), Color.WHITE);
        next.addActionListener(e -> onNext());

        p.add(back);
        p.add(next);
        return p;
    }

    private void onNext() {
        String selectedScenario = null;
        for (JRadioButton btn : scenarioButtons) {
            if (btn.isSelected()) { selectedScenario = btn.getText(); break; }
        }
        if (selectedScenario == null) {
            JOptionPane.showMessageDialog(this,
                "Please select a scenario before proceeding.",
                "Missing Selection", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String qualityType = productBtn.isSelected() ? "Product" : "Process";
        String mode        = educationBtn.isSelected() ? "Education" : "Health";
        Scenario scenario  = ScenarioRepository.findScenario(mode, selectedScenario);

        mainFrame.getSession().setQualityType(qualityType);
        mainFrame.getSession().setMode(mode);
        mainFrame.getSession().setScenario(scenario);
        mainFrame.goToStep(2);
    }

    // -----------------------------------------------------------------------
    // Utility
    // -----------------------------------------------------------------------
    private JLabel sectionLabel(String text) {
        JLabel l = new JLabel(text);
        l.setFont(new Font("SansSerif", Font.BOLD, 14));
        l.setForeground(new Color(33, 102, 172));
        l.setAlignmentX(Component.LEFT_ALIGNMENT);
        return l;
    }

    private void style(JRadioButton btn, String tooltip) {
        btn.setFont(new Font("SansSerif", Font.PLAIN, 13));
        btn.setBackground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setToolTipText(tooltip);
    }
}
