/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package modules;

import java.io.IOException;
import java.util.Date;
import org.apache.log4j.LogManager;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import misc.LogUtility;
import misc.RawFileHandler;

/**
 *
 * @author Administrator
 */
public class NOSfiles {

    RawFileHandler rfh = new RawFileHandler();
    
    static Logger log = LogManager.getLogger(NOSfiles.class.getName());

    public void RenewCarSlots(String SentinelID) throws IOException {        //For BACKOUT PURPOSES
        String newcurr = "";
        String curr = "";
        if (rfh.FindFileFolder("/SUBSYSTEMS/", SentinelID + "SERV.SER") == true) {
            curr = rfh.readFline("/SUBSYSTEMS/", SentinelID + "SERV.SER", 1);
        } else {
            try {
                rfh.putfile("/SUBSYSTEMS/", SentinelID + "SERV.SER", "1");
            } catch (Exception ex) {
                rfh.putfile("/SUBSYSTEMS/", SentinelID + "SERV.SER", "1");
            }
        }
        int newcount = 0;
        int oldcount = Integer.parseInt(curr);
        newcount = oldcount - 1;
        newcurr = String.valueOf(newcount);
        try {
            rfh.putfile("/SUBSYSTEMS/", SentinelID + "SERV.SER", newcurr);
        } catch (Exception ex) {
            rfh.putfile("/SUBSYSTEMS/", SentinelID + "SERV.SER", newcurr);
        }
//        try {
//            Process s = Runtime.getRuntime().exec("sudo chmod 777 /SUBSYSTEMS/"+ SentinelID + "SERV.SER");
//            s.waitFor();
//        } catch (InterruptedException ex) {
//            LogManager.getLogger(NOSfiles.class.getName()).log(Level.SEVERE, null, ex);
//        }
        boolean foundfile = rfh.FindFileFolder("/SYSTEMS/online.aaa");
        if (foundfile == true) {
            rfh.copySource2Dest("/SUBSYSTEMS/" + SentinelID + "SERV.SER", "/SYSTEMS/" + SentinelID + "SERV.SER");
//        try {
//            Process s = Runtime.getRuntime().exec("sudo chmod 777 /SYSTEMS/"+ SentinelID + "SERV.SER");
//            s.waitFor();
//        } catch (InterruptedException ex) {
//            LogManager.getLogger(NOSfiles.class.getName()).log(Level.SEVERE, null, ex);
//        }
        }
    }

    public void UpdateCarSlots(String SentinelID, String slotsmode) {        //used by 'Entrance Tickets plus one'
        String newcurr = "";
        String curr = "0";
        boolean copyserver = false;
        Runtime rt = Runtime.getRuntime();
        if (slotsmode.compareToIgnoreCase("STANDALONE") == 0) {
            copyserver = false;
        } else {
            copyserver = true;
        }
//        try {
////            Process s = Runtime.getRuntime().exec("sudo chmod 777 /SUBSYSTEMS/"+ SentinelID + "SERV.SER");
//            //s.waitFor();
//        } catch (Exception ex) {
//            LogManager.getLogger(NOSfiles.class.getName()).log(Level.SEVERE, null, ex);
//        }
        if (rfh.FindFileFolder("/SUBSYSTEMS/", SentinelID + "SERV.SER") == true) {
            try {
                curr = rfh.readFline("/SUBSYSTEMS/", SentinelID + "SERV.SER", 1);
            } catch (Exception ex) {
                log.error(ex.getMessage());
            }
        } else {
            try {
                rfh.putfile("/SUBSYSTEMS/", SentinelID + "SERV.SER", "1");
            } catch (Exception ex) {
                log.error(ex.getMessage());
            }

        }
        int newcount = 0;
        int oldcount = Integer.parseInt(curr);
        newcount = oldcount + 1;
        newcurr = String.valueOf(newcount);
        try {
            rfh.putfile("/SUBSYSTEMS/", SentinelID + "SERV.SER", newcurr);
        } catch (Exception ex) {
            rfh.putfile("/SUBSYSTEMS/", SentinelID + "SERV.SER", newcurr);
        }
//        try {
//            Process s = Runtime.getRuntime().exec("sudo chmod 777 /SUBSYSTEMS/"+ SentinelID + "SERV.SER");
//            s.waitFor();
//        } catch (InterruptedException ex) {
//            LogManager.getLogger(NOSfiles.class.getName()).log(Level.SEVERE, null, ex);
//        }
        LogUtility logthis = new LogUtility();
        Date Now = new Date();
        logthis.setsysLog(SentinelID, Now.getTime() + "local slots: " + newcurr);

        if (copyserver == true) {
            boolean foundfile = false;
            try {
                foundfile = rfh.FindFileFolder("/SYSTEMS/online.aaa");
            } catch (Exception ex) {
                log.error(ex.getMessage());
            }
            if (foundfile == true) {
                rfh.copySource2Dest("/SUBSYSTEMS/" + SentinelID + "SERV.SER", "/SYSTEMS/" + SentinelID + "SERV.SER");
            }
        } else {
            rfh.copySource2Dest("/SUBSYSTEMS/" + SentinelID + "SERV.SER", "C://JTerminals/" + SentinelID + "SERV.SER");
        }
    }

    public void UpdateEntryTicketServed() {
        try {

            String newcurr = "";
            boolean foundfile = rfh.FindFileFolder("C://JTerminals/de4Dd87d/CfgJ9rl/", "hbugs.jrt");
            if (foundfile == true) {
                String curr = rfh.readFline("C://JTerminals/de4Dd87d/CfgJ9rl/", "hbugs.jrt", 1);

                int newcount = 0;
                int oldcount = Integer.parseInt(curr);
                newcount = oldcount + 1;
                newcurr = String.valueOf(newcount);
                rfh.putfile("C://JTerminals/de4Dd87d/CfgJ9rl/", "hbugs.jrt", newcurr);
            } else {
                newcurr = "1";
            }
            rfh.putfile("C://JTerminals/de4Dd87d/CfgJ9rl/", "hbugs.jrt", newcurr);
        } catch (Exception ex) {
            log.error(ex.getMessage());
        }

    }

    public String getCarAVSlotsII(String SlotsID) throws Exception {

        String curr = "";
        boolean foundfile = rfh.FindFileFolder("/SYSTEMS/", SlotsID + ".NOS");
        if (foundfile == true) {
            curr = rfh.readFline("/SYSTEMS/", SlotsID + ".NOS", 1);
        }
        return curr;
    }

    public String getCarOvrdSlots(String ParkingArea) throws IOException {

        String curr = "";
        boolean foundfile = rfh.FindFileFolder("/SYSTEMS/", ParkingArea + "OVRD.SER");
        if (foundfile == true) {
            curr = rfh.readFline("/SYSTEMS/", ParkingArea + "OVRD.SER", 1);
        }
        return curr;
    }

    public String GetCarSlots(String SentinelID) {
        String curr = "0";
        try {
            if (rfh.FindFileFolder("/SUBSYSTEMS/", SentinelID + "SERV.SER") == true) {
                curr = rfh.readFline("/SUBSYSTEMS/", SentinelID + "SERV.SER", 1);
            } else {
                rfh.putfile("/SUBSYSTEMS/", SentinelID + "SERV.SER", "1");
                curr = "1";
            }
        } catch (Exception ex) {
            log.error(ex.getMessage());
        }
        return curr;
    }

    public String getCarAVSlots(String SentinelID) throws IOException {

        String newcurr = "";
//        try {
//            Process s = Runtime.getRuntime().exec("sudo chmod 777 /SYSTEMS/AVSLOTS"+ SentinelID + ".nos");
//            s.waitFor();
//        } catch (InterruptedException ex) {
//            LogManager.getLogger(NOSfiles.class.getName()).log(Level.SEVERE, null, ex);
//        }

        boolean foundfile = rfh.FindFileFolder("/SYSTEMS/AVSLOTS", SentinelID + ".nos");
        if (foundfile == true) {
            String curr = rfh.readFline("/SYSTEMS/AVSLOTS", SentinelID + ".nos", 1);
            return curr;
        } else {
            return newcurr;
        }

    }

    public void RenewCarServed() throws IOException //for backout entrance only
    {
        String newcurr = "";
        boolean foundfile = rfh.FindFileFolder("C://JTerminals/de4Dd87d/CfgJ9rl/", "hcornell.jrt");
        if (foundfile == true) {
            String curr = rfh.readFline("C://JTerminals/de4Dd87d/CfgJ9rl/", "hcornell.jrt", 1);

            int newcount = 0;
            int oldcount = Integer.parseInt(curr);
            newcount = oldcount - 1;
            newcurr = String.valueOf(newcount);
            rfh.putfile("C://JTerminals/de4Dd87d/CfgJ9rl/", "hcornell.jrt", newcurr);
        } else {
            newcurr = "1";
            rfh.putfile("C://JTerminals/de4Dd87d/CfgJ9rl/", "hcornell.jrt", newcurr);
        }
//        try {
//            Process s = Runtime.getRuntime().exec("sudo chmod 777 /JTerminals/hcornell.jrt");
//            s.waitFor();
//        } catch (InterruptedException ex) {
//            LogManager.getLogger(NOSfiles.class.getName()).log(Level.SEVERE, null, ex);
//        }
    }

    public void UpdateCarServed() throws Exception {
        String newcurr = "";
        boolean foundfile = rfh.FindFileFolder("C://JTerminals/de4Dd87d/CfgJ9rl/", "hcornell.jrt");
        if (foundfile == true) {
            String curr = rfh.readFline("C://JTerminals/de4Dd87d/CfgJ9rl/", "hcornell.jrt", 1);

            int newcount = 0;
            int oldcount = Integer.parseInt(curr);
            newcount = oldcount + 1;
            newcurr = String.valueOf(newcount);
            rfh.putfile("C://JTerminals/de4Dd87d/CfgJ9rl/", "hcornell.jrt", newcurr);
        } else {
            newcurr = "1";
            rfh.putfile("C://JTerminals/de4Dd87d/CfgJ9rl/", "hcornell.jrt", newcurr);
//        try {
//            Process s = Runtime.getRuntime().exec("sudo chmod 777 /JTerminals/hcornell.jrt");
//            s.waitFor();
//        } catch (InterruptedException ex) {
//            LogManager.getLogger(NOSfiles.class.getName()).log(Level.SEVERE, null, ex);
//        }
        }

    }

    public String getCarServed() {
        String curr = "";
        boolean foundfile = false;
        foundfile = rfh.FindFileFolder("C://JTerminals/de4Dd87d/CfgJ9rl/", "hcornell.jrt");
        if (foundfile == true) {
            curr = rfh.readFline("C://JTerminals/de4Dd87d/CfgJ9rl/", "hcornell.jrt", 1);
        } else {
            curr = "0";
        }
        return curr;
    }

    public String getExitCarServed() throws IOException {
        String curr = "";
        boolean foundfile = rfh.FindFileFolder("C://JTerminals/de4Dd87d/CfgJ9rl/", "hbrentbay.jrt");
        if (foundfile == true) {
            curr = rfh.readFline("C://JTerminals/de4Dd87d/CfgJ9rl/", "hbrentbay.jrt", 1);
        } else {
            curr = "0";
        }
        return curr;
    }

    public String getEntryTicketsServed() throws IOException {
        String curr = "";
        boolean foundfile = rfh.FindFileFolder("C://JTerminals/de4Dd87d/CfgJ9rl/", "hbugs.jrt");
        if (foundfile == true) {
            curr = rfh.readFline("C://JTerminals/de4Dd87d/CfgJ9rl/", "hbugs.jrt", 1);
        } else {
            curr = "0";
        }
        return curr;
    }

    public String getExitTicketsServed() throws IOException {
        String curr = "";
        boolean foundfile = rfh.FindFileFolder("C://JTerminals/de4Dd87d/CfgJ9rl/", "hbacta.jrt");
        if (foundfile == true) {
            curr = rfh.readFline("C://JTerminals/de4Dd87d/CfgJ9rl/", "hbacta.jrt", 1);
        } else {
            curr = "0";
        }
        return curr;
    }

    public void ResetCarServed() throws IOException {
        rfh.putfile("C://JTerminals/de4Dd87d/CfgJ9rl/", "hcornell.jrt", "0");
        rfh.putfile("C://JTerminals/de4Dd87d/CfgJ9rl/", "hbrentbay.jrt", "0");
//     try {
//            Process s = Runtime.getRuntime().exec("sudo chmod 777 /JTerminals/hcornell.jrt");
//            s.waitFor();
//            Process p = Runtime.getRuntime().exec("sudo chmod 777 /JTerminals/hbrentbay.jrt");
//            p.waitFor();
//        } catch (InterruptedException ex) {
//            LogManager.getLogger(NOSfiles.class.getName()).log(Level.SEVERE, null, ex);
//        }
    }

    public void setSlotsOverride(String SentinelID, String Overridevalue) throws IOException {
        rfh.putfile("/SUBSYSTEMS/", SentinelID + "OVRD.SER", Overridevalue);
        boolean foundfile = rfh.FindFileFolder("/SYSTEMS/online.aaa");
        if (foundfile == true) {
            rfh.copySource2Dest("/SUBSYSTEMS/" + SentinelID + "OVRD.SER", "/SYSTEMS/" + SentinelID + "OVRD.SER");
        }
    }

    public void calculateOverride(String setnos, String SlotsID, String ParkingArea) {
        long current = 0, need2be = 0, actualoverride, currentoverride;
        try {
            String currentstr = getCarAVSlotsII(SlotsID);
            String currentoverridestr = getCarOvrdSlots(ParkingArea);
            current = Long.parseLong(currentstr);
            currentoverride = Long.parseLong(currentoverridestr);
            need2be = Long.parseLong(setnos);

            //Formula
            actualoverride = need2be - current + currentoverride;
            //*eof Formula*//
            String actualoverridestr = String.valueOf(actualoverride);
            this.setSlotsOverride(ParkingArea, actualoverridestr);

        } catch (Exception ex) {
            log.error(ex.getMessage());
        }
    }

    public void logSet(String setnos, String SlotsID, String ParkingArea, String CashierName) {
        Date now = new Date();
        try {
            rfh.appendfile("/SUBSYSTEMS/", "sl.frd", now + " SET to: " + setnos + " on Terminal " + ParkingArea + " " + SlotsID + " with Cashier on Duty:" + CashierName);
        } catch (IOException ex) {
            log.error(ex.getMessage());
        }
    }
}
