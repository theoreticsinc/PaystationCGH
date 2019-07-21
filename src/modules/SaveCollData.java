/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package modules;

import java.io.IOException;
import org.apache.log4j.LogManager;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import misc.DataBaseHandler;
import misc.LogUtility;
import misc.RawFileHandler;

/**
 *
 * @author Administratorv This saves,update, and deletes all the counters into
 * files
 */
public class SaveCollData {

    RawFileHandler rfh = new RawFileHandler();
    DataBaseHandler dbh = new DataBaseHandler();
    
    static Logger log = LogManager.getLogger(SaveCollData.class.getName());

    private String loginID;
        
    public void UpdatePtypecount(String Ftype) {
        try {
            String newcurr = "";
            boolean foundfile = rfh.FindFileFolder("C://JTerminals/de4Dd87d/CfgJ9rl/", Ftype + ".jrt");
            if (foundfile == true) {
                String curr = rfh.readFline("C://JTerminals/de4Dd87d/CfgJ9rl/", Ftype + ".jrt", 1);

                int newcount = 0;
                int oldcount = Integer.parseInt(curr);
                newcount = oldcount + 1;
                newcurr = String.valueOf(newcount);
            } else {
                newcurr = "1";
            }
            rfh.putfile("C://JTerminals/de4Dd87d/CfgJ9rl/", Ftype + ".jrt", newcurr);
        } catch (Exception ex) {
            log.error(ex.getMessage());
        }

    }
    
    public void UpdateImptCountDB(String fieldName, String logcode) {
        try {
            int oldCount = Integer.parseInt(dbh.getImptCount(fieldName, logcode)) + 1;
            dbh.setImptCount(fieldName, logcode, oldCount);
        } catch (Exception ex) {
            log.error(ex.getMessage());
        }

    }
    
    public void UpdatePtypecountDB(String Ftype, String logcode) {
        try {
            String ptypeName = dbh.getPtypeName(Ftype);
            int oldCount = Integer.parseInt(dbh.getPtypecount(ptypeName, logcode)) + 1;
            dbh.setPtypecount(ptypeName, logcode, oldCount);
        } catch (Exception ex) {
            log.error(ex.getMessage());
        }
    }

    public void UpdateVOIDPtypecountDB(String Ftype, String logcode) {
        try {
            String ptypeName = Ftype;
            int oldCount = Integer.parseInt(dbh.getPtypecount(ptypeName, logcode)) + 1;
            dbh.setPtypecount(ptypeName, logcode, oldCount);
        } catch (Exception ex) {
            log.error(ex.getMessage());
        }
    }

    public void SubtractPtypecountDB(String Ftype, String logcode) {
        try {
            String ptypeName = dbh.getPtypeName(Ftype);
            int oldCount = Integer.parseInt(dbh.getPtypecount(ptypeName, logcode)) - 1;
            dbh.setPtypecount(ptypeName, logcode, oldCount);
        } catch (Exception ex) {
            log.error(ex.getMessage());
        }

    }

    public void UpdatePtypecount(String Ftype, String data) {
        rfh.putfile("C://JTerminals/de4Dd87d/CfgJ9rl/", Ftype + ".jrt", data);
    }

    
    public void ErasePtypeAmount(String Ftype) {
        try {
            String newcurr = "0";
            rfh.putfile("C://JTerminals/FnF/iXyZp12R/", Ftype + ".dat", newcurr);
        } catch (Exception ex) {
            log.error(ex.getMessage());
        }

    }
    
     public void UpdateImptAmountDB(String fieldName, String logcode, Float data) {
        try {
            float oldAmount = dbh.getImptAmount(fieldName, logcode);
            float newAmount = oldAmount + data;
            dbh.setImptAmount(fieldName, logcode, newAmount);
        } catch (Exception ex) {
            log.error(ex.getMessage());
        }
    }   
            
    public void UpdatePtypeAmountDB(String Ftype, String logcode, Float data) {
        try {
            String ptypeName = dbh.getPtypeName(Ftype);
            float oldAmount = dbh.getPtypeAmount(ptypeName, logcode);
            float newAmount = oldAmount + data;
            dbh.setPtypeAmount(ptypeName, logcode, newAmount);
        } catch (Exception ex) {
            log.error(ex.getMessage());
        }
    }
    
    public void UpdateVOIDPtypeAmountDB(String Ftype, String logcode, Float data) {
        try {
            String ptypeName = Ftype;
            float oldAmount = dbh.getPtypeAmount(ptypeName, logcode);
            float newAmount = oldAmount + data;
            dbh.setPtypeAmount(ptypeName, logcode, newAmount);
        } catch (Exception ex) {
            log.error(ex.getMessage());
        }
    }

    public void SubtractPtypeAmountDB(String Ftype, String logcode, Float data) {
        try {
            String ptypeName = dbh.getPtypeName(Ftype);
            float oldAmount = dbh.getPtypeAmount(ptypeName, logcode);
            float newAmount = oldAmount - data;
            dbh.setPtypeAmount(ptypeName, logcode, newAmount);
        } catch (Exception ex) {
            log.error(ex.getMessage());
        }
    }

    public void UpdatePtypeAmount(String Ftype, String data) {
        try {
            String newcurr = "0";
            boolean foundfile = rfh.FindFileFolder("C://JTerminals/FnF/iXyZp12R/", Ftype + ".dat");
            if (foundfile == true) {
                String curr = rfh.readFline("C://JTerminals/FnF/iXyZp12R/", Ftype + ".dat", 1);

                float newcount = Float.parseFloat(data);
                float oldcount = Float.parseFloat(curr);
                newcount = oldcount + newcount;
                newcurr = String.valueOf(newcount);
            } else {
                float newcount = Float.parseFloat(data);
                newcurr = String.valueOf(newcount);
            }
            rfh.putfile("C://JTerminals/FnF/iXyZp12R/", Ftype + ".dat", newcurr);
        } catch (Exception ex) {
            log.error(ex.getMessage());
        }
    }

    public void UpdateSlotsNos(String SentinelID, String serverIP) {
        String newcurr = "";
        String curr = "0";
        if (rfh.FindFileFolder("/SUBSYSTEMS/", SentinelID + "SERV.SER") == true) {
            try {
                curr = rfh.readFline("/SUBSYSTEMS/", SentinelID + "SERV.SER", 1);
            } catch (Exception ex) {
                curr = rfh.readFline("/SUBSYSTEMS/", SentinelID + "SERV.SER", 1);
            }
        } else {
            rfh.putfile("/SUBSYSTEMS/", SentinelID + "SERV.SER", "1");
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
        LogUtility logthis = new LogUtility();
        logthis.setsysLog(SentinelID, "Slots: = " + newcurr);
//        Process s = Runtime.getRuntime().exec("sudo chmod 777 /SUBSYSTEMS/"+ SentinelID + "SERV.SER");
        boolean foundfile = false;
        try {
            foundfile = rfh.FindFileFolder("/SYSTEMS/online.aaa");
        } catch (Exception ex) {
            log.error(ex.getMessage());
        }
        if (foundfile == true) {
            rfh.copySource2Dest("/SUBSYSTEMS/" + SentinelID + "SERV.SER", "/SYSTEMS/" + SentinelID + "SERV.SER");
        }
    }

    public void UpdateCarServed() throws IOException {
        String newcurr = "";
        boolean foundfile = rfh.FindFileFolder("C://JTerminals/de4Dd87d/CfgJ9rl/", "hbrentbay.jrt");
        if (foundfile == true) {
            String curr = rfh.readFline("C://JTerminals/de4Dd87d/CfgJ9rl/", "hbrentbay.jrt", 1);

            int newcount = 0;
            int oldcount = Integer.parseInt(curr);
            newcount = oldcount + 1;
            newcurr = String.valueOf(newcount);
            rfh.putfile("C://JTerminals/de4Dd87d/CfgJ9rl/", "hbrentbay.jrt", newcurr);
        } else {
            newcurr = "1";
        }
        rfh.putfile("C://JTerminals/de4Dd87d/CfgJ9rl/", "hbrentbay.jrt", newcurr);
    }
    
    public void UpdateCarServedDB(String loginID, String carServed) {
        
        dbh.setCarServed(loginID, carServed);
    }
    
    public void UpdateCarServedDB(String trtype, String loginID, String carServed, String totalAmount, String extendedCount, String extendedAmount, String overnightCount, String overnightAmount) throws IOException {
        dbh.setCarServed(trtype, loginID, carServed, totalAmount, extendedCount, extendedAmount, overnightCount, overnightAmount);
    }
    
    public void UpdateReceiptNos() throws IOException {
        String newcurr = "";
        boolean foundfile = rfh.FindFileFolder("C://JTerminals/de4Dd87d/CfgJ9rl/", "trent.jrt");
        if (foundfile == true) {
            String curr = rfh.readFline("C://JTerminals/de4Dd87d/CfgJ9rl/", "trent.jrt", 1);

            int newcount = 0;
            int oldcount = Integer.parseInt(curr);
            newcount = oldcount + 1;
            newcurr = String.valueOf(newcount);
            rfh.putfile("C://JTerminals/de4Dd87d/CfgJ9rl/", "trent.jrt", newcurr);
        } else {
            newcurr = "0";
        }
        rfh.putfile("C://JTerminals/de4Dd87d/CfgJ9rl/", "trent.jrt", newcurr);
    }

    public void UpdateReceiptAmount(float AmountRCPT) throws IOException {
        String newcurr = "";
        boolean foundfile = rfh.FindFileFolder("C://JTerminals/de4Dd87d/CfgJ9rl/", "scrand.jrt");
        if (foundfile == true) {
            String curr = rfh.readFline("C://JTerminals/de4Dd87d/CfgJ9rl/", "scrand.jrt", 1);

            double newcount = 0;
            double oldcount = Double.parseDouble(curr);
            newcount = oldcount + AmountRCPT;
            newcurr = String.valueOf(newcount);
            rfh.putfile("C://JTerminals/de4Dd87d/CfgJ9rl/", "scrand.jrt", newcurr);
        } else {
            newcurr = "0";
        }
        rfh.putfile("C://JTerminals/de4Dd87d/CfgJ9rl/", "scrand.jrt", newcurr);
    }
    
    
    public void UpdateTransactionNum() throws IOException {
        String newcurr = "";
        boolean foundfile = rfh.FindFileFolder("C://JTerminals/FnF/iXyZp12R/", "astrid.jrt");
        if (foundfile == true) {
            String curr = rfh.readFline("C://JTerminals/FnF/iXyZp12R/", "astrid" + ".jrt", 1);

            int newcount = 0;
            int oldcount = Integer.parseInt(curr);
            newcount = oldcount + 1;
            newcurr = String.valueOf(newcount);
            rfh.putfile("C://JTerminals/FnF/iXyZp12R/", "astrid" + ".jrt", newcurr);
        } else {
            newcurr = "0";
        }
        rfh.putfile("C://JTerminals/FnF/iXyZp12R/", "astrid.jrt", newcurr);
    }
    
    public String getGRANDTOTAL() throws IOException {
        String curr = "";
        boolean foundfile = rfh.FindFileFolder("C://JTerminals/FnF/iXyZp12R/", "XOR.jrt");
        if (foundfile == true) {
            curr = rfh.readFline("C://JTerminals/FnF/iXyZp12R/", "XOR" + ".jrt", 1);
        } else {
            curr = "0";
        }
        return curr;
    }

    public void UpdateGRANDTOTAL(float AmountRCPT) throws IOException {
        String newcurr = "";
        boolean foundfile = rfh.FindFileFolder("C://JTerminals/FnF/iXyZp12R/", "XOR.jrt");
        if (foundfile == true) {
            String curr = rfh.readFline("C://JTerminals/FnF/iXyZp12R/", "XOR" + ".jrt", 1);

            double newcount = 0;
            double oldcount = Double.parseDouble(curr);
            newcount = oldcount + AmountRCPT;
            newcurr = String.valueOf(newcount);
            rfh.putfile("C://JTerminals/FnF/iXyZp12R/", "XOR" + ".jrt", newcurr);
        } else {
            newcurr = String.valueOf(AmountRCPT);
        }
        rfh.putfile("C://JTerminals/FnF/iXyZp12R/", "XOR" + ".jrt", newcurr);
//        try {
//            Process s = Runtime.getRuntime().exec("sudo chmod 777 /JTerminals/FnF/iXyZp12R/XOR.jrt");
//            s.waitFor();
//        } catch (InterruptedException ex) {
//            LogManager.getLogger(SaveCollData.class.getName()).log(Level.SEVERE, null, ex);
//        }

    }
    
    public String getPtypeAmount(String Ftype) {
        String newcurr = "0";
        try {
            boolean foundfile = rfh.FindFileFolder("C://JTerminals/FnF/iXyZp12R/", Ftype + ".dat");
            if (foundfile == true) {
                String curr = rfh.readFline("C://JTerminals/FnF/iXyZp12R/", Ftype + ".dat", 1);

                int amt = Integer.parseInt(curr);
                
                newcurr = String.valueOf(amt);
            } else {
                newcurr = "0";
            }
        } catch (Exception ex) {
            log.error(ex.getMessage());
        }
        return newcurr;
    }

    public String getReceiptNos() throws IOException {
        String newReceipt = "";
        boolean foundfile = rfh.FindFileFolder("C://JTerminals/de4Dd87d/CfgJ9rl/", "trent.jrt");
        if (foundfile == true) {
            String curr = rfh.readFline("C://JTerminals/de4Dd87d/CfgJ9rl/", "trent.jrt", 1);
            int oldcount = Integer.parseInt(curr);
            if (oldcount == 0) {
                oldcount = 1;
            }
            newReceipt = String.valueOf(oldcount);
            int stoploop = 12 - newReceipt.length();
            int i = 0;
            do {
                newReceipt = "0" + newReceipt;
                i++;
            } while (i != stoploop);
        } else {
            newReceipt = "000000000000";  //twelve digits
        }
        return newReceipt;
    }

    public String getCarServed() throws IOException {
        String curr = "";
        boolean foundfile = rfh.FindFileFolder("C://JTerminals/de4Dd87d/CfgJ9rl/", "hcornell.jrt");
        if (foundfile == true) {
            curr = rfh.readFline("C://JTerminals/de4Dd87d/CfgJ9rl/", "hcornell.jrt", 1);
        } else {
            curr = "0";
        }
        return curr;
    }

    public String getExitCarServed() {
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
    }

    public void ResetExitCarServed() throws IOException {
        rfh.putfile("C://JTerminals/de4Dd87d/CfgJ9rl/", "hbrentbay.jrt", "0");
    }

    public void ResetEntryTicketsServed() throws IOException {
        rfh.putfile("C://JTerminals/de4Dd87d/CfgJ9rl/", "hbugs.jrt", "0");
    }

    public void ResetExitTicketsServed() throws IOException {
        rfh.putfile("C://JTerminals/de4Dd87d/CfgJ9rl/", "hbacta.jrt", "0");
    }

    public void ResetCurrReceipt_Counter() throws IOException {
        rfh.putfile("C://JTerminals/de4Dd87d/CfgJ9rl/", "scrand.jrt", "0");
    }

    public void saveZRead(String logID, String Exitpoint, String lastTransaction, String logcode) {
        try {
            String receiptNos = getReceiptNos();
            String grandTotal = getGRANDTOTAL();
            //String transaction = dbh.getTransactionNos();
            dbh.saveZReadLogIn(logID, Exitpoint, receiptNos, grandTotal, lastTransaction, logcode);
        } catch (IOException ex) {
            ex.printStackTrace();
            log.error(ex.getMessage());
        }
    }
    
    
    public void updateZRead(String logID, String Exitpoint, String lastTransaction, String logcode, String totalAmount, String vatSale, String vat12Sale) {
        try {
            String endingReceiptNos = getReceiptNos();
            String endingGrandTotal = getGRANDTOTAL();
            //String transaction = dbh.getTransactionNos();
            dbh.saveZReadLogOut(logID, Exitpoint, endingReceiptNos, endingGrandTotal, lastTransaction, logcode, totalAmount, vatSale, vat12Sale);
        } catch (IOException ex) {
            ex.printStackTrace();
            log.error(ex.getMessage());
        }
    }
    
    public String formatNos(String newReceipt) {
        int stoploop = 10 - newReceipt.length();
        int i = 0;
        do {
            newReceipt = "0" + newReceipt;
            i++;
        } while (i != stoploop);

        return newReceipt;
    }
}
