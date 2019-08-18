/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package misc;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.Authenticator;
import java.net.PasswordAuthentication;
import java.net.URL;
import java.net.URLConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import org.apache.log4j.LogManager;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import modules.SystemStatus;

/**
 *
 * @author Administrator Remember: Entrance and Exit use this Module
 */
public class ParkerDataHandler {

    static Logger log = LogManager.getLogger(ParkerDataHandler.class.getName());

    String EntryID = "Entry Zone 2";
    Statement stmt = null;
    PreparedStatement statement = null;
    Connection conn = null;
    RawFileHandler rfh = new RawFileHandler();

    public boolean saveParkerSTUB(String AreaID, String entranceID, String Card, String Plate, String TRType, String DateTimeIN) {
        try {
            rfh.putfile("/Offline/", Card.toString() + ".crd", entranceID + Card + Plate + TRType + DateTimeIN);
            rfh.putfile("/Offline/", Plate.toString() + ".plt", entranceID + Card + Plate + TRType + DateTimeIN);
            return true;
        } catch (Exception ex) {
            log.error(ex.getMessage());
            return false;
        }
    }

    public void uploadLog(String SentinelID) {
        DateConversionHandler dch = new DateConversionHandler();
        Date theTime = new Date();
        DateFormat df = DateFormat.getDateInstance(DateFormat.SHORT);
        String datetoday = df.format(theTime);
        String filetoday = dch.convertDate2base(datetoday);
        //local log
        if (rfh.FindFileFolder("C://JTerminals/" + SentinelID + filetoday + ".log") == true) {
            try {
                if (rfh.copytoserver("C://JTerminals/", SentinelID + filetoday + ".log") == false) {
                    try {
                        Process s = Runtime.getRuntime().exec("sudo cp /JTerminals/" + SentinelID + filetoday + ".log" + " /SYSTEMS");
                    } catch (Exception ex) {
                        log.error(ex.getMessage());
                    }
                }
            } catch (Exception ex) {
                log.error(ex.getMessage());
            }
        }
    }

    public boolean copyParkerTrans2Server(String SentinelID) {

        String Nday = null;
        Calendar curr = Calendar.getInstance();
        Date now = new Date();
        Integer yearInt = now.getYear() + 1900;
        Integer monthInt = now.getMonth();
        Integer dateInt = now.getDate();

        curr.set(yearInt, monthInt, dateInt);  //month is at index 1

        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");

        curr.add(Calendar.DATE, -1);

        Nday = formatter.format(curr.getTime());
        Date FileStamp = curr.getTime();
        int YrStamp = FileStamp.getYear();
        YrStamp = YrStamp - 100;

//        String filename = SentinelID + this.formatMonthStamp(FileStamp.getMonth()) + this.formatDayStamp(FileStamp.getDate()) + this.formatYearStamp(YrStamp);
//        if (rfh.copytoserver("C://JTerminals/", filename) == false) {
//            try {
//                Process s = Runtime.getRuntime().exec("sudo cp /JTerminals/" + filename + " /SYSTEMS"); //Linux Update Time
//            } catch (Exception ex) {
//                log.error(ex.getMessage());
//            }
//        } else {
//            return true;
//        }
        return false;
    }

    public boolean saveParker2Server33(String AreaID, String entranceID, String Card, String Plate, String TRType, String DateTimeIN) {

        try {
            if (entranceID.equalsIgnoreCase("") == true) {
                return false;
            }
            if (Card.equalsIgnoreCase("") == true) {
                return false;
            }
            if (Plate.equalsIgnoreCase("") == true) {
                return false;
            }
            if (TRType.equalsIgnoreCase("") == true) {
                return false;
            }
            if (DateTimeIN.equalsIgnoreCase("") == true) {
                return false;
            }
            /**
             * ********************* Old school if
             * (rfh.FindFileFolder("/SYSTEMS/", "online.aaa") == true ||
             * rfh.FindFileFolder("/SYSTEMS/", "ONLINE.AAA") == true) { if
             * (rfh.putfile("/SYSTEMS/", Card.toString() + ".crd", entranceID +
             * Card + Plate + TRType + DateTimeIN) == true) {
             * rfh.putfile("/SYSTEMS/", Plate.toString() + ".plt", entranceID +
             * Card + Plate + TRType + DateTimeIN); return true; } else { return
             * false; } // rfh.putfile("/SYSTEMS/"+AreaID+"/", Card.toString() +
             * ".crd", entranceID+Card+Plate+TRType+DateTimeIN); //
             * rfh.putfile("/SYSTEMS/"+AreaID+"/", Plate.toString() + ".plt",
             * entranceID+Card+Plate+TRType+DateTimeIN); } else {
             * rfh.putfile("C://JTerminals/", Card.toString() + ".off",
             * entranceID + Card + Plate + TRType + DateTimeIN);
             * //rfh.putfile("C://JTerminals/", Plate.toString() + ".plt",
             * entranceID+Card+Plate+TRType+DateTimeIN); return false; }
             * ********************
             */

        } catch (Exception ex) {
            //log.error(ex.getMessage());
            return false;
        }
        return false;
    }

    public boolean saveParkerDB(String ipaddress, String username, String password, String AreaID, String entranceID, String Card, String Plate, String TRType, String DateTimeIN, Long timeStampIN) {
        DataBaseHandler DB = new DataBaseHandler();
        //DateTimeIN should now be null because Mysql is inserting a default timestamp
        //DateTimeIN = "";
        URLConnection uc1 = null;
        URLConnection uc2 = null;
        InputStream is1 = null;
        InputStream is2 = null;
        javax.net.ssl.HttpsURLConnection.setDefaultHostnameVerifier(
                new javax.net.ssl.HostnameVerifier() {

            public boolean verify(String hostname,
                    javax.net.ssl.SSLSession sslSession) {
                return hostname.equals(ipaddress);
            }
        });
        Authenticator.setDefault(new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password.toCharArray());
            }
        });
        try {
            String loginPassword = username + ":" + password;
            String encoded = new sun.misc.BASE64Encoder().encode(loginPassword.getBytes());

            //URL url = new URL("http://www.avajava.com/images/avajavalogo.jpg");
            //HIKVISION IP Cameras Old Versions
            //URL url = new URL("http://" + username + ":" + password + "@" + ipaddress + "/Streaming/channels/1/picture");
            //HIKVISION IP Cameras
            URL url = new URL("http://" + username + ":" + password + "@" + ipaddress + "/onvif-http/snapshot?Profile_1");
            //HIKVISION DVR
            //URL url = new URL("http://"+username+":"+password+"@"+ipaddress+"/onvifsnapshot/media_service/snapshot?channel=1&subtype=0");
            //URL url = new URL("http://192.168.100.220/onvifsnapshot/media_service/snapshot?channel=1&subtype=1");
            //URL url = new URL("http://admin:user1234@192.168.100.220/cgi-bin/snapshot.cgi?loginuse=admin&loginpas=user1234");

            //**********************
            uc1 = url.openConnection();
            uc2 = url.openConnection();
            String userpass = username + ":" + password;
            //String userpass = "root" + ":" + "Th30r3t1cs";
            String basicAuth = "Basic " + new String(new sun.misc.BASE64Encoder().encode(userpass.getBytes()));
            uc1.setRequestProperty("Authorization", basicAuth);
            uc2.setRequestProperty("Authorization", basicAuth);

            uc1.setConnectTimeout(1000);
            uc2.setConnectTimeout(1000);
            try {
                if (null != uc1) {
                    is1 = (InputStream) uc1.getInputStream();
                }
            } catch (Exception ex) {
//                ex.printStackTrace();
            }
            try {
                if (null != uc2) {
                    is2 = (InputStream) uc2.getInputStream();
                }
            } catch (Exception ex) {
//                ex.printStackTrace();
            }

            conn = DB.getConnection(true);
            //WITH CAMERA TO DATABASE
            String SQL = "INSERT INTO unidb.timeindb (`ID`, `CardCode`, `Vehicle`, `Plate`, `Operator`, `PC`, `PIC`, `PIC2`, `Lane`, `Timein`) VALUES "
                    + "(NULL, ?, 'CAR' , ?, NULL, ?, ?, ?, 'ENTRY', ?)";
            statement = conn.prepareStatement(SQL);
            if (null != is1 && null != is2) {
                statement = conn.prepareStatement(SQL);
                statement.setBinaryStream(4, is1, 1024 * 96); //Last Parameter has to be bigger than actual      
                statement.setBinaryStream(5, is2, 1024 * 96); //Last Parameter has to be bigger than actual 
                statement.setString(6, DateTimeIN);
            }
            if (null == is1 && null != is2) {
                SQL = "INSERT INTO unidb.timeindb (`ID`, `CardCode`, `Vehicle`, `Plate`, `Operator`, `PC`, `PIC`, `PIC2`, `Lane`, `Timein`) VALUES "
                        + "(NULL, ?, 'CAR' , ?, NULL, NULL, ?, ?, 'ENTRY', ?)";
                statement = conn.prepareStatement(SQL);
                statement.setBinaryStream(4, is2, 1024 * 96); //Last Parameter has to be bigger than actual 
                statement.setString(5, DateTimeIN);
            }
            if (null != is1 && null == is2) {
                SQL = "INSERT INTO unidb.timeindb (`ID`, `CardCode`, `Vehicle`, `Plate`, `Operator`, `PC`, `PIC`, `PIC2`, `Lane`, `Timein`) VALUES "
                        + "(NULL, ?, 'CAR' , ?, NULL, ?, NULL, ?, 'ENTRY', ?)";
                statement = conn.prepareStatement(SQL);
                statement.setBinaryStream(4, is1, 1024 * 96); //Last Parameter has to be bigger than actual 
                statement.setString(5, DateTimeIN);
            }
            if (null == is1 && null == is2) {
                SQL = "INSERT INTO unidb.timeindb (`ID`, `CardCode`, `Vehicle`, `Plate`, `Operator`, `PC`, `PIC`, `PIC2`, `Lane`, `Timein`) VALUES "
                        + "(NULL, ?, 'CAR' , ?, NULL, ?, NULL, NULL, 'ENTRY', ?)";
                statement = conn.prepareStatement(SQL);
                statement.setString(4, DateTimeIN);
            }
            statement.setString(1, Card);
            statement.setString(2, Plate);
            statement.setString(3, EntryID);

            statement.executeUpdate();

            //int status2 = stmt.executeUpdate("INSERT INTO unidb.timeindb (ID, CardCode, Vehicle, Plate, Timein, Operator, PC, PIC, PIC2, Lane) "
            //        + "VALUES (NULL, '" + Card + "', 'CAR', '" + Plate + "', NOW(), NULL, '" + EntryID + "', NULL, NULL, 'LANE')");
            //int status2 = stmt.executeUpdate("INSERT INTO crdplt.main (areaID, entranceID, cardNumber, plateNumber, trtype, isLost, datetimeIN, datetimeINStamp) "
            //        + "VALUES ('" + AreaID + "', '" + entranceID + "', '" + Card + "', '" + Plate + "', '" + TRType + "', '0', '" + DateTimeIN + "','" + timeStampIN.toString() + "')");
            if (TRType.compareToIgnoreCase("M") == 0) {
                DB.Slotsminus1("motor");
            } else {
                DB.Slotsminus1("car");
            }

            return true;
        } catch (FileNotFoundException e) {
            //System.out.println("FileNotFoundException: - " + e);
        } catch (Exception e) {
            //System.out.println("Exception: - " + e);
        } finally {

            try {
                conn.close();
                statement.close();
                is1.close();
                is2.close();
            } catch (Exception e) {
                //System.out.println("Exception Finally: - " + e);
            }
        }
        return false;
    }

    public boolean saveEXParkerTrans2DB(String serverIP, String SentinelID, String TransactionNum, String Entrypoint, String ReceiptNo, String CashierID, String CashierName, String Card, String Plate, String TRType, String DateTimeIN, String DateTimeOUT, String AmountGross, String AmountPaid, long HoursElapsed, long MinutesElapsed, String settlementRef, String settlementName, String settlementAddr, String settlementTIN, String settlementBusStyle, double VAT12, double VATSALE, double VATEXEMPT, String discount, Double tenderFloat, String changeDue) {
//001000000000234,11111111,SERVCE,E01,000001,GELO01,V,0542,04142008,0543,04142008,B,O,C,0
//RECEIPT NO     ,ID      ,Name  ,SW1,CARD  ,PLT   ,T,Time,DATEIN  ,TOut,DATEOUT , , , ,AMOUNT 
        //SW03157842GELO47R1207072478203 <<-.crd .plt

//INSERT INTO `exit_trans` (`ReceiptNumber`, `CashierName`, `EntranceID`, `ExitID`, `CardNumber`, `PlateNumber`, `ParkerType`, `Amount`, `DateTimeIN`, `DateTimeOUT`) 
        //VALUES ('R0000131', 'JENNY', 'EN01', 'EX01', 'DBAE', 'TEST001', 'R', '30', '2017-05-28 06:33:31', CURRENT_TIMESTAMP);
        DataBaseHandler DB = new DataBaseHandler();
        try {

            //DateTimeIN should now be null because Mysql is inserting a default timestamp
            //DateTimeIN = "";
            if (null == DateTimeIN || DateTimeIN.compareToIgnoreCase("") == 0) {  //LOST CARD Scenario
                DateTimeIN = "CURRENT_TIMESTAMP";
            } else {
                DateTimeIN = "'" + DateTimeIN + "'";
            }
            if (changeDue.compareToIgnoreCase("") == 0) {
                changeDue = "0";
            }
            String SQLA = "insert into carpark.exit_trans "
                    + "values(null, 0, null, '" + ReceiptNo + "', '" + CashierID + "', '" + Entrypoint + "', '" + SentinelID + "', '" + Card + "', '" + Plate + "', '" + TRType + "', '" + AmountPaid + "', '" + AmountGross + "', '"+ discount + "', '" + VAT12 + "', '" + VATSALE + "', '" + VATEXEMPT + "', " + DateTimeIN + "" + ", CURRENT_TIMESTAMP, " + HoursElapsed + ", " + MinutesElapsed + ", '" + settlementRef + "', '" + settlementName + "', '" + settlementAddr + "', '" + settlementTIN + "', '" + settlementBusStyle + "' )";
            //INSERT INTO `incomereport` (`ID`, `TRno`, `Cardcode`, `Plate`, `Operator`, `PC`, `Timein`, `TimeOut`, `BusnessDate`, `Total`, `Vat`, `NonVat`, `VatExemp`, `TYPE`, `Tender`, `Change`, `Regular`, `Overnight`, `Lostcard`, `Payment`, `DiscountType`, `DiscountAmount`, `DiscountReference`, `Cash`, `Credit`, `CreditCardid`, `CreditCardType`, `VoucherAmount`, `GPRef`, `GPDiscount`, `GPoint`, `CompliType`, `Compli`, `CompliRef`, `PrepaidType`, `Prepaid`, `PrepaidRef`) VALUES (NULL, '2-38932', 'ABC23456', 'ABC123', 'cindy', 'POS-2', '2019-07-18 06:29:00', '2019-07-18 12:43:00', '2019-07-18', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
            String SQL = "INSERT INTO unidb.incomereport (`ID`, `TRno`, `Cardcode`, `Plate`, `Operator`, `PC`, `Timein`, `TimeOut`, `BusnessDate`, `Total`, `Vat`, `NonVat`, `VatExemp`, `TYPE`, `Tender`, `Change`, `Regular`, `Overnight`, `Lostcard`, `Payment`, `DiscountType`, `DiscountAmount`, `DiscountReference`, `Cash`, `Credit`, `CreditCardid`, `CreditCardType`, `VoucherAmount`, `GPRef`, `GPDiscount`, `GPoint`, `CompliType`, `Compli`, `CompliRef`, `PrepaidType`, `Prepaid`, `PrepaidRef`) "
                    + "VALUES (NULL, '" + ReceiptNo + "', '" + Card + "', '" + Plate + "', '" + CashierID + "', 'POS-2', " + DateTimeIN + ", '" + DateTimeOUT + "', CURRENT_DATE, '" + AmountGross + "', '" + VAT12 + "', '" + VATSALE + "', '" + VATEXEMPT + "', 'REGULAR', '" + tenderFloat + "', '" + changeDue + "', '" + AmountGross + "', '0', '0', 'Regular', '-', " + discount + ", '-', '" + AmountPaid + "', '0', NULL, NULL, '0', NULL, '0', '0', NULL, '0', NULL, NULL, '0', NULL)";

            //        + "values(null, 0, null, '" + ReceiptNo + "', '" + CashierID + "', '" + Entrypoint + "', '" + SentinelID + "', '" + Card + "', '" + Plate + "', '" + TRType + "', '" + Amount + "', " + DateTimeIN + "" + ", CURRENT_TIMESTAMP, " + HoursElapsed + ", " + MinutesElapsed + ", '" + settlementRef + "', '" + settlementName + "', '" + settlementAddr + "', '" + settlementTIN + "', '" + settlementBusStyle + "' )";
            try {
                conn = DB.getConnection(true);
                stmt = conn.createStatement();
                int status2 = stmt.executeUpdate(SQL);
                int status3 = stmt.executeUpdate(SQLA);
                return true;
            } catch (Exception ex) {
                conn = DB.getConnection(false);
                stmt = conn.createStatement();
                log.info("Print Error in : " + SQL);
                int status2 = stmt.executeUpdate(SQL);
                int status3 = stmt.executeUpdate(SQLA);
                return true;
            }

        } catch (Exception ex) {
            log.error(ex.getMessage());
            return false;
        } finally {
            try {
                DB.saveLog("E0", CashierID, ReceiptNo);
                stmt.close();
                conn.close();
            } catch (SQLException ex) {
                //log.error(ex.getMessage());
            }
        }

    }

    public boolean saveEXParkerTrans2VOIDDB(String VOIDrefNumber, String SentinelID, String TransactionNum, String Entrypoint, String ReceiptNo, String CashierID, String CashierName, String Card, String Plate, String TRType, String DateTimeIN, double Amount, long HoursElapsed, long MinutesElapsed, String settlementRef, String settlementName, String settlementAddr, String settlementTIN, String settlementBusStyle) {
//001000000000234,11111111,SERVCE,E01,000001,GELO01,V,0542,04142008,0543,04142008,B,O,C,0
//RECEIPT NO     ,ID      ,Name  ,SW1,CARD  ,PLT   ,T,Time,DATEIN  ,TOut,DATEOUT , , , ,AMOUNT 
        //SW03157842GELO47R1207072478203 <<-.crd .plt

//INSERT INTO `exit_trans` (`ReceiptNumber`, `CashierName`, `EntranceID`, `ExitID`, `CardNumber`, `PlateNumber`, `ParkerType`, `Amount`, `DateTimeIN`, `DateTimeOUT`) 
        //VALUES ('R0000131', 'JENNY', 'EN01', 'EX01', 'DBAE', 'TEST001', 'R', '30', '2017-05-28 06:33:31', CURRENT_TIMESTAMP);
        try {
            DataBaseHandler DB = new DataBaseHandler();
            //DateTimeIN should now be null because Mysql is inserting a default timestamp
            //DateTimeIN = "";
            if (null == DateTimeIN || DateTimeIN.compareToIgnoreCase("") == 0) {  //LOST CARD Scenario
                DateTimeIN = "CURRENT_TIMESTAMP";
            } else {
                DateTimeIN = "'" + DateTimeIN + "'";
            }
            String SQL = "insert into carpark.void_trans "
                    + "values(null, 1, LPAD('" + VOIDrefNumber + "',12,0), '" + ReceiptNo + "', '" + CashierID + "', '" + Entrypoint + "', '" + SentinelID + "', '" + Card + "', '" + Plate + "', '" + TRType + "', '" + Amount + "', " + DateTimeIN + "" + ", CURRENT_TIMESTAMP, " + HoursElapsed + ", " + MinutesElapsed + ", '" + settlementRef + "', '" + settlementName + "', '" + settlementAddr + "', '" + settlementTIN + "', '" + settlementBusStyle + "' )";
            try {
                conn = DB.getConnection(true);
                stmt = conn.createStatement();
                int status2 = stmt.executeUpdate(SQL);
                return true;
            } catch (Exception ex) {
                conn = DB.getConnection(false);
                stmt = conn.createStatement();
                log.info("Print Error in : " + SQL);
                int status2 = stmt.executeUpdate(SQL);
                return true;
            }
        } catch (Exception ex) {
            log.error(ex.getMessage());
            return false;
        }

    }

    public boolean updateEXParkerTrans4VOID(String VOIDrefNumber, String ReceiptNo) {
        try {
            DataBaseHandler DB = new DataBaseHandler();
//            LPAD('" + VOIDrefNumber + "',12,0), '" 
            String SQL = "UPDATE carpark.exit_trans "
                    + "SET void = 1, voidRefID = LPAD('" + VOIDrefNumber + "',12,0) WHERE ReceiptNumber = '" + ReceiptNo + "';";
            try {
                conn = DB.getConnection(true);
                stmt = conn.createStatement();
                int status2 = stmt.executeUpdate(SQL);
                return true;
            } catch (Exception ex) {
                conn = DB.getConnection(false);
                stmt = conn.createStatement();
                log.info("Print Error in : " + SQL);
                int status2 = stmt.executeUpdate(SQL);
                return true;
            }
        } catch (Exception ex) {
            log.error(ex.getMessage());
            return false;
        }

    }

    public boolean saveOldEXParkerTrans(String serverIP, String SentinelID, String Entrypoint, String ReceiptNo, String CashierID, String CashierName, String Card, String Plate, String TRType, String DateTimeIN, String DateIN, String TimeOUT, String DateOUT, String Amount) {
//001000000000234,11111111,SERVCE,E01,000001,GELO01,V,0542,04142008,0543,04142008,B,O,C,0
//RECEIPT NO     ,ID      ,Name  ,SW1,CARD  ,PLT   ,T,Time,DATEIN  ,TOut,DATEOUT , , , ,AMOUNT 
        //SW03157842GELO47R1207072478203 <<-.crd .plt
        Date FileStamp = new Date();
        int YrStamp = FileStamp.getYear();
        YrStamp = YrStamp - 100;
        EncryptionTool enc = new EncryptionTool();
        String filename = SentinelID + this.formatMonthStamp(FileStamp.getMonth()) + this.formatDayStamp(FileStamp.getDate()) + this.formatYearStamp(YrStamp);
        String encfilename = "X" + SentinelID.substring(1) + this.formatMonthStamp(FileStamp.getMonth()) + this.formatDayStamp(FileStamp.getDate()) + this.formatYearStamp(YrStamp);
        if (Entrypoint.length() == 4) {
            Entrypoint = "E" + Entrypoint.substring(2, 4);
        }
        String formattedAmount = Amount.substring(0, Amount.length() - 2);
        String output = ReceiptNo + "," + CashierID + "," + CashierName + "," + Entrypoint + "," + Card + "," + Plate + "," + TRType + "," + DateTimeIN + "," + DateIN + "," + TimeOUT + "," + DateOUT + ",B,O,C," + formattedAmount;
        String encryptedoutput = reformatENCRYPTION(enc.encrypt(output.toString(), "itheoretics"));
        try {
            rfh.appendfile("C://JTerminals/", filename, output);
        } catch (Exception ex) {
            log.error(ex.getMessage());
        }
//        try{
//            SystemStatus ss = new SystemStatus();
//            if (ss.checkPING(serverIP)==true)
//            {
//                if (ss.checkOnline()==true)
//                {                 
//                   rfh.copySource2Dest("C://JTerminals/"+ filename, "/SYSTEMS/"+ filename);
//                }
//            }
//        }
//        catch(Exception ex)
//        {
//            log.error(ex.getMessage());
//        }
        try {
            rfh.appendfile("C://JTerminals/", encfilename, encryptedoutput.toString());
        } catch (Exception ex) {
            log.error(ex.getMessage());
        }
//        try{
//            SystemStatus ss = new SystemStatus();
//            if (ss.checkPING(serverIP)==true)
//            {
//                if (ss.checkOnline()==true)
//                {
//                rfh.appendfile("/SYSTEMS/", encfilename, encryptedoutput.toString());   
//                }
//            }
//            
//        }
//        catch(Exception ex)
//        {
//            log.error(ex.getMessage());
//            return false;
//        }
        return true;
        //log.info(Long.valueOf(DateTimeIN));
        //computetime(Long.valueOf(DateTimeIN));
    }

    public boolean saveENParkerTrans(String CashierID, String CashierName, String entranceID, String Card, String Plate, String TRType, String DateTimeIN) {

        //SW03,157842,GELO47,R,1207,772478203
        //010210,ANGELO,E01,213684,PNZ159,R,0921,01102006,B,O
        //00010001,TELER1,EN03,388589,TES111,R,2150
        DateConversionHandler dch = new DateConversionHandler();
        EncryptionTool enc = new EncryptionTool();
        Date FileStamp = new Date();
        DateFormat df = DateFormat.getDateInstance(DateFormat.SHORT);
        String DateNow = df.format(FileStamp);
        String DateNowSave = dch.convertDate2base(DateNow);

        int YrStamp = FileStamp.getYear();
        YrStamp = YrStamp - 100;

        String filename = entranceID + this.formatMonthStamp(FileStamp.getMonth()) + this.formatDayStamp(FileStamp.getDate()) + this.formatYearStamp(YrStamp);
        String encfilename = "N" + entranceID.substring(1) + this.formatMonthStamp(FileStamp.getMonth()) + this.formatDayStamp(FileStamp.getDate()) + this.formatYearStamp(YrStamp);
        if (entranceID.length() == 4) {
            entranceID = "E" + entranceID.substring(2, 4);
        }
        String output = CashierID + "," + CashierName + "," + entranceID + "," + Card + "," + Plate + "," + TRType + "," + DateTimeIN + "," + DateNowSave + ",B,O";
        String encryptedoutput = reformatENCRYPTION(enc.encrypt(output.toString(), "itheoretics"));

        try {
            rfh.appendfile("C://JTerminals/", filename, output);
        } catch (Exception ex) {
            log.error(ex.getMessage());
        }
//        try{
//            rfh.appendfile("/SYSTEMS/", filename, output);
//        }
//        catch(Exception ex)
//        {
//            log.error(ex.getMessage());
//        }
        try {
            rfh.appendfile("C://JTerminals/", encfilename, encryptedoutput.toString());
        } catch (Exception ex) {
            log.error(ex.getMessage());
        }

//        try{
//            rfh.appendfile("/SYSTEMS/", encfilename, encryptedoutput.toString());
//            
//        }
//        catch(Exception ex)
//        {
//            log.error(ex.getMessage());
//            return false;
//        }
        return true;
        //log.info(Long.valueOf(DateTimeIN));
        //computetime(Long.valueOf(DateTimeIN));
    }

    private String reformatENCRYPTION(String oldencryption) {
        StringBuffer strbuff = new StringBuffer("");
        int i = 0;
        while (i != oldencryption.length()) {
            String x = String.valueOf(oldencryption.charAt(i));

            if (x.compareToIgnoreCase("\n") != 0 && x.compareToIgnoreCase("\r") != 0) {
                strbuff.append(oldencryption.charAt(i));
            }
            i++;
        }
        return strbuff.toString();
    }

    private String formatYearStamp(int YrStamp) {
        String temp = "";
        if (YrStamp <= 9) {
            temp = ".00" + String.valueOf(YrStamp);
        } else if (YrStamp > 9) {
            temp = ".0" + String.valueOf(YrStamp);
        }
        return temp;
    }

    private String formatMonthStamp(int MonthStamp) {
        String temp = "";
        MonthStamp = MonthStamp + 1;
        if (MonthStamp <= 9) {
            temp = "0" + String.valueOf(MonthStamp);
        } else if (MonthStamp > 9) {
            temp = String.valueOf(MonthStamp);
        }
        return temp;
    }

    private String formatDayStamp(int DayStamp) {
        String temp = "";
        if (DayStamp <= 9) {
            temp = "0" + String.valueOf(DayStamp);
        } else if (DayStamp > 9) {
            temp = String.valueOf(DayStamp);
        }
        return temp;
    }

}
