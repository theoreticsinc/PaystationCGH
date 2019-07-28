/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package api;

import UserInteface.HybridPanelUI;
import java.awt.image.BufferedImage;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.ResourceBundle;
import javax.swing.ImageIcon;
import misc.DataBaseHandler;
import org.apache.log4j.LogManager;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import misc.DateConversionHandler;
import misc.LogUtility;
import misc.ParkerDataHandler;
import misc.XMLreader;
import modules.NOSfiles;

/**
 *
 * @author Angelo
 */
public class EntranceAPI {

    static Logger log = LogManager.getLogger(EntranceAPI.class.getName());
    ReadMIFARE mifare;
    private String datamode;
    public String[] SysMsg = new String[20];
    public String[] PrkrMsg = new String[20];
    public String servStatus = "";
    //public String PrevParker = "";
    //public boolean StartEntrance(String SentinelID, String PlateInput2, String CardInput2, String trtype, String CashierID, String CashierName)    
    private HybridPanelUI tpui;

    public EntranceAPI(HybridPanelUI stui) {
        tpui = stui;
        XMLreader xr = new XMLreader();

        try {
            datamode = xr.getElementValue("C://JTerminals/initH.xml", "datamode");
        } catch (Exception ex) {
            log.error("getElementValues " + ex.getMessage());
        }
    }

    public void InitiateEntry(String MEMtrtype) {

        if (tpui.Cardinput.length() >= tpui.CardDigits) {
            try {
                CRDPLTcheckDupAPI dupCP = new CRDPLTcheckDupAPI();
                tpui.SysMessage10.setText("Checking Card Number...");

                if (dupCP.isCRDDuplicate(tpui.CardInput2.getText().substring(0, tpui.CardDigits)) == true) {
                    tpui.clearLeftMIDMsgPanel();
                    tpui.clearRightPanel();

                    tpui.SysMessage11.setText(tpui.CardInput2.getText());
                    tpui.SysMessage12.setText("Card Duplicate");
                    tpui.SysMessage14.setText("try scanning");
                    tpui.SysMessage15.setText("another card");

                    tpui.Plateinput.delete(0, tpui.Plateinput.length());
                    tpui.PlateInput2.setText("");
                    tpui.Cardinput.delete(0, tpui.Cardinput.length());
                    tpui.CardInput2.setText("");
                } else {
                    //PlateComplete();
                    tpui.clearLeftMIDMsgPanel();
                    tpui.clearRightPanel();
                    //CheckCardforVIP();
                    //---Will transact entrance ---
                    //CardInput2.setText(Cardinput.toString());
                    if (this.StartEntrance(tpui, MEMtrtype) == true) {
                        /*DataBaseHandler dbh = new DataBaseHandler();
                        BufferedImage buf = dbh.GetImageFromDB(tpui.CardInput2.getText());
                        if (null != buf) {
                            tpui.entryCamera.setIcon(new ImageIcon(buf));
                        }*/
                        tpui.npd.Greeter.setVisible(false);
                        tpui.npd.VIPS.setText("");
                        tpui.ServedNo.setText(servStatus);
                        tpui.processLeftPanelMsgs(SysMsg);
                        //reset
                        tpui.trtype = "R";
                        tpui.Plateinput.delete(0, tpui.Plateinput.length());
                        tpui.PlateInput2.setText("");
                        tpui.Plateinput.delete(0, tpui.Plateinput.length());
                        tpui.Cardinput.delete(0, tpui.Cardinput.length());
                        tpui.CardInput2.setText("");
                    }
                    //------------------------------
                }
            } catch (Exception ex) {
                log.error(ex.getMessage());
            }
        }
    }

    public boolean StartEntrance(HybridPanelUI tpui, String MEMtrtype) {
        mifare = new ReadMIFARE();
        String bundleName = "mapping.idproperties";
        ResourceBundle myResources = ResourceBundle.getBundle(bundleName, Locale.getDefault());
        int CardDigits = Integer.parseInt(myResources.getString("CardNumbers"));
        DateConversionHandler dch = new DateConversionHandler();
        Date Now = new Date();
        String sendTime = String.valueOf(Now.getTime());
        String checkedMinutes = dch.formatTimeStamp(Now.getMinutes() - 1);
        String checkedHours = dch.formatTimeStamp(Now.getHours() - 1);
        if (checkedMinutes.compareTo("60") == 0) {
            int NextHour = Integer.parseInt(checkedHours) + 1;
            checkedHours = String.valueOf(NextHour);
            checkedMinutes = "00";
        }
        String transTime = checkedHours + checkedMinutes;
        ParkerDataHandler pdh = new ParkerDataHandler();
        
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-M-dd H:mm:ss.S");
        String d2 = sdf.format(Now);
        long timeStampIN = dch.convertJavaDate2UnixTime(Now);
//CHANGES TO MANUAL ENTRY. NO PLATE ENCODING AT ENTRY
        //if (tpui.PlateInput2.getText().length() >= tpui.PlateDigits) {
            /*
            try {
                Process t = Runtime.getRuntime().exec("C://JTerminals/tim.bat");//Linux Update Time                        
            } catch (Exception ex) {
                log.error(ex.getMessage());
            }
            try {
                //Runtime.getRuntime().exec("C://JTerminals/tim.bat");   //WINDOWS ONLY
                Process t = Runtime.getRuntime().exec("net time set --ipaddress=" + tpui.serverIP);//Linux Update Time      
            } catch (Exception ex) {
                log.error(ex.getMessage());
            }
            */
            //Check if plate and card input is filled
            //HYBRID ONLY --- Remember that Sentinel ID is separated into EN and EX
            //if (tpui.CardInput2.getText().compareToIgnoreCase("") != 0 && tpui.PlateInput2.getText().compareToIgnoreCase("") != 0) {
            if (tpui.CardInput2.getText().compareToIgnoreCase("") != 0) {
                SysMsg[11] = "Checking Server...";
                tpui.npd.PlsPay.setText("Welcome");
                tpui.npd.lblComeAgain.setText("Have a pleasant day");
                //Write to Card
                long cardtimeStampIN = dch.convertArduinoDate2UnixTime(Now);
                if (datamode.compareToIgnoreCase("cards") == 0) {
                    mifare.writeManualEntrance(String.valueOf(cardtimeStampIN));
                }
                if (pdh.saveParkerDB(tpui.entryIPCamera,"admin","user1234",tpui.ParkingArea, tpui.EN_SentinelID, tpui.CardInput2.getText(), tpui.PlateInput2.getText(), MEMtrtype, d2, timeStampIN) == false) {
                    LogUtility logthis = new LogUtility();
                    logthis.setLog(tpui.EN_SentinelID, "Unable to reach Server :" + Now.getTime() + " to save Ent Transaction: " + tpui.CardInput2.getText() + "   " + tpui.PlateInput2.getText());
                    tpui.PrevParker = tpui.CardInput2.getText();
                    //if unable to save to server database then save to offline text file
                    SysMsg[0] = "SERVER OFFLINE";
                    if (pdh.saveParkerSTUB(tpui.ParkingArea, tpui.EN_SentinelID, tpui.CardInput2.getText().substring(0, CardDigits), tpui.PlateInput2.getText(), MEMtrtype, sendTime) == false) //unable to save to self
                    {
                        SysMsg[11] = "ENTRY FAILED";
                        SysMsg[12] = "Please Scan again: " + tpui.EN_SentinelID;
                    } else //means CRD & PLT was saved to self
                    {
                        logthis.setsysLog(tpui.EN_SentinelID, Now.getTime() + "saved locally: " + tpui.CardInput2.getText() + "   " + tpui.PlateInput2.getText());
                        SysMsg[11] = "GIVE CARD";
                        SysMsg[13] = "Server Offline";
                        try //update car served
                        {
                            NOSfiles nf = new NOSfiles();
                            nf.UpdateCarServed();
                            logthis.setsysLog(tpui.EN_SentinelID, Now.getTime() + "car served: " + servStatus);
                            if (MEMtrtype.compareToIgnoreCase("M") != 0) {
                                nf.UpdateCarSlots(tpui.EN_SentinelID, tpui.slotsmode);
                            }
                            servStatus = nf.getCarServed();
                        } catch (Exception ex) {
                            log.error(ex.getMessage());
                        }
                    }
                } else {
                    //SysMessage1.setText("Server not Found");
                    try //update car served and car slots because of the errors in saving the parking transactions
                    {
                        /*DataBaseHandler dbh = new DataBaseHandler();
                        BufferedImage buf = dbh.GetImageFromDB(tpui.CardInput2.getText());
                        if (null != buf) {
                            tpui.entryCamera.setIcon(new ImageIcon(buf));
                        }*/
                        LogUtility logthis = new LogUtility();
                        logthis.setsysLog(tpui.EN_SentinelID, Now.getTime() + "successful Transaction: " + tpui.CardInput2.getText() + "   " + tpui.PlateInput2.getText());
                        NOSfiles nf = new NOSfiles();
//                        nf.UpdateCarServed();
                        servStatus = nf.getCarServed();
                        logthis.setsysLog(tpui.EN_SentinelID, Now.getTime() + "car served: " + servStatus);
                        if (MEMtrtype.compareToIgnoreCase("M") != 0) {
//                            nf.UpdateCarSlots(tpui.EN_SentinelID, tpui.slotsmode);
                        }

                    } catch (Exception ex) {
                        log.error(ex.getMessage());
                    }
                    if (pdh.saveENParkerTrans(tpui.CashierID, tpui.CashierName, tpui.EN_SentinelID, tpui.CardInput2.getText().substring(0, CardDigits), tpui.PlateInput2.getText(), MEMtrtype, transTime) == false) {
                        SysMsg[17] = "TRANSACTION Saved Locally";
                    }
                    SysMsg[11] = "GIVE CARD";

                    SysMsg[13] = tpui.PlateInput2.getText();
                    SysMsg[15] = tpui.CardInput2.getText();
                    ParkersAPI pa = new ParkersAPI();
                    String ptemp = pa.checkPTypeFromDB(MEMtrtype);
                    SysMsg[16] = "<" + ptemp + ">";

                    SysMsg[18] = "Transaction Saved to Server";
                    tpui.PrevParker = tpui.CardInput2.getText();
//                        Process s = Runtime.getRuntime().exec("sudo chmod 777 /SYSTEMS/"+tpui.PrevParker+".crd");
//                        s.waitFor();
//                        s = Runtime.getRuntime().exec("sudo chmod 777 /SYSTEMS/"+tpui.PlateInput2.getText()+".plt");
//                        s.waitFor();                                                
                }
            }
            //reset values
//            trtype = "R";
//            PlateInput2.setText("");
//            Cardinput.delete(0, Cardinput.length());
//            CardInput2.setText(""); 
            return true;
        //}
        //return false;
    }
    
    public boolean createManualEntry(String MEMtrtype, String dateManuallyCreated, String plateManuallyCreated) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-M-dd H:mm:ss.S");
        ParkerDataHandler pdh = new ParkerDataHandler();
        if (pdh.saveParkerDB(tpui.entryIPCamera,"admin","user1234",tpui.ParkingArea, tpui.EN_SentinelID, tpui.CardInput2.getText(), plateManuallyCreated.toUpperCase(), MEMtrtype, dateManuallyCreated, 0L) == false) {
        }
        return false;
    }

}
