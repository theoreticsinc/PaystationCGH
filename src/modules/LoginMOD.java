/*
 * Combination of Entrance and Exit
 * LoginAPI = entrance
 * 
 */
package modules;

import UserInteface.HybridPanelUI;
import api.CashierAPI;
import misc.USBEpsonHandler;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.apache.log4j.LogManager;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import misc.DateConversionHandler;
import misc.LogUtility;
import misc.RawFileHandler;
import misc.XMLreader;
import misc.DataBaseHandler;

/**
 *
 * @author Administrator
 */
public class LoginMOD extends javax.swing.JPanel {

    public String[] SysMsg = new String[10];
    LogUtility logthis = new LogUtility();
    private RawFileHandler rfh = new RawFileHandler();
    private String login_id;

    static Logger log = LogManager.getLogger(LoginMOD.class.getName());

    public boolean isReadable() throws Exception {
        XMLreader xr = new XMLreader();
        String ID = xr.getElementValue("C://JTerminals/ginH.xml", "cashier_id");
        String Name = xr.getElementValue("C://JTerminals/ginH.xml", "cashier_name");
        if ((Name.compareTo("") != 0) && (ID.compareTo("") != 0)) {
            return true;
        }
        return false;
    }

    public boolean saveLogintoFile(Date logStamp, String logID, String logcode, String logname) throws FileNotFoundException, IOException {
//        Writer writer = null;
//        Node node1 = null;
//        //node1.setNodeValue("logfile");
//        writer = new BufferedWriter(new OutputStrehmWriter(System.out));
//        testXMLwriter xm = new testXMLwriter(writer);
//        xm.write(node1);

//        DateFormat df = DateFormat.getDateInstance(DateFormat.MEDIUM);
//        DateFormat tf = DateFormat.getTimeInstance(DateFormat.MEDIUM);
//        int month = LogStamp.getMonth() + 1;
//        int yehr = LogStamp.getYehr() - 100;
        String out = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<log>\n<cashier_id>" + logcode + "</cashier_id>\n"
                + "<log_id>" + logID + "</log_id>\n"
                + "<cashier_name>" + logname + "</cashier_name>\n<logintime>" + logStamp.getTime() + "</logintime>\n"
                + "<logindate>" + logStamp.getTime() + "</logindate>\n<loosechange>000</loosechange>\n"
                + "<validation>\n<key></key>\n</validation>\n</log>";
        RawFileHandler rfh = new RawFileHandler();
        rfh.putfile("C://JTerminals/", "ginH.xml", out);
        return false;
    }

    public String getLOGINDATcashiernameFromDB(String loginCode, String password) throws IOException {

        DataBaseHandler dbh = new DataBaseHandler();
        String name = dbh.getLoginUsername(loginCode, password);

        return name;
    }

    public String getLOGINDATcashiername(String logcode) throws IOException {
        String logname = "";
        String linefile = "";
        String compcode = "";
        int midline = 0;
        RawFileHandler rfh = new RawFileHandler();
        String loginfile = "";

        if (rfh.FindFileFolder("C://JTerminals/LOGIN/", logcode.substring(7, 8) + "LOGIN.DAT") == false) {
            loginfile = logcode.substring(7, 8) + "login.dat";
        } else {
            loginfile = logcode.substring(7, 8) + "LOGIN.DAT";
        }

        if (rfh.FindFileFolder("C://JTerminals/LOGIN/", loginfile) == true) {
            int loop = rfh.getTotalFLines("C://JTerminals/LOGIN/", loginfile);
            int i = 1;
            do {
                linefile = rfh.readFline("C://JTerminals/LOGIN/", loginfile, i);
                linefile = linefile.replaceAll(" ", "");
                if (linefile.length() == 12) {
                    midline = 6;
                } else {
                    midline = 8;
                }
                compcode = linefile.substring(0, midline);
                if (compcode.compareToIgnoreCase(logcode) == 0) {
                    logname = linefile.substring(midline, linefile.length());
                    return logname;
                }
                i++;
            } while (loop >= i);
        }
        return logname;
    }

    public String getCashierID() throws Exception {
        XMLreader xr = new XMLreader();
        String CID = xr.getElementValue("C://JTerminals/ginH.xml", "cashier_id");
        return CID;
    }

    public boolean getCashierPassword(String logCode, String password) throws Exception {
        DataBaseHandler dbh = new DataBaseHandler();
        boolean pword = dbh.getLoginPassword(logCode, password);
        return pword;
    }

    public String getCashierName() throws Exception {
        XMLreader xr = new XMLreader();
        String CN = xr.getElementValue("C://JTerminals/ginH.xml", "cashier_name");
        CN = CN.replaceAll(" ", "");
        return CN;
    }

    public void clearCollectReceipt() throws IOException {
        RawFileHandler rfh = new RawFileHandler();
        rfh.putfile("C://JTerminals/de4Dd87d/CfgJ9rl/", "collect.jrt", "");
    }

    private void sendColl2USBPrinter(int i, String Exitpoint, String CName, String LoginDate, String LoginTime, String Loosechange, String LogoutDate, String LogoutTime,
            String RegularParkers, String MotorcycleParkers, String QCSeniorParkers, String GraceParkers, String VIPParkers, String LOSTParkers, String LCEPParkers,
            String InvalidFlatRateParkers, String DeliveryParkers, String NonQCSeniorParkers, String BPOMotorParkers, String CarServed, String ReceiptServed) {

        try {
            StringBuilder str = new StringBuilder();
            str.append("ESC @\n");//Init
            str.append("ESC ! 0\n");//Font A 0or1
            str.append("ESC E 0\n");//Emphasized 0or1
            str.append("ESC a 0\n");//Center 0-1-2 L-C-R
            str.append("ESC SP 1\n");//Init
            str.append("ESC - 0 \n");//Init

            str.append("CR LF\n");

            str.append("ESC a 0\n");//Center 0-1-2 L-C-R

            str.append("\"Record No : " + i + "\" CR LF\n");
            str.append(" CR LF\n");
            str.append("\"Terminal ID : \"" + Exitpoint + "\" CR LF\n");
            str.append("\"Cashier Code: ****\" CR LF\n");//+CID
            str.append("\"" + CName + "\" CR LF\n");
            str.append("\"" + LoginDate + "\" CR LF\n");
            //str.append("\"" + LoginTime + "\" CR LF\n");
            //str.append("\"" + Loosechange + "\" CR LF\n");
            str.append("\"" + LogoutDate + "\" CR LF\n");
            //str.append("\"" + LogoutTime + "\" CR LF\n");
            //delay(1000);
            str.append("\"                   Count  Amount\" CR LF\n");
            str.append("\"" + RegularParkers + "\" CR LF\n");
            str.append("\"" + MotorcycleParkers + "\" CR LF\n");
            str.append("\"" + QCSeniorParkers + "\" CR LF\n");
            str.append("\"" + GraceParkers + "\" CR LF\n");
            str.append("\"" + DeliveryParkers + "\" CR LF\n");
            str.append("\"" + VIPParkers + "\" CR LF\n");
            str.append("\"" + LOSTParkers + "\" CR LF\n");
            str.append("\"" + LCEPParkers + "\" CR LF\n");
            str.append("\"" + InvalidFlatRateParkers + "\" CR LF\n");
            str.append("\"" + DeliveryParkers + "\" CR LF\n");
            str.append("\"" + NonQCSeniorParkers + "\" CR LF\n");
            str.append("\"" + BPOMotorParkers + "\" CR LF\n");

            str.append("\"" + CarServed + "\" CR LF\n");
            str.append("\"" + ReceiptServed + "\" CR LF\n");

            str.append("CR LF\n");
            str.append("\"Date Printed: " + new Date().toString() + "\" CR LF\n");

            str.append("ESC E 1\n");//Emphasized 0or1
            str.append("ESC a 0\n");//Center 0-1-2 L-C-R

            str.append("ESC d 10\n");//Feed 10 lines
            str.append("GS V 1\n");//Send Cut

            rfh.putfile("C://JTerminals/Outline/", "coll", str.toString());

            String pingCmd = "C://JTerminals/senddat.exe C://JTerminals/Outline/coll USBPRN0";
            Runtime r = Runtime.getRuntime();
            Process p = r.exec(pingCmd);
            p.waitFor();
        } catch (Exception ex) {
            log.error(ex.getMessage());
        }

    }

    private void sendZRead2USBEpsonPrinter(int i, String Exitpoint, String Title, String datePrint, String line0, String line1, String line2, String line3, String line4,
            String line5, String line6, String line7, String line8, String line9, String line10, String line11) {

        try {
            XMLreader xr = new XMLreader();
            String feederlines = xr.getElementValue("C://JTerminals/initH.xml", "feederlines");
            USBEpsonHandler eh = new USBEpsonHandler();
            eh.closePrinter();
            eh.openPrinter();
            eh.initializePrinter();
            eh.setBlack();
            eh.feedpaperup((byte) 1);

            eh.Justify((byte) 1);
            eh.printline(Title);
            eh.Justify((byte) 0);
            eh.printline(datePrint);
            eh.printline("\n");
            eh.printline(line0);
            eh.printline(line1);
            eh.printline(line2);
            eh.printline(line3);
            eh.printline(line4);
            eh.printline(line5);
            eh.printline(line6);
            eh.printline(line7);
            eh.printline(line8);
            eh.printline(line9);
            eh.printline(line10);
            eh.printline(line11);

            eh.startPrinter();
            //eh.feedpaperup((byte) Short.parseShort(feederlines));
            eh.feedpaperup((byte) 1);
            //eh.fullcut();
            eh.closeReceiptFile(Exitpoint);
            eh.closePrinter();
        } catch (Exception ex) {
            log.error(ex.getMessage());
        }
    }

    private void sendColl2USBEpsonPrinter(int i, String Exitpoint, String CName, String LoginDate, String LoginTime, String Loosechange, String LogoutDate, String LogoutTime,
            String RegularParkers, String MotorcycleParkers, String QCSeniorParkers, String GraceParkers, String VIPParkers, String LOSTParkers, String LCEPParkers,
            String InvalidFlatRateParkers, String DeliveryParkers, String NonQCSeniorParkers, String CarServed, String ReceiptServed) {

        try {
            XMLreader xr = new XMLreader();
            String feederlines = xr.getElementValue("C://JTerminals/initH.xml", "feederlines");
            USBEpsonHandler eh = new USBEpsonHandler();
            eh.closePrinter();
            eh.openPrinter();
            eh.initializePrinter();
            eh.Justify((byte) 0);
            eh.setBlack();
            eh.feedpaperup((byte) 2);
            if ((i % 4) == 0) {
                eh.setBlack();
                delay(3000);
            } else if ((i % 2) == 0) {
                delay(3000);
                eh.setRed();
            }
            eh.printline("Record No : " + i + "\n");
            //eh.feedpaperup((byte) 1);
            eh.printline("Terminal ID : " + Exitpoint);
            eh.printline("Cashier Code: ****");//+CID
            eh.printline(CName);
            eh.printline(LoginDate);
            eh.printline(LoginTime);
            eh.printline(Loosechange);
            eh.printline(LogoutDate);
            eh.printline(LogoutTime);
            //delay(1000);
            eh.printline(RegularParkers);
            eh.printline(MotorcycleParkers);
            eh.printline(GraceParkers);
            eh.printline(QCSeniorParkers);
            eh.printline(DeliveryParkers);
            eh.printline(VIPParkers);
            eh.printline(NonQCSeniorParkers);
            eh.printline(LOSTParkers);
            eh.printline(LCEPParkers);
            eh.printline(InvalidFlatRateParkers);
            //eh.printline(PromoParkers);
            eh.printline("\n");
            eh.printline(CarServed);
            eh.printnumline(ReceiptServed);
            eh.startPrinter();
            eh.feedpaperup((byte) Short.parseShort(feederlines));
            eh.feedpaperup((byte) 2);
            eh.fullcut();
            eh.closePrinter();
        } catch (Exception ex) {
            log.error(ex.getMessage());
        }
    }

    private void sendColl2EpsonPrinter(int i, String Exitpoint, String CName, String LoginDate, String LoginTime, String Loosechange, String LogoutDate, String LogoutTime,
            String RegularParkers, String MotorcycleParkers, String QCSeniorParkers, String GraceParkers, String VIPParkers, String LOSTParkers, String LCEPParkers,
            String InvalidFlatRateParkers, String DeliveryParkers, String NonQCSeniorParkers, String BPOMotorParkers, String CarServed, String ReceiptServed) {

        try {
            XMLreader xr = new XMLreader();
            String feederlines = xr.getElementValue("C://JTerminals/initH.xml", "feederlines");
            USBEpsonHandler eh = new USBEpsonHandler();
            eh.closePrinter();
            eh.openPrinter();
            eh.initializePrinter();
            eh.Justify((byte) 0);
            eh.setBlack();
            eh.feedpaperup((byte) 2);
            if ((i % 4) == 0) {
                eh.setBlack();
                delay(3000);
            } else if ((i % 2) == 0) {
                delay(3000);
                eh.setRed();
            }
            eh.printline("Record No : " + i);
            eh.feedpaperup((byte) 1);
            eh.printline("Terminal ID : " + Exitpoint);
            eh.printline("Cashier Code: ****");//+CID
            eh.printline(CName);
            eh.printline(LoginDate);
            eh.printline(LoginTime);
            eh.printline(Loosechange);
            eh.printline(LogoutDate);
            eh.printline(LogoutTime);
            //delay(1000);
            eh.printline(RegularParkers);
            eh.printline(MotorcycleParkers);
            eh.printline(GraceParkers);
            eh.printline(QCSeniorParkers);
            eh.printline(DeliveryParkers);
            eh.printline(VIPParkers);
            eh.printline(LOSTParkers);
            eh.printline(LCEPParkers);
            eh.printline(InvalidFlatRateParkers);
            //eh.printline(PromoParkers);

            eh.feedpaperup((byte) 1);
            eh.printline(CarServed);
            eh.printnumline(ReceiptServed);

            eh.feedpaperup((byte) Short.parseShort(feederlines));
            eh.feedpaperup((byte) 2);
            eh.fullcut();
            eh.closePrinter();
        } catch (Exception ex) {
            log.error(ex.getMessage());
        }
    }

    private void oldsendColl2EpsonPrinter(int i, String Exitpoint, String CName, String LoginDate, String LoginTime, String Loosechange, String LogoutDate, String LogoutTime,
            String RegularParkers, String MotorcycleParkers, String QCSeniorParkers, String GraceParkers, String VIPParkers, String LOSTParkers, String LCEPParkers,
            String InvalidFlatRateParkers, String PromoParkers, String CarServed, String ReceiptServed) {
        try {
            USBEpsonHandler eh = new USBEpsonHandler();
            eh.initializePrinter();
            eh.Justify((byte) 0);
            eh.setBlack();
            eh.feedpaperup((byte) 2);
            if ((i % 4) == 0) {
                eh.setBlack();
                delay(3000);
            } else if ((i % 2) == 0) {
                delay(3000);
                eh.setRed();
            }
            eh.printline("Record No : " + i);
            eh.feedpaperup((byte) 1);
            eh.printline("Terminal ID : " + Exitpoint);
            eh.printline("Cashier Code: ****");//+CID
            eh.printline(CName);
            eh.printline(LoginDate);
            eh.printline(LoginTime);
            eh.printline(Loosechange);
            eh.printline(LogoutDate);
            eh.printline(LogoutTime);
            //delay(1000);
            eh.printline(RegularParkers);
            eh.printline(MotorcycleParkers);
            eh.printline(GraceParkers);
            eh.printline(QCSeniorParkers);
            eh.printline("Delivery Parkers   : 0");
            eh.printline(VIPParkers);
            eh.printline("OCLP Parkers       : 0");
            eh.printline(LOSTParkers);
            eh.printline(LCEPParkers);
            eh.printline(InvalidFlatRateParkers);
            eh.printline(PromoParkers);

            eh.feedpaperup((byte) 1);
            eh.printline(CarServed);
            eh.printnumline(ReceiptServed);

            eh.feedpaperup((byte) 2);
            eh.closePrinter();
        } catch (Exception ex) {
            log.error(ex.getMessage());
        }
    }

    public void printAllofTodaysZReadFromDB(String Exitpoint) {
        String accumulatedTotal = "";
        try {
            XMLreader xr = new XMLreader();
            login_id = xr.getElementValue("C://JTerminals/ginH.xml", "log_id");
            String resetCount = xr.getElementValue("C://JTerminals/initH.xml", "resetCount");
            SaveCollData scd = new SaveCollData();
            DataBaseHandler dbh = new DataBaseHandler();

            Float totalCollected = dbh.getImptAmount("totalAmount", login_id);
            Double Sale12Vat = (double) (totalCollected / 1.12) * 0.12f;
            //Double Sale12Vat = (double) totalCollected * 0.12;
            Double vatSale = totalCollected - Sale12Vat;

            ResultSet rs = dbh.getTodaysZReadbydateColl(totalCollected, Sale12Vat, vatSale);

            String receiptNos = scd.getReceiptNos();
            String grandTotal = scd.getGRANDTOTAL();
            String lastTransaction = dbh.getLastTransaction(Exitpoint);

            int i = 1;
            while (rs.next()) {
                String terminalnum = rs.getString("terminalnum");
                String datetimeOut = rs.getString("CURRENT_TIMESTAMP");
                String todaysale_dbl = rs.getString("TODAYSALE");
                String vatablesale_dbl = rs.getString("VATABLESALE");
                String vat12_dbl = rs.getString("VAT12");
                String beginOR = rs.getString("BEGINOR");
                String endOR = receiptNos;
                String beginTrans = rs.getString("beginTrans");
                String endTrans = lastTransaction;
                String oldGrand = rs.getString("oldGrand");
                String newGrand = grandTotal;
                String startZCount = rs.getString("startZCount");
                String endZCount = rs.getString("endZCount");
                //String tellerCode = rs.getString("tellerCode");
                //String logINID = rs.getString("logINID");

                terminalnum = "Terminal N0:   " + terminalnum;
                datetimeOut = "Date Printed:  " + datetimeOut.substring(0, 16);
                String todaysale = "Today's Sales      : " + todaysale_dbl;
                String vatablesale = "VAT Sales          : " + vatablesale_dbl;
                String vat12 = "12% VAT Sales      : " + vat12_dbl;
                beginOR = "Beginning OR       :" + Exitpoint + beginOR;
                endOR = "Ending OR          :" + Exitpoint + endOR;
                beginTrans = "Beginning Trans No :" + beginTrans;
                endTrans = "Ending Trans No    :" + endTrans;
                oldGrand = "Old Grand Total    : " + getAmountDue(Float.parseFloat(oldGrand));
                newGrand = "New Grand Total    : " + getAmountDue(Float.parseFloat(newGrand));
                startZCount = "Z-Count            : " + startZCount;
                endZCount = "Z-Count(end)       : " + endZCount;
//                String rCount = "Reset Count        : " + resetCount;

                sendZRead2USBEpsonPrinter(i, Exitpoint, "--- CURRENT ZREADING ---", terminalnum, datetimeOut, todaysale, vatablesale, vat12, beginOR, endOR, beginTrans, endTrans, oldGrand, newGrand, startZCount, "");

                if ((i % 2) == 0) {
                    delay(2000);
                }
                i++;
            }

            //ALSO GET THE TOTAL COLLECTION PER PARKER TYPE
            //ResultSet collectionsToday = dbh.getTodaysTotalCollectionBydateColl();
            accumulatedTotal = "Accumulated Grand Total    : " + getAmountDue(Float.parseFloat(grandTotal));
            this.epsonPrintTOTALLogoutReceiptFromDB(Exitpoint);
            this.printAccumulatedTotal(accumulatedTotal, Exitpoint);
            delay(1000);
        } catch (Exception ex) {
            log.error(ex.getMessage());
        }

    }

    public void printTodaysZReadFromDB(String Exitpoint) {
        String accumulatedTotal = "";
        try {
            XMLreader xr = new XMLreader();
            login_id = xr.getElementValue("C://JTerminals/ginH.xml", "log_id");
            String resetCount = xr.getElementValue("C://JTerminals/initH.xml", "resetCount");
            SaveCollData scd = new SaveCollData();
            DataBaseHandler dbh = new DataBaseHandler();

            Float totalCollected = dbh.getImptAmount("totalAmount", login_id);
            Double Sale12Vat = (double) (totalCollected / 1.12) * 0.12f;
            //Double Sale12Vat = (double) totalCollected * 0.12;
            Double vatSale = totalCollected - Sale12Vat;

            ResultSet rs = dbh.getTodaysZReadbydateColl(totalCollected, Sale12Vat, vatSale);

            String receiptNos = scd.getReceiptNos();
            String grandTotal = scd.getGRANDTOTAL();
            String lastTransaction = dbh.getLastTransaction(Exitpoint);

            int i = 1;
            while (rs.next()) {
                String terminalnum = rs.getString("terminalnum");
                String datetimeOut = rs.getString("CURRENT_TIMESTAMP");
                String todaysale_dbl = rs.getString("TODAYSALE");
                String vatablesale_dbl = rs.getString("VATABLESALE");
                String vat12_dbl = rs.getString("VAT12");
                String beginOR = rs.getString("BEGINOR");
                String endOR = receiptNos;
                String beginTrans = rs.getString("beginTrans");
                String endTrans = lastTransaction;
                String oldGrand = rs.getString("oldGrand");
                String newGrand = grandTotal;
                String startZCount = rs.getString("startZCount");
                String endZCount = rs.getString("endZCount");
                //String tellerCode = rs.getString("tellerCode");
                //String logINID = rs.getString("logINID");

                terminalnum = "Terminal N0:   " + terminalnum;
//                datetimeOut = "Date:          " + datetimeOut;
                datetimeOut = "Date Printed:  " + datetimeOut.substring(0, 16);
                String todaysale = "Today's Sales        : " + todaysale_dbl;
                String vatablesale = "VAT Sales           : " + vatablesale_dbl;
                String vat12 = "12% VAT Sales       : " + vat12_dbl;
                beginOR = "Beginning OR       :" + Exitpoint + beginOR;
                endOR = "Ending OR          :" + Exitpoint + endOR;
                beginTrans = "Beginning Trans No :" + beginTrans;
                endTrans = "Ending Trans No    :" + endTrans;
                oldGrand = "Old Grand Total    : " + oldGrand;
                newGrand = "New Grand Total    : " + newGrand;
                startZCount = "Z-Count            : " + startZCount;
                endZCount = "Z-Count(end)       : " + endZCount;
//                String rCount = "Reset Count        : " + resetCount;

                sendZRead2USBEpsonPrinter(i, Exitpoint, "--- CURRENT ZREADING ---", terminalnum, datetimeOut, todaysale, vatablesale, vat12, beginOR, endOR, beginTrans, endTrans, oldGrand, newGrand, startZCount, "");

                if ((i % 2) == 0) {
                    delay(2000);
                }
                i++;
            }

            //ALSO GET THE TOTAL COLLECTION PER PARKER TYPE
            //ResultSet collectionsToday = dbh.getTodaysTotalCollectionBydateColl();
            accumulatedTotal = "Accumulated Grand Total    : " + getAmountDue(Float.parseFloat(grandTotal));
            this.epsonPrintTOTALLogoutReceiptFromDB(Exitpoint);
            this.printAccumulatedTotal(accumulatedTotal, Exitpoint);
            delay(1000);
        } catch (Exception ex) {
            log.error(ex.getMessage());
        }

    }

    //REPRINTS ZReading
    public void printZReadFromDB(String Exitpoint, String logcode, String dateColl, String mastercard) {
        try {
            XMLreader xr = new XMLreader();
            DataBaseHandler dbh = new DataBaseHandler();
            dbh.saveLog("RZ", mastercard, logcode);
            String resetCount = null;
            try {
                resetCount = xr.getElementValue("C://JTerminals/initH.xml", "resetCount");
            } catch (Exception ex) {
                log.error(ex.getMessage());
            }
            String terminalnum = null;
            String newGrand = "";
            String datetimeOut = "";
            String datetimeOut1 = "";
            ResultSet rs = dbh.getZReadbydateColl(dateColl);
            int i = 1;
            while (rs.next()) {
                terminalnum = rs.getString("terminalnum");
                if (null == terminalnum) {
                    break;
                }
                printHEADER(Exitpoint);
                datetimeOut = rs.getString("datetimeOut");
                String todaysale_dbl = rs.getString("TODAYSALE");

                String vatablesale_dbl = rs.getString("VATABLESALE");
                String vat12_dbl = rs.getString("VAT12");
                String beginOR = rs.getString("BEGINOR");
                String endOR = rs.getString("ENDOR");
                String beginTrans = rs.getString("beginTrans");
                String endTrans = rs.getString("endTrans");
                String oldGrand = rs.getString("oldGrand");
                newGrand = rs.getString("newGrand");
                String startZCount = rs.getString("startZCount");
                String endZCount = rs.getString("endZCount");
                //String tellerCode = rs.getString("tellerCode");
                //String logINID = rs.getString("logINID");

                terminalnum = "            R E P R I N T\n" + "Terminal N0:   " + terminalnum;
                datetimeOut1 = "Date:          " + datetimeOut;
                String todaysale = "Todays Sale        : " + todaysale_dbl;
                String vatablesale = "VAT Sale           : " + vatablesale_dbl;
                String vat12 = "12% VAT Sale       : " + vat12_dbl;
                beginOR = "Beginning OR       :" + Exitpoint + beginOR;
                endOR = "Ending OR          :" + Exitpoint + endOR;
                beginTrans = "Beginning Trans No :" + beginTrans;
                endTrans = "Ending Trans No    :" + endTrans;
                oldGrand = "Old Grand Total    : " + oldGrand;
                //newGrand = "New Grand Total    : " + newGrand;
                startZCount = "Z-Count            : " + startZCount;
                endZCount = "Z-Count(end)       : " + endZCount;
//                String rCount = "Reset Count        : " + resetCount;

                sendZRead2USBEpsonPrinter(i, Exitpoint, "--- ZReading ---", terminalnum, datetimeOut1, todaysale, vatablesale, vat12, beginOR, endOR, beginTrans, endTrans, oldGrand, "New Grand Total    : " + newGrand, startZCount, "");

                if ((i % 2) == 0) {
                    delay(2000);
                }
                i++;
            }

            if (null != terminalnum) {
                //add XRead here
                //ALSO GET THE TOTAL COLLECTION PER PARKER TYPE
                //ResultSet collectionsToday = dbh.getTodaysTotalCollectionBydateColl();
                String accumulatedTotal = "Accumulated Grand Total    : " + getAmountDue(Float.parseFloat(newGrand));
                this.epsonPrintTOTALLogoutReceiptFromDBByDate(Exitpoint, datetimeOut);
                this.printAccumulatedTotal(accumulatedTotal, Exitpoint);
            }
            delay(1000);
        } catch (SQLException ex) {
            log.error(ex.getMessage());
        }
    }

    public void printCollectReceiptFromDB(String Exitpoint, String logcode, String dateColl, String masterCard) {
        try {
            DataBaseHandler dbh = new DataBaseHandler();
            dbh.saveLog("RX", masterCard, logcode);
            Map<String, String> parkerTypeCount = new HashMap<String, String>();
            Map<String, String> parkerTypeAmount = new HashMap<String, String>();
            DateFormat df = DateFormat.getDateInstance(DateFormat.SHORT);
            DateFormat tf = DateFormat.getTimeInstance(DateFormat.SHORT);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
            SimpleDateFormat dateOutFormat = new SimpleDateFormat("M/dd/yy");
            SimpleDateFormat timeOutFormat = new SimpleDateFormat("hh:mm a");
            dbh.manualOpen();
            ResultSet types = dbh.getAllActivePtypes();
            while (types.next()) {
                parkerTypeCount.put(types.getString("ptypename"), "*");
                parkerTypeAmount.put(types.getString("ptypename"), "*");
            }
            dbh.manualClose();

            //****************----------------****************
            List<String> ptypesByKey = new ArrayList<>(parkerTypeCount.keySet());
            Collections.sort(ptypesByKey);

            Iterator itr0 = ptypesByKey.iterator();
//            while (itr0.hasNext()) {
//                System.out.println(itr0.next());         
//                System.out.println(parkerTypeCount.get(itr0.next()));
//            }

            ResultSet rs = dbh.getSummaryCollbydateColl(dateColl);

            int i = 1;
//            Set<String> keys = parkerTypeCount.keySet();
            String CName = null;
            String CCode = null;
            String logINID = null;
            while (rs.next()) {
                CName = rs.getString("userName");
                CCode = rs.getString("userCode");
                logINID = rs.getString("logINID");
                if (null == CName) {
                    break;
                }
                ResultSet rs2 = dbh.getCGHIncomeSummaryCollbydateColl(CName, dateColl);
                while (rs2.next()) {

                }
//                String RegularParkers = rs.getString("regularCount");//regularCount
                printHEADER(rs.getString("SentinelID"));
                String LoginDate = rs.getString("loginStamp");
                String LogoutDate = rs.getString("logoutStamp");
                Date BusinessDate = rs.getDate("logoutStamp");

                Date LogIN = null;
                Date LogOUT = null;
                try {
                    LogIN = sdf.parse(LoginDate);
                    LogOUT = sdf.parse(LogoutDate);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                String DateOLogNow = dateOutFormat.format(LogOUT);
                String TimeOLogNow = timeOutFormat.format(LogOUT);
                String DateILogNow = dateOutFormat.format(LogIN);
                String TimeILogNow = timeOutFormat.format(LogIN);
                Iterator<Map.Entry<String, String>> itr = parkerTypeCount.entrySet().iterator();
                try {
                    XMLreader xr = new XMLreader();
                    String feederlines = xr.getElementValue("C://JTerminals/initH.xml", "feederlines");
                    String loosechange = xr.getElementValue("C://JTerminals/ginH.xml", "loosechange");
                    //***********PRINT*******
                    USBEpsonHandler eh = new USBEpsonHandler();
                    eh.closePrinter();
                    eh.openPrinter();
                    eh.initializePrinter();
                    eh.Justify((byte) 1);
                    eh.setRed();

                    eh.printline("---------------------------------------");
                    eh.printline("        CASHIER LOG-OUT REPORT     ");
                    eh.printline("");

                    //eh.printline("  R E P R I N T");
                    //eh.printline("--- X READING ---");
                    //eh.printline("--Log Out Collection--");
                    eh.startPrinter();
                    eh.feedpaperup((byte) 1);
                    eh.Justify((byte) 0);
//
//            
//                    USBEpsonHandler eh = new USBEpsonHandler();
//                    eh.closePrinter();
//                    eh.openPrinter();
//                    eh.initializePrinter();
//                    eh.Justify((byte) 0);
//                    eh.setBlack();
//                    eh.feedpaperup((byte) 2);
//                    if ((i % 4) == 0) {
//                        eh.setBlack();
//                        delay(3000);
//                    } else if ((i % 2) == 0) {
//                        delay(3000);
//                        eh.setRed();
//                    }
//                    CName = "Cashier Name: " + CName;
//                    LoginDate = "Log In  Date: " + LoginDate;
//                    LogoutDate = "Log out Date: " + LogoutDate;
//                    eh.printline("Record No : " + i + "\n");
//                    //eh.feedpaperup((byte) 1);
//                    eh.printline("Terminal ID : " + Exitpoint);
//                    eh.printline("Cashier Code: ****");//+CID
//                    eh.printline(CName);
//                    eh.printline(LoginDate);
//                    eh.printline(LoginTime);
//                    eh.printline(Loosechange);
//                    eh.printline(LogoutDate);
//                    eh.printline(LogoutTime);
                    eh.feedpaperup((byte) 1);
                    //eh.printline("Terminal ID : " + Exitpoint);
                    eh.printline("Business Date : " + sdf.format(BusinessDate));
                    eh.printline("Teller        : " + CName);
                    eh.printline("Log In        : " + DateILogNow + " " + TimeILogNow);
                    eh.printline("Log Out       : " + DateOLogNow + " " + TimeOLogNow);
                    eh.printline("Reprint Count : 0");

                    eh.printline("---------------------------------------");
                    String rdataCount = rs.getString("regularCount");
                    String rdataAmount = rs.getString("regularAmount");
                    DecimalFormat df2 = new DecimalFormat("#.00");

                    eh.printline("Regular-Par   : " + rdataCount + " P " + df2.format(rdataAmount));
                    eh.printline("Lost Card     : " + 0 + " P " + df2.format("0"));
                    eh.printline("Overnight     : " + 0 + " P " + df2.format("0"));
                    eh.printline("Discount      : " + rdataCount + " P " + df2.format(rdataAmount));

                    eh.printline("---------------------------------------");
                    //      COUNT
//                    eh.printline("Cashier Name: " + CName);
//                    eh.printline("Cashier Code: " + CCode);
//                    eh.printline("Cashier Name: " + CName);
//                    eh.printline("Log In  Date: " + DateILogNow);
//                    eh.printline("Log In  Time: " + TimeILogNow);
//                    eh.printline("Loose Change: " + loosechange);
//                    eh.printline("Log out Date: " + DateOLogNow);
//                    eh.printline("Log out Time: " + TimeOLogNow);
                    /*
                    eh.printline("");
                    eh.printline("                    Count     Amount");
                    //delay(1000);
                    
                    while (itr0.hasNext()) {
                        String entry = (String) itr0.next();
//                        System.out.println(entry);
                        String dataCount = rs.getString(entry.toLowerCase().trim() + "Count");
                        String dataAmount = rs.getString(entry.toLowerCase().trim() + "Amount");
                        //      COUNT
//                        System.out.print(dataCount);
//                        System.out.print(parkerTypeCount.get(entry));
                        parkerTypeCount.put(entry, dataCount);
//                        System.out.println(" Count: " + parkerTypeCount.get(entry));
                        //      AMOUNT
//                        System.out.print(dataAmount);
//                        System.out.print(parkerTypeAmount.get(entry));
                        parkerTypeAmount.put(entry, getAmountDue(Float.parseFloat(dataAmount)));
//                        System.out.println(" Amount: " + parkerTypeAmount.get(entry));
                        String out = dbh.formatSpaces(entry + " Parkers") + ": " + dataCount + "    " + parkerTypeAmount.get(entry);
                        eh.printline(out);
                    } */
                    eh.startPrinter();

                    String CarServed = rs.getString("carServed");
                    String ReceiptServed = rs.getString("totalAmount");
                    String OvernightCount = rs.getString("overnightCount");
                    String OvernightAmount = rs.getString("overnightAmount");

                    eh.printline("");
                    //******************************
                    //eh.printline("Extended Count     : " + ExtendedCount);
                    //eh.printline("Extended Amount    : " + ExtendedAmount);
//                    eh.printline("Overnight Count    :  " + OvernightCount);
//                    eh.printline("Overnight Amount   :  " + getAmountDue(Float.parseFloat(OvernightAmount)));
//
//                    eh.printline("");
//
//                    eh.printline("Total Cars Served  :  " + CarServed);
//                    eh.printline("Total Collection   :  " + getAmountDue(Float.parseFloat(ReceiptServed)));
//                    eh.startPrinter();
//                    eh.feedpaperup((byte) 3);
                    //**********************
                    //ResultSet rs1 = dbh.getTodaysZReadbydateColl(totalCollected, Sale12Vat, vatSale);
//                    ResultSet rs1 = dbh.getZReadbylogINID(logINID);
//                    //dbh.getTodaysTotalCollectionBydateColl();
//                    String beginOR = "";
//                    String beginTrans = "";
//                    String beginGrandTotal = "";
//                    String receiptNos = "";
//                    String grandTotal = "";
//                    int beginORnum = 0;
//                    while (rs1.next()) {
//                        beginOR = rs1.getString("beginOR");
//                        beginTrans = rs1.getString("beginTrans");
//                        receiptNos = rs1.getString("endOR");
//                        grandTotal = rs1.getString("newGrand");
//                        beginGrandTotal = rs1.getString("oldGrand");
//                        //***********PRINT*******
//                    }
//                    beginORnum = Integer.parseInt(beginOR);
//                    beginOR = formatNos(String.valueOf(beginORnum));
//                    Float totalCollected = dbh.getImptAmount("totalAmount", login_id);
//                    Double Sale12Vat = (double) (totalCollected / 1.12) * 0.12f;
//                    //Double Sale12Vat = (double) totalCollected * 0.12;
//                    Double vatSale = totalCollected - Sale12Vat;
//
//                    String lastTransaction = dbh.getLastTransaction(Exitpoint);
//
//                    eh.printline("Beginning OR No.  : " + Exitpoint + beginOR);
//                    eh.printline("Ending OR No.     : " + Exitpoint + receiptNos);
//                    eh.printline("Beginning Balance : " + getAmountDue(Float.parseFloat(beginGrandTotal)));
//                    eh.printline("Ending Balance    : " + getAmountDue(Float.parseFloat(grandTotal)));

                    eh.startPrinter();
                    eh.feedpaperup((byte) 8);
                    eh.fullcut();
                    eh.closeReceiptFile(Exitpoint);
                    eh.closePrinter();

                } catch (Exception ex) {
                    log.error(ex.getMessage());
                }

//                for (String key : parkerTypeCount.keySet()) {
//                    System.out.println(key);
//                    String RegularParkers = rs.getString("regularCount");//regularCount
//                }
//                sendColl2USBEpsonPrinter(i, Exitpoint, CName, LoginDate, "", "", LogoutDate, "",
//                        RegularParkers, MotorcycleParkers, QCSeniorParkers, GraceParkers, VIPParkers, LOSTParkers, "",
//                        "", DeliveryParkers, NonQCSeniorParkers, CarServed, ReceiptServed);
//
//                if ((i % 2) == 0) {
//                    delay(2000);
//                }
//                i++;
            }

            delay(1000);
        } catch (SQLException ex) {
            log.error(ex.getMessage());
        }
    }

    public void printCollectReceipt(String Exitpoint) throws Exception {
        RawFileHandler rfh = new RawFileHandler();
        String CollData = "";

        if (rfh.FindFileFolder("C://JTerminals/de4Dd87d/CfgJ9rl/", "collect.jrt") == true) {
            //because rfh.rehdFline is indexed at 1
            int loop = rfh.getTotalFLines("C://JTerminals/de4Dd87d/CfgJ9rl/", "collect.jrt") + 1;
            int i = 1;
            do {
                CollData = rfh.readFline("C://JTerminals/de4Dd87d/CfgJ9rl/", "collect.jrt", i);
                CollData = CollData.replaceAll(" ", "");
                if (CollData != null) {
                    if (CollData.equalsIgnoreCase("") == false) {
                        String CID = CollData.substring(0, 8);
                        CID = "*" + CID + "*";
                        String CName = CollData.substring(8, 14);
                        String LoginTime = CollData.substring(14, 18);
                        String LoginDate = CollData.substring(18, 26);
                        String Loosechange = CollData.substring(26, 29);
                        String LogoutTime = CollData.substring(29, 33);
                        String LogoutDate = CollData.substring(33, 41);

                        String CarServed = formatBack(CollData.substring(41, 51));
                        String ReceiptServed = formatBack(CollData.substring(51, 61));

                        String RegularParkers = formatBack(CollData.substring(61, 71));
                        String MotorcycleParkers = "0";
                        if (CollData.length() >= 81) {
                            MotorcycleParkers = formatBack(CollData.substring(71, 81));
                        }
                        String QCSeniorParkers = "0";
                        if (CollData.length() >= 91) {
                            QCSeniorParkers = formatBack(CollData.substring(81, 91));
                        }
                        String GraceParkers = "0";
                        if (CollData.length() >= 101) {
                            GraceParkers = formatBack(CollData.substring(91, 101));
                        }
                        String VIPParkers = "0";
                        if (CollData.length() >= 111) {
                            VIPParkers = formatBack(CollData.substring(101, 111));
                        }
                        String LOSTParkers = "0";
                        if (CollData.length() >= 121) {
                            LOSTParkers = formatBack(CollData.substring(111, 121));
                        }
                        String LCEPParkers = "0";
                        if (CollData.length() >= 131) {
                            LCEPParkers = formatBack(CollData.substring(121, 131));
                        }
                        String InvalidFlatRateParkers = "0";
                        if (CollData.length() >= 141) {
                            InvalidFlatRateParkers = formatBack(CollData.substring(131, 141));
                        }
                        String DeliveryParkers = "0";
                        if (CollData.length() >= 151) {
                            DeliveryParkers = formatBack(CollData.substring(141, 151));
                        }
                        String NonQCSeniorParkers = "0";
                        if (CollData.length() >= 161) {
                            NonQCSeniorParkers = formatBack(CollData.substring(151, 161));
                        }
                        String BPOMotorParkers = "0";
                        if (CollData.length() >= 171) {
                            BPOMotorParkers = formatBack(CollData.substring(161, 171));
                        }
                        String EXTicketServed = "0";
                        if (CollData.length() >= 181) {
                            EXTicketServed = formatBack(CollData.substring(171, 181));
                        }
                        String ENTicketServed = "0";
                        if (CollData.length() >= 191) {
                            ENTicketServed = formatBack(CollData.substring(181, 191));
                        }
                        String RegularAmount = "0";
                        if (CollData.length() >= 201) {
                            RegularAmount = formatBack(CollData.substring(191, 201));
                        }
                        String MotorAmount = "0";
                        if (CollData.length() >= 211) {
                            MotorAmount = formatBack(CollData.substring(201, 211));
                        }
                        String QCAmount = "0";
                        if (CollData.length() >= 221) {
                            QCAmount = formatBack(CollData.substring(211, 221));
                        }
                        String LostAmount = "0";
                        if (CollData.length() >= 231) {
                            LostAmount = formatBack(CollData.substring(221, 231));
                        }
                        String DeliveryAmount = "0";
                        if (CollData.length() >= 241) {
                            DeliveryAmount = formatBack(CollData.substring(231, 241));
                        }
                        String NonQCSeniorAmount = "0";
                        if (CollData.length() >= 251) {
                            NonQCSeniorAmount = formatBack(CollData.substring(241, 251));
                        }
                        String BPOMotorAmount = "0";
                        if (CollData.length() >= 261) {
                            BPOMotorAmount = formatBack(CollData.substring(251, 261));
                        }

                        CID = "Cashier Code: " + CID;
                        CName = "Cashier Name: " + CName;
                        LoginDate = "Log In  Date: " + LoginDate;
                        LoginTime = "Log In  Time: " + LoginTime;
                        Loosechange = "Loose Change: " + Loosechange;
                        LogoutDate = "Log out Date: " + LogoutDate;
                        LogoutTime = "Log out Time: " + LogoutTime;
                        //delay(1000);
                        RegularParkers = "Private Parkers    : " + RegularParkers + "    " + RegularAmount;
                        MotorcycleParkers = "Motorcycle Parkers : " + MotorcycleParkers + "    " + MotorAmount;
                        QCSeniorParkers = "QC Senior Parkers  : " + QCSeniorParkers + "    " + QCAmount;
                        GraceParkers = "Grace Parkers      : " + GraceParkers + "    " + 0;
                        VIPParkers = "VIP Parkers        : " + VIPParkers + "    " + 0;
//                eh.printline("OCLP Parkers       : 0");
                        LOSTParkers = "Lost Card Parkers  : " + LOSTParkers + "    " + LostAmount;
                        LCEPParkers = "LCEP Parkers       : " + LCEPParkers + "    " + 0;
                        InvalidFlatRateParkers = "Invalid Cards      : " + InvalidFlatRateParkers + "    " + 0;
                        DeliveryParkers = "Delivery Parkers   : " + DeliveryParkers + "    " + DeliveryAmount;
                        NonQCSeniorParkers = "BPO Car Parker     : " + NonQCSeniorParkers + "    " + NonQCSeniorAmount;
                        BPOMotorParkers = "BPO Motor Parker   : " + BPOMotorParkers + "    " + BPOMotorAmount;

                        CarServed = "Total Cars Served  : " + CarServed;
                        ENTicketServed = "Total Entry Tickets: " + ENTicketServed;
                        EXTicketServed = "Total Exit Tickets : " + EXTicketServed;

                        ReceiptServed = "Total Collection   : " + ReceiptServed;

                        sendColl2EpsonPrinter(i, Exitpoint, CName, LoginDate, LoginTime, Loosechange, LogoutDate, LogoutTime, RegularParkers, MotorcycleParkers, QCSeniorParkers, GraceParkers, VIPParkers, LOSTParkers, LCEPParkers,
                                InvalidFlatRateParkers, DeliveryParkers, NonQCSeniorParkers, BPOMotorParkers, CarServed, ReceiptServed);
                        if ((i % 2) == 0) {
                            delay(2000);
                        }
                        delay(1000);
                    }
                }
                i++;
            } while (i != loop);
        }
//        USBEpsonHandler eh = new USBEpsonHandler();
//        eh.feedpaperup((byte) 1);
//        eh.printHEADER(Exitpoint);
//        eh.fullcut();
//        eh.closeprinter();
    }
//    public void delay2 (int howLong) // delay function to waste time
//    {
//        double garbage = 0;
//        for (int i = 1 ; i <= howLong ; i++)
//        {
//        garbage = Math.PI * Math.PI;
//        }
//        garbage = 0;
//    }

    public void delay(int howLong) // delay function to waste time
    {
        Date theTime = new Date();
        Date busyStamp = new Date();
        do {
            theTime = new Date();
        } while (theTime.getTime() < busyStamp.getTime() + howLong);
    }

    public void printLogoutReceipt(String Exitpoint, boolean currentcoll) {
        try {
            SaveCollData scd = new SaveCollData();
            DateConversionHandler dch = new DateConversionHandler();
            Date LogStamp = new Date();
            DateFormat df = DateFormat.getDateInstance(DateFormat.SHORT);
            DateFormat tf = DateFormat.getTimeInstance(DateFormat.SHORT);

            XMLreader xr = new XMLreader();
            String logintime = xr.getElementValue("C://JTerminals/ginH.xml", "logindate");
            String cashiercode = xr.getElementValue("C://JTerminals/ginH.xml", "cashier_id");
            String cashiername = xr.getElementValue("C://JTerminals/ginH.xml", "cashier_name");
            cashiername = cashiername.replaceAll(" ", "");
            String loosechange = xr.getElementValue("C://JTerminals/ginH.xml", "loosechange");
            String ccode = "*" + cashiercode.toString() + "*";
            Date LoginStamp = new Date(Long.parseLong(logintime));

            String RegularParkers = readComFiles("R");
            String MotorcycleParkers = readComFiles("M");
            String MPPParkers = readComFiles("P");
            String GraceParkers = readComFiles("G");
            String VIPParkers = readComFiles("V");
            String LostCardParkers = readComFiles("L");
            String LCEPParkers = readComFiles("E");
            String InvalidFlatRateParkers = readComFiles("F");
            String JoggerParkers = readComFiles("J");
            String PromoParkers = readComFiles("O");
            String DeliveryParkers = readComFiles("D");
            String QCSeniorParkers = readComFiles("Q");
            String NonQCSeniorParkers = readComFiles("BC");
            String BPOMotorParkers = readComFiles("BM");

            String RegularAmount = getPtypeAmount("R");
            String MotorcycleAmount = getPtypeAmount("M");
            String LostAmount = getPtypeAmount("L");
            String DeliveryAmount = getPtypeAmount("D");
            String QCSeniorAmount = getPtypeAmount("Q");
            String NonQCSeniorAmount = getPtypeAmount("BC");
            String BPOMotorAmount = getPtypeAmount("BM");

            String FixRetail = readComFiles("R1");
            String SuccRetail = readComFiles("R2");
            String EntCarServed = readComFiles("hcornell");
            String ExtCarServed = readComFiles("hbrentbay");
            String ENTicketServed = readComFiles("hbugs");
            String EXTicketServed = readComFiles("hbacta");
            String ReceiptServed = readComFiles("Receipt");
            String DateOLogNow = df.format(LogStamp);
            String TimeOLogNow = tf.format(LogStamp);
            String DateILogNow = df.format(LoginStamp);
            String TimeILogNow = tf.format(LoginStamp);

            String LoginTimeSave = dch.convertTime2base(TimeILogNow.toString());
            String LoginDateSave = dch.convertDate2base(DateILogNow);
            String LogoutTimeSave = dch.convertTime2base(TimeOLogNow);
            String LogoutDateSave = dch.convertDate2base(DateOLogNow);

            //***********PRINT*******
            StringBuilder str = new StringBuilder();
            str.append("ESC @\n");//Init
            str.append("ESC ! 0\n");//Font A 0or1
            str.append("ESC E 0\n");//Emphasized 0or1
            str.append("ESC a 1\n");//Center 0-1-2 L-C-R
            str.append("ESC SP 1\n");//Init
            str.append("ESC - 0 \n");//Init
            if (currentcoll == false) {
                str.append("\"" + "--Log Out Collection--" + "\" CR LF\n");
            } else {
                str.append("\"" + "--Current Collection--" + "\" CR LF\n");
            }
            str.append("ESC a 0\n");//Center 0-1-2 L-C-R
            str.append("\"" + "\" CR LF\n");
            str.append("\"Terminal ID : " + Exitpoint + "\" CR LF\n");
            str.append("\"Cashier Code: ****" + "\" CR LF\n");
            str.append("\"Cashier Name: " + cashiername + "\" CR LF\n");
            str.append("\"Log In  Date: " + DateILogNow + "\" CR LF\n");
            str.append("\"Log In  Time: " + TimeILogNow + "\" CR LF\n");
            str.append("\"Loose Change: " + loosechange + "\" CR LF\n");
            str.append("\"Log out Date: " + DateOLogNow + "\" CR LF\n");
            str.append("\"Log out Time: " + TimeOLogNow + "\" CR LF\n");
            str.append("\"" + "\" CR LF\n");
            str.append("\"                     Count         Amount\" CR LF\n");
            str.append("\"Private Parkers     : " + RegularParkers + "     " + RegularAmount + "\" CR LF\n");
            //str.append("\"    **Fix Rate     : " + FixRetail + "\" CR LF\n");
            //str.append("\"    **w/ Succ      : " + SuccRetail + "\" CR LF\n");
            str.append("\"Motorcycle Parkers : " + MotorcycleParkers + "     " + MotorcycleAmount + "\" CR LF\n");
            str.append("\"Grace Parkers      : " + GraceParkers + "     " + 0 + "\" CR LF\n");
            str.append("\"VIP Parkers        : " + VIPParkers + "     " + 0 + "\" CR LF\n");
            str.append("\"Lost Card Parkers  : " + LostCardParkers + "     " + LostAmount + "\" CR LF\n");
            //str.append("\"LCEP Parkers       : " + LCEPParkers + "\" CR LF\n");
            //str.append("\"Invalid Card Parker: " + InvalidFlatRateParkers + "\" CR LF\n");
            //str.append("\"Promo Parkers      : " + PromoParkers + "\" CR LF\n");
            str.append("\"QCSenior Parkers   : " + QCSeniorParkers + "     " + QCSeniorAmount + "\" CR LF\n");
            str.append("\"Delivery Parkers   : " + DeliveryParkers + "     " + DeliveryAmount + "\" CR LF\n");
            str.append("\"BPO Car Parkers    : " + NonQCSeniorParkers + "     " + NonQCSeniorAmount + "\" CR LF\n");
            str.append("\"BPO Motor Parkers  : " + BPOMotorParkers + "     " + BPOMotorAmount + "\" CR LF\n");
            str.append("\"" + "\" CR LF\n");
            str.append("\"" + "\" CR LF\n");
            str.append("\"" + "\" CR LF\n");
            str.append("\"" + "\" CR LF\n");

            str.append("ESC E 1\n");//Emphasized 0or1
            str.append("ESC a 0\n");//Center 0-1-2 L-C-R

            str.append("ESC d 10\n");//Feed 10 lines
            str.append("GS V 1\n");//Send Cut

            rfh.putfile("C://JTerminals/Outline/", "bar", str.toString());

            String pingCmd = "C://JTerminals/senddat.exe C://JTerminals/Outline/bar USBPRN0";
            Runtime r = Runtime.getRuntime();
            Process p = r.exec(pingCmd);
            p.waitFor();

            /*
            USBEpsonHandler eh = new USBEpsonHandler();
            eh.initializePrinter();
            eh.Justify((byte) 1);
            eh.setRed();
            eh.feedpaperup((byte) 2);
            if (currentcoll == false) {
                eh.printline("--Log Out Collection--");
            } else {
                eh.printline("--Current Collection--");
            }

            eh.feedpaperup((byte) 1);
            eh.Justify((byte) 0);
            eh.printline("Terminal ID : " + Exitpoint);
            eh.printline("Cashier Code: ****");//+ccode
            eh.printline("Cashier Name: " + cashiername);
            eh.printline("Log In  Date: " + DateILogNow);
            eh.printline("Log In  Time: " + TimeILogNow);
            eh.printline("Loose Change: " + loosechange);
            eh.printline("Log out Date: " + DateOLogNow);
            eh.printline("Log out Time: " + TimeOLogNow);
            eh.feedpaperup((byte) 1);
            //delay(500000);
            //delay(1000);
            eh.printline("Retail Parkers     : " + RegularParkers);
            eh.printline("    **Fix Rate     : " + FixRetail);
            eh.printline("    **w/ Succ      : " + SuccRetail);
            eh.printline("Motorcycle Parkers : " + MotorcycleParkers);
            eh.printline("Monthly Parkers    : " + MPPParkers);
            eh.printline("Grace Parkers      : " + GraceParkers);
            eh.printline("Jogger Parkers     : " + JoggerParkers);
            eh.printline("Delivery Parkers   : 0");
            eh.printline("VIP Parkers        : " + VIPParkers);
            eh.printline("OCLP Parkers       : 0");
            eh.printline("Lost Card Parkers  : " + LostCardParkers);
            eh.printline("LCEP Parkers       : " + LCEPParkers);
            eh.printline("Invalid Card Parker: " + InvalidFlatRateParkers);
            eh.printline("Promo Parkers      : " + PromoParkers);

            eh.feedpaperup((byte) 1);
            //delay(500000);
            //delay(1000);
            eh.printline("Total Entry Cars Served: " + EntCarServed);
            eh.printline("Total Exit  Cars Served: " + ExtCarServed);
            eh.printline("Total Entry Tickets    :" + ENTicketServed);
            eh.printline("Total Exit Tickets     :" + EXTicketServed);
            eh.printline("Total Collection : " + ReceiptServed);

            eh.feedpaperup((byte) 4);
            eh.printHEADER(Exitpoint);
            eh.fullcut();
            eh.closeprinter();
            
             */
            //deletion process for new login
            if (currentcoll == false) //true is for spot checking, false is for complete logout
            {
                scd.ResetCarServed();
                scd.ResetEntryTicketsServed();
                scd.ResetExitTicketsServed();
                scd.ResetCurrReceipt_Counter();  //reset this for the next LogIN 
                scd.UpdatePtypecount("R", "0");  //RETAIL
                scd.UpdatePtypecount("M", "0");  //MOTORCYCLE
                scd.UpdatePtypecount("P", "0");  //PREPAID
                scd.UpdatePtypecount("G", "0");  //GRACE
                scd.UpdatePtypecount("V", "0");  //VIP
                scd.UpdatePtypecount("L", "0");  //LOST
                scd.UpdatePtypecount("E", "0");  //LCEP
                scd.UpdatePtypecount("J", "0");  //JOGGERS
                scd.UpdatePtypecount("O", "0");  //PROMO
                scd.UpdatePtypecount("F", "0");  //Invalid FLATRATES
                scd.UpdatePtypecount("Q", "0");  //QC Senior
                scd.UpdatePtypecount("BC", "0");  //BPO Cars
                scd.UpdatePtypecount("BM", "0");  //BPO Motors
                scd.UpdatePtypecount("D", "0");  //DELIVERY
                scd.UpdatePtypecount("R1", "0");  //Fixed Retail
                scd.UpdatePtypecount("R2", "0");  //Succeeding Retail

                scd.ErasePtypeAmount("R");
                scd.ErasePtypeAmount("M");
                scd.ErasePtypeAmount("L");
                scd.ErasePtypeAmount("D");
                scd.ErasePtypeAmount("Q");
                scd.ErasePtypeAmount("BC");
                scd.ErasePtypeAmount("BM");

                String ReceiptServedSave = "0";
                if (ReceiptServed.length() > 3) {
                    ReceiptServedSave = ReceiptServed.substring(0, ReceiptServed.length() - 2);
                }
                String line = cashiercode + cashiername + LoginTimeSave + LoginDateSave + loosechange
                        + LogoutTimeSave + LogoutDateSave
                        + formatNos(ExtCarServed)
                        + formatNos(ReceiptServedSave)
                        + formatNos(RegularParkers) + formatNos(MotorcycleParkers) + formatNos(QCSeniorParkers)
                        + formatNos(GraceParkers) + formatNos(VIPParkers) + formatNos(LostCardParkers)
                        + formatNos(LCEPParkers) + formatNos(InvalidFlatRateParkers) + formatNos(DeliveryParkers)
                        + formatNos(NonQCSeniorParkers) + formatNos(BPOMotorParkers)
                        + formatNos(EXTicketServed) + formatNos(ENTicketServed)
                        + formatNos(RegularAmount) + formatNos(MotorcycleAmount)
                        + formatNos(QCSeniorAmount) + formatNos(LostAmount)
                        + formatNos(DeliveryAmount) + formatNos(NonQCSeniorAmount) + formatNos(BPOMotorAmount);

                this.UpdateCollect(line);
                if (currentcoll == false) {
                    this.UpdateServerCollect(Exitpoint, line);
                }
            }
        } catch (Exception ex) {
            log.error(ex.getMessage());
        }

    }

    public void printAccumulatedTotal(String accTotal, String Exitpoint) {
        USBEpsonHandler eh = new USBEpsonHandler();
        eh.closePrinter();
        eh.openPrinter();
        eh.initializePrinter();
        eh.Justify((byte) 1);
        eh.setRed();
        eh.feedpaperup((byte) 1);
        eh.printline(accTotal);
        eh.startPrinter();
        eh.feedpaperup((byte) 8);
        eh.fullcut();
        eh.closeReceiptFile(Exitpoint);
        eh.closePrinter();
    }

    //06/16/2019
    //CURRENT ZREAD WITH X READING (Today's)
    public void epsonPrintTOTALLogoutReceiptFromDB(String Exitpoint) {
        try {
            SaveCollData scd = new SaveCollData();
            DataBaseHandler dbh = new DataBaseHandler();
            Map<String, String> parkerTypeCount = new HashMap<String, String>();
            Map<String, String> parkerTypeAmount = new HashMap<String, String>();
            Map<String, String> parkerTypeNames = new HashMap<String, String>();

            dbh.manualOpen();
            ResultSet types = dbh.getAllActivePtypes();
            while (types.next()) {
                parkerTypeCount.put(types.getString("ptypename"), "0");
                parkerTypeAmount.put(types.getString("ptypename"), "0");
                parkerTypeNames.put(types.getString("ptypename"), types.getString("ptypename"));
            }
            dbh.manualClose();
            List<String> ptypesByKey = new ArrayList<>(parkerTypeCount.keySet());
            Collections.sort(ptypesByKey);
            Iterator itr0 = ptypesByKey.iterator();
            Iterator itr1 = ptypesByKey.iterator();
//            ResultSet rs = dbh.getTotalCollectionBydateColl(datetimeOut);

            ResultSet rs = dbh.getTodaysTotalCollectionBydateColl();

            USBEpsonHandler eh = new USBEpsonHandler();
            while (rs.next()) {
                String OvernightCount = rs.getString("overnightCount");
                String OvernightAmount = rs.getString("overnightAmount");
                int ExtCarServed = rs.getInt("carServed");
                float ReceiptServed = rs.getFloat("totalAmount");
                ExtCarServed = ExtCarServed;// - RefundCount;
                ReceiptServed = ReceiptServed;// - RefundAmount;
                //***********PRINT*******
                eh.closePrinter();
                eh.openPrinter();
                eh.initializePrinter();
                eh.Justify((byte) 1);
                eh.setRed();

                eh.printline("--- TODAY's X READINGS ---");
                eh.printline("--Log Out Collection--");

                eh.startPrinter();
                eh.feedpaperup((byte) 1);
                eh.Justify((byte) 0);

                eh.printline("Terminal ID : " + Exitpoint);
                eh.printline("");
                while (itr0.hasNext()) {
                    String entry = (String) itr0.next();
//                    System.out.println(entry);
                    String dataCount = rs.getString(entry.toLowerCase().trim() + "Count");
                    String dataAmount = rs.getString(entry.toLowerCase().trim() + "Amount");
                    //      COUNT
//                    System.out.print(dataCount);
//                    System.out.print(parkerTypeCount.get(entry));
                    parkerTypeCount.put(entry, dataCount);
//                    System.out.println(" Count: " + parkerTypeCount.get(entry));
                    //      AMOUNT
//                    System.out.print(dataAmount);
//                    System.out.print(parkerTypeAmount.get(entry));
                    parkerTypeAmount.put(entry, getAmountDue(Float.parseFloat(dataAmount)));
//                    System.out.println(" Amount: " + parkerTypeAmount.get(entry));
                    String out = dbh.formatSpaces(entry + " Parkers") + ": " + dataCount + "    " + parkerTypeAmount.get(entry);
                    eh.printline(out);
                }
                eh.startPrinter();
                //eh.printline("");
                eh.printline("");
                eh.printline("Overnight Count    :  " + OvernightCount);
                eh.printline("Overnight Amount   :  " + OvernightAmount);

                eh.printline("");

                eh.printline("Total Cars Served  :  " + ExtCarServed);
                eh.printline("Total Collection   :  " + getAmountDue(ReceiptServed));
                eh.startPrinter();

            }
//            eh.feedpaperup((byte) 8);
//            eh.fullcut();
            eh.closeReceiptFile(Exitpoint);
            eh.closePrinter();
        } catch (Exception ex) {
            log.error(ex.getMessage());
        }

    }

    //TODO EDIT NOW
    public void epsonPrintTOTALLogoutReceiptFromDBByDate(String Exitpoint, String datetimeOut) {
        try {
            SaveCollData scd = new SaveCollData();
            DataBaseHandler dbh = new DataBaseHandler();
            Map<String, String> parkerTypeCount = new HashMap<String, String>();
            Map<String, String> parkerTypeAmount = new HashMap<String, String>();
            Map<String, String> parkerTypeNames = new HashMap<String, String>();

            dbh.manualOpen();
            ResultSet types = dbh.getAllActivePtypes();
            while (types.next()) {
                parkerTypeCount.put(types.getString("ptypename"), "0");
                parkerTypeAmount.put(types.getString("ptypename"), "0");
                parkerTypeNames.put(types.getString("ptypename"), types.getString("ptypename"));
            }
            dbh.manualClose();
            List<String> ptypesByKey = new ArrayList<>(parkerTypeCount.keySet());
            Collections.sort(ptypesByKey);
            Iterator itr0 = ptypesByKey.iterator();
            Iterator itr1 = ptypesByKey.iterator();
            ResultSet rs = dbh.getTotalCollectionBydateColl(datetimeOut);

            USBEpsonHandler eh = new USBEpsonHandler();
            while (rs.next()) {
                String OvernightCount = rs.getString("overnightCount");
                String OvernightAmount = rs.getString("overnightAmount");
                int ExtCarServed = rs.getInt("carServed");
                Float ReceiptServed = rs.getFloat("totalAmount");
                ExtCarServed = ExtCarServed;// - RefundCount;
                ReceiptServed = ReceiptServed;// - RefundAmount;
                //***********PRINT*******
                eh.closePrinter();
                eh.openPrinter();
                eh.initializePrinter();
                eh.Justify((byte) 1);
                eh.setRed();

                eh.printline("--- TODAY's X READINGS ---");
                eh.printline("--Log Out Collection--");

                eh.startPrinter();
//                eh.feedpaperup((byte) 1);
                eh.Justify((byte) 0);

                eh.printline("Terminal ID : " + Exitpoint);
                eh.printline("");
                while (itr0.hasNext()) {
                    String entry = (String) itr0.next();
//                    System.out.println(entry);
                    String dataCount = rs.getString(entry.toLowerCase().trim() + "Count");
                    String dataAmount = rs.getString(entry.toLowerCase().trim() + "Amount");
                    //      COUNT
//                    System.out.print(dataCount);
//                    System.out.print(parkerTypeCount.get(entry));
                    parkerTypeCount.put(entry, dataCount);
//                    System.out.println(" Count: " + parkerTypeCount.get(entry));
                    //      AMOUNT
//                    System.out.print(dataAmount);
//                    System.out.print(parkerTypeAmount.get(entry));
                    parkerTypeAmount.put(entry, getAmountDue(Float.parseFloat(dataAmount)));
//                    System.out.println(" Amount: " + parkerTypeAmount.get(entry));
                    String out = dbh.formatSpaces(entry + " Parkers") + ": " + dataCount + "    " + parkerTypeAmount.get(entry);
                    eh.printline(out);
                }
                eh.startPrinter();
                //eh.printline("");
                eh.printline("");
                eh.printline("Overnight Count    :  " + OvernightCount);
                eh.printline("Overnight Amount   :  " + OvernightAmount);

                eh.printline("");

                eh.printline("Total Cars Served  :  " + ExtCarServed);
                eh.printline("Total Collection   :  " + getAmountDue(ReceiptServed));
                eh.startPrinter();

            }
//            eh.feedpaperup((byte) 4);
//            eh.fullcut();
            eh.closeReceiptFile(Exitpoint);
            eh.closePrinter();
        } catch (Exception ex) {
            log.error(ex.getMessage());
        }

    }

    //NEW COLLTRAIN
    //Dynamic Parker Types for X Read
    public void epsonPrintLogoutReceiptFromDB(String Exitpoint, boolean currentcoll) {
        try {
            SaveCollData scd = new SaveCollData();
            DateConversionHandler dch = new DateConversionHandler();
            DataBaseHandler dbh = new DataBaseHandler();
            Date LogStamp = new Date();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
            DateFormat df = DateFormat.getDateInstance(DateFormat.SHORT);
            DateFormat tf = DateFormat.getTimeInstance(DateFormat.SHORT);

            Map<String, String> parkerTypeCount = new HashMap<String, String>();
            Map<String, String> parkerTypeAmount = new HashMap<String, String>();
            Map<String, String> parkerTypeNames = new HashMap<String, String>();

            dbh.manualOpen();
//            ResultSet types = dbh.getAllActivePtypes();
//            while (types.next()) {
//                parkerTypeCount.put(types.getString("parkertype"), "0");
//                parkerTypeAmount.put(types.getString("parkertype"), "0");
//                parkerTypeNames.put(types.getString("parkertype"), types.getString("ptypename"));
//            }
//            dbh.manualClose();
//            List<String> ptypesByKey = new ArrayList<>(parkerTypeCount.keySet());
//            Collections.sort(ptypesByKey);
//            Iterator itr0 = ptypesByKey.iterator();
//            Iterator itr1 = ptypesByKey.iterator();

            XMLreader xr = new XMLreader();
            String logintime = xr.getElementValue("C://JTerminals/ginH.xml", "logindate");
            login_id = xr.getElementValue("C://JTerminals/ginH.xml", "log_id");
            String cashiercode = xr.getElementValue("C://JTerminals/ginH.xml", "cashier_id");
            String cashiername = xr.getElementValue("C://JTerminals/ginH.xml", "cashier_name");
            //cashiername = cashiername.replaceAll(" ", "");
            String loosechange = xr.getElementValue("C://JTerminals/ginH.xml", "loosechange");
            String ccode = "*" + cashiercode.toString() + "*";
            ccode = "**" + "**";
            Date LoginStamp = new Date(Long.parseLong(logintime));

            //String FixRetail = readComFiles("R1");
            //String SuccRetail = readComFiles("R2");
            //String EntCarServed = readComFiles("hcornell");
            //String ExtCarServed = getImptCountFromDB("carServed");
            //int extCarServed = Integer.parseInt(ExtCarServed);// - refundCnt;
            //String ExtCarServed = readComFiles("hbrentbay");
            //String ENTicketServed = readComFiles("hbugs");
            //String EXTicketServed = readComFiles("hbacta");

            //String ReceiptServed = readComFiles("Receipt");
            String DateOLogNow = df.format(LogStamp);
            String TimeOLogNow = tf.format(LogStamp);
            String DateILogNow = df.format(LoginStamp);
            String TimeILogNow = tf.format(LoginStamp);

            String LoginTimeSave = dch.convertTime2base(TimeILogNow.toString());
            String LoginDateSave = dch.convertDate2base(DateILogNow);
            String LogoutTimeSave = dch.convertTime2base(TimeOLogNow);
            String LogoutDateSave = dch.convertDate2base(DateOLogNow);

            //***********PRINT*******
            USBEpsonHandler eh = new USBEpsonHandler();
            eh.closePrinter();
            eh.openPrinter();
            eh.initializePrinter();
            //eh.Justify((byte) 1);
            //eh.setRed();

            //if (currentcoll == false) {
            //    eh.printline("--- X READING ---");
            //    eh.printline("--Log Out Collection--");
            //} else {
            //eh.printline("--Current Collection--");
            //}
            //eh.printHEADER(Exitpoint);
            eh.startPrinter();
            eh.feedpaperup((byte) 1);
            eh.Justify((byte) 0);

            eh.printline("---------------------------------------");
            eh.printline("        CASHIER LOG-OUT REPORT     ");
            eh.printline("");

            //eh.printline("  R E P R I N T");
            //eh.printline("--- X READING ---");
            //eh.printline("--Log Out Collection--");
            eh.startPrinter();
            eh.feedpaperup((byte) 1);
            eh.Justify((byte) 0);
//
//            
//                    USBEpsonHandler eh = new USBEpsonHandler();
//                    eh.closePrinter();
//                    eh.openPrinter();
//                    eh.initializePrinter();
//                    eh.Justify((byte) 0);
//                    eh.setBlack();
//                    eh.feedpaperup((byte) 2);
//                    if ((i % 4) == 0) {
//                        eh.setBlack();
//                        delay(3000);
//                    } else if ((i % 2) == 0) {
//                        delay(3000);
//                        eh.setRed();
//                    }
//                    CName = "Cashier Name: " + CName;
//                    LoginDate = "Log In  Date: " + LoginDate;
//                    LogoutDate = "Log out Date: " + LogoutDate;
//                    eh.printline("Record No : " + i + "\n");
//                    //eh.feedpaperup((byte) 1);
//                    eh.printline("Terminal ID : " + Exitpoint);
//                    eh.printline("Cashier Code: ****");//+CID
//                    eh.printline(CName);
//                    eh.printline(LoginDate);
//                    eh.printline(LoginTime);
//                    eh.printline(Loosechange);
//                    eh.printline(LogoutDate);
//                    eh.printline(LogoutTime);
            eh.feedpaperup((byte) 1);
            //eh.printline("Terminal ID : " + Exitpoint);
            eh.printline("Business Date : " + df.format(LogStamp));
            eh.printline("Teller        : " + cashiername);
            eh.printline("Log In        : " + DateILogNow + " " + TimeILogNow);
            eh.printline("Log Out       : " + DateOLogNow + " " + TimeOLogNow);
            eh.printline("Reprint Count : 0");

            eh.printline("---------------------------------------");
            eh.startPrinter();
            ResultSet rs = dbh.getSummaryCollbyLoginID(login_id);
            String rdataCount = "";
            String rdataAmount = "";
            String RefundAmount = "";
            String OvernightAmount = "";
            String ExtendedAmount = "";
            String discountAmount = "";
            
            String vatExemptAmount = "";
            String vat12Amount = "";
            String vatsaleAmount = "";
            String ReceiptServed = "";
            String RefundCount = "";
            
            String ExtendedCount = "";
            String OvernightCount = "";
            String discountCount = "";
            String vatExemptCount = "";
            String vat12Count = "";
            String vatsaleCount = "";
            
            
            String totalCount = "";
            String totalAmount = "";
            
            while (rs.next()) {
            rdataCount = rs.getString("regularCount");
            rdataAmount = rs.getString("regularAmount");

            RefundAmount = rs.getString("refundAmount");
            float refundAmt = Float.parseFloat(RefundAmount);
            OvernightAmount = rs.getString("overnightAmount");
            ExtendedAmount = rs.getString("extendedAmount");
            discountAmount = rs.getString("discountAmount");

            vatExemptAmount = rs.getString("vatExemptAmount");
            vat12Amount = rs.getString("vat12Amount");
            vatsaleAmount = rs.getString("vatsaleAmount");

            ReceiptServed = rs.getString("totalAmount");
            float receiptAmt = Float.parseFloat(ReceiptServed);

            RefundCount = getImptCountFromDB("refundCount");
            if (RefundCount.compareToIgnoreCase("") == 0) {
                RefundCount = "0";
            }
            int refundCnt = Integer.parseInt(RefundCount);
            ExtendedCount = getImptCountFromDB("extendedCount");
            OvernightCount = getImptCountFromDB("overnightCount");
            discountCount = getImptCountFromDB("discountCount");
            vatExemptCount = getImptCountFromDB("vatExemptCount");
            vat12Count = getImptCountFromDB("vat12Count");
            vatsaleCount = getImptCountFromDB("vatsaleCount");
            
            
            totalCount = getImptCountFromDB("totalCount");
            totalAmount = rs.getString("totalAmount");
}

            DecimalFormat df2 = new DecimalFormat("#.00");
            eh.Justify((byte) 1);
            eh.printline("Regular-Pays   : " + rdataCount + " P " + df2.format(rdataAmount));
            eh.printline("Lost Card     : " + 0 + " P " + df2.format("0"));
            eh.printline("Overnight     : " + 0 + " P " + df2.format("0"));
            eh.printline("Discount      : " + discountCount + " P " + df2.format(discountAmount));
            eh.printline("---------------------------------------");
            eh.startPrinter();
            eh.Justify((byte) 0);
            eh.printline("Cash          : " + totalCount + " P " + df2.format(totalAmount));
            eh.printline("Credit        : " + 0 + " P 0.00");
            eh.printline("---------------------------------------");
            eh.startPrinter();
            eh.printline("TOTAL-COLL    : " + vatsaleCount + " P " + df2.format(vatsaleAmount));
            eh.printline("TOTAL-NET     : " + totalCount + " P " + df2.format(totalAmount));
            eh.printline("TOTAL-VAT     : " + vat12Count + " P " + df2.format(vat12Amount));
            eh.printline("VAT-EXEMP     : " + vatExemptCount + " P " + df2.format(vatExemptAmount));
            eh.printline("");
            eh.printline("");
            eh.printline("Teller Sign :__________________________");
            eh.feedpaperup((byte) 3);
            eh.printline("Supervisor  :__________________________");
            //      COUNT
//                    eh.printline("Cashier Name: " + CName);
//                    eh.printline("Cashier Code: " + CCode);
//                    eh.printline("Cashier Name: " + CName);
//                    eh.printline("Log In  Date: " + DateILogNow);
//                    eh.printline("Log In  Time: " + TimeILogNow);
//                    eh.printline("Loose Change: " + loosechange);
//                    eh.printline("Log out Date: " + DateOLogNow);
//                    eh.printline("Log out Time: " + TimeOLogNow);
            /*
                    eh.printline("");
                    eh.printline("                    Count     Amount");
                    //delay(1000);
                    
                    while (itr0.hasNext()) {
                        String entry = (String) itr0.next();
//                        System.out.println(entry);
                        String dataCount = rs.getString(entry.toLowerCase().trim() + "Count");
                        String dataAmount = rs.getString(entry.toLowerCase().trim() + "Amount");
                        //      COUNT
//                        System.out.print(dataCount);
//                        System.out.print(parkerTypeCount.get(entry));
                        parkerTypeCount.put(entry, dataCount);
//                        System.out.println(" Count: " + parkerTypeCount.get(entry));
                        //      AMOUNT
//                        System.out.print(dataAmount);
//                        System.out.print(parkerTypeAmount.get(entry));
                        parkerTypeAmount.put(entry, getAmountDue(Float.parseFloat(dataAmount)));
//                        System.out.println(" Amount: " + parkerTypeAmount.get(entry));
                        String out = dbh.formatSpaces(entry + " Parkers") + ": " + dataCount + "    " + parkerTypeAmount.get(entry);
                        eh.printline(out);
                    } */
            eh.startPrinter();

            eh.printline("");
            //eh.printline("Extended Count     : " + ExtendedCount);
            //eh.printline("Extended Amount    : " + ExtendedAmount);
            eh.printline("Overnight Count    :  " + OvernightCount);
            eh.printline("Overnight Amount   :  " + getAmountDue(Float.parseFloat(OvernightAmount)));
            eh.printline("Discount Count     :  " + discountCount);
            eh.printline("Discount Amount    :  " + getAmountDue(Float.parseFloat(discountAmount)));

            eh.printline("VAT Sale Amount    :  " + getAmountDue(Float.parseFloat(vatsaleAmount)));
            eh.printline("VAT 12% Amount     :  " + getAmountDue(Float.parseFloat(vat12Amount)));
            eh.printline("VAT Exempt Amount  :  " + getAmountDue(Float.parseFloat(vatExemptAmount)));

            eh.printline("");

            //eh.printline("Total Cars Served  :  " + extCarServed);
            //eh.printline("Total Collection   :  " + getAmountDue(receiptAmt));
            eh.startPrinter();
            eh.feedpaperup((byte) 3);
            //eh.fullcut();
            //eh.closePrinter();
            eh.startPrinter();
            eh.feedpaperup((byte) 8);
            eh.fullcut();
            eh.closeReceiptFile(Exitpoint);
            eh.closePrinter();

            if (currentcoll == false) {
                updateLogoutDB();
            }
            //deletion process for new login
            if (currentcoll == false) //true is for spot checking, false is for complete logout
            {
                scd.ResetCarServed();
                scd.ResetEntryTicketsServed();
                scd.ResetExitTicketsServed();
                scd.ResetCurrReceipt_Counter();  //reset this for the next LogIN 

//                while (itr1.hasNext()) {
//                    String entry = (String) itr1.next();
//                    System.out.println(entry);
//                    scd.UpdatePtypecount(entry, "0");
//                    scd.ErasePtypeAmount(entry);
//                }

//                String ReceiptServedSave = "0";
//                if (ReceiptServed.length() > 3) {
//                    ReceiptServedSave = ReceiptServed.substring(0, ReceiptServed.length() - 2);
//                }
//                String line = cashiercode + cashiername + LoginTimeSave + LoginDateSave + loosechange
//                        + LogoutTimeSave + LogoutDateSave
//                        + formatNos(ExtCarServed)
//                        + formatNos(ReceiptServedSave)
//                        + formatNos(RegularParkers) + formatNos(MotorcycleParkers)
//                        + formatNos(GraceParkers) + formatNos(VIPParkers) + formatNos(LostCardParkers)
//                        + formatNos(LCEPParkers) + formatNos(LCEPParkers)
//                        + formatNos(EXTicketServed) + formatNos(ENTicketServed)
//                        + formatNos(RegularAmount) + formatNos(MotorcycleAmount)
//                        + formatNos(LostAmount);
//
//                this.UpdateCollect(line);
//                if (currentcoll == false) {
//                    this.UpdateServerCollect(Exitpoint, line);
//                }
            }
        } catch (Exception ex) {
            log.error(ex.getMessage());
        }

    }

    //COLLTRAIN ERRORS
    public void epsonPrintLogoutReceiptFromDB_Old(String Exitpoint, boolean currentcoll) {
        try {
            SaveCollData scd = new SaveCollData();
            DateConversionHandler dch = new DateConversionHandler();
            DataBaseHandler dbh = new DataBaseHandler();
            Date LogStamp = new Date();
            DateFormat df = DateFormat.getDateInstance(DateFormat.SHORT);
            DateFormat tf = DateFormat.getTimeInstance(DateFormat.SHORT);

            XMLreader xr = new XMLreader();
            String logintime = xr.getElementValue("C://JTerminals/ginH.xml", "logindate");
            login_id = xr.getElementValue("C://JTerminals/ginH.xml", "log_id");
            String cashiercode = xr.getElementValue("C://JTerminals/ginH.xml", "cashier_id");
            String cashiername = xr.getElementValue("C://JTerminals/ginH.xml", "cashier_name");
            //cashiername = cashiername.replaceAll(" ", "");
            String loosechange = xr.getElementValue("C://JTerminals/ginH.xml", "loosechange");
            String ccode = "*" + cashiercode.toString() + "*";
            ccode = "**" + "**";
            Date LoginStamp = new Date(Long.parseLong(logintime));

            String RegularParkers = readDBCom("R");
            String MotorcycleParkers = readDBCom("M");
            String GraceParkers = readDBCom("G");
            String VIPParkers = readDBCom("V");
            String PromoParkers = readDBCom("P");
            String LostCardParkers = readDBCom("L");
            String LCEPParkers = "0";
            String DeliveryParkers = readDBCom("D");
            String QCSeniorParkers = readDBCom("Q");
            String NonQCSeniorParkers = readDBCom("NQ");
            //String BPOMotorParkers = readDBCom("BM");
            String PWDParkers = readDBCom("PW");

            String RegularAmount = getPtypeAmountFromDB("R");
            String MotorcycleAmount = getPtypeAmountFromDB("M");
            String LostAmount = getPtypeAmountFromDB("L");
            String PromoAmount = getPtypeAmountFromDB("P");
            String DeliveryAmount = getPtypeAmountFromDB("D");
            String QCSeniorAmount = getPtypeAmountFromDB("Q");
            String NonQCSeniorAmount = getPtypeAmountFromDB("NQ");
            //String BPOMotorAmount = getPtypeAmountFromDB("BM");
            String PWDAmount = getPtypeAmountFromDB("PW");

            String RefundCount = getImptCountFromDB("refundCount");
            int refundCnt = Integer.parseInt(RefundCount);
            String ExtendedCount = getImptCountFromDB("extendedCount");
            String OvernightCount = getImptCountFromDB("overnightCount");

            //String FixRetail = readComFiles("R1");
            //String SuccRetail = readComFiles("R2");
            //String EntCarServed = readComFiles("hcornell");
            String ExtCarServed = getImptCountFromDB("carServed");
            int extCarServed = Integer.parseInt(ExtCarServed);// - refundCnt;
            //String ExtCarServed = readComFiles("hbrentbay");
            String ENTicketServed = readComFiles("hbugs");
            String EXTicketServed = readComFiles("hbacta");

            String RefundAmount = getImptAmountFromDB("refundAmount");
            float refundAmt = Float.parseFloat(RefundAmount);
            String OvernightAmount = getImptAmountFromDB("overnightAmount");
            String ExtendedAmount = getImptAmountFromDB("extendedAmount");

            String ReceiptServed = getImptAmountFromDB("totalAmount");
            float receiptAmt = Float.parseFloat(ReceiptServed);
            //String ReceiptServed = readComFiles("Receipt");

            String DateOLogNow = df.format(LogStamp);
            String TimeOLogNow = tf.format(LogStamp);
            String DateILogNow = df.format(LoginStamp);
            String TimeILogNow = tf.format(LoginStamp);

            String LoginTimeSave = dch.convertTime2base(TimeILogNow.toString());
            String LoginDateSave = dch.convertDate2base(DateILogNow);
            String LogoutTimeSave = dch.convertTime2base(TimeOLogNow);
            String LogoutDateSave = dch.convertDate2base(DateOLogNow);

            //***********PRINT*******
            USBEpsonHandler eh = new USBEpsonHandler();
            eh.closePrinter();
            eh.openPrinter();
            eh.initializePrinter();
            eh.Justify((byte) 1);
            eh.setRed();

            if (currentcoll == false) {
                eh.printline("--- X READING ---");
                eh.printline("--Log Out Collection--");
            } else {
                //eh.printline("--Current Collection--");
            }
            eh.startPrinter();
            eh.feedpaperup((byte) 1);
            eh.Justify((byte) 0);

            eh.printline("Terminal ID : " + Exitpoint);
            eh.printline("Cashier Code: " + cashiercode);
            eh.printline("Cashier Name: " + cashiername);
            eh.printline("Log In  Date: " + DateILogNow);
            eh.printline("Log In  Time: " + TimeILogNow);
            eh.printline("Loose Change: " + loosechange);
            eh.printline("Log out Date: " + DateOLogNow);
            eh.printline("Log out Time: " + TimeOLogNow);
            eh.printline("");
            eh.printline("                    Count     Amount");
            eh.printline("Private Parkers    :  " + RegularParkers + "       " + RegularAmount);
            //eh.printline("    **Fix Rate     : " + FixRetail);
            //eh.printline("    **w/ Succ      : " + SuccRetail);
            eh.printline("Motorcycle Parkers :  " + MotorcycleParkers + "       " + MotorcycleAmount);
            eh.printline("Grace Parkers      :  " + GraceParkers + "       " + 0.00);
            eh.printline("VIP Parkers        :  " + VIPParkers + "       " + 0.00);
            eh.printline("Lost Card Parkers  :  " + LostCardParkers + "       " + LostAmount);
            //eh.printline("LCEP Parkers       : " + LCEPParkers);
            //eh.printline("Invalid Card Parker: " + InvalidFlatRateParkers);
            eh.printline("Promo Parkers      :  " + PromoParkers + "       " + PromoAmount);
            eh.printline("PWD Parkers        :  " + PWDParkers + "       " + PWDAmount);
            eh.printline("QC Senior Parkers  :  " + QCSeniorParkers + "       " + QCSeniorAmount);
            eh.printline("Non QC Parkers     :  " + NonQCSeniorParkers + "       " + NonQCSeniorAmount);
            eh.printline("Delivery Parkers   :  " + DeliveryParkers + "       " + DeliveryAmount);
            eh.printline("VOID               :  " + RefundCount + "       " + RefundAmount);
            //eh.printline("BPO Motor Parkers  : " + BPOMotorParkers + "     " + BPOMotorAmount);
            //eh.printline("");
            eh.printline("");
            //eh.printline("Extended Count     : " + ExtendedCount);
            //eh.printline("Extended Amount    : " + ExtendedAmount);
            eh.printline("Overnight Count    :  " + OvernightCount);
            eh.printline("Overnight Amount   :  " + OvernightAmount);

            eh.printline("");

            eh.printline("Total Cars Served  :  " + extCarServed);
            eh.printline("Total Collection   :  " + getAmountDue(receiptAmt));
            eh.startPrinter();
            eh.feedpaperup((byte) 3);
            //eh.fullcut();
            //eh.closePrinter();

            //ResultSet rs1 = dbh.getTodaysZReadbydateColl(totalCollected, Sale12Vat, vatSale);
            ResultSet rs = dbh.getTodaysZReadbyloginID(login_id);
            //dbh.getTodaysTotalCollectionBydateColl();
            String beginOR = "";
            String beginTrans = "";
            String beginGrandTotal = "";
            int beginORnum = 0;
            while (rs.next()) {
                beginOR = rs.getString("beginOR");
                beginTrans = rs.getString("beginTrans");
                beginGrandTotal = rs.getString("oldGrand");

                //***********PRINT*******
            }

            beginORnum = Integer.parseInt(beginOR);
            beginOR = formatNos(String.valueOf(beginORnum));
            Float totalCollected = dbh.getImptAmount("totalAmount", login_id);
            Double Sale12Vat = (double) (totalCollected / 1.12) * 0.12f;
            //Double Sale12Vat = (double) totalCollected * 0.12;
            Double vatSale = totalCollected - Sale12Vat;

            String receiptNos = scd.getReceiptNos();
            String grandTotal = scd.getGRANDTOTAL();
            String lastTransaction = dbh.getLastTransaction(Exitpoint);

            eh.printline("Beginning OR No.  : " + Exitpoint + beginOR);
            eh.printline("Ending OR No.     : " + Exitpoint + receiptNos);
            eh.printline("Beginning Balance : " + getAmountDue(Float.parseFloat(beginGrandTotal)));
            eh.printline("Ending Balance    : " + getAmountDue(Float.parseFloat(grandTotal)));

            eh.startPrinter();
            eh.feedpaperup((byte) 8);
            eh.fullcut();
            eh.closeReceiptFile(Exitpoint);
            eh.closePrinter();

            if (currentcoll == false) {
                updateLogoutDB();
            }

            /*
            USBEpsonHandler eh = new USBEpsonHandler();
            eh.initializePrinter();
            eh.Justify((byte) 1);
            eh.setRed();
            eh.feedpaperup((byte) 2);
            if (currentcoll == false) {
                eh.printline("--Log Out Collection--");
            } else {
                eh.printline("--Current Collection--");
            }

            eh.feedpaperup((byte) 1);
            eh.Justify((byte) 0);
            eh.printline("Terminal ID : " + Exitpoint);
            eh.printline("Cashier Code: ****");//+ccode
            eh.printline("Cashier Name: " + cashiername);
            eh.printline("Log In  Date: " + DateILogNow);
            eh.printline("Log In  Time: " + TimeILogNow);
            eh.printline("Loose Change: " + loosechange);
            eh.printline("Log out Date: " + DateOLogNow);
            eh.printline("Log out Time: " + TimeOLogNow);
            eh.feedpaperup((byte) 1);
            //delay(500000);
            //delay(1000);
            eh.printline("Retail Parkers     : " + RegularParkers);
            eh.printline("    **Fix Rate     : " + FixRetail);
            eh.printline("    **w/ Succ      : " + SuccRetail);
            eh.printline("Motorcycle Parkers : " + MotorcycleParkers);
            eh.printline("Monthly Parkers    : " + MPPParkers);
            eh.printline("Grace Parkers      : " + GraceParkers);
            eh.printline("Jogger Parkers     : " + JoggerParkers);
            eh.printline("Delivery Parkers   : 0");
            eh.printline("VIP Parkers        : " + VIPParkers);
            eh.printline("OCLP Parkers       : 0");
            eh.printline("Lost Card Parkers  : " + LostCardParkers);
            eh.printline("LCEP Parkers       : " + LCEPParkers);
            eh.printline("Invalid Card Parker: " + InvalidFlatRateParkers);
            eh.printline("Promo Parkers      : " + PromoParkers);

            eh.feedpaperup((byte) 1);
            //delay(500000);
            //delay(1000);
            eh.printline("Total Entry Cars Served: " + EntCarServed);
            eh.printline("Total Exit  Cars Served: " + ExtCarServed);
            eh.printline("Total Entry Tickets    :" + ENTicketServed);
            eh.printline("Total Exit Tickets     :" + EXTicketServed);
            eh.printline("Total Collection : " + ReceiptServed);

            eh.feedpaperup((byte) 4);
            eh.printHEADER(Exitpoint);
            eh.fullcut();
            eh.closeprinter();
            
             */
            //deletion process for new login
            if (currentcoll == false) //true is for spot checking, false is for complete logout
            {
                scd.ResetCarServed();
                scd.ResetEntryTicketsServed();
                scd.ResetExitTicketsServed();
                scd.ResetCurrReceipt_Counter();  //reset this for the next LogIN 
                scd.UpdatePtypecount("R", "0");  //RETAIL
                scd.UpdatePtypecount("M", "0");  //MOTORCYCLE
                scd.UpdatePtypecount("P", "0");  //PREPAID
                scd.UpdatePtypecount("PW", "0"); //PWD
                scd.UpdatePtypecount("G", "0");  //GRACE
                scd.UpdatePtypecount("V", "0");  //VIP
                scd.UpdatePtypecount("L", "0");  //LOST
                scd.UpdatePtypecount("E", "0");  //LCEP
                scd.UpdatePtypecount("J", "0");  //JOGGERS
                scd.UpdatePtypecount("O", "0");  //PROMO
                scd.UpdatePtypecount("F", "0");  //Invalid FLATRATES
                scd.UpdatePtypecount("Q", "0");  //QC Senior
                scd.UpdatePtypecount("NQ", "0");  //BPO Cars
                scd.UpdatePtypecount("BM", "0");  //BPO Motors
                scd.UpdatePtypecount("D", "0");  //DELIVERY
                scd.UpdatePtypecount("R1", "0");  //Fixed Retail
                scd.UpdatePtypecount("R2", "0");  //Succeeding Retail

                scd.ErasePtypeAmount("R");
                scd.ErasePtypeAmount("M");
                scd.ErasePtypeAmount("L");
                scd.ErasePtypeAmount("D");
                scd.ErasePtypeAmount("PW");
                scd.ErasePtypeAmount("Q");
                scd.ErasePtypeAmount("NQ");
                scd.ErasePtypeAmount("BM");

                String ReceiptServedSave = "0";
                if (ReceiptServed.length() > 3) {
                    ReceiptServedSave = ReceiptServed.substring(0, ReceiptServed.length() - 2);
                }
                String line = cashiercode + cashiername + LoginTimeSave + LoginDateSave + loosechange
                        + LogoutTimeSave + LogoutDateSave
                        + formatNos(ExtCarServed)
                        + formatNos(ReceiptServedSave)
                        + formatNos(RegularParkers) + formatNos(MotorcycleParkers)
                        + formatNos(GraceParkers) + formatNos(VIPParkers) + formatNos(LostCardParkers)
                        + formatNos(LCEPParkers) + formatNos(LCEPParkers)
                        + formatNos(EXTicketServed) + formatNos(ENTicketServed)
                        + formatNos(RegularAmount) + formatNos(MotorcycleAmount)
                        + formatNos(LostAmount);

                this.UpdateCollect(line);
                if (currentcoll == false) {
                    this.UpdateServerCollect(Exitpoint, line);
                }

            }
        } catch (Exception ex) {
            log.error(ex.getMessage());
        }

    }

    public void printLoginUSBSTUB(Date logStamp, String logcode, String logname, String Exitpoint) {
        try {
            USBEpsonHandler eh = new USBEpsonHandler();

            DateFormat df = DateFormat.getDateInstance(DateFormat.SHORT);
            DateFormat tf = DateFormat.getTimeInstance(DateFormat.SHORT);

            XMLreader xr = new XMLreader();

            String DateILogNow = df.format(logStamp);
            String TimeILogNow = tf.format(logStamp);
            setLoginSeriesFromDB();
            String loginSeries = getLoginSeriesFromDB();
            //logcode = "*" + logcode + "*";

            //StringBuilder str = new StringBuilder();
            //str.append("ESC @\n");//Init
            eh.printline("--Log In Stub--");
            eh.printline("Login Series No." + loginSeries);
            eh.printline("Terminal ID : " + Exitpoint + "");
            eh.printline("Cashier Name : " + logname + "");
            eh.printline("Log In  Date : " + DateILogNow + "");
            eh.printline("Log In  Time : " + TimeILogNow + "");
            eh.printline("Terminal ID : " + Exitpoint + "");
            eh.startPrinter();
            eh.feedpaperup((byte) 6);
            eh.fullcut();

        } catch (Exception ex) {
            log.error(ex.getMessage());
        }

    }

    public void printLoginSTUB(Date logStamp, String logcode, String logname, String Exitpoint) {
        try {
            DateFormat df = DateFormat.getDateInstance(DateFormat.SHORT);
            DateFormat tf = DateFormat.getTimeInstance(DateFormat.SHORT);

            XMLreader xr = new XMLreader();

            String DateILogNow = df.format(logStamp);
            String TimeILogNow = tf.format(logStamp);
            logcode = "*" + logcode + "*";

            StringBuilder str = new StringBuilder();
            //str.append("ESC @\n");//Init
            str.append("ESC ! 0\n");//Font A 0or1
            str.append("ESC E 0\n");//Emphasized 0or1
            str.append("ESC a 1\n");//Center 0-1-2 L-C-R
            str.append("ESC SP 1\n");//Init
            str.append("ESC - 0 \n");//Init

            str.append("\"--Log In Stub--\" CR LF CR LF\n");

            str.append("\"Terminal ID : " + Exitpoint + "\" CR LF\n");
            str.append("\"Cashier Name : " + logname + "\" CR LF\n");
            str.append("\"Log In  Date : " + DateILogNow + "\" CR LF\n");
            str.append("\"Log In  Time : " + TimeILogNow + "\" CR LF\n");
            //str.append("\"Terminal ID : " + Exitpoint + "\" CR LF\n");

            str.append("ESC E 1\n");//Emphasized 0or1
            str.append("ESC a 0\n");//Center 0-1-2 L-C-R

            str.append("ESC d 10\n");//Feed 10 lines
            str.append("GS V 1\n");//Send Cut

            rfh.putfile("C://JTerminals/Outline/", "lin", str.toString());

            String pingCmd = "C://JTerminals/senddat.exe C://JTerminals/Outline/lin USBPRN0";
            Runtime r = Runtime.getRuntime();
            Process p = r.exec(pingCmd);
            p.waitFor();

        } catch (Exception ex) {
            log.error(ex.getMessage());
        }

    }

    public void setLoginSeriesFromDB() throws IOException {
        DataBaseHandler dbh = new DataBaseHandler();
        int loginSeries = Integer.parseInt(getLoginSeriesFromDB());
        loginSeries++;
        try {
            dbh.setLoginSeries(loginSeries);
        } catch (SQLException ex) {
            log.error(ex.getMessage());
        }
    }

    public String getLoginSeriesFromDB() throws IOException {
        DataBaseHandler dbh = new DataBaseHandler();
        String ComData = "0";
        try {
            ComData = String.valueOf(dbh.getLoginSeries());
        } catch (SQLException ex) {
            log.error(ex.getMessage());
        }
        return ComData;
    }

    public boolean CheckValidCashierStamp(HybridPanelUI tpui) {
        CashierAPI ca = new CashierAPI();
        try {
            //this.clehrLeftMIDMsgPanel();
            String cID = ca.getCashierID();
            tpui.CashierName = ca.getCashierName();
            tpui.TellerName.setText(tpui.CashierName);
            if (cID.compareToIgnoreCase("") == 0) {
                tpui.stopEntranceTransacting();
                return false;
            } else {
                tpui.CashierID = cID;
                tpui.startEntranceTransacting();
                return true;
            }

        } catch (Exception ex) {
            log.error(ex.getMessage());
        }
        return false;
    }

    private void UpdateCollect(String line) {
        try {
            RawFileHandler rfh = new RawFileHandler();
            line = line.replaceAll(" ", "");
            rfh.appendfile("C://JTerminals/de4Dd87d/CfgJ9rl/", "collect.jrt", line);
        } catch (Exception ex) {
            log.error(ex.getMessage());
        }
    }

    private void UpdateServerCollect(String Exitpoint, String line) throws IOException {
        Date now = new Date();
        SimpleDateFormat mdf = new SimpleDateFormat("MM");
        SimpleDateFormat ydf = new SimpleDateFormat("yy");
        String extension = Exitpoint.substring(2, 4) + mdf.format(now) + ".0" + ydf.format(now);
        RawFileHandler rfh = new RawFileHandler();
        if (rfh.FindFileFolder("C://JTerminals/csys/") == false) {
            rfh.CreateNewFolder("C://JTerminals/csys/");
        }
        rfh.appendfile("C://JTerminals/csys/", "CASH" + extension, "COL-" + line);
        if (rfh.FindFileFolder("/SYSTEMS/online.aaa") == true) {
            //rfh.copytoserver("C://JTerminals/csys/", "CASH"+extension); // February 2009 Update
            Process t = Runtime.getRuntime().exec("sudo cp /JTerminals/csys/" + "CASH" + extension + " /SYSTEMS/"); //because copying to server takes time
        }
    }

    private String readDBCom(String Com) throws IOException {
        DataBaseHandler dbh = new DataBaseHandler();
        String name = dbh.getPtypeName(Com);
        name = name.trim().toLowerCase();
        String ComData = "0";
        try {
            ComData = dbh.getPtypecount(name, login_id);
        } catch (SQLException ex) {
            log.error(ex.getMessage());
        }
        return ComData;
    }

    private String readComFiles(String Com) throws IOException {
        RawFileHandler rfh = new RawFileHandler();
        String ComData = "0";
        if (rfh.FindFileFolder("C://JTerminals/de4Dd87d/CfgJ9rl/", Com + ".jrt") == true) {
            ComData = rfh.readFline("C://JTerminals/de4Dd87d/CfgJ9rl/", Com + ".jrt", 1);
        }
        return ComData;
    }

    private String getImptCountFromDB(String fieldName) throws IOException {
        DataBaseHandler dbh = new DataBaseHandler();
        String ComData = "0";
        try {
            ComData = dbh.getImptCount(fieldName, login_id);
        } catch (SQLException ex) {
            log.error(ex.getMessage());
        }
        return ComData;
    }

    private String getImptAmountFromDB(String fieldName) throws IOException {
        DataBaseHandler dbh = new DataBaseHandler();
        String ComData = "0.00";
        try {
            ComData = String.valueOf(dbh.getImptAmount(fieldName, login_id));
        } catch (SQLException ex) {
            log.error(ex.getMessage());
        }
        return ComData;
    }

    private String getPtypeAmountFromDB(String Com) throws IOException {
        DataBaseHandler dbh = new DataBaseHandler();
        String name = dbh.getPtypeName(Com);
        String ComData = "0.00";
        try {
            ComData = String.valueOf(dbh.getPtypeAmount(name, login_id));
        } catch (SQLException ex) {
            log.error(ex.getMessage());
        }
        return ComData;
    }

    public String getPtypeAmount(String Ftype) {
        String newcurr = "0";
        try {
            boolean foundfile = rfh.FindFileFolder("C://JTerminals/FnF/iXyZp12R/", Ftype + ".dat");
            if (foundfile == true) {
                String curr = rfh.readFline("C://JTerminals/FnF/iXyZp12R/", Ftype + ".dat", 1);

                float amt = Float.parseFloat(curr);

                newcurr = String.valueOf(amt);
            } else {
                newcurr = "0";
            }
        } catch (Exception ex) {
            log.error(ex.getMessage());
        }
        return newcurr;
    }

    public String formatNos(String newReceipt) throws IOException {
        int stoploop = 12 - newReceipt.length();
        int i = 0;
        do {
            newReceipt = "0" + newReceipt;
            i++;
        } while (i != stoploop);

        return newReceipt;
    }

    public String formatBack(String intNos) throws IOException {
        String StrNos = "";
        try {
            int i = Integer.parseInt(intNos);
            StrNos = String.valueOf(i);
        } catch (Exception ex) {
            float f = Float.parseFloat(intNos);
            StrNos = String.valueOf(f);
        }
        return StrNos;
    }

    private void updateLogoutDB() {
        try {
            DataBaseHandler dbh = new DataBaseHandler();
            XMLreader xr = new XMLreader();
            login_id = xr.getElementValue("C://JTerminals/ginH.xml", "log_id");
            dbh.updateTimeRecord("logoutStamp", "CURRENT_TIMESTAMP", login_id);
        } catch (Exception ex) {
            log.error(ex.getMessage());
        }
    }

    public static void main(String[] args) throws Exception {
        LoginMOD lm = new LoginMOD();
        //lm.readComFiles("M");
        //lm.printLogoutReceipt("EX01");
        lm.printCollectReceipt("EX01");
        //String cname = lm.getLOGINDATcashiername("00090009");
        //log.info(cname);
        //lm.saveLogintoFile("11223344");
    }

    void setDateofLastZRead(String sentinel) {
        DataBaseHandler dbh = new DataBaseHandler();
        dbh.updateZReadLastDate(sentinel);
    }

    private String getAmountDue(float AmountDue) {
        if (AmountDue == 0) {
            return "0.00";
        }
        DecimalFormat df2 = new DecimalFormat("#.00");
        return df2.format(AmountDue);

    }

    public void printHEADER(String EX_SentinelID) {
        USBEpsonHandler eh = new USBEpsonHandler();
        eh.closePrinter();
        eh.openPrinter();
        //eh.initializePrinter();
        //eh.Justify((byte) 0);
        //eh.setRed();
        eh.printHEADER(EX_SentinelID);
        eh.closeReceiptFile(EX_SentinelID);
    }

    public void printFOOTER(String EX_SentinelID) {
        USBEpsonHandler eh = new USBEpsonHandler();
        eh.closePrinter();
        eh.openPrinter();
        eh.initializePrinter();
        eh.Justify((byte) 0);
        eh.setRed();
        eh.printFOOTER(EX_SentinelID, true);
    }

}
