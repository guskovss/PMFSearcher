/*
 * Pipeline Magnetic Field Searcher
 */
package ru.gss.pmfsearcher.data;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Locale;
import javax.swing.JTextArea;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

/**
 * List of sources.
 * @version 1.1.0 21.01.2021
 * @author Sergey Guskov
 */
public class DataList {

    /**
     * Parameters.
     */
    private Parameter parameter;
    /**
     * List of sources.
     */
    private ArrayList<Source> source;
    /**
     * List of repers.
     */
    private ArrayList<Reper> reper;
    /**
     * Result of calculation.
     */
    private ArrayList<DataLine> data;
    /**
     * Show repers on charts.
     */
    private boolean showReper;
    /**
     * Name of file.
     */
    private String fileName;
    /**
     * Count of parse exeptions.
     */
    private int parseExceptionCount;

    /**
     * Constructor.
     */
    public DataList() {
        parameter = new Parameter();
        source = new ArrayList<Source>();
        reper = new ArrayList<Reper>();
        data = new ArrayList<DataLine>();
    }

    /**
     * Save text area to file.
     * @param file file
     * @param jta text area
     * @throws java.io.IOException exception
     */
    public void saveTextAreaToFile(final File file, final JTextArea jta) throws IOException {
        PrintWriter out = null;
        try {
            out = new PrintWriter(new FileOutputStream(file), true);
            out.print(jta.getText());
        } finally {
            out.close();
        }
    }

    /**
     * Parse double value.
     * @param s string representation of double value
     * @return double value or null
     */
    private Double parseDouble(final String s) {
        if (s.trim().isEmpty()) {
            return null;
        }
        if (s.equals("-")) {
            return null;
        }
        try {
            String ss = s.replaceAll(",", ".");
            return Double.valueOf(ss);
        } catch (NumberFormatException ex) {
            parseExceptionCount++;
            return null;
        }
    }

    /**
     * Convert double value to string.
     * @param value double value
     * @param precision count of symbols after separator
     * @return string representation of value
     */
    private String convertToString(final Double value, final int precision) {
        if (value == null) {
            return " ";
        }
        return String.format(Locale.US, "%." + precision + "f", value);
    }

    /**
     * Load data from file.
     * @param file file
     * @throws java.io.IOException exception
     */
    public void loadDataFromFile(final File file) throws IOException {
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader(file));
            ArrayList<String> strings = new ArrayList<String>();
            String line;
            while ((line = reader.readLine()) != null) {
                strings.add(line);
            }
            parseExceptionCount = 0;
            fileName = file.getAbsolutePath();
            source.clear();
            for (int i = 0; i < strings.size(); i++) {
                String[] columns = strings.get(i).split("\t");
                if (columns.length > 8) {
                    Source o = new Source();
                    o.setCoordLinear(parseDouble(columns[0]));
                    o.setCoordAngular(parseDouble(columns[1]));
                    o.setLengthLinear(parseDouble(columns[2]));
                    o.setLengthAngular(parseDouble(columns[3]));
                    o.setStepLinear(parseDouble(columns[4]));
                    o.setStepAngular(parseDouble(columns[5]));
                    o.getMagnetization().setX(parseDouble(columns[6]));
                    o.getMagnetization().setY(parseDouble(columns[7]));
                    o.getMagnetization().setZ(parseDouble(columns[8]));
                    source.add(o);
                }
            }
        } finally {
            reader.close();
        }
    }

    /**
     * Save data to file.
     * @param file file
     * @throws java.io.IOException exception
     */
    public void saveDataToFile(final File file) throws IOException {
        BufferedWriter writer = null;
        try {
            writer = new BufferedWriter(new FileWriter(file));
            String line;
            for (int i = 0; i < source.size(); i++) {
                Source o = source.get(i);
                line = convertToString(o.getCoordLinear(), 4) + "\t" +
                        convertToString(o.getCoordAngular(), 4) + "\t" +
                        convertToString(o.getLengthLinear(), 4) + "\t" +
                        convertToString(o.getLengthAngular(), 4) + "\t" +
                        convertToString(o.getStepLinear(), 4) + "\t" +
                        convertToString(o.getStepAngular(), 4) + "\t" +
                        convertToString(o.getMagnetization().getX(), 4) + "\t" +
                        convertToString(o.getMagnetization().getY(), 4) + "\t" +
                        convertToString(o.getMagnetization().getZ(), 4);
                writer.write(line);
                writer.newLine();
            }
            fileName = file.getAbsolutePath();
        } finally {
            writer.close();
        }
    }

    /**
     * Calculation.
     */
    public void calculate() {
        data.clear();
        double d = parameter.getDiameter();
        double t = parameter.getThickness();
        FieldVector vh = new FieldVector();
        FieldVector vr = new FieldVector();
        double y = parameter.getCoordBegin();
        for (int i = 0; i < parameter.getCoordCount(); i++) {
            vr.setX(-parameter.getDepth());
            vr.setY(y);
            vr.setZ(0.0);
            vh.init();
            for (int j = 0; j < source.size(); j++) {
                Source dip = source.get(j);
                vh.add(dip.calcFieldSource(vr, d, t));
            }
            DataLine dl = new DataLine();
            dl.setCoordinate(y);
            for (int j = 0; j < 3; j++) {
                dl.getField().set(j, vh.get(j));
            }
            data.add(dl);
            y = y + parameter.getCoordStep();
        }
    }

     /**
     * Create dataset for chart.
     * @return dataset
     */
    public XYSeriesCollection createDataset() {
        XYSeriesCollection dataset = new XYSeriesCollection();
        XYSeries seriesX = new XYSeries("Hx");
        XYSeries seriesY = new XYSeries("Hy");
        XYSeries seriesZ = new XYSeries("Hz");
        for (int j = 0; j < data.size(); j++) {
            seriesX.add(data.get(j).getCoordinate(), data.get(j).getField().getX());
            seriesY.add(data.get(j).getCoordinate(), data.get(j).getField().getY());
            seriesZ.add(data.get(j).getCoordinate(), data.get(j).getField().getZ());
        }
        dataset.addSeries(seriesX);
        dataset.addSeries(seriesY);
        dataset.addSeries(seriesZ);
        return dataset;
    }

    /**
     * Parameters.
     * @return parameters
     */
    public Parameter getParameter() {
        return parameter;
    }

    /**
     * List of sources.
     * @return list of sources
     */
    public ArrayList<Source> getSource() {
        return source;
    }

    /**
     * List of repers.
     * @return list of repers
     */
    public ArrayList<Reper> getReper() {
        return reper;
    }

    /**
     * Result of calculation.
     * @return result of calculation
     */
    public ArrayList<DataLine> getData() {
        return data;
    }

    /**
     * Name of file.
     * @return name of file
     */
    public String getFileName() {
        return fileName;
    }

    /**
     * Name of file.
     * @param aFileName name of file
     */
    public void setFileName(final String aFileName) {
        fileName = aFileName;
    }

    /**
     * Show repers on charts.
     * @return show repers on charts
     */
    public boolean isShowReper() {
        return showReper;
    }

    /**
     * Show repers on charts.
     * @param aShowReper show repers on charts
     */
    public void setShowReper(final boolean aShowReper) {
        showReper = aShowReper;
    }

    /**
     * Count of parse exeptions.
     * @return count of parse exeptions
     */
    public int getParseExceptionCount() {
        return parseExceptionCount;
    }
}
