/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tlkhandler;

import UI.MainFrame;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author ovoloshchuk
 */
public class TlkHandler {
    public final static String TLK_FILENAME = "/home/seth/dialog.tlk";
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        MainFrame f = new MainFrame();
        f.setVisible(true);
        return;
//        try {
//            TLK tlk = new TLK(TLK_FILENAME);
//            tlk.load();
//            System.out.println(String.format("Language ID is %d; "
//                    + "String count is %d; "
//                    + "String data starts at %d.", 
//                    tlk.getHeader().getLanguageID(),
//                    tlk.getHeader().getStringCount(),
//                    tlk.getHeader().getStringEntriesOffset()));
//        } catch (IOException ex) {
//            Logger.getLogger(TlkHandler.class.getName()).log(Level.SEVERE, null, ex);
//        }
    }
}
