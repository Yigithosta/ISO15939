package view;

import model.UserProfile;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

/**
 * Step 1 – Profile
 * Collects username, school, and session name with validation.
 */
public class ProfilePanel extends JPanel {

    private final MainFrame    mainFrame;
    private final JTextField   usernameField    = new JTextField(25);
    private final JTextField   schoolField      = new JTextField(25);
    private final JTextField   sessionNameField = new JTextField(25);

    public ProfilePanel(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);
        buildUI();
    }

    private void buildUI() {
        add(buildHeader(), BorderLayout.NORTH);
        add(buildForm(),   BorderLayout.CENTER);
        add(buildNav(),    BorderLayout.SOUTH);
    }

    private JPanel buildHeader() {
        JPanel p = new JPanel(new BorderLayout());
        p.setBackground(Color.WHITE);
        p.setBorder(new EmptyBorder(20, 25, 10, 25));

        JLabel title = new JLabel("Step 1: Profile");
        title.setFont(new Font("SansSerif", Font.BOLD, 22));
        title.setForeground(new Color(33, 102, 172));

        JLabel sub = new JLabel("Enter your user and session information to begin.");
        sub.setFont(new Font("SansSerif", Font.PLAIN, 13));
        sub.setForeground(new Color(100, 110, 130));

        p.add(title, BorderLayout.NORTH);
        p.add(sub,   BorderLayout.SOUTH);
        return p;
    }

    private JPanel buildForm() {
        JPanel wrap = new JPanel(new GridBagLayout());
        wrap.setBackground(Color.WHITE);

        JPanel form = new JPanel(new GridBagLayout());
        form.setBackground(Color.WHITE);
        form.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(220, 228, 245), 1),
            new EmptyBorder(30, 40, 30, 40)
        ));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 8, 10, 8);
        gbc.fill   = GridBagConstraints.HORIZONTAL;

        addRow(form, gbc, 0, "Username:",     usernameField,    "Your name or student ID");
        addRow(form, gbc, 1, "School:",        schoolField,      "Your university / institution");
        addRow(form, gbc, 2, "Session Name:",  sessionNameField, "A descriptive name for this session");

        wrap.add(form);
        return wrap;
    }

    private void addRow(JPanel form, GridBagConstraints gbc, int row, String labelText, JTextField field, String placeholder) {
        gbc.gridx = 0; gbc.gridy = row; gbc.weightx = 0;
        JLabel lbl = new JLabel(labelText);
        lbl.setFont(new Font("SansSerif", Font.BOLD, 14));
        lbl.setPreferredSize(new Dimension(140, 30));
        form.add(lbl, gbc);

        gbc.gridx = 1; gbc.weightx = 1;
        field.setFont(new Font("SansSerif", Font.PLAIN, 14));
        field.setPreferredSize(new Dimension(280, 35));
        field.setToolTipText(placeholder);
        form.add(field, gbc);
    }

    private JPanel buildNav() {
        JPanel p = new JPanel(new FlowLayout(FlowLayout.RIGHT, 20, 12));
        p.setBackground(new Color(245, 247, 252));
        p.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, new Color(220, 225, 235)));

        JButton next = makeButton("Next →", new Color(33, 102, 172), Color.WHITE);
        next.addActionListener(e -> onNext());
        p.add(next);
        return p;
    }

    private void onNext() {
        String user    = usernameField.getText().trim();
        String school  = schoolField.getText().trim();
        String session = sessionNameField.getText().trim();

        if (user.isEmpty()) {
            warn("Please enter your username to continue.", usernameField);
            return;
        }
        if (school.isEmpty()) {
            warn("Please enter your school name to continue.", schoolField);
            return;
        }
        if (session.isEmpty()) {
            warn("Please enter a session name to continue.", sessionNameField);
            return;
        }

        mainFrame.getSession().setProfile(new UserProfile(user, school, session));
        mainFrame.goToStep(1);
    }

    private void warn(String msg, JTextField focus) {
        JOptionPane.showMessageDialog(this, msg, "Missing Information", JOptionPane.WARNING_MESSAGE);
        focus.requestFocus();
    }

    /** Called by MainFrame.resetSession() to clear the form. */
    public void clearFields() {
        usernameField.setText("");
        schoolField.setText("");
        sessionNameField.setText("");
    }

    // -----------------------------------------------------------------------
    // Utility
    // -----------------------------------------------------------------------
    static JButton makeButton(String text, Color bg, Color fg) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("SansSerif", Font.BOLD, 14));
        btn.setBackground(bg);
        btn.setForeground(fg);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setPreferredSize(new Dimension(130, 40));
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.setOpaque(true);
        return btn;
    }
}
