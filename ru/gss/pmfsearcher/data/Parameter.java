/*
 * Pipeline Magnetic Field Searcher
 */
package ru.gss.pmfsearcher.data;

/**
 * Model parameters.
 * @version 1.1.0 21.01.2021
 * @author Sergey Guskov
 */
public class Parameter {

    /**
     * Pipeline diameter, m.
     */
    private double diameter;
    /**
     * Pipeline wall thickness, m.
     */
    private double thickness;
    /**
     * Pipeline depth, m.
     */
    private double depth;
    /**
     * Site begin coordinate, m.
     */
    private double coordBegin;
    /**
     * Site step, m.
     */
    private double coordStep;
    /**
     * Site point count.
     */
    private int coordCount;

    /**
     * Constructor.
     */
    public Parameter() {
        diameter = 1.02;
        thickness = 0.02;
        depth = 2;
        coordBegin = 0;
        coordStep = 1;
        coordCount = 201;
    }

    /**
     * Pipeline diameter.
     * @return pipeline diameter
     */
    public double getDiameter() {
        return diameter;
    }

    /**
     * Pipeline diameter.
     * @param aDiameter pipeline diameter
     */
    public void setDiameter(final double aDiameter) {
        diameter = aDiameter;
    }

    /**
     * Pipeline wall thickness.
     * @return pipeline wall thickness
     */
    public double getThickness() {
        return thickness;
    }

    /**
     * Pipeline wall thickness.
     * @param aThickness pipeline wall thickness
     */
    public void setThickness(final double aThickness) {
        thickness = aThickness;
    }

    /**
     * Pipeline depth.
     * @return pipeline depth
     */
    public double getDepth() {
        return depth;
    }

    /**
     * Pipeline depth.
     * @param aDepth pipeline depth
     */
    public void setDepth(final double aDepth) {
        depth = aDepth;
    }

    /**
     * Site begin coordinate.
     * @return site begin coordinate
     */
    public double getCoordBegin() {
        return coordBegin;
    }

    /**
     * Site begin coordinate.
     * @param aCoordBegin site begin coordinate
     */
    public void setCoordBegin(final double aCoordBegin) {
        coordBegin = aCoordBegin;
    }

    /**
     * Site step.
     * @return Site step
     */
    public double getCoordStep() {
        return coordStep;
    }

    /**
     * Site step.
     * @param aCoordStep site step
     */
    public void setCoordStep(final double aCoordStep) {
        coordStep = aCoordStep;
    }

    /**
     * Site point count.
     * @return site point count
     */
    public int getCoordCount() {
        return coordCount;
    }

    /**
     * Site point count.
     * @param aCoordCount site point count
     */
    public void setCoordCount(final int aCoordCount) {
        coordCount = aCoordCount;
    }
}
