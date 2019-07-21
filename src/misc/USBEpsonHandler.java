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
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.log4j.LogManager;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;

/**
 *
 * @author Administrator
 */
public class USBEpsonHandler {
    
    static Logger log = LogManager.getLogger(USBEpsonHandler.class.getName());
    
    StringBuilder strBldr = new StringBuilder();
    private RawFileHandler rfh = new RawFileHandler();
   
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
    
    private String printerName;
    
    USBPrinterService printerService = new USBPrinterService();

    public USBEpsonHandler() {
        try {
        printerName = xr.getElementValue("C:\\JTerminals\\initH.xml", "printerName");
        //uline(false);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    
    public void initializePrinter() {
        //byte[] init = new byte[] { 0x1b, 40 };
        //printerService.printBytes(printerName, init);
    }


    public void initializePrinterOld() {
        try {
            byte d = 0x1B; //escape character
            byte i = 0x40;
            bos.write(d);
            bos.write(i);
            bos.close();
        } catch (IOException ex) {
            log.error(ex.getMessage());
        }
    }

    public void printTestHEADER() {
        try {

            this.setRed();
            this.Justify((byte) 1);
            //this.feedpaperup((byte)1);
            this.printline("/*-/*-/*-//*-/*-/*-/");
            this.fullcut();
            this.Justify((byte) 0);
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
            String MINNumber = xr.getElementValue("C:/JTerminals/min.xml", "machineIDno");
            String headerline1PrintEnabled = xr.getAttributeValue("C://JTerminals/initH.xml", "headerline1", "print");
            String headerline2PrintEnabled = xr.getAttributeValue("C://JTerminals/initH.xml", "headerline2", "print");
            String headerline3PrintEnabled = xr.getAttributeValue("C://JTerminals/initH.xml", "headerline3", "print");
            String headerline4PrintEnabled = xr.getAttributeValue("C://JTerminals/initH.xml", "headerline4", "print");
            String headerline5PrintEnabled = xr.getAttributeValue("C://JTerminals/initH.xml", "headerline5", "print");
            String headerline6PrintEnabled = xr.getAttributeValue("C://JTerminals/initH.xml", "headerline6", "print");
            String headerline7PrintEnabled = xr.getAttributeValue("C://JTerminals/initH.xml", "headerline7", "print");
            String headerline8PrintEnabled = xr.getAttributeValue("C://JTerminals/initH.xml", "headerline8", "print");
            String headerline9PrintEnabled = xr.getAttributeValue("C://JTerminals/initH.xml", "headerline9", "print");
            String headerline10PrintEnabled = xr.getAttributeValue("C://JTerminals/initH.xml", "headerline10", "print");
            String headerline1 = xr.getElementValue("C:/JTerminals/initH.xml", "headerline1");
            String headerline2 = xr.getElementValue("C:/JTerminals/initH.xml", "headerline2");
            String headerline3 = xr.getElementValue("C:/JTerminals/initH.xml", "headerline3");
            String headerline4 = xr.getElementValue("C:/JTerminals/initH.xml", "headerline4");
            String headerline5 = xr.getElementValue("C:/JTerminals/initH.xml", "headerline5");
            String headerline6 = xr.getElementValue("C:/JTerminals/initH.xml", "headerline6");
            String headerline7 = xr.getElementValue("C:/JTerminals/initH.xml", "headerline7");
            String headerline8 = xr.getElementValue("C:/JTerminals/initH.xml", "headerline8");
            String headerline9 = xr.getElementValue("C:/JTerminals/initH.xml", "headerline9");
            String headerline10 = xr.getElementValue("C:/JTerminals/initH.xml", "headerline10");
            String BIRHeaderEnabled = xr.getElementValue("C://JTerminals/initH.xml", "BIRheader");
            this.setRed();
            this.Justify((byte) 0);
            //this.feedpaperup((byte)1);
            
            if(headerline1PrintEnabled.compareToIgnoreCase("enabled") == 0) {
                this.printline(headerline1);
            }
            if(headerline2PrintEnabled.compareToIgnoreCase("enabled") == 0) {
                this.printline(headerline2);
            }
            if(headerline3PrintEnabled.compareToIgnoreCase("enabled") == 0) {
                this.printline(headerline3);
            }
            if(headerline4PrintEnabled.compareToIgnoreCase("enabled") == 0) {
                this.printline(headerline4);
            }
            if(headerline5PrintEnabled.compareToIgnoreCase("enabled") == 0) {
                this.printline(headerline5);
            }
            if(headerline6PrintEnabled.compareToIgnoreCase("enabled") == 0) {
                this.printline(headerline6);
            }
            if(headerline7PrintEnabled.compareToIgnoreCase("enabled") == 0) {
                this.printline(headerline7);
            }
            if(headerline8PrintEnabled.compareToIgnoreCase("enabled") == 0) {
                this.printline(headerline8);
            }
            if(headerline9PrintEnabled.compareToIgnoreCase("enabled") == 0) {
                this.printline(headerline9);
            }
            if(headerline10PrintEnabled.compareToIgnoreCase("enabled") == 0) {
                this.printline(headerline10);
            }
            
            this.setBlack();
            
            if (BIRHeaderEnabled.compareToIgnoreCase("enabled") == 0) {
                //this.printline("TIN: " + TINno);
                this.printline("S/N: " + serialno);
                this.printline("MIN: " + MINNumber);
            }
            //this.printline("Exit ID: " + SentinelID);
            //this.Justify((byte) 0);
            this.startPrinter();
            //bos.write(13);
            //bos.close();
        } catch (Exception ex) {
            log.error(ex.getMessage());
        }
    }

    public void printFOOTER(String SentinelID, boolean isOfficial) {
        try {
            String footerline1PrintEnabled = xr.getAttributeValue("C://JTerminals/initH.xml", "footerline1", "print");
            String footerline2PrintEnabled = xr.getAttributeValue("C://JTerminals/initH.xml", "footerline2", "print");
            String footerline3PrintEnabled = xr.getAttributeValue("C://JTerminals/initH.xml", "footerline3", "print");
            String footerline4PrintEnabled = xr.getAttributeValue("C://JTerminals/initH.xml", "footerline4", "print");
            String footerline5PrintEnabled = xr.getAttributeValue("C://JTerminals/initH.xml", "footerline5", "print");
            String footerline6PrintEnabled = xr.getAttributeValue("C://JTerminals/initH.xml", "footerline6", "print");
            String footerline7PrintEnabled = xr.getAttributeValue("C://JTerminals/initH.xml", "footerline7", "print");
            String footerline8PrintEnabled = xr.getAttributeValue("C://JTerminals/initH.xml", "footerline8", "print");
            String footerline9PrintEnabled = xr.getAttributeValue("C://JTerminals/initH.xml", "footerline9", "print");
            String footerline10PrintEnabled = xr.getAttributeValue("C://JTerminals/initH.xml", "footerlin107", "print");
            String footerline1 = xr.getElementValue("C:/JTerminals/initH.xml", "footerline1");
            String footerline2 = xr.getElementValue("C:/JTerminals/initH.xml", "footerline2");
            String footerline3 = xr.getElementValue("C:/JTerminals/initH.xml", "footerline3");
            String footerline4 = xr.getElementValue("C:/JTerminals/initH.xml", "footerline4");
            String footerline5 = xr.getElementValue("C:/JTerminals/initH.xml", "footerline5");
            String footerline6 = xr.getElementValue("C:/JTerminals/initH.xml", "footerline6");
            String footerline7 = xr.getElementValue("C:/JTerminals/initH.xml", "footerline7");
            String footerline8 = xr.getElementValue("C:/JTerminals/initH.xml", "footerline8");
            String footerline9 = xr.getElementValue("C:/JTerminals/initH.xml", "footerline9");
            String footerline10 = xr.getElementValue("C:/JTerminals/initH.xml", "footerline10");
            String FooterEnabled = xr.getElementValue("C://JTerminals/initH.xml", "footer");
            String BIRFooterEnabled = xr.getElementValue("C://JTerminals/initH.xml", "BIRfooter");
            
            if (FooterEnabled.compareToIgnoreCase("enabled") == 0) {
                this.printline("POS Provider:");
                this.printline("Applied Modern Theoretics Inc.");
                this.printline("5F Builders Center Bldg.");
                this.printline("Salcedo Street Legaspi Village");
                this.printline("Makati Philippines");
                this.startPrinter();
            }
            
            this.setRed();            
            if(footerline1PrintEnabled.compareToIgnoreCase("enabled") == 0) {
                this.printline(footerline1);
            }
            if(footerline2PrintEnabled.compareToIgnoreCase("enabled") == 0) {
                this.printline(footerline2);
            }
            if(footerline3PrintEnabled.compareToIgnoreCase("enabled") == 0) {
                this.printline(footerline3);
            }
            if(footerline4PrintEnabled.compareToIgnoreCase("enabled") == 0) {
                this.printline(footerline4);
            }
            if(footerline5PrintEnabled.compareToIgnoreCase("enabled") == 0) {
                this.printline(footerline5);
            }
            if(footerline6PrintEnabled.compareToIgnoreCase("enabled") == 0) {
                this.printline(footerline6);
            }
            if(footerline7PrintEnabled.compareToIgnoreCase("enabled") == 0) {
                this.printline(footerline7);
            }
            if(footerline8PrintEnabled.compareToIgnoreCase("enabled") == 0) {
                this.printline(footerline8);
            }
            if(footerline9PrintEnabled.compareToIgnoreCase("enabled") == 0) {
                this.printline(footerline9);
            }
            if(footerline10PrintEnabled.compareToIgnoreCase("enabled") == 0) {
                this.printline(footerline10);
            }
            this.setBlack();
            this.Justify((byte) 1);
            if (isOfficial) {
                if (BIRFooterEnabled.compareToIgnoreCase("enabled") == 0) {
                this.printline("THIS RECEIPT SHALL BE VALID FOR FIVE(5)");
                this.printline("YEARS FROM THE DATE OF THE PERMIT TO USE");
//                this.printline("     THIS DOCUMENT IS NOT VALID");
//                this.printline("       FOR CLAIM OF INPUT TAX");
                //this.startPrinter();
                }
            } else {
                if (BIRFooterEnabled.compareToIgnoreCase("enabled") == 0) {
                    this.printline("THIS DOCUMENT SHALL BE VALID FOR FIVE(5)");
                    this.printline("YEARS FROM THE DATE OF THE PERMIT TO USE");
                    this.printline("     THIS DOCUMENT IS NOT VALID");
                    this.printline("       FOR CLAIM OF INPUT TAX");
                    //this.startPrinter();
                }
            }
            this.Justify((byte) 0);
            this.startPrinter();
        } catch (Exception ex) {
            log.error(ex.getMessage());
        }
    }
    
    public void carriagereturn() {
        try {
            String line = "\n";
            bos.write(line.getBytes());
            bos.close();
        } catch (IOException ex) {
            log.error(ex.getMessage());
        }
    }

    public void printword(String line) {
        try {
            bos.write(line.getBytes());
            bos.close();
        } catch (IOException ex) {
            log.error(ex.getMessage());
        }
    }

    public void printlineOld(String line) {
        try {
            line = line + "\n";
            bos.write(line.getBytes());
            bos.write(13);
            bos.close();
        } catch (IOException ex) {
            log.error(ex.getMessage());
        }
    }
    
    public void printnumline(String line) {
        try {
            printerService.printString(line + "");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void oldprintnumline(String line) {
        try {
            String l = line.toString() + "\n";
            bos.write(l.getBytes());
            bos.write(13);
            //bos.close();
        } catch (IOException ex) {
            log.error(ex.getMessage());
        }
        //bos.close(); 
    }
    
    public void fullcut() {
        byte[] cutP = new byte[] { 0x1d, 'V', 1};
	printerService.printBytes(printerName, cutP);
    }


    public void fullcutOld() {
        try {
            byte d = 0x1b; //escape character
            byte c = 0x69;
            bos.write(d);
            bos.write(c);
            bos.close();
            kickdrawer();
        } catch (IOException ex) {
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
        } catch (IOException ex) {
            log.error(ex.getMessage());
        }
    }

    public void JustifyOld(short align) {
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
        } catch (IOException ex) {
            log.error(ex.getMessage());
        }
    }

    public void setRedOld() {
        try {
            byte d = 0x1b; //escape character
            byte red1 = 0x72;
            byte red2 = 0x01;
            bos.write(d);
            bos.write(red1);
            bos.write(red2);
        } catch (IOException ex) {
            log.error(ex.getMessage());
        }
    }

    public void setBlackOld() {
        try {
            byte d = 0x1b; //escape character
            byte black1 = 0x72;
            byte black2 = 0x00;
            bos.write(d);
            bos.write(black1);
            bos.write(black2);
        } catch (IOException ex) {
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
        } catch (IOException ex) {
            log.error(ex.getMessage());
        }
    }

    public void feedpaperupOld(short feedno) {
        try {
            byte d = 0x1b; //escape character
            byte feed = 0x64;
            byte feedn = (byte) feedno;
            bos.write(d);
            bos.write(feed);
            bos.write(feedn);
        } catch (IOException ex) {
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
        } catch (IOException ex) {
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
        } catch (IOException ex) {
            log.error(ex.getMessage());
        }
    }
    public void selectEMstyle(boolean style) {
        byte[] emp0 = new byte[]{0x1b, 'E', 0};
        byte[] emp1 = new byte[]{0x1b, 'E', 1};
        if (style == true) {
        //    printerService.printBytes (printerName, emp1);
        }
        else if (style == false) {
        //    printerService.printBytes (printerName, emp0);
        }
    }
    
    public void selectEMstyleOld(boolean style) {
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
        } catch (IOException ex) {
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
        } catch (IOException ex) {
            log.error(ex.getMessage());
        }
    }

    public void closeprinterOld() {
        
        try {
            bos.close();
        } catch (IOException ex) {
            log.error(ex.getMessage());
        }
        if (PrnPortType.compareToIgnoreCase("COM6") == 0 || PrnPortType.compareToIgnoreCase("COM2") == 0 || PrnPortType.compareToIgnoreCase("COM3") == 0 || PrnPortType.compareToIgnoreCase("COM4") == 0) {
            serialPort.close();
        } else {
            parallelPort.close();
        }
        
    }
    
    public void closeprinter() {
        
    }

    public boolean checkPrinterOFFstatus() {
        /*if (PrnPortType.compareToIgnoreCase("LPT1") == 0 || PrnPortType.compareToIgnoreCase("LPT2") == 0) {
            if (parallelPort.isPrinterError() == true) {
                return true;
            }
        }*/
            /*if (serialPort.isCD() == false) {
                return true;
            }
            if (serialPort.isCTS() == false) {
                return true;
            }*/
        return false;
    }
    
    public void uline(boolean on) {
        if (on) {
            byte[] ulineOn = new byte[] { 0x1b, '-', 1 };
            printerService.printBytes(printerName, ulineOn);
        } else {
            byte[] ulineOff = new byte[] { 0x1b, '-', 0 };
            printerService.printBytes(printerName, ulineOff);
        }
    }
        
    
    public void printline(String line) {
        try {
            printerService.printString(line + "");
            strBldr.append(line + "" + "\n");//Init
            
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    
    public void startPrinter() {
        printerService.startPrinter(printerName);
    }

    
    public void usbPrintTest() {
		
		log.info(printerService.getPrinters());
				
                //byte[] init = new byte[] { 0x1b, 40 };
                //printerService.printBytes(printerName, init);
                
                byte[] emp = new byte[] { 0x1b, 'E', 1 };
                printerService.printBytes(printerName, emp);
                byte[] ulineOff = new byte[] { 0x1b, '-', 0 };
                printerService.printBytes(printerName, ulineOff);
		//print some stuff
                byte[] just = new byte[] { 0x1b, 0x61, 0 };
                printerService.printBytes(printerName, just);
                byte[] red = new byte[] { 0x1b, 0x72, 0x01 };
                byte[] black = new byte[] { 0x1b, 0x72, 0x00 };
                printerService.printBytes(printerName, red);
		printerService.printString("Program by:");
                printerService.startPrinter(printerName);
                byte[] feed = new byte[] { 0x1b, 'd', 1 };
                //printerService.printBytes(printerName, feed);
	
                byte[] ulineOn = new byte[] { 0x1b, '-', 2 };
                printerService.printBytes(printerName, ulineOn);
                printerService.printBytes(printerName, black);
                //print some stuff
                byte[] just1 = new byte[] { 0x1b, 0x61, 1 };
                printerService.printBytes(printerName, just1);
		printerService.printString("Angelo Dizon");
                printerService.startPrinter(printerName);
                byte[] feed2 = new byte[] { 0x1b, 'd', 10 };
                printerService.printBytes(printerName, feed2);
		// cut that paper!
                //ASCII	   	GS	  	V	  	m
                //Hex		1D		56		m
		//byte[] cutP = new byte[] { 0x1d, 'V', 1};
		//printerService.printBytes(printerName, cutP);
                
    }
    
    public void feedpaperup(byte feedno) {
        byte[] feed = new byte[] { 0x1b, 'd', feedno };
        printerService.printBytes(printerName, feed);
    }
    
    public void setRed() {
        //byte[] red = new byte[] { 0x1b, 0x72, 0x01 };
        byte[] red = new byte[] { 0x1b, 0x72, 0x00 };
        printerService.printBytes(printerName, red);
    }
    
    public void setBlack() {
        byte[] black = new byte[] { 0x1b, 0x72, 0x00 };
        printerService.printBytes(printerName, black);
    }
    /*
    justification 0 = Left, 1 = Center, 2 = Right
    */
    public void Justify(byte justification) {
        byte[] just = new byte[] { 0x1b, 0x61, justification };
        printerService.printBytes(printerName, just);
    }

    public static void main(String[] args) {
        USBEpsonHandler ea = new USBEpsonHandler();
        ea.usbPrintTest();
    }

    public static void mainOld(String[] args) {
        try {
            USBEpsonHandler ea = new USBEpsonHandler();
            ea.initializePrinter();
            ea.Justify((byte)1);
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
            ea.closeprinter();
        } catch (Exception ex) {
            log.error(ex.getMessage());
        }
    }

    public void closeReceiptFile(String Exitpoint) {
        
        RawFileHandler rfh = new RawFileHandler();
        Date now = new Date();
        SimpleDateFormat ddf = new SimpleDateFormat("dd");
        SimpleDateFormat mdf = new SimpleDateFormat("MM");
        SimpleDateFormat ydf = new SimpleDateFormat("yy");
        String extension = Exitpoint.substring(2, 4) + ddf.format(now) + mdf.format(now) + ".0" + ydf.format(now);
        
        boolean found = rfh.FindFileFolder("C://JTerminals/General/");
            if (found == false) {
                rfh.CreateNewFolder("C://JTerminals/General/");
            }
        try {
            rfh.appendfile("C://JTerminals/General/", "R2" + extension, strBldr.toString());
        } catch (IOException ex) {
            log.error(ex.getMessage());
        }
    
    }

    public void closePrinter() {
    }
    
    public void openPrinter() {
    }
}
