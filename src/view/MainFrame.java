package view;

import model.MeasurementSession;
import javax.swing.*;
import java.awt.*;

/**
 * Main application window. Uses CardLayout as a wizard container.
 * Manages navigation between the 5 steps and tracks completion state.
 */
public class MainFrame extends JFrame {

    private static final String[] STEP_NAMES = {"Profile", "Define", "Plan", "Collect", "Analyse"};

    private final CardLayout          cardLayout    = new CardLayout();
    private final JPanel              mainPanel     = new JPanel(cardLayout);
    private final StepIndicatorPanel  stepIndicator = new StepIndicatorPanel(STEP_NAMES);
    private final MeasurementSession  session       = new MeasurementSession();
    private final boolean[]           completed     = new boolean[STEP_NAMES.length];

    private int currentStep = 0;

    // Step panels
    private final ProfilePanel  profilePanel;
    private final DefinePanel   definePanel;
    private final PlanPanel     planPanel;
    private final CollectPanel  collectPanel;
    private final AnalysePanel  analysePanel;

    public MainFrame() {
        setTitle("ISO 15939 Measurement Process Simulator");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(950, 700);
        setMinimumSize(new Dimension(820, 580));
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        add(stepIndicator, BorderLayout.NORTH);

        profilePanel  = new ProfilePanel(this);
        definePanel   = new DefinePanel(this);
        planPanel     = new PlanPanel(this);
        collectPanel  = new CollectPanel(this);
        analysePanel  = new AnalysePanel(this);

        mainPanel.add(profilePanel,  "Profile");
        mainPanel.add(definePanel,   "Define");
        mainPanel.add(planPanel,     "Plan");
        mainPanel.add(collectPanel,  "Collect");
        mainPanel.add(analysePanel,  "Analyse");

        add(mainPanel, BorderLayout.CENTER);

        stepIndicator.update(0, completed);
    }

    /** Navigate to a step, marking the previous step as completed if going forward. */
    public void goToStep(int step) {
        if (step > currentStep) {
            completed[currentStep] = true;
        }
        currentStep = step;
        stepIndicator.update(currentStep, completed);
        cardLayout.show(mainPanel, STEP_NAMES[step]);

        switch (step) {
            case 2: planPanel.refresh(session);    break;
            case 3: collectPanel.refresh(session); break;
            case 4: analysePanel.refresh(session); break;
            default: break;
        }
    }

    /** Resets session state and returns to Step 1 (used by "New Session"). */
    public void resetSession() {
        for (int i = 0; i < completed.length; i++) completed[i] = false;
        currentStep = 0;
        stepIndicator.update(0, completed);
        cardLayout.show(mainPanel, "Profile");
        profilePanel.clearFields();
    }

    public MeasurementSession getSession()  { return session; }
    public int                getCurrentStep() { return currentStep; }
}
