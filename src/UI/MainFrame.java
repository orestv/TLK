/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package UI;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.filechooser.FileNameExtensionFilter;
import net.miginfocom.swing.MigLayout;
import tlkhandler.DataStore;
import tlkhandler.TLK;

/**
 *
 * @author seth
 */
public class MainFrame extends JFrame implements ActionListener{
    private JPanel _mainPanel;
    private final TranslationTable _translationTable = new TranslationTable();
    private final DataStore _ds = new DataStore();
    private final static FileNameExtensionFilter FilterTLK = new FileNameExtensionFilter("NeverWinter Nights Dialog Files", "tlk");
    private final static FileNameExtensionFilter FilterTDS = new FileNameExtensionFilter("NeverWinter Nights Translation File", "tds");

    private File _currentFile = null;
    
    public MainFrame() {
        init();
        initMenu();
        positionAtCenter();
    }

    private void init() {
        _mainPanel = new JPanel(new MigLayout("", 
                "[15%][60%][15%]", //column
                "[90%][10%]"  //row
                ));                
        JScrollPane scrollPane = new JScrollPane(_translationTable);
        _mainPanel.add(scrollPane, "cell 1 0, grow, span 1 2");
        
        this.add(_mainPanel);
        this.setSize(800, 600);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
    }
    private void initMenu() {        
        JMenu file = new JMenu("File");
        file.setMnemonic('F');
        final MenuItemInfo[] menuItems = new MenuItemInfo[] {
            new MenuItemInfo("Open", 'O', file),
            new MenuItemInfo("Import", 'I', file),
            new MenuItemInfo("Save", 'S', file),
            new MenuItemInfo("Save As", file)
        };

        for (MenuItemInfo info : menuItems) {
            JMenuItem item = new JMenuItem(info.getName(), info.getMnemonic());
            item.addActionListener(this);
            info.getParent().add(item);
        }
        
        JMenuBar bar = new JMenuBar();
        bar.add(file);
        setJMenuBar(bar);
    }
    private void positionAtCenter() {
        Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
        int x = (int) ((d.getWidth() - this.getWidth())/2);
        int y = (int) ((d.getHeight() - this.getHeight())/2);
        this.setLocation(x, y);
    }

    private void close() {
        _ds.clear();
    }
    private void importTLK(File inputFile) {
        close();
        try {
            TLK.load(_ds, inputFile);
            _translationTable.setModel(new TranslationModel(_ds));
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this, "Failed to parse input file", "Import error", JOptionPane.ERROR_MESSAGE);
        }
    }
    private void save() {
        if (_currentFile != null) {
            try {
                _ds.save(_currentFile);
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(this, "Failed to save file!", "Save error", JOptionPane.ERROR_MESSAGE);
            }
        }
        else
            saveAs();
    }
    private void saveAs() {
        JFileChooser chooser = new JFileChooser();
        chooser.setFileFilter(FilterTDS);
        chooser.setAcceptAllFileFilterUsed(false);
        chooser.setMultiSelectionEnabled(false);
        if (chooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
            _currentFile = chooser.getSelectedFile();
            save();
        }
    }

    private void commandImport() {
        JFileChooser chooser = new JFileChooser();
        chooser.setFileFilter(FilterTLK);
        chooser.setAcceptAllFileFilterUsed(false);
        if (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION)
            importTLK(chooser.getSelectedFile());
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String actionCommand = e.getActionCommand();
        if (actionCommand.equals("Import"))
            commandImport();
        else if (actionCommand.equals("Save"))
            save();
        else if (actionCommand.equals("Save As"))
            saveAs();
    }

    private static class MenuItemInfo {
        private String name;
        private char mnemonic;
        private JMenu parent;

        public MenuItemInfo(String name, JMenu parent) {
            this.name = name;
            this.parent = parent;
        }
        public MenuItemInfo(String name, char mnemonic, JMenu parent) {
            this(name, parent);
            this.mnemonic = mnemonic;
        }

        public String getName() {
            return name;
        }

        public char getMnemonic() {
            return mnemonic;
        }

        public JMenu getParent() {
            return parent;
        }
        
    }
}
