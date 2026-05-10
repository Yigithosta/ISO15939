package model;

/**
 * Holds the user profile information collected in Step 1.
 */
public class UserProfile {

    private String username;
    private String school;
    private String sessionName;

    public UserProfile(String username, String school, String sessionName) {
        this.username    = username;
        this.school      = school;
        this.sessionName = sessionName;
    }

    // Getters & Setters
    public String getUsername()            { return username; }
    public void   setUsername(String v)    { this.username = v; }

    public String getSchool()              { return school; }
    public void   setSchool(String v)      { this.school = v; }

    public String getSessionName()         { return sessionName; }
    public void   setSessionName(String v) { this.sessionName = v; }
}
