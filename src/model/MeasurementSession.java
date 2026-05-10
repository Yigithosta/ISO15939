package model;

/**
 * Central session object passed through all wizard steps.
 * Aggregates user profile, quality type, mode, and selected scenario.
 */
public class MeasurementSession {

    private UserProfile profile;
    private String      qualityType; // "Product" or "Process"
    private String      mode;        // "Health" or "Education"
    private Scenario    scenario;

    public MeasurementSession() {}

    // Getters & Setters
    public UserProfile getProfile()              { return profile; }
    public void        setProfile(UserProfile p) { this.profile = p; }

    public String  getQualityType()            { return qualityType; }
    public void    setQualityType(String qt)   { this.qualityType = qt; }

    public String  getMode()                   { return mode; }
    public void    setMode(String m)           { this.mode = m; }

    public Scenario getScenario()              { return scenario; }
    public void     setScenario(Scenario s)    { this.scenario = s; }
}
