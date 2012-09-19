/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package UI;

import javax.swing.table.AbstractTableModel;
import tlkhandler.TLK;
import tlkhandler.TLK.Entry;

/**
 *
 * @author ovoloshchuk
 */
public class TranslationModel extends AbstractTableModel {
    private TLK _tlk;

    public TranslationModel(TLK tlk) {
        _tlk = tlk;
    }

    @Override
    public int getRowCount() {
        return _tlk == null ? 0 : _tlk.getEntriesCount();
    }

    @Override
    public int getColumnCount() {
        return 3;
    }

    @Override
    public Object getValueAt(int row, int col) {
        Entry d = _tlk.getEntry(row);
        if (d == null)
            return null;
        switch(col) {
            case 0:
                return d.getStrRef();
            case 1:
                return d.getValue();
            case 2:
                return d.getTranslation();
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
}
