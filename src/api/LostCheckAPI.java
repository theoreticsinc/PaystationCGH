/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package api;

import UserInteface.HybridPanelUI;
import java.io.IOException;
import org.apache.log4j.LogManager;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import misc.RawFileHandler;
import modules.SystemStatus;

/**
 *
 * @author teller2
 */
public class LostCheckAPI {
    
    static Logger log = LogManager.getLogger(LostCheckAPI.class.getName());

    SystemStatus ss = new SystemStatus();
    RawFileHandler rfh = new RawFileHandler();
    public String[] SysMsg = new String[10];

    public boolean isPLTvalid(String plate2check, String ip) {
        boolean found = false;
        if (ss.checkPING(ip) == true) {
            try {
                found = rfh.FindFileFolder("/SYSTEMS/" + plate2check + ".plt");
            } catch (Exception ex) {
                log.error(ex.getMessage());
            }
        }
        return found;
    }

    public String getSysID(String PlateCheck) throws IOException {
        String proc = rfh.readFline("/SYSTEMS/", PlateCheck + ".plt", 1);
        return proc.substring(0, 4);
    }

    public String getCardID(String PlateCheck) {
        try {
            String proc = rfh.readFline("/SYSTEMS/", PlateCheck + ".plt", 1);
            return proc.substring(4, 10);
        } catch (Exception ex) {
            log.error(ex.getMessage());
        }
        return "";
    }

    public String getPlateID(String PlateCheck) throws IOException {
        String proc = rfh.readFline("/SYSTEMS/", PlateCheck + ".plt", 1);
        return proc.substring(10, 16);
    }

    public String getTRID(String PlateCheck) throws IOException {
        String proc = rfh.readFline("/SYSTEMS/", PlateCheck + ".plt", 1);
        return proc.substring(16, 17);
    }

    public String getTimeID(String PlateCheck) throws IOException {
        String proc = rfh.readFline("/SYSTEMS/", PlateCheck + ".plt", 1);
        return proc.substring(17, 30);
    }

    public boolean StartLOSTcheck(HybridPanelUI stn) {
        String plate2check = stn.PlateInput2.getText().toString().toUpperCase();
        stn.LCEPtemp = plate2check;

        if (isPLTvalid(plate2check, stn.serverIP) == true) {
            String LostCard = getCardID(plate2check);
            SysMsg[5] = "LOST CARD";
            SysMsg[6] = LostCard;
            SysMsg[7] = "FOUND";
            stn.Cardinput.delete(0, stn.Cardinput.length());
            stn.Cardinput.append(LostCard);
            stn.CardInput2.setText(LostCard);
            stn.LostOverride = true;
            return true;
        } else {
            SysMsg[5] = ("LOST CARD Retrival Failed");
            SysMsg[6] = (plate2check + " not found");

            return false;
        }
    }
}
