/*
 * Pipeline Magnetic Field Searcher
 */
package ru.gss.pmfsearcher.data;

/**
 * Source of magnetic field.
 * @version 1.1.0 21.01.2021
 * @author Sergey Guskov
 */
public class Source {
    
    /**
     * Coordinate linear, m.
     */
    private double coordLinear;
    /**
     * Coordinate angular, deg.
     */
    private double coordAngular;
    /**
     * Length linear, m.
     */
    private double lengthLinear;
    /**
     * Length angular, deg.
     */
    private double lengthAngular;
    /**
     * Step linear, m.
     */
    private double stepLinear;
    /**
     * Step angular, deg.
     */
    private double stepAngular;
    /**
     * Magnetization, A/m.
     */
    private FieldVector magnetization;

    /**
     * Constructor.
     */
    public Source() {
        coordLinear = 0;
        coordAngular = 0;
        lengthLinear = 1;
        lengthAngular = 360;
        stepLinear = 0.1;
        stepAngular = 10;
        magnetization = new FieldVector();
    }

    /**
     * Convert degrees to radians.
     * @param v angle in degrees
     * @return angle in radians
     */
    private double degreeToRad(final double v) {
        return v * Math.PI / 180;
    }

    /**
     * Calculating the dipole field.
     * @param vi magnetization
     * @param vr radius-vector of the field calculation point
     * @param v volume
     * @return dipole field
     */
    private FieldVector calcFieldDipol(final FieldVector vi, final FieldVector vr, final double v) {
        FieldVector vh = new FieldVector();
        double r = Math.sqrt(vr.getX() * vr.getX() + vr.getY() * vr.getY() + vr.getZ() * vr.getZ());
        if (r > 20) {
            return vh;
        }
        double r3 = r * r * r;
        double r5 = r3 * r * r;
        double sp = vi.getX() * vr.getX() +  vi.getY() * vr.getY() +  vi.getZ() * vr.getZ();
        for (int i = 0; i < 3; i++) {
            vh.set(i, v * (3 * sp * vr.get(i) / r5 - vi.get(i) / r3) / 4 / Math.PI);
        }
        return vh;
    }

    /**
     * Calculating the source field.
     * @param vr radius-vector of the field calculation point
     * @param diameter pipeline diameter
     * @param thickness pipeline wall thickness
     * @return source field
     */
    public FieldVector calcFieldSource(final FieldVector vr, final double diameter, final double thickness) {
        //Calculating the radius
        double radius = diameter / 2 - thickness / 2;
        FieldVector vh = new FieldVector();
        FieldVector vd = new FieldVector();
        //The volume of the site
        double v = thickness * getStepLinear() * radius * degreeToRad(stepAngular);
        //Cycles by length and by angle
        int ni = (int) Math.round(lengthLinear / stepLinear);
        int nj = (int) Math.round(lengthAngular / stepAngular);
        for (int i = 0; i < ni; i++) {
            double y = coordLinear - lengthLinear / 2 + stepLinear / 2 + i * stepLinear;
            for (int j = 0; j < nj; j++) {
                double fi = coordAngular - lengthAngular / 2 + stepAngular / 2 + j * stepAngular;
                //Calculating the radius-vector
                vd.setX(vr.getX() + radius * Math.cos(degreeToRad(fi)));
                vd.setY(vr.getY() - y);
                vd.setZ(vr.getZ() - radius * Math.sin(degreeToRad(fi)));
                //Calculating the field
                vh.add(calcFieldDipol(magnetization, vd, v));
            }
        }
        return vh;
    }

    /**
     * Coordinate linear.
     * @return coordinate linear
     */
    public double getCoordLinear() {
        return coordLinear;
    }

    /**
     * Coordinate linear.
     * @param aCoordLinear coordinate linear
     */
    public void setCoordLinear(final double aCoordLinear) {
        coordLinear = aCoordLinear;
    }

    /**
     * Coordinate angular.
     * @return coordinate angular
     */
    public double getCoordAngular() {
        return coordAngular;
    }

    /**
     * Coordinate angular.
     * @param aCoordAngular coordinate angular
     */
    public void setCoordAngular(final double aCoordAngular) {
        coordAngular = aCoordAngular;
    }

    /**
     * Length linear.
     * @return length linear
     */
    public double getLengthLinear() {
        return lengthLinear;
    }

    /**
     * Length linear.
     * @param aLengthLinear length linear
     */
    public void setLengthLinear(final double aLengthLinear) {
        lengthLinear = aLengthLinear;
    }

    /**
     * Length angular.
     * @return length angular
     */
    public double getLengthAngular() {
        return lengthAngular;
    }

    /**
     * Length angular.
     * @param aLengthAngular length angular
     */
    public void setLengthAngular(final double aLengthAngular) {
        lengthAngular = aLengthAngular;
    }

    /**
     * Step linear.
     * @return step linear
     */
    public double getStepLinear() {
        return stepLinear;
    }

    /**
     * Step linear.
     * @param aStepLinear step linear
     */
    public void setStepLinear(final double aStepLinear) {
        stepLinear = aStepLinear;
    }

    /**
     * Step angular.
     * @return step angular
     */
    public double getStepAngular() {
        return stepAngular;
    }

    /**
     * Step angular.
     * @param aStepAngular step angular
     */
    public void setStepAngular(final double aStepAngular) {
        stepAngular = aStepAngular;
    }

    /**
     * Magnetisation.
     * @return magnetization
     */
    public FieldVector getMagnetization() {
        return magnetization;
    }
}
