/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package misc;

import java.text.DateFormat;
import java.util.Date;
import org.apache.log4j.LogManager;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;

/**
 *
 * @author Angelologthis.setLog(stn.EX_SentinelID, "Card not found: " +
 * CardCheck);
 */
public class LogUtility {
    
    static Logger log = LogManager.getLogger(LogUtility.class.getName());

    public void setLog(String SentinelID, String logline) {
        try {
            DateConversionHandler dch = new DateConversionHandler();
            Date theTime = new Date();
            DateFormat df = DateFormat.getDateInstance(DateFormat.SHORT);
            String datetoday = df.format(theTime);
            String filetoday = dch.convertDate2base(datetoday);

            RawFileHandler rfh = new RawFileHandler();
            //local log
            if (rfh.FindFileFolder("C://JTerminals/" + SentinelID + filetoday + ".log") == true) {
                rfh.appendfile("C://JTerminals/", SentinelID + filetoday + ".log", logline + "\n");
            } else {
                rfh.putfile("C://JTerminals/", SentinelID + filetoday + ".log", logline + "\n");
            }
            //server log
//            if (rfh.FindFileFolder("/SYSTEMS/" + SentinelID + filetoday + ".log") == true) {
//                rfh.appendfile("/SYSTEMS/", SentinelID + filetoday + ".log", logline + "\n");
//            } else {
//                rfh.putfile("/SYSTEMS/", SentinelID + filetoday + ".log", logline + "\n");
//            }
        } catch (Exception ex) {
            log.error(ex.getMessage());
        }
    }

    public void setsysLog(String SentinelID, String logline) {
        try {
            DateConversionHandler dch = new DateConversionHandler();
            Date theTime = new Date();
            DateFormat df = DateFormat.getDateInstance(DateFormat.SHORT);
            String datetoday = df.format(theTime);
            String filetoday = dch.convertDate2base(datetoday);

            RawFileHandler rfh = new RawFileHandler();
            //local log
            if (rfh.FindFileFolder("C://JTerminals/" + SentinelID + filetoday + ".log") == true) {
                rfh.appendfile("C://JTerminals/", SentinelID + filetoday + ".log", logline + "\n");
            } else {
                rfh.putfile("C://JTerminals/", SentinelID + filetoday + ".log", logline + "\n");
            }
            //server log
//            if (rfh.FindFileFolder("/SYSTEMS/" + SentinelID + filetoday + ".log") == true) {
//                rfh.appendfile("/SYSTEMS/", SentinelID + filetoday + ".log", logline + "\n");
//            } else {
//                rfh.putfile("/SYSTEMS/", SentinelID + filetoday + ".log", logline + "\n");
//            }
        } catch (Exception ex) {
            log.error(ex.getMessage());
        }
    }
}
