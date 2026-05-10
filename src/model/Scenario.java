package model;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a measurement scenario containing a set of quality dimensions.
 */
public class Scenario {

    private final String name;
    private final String mode;
    private final List<Dimension> dimensions;

    public Scenario(String name, String mode) {
        this.name = name;
        this.mode = mode;
        this.dimensions = new ArrayList<>();
    }

    public void addDimension(Dimension dimension) {
        dimensions.add(dimension);
    }

    // Getters
    public String          getName()      { return name; }
    public String          getMode()      { return mode; }
    public List<Dimension> getDimensions(){ return dimensions; }
}
