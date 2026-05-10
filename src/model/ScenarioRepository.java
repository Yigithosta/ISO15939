package model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Central data store for all hard-coded scenarios.
 * Provides lookup methods used by the Define and Plan steps.
 *
 * Structure: mode -> List<Scenario>
 * Each scenario contains Dimensions, each Dimension contains Metrics with raw values.
 */
public class ScenarioRepository {

    private static final Map<String, List<Scenario>> SCENARIO_MAP = new HashMap<>();

    static {
        buildEducationScenarios();
        buildHealthScenarios();
    }

    // -----------------------------------------------------------------------
    // Education Mode
    // -----------------------------------------------------------------------
    private static void buildEducationScenarios() {
        List<Scenario> list = new ArrayList<>();

        // --- Scenario C: Team Alpha ---
        Scenario c = new Scenario("Scenario C — Team Alpha", "Education");

        Dimension usabilityC = new Dimension("Usability", 25);
        usabilityC.addMetric(new Metric("SUS score",       50, true,  0,  100, "points", 89));
        usabilityC.addMetric(new Metric("Onboarding time", 50, false, 0,  60,  "min",     5));
        c.addDimension(usabilityC);

        Dimension perfC = new Dimension("Perf. Efficiency", 20);
        perfC.addMetric(new Metric("Video start time",  50, false, 0,  15,  "sec",   2));
        perfC.addMetric(new Metric("Concurrent exams",  50, true,  0,  600, "users", 450));
        c.addDimension(perfC);

        Dimension accessC = new Dimension("Accessibility", 20);
        accessC.addMetric(new Metric("WCAG compliance",     50, true, 0, 100, "%", 92));
        accessC.addMetric(new Metric("Screen reader score", 50, true, 0, 100, "%", 85));
        c.addDimension(accessC);

        Dimension relC = new Dimension("Reliability", 20);
        relC.addMetric(new Metric("Uptime", 50, true,  95, 100, "%",   99.5));
        relC.addMetric(new Metric("MTTR",   50, false, 0,  120, "min", 10));
        c.addDimension(relC);

        Dimension funcC = new Dimension("Func. Suitability", 15);
        funcC.addMetric(new Metric("Feature completion",     50, true, 0, 100, "%", 95));
        funcC.addMetric(new Metric("Assignment submit rate", 50, true, 0, 100, "%", 88));
        c.addDimension(funcC);

        list.add(c);

        // --- Scenario D: Team Beta ---
        Scenario d = new Scenario("Scenario D — Team Beta", "Education");

        Dimension usabilityD = new Dimension("Usability", 25);
        usabilityD.addMetric(new Metric("SUS score",       50, true,  0,  100, "points", 72));
        usabilityD.addMetric(new Metric("Onboarding time", 50, false, 0,  60,  "min",    20));
        d.addDimension(usabilityD);

        Dimension perfD = new Dimension("Perf. Efficiency", 20);
        perfD.addMetric(new Metric("Video start time",  50, false, 0,  15,  "sec",    8));
        perfD.addMetric(new Metric("Concurrent exams",  50, true,  0,  600, "users", 200));
        d.addDimension(perfD);

        Dimension accessD = new Dimension("Accessibility", 20);
        accessD.addMetric(new Metric("WCAG compliance",     50, true, 0, 100, "%", 65));
        accessD.addMetric(new Metric("Screen reader score", 50, true, 0, 100, "%", 55));
        d.addDimension(accessD);

        Dimension relD = new Dimension("Reliability", 20);
        relD.addMetric(new Metric("Uptime", 50, true,  95, 100, "%",   97));
        relD.addMetric(new Metric("MTTR",   50, false, 0,  120, "min", 45));
        d.addDimension(relD);

        Dimension funcD = new Dimension("Func. Suitability", 15);
        funcD.addMetric(new Metric("Feature completion",     50, true, 0, 100, "%", 78));
        funcD.addMetric(new Metric("Assignment submit rate", 50, true, 0, 100, "%", 70));
        d.addDimension(funcD);

        list.add(d);

        SCENARIO_MAP.put("Education", list);
    }

    // -----------------------------------------------------------------------
    // Health Mode
    // -----------------------------------------------------------------------
    private static void buildHealthScenarios() {
        List<Scenario> list = new ArrayList<>();

        // --- Scenario A: Clinic Alpha ---
        Scenario a = new Scenario("Scenario A — Clinic Alpha", "Health");

        Dimension perfA = new Dimension("Performance", 30);
        perfA.addMetric(new Metric("Response time", 50, false, 0,  10,   "sec",   1.5));
        perfA.addMetric(new Metric("Throughput",    50, true,  0,  1000, "req/s", 750));
        a.addDimension(perfA);

        Dimension secA = new Dimension("Security", 30);
        secA.addMetric(new Metric("Vulnerability score", 50, false, 0, 100, "points", 10));
        secA.addMetric(new Metric("Auth success rate",   50, true,  0, 100, "%",      99));
        a.addDimension(secA);

        Dimension availA = new Dimension("Availability", 20);
        availA.addMetric(new Metric("System uptime",  50, true,  95, 100, "%",   99.9));
        availA.addMetric(new Metric("Recovery time",  50, false, 0,  60,  "min",  5));
        a.addDimension(availA);

        Dimension usabilityA = new Dimension("Usability", 20);
        usabilityA.addMetric(new Metric("User satisfaction", 50, true,  0,  100, "points", 85));
        usabilityA.addMetric(new Metric("Error rate",        50, false, 0,  20,  "%",       2));
        a.addDimension(usabilityA);

        list.add(a);

        // --- Scenario B: Hospital Beta ---
        Scenario b = new Scenario("Scenario B — Hospital Beta", "Health");

        Dimension perfB = new Dimension("Performance", 30);
        perfB.addMetric(new Metric("Response time", 50, false, 0,  10,   "sec",   4));
        perfB.addMetric(new Metric("Throughput",    50, true,  0,  1000, "req/s", 400));
        b.addDimension(perfB);

        Dimension secB = new Dimension("Security", 30);
        secB.addMetric(new Metric("Vulnerability score", 50, false, 0, 100, "points", 35));
        secB.addMetric(new Metric("Auth success rate",   50, true,  0, 100, "%",      92));
        b.addDimension(secB);

        Dimension availB = new Dimension("Availability", 20);
        availB.addMetric(new Metric("System uptime", 50, true,  95, 100, "%",   96));
        availB.addMetric(new Metric("Recovery time", 50, false, 0,  60,  "min", 25));
        b.addDimension(availB);

        Dimension usabilityB = new Dimension("Usability", 20);
        usabilityB.addMetric(new Metric("User satisfaction", 50, true,  0, 100, "points", 65));
        usabilityB.addMetric(new Metric("Error rate",        50, false, 0,  20, "%",       8));
        b.addDimension(usabilityB);

        list.add(b);

        SCENARIO_MAP.put("Health", list);
    }

    // -----------------------------------------------------------------------
    // Query methods
    // -----------------------------------------------------------------------

    public static List<String> getModes() {
        return new ArrayList<>(SCENARIO_MAP.keySet());
    }

    public static List<Scenario> getScenarios(String mode) {
        return SCENARIO_MAP.getOrDefault(mode, new ArrayList<>());
    }

    public static List<String> getScenarioNames(String mode) {
        List<String> names = new ArrayList<>();
        for (Scenario s : getScenarios(mode)) {
            names.add(s.getName());
        }
        return names;
    }

    public static Scenario findScenario(String mode, String scenarioName) {
        for (Scenario s : getScenarios(mode)) {
            if (s.getName().equals(scenarioName)) return s;
        }
        return null;
    }
}
