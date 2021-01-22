/*
 * Pipeline Magnetic Field Searcher
 */
package ru.gss.pmfsearcher.data;

/**
 * Result of calculation.
 * @version 1.1.0 21.01.2021
 * @author Sergey Guskov
 */
public class DataLine {

    /**
     * Coordinate, m.
     */
    private double coordinate;
    /**
     * Magnetic field, A/m.
     */
    private FieldVector field;
    
    /**
     * Constructor.
     */
    public DataLine() {
        field = new FieldVector();
    }

    /**
     * Coordinate.
     * @return coordinate
     */
    public double getCoordinate() {
        return coordinate;
    }

    /**
     * Coordinate.
     * @param aCoordinate coordinate
     */
    public void setCoordinate(final double aCoordinate) {
        coordinate = aCoordinate;
    }

    /**
     * Magnetic field.
     * @return magnetic field
     */
    public FieldVector getField() {
        return field;
    }
}
