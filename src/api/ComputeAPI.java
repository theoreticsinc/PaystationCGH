package api;

import UserInteface.HybridPanelUI;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.apache.log4j.LogManager;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import misc.DataBaseHandler;
import misc.DateConversionHandler;
import misc.LogUtility;
import misc.ParkerDataHandler;
import misc.XMLreader;
import modules.SaveCollData;
import modules.SystemStatus;

/**
 * @author root Changed May. 6, 2017 Instead of Computing just the exit date, We
 * compute from the start day til the end day. Problems coordinating with the
 * Exit Date and the adding from the start of days Expected...
 */
public class ComputeAPI {

    static Logger log = LogManager.getLogger(ComputeAPI.class.getName());

    public String[] PrksMsg = new String[21];
    public long HoursElapsed = 0;
    public long MinutesElapsed = 0;
    public long HoursPaidElapsed = 0;
    public long MinutesPaidElapsed = 0;
    public long NumHrs2ComputeCurrent = 0;

    private int HourIN = 0;
    private int MinIN = 0;
    private int HourPaid = 0;
    private int MinPaid = 0;
    private HybridPanelUI stn;
    private String Entrypoint = "";
    private String Cardno = "";
    private String Plateno = "";
    private String ParkerType = "";
    private boolean isLost;
    private String datetimeIN = "";
    private String datetimeOUT = "";
    private String dateTimeINstamp;
    private String datetimePaid = "";
    private String dateTimePaidstamp;
    private String TimeIN = "";
    private String sDateIN = "";
    private String sTimeIN = "";
    private String TimeOUT = "";
    private String CardCheck = "";
    private String PlateCheck = "";
    private String sDateOUT = "";
    private boolean sets = false;
    private boolean IFR = false;
    public boolean FoundPlate = false;
    private double CGHAmountGross;
    private double CGHAmountDue;
    private double AmountGross;
    private double AmountDue;
    private double NetOfDiscount;
    private double tenderFloat;
    private double CGHtenderFloat;
    private float AmountPaid = 0;
    private String RNos = "";
    private String sTimeOUT = "";
    private boolean OvernightOverride = false;
    private boolean JoggerOverride = false;
    private boolean MotorcycleOverriden = false;
    private boolean PrinterEnabled = false;
    private Date datePaid;
    private int flagOvernights = 0;
    private int flagExtended = 0;
    private double OvernightPrice = 0;
    private double ExtendedPrice = 0;
    private boolean hasOvernight = false;
    private boolean hasExtended = false;

    private float extendedHoursAmount = 200;
    private ReadMIFARE mifare;

    private boolean dataFromCard;
    private Date ParkerStamp;
    private Long today;
    private Long timeStampIN;
    private Long timeStampPaid;
    private Long nextDueTimeStamp = 0L;
    private boolean printerCutter;
    private String printerType;
    private String datamode;
    private String exitType;

    public ParkersAPI SP;

    public ComputeAPI(HybridPanelUI sui) {
        stn = sui;
        mifare = new ReadMIFARE();
        XMLreader xr = new XMLreader();

        try {
            printerType = xr.getElementValue("C://JTerminals/initH.xml", "printerType");
            printerCutter = xr.getElementValue("C://JTerminals/initH.xml", "printerCutter").compareToIgnoreCase("true") == 0;
            exitType = xr.getElementValue("C://JTerminals/initH.xml", "exitType");
            datamode = xr.getElementValue("C://JTerminals/initH.xml", "datamode");
        } catch (Exception ex) {
            log.error(ex.getMessage());
        }

    }

    /**
     * @param GSCoverride = true if GSC wants to print out a receipt without
     * card data
     * @return 0 = successful, 1 = no card, 2 = no plate, 3 = started only, 4 =
     * card found but unprocessed, 5 = card rescanned still nothing, 6 = card
     * rescanned and successful 7 = parker type in card is wrong 8 = wrong
     * date/time 9 = errors found
     * @throws java.lang.Exception *
     */
    public short isValidInputController(Date NowStamp, boolean GSCoverride, boolean firstscan, String Override, boolean PrinterOverride) {
        PrinterEnabled = PrinterOverride;
        HoursElapsed = 0;
        MinutesElapsed = 0;
        datePaid = new Date();
        //Check this first :: Maybe because of locale
        //datePaid.setHours(datePaid.getHours());
        short retval = 3;
        boolean cardempty = false;
        sets = false;
        LogUtility logthis = new LogUtility();

        SP = new ParkersAPI();
        //ParkersAPI pa = new ParkersAPI();

        CardCheck = stn.CardInput2.getText().substring(0, stn.CardDigits);

        stn.clearRightPanel();
        if (stn.payuponentry.compareToIgnoreCase("enabled") == 0) {
            //Start Entrance Procedure First
            EntranceAPI na = new EntranceAPI(stn);
            na.InitiateEntry(stn.trtype);

        }
        try {
            //---start of card checking
            if (mifare.terminal.isCardPresent()) {
                if (datamode.compareToIgnoreCase("card") == 0 || datamode.compareToIgnoreCase("cards") == 0) {
                    sets = mifare.retrieveCRDPLTFromCard(false);
                    dataFromCard = true;
                } else if (datamode.compareToIgnoreCase("db") == 0) {
                    if (firstscan) {
                        stn.SavedStamp = NowStamp;
                        sets = SP.retrieveCRDPLTFromDB(CardCheck, stn.serverIP, true);
                    } else {
                        NowStamp = stn.SavedStamp;
                        sets = SP.retrieveCRDPLTFromDB(CardCheck, stn.serverIP, true);
                        //sets = SP.retrieveEXTCRDFromDB(CardCheck, stn.serverIP, true);
                    }
                    dataFromCard = false;
                }
            }
        } catch (Exception ex) {
            log.error(ex.getMessage());
            if (stn.LostOverride) {
                retval = 0;
                sets = true;
            }
            retval = 10;
        }
        //---start of DB checking
        if (sets == false) {
            if (firstscan) {
                sets = SP.retrieveCRDPLTFromDB(CardCheck, stn.serverIP, true);
            } else {
                sets = SP.retrieveEXTCRDFromDB(CardCheck, stn.serverIP, true);
            }
            dataFromCard = false;
        }

        if (sets == true) {
            //sets = SP.checkDatafromServerCRDPLT(CardCheck, true, false);//this checks the data length

            //***********Angelo
            if (sets == false) {
                logthis.setLog(stn.EX_SentinelID, "Card found: " + CardCheck + " * error reading data length");
                cardempty = true;
                retval = 1;
                if (firstscan == false) {
                    retval = 5;
                }
            } else {
                retval = 4;
            }
        } else if (sets == false) {
            logthis.setLog(stn.EX_SentinelID, "Card not found: " + CardCheck);
            cardempty = true;
            retval = 1;
            if (firstscan == false) {
                retval = 5;
            }
        }
        //      1a******

        // +++++++++ this overrides invalid flatrates
        if (GSCoverride == true) {
            PlateCheck = stn.PlateInput2.getText().substring(0, stn.PlateDigits);
            sets = SP.checkDatafromServerCRDPLT(PlateCheck, false, false);
            if (sets == true) {
                FoundPlate = true;
            }
            cardempty = true;
        }

        // +++++++++ this overrides invalid flatrates
        if (sets == true || stn.LCEPOverride == true || GSCoverride == true || stn.LostOverride == true) {
            if (sets == true) {
//                //copy to last plate/card first
                if (cardempty == false) {
                    //SP.copyCRDPLTtoLAST(CardCheck, stn.serverIP);
                } else if (cardempty == true) {
                    //SP.copyCRDPLTtoLAST(PlateCheck, stn.serverIP);
                }
            }
            DateConversionHandler dch = new DateConversionHandler();
            ParkerStamp = datePaid;
            /*
            if (sets == true) {
                //   *+*+*+*+*+*this stores data in SYSTEMS file to memory*+*+*+*+*
                if (GSCoverride == true) {
                    //SP.getDatafromServerCRDPLT(PlateCheck, false, false);
                    //To Totally move from the problems of the network
                    SP.getDatafromtrayCRDPLT();
                } else if (cardempty == true) {
                    //SP.getDatafromServerCRDPLT(PlateCheck, false, false);
                    SP.getDatafromtrayCRDPLT();
                } else {
                    SP.getDatafromServerCRDPLT(CardCheck, true, false);
                    //SP.getDatafromtrayCRDPLT();
                }
                //   *+*+*+*+*+*this stores data in SYSTEMS file to memory*+*+*+*+*
                logthis.setLog(stn.EX_SentinelID, ParkerStamp + " plate num: " + PlateCheck + "card num: " + CardCheck);
             */
            if (dataFromCard) {
                // Get From Card
                Entrypoint = mifare.getEntID();
                Cardno = mifare.getCardID();
                Plateno = mifare.getPlateID();  //Always Blank
                ParkerType = mifare.getTrID();
            } else {
                if (stn.LostOverride) {
                    Entrypoint = "EN01";
                    Cardno = "12345678";
                    Plateno = stn.LostCardEntryPlate.getText().trim().toUpperCase();
                    ParkerType = "R";
                    isLost = true;
                    AmountPaid = 0;
                } else {
                    // Get From Database
                    SP.startDataSplit();
                    Entrypoint = SP.getSysID();
                    Cardno = SP.getCardID();
                    Plateno = SP.getPlateID();
                    ParkerType = SP.getTRID();
                    isLost = SP.getIsLost();
                    AmountPaid = Float.parseFloat(SP.getAmountPaid());
                }

            }
            if (null == Plateno) {
                Plateno = stn.PlateInput2.getText();
            } else if (Plateno.compareToIgnoreCase("") == 0) {
                Plateno = stn.PlateInput2.getText();
            } else if (Plateno.compareToIgnoreCase("Empty") == 0) {
                Plateno = stn.PlateInput2.getText();
            } else if (Plateno.compareToIgnoreCase("null") == 0) {
                Plateno = stn.PlateInput2.getText();
            }
            //***************PLATE NO Override
            if (firstscan) {
                //For Paystation Only
                //Plateno = stn.PlateInput2.getText();
                datetimeIN = SP.getTimeID();
                datetimePaid = SP.getDateTimePaid();
                dateTimeINstamp = SP.getDateTimeINStamp();
                dateTimePaidstamp = SP.getDateTimePaidStamp();
            }

            if (SP.checkPTypeFromDB(ParkerType).equalsIgnoreCase("NOTFOUND") == true) {
                return 7;
            }
            if (dataFromCard == false) {
                // Get From Database
                datetimeIN = SP.getTimeID();
                datetimePaid = SP.getDateTimePaid();
                dateTimeINstamp = SP.getDateTimeINStamp();
                dateTimePaidstamp = SP.getDateTimePaidStamp();

                SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
                SimpleDateFormat sdf2 = new SimpleDateFormat("MM/dd/yy HH:mm");
                Date dtStamp = null;
                try {
                    if (stn.LostOverride) {
                        String dateManuallyCreated = stn.lostEntryDate.getText() + " " + stn.lostEntryTime.getTime();
                        dtStamp = sdf2.parse(dateManuallyCreated);
                        datetimeIN = sdf1.format(dtStamp);
                        dateTimeINstamp = dch.convertEverArduinoDate2UnixTime(dtStamp) + "";
                        sets = true;
                    } else {
                        dtStamp = sdf1.parse(datetimeIN);
                    }
                } catch (Exception ex) {
                    log.error(ex.getMessage());
                }
                HourIN = dtStamp.getHours();
                MinIN = dtStamp.getMinutes();

                if (firstscan == false) {

                    try {
                        if (null != datetimePaid) {
                            dtStamp = sdf1.parse(datetimePaid);
                        }
                    } catch (Exception ex) {
                        log.error(ex.getMessage());
                    }
                    HourPaid = dtStamp.getHours();
                    MinPaid = dtStamp.getMinutes();

                }
            } else {
                // Get From CARD
                datetimeIN = mifare.getTimestampINID();
                dateTimeINstamp = mifare.getTimestampINID();
            }

            ///----end of card checking---
            //-----check for overrides
            if (Override.compareToIgnoreCase("") == 0) {
                if (stn.LostOverride == true) {
                    ParkerType = "L";
                } else if (stn.LCEPOverride == true) {
                    ParkerType = "E";
                    logthis.setLog(stn.EX_SentinelID, "LCEP pressed  : " + CardCheck);
                } else if (stn.VIPOverride == true) {
                    ParkerType = "V";
                } else if (stn.PrepaidOverride == true) {
                    ParkerType = "C";
                } else if (GSCoverride == true) {
                    ParkerType = "F";
                    IFR = true;
                } else if (stn.MotorOverride == true) {
                    ParkerType = "M";
                } else if (stn.QCSeniorOverride == true) {
                    ParkerType = "Q";
                } else if (stn.BPOCarOverride == true) {
                    ParkerType = "BC";
                } else if (stn.BPOMotorOverride == true) {
                    ParkerType = "BM";
                } else if (stn.DeliveryOverride == true) {
                    ParkerType = "D";
                }
            } else {
                ParkerType = Override;
            }

            //-----end of overrides
            if (sets == true) {
                try {
                    stn.SysMessage2.setText("Processing...");
                    ComputeInSeconds(dateTimeINstamp, dateTimePaidstamp);
                } catch (Exception ex) {
                    return 8;//Unable to convert the time from the card
                }
            }
            //if(datamode.compareToIgnoreCase("cards") == 0) {
            //    Plateno = "";
            //}
            PrksMsg[0] = Entrypoint;
            PrksMsg[1] = Cardno;
            PrksMsg[2] = Plateno;
            stn.npd.PlsPay.setText(Plateno + " please pay");
            PrksMsg[3] = SP.checkPTypeFromDB(ParkerType);
            stn.trtype = ParkerType;
            //ParkerInfo5.setText(datetime);
            if (sets == true) {
                try {
                    Long convlong = Long.valueOf(dateTimeINstamp);
                    ParkerStamp = dch.convertJavaUnixTime2Date(convlong);
                } catch (Exception ex) {
                    return 8;//Unable to convert the time from the card
                }
            }
            //Date NowStamp = datePaid;

            DateFormat df = DateFormat.getDateInstance(DateFormat.MEDIUM);
            SimpleDateFormat sdf = new SimpleDateFormat("MM-dd-yyyy");

            DateFormat tf = DateFormat.getTimeInstance(DateFormat.MEDIUM);
            DateFormat ds = DateFormat.getDateInstance(DateFormat.SHORT);
            DateFormat ts = DateFormat.getTimeInstance(DateFormat.SHORT);

            PrksMsg[5] = sdf.format(ParkerStamp);// + " Mins: " + tf.format(ParkerStamp);
            PrksMsg[6] = tf.format(ParkerStamp);
            SimpleDateFormat dbf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S");
            datetimeOUT = dbf.format(NowStamp);

            if (sets == true) {
                TimeIN = df.format(ParkerStamp) + " " + tf.format(ParkerStamp);
            } else //LCEP
            {
                TimeIN = df.format(ParkerStamp) + " " + tf.format(NowStamp);
            }
            TimeOUT = df.format(NowStamp) + " " + tf.format(NowStamp);
            if (sets == true) {
                sDateIN = dch.convertDate2base(ds.format(ParkerStamp));
            } else //LCEP
            {
                sDateIN = dch.convertDate2base(ds.format(NowStamp));
            }
            sDateOUT = dch.convertDate2base(ds.format(NowStamp));
            if (sets == true) {
                sTimeIN = dch.convertTime2base(ts.format(ParkerStamp));
            } else //LCEP
            {
                sTimeIN = dch.convertTime2base(ts.format(NowStamp));
            }
            sTimeOUT = dch.convertTime2base(ts.format(NowStamp));
            if (datamode.compareToIgnoreCase("cards") == 0) {
                SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-M-dd HH:mm:ss.S");
                datetimeIN = sdf2.format(ParkerStamp);
            }

            //Check this for Zero Amount
            if (stn.VIPOverride == true) {
                AmountDue = 0.00f;
                logthis.setLog(stn.EX_SentinelID, "VIP Override: " + CardCheck + ParkerStamp);
            } else if (stn.PrepaidOverride == true) {
                AmountDue = 0.00f;
                logthis.setLog(stn.EX_SentinelID, "Prepaid Override  : " + CardCheck + ParkerStamp);
            } else if (GSCoverride == true) {
                AmountDue = 50;
                logthis.setLog(stn.EX_SentinelID, "GSC Invalid Flatrate Override: " + CardCheck + ParkerStamp);
            } else {

                /////////////////*+*+*+*+*+*+*+*+*+*////////////////////////
                //AmountDue = this.NewComputeforAmount(NowStamp, ParkerStamp);
                if (ParkerType.compareToIgnoreCase("V") == 0) {
                    AmountDue = 0;
                } else {
                    firstscan = true;
                    AmountDue = this.Computation(ParkerType, firstscan, isLost);
                    CGHAmountDue = AmountDue;
                    AmountGross = AmountDue;
                }
                /////////////////*+*+*+*+*+*+*+*+*+*////////////////////////
                AmountDue = AmountDue - AmountPaid;
                CGHAmountDue = AmountDue;
                if (AmountDue < 0) {
                    AmountDue = 0;
                }
                if (AmountDue == 0) {
                    if (ParkerType.compareToIgnoreCase("G") != 0) {
                        logthis.setLog(stn.EX_SentinelID, stn.trtype + "Zero Amount: " + CardCheck + ParkerStamp);
                        //SP.copyCRDPLTtoZERO(CardCheck, stn.serverIP);
                    }
                }
            }
            if (isLost) {
                stn.trtype = "L";
                ParkerType = "L";
            }
            PrksMsg[3] = SP.checkPTypeFromDB(ParkerType);   //in case trtype was overriden
            //Write to MifareCard Here
            if (mifare != null && stn.LostOverride == false) {
                mifare.startWaiting4CardPresent();
                //String entdata = mifare.readDecoded16BytesbyBlockNum((byte) 0x04);
                //mifare.write16Chars2Block((byte) 0x04, "P1E1##" + datetimeIN);

                /*
            int M = NowStamp.getMonth();
            int d = NowStamp.getDate();
            int y = NowStamp.getYear();
            int h = NowStamp.getHours();
            int m = NowStamp.getMinutes();
            
            String mm = "";
            String dd = "";
            String yy = "";
            String hh = "";
            String mn = "";
            M++;
            if (M < 10) {
                mm = "0" + M;
            } else {
                mm = M + "";
            }
            if (d < 10) {
                dd = "0" + d;
            } else {
                dd = d + "";
            }
            
                y = y - 100;
                yy = y + "";
           
            if (h < 10) {
                hh = "0" + h;
            } else {
                hh = h + "";
            }
            if (m < 10) {
                mn = "0" + m;
            } else {
                mn = m + "";
            }
                 */
                //Date crdExtDate = datePaid;
                //Long today = dch.convertJavaDate2UnixTime4Card(crdExtDate);
                //SysMessage14.setText("Date OUT    : " + dch.convertInt2base(crdExtDate.getMonth() + 1) + "/" + dch.convertInt2base(crdExtDate.getDate()) + "/20" + (crdExtDate.getYear() - 100));
                //}
                //        SysMessage15.setText("Time OUT    : " + dch.convertInt2base(crdExtDate.getHours()) + ":" + dch.convertInt2base(crdExtDate.getMinutes()));
                //}
                //String outCard = "P1X1PR" + today;
                //mifare.write16Chars2Block((byte) 0x05, outCard);
                //outCard = "P1X1PR" + nextDueTimeStamp;
            }
            //mifare.write16Chars2Block((byte) 0x06, outCard);
//            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-M-dd HH:mm:ss.S");
//            String d = datetimeIN;
//            String p = sdf.format(datePaid);
//            String n = sdf.format(dch.convertJavaUnixTime2Date(nextDueTimeStamp));
//            SP.writeExitCRD2DB(CardCheck, d, p, n);
//            SP.eraseCRDPLTFromDB(CardCheck);
//            try {
//                if (updateAllTransFiles() == false)//Update Local Files
//                {
//                    return 9;
//                }
//                RNos = updateReceiptFiles(AmountDue);
//                if (RNos.equalsIgnoreCase("") == true) {
//                    return 9;
//                }
//            } catch (Exception ex) {
//            }
            // Slots Updating must be done before printing and saving transactions to terminal and server
            // because of the errors concerning the network.
            // it skips the saving of the exit slots if errors occur in saving the transaction files
//            this.updateNetSlots();
            PrksMsg[11] = "Entrance From";
            PrksMsg[12] = "Card Number";
            PrksMsg[13] = "Plate Number";
            PrksMsg[14] = "Transaction Type";
            PrksMsg[15] = "Duration";
            PrksMsg[16] = "Date IN";
            PrksMsg[17] = "Time IN";
            PrksMsg[7] = "P " + AmountPaid;
            PrksMsg[18] = "Amount Already Paid:";

            RNos = "" + stn.EX_SentinelID.substring(2, 4) + RNos;
            AmountGross = AmountDue;
            CGHAmountGross = AmountDue;

            int duplicateReceiptType = SP.checkDupReceiptFromPType(ParkerType);
            boolean isDiscounted = SP.getDiscounted(ParkerType);
            float discountPercentage = 0;
            double vat12 = getVat(AmountDue);
            double vatsale = getNonVat(AmountDue);
            String discount = "0.00";
            double vatexempt = 0;
            double vatExemptedSales = 0;
            AmountGross = AmountDue;
            DecimalFormat df2 = new DecimalFormat("#.00");
            stn.AMOUNTdisplay.setText("P" + String.valueOf(df2.format(AmountDue)));
            if (isDiscounted) {

                //*************** OLD COMPUTATION
                discountPercentage = SP.getdiscountPercentage(ParkerType);
                discount = getDiscountFromVat(AmountDue, discountPercentage);
                vat12 = 0;
                vatsale = getNonVat(AmountDue);
                vatexempt = getVat(AmountDue);
                CGHAmountDue = (vatsale - Float.parseFloat(discount));
                stn.AMOUNTdisplay.setText("P" + String.valueOf(df2.format(CGHAmountDue)));

                //******************************
                //*************NEW COMPUTATION                
                
                discountPercentage = SP.getdiscountPercentage(ParkerType);
                discount = getDiscountFromVat(AmountDue, discountPercentage);
                vat12 = 0;
                vatsale = 0;
                vatExemptedSales = getNonVat(AmountDue);
                NetOfDiscount = (vatExemptedSales - Float.parseFloat(discount));
                AmountDue = NetOfDiscount + (NetOfDiscount * .12);
                
                //stn.AMOUNTdisplay.setText("P" + String.valueOf(df2.format(AmountDue)));            
                //****************************
            } else {
                NetOfDiscount = 0;
            }

            //stn.AMOUNTdisplay.setText("P" + String.valueOf(df2.format(AmountDue)));
            stn.npd.PlateNo.setText("P" + String.valueOf(AmountDue) + "0");
            stn.PlateInput2.setText(Plateno);
            stn.Plateinput.delete(0, stn.Plateinput.length());
            stn.Plateinput.append(Plateno);
            stn.processRightPanelMsgs(PrksMsg);
            stn.repaint();
            stn.requestFocus();
            stn.validate();
            if (AmountDue == 0) {
                if (ParkerType.compareToIgnoreCase("G") != 0) {
                    logthis.setLog(stn.EX_SentinelID, stn.trtype + "Zero Amount after Updates: " + CardCheck + ParkerStamp);
                    //SP.copyCRDPLTtoZERO(CardCheck, stn.serverIP);
                }
            }
            return 0;
        }
        return retval;
    }

    public void ValidPartII() {
        stn.Cardinput.delete(0, stn.Cardinput.length());
        stn.CardInput2.setText("PROCESSING...");
        ParkersAPI SP = new ParkersAPI();
        ParkerDataHandler PDH = new ParkerDataHandler();
        SystemStatus ss = new SystemStatus();
        //REFRESH MEMORY
        /*
        if (ss.checkOnline() == true) {
            sets = SP.retrieveCRDPLTFromDB(CardCheck, stn.serverIP, true);
            //sets = SP.checkDatafromServerCRDPLT(CardCheck, true, false);
            if (sets == true) {
                SP.retrieveCRDPLTFromDB(CardCheck, stn.serverIP, false);
                SP.startDataSplit();
                Entrypoint = SP.getSysID();
                Cardno = SP.getCardID();
                Plateno = SP.getPlateID();
                //ParkerType = SP.getTRID();
                datetimeIN = SP.getTimeID();
                DatetimeINstamp = SP.getDateTimeINStamp();
            } else if (sets == false) {
                sets = SP.retrieveCRDPLTFromDB(PlateCheck, stn.serverIP, false);
                //sets = SP.checkDatafromServerCRDPLT(PlateCheck, false, false);
                if (sets == true) {
                    SP.getDatafromServerCRDPLT(PlateCheck, false, false);
                    Entrypoint = SP.getSysID();
                    Cardno = SP.getCardID();
                    Plateno = SP.getPlateID();
                    //ParkerType = SP.getTRID();
                    datetimeIN = SP.getTimeID();
                    DatetimeINstamp = SP.getDateTimeINStamp();
                }
            }
        }
         */
        //----------valid part II---------------
        String DuplicateReceiptHeader = "";
        ParkersAPI pa = new ParkersAPI();
        int duplicateReceiptType = pa.checkDupReceiptFromPType(ParkerType);
        boolean isDiscounted = pa.getDiscounted(ParkerType);
        float discountPercentage = 0;
        double CGHvat12 = getVat(CGHAmountDue);
        double CGHvatsale = getNonVat(CGHAmountDue);
        double vat12 = getVat(AmountDue);
        double vatsale = getNonVat(AmountDue);
        String discount = "0.00";
        String CGHdiscount = "0.00";
        double discountDbl = 0;
        double vatAdjustment = 0;
        double vatExemptedSales = 0;
        double vatexempt = 0;
        double CGHvatexempt = 0;
        //AmountGross = AmountDue;
        DecimalFormat df2 = new DecimalFormat("#.00");
        if (isDiscounted) {
            ///************** OLD Computation
            discountPercentage = pa.getdiscountPercentage(ParkerType);
            CGHdiscount = getDiscountFromVat(CGHAmountGross, discountPercentage);
            CGHvat12 = 0;
            CGHvatsale = getNonVat(CGHAmountGross);
            Double vatexemptF = getVat(CGHAmountGross);
            CGHvatexempt = vatexemptF;
            CGHAmountDue = (CGHvatsale - Float.parseFloat(CGHdiscount));
            stn.AMOUNTdisplay.setText("P" + String.valueOf(df2.format(CGHAmountDue)));
            //updateOneTransFiles("discount", Float.parseFloat(CGHdiscount));
            //updateOneTransFiles("vatExempt", CGHvatexempt);            
            //****************************
            // ************* NEW Discount            
            discountPercentage = pa.getdiscountPercentage(ParkerType);
            //discountDbl = getdDiscountFromVat(AmountGross, discountPercentage);
            discountDbl = getDiscount(AmountGross, discountPercentage);
            vatAdjustment = getVatAdjustment(AmountGross, discountPercentage);
            Double vT = getNonVat(AmountGross);       
            NetOfDiscount = (vT - discountDbl);
            AmountDue = NetOfDiscount + (NetOfDiscount * .12);
            //stn.AMOUNTdisplay.setText("P" + String.valueOf(df2.format(AmountDue)));
            updateOneTransFiles("exemptedVat12", 0);
            updateOneTransFiles("vatExemptedSales", 0);
            if (ParkerType.compareToIgnoreCase("PW") == 0) {
                updateOneTransFiles("pwdDiscount", discountDbl);
                updateOneTransFiles("vatAdjPWD", vatAdjustment);
            } else if (ParkerType.compareToIgnoreCase("S") == 0) {
                updateOneTransFiles("seniorDiscount", discountDbl);
                updateOneTransFiles("vatAdjSenior", vatAdjustment);
            } else if (ParkerType.compareToIgnoreCase("LS") == 0) {
                updateOneTransFiles("localSeniorDiscount", discountDbl);
                updateOneTransFiles("vatAdjLocalSenior", vatAdjustment);
            } else {
//                updateOneTransFiles("discountAmount", vatAdjustment);
            }
            
        } else {
            NetOfDiscount = 0;
        }
        updateOneTransFiles("vat12", vat12);
        updateOneTransFiles("vatsale", vatsale);
        updateOneTransFiles("gross", AmountGross);
        if (sets == true) {
            //ParkerType = stn.trtype;
            //ParkerType = pa.checkPTypeFromDB(stn.trtype).toUpperCase();
            if (stn.LostOverride) {
                ParkerType = "LOST";
            }
            DateConversionHandler dch = new DateConversionHandler();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-M-dd HH:mm:ss.S");
            String d = datetimeIN;
            String p = sdf.format(datePaid);
            String n = "";
            if (ParkerType.compareToIgnoreCase("V") == 0) {
                n = sdf.format(dch.convertJavaUnixTime2Date4DB(199478520));
            } else {
                n = sdf.format(dch.convertJavaUnixTime2Date4DB(nextDueTimeStamp));
            }
            //SP.writeExitCRD2DB(CardCheck, Plateno, d, p, n, stn.trtype, AmountDue);
            SP.eraseCRDPLTFromDB(CardCheck);
            try {
                if (datamode.compareToIgnoreCase("cards") == 0) {
                    if (mifare != null) {
                        mifare.eraseBlock((byte) 4);
                    }
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            if (exitType.compareToIgnoreCase("manned") == 0) {
                SP.eraseEXTCRDFromDB(CardCheck);
            }
            try {
                if (updateAllTransFiles() == false)//Update Parker Type Data Column
                {
                }
                if (updateTrans2DB() == false) {//Updates Car Served and Total Amount
                }

                RNos = updateReceiptFiles(AmountDue, AmountGross);
                //RNos = updateReceiptFiles(AmountDue);
                if (RNos.equalsIgnoreCase("") == true) {
                }
                RNos = stn.EX_SentinelID.substring(2, 4) + RNos;
            } catch (Exception ex) {
            }
            if (PrinterEnabled) {
                stn.CardInput2.setText("PRINTING...");
                Double tenderFloat = null;
                try {
                    if (stn.AmtTendered.getText().trim().compareToIgnoreCase("") != 0) {
                        tenderFloat = Double.parseDouble(stn.AmtTendered.getText());
                        CGHtenderFloat = Double.parseDouble(stn.AmtTendered.getText());
                    } else if (stn.AmtTendered.getText().trim().compareToIgnoreCase("") == 0) {
                        tenderFloat = AmountDue;
                        CGHtenderFloat = AmountDue;
                    }

                } catch (Exception x) {

                }
                if (duplicateReceiptType == 0) {
                    /*
                    if (printerType.compareToIgnoreCase("serial") == 0) {
                        SP.printSerialReceipt(stn.EX_SentinelID, Entrypoint, Plateno, ParkerType, TimeIN, TimeOUT, HoursElapsed, MinutesElapsed, AmountDue, RNos, stn.CashierName, stn.settlementRef);
                        //SP.saveReceiptforDUP(stn.EX_SentinelID, Entrypoint, Plateno, ParkerType, TimeIN, TimeOUT, HoursElapsed, MinutesElapsed, AmountDue, RNos, stn.CashierName, OvernightOverride);
                    } else {                    
                        SP.printUSBReceipt(stn.firstRun, false, stn.EX_SentinelID, Entrypoint, Plateno, ParkerType, TimeIN, TimeOUT, HoursElapsed, MinutesElapsed, AmountDue, RNos, stn.CashierID, stn.CashierName, stn.settlementRef, DuplicateReceiptHeader);
                    }*/
                } else if (duplicateReceiptType == 1) {
                    DuplicateReceiptHeader = "";
                    if (printerType.compareToIgnoreCase("serial") == 0) {
                        SP.printSerialReceipt(stn.EX_SentinelID, Entrypoint, Plateno, ParkerType, TimeIN, TimeOUT, HoursElapsed, MinutesElapsed, AmountDue, RNos, stn.CashierName, stn.settlementRef, DuplicateReceiptHeader);
                        //SP.saveReceiptforDUP(stn.EX_SentinelID, Entrypoint, Plateno, ParkerType, TimeIN, TimeOUT, HoursElapsed, MinutesElapsed, AmountDue, RNos, stn.CashierName, OvernightOverride);
                    } else {
                        SP.printUSBReceipt(stn.firstRun, false, stn.EX_SentinelID, Entrypoint, Plateno, CardCheck, ParkerType, TimeIN, TimeOUT, HoursElapsed, MinutesElapsed, AmountDue, AmountGross, vat12, vatsale, vatexempt, RNos, stn.CashierID, stn.CashierName, stn.settlementRef, stn.settlementName, stn.settlementAddr, stn.settlementTIN, stn.settlementBusStyle, DuplicateReceiptHeader, isDiscounted, discountPercentage, tenderFloat, stn.ChangeDisplay.getText(), discount, printerCutter);
                    }
                } else if (duplicateReceiptType == 2) {
                    DuplicateReceiptHeader = "            CUSTOMER COPY";
                    if (printerType.compareToIgnoreCase("serial") == 0) {
                        SP.printSerialReceipt(stn.EX_SentinelID, Entrypoint, Plateno, ParkerType, TimeIN, TimeOUT, HoursElapsed, MinutesElapsed, AmountDue, RNos, stn.CashierName, stn.settlementRef, DuplicateReceiptHeader);
                        //SP.saveReceiptforDUP(stn.EX_SentinelID, Entrypoint, Plateno, ParkerType, TimeIN, TimeOUT, HoursElapsed, MinutesElapsed, AmountDue, RNos, stn.CashierName, OvernightOverride);
                    } else {
                        SP.printUSBReceipt(stn.firstRun, false, stn.EX_SentinelID, Entrypoint, Plateno, CardCheck, ParkerType, TimeIN, TimeOUT, HoursElapsed, MinutesElapsed, CGHAmountDue, CGHAmountGross, CGHvat12, CGHvatsale, CGHvatexempt, RNos, stn.CashierID, stn.CashierName, stn.settlementRef, stn.settlementName, stn.settlementAddr, stn.settlementTIN, stn.settlementBusStyle, DuplicateReceiptHeader, isDiscounted, discountPercentage, CGHtenderFloat, stn.ChangeDisplay.getText(), CGHdiscount, printerCutter);
                    }
                    DuplicateReceiptHeader = "        ACCOUNTING / STORE COPY";
                    if (printerType.compareToIgnoreCase("serial") == 0) {
                        SP.printSerialReceipt(stn.EX_SentinelID, Entrypoint, Plateno, ParkerType, TimeIN, TimeOUT, HoursElapsed, MinutesElapsed, AmountDue, RNos, stn.CashierName, stn.settlementRef, DuplicateReceiptHeader);
                        //SP.saveReceiptforDUP(stn.EX_SentinelID, Entrypoint, Plateno, ParkerType, TimeIN, TimeOUT, HoursElapsed, MinutesElapsed, AmountDue, RNos, stn.CashierName, OvernightOverride);
                    } else {
                        SP.printUSBReceipt(false, false, stn.EX_SentinelID, Entrypoint, Plateno, CardCheck, ParkerType, TimeIN, TimeOUT, HoursElapsed, MinutesElapsed, CGHAmountDue, CGHAmountGross, CGHvat12, CGHvatsale, CGHvatexempt, RNos, stn.CashierID, stn.CashierName, stn.settlementRef, stn.settlementName, stn.settlementAddr, stn.settlementTIN, stn.settlementBusStyle, DuplicateReceiptHeader, isDiscounted, discountPercentage, CGHtenderFloat, stn.ChangeDisplay.getText(), CGHdiscount, printerCutter);
                    }
                }
            }
            try {
                //Remove Data from Main Database CRDPLT
                SP.delParkerCRDPLT2(Cardno);
            } catch (Exception ex) {
                log.error(ex.getMessage());
            }
            stn.firstRun = false;
            //if (SP.delParkerCRDPLTDB(Cardno, stn.serverIP) == false) {
            //SP.savetoOffline(Cardno + ".crd");//Redundancy deletion of card and plt
            //}
            //if (FoundPlate == true) {
            //    if (SP.delParkerPLTCRD(Plateno, stn.serverIP) == false) {
            //        SP.savetoOffline(Plateno + ".plt");//Redundancy deletion of card and plt
            //    }
            //}
        } else if (IFR == true) {
            //SP.printAutoFlatRateReceipt(stn.EX_SentinelID, Entrypoint, Plateno, ParkerType, TimeIN, TimeOUT, HoursElapsed, MinutesElapsed, AmountDue, RNos, stn.CashierName);
            //SP.printUSBReceipt(stn.firstRun, false, stn.EX_SentinelID, Entrypoint, Plateno, ParkerType, TimeIN, TimeOUT, HoursElapsed, MinutesElapsed, AmountDue, RNos, stn.CashierID, stn.CashierName, stn.settlementRef);

        } else {
            //SP.printLCEPReceipt(stn.EX_SentinelID, Entrypoint, Plateno, ParkerType, TimeOUT, AmountDue, RNos, stn.CashierName);
            tenderFloat = 0f;
            try {
                if (stn.AmtTendered.getText().trim().compareToIgnoreCase("") != 0) {
                    tenderFloat = Float.parseFloat(stn.AmtTendered.getText());
                } else {
                    tenderFloat = AmountDue;
                }

            } catch (Exception x) {

            }

            SP.printUSBReceipt(stn.firstRun, false, stn.EX_SentinelID, Entrypoint, Plateno, CardCheck, ParkerType, TimeIN, TimeOUT, HoursElapsed, MinutesElapsed, AmountDue, AmountGross, vat12, vatsale, vatexempt, RNos, stn.CashierID, stn.CashierName, stn.settlementRef, stn.settlementName, stn.settlementAddr, stn.settlementTIN, stn.settlementBusStyle, DuplicateReceiptHeader, isDiscounted, discountPercentage, tenderFloat, stn.ChangeDisplay.getText(), discount, printerCutter);

        }
        String transactionNum = stn.EX_SentinelID.substring(2) + "" + RNos;
        //String vat12 = getVat(AmountDue);
        //String nonvat = getNonVat(AmountDue);
        tenderFloat = 0f;
        try {
            if (stn.AmtTendered.getText().trim().compareToIgnoreCase("") != 0) {
                tenderFloat = Float.parseFloat(stn.AmtTendered.getText());
            } else {
                tenderFloat = AmountDue;
            }

        } catch (Exception x) {

        }
        String SaveParkerType = pa.checkPTypeFromDB(ParkerType).toUpperCase();
        if (SaveParkerType.contains("SENIOR")) {
            SaveParkerType = "SENIOR";
        }

        boolean saveParkerTrans = PDH.saveEXParkerTrans2CGHDB(stn.serverIP, stn.EX_SentinelID, transactionNum, Entrypoint, RNos, stn.CashierID, stn.CashierName, Cardno, Plateno, SaveParkerType, datetimeIN, datetimeOUT, String.valueOf(CGHAmountGross), String.valueOf(CGHAmountDue), HoursElapsed, MinutesElapsed, stn.settlementRef, stn.settlementName, stn.settlementAddr, stn.settlementTIN, stn.settlementBusStyle, CGHvat12, CGHvatsale, CGHvatexempt, CGHdiscount, CGHtenderFloat, stn.ChangeDisplay.getText());
        RNos = stn.EX_SentinelID + "" + RNos.substring(2, RNos.length());
        saveParkerTrans = PDH.saveEXParkerTrans2DB(stn.serverIP, stn.EX_SentinelID, transactionNum, Entrypoint, RNos, stn.CashierID, stn.CashierName, Cardno, Plateno, ParkerType, datetimeIN, datetimeOUT, String.valueOf(NetOfDiscount), String.valueOf(AmountGross), String.valueOf(AmountDue), HoursElapsed, MinutesElapsed, stn.settlementRef, stn.settlementName, stn.settlementAddr, stn.settlementTIN, stn.settlementBusStyle, vatAdjustment, vat12, vatsale, vatExemptedSales, discountDbl, tenderFloat, stn.ChangeDisplay.getText());
        if (saveParkerTrans == false) {    //save twice just in case
            //saveParkerTrans = PDH.saveEXParkerTrans2DB(stn.serverIP, stn.EX_SentinelID, transactionNum, Entrypoint, RNos, stn.CashierID, stn.CashierName, Cardno, Plateno, ParkerType, datetimeIN, String.valueOf(AmountGross), String.valueOf(AmountDue), HoursElapsed, MinutesElapsed, stn.settlementRef, stn.settlementName, stn.settlementAddr, stn.settlementTIN, stn.settlementBusStyle, vat12, vatsale, vatexempt, discount, tenderFloat, stn.ChangeDisplay.getText());
        }
        if (stn.PrepaidOverride == true) {
            SP.updateCouponList(stn.Prepaid2Save);
        }
        if (ParkerType.compareToIgnoreCase("M") == 0) {
            SP.addCarSlots("motor");
        } else {
            SP.addCarSlots("car");
        }
        //SP.UpdateCarSlots(stn.EX_SentinelID);
        stn.trtype = "R";
        stn.resetAllOverrides();
        stn.Cardinput.delete(0, stn.Cardinput.length());
        stn.CardInput2.setText("PRINTING...");
        stn.AmtTendered.setText("");
        stn.isEnterPressed = false;
        stn.PrevPlate = "";
    }

    private String getAmountDue(float AmountDue) {
        if (AmountDue == 0) {
            return "0.00";
        }
        DecimalFormat df2 = new DecimalFormat("#.00");
        return df2.format(AmountDue);

    }

    private String getVatSales(float AmountDue) {
        if (AmountDue == 0) {
            return "0.00";
        }
        DecimalFormat df2 = new DecimalFormat("#.00");
        //float vatSales = (float) (AmountDue * .12);
        float vatSales = (float) (AmountDue / 1.12) * 0.12f;
        AmountDue = AmountDue - vatSales;
        //return df2.format(AmountDue);
        return AmountDue + "";
    }

    private double getVat(double AmountDue) {
        if (AmountDue == 0) {
            return 0;
        }
        //DecimalFormat df2 = new DecimalFormat("#.00");

        //float vatSales = (float) (AmountDue * .12);
        double vatSales = (double) (AmountDue / 1.12) * 0.12f;
        //return df2.format(vatSales);
        return vatSales;
    }

    private double getNonVat(double AmountDue) {
        if (AmountDue == 0) {
            return 0;
        }
        DecimalFormat df2 = new DecimalFormat("#.00");

        //float vatSales = (float) (AmountDue * .12);
        double vatSales = (double) (AmountDue / 1.12);
        //return df2.format(vatSales);
        return vatSales;
    }

    private double getVatAdjustment(double AmountDue, float discountPercentage) {
        if (AmountDue == 0) {
            return 0D;
        }
        //float vatSales = (float) (AmountDue * .12);
        double adj = (double) AmountDue * (discountPercentage / 100) - ((AmountDue * (discountPercentage / 100)) / 1.12);
        //return df2.format(vatSales);
        return adj;
    }

    private double getDiscount(double AmountDue, float discountPercentage) {
        if (AmountDue == 0) {
            return 0D;
        }
        //float vatSales = (float) (AmountDue * .12);
        double discount = (double) (AmountDue * (discountPercentage / 100)) / 1.12;
        //return df2.format(vatSales);
        return discount;
    }

    private String getDiscountFromVat(double AmountDue, float discountPercentage) {
        if (AmountDue == 0) {
            return "0.00";
        }
        DecimalFormat df2 = new DecimalFormat("#.00");

        //float vatSales = (float) (AmountDue * .12);
        double vatSales = (double) (AmountDue / 1.12) * (discountPercentage / 100);
        //return df2.format(vatSales);
        return vatSales + "";
    }

    /**
     *
     * @param datetimeIN if HoursElapsed is > 24 then error in UTC .. just
     * subtract 24 because of 00:00:00 in computation instead of 12:00:00AM
     */
    private void ComputeInSeconds(String datetimeIN, String dateTimePaid) {
        DateConversionHandler dch = new DateConversionHandler();
//        Date dnow = datePaid;
//        Long convlong = Long.valueOf(datetext);
//        Date dnow2 = new Date(convlong);
//        Long convnow = dnow.getTime();
//        Long convlong2 = dnow2.getTime();
/*
        Long convnow = dch.convertNow2Sec();
        Long convlong2 = dch.convertDateLong2Sec(datetext);
        //Long HoursMins = Long.valueOf(datetext);
        //Date TimeIn = new Date(convlong2);
        //dch.convertDateLong2Sec(datetext);
        //HourIN = TimeIn.getHours();
        //MinIN = TimeIn.getMinutes();
        long ParkerElapsed;
        ParkerElapsed = convnow - convlong2;
        HoursElapsed = dch.getHoursfromseconds(ParkerElapsed);
        //Removed bec of Overnight Problems
//        if (HoursElapsed >= 24)
//        {
//            HoursElapsed = HoursElapsed - 24;
//        }
        MinutesElapsed = dch.getRemainingMinutesfromseconds(ParkerElapsed);
         */
        Date crdEntDate = dch.convertArduinoUnixTime2Date(datetimeIN);
        Date crdExtDate = datePaid;
        timeStampIN = dch.convertJavaDate2UnixTime(crdEntDate);
        today = dch.convertJavaDate2UnixTime(crdExtDate);
        log.info("Parker Type = " + ParkerType);
        log.info("IN : " + timeStampIN);
        log.info("IN : " + timeStampIN);
        log.info("TODAY : " + today);
        log.info("TODAY : " + today);
        Long elapsed = today - timeStampIN + 72000;

        log.info("elapsed : " + elapsed);
        HoursElapsed = dch.getHoursfromseconds(elapsed);
        MinutesElapsed = dch.getRemainingMinutesfromseconds(elapsed);

        PrksMsg[4] = (String.valueOf(HoursElapsed) + "Hours  " + String.valueOf(MinutesElapsed) + " Mins");
        if (null != dateTimePaid) {
            Date crdPaidDate = dch.convertJavaUnixTime2Date(dateTimePaid);
            timeStampPaid = dch.convertJavaDate2UnixTime(crdPaidDate);
            log.info("PAID : " + timeStampPaid);
            Long elapsedPaid = today - timeStampPaid;
            HoursPaidElapsed = dch.getHoursfromseconds(elapsedPaid);
            MinutesPaidElapsed = dch.getRemainingMinutesfromseconds(elapsedPaid);
            PrksMsg[9] = (String.valueOf(HoursPaidElapsed) + "Hours  " + String.valueOf(MinutesPaidElapsed) + " Mins ");

        }
//PrksMsg[5] = ("Minutes: " + String.valueOf(MinutesElapsed));

//                Date DateElapsed = new Date(ParkerElapsed);
//
//                int minElapsed = DateElapsed.getMinutes();
//                int hourElapsed = DateElapsed.getHours();
//                SysMessage1.setText("Hours" + String.valueOf(hourElapsed));
//                SysMessage2.setText("Minutes" + String.valueOf(minElapsed));
//
//                DateFormat df = DateFormat.getDateInstance(DateFormat.MEDIUM);
//                DateFormat tf = DateFormat.getTimeInstance(DateFormat.MEDIUM);
//
//                DateFormat de = DateFormat.getDateInstance(DateFormat.MEDIUM);
//                DateFormat te = DateFormat.getTimeInstance(DateFormat.MEDIUM);
//                SysMessage3.setText(de.format(DateElapsed));
//                SysMessage4.setText(te.format(DateElapsed));
//                SysMessage8.setText(df.format(ParkerStamp));
//                SysMessage6.setText(tf.format(ParkerStamp));
    }

    private float NewComputeforAmount(Date NowStamp, Date ParkerStamp) {
        //Sunday = 0
        int dayOut = NowStamp.getDay();
        int dayIN = ParkerStamp.getDay();
        float AmountComputed = 0;
        float OvrnghtAmountComputed = 0;
        OvrnghtAmountComputed = checkOvernight(NowStamp, ParkerStamp);
//        if (checkPromo(NowStamp) == true && stn.trtype.compareToIgnoreCase("M") != 0)
//        {
//            //Because Promo is for Retail Only
//            if (ParkerType.compareToIgnoreCase("R") == 0) //RETAIL
//            {
//                float FlatRate = (float) 50.00;
//                AmountComputed = FlatRate + OvrnghtAmountComputed;
//                stn.trtype = "O"; //Promo
//                PrksMsg[8] = "Promo 2000H - 0400H";
//                if (HoursElapsed == 0) {  //Grace
//                    if (MinutesElapsed <= 15) {
//                        stn.trtype = "G";
//                        ParkerType = "G";
//                        MotorcycleOverriden = true;
//                        AmountComputed = (float) 0.00;
//                    }
//                }
//                return AmountComputed;
//            }
//        }

        if (ParkerType.compareToIgnoreCase("M") == 0) //MOTORCYCLES
        {
            float FlatRate = (float) 35.00 * getOvernightNum();
            if (stn.PrepaidOverride == true) {
                FlatRate = (float) 0.00;
                stn.trtype = "C";
                ParkerType = "C";
                MotorcycleOverriden = true;
            }
            //******ARANETA*******
            if (HoursElapsed == 0) //Grace Period
            {
                if (MinutesElapsed <= 15) {
                    AmountComputed = (float) 0.00;
                    if (stn.LostOverride == true) {
                        stn.trtype = "L";
                        ParkerType = "L";
                    } else if (stn.LCEPOverride == true) {
                        stn.trtype = "E";
                        ParkerType = "E";
                    } else if (stn.PrepaidOverride == true) {
                        stn.trtype = "C";
                        ParkerType = "C";
                    } else {
                        stn.trtype = "G";
                        ParkerType = "G";
                    }
                    MotorcycleOverriden = true;
                } else {
                    AmountComputed = FlatRate;
                }
            } else if (HoursElapsed <= 9) {
                AmountComputed = FlatRate;
            } else if (HoursElapsed >= 10) {
                if (MinutesElapsed == 0) {
                    AmountComputed = FlatRate;
                } else {
                    AmountComputed = FlatRate + extendedHoursAmount;
                }
            }
            //**************

            if (stn.PrepaidOverride == true) {
                FlatRate = (float) 0.00;
                stn.trtype = "C";
                ParkerType = "C";
            }
            return AmountComputed;
        } else if (ParkerType.compareToIgnoreCase("E") == 0) //LCEP
        {
            float LOSTAmount = (float) 200.0;
            float FlatRate = (float) 50.00;
            AmountComputed = LOSTAmount + FlatRate + OvrnghtAmountComputed;// + overnight
            return AmountComputed;
        } else if (ParkerType.compareToIgnoreCase("V") == 0) //VIP
        {
            AmountComputed = 0;
            return AmountComputed;
        } else if (ParkerType.compareToIgnoreCase("P") == 0) //MPP
        {
            AmountComputed = 0;
            return AmountComputed;
        } else if (ParkerType.compareToIgnoreCase("D") == 0) //DELIVERY
        {
            AmountComputed = DeliveryCheck(OvrnghtAmountComputed, dayIN, NowStamp);
            return AmountComputed;
        } else if (ParkerType.compareToIgnoreCase("L") == 0) //LOST
        {
            //float LostComputed = (float) 200.00 + OvrnghtAmountComputed;//+ Overnight
            float FlatRate = (float) 50.00;
            float LostComputed = (float) 200.00 + FlatRate;//+ Overnight
            /*NOTE: this wont work anymore because of the differences in
             * rates checking everyday
             * Instead of weekday and weekend computations only
            if (dayIN == 0 || dayIN == 6 || stn.HolidayOverride == true) //weekends
            {
                //AmountComputed = WeekEndRetail(OvrnghtAmountComputed);
                AmountComputed = FlatRateRetail(OvrnghtAmountComputed);
            } else {
                AmountComputed = NewOrdinaryRetail(OvrnghtAmountComputed, dayIN);

            }*/
            AmountComputed = NewRetailChecking_WE_WD(OvrnghtAmountComputed, dayIN, NowStamp);
            AmountComputed = AmountComputed + LostComputed;
            return AmountComputed;
        } else if (ParkerType.compareToIgnoreCase("R") == 0 || ParkerType.compareToIgnoreCase("Q") == 0
                || ParkerType.compareToIgnoreCase("BC") == 0 || ParkerType.compareToIgnoreCase("BM") == 0) //RETAIL
        {
            /*NOTE: this wont work anymore because of the differences in
             * rates checking everyday
             * Instead of weekday and weekend computations only
            if (dayIN == 0 || dayIN == 6 || stn.HolidayOverride == true) //weekends
            {
                //AmountComputed = WeekEndRetail(OvrnghtAmountComputed);
                AmountComputed = FlatRateRetail(OvrnghtAmountComputed);
            } else {
                AmountComputed = NewOrdinaryRetail(OvrnghtAmountComputed, dayIN);

            }*/

            AmountComputed = NewRetailChecking_WE_WD(OvrnghtAmountComputed, dayIN, NowStamp);

            //IF Joggers then Override Every Computation
            if (stn.ComputeJogger == true) {
                if (checkJoggers(NowStamp, ParkerStamp) == true) {
                    AmountComputed = 0 + OvrnghtAmountComputed;//Remove + AmountComputed for Overnight
                    PrksMsg[8] = "Joggers 0400H - 0830H ";
                    stn.trtype = "J";
                    ParkerType = "J";
                    JoggerOverride = true;
                }
            }
            return AmountComputed;
        }
        return AmountComputed;
    }

    private float FlatRateRetail(float OvrnghtAmountComputed) {
        float FlatRate = (float) 50.00;
        float AmountComputed = (float) 0.00;

        SaveCollData scd = new SaveCollData();
        if (stn.PrepaidOverride == true) {
            FlatRate = (float) 0.00;
            stn.trtype = "C";
            ParkerType = "C";
        }

        if (HoursElapsed == 0) //Grace Period
        {
            if (MinutesElapsed <= 15) {
                AmountComputed = (float) 0.00 + OvrnghtAmountComputed;
                if (stn.LostOverride == true) {
                    stn.trtype = "L";
                    ParkerType = "L";
                } else {
                    stn.trtype = "G";
                    ParkerType = "G";
                }
            } else {
                AmountComputed = FlatRate + OvrnghtAmountComputed;
            }
        } //Because Even Succeeding becomes flatrate
        else {
            AmountComputed = FlatRate + OvrnghtAmountComputed;
        }
        if (stn.PrepaidOverride == true) {
            if (HoursElapsed >= 4) {//this is because prepaid coupon is four hours free and not only for the first 3hours

                AmountComputed = AmountComputed - (float) 10.00;
                stn.trtype = "C";
                ParkerType = "C";
            }
        }
        //Retail Parkers Succ and Fix Rate segregation
        if (AmountComputed == (float) 50.00) {
            scd.UpdatePtypecount("R1");
        } else if (AmountComputed > (float) 50.00) {
            scd.UpdatePtypecount("R2");
        }
        return AmountComputed;
    }

    private long getHoursStart2Compute() {
        long HrsLeft2Compute = 0;
        HrsLeft2Compute = HoursElapsed;
        //Find out if Hours Elapsed is greater than Todays Cutoff Hour
        int HoursTil12 = 24 - HourIN;
        int HourstilCutoff = HoursTil12 + 2;//2:00AM cutoff
        //int DaysCutoff24Hours = HourstilCutoff;
        if (HrsLeft2Compute >= HourstilCutoff) //This means that it is MORE than one day and that you have to
        // ComputeforNextday
        //Therefore NumHrs2ComputeCurrent should be greater than HoursElapsed to break the loop
        //BREAK THIS:  while (NumHrs2ComputeCurrent < HoursElapsed)
        {
            //int DaysOvernight = (int) ((HoursElapsed - DaysCutoff24Hours) / 24);
            NumHrs2ComputeCurrent = HourstilCutoff;
            HrsLeft2Compute = HoursElapsed - HourstilCutoff;
        } else if (HrsLeft2Compute < HourstilCutoff) {
            NumHrs2ComputeCurrent = HoursElapsed;
            HrsLeft2Compute = 0;
        }
        return HrsLeft2Compute;
    }

    private long getHoursLeft2Compute(long Hours2Compute) //Hours2RECompute is now a number
    {
        long HrsLeft2Compute = 0;
        HrsLeft2Compute = Hours2Compute;
        //HrsLeft2Compute = NumHrs2ComputeCurrent - Hours2Compute;
        //Find out if Hours Elapsed is greater than Todays Cutoff Hour
        int HoursTil12 = 24 - 2; //for 2 AM to 2AM
        int HourstilCutoff = HoursTil12 + 2;//2:00AM cutoff
        //int DaysCutoff24Hours = HourstilCutoff;
        if (HrsLeft2Compute >= HourstilCutoff) //This means that it is MORE than one day and that you have to
        // ComputeforNextday
        //Therefore NumHrs2ComputeCurrent should be greater than HoursElapsed to break the loop
        //BREAK THIS:  while (NumHrs2ComputeCurrent < HoursElapsed)
        {
            //int DaysOvernight = (int) ((HoursElapsed - DaysCutoff24Hours) / 24);
            NumHrs2ComputeCurrent = HourstilCutoff;
            HrsLeft2Compute = Hours2Compute - HourstilCutoff;
        } else if (HrsLeft2Compute < HourstilCutoff) {
            NumHrs2ComputeCurrent = HrsLeft2Compute;
            HrsLeft2Compute = 0;
        }
        return HrsLeft2Compute;
    }

    private float ComputeMondays(Date NowStamp) {
        float a = ComputeWeekDay(NowStamp);
        return a;
    }

    private float ComputeTuesdays(Date NowStamp) {
        float a = ComputeWeekDay(NowStamp);
        return a;
    }

    private float ComputeWednesdays(Date NowStamp) {
        float a = ComputeWeekDay(NowStamp);
        return a;
    }

    private float ComputeThursdays(Date NowStamp) {
        float a = ComputeWeekDay(NowStamp);
        return a;
    }

    private float ComputeFridays(Date NowStamp) {
        float a = ComputeWeekDay(NowStamp);
        return a;
    }

    private float ComputeSaturdays(Date NowStamp) {
        float a = ComputeWeekDay(NowStamp);
        return a;
    }

    private float ComputeSundays(Date NowStamp) {
        float a = ComputeWeekDay(NowStamp);
        return a;
    }

    private float ComputeGrace() {
        float AmountComputed = 0;
        float FlatRate = (float) 50.00;
        if (stn.PrepaidOverride == true) {
            FlatRate = (float) 0.00;
            stn.trtype = "C";
            ParkerType = "C";
        }
        if (HoursElapsed == 0) //Grace Period
        {
            if (MinutesElapsed <= 15) {
                AmountComputed = (float) 0.00;
                if (stn.LostOverride == true) {
                    stn.trtype = "L";
                    ParkerType = "L";
                } else {
                    stn.trtype = "G";
                    ParkerType = "G";
                }
            } else {
                AmountComputed = FlatRate;
            }
        }
        return AmountComputed;
    }

    //*********UNUSED by Araneta so just use ComputeWeekDay
    private float ComputeWeekEND(Date NowStamp) //returns a float to be Grand Totalled later
    {
        float AmountComputed = 0;
        float FlatRate = (float) 50.00;
        if (stn.PrepaidOverride == true) {
            FlatRate = (float) 0.00;
            stn.trtype = "C";
            ParkerType = "C";
        }
        /*
        if (checkPromo(NowStamp) == true && stn.trtype.compareToIgnoreCase("M") != 0) {
            //Because Promo is for Retail Only
            if (ParkerType.compareToIgnoreCase("R") == 0) //RETAIL
            {
                //FlatRate = (float) 50.00;
                AmountComputed = FlatRate + AmountComputed;
                stn.trtype = "O"; //Promo
                PrksMsg[8] = "Promo 2000H - 0400H";
                if (HoursElapsed == 0) {  //Grace
                    if (MinutesElapsed <= 15) {
                        stn.trtype = "G";
                        ParkerType = "G";
                        MotorcycleOverriden = true;
                        AmountComputed = (float) 0.00;
                    }
                }
                return AmountComputed;
            }
        }
         */
        //NON Promo Parkers
        if (HoursElapsed == 0) //Grace Period
        {
            if (MinutesElapsed <= 15) {
                AmountComputed = (float) 0.00;
                if (stn.LostOverride == true) {
                    stn.trtype = "L";
                    ParkerType = "L";
                } else {
                    stn.trtype = "G";
                    ParkerType = "G";
                }
            } else {
                AmountComputed = FlatRate;
            }
        } else {
            AmountComputed = FlatRate;
        }

        /**
         * ******** SUCCEEDING RATES ONLY else if (NumHrs2ComputeCurrent <= 2) //First 3 hours
         * {
         * AmountComputed = FlatRate;
         * } else if (NumHrs2ComputeCurrent == 3) //First 3 hours + succeeding
         * {
         * AmountComputed = FlatRate;
         * if (MinutesElapsed > 0) { AmountComputed = (float) 10.00 +
         * AmountComputed; } } else if (NumHrs2ComputeCurrent == 4) //Past 3
         * hours { AmountComputed = FlatRate + (float) 10.00; if (MinutesElapsed
         * > 0) { AmountComputed = (float) 10.00 + AmountComputed; } } else if
         * (NumHrs2ComputeCurrent == 5) { AmountComputed = FlatRate + (float)
         * 20.00; if (MinutesElapsed > 0) { AmountComputed = (float) 20.00 +
         * AmountComputed; } } else { AmountComputed = FlatRate + (float) 20.00;
         * float OverHours = (float) ((NumHrs2ComputeCurrent - 5) * 20);
         * AmountComputed = OverHours + AmountComputed; if (MinutesElapsed > 0)
         * { AmountComputed = (float) 20.00 + AmountComputed; } }
         */
        if (stn.PrepaidOverride == true) {
            if (NumHrs2ComputeCurrent >= 4) {//this is because prepaid coupon is four hours free and not only for the first 3hours

                AmountComputed = AmountComputed - (float) 10.00;
                stn.trtype = "C";
                ParkerType = "C";
            }
        }
        return AmountComputed;
    }

    private float ComputeWeekDay(Date NowStamp) //returns a float to be Grand Totalled later
    {
        float AmountComputed = 0;
        float FlatRate = (float) 50.00;
        if (stn.PrepaidOverride == true) {
            FlatRate = (float) 0.00;
            stn.trtype = "C";
            ParkerType = "C";
        }
        /*
        if (checkPromo(NowStamp) == true && stn.trtype.compareToIgnoreCase("M") != 0) {
            //Because Promo is for Retail Only
            if (ParkerType.compareToIgnoreCase("R") == 0) //RETAIL
            {
                //FlatRate = (float) 50.00;
                AmountComputed = FlatRate + AmountComputed;
                stn.trtype = "O"; //Promo
                PrksMsg[8] = "Promo 2000H - 0400H";
                if (HoursElapsed == 0) {  //Grace
                    if (MinutesElapsed <= 15) {
                        stn.trtype = "G";
                        ParkerType = "G";
                        MotorcycleOverriden = true;
                        AmountComputed = (float) 0.00;
                    }
                }
                return AmountComputed;
            }
        }
         */

        //NON PROMO
        if (HoursElapsed == 0) //Grace Period
        {
            if (MinutesElapsed <= 15) {
                AmountComputed = (float) 0.00;
                if (stn.LostOverride == true) {
                    stn.trtype = "L";
                    ParkerType = "L";
                } else {
                    stn.trtype = "G";
                    ParkerType = "G";
                }
            } else {
                AmountComputed = FlatRate;
            }
        } else if (HoursElapsed <= 9) {
            if (ParkerType.compareToIgnoreCase("Q") == 0) {
                FlatRate = 0;
            } else if (ParkerType.compareToIgnoreCase("BC") == 0) {
                AmountComputed = (float) 50.00;
            } else if (ParkerType.compareToIgnoreCase("BM") == 0) {
                AmountComputed = (float) 35.00;
            } else {
                AmountComputed = FlatRate;
            }
        } else if (HoursElapsed >= 10) {
            if (ParkerType.compareToIgnoreCase("Q") == 0) {
                AmountComputed = 0 + (checkNumOfExtendedTimes() * extendedHoursAmount);
            } else if (ParkerType.compareToIgnoreCase("BC") == 0) {
                AmountComputed = FlatRate + ((checkNumOfExtendedTimes() - 1) * extendedHoursAmount);
            } else if (ParkerType.compareToIgnoreCase("BM") == 0) {
                AmountComputed = (float) 35.00 + ((checkNumOfExtendedTimes() - 1) * extendedHoursAmount);
            } else {
                AmountComputed = FlatRate + (checkNumOfExtendedTimes() * extendedHoursAmount);
            }

            //if (MinutesElapsed == 0) {
            //    AmountComputed = FlatRate;
            //} else 
        }

        /**
         * ******** SUCCEEDING RATES ONLY else if (NumHrs2ComputeCurrent <= 2) //First 3 hours
         * {
         * AmountComputed = FlatRate;
         * } else if (NumHrs2ComputeCurrent == 3) //First 3 hours + succeeding
         * {
         * AmountComputed = FlatRate;
         * if (MinutesElapsed > 0) { AmountComputed = (float) 10.00 +
         * AmountComputed; } } else if (NumHrs2ComputeCurrent == 4) //Past 3
         * hours { AmountComputed = FlatRate + (float) 10.00; if (MinutesElapsed
         * > 0) { AmountComputed = (float) 10.00 + AmountComputed; } } else if
         * (NumHrs2ComputeCurrent == 5) { AmountComputed = FlatRate + (float)
         * 20.00; if (MinutesElapsed > 0) { AmountComputed = (float) 20.00 +
         * AmountComputed; } } else { AmountComputed = FlatRate + (float) 20.00;
         * float OverHours = (float) ((NumHrs2ComputeCurrent - 5) * 20);
         * AmountComputed = OverHours + AmountComputed; if (MinutesElapsed > 0)
         * { AmountComputed = (float) 20.00 + AmountComputed; } }
         */
        if (stn.PrepaidOverride == true) {
            if (NumHrs2ComputeCurrent >= 4) {//this is because prepaid coupon is four hours free and not only for the first 3hours

                AmountComputed = AmountComputed - (float) 10.00;
                stn.trtype = "C";
                ParkerType = "C";
            }
        }
        return AmountComputed;
    }

    private float checkNumOfExtendedTimes() {
        float numOfExtendedTimes = 0;
        numOfExtendedTimes = HoursElapsed / 10;
//        if (HoursElapsed == 10) {
//            numOfExtendedTimes = 1;
//        }
        return numOfExtendedTimes;
    }

    private float DeliveryCheck(float OvrnghtAmountComputed, int dayIN, Date NowStamp) {
        float AmountComputed = (float) 0.00;
        float FlatRate = (float) 0.00;
        //Add the Overnight Computation to the GRANDTOTAL Computation
        AmountComputed = OvrnghtAmountComputed;

        //NON PROMO
        if (HoursElapsed == 0) //Grace Period
        {
            if (MinutesElapsed <= 15) {
                AmountComputed = (float) 0.00;
                if (stn.LostOverride == true) {
                    stn.trtype = "L";
                    ParkerType = "L";
                } else {
                    stn.trtype = "G";
                    ParkerType = "G";
                }
            } else {
                AmountComputed = 0;
            }
        } else if (HoursElapsed == 1) {
            AmountComputed = FlatRate + (float) 100.00;
            if (MinutesElapsed == 0) {
                AmountComputed = 0;
            } else if (MinutesElapsed > 0) {
                float OverHours = (float) ((HoursElapsed) * 100);
                AmountComputed = OverHours + AmountComputed;
            }
        } else if (HoursElapsed >= 2) {
            float OverHours = (float) ((HoursElapsed - 1) * 100);
            AmountComputed = OverHours + AmountComputed;
            if (MinutesElapsed == 0) {
                AmountComputed = AmountComputed + 0;
            } else if (MinutesElapsed > 0) {
                float OverMins = (float) 100.00;
                AmountComputed = OverMins + AmountComputed;
            }
        }

        return AmountComputed;
    }

    private float NewRetailChecking_WE_WD(float OvrnghtAmountComputed, int dayIN, Date NowStamp) {
        //float FlatRate = (float) 50.00;
        float AmountComputed = (float) 0.00;
        boolean loopctr_end = false;
        //place loop here:
        NumHrs2ComputeCurrent = 0;
        long HrsLeft2Compute = 0;
        //Add the Overnight Computation to the GRANDTOTAL Computation
        AmountComputed = OvrnghtAmountComputed;
//        if(HoursElapsed == 0)//Check Grace Period...
//        {
//            AmountComputed = ComputeGrace();
//        }
        HrsLeft2Compute = getHoursStart2Compute();
        //do //runs once then conditionally loops if 24hrs...
        //{
        if (dayIN == 1) {
            AmountComputed = AmountComputed + ComputeMondays(NowStamp);
        } else if (dayIN == 2) {
            AmountComputed = AmountComputed + ComputeTuesdays(NowStamp);
        } else if (dayIN == 3) {
            AmountComputed = AmountComputed + ComputeWednesdays(NowStamp);
        } else if (dayIN == 4) {
            AmountComputed = AmountComputed + ComputeThursdays(NowStamp);
        } else if (dayIN == 5) {
            AmountComputed = AmountComputed + ComputeFridays(NowStamp);
        } else if (dayIN == 6) {
            AmountComputed = AmountComputed + ComputeSaturdays(NowStamp);
        } else if (dayIN == 0) {
            AmountComputed = AmountComputed + ComputeSundays(NowStamp);
        }

        dayIN++;
        if (dayIN == 7) {
            dayIN = 0;
        }

        if (HrsLeft2Compute == 0) {//From the start if it is Zero
            //it means that it is only one day
            loopctr_end = true;
        } else {
            HrsLeft2Compute = getHoursLeft2Compute(HrsLeft2Compute);
        }
        //} //while loop by 24 because if less than
        //while (loopctr_end == false);

        SaveCollData scd = new SaveCollData();
        //Retail Parkers Succ and Fix Rate segregation
        if (AmountComputed == (float) 50.00) {
            scd.UpdatePtypecount("R1");
        } else if (AmountComputed > (float) 50.00) {
            scd.UpdatePtypecount("R2");
        }
        return AmountComputed;
    }

    /*    private float OldOrdinaryRetail(float OvrnghtAmountComputed) {
        float FlatRate = (float) 50.00;
        float AmountComputed = (float) 0.00;

        SaveCollData scd = new SaveCollData();
        if (stn.PrepaidOverride == true) {
            FlatRate = (float) 0.00;
            stn.trtype = "C";
            ParkerType = "C";
        }

        if (HoursElapsed == 0) //Grace Period
        {
            if (MinutesElapsed <= 15) {
                AmountComputed = (float) 0.00 + OvrnghtAmountComputed;
                if (stn.LostOverride == true) {
                    stn.trtype = "L";
                    ParkerType = "L";
                } else {
                    stn.trtype = "G";
                    ParkerType = "G";
                }
            } else {
                AmountComputed = FlatRate + OvrnghtAmountComputed;
            }
        } else if (HoursElapsed <= 2) //First 3 hours
        {
            AmountComputed = FlatRate + OvrnghtAmountComputed;
        } else if (HoursElapsed == 3) //First 3 hours + succeeding
        {
            AmountComputed = FlatRate + OvrnghtAmountComputed;
            if (MinutesElapsed > 0) {
                AmountComputed = (float) 10.00 + AmountComputed;
            }
        } else if (HoursElapsed == 4) //Past 3 hours
        {
            AmountComputed = FlatRate + (float) 10.00 + OvrnghtAmountComputed;
            if (MinutesElapsed > 0) {
                AmountComputed = (float) 10.00 + AmountComputed;
            }
        } else if (HoursElapsed == 5) {
            AmountComputed = FlatRate + (float) 20.00 + OvrnghtAmountComputed;
            if (MinutesElapsed > 0) {
                AmountComputed = (float) 20.00 + AmountComputed;
            }
        } else {
            AmountComputed = FlatRate + (float) 20.00 + OvrnghtAmountComputed;
            float OverHours = (float) ((HoursElapsed - 5) * 20);
            AmountComputed = OverHours + AmountComputed;
            if (MinutesElapsed > 0) {
                AmountComputed = (float) 20.00 + AmountComputed;
            }
        }
        if (stn.PrepaidOverride == true) {
            if (HoursElapsed >= 4) {//this is because prepaid coupon is four hours free and not only for the first 3hours

                AmountComputed = AmountComputed - (float) 10.00;
                stn.trtype = "C";
                ParkerType = "C";
            }
        }
        //Retail Parkers Succ and Fix Rate segregation
        if (AmountComputed == (float) 50.00) {
            scd.UpdatePtypecount("R1");
        } else if (AmountComputed > (float) 50.00) {
            scd.UpdatePtypecount("R2");
        }
        return AmountComputed;
    }
     */
    private float WeekEndRetail(float OvrnghtAmountComputed) {
        float FlatRate = (float) 50.00;
        float AmountComputed = 0;
        SaveCollData scd = new SaveCollData();
        if (stn.PrepaidOverride == true) {
            FlatRate = (float) 0.00;
            stn.trtype = "C";
            ParkerType = "C";
        }
        if (HoursElapsed == 0) {
            if (MinutesElapsed <= 15) {
                AmountComputed = (float) 0.00 + OvrnghtAmountComputed;
                stn.trtype = "G";
                ParkerType = "G";
            } else {
                AmountComputed = FlatRate + OvrnghtAmountComputed;
            }
        } else if (HoursElapsed <= 3) {
            AmountComputed = FlatRate + OvrnghtAmountComputed;
        } else if (HoursElapsed == 4) //First 4 hours     //P30
        {
            AmountComputed = FlatRate + OvrnghtAmountComputed;
            if (MinutesElapsed > 0) {
                AmountComputed = (float) 10.00 + AmountComputed;       //P40 plus fraction of hour

            }
        } else if (HoursElapsed == 5) //P40
        {
            AmountComputed = FlatRate + (float) 10.00 + OvrnghtAmountComputed;
            if (MinutesElapsed > 0) {
                AmountComputed = (float) 10.00 + AmountComputed;       //P50 plus fraction of hour

            }
        } else if (HoursElapsed == 6) //P50
        {
            AmountComputed = FlatRate + (float) 20.00 + OvrnghtAmountComputed;
            if (MinutesElapsed > 0) {
                AmountComputed = (float) 20.00 + AmountComputed;       //P70 plus fraction of hour

            }
        } else {
            AmountComputed = FlatRate + (float) 20.00 + OvrnghtAmountComputed;
            float OverHours = (float) ((HoursElapsed - 6) * 20);
            AmountComputed = OverHours + AmountComputed;
            if (MinutesElapsed > 0) {
                AmountComputed = (float) 20.00 + AmountComputed;
            }
        }
        //Retail Parkers Succ and Fix Rate segregation
        if (AmountComputed == (float) 50.00) {
            scd.UpdatePtypecount("R1");
        } else if (AmountComputed > (float) 50.00) {
            scd.UpdatePtypecount("R2");
        }
//        if (stn.PrepaidOverride == true)
//        {
//            if (HoursElapsed>=4)
//            {//this is because prepaid coupon is four hours free and not only for the first 3hours
//            AmountComputed = AmountComputed - 10;
//            stn.trtype = "C";
//            }
//        }

        return AmountComputed;
    }

    private boolean checkPromo(Date NowStamp) //New CheckPromo looks at the remaining hours Elapsed rather than the whole...
    {
        int HourOut = NowStamp.getHours();
        int MinOut = NowStamp.getMinutes();
        if (HourIN >= 20) {
            if (HourOut >= 20) {
                if (NumHrs2ComputeCurrent == 0) {
                    if (MinutesElapsed <= 15) {
                        return false;
                    }
                }
                return true;
            } else if (HourOut == 4 && MinOut == 0) {
                if (NumHrs2ComputeCurrent == 0) {
                    if (MinutesElapsed <= 15) {
                        return false;
                    }
                }
                return true;
            } else if (HourOut >= 0 && HourOut < 4) {
                if (NumHrs2ComputeCurrent == 0) {
                    if (MinutesElapsed <= 15) {
                        return false;
                    }
                }
                return true;
            }
        } else if (HourIN >= 0 && HourIN < 4) {
            if (HourOut >= 20) {
                if (NumHrs2ComputeCurrent == 0) {
                    if (MinutesElapsed <= 15) {
                        return false;
                    }
                }
                return true;
            } else if (HourOut == 4 && MinOut == 0) {
                if (NumHrs2ComputeCurrent == 0) {
                    if (MinutesElapsed <= 15) {
                        return false;
                    }
                }
                return true;
            } else if (HourOut >= 0 && HourOut < 4) {
                if (NumHrs2ComputeCurrent == 0) {
                    if (MinutesElapsed <= 15) {
                        return false;
                    }
                }
                return true;
            }
        }
        return false;
    }

    private boolean checkJoggers(Date NowStamp, Date ParkerStamp) {
        int HourOut = NowStamp.getHours();
        int MinOut = NowStamp.getMinutes();
        if (isNextDay(NowStamp, ParkerStamp) == true) {
            return false;
        }
        if (HourIN >= 5) {
            if (HourIN < 8) {
                if (HourOut < 9) {
                    return true;
                }
                //else if (HourOut == 9 && MinOut <= 30)
                //{
                //   return true;
                //}
            } else if (HourIN == 8 && MinOut == 0) {
                if (HourOut < 9) {
                    return true;
                }
                //else if (HourOut == 9 && MinOut <= 30)
                //{
                //   return true;
                //}
            }

        }
        return false;
    }

    private int getOvernightNum() {
        int numofOvernights = 0;

        int Hourstil12 = 24 - HourIN;
        int HourstilCutoff = Hourstil12 + 2;//2:00AM cutoff
        int DaysCutoff24Hours = HourstilCutoff;

        if (HoursElapsed >= HourstilCutoff) {
            numofOvernights = (int) ((HoursElapsed - DaysCutoff24Hours) / 24);
        }
        numofOvernights++;//bec of the default of one day inside
        return numofOvernights;
    }

    /**
     * First find if the parker left after Midnight if true then find out how
     * many 24Hours has she parked if still within 24hours {add 1 for the
     * computation} but check if she went in before 12midnight and left after
     * 2:00am if before "IN: less than 12midnight" AND OUT: greater than or
     * equal to 2:00
     *
     * * @return Amount of overnight
     */
    private float checkOvernight(Date NowStamp, Date ParkerStamp) {
        float OvernightAmount = 0;
        int HourOut = NowStamp.getHours();
        int MinOut = NowStamp.getMinutes();

        int numofOvernights = 0;

        int HoursTil12 = 24 - HourIN;
        int HourstilCutoff = HoursTil12 + 2;//2:00AM cutoff
        int DaysCutoff24Hours = HourstilCutoff;

        if (isNextDay(NowStamp, ParkerStamp) == true) {
            if (ParkerType.compareToIgnoreCase("BC") == 0 || ParkerType.compareToIgnoreCase("BM") == 0) {
                numofOvernights = 0;
            } else {
                numofOvernights = 1;
                if (NowStamp.getHours() >= 2) {
                    OvernightAmount = (float) 200.00;
                }
            }
//            if (NowStamp.getHours() >= 2) {
//                OvernightAmount = (float) 200.00;
//            }
//            OvernightAmount = (float) 200.00;
//            OvernightAmount = OvernightAmount * numofOvernights;
//
//            if (HourIN > 6)//Still for debate
//            {
//                if (HourIN <= 23) {
//                    if (MinIN <= 59) {
//                        if (HourOut >= 6) {
//                            if (MinOut >= 0) {
//                                OvernightAmount = OvernightAmount + (float) 200.00;//this activates the Overnight charges, without it it returns an additional of zero
//                                numofOvernights++;
//    //                            if (numofOvernights == 0) {
//    //                                numofOvernights++;
//    //                            }
//                            }
//                        }
//                    }
//                }
        }
        if (OvernightAmount != 0) {
            OvernightOverride = true;
            PrksMsg[8] = "::OVERNIGHT  " + numofOvernights + "::";
        }
        //}
        return OvernightAmount;
    }

    private float OldcheckOvernight(Date NowStamp, Date ParkerStamp) {
        float OvernightAmount = 0;
        int HourOut = NowStamp.getHours();
        int MinOut = NowStamp.getMinutes();

        int numofOvernights = 0;

        int HoursTil12 = 24 - HourIN;
        int HourstilCutoff = HoursTil12 + 2;//2:00AM cutoff
        int DaysCutoff24Hours = HourstilCutoff;

        if (HoursElapsed >= HourstilCutoff) {
            int DaysOvernight = (int) ((HoursElapsed - DaysCutoff24Hours) / 24);
            numofOvernights = 1 + DaysOvernight;
            OvernightAmount = (float) 200.00;
            OvernightAmount = OvernightAmount * numofOvernights;
        }
        //PrksMsg[6] = df.format(ParkerStamp);
        //PrksMsg[7] = tf.format(ParkerStamp);

//        if (isNextDay(NowStamp, ParkerStamp) == true) {
//            int numofOvernights = 0;
//            getnumofOvernights();
//            //log.info(getnumofOvernights());
//            OvernightAmount = (float) 200.00;
//            OvernightAmount = OvernightAmount * numofOvernights;
//
//            if (HourIN > 6)//Still for debate
//            {
//                if (HourIN <= 23) {
//                    if (MinIN <= 59) {
//                        if (HourOut >= 6) {
//                            if (MinOut >= 0) {
//                                OvernightAmount = OvernightAmount + (float) 200.00;//this activates the Overnight charges, without it it returns an additional of zero
//                                numofOvernights++;
//    //                            if (numofOvernights == 0) {
//    //                                numofOvernights++;
//    //                            }
//                            }
//                        }
//                    }
//                }
//            }
        if (OvernightAmount != 0) {
            OvernightOverride = true;
            PrksMsg[9] = "::OVERNIGHT  " + numofOvernights + "::";
        }
        //}
        return OvernightAmount;
    }

    private boolean isNextDay(Date NowStamp, Date ParkerStamp) {
        int NowYear = NowStamp.getYear();
        int NowMonth = NowStamp.getMonth();
        int NowDate = NowStamp.getDate();
        int PStampYear = ParkerStamp.getYear();
        int PStampMonth = ParkerStamp.getMonth();
        int PStampDate = ParkerStamp.getDate();

        if (NowYear > PStampYear) {
            return true;
        }
        if (NowMonth > PStampMonth) {
            return true;
        }
        if (NowDate > PStampDate) {
            return true;
        }

        return false;
    }

    private String updateReceiptFiles(double AmountRCPT, double GrossAmount) {
        try {
            SaveCollData scd = new SaveCollData();
            scd.UpdateReceiptNos();
            //scd.UpdateReceiptAmount(AmountRCPT);                  //
            scd.UpdateGRANDTOTAL(AmountRCPT);
            scd.UpdateGRANDGROSSTOTAL(GrossAmount);
            return scd.getCurrentReceiptNos();
        } catch (Exception ex) {
            log.error(ex.getMessage());
            return "";
        }
    }

    private void updateNetSlots() {
        SaveCollData scd = new SaveCollData();
        if (stn.trtype.equalsIgnoreCase("M") == false) {
            if (MotorcycleOverriden == false) {
                try {
                    scd.UpdateSlotsNos(stn.EX_SentinelID, stn.serverIP);
                } catch (Exception ex) {
                    log.error(ex.getMessage());
                }
            }
        }
    }

    private boolean updateTrans2DB() {
        try {
            SaveCollData scd = new SaveCollData();
            scd.UpdatePtypecountDB(stn.trtype, stn.loginID);               //ParkerType Listing
            scd.UpdatePtypeAmountDB(stn.trtype, stn.loginID, AmountDue);

            scd.UpdateImptCountDB("carServed", stn.loginID);               //ParkerType Listing
            if (hasExtended) {
                scd.UpdateImptCountDB("extendedCount", stn.loginID);               //ParkerType Listing
                scd.UpdateImptAmountDB("extendedAmount", stn.loginID, ExtendedPrice);
            }
            hasExtended = false;
            if (hasOvernight) {
                scd.UpdateImptCountDB("overnightCount", stn.loginID);               //ParkerType Listing
                scd.UpdateImptAmountDB("overnightAmount", stn.loginID, OvernightPrice);
            }

            hasOvernight = false;
            scd.UpdateImptAmountDB("totalAmount", stn.loginID, AmountDue);

            return true;
        } catch (Exception ex) {
            log.error(ex.getMessage());
            return false;
        }
    }

    private boolean updateOneTransFiles(String fieldName, double Amount) {//Local files
        try {
            SaveCollData scd = new SaveCollData();
            LogUtility logthis = new LogUtility();
            scd.UpdateImptCountDB(fieldName + "Count", stn.loginID);               //ParkerType Listing
            scd.UpdateImptAmountDB(fieldName + "Amount", stn.loginID, Amount);
            return true;
        } catch (Exception ex) {
            log.error(ex.getMessage());
            return false;
        }

    }

    private boolean updateAllTransFiles() {//Local files
        try {
            SaveCollData scd = new SaveCollData();
            LogUtility logthis = new LogUtility();
            scd.UpdatePtypecount(stn.trtype);               //ParkerType Listing
            scd.UpdatePtypeAmount(stn.trtype, String.valueOf(AmountDue));
            scd.UpdateCarServed();                          //All important Car Served
            //Show CarServed
            String tempserved = scd.getExitCarServed();
            logthis.setsysLog(stn.EX_SentinelID, "car served:= " + tempserved);
            stn.ServedExit.setText(tempserved);
            return true;
        } catch (Exception ex) {
            log.error(ex.getMessage());
            return false;
        }

    }

    public float Computation(String ParkerType, boolean firstscan, boolean isLost) {
        float AmountComputed = 0;
        int numOfDays = 1;
        DataBaseHandler dbh = new DataBaseHandler();
        try {
            dbh.getActiveRatesParameter();
        } catch (Exception ex) {
            ex.printStackTrace();
            log.error(ex.getMessage());
        }
        int GracePeriod = dbh.getGracePeriod(ParkerType);
        int EveryNthHour = dbh.getEveryNthHour(ParkerType);
        String NthHourRate = dbh.getNthHourRate(ParkerType);
        String LostPrice = dbh.getLostPrice(ParkerType);
        Boolean FractionThereOf = dbh.getFractionThereOf(ParkerType);
        int OTCutoff = dbh.getOTCutoff(ParkerType);
        String OTPrice = dbh.getOTPrice(ParkerType);
        Boolean OTWaived1st = dbh.getOTWaived1st(ParkerType);
        int TreatNextDayAsNewDay = dbh.getTreatNextDayAsNewDay(ParkerType);
        String SucceedingRate = dbh.getSucceedingRate(ParkerType);
        String HR[] = {" +1 ", "+1", "+1", "+1", "+1", "+1", "+1", "+1", "+1", "+1", "+1", "+1", "+1", "+1", "+1", "+1", "+1", "+1", "+1", "+1", "+1", "+1", "+1", "+1", "+1", "+1", "+1", "+1", "+1", "+1"};
        String HRplus[] = {" +100 ", "+200", "+300", "+400", "+500", "+600", "+700", "+800", "+900", "+1000", "+1100", "+1200", "+1300", "+1400", "+1500", "+1600", "+1700", "+1800", "+1900", "+2000", "+2100", "+2200", "+2300", "+2400", "+2500", "+2600", "+2700", "+2800", "+2900", "+3000"};
        int newArraySize = (int) (HoursElapsed + 1);

        if (HoursElapsed > 24) {
            // For getting the nextDueStamp, add Extra 24Hrs to the array to find it
            newArraySize = newArraySize + (int) ((HoursElapsed / 24) * 24) - 1;
        }
        Boolean HRWaived1st[] = new Boolean[newArraySize];
        Boolean HRplusWaived1st[] = new Boolean[newArraySize];
        HR = dbh.getHourParams(ParkerType, newArraySize);
        HRplus = dbh.getHourPlusParams(ParkerType, newArraySize);
        HRWaived1st = dbh.getHourWaived1st(ParkerType, newArraySize);
        HRplusWaived1st = dbh.getHourPlusWaived1st(ParkerType, newArraySize);

        if (HoursPaidElapsed == 0 && MinutesPaidElapsed <= GracePeriod && firstscan == false) {
            //This might generate a negative value
            return 0;
        }
        /**
         * 0 = Interval of nth Hour or "Extended" 1 = Continuous Succeeding Rate
         * 2 = New Day
         */
        if (TreatNextDayAsNewDay == 0) {
            // must remove certain hours... and get the first hour that the rates have an amount
            //Determine the first hour

            //int firstNonZeroHR = getFirstHourwidNonZeroRate(HR, HRplus, HRWaived1st, HRplusWaived1st);
            int lastNonZeroHR = getLastHourwidNonZeroRate(HR, HRplus, HRWaived1st, HRplusWaived1st);
            if (HoursElapsed > 24) {
                int offset = 0;
                offset = 25 - 24 - 1 + EveryNthHour - lastNonZeroHR;
                // 25 because the next hour after midnight
                //-24 because we get the new rates from the beginning again
                //-1 Because of the next Hour that is shud start from
                //lastNonZeroHR to get the offset value
                int Interval = 0;
                //First Interval
                Interval = 25 + EveryNthHour - offset + 1;
                for (int x = 25; x < newArraySize; x++) {
                    //25 Because 12midnight starts at 0
                    //+1 Because of Index issues
                    if (x == Interval) {
                        HR[x] = NthHourRate;
                        HRplus[x] = "+0";
                        HRWaived1st[x] = false;
                        HRplusWaived1st[x] = false;
                        Interval = EveryNthHour + Interval;
                    } else {
                        HR[x] = "+0";
                        HRplus[x] = "+0";
                        HRWaived1st[x] = false;
                        HRplusWaived1st[x] = false;
                    }

                }

            }

            //Determine how many Extended hours
            int Interval = EveryNthHour;
            for (int x = 0; x <= HoursElapsed; x++) {
                if (x == Interval) {
                    if (HRWaived1st[x] == false) {
                        flagExtended++;
                    }
                    Interval = Interval + EveryNthHour;
                }
                //flagExtended = (int) (HoursElapsed / EveryNthHour);
            }
            //Not Needed in EVER Commonwealth
            //PrksMsg[7] = "::EXTENDED  " + flagExtended + "::";
            //hasExtended = true;
            ExtendedPrice = Float.parseFloat(NthHourRate.trim().substring(1));
            ExtendedPrice = ExtendedPrice * flagExtended;
        } else if (TreatNextDayAsNewDay == 1) {
            if (HoursElapsed == 24) {
                for (int x = 25; x <= newArraySize; x++) {
                    HR[x] = HR[x - 25];
                    HRplus[x] = HRplus[x - 25];
                    HRWaived1st[x] = HRWaived1st[x - 25];
                    HRplusWaived1st[x] = HRplusWaived1st[x - 25];
                }
            } else if (HoursElapsed >= 25) {
                for (int x = 25; x < newArraySize; x++) {
                    HR[x] = SucceedingRate;
                    HRplus[x] = "+0";
                    HRWaived1st[x] = false;
                    HRplusWaived1st[x] = false;
                }
            }
        } else if (TreatNextDayAsNewDay == 2) {
            if (HoursElapsed == 24) {
                for (int x = 25; x <= newArraySize; x++) {
                    HR[x] = HR[x - 25];
                    HRplus[x] = HRplus[x - 25];
                    HRWaived1st[x] = HRWaived1st[x - 25];
                    HRplusWaived1st[x] = HRplusWaived1st[x - 25];
                }
            } else if (HoursElapsed >= 25) {
                for (int x = 24; x <= newArraySize - 1; x++) {
                    if (x % 24 == 0) {
                        HR[x] = HR[0];
                        HRplus[x] = HRplus[0];
                        HRWaived1st[x] = HRWaived1st[0];
                        HRplusWaived1st[x] = HRplusWaived1st[0];
//                        System.out.println("HR["+x+"] : " + HR[x]);                       
                    } else {
                        HR[x] = HR[x - 24];
                        HRplus[x] = HRplus[x - 24];
                        HRWaived1st[x] = HRWaived1st[x - 24];
                        HRplusWaived1st[x] = HRplusWaived1st[x - 24];
//                        System.out.println("HR["+x+"] : " + HR[x]);
                    }
                }
            }
        }
        //Add the Overnight Amount to the correct Array
        //TODO:: First Determine which Array the Overnight Cutoff is
        //24 - HourIN + Cutoff hour = number of Array
        int OTHour = 0;
        if (HourIN < OTCutoff) {
            OTHour = OTCutoff;
        } else if (HourIN >= OTCutoff) {
            OTHour = 24 - HourIN + OTCutoff;
        }
        float otPrice = 0;
        if (OTWaived1st == false) {
            if (OTPrice.trim().substring(0, 1).compareToIgnoreCase("+") == 0) {
                otPrice = Float.parseFloat(OTPrice.trim().substring(1));
                OvernightPrice = otPrice;
                if (HR[OTHour].trim().substring(0, 1).compareToIgnoreCase("+") == 0) {
                    float newOT = Float.parseFloat(HR[OTHour].trim().substring(1)) + otPrice;
                    HR[OTHour] = "+" + newOT;
                } else if (HR[OTHour].trim().substring(0, 1).compareToIgnoreCase("=") == 0) {
                    float newOT = Float.parseFloat(HR[OTHour].trim().substring(1)) + otPrice;
                    HR[OTHour] = "=" + newOT;
                }

            } else if (OTPrice.trim().substring(0, 1).compareToIgnoreCase("=") == 0) {
                otPrice = Float.parseFloat(OTPrice.trim().substring(1));
                OvernightPrice = otPrice;
                if (HR[OTHour].trim().substring(0, 1).compareToIgnoreCase("+") == 0) {
                    float newOT = otPrice;
                    HR[OTHour] = "=" + newOT;
                } else if (HR[OTHour].trim().substring(0, 1).compareToIgnoreCase("=") == 0) {
                    float newOT = otPrice;
                    HR[OTHour] = "=" + newOT;
                }

            }
        }

        OTHour += 24;

        while (OTHour <= HoursElapsed) {

            if (OTPrice.trim().substring(0, 1).compareToIgnoreCase("+") == 0) {
                otPrice = Float.parseFloat(OTPrice.trim().substring(1));
                OvernightPrice = otPrice;
                if (HR[OTHour].trim().substring(0, 1).compareToIgnoreCase("+") == 0) {
                    float newOT = Float.parseFloat(HR[OTHour].trim().substring(1)) + otPrice;
                    HR[OTHour] = "+" + newOT;
                } else if (HR[OTHour].trim().substring(0, 1).compareToIgnoreCase("=") == 0) {
                    float newOT = Float.parseFloat(HR[OTHour].trim().substring(1)) + otPrice;
                    HR[OTHour] = "=" + newOT;
                }

            } else if (OTPrice.trim().substring(0, 1).compareToIgnoreCase("=") == 0) {
                otPrice = Float.parseFloat(OTPrice.trim().substring(1));
                OvernightPrice = otPrice;
                if (HR[OTHour].trim().substring(0, 1).compareToIgnoreCase("+") == 0) {
                    float newOT = otPrice;
                    HR[OTHour] = "=" + newOT;
                } else if (HR[OTHour].trim().substring(0, 1).compareToIgnoreCase("=") == 0) {
                    float newOT = otPrice;
                    HR[OTHour] = "=" + newOT;
                }

            }

            OTHour += 24;
            numOfDays++;
        }
        //If HoursElapsed is still > OTHour 
        //HR[OTHour + 24] += OTPrice;

        //HR0 -to- HR
        //long HoursElapsed = 0;
        //long MinsElapsed = 0;
        int i = 0;
        OTHour = OTHour - (24 * numOfDays);
        for (int x = 0; x <= HoursElapsed; x++) {
//            if (x < 25) {
            i = x;
            //If greater than 24hrs then check the adjustments on start HR to the first HR of first Rates
            //whatIsTheFirstHour()
            /*
            if (i > 24 && i < 48) {
                i = i - 24;
                //RESET 1stWaived
                Arrays.fill(HRWaived1st, Boolean.FALSE);
            } else if (i > 48 && i < 72) {
                i = i - 48;
            }
             */
            //log.info("is Waived:" + HRWaived1st[i]);
            if (i == OTHour) {
                flagOvernights++;
                OTHour = OTHour + 24; //Next OT Hour
                PrksMsg[8] = "::OVERNIGHT  " + flagOvernights + "::";
                OvernightPrice = OvernightPrice * flagOvernights;
                hasOvernight = true;
            }
            //Virtually impossible to be equal to 0 minutes and 0 hours
            //First Check for Grace Period
//Category 1 = Hours = 0 and Mins < Grace
            if (HoursElapsed == 0 && MinutesElapsed <= GracePeriod && firstscan == true) {
                AmountComputed = 0;
                stn.trtype = "G";
                ParkerType = "G";
                /* This is For PayUponEntry Only
                if (HRWaived1st[i] == false) {
                    if (HR[i].trim().substring(0, 1).compareToIgnoreCase("+") == 0) {
                        AmountComputed = AmountComputed + Float.parseFloat(HR[i].trim().substring(1));
                    } else if (HR[i].trim().substring(0, 1).compareToIgnoreCase("-") == 0) {
                        AmountComputed = AmountComputed - Float.parseFloat(HR[i].trim().substring(1));
                    } else if (HR[i].trim().substring(0, 1).compareToIgnoreCase("=") == 0) {
                        AmountComputed = Float.parseFloat(HR[i].trim().substring(1));
                    }
                }*/
            } //Next HR0 and Greater than GracePeriod
            //Category 2 = Hours = 0 and Mins > Grace
            else if (HoursElapsed == 0 && MinutesElapsed > GracePeriod) {
                if (HRWaived1st[i] == false) {
                    if (HR[i].trim().substring(0, 1).compareToIgnoreCase("+") == 0) {
                        AmountComputed = AmountComputed + Float.parseFloat(HR[i].trim().substring(1));
                    } else if (HR[i].trim().substring(0, 1).compareToIgnoreCase("-") == 0) {
                        AmountComputed = AmountComputed - Float.parseFloat(HR[i].trim().substring(1));
                    } else if (HR[i].trim().substring(0, 1).compareToIgnoreCase("=") == 0) {
                        AmountComputed = Float.parseFloat(HR[i].trim().substring(1));
                    }
                }
            } //Check This ******************************************************
            //Category 2 = Hours >= 1 and Mins = 0
            else if (HoursElapsed > 0 && MinutesElapsed == 0) {
                if (HRWaived1st[i] == false) {
                    if (HR[i].trim().substring(0, 1).compareToIgnoreCase("+") == 0) {
                        AmountComputed = AmountComputed + Float.parseFloat(HR[i].trim().substring(1));
                    } else if (HR[i].trim().substring(0, 1).compareToIgnoreCase("-") == 0) {
                        AmountComputed = AmountComputed - Float.parseFloat(HR[i].trim().substring(1));
                    } else if (HR[i].trim().substring(0, 1).compareToIgnoreCase("=") == 0) {
                        AmountComputed = Float.parseFloat(HR[i].trim().substring(1));
                    }
                }
            } //Category 2 = Hours >= 1 and Mins >= 1
            else if (HoursElapsed > 0 && MinutesElapsed > 0) {
                if (HRWaived1st[i] == false) {
                    if (HR[i].trim().substring(0, 1).compareToIgnoreCase("+") == 0) {
                        AmountComputed = AmountComputed + Float.parseFloat(HR[i].trim().substring(1));
                    } else if (HR[i].trim().substring(0, 1).compareToIgnoreCase("-") == 0) {
                        AmountComputed = AmountComputed - Float.parseFloat(HR[i].trim().substring(1));
                    } else if (HR[i].trim().substring(0, 1).compareToIgnoreCase("=") == 0) {
                        AmountComputed = Float.parseFloat(HR[i].trim().substring(1));
                    }
                }
//                if (i == HoursElapsed && MinutesElapsed == 0) {
//                    return AmountComputed;
//                }
                //Add the succeeding rates 
                //+++++ if FractionThereOf is true only
//                if (HRplusWaived1st[i] == false) {
//                    if (HRplus[i].trim().substring(0, 1).compareToIgnoreCase("+") == 0) {
//                        AmountComputed = AmountComputed + Float.parseFloat(HRplus[i].trim().substring(1));
//                    } else if (HRplus[i].trim().substring(0, 1).compareToIgnoreCase("-") == 0) {
//                        AmountComputed = AmountComputed - Float.parseFloat(HRplus[i].trim().substring(1));
//                    } else if (HRplus[i].trim().substring(0, 1).compareToIgnoreCase("=") == 0) {
//                        AmountComputed = Float.parseFloat(HRplus[i].trim().substring(1));
//                    }
//                }

            }

            System.out.print(".");
        }

        //i++; //Add 1 to check the next hour because the current hour is already paid 
        //and FractionThereOf takes the next Hour instead of the last
        //FractionThereOf
        //Must Add the HRplus
        if (FractionThereOf || MinutesElapsed > 0) {
            if (HRplusWaived1st[i] == false) {
                if (HRplus[i].trim().substring(0, 1).compareToIgnoreCase("+") == 0) {
                    if (i == 3) {
                        AmountComputed = AmountComputed + Float.parseFloat(HRplus[i].trim().substring(1));
                    } else if (i >= 4) {
                        AmountComputed = AmountComputed + Float.parseFloat(HR[i].trim().substring(1));
                    }
                } else if (HRplus[i].trim().substring(0, 1).compareToIgnoreCase("-") == 0) {
                    AmountComputed = AmountComputed - Float.parseFloat(HR[i].trim().substring(1));
                } else if (HRplus[i].trim().substring(0, 1).compareToIgnoreCase("=") == 0) {
                    AmountComputed = Float.parseFloat(HR[i].trim().substring(1));
                }
            }
        }

        //Add Lost Fees
        if (isLost) {
            if (LostPrice.trim().substring(0, 1).compareToIgnoreCase("+") == 0) {
                AmountComputed = AmountComputed + Float.parseFloat(LostPrice.trim().substring(1));
            } else if (LostPrice.trim().substring(0, 1).compareToIgnoreCase("-") == 0) {
                AmountComputed = AmountComputed - Float.parseFloat(LostPrice.trim().substring(1));
            } else if (LostPrice.trim().substring(0, 1).compareToIgnoreCase("=") == 0) {
                AmountComputed = Float.parseFloat(LostPrice.trim().substring(1));
            }
        }

        //Check the Next Due TimeStamp
        for (int x = i; x <= HR.length; x++) {
            if (x < 25) {
                i = x;
                if (HR[i].trim().substring(0, 1).compareToIgnoreCase("+") == 0) {
                    if (Float.parseFloat(HR[i].trim().substring(1)) > 0) {
                        DateConversionHandler dch = new DateConversionHandler();
                        nextDueTimeStamp = Long.parseLong(dateTimeINstamp) + (i * 3600);
                        Date d2 = dch.convertJavaUnixTime2Date(nextDueTimeStamp);
                        d2.setMinutes(0);
                        //nextDueTimeStamp = (long) (HourIN * 3600) + (i * 3600);
                        Date d1 = dch.convertJavaUnixTime2Date(dateTimeINstamp);
                        //log.info("dateTimeINstamp  converted:" + d1.toString());
                        //log.info("dateTimeINstamp  converted:" + d1.toString());
                        nextDueTimeStamp = dch.convertJavaDate2UnixTime(d2);
//                        log.info("nextDueTimeStamp converted:" + d2.toString());
//                        log.info("nextDueTimeStamp converted:" + d2.toString());
                        AmountGross = AmountComputed;
                        return AmountComputed;
                    }

                } else if (HR[i].trim().substring(0, 1).compareToIgnoreCase("-") == 0) {
                    //AmountComputed = AmountComputed - Float.parseFloat(HR[i].trim().substring(1));
                } else if (HR[i].trim().substring(0, 1).compareToIgnoreCase("=") == 0) {
                    if (Float.parseFloat(HR[i].trim().substring(1)) > 0) {
                        nextDueTimeStamp = Long.parseLong(dateTimeINstamp) + (i * 3600);
                        AmountGross = AmountComputed;
                        return AmountComputed;
                    }
                }
                if (FractionThereOf) {
                    if (HRplus[i].trim().substring(0, 1).compareToIgnoreCase("+") == 0) {
                        if (Float.parseFloat(HRplus[i].trim().substring(1)) > 0) {
                            nextDueTimeStamp = Long.parseLong(dateTimeINstamp) + (i * 3600) + 1; //plus 1 sec
                            AmountGross = AmountComputed;
                            return AmountComputed;
                        }
                    } else if (HRplus[i].trim().substring(0, 1).compareToIgnoreCase("-") == 0) {
                        //AmountComputed = AmountComputed - Float.parseFloat(HRplus[i].trim().substring(1));
                    } else if (HRplus[i].trim().substring(0, 1).compareToIgnoreCase("=") == 0) {
                        if (Float.parseFloat(HRplus[i].trim().substring(1)) > 0) {
                            nextDueTimeStamp = Long.parseLong(dateTimeINstamp) + (i * 3600) + 1;
                            AmountGross = AmountComputed;
                            return AmountComputed;
                        }
                    }
                }
            }
        }
//        log.info("Amount: " + AmountComputed);
//        log.info("GracePeriod: " + GracePeriod);
//        log.info("OTCutoff: " + OTCutoff);
        AmountGross = AmountComputed;
        return AmountComputed;
    }

    private int getFirstHourwidNonZeroRate(String[] HR, String[] HRplus, Boolean[] HRWaived1st, Boolean[] HRplusWaived1st) {
        float AmountComputed = 0;
        int firstHrwidNonZero = 0;
        for (int x = 1; x <= 23; x++) {
            if (HRWaived1st[x] == false) {
                if (HR[x].trim().substring(0, 1).compareToIgnoreCase("+") == 0) {
                    AmountComputed = Float.parseFloat(HR[x].trim().substring(1));
                } else if (HR[x].trim().substring(0, 1).compareToIgnoreCase("-") == 0) {
                    AmountComputed = AmountComputed - Float.parseFloat(HR[x].trim().substring(1));
                } else if (HR[x].trim().substring(0, 1).compareToIgnoreCase("=") == 0) {
                    AmountComputed = Float.parseFloat(HR[x].trim().substring(1));
                }
                if (HRplusWaived1st[x] == false) {
                    if (HRplus[x].trim().substring(0, 1).compareToIgnoreCase("+") == 0) {
                        AmountComputed = AmountComputed + Float.parseFloat(HRplus[x].trim().substring(1));
                    } else if (HRplus[x].trim().substring(0, 1).compareToIgnoreCase("-") == 0) {
                        AmountComputed = AmountComputed - Float.parseFloat(HRplus[x].trim().substring(1));
                    } else if (HRplus[x].trim().substring(0, 1).compareToIgnoreCase("=") == 0) {
                        AmountComputed = Float.parseFloat(HRplus[x].trim().substring(1));
                    }
                }
                if (AmountComputed > 0) {
                    firstHrwidNonZero = x;  //Because of Indexing issues
                    return firstHrwidNonZero;
                }
            }
        }
        return firstHrwidNonZero;
    }

    private int getLastHourwidNonZeroRate(String[] HR, String[] HRplus, Boolean[] HRWaived1st, Boolean[] HRplusWaived1st) {
        float AmountComputed = 0;
        int lastHrwidNonZero = 0;
        for (int x = 24; x >= 1; x--) {
            if (HRWaived1st[x] == false) {
                if (HR[x].trim().substring(0, 1).compareToIgnoreCase("+") == 0) {
                    AmountComputed = Float.parseFloat(HR[x].trim().substring(1));
                } else if (HR[x].trim().substring(0, 1).compareToIgnoreCase("-") == 0) {
                    AmountComputed = AmountComputed - Float.parseFloat(HR[x].trim().substring(1));
                } else if (HR[x].trim().substring(0, 1).compareToIgnoreCase("=") == 0) {
                    AmountComputed = Float.parseFloat(HR[x].trim().substring(1));
                }
                if (HRplusWaived1st[x] == false) {
                    if (HRplus[x].trim().substring(0, 1).compareToIgnoreCase("+") == 0) {
                        AmountComputed = AmountComputed + Float.parseFloat(HRplus[x].trim().substring(1));
                    } else if (HRplus[x].trim().substring(0, 1).compareToIgnoreCase("-") == 0) {
                        AmountComputed = AmountComputed - Float.parseFloat(HRplus[x].trim().substring(1));
                    } else if (HRplus[x].trim().substring(0, 1).compareToIgnoreCase("=") == 0) {
                        AmountComputed = Float.parseFloat(HRplus[x].trim().substring(1));
                    }
                }
                if (AmountComputed > 0) {
                    lastHrwidNonZero = 24 - x;  // Because of Indexing issues +1
                    return lastHrwidNonZero;
                }
            }
        }
        return lastHrwidNonZero;
    }

    public static void main(String args[]) {
        HybridPanelUI stn = new HybridPanelUI();
        ParkersAPI SP = new ParkersAPI();

        SP.setSysID("EN01");
        SP.setCardID("A82A94B1");
        SP.setPlateID("AA28934");
        SP.setTRID("R");
        SP.setAmountPaid("");

        ComputeAPI ca = new ComputeAPI(null);
        ca.SP = SP;
        ca.stn = stn;
        DateConversionHandler dch = new DateConversionHandler();
        dch.convertJavaDate2UnixTime(new Date());
        ca.dateTimeINstamp = new Date().getTime() + "";

        Float computed = 0f;

        /////GRACE PERIOD
        ca.HoursElapsed = 0;
        ca.MinutesElapsed = 0;
        computed = ca.Computation("R", true, false);
        System.out.println("       " + ca.HoursElapsed + "Hours : " + ca.MinutesElapsed + "Min :== * Amount is: " + computed);

        ca.HoursElapsed = 0;
        ca.MinutesElapsed = 19;
        computed = ca.Computation("R", true, false);
        System.out.println("       " + ca.HoursElapsed + "Hours : " + ca.MinutesElapsed + "Min :== * Amount is: " + computed);
        /////

        for (int i = 1; i <= 48; i++) {
            ca.HoursElapsed = i;
            ca.MinutesElapsed = 0;
            computed = ca.Computation("R", true, false);
            System.out.println("       " + ca.HoursElapsed + "Hours : " + ca.MinutesElapsed + "Min :== * Amount is: " + computed);

            ca.HoursElapsed = i;
            ca.MinutesElapsed = 1;
            computed = ca.Computation("R", true, false);
            System.out.println("       " + ca.HoursElapsed + "Hours : " + ca.MinutesElapsed + "Min :== * Amount is: " + computed);

        }

        System.exit(0);
    }
}
