/*
 * Pipeline Magnetic Field Searcher
 */
package ru.gss.pmfsearcher.chart;

import javax.swing.text.DefaultFormatterFactory;
import org.jdesktop.application.Action;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.NumberTickUnit;
import ru.gss.pmfsearcher.commons.DlgDirEdit;
import ru.gss.pmfsearcher.commons.NoLocaleNumberFormatter;

/**
 * Dialog for edit of chart parameters.
 * @version 1.1.0 21.01.2021
 * @author Sergey Guskov
 */
public class DlgParameterChartEdit extends DlgDirEdit < JFreeChart > {

    /**
     * Constructor.
     */
    public DlgParameterChartEdit() {
        super();
        initComponents();
        jftfX1.setFormatterFactory(new DefaultFormatterFactory(new NoLocaleNumberFormatter(2)));
        jftfX2.setFormatterFactory(new DefaultFormatterFactory(new NoLocaleNumberFormatter(2)));
        jftfX3.setFormatterFactory(new DefaultFormatterFactory(new NoLocaleNumberFormatter(2)));
        jftfY1.setFormatterFactory(new DefaultFormatterFactory(new NoLocaleNumberFormatter(2)));
        jftfY2.setFormatterFactory(new DefaultFormatterFactory(new NoLocaleNumberFormatter(2)));
        jftfY3.setFormatterFactory(new DefaultFormatterFactory(new NoLocaleNumberFormatter(2)));
    }

    /**
     * Setter editing object.
     * @param aTempObj editing object
     */
    @Override
    public void setTempObj(final JFreeChart aTempObj) {
        putTempObj(aTempObj);
        jftfX1.setValue(getTempObj().getXYPlot().getDomainAxis().getRange().getLowerBound());
        jftfX2.setValue(getTempObj().getXYPlot().getDomainAxis().getRange().getUpperBound());
        jftfX3.setValue(((NumberAxis) getTempObj().getXYPlot().getDomainAxis()).getTickUnit().getSize());
        jchbX1.setSelected(getTempObj().getXYPlot().getDomainAxis().isAutoRange());
        jchbX3.setSelected(getTempObj().getXYPlot().getDomainAxis().isAutoTickUnitSelection());
        jftfY1.setValue(getTempObj().getXYPlot().getRangeAxis().getRange().getLowerBound());
        jftfY2.setValue(getTempObj().getXYPlot().getRangeAxis().getRange().getUpperBound());
        jftfY3.setValue(((NumberAxis) getTempObj().getXYPlot().getRangeAxis()).getTickUnit().getSize());
        jchbY1.setSelected(getTempObj().getXYPlot().getRangeAxis().isAutoRange());
        jchbY3.setSelected(getTempObj().getXYPlot().getRangeAxis().isAutoTickUnitSelection());
        getRootPane().setDefaultButton(jbtnOk);
    }

    /**
     * Init new object.
     * @return new object
     */
    @Override
    public JFreeChart createTempObj() {
        return null;
    }

    /**
     * Action for Cancel button.
     */
    @Action
    public void acCancel() {
        setChangeObj(false);
    }

    /**
     * Action for OK button.
     */
    @Action
    public void acOk() {
        if (checkFormattedTextFieldNullNoSupposed(jftfX1, -1e5, 1e5)) {
            return;
        }
        if (checkFormattedTextFieldNullNoSupposed(jftfX2, -1e5, 1e5)) {
            return;
        }
        if (checkFormattedTextFieldNullNoSupposed(jftfX3, 0.0, 1e5)) {
            return;
        }
        if (checkFormattedTextFieldNullNoSupposed(jftfY1, -1e5, 1e5)) {
            return;
        }
        if (checkFormattedTextFieldNullNoSupposed(jftfY2, -1e5, 1e5)) {
            return;
        }
        if (checkFormattedTextFieldNullNoSupposed(jftfY3, 0.0, 1e5)) {
            return;
        }
        getTempObj().getXYPlot().getDomainAxis().setRange(getDoubleFromFormattedTextField(jftfX1), getDoubleFromFormattedTextField(jftfX2));   
        ((NumberAxis) getTempObj().getXYPlot().getDomainAxis()).setTickUnit(new NumberTickUnit(getDoubleFromFormattedTextField(jftfX3)));
        getTempObj().getXYPlot().getDomainAxis().setAutoRange(jchbX1.isSelected());
        getTempObj().getXYPlot().getDomainAxis().setAutoTickUnitSelection(jchbX3.isSelected());
        getTempObj().getXYPlot().getRangeAxis().setRange(getDoubleFromFormattedTextField(jftfY1), getDoubleFromFormattedTextField(jftfY2));
        ((NumberAxis) getTempObj().getXYPlot().getRangeAxis()).setTickUnit(new NumberTickUnit(getDoubleFromFormattedTextField(jftfY3)));
        getTempObj().getXYPlot().getRangeAxis().setAutoRange(jchbY1.isSelected());
        getTempObj().getXYPlot().getRangeAxis().setAutoTickUnitSelection(jchbY3.isSelected());
        setChangeObj(true);
    }

    //CHECKSTYLE:OFF
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jbtnOk = new javax.swing.JButton();
        jbtnCancel = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();
        jlbX1 = new javax.swing.JLabel();
        jftfX1 = new javax.swing.JFormattedTextField();
        jftfX2 = new javax.swing.JFormattedTextField();
        jftfX3 = new javax.swing.JFormattedTextField();
        jlbX3 = new javax.swing.JLabel();
        jchbX3 = new javax.swing.JCheckBox();
        jchbX1 = new javax.swing.JCheckBox();
        jPanel2 = new javax.swing.JPanel();
        jlbY1 = new javax.swing.JLabel();
        jftfY1 = new javax.swing.JFormattedTextField();
        jftfY2 = new javax.swing.JFormattedTextField();
        jftfY3 = new javax.swing.JFormattedTextField();
        jlbY3 = new javax.swing.JLabel();
        jchbY1 = new javax.swing.JCheckBox();
        jchbY3 = new javax.swing.JCheckBox();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        org.jdesktop.application.ResourceMap resourceMap = org.jdesktop.application.Application.getInstance(ru.gss.pmfsearcher.PMFSearcherApp.class).getContext().getResourceMap(DlgParameterChartEdit.class);
        setTitle(resourceMap.getString("Form.title")); // NOI18N
        setModal(true);
        setName("Form"); // NOI18N
        setResizable(false);

        javax.swing.ActionMap actionMap = org.jdesktop.application.Application.getInstance(ru.gss.pmfsearcher.PMFSearcherApp.class).getContext().getActionMap(DlgParameterChartEdit.class, this);
        jbtnOk.setAction(actionMap.get("acOk")); // NOI18N
        jbtnOk.setName("jbtnOk"); // NOI18N

        jbtnCancel.setAction(actionMap.get("acCancel")); // NOI18N
        jbtnCancel.setName("jbtnCancel"); // NOI18N

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(resourceMap.getString("jPanel1.border.title"))); // NOI18N
        jPanel1.setName("jPanel1"); // NOI18N

        jlbX1.setText(resourceMap.getString("jlbX1.text")); // NOI18N
        jlbX1.setName("jlbX1"); // NOI18N

        jftfX1.setName("jftfX1"); // NOI18N
        jftfX1.setPreferredSize(new java.awt.Dimension(50, 20));

        jftfX2.setName("jftfX2"); // NOI18N
        jftfX2.setPreferredSize(new java.awt.Dimension(50, 20));

        jftfX3.setName("jftfX3"); // NOI18N
        jftfX3.setPreferredSize(new java.awt.Dimension(50, 20));

        jlbX3.setText(resourceMap.getString("jlbX3.text")); // NOI18N
        jlbX3.setName("jlbX3"); // NOI18N

        jchbX3.setText(resourceMap.getString("jchbX3.text")); // NOI18N
        jchbX3.setName("jchbX3"); // NOI18N

        jchbX1.setText(resourceMap.getString("jchbX1.text")); // NOI18N
        jchbX1.setName("jchbX1"); // NOI18N

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jlbX1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jchbX1))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jlbX3, javax.swing.GroupLayout.PREFERRED_SIZE, 87, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jchbX3)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jftfX1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jftfX2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jftfX3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jlbX1)
                    .addComponent(jftfX1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jftfX2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jchbX1))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jlbX3)
                    .addComponent(jchbX3)
                    .addComponent(jftfX3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder(resourceMap.getString("jPanel2.border.title"))); // NOI18N
        jPanel2.setName("jPanel2"); // NOI18N

        jlbY1.setText(resourceMap.getString("jlbY1.text")); // NOI18N
        jlbY1.setName("jlbY1"); // NOI18N

        jftfY1.setName("jftfY1"); // NOI18N
        jftfY1.setPreferredSize(new java.awt.Dimension(50, 20));

        jftfY2.setName("jftfY2"); // NOI18N
        jftfY2.setPreferredSize(new java.awt.Dimension(50, 20));

        jftfY3.setName("jftfY3"); // NOI18N
        jftfY3.setPreferredSize(new java.awt.Dimension(50, 20));

        jlbY3.setText(resourceMap.getString("jlbY3.text")); // NOI18N
        jlbY3.setName("jlbY3"); // NOI18N

        jchbY1.setText(resourceMap.getString("jchbY1.text")); // NOI18N
        jchbY1.setName("jchbY1"); // NOI18N

        jchbY3.setText(resourceMap.getString("jchbY3.text")); // NOI18N
        jchbY3.setName("jchbY3"); // NOI18N

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jlbY1)
                    .addComponent(jlbY3))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jchbY1)
                    .addComponent(jchbY3))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jftfY1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jftfY2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jftfY3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jlbY1)
                    .addComponent(jftfY1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jftfY2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jchbY1))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jlbY3)
                    .addComponent(jftfY3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jchbY3))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(6, 6, 6)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(471, Short.MAX_VALUE)
                .addComponent(jbtnOk, javax.swing.GroupLayout.PREFERRED_SIZE, 74, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jbtnCancel, javax.swing.GroupLayout.PREFERRED_SIZE, 73, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(jPanel2, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jbtnOk)
                    .addComponent(jbtnCancel, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JButton jbtnCancel;
    private javax.swing.JButton jbtnOk;
    private javax.swing.JCheckBox jchbX1;
    private javax.swing.JCheckBox jchbX3;
    private javax.swing.JCheckBox jchbY1;
    private javax.swing.JCheckBox jchbY3;
    private javax.swing.JFormattedTextField jftfX1;
    private javax.swing.JFormattedTextField jftfX2;
    private javax.swing.JFormattedTextField jftfX3;
    private javax.swing.JFormattedTextField jftfY1;
    private javax.swing.JFormattedTextField jftfY2;
    private javax.swing.JFormattedTextField jftfY3;
    private javax.swing.JLabel jlbX1;
    private javax.swing.JLabel jlbX3;
    private javax.swing.JLabel jlbY1;
    private javax.swing.JLabel jlbY3;
    // End of variables declaration//GEN-END:variables
    //CHECKSTYLE:ON
}
