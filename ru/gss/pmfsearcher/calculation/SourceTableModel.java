/*
 * Pipeline Magnetic Field Searcher
 */
package ru.gss.pmfsearcher.calculation;

import java.util.Locale;
import javax.swing.table.AbstractTableModel;
import ru.gss.pmfsearcher.data.DataList;
import ru.gss.pmfsearcher.data.Source;

/**
 * Model of sections table.
 * @version 1.1.0 21.01.2021
 * @author Sergey Guskov
 */
public class SourceTableModel extends AbstractTableModel {

    /**
     * Data.
     */
    private DataList data;
    /**
     * Headers of table columns.
     */
    private String[] colNames = {"y, м", "Ix, А/м", "Iy, А/м", "Iz, А/м"};
 
    /**
     * Constructor.
     * @param aData data
     */
    public SourceTableModel(final DataList aData) {
        data = aData;
    }

    /**
     * Header of table column.
     * @param column index of table column
     * @return header of table column
     */
    @Override
    public String getColumnName(final int column) {
        return colNames[column];
    }

    /**
     * Count of table column.
     * @return count of table column
     */
    public int getColumnCount() {
        return 4;
    }

    /**
     * Count of table row.
     * @return count of table row
     */
    public int getRowCount() {
        return data.getSource().size();
    }

    /**
     * Class of table column.
     * @param columnIndex index of table column
     * @return class of table column
     */
    @Override
    public Class < ? > getColumnClass(final int columnIndex) {
        return String.class;
    }

    /**
     * Convertation number to string.
     * @param value number
     * @param format count of symbols after separator
     * @return string representation of number
     */
    private String convertToString(final Double value, final int format) {
        if (value == null) {
            return "";
        }
        return String.format(Locale.US, "%." + format + "f", value);
    }

    /**
     * Value of table cell.
     * @param rowIndex index of table row
     * @param columnIndex index of table column
     * @return value of table cell
     */
    public Object getValueAt(final int rowIndex, final int columnIndex) {
        Source o = data.getSource().get(rowIndex);
        switch (columnIndex) {
            case 0:
                return convertToString(o.getCoordLinear(), 0);
            case 1:
                return convertToString(o.getMagnetization().getX(), 0);
            case 2:
                return convertToString(o.getMagnetization().getY(), 0);
            case 3:
                return convertToString(o.getMagnetization().getZ(), 0);
            default:
                return null;
        }
    }
}
