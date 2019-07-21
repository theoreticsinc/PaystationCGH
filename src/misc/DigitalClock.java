/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package misc;

import java.text.DateFormat;
import java.util.Date;
import javax.swing.JLabel;
import org.apache.log4j.LogManager;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;

/**
 *
 * @author Administrator
 */
public class DigitalClock extends JLabel implements Runnable {

    Logger log = LogManager.getLogger(DigitalClock.class.getName());
    DigitalClock() {
        Thread dc = new Thread(this);
        dc.start();
        //dc.run();
    }

    @Override
    public void run() {
        try {
            while (true) {
                Date theTime = new Date();
                DateFormat df = DateFormat.getDateInstance(DateFormat.MEDIUM);
                log.info(df.format(theTime));

                DateFormat tf = DateFormat.getTimeInstance(DateFormat.MEDIUM);
                log.info(tf.format(theTime));

                Thread.sleep(1000);
            }
        } catch (Exception ex) {
            //log.error(ex.getMessage());
        }
    }

}
