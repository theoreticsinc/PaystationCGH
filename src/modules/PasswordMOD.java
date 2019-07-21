/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package modules;

import UserInteface.HybridPanelUI;
import api.MasterCardAPI;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import misc.LogUtility;
import misc.RawFileHandler;

/**
 *
 * @author root
 */
public class PasswordMOD {

    public String[] SysMsg = new String[20];
    LogUtility logthis = new LogUtility();
    private misc.DataBaseHandler dbh = new misc.DataBaseHandler();

    public boolean isPasswordValid(HybridPanelUI stn) {
        String Password1 = "CASHCOL";
        String Password2 = "SUMMCOL";
        String Password3 = "ZREAD";
        String Password4 = "ZREADNOW";
        String Password5 = "REFUND";
        String Password6 = "ZREADALL";
        String Password7 = "010203";
        String Holiday = "HOLIDAY";
        String Weekday = "WEEKDAY";
        String PrinterON = "PRINTERON";
        String PrinterOFF = "PRINTEROFF";
        
        //MasterCard1 = true;
        //MasterCard2 = false;
        if (Password1.compareToIgnoreCase(stn.PWORDinput.toString()) == 0) {

            if (stn.MasterCard1 == true) {
                stn.resetPassword();
                stn.clearLeftMIDMsgPanel();
                stn.LowerLeftPanel.setVisible(true);
//                LoginMOD lm = new LoginMOD();
//                try {
//                    //lm.printCollectReceipt(stn.EX_SentinelID);
//                    lm.printCollectReceiptFromDB(stn.EX_SentinelID, stn.loginID);
//                } catch (Exception ex) {
//                    LogManager.getLogger(PasswordMOD.class.getName()).log(Level.SEVERE, null, ex);
//                }
                Date printdate = new Date();
                logthis.setLog(stn.EX_SentinelID, "Printing Summary Collection at " + printdate);

                //stn.awaitClearCollect = true;
                //SysMsg[3] = "Press Y";
                //SysMsg[4] = "to delete Collection Records";
                //SysMsg[7] = "Press N";
                //SysMsg[8] = "to proceed without deletion";
//                if (secret == 3)
//                {
//                    clearCollectReceipt();
//                    secret = 0;
//                }
            } else {
                stn.resetPassword();
                stn.clearLeftMIDMsgPanel();
                SysMsg[11] = "MasterCard 2";
                SysMsg[12] = "- is active -";
                SysMsg[14] = "Password";
                SysMsg[15] = "- is valid -";
                SysMsg[17] = "MasterCard 1";
                SysMsg[18] = " is needed";
            }
            return true;
        } else if (Password2.compareToIgnoreCase(stn.PWORDinput.toString()) == 0) {
            stn.clearLeftMIDMsgPanel();
            if (stn.MasterCard1 == true) {
                LoginMOD lm = new LoginMOD();
                //lm.printLogoutReceipt(stn.EX_SentinelID, true);
                lm.epsonPrintLogoutReceiptFromDB(stn.EX_SentinelID, true);
                Date printdate = new Date();
                logthis.setLog(stn.EX_SentinelID, "printing current Collection at " + printdate);
                SysMsg[11] = "Current Collection";
                SysMsg[13] = "Printout";
            } else {
                stn.clearLeftMIDMsgPanel();
                SysMsg[11] = "MasterCard 2";
                SysMsg[12] = "- is active -";
                SysMsg[14] = "Password";
                SysMsg[15] = "- is valid -";
                SysMsg[17] = "MasterCard 1";
                SysMsg[18] = " is needed";
            }
            stn.resetPassword();
            return true;
        } else if (Password3.compareToIgnoreCase(stn.PWORDinput.toString()) == 0) {

            if (stn.MasterCard1 == true) {
                stn.resetPassword();
                stn.clearLeftMIDMsgPanel();
                stn.ZReadingPanel.setVisible(true);
                Date printdate = new Date();
                logthis.setLog(stn.EX_SentinelID, "Printing ZReading at " + printdate);
            } else {
                stn.resetPassword();
                stn.clearLeftMIDMsgPanel();
                SysMsg[11] = "MasterCard 2";
                SysMsg[12] = "- is active -";
                SysMsg[14] = "Password";
                SysMsg[15] = "- is valid -";
                SysMsg[17] = "MasterCard 1";
                SysMsg[18] = " is needed";
            }
            return true;
        } else if (Password4.compareToIgnoreCase(stn.PWORDinput.toString()) == 0) {
            stn.clearLeftMIDMsgPanel();
            if (stn.MasterCard1 == true) {
                LoginMOD lm = new LoginMOD();
                //lm.printLogoutReceipt(stn.EX_SentinelID, true);
                lm.printHEADER(stn.EX_SentinelID);
                lm.printTodaysZReadFromDB(stn.EX_SentinelID);
                Date printdate = new Date();
                logthis.setLog(stn.EX_SentinelID, "printing current zread at " + printdate);
                SysMsg[11] = "Current ZRead";
                SysMsg[13] = "Printout";
                lm.setDateofLastZRead(stn.EX_SentinelID);
            } else {
                stn.clearLeftMIDMsgPanel();
                SysMsg[11] = "MasterCard 2";
                SysMsg[12] = "- is active -";
                SysMsg[14] = "Password";
                SysMsg[15] = "- is valid -";
                SysMsg[17] = "MasterCard 1";
                SysMsg[18] = " is needed";
            }
            stn.resetPassword();
            return true;
        } else if (Password5.compareToIgnoreCase(stn.PWORDinput.toString()) == 0) {

            if (stn.MasterCard1 == true) {
                stn.resetPassword();
                stn.clearLeftMIDMsgPanel();
                stn.SettPanel.setVisible(false);
                stn.RefundPanel.setVisible(true);
                Date printdate = new Date();
                logthis.setLog(stn.EX_SentinelID, "Printing Refund at " + printdate);
            } else {
                stn.resetPassword();
                stn.clearLeftMIDMsgPanel();
                SysMsg[11] = "MasterCard 2";
                SysMsg[12] = "- is active -";
                SysMsg[14] = "Password";
                SysMsg[15] = "- is valid -";
                SysMsg[17] = "MasterCard 1";
                SysMsg[18] = " is needed";
            }
            return true;
        } else if (Password6.compareToIgnoreCase(stn.PWORDinput.toString()) == 0) {
            stn.clearLeftMIDMsgPanel();
            if (stn.MasterCard1 == true) {
                LoginMOD lm = new LoginMOD();
                //lm.printLogoutReceipt(stn.EX_SentinelID, true);
                lm.printHEADER(stn.EX_SentinelID);
                lm.printAllofTodaysZReadFromDB(stn.EX_SentinelID);
                Date printdate = new Date();
                logthis.setLog(stn.EX_SentinelID, "printing current zread at " + printdate);
                SysMsg[11] = "Current ZRead";
                SysMsg[13] = "Printout";
                MasterCardAPI mc = new MasterCardAPI();
                try {
                    String mastercard1 = mc.getMasterRec1();
                    String mcardOwner1 = mc.getMasterOwner1();
                    dbh.saveLog("Z1", mcardOwner1);
                } catch (Exception ex) {
                    Logger.getLogger(PasswordMOD.class.getName()).log(Level.SEVERE, null, ex);
                }
                
                lm.setDateofLastZRead(stn.EX_SentinelID);
            } else {
                stn.clearLeftMIDMsgPanel();
                SysMsg[11] = "MasterCard 2";
                SysMsg[12] = "- is active -";
                SysMsg[14] = "Password";
                SysMsg[15] = "- is valid -";
                SysMsg[17] = "MasterCard 1";
                SysMsg[18] = " is needed";
            }
            stn.resetPassword();
            return true;
        } else if (Holiday.compareToIgnoreCase(stn.PWORDinput.toString()) == 0) {
            stn.clearLeftMIDMsgPanel();
            stn.HolidayOverride = true;
            SysMsg[11] = "Holiday Settings";
            SysMsg[12] = "Activated";
            stn.resetPassword();
            return true;
        } else if (PrinterOFF.compareToIgnoreCase(stn.PWORDinput.toString()) == 0) {
            stn.clearLeftMIDMsgPanel();
            stn.PrinterEnabled = true;
            SysMsg[11] = "Printer Overriding";
            SysMsg[12] = "Activated";
            stn.resetPassword();
            return true;
        } else if (PrinterON.compareToIgnoreCase(stn.PWORDinput.toString()) == 0) {
            stn.clearLeftMIDMsgPanel();
            stn.PrinterEnabled = false;
            SysMsg[14] = "Printer Overriding";
            SysMsg[16] = "Deactivated";
            stn.resetPassword();
            return true;
        } else if (Weekday.compareToIgnoreCase(stn.PWORDinput.toString()) == 0) {
            stn.clearLeftMIDMsgPanel();
            stn.HolidayOverride = false;
            SysMsg[11] = "Weekday Settings";
            SysMsg[13] = "Activated";
            stn.resetPassword();
            return true;
        } else if (Password7.compareToIgnoreCase(stn.PWORDinput.toString()) == 0) {
            stn.resetPassword();
            stn.clearLeftMIDMsgPanel();
            stn.SettPanel.setVisible(false);
            stn.RefundPanel.setVisible(true);
            Date printdate = new Date();
            SysMsg[17] = "VOID Transactions";
            SysMsg[19] = "     PRINTED";
            
                MasterCardAPI mc = new MasterCardAPI();
                try {
                    String mastercard1 = mc.getMasterRec1();
                    String mcardOwner1 = mc.getMasterOwner1();
                    dbh.saveLog("V0", mcardOwner1);
                } catch (Exception ex) {
                    Logger.getLogger(PasswordMOD.class.getName()).log(Level.SEVERE, null, ex);
                }

        } else {
            SysMsg[17] = "Wrong Password";
            SysMsg[19] = "Try Again";
        }

        stn.resetPassword();
        //secret = 0;
        return false;
    }

    public void clearCollectReceipt(String Exitpoint) {
        RawFileHandler rfh = new RawFileHandler();
        Date now = new Date();
        SimpleDateFormat mdf = new SimpleDateFormat("MM");
        SimpleDateFormat ydf = new SimpleDateFormat("yy");
        String extension = Exitpoint.substring(2, 4) + mdf.format(now) + ".0" + ydf.format(now);
        //rfh.copySource2Dest("C://JTerminals/collect.jrt", "C://JTerminals/CLT" + extension);
        rfh.deleteFile("C://JTerminals/de4Dd87d/CfgJ9rl/", "collect.jrt");
    }
}
