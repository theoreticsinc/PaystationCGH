/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package api;

import misc.USBEpsonHandler;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;
import misc.DataBaseHandler;
import misc.ParkerDataHandler;
import misc.RawFileHandler;
import misc.SerialEpsonHandler;
import misc.XMLreader;
import modules.SaveCollData;
import modules.SystemStatus;
import org.apache.log4j.LogManager;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;

/**
 *
 * @author Administrator
 */
public class ParkersAPI {

    Statement stmt = null;
    Connection conn = null;
    SystemStatus ss = new SystemStatus();
    private RawFileHandler rfh = new RawFileHandler();
    public String[] SysMsg = new String[20];
    private String CRDPLTdata;
    private String compareCRDPLTdata;
    public String[] parkingData = new String[7];
    public String dateTimeIN;
    public String dateTimePaid;
    public String dateTimeINStamp;
    public String dateTimePaidStamp;
    private boolean black;

    static Logger log = LogManager.getLogger(ParkersAPI.class.getName());

    public void getPdata(String plate2check) {
        if (rfh.FindFileFolder("/SYSTEMS/", plate2check.toUpperCase() + ".PLT") == true) {
            this.getDatafromServerCRDPLT(plate2check.toUpperCase(), false, true);
            //String info = rfh.readFline("/SYSTEMS/", plate2check+".PLT", 1);
            String Entrypoint = getSysID();
            String Cardno = getCardID();
            String Plateno = getPlateID();
            String ParkerType = getTRID();
            String dateIN = getFormattedDateID();
            String timeIN = getFormattedTimeID();
            SysMsg[0] = "Entry: " + Entrypoint;
            SysMsg[2] = "Card No:  " + Cardno;
            SysMsg[3] = "Plate No: " + Plateno;
            SysMsg[5] = "Type: " + ParkerType;
            SysMsg[7] = "Date In: " + dateIN;
            SysMsg[9] = "Time In: " + timeIN;
        } else if (rfh.FindFileFolder("/SYSTEMS/", plate2check.toUpperCase() + ".plt") == true) {
            this.getDatafromServerCRDPLT(plate2check.toUpperCase(), false, false);
            String Entrypoint = getSysID();
            String Cardno = getCardID();
            String Plateno = getPlateID();
            String ParkerType = getTRID();
            String dateIN = getFormattedDateID();
            String timeIN = getFormattedTimeID();
            SysMsg[0] = "Entry: " + Entrypoint;
            SysMsg[2] = "Card No:  " + Cardno;
            SysMsg[3] = "Plate No: " + Plateno;
            SysMsg[5] = "Type: " + ParkerType;
            SysMsg[7] = "Date In: " + dateIN;
            SysMsg[9] = "Time In: " + timeIN;
        } else if (rfh.FindFileFolder("/SYSTEMS/", plate2check.toLowerCase() + ".PLT") == true) {
            this.getDatafromServerCRDPLT(plate2check.toLowerCase(), false, true);
            //String info = rfh.readFline("/SYSTEMS/", plate2check+".plt", 1);
            String Entrypoint = getSysID();
            String Cardno = getCardID();
            String Plateno = getPlateID();
            String ParkerType = getTRID();
            String dateIN = getFormattedDateID();
            String timeIN = getFormattedTimeID();
            SysMsg[0] = "Entry: " + Entrypoint;
            SysMsg[2] = "Card No: " + Cardno;
            SysMsg[3] = "Plate No: " + Plateno;
            SysMsg[5] = "Type: " + ParkerType;
            SysMsg[7] = "Date In: " + dateIN;
            SysMsg[9] = "Time In: " + timeIN;
        } else if (rfh.FindFileFolder("/SYSTEMS/", plate2check.toLowerCase() + ".plt") == true) {
            this.getDatafromServerCRDPLT(plate2check.toLowerCase(), false, false);
            //String info = rfh.readFline("/SYSTEMS/", plate2check+".plt", 1);
            String Entrypoint = getSysID();
            String Cardno = getCardID();
            String Plateno = getPlateID();
            String ParkerType = getTRID();
            String dateIN = getFormattedDateID();
            String timeIN = getFormattedTimeID();
            SysMsg[0] = "Entry: " + Entrypoint;
            SysMsg[2] = "Card No: " + Cardno;
            SysMsg[3] = "Plate No: " + Plateno;
            SysMsg[5] = "Type: " + ParkerType;
            SysMsg[7] = "Date In: " + dateIN;
            SysMsg[9] = "Time In: " + timeIN;
        } else {
            SysMsg[0] = plate2check;
            SysMsg[3] = "No Record Found";
        }
    }

    public boolean checkDatafromServerCRDPLT(String CardCheck, boolean extcard, boolean Ucase) {
        /*
        String ext = "";
        if (extcard == true) {
            if (Ucase == false) {
                ext = ".crd";
            } else {
                ext = ".CRD";
            }

        } else if (Ucase == false) {
            ext = ".plt";
        } else {
            ext = ".PLT";
        }
        try {
            String testdata = "";
            if (rfh.FindFileFolder("/SYSTEMS/", CardCheck + ext) == true) {
                testdata = rfh.readFline("/SYSTEMS/", CardCheck + ext, 1);
            }
            if (testdata.length() >= 30) {
                rfh.putfile("C://JTerminals/de4Dd87d/CfgJ9rl/", "crdtray", testdata);
                return true;
            } else {
                return false;
            }
        } catch (Exception ex) {
            LogManager.getLogger(ParkersAPI.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
         */
        return true;
    }

    public boolean retrieveCRDPLTOld(String card2check, String ip, boolean iscard) {
        boolean found = false;
        String ext = null;
        try {
            if (iscard == true) {
                ext = ".crd";
            } else {
                ext = ".plt";
            }
            if (ss.checkOnline() == true) {
                found = rfh.FindFileFolder("/SYSTEMS/" + card2check + ext);
            }
        } catch (Exception ex) {
            log.error(ex.getMessage());
            return false;
        }
        return found;
    }
    
    public String[] retrieveXRead(String collectionDate) {
        String data[] = null;
        try {
            DataBaseHandler dbh = new DataBaseHandler();
            data = dbh.findXReadings(collectionDate);
            //dateTimeIN = dbh.getTimeINStamp();

        } catch (Exception ex) {
            log.error(ex.getMessage());
        }
        return data;
    }

    public String[] retrieveReceipt(String plate2check) {
        String data[] = null;
        try {
            DataBaseHandler dbh = new DataBaseHandler();
            data = dbh.findReceiptsByRNos(plate2check);
            //dateTimeIN = dbh.getTimeINStamp();

        } catch (Exception ex) {
            log.error(ex.getMessage());
        }
        return data;
    }

    public String[] retrieveReceiptByCategory(int categ, String checkData) {
        String data[] = null;
        try {
            DataBaseHandler dbh = new DataBaseHandler();
            data = dbh.findReceiptsByCategory(categ, checkData);

            //dateTimeIN = dbh.getTimeINStamp();
        } catch (Exception ex) {
            log.error(ex.getMessage());
        }
        return data;
    }

    /*
        Must also save data from Database to temporary file on file
     */
    public boolean retrieveCRDPLTFromDB(String card2check, String ip, boolean iscard) {
        boolean found = false;
        try {
            DataBaseHandler dbh = new DataBaseHandler();
            found = dbh.findEntranceCard(card2check);
            //dateTimeIN = dbh.getTimeINStamp();
            if (found) {
                String data = dbh.getEntCard(card2check);
                CRDPLTdata = data;
                dateTimeIN = dbh.getTimeIN();
                dateTimeINStamp = dbh.getTimeINStamp();
                rfh.putfile("C://JTerminals/de4Dd87d/CfgJ9rl/", "crdtray", data);
            }
        } catch (Exception ex) {
            log.error(ex.getMessage());
        }
        return found;
    }

    public boolean retrieveEXTCRDFromDB(String card2check, String ip, boolean iscard) {
        boolean found = false;
        try {
            DataBaseHandler dbh = new DataBaseHandler();
            found = dbh.findExitCard(card2check);
            //dateTimeIN = dbh.getTimeINStamp();
            if (found) {
                String data = dbh.getExitCard(card2check);
                CRDPLTdata = data;
                dateTimeIN = dbh.getTimeIN();
                dateTimePaid = dbh.getDateTimePaid();
                dateTimeINStamp = dbh.getTimeINStamp();
                dateTimePaidStamp = dbh.getDateTimePaidStamp();

                rfh.putfile("C://JTerminals/de4Dd87d/CfgJ9rl/", "crdtray", data);
            }
        } catch (Exception ex) {
            log.error(ex.getMessage());
        }
        return found;
    }

    public boolean writeExitCRD2DB(String card2check, String PlateCheck, String datetimeIN, String datetimePaid, String datetimeNextDue, String trtype, float amountPaid) {
        boolean written = false;
        try {
            DataBaseHandler dbh = new DataBaseHandler();
            written = dbh.writeExit(card2check, PlateCheck, datetimeIN, datetimePaid, datetimeNextDue, trtype, amountPaid);

        } catch (Exception ex) {
            log.error(ex.getMessage());
        }
        return written;
    }

    public boolean eraseCRDPLTFromDB(String card2check) {
        boolean found = false;
        try {
            DataBaseHandler dbh = new DataBaseHandler();
            found = dbh.eraseEntryCard(card2check);

        } catch (Exception ex) {
            log.error(ex.getMessage());
        }
        return found;
    }

    public boolean eraseEXTCRDFromDB(String card2check) {
        boolean found = false;
        try {
            DataBaseHandler dbh = new DataBaseHandler();
            found = dbh.eraseExitCard(card2check);

        } catch (Exception ex) {
            log.error(ex.getMessage());
        }
        return found;
    }

    public String retrieveVIP_CouponCRDPLT() {
        String found = "";
        try {
            if (rfh.FindFileFolder("C://JTerminals/de4Dd87d/CfgJ9rl/", "tmpcrd") == true) {
                found = rfh.readFline("C://JTerminals/de4Dd87d/CfgJ9rl/", "tmpcrd", 1);
            }
        } catch (Exception ex) {
            log.error(ex.getMessage());
        }

        return found;
    }

    public void addCarSlots(String type) {
        DataBaseHandler DB = new DataBaseHandler();
        DB.Slotsplus1(type);
    }

    public void UpdateCarSlots(String SentinelID, String slotsmode) {        //USED for EXIT TICKETS ONLY
        String newcurr = "";
        String curr = "0";
        boolean copyserver = false;
        if (slotsmode.compareToIgnoreCase("STANDALONE") == 0) {
            copyserver = false;
        } else {
            copyserver = true;
        }
        try {
//            Process s = Runtime.getRuntime().exec("sudo chmod 777 /SUBSYSTEMS/"+ SentinelID + "SERV.SER");
//            s.waitFor();
            if (rfh.FindFileFolder("/SUBSYSTEMS/", SentinelID + "SERV.SER") == true) {
                try {
                    curr = rfh.readFline("/SUBSYSTEMS/", SentinelID + "SERV.SER", 1);
                } catch (Exception ex) {
                    curr = rfh.readFline("/SUBSYSTEMS/", SentinelID + "SERV.SER", 1);
                }

            } else {
                try {
                    rfh.putfile("/SUBSYSTEMS/", SentinelID + "SERV.SER", "1");
                } catch (Exception ex) {
                }

            }
        } catch (Exception ex) {
            log.error(ex.getMessage());
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

    public void UpdateExitTicketServed() {
        try {
            String newcurr = "";
            boolean foundfile = rfh.FindFileFolder("C://JTerminals/de4Dd87d/CfgJ9rl/", "hbacta.jrt");
            if (foundfile == true) {
                String curr = rfh.readFline("C://JTerminals/de4Dd87d/CfgJ9rl/", "hbacta.jrt", 1);

                int newcount = 0;
                int oldcount = Integer.parseInt(curr);
                newcount = oldcount + 1;
                newcurr = String.valueOf(newcount);
                rfh.putfile("C://JTerminals/de4Dd87d/CfgJ9rl/", "hbacta.jrt", newcurr);
            } else {
                newcurr = "1";
            }
            rfh.putfile("C://JTerminals/de4Dd87d/CfgJ9rl/", "hbacta.jrt", newcurr);
        } catch (Exception ex) {
            log.error(ex.getMessage());
        }
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

    public String getExitTicketsServed() {
        String curr = "0";
        try {
            if (rfh.FindFileFolder("C://JTerminals/de4Dd87d/CfgJ9rl/", "hbacta.jrt") == true) {
                curr = rfh.readFline("C://JTerminals/de4Dd87d/CfgJ9rl/", "hbacta.jrt", 1);
            } else {
                rfh.putfile("C://JTerminals/de4Dd87d/CfgJ9rl/", "hbacta.jrt", "1");
                curr = "1";
            }
        } catch (Exception ex) {
            log.error(ex.getMessage());
        }
        return curr;
    }

    public void copyCRDPLT2ZERO(String card2copy, String serverIP) {
        card2copy = card2copy + ".crd";
        try {
            //Process pt = Runtime.getRuntime().exec("sudo chmod 777 /SYSTEMS/" + card2copy + ".crd");
            //pt.waitFor();
            //rfh.copySource2Dest("/SYSTEMS/"+card2copy, "C://JTerminals/F1/tmpcrd");
            rfh.copySource2Dest("/SYSTEMS/" + card2copy, "C://JTerminals/F1/" + card2copy);
            //Process pr = Runtime.getRuntime().exec("sudo chmod 777 /JTerminals/F1/"+card2copy);
            //pr.waitFor();                
            //The following maybe the cause of Files left unlocked in the server. and the cause for card duplicates in the entrance
            //Process s = Runtime.getRuntime().exec("sudo rm /JTerminals/tmpcrd");
            //s.waitFor();
            //s = Runtime.getRuntime().exec("sudo cp /SYSTEMS/"+card2copy+" /JTerminals/tmpcrd");                
        } catch (Exception ex) {
            log.error(ex.getMessage());
        }
    }

    public void copyCRDPLTtoZERO(String card2copy, String serverIP) {
        card2copy = card2copy + ".crd";
        try {
            if (rfh.FindFileFolder("/SYSTEMS/", card2copy) == true) {
                String temp = rfh.readFline("/SYSTEMS/", card2copy, 1);
                rfh.putfile("C://JTerminals/F1/", card2copy, temp);
            }
        } catch (Exception ex) {
            log.error(ex.getMessage());
        }
    }

    public void copyCRDPLT2LAST(String card2copy, String serverIP) {
        card2copy = card2copy + ".crd";
        try {
            //Process s = Runtime.getRuntime().exec("sudo rm /JTerminals/tmpcrd");//Linux
            //Process s = Runtime.getRuntime().exec("del C:\\JTerminals\\tmpcrd");
            //s.waitFor();//do not remove any waitFor .. this is due to file locking errors
            //s = Runtime.getRuntime().exec("sudo cp /SYSTEMS/"+card2copy+" /JTerminals/tmpcrd");
            if (rfh.FindFileFolder("/SYSTEMS/", card2copy) == true) {
                rfh.copySource2Dest("/SYSTEMS/" + card2copy, "C://JTerminals/tmpcrd");
            }
        } catch (Exception ex) {
            log.error(ex.getMessage());
        }
    }

    public void copyCRDPLTtoLAST(String card2copy, String serverIP) {
        card2copy = card2copy + ".crd";
        try {
            //Process s = Runtime.getRuntime().exec("sudo rm /JTerminals/tmpcrd");//Linux
            //Process s = Runtime.getRuntime().exec("del C:\\JTerminals\\tmpcrd");
            //s.waitFor();//do not remove any waitFor .. this is due to file locking errors
            //s = Runtime.getRuntime().exec("sudo cp /SYSTEMS/"+card2copy+" /JTerminals/tmpcrd");
            if (rfh.FindFileFolder("/SYSTEMS/", card2copy) == true) {
                String temp = rfh.readFline("/SYSTEMS/", card2copy, 1);
                rfh.putfile("C://JTerminals/de4Dd87d/CfgJ9rl/", "tmpcrd", temp);
            }
        } catch (Exception ex) {
            log.error(ex.getMessage());
        }
    }

    /**
     *
     * @param CardCheck
     * @param extcard if true = .crd
     * @param Ucase - for Linux case sensitive
     */
    public void getDatafromServerCRDPLT(String CardCheck, boolean extcard, boolean Ucase) {
        String ext = "";
        if (extcard == true) {
            if (Ucase == false) {
                ext = ".crd";
            } else {
                ext = ".CRD";
            }

        } else if (Ucase == false) {
            ext = ".plt";
        } else {
            ext = ".PLT";
        }
        try {
            CRDPLTdata = rfh.readFline("/SYSTEMS/", CardCheck + ext, 1);
            compareCRDPLTdata = rfh.readFline("/SYSTEMS/", CardCheck + ext, 1);
        } catch (Exception ex) {
            log.error(ex.getMessage());
        }

    }

    public void getDatafromtrayCRDPLT() {
        try {
            CRDPLTdata = rfh.readFline("C://JTerminals/de4Dd87d/CfgJ9rl/", "crdtray", 1);
            compareCRDPLTdata = rfh.readFline("C://JTerminals/de4Dd87d/CfgJ9rl/", "crdtray", 1);
        } catch (Exception ex) {
            log.error(ex.getMessage());
        }

    }

    public String getLastSysID() throws IOException {
        String proc = rfh.readFline("C://JTerminals/de4Dd87d/CfgJ9rl/", "tmpcrd", 1);
        return proc.substring(0, 4);
    }

    public boolean getIsLost() {
        boolean lost = false;
        if (parkingData[5].compareToIgnoreCase("false") == 0) {
            lost = false;
        } else {
            lost = true;
        }
        return lost;
    }    

    public String getSysID() {
        /*String proc = null, temp = null;
        try {
            if (CRDPLTdata.equalsIgnoreCase(compareCRDPLTdata) == true) {
                proc = CRDPLTdata;
                temp = proc.substring(0, 4);
            }
            return temp;
        } catch (Exception ex) {
            LogManager.getLogger(ParkersAPI.class.getName()).log(Level.SEVERE, null, ex);
        }
        return temp;
         */
        return parkingData[0];
    }
    
    public void setSysID(String sysID) {
        parkingData[0] = sysID;
    }

    public String getCardID() {
//        String proc = null, temp = null;
//        try {
//            if (CRDPLTdata.equalsIgnoreCase(compareCRDPLTdata) == true) {
//                proc = CRDPLTdata;
//                temp = proc.substring(4, 12);
//            }
//            return temp;
//        } catch (Exception ex) {
//            LogManager.getLogger(ParkersAPI.class.getName()).log(Level.SEVERE, null, ex);
//        }
//        return temp;
        return parkingData[1];
    }
    
    public void setCardID(String cardID) {
        parkingData[1] = cardID;
    }

    public String getPlateID() {
//        String proc = null, temp = null;
//        try {
//            if (CRDPLTdata.equalsIgnoreCase(compareCRDPLTdata) == true)//problems with Memory allocation
//            {
//                proc = CRDPLTdata;
//                temp = proc.substring(12, 16);
//            }
//            return temp;
//        } catch (Exception ex) {
//            LogManager.getLogger(ParkersAPI.class.getName()).log(Level.SEVERE, null, ex);
//        }
//        return temp;
        return parkingData[2];
    }
    
    public void setPlateID(String plateID) {
        parkingData[2] = plateID;
    }
    
    public String getTRID() {
//        String proc = null, temp = null;
//        try {
//            if (CRDPLTdata.equalsIgnoreCase(compareCRDPLTdata) == true) {
//                proc = CRDPLTdata;
//                temp = proc.substring(16, 17);
//            }
//            return temp;
//        } catch (Exception ex) {
//            LogManager.getLogger(ParkersAPI.class.getName()).log(Level.SEVERE, null, ex);
//        }
//        return temp;
        return parkingData[3];
    }
    
    public void setTRID(String trID) {
        parkingData[3] = trID;
    }

    public String getAmountPaid() {
        String paid = "0";
        if (parkingData.length >= 7) {
            paid = parkingData[6];
        }
        return paid;
    }
    
    public void setAmountPaid(String amountPaid) {
        String paid = "0";
        if (parkingData.length >= 7) {
            parkingData[6] = amountPaid;
        }        
        else {
            parkingData[6] = paid;
        }
    }

    public String getLastCardID() throws IOException {
        String proc = rfh.readFline("C://JTerminals/de4Dd87d/CfgJ9rl/", "tmpcrd", 1);
        return proc.substring(4, 10);
    }

    public String getLastPlateID() throws IOException {
        String proc = rfh.readFline("C://JTerminals/de4Dd87d/CfgJ9rl/", "tmpcrd", 1);
        return proc.substring(10, 16);
    }

    public String getLastTRID() throws IOException {
        String proc = rfh.readFline("C://JTerminals/de4Dd87d/CfgJ9rl/", "tmpcrd", 1);
        return proc.substring(16, 17);
    }

    public String getFormattedTimeID() {
        String proc = null, temp = null;
        try {
            if (CRDPLTdata.equalsIgnoreCase(compareCRDPLTdata) == true) {
                proc = CRDPLTdata;
                long timelong = Long.parseLong(proc.substring(17, proc.length()));
                Date timein = new Date(timelong);
                SimpleDateFormat sdf = new SimpleDateFormat("H:mm");
                temp = sdf.format(timein);
            }
            return temp;
        } catch (Exception ex) {
            log.error(ex.getMessage());
        }
        return proc;
    }

    public String getTimeID() {
//        String proc = null, temp = null;
//        try {
//            if (CRDPLTdata.equalsIgnoreCase(compareCRDPLTdata) == true) {
//                proc = CRDPLTdata;
//                temp = proc.substring(17, 30);
//            }
//            return temp;
//        } catch (Exception ex) {
//            LogManager.getLogger(ParkersAPI.class.getName()).log(Level.SEVERE, null, ex);
//        }
//        return proc;

        return dateTimeIN;
    }

    public String getDateTimePaid() {
        return dateTimePaid;
    }

    public String getDateTimeINStamp() {
        return dateTimeINStamp;
    }

    public String getDateTimePaidStamp() {
        return dateTimePaidStamp;
    }

    public String getFormattedDateID() {
        String proc = null, temp = null;
        try {
            if (CRDPLTdata.equalsIgnoreCase(compareCRDPLTdata) == true) {
                proc = CRDPLTdata;
                long datelong = Long.parseLong(proc.substring(17, proc.length()));
                Date datein = new Date(datelong);
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                temp = sdf.format(datein);
            }
            return temp;
        } catch (Exception ex) {
            log.error(ex.getMessage());
        }
        return temp;
    }

    public String getLastTimeID() throws IOException {
        String proc = rfh.readFline("C://JTerminals/de4Dd87d/CfgJ9rl/", "tmpcrd", 1);
        return proc.substring(17, 30);
    }

    public short checkPC(String couponNO) {
        try {
            int lines2check = 0;
            if (rfh.FindFileFolder("C://JTerminals/de4Dd87d/CfgJ9rl/", "couponf.rec") == true) {
                lines2check = rfh.getTotalFLines("C://JTerminals/de4Dd87d/CfgJ9rl/", "couponf.rec");
            }
            int Startint = 0;
            int Endint = 0;
            int Couponint = 0;
            String procline = "";
            for (int x = 1; x <= lines2check; x++) {
                if (rfh.FindFileFolder("C://JTerminals/de4Dd87d/CfgJ9rl/", "couponf.rec") == true) {
                    procline = rfh.readFline("C://JTerminals/de4Dd87d/CfgJ9rl/", "couponf.rec", x);
                    Startint = Integer.parseInt(procline.substring(0, 6));
                    Endint = Integer.parseInt(procline.substring(7, 13));
                }
                Couponint = Integer.parseInt(couponNO);
                if (Startint <= Couponint && Couponint <= Endint) {
                    if (this.checkUnusedPC(couponNO) == true) {
                        return 2;//2 if COUPON is not used and is in range
                    } else {
                        return 1;//1 if COUPON has been used already
                    }
                }
            }
        } catch (Exception ex) {
            log.error(ex.getMessage());
        }
        return 0;//0 if COUPON is not in range
    }

    private boolean checkUnusedPC(String couponNO) {
        try {
            String procline = "";
            int lines2check = 0;
            rfh.CheckFileFolderFailSafe("C://JTerminals/de4Dd87d/CfgJ9rl/", "usedcoup.rec");
            if (rfh.FindFileFolder("C://JTerminals/de4Dd87d/CfgJ9rl/", "usedcoup.rec") == true) {
                lines2check = rfh.getTotalFLines("C://JTerminals/de4Dd87d/CfgJ9rl/", "usedcoup.rec");
            }
            for (int x = 1; x <= lines2check; x++) {
                procline = rfh.readFline("C://JTerminals/de4Dd87d/CfgJ9rl/", "usedcoup.rec", x);
                int usedNum = Integer.parseInt(procline);
                int couponNum = Integer.parseInt(couponNO);
                if (usedNum == couponNum) {
                    return false;
                }
            }
            return true;
        } catch (Exception ex) {
            log.error(ex.getMessage());
        }
        return true;
    }

    public void updateCouponList(String Prepaid2Save)//CHANGE THIS TO FOLDER TYPE - SEPT 14,2008
    {

        rfh.putfile("C://JTerminals/Coupons/", Prepaid2Save, "theoretics");

    }

    public ResultSet retrieveCRDPLT2(String card2check) {
        ResultSet found = null;
        try {
            this.openDB(false);  //Check first subserver
            try {
                String SQL = "select * from crdplt.main where CardNo = '" + card2check + "'";
                found = stmt.executeQuery(SQL);
                if (found.next() == false) {
                    this.close();
                    this.openDB(true);
                    SQL = "select * from crdplt.main where CardNo = '" + card2check + "'";
                    found = stmt.executeQuery(SQL);
                } else {
                    found.previous();
                }
                return found;
            } catch (SQLException ex) {
                log.error(ex.getMessage());

                this.openDB(true);
                String SQL = "select * from crdplt.main where CardNo = '" + card2check + "'";
                found = stmt.executeQuery(SQL);

                return found;
            }
        } catch (Exception ex) {
            log.info("retrieveCRDPLT ==>");
            log.error(ex.getMessage());
        }
        return found;
    }

    public void printLCEPReceipt(String SentinenlID, String Entrypoint, String Plateno, String ParkerType, String TimeOUT, float AmountDue, String RNos, String CashierName) {
        try {
            USBEpsonHandler eh = new USBEpsonHandler();
            //eh.initializePrinter();
            String StrAmount = String.valueOf(AmountDue);
            //String timeElapsed = String.valueOf(HoursParked)+"hrs :"+String.valueOf(MinutesParked)+"mins";
            float VAT = 0;
            float VAmount = 0;

            VAT = (float) (AmountDue * .12);
            VAmount = (float) (AmountDue - VAT);

            eh.Justify((byte) 0);
            String ptemp = this.checkPTypeFromDB(ParkerType);
            eh.printline(".::" + ptemp + "::.");
            eh.feedpaperup((byte) 1);
            eh.setBlack();
            eh.printline("Plate Number : " + Plateno);
            eh.printline("Entry Point : " + Entrypoint);
            eh.printline("Time OUT : " + TimeOUT);
            eh.printline("Total : " + VAmount + "0");
            eh.printline("VAT : " + VAT + "0");
            eh.printline("Amount (incl. TAX): " + StrAmount + "0");
            eh.feedpaperup((byte) 1);
            eh.setRed();
            eh.printline("Cashier : " + CashierName);
            eh.printline(RNos);
            eh.feedpaperup((byte) 1);
            eh.Justify((byte) 1);
            eh.printline("This serves as your");
            eh.printline("Official Receipt");
            eh.printline("Thank you. Please Park with us Again");
            eh.feedpaperup((byte) 1);
            eh.printHEADER(SentinenlID);
            //eh.fullcut();
            eh.closePrinter();
        } catch (Exception ex) {
            log.error(ex.getMessage());
        }
    }

    public void saveReceiptforDUP(String SentinenlID, String Entrypoint, String Plateno, String ParkerType, String TimeIN, String TimeOUT, long HoursParked, long MinutesParked, float AmountDue, String RNos, String CashierName, boolean OvernightOverride) {
        DupReceiptAPI dr = new DupReceiptAPI();
        dr.saveDUPreceipt(SentinenlID, Entrypoint, Plateno, ParkerType, TimeIN, TimeOUT, HoursParked, MinutesParked, AmountDue, RNos, CashierName, OvernightOverride);
    }

    public void printSerialReceipt(String SentinenlID, String Entrypoint, String Plateno, String ParkerType, String TimeIN, String TimeOUT, long HoursParked, long MinutesParked, double AmountDue, String RNos, String CashierName, String settlementRef, String duplicateReceiptHeader) {
        try {
            SerialEpsonHandler eh = new SerialEpsonHandler();
            eh.closePrinter();
            eh.openPrinter();
            //eh.initializePrinter();
            //eh.printHEADER(SentinenlID);

            boolean found = rfh.FindFileFolder("C://JTerminals/Outline/");
            if (found == false) {
                rfh.CreateNewFolder("C://JTerminals/Outline/");
            }
            XMLreader xr = new XMLreader();
            //will overwrite
            String TINno = xr.getElementValue("C://JTerminals/initH.xml", "receiptno");
            String serialno = xr.getElementValue("C://JTerminals/initH.xml", "serialno");
            String headerline1 = xr.getElementValue("C://JTerminals/initH.xml", "headerline1");
            String headerline2 = xr.getElementValue("C://JTerminals/initH.xml", "headerline2");
            String headerline3 = xr.getElementValue("C://JTerminals/initH.xml", "headerline3");
            String MINNumber = xr.getElementValue("C://JTerminals/min.xml", "machineIDno");
            String PermitNumber = xr.getElementValue("C://JTerminals/min.xml", "permitno");
            String HeaderEnabled = xr.getElementValue("C://JTerminals/initH.xml", "header");
            String BIRHeaderEnabled = xr.getElementValue("C://JTerminals/initH.xml", "BIRheader");
            String FooterEnabled = xr.getElementValue("C://JTerminals/initH.xml", "footer");
            String BIRFooterEnabled = xr.getElementValue("C://JTerminals/initH.xml", "BIRfooter");
            String feederlines = xr.getElementValue("C://JTerminals/initH.xml", "feederlines");
            String nonvat = xr.getElementValue("C://JTerminals/initH.xml", "nonvat");
            String settlement = xr.getElementValue("C://JTerminals/initH.xml", "settlement");
            String official = xr.getElementValue("C://JTerminals/initH.xml", "official");
            String message1 = xr.getElementValue("C://JTerminals/initH.xml", "message1");
            String message2 = xr.getElementValue("C://JTerminals/initH.xml", "message2");

            eh.Justify((byte) 1);
            //StringBuilder str = new StringBuilder();
            if (black) {
                eh.setBlack();
                black = false;
            } else {
                eh.setRed();
                black = true;
            }
            if (HeaderEnabled.compareToIgnoreCase("enabled") == 0) {
                eh.printline(headerline1);
                eh.printline(headerline2);
                eh.printline(headerline3);
            }
            if (BIRHeaderEnabled.compareToIgnoreCase("enabled") == 0) {
                eh.printline("VAT REG TIN. " + TINno);
                eh.printline("Serial No.: " + serialno);
                eh.printline("MIN No.:" + MINNumber);
                eh.printline("Permit No.:" + PermitNumber);
            }
            if (official.compareToIgnoreCase("enabled") == 0) {
                eh.printline("Official Receipt Num.:POS-" + SentinenlID + "-" + RNos);
            } else {
                eh.printline("Receipt Num.:POS-" + SentinenlID + "-" + RNos);
            }
            eh.printline(duplicateReceiptHeader);
            eh.Justify((byte) 0);
            eh.feedpaperup((byte) 1);
            eh.printline("Ent ID.:" + Entrypoint);
            eh.printline("Location: POS Reception");
            eh.printline("Plate Number:" + Plateno);
//String SentinenlID, String Entrypoint, String Plateno, String ParkerType, String TimeIN, String TimeOUT, 
//long HoursParked, long MinutesParked, float AmountDue, String RNos, String CashierName, boolean OvernightOverride) {
            eh.printline("Parker Type: " + checkPTypeFromDB(ParkerType));
            eh.printline("TIME IN:" + TimeIN);
            eh.printline("TIME OUT:" + TimeOUT);
            eh.printline("Duration: " + HoursParked + " Hours " + MinutesParked + " Mins");

            if (nonvat.compareToIgnoreCase("enabled") == 0) {
                eh.printline("Amount Paid:  " + AmountDue);
            } else {
                eh.printline("VATable Sales     :  P" + getVatSales(AmountDue));
                eh.printline("VAT Amount (12%)  :  P" + getVat(AmountDue));
            }

            //eh.selectEMstyle(true);
            eh.printline("Total Amount Due  :  P" + AmountDue);
            //eh.startPrinter();
            //eh.selectEMstyle(false);
            //eh.startPrinter();
            //eh.Justify((byte) 0);
            if (settlement.compareToIgnoreCase("enabled") == 0) {
                eh.printline("Name:");
                eh.printline("________________________________");
                eh.printline("Addr:");
                eh.printline("________________________________");
                eh.printline("TIN :");
                eh.printline("________________________________");
                eh.printline("\n");
                eh.printline("Settlement Ref.:  ");
                eh.printline(settlementRef);

            }
            //eh.Justify((byte) 1);
            //eh.selectEMstyle(true);
            eh.printline(message1);
            eh.printline(message2);
            //eh.startPrinter();
            //eh.selectEMstyle(false);
            //eh.Justify((byte) 1);
            if (FooterEnabled.compareToIgnoreCase("enabled") == 0) {
                //eh.printline("ESC a 0\n");//Center 0-1-2 L-C-R
                eh.printline("POS Provider:");
                //eh.printline("ESC a 1\n");//Center 0-1-2 L-C-R
                eh.printline("Applied Modern Theoretics Inc");
                eh.printline("5F Builders Center Bldg.");
                eh.printline("Salcedo Street Legaspi Village");
                eh.printline("Makati Philippines");
                eh.printline("");
            }
            if (BIRFooterEnabled.compareToIgnoreCase("enabled") == 0) {
                eh.printline("THIS SERVES AS YOUR OFFICIAL RECEIPT");
                eh.printline("\n");
                eh.printline("THIS RECEIPT SHALL BE VALID FOR FIVE(5)");
                eh.printline("YEARS FROM THE DATE OF THE PERMIT TO USE");
            }
            eh.selectEMstyle(true);
            eh.feedpaperup((byte) Short.parseShort(feederlines));
            //rfh.putfile("C://JTerminals/Outline/", "p", str.toString());
            eh.fullcut();
            eh.closePrinter();
        } catch (Exception ex) {
            ex.printStackTrace();
            log.error(ex.getMessage());
        }
    }

    public void printUSBReceipt(boolean firstRun, boolean isReprint, String SentinenlID, String Entrypoint,
            String Plateno, String Cardno, String ParkerType, String TimeIN, String TimeOUT, long HoursParked, long MinutesParked,
            double AmountDue, double AmountGross, double vat12, double vatsale, double vatexempt, String RNos, String CashierID, String CashierName, String settlementRef,
            String settlementName,String settlementAddr,String settlementTIN,String settlementBusStyle,
            String duplicateReceiptHeader, boolean isDiscounted, float discountPercentage, double tenderFloat, String changeDue, String discountFloat,
            boolean printerCutter) {
        try {
            USBEpsonHandler eh = new USBEpsonHandler();
            eh.closePrinter();
            eh.openPrinter();
            eh.initializePrinter();
            //eh.printHEADER(SentinenlID);
            //eh.uline(false);
            XMLreader xr = new XMLreader();
            //will overwrite
            /*
            String TINno = xr.getElementValue("C://JTerminals/initH.xml", "receiptno");
            String serialno = xr.getElementValue("C://JTerminals/initH.xml", "serialno");
            String headerline1PrintEnabled = xr.getAttributeValue("C://JTerminals/initH.xml", "headerline1", "print");
            String headerline2PrintEnabled = xr.getAttributeValue("C://JTerminals/initH.xml", "headerline2", "print");
            String headerline3PrintEnabled = xr.getAttributeValue("C://JTerminals/initH.xml", "headerline3", "print");
            String headerline4PrintEnabled = xr.getAttributeValue("C://JTerminals/initH.xml", "headerline4", "print");
            String headerline1 = xr.getElementValue("C:/JTerminals/initH.xml", "headerline1");
            String headerline2 = xr.getElementValue("C:/JTerminals/initH.xml", "headerline2");
            String headerline3 = xr.getElementValue("C:/JTerminals/initH.xml", "headerline3");
            String headerline4 = xr.getElementValue("C:/JTerminals/initH.xml", "headerline4");
            String MINNumber = xr.getElementValue("C://JTerminals/min.xml", "machineIDno");
            String PermitNumber = xr.getElementValue("C://JTerminals/min.xml", "permitno");
            String HeaderEnabled = xr.getElementValue("C://JTerminals/initH.xml", "header");
            String BIRHeaderEnabled = xr.getElementValue("C://JTerminals/initH.xml", "BIRheader");            
            String FooterEnabled = xr.getElementValue("C://JTerminals/initH.xml", "footer");
            String BIRFooterEnabled = xr.getElementValue("C://JTerminals/initH.xml", "BIRfooter");
             */
            String feederlines = xr.getElementValue("C://JTerminals/initH.xml", "feederlines");
            String nonvat = xr.getElementValue("C://JTerminals/initH.xml", "nonvat");
            String settlement = xr.getElementValue("C://JTerminals/initH.xml", "settlement");
            //String official = xr.getElementValue("C://JTerminals/initH.xml", "official");
            String message1 = xr.getElementValue("C://JTerminals/initH.xml", "message1");
            String message2 = xr.getElementValue("C://JTerminals/initH.xml", "message2");

//            eh.Justify((byte) 1);
            //StringBuilder str = new StringBuilder();
            if (black) {
                eh.setBlack();
                black = false;
            } else {
                eh.setRed();
                black = true;
            }
            if (isReprint) {
                eh.printline("          --- REPRINT ---");
            }

            //SAVES ON POS JOURNALS
            if (firstRun) {
                eh.printHEADER(SentinenlID);
            } else {
                eh.printHEADER(SentinenlID);
            }

            //eh.printline("            OFFICIAL RECEIPT");

            eh.printline(duplicateReceiptHeader);

            eh.printline("Official-Receipt # " + RNos);

            eh.startPrinter();
            eh.Justify((byte) 0);
            eh.feedpaperup((byte) 1);
            eh.printline("CardCode      : " + Cardno);
            eh.printline("Plate#        : " + Plateno);
            //eh.printline("Ent ID.:" + Entrypoint);
            //eh.printline("Cashier ID:" + CashierID);
            //eh.printline("Cashier Name:" + CashierName);
//            eh.printline("Location     :" + SentinenlID);
            
//String SentinenlID, String Entrypoint, String Plateno, String ParkerType, String TimeIN, String TimeOUT, 
//long HoursParked, long MinutesParked, float AmountDue, String RNos, String CashierName, boolean OvernightOverride) {
            //String ptype = checkPTypeFromDB(ParkerType);
            eh.printline("Vehicle       : " + ParkerType);
            eh.printline("Time-in       : " + TimeIN);
            eh.printline("Time-Out      : " + TimeOUT);
            eh.printline("Time          : " + HoursParked + " Hrs " + MinutesParked + " Mins");
            eh.printline("---------------------------------------");
            eh.printline("COMPUTATION");

            if (nonvat.compareToIgnoreCase("enabled") == 0) {
                eh.printline("Amount Paid:  " + displayAmount2Decimals(AmountDue));
            } else {
                /*if (isDiscounted) {
                    //Selling Price: 1000
                    //Vat 12%
                    //Formula: VAT Exempt Sales = 1000 / 1.12 = 892.86
                    ///////
                    //Senior Citizen Discount = VAT Exempt Sales * 0.20
                    // 892.86 * 0.20    = 178.57
                    //Amount to be Paid = 892 - 178.57 = 714.29
                    
                     // VAT Exempt = 30 / 1.12 = 26.7857 Senior Citizen Discount
                     // = 26.7857 * 0.20 = 5.3571
                     
                    float grossB4Discount = AmountDue / ((100 - discountPercentage) / 100);
                    if (AmountDue <= 0) {
                        grossB4Discount = 0;
                    }
                    eh.printline("Gross Sales                :  P" + getAmountDue(grossB4Discount));
                    float lessDiscount = grossB4Discount - AmountDue;
                    if (AmountDue <= 0) {
                        lessDiscount = 0;
                    }
                    eh.printline("Less " + ptype + " DSC " + discountPercentage + "% : -P" + getAmountDue(lessDiscount));
                } else {
                    
                }*/
                double change = 0f;
                if (tenderFloat <= AmountDue) {
                    change = 0f;
                } else {
                    change = AmountDue - tenderFloat;
                }
                    eh.printline("Total Payment   : P " + displayAmount2Decimals(AmountGross));
                    eh.printline("Amount Tendered : P " + displayAmount2Decimals(tenderFloat));
                    eh.printline("Change          : P " + changeDue);
                    eh.printline("VAT-SALE        : P " + displayAmount2Decimals(vatsale));
                    eh.printline("VAT-12%         : P " + displayAmount2Decimals(vat12));
                    eh.printline("VAT-EXEMP       : P " + displayAmount2Decimals(vatexempt));
                    eh.printline("Discount        : P " + displayAmount2Decimals(Float.parseFloat(discountFloat)));
                    eh.printline("Amount Due      : P " + displayAmount2Decimals(AmountDue));
                    
//                eh.printline("Zero-Rated Sales           :  P0.00");
            }
            //eh.selectEMstyle(true);
            //eh.printline("TOTAL AMOUNT               :  P" + getAmountDue(AmountDue));
            //eh.startPrinter();
            //eh.selectEMstyle(false);
            //eh.startPrinter();
            //eh.Justify((byte) 0);
            if (settlement.compareToIgnoreCase("enabled") == 0) {
                eh.printline("---------------------------------------");
                eh.printline("***** CUSTOMER INFO *****");
                if (null == settlementName || settlementName.compareToIgnoreCase("") == 0)
                    eh.printline("Customer Name : _____________________");
                else
                    eh.printline("Customer Name :    " + settlementName);
                if (null == settlementAddr || settlementAddr.compareToIgnoreCase("") == 0) {
                    eh.printline("Address       : _____________________");
                    eh.printline("____________________________________");
                }
                else
                    eh.printline("Addr:     " + settlementAddr);
                if (null == settlementTIN || settlementTIN.compareToIgnoreCase("") == 0)
                    eh.printline("TIN-#         : _____________________");
                else
                    eh.printline("TIN-# : " + settlementTIN);
                if (null == settlementBusStyle || settlementBusStyle.compareToIgnoreCase("") == 0)
                    eh.printline("Business Type : ____________________");
                else
                    eh.printline("Business Type :    " + settlementBusStyle);
                /*
                if (isDiscounted) {
                    eh.printline("SC/PWD/OSCA ID No.:  " + settlementRef);
                    eh.printline("NAME      :____________________\n");
                    eh.printline("SIGNATURE :____________________");
                } else if (settlementRef.trim().compareToIgnoreCase("") != 0) {
                    eh.printline("Settlement Ref :  " + settlementRef);
                }
                */
                eh.startPrinter();
            }
            eh.Justify((byte) 1);
            eh.selectEMstyle(true);
            //eh.printline(message1);
            //eh.printline(message2);
            //eh.startPrinter();
            //eh.selectEMstyle(false);
            //eh.Justify((byte) 1);
            eh.printFOOTER(SentinenlID, true);
            eh.printline("THANK YOU FOR PARKING");
            eh.printline("DRIVE SAFELY !!!");
            eh.printline("CASHIER : " + CashierName);
            
            eh.startPrinter();

    
            // IF NOT AUTOCUTTER 
            // Instead of feedpaperup use PrintHeader to feed up Then FULLCUT
            eh.feedpaperup((byte) Short.parseShort(feederlines));
            //eh.printHEADER(SentinenlID);
            if (printerCutter) {
                eh.fullcut();
            }

            eh.closeReceiptFile(SentinenlID);
            eh.closePrinter();
        } catch (Exception ex) {
            log.error(ex.getMessage());
        }
    }

    public void printUSBVOIDReceipt(String VoidID, boolean firstRun, boolean isReprint, String SentinenlID, String Entrypoint,
            String Plateno, String ParkerType, String TimeIN, String TimeOUT, long HoursParked, long MinutesParked,
            double AmountDue, String RNos, String CashierID, String CashierName, String settlementRef,
            String settlementName, String settlementAddr, String settlementTIN, String settlementBusStyle,
            String duplicateReceiptHeader, boolean isDiscounted, float discountPercentage,
            boolean printerCutter) {
        try {
            USBEpsonHandler eh = new USBEpsonHandler();
            eh.closePrinter();
            eh.openPrinter();
            eh.initializePrinter();
            eh.uline(false);
            XMLreader xr = new XMLreader();
            String feederlines = xr.getElementValue("C://JTerminals/initH.xml", "feederlines");
            String nonvat = xr.getElementValue("C://JTerminals/initH.xml", "nonvat");
            String settlement = xr.getElementValue("C://JTerminals/initH.xml", "settlement");
            String message1 = xr.getElementValue("C://JTerminals/initH.xml", "message1");
            String message2 = xr.getElementValue("C://JTerminals/initH.xml", "message2");

            eh.Justify((byte) 1);
            if (black) {
                eh.setBlack();
                black = false;
            } else {
                eh.setRed();
                black = true;
            }
            if (isReprint) {
                eh.printline("          --- REPRINT ---");
            }

            //SAVES ON POS JOURNALS
            if (firstRun) {
                eh.printHEADER(SentinenlID);
            } else {
                eh.printHEADER(SentinenlID);
            }

            eh.printline("     * * * *   V O I D   * * * *     ");
            eh.printline("Void Counter: " + VoidID);
            eh.printline(duplicateReceiptHeader);

            eh.printline("Receipt Num.:" + RNos);

            eh.startPrinter();
            eh.Justify((byte) 0);
            eh.feedpaperup((byte) 1);
            eh.printline("Ent ID.:" + Entrypoint);
            eh.printline("Cashier ID:" + CashierID);
            eh.printline("Cashier Name:" + CashierName);
            eh.printline("Location:" + SentinenlID);
            eh.printline("Plate Number:" + Plateno);
            String ptype = checkPTypeFromDB(ParkerType);
            eh.printline("Parker Type: " + ptype);
            eh.printline("TIME IN:" + TimeIN);
            eh.printline("TIME OUT:" + TimeOUT);
            eh.printline("Duration: " + HoursParked + " Hours " + MinutesParked + " Mins");

            if (nonvat.compareToIgnoreCase("enabled") == 0) {
                eh.printline("Amount Paid:  " + displayAmount2Decimals(AmountDue));
            } else {
                if (isDiscounted) {
                    double grossB4Discount = AmountDue / ((100 - discountPercentage) / 100);
                    if (AmountDue <= 0) {
                        grossB4Discount = 0;
                    }
                    eh.printline("Gross Sales                :  P" + displayAmount2Decimals(grossB4Discount));
                    double lessDiscount = grossB4Discount - AmountDue;
                    if (AmountDue <= 0) {
                        lessDiscount = 0;
                    }
                    eh.printline("Less " + ptype + " DSC " + discountPercentage + "% : -P" + displayAmount2Decimals(lessDiscount));
                } else {
                    eh.printline("Gross Sales                :  P" + displayAmount2Decimals(AmountDue));
                }
                eh.printline("VATable Sales              :  P" + getVatSales(AmountDue));
                eh.printline("VAT Amount (12%)           :  P" + getVat(AmountDue));
                eh.printline("VAT Exempt Sales           :  P0.00");
//                eh.printline("Zero-Rated Sales           :  P0.00");
            }
            eh.printline("TOTAL AMOUNT               :  P" + displayAmount2Decimals(AmountDue));
            if (settlement.compareToIgnoreCase("enabled") == 0) {
                eh.printline("Customer Name:______________________");
                eh.printline("Addr:_______________________________");
                eh.printline("TIN :_______________________________");
                eh.printline("Business Style :____________________");
                if (isDiscounted) {
                    eh.printline("SC/PWD/OSCA ID No.:  " + settlementRef);
                    eh.printline("NAME      :____________________\n");
                    eh.printline("SIGNATURE :____________________");
                } else if (settlementRef.trim().compareToIgnoreCase("") != 0) {
                    eh.printline("" + settlementRef);
                }
                eh.startPrinter();
            }
            eh.Justify((byte) 1);
            eh.selectEMstyle(true);
            eh.printline(message1);
            eh.printline(message2);
            eh.startPrinter();

            eh.printFOOTER(SentinenlID, false);

            eh.selectEMstyle(true);

            eh.feedpaperup((byte) Short.parseShort(feederlines));
            if (printerCutter) {
                eh.fullcut();
            }

            eh.closeReceiptFile(SentinenlID);
            eh.closePrinter();
        } catch (Exception ex) {
            log.error(ex.getMessage());
        }
    }

    public void reprintOut(boolean firstRun, String plate2check, String date2check) {
        try {
            DataBaseHandler dbh = new DataBaseHandler();
            dbh.manualOpen();
            ResultSet rs = dbh.getReceipt4Reprint(plate2check, date2check);

//public void printUSBReceipt(String SentinenlID, String Entrypoint, String Plateno, String ParkerType, String TimeIN, String TimeOUT, long HoursParked, long MinutesParked, float AmountDue, String RNos, String CashierName, String settlementRef, boolean OvernightOverride) {
            while (rs.next()) {
                printUSBReceipt(
                        firstRun,
                        true,
                        rs.getString("PC"),
                        rs.getString("PC"),
                        rs.getString("Plate"),
                        rs.getString("Cardcode"),
                        rs.getString("TYPE"),
                        rs.getString("Timein"),
                        rs.getString("TimeOut"),
                        0L,
                        0L,
                        rs.getFloat("Cash"),
                        rs.getFloat("Total"),
                        0,
                        0,
                        0,
                        rs.getString("TRno"),
                        rs.getString("Operator"),
                        rs.getString("Operator"),
                        "",
                        "",
                        "",
                        "",
                        "",
                        "           ###  REPRINT  ###", false, 0f, 0f, "0.00", "0.00", true);
            }

            dbh.manualClose();
            //dateTimeIN = dbh.getTimeINStamp();

        } catch (Exception ex) {
            log.error(ex.getMessage());
        }
    }

    public void reprintRefund(boolean firstRun, String plate2check, String date2check) {
        try {
            DataBaseHandler dbh = new DataBaseHandler();
            dbh.manualOpen();
            ResultSet rs = dbh.getReceipt4Reprint(plate2check, date2check);

//public void printUSBReceipt(String SentinenlID, String Entrypoint, String Plateno, String ParkerType, String TimeIN, String TimeOUT, long HoursParked, long MinutesParked, float AmountDue, String RNos, String CashierName, String settlementRef, boolean OvernightOverride) {
            while (rs.next()) {
                printUSBReceipt(
                        firstRun,
                        false,
                        rs.getString("ExitID"),
                        rs.getString("EntranceID"),
                        rs.getString("PlateNumber"),
                        rs.getString("CardNumber"),
                        rs.getString("ParkerType"),
                        rs.getString("DateTimeIN"),
                        rs.getString("DateTimeOUT"),
                        rs.getLong("HoursParked"),
                        rs.getLong("MinutesParked"),
                        -rs.getFloat("Cash"),
                        -rs.getFloat("Amount"),
                        0,
                        0,
                        0,
                        rs.getString("ReceiptNumber"),
                        rs.getString("CashierName"),
                        rs.getString("username"),
                        rs.getString("SettlementRef"),
                        rs.getString("SettlementName"),
                        rs.getString("SettlementAddr"),
                        rs.getString("SettlementTIN"),
                        rs.getString("SettlementBusStyle"),
                        "", false, 0f, 0f, "0.00", "0.00", true);
            }

            dbh.manualClose();
            //dateTimeIN = dbh.getTimeINStamp();

        } catch (Exception ex) {
            log.error(ex.getMessage());
        }
    }

    public void reprintRefundByCard(boolean firstRun, String card2check, String date2check, String CashierID) {
        try {
            double refundAmount = 0;
            String currentParkerType = "";

            Random r = new Random();
            int low = 10000;
            int high = 99999;
            int result = r.nextInt(high - low) + low;
            int result2 = (int) new Date().getTime();

            final String alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
            final int N = alphabet.length();
            Random r2 = new Random();
            System.out.print(alphabet.charAt(r2.nextInt(N)));
            String RandomID = "" + alphabet.charAt(r2.nextInt(N)) + result + alphabet.charAt(r2.nextInt(N)) + result2;
            DataBaseHandler dbh = new DataBaseHandler();
            dbh.manualOpen();
            RandomID = dbh.getVoidPkid();
            ResultSet rs = dbh.getReceipt4ReprintByCategory(0, card2check, date2check);

//public void printUSBReceipt(String SentinenlID, String Entrypoint, String Plateno, String ParkerType, String TimeIN, String TimeOUT, long HoursParked, long MinutesParked, float AmountDue, String RNos, String CashierName, String settlementRef, boolean OvernightOverride) {
            while (rs.next()) {
                refundAmount = rs.getFloat("Amount");
                currentParkerType = rs.getString("ParkerType");
                printUSBVOIDReceipt(RandomID,
                        firstRun,
                        false,
                        rs.getString("ExitID"),
                        rs.getString("EntranceID"),
                        rs.getString("PlateNumber"),
                        rs.getString("ParkerType"),
                        rs.getString("DateTimeIN"),
                        rs.getString("DateTimeOUT"),
                        rs.getLong("HoursParked"),
                        rs.getLong("MinutesParked"),
                        -refundAmount,
                        rs.getString("ReceiptNumber"),
                        rs.getString("CashierName"),
                        rs.getString("username"),
                        rs.getString("SettlementRef"),
                        rs.getString("SettlementName"),
                        rs.getString("SettlementAddr"),
                        rs.getString("SettlementTIN"),
                        rs.getString("SettlementBusStyle"),
                        "", false, 0f, true);
            }

            //SAVE TO VOID TABLE
            ResultSet sav = dbh.getReceipt4ReprintByCategory(0, card2check, date2check);
            ParkerDataHandler pdh = new ParkerDataHandler();
            while (sav.next()) {
                pdh.saveEXParkerTrans2VOIDDB(
                        RandomID,
                        sav.getString("ExitID"),
                        "",
                        sav.getString("EntranceID"),
                        sav.getString("ReceiptNumber"),
                        "",
                        sav.getString("CashierName"),
                        sav.getString("CardNumber"),
                        sav.getString("PlateNumber"),
                        sav.getString("ParkerType"),
                        sav.getString("DateTimeIN"),
                        -refundAmount,
                        sav.getLong("HoursParked"),
                        sav.getLong("MinutesParked"),
                        sav.getString("SettlementRef"),
                        sav.getString("SettlementName"),
                        sav.getString("SettlementAddr"),
                        sav.getString("SettlementTIN"),
                        sav.getString("SettlementBusStyle"));
            }
            ResultSet up = dbh.getReceipt4ReprintByCategory(0, card2check, date2check);
            while (up.next()) {
                pdh.updateEXParkerTrans4VOID(
                        RandomID,
                        up.getString("ReceiptNumber"));

                //dbh.setPtypeAmount(up.getString("ParkerType"),"",refundAmount);
                //String refund_count = dbh.getPtypecount(up.getString("ParkerType"),"");
            }
            SaveCollData scd = new SaveCollData();
            scd.SubtractPtypecountDB(currentParkerType, CashierID);
            scd.SubtractPtypeAmountDB(currentParkerType, CashierID, refundAmount);
            scd.UpdateVOIDPtypecountDB("voids", CashierID);
            scd.UpdateVOIDPtypeAmountDB("voids", CashierID, -refundAmount);
            scd.UpdateImptAmountDB("totalAmount", CashierID, -refundAmount);
            scd.UpdateGRANDTOTAL(-refundAmount);
            dbh.manualClose();
            //dateTimeIN = dbh.getTimeINStamp();

        } catch (Exception ex) {
            log.error(ex.getMessage());
        }
    }

    public void reprintRefundByPlate(boolean firstRun, String plate2check, String date2check, String CashierID) {
        try {
            double refundAmount = 0;
            String currentParkerType = "";
            Random r = new Random();
            int low = 10000;
            int high = 99999;
            int result1 = r.nextInt(high - low) + low;

            int result2 = r.nextInt(high - low) + low;

            final String alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
            final int N = alphabet.length();
            Random r2 = new Random();
            String RandomID = "" + alphabet.charAt(r2.nextInt(N)) + result1 + alphabet.charAt(r2.nextInt(N)) + result2;
            DataBaseHandler dbh = new DataBaseHandler();
            dbh.manualOpen();

            RandomID = dbh.getVoidPkid();

            //findReceiptsByCategory
            ResultSet rs = dbh.getReceipt4ReprintByCategory(1, plate2check, date2check);

//public void printUSBReceipt(String SentinenlID, String Entrypoint, String Plateno, String ParkerType, String TimeIN, String TimeOUT, long HoursParked, long MinutesParked, float AmountDue, String RNos, String CashierName, String settlementRef, boolean OvernightOverride) {
            while (rs.next()) {
                refundAmount = rs.getFloat("Amount");
                currentParkerType = rs.getString("ParkerType");
                printUSBVOIDReceipt(RandomID,
                        firstRun,
                        false,
                        rs.getString("ExitID"),
                        rs.getString("EntranceID"),
                        rs.getString("PlateNumber"),
                        rs.getString("ParkerType"),
                        rs.getString("DateTimeIN"),
                        rs.getString("DateTimeOUT"),
                        rs.getLong("HoursParked"),
                        rs.getLong("MinutesParked"),
                        -rs.getFloat("Amount"),
                        rs.getString("ReceiptNumber"),
                        rs.getString("CashierName"),
                        rs.getString("username"),
                        rs.getString("SettlementRef"),
                        rs.getString("SettlementName"),
                        rs.getString("SettlementAddr"),
                        rs.getString("SettlementTIN"),
                        rs.getString("SettlementBusStyle"),
                        "", false, 0f, true);
            }

            //SAVE TO VOID TABLE
            ResultSet sav = dbh.getReceipt4ReprintByCategory(1, plate2check, date2check);
            ParkerDataHandler pdh = new ParkerDataHandler();
            while (sav.next()) {
                pdh.saveEXParkerTrans2VOIDDB(
                        RandomID,
                        sav.getString("ExitID"),
                        "",
                        sav.getString("EntranceID"),
                        sav.getString("ReceiptNumber"),
                        "",
                        sav.getString("CashierName"),
                        sav.getString("CardNumber"),
                        sav.getString("PlateNumber"),
                        sav.getString("ParkerType"),
                        sav.getString("DateTimeIN"),
                        -refundAmount,
                        sav.getLong("HoursParked"),
                        sav.getLong("MinutesParked"),
                        sav.getString("SettlementRef"),
                        sav.getString("SettlementName"),
                        sav.getString("SettlementAddr"),
                        sav.getString("SettlementTIN"),
                        sav.getString("SettlementBusStyle"));
            }
            ResultSet up = dbh.getReceipt4ReprintByCategory(1, plate2check, date2check);
            while (up.next()) {
                pdh.updateEXParkerTrans4VOID(
                        RandomID,
                        up.getString("ReceiptNumber"));
            }
            SaveCollData scd = new SaveCollData();
            scd.SubtractPtypecountDB(currentParkerType, CashierID);
            scd.SubtractPtypeAmountDB(currentParkerType, CashierID, refundAmount);
            scd.UpdateVOIDPtypecountDB("voids", CashierID);
            scd.UpdateVOIDPtypeAmountDB("voids", CashierID, -refundAmount);
            scd.UpdateImptAmountDB("totalAmount", CashierID, -refundAmount);
            scd.UpdateGRANDTOTAL(-refundAmount);
            dbh.manualClose();
            //dateTimeIN = dbh.getTimeINStamp();

        } catch (Exception ex) {
            log.error(ex.getMessage());
        }
    }

    public void reprintRefundByReceipt(boolean firstRun, String receipt2check, String date2check, String CashierID) {
        try {
            double refundAmount = 0;
            String currentParkerType = "";
            Random r = new Random();
            int low = 10000;
            int high = 99999;
            int result1 = r.nextInt(high - low) + low;
            int result2 = r.nextInt(high - low) + low;

            final String alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
            final int N = alphabet.length();
            Random r2 = new Random();
            String RandomID = "" + alphabet.charAt(r2.nextInt(N)) + result1 + alphabet.charAt(r2.nextInt(N)) + result2;
            DataBaseHandler dbh = new DataBaseHandler();
            dbh.manualOpen();
            RandomID = dbh.getVoidPkid();
            ResultSet rs = dbh.getReceipt4ReprintByCategory(2, receipt2check, date2check);

//public void printUSBReceipt(String SentinenlID, String Entrypoint, String Plateno, String ParkerType, String TimeIN, String TimeOUT, long HoursParked, long MinutesParked, float AmountDue, String RNos, String CashierName, String settlementRef, boolean OvernightOverride) {
            while (rs.next()) {
                refundAmount = rs.getFloat("Amount");
                currentParkerType = rs.getString("ParkerType");
                printUSBVOIDReceipt(RandomID,
                        firstRun,
                        false,
                        rs.getString("ExitID"),
                        rs.getString("EntranceID"),
                        rs.getString("PlateNumber"),
                        rs.getString("ParkerType"),
                        rs.getString("DateTimeIN"),
                        rs.getString("DateTimeOUT"),
                        rs.getLong("HoursParked"),
                        rs.getLong("MinutesParked"),
                        -rs.getFloat("Amount"),
                        rs.getString("ReceiptNumber"),
                        rs.getString("CashierName"),
                        rs.getString("username"),
                        rs.getString("SettlementRef"),
                        rs.getString("SettlementName"),
                        rs.getString("SettlementAddr"),
                        rs.getString("SettlementTIN"),
                        rs.getString("SettlementBusStyle"),
                        "", false, 0f, true);
            }

            //SAVE TO VOID TABLE
            ResultSet sav = dbh.getReceipt4ReprintByCategory(2, receipt2check, date2check);
            ParkerDataHandler pdh = new ParkerDataHandler();
            while (sav.next()) {
                pdh.saveEXParkerTrans2VOIDDB(
                        RandomID,
                        sav.getString("ExitID"),
                        "",
                        sav.getString("EntranceID"),
                        sav.getString("ReceiptNumber"),
                        "",
                        sav.getString("CashierName"),
                        sav.getString("CardNumber"),
                        sav.getString("PlateNumber"),
                        sav.getString("ParkerType"),
                        sav.getString("DateTimeIN"),
                        -refundAmount,
                        sav.getLong("HoursParked"),
                        sav.getLong("MinutesParked"),
                        sav.getString("SettlementRef"),
                        sav.getString("SettlementName"),
                        sav.getString("SettlementAddr"),
                        sav.getString("SettlementTIN"),
                        sav.getString("SettlementBusStyle"));
            }
            ResultSet up = dbh.getReceipt4ReprintByCategory(2, receipt2check, date2check);
            while (up.next()) {
                pdh.updateEXParkerTrans4VOID(
                        RandomID,
                        up.getString("ReceiptNumber"));
            }
            SaveCollData scd = new SaveCollData();
            scd.SubtractPtypecountDB(currentParkerType, CashierID);
            scd.SubtractPtypeAmountDB(currentParkerType, CashierID, refundAmount);
            scd.UpdateVOIDPtypecountDB("voids", CashierID);
            scd.UpdateVOIDPtypeAmountDB("voids", CashierID, -refundAmount);
            scd.UpdateImptAmountDB("totalAmount", CashierID, -refundAmount);
            scd.UpdateGRANDTOTAL(-refundAmount);
            dbh.manualClose();
            //dateTimeIN = dbh.getTimeINStamp();

        } catch (Exception ex) {
            log.error(ex.getMessage());
        }
    }

    //Non EPSON Printer
    public void printNEReceipt(String SentinenlID, String Entrypoint, String Plateno, String ParkerType, String TimeIN, String TimeOUT, long HoursParked, long MinutesParked, float AmountDue, String RNos, String CashierName, String settlementRef) {
        try {
            boolean found = rfh.FindFileFolder("C://JTerminals/Outline/");
            if (found == false) {
                rfh.CreateNewFolder("C://JTerminals/Outline/");
            }
            XMLreader xr = new XMLreader();
            //will overwrite
            String TINno = xr.getElementValue("C://JTerminals/initH.xml", "receiptno");
            String serialno = xr.getElementValue("C://JTerminals/initH.xml", "serialno");
            String headerline1 = xr.getElementValue("C://JTerminals/initH.xml", "headerline1");
            String headerline2 = xr.getElementValue("C://JTerminals/initH.xml", "headerline2");
            String headerline3 = xr.getElementValue("C://JTerminals/initH.xml", "headerline3");
            String MINNumber = xr.getElementValue("C://JTerminals/min.xml", "machineIDno");
            String PermitNumber = xr.getElementValue("C://JTerminals/min.xml", "permitno");
            String HeaderEnabled = xr.getElementValue("C://JTerminals/initH.xml", "header");
            String BIRHeaderEnabled = xr.getElementValue("C://JTerminals/initH.xml", "BIRheader");
            String FooterEnabled = xr.getElementValue("C://JTerminals/initH.xml", "footer");
            String BIRFooterEnabled = xr.getElementValue("C://JTerminals/initH.xml", "BIRfooter");
            String feederlines = xr.getElementValue("C://JTerminals/initH.xml", "feederlines");
            String nonvat = xr.getElementValue("C://JTerminals/initH.xml", "nonvat");
            String settlement = xr.getElementValue("C://JTerminals/initH.xml", "settlement");
            String official = xr.getElementValue("C://JTerminals/initH.xml", "official");
            String message1 = xr.getElementValue("C://JTerminals/initH.xml", "message1");
            String message2 = xr.getElementValue("C://JTerminals/initH.xml", "message2");

            StringBuilder str = new StringBuilder();
            str.append("ESC @\n");//Init
            str.append("ESC ! 0\n");//Font A 0or1
            str.append("ESC E 0\n");//Emphasized 0or1
            str.append("ESC a 1\n");//Center 0-1-2 L-C-R
            str.append("ESC SP 1\n");//Init
            str.append("ESC - 0 \n");//Init
            if (HeaderEnabled.compareToIgnoreCase("enabled") == 0) {
                str.append("\"" + headerline1 + "\" CR LF\n");
                str.append("\"" + headerline2 + "\" CR LF\n");
                str.append("\"" + headerline3 + "\" CR LF\n");
                str.append("CR LF\n");
            }
            if (BIRHeaderEnabled.compareToIgnoreCase("enabled") == 0) {
                str.append("\"VAT REG TIN. " + TINno + "\" CR LF\n");
                str.append("\"Serial No.: " + serialno + "\" CR LF\n");
                str.append("\"MIN No.:" + MINNumber + "\" CR LF\n");
                str.append("\"Permit No.:" + PermitNumber + "\" CR LF LF LF\n");
            }
            if (official.compareToIgnoreCase("enabled") == 0) {
                str.append("\"Official Receipt Num.:POS-" + SentinenlID + "-" + RNos + "\" CR LF LF LF\n");
            } else {
                str.append("\"Receipt Num.:POS-" + SentinenlID + "-" + RNos + "\" CR LF LF LF\n");
            }

            //str.append("ESC a 0\n");//Center 0-1-2 L-C-R
            //str.append("ESC ! 0\n");//Font A 0or1
            str.append("\"Ent ID.:" + Entrypoint + "\" CR LF\n");
            str.append("\"Location: POS Reception\" CR LF\n");
            str.append("\"Plate Number:" + Plateno + "\" CR LF\n");

//String SentinenlID, String Entrypoint, String Plateno, String ParkerType, String TimeIN, String TimeOUT, 
//long HoursParked, long MinutesParked, float AmountDue, String RNos, String CashierName, boolean OvernightOverride) {
            str.append("\"Parker Type: " + checkPTypeFromDB(ParkerType) + "\" CR LF\n");
            str.append("\"TIME IN:" + TimeIN + "\" CR LF\n");
            str.append("\"TIME OUT:" + TimeOUT + "\" CR LF\n");
            str.append("\"Duration: " + HoursParked + " Hours " + MinutesParked + " Mins\" CR LF\n");

            if (nonvat.compareToIgnoreCase("enabled") == 0) {
                str.append("\"Amount Paid:  " + AmountDue + "\" CR LF\n");
            } else {
                str.append("\"VATable Sales     :  P" + getVatSales(AmountDue) + "\" CR LF\n");
                str.append("\"VAT Amount (12%)  :  P" + getVat(AmountDue) + "\" CR LF\n");
            }
            str.append("ESC E 1\n");//Emphasized 0or1
            str.append("ESC G 1\n");//Emphasized 0or1
            str.append("\"Total Amount Due  :  P" + AmountDue + "\" CR LF\n");
            str.append("ESC E 0\n");//Emphasized 0or1
            str.append("ESC G 0\n");//Emphasized 0or1
            if (settlement.compareToIgnoreCase("enabled") == 0) {
                str.append("\"Name:\"");
                str.append("ESC - 2\n");
                str.append("\"________________________________\" CR LF\n");
                str.append("ESC - 0\n");
                str.append("\"Addr:\"");
                str.append("ESC - 2\n");
                str.append("\"________________________________\" CR LF\n");
                str.append("ESC - 0\n");
                str.append("\"TIN :\"");
                str.append("ESC - 2\n");
                str.append("\"________________________________\" CR LF\n");
                str.append("CR LF\n");

                str.append("\"Settlement Ref.:  \" CR LF\n");
                str.append("ESC a 1\n");//Center 0-1-2 L-C-R
                str.append("ESC - 0\n");
                str.append("\"" + settlementRef + "\" CR LF CR LF\n");
            }
            str.append("ESC E 1\n");//Emphasized 0or1
            str.append("ESC G 1\n");//Emphasized 0or1
            str.append("\"" + message1 + "\" CR LF CR LF\n");
            str.append("\"" + message2 + "\" CR LF CR LF\n");
            str.append("ESC E 0\n");//Emphasized 0or0
            str.append("ESC G 0\n");//Emphasized 0or0

            if (FooterEnabled.compareToIgnoreCase("enabled") == 0) {
                //str.append("ESC a 0\n");//Center 0-1-2 L-C-R
                str.append("\"POS Provider: \" CR LF CR LF\n");
                //str.append("ESC a 1\n");//Center 0-1-2 L-C-R
                str.append("\" \" CR LF\n");
                str.append("\" \" CR LF\n");
                str.append("\" \" CR LF\n");
                str.append("\" \" CR LF\n");
                str.append("\" \" CR LF\n");
                str.append("\" \" CR LF\n");
                str.append("\" \" CR LF CR LF\n");
            }
            if (BIRFooterEnabled.compareToIgnoreCase("enabled") == 0) {
                str.append("\"THIS SERVES AS YOUR OFFICIAL RECEIPT\" CR LF\n");
                str.append("CR LF\n");
                str.append("\"THIS RECEIPT SHALL BE VALID FOR FIVE(5)\" CR LF\n");
                str.append("\"YEARS FROM THE DATE OF THE PERMIT TO USE\" CR LF\n");
            }
            //str.append("ESC E 1\n");//Emphasized 0or1
            //str.append("ESC a 0\n");//Center 0-1-2 L-C-R

            str.append("ESC d " + feederlines + "\n");//Feed 10 lines
            str.append("GS V 1\n");//Send Cut

            rfh.putfile("C://JTerminals/Outline/", "p", str.toString());

            String pingCmd = "C://JTerminals/senddat.exe C://JTerminals/Outline/p USBPRN0";
            Runtime r = Runtime.getRuntime();
            Process p = r.exec(pingCmd);
            p.waitFor();

            //rfh.deleteFile("C://JTerminals/Outline/", "p");//does not work
        } catch (Exception ex) {
            log.error(ex.getMessage());
        }
    }

    private String displayAmount2Decimals(float AmountDue) {
        if (AmountDue == 0) {
            return "0.00";
        }
        DecimalFormat df2 = new DecimalFormat("#.00");
        return df2.format(AmountDue);

    }
    
    private String displayAmount2Decimals(double AmountDue) {
        if (AmountDue == 0) {
            return "0.00";
        }
        DecimalFormat df2 = new DecimalFormat("#.00");
        return df2.format(AmountDue);

    }

    private String getVatSales(double AmountDue) {
        if (AmountDue == 0) {
            return "0.00";
        }
        DecimalFormat df2 = new DecimalFormat("#.00");
        //float vatSales = (float) (AmountDue * .12);
        double vatSales = (double) (AmountDue / 1.12) * 0.12f;
        AmountDue = AmountDue - vatSales;
        return df2.format(AmountDue) + "";

    }

    private String getVat(double AmountDue) {
        if (AmountDue == 0) {
            return "0.00";
        }
        DecimalFormat df2 = new DecimalFormat("#.00");

        //float vatSales = (float) (AmountDue * .12);
        double vatSales = (double) (AmountDue / 1.12) * 0.12f;
        return df2.format(vatSales);
        //return vatSales + "";
    }

    public void printOldReceipt(String SentinenlID, String Entrypoint, String Plateno, String ParkerType, String TimeIN, String TimeOUT, long HoursParked, long MinutesParked, float AmountDue, String RNos, String CashierName, boolean OvernightOverride) {
        try {
            USBEpsonHandler eh = new USBEpsonHandler();
            //eh.initializePrinter();
            String StrAmount = String.valueOf(AmountDue);
            String timeElapsed = String.valueOf(HoursParked) + "hrs :" + String.valueOf(MinutesParked) + "mins";
            float VAT = 0;
            float VAmount = 0;

            VAT = (float) (AmountDue * .12);
            VAmount = (float) (AmountDue - VAT);

            //eh.Justify((byte) 0);
            String ptemp = this.checkPTypeFromDB(ParkerType);

            if (OvernightOverride == false) {
                eh.printline(".::" + ptemp + "::.");
            } else {
                eh.printline(".::OVERNIGHT PARKER::.");
            }
            eh.feedpaperup((byte) 1);
            eh.setBlack();
            eh.printline("Plate Number : " + Plateno);
            eh.printline("Entry Point : " + Entrypoint);
            eh.printline("Time IN : " + TimeIN);
            eh.printline("Time OUT : " + TimeOUT);
            eh.printline("Hours Parked : " + timeElapsed);
            eh.printline("Total : " + VAmount + "0");
            eh.printline("VAT : " + VAT + "0");
            eh.printline("Amount (incl. TAX): " + StrAmount + "0");
            eh.feedpaperup((byte) 1);
            eh.setRed();
            eh.printline("Cashier : " + CashierName);
            eh.printline(RNos);
            eh.feedpaperup((byte) 1);
            eh.Justify((byte) 1);
            eh.printline("This serves as your");
            eh.printline("Official Receipt");
            eh.printline("Thank you. Please Park with us Again");
            eh.feedpaperup((byte) 1);
            eh.printHEADER(SentinenlID);
            //eh.fullcut();
            eh.closePrinter();
        } catch (Exception ex) {
            log.error(ex.getMessage());
        }
    }

    public void printAutoFlatRateReceipt(USBEpsonHandler eh, String SentinenlID, String Entrypoint, String Plateno, String ParkerType, String TimeIN, String TimeOUT, long HoursParked, long MinutesParked, float AmountDue, String RNos, String CashierName) {
        try {
            //EpsonHandler ea = new USBEpsonHandler();
            //eh.initializePrinter();
            String StrAmount = String.valueOf(AmountDue);

            float VAT = 0;
            float VAmount = 0;

            VAT = (float) (AmountDue * .12);
            VAmount = (float) (AmountDue - VAT);

            eh.Justify((byte) 0);
            String ptemp = this.checkPTypeFromDB(ParkerType);
            eh.printline(".::" + ptemp + "::.");
            eh.feedpaperup((byte) 1);
            eh.setBlack();
            eh.printline("Plate Number : " + Plateno);
            eh.printline("Entry Point : " + Entrypoint);

            eh.printline("Time OUT : " + TimeOUT);

            eh.printline("Total : " + VAmount + "0");
            eh.printline("VAT : " + VAT + "0");
            eh.printline("Amount (incl. TAX): " + StrAmount + "0");
            eh.feedpaperup((byte) 1);
            eh.setRed();
            eh.printline("Cashier : " + CashierName);
            eh.printline(RNos);
            eh.feedpaperup((byte) 1);
            eh.Justify((byte) 1);
            eh.printline("This serves as your");
            eh.printline("Official Receipt");
            eh.printline("Thank you. Please Park with us Again");
            eh.feedpaperup((byte) 1);
            eh.printHEADER(SentinenlID);
//            eh.fullcut();
            eh.closePrinter();
        } catch (Exception ex) {
            log.error(ex.getMessage());
        }
    }

    public boolean delParkerCRDPLT(String Cardno, String serverIP) {
        boolean deletedall = false;
        try {
            String proc = "";
            String PLT = "";
            if (ss.checkPING(serverIP) == true) {
                if (ss.checkOnline() == true) {
                    boolean found = rfh.FindFileFolder("/SYSTEMS/" + Cardno + ".crd");
                    if (found == true) {
                        proc = rfh.readFline("/SYSTEMS/", Cardno + ".crd", 1);
                        PLT = proc.substring(10, 16);
                        //                LINUX
                        Process p = Runtime.getRuntime().exec("sudo chmod 777 /SYSTEMS/" + Cardno + ".crd");
                        p.waitFor();
                        deletedall = rfh.deleteFile("/SYSTEMS/", Cardno + ".crd");
                        if (deletedall == false) {
                            Process p1 = Runtime.getRuntime().exec("sudo rm -f /SYSTEMS/" + Cardno + ".crd");
                            p1.waitFor();
                        }
                    }
                    found = rfh.FindFileFolder("/SYSTEMS/" + PLT + ".plt");
                    if (found == true) {
                        //                LINUX
                        Process pr = Runtime.getRuntime().exec("sudo chmod 777 /SYSTEMS/" + PLT + ".plt");
                        pr.waitFor();
                        deletedall = rfh.deleteFile("/SYSTEMS/", PLT + ".plt");
                        if (deletedall == false) {
                            Process pr2 = Runtime.getRuntime().exec("sudo rm -f /SYSTEMS/" + PLT + ".plt");
                            pr2.waitFor();
                        }
                    }
                }
            }
        } catch (Exception ex) {
            log.error(ex.getMessage());
        }
        return deletedall;
    }

    public boolean delParkerPLTCRD(String Plateno, String serverIP) {
        boolean deletedall = false;
        try {
            String proc = "";
            String CRD = "";
            if (ss.checkPING(serverIP) == true) {
                if (ss.checkOnline() == true) {
                    boolean found = rfh.FindFileFolder("/SYSTEMS/" + Plateno + ".crd");
                    if (found == true) {
                        proc = rfh.readFline("/SYSTEMS/", Plateno + ".crd", 1);
                        CRD = proc.substring(10, 16);
                        //                LINUX
                        Process p = Runtime.getRuntime().exec("sudo chmod 777 /SYSTEMS/" + Plateno + ".crd");
                        p.waitFor();
                        deletedall = rfh.deleteFile("/SYSTEMS/", Plateno + ".crd");
                        if (deletedall == false) {
                            Process p1 = Runtime.getRuntime().exec("sudo rm -f /SYSTEMS/" + Plateno + ".crd");
                            p1.waitFor();
                        }
                    }
                    found = rfh.FindFileFolder("/SYSTEMS/" + CRD + ".crd");
                    if (found == true) {
                        //                LINUX
                        Process pr = Runtime.getRuntime().exec("sudo chmod 777 /SYSTEMS/" + CRD + ".crd");
                        pr.waitFor();
                        deletedall = rfh.deleteFile("/SYSTEMS/", CRD + ".crd");
                        if (deletedall == false) {
                            Process pr2 = Runtime.getRuntime().exec("sudo rm -f /SYSTEMS/" + CRD + ".crd");
                            pr2.waitFor();
                        }
                    }
                }
            }
        } catch (Exception ex) {
            log.error(ex.getMessage());
        }
        return deletedall;
    }

    public boolean delParkerCRDPLT2(String Cardno) throws Exception {
        DataBaseHandler DB = new DataBaseHandler();
        boolean status2;
        try {
            //boolean status2 = stmt.execute("DELETE FROM crdplt.main WHERE CONVERT(`main`.`"+Cardno+"` USING utf8) = '000010' LIMIT 1;");
            conn = DB.getConnection(true);
            stmt = conn.createStatement();
            //status2 = stmt.execute("DELETE FROM crdplt.main WHERE cardNumber = '" + Cardno + "' LIMIT 1;");

        } catch (Exception ex) {
            conn = DB.getConnection(false);
            stmt = conn.createStatement();
            //status2 = stmt.execute("DELETE FROM crdplt.main WHERE cardNumber = '" + Cardno + "' LIMIT 1;");
            //boolean status2 = stmt.execute("DELETE FROM crdplt.main WHERE CONVERT(`main`.`"+Cardno+"` USING utf8) = '000010' LIMIT 1;");
        }
        //return status2;
        return true;
    }

    public void savetoOffline(String Cardno) {
        try {
            boolean found = rfh.FindFileFolder("C://JTerminals/Outline/");
            if (found == false) {
                rfh.CreateNewFolder("C://JTerminals/Outline/");
            }
            //will overwrite
            rfh.putfile("C://JTerminals/Outline/", Cardno, "0");
        } catch (Exception ex) {
            log.error(ex.getMessage());
        }
    }

    public String checkPTypeOld(String ParkerType) {
        String Type = ParkerType;
        if (ParkerType.compareToIgnoreCase("R") == 0) {
            Type = "Regular";
        } else if (ParkerType.compareToIgnoreCase("G") == 0) {
            Type = "Retail GP";
        } else if (ParkerType.compareToIgnoreCase("V") == 0) {
            Type = "VIP";
        } else if (ParkerType.compareToIgnoreCase("M") == 0) {
            Type = "Motorcycle";
        } else if (ParkerType.compareToIgnoreCase("J") == 0) {
            Type = "Joggers";
        } else if (ParkerType.compareToIgnoreCase("C") == 0) {
            Type = "Prepaid Coupon";
        } else if (ParkerType.compareToIgnoreCase("L") == 0) {
            Type = "Lost Card";
        } else if (ParkerType.compareToIgnoreCase("E") == 0) {
            Type = "LCEP";
        } else if (ParkerType.compareToIgnoreCase("P") == 0) {
            Type = "Monthly Prepaid";
        } else if (ParkerType.compareToIgnoreCase("F") == 0) {
            Type = "Flat Rate";
        } else if (ParkerType.compareToIgnoreCase("O") == 0) {
            Type = "Promo Parker";
        } else if (ParkerType.compareToIgnoreCase("Q") == 0) {
            Type = "Q.C. Senior";
        } else if (ParkerType.compareToIgnoreCase("BC") == 0) {
            Type = "BPO Car";
        } else if (ParkerType.compareToIgnoreCase("BM") == 0) {
            Type = "BPO Motor";
        } else if (ParkerType.compareToIgnoreCase("D") == 0) {
            Type = "Delivery";
        } else {
            Type = "NOTFOUND";
        }
        return Type;
    }

    public String checkPTypeFromDB(String ParkerType) {
        DataBaseHandler dbh = new DataBaseHandler();
        String p = dbh.getPtypeName(ParkerType);
        return p;
    }

    public int checkDupReceiptFromPType(String ParkerType) {
        DataBaseHandler dbh = new DataBaseHandler();
        int p = dbh.getDupReceipt(ParkerType);
        return p;
    }

    public boolean getDiscounted(String ParkerType) {
        DataBaseHandler dbh = new DataBaseHandler();
        int p = dbh.getDiscounted(ParkerType);
        if (p == 1) {
            return true;
        }
        return false;
    }

    public float getdiscountPercentage(String ParkerType) {
        DataBaseHandler dbh = new DataBaseHandler();
        float p = dbh.getdiscountPercentage(ParkerType);

        return p;
    }

    public void openDB(boolean mainDB) throws Exception {
        DataBaseHandler DB = new DataBaseHandler();
        conn = DB.getConnection(mainDB);
        stmt = conn.createStatement();

    }

    public void close() {
        try {
            stmt.close();
            conn.close();
        } catch (SQLException ex) {
            log.error(ex.getMessage());
        }

    }

    public void startDataSplit() {
        if (null != CRDPLTdata) {
            parkingData = CRDPLTdata.split(",");
        }
    }

}
