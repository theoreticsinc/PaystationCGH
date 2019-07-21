/*
 * Main.java
 *
 * Created on January 28, 2008, 5:15 AM
 *
 * To change this template, choose Tools | Options and locate the template under
 * the Source Creation and Management node. Right-click the template and choose
 * Open. You can then make changes to the template in the Source Editor.
 */
package UserInteface;

import UserInteface.HybridPanelUI.*;
import org.apache.log4j.LogManager;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;

/**
 *
 * @author Angelo
 */
public class Main {

    static Logger log = LogManager.getLogger(Main.class.getName());
    /**
     * Creates a new instance of Main
     */
    public Main() {
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws Exception {
        log.info("Starting Hybrid");
        HybridPanelUI tpUI = new HybridPanelUI();
        tpUI.dispose();
        tpUI.setUndecorated(true);
        tpUI.setVisible(true);
        tpUI.toFront();
        tpUI.StartUI();
        tpUI.requestFocus();
    }

}
