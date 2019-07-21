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

/**
 *
 * @author Administrator
 */
public class MasterCardAPI {

    public String[] SysMsg = new String[20];
    
    static Logger log = LogManager.getLogger(MasterCardAPI.class.getName());

    public String getMasterRec1() throws IOException {
        RawFileHandler rfh = new RawFileHandler();

        String newCard = "";
        boolean foundfile = rfh.FindFileFolder("C://JTerminals/de4Dd87d/CfgJ9rl/", "mcard.jrt");
        if (foundfile == true) {
            newCard = rfh.readFline("C://JTerminals/de4Dd87d/CfgJ9rl/", "mcard.jrt", 1);
        } else {
            newCard = "ABCD5678";  //eight digits
            rfh.putfile("C://JTerminals/de4Dd87d/CfgJ9rl/", "mcard.jrt", newCard);
        }
        return newCard;
    }

    public String getMasterOwner1() throws IOException {
        RawFileHandler rfh = new RawFileHandler();

        String newCard = "";
        boolean foundfile = rfh.FindFileFolder("C://JTerminals/de4Dd87d/CfgJ9rl/", "mcard.jrt");
        if (foundfile == true) {
            newCard = rfh.readFline("C://JTerminals/de4Dd87d/CfgJ9rl/", "mcard.jrt", 2);
        }
        return newCard;
    }

    public String getMasterRec2() throws IOException {
        RawFileHandler rfh = new RawFileHandler();

        String newCard = "";
        boolean foundfile = rfh.FindFileFolder("C://JTerminals/de4Dd87d/CfgJ9rl/", "m2card.jrt");
        if (foundfile == true) {
            newCard = rfh.readFline("C://JTerminals/de4Dd87d/CfgJ9rl/", "m2card.jrt", 1);
        } else {
            newCard = "ABCD1234";  //eight digits
            rfh.putfile("C://JTerminals/de4Dd87d/CfgJ9rl/", "m2card.jrt", newCard);
        }
        return newCard;
    }
    
    public String getMasterOwner2() throws IOException {
        RawFileHandler rfh = new RawFileHandler();

        String newCard = "";
        boolean foundfile = rfh.FindFileFolder("C://JTerminals/de4Dd87d/CfgJ9rl/", "m2card.jrt");
        if (foundfile == true) {
            newCard = rfh.readFline("C://JTerminals/de4Dd87d/CfgJ9rl/", "m2card.jrt", 2);
        }
        return newCard;
    }

    public boolean isMasterCardValid(HybridPanelUI stn) {
        try {

            String masterpword1 = this.getMasterRec1();
            String masterpword2 = this.getMasterRec2();
            if (masterpword1.compareTo(stn.MasterCardinput.toString()) == 0) {
                SysMsg[11] = "Master1 Card";
                //SysMessage2.setText(MasterCardInput2.getText());
                SysMsg[12] = "- is Valid -";
                SysMsg[16] = "Enter Password";
                stn.MasterIN = false;
                stn.MasterCard1 = true;
                stn.MasterCard2 = false;
                stn.resetMasterCard();
                return true;
            }
            if (masterpword2.compareTo(stn.MasterCardinput.toString()) == 0) {
                SysMsg[11] = "Master2 Card";
                //SysMessage2.setText(MasterCardInput2.getText());
                SysMsg[12] = "- is Valid -";
                SysMsg[16] = "Enter Password";
                stn.MasterIN = false;
                stn.MasterCard1 = false;
                stn.MasterCard2 = true;
                stn.resetMasterCard();
                return true;
            } else {
                SysMsg[11] = "INCORRECT";
                //SysMessage2.setText(MasterCardInput2.getText());
                SysMsg[14] = "MasterCard";
                SysMsg[16] = "Scan Card Again";
                stn.MasterIN = false;
                stn.resetMasterCard();
                //secret = 0;
            }

        } catch (IOException ex) {
            log.error(ex.getMessage());
        }
        return false;
    }


    public String getMasterRec3() throws IOException {
        RawFileHandler rfh = new RawFileHandler();

        String newCard = "";
        boolean foundfile = rfh.FindFileFolder("C://JTerminals/de4Dd87d/CfgJ9rl/", "m3card.jrt");
        if (foundfile == true) {
            newCard = rfh.readFline("C://JTerminals/de4Dd87d/CfgJ9rl/", "m3card.jrt", 1);
        } else {
            newCard = "ABCD1234";  //eight digits
            rfh.putfile("C://JTerminals/de4Dd87d/CfgJ9rl/", "m3card.jrt", newCard);
        }
        return newCard;
    }
    
    public String getMasterOwner3() throws IOException {
        RawFileHandler rfh = new RawFileHandler();

        String newCard = "";
        boolean foundfile = rfh.FindFileFolder("C://JTerminals/de4Dd87d/CfgJ9rl/", "m3card.jrt");
        if (foundfile == true) {
            newCard = rfh.readFline("C://JTerminals/de4Dd87d/CfgJ9rl/", "m3card.jrt", 2);
        }
        return newCard;
    }
    public String getMasterRec4() throws IOException {
        RawFileHandler rfh = new RawFileHandler();

        String newCard = "";
        boolean foundfile = rfh.FindFileFolder("C://JTerminals/de4Dd87d/CfgJ9rl/", "m4card.jrt");
        if (foundfile == true) {
            newCard = rfh.readFline("C://JTerminals/de4Dd87d/CfgJ9rl/", "m4card.jrt", 1);
        } else {
            newCard = "ABCD1234";  //eight digits
            rfh.putfile("C://JTerminals/de4Dd87d/CfgJ9rl/", "m4card.jrt", newCard);
        }
        return newCard;
    }
    
    public String getMasterOwner4() throws IOException {
        RawFileHandler rfh = new RawFileHandler();

        String newCard = "";
        boolean foundfile = rfh.FindFileFolder("C://JTerminals/de4Dd87d/CfgJ9rl/", "m4card.jrt");
        if (foundfile == true) {
            newCard = rfh.readFline("C://JTerminals/de4Dd87d/CfgJ9rl/", "m4card.jrt", 2);
        }
        return newCard;
    }
}
