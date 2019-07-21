/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package misc;

import org.apache.log4j.LogManager;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;

/**
 *
 * @author Angelo
 */
public class XORstuff {
    
    static Logger log = LogManager.getLogger(XORstuff.class.getName());
    
    public String XOR2integers2String(int STX, int ETX) {//Decimal values
        int XOR = STX ^ ETX;
        return String.valueOf(XOR);
    }

    public int XOR2integers2Integer(int STX, int ETX) {//Decimal values
        int XOR = STX ^ ETX;
        return XOR;
    }

    public int StrtoHexInt(String str) {
        int BCC1Hex = 0;
        if (str.compareToIgnoreCase("0") == 0) {
            BCC1Hex = 0x30;
        }
        if (str.compareToIgnoreCase("1") == 0) {
            BCC1Hex = 0x31;
        }
        if (str.compareToIgnoreCase("2") == 0) {
            BCC1Hex = 0x32;
        }
        if (str.compareToIgnoreCase("3") == 0) {
            BCC1Hex = 0x33;
        }
        if (str.compareToIgnoreCase("4") == 0) {
            BCC1Hex = 0x34;
        }
        if (str.compareToIgnoreCase("5") == 0) {
            BCC1Hex = 0x35;
        }
        if (str.compareToIgnoreCase("6") == 0) {
            BCC1Hex = 0x36;
        }
        if (str.compareToIgnoreCase("7") == 0) {
            BCC1Hex = 0x37;
        }
        if (str.compareToIgnoreCase("8") == 0) {
            BCC1Hex = 0x38;
        }
        if (str.compareToIgnoreCase("9") == 0) {
            BCC1Hex = 0x39;
        }
        if (str.compareToIgnoreCase("A") == 0) {
            BCC1Hex = 0x41;
        }
        if (str.compareToIgnoreCase("B") == 0) {
            BCC1Hex = 0x42;
        }
        if (str.compareToIgnoreCase("C") == 0) {
            BCC1Hex = 0x43;
        }
        if (str.compareToIgnoreCase("D") == 0) {
            BCC1Hex = 0x44;
        }
        if (str.compareToIgnoreCase("E") == 0) {
            BCC1Hex = 0x45;
        }
        if (str.compareToIgnoreCase("F") == 0) {
            BCC1Hex = 0x46;
        }
        return BCC1Hex;
    }

    public String DecToHexString(String hex) {
        int i = Integer.parseInt(hex);
        String hex1 = Integer.toHexString(i);
        String hex2 = "";
        if (hex.length() > 1) {
            hex2 = hex1.substring(hex1.length() - 2, hex1.length()).toUpperCase();
        } else {
            hex2 = hex1.substring(hex1.length() - 1, hex1.length()).toUpperCase();
        }
        return hex2;
    }

    public int converthex2int(String hex) {
        int intValue = Integer.parseInt(hex, 16);
        return intValue;
    }

    public int convertHEX2int(int hex) {
        String strhex = String.valueOf(hex);
        int intValue = Integer.parseInt(strhex, 16);
        return intValue;
    }

    public static void main(String[] args) {
        int ITX = 0x5F;
        int NTX = 0x3A;
        int ndTX = 0xF4;
        XORstuff xs = new XORstuff();
        log.info(xs.converthex2int("A"));
        String Firstno = xs.XOR2integers2String(ITX, NTX);
        int FirstInt = xs.XOR2integers2Integer(ITX, NTX);
        String Secondno = xs.XOR2integers2String(FirstInt, ndTX);

        log.info("XOR: ITX+NTX = " + Firstno + " in Decimal Form");
        log.info("Hex of ITX+NTX is:" + xs.DecToHexString(Firstno));

        log.info("XOR: First with ndTX = " + Secondno + " in Decimal Form");
        log.info("Hex of First with ndTX is:" + xs.DecToHexString(Secondno));
    }

}
