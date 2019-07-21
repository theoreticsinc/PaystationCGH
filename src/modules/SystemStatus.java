/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package modules;

import api.ParkersAPI;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import org.apache.log4j.LogManager;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import misc.RawFileHandler;

/**
 *
 * @author Administrator Combine Exit with Entrance - must have ping and online
 * checking
 *
 */
public class SystemStatus {

    RawFileHandler rfh = new RawFileHandler();

    static Logger log = LogManager.getLogger(SystemStatus.class.getName());

    public boolean checkPING(String ip) {
        //log.info(inputLine);        
        boolean status;
        try {
            String pingCmd = "ping " + ip + "";

            Runtime r = Runtime.getRuntime();
            Process p = r.exec(pingCmd);

            BufferedReader in = new BufferedReader(new InputStreamReader(p.getInputStream()));
            String inputLine;
            inputLine = in.readLine(); //LINUX ONLY - needs the second line as the result of the ping
            //log.info(inputLine);
            inputLine = in.readLine();
            inputLine = in.readLine();
            //log.info(inputLine);
            if (inputLine != null) {
                //log.info(inputLine);
                if (inputLine.compareTo("") == 0) {
                    //log.info(inputLine);
                    //log.info("Offline");
                    in.close();
                    status = false;
                    return status;
                } else if (inputLine != null && inputLine.contains("unreachable")) {
                    //log.info(inputLine);
                    //log.info("Offline");
                    in.close();
                    status = false;
                    return status;

                } else if (inputLine != null && inputLine.compareToIgnoreCase("Request timed out.") == 0) {
                    //log.info(inputLine);
                    //log.info("Offline");
                    in.close();
                    status = false;
                    return status;
                } else if (inputLine != null && inputLine.substring(0, 1).compareToIgnoreCase("-") == 0) {
                    //log.info(inputLine);
                    //log.info("Offline");
                    in.close();
                    status = false;
                    return status;
                }
                //log.info("Online");
                //log.info(inputLine);
                in.close();
                status = true;
                return status;
            }
            in.close();
            status = false;
            return status;
        } catch (Exception ex) {
            log.error(ex.getMessage());
        }
        return false;

    }

    public boolean checkOnline() {
        //boolean found = rfh.FindFileFolder("/SYSTEMS/", "online.aaa");
        return true;
    }

    public void deleteOfflineCRDPLT(String serverIP) {
        File pathName = new File("C://JTerminals/Outline/");
        String[] fileNames = pathName.list();
        ParkersAPI SP = new ParkersAPI();
        String threestrikes, newthreestrikes;
        int strikes3 = 0;
        String cardfileno;
        for (int i = 0; i < fileNames.length; i++) {
            try {
                cardfileno = fileNames[i].substring(0, fileNames[i].lastIndexOf('.'));
                threestrikes = rfh.readFline("C://JTerminals/Outline/", fileNames[i], 1);
                strikes3 = Integer.parseInt(threestrikes) + 1;
                newthreestrikes = String.valueOf(strikes3);
                if (SP.delParkerCRDPLT(cardfileno, serverIP) == true) {
                    rfh.deleteFile("C://JTerminals/Outline/", fileNames[i]);
                } else if (strikes3 < 100) {
                    rfh.putfile("C://JTerminals/Outline/", fileNames[i], newthreestrikes);
                } else if (strikes3 >= 100) {
                    rfh.deleteFile("C://JTerminals/Outline/", fileNames[i]);
                    rfh.putfile("/opt/Errors/", fileNames[i] + ".err", "Error");
                }
            } catch (Exception ex) {
                log.debug(ex);
                rfh.deleteFile("C://JTerminals/Outline/", fileNames[i]);
                rfh.putfile("/opt/Errors/", fileNames[i] + ".err", "Error");
            }
        }

    }

    public void updateCoupons(String serverIP) {
        try {
            File pathName = new File("C://JTerminals/Coupons/");
            String[] fileNames = pathName.list();
            ParkersAPI SP = new ParkersAPI();
            String cardfileno;
            for (int i = 0; i < fileNames.length; i++) {
                try {
                    cardfileno = fileNames[i];
                    rfh.appendfile("/SYSTEMS/", "usedcoup.rec", cardfileno);
                    rfh.deleteFile("C://JTerminals/Coupons/", fileNames[i]);
                } catch (IOException ex) {
                    log.debug(ex);
                }
            }

            //LINUX
            //Process s = Runtime.getRuntime().exec("cp /SYSTEMS/usedcoup.rec /JTerminals/ &");           
//            try {
            //Process s = Runtime.getRuntime().exec("cp /SYSTEMS/usedcoup.rec /JTerminals/");
            //s.waitFor();
            if (rfh.FindFileFolder("/SYSTEMS/couponf.rec") == true) {
                rfh.copySource2Dest("/SYSTEMS/couponf.rec", "C://JTerminals/couponf.rec");
            }
//            } catch (InterruptedException ex) {
//                LogManager.getLogger(SystemStatus.class.getName()).log(Level.SEVERE, null, ex);
//            }
        } catch (Exception ex) {
            log.debug(ex);
        }
//            } catch (InterruptedException ex) {
//                LogManager.getLogger(SystemStatus.class.getName()).log(Level.SEVERE, null, ex);
//            }
    }

    public void updateServerCRDPLT() {
        try {
            File pathName = new File("/Offline/");
            String[] fileNames = pathName.list();
            String ext = null;
            for (int i = 0; i < fileNames.length; i++) {
                File tf = new File(pathName.getPath(), fileNames[i]);
                ext = fileNames[i].substring(fileNames[i].lastIndexOf('.') + 1, fileNames[i].length());

                if (tf.isDirectory() == false) {
                    // log.info(tf.getCanonicalPath());
                    if (ext.compareToIgnoreCase("CRD") == 0) {
                        if (rfh.copy2server("/Offline/", fileNames[i]) == true) {
                            rfh.deleteFile("/Offline/", fileNames[i]);
                        }
                        log.info(fileNames[i] + "   :: EXT=" + ext);
                    } else if (ext.compareToIgnoreCase("PLT") == 0) {
                        if (rfh.copy2server("/Offline/", fileNames[i]) == true) {
                            rfh.deleteFile("/Offline/", fileNames[i]);
                        }
                        log.info(fileNames[i] + "   :: EXT=" + ext);
                    } else //if (ext.compareToIgnoreCase("008") == 0) 
                    {
                        if (rfh.copy2server("/Offline/", fileNames[i]) == true) {
                            rfh.deleteFile("/Offline/", fileNames[i]);
                        }
                        log.info(fileNames[i] + "   :: EXT=" + ext);
                    }
                }
            }
        } catch (Exception ex) {
            log.debug(ex);
        }

    }

    public static void main(String args[]) throws Exception {
        SystemStatus ss = new SystemStatus();
        //log.info(ss.checkOnline());

        //ss.updateServerCRDPLT();
    }
}
