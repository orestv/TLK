/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package UI;

import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Toolkit;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import net.miginfocom.swing.MigLayout;
import tlkhandler.DataStore;
import tlkhandler.TLK;

/**
 *
 * @author seth
 */
public class MainFrame extends JFrame{
    private JPanel _mainPanel;
    private final DataStore _ds = new DataStore();
    
    public MainFrame() {
        init();
        positionAtCenter();
        addWindowListener(new WindowAdapter() {

            @Override
            public void windowClosing(WindowEvent e) {
                super.windowClosing(e);
                try {
                    TLK.save(_ds, "D:\\tlk\\dialog_generated.tlk");
                } catch (IOException ex) {
                    Logger.getLogger(MainFrame.class.getName()).log(Level.SEVERE, null, ex);
                }
            }

        });
    }
    
    private void init() {
        _mainPanel = new JPanel(new MigLayout("debug", 
                "[15%][60%][15%]", //column
                "[90%][10%]"  //row
                ));        
        try {
            TLK.load(_ds, "D:\\tlk\\dialog_natalias_1251-1300_short.tlk");
        } catch (IOException ex) {
            Logger.getLogger(MainFrame.class.getName()).log(Level.SEVERE, null, ex);
        }
        TranslationTable table = new TranslationTable();
        table.setModel(new TranslationModel(_ds));
        JScrollPane scrollPane = new JScrollPane(table);
        _mainPanel.add(scrollPane, "cell 1 0, grow, span 1 2");
        
        this.add(_mainPanel);
        this.setSize(800, 600);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
    }
    private void positionAtCenter() {
        Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
        int x = (int) ((d.getWidth() - this.getWidth())/2);
        int y = (int) ((d.getHeight() - this.getHeight())/2);
        this.setLocation(x, y);
    }
}
