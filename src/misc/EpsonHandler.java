/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package misc;

import gnu.io.CommPort;
import gnu.io.CommPortIdentifier;
import gnu.io.ParallelPort;
import gnu.io.SerialPort;
import java.io.BufferedOutputStream;
import java.io.OutputStream;
import org.apache.log4j.LogManager;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;

/**
 *
 * @author Administrator
 */
public class EpsonHandler {
    
    static Logger log = LogManager.getLogger(EpsonHandler.class.getName());

    /*   /dev/lp0     - Parallel Port   LINUX
     *   /dev/ttyS0   - Comm1 Port
     *   LPT1
     *   COM1
     */
    XMLreader xr = new XMLreader();
    //String PrnPortType = "";

    private CommPortIdentifier portId = null;
    private CommPort port = null;
    private OutputStream os = null;
    private BufferedOutputStream bos = null;
    private ParallelPort parallelPort;
    private SerialPort serialPort;

    private String PrnPortType;

    public boolean PrinterEnabled;

    public EpsonHandler() {
        openPrinter();
    }
    
    public void openPrinter() {
        try {
            if ("enabled".compareToIgnoreCase(xr.getElementValue("C:\\JTerminals\\initH.xml", "printer"))==0) {
                PrinterEnabled = true;
            } else {
                PrinterEnabled = false;
            }
            if (PrinterEnabled) {
                PrnPortType = xr.getAttributeValue("C:\\JTerminals\\initH.xml", "printer", "port");
                String portname = "";
                if (null != serialPort) {
                    serialPort.close();
                }
                if (PrnPortType.compareToIgnoreCase("COM1") == 0) {
                    portname = "COM1";
                    portId = CommPortIdentifier.getPortIdentifier(portname);
                    serialPort = (SerialPort) portId.open("Java Porting", 4500);
                    serialPort.setSerialPortParams(9600, SerialPort.DATABITS_8, SerialPort.STOPBITS_1, SerialPort.PARITY_NONE);
                    os = serialPort.getOutputStream();
                    bos = new BufferedOutputStream(os);
                } else if (PrnPortType.compareToIgnoreCase("COM2") == 0) {
                    portname = "/dev/ttyS1";
                    portId = CommPortIdentifier.getPortIdentifier(portname);
                    serialPort = (SerialPort) portId.open("Java Porting", 4500);
                    serialPort.setSerialPortParams(9600, SerialPort.DATABITS_8, SerialPort.STOPBITS_1, SerialPort.PARITY_NONE);
                    os = serialPort.getOutputStream();
                    bos = new BufferedOutputStream(os);
                } else if (PrnPortType.compareToIgnoreCase("COM3") == 0) {
                    portname = "/dev/ttyS2";
                    portId = CommPortIdentifier.getPortIdentifier(portname);
                    serialPort = (SerialPort) portId.open("Java Printing", 3500);
                    serialPort.setSerialPortParams(9600, SerialPort.DATABITS_8, SerialPort.STOPBITS_1, SerialPort.PARITY_NONE);
                    os = serialPort.getOutputStream();
                    bos = new BufferedOutputStream(os);
                } else if (PrnPortType.compareToIgnoreCase("COM4") == 0) {
                    portname = "/dev/ttyS3";
                    portname = PrnPortType;
                    portId = CommPortIdentifier.getPortIdentifier(portname);
                    serialPort = (SerialPort) portId.open("Java Printing", 3500);
                    serialPort.setSerialPortParams(9600, SerialPort.DATABITS_8, SerialPort.STOPBITS_1, SerialPort.PARITY_NONE);
                    os = serialPort.getOutputStream();
                    bos = new BufferedOutputStream(os);
                } else if (PrnPortType.compareToIgnoreCase("LPT1") == 0) {
                    portname = "/dev/lp0";
                    portId = CommPortIdentifier.getPortIdentifier(portname);
                    parallelPort = (ParallelPort) portId.open("CommTest", 3500);
                    os = parallelPort.getOutputStream();
                    bos = new BufferedOutputStream(os);
                } else if (PrnPortType.compareToIgnoreCase("LPT2") == 0) {
                    portname = "/dev/lp1";
                    portId = CommPortIdentifier.getPortIdentifier(portname);
                    parallelPort = (ParallelPort) portId.open("CommTest", 3500);
                    os = parallelPort.getOutputStream();
                    bos = new BufferedOutputStream(os);
                } else {
                    //portname = "/dev/lp0";
                    portname = PrnPortType;
                    portId = CommPortIdentifier.getPortIdentifier(portname);
                    serialPort = (SerialPort) portId.open("Java Printing", 3500);
                    serialPort.setSerialPortParams(9600, SerialPort.DATABITS_8, SerialPort.STOPBITS_1, SerialPort.PARITY_NONE);
                    os = serialPort.getOutputStream();
                    bos = new BufferedOutputStream(os);
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            log.error(ex.toString());
        }
    }

    public void initializePrinter() {
        try {
            byte d = 0x1B; //escape character
            byte i = 0x40;
            bos.write(d);
            bos.write(i);
            bos.close();
        } catch (Exception ex) {
            log.error(ex.getMessage());
        }
    }

    public void printTestHEADER() {
        try {

            this.setRed();
            this.Justify((short) 1);
            //this.feedpaperup((short)1);
            this.printline("/*-/*-/*-//*-/*-/*-/");
            this.fullcut();
            this.Justify((short) 0);
            bos.write(13);
            bos.close();
        } catch (Exception ex) {
            log.error(ex.getMessage());
        }
    }

    public void printHEADER(String SentinelID) {
        try {
            String TINno = xr.getElementValue("C:/JTerminals/initH.xml", "receiptno");
            String serialno = xr.getElementValue("C:/JTerminals/initH.xml", "serialno");
            String headerline1 = xr.getElementValue("C:/JTerminals/initH.xml", "headerline1");
            String headerline2 = xr.getElementValue("C:/JTerminals/initH.xml", "headerline2");
            String headerline3 = xr.getElementValue("C:/JTerminals/initH.xml", "headerline3");
            String MINNumber = xr.getElementValue("C:/JTerminals/min.xml", "machineIDno");
            String PermitNumber = xr.getElementValue("C:/JTerminals/min.xml", "permitno");

            this.setRed();
            this.Justify((short) 1);
            //this.feedpaperup((short)1);
            this.printline(headerline1);
            this.printline(headerline2);
            this.printline(headerline3);
            this.setBlack();
            this.printline(PermitNumber);
            this.printline(TINno);
            this.printline(serialno);
            this.printline(MINNumber);
            this.printline("Exit ID: " + SentinelID);
            this.Justify((short) 0);
            bos.write(13);
            bos.close();
        } catch (Exception ex) {
            log.error(ex.getMessage());
        }
    }

    public void carriagereturn() {
        try {
            String line = "\n";
            bos.write(line.getBytes());
            bos.close();
        } catch (Exception ex) {
            log.error(ex.getMessage());
        }
    }

    public void printword(String line) {
        try {
            bos.write(line.getBytes());
            bos.close();
        } catch (Exception ex) {
            log.error(ex.getMessage());
        }
    }

    public void printline(String line) {
        try {
            line = line + "\n";
            bos.write(line.getBytes());
            bos.write(13);
            bos.close();
        } catch (Exception ex) {
            log.error(ex.getMessage());
        }
    }

    public void printnumline(String line) {
        try {
            String l = line.toString() + "\n";
            bos.write(l.getBytes());
            bos.write(13);
            //bos.close();
        } catch (Exception ex) {
            log.error(ex.getMessage());
        }
        //bos.close(); 
    }

    public void fullcut() {
        try {
            byte d = 0x1b; //escape character
            byte c = 0x69;
            bos.write(d);
            bos.write(c);
            bos.close();
            kickdrawer();
        } catch (Exception ex) {
            log.error(ex.getMessage());
        }
    }

    public void kickdrawer() {
        try {
            byte d = 0x1b; //escape character
            byte c = 0x70;
            byte pin = 0x00;
            byte on = 0x50;
            byte off = 0x60;
            bos.write(d);
            bos.write(c);
            bos.write(pin);
            bos.write(on);
            bos.write(off);
            bos.close();
        } catch (Exception ex) {
            log.error(ex.getMessage());
        }
    }

    public void Justify(short align) {
        try {
            byte d = 0x1b; //escape character
            byte justify = 0x61;
            byte left = 0x00;
            byte center = 0x01;
            byte right = 0x02;
            bos.write(d);
            bos.write(justify);
            switch (align) {
                case 0:
                    bos.write(left);
                case 1:
                    bos.write(center);
                case 2:
                    bos.write(right);
            }
        } catch (Exception ex) {
            log.error(ex.getMessage());
        }
    }

    public void setRed() {
        try {
            byte d = 0x1b; //escape character
            byte red1 = 0x72;
            byte red2 = 0x01;
            bos.write(d);
            bos.write(red1);
            bos.write(red2);
        } catch (Exception ex) {
            log.error(ex.getMessage());
        }
    }

    public void setBlack() {
        try {
            byte d = 0x1b; //escape character
            byte black1 = 0x72;
            byte black2 = 0x00;
            bos.write(d);
            bos.write(black1);
            bos.write(black2);
        } catch (Exception ex) {
            log.error(ex.getMessage());
        }

    }

    public void printupsidedown(short way) {
        try {
            byte d = 0x1b; //escape character
            byte updown = 0x7B;
            byte updown0 = 0x00; //rightside down printing
            byte updown1 = 0x01; //upside down printing
            bos.write(d);
            bos.write(updown);
            switch (way) {
                case 0:
                    bos.write(updown0);
                case 1:
                    bos.write(updown1);
            }
        } catch (Exception ex) {
            log.error(ex.getMessage());
        }
    }

    public void feedpaperup(short feedno) {
        try {
            byte d = 0x1b; //escape character
            byte feed = 0x64;
            byte feedn = (byte) feedno;
            bos.write(d);
            bos.write(feed);
            bos.write(feedn);
        } catch (Exception ex) {
            log.error(ex.getMessage());
        }
    }

    public void paperDetector(short feedno) {
        try {
            byte d = 0x1B; //escape character
            byte feed0 = 0x63;
            byte feed1 = 0x33;
            byte feedn = (byte) feedno;
            bos.write(d);
            bos.write(feed0);
            bos.write(feed1);
            bos.write(feedn);
        } catch (Exception ex) {
            log.error(ex.getMessage());
        }
    }

    public void selectstyle(short style) {
        try {
            byte d = 0x1B; //escape character
            byte pmode = 0x21;
            byte pmodeff = 0x00; //fontsize 9x9
            byte pmode0 = 0x01; //fontsize 7x9
            byte pmode1 = 0x08; //emphasized mode
            byte pmode2 = 0x10; //double height
            byte pmode3 = 0x20; //double width
            byte pmode4 = (byte) 0x80; //underlinedbos.write(d);
            bos.write(d);
            bos.write(pmode);
            switch (style) {
                case 0:
                    bos.write(pmodeff);
                case 1:
                    bos.write(pmode0);
                case 2:
                    bos.write(pmode1);
                case 3:
                    bos.write(pmode2);
                case 4:
                    bos.write(pmode3);
                case 5:
                    bos.write(pmode4);
            }
        } catch (Exception ex) {
            log.error(ex.getMessage());
        }
    }

    public void selectEMstyle(boolean style) {
        try {
            byte d = 0x1B; //escape character
            byte pmode = 0x45;
            byte pmode0 = 0x00; //fontsize+1
            byte pmode1 = 0x01; //emphasized mode
            bos.write(d);
            bos.write(pmode);
            if (style == true) {
                bos.write(pmode1);
            } else if (style == false) {
                bos.write(pmode0);
            }
        } catch (Exception ex) {
            log.error(ex.getMessage());
        }
    }

    public void select2Strikestyle(boolean style) {
        try {
            byte d = 0x1B; //escape character
            byte pmode = 0x45;
            byte pmode0 = 0x00; //fontsize+1
            byte pmode1 = 0x01; //emphasized mode
            bos.write(d);
            bos.write(pmode);
            if (style == true) {
                bos.write(pmode1);
            } else if (style == false) {
                bos.write(pmode0);
            }
        } catch (Exception ex) {
            log.error(ex.getMessage());
        }
    }

    public void closePrinter() {
        
        try {
            bos.close();
            if (PrnPortType.startsWith("COM")) {
                serialPort.close();
            } else {
                parallelPort.close();
            }
        } catch (Exception ex) {
            log.error(ex.getMessage());
        }
        
        
    }

    public boolean checkPrinterOFFstatus() {
        /*if (PrnPortType.compareToIgnoreCase("LPT1") == 0 || PrnPortType.compareToIgnoreCase("LPT2") == 0) {
            if (parallelPort.isPrinterError() == true) {
                return true;
            }
        }*/
            if (serialPort.isCD() == false) {
                return true;
            }
            if (serialPort.isCTS() == false) {
                return true;
            }
        return false;
    }

    public static void main(String[] args) {
        try {
            EpsonHandler ea = new EpsonHandler();
            ea.initializePrinter();
            ea.Justify((short)1);
//            ea.selectstyle((short) 1);
            ea.printline("test");
            ea.printline("test");
            ea.printline("test");
            ea.printline("test");
            ea.printline("test");
            ea.printline("test");
            ea.printline("test");
            ea.printline("test");
            ea.printline("test");

//            ea.paperDetector((short) 1);
//            ea.paperDetector((short) 2);
//            ea.paperDetector((short) 4);
//            ea.paperDetector((short) 8);
//            ea.selectstyle((short) 0);
            ea.printline("test");

            //log.info(ea.checkPrinterOFFstatus());
            //ea.fullcut();
            //ea.kickdrawer();
            ea.closePrinter();
        } catch (Exception ex) {
            log.error(ex.getMessage());
        }
    }
}
