/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package UI;

import javax.swing.JTable;
import javax.swing.event.TableColumnModelEvent;

/**
 *
 * @author seth
 */
public class TranslationTable extends JTable {    
    public TranslationTable() {        
    }

    @Override
    public void columnAdded(TableColumnModelEvent e) {
        super.columnAdded(e);
        getColumnModel().getColumn(0).setMaxWidth(150);
        getColumnModel().getColumn(0).setPreferredWidth(50);
    }
    
}
