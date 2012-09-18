/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package UI;

import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;
import tlkhandler.TLK;

/**
 *
 * @author seth
 */
public class TranslationTable extends JTable {    
    public TranslationTable(TLK tlk) {
        this.setModel(new TranslationModel(tlk));
        this.setEditingColumn(0);
    }
    public class TranslationModel extends AbstractTableModel {
        private TLK _tlk;
        
        public TranslationModel(TLK tlk) {
            _tlk = tlk;
        }
        
        @Override
        public int getRowCount() {
            return 5;
        }

        @Override
        public int getColumnCount() {
            return 2;
        }

        @Override
        public Object getValueAt(int row, int col) {
            return row * col;
        }

        @Override
        public boolean isCellEditable(int rowIndex, int columnIndex) {
            return true;
        }
        
    }
    
}
