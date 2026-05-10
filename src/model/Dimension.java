package model;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a quality dimension (e.g., Usability, Reliability) that groups related metrics.
 * Computes a weighted average score across all its metrics.
 */
public class Dimension {

    private final String name;
    private final int coefficient;
    private final List<Metric> metrics;

    public Dimension(String name, int coefficient) {
        this.name = name;
        this.coefficient = coefficient;
        this.metrics = new ArrayList<>();
    }

    public void addMetric(Metric metric) {
        metrics.add(metric);
    }

    /**
     * Weighted average: sum(score * coeff) / sum(coeff)
     */
    public double calculateScore() {
        if (metrics.isEmpty()) return 0.0;
        double weightedSum = 0.0;
        int totalCoeff = 0;
        for (Metric m : metrics) {
            weightedSum += m.calculateScore() * m.getCoefficient();
            totalCoeff  += m.getCoefficient();
        }
        return totalCoeff == 0 ? 0.0 : weightedSum / totalCoeff;
    }

    // Getters
    public String      getName()       { return name; }
    public int         getCoefficient(){ return coefficient; }
    public List<Metric> getMetrics()   { return metrics; }
}
