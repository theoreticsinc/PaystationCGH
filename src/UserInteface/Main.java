//Github Token
//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
package UserInteface;

import org.apache.log4j.LogManager;
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
        log.info("Starting Hybrid v.5.7 Senior Enhancements v.2");
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
