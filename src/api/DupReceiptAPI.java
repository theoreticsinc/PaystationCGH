/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package api;

import java.io.IOException;
import org.apache.log4j.LogManager;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import misc.USBEpsonHandler;
import misc.RawFileHandler;

/**
 *
 * @author root
 */
public class DupReceiptAPI {

    static Logger log = LogManager.getLogger(DupReceiptAPI.class.getName());
    RawFileHandler rfh = new RawFileHandler();

    public void printDUPreceipt() {
        String SentinenlID = null, Entrypoint = null, Plateno = null, ParkerType = null, TimeIN = null, TimeOUT = null;
        String HoursParked = "", MinutesParked = "";
        float AmountDue = 0;
        String RNos = null, CashierName = null;
        boolean OvernightOverride = false;

        USBEpsonHandler ea = new USBEpsonHandler();
        //ea.initializePrinter();
        String StrAmount = "";
        String timeElapsed = "";
        float VAT = 0;
        float VAmount = 0;
        try {
            SentinenlID = rfh.readFline("C://JTerminals/de4Dd87d/CfgJ9rl/", "rAILs", 1);
            Entrypoint = rfh.readFline("C://JTerminals/de4Dd87d/CfgJ9rl/", "rAILs", 2);
            Plateno = rfh.readFline("C://JTerminals/de4Dd87d/CfgJ9rl/", "rAILs", 3);
            ParkerType = rfh.readFline("C://JTerminals/de4Dd87d/CfgJ9rl/", "rAILs", 4);
            TimeIN = rfh.readFline("C://JTerminals/de4Dd87d/CfgJ9rl/", "rAILs", 5);
            TimeOUT = rfh.readFline("C://JTerminals/de4Dd87d/CfgJ9rl/", "rAILs", 6);
            HoursParked = rfh.readFline("C://JTerminals/de4Dd87d/CfgJ9rl/", "rAILs", 7);
            MinutesParked = rfh.readFline("C://JTerminals/de4Dd87d/CfgJ9rl/", "rAILs", 8);
            timeElapsed = HoursParked + "hrs :" + MinutesParked + "mins";
            StrAmount = rfh.readFline("C://JTerminals/de4Dd87d/CfgJ9rl/", "rAILs", 9);
            AmountDue = Float.parseFloat(StrAmount);
            RNos = rfh.readFline("C://JTerminals/de4Dd87d/CfgJ9rl/", "rAILs", 10);
            CashierName = rfh.readFline("C://JTerminals/de4Dd87d/CfgJ9rl/", "rAILs", 11);
            String strOvernightOverride = rfh.readFline("C://JTerminals/de4Dd87d/CfgJ9rl/", "rAILs", 12);
            OvernightOverride = Boolean.parseBoolean(strOvernightOverride);

        } catch (Exception ex) {
            log.error(ex.getMessage());
        }

        //VAT = (float) (AmountDue * .12);
        VAT = (float) (AmountDue / 1.12) * 0.12f;
        VAmount = (float) (AmountDue - VAT);
        ea.Justify((byte) 1);
        ea.printline("DUPLICATE RECEIPT ONLY");
        ea.Justify((byte) 0);
        if (OvernightOverride == false) {
            ea.printline(".::" + this.checkPType(ParkerType) + "::.");
        } else {
            ea.printline(".::OVERNIGHT PARKER::.");
        }
        ea.feedpaperup((byte) 1);

        ea.setBlack();
        ea.printline("Plate Number : " + Plateno);
        ea.printline("Entry Point : " + Entrypoint);
        ea.printline("Time IN : " + TimeIN);
        ea.printline("Time OUT : " + TimeOUT);
        ea.printline("Hours Parked : " + timeElapsed);
        ea.printline("Total : " + VAmount + "0");
        ea.printline("VAT : " + VAT + "0");
        ea.printline("Amount (incl. TAX): " + StrAmount + "0");
        ea.feedpaperup((byte) 1);
        ea.setRed();
        ea.printline("Cashier : " + CashierName);
        ea.printline(RNos);
        ea.feedpaperup((byte) 1);
        ea.Justify((byte) 1);
        ea.printline("This serves as your");
        ea.printline("Official Receipt <DUPLICATE RECEIPT>");
        ea.printline("Thank you. Please Park with us Again");
        ea.feedpaperup((byte) 1);
        ea.printHEADER(SentinenlID);
        //ea.fullcut();
        ea.closePrinter();
    }

    public void saveDUPreceipt(String SentinenlID, String Entrypoint, String Plateno, String ParkerType, String TimeIN, String TimeOUT, long HoursParked, long MinutesParked, float AmountDue, String RNos, String CashierName, boolean OvernightOverride) {
        try {
            rfh.putfile("C://JTerminals/de4Dd87d/CfgJ9rl/", "rAILs", SentinenlID);
            rfh.appendfile("C://JTerminals/de4Dd87d/CfgJ9rl/", "rAILs", "");
            rfh.appendfile("C://JTerminals/de4Dd87d/CfgJ9rl/", "rAILs", Entrypoint);
            rfh.appendfile("C://JTerminals/de4Dd87d/CfgJ9rl/", "rAILs", Plateno);
            rfh.appendfile("C://JTerminals/de4Dd87d/CfgJ9rl/", "rAILs", ParkerType);
            rfh.appendfile("C://JTerminals/de4Dd87d/CfgJ9rl/", "rAILs", TimeIN);
            rfh.appendfile("C://JTerminals/de4Dd87d/CfgJ9rl/", "rAILs", TimeOUT);
            String strHoursParked = String.valueOf(HoursParked);
            rfh.appendfile("C://JTerminals/de4Dd87d/CfgJ9rl/", "rAILs", strHoursParked);
            String strMinutesParked = String.valueOf(MinutesParked);
            rfh.appendfile("C://JTerminals/de4Dd87d/CfgJ9rl/", "rAILs", strMinutesParked);
            String strAmountDue = String.valueOf(AmountDue);
            rfh.appendfile("C://JTerminals/de4Dd87d/CfgJ9rl/", "rAILs", strAmountDue);
            rfh.appendfile("C://JTerminals/de4Dd87d/CfgJ9rl/", "rAILs", RNos);
            rfh.appendfile("C://JTerminals/de4Dd87d/CfgJ9rl/", "rAILs", CashierName);
            String strOvernightOverride = String.valueOf(OvernightOverride);
            rfh.appendfile("C://JTerminals/de4Dd87d/CfgJ9rl/", "rAILs", strOvernightOverride);
        } catch (IOException ex) {
            log.error(ex.getMessage());
        }
    }

    public String checkPType(String ParkerType) {
        String Type = ParkerType;
        if (ParkerType.compareToIgnoreCase("R") == 0) {
            Type = "Retail";
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
        }
        return Type;
    }

}
