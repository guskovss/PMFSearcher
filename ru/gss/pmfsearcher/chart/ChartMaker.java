/*
 * Pipeline Magnetic Field Searcher
 */
package ru.gss.pmfsearcher.chart;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.text.NumberFormat;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.labels.StandardXYToolTipGenerator;
import org.jfree.chart.plot.IntervalMarker;
import org.jfree.chart.plot.Marker;
import org.jfree.chart.plot.ValueMarker;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.chart.renderer.xy.XYStepRenderer;
import org.jfree.data.xy.XYDataset;
import org.jfree.ui.LengthAdjustmentType;
import org.jfree.ui.RectangleAnchor;
import org.jfree.ui.RectangleEdge;
import org.jfree.ui.RectangleInsets;
import org.jfree.ui.TextAnchor;
import ru.gss.pmfsearcher.data.DataList;
import ru.gss.pmfsearcher.data.Reper;

/**
 * Chart.
 * @version 1.1.0 21.01.2021
 * @author Sergey Guskov
 */
public class ChartMaker {

    /**
     * Constructor.
     */
    public ChartMaker() {
    }

    /**
     * Create plot.
     * @param dataset data
     * @param labelX name of axis x
     * @param labelY name of axis y
     * @param isStepPlot step chart
     * @return plot
     */
    private XYPlot createPlot(final XYDataset dataset, 
            final String labelX, final String labelY, final boolean isStepPlot) {
        NumberAxis xAxis = new NumberAxis(labelX);
        xAxis.setTickLabelFont(new Font("Tahoma", Font.PLAIN, 12));
        xAxis.setLabelFont(new Font("Tahoma", Font.BOLD, 12));
        xAxis.setAutoRangeIncludesZero(false);
        xAxis.setLowerMargin(0);
        xAxis.setUpperMargin(0);
        NumberAxis yAxis = new NumberAxis(labelY);
        yAxis.setTickLabelFont(new Font("Tahoma", Font.PLAIN, 12));
        yAxis.setLabelFont(new Font("Tahoma", Font.BOLD, 12));
        yAxis.setAutoRangeIncludesZero(false);
        //Parameters of series
        XYItemRenderer renderer;
        if (isStepPlot) {
            renderer = new XYStepRenderer();
        } else {
            renderer = new XYLineAndShapeRenderer();
            for (int i = 0; i < 3; i++) {
                ((XYLineAndShapeRenderer) renderer).setSeriesShapesVisible(i, false);
            }
        }
        //Colors
        renderer.setSeriesPaint(0, Color.BLUE);
        renderer.setSeriesPaint(1, Color.RED);
        renderer.setSeriesPaint(2, new Color(0, 170, 0));
        //Tooltips
        for (int i = 0; i < 3; i++) {
            renderer.setSeriesToolTipGenerator(i, new StandardXYToolTipGenerator("{1}; {2}", NumberFormat.getNumberInstance(), NumberFormat.getNumberInstance()));
        }
        XYPlot plot = new XYPlot(dataset, xAxis, yAxis, renderer);
        plot.setBackgroundPaint(Color.white);
        plot.setDomainGridlinePaint(Color.darkGray);
        plot.setRangeGridlinePaint(Color.darkGray);
        plot.setAxisOffset(new RectangleInsets(4, 4, 4, 4));
        return plot;
    }

    /**
     * Create markers.
     * @param data data
     * @param plot plot
     */
    private void createMarker(final DataList data, final XYPlot plot) {
        for (int k = 0; k < data.getSource().size(); k++) {
            //Repers
            if (data.isShowReper()) {
                for (int i = 0; i < data.getReper().size(); i++) {
                    Reper rl = data.getReper().get(i);
                    //Markers
                    if (rl.getType() == 0) {
                        Marker m = new ValueMarker(rl.getCoord1());
                        m.setStroke(new BasicStroke(1.5F));
                        m.setPaint(Color.ORANGE);
                        //Labels
                        String label = rl.getName();
                        if (label.startsWith("<L>")) {
                            m.setLabelAnchor(RectangleAnchor.TOP_LEFT);
                            m.setLabelTextAnchor(TextAnchor.TOP_RIGHT);
                            label = label.substring(3, label.length());
                        } else {
                            m.setLabelAnchor(RectangleAnchor.TOP_RIGHT);
                            m.setLabelTextAnchor(TextAnchor.TOP_LEFT);
                        }
                        m.setLabelOffsetType(LengthAdjustmentType.EXPAND);
                        m.setLabel(label);
                        m.setLabelFont(new Font("Tahoma", Font.PLAIN, 11));
                        m.setLabelPaint(Color.BLACK);
                        plot.addDomainMarker(m);
                    } else {
                        //Interval
                        Marker m = new IntervalMarker(rl.getCoord1(), rl.getCoord2());
                        m.setAlpha(0.3F);
                        m.setPaint(Color.ORANGE);
                        plot.addDomainMarker(m);
                        //Labels
                        String label = rl.getName();
                        boolean leftLabel = false;
                        if (label.startsWith("<L>")) {
                            label = label.substring(3, label.length());
                            leftLabel = true;
                        }
                        //Left border
                        m = new ValueMarker(rl.getCoord1());
                        m.setStroke(new BasicStroke(1.0F));
                        m.setPaint(Color.ORANGE);
                        if (leftLabel) {
                            m.setLabelAnchor(RectangleAnchor.TOP_LEFT);
                            m.setLabelTextAnchor(TextAnchor.TOP_RIGHT);
                            m.setLabelOffsetType(LengthAdjustmentType.EXPAND);
                            m.setLabel(label);
                            m.setLabelFont(new Font("Tahoma", Font.PLAIN, 11));
                            m.setLabelPaint(Color.BLACK);
                        }
                        plot.addDomainMarker(m);
                        //Right border
                        m = new ValueMarker(rl.getCoord2());
                        m.setStroke(new BasicStroke(1.0F));
                        m.setPaint(Color.ORANGE);
                        if (!leftLabel) {
                            m.setLabelAnchor(RectangleAnchor.TOP_RIGHT);
                            m.setLabelTextAnchor(TextAnchor.TOP_LEFT);
                            m.setLabelOffsetType(LengthAdjustmentType.EXPAND);
                            m.setLabel(label);
                            m.setLabelFont(new Font("Tahoma", Font.PLAIN, 11));
                            m.setLabelPaint(Color.BLACK);
                        }
                        plot.addDomainMarker(m);
                    }
                }
            }
        }
    }

    /**
     * Create chart.
     * @param data data
     * @return chart
     */
    public JFreeChart createChart(final DataList data) {
        XYPlot plot = createPlot(data.createDataset(), "Линейная координата, м", "Напряженность магнитного поля, А/м", false);
        createMarker(data, plot);
        JFreeChart chart = new JFreeChart("", plot);
        chart.setBackgroundPaint(Color.white);
        chart.getLegend().setPosition(RectangleEdge.RIGHT);
        chart.getLegend().setBorder(0, 0, 0, 0);
        chart.setPadding(new RectangleInsets(8, 4, 0, 10));
        return chart;
    }
}
