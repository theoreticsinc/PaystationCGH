/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package api;

import UserInteface.HybridPanelUI;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.util.Date;
import javax.swing.ImageIcon;
import misc.DataBaseHandler;
import org.apache.log4j.LogManager;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;

/**
 *
 * @author Administrator
 */
public class ExitAPI implements Runnable {

    public String[] SysMsg = new String[20];
    private HybridPanelUI stn;
    private ComputeAPI cmp;

    //initializing the logger
    static Logger log = LogManager.getLogger(ExitAPI.class.getName());

    public ExitAPI(HybridPanelUI stui) {
        stn = stui;
        cmp = new ComputeAPI(stn);
    }

    public boolean InitiateExit(Date NowStamp, boolean firstscan, String Override, boolean PrinterOverride) {
        if (stn.CardInput2.getText().length() >= 8) {
            //stn.AmtTendered.setText("");
            //stn.PreviousCard = stn.CardInput2.getText();//Disable Double scanning by Barcode Scanner
            /*
            try {
                //Process t = Runtime.getRuntime().exec("C://JTerminals/senddat.exe output.txt USBPRN0"); //Linux Update Time
                //String pingCmd = "sudo mount -t cifs //192.168.1.10/SYSTEMS /SYSTEMS -o user=root,password=sssigsc";
                //PRINTER
                //INITIALIZE
                String pingCmd = "C://JTerminals/senddat.exe D:/output.txt USBPRN0";
                Runtime r = Runtime.getRuntime();
                Process p = r.exec(pingCmd);
                p.waitFor();
               
            } catch (Exception ex) {
                LogManager.getLogger(ExitAPI.class.getName()).log(Level.SEVERE, null, ex);
            }
             */
            try {
                short process = cmp.isValidInputController(NowStamp, false, firstscan, Override, PrinterOverride);//computing stuff                
                if (process == 0) {
                    stn.firstscan = true;
                    //stn.PreviousCard = stn.CardInput2.getText();
                    stn.processRightPanelMsgs(cmp.PrksMsg);
                    //stn.validate();
                    //cmp.ValidPartII();//printing stuff and saving stuff
                    //do not reset back yet
                    //stn.trtype = "R";
                    //PlateInput2.setText("");
                    //stn.Cardinput.delete(0, stn.Cardinput.length());
                    //stn.CardInput2.setText("");
//                    SysMessage1.setText(CardInput2.getText().substring(0, CardDigits));
//                    SysMessage2.setText("FOUND");  
                    DataBaseHandler dbh = new DataBaseHandler();
                    BufferedImage buf = dbh.GetImageFromDB(stn.CardInput2.getText());
                    Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

                    if (null != buf) {
                        Image img = getScaledImage(buf, screenSize.width / 4 + 100, screenSize.height / 3);

                        stn.entryCamera.setIcon(new ImageIcon(img));
                        stn.entryCamera.setText("ENTRY");
                    }
                    //stn.AmtTendered.requestFocus();
                    return true;
                } else if (process == 1) {
                    stn.PreviousCard = "";
                    stn.clearLeftMIDMsgPanel();
                    stn.clearRightPanel();
                    stn.StartComparingCard2DB();
                    //stn.firstscan = false;
                    //stn.trtype = "R";
                    //this.InitiateRescan();
                    //stn.Cardinput.delete(0, stn.Cardinput.length());
                    //stn.CardInput2.setText("");
                    //stn.Plateinput.delete(0, stn.Plateinput.length());
                    //stn.PlateInput2.setText("");
                    return false;
                } else if (process == 4) {
                    SysMsg[2] = "Card Found";
                    SysMsg[3] = "Errors";
                    SysMsg[4] = "Card Unprocessed";
                    SysMsg[6] = "Please separate card";
                    stn.trtype = "R";
                    stn.PreviousCard = "";
                    stn.Cardinput.delete(0, stn.Cardinput.length());
                    stn.CardInput2.setText("");
                    return false;
                } else if (process == 5) {
                    stn.firstscan = true;
                    stn.PreviousCard = stn.CardInput2.getText();
                    stn.processRightPanelMsgs(cmp.PrksMsg);
                    stn.clearLeftMIDMsgPanel();
                    stn.processRightPanelMsgs(cmp.PrksMsg);
                    SysMsg[1] = stn.CardInput2.getText();
                    SysMsg[2] = "Card Still Not Found";
                    SysMsg[4] = "Input Plate Number";
                    SysMsg[5] = "then press Enter";
                    SysMsg[7] = "::Create Manual Card::";
                    //SysMsg[8] = "P30.00";
                    //stn.StartInvalidFlatRate();
                    stn.processRightPanelMsgs(cmp.PrksMsg);
                    stn.trtype = "R";
                    //PlateInput2.setText("");
                    stn.Plateinput.delete(0, stn.Plateinput.length());
                    stn.PlateInput2.setText("");
                    return false;
                } else if (process == 7) {
                    //SysMsg[2] = "Card has wrong PARKER TYPE";
                    //SysMsg[4] = "Please separate card";
                    SysMsg[2] = "Card has already been paid for";
                    SysMsg[4] = "Please proceed to Exit";
                    stn.trtype = "R";
                    stn.PreviousCard = "";
                    stn.Cardinput.delete(0, stn.Cardinput.length());
                    stn.CardInput2.setText("");

                    return false;
                } else if (process == 8) {
                    SysMsg[2] = "Card has wrong Date/Time";
                    SysMsg[4] = "Please separate card";
                    stn.trtype = "R";
                    stn.PreviousCard = "";
                    stn.Cardinput.delete(0, stn.Cardinput.length());
                    stn.CardInput2.setText("");

                    return false;
                } else if (process == 9) {
                    SysMsg[2] = "Errors in saving";
                    SysMsg[3] = "Local Files";
                    SysMsg[4] = "Please Call Service Engineers";
                    SysMsg[5] = "immediately";
                    stn.trtype = "R";
                    stn.PreviousCard = "";
                    stn.Cardinput.delete(0, stn.Cardinput.length());
                    stn.CardInput2.setText("");
                    return false;
                }
            } catch (Exception ex) {
                ex.printStackTrace();
                log.debug(ex);
            }

        }
        //after processing exit trtrype must be reset to "R"
        stn.trtype = "R";
        //PlateInput2.setText("");
        //stn.Cardinput.delete(0, stn.Cardinput.length());
        //stn.CardInput2.setText("");

        return false;
    }

    private Image getScaledImage(Image srcImg, int w, int h) {
        BufferedImage resizedImg = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = resizedImg.createGraphics();

        g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g2.drawImage(srcImg, 0, 0, w, h, null);
        g2.dispose();

        return resizedImg;
    }

    public void InitiateRescan() {
        stn.processRightPanelMsgs(cmp.PrksMsg);
        stn.clearLeftMIDMsgPanel();
        stn.processRightPanelMsgs(cmp.PrksMsg);
        SysMsg[2] = "Please Rescan";
        SysMsg[3] = "Previous Card";

        SysMsg[7] = "Card Error: " + stn.CardInput2.getText();
    }

    public boolean InitiateInvalids() {
        if (stn.PlateInput2.getText().length() >= 6) {
            try {
                stn.processRightPanelMsgs(cmp.PrksMsg);
                stn.clearLeftMIDMsgPanel();
                stn.processRightPanelMsgs(cmp.PrksMsg);
                stn.trtype = "F";
                stn.IFROverride = true;//for resetting later
                //cmp.isValidInputController(true, false, Override);//true if LCEP must printout without record                    

                stn.processRightPanelMsgs(cmp.PrksMsg);

//                    ParkersAPI SP = new ParkersAPI();
//                    SP.savetoOffline(stn.CardInput2.getText());
                stn.InvalidFlatRate = false;
                stn.Plateinput.delete(0, stn.Plateinput.length());
                stn.PlateInput2.setText("");
                stn.Cardinput.delete(0, stn.Cardinput.length());
                stn.CardInput2.setText("");
                stn.InvalidFlatRate = false;
                stn.IFROverride = false;
                stn.VIPOverride = false;
                stn.resetInvalidFlatRates();
                return true;
            } catch (Exception ex) {
                log.debug(ex);
                return false;
            }

        }
        //after processing exit trtrype must be reset to "R"
//                    stn.trtype = "R";
//                    stn.InvalidFlatRate = false;
//                    stn.Plateinput.delete(0, stn.Plateinput.length());
//                    stn.PlateInput2.setText("");
//                    stn.Cardinput.delete(0, stn.Cardinput.length());
//                    stn.CardInput2.setText("");

        return false;
    }

    public void ValidPartII() {
        cmp.ValidPartII();//printing stuff and saving stuff
        stn.trtype = "R";
    }

    @Override
    public void run() {
        ValidPartII();
    }
}
