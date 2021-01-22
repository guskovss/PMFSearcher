/*
 * Pipeline Magnetic Field Searcher
 */
package ru.gss.pmfsearcher;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import org.jdesktop.application.Action;
import org.jdesktop.application.SingleFrameApplication;
import org.jdesktop.application.FrameView;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.ListSelectionModel;
import javax.swing.UIManager;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import org.jdesktop.swingx.decorator.HighlighterFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.ChartUtilities;
import ru.gss.pmfsearcher.chart.ChartMaker;
import ru.gss.pmfsearcher.calculation.DlgReperEdit;
import ru.gss.pmfsearcher.calculation.ReperTableModel;
import ru.gss.pmfsearcher.calculation.DataTableModel;
import ru.gss.pmfsearcher.calculation.DlgParameterEdit;
import ru.gss.pmfsearcher.calculation.DlgSourceEdit;
import ru.gss.pmfsearcher.calculation.SourceTableModel;
import ru.gss.pmfsearcher.chart.DlgParameterChartEdit;
import ru.gss.pmfsearcher.commons.FileChooserFactory;
import ru.gss.pmfsearcher.data.DataList;

/**
 * The main frame of the application.
 * @version 1.1.0 21.01.2021
 * @author Sergey Guskov
 */
public class PMFSearcherView extends FrameView {

    static {
        UIManager.put("JXTable.column.horizontalScroll", "Горизонтальная прокрутка");
        UIManager.put("JXTable.column.packAll", "Упаковка всех столбцов");
        UIManager.put("JXTable.column.packSelected", "Упаковка выбранного столбца");
    }

    /**
     * Constructor.
     * @param app application
     */
    public PMFSearcherView(final SingleFrameApplication app) {
        super(app);
        initComponents();

        //Icon
        //org.jdesktop.application.ResourceMap resourceMap = org.jdesktop.application.Application.getInstance(ru.gss.plsviewer.PMFSearcherApp.class).getContext().getResourceMap(PMFSearcherView.class);
        //getFrame().setIconImage(resourceMap.getImageIcon("mainFrame.icon").getImage());

        //Translate
        UIManager.put("FileChooser.fileNameLabelText", "Имя файла:");
        UIManager.put("FileChooser.lookInLabelText", "Папка:");
        UIManager.put("FileChooser.saveInLabelText", "Папка:");
        UIManager.put("FileChooser.filesOfTypeLabelText", "Тип:");
        UIManager.put("FileChooser.filesOfTypeLabelText", "Фильтр");
        UIManager.put("FileChooser.upFolderToolTipText", "Наверх");
        UIManager.put("FileChooser.homeFolderToolTipText", "Домой");
        UIManager.put("FileChooser.newFolderToolTipText", "Новая папка");
        UIManager.put("FileChooser.listViewButtonToolTipText", "Список");
        UIManager.put("FileChooser.detailsViewButtonToolTipText", "Таблица");
        UIManager.put("FileChooser.saveButtonText", "Сохранить");
        UIManager.put("FileChooser.openButtonText", "Открыть");
        UIManager.put("FileChooser.cancelButtonText", "Отмена");
        UIManager.put("FileChooser.updateButtonText", "Обновить");
        UIManager.put("FileChooser.helpButtonText", "Справка");
        UIManager.put("FileChooser.saveButtonToolTipText", "Сохранить");
        UIManager.put("FileChooser.openButtonToolTipText", "Открыть");
        UIManager.put("FileChooser.cancelButtonToolTipText", "Отмена");
        UIManager.put("FileChooser.updateButtonToolTipText", "Обновить");
        UIManager.put("FileChooser.helpButtonToolTipText", "Справка");
        UIManager.put("FileChooser.openDialogTitleText", "Открыть");
        UIManager.put("FileChooser.saveDialogTitleText", "Сохранить как");
        UIManager.put("ProgressMonitor.progressText", "Загрузка...");
        UIManager.put("OptionPane.cancelButtonText", "Отмена");
        UIManager.put("OptionPane.yesButtonText", "Да");
        UIManager.put("OptionPane.noButtonText", "Нет");
        UIManager.put("OptionPane.messageDialogTitle", "Внимание");

        //Main objects
        data = new DataList();
        chartMaker = new ChartMaker();

        //Settings of source table
        tmSource = new SourceTableModel(data);
        jtSource.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        jtSource.addHighlighter(HighlighterFactory.createSimpleStriping());
        jtSource.setModel(tmSource);
        jtSource.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            public void valueChanged(final ListSelectionEvent e) {
                if (e.getFirstIndex() >= 0) {
                    setSelectSource(true);
                } else {
                    setSelectSource(false);
                }
            }
        });
        jtSource.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(final MouseEvent e) {
                if ((e.getClickCount() == 2) && (e.getButton() == 1) && (jtSource.getSelectedRow() >= 0)) {
                    acEditSource();
                }
            }
        });

        //Settings of data table
        tmData = new DataTableModel(data);
        jtData.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        jtData.addHighlighter(HighlighterFactory.createSimpleStriping());
        jtData.setModel(tmData);

        //Settings of reper table
        tmReper = new ReperTableModel(data);
        jtReper.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        jtReper.addHighlighter(HighlighterFactory.createSimpleStriping());
        jtReper.setModel(tmReper);
        jtReper.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            public void valueChanged(final ListSelectionEvent e) {
                if (e.getFirstIndex() >= 0) {
                    setSelectReper(true);
                } else {
                    setSelectReper(false);
                }
            }
        });
        jtReper.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(final MouseEvent e) {
                if ((e.getClickCount() == 2) && (e.getButton() == 1) && (jtReper.getSelectedRow() >= 0)) {
                    acEditReper();
                }
            }
        });
        chartPanel = new ChartPanel(chartMaker.createChart(data));
        chartPanel.setPopupMenu(jpmChart);
        jpChart.add(chartPanel);
    }

    /**
     * Save log to file.
     */
    @Action
    public void acSaveLogToFile() {
        JFileChooser chooser = FileChooserFactory.getChooser(3);
        if (chooser.showSaveDialog(this.getFrame()) == JFileChooser.APPROVE_OPTION) {
            File f = chooser.getSelectedFile();
            try {
                data.saveTextAreaToFile(f, jtaLog);
                addToLog("Запись сообщений в файл " + f.getAbsolutePath());
            } catch (IOException ex) {
                showErrorMessage(ex);
            }
        }
    }

    /**
     * Text message.
     * @param s message
     */
    private void addToLog(final String s) {
        jtaLog.append(s + "\n");
    }

    /**
     * Select row in source table.
     */
    private boolean selectSource = false;

    /**
     * Select row in source table.
     * @return select row in source table
     */
    public boolean isSelectSource() {
        return selectSource;
    }

    /**
     * Select row in source table.
     * @param b select row in source table
     */
    public void setSelectSource(final boolean b) {
        boolean old = isSelectSource();
        selectSource = b;
        firePropertyChange("selectSource", old, isSelectSource());
    }

     /**
     * Select row in reper table.
     */
    private boolean selectReper = false;

    /**
     * Select row in reper table.
     * @return select row in reper table
     */
    public boolean isSelectReper() {
        return selectReper;
    }

    /**
     * Select row in reper table.
     * @param b select row in reper table
     */
    public void setSelectReper(final boolean b) {
        boolean old = isSelectReper();
        selectReper = b;
        firePropertyChange("selectReper", old, isSelectReper());
    }

    /**
     * Message of number parse exeptions.
     * @param n count of exeptions
     */
    private void showParseExceptionMessage(final int n) {
        JOptionPane.showMessageDialog(this.getFrame(),
                "Количество ошибок при распознавании чисел - " + n,
                "Внимание", JOptionPane.ERROR_MESSAGE);
    }

    /**
     * Message of exeption.
     * @param ex exeption
     */
    public void showErrorMessage(final Exception ex) {
        JOptionPane.showMessageDialog(
                PMFSearcherApp.getApplication().getMainFrame(), ex,
                "Ошибка", JOptionPane.ERROR_MESSAGE);
    }

    /**
     * Chart save.
     */
    @Action
    public void acChartSaveAs() {
        JFileChooser ch = FileChooserFactory.getChooser(5);
        if (ch.showSaveDialog(this.getFrame()) == JFileChooser.APPROVE_OPTION) {
            File f = ch.getSelectedFile();
            try {
                ChartUtilities.saveChartAsPNG(f, chartPanel.getChart(), 700, 500);
            } catch (IOException ex) {
                showErrorMessage(ex);
            }
        }
    }

    /**
     * Chart parameters.
     */
    @Action
    public void acChartParameter() { 
        DlgParameterChartEdit d = new DlgParameterChartEdit();
        d.setTempObj(chartPanel.getChart());
        d.setLocationRelativeTo(this.getFrame());
        d.setVisible(true);
    }
    
    /**
     * Chart repaint.
     */
    @Action
    public void acPlot() {
        chartPanel.setChart(chartMaker.createChart(data));
    }

    /**
     * Open sources from file.
     */
    @Action
    public void acOpenFile() {
        JFileChooser chooser = FileChooserFactory.getChooser(4);
        if (chooser.showOpenDialog(this.getFrame()) == JFileChooser.APPROVE_OPTION) {
            File f = chooser.getSelectedFile();
            try {
                data.loadDataFromFile(f);
                tmSource.fireTableDataChanged();
                addToLog("Открыт файл с данными " + f.getAbsolutePath());
            } catch (FileNotFoundException ex) {
                showErrorMessage(ex);
            } catch (IOException ex) {
                showErrorMessage(ex);
            }
            if (data.getParseExceptionCount() > 0) {
                showParseExceptionMessage(data.getParseExceptionCount());
                return;
            }
        }
    }

    /**
     * Save sources to file.
     */
    @Action
    public void acSaveFile() {
        JFileChooser chooser = FileChooserFactory.getChooser(4);
        if (chooser.showSaveDialog(this.getFrame()) == JFileChooser.APPROVE_OPTION) {
            File f = chooser.getSelectedFile();
            try {
                data.saveDataToFile(f);
                addToLog("Данные сохранены в файл " + f.getAbsolutePath());
            } catch (IOException ex) {
                showErrorMessage(ex);
            }
        }
    }

    /**
     * Edit model parameters.
     */
    @Action
    public void acEditParameter() {
        DlgParameterEdit d = new DlgParameterEdit();
        d.setTempObj(data.getParameter());
        d.setLocationRelativeTo(this.getFrame());
        d.setVisible(true);
    }

    /**
     * Add source.
     */
    @Action
    public void acAddSource() {
        DlgSourceEdit d = new DlgSourceEdit();
        d.setTempObj(d.createTempObj());
        d.setLocationRelativeTo(this.getFrame());
        d.setVisible(true);
        if (d.isChangeObj()) {
            data.getSource().add(d.getTempObj());
            int i = data.getSource().size() - 1;
            tmSource.fireTableRowsInserted(i, i);
            int j = jtSource.convertRowIndexToView(i);
            jtSource.setRowSelectionInterval(j, j);
            jtSource.scrollRectToVisible(jtSource.getCellRect(j, 0, true));
        }
    }

    /**
     * Edit source.
     */
    @Action(enabledProperty = "selectSource")
    public void acEditSource() {
        int i = jtSource.convertRowIndexToModel(jtSource.getSelectedRow());
        DlgSourceEdit d = new DlgSourceEdit();
        d.setTempObj(data.getSource().get(i));
        d.setLocationRelativeTo(this.getFrame());
        d.setVisible(true);
        if (d.isChangeObj()) {
            tmSource.fireTableRowsUpdated(i, i);
            jtSource.getRowSorter().allRowsChanged();
        }
    }

    /**
     * Delete source.
     */
    @Action(enabledProperty = "selectSource")
    public void acDelSource() {
        int i = jtSource.convertRowIndexToModel(jtSource.getSelectedRow());
        data.getSource().remove(i);
        tmSource.fireTableRowsDeleted(i, i);
        setSelectSource(false);
    }

    /**
     * Calculate.
     */
    @Action
    public void acCalculate() {
        data.calculate();
        tmData.fireTableDataChanged();
        acPlot();
    }

    /**
     * Add reper.
     */
    @Action
    public void acAddReper() {
        DlgReperEdit d = new DlgReperEdit();
        d.setTempObj(d.createTempObj());
        d.setLocationRelativeTo(this.getFrame());
        d.setVisible(true);
        if (d.isChangeObj()) {
            data.getReper().add(d.getTempObj());
            int i = data.getReper().size() - 1;
            tmReper.fireTableRowsInserted(i, i);
            int j = jtReper.convertRowIndexToView(i);
            jtReper.setRowSelectionInterval(j, j);
            jtReper.scrollRectToVisible(jtReper.getCellRect(j, 0, true));
        }
    }

    /**
     * Edit reper.
     */
    @Action(enabledProperty = "selectReper")
    public void acEditReper() {
        int i = jtReper.convertRowIndexToModel(jtReper.getSelectedRow());
        DlgReperEdit d = new DlgReperEdit();
        d.setTempObj(data.getReper().get(i));
        d.setLocationRelativeTo(this.getFrame());
        d.setVisible(true);
        if (d.isChangeObj()) {
            tmReper.fireTableRowsUpdated(i, i);
            jtReper.getRowSorter().allRowsChanged();
        }
    }

    /**
     * Delete reper.
     */
    @Action(enabledProperty = "selectReper")
    public void acDelReper() {
        int i = jtReper.convertRowIndexToModel(jtReper.getSelectedRow());
        data.getReper().remove(i);
        tmReper.fireTableRowsDeleted(i, i);
        setSelectReper(false);
    }

    /**
     * Show repers.
     */
    @Action
    public void acShowReper() {
        data.setShowReper(jtbtnShowReper.isSelected());
    }

    /**
     * Action for About button.
     */
    @Action
    public void acShowAboutBox() {
        if (aboutBox == null) {
            aboutBox = new PMFSearcherAboutBox();
        }
        aboutBox.setLocationRelativeTo(this.getFrame());
        aboutBox.setVisible(true);
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

        mainPanel = new javax.swing.JPanel();
        jPanel6 = new javax.swing.JPanel();
        jSplitPane1 = new javax.swing.JSplitPane();
        jPanel2 = new javax.swing.JPanel();
        jpChart = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel7 = new javax.swing.JPanel();
        jSplitPane2 = new javax.swing.JSplitPane();
        jTabbedPane2 = new javax.swing.JTabbedPane();
        jPanel1 = new javax.swing.JPanel();
        jToolBar4 = new javax.swing.JToolBar();
        jbtnCalculation = new javax.swing.JButton();
        jScrollPane3 = new javax.swing.JScrollPane();
        jtData = new org.jdesktop.swingx.JXTable();
        jPanel4 = new javax.swing.JPanel();
        jToolBar5 = new javax.swing.JToolBar();
        jbtnAddReper = new javax.swing.JButton();
        jbtnEditReper = new javax.swing.JButton();
        jbtnDelReper = new javax.swing.JButton();
        jSeparator7 = new javax.swing.JToolBar.Separator();
        jtbtnShowReper = new javax.swing.JToggleButton();
        jScrollPane4 = new javax.swing.JScrollPane();
        jtReper = new org.jdesktop.swingx.JXTable();
        jPanel5 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        jtSource = new org.jdesktop.swingx.JXTable();
        jToolBar2 = new javax.swing.JToolBar();
        jbtnAddSource = new javax.swing.JButton();
        jbtnEditSource = new javax.swing.JButton();
        jbtnDelSource = new javax.swing.JButton();
        jPanel8 = new javax.swing.JPanel();
        jToolBar3 = new javax.swing.JToolBar();
        jbtnSaveLog = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        jtaLog = new javax.swing.JTextArea();
        jToolBar1 = new javax.swing.JToolBar();
        jbtnOpenSource = new javax.swing.JButton();
        jbtnSaveSource = new javax.swing.JButton();
        jSeparator5 = new javax.swing.JToolBar.Separator();
        jbtnParameter = new javax.swing.JButton();
        jSeparator6 = new javax.swing.JToolBar.Separator();
        jbtnRefresh = new javax.swing.JButton();
        menuBar = new javax.swing.JMenuBar();
        javax.swing.JMenu jmFile = new javax.swing.JMenu();
        jmiOpenSource = new javax.swing.JMenuItem();
        jmiSaveSource = new javax.swing.JMenuItem();
        jSeparator1 = new javax.swing.JSeparator();
        jmiAddSource = new javax.swing.JMenuItem();
        jmiEditSource = new javax.swing.JMenuItem();
        jmiDelSource = new javax.swing.JMenuItem();
        jSeparator2 = new javax.swing.JSeparator();
        jmiCalculate = new javax.swing.JMenuItem();
        jSeparator3 = new javax.swing.JSeparator();
        jmiExit = new javax.swing.JMenuItem();
        javax.swing.JMenu jmHelp = new javax.swing.JMenu();
        javax.swing.JMenuItem jmiAbout = new javax.swing.JMenuItem();
        statusPanel = new javax.swing.JPanel();
        statusMessageLabel = new javax.swing.JLabel();
        jpmChart = new javax.swing.JPopupMenu();
        jmiChartSave = new javax.swing.JMenuItem();
        jSeparator4 = new javax.swing.JSeparator();
        jmiChartParameters = new javax.swing.JMenuItem();

        mainPanel.setName("mainPanel"); // NOI18N

        jPanel6.setName("jPanel6"); // NOI18N

        jSplitPane1.setBorder(null);
        jSplitPane1.setDividerLocation(400);
        jSplitPane1.setDividerSize(3);
        jSplitPane1.setName("jSplitPane1"); // NOI18N

        jPanel2.setMinimumSize(new java.awt.Dimension(600, 290));
        jPanel2.setName("jPanel2"); // NOI18N
        jPanel2.setPreferredSize(new java.awt.Dimension(762, 290));

        jpChart.setName("jpChart"); // NOI18N
        jpChart.setLayout(new javax.swing.BoxLayout(jpChart, javax.swing.BoxLayout.LINE_AXIS));

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jpChart, javax.swing.GroupLayout.DEFAULT_SIZE, 636, Short.MAX_VALUE)
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jpChart, javax.swing.GroupLayout.DEFAULT_SIZE, 598, Short.MAX_VALUE)
        );

        jSplitPane1.setRightComponent(jPanel2);

        jPanel3.setName("jPanel3"); // NOI18N
        jPanel3.setPreferredSize(new java.awt.Dimension(626, 210));
        jPanel3.setRequestFocusEnabled(false);

        jTabbedPane1.setTabLayoutPolicy(javax.swing.JTabbedPane.SCROLL_TAB_LAYOUT);
        jTabbedPane1.setMinimumSize(new java.awt.Dimension(290, 0));
        jTabbedPane1.setName("jTabbedPane1"); // NOI18N

        jPanel7.setName("jPanel7"); // NOI18N

        jSplitPane2.setBorder(null);
        jSplitPane2.setDividerLocation(250);
        jSplitPane2.setDividerSize(3);
        jSplitPane2.setOrientation(javax.swing.JSplitPane.VERTICAL_SPLIT);
        jSplitPane2.setMinimumSize(new java.awt.Dimension(200, 226));
        jSplitPane2.setName("jSplitPane2"); // NOI18N

        jTabbedPane2.setTabLayoutPolicy(javax.swing.JTabbedPane.SCROLL_TAB_LAYOUT);
        jTabbedPane2.setMinimumSize(new java.awt.Dimension(0, 220));
        jTabbedPane2.setName("jTabbedPane2"); // NOI18N

        jPanel1.setName("jPanel1"); // NOI18N
        jPanel1.setPreferredSize(new java.awt.Dimension(390, 248));

        jToolBar4.setBorder(javax.swing.BorderFactory.createEmptyBorder(5, 5, 0, 0));
        jToolBar4.setFloatable(false);
        jToolBar4.setRollover(true);
        jToolBar4.setMaximumSize(new java.awt.Dimension(314, 36));
        jToolBar4.setMinimumSize(new java.awt.Dimension(228, 36));
        jToolBar4.setName("jToolBar4"); // NOI18N
        jToolBar4.setPreferredSize(new java.awt.Dimension(100, 36));

        javax.swing.ActionMap actionMap = org.jdesktop.application.Application.getInstance(ru.gss.pmfsearcher.PMFSearcherApp.class).getContext().getActionMap(PMFSearcherView.class, this);
        jbtnCalculation.setAction(actionMap.get("acCalculate")); // NOI18N
        org.jdesktop.application.ResourceMap resourceMap = org.jdesktop.application.Application.getInstance(ru.gss.pmfsearcher.PMFSearcherApp.class).getContext().getResourceMap(PMFSearcherView.class);
        jbtnCalculation.setIcon(resourceMap.getIcon("jbtnCalculation.icon")); // NOI18N
        jbtnCalculation.setToolTipText(resourceMap.getString("jbtnCalculation.toolTipText")); // NOI18N
        jbtnCalculation.setDisabledIcon(resourceMap.getIcon("jbtnCalculation.disabledIcon")); // NOI18N
        jbtnCalculation.setFocusable(false);
        jbtnCalculation.setHideActionText(true);
        jbtnCalculation.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jbtnCalculation.setName("jbtnCalculation"); // NOI18N
        jbtnCalculation.setRolloverIcon(resourceMap.getIcon("jbtnCalculation.rolloverIcon")); // NOI18N
        jbtnCalculation.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jToolBar4.add(jbtnCalculation);

        jScrollPane3.setName("jScrollPane3"); // NOI18N

        jtData.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jtData.setColumnControlVisible(true);
        jtData.setName("jtData"); // NOI18N
        jtData.setSortable(false);
        jScrollPane3.setViewportView(jtData);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jToolBar4, javax.swing.GroupLayout.DEFAULT_SIZE, 390, Short.MAX_VALUE)
            .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 390, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jToolBar4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 205, Short.MAX_VALUE))
        );

        jTabbedPane2.addTab(resourceMap.getString("jPanel1.TabConstraints.tabTitle"), jPanel1); // NOI18N

        jPanel4.setName("jPanel4"); // NOI18N

        jToolBar5.setBorder(javax.swing.BorderFactory.createEmptyBorder(5, 5, 0, 0));
        jToolBar5.setFloatable(false);
        jToolBar5.setRollover(true);
        jToolBar5.setMaximumSize(new java.awt.Dimension(314, 36));
        jToolBar5.setMinimumSize(new java.awt.Dimension(228, 36));
        jToolBar5.setName("jToolBar5"); // NOI18N
        jToolBar5.setPreferredSize(new java.awt.Dimension(100, 36));

        jbtnAddReper.setAction(actionMap.get("acAddReper")); // NOI18N
        jbtnAddReper.setIcon(resourceMap.getIcon("jbtnAddReper.icon")); // NOI18N
        jbtnAddReper.setDisabledIcon(resourceMap.getIcon("jbtnAddReper.disabledIcon")); // NOI18N
        jbtnAddReper.setFocusable(false);
        jbtnAddReper.setHideActionText(true);
        jbtnAddReper.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jbtnAddReper.setName("jbtnAddReper"); // NOI18N
        jbtnAddReper.setRolloverIcon(resourceMap.getIcon("jbtnAddReper.rolloverIcon")); // NOI18N
        jbtnAddReper.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jToolBar5.add(jbtnAddReper);

        jbtnEditReper.setAction(actionMap.get("acEditReper")); // NOI18N
        jbtnEditReper.setIcon(resourceMap.getIcon("jbtnEditReper.icon")); // NOI18N
        jbtnEditReper.setDisabledIcon(resourceMap.getIcon("jbtnEditReper.disabledIcon")); // NOI18N
        jbtnEditReper.setFocusable(false);
        jbtnEditReper.setHideActionText(true);
        jbtnEditReper.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jbtnEditReper.setName("jbtnEditReper"); // NOI18N
        jbtnEditReper.setRolloverIcon(resourceMap.getIcon("jbtnEditReper.rolloverIcon")); // NOI18N
        jbtnEditReper.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jToolBar5.add(jbtnEditReper);

        jbtnDelReper.setAction(actionMap.get("acDelReper")); // NOI18N
        jbtnDelReper.setIcon(resourceMap.getIcon("jbtnDelReper.icon")); // NOI18N
        jbtnDelReper.setDisabledIcon(resourceMap.getIcon("jbtnDelReper.disabledIcon")); // NOI18N
        jbtnDelReper.setFocusable(false);
        jbtnDelReper.setHideActionText(true);
        jbtnDelReper.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jbtnDelReper.setName("jbtnDelReper"); // NOI18N
        jbtnDelReper.setRolloverIcon(resourceMap.getIcon("jbtnDelReper.rolloverIcon")); // NOI18N
        jbtnDelReper.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jToolBar5.add(jbtnDelReper);

        jSeparator7.setName("jSeparator7"); // NOI18N
        jToolBar5.add(jSeparator7);

        jtbtnShowReper.setAction(actionMap.get("acShowReper")); // NOI18N
        jtbtnShowReper.setIcon(resourceMap.getIcon("jtbtnShowReper.icon")); // NOI18N
        jtbtnShowReper.setDisabledIcon(resourceMap.getIcon("jtbtnShowReper.disabledIcon")); // NOI18N
        jtbtnShowReper.setFocusable(false);
        jtbtnShowReper.setHideActionText(true);
        jtbtnShowReper.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jtbtnShowReper.setName("jtbtnShowReper"); // NOI18N
        jtbtnShowReper.setRolloverIcon(resourceMap.getIcon("jtbtnShowReper.rolloverIcon")); // NOI18N
        jtbtnShowReper.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jToolBar5.add(jtbtnShowReper);

        jScrollPane4.setName("jScrollPane4"); // NOI18N

        jtReper.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jtReper.setColumnControlVisible(true);
        jtReper.setName("jtReper"); // NOI18N
        jtReper.setSortable(false);
        jScrollPane4.setViewportView(jtReper);

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jToolBar5, javax.swing.GroupLayout.DEFAULT_SIZE, 390, Short.MAX_VALUE)
            .addComponent(jScrollPane4, javax.swing.GroupLayout.DEFAULT_SIZE, 390, Short.MAX_VALUE)
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addComponent(jToolBar5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane4, javax.swing.GroupLayout.DEFAULT_SIZE, 205, Short.MAX_VALUE))
        );

        jTabbedPane2.addTab(resourceMap.getString("jPanel4.TabConstraints.tabTitle"), jPanel4); // NOI18N

        jSplitPane2.setRightComponent(jTabbedPane2);

        jPanel5.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        jPanel5.setMinimumSize(new java.awt.Dimension(0, 200));
        jPanel5.setName("jPanel5"); // NOI18N
        jPanel5.setPreferredSize(new java.awt.Dimension(250, 128));

        jScrollPane2.setName("jScrollPane2"); // NOI18N

        jtSource.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jtSource.setColumnControlVisible(true);
        jtSource.setName("jtSource"); // NOI18N
        jtSource.setSortable(false);
        jScrollPane2.setViewportView(jtSource);

        jToolBar2.setBorder(javax.swing.BorderFactory.createEmptyBorder(5, 5, 0, 0));
        jToolBar2.setFloatable(false);
        jToolBar2.setRollover(true);
        jToolBar2.setMaximumSize(new java.awt.Dimension(314, 36));
        jToolBar2.setMinimumSize(new java.awt.Dimension(228, 36));
        jToolBar2.setName("jToolBar2"); // NOI18N
        jToolBar2.setPreferredSize(new java.awt.Dimension(100, 36));

        jbtnAddSource.setAction(actionMap.get("acAddSource")); // NOI18N
        jbtnAddSource.setIcon(resourceMap.getIcon("jbtnAddSource.icon")); // NOI18N
        jbtnAddSource.setFocusable(false);
        jbtnAddSource.setHideActionText(true);
        jbtnAddSource.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jbtnAddSource.setName("jbtnAddSource"); // NOI18N
        jbtnAddSource.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jToolBar2.add(jbtnAddSource);

        jbtnEditSource.setAction(actionMap.get("acEditSource")); // NOI18N
        jbtnEditSource.setIcon(resourceMap.getIcon("jbtnEditSource.icon")); // NOI18N
        jbtnEditSource.setDisabledIcon(resourceMap.getIcon("jbtnEditSource.disabledIcon")); // NOI18N
        jbtnEditSource.setFocusable(false);
        jbtnEditSource.setHideActionText(true);
        jbtnEditSource.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jbtnEditSource.setName("jbtnEditSource"); // NOI18N
        jbtnEditSource.setRolloverIcon(resourceMap.getIcon("jbtnEditSource.rolloverIcon")); // NOI18N
        jbtnEditSource.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jToolBar2.add(jbtnEditSource);

        jbtnDelSource.setAction(actionMap.get("acDelSource")); // NOI18N
        jbtnDelSource.setIcon(resourceMap.getIcon("jbtnDelSource.icon")); // NOI18N
        jbtnDelSource.setDisabledIcon(resourceMap.getIcon("jbtnDelSource.disabledIcon")); // NOI18N
        jbtnDelSource.setFocusable(false);
        jbtnDelSource.setHideActionText(true);
        jbtnDelSource.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jbtnDelSource.setName("jbtnDelSource"); // NOI18N
        jbtnDelSource.setRolloverIcon(resourceMap.getIcon("jbtnDelSource.rolloverIcon")); // NOI18N
        jbtnDelSource.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jToolBar2.add(jbtnDelSource);

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jToolBar2, javax.swing.GroupLayout.DEFAULT_SIZE, 395, Short.MAX_VALUE)
            .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 395, Short.MAX_VALUE)
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addComponent(jToolBar2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 208, Short.MAX_VALUE))
        );

        jSplitPane2.setLeftComponent(jPanel5);

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jSplitPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 395, Short.MAX_VALUE)
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jSplitPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 528, Short.MAX_VALUE)
        );

        jTabbedPane1.addTab(resourceMap.getString("jPanel7.TabConstraints.tabTitle"), jPanel7); // NOI18N

        jPanel8.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        jPanel8.setName("jPanel8"); // NOI18N

        jToolBar3.setBorder(javax.swing.BorderFactory.createEmptyBorder(5, 5, 0, 0));
        jToolBar3.setFloatable(false);
        jToolBar3.setRollover(true);
        jToolBar3.setMaximumSize(new java.awt.Dimension(314, 36));
        jToolBar3.setMinimumSize(new java.awt.Dimension(228, 36));
        jToolBar3.setName("jToolBar3"); // NOI18N
        jToolBar3.setPreferredSize(new java.awt.Dimension(100, 36));

        jbtnSaveLog.setAction(actionMap.get("acSaveLogToFile")); // NOI18N
        jbtnSaveLog.setIcon(resourceMap.getIcon("jbtnSaveLog.icon")); // NOI18N
        jbtnSaveLog.setDisabledIcon(resourceMap.getIcon("jbtnSaveLog.disabledIcon")); // NOI18N
        jbtnSaveLog.setFocusable(false);
        jbtnSaveLog.setHideActionText(true);
        jbtnSaveLog.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jbtnSaveLog.setName("jbtnSaveLog"); // NOI18N
        jbtnSaveLog.setRolloverIcon(resourceMap.getIcon("jbtnSaveLog.rolloverIcon")); // NOI18N
        jbtnSaveLog.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jToolBar3.add(jbtnSaveLog);

        jScrollPane1.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
        jScrollPane1.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        jScrollPane1.setName("jScrollPane1"); // NOI18N

        jtaLog.setColumns(20);
        jtaLog.setEditable(false);
        jtaLog.setFont(resourceMap.getFont("jtaLog.font")); // NOI18N
        jtaLog.setRows(5);
        jtaLog.setWrapStyleWord(true);
        jtaLog.setName("jtaLog"); // NOI18N
        jScrollPane1.setViewportView(jtaLog);

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jToolBar3, javax.swing.GroupLayout.DEFAULT_SIZE, 395, Short.MAX_VALUE)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 395, Short.MAX_VALUE)
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addComponent(jToolBar3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 486, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab(resourceMap.getString("jPanel8.TabConstraints.tabTitle"), jPanel8); // NOI18N

        jToolBar1.setBorder(javax.swing.BorderFactory.createEmptyBorder(5, 5, 0, 0));
        jToolBar1.setFloatable(false);
        jToolBar1.setRollover(true);
        jToolBar1.setMaximumSize(new java.awt.Dimension(314, 36));
        jToolBar1.setMinimumSize(new java.awt.Dimension(228, 36));
        jToolBar1.setName("jToolBar1"); // NOI18N
        jToolBar1.setPreferredSize(new java.awt.Dimension(100, 36));

        jbtnOpenSource.setAction(actionMap.get("acOpenFile")); // NOI18N
        jbtnOpenSource.setIcon(resourceMap.getIcon("jbtnOpenSource.icon")); // NOI18N
        jbtnOpenSource.setDisabledIcon(resourceMap.getIcon("jbtnOpenSource.disabledIcon")); // NOI18N
        jbtnOpenSource.setFocusable(false);
        jbtnOpenSource.setHideActionText(true);
        jbtnOpenSource.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jbtnOpenSource.setName("jbtnOpenSource"); // NOI18N
        jbtnOpenSource.setRolloverIcon(resourceMap.getIcon("jbtnOpenSource.rolloverIcon")); // NOI18N
        jbtnOpenSource.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jToolBar1.add(jbtnOpenSource);

        jbtnSaveSource.setAction(actionMap.get("acSaveFile")); // NOI18N
        jbtnSaveSource.setIcon(resourceMap.getIcon("jbtnSaveSource.icon")); // NOI18N
        jbtnSaveSource.setDisabledIcon(resourceMap.getIcon("jbtnSaveSource.disabledIcon")); // NOI18N
        jbtnSaveSource.setFocusable(false);
        jbtnSaveSource.setHideActionText(true);
        jbtnSaveSource.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jbtnSaveSource.setName("jbtnSaveSource"); // NOI18N
        jbtnSaveSource.setRolloverIcon(resourceMap.getIcon("jbtnSaveSource.rolloverIcon")); // NOI18N
        jbtnSaveSource.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jToolBar1.add(jbtnSaveSource);

        jSeparator5.setName("jSeparator5"); // NOI18N
        jToolBar1.add(jSeparator5);

        jbtnParameter.setAction(actionMap.get("acEditParameter")); // NOI18N
        jbtnParameter.setIcon(resourceMap.getIcon("jbtnParameter.icon")); // NOI18N
        jbtnParameter.setText(resourceMap.getString("jbtnParameter.text")); // NOI18N
        jbtnParameter.setDisabledIcon(resourceMap.getIcon("jbtnParameter.disabledIcon")); // NOI18N
        jbtnParameter.setFocusable(false);
        jbtnParameter.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jbtnParameter.setName("jbtnParameter"); // NOI18N
        jbtnParameter.setRolloverIcon(resourceMap.getIcon("jbtnParameter.rolloverIcon")); // NOI18N
        jbtnParameter.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jToolBar1.add(jbtnParameter);

        jSeparator6.setName("jSeparator6"); // NOI18N
        jToolBar1.add(jSeparator6);

        jbtnRefresh.setAction(actionMap.get("acPlot")); // NOI18N
        jbtnRefresh.setIcon(resourceMap.getIcon("jbtnRefresh.icon")); // NOI18N
        jbtnRefresh.setDisabledIcon(resourceMap.getIcon("jbtnRefresh.disabledIcon")); // NOI18N
        jbtnRefresh.setFocusable(false);
        jbtnRefresh.setHideActionText(true);
        jbtnRefresh.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jbtnRefresh.setName("jbtnRefresh"); // NOI18N
        jbtnRefresh.setRolloverIcon(resourceMap.getIcon("jbtnRefresh.rolloverIcon")); // NOI18N
        jbtnRefresh.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jToolBar1.add(jbtnRefresh);

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jTabbedPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 400, Short.MAX_VALUE)
            .addComponent(jToolBar1, javax.swing.GroupLayout.DEFAULT_SIZE, 400, Short.MAX_VALUE)
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addComponent(jToolBar1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTabbedPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 556, Short.MAX_VALUE))
        );

        jSplitPane1.setLeftComponent(jPanel3);

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jSplitPane1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 1039, Short.MAX_VALUE)
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jSplitPane1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 598, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout mainPanelLayout = new javax.swing.GroupLayout(mainPanel);
        mainPanel.setLayout(mainPanelLayout);
        mainPanelLayout.setHorizontalGroup(
            mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        mainPanelLayout.setVerticalGroup(
            mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        menuBar.setName("menuBar"); // NOI18N

        jmFile.setText(resourceMap.getString("jmFile.text")); // NOI18N
        jmFile.setName("jmFile"); // NOI18N

        jmiOpenSource.setAction(actionMap.get("acOpenFile")); // NOI18N
        jmiOpenSource.setName("jmiOpenSource"); // NOI18N
        jmFile.add(jmiOpenSource);

        jmiSaveSource.setAction(actionMap.get("acSaveFile")); // NOI18N
        jmiSaveSource.setName("jmiSaveSource"); // NOI18N
        jmFile.add(jmiSaveSource);

        jSeparator1.setName("jSeparator1"); // NOI18N
        jmFile.add(jSeparator1);

        jmiAddSource.setAction(actionMap.get("acAddSource")); // NOI18N
        jmiAddSource.setText(resourceMap.getString("jmiAddSource.text")); // NOI18N
        jmiAddSource.setToolTipText(resourceMap.getString("jmiAddSource.toolTipText")); // NOI18N
        jmiAddSource.setName("jmiAddSource"); // NOI18N
        jmFile.add(jmiAddSource);

        jmiEditSource.setAction(actionMap.get("acEditSource")); // NOI18N
        jmiEditSource.setText(resourceMap.getString("jmiEditSource.text")); // NOI18N
        jmiEditSource.setToolTipText(resourceMap.getString("jmiEditSource.toolTipText")); // NOI18N
        jmiEditSource.setName("jmiEditSource"); // NOI18N
        jmFile.add(jmiEditSource);

        jmiDelSource.setAction(actionMap.get("acDelSource")); // NOI18N
        jmiDelSource.setText(resourceMap.getString("jmiDelSource.text")); // NOI18N
        jmiDelSource.setToolTipText(resourceMap.getString("jmiDelSource.toolTipText")); // NOI18N
        jmiDelSource.setName("jmiDelSource"); // NOI18N
        jmFile.add(jmiDelSource);

        jSeparator2.setName("jSeparator2"); // NOI18N
        jmFile.add(jSeparator2);

        jmiCalculate.setAction(actionMap.get("acCalculate")); // NOI18N
        jmiCalculate.setToolTipText(resourceMap.getString("jmiCalculate.toolTipText")); // NOI18N
        jmiCalculate.setName("jmiCalculate"); // NOI18N
        jmFile.add(jmiCalculate);

        jSeparator3.setName("jSeparator3"); // NOI18N
        jmFile.add(jSeparator3);

        jmiExit.setAction(actionMap.get("quit")); // NOI18N
        jmiExit.setName("jmiExit"); // NOI18N
        jmFile.add(jmiExit);

        menuBar.add(jmFile);

        jmHelp.setText(resourceMap.getString("jmHelp.text")); // NOI18N
        jmHelp.setName("jmHelp"); // NOI18N

        jmiAbout.setAction(actionMap.get("acShowAboutBox")); // NOI18N
        jmiAbout.setName("jmiAbout"); // NOI18N
        jmHelp.add(jmiAbout);

        menuBar.add(jmHelp);

        statusPanel.setMaximumSize(new java.awt.Dimension(32767, 20));
        statusPanel.setName("statusPanel"); // NOI18N

        statusMessageLabel.setMinimumSize(new java.awt.Dimension(20, 20));
        statusMessageLabel.setName("statusMessageLabel"); // NOI18N

        javax.swing.GroupLayout statusPanelLayout = new javax.swing.GroupLayout(statusPanel);
        statusPanel.setLayout(statusPanelLayout);
        statusPanelLayout.setHorizontalGroup(
            statusPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(statusPanelLayout.createSequentialGroup()
                .addComponent(statusMessageLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 287, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(752, Short.MAX_VALUE))
        );
        statusPanelLayout.setVerticalGroup(
            statusPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(statusMessageLabel, javax.swing.GroupLayout.DEFAULT_SIZE, 21, Short.MAX_VALUE)
        );

        jpmChart.setName("jpmChart"); // NOI18N

        jmiChartSave.setAction(actionMap.get("acChartSaveAs")); // NOI18N
        jmiChartSave.setName("jmiChartSave"); // NOI18N
        jpmChart.add(jmiChartSave);

        jSeparator4.setName("jSeparator4"); // NOI18N
        jpmChart.add(jSeparator4);

        jmiChartParameters.setAction(actionMap.get("acChartParameter")); // NOI18N
        jmiChartParameters.setName("jmiChartParameters"); // NOI18N
        jpmChart.add(jmiChartParameters);

        setComponent(mainPanel);
        setMenuBar(menuBar);
        setStatusBar(statusPanel);
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JSeparator jSeparator3;
    private javax.swing.JSeparator jSeparator4;
    private javax.swing.JToolBar.Separator jSeparator5;
    private javax.swing.JToolBar.Separator jSeparator6;
    private javax.swing.JToolBar.Separator jSeparator7;
    private javax.swing.JSplitPane jSplitPane1;
    private javax.swing.JSplitPane jSplitPane2;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JTabbedPane jTabbedPane2;
    private javax.swing.JToolBar jToolBar1;
    private javax.swing.JToolBar jToolBar2;
    private javax.swing.JToolBar jToolBar3;
    private javax.swing.JToolBar jToolBar4;
    private javax.swing.JToolBar jToolBar5;
    private javax.swing.JButton jbtnAddReper;
    private javax.swing.JButton jbtnAddSource;
    private javax.swing.JButton jbtnCalculation;
    private javax.swing.JButton jbtnDelReper;
    private javax.swing.JButton jbtnDelSource;
    private javax.swing.JButton jbtnEditReper;
    private javax.swing.JButton jbtnEditSource;
    private javax.swing.JButton jbtnOpenSource;
    private javax.swing.JButton jbtnParameter;
    private javax.swing.JButton jbtnRefresh;
    private javax.swing.JButton jbtnSaveLog;
    private javax.swing.JButton jbtnSaveSource;
    private javax.swing.JMenuItem jmiAddSource;
    private javax.swing.JMenuItem jmiCalculate;
    private javax.swing.JMenuItem jmiChartParameters;
    private javax.swing.JMenuItem jmiChartSave;
    private javax.swing.JMenuItem jmiDelSource;
    private javax.swing.JMenuItem jmiEditSource;
    private javax.swing.JMenuItem jmiExit;
    private javax.swing.JMenuItem jmiOpenSource;
    private javax.swing.JMenuItem jmiSaveSource;
    private javax.swing.JPanel jpChart;
    private javax.swing.JPopupMenu jpmChart;
    private org.jdesktop.swingx.JXTable jtData;
    private org.jdesktop.swingx.JXTable jtReper;
    private org.jdesktop.swingx.JXTable jtSource;
    private javax.swing.JTextArea jtaLog;
    private javax.swing.JToggleButton jtbtnShowReper;
    private javax.swing.JPanel mainPanel;
    private javax.swing.JMenuBar menuBar;
    private javax.swing.JLabel statusMessageLabel;
    private javax.swing.JPanel statusPanel;
    // End of variables declaration//GEN-END:variables
    private JDialog aboutBox;
    private DataTableModel tmData;
    private SourceTableModel tmSource;
    private ReperTableModel tmReper;
    private DataList data;
    private ChartMaker chartMaker;
    private ChartPanel chartPanel;
    //CHECKSTYLE:ON
}
