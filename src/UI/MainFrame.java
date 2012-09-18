/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package UI;

import java.awt.Dimension;
import java.awt.Toolkit;
import javax.swing.JFrame;
import javax.swing.JPanel;
import net.miginfocom.swing.MigLayout;

/**
 *
 * @author seth
 */
public class MainFrame extends JFrame{
    private JPanel _mainPanel;
    
    public MainFrame() {
        init();
        positionAtCenter();
    }
    
    private void init() {
        _mainPanel = new JPanel(new MigLayout("debug", 
                "[5cm][15cm][5cm]", //column
                ""  //row
                ));
        
        TranslationTable table = new TranslationTable();
        _mainPanel.add(table, "cell 1 0, growx");
        
        this.add(_mainPanel);
        this.pack();        
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
    }
    private void positionAtCenter() {
        Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
        int x = (int) ((d.getWidth() - this.getWidth())/2);
        int y = (int) ((d.getHeight() - this.getHeight())/2);
        this.setLocation(x, y);
    }
}
