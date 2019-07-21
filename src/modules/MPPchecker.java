/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package modules;

import java.io.IOException;
import java.util.Hashtable;
import org.apache.log4j.LogManager;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import misc.RawFileHandler;

/**
 *
 * @author teller2
 */
public class MPPchecker {

    static Logger log = LogManager.getLogger(MPPchecker.class.getName());
    
    RawFileHandler rfh = new RawFileHandler();
//    public String[] SysMsg;
//    public MPPchecker() throws Exception
//    {
//        SysMsg  = new String[9];
//    }
    public String[] SysMsg;

    public MPPchecker() {
        SysMsg = new String[20];
    }

    public boolean isvalidMPP(String plateno) throws IOException {
        String MPPRECORD = plateno.substring(0, 1).toUpperCase() + "MPP.REC";
        boolean found = rfh.FindFileFolder("C://JTerminals/", MPPRECORD);
        if (found == false) {
            MPPRECORD = plateno.substring(0, 1).toLowerCase() + "mpp.rec";
            found = rfh.FindFileFolder("C://JTerminals/", MPPRECORD);
        }
        return found;
    }

    public Hashtable getdatafromPlateNo(String plateno) throws IOException {        //216989,XPK285,XPK285,080511
        String procline = "";
        String MppData[] = new String[4];
        Hashtable MPPhash = new Hashtable();
        boolean found = false;
        String MPPRECORD = "";
        int x = 1;
        if (plateno.length() > 0) {
            found = rfh.FindFileFolder("C://JTerminals/", plateno.substring(0, 1).toUpperCase() + "MPP.REC".toUpperCase());
            if (found == false) {
                MPPRECORD = plateno.substring(0, 1).toLowerCase() + "mpp.rec";
                found = rfh.FindFileFolder("C://JTerminals/", MPPRECORD);
            } else {
                MPPRECORD = plateno.substring(0, 1).toUpperCase() + "MPP.REC";
            }
        }
        if (found == true) {
            while (procline != null) {
                procline = rfh.readFline("C://JTerminals/", MPPRECORD, x);
                MppData = procline.split(",");
                if (plateno.compareToIgnoreCase(MppData[1]) == 0 || plateno.compareToIgnoreCase(MppData[2]) == 0) {
                    MPPhash.put("cardno", MppData[0].replace("*", " ").trim());
                    MPPhash.put("plate1", MppData[1]);
                    MPPhash.put("plate2", MppData[2]);
                    MPPhash.put("expiration", "200" + MppData[3]);
                    return MPPhash;
                }
                x++;
            }
        }
        return MPPhash;
    }
//    public Hashtable getdatafromCardNo(String plateno) throws IOException
//    {        
//            String procline = "";
//            String checkline = "";
//            Hashtable MPPhash = new Hashtable();
//            
//            int x = 1;
//            boolean found = rfh.FindFileFolder("/SYSTEMS", plateno.substring(0, 1).toLowerCase()+"mpp.rec");
//            if (found==true)
//            {   
//                while(procline!=null)
//                {
//                    procline = rfh.readFline("/SYSTEMS/", plateno.substring(0, 1).toLowerCase()+"mpp.rec", x);
//                    checkline = procline.substring(0, 7);
//
//                    if (plateno.compareToIgnoreCase(checkline)==0)
//                    {
//                        MPPhash.put("cardno", procline.substring(0, 7));
//                        MPPhash.put("plate1", procline.substring(9, 15));
//                        MPPhash.put("plate2", procline.substring(16, 22));
//                        MPPhash.put("expiration", procline.substring(23, 29));
//                        return MPPhash;        
//                    }
//                    x++;
//                }
//            }
//            return MPPhash;   
//    }

    public void checkMPPRec(String serverIP, String PlateInput2) {
        try {
            if (isvalidMPP(PlateInput2.substring(0, 6)) == true) {
                Hashtable temphash = getdatafromPlateNo(PlateInput2.substring(0, 6));
                if (temphash.containsKey("cardno") == true) {
                    SysMsg[0] = "MPP VALID";
                    SysMsg[1] = "";
                    SysMsg[2] = "CARD NO: " + temphash.get("cardno").toString();
                    SysMsg[3] = "";
                    SysMsg[4] = "";
                    SysMsg[5] = "PLATE NO<1>: " + temphash.get("plate1").toString();
                    SysMsg[6] = "PLATE NO<2>: " + temphash.get("plate2").toString();
                    SysMsg[7] = "";
                    SysMsg[8] = "Expires: " + temphash.get("expiration").toString();
                    SysMsg[9] = "";
                }
            } else {
                SysMsg[1] = "MPP NOT FOUND";
            }
        } catch (IOException ex) {
            log.error(ex.getMessage());
        }
    }

    public static void main(String[] args) throws Exception {
        MPPchecker mpc = new MPPchecker();
        Hashtable temphash = mpc.getdatafromPlateNo("RACKS1");
        if (temphash.containsKey("cardno") == true) {
            log.info(temphash.get("cardno").toString());
            log.info(temphash.get("plate1").toString());
            log.info(temphash.get("plate2").toString());
            log.info(temphash.get("expiration").toString());
        }
        //XNJ805
    }
}
