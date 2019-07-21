/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package api;

import org.apache.log4j.LogManager;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import misc.RawFileHandler;
import misc.XMLreader;

/**
 *
 * @author Administrator
 */
public class SpecialParkersAPI {
    
    static Logger log = LogManager.getLogger(SpecialParkersAPI.class.getName());

    XMLreader xr = new XMLreader();
    RawFileHandler rfh = new RawFileHandler();
    String MppData[] = new String[4];

    public boolean checkVIPPlate(String card2check) {
        int z = 1;
        String vipData[] = new String[4];
        String VipParker1 = "";
        String date = "";
        String crd1 = "";
        String vip1 = "";
        String vip2 = "";
        try {
            //String plate1 = xr.getAttributeValue("vip.xml", "A101000142", "plate1");
            String sort = card2check.substring(0, 1);
            if (rfh.FindFileFolder("C://JTerminals/de4Dd87d/CfgJ9rl/", "vip.rec")) {
                int loop = rfh.getTotalFLines("C://JTerminals/de4Dd87d/CfgJ9rl/", "vip.rec");

                while (z < loop + 1) {
                    VipParker1 = rfh.readFline("C://JTerminals/de4Dd87d/CfgJ9rl/", "vip.rec", z);
                    vipData = VipParker1.split(",");
                    crd1 = vipData[0].trim();
                    vip1 = vipData[1].trim();
                    vip2 = vipData[2].trim();

                    if (card2check.compareToIgnoreCase(vip1) == 0) {
                        date = vipData[3];
                        return true;
                    }
                    if (card2check.compareToIgnoreCase(vip2) == 0) {
                        date = vipData[3];
                        return true;
                    }
                    if (card2check.compareToIgnoreCase(crd1) == 0) {
                        date = vipData[3];
                        return true;
                    }
                    z++;
                }
            }
        } catch (Exception ex) {
            log.error(ex.getMessage());
        }
        return false;
    }

    public String checkVIPCard(String card2check) {
        String VipCardParker1 = "";
        try {
            //String plate1 = xr.getAttributeValue("vip.xml", "A101000142", "plate1");
            VipCardParker1 = xr.getElementValue("C://JTerminals/vip.xml", card2check);
        } catch (Exception ex) {
            log.error(ex.getMessage());
        }
        return VipCardParker1;
    }

    public String checkMPPCrdPlt(String card2check) {
        int z = 1;
        String VipParker1 = "";
        String date = "";
        String crd1 = "";
        String mpp1 = "";
        String mpp2 = "";
        try {
            //String plate1 = xr.getAttributeValue("vip.xml", "A101000142", "plate1");
            String sort = card2check.substring(0, 1);
            if (rfh.FindFileFolder("C://JTerminals/", sort + "MPP.REC")) {
                int loop = rfh.getTotalFLines("C://JTerminals/", sort + "MPP.REC");

                while (z < loop + 1) {
                    VipParker1 = rfh.readFline("C://JTerminals/", sort + "MPP.REC", z);
                    MppData = VipParker1.split(",");
                    crd1 = MppData[0].trim();
                    mpp1 = MppData[1].trim();
                    mpp2 = MppData[2].trim();

                    if (card2check.compareToIgnoreCase(mpp1) == 0) {
                        date = MppData[3];
                        return date;
                    }
                    if (card2check.compareToIgnoreCase(mpp2) == 0) {
                        date = MppData[3];
                        return date;
                    }
                    if (card2check.compareToIgnoreCase(crd1) == 0) {
                        date = MppData[3];
                        return date;
                    }
                    z++;
                }

            }

        } catch (Exception ex) {
            log.error(ex.getMessage());
        }
        return date;
    }

    public boolean check2ndMPPCrdPlt(String card2check) {
        String VipParker1 = "";
        String date = "";
        String crd1 = "";
        String mpp1 = "";
        String mpp2 = "";
        int loop = 0;
        try {
            //String plate1 = xr.getAttributeValue("vip.xml", "A101000142", "plate1");
            String sort = card2check.substring(0, 1);
            if (rfh.FindFileFolder("C://JTerminals/", sort + "MPP.REC") == true) {
                loop = rfh.getTotalFLines("C://JTerminals/", sort + "MPP.REC");

                int y = 1;

                while (y < loop + 1) {
                    VipParker1 = rfh.readFline("C://JTerminals/", sort + "MPP.REC", y);
                    MppData = VipParker1.split(",");
                    crd1 = MppData[0].trim();
                    mpp1 = MppData[1].trim();
                    mpp2 = MppData[2].trim();
                    //check first plate first
                    //then find 2nd plate number 
                    if (card2check.compareToIgnoreCase(mpp1) == 0) {
                        boolean check2 = check2ndMPPplate(mpp2);
                        return check2;
                    }
                    if (card2check.compareToIgnoreCase(mpp2) == 0) {
                        boolean check2 = check2ndMPPplate(mpp1);
                        return check2;
                    }
                    y++;
                }

            }
        } catch (Exception ex) {
            log.error(ex.getMessage());
        }
        return false;
    }

    public boolean checkRunAwayPlate(String plate2check) {
        if (plate2check.compareToIgnoreCase("RUN123") == 0) {
            return true;
        }
        return false;
    }

    public boolean check2ndMPPplate(String plate2) throws Exception {
        plate2 = plate2 + ".plt";
        if (rfh.FindFileFolder("/SYSTEMS/", plate2) == true) {
            String details2ndplate = rfh.readFline("/SYSTEMS/", plate2, 1);
            return true;
        }
        return false;
    }

    public boolean checkTicket(String couponNO) {
        try {
            int lines2check = rfh.getTotalFLines("C://JTerminals/de4Dd87d/CfgJ9rl/", "couponf.rec");
            int Startint = 0;
            int Endint = 0;
            int Couponint = 0;
            String procline = "";
            for (int x = 1; x <= lines2check; x++) {
                if (rfh.FindFileFolder("C://JTerminals/de4Dd87d/CfgJ9rl/", "couponf.rec") == true) {
                    procline = rfh.readFline("C://JTerminals/de4Dd87d/CfgJ9rl/", "couponf.rec", x);
                    Startint = Integer.parseInt(procline.substring(0, 6));
                    Endint = Integer.parseInt(procline.substring(7, 13));
                }
                Couponint = Integer.parseInt(couponNO);
                if (Startint <= Couponint && Couponint <= Endint) {
                    return true;
                }
            }
        } catch (Exception ex) {
            log.error(ex.getMessage());
        }
        return false;
    }

    public static void main(String[] args) {
        SpecialParkersAPI SP = new SpecialParkersAPI();

    }
}
