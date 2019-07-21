 package misc;

import gnu.io.CommPortIdentifier;
import gnu.io.SerialPort;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import org.apache.log4j.LogManager;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;

/**
 *
 * @author Angelo Dizon
 */
public class SBoardHandler {
    
    static Logger log = LogManager.getLogger(SBoardHandler.class.getName());

    /*   /dev/lp0     - Parallel Port   LINUX
     *   /dev/ttyS0   - Comm1 Port
     *   LPT1
     *   COM1
     */
    //String PrnPortType = "";
    private CommPortIdentifier portId = null;
    private SerialPort port = null;
    private OutputStream os = null;
    private BufferedOutputStream bos = null;

    public void openPort() {
        try {
            String portname = "";
            XMLreader xr = new XMLreader();
            portname = xr.getElementValue("C://JTerminals/initH.xml", "slots_port");
            portId = CommPortIdentifier.getPortIdentifier(portname);
            port = (SerialPort) portId.open("Slots Porting", 5700);
            port.setSerialPortParams(9600, SerialPort.DATABITS_8, SerialPort.STOPBITS_1, SerialPort.PARITY_NONE);
            os = port.getOutputStream();
            bos = new BufferedOutputStream(os);
        } catch (Exception ex) {
            this.closePort();
            log.error(ex.getMessage());
        }
    }

    public void closePort() {
        try {
            portId = null;
            //port.close();
            bos.close();
            port.close();
        } catch (Exception ex) {
            log.error(ex.getMessage());
        }
    }

    public void sendWELCOME() {

        try {
            this.openPort();
            byte STX = 0x02;
            byte ct = 0x35;
            byte id = 0x31;
            byte msgid = 0x30;
            byte d = 0x1B;//escape character   
            byte a = 0x61;
            byte three = 0x33;
            byte zero = 0x30;
            byte A = 0x41;
            byte B = 0x42;
            byte C = 0x43;
            byte D = 0x44;
            byte E = 0x45;
            byte F = 0x46;
            byte G = 0x47;
            byte H = 0x48;
            byte I = 0x49;
            byte J = 0x4A;
            byte K = 0x4B;
            byte L = 0x4C;
            byte M = 0x4D;
            byte N = 0x4E;
            byte O = 0x4F;
            byte P = 0x50;
            byte Q = 0x51;
            byte R = 0x52;
            byte S = 0x53;
            byte T = 0x54;
            byte U = 0x55;
            byte V = 0x56;
            byte W = 0x57;
            byte X = 0x58;
            byte Y = 0x59;
            byte Z = 0x5A;

            byte space = 0x20;

            byte BCC1 = 0x31;
            byte BCC2 = 0x38;
            byte ETX = 0x03;

            bos.write(STX);
            bos.write(ct);
            bos.write(id);
            bos.write(msgid);
            bos.write(d);
            bos.write(a);
            bos.write(three);
            bos.write(zero);
            bos.write(W);
            bos.write(E);
            bos.write(L);
            bos.write(C);
            bos.write(O);
            bos.write(M);
            bos.write(E);
            bos.write(space);
            bos.write(T);
            bos.write(O);
            bos.write(space);
            bos.write(G);
            bos.write(R);
            bos.write(E);
            bos.write(E);
            bos.write(N);
            bos.write(H);
            bos.write(I);
            bos.write(L);
            bos.write(L);
            bos.write(S);
            bos.write(space);
            bos.write(S);
            bos.write(H);
            bos.write(O);
            bos.write(P);
            bos.write(P);
            bos.write(I);
            bos.write(N);
            bos.write(G);
            bos.write(space);
            bos.write(C);
            bos.write(E);
            bos.write(N);
            bos.write(T);
            bos.write(E);
            bos.write(R);

            bos.write(BCC1);
            bos.write(BCC2);
            bos.write(ETX);
            this.closePort();
        } catch (Exception ex) {
            log.error(ex.getMessage());
            this.closePort();
        }

    }

    public String getmarqueeout(String ParkingArea) {
        String Mout = "";
        String Sout = "";
        try {
            RawFileHandler rfh = new RawFileHandler();
            if (rfh.FindFileFolder("/SYSTEMS/", "marquee" + ParkingArea + ".out") == true) {
                Sout = rfh.readFline("/SYSTEMS/", "marquee" + ParkingArea + ".out", 1);
                if (rfh.FindFileFolder("C://JTerminals/de4Dd87d/CfgJ9rl/", "marquee" + ParkingArea + ".out") == true) {
                    Mout = rfh.readFline("C://JTerminals/de4Dd87d/CfgJ9rl/", "marquee" + ParkingArea + ".out", 1);
                }
                if (Sout.equals(Mout) == false) {
                    rfh.copytoworkpath("/SYSTEMS/", "marquee" + ParkingArea + ".out");
                }
            }
            if (rfh.FindFileFolder("C://JTerminals/de4Dd87d/CfgJ9rl/", "marquee" + ParkingArea + ".out") == true) {
                Mout = rfh.readFline("C://JTerminals/de4Dd87d/CfgJ9rl/", "marquee" + ParkingArea + ".out", 1);
            }

        } catch (Exception ex) {
            log.error(ex.getMessage());
        }
        return Mout;
    }

    public void SendMarqueeText(String sendstr, int Speed) {
        LogUtility logthis = new LogUtility();
        XORstuff xor = new XORstuff();
        try {
            this.openPort();
            int STX = 0x02;
            int ct = 0x35;
            int id = 0x31;
            int msgid = 0x30;
            int d = 0x1B;//escape character   
            int a = 0x61;
            int three = 0x33;//SPEED
            if (Speed == 0x00) {
                three = 0x33;//SPEED
            } else {
                three = Speed;
            }
            int zero = 0x30;

            int ETX = 0x03;

            int BCC = 0x00;
            int BCC1 = 0x00;
            int BCC2 = 0x00;
            int BCC1Hex = 0x00;
            int BCC2Hex = 0x00;

            bos.write(STX);
            bos.write(ct);
            bos.write(id);
            bos.write(msgid);
            bos.write(d);
            bos.write(a);
            bos.write(three);
            bos.write(zero);

            StringBuilder sb = new StringBuilder(sendstr);
            if (three == 0x31) {
                BCC = 0x4D;
            } //if speed is 1 or 0x31
            else if (three == 0x32) {
                BCC = 0x4E;
            } //if speed is 2 or 0x34
            else if (three == 0x33) {
                BCC = 0x4F;
            } //if speed is 3 or 0x33
            else if (three == 0x34) {
                BCC = 0x48;
            } //if speed is 4 or 0x34
            else if (three == 0x35) {
                BCC = 0x49;
            } //if speed is 5 or 0x35
            for (int i = 0; i < sb.length(); i++) {
                char chr = sb.charAt(i);
                //System.out.print(chr + ": ");
                int j = (int) chr;
                bos.write(j);
                //log.info(j);

                BCC = xor.XOR2integers2Integer(BCC, j);
            }
            String StrDecBCC = String.valueOf(BCC);
            String StrBCCHex = xor.DecToHexString(StrDecBCC);
            if (StrBCCHex.length() < 1) {
                String BCCString = StrBCCHex;
                BCC1Hex = 0;
                BCC2Hex = xor.StrtoHexInt(BCCString.substring(0, 1));
            } else {
                String BCCStr = StrBCCHex;
                //BCC1Hex = 0x33;
                BCC1Hex = xor.StrtoHexInt(BCCStr.substring(0, 1));
                //BCC1 = xor.converthex2int(BCC1);
                //BCC2 = Integer.parseInt(BCCStr.substring(1, 2));
                //BCC2Hex = 0x43;
                BCC2Hex = xor.StrtoHexInt(BCCStr.substring(1, 2));
                //BCC2 = xor.converthex2int(BCC2);
            }
            bos.write(BCC1Hex);
            bos.write(BCC2Hex);
            bos.write(ETX);
            this.closePort();
        } catch (Exception ex) {
            logthis.setLog("SLOTPROB", ex.toString());
            this.closePort();
        }
    }

    public void sendFULL() {
        try {
            this.openPort();
            byte STX = 0x02;
            byte ct = 0x30;
            byte id = 0x31;
            byte msgid1 = 0x37;
            byte msgid2 = 0x46;
            byte msgid3 = 0x55;
            byte msgid4 = 0x4C;
            byte msgid5 = 0x4C;

            byte BCC1 = 0x32;
            byte BCC2 = 0x37;
            byte ETX = 0x03;

            bos.write(STX);
            bos.write(ct);
            bos.write(id);
            bos.write(msgid1);
            bos.write(msgid2);
            bos.write(msgid3);
            bos.write(msgid4);
            bos.write(msgid5);

            bos.write(BCC1);
            bos.write(BCC2);
            bos.write(ETX);
            this.closePort();
        } catch (Exception ex) {
            log.error(ex.getMessage());
        }
    }

    public void sendOPEN() {

        try {
            this.openPort();
            byte STX = 0x02;
            byte ct = 0x30;
            byte id = 0x31;
            byte msgid1 = 0x34;
            byte msgid2 = 0x4F;
            byte msgid3 = 0x50;
            byte msgid4 = 0x45;
            byte msgid5 = 0x4E;

            byte BCC1 = 0x32;
            byte BCC2 = 0x33;
            byte ETX = 0x03;

            bos.write(STX);
            bos.write(ct);
            bos.write(id);
            bos.write(msgid1);
            bos.write(msgid2);
            bos.write(msgid3);
            bos.write(msgid4);
            bos.write(msgid5);

            bos.write(BCC1);
            bos.write(BCC2);
            bos.write(ETX);
            this.closePort();
        } catch (Exception ex) {
            this.closePort();
            log.error(ex.getMessage());
        }

    }

    private void send2DigitController(int STX, int DevID1, int DevID2, int brightlight, int thousands, int hundreds, int tens, int ones, int BCC1Hex, int BCC2Hex, int ETX) {

        try {
            this.openPort();
            bos.write(STX);
            bos.write(DevID1);
            bos.write(DevID2);

            bos.write(brightlight);

            bos.write(thousands);
            bos.write(hundreds);
            bos.write(tens);
            bos.write(ones);

            bos.write(BCC1Hex);
            bos.write(BCC2Hex);
            bos.write(ETX);
            this.closePort();
        } catch (Exception ex) {
            log.error(ex.getMessage());
        }
    }

    public void sendAVnum2BoardTest() {
        try {
            int STX = 0x02;
            int DevID1 = 0x30;
            int DevID2 = 0x31;
            int brightlight = 0x34;
            int thousands = 0x31;
            int hundreds = 0x33;
            int tens = 0x31;
            int ones = 0x32;
            int BCC = 0x36;
            log.info("Hex Manual: " + BCC);
            int BCC1Hex = 0x33;
            int BCC2Hex = 0x36;
            int ETX = 0x03;
            bos.write(STX);
            bos.write(DevID1);
            bos.write(DevID2);
            bos.write(brightlight);
            bos.write(thousands);
            bos.write(hundreds);
            bos.write(tens);
            bos.write(ones);
            bos.write(BCC1Hex);
            bos.write(BCC2Hex);
            bos.write(ETX);

        } catch (Exception ex) {
            log.error(ex.getMessage());
        }
    }

    public void sendAVnum2Board(int brightness, int AVSLOT1, int AVSLOT2, int AVSLOT3, int AVSLOT4) {

        int STX = 0x02;
        int DevID1 = 0x30;
        int DevID2 = 0x31;
        int brightlight = 0x00;
        int thousands = 0x00;
        int hundreds = 0x00;
        int tens = 0x00;
        int ones = 0x00;
        int BCC = 0x00;
        int BCC1Hex = 0x00;
        int BCC2Hex = 0x00;
        int ETX = 0x03;

        switch (brightness) {
            case 1:
                brightlight = 0x31;
                break;
            case 2:
                brightlight = 0x32;
                break;
            case 3:
                brightlight = 0x33;
                break;
            case 4:
                brightlight = 0x34;
                break;
            case 5:
                brightlight = 0x35;
                break;
            case 6:
                brightlight = 0x36;
                break;
            case 7:
                brightlight = 0x37;
                break;
            //Rem: This converts Hex to Decimal
        }
        switch (AVSLOT1) {
            case 1:
                thousands = 0x31;
                break;
            case 2:
                thousands = 0x32;
                break;
            case 3:
                thousands = 0x33;
                break;
            case 4:
                thousands = 0x34;
                break;
            case 5:
                thousands = 0x35;
                break;
            case 6:
                thousands = 0x36;
                break;
            case 7:
                thousands = 0x37;
                break;
            case 8:
                thousands = 0x38;
                break;
            case 9:
                thousands = 0x39;
                break;
            case 0:
                thousands = 0x30;
                break;
        }
        switch (AVSLOT2) {
            case 1:
                hundreds = 0x31;
                break;
            case 2:
                hundreds = 0x32;
                break;
            case 3:
                hundreds = 0x33;
                break;
            case 4:
                hundreds = 0x34;
                break;
            case 5:
                hundreds = 0x35;
                break;
            case 6:
                hundreds = 0x36;
                break;
            case 7:
                hundreds = 0x37;
                break;
            case 8:
                hundreds = 0x38;
                break;
            case 9:
                hundreds = 0x39;
                break;
            case 0:
                hundreds = 0x30;
                break;
        }
        switch (AVSLOT3) {
            case 1:
                tens = 0x31;
                break;
            case 2:
                tens = 0x32;
                break;
            case 3:
                tens = 0x33;
                break;
            case 4:
                tens = 0x34;
                break;
            case 5:
                tens = 0x35;
                break;
            case 6:
                tens = 0x36;
                break;
            case 7:
                tens = 0x37;
                break;
            case 8:
                tens = 0x38;
                break;
            case 9:
                tens = 0x39;
                break;
            case 0:
                tens = 0x30;
                break;
        }
        switch (AVSLOT4) {
            case 1:
                ones = 0x31;
                break;
            case 2:
                ones = 0x32;
                break;
            case 3:
                ones = 0x33;
                break;
            case 4:
                ones = 0x34;
                break;
            case 5:
                ones = 0x35;
                break;
            case 6:
                ones = 0x36;
                break;
            case 7:
                ones = 0x37;
                break;
            case 8:
                ones = 0x38;
                break;
            case 9:
                ones = 0x39;
                break;
            case 0:
                ones = 0x30;
                break;
        }
        XORstuff xor = new XORstuff();
        //Compute in Decimal start with 0x00 == 48
        BCC = 0x03;
        BCC = xor.XOR2integers2Integer(BCC, brightlight);
        BCC = xor.XOR2integers2Integer(BCC, thousands);
        BCC = xor.XOR2integers2Integer(BCC, hundreds);
        BCC = xor.XOR2integers2Integer(BCC, tens);
        BCC = xor.XOR2integers2Integer(BCC, ones);
        String StrDecBCC = String.valueOf(BCC);
        String StrBCCHex = xor.DecToHexString(StrDecBCC);
        if (StrBCCHex.length() < 1) {
            String BCCString = StrBCCHex;
            BCC1Hex = 0;
            BCC2Hex = xor.StrtoHexInt(BCCString.substring(0, 1));
        } else {
            String BCCStr = StrBCCHex;
            //BCC1Hex = 0x33;
            BCC1Hex = xor.StrtoHexInt(BCCStr.substring(0, 1));
            //BCC1 = xor.converthex2int(BCC1);
            //BCC2 = Integer.parseInt(BCCStr.substring(1, 2));
            //BCC2Hex = 0x43;
            BCC2Hex = xor.StrtoHexInt(BCCStr.substring(1, 2));
            //BCC2 = xor.converthex2int(BCC2);
        }
//        switch (BCC1)
//        {        
//            case 1 : BCC1Hex = 0x31;break;
//            case 2 : BCC1Hex = 0x32;break;
//            case 3 : BCC1Hex = 0x33;break;
//            case 4 : BCC1Hex = 0x34;break;
//            case 5 : BCC1Hex = 0x35;break;
//            case 6 : BCC1Hex = 0x36;break;
//            case 7 : BCC1Hex = 0x37;break;
//            case 8 : BCC1Hex = 0x38;break;
//            case 9 : BCC1Hex = 0x39;break;
//            case 'A': BCC1Hex = 0x40;break;
//            case 'B': BCC1Hex = 0x41;break;
//            case 'C': BCC1Hex = 0x42;break;            
//            case 0 : BCC1Hex = 0x30;break;
//        }
//        switch (BCC2)
//        {        
//            case 1 : BCC2Hex = 0x31;break;
//            case 2 : BCC2Hex = 0x32;break;
//            case 3 : BCC2Hex = 0x33;break;
//            case 4 : BCC2Hex = 0x34;break;
//            case 5 : BCC2Hex = 0x35;break;
//            case 6 : BCC2Hex = 0x36;break;
//            case 7 : BCC2Hex = 0x37;break;
//            case 8 : BCC2Hex = 0x38;break;
//            case 9 : BCC2Hex = 0x39;break;
//            case 'A': BCC1Hex = 0x40;break;
//            case 'B': BCC1Hex = 0x41;break;
//            case 'C': BCC1Hex = 0x42;break;
//            case 0 : BCC2Hex = 0x30;break;
//        }
        send2DigitController(STX, DevID1, DevID2, brightlight, thousands, hundreds, tens, ones, BCC1Hex, BCC2Hex, ETX);
    }

    public static void main(String[] args) {
        try {
            SBoardHandler sbh = new SBoardHandler();
            //sbh.openPort();
            //sbh.sendWELCOME();
            sbh.sendOPEN();
            //sbh.sendAVnum2BoardTest();
            //sbh.sendAVnum2Board(1, 0, 7, 3, 9);
            //sbh.SendMarqueeText("Blanks", 0x35);
            //sbh.closePort();

//        int ITX = 0x5F;
//        int NTX = 0x3A;
//        int ndTX = 0xF4;
//        XORstuff xs = new XORstuff();
//        String Firstno = xs.XOR2integers2String(ITX,NTX);
//        int FirstInt = xs.XOR2integers2Integer(ITX,NTX);        
//        String Secondno = xs.XOR2integers2String(FirstInt,ndTX);
//        
//        log.info("XOR: ITX+NTX = "+Firstno+ " in Decimal Form");
//        log.info("Hex of ITX+NTX is:"+xs.DecToHexString(Firstno));
//        
//        log.info("XOR: First with ndTX = "+Secondno+ " in Decimal Form");        
//        log.info("Hex of First with ndTX is:"+xs.DecToHexString(Secondno));
        } catch (Exception ex) {
            log.error(ex.getMessage());
        }
    }
}
