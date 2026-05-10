package model;

/**
 * Represents a single measurable metric within a quality dimension.
 * Encapsulates all metric properties and score calculation logic.
 */
public class Metric {

    private final String name;
    private final int coefficient;
    private final boolean higherIsBetter;
    private final double rangeMin;
    private final double rangeMax;
    private final String unit;
    private double value;

    public Metric(String name, int coefficient, boolean higherIsBetter,
                  double rangeMin, double rangeMax, String unit, double value) {
        this.name = name;
        this.coefficient = coefficient;
        this.higherIsBetter = higherIsBetter;
        this.rangeMin = rangeMin;
        this.rangeMax = rangeMax;
        this.unit = unit;
        this.value = value;
    }

    /**
     * Calculates a score between 1.0 and 5.0, rounded to nearest 0.5.
     * Higher is better:  score = 1 + (value - min) / (max - min) * 4
     * Lower is better:   score = 5 - (value - min) / (max - min) * 4
     */
    public double calculateScore() {
        double range = rangeMax - rangeMin;
        double raw;
        if (higherIsBetter) {
            raw = 1.0 + (value - rangeMin) / range * 4.0;
        } else {
            raw = 5.0 - (value - rangeMin) / range * 4.0;
        }
        raw = Math.max(1.0, Math.min(5.0, raw));
        return Math.round(raw * 2) / 2.0;
    }

    public String getDirectionText() {
        return higherIsBetter ? "Higher ↑" : "Lower ↓";
    }

    public String getRangeText() {
        String minStr = (rangeMin == Math.floor(rangeMin)) ? String.valueOf((int) rangeMin) : String.valueOf(rangeMin);
        String maxStr = (rangeMax == Math.floor(rangeMax)) ? String.valueOf((int) rangeMax) : String.valueOf(rangeMax);
        return minStr + "–" + maxStr;
    }

    public String getValueText() {
        return (value == Math.floor(value)) ? String.valueOf((int) value) : String.valueOf(value);
    }

    // Getters
    public String getName()        { return name; }
    public int getCoefficient()    { return coefficient; }
    public boolean isHigherIsBetter() { return higherIsBetter; }
    public double getRangeMin()    { return rangeMin; }
    public double getRangeMax()    { return rangeMax; }
    public String getUnit()        { return unit; }
    public double getValue()       { return value; }
    public void setValue(double v) { this.value = v; }
}
