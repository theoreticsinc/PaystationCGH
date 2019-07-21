/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package api;

import UserInteface.HybridPanelUI;
import misc.LogUtility;
import misc.RawFileHandler;
import misc.ServerDataHandler;
import modules.NOSfiles;
import org.apache.log4j.LogManager;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;

/**
 *
 * @author Angelo
 */
public class PreEntranceAPI {
    
    static Logger log = LogManager.getLogger(PreEntranceAPI.class.getName());

    public String[] SysMsg = new String[10];
    public String[] PrkrMsg = new String[10];
    public String servStatus = "";
    //public String PrevParker = "";
    //public boolean StartEntrance(String SentinelID, String PlateInput2, String CardInput2, String trtype, String CashierID, String CashierName)    

    public boolean StartBackout(HybridPanelUI tpui) {
        LogUtility logthis = new LogUtility();
        ServerDataHandler sdh = new ServerDataHandler();
        RawFileHandler rfh = new RawFileHandler();
        try {
            if (tpui.PrevParker.compareTo(tpui.CardInput2.getText().substring(0, 6)) == 0) {
                String pastTR = "";
                if (rfh.FindFileFolder("/SYSTEMS/", tpui.Cardinput.substring(0, 6) + ".crd") == true) {
                    String pastcard = rfh.readFline("/SYSTEMS/", tpui.Cardinput.substring(0, 6) + ".crd", 1);
                    pastTR = pastcard.substring(16, 17);
                }
                String tempplate = "";

                tempplate = sdh.DeletePrevParker(tpui.Cardinput.substring(0, 6));
                logthis.setLog(tpui.EN_SentinelID, tpui.CashierName + "  " + tpui.CashierID + "  " + "Parker Backout: " + tempplate + "Card: " + tpui.Cardinput.substring(0, 6));
                PrkrMsg[0] = "Backout SUCCESSFUL";
                PrkrMsg[2] = "Plate: " + tempplate + " Deleted";
                PrkrMsg[4] = "Card:  " + tpui.PrevParker + " Deleted";
                tpui.PrevParker = "";
                try //update car served backwards//BACKOUT numbers
                {
                    NOSfiles nf = new NOSfiles();
                    nf.RenewCarServed();
                    if (pastTR.compareTo("M") != 0) {
                        nf.RenewCarSlots(tpui.EN_SentinelID);
                    }
                    servStatus = nf.getCarServed();
                    tpui.EntryTickets.setText(nf.getEntryTicketsServed());
                    tpui.ExitTickets.setText(nf.getExitTicketsServed());
                    return true;
                } catch (Exception ex) {
                    log.error(ex.getMessage());
                }
            } else {
                PrkrMsg[1] = "Backout FAILED";
                PrkrMsg[3] = "CARD: " + tpui.Cardinput.substring(0, 6);
            }
        } catch (Exception ex) {
            log.error(ex.getMessage());
        }
        return false;
    }

    public boolean PlateComplete(HybridPanelUI tpui) {
        CRDPLTcheckDupAPI dupPlt = new CRDPLTcheckDupAPI();
        if (tpui.PlateInput2.getText().length() == tpui.PlateDigits && tpui.ExitSwitch == false) {
            //tpui.SysMessage10.setText("...6 digit Plate Number complete...");
            try {
                if (dupPlt.isPLTDuplicate(tpui.PlateInput2.getText().substring(0, 6)) == true) {
                    //clearLeftMIDMsgPanel();
                    tpui.SysMessage1.setText(tpui.PlateInput2.getText());
                    tpui.SysMessage3.setText("Plate Duplicate");
                    tpui.PlateInput2.setText("");
                    tpui.Plateinput.delete(0, tpui.Plateinput.length());
                    tpui.SysMessage10.setText("Check Plate Number");
                    return false;
                } else {
                    //clearLeftMIDMsgPanel();
                    if (tpui.Cardinput.length() >= tpui.CardDigits) {
                        tpui.SysMessage4.setText("Plate No. is valid");
                        tpui.SysMessage5.setText("Card  No. is valid");
                        tpui.SysMessage7.setText("Press Enter");
                    } else {
                        tpui.SysMessage5.setText("Plate No. is valid");
                        tpui.SysMessage7.setText("SCAN CARD");
                    }
                    return true;
                }
            } catch (Exception ex) {
                log.error(ex.getMessage());
                return false;
            }

        }
        return false;
    }

    public void SPCLPlateComplete2(HybridPanelUI tpui) {
        SpecialParkersAPI SP = new SpecialParkersAPI();
        boolean Vipfound = SP.checkVIPPlate(tpui.PlateInput2.getText());
        if (Vipfound == true) {
            tpui.clearLeftMIDMsgPanel();
            tpui.trtype = "V";
            tpui.ParkerInfo1.setText(tpui.PlateInput2.getText() + " is VIP Parker");
            tpui.ParkerInfo3.setText(tpui.PlateInput2.getText());
            tpui.ParkerInfo4.setText("Have A Pleasant Day");
            tpui.npd.Greeter.setVisible(true);
            tpui.npd.VIPS.setText(tpui.PlateInput2.getText());
        } else {
            String MPPcrdpltFound = SP.checkMPPCrdPlt(tpui.PlateInput2.getText());
            if (MPPcrdpltFound.compareToIgnoreCase("") != 0) {
                tpui.clearLeftMIDMsgPanel();
                tpui.trtype = "P";
                tpui.ParkerInfo1.setText(" Monthly Prepaid Parker");
                tpui.ParkerInfo2.setText(tpui.PlateInput2.getText());
                tpui.ParkerInfo4.setText(tpui.PlateInput2.getText());
                tpui.ParkerInfo6.setText("Promo will deactivate in: " + MPPcrdpltFound);
                tpui.npd.Greeter.setVisible(true);
                tpui.npd.VIPS.setText(tpui.PlateInput2.getText());
            }
            if (SP.check2ndMPPCrdPlt(tpui.PlateInput2.getText()) == true) {
                tpui.clearLeftMIDMsgPanel();
                tpui.trtype = "R";
                tpui.ParkerInfo1.setText("MPP Parker");
                tpui.ParkerInfo3.setText("2nd Plate");
                tpui.ParkerInfo4.setText("Already active");
                tpui.ParkerInfo6.setText("Returning to Retail");
                tpui.npd.Greeter.setVisible(true);
                tpui.npd.VIPS.setText(tpui.PlateInput2.getText());
            }

            if (SP.checkRunAwayPlate(tpui.PlateInput2.getText()) == true) {
                tpui.clearLeftMIDMsgPanel();
                tpui.ParkerInfo1.setText("RUN123 is RUNAWAY Parker. Call for assistance");
                tpui.ParkerInfo3.setText("RUN123 has outstanding Parking Tickets");
                tpui.ParkerInfo4.setText("and has ran awayed from the Barangay Tire-Boot Team");
            }
        }
    }

}
