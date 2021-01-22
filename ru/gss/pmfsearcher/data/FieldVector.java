/*
 * Pipeline Magnetic Field Searcher
 */
package ru.gss.pmfsearcher.data;

/**
 * Vector of field.
 * @version 1.1.0 21.01.2021
 * @author Sergey Guskov
 */
public class FieldVector {
    
    /**
     * Components of vector.
     */
    private Double[] components = new Double[3];
    
    /**
     * Constructor.
     */
    public FieldVector() {
        init();
    }

    /**
     * Constructor.
     * @param aX x-component
     * @param aY y-component
     * @param aZ z-component
     */
    public FieldVector(final Double aX, final Double aY, final Double aZ) {
        components[0] = aX;
        components[1] = aY;
        components[2] = aZ;
    }

    /**
     * Initialization.
     */
    public void init() {
        for (int i = 0; i < 3; i++) {
            components[i] = 0.0;
        }
    }

    /**
     * Adding vector.
     * @param v vector to add
     */
    public void add(final FieldVector v) {
        for (int i = 0; i < 3; i++) {
            components[i] = components[i] + v.get(i);
        }
    }

    /**
     * X-component.
     * @return x-component
     */
    public Double getX() {
        return components[0];
    }

    /**
     * X-component.
     * @param aX x-component
     */
    public void setX(final Double aX) {
        components[0] = aX;
    }

    /**
     * Y-component.
     * @return y-component
     */
    public Double getY() {
        return components[1];
    }

    /**
     * Y-component.
     * @param aY y-component
     */
    public void setY(final Double aY) {
        components[1] = aY;
    }

    /**
     * Z-component.
     * @return z-component
     */
    public Double getZ() {
        return components[2];
    }

    /**
     * Z-component.
     * @param aZ z-component
     */
    public void setZ(final Double aZ) {
        components[2] = aZ;
    }

    /**
     * Component by index.
     * @param i index
     * @return component by index
     */
    public Double get(final int i) {
        return components[i];
    }

    /**
     * Component by index.
     * @param i index
     * @param value component by index
     */
    public void set(final int i, final Double value) {
        components[i] = value;
    }
}
