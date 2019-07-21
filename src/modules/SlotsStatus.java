/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package modules;

import java.io.IOException;
import java.util.Date;
import org.apache.log4j.LogManager;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import misc.RawFileHandler;
import misc.SBoardHandler;
import misc.XMLreader;

/**
 *
 * @author root
 */
public class SlotsStatus {

    XMLreader xr = new XMLreader();
    RawFileHandler rfh = new RawFileHandler();
    
    static Logger log = LogManager.getLogger(SlotsStatus.class.getName());

    public String getENSERVdata(int i) {
        String[] ENresets = loadENslots();
        if (rfh.FindFileFolder("C://JTerminals/", ENresets[i])) {
            return rfh.readFline("C://JTerminals/", ENresets[i], 1);
        }
        return ENresets[i];
    }

    public String getEXSERVdata(int i) {
        String[] EXresets = loadEXslots();
        if (rfh.FindFileFolder("C://JTerminals/", EXresets[i])) {
            return rfh.readFline("C://JTerminals/", EXresets[i], 1);
        }
        return EXresets[i];
    }

    public void StartResetAllSlots() {
        String[] ENresets = loadENslots();
        String[] EXresets = loadEXslots();
        for (int i = 0; i < ENresets.length; i++) {
            rfh.putfile("C://JTerminals/", ENresets[i].toUpperCase(), "0");
            rfh.putfile("C://JTerminals/", ENresets[i].toLowerCase(), "0");
            if (rfh.FindFileFolder("/SYSTEMS/", "online.aaa") == true) {
                rfh.putfile("/SYSTEMS/", ENresets[i].toUpperCase(), "0");
                rfh.putfile("/SYSTEMS/", ENresets[i].toLowerCase(), "0");
            }
            ENresets[i] = ENresets[i].subSequence(0, 4) + "REST.NOS";
            rfh.putfile("/opt/", ENresets[i], "blackout");
            try {
                if (rfh.FindFileFolder("/SYSTEMS/", "online.aaa") == true) {
                    rfh.copy2server("/opt/", ENresets[i]);
                }
            } catch (Exception ex) {
                log.error(ex.getMessage());
            }
        }
        for (int i = 0; i < EXresets.length; i++) {
            rfh.putfile("C://JTerminals/", EXresets[i].toUpperCase(), "0");
            rfh.putfile("C://JTerminals/", EXresets[i].toLowerCase(), "0");
            if (rfh.FindFileFolder("/SYSTEMS/", "online.aaa") == true) {
                rfh.putfile("/SYSTEMS/", EXresets[i].toUpperCase(), "0");
                rfh.putfile("/SYSTEMS/", EXresets[i].toLowerCase(), "0");
            }
            EXresets[i] = EXresets[i].subSequence(0, 4) + "REST.NOS";
            rfh.putfile("/opt/", EXresets[i], "blackout");
            try {
                if (rfh.FindFileFolder("/SYSTEMS/", "online.aaa") == true) {
                    rfh.copy2server("/opt/", EXresets[i]);
                }
            } catch (Exception ex) {
                log.error(ex.getMessage());
            }
        }

    }

    public boolean checkOnline() {
        boolean found = rfh.FindFileFolder("/SYSTEMS/", "online.aaa");
        return found;
    }

    public void checkReset(String SentinelID) {
        try {
            boolean found = rfh.FindFileFolder("/SYSTEMS/", SentinelID + "REST.NOS");
            if (found == true) {
                this.ResetCarSlots(SentinelID);
                if (checkOnline() == true) {
                    rfh.putfile("/SYSTEMS/", SentinelID + "CLR.FIX", new Date().toString());
                    rfh.deleteFile("/SYSTEMS/", SentinelID + "REST.NOS");
                }
            }
        } catch (Exception ex) {
            log.error(ex.getMessage());
        }
    }

    public void ResetCarSlots(String SentinelID) throws IOException {
        rfh.putfile("/SUBSYSTEMS/", SentinelID + "SERV.SER", "0");
        if (checkOnline() == true) {
            rfh.copySource2Dest("/SUBSYSTEMS/" + SentinelID + "SERV.SER", "/SYSTEMS/" + SentinelID + "SERV.SER");
        }
    }

    public short getENslots() {
        short noofentry = 0;
        try {
            String entryslots = xr.getElementValue("C://JTerminals/initH.xml", "entry");
            noofentry = Short.parseShort(entryslots);
        } catch (Exception ex) {
            log.error(ex.getMessage());
        }
        return noofentry;
    }

    public short getEXslots() {
        short noofexit = 0;
        try {
            String exitslots = xr.getElementValue("C://JTerminals/initH.xml", "exit");
            noofexit = Short.parseShort(exitslots);
        } catch (Exception ex) {
            log.error(ex.getMessage());
        }
        return noofexit;
    }

    public String[] loadENslots() {
        String entryslots;
        short noofentry;
        String[] computeEntry = null;
        try {
            entryslots = xr.getElementValue("C://JTerminals/initH.xml", "entry");

            noofentry = Short.parseShort(entryslots);

            computeEntry = new String[noofentry];

            for (int i = 0; i < noofentry; i++) {
                short index = (short) (i + 1);
                computeEntry[i] = xr.getAttributeValue("C://JTerminals/initH.xml", "entry", "n" + index);
            }

        } catch (Exception ex) {
            log.error(ex.getMessage());
        }
        return computeEntry;
    }

    public String[] loadEXslots() {
        String exitslots;
        short noofexit;
        String[] computeExit = null;
        try {
            exitslots = xr.getElementValue("C://JTerminals/initH.xml", "exit");

            noofexit = Short.parseShort(exitslots);

            computeExit = new String[noofexit];

            for (int i = 0; i < noofexit; i++) {
                short index = (short) (i + 1);
                computeExit[i] = xr.getAttributeValue("C://JTerminals/initH.xml", "exit", "x" + index);
            }

        } catch (Exception ex) {
            log.error(ex.getMessage());
        }
        return computeExit;
    }

    public String checkAvailableSlots(String SlotsID) {
        SBoardHandler sbh = new SBoardHandler();
        String Available = "";
        long AvailableSLOTS = 0;
        int AVSLOT1 = 0;
        int AVSLOT2 = 0;
        int AVSLOT3 = 0;
        int AVSLOT4 = 0;
        try {
            if (rfh.FindFileFolder("/SYSTEMS/", SlotsID + ".NOS") == true) {
                rfh.copytoworkpath("/SYSTEMS/", SlotsID + ".NOS");
            }
            if (rfh.FindFileFolder("C://JTerminals/", SlotsID + ".NOS") == true) {
                Available = rfh.readFline("C://JTerminals/", SlotsID + ".NOS", 1);
                //Here Entrances must send binary output to the Display Boards
                AvailableSLOTS = Long.parseLong(Available);

                if (AvailableSLOTS <= 0) {
                    sbh.sendFULL();
                } else {
                    if (Available.length() == 4) {
                        AVSLOT1 = Integer.parseInt(Available.substring(0, 1));
                        AVSLOT2 = Integer.parseInt(Available.substring(1, 2));
                        AVSLOT3 = Integer.parseInt(Available.substring(2, 3));
                        AVSLOT4 = Integer.parseInt(Available.substring(3, 4));
                    } else if (Available.length() == 3) {
                        AVSLOT1 = Integer.parseInt("0");
                        AVSLOT2 = Integer.parseInt(Available.substring(0, 1));
                        AVSLOT3 = Integer.parseInt(Available.substring(1, 2));
                        AVSLOT4 = Integer.parseInt(Available.substring(2, 3));
                    } else if (Available.length() == 2) {
                        AVSLOT1 = Integer.parseInt("0");
                        AVSLOT2 = Integer.parseInt("0");
                        AVSLOT3 = Integer.parseInt(Available.substring(0, 1));
                        AVSLOT4 = Integer.parseInt(Available.substring(1, 2));
                    } else if (Available.length() == 1) {
                        AVSLOT1 = Integer.parseInt("0");
                        AVSLOT2 = Integer.parseInt("0");
                        AVSLOT3 = Integer.parseInt("0");
                        AVSLOT4 = Integer.parseInt(Available.substring(0, 1));
                    }
                    sbh.sendAVnum2Board(3, AVSLOT1, AVSLOT2, AVSLOT3, AVSLOT4);
                }
                sbh.closePort();

            }
        } catch (Exception ex) {
            sbh.closePort();
            log.error(ex.getMessage());
        }
        return Available;
    }

    public String computeSlots(String slotsmode, String SlotsID, String ParkingArea, String[] computeEntry, String[] computeExit, short numofentrances, short numofexits) {
        String Available = "";
        String OSlots = "";
        String Entrances = "";
        String Exits = "";
        String Override = "";
        boolean copyserver = false;
        long dOSlots = 0;
        long dEntrances[];
        long dExits[];
        long tEntrances = 0;
        long tExits = 0;
        long dOverride = 0;
        long dAvailable = 0;

        dEntrances = new long[numofentrances];
        dExits = new long[numofexits];

        if (slotsmode.compareToIgnoreCase("STANDALONE") == 0) {
            copyserver = false;
        } else {
            copyserver = true;
        }
//////////////////SEND TO MEMORY///////////////////////////////////////////////
        try {
            if (copyserver == true) {
                for (int i = 0; i < dEntrances.length; i++) {
                    if (rfh.FindFileFolder("/SYSTEMS/", computeEntry[i]) == true) {
                        rfh.copytoworkpath("/SYSTEMS/", computeEntry[i]);
                    }
                }
                for (int i = 0; i < dExits.length; i++) {
                    if (rfh.FindFileFolder("/SYSTEMS/", computeExit[i]) == true) {
                        rfh.copytoworkpath("/SYSTEMS/", computeExit[i]);
                    }
                }
                if (rfh.FindFileFolder("/SYSTEMS/", ParkingArea + "ORIG.SER") == true) {
                    rfh.copytoworkpath("/SYSTEMS/", ParkingArea + "ORIG.SER");
                }
                if (rfh.FindFileFolder("/SYSTEMS/", ParkingArea + "OVRD.SER") == true) {
                    rfh.copytoworkpath("/SYSTEMS/", ParkingArea + "OVRD.SER");
                }
            }
        } catch (Exception ex) {
            log.error(ex.getMessage());
        }
        ////////////////Local Files///////////////////////
        try {
            for (int i = 0; i < dEntrances.length; i++) {
                if (rfh.FindFileFolder("C://JTerminals/", computeEntry[i]) == true) {
                    Entrances = rfh.readFline("C://JTerminals/", computeEntry[i], 1);
                    dEntrances[i] = Long.parseLong(Entrances);
                }
            }
            //---------------------------------//                
            for (int i = 0; i < dExits.length; i++) {
                if (rfh.FindFileFolder("C://JTerminals/", computeExit[i]) == true) {
                    Exits = rfh.readFline("C://JTerminals/", computeExit[i], 1);
                    dExits[i] = Long.parseLong(Exits);
                }
            }
        } catch (Exception ex) {
            log.error(ex.getMessage());
        }
        /////////////////////////////////////////////////////////////////            
        try {
            if (rfh.FindFileFolder("C://JTerminals/", ParkingArea + "ORIG.SER") == true) {
                OSlots = rfh.readFline("C://JTerminals/", ParkingArea + "ORIG.SER", 1);

                dOSlots = Long.parseLong(OSlots);
            }
            if (rfh.FindFileFolder("C://JTerminals/", ParkingArea + "OVRD.SER") == true) {
                Override = rfh.readFline("C://JTerminals/", ParkingArea + "OVRD.SER", 1);
                dOverride = Long.parseLong(Override);
            }
        } catch (Exception ex) {
            log.error(ex.getMessage());
        }
        //COMPUTE HERE        
        try {
            for (int i = 0; i < dEntrances.length; i++) {
                tEntrances = tEntrances + dEntrances[i];
            }
            for (int i = 0; i < dExits.length; i++) {
                tExits = tExits + dExits[i];
            }

            dAvailable = dOSlots + tExits - tEntrances + dOverride;
            //DONE

            Available = String.valueOf(dAvailable);
            rfh.putfile("C://JTerminals/", SlotsID + ".NOS", Available);
            rfh.copytoserver("C://JTerminals/", SlotsID + ".NOS");
        } catch (Exception ex) {
            log.error(ex.getMessage());
        }
        return Available;
    }

}
