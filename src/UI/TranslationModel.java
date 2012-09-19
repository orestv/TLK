/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package UI;

import javax.swing.table.AbstractTableModel;
import tlkhandler.DataStore;
import tlkhandler.DataStore.Entry;

/**
 *
 * @author ovoloshchuk
 */
public class TranslationModel extends AbstractTableModel {
    private DataStore _dataStore;

    public TranslationModel(DataStore dataStore) {
        _dataStore = dataStore;
    }

    @Override
    public int getRowCount() {
        return _dataStore.size();
    }

    @Override
    public int getColumnCount() {
        return 3;
    }

    @Override
    public Object getValueAt(int row, int col) {
        DataStore.Entry e = _dataStore.get(row);
        if (e == null)
            return null;
        switch(col) {
            case 0:
                return e.getStrref();
            case 1:
                return e.getOriginal();
            case 2:
                return e.getTranslation();
        }
        return null;
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return (columnIndex == 2);
    }

    @Override
    public String getColumnName(int column) {
        switch (column) {
            case 0:
                return "StrRef";
            case 1:
                return "Original";
            case 2:
                return "Translated";
        }
        return null;
    }    

    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
        super.setValueAt(aValue, rowIndex, columnIndex);
        if (columnIndex != 2)
            return;
        int ref = (int)getValueAt(rowIndex, 0);
        Entry entry = _dataStore.getByRef(ref);
        entry.setTranslation((String)aValue);
    }
}
