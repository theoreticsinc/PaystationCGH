/*
 * Main.java
 *
 * Created on January 28, 2008, 5:15 AM
 *
 * To change this template, choose Tools | Options and locate the template under
 * the Source Creation and Management node. Right-click the template and choose
 * Open. You can then make changes to the template in the Source Editor.
 */

//Github Token
//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
//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
//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
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
        log.info("Starting Hybrid v.5.7 reprint 3Checks fix v.1");
        HybridPanelUI tpUI = new HybridPanelUI();
//        
//        System.out.print("OK, only this instance is running");
//        System.out.println(" but will terminate in 10 seconds.");
//        try {
//            //Thread.sleep(10000);
//            //if (tpUI.s != null && !tpUI.s.isClosed()) tpUI.s.close();
//        } catch (Exception e) {
//            System.err.println(e);
//        }
        
        tpUI.dispose();
        tpUI.setUndecorated(true);
        tpUI.setVisible(true);
        tpUI.toFront();
        tpUI.StartUI();
        tpUI.requestFocus();
    }

}
