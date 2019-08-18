/*
 * Angelo Dizon
 * Update to Linux kernel version 2.6.23.17
 * Created on January 27, 2008, 5:27 PM
 * Modified for Araneta on July 2017 1:34 PM
 * Modified for Ever Gotesco on Sept 2018 3:40 PM
 */
package UserInteface;

import java.awt.event.WindowEvent;
import java.util.Date;
import java.util.Locale;
import java.util.ResourceBundle;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import javax.swing.JLabel;
import java.awt.event.KeyEvent;
import java.text.DateFormat;

import api.EntranceAPI;
import api.ExitAPI;
import api.DupReceiptAPI;
import api.LostCheckAPI;
import api.MasterCardAPI;
import api.ParkersAPI;
import api.PoleDisplayAPI;

import api.PreEntranceAPI;
import api.ReadMIFARE;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.awt.event.WindowFocusListener;
import java.awt.image.BufferedImage;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Random;
import java.util.logging.Level;
import javax.smartcardio.Card;
import javax.smartcardio.CardException;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.SwingConstants;
import misc.DataBaseHandler;
import misc.DateConversionHandler;
import misc.EncryptionTool;
import misc.USBEpsonHandler;
import misc.SerialEpsonHandler;
import misc.LogUtility;
import misc.XMLreader;
import misc.ServerDataHandler;
import modules.LoginMOD;
import modules.MPPchecker;
import modules.NOSfiles;
import modules.PasswordMOD;
import modules.SaveCollData;
import modules.SlotsStatus;
import modules.SystemStatus;

public class HybridPanelUI extends javax.swing.JFrame implements WindowFocusListener {

    //public String entryIPCamera = "192.168.100.220";
    public String entryIPCamera = "192.168.1.64";
    //public String exitIPCamera = "192.168.100.219";    
    public String exitIPCamera = "192.168.1.64";
    public boolean isEnterPressed = false;
    char[] characterSet = {'A', 'B', 'C', 'D', 'E', '1', '2', '3', '4', '5', '6', '7', '8', '9', '0'};
    public boolean debugMode = false;
    private boolean altIsPressed = false;
    String switchmode = null;
    String bundleName = "mapping.idproperties";
    ResourceBundle myResources = ResourceBundle.getBundle(bundleName, Locale.getDefault());
    static Logger log = LogManager.getLogger(HybridPanelUI.class.getName());

    public int waitMultiplier = 0;
    //public USBEpsonHandler eh = new USBEpsonHandler();
    public boolean ExitSwitch = true;
//      short secret = 0;
//      public boolean logging = false;
    public javax.swing.JLabel errorMsg[];
    public String currentmode = "";
    public String currenttype = "R";
    public boolean firstscan = true, firstRun = true;
    public boolean lostEnabled = false;
    public boolean settlementEnabled = false;
    public boolean reprintEnabled = true;
    private boolean exitCamPressed = false;
    boolean online = false;
    boolean KeyAccepted = true;
    //--exit
    boolean mountedonce = false;
    boolean Password = false;
    public boolean ComputeJogger = false;
    public boolean MasterIN = false;
    public boolean MasterCard1 = false;
    public boolean MasterCard2 = false;
    public boolean searchparker = false;
    public boolean awaitClearCollect = false;
    //boolean Logout = false;
    //boolean CheckVIP = false;
    //boolean CheckPREPAID = false;
    //boolean CheckMPP = false;
    public String Prepaid2Save;

    //boolean preCheckLCEP = false;
    //boolean CheckLCEP = false;
    public String LCEPtemp = "";
    public String PreviousCard = "";

    public boolean modal = false;

    public boolean LCEPOverride = false;
    public boolean LostOverride = false;       //overrides type
    public boolean VIPOverride = false;        //from read card
    public boolean PrepaidOverride = false;
    public boolean MotorOverride = false;
    public boolean QCSeniorOverride = false;
    public boolean BPOMotorOverride = false;
    public boolean BPOCarOverride = false;
    public boolean HolidayOverride = false;
    public boolean DeliveryOverride = false;
    public boolean IFROverride = false;
    public boolean InvalidFlatRate = false;
    public boolean TicketInput = false;
    public boolean ManualCard = false;
    public boolean PrinterEnabled = false;

    private Thread ThrNetworkClock;
    private Thread ThrDigitalClock;
    private Thread ThrMIFARE;
    private Thread ThrUpdaterClock;
    private Thread ThrSlotsClock;
    private Thread ThrShowExitCamera;
    private Thread ThrQuickUpdaterClock;

    Runtime rt = Runtime.getRuntime();
    Process proc;

    private String rootpasswd;
    public String loginID;
    public String serverIP, key;
    public String BackupMainServer, printer, printerType, slotsmode, SlotsResetEnabled, sResetTime;
    public String payuponentry;
    public boolean slotscompute = false;
    public Date busyStamp = new Date();
    public Date SavedStamp = new Date();
    private String[] computeEntry, computeExit;
    private short numofentrances, numofexits;

    public String marqueetext = "";
    LogUtility logthis = new LogUtility();
    public PoleDisplayAPI npd = new PoleDisplayAPI();
    public StringBuffer Loginput = new StringBuffer("");
    public StringBuffer Cardinput = new StringBuffer("");
    public int CardDigits = Integer.parseInt(myResources.getString("CardNumbers"));
    public int PlateDigits = Integer.parseInt(myResources.getString("PlateNumbers"));
    public int LoginDigits = Integer.parseInt(myResources.getString("LoginNumbers"));
    public String ParkingArea;
    public String trtype = "R";
    private String mastercard1, mastercard2, mastercard3, mastercard4 = "";
    private String mcardOwner1, mcardOwner2, mcardOwner3, mcardOwner4 = "";

    public String EN_SentinelID;
    public String EX_SentinelID;
    public String SlotsID;
    public String TerminalType = "";
    public String CashierID = "";
    public String CashierName = "";
    public String PrevParker = "";
    public String PrevPlate = "";
    public String settlementName;
    public String settlementAddr;
    public String settlementTIN;
    public String settlementBusStyle;
    public String settlementRef;
    //--exit
    public StringBuffer PWORDinput = new StringBuffer("");
    public StringBuffer MasterCardinput = new StringBuffer("");
    public StringBuffer Plateinput = new StringBuffer("");
    public StringBuffer VIPinput = new StringBuffer("");
    public StringBuffer Prepaidinput = new StringBuffer("");
    private DataBaseHandler dbh = new DataBaseHandler();

    ReadMIFARE mifare;

    private int width;
    private int height;
    private float buttonSize;

    //---User Inteface---
    public HybridPanelUI() {
        super();
        setUndecorated(debugMode);
        initSentinelValues();
        initComponents();
        initLoadParkerTypes();
        initLoadSlots();

        this.setExtendedState(JFrame.MAXIMIZED_BOTH);
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        //Dimension frameSize = getSize();
        width = screenSize.width;
        height = screenSize.height + 500;

        BG.setSize(new Dimension(screenSize.width, screenSize.height));
        BGPanel.setSize(new Dimension(screenSize.width, screenSize.height));
        BG.setIcon(new ImageIcon(new ImageIcon(getClass()
                .getResource("/hybrid/resources/parkingBG.jpg"))
                .getImage()
                .getScaledInstance(screenSize.width, screenSize.height, Image.SCALE_DEFAULT)));
        log.info(String.valueOf(screenSize.width) + "::"
                + String.valueOf(screenSize.height));

        //PayUponEntry
        OverrideSwitch_set2Exit(true);
        this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);

    }

    public void StartUI() {
        try {
            log.info("Loading User Interface");
            buttonSize = (float) 0.05;
            log.info("buttonSize:" + buttonSize);
            initDisplay();
            setTitle("Theoretics Inc.");
            fullKeyBoard.setVisible(false);
            WestPanel.setVisible(false);
            LowerLeftPanel.setVisible(false);
            LowerLeftPanel.setVisible(false);
            ZReadingPanel.setVisible(false);
            RefundPanel.setVisible(false);
            LogoutPanelX.setVisible(false);
            MainFuncPad.setVisible(true);
            SecretFuncPad.setVisible(false);
            SearchPanel.setVisible(false);
            ManualEntryPanel.setVisible(false);

            this.setExtendedState(JFrame.MAXIMIZED_BOTH);
            log.info("User Interface Loading... Done");
            LoginMOD la = new LoginMOD();
            la.CheckValidCashierStamp(this);
            this.addWindowFocusListener(this);
            ServerDataHandler sdh = new ServerDataHandler();
            sdh.initFolders();
            logthis.setLog(EX_SentinelID, CashierName + "  " + CashierID + "  " + "Starting Program..");
            dbh.saveLog("01", CashierID);
            initThreads();
            initDevices();

            //testShowCameraOnScreen();
            this.repaint();
            this.requestFocus();
            this.validate();
        } catch (Exception ex) {
            log.error(ex.getMessage());
        }
    }

    private void initSentinelValues() {
        try {
            log.info("Initializing Values");

            XMLreader xr = new XMLreader();
            SlotsStatus ss = new SlotsStatus();
            computeEntry = ss.loadENslots();
            computeExit = ss.loadEXslots();
            numofentrances = ss.getENslots();
            numofexits = ss.getEXslots();
            switchmode = "exit";
            ExitSwitch = true;
            errorMsg = new javax.swing.JLabel[8];
            key = xr.getElementValue("C://JTerminals/initH.xml", "key");

            BackupMainServer = xr.getElementValue("C://JTerminals/net.xml", "backupmain1");
            printer = xr.getElementValue("C://JTerminals/initH.xml", "printer");
            printerType = xr.getElementValue("C://JTerminals/initH.xml", "printerType");
            SlotsResetEnabled = xr.getElementValue("C://JTerminals/initH.xml", "reset");
            sResetTime = xr.getAttributeValue("C://JTerminals/initH.xml", "reset", "time");
            loginID = xr.getElementValue("C://JTerminals/ginH.xml", "log_id");
            TerminalType = xr.getAttributeValue("C://JTerminals/initH.xml", "terminal_id", "type");
            EN_SentinelID = xr.getElementValue("C://JTerminals/initH.xml", "HNterminal_id");
            EX_SentinelID = xr.getElementValue("C://JTerminals/initH.xml", "HXterminal_id");
            ParkingArea = xr.getElementValue("C://JTerminals/initH.xml", "area_id");
            SlotsID = xr.getElementValue("C://JTerminals/initH.xml", "slots_id");
            serverIP = xr.getElementValue("C://JTerminals/initH.xml", "server_ip");
            slotsmode = xr.getElementValue("C://JTerminals/initH.xml", "slotsmode");
            payuponentry = xr.getElementValue("C://JTerminals/initH.xml", "payuponentry");
            String scompute = xr.getElementValue("C://JTerminals/initH.xml", "slotscompute");
            EncryptionTool et = new EncryptionTool();
            rootpasswd = et.decrypt(xr.getElementValue("C://JTerminals/commandX.xml", "pwd"), "itheoretics");
            String EnableJogger = xr.getElementValue("C://JTerminals/initH.xml", "joggers");
            if (EnableJogger.compareToIgnoreCase("enabled") == 0) {
                ComputeJogger = true;
            }
            if (scompute.compareToIgnoreCase("active") == 0) {
                slotscompute = true;
            }
            MasterCardAPI mc = new MasterCardAPI();
            mastercard1 = mc.getMasterRec1();
            mastercard2 = mc.getMasterRec2();
            mastercard3 = mc.getMasterRec3();
            mastercard4 = mc.getMasterRec4();
            mcardOwner1 = mc.getMasterOwner1();
            mcardOwner2 = mc.getMasterOwner2();
            mcardOwner3 = mc.getMasterOwner3();
            mcardOwner4 = mc.getMasterOwner4();
        } catch (Exception ex) {
            log.error(ex.getMessage());
        }
    }

    private void initDisplay() {

        version.setText("V 5.1.1.1a");
        log.info("Initializing Displays");
        TerminalIDlbl.setText(EX_SentinelID);
        switchbutton1.setVisible(false);
        switchbutton2.setVisible(false);
        //SlotsInput.setVisible(false);
        Funcbutton1.setVisible(true);
        LostPanel.setVisible(false);
        ReprintPanel.setVisible(false);
        SettPanel.setVisible(settlementEnabled);
        settlementEnabled = false;
        PWDoscaID.setEditable(settlementEnabled);
        entryCamera.setText("");
        exitCamera.setText("");

        //ImagePanel panel = new ImagePanel(new ImageIcon(getClass().getResource("/hybrid/resources/wallpaper.jpg")).getImage());
        //inputPanel = panel;
        //this.Backoutlbl.setVisible(true);//login logout
        //this.Backoutlbl.setVisible(false);
        //Funcbutton4.setVisible(true);this.Backout.setVisible(false);//backout
        /*
        Funcbutton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/hybrid/resources/UL1.png")));
        Funcbutton2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/hybrid/resources/UL2.png")));
        Showslotsbutton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/hybrid/resources/UL3.png")));
        SetSlotsbutton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/hybrid/resources/UL4.png")));
        Funcbutton5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/hybrid/resources/UL5.png")));
        Funcbutton6.setIcon(new javax.swing.ImageIcon(getClass().getResource("/hybrid/resources/UL6.png")));
        Funcbutton7.setIcon(new javax.swing.ImageIcon(getClass().getResource("/hybrid/resources/UL7.png")));
        Funcbutton8.setIcon(new javax.swing.ImageIcon(getClass().getResource("/hybrid/resources/UL8.png")));
        XFunc1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/hybrid/resources/UR1.png")));
        XFunc2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/hybrid/resources/UR2.png")));
        XFunc3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/hybrid/resources/UR3.png")));
        XFunc4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/hybrid/resources/UR4.png")));
        XFunc5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/hybrid/resources/UR5.png")));
        XFunc6.setIcon(new javax.swing.ImageIcon(getClass().getResource("/hybrid/resources/UR6.png")));
        XFunc7.setIcon(new javax.swing.ImageIcon(getClass().getResource("/hybrid/resources/UR7.png")));
        XFunc8.setIcon(new javax.swing.ImageIcon(getClass().getResource("/hybrid/resources/UR8.png")));
         */
        //Switch_Entry.setIcon(new javax.swing.ImageIcon(getClass().getResource("/hybrid/resources/SwitchEntryA.png")));
        //Switch_Exit.setIcon(new javax.swing.ImageIcon(getClass().getResource("/hybrid/resources/SwitchExit.png")));
        MainFuncPad.setVisible(false);
        PasswordPanel.setVisible(false);
        MasterCardPanel.setVisible(false);
        CouponPanel.setVisible(false);
        try {
            //EpsonHandler ea = new USBEpsonHandler();
            //if (ea.PrinterEnabled) {
            //ea.initializePrinter();
            //ea.printTestHEADER();
            //ea.closeprinter();
            //}
            ImageIcon imageIcon = new ImageIcon(getClass().getResource("/hybrid/resources/qwerty.png")); // load the image to a imageIcon
            Image image = imageIcon.getImage(); // transform it
            Image newimg = image.getScaledInstance((int) Math.round(width * buttonSize), (int) Math.round(height * buttonSize), java.awt.Image.SCALE_SMOOTH); // scale it the smooth way
            imageIcon = new ImageIcon(newimg);  // transform it back

            A.setIcon(imageIcon);
            B.setIcon(imageIcon);
            C.setIcon(imageIcon);
            D.setIcon(imageIcon);
            E.setIcon(imageIcon);
            F.setIcon(imageIcon);
            G.setIcon(imageIcon);
            H.setIcon(imageIcon);
            I.setIcon(imageIcon);
            J.setIcon(imageIcon);
            K.setIcon(imageIcon);
            L.setIcon(imageIcon);
            M.setIcon(imageIcon);
            N.setIcon(imageIcon);
            O.setIcon(imageIcon);
            P.setIcon(imageIcon);
            Q.setIcon(imageIcon);
            R.setIcon(imageIcon);
            S.setIcon(imageIcon);
            T.setIcon(imageIcon);
            U.setIcon(imageIcon);
            V.setIcon(imageIcon);
            W.setIcon(imageIcon);
            X.setIcon(imageIcon);
            Y.setIcon(imageIcon);
            Z.setIcon(imageIcon);

            //bg.setIcon(new javax.swing.ImageIcon(getClass().getResource("/hybrid/resources/Numpad_midGreen.png")));
            imageIcon = new ImageIcon(getClass().getResource("/hybrid/resources/Numpad_midGreen.png")); // load the image to a imageIcon
            image = imageIcon.getImage(); // transform it
            newimg = image.getScaledInstance((int) Math.round(width * buttonSize), (int) Math.round(height * buttonSize), java.awt.Image.SCALE_SMOOTH); // scale it the smooth way
            imageIcon = new ImageIcon(newimg);  // transform it back
            num9.setIcon(imageIcon);
            num8.setIcon(imageIcon);
            num7.setIcon(imageIcon);
            num6.setIcon(imageIcon);
            num5.setIcon(imageIcon);
            num4.setIcon(imageIcon);
            num3.setIcon(imageIcon);
            num2.setIcon(imageIcon);
            num1.setIcon(imageIcon);
            num0.setIcon(imageIcon);
            numBlank.setIcon(imageIcon);
            AllClear.setIcon(imageIcon);

        } catch (Exception ex) {
            log.error(ex.getMessage());
        }

        //showHelp();
        //background.setIcon(new javax.swing.ImageIcon(getClass().getResource("/hybrid/resources/New_menu_blank.jpg")));
        log.info("Loading Motorist Pole Display");
        //npd.dispose();
        //npd.setUndecorated(true);
        //npd.toBack();
        //npd.setVisible(true);
        RightMsgPanellbl.setVisible(false);
        LoginPanelX.setVisible(false);
        try {
            //SBoardHandler sh = new SBoardHandler();
            //sh.sendWELCOME();
            //sh.sendOPEN();
        } catch (Exception ex) {
            log.error(ex.getMessage());
        }
        try {
            //SBoardHandler sh = new SBoardHandler();
            //String mout = sh.getmarqueeout(ParkingArea);
            //sh.SendMarqueeText(mout, 0x35);
        } catch (Exception ex) {
            log.error(ex.getMessage());
        }
        try {
            NOSfiles nf = new NOSfiles();
            ServedNo.setText(nf.getCarServed());
            ServedExit.setText(nf.getExitCarServed());
            EntryTickets.setText(nf.getEntryTicketsServed());
            ExitTickets.setText(nf.getExitTicketsServed());
        } catch (IOException ex) {
            log.error(ex.getMessage());
        }
    }

    private void initThreads() {
        //Thread pd = new Thread(npd);
        //pd.start();
        DigitalClock dc = new DigitalClock();
        ThrDigitalClock = new Thread(dc);
        ThrDigitalClock.start();
        NetworkClock nc = new NetworkClock();
        ThrNetworkClock = new Thread(nc);
        ThrNetworkClock.start();
        //new SubServerCopier();
        OnlineUpdater oc = new OnlineUpdater();
        ThrUpdaterClock = new Thread(oc);
        ThrUpdaterClock.start();
        SlotsComputer sc = new SlotsComputer(slotscompute);
        ThrSlotsClock = new Thread(sc);
        ThrSlotsClock.start();
        ShowExitCamera sec = new ShowExitCamera();
        ThrShowExitCamera = new Thread(sec);
        ThrShowExitCamera.start();
        //OnlineQuickUpdater qc = new OnlineQuickUpdater();
        //ThrQuickUpdaterClock = new Thread(qc);
        //ThrQuickUpdaterClock.start();
        this.ThrDigitalClock.setPriority(8);
        this.ThrDigitalClock.setPriority(7);
        this.ThrNetworkClock.setPriority(6);
        //this.ThrUpdaterClock.setPriority(5);
        //this.ThrQuickUpdaterClock.setPriority(4);
    }

    private void initDevices() {
        startMIFAREReader();
        MIFAREpolling nc = new MIFAREpolling(this);
        ThrMIFARE = new Thread(nc);
        ThrMIFARE.start();
        this.ThrMIFARE.setPriority(8);
    }
    //------------------

    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        Switch = new javax.swing.JLabel();
        Mode = new javax.swing.JLabel();
        switchbutton1 = new javax.swing.JLabel();
        Special = new javax.swing.JLabel();
        Parkers = new javax.swing.JLabel();
        switchbutton2 = new javax.swing.JLabel();
        SUBMIT = new javax.swing.JLabel();
        ticketsPanel = new javax.swing.JPanel();
        exitTicket = new javax.swing.JButton();
        MidMsgPanel = new javax.swing.JPanel();
        ServerInfo10 = new javax.swing.JLabel();
        ServedNo = new javax.swing.JLabel();
        ServerInfo11 = new javax.swing.JLabel();
        ServedExit = new javax.swing.JLabel();
        Cnamelbl = new javax.swing.JLabel();
        TellerName = new javax.swing.JLabel();
        TicketMsgPanel = new javax.swing.JPanel();
        Ticketslbl = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        EntryTickets = new javax.swing.JLabel();
        ExitTickets = new javax.swing.JLabel();
        TicketPlusOne = new javax.swing.JButton();
        CouponPanel = new javax.swing.JPanel();
        CouponNolbl = new javax.swing.JLabel();
        PrepaidInput2 = new javax.swing.JLabel();
        HybridPanel = new javax.swing.JPanel();
        Switch_Entry = new javax.swing.JLabel();
        Switch_Exit = new javax.swing.JLabel();
        settTIN = new javax.swing.JTextField();
        settLbl2 = new javax.swing.JLabel();
        settLbl1 = new javax.swing.JLabel();
        settName = new javax.swing.JTextField();
        settLbl = new javax.swing.JLabel();
        CameraPanel = new javax.swing.JPanel();
        Camera = new javax.swing.JLabel();
        logcancelbutton = new javax.swing.JLabel();
        LogUsercode = new javax.swing.JTextField();
        LogPassword = new javax.swing.JPasswordField();
        LoginButton = new javax.swing.JButton();
        Funcbutton7 = new javax.swing.JLabel();
        Funcbutton3 = new javax.swing.JLabel();
        XFunc6 = new javax.swing.JLabel();
        Funcbutton6 = new javax.swing.JLabel();
        Funcbutton4 = new javax.swing.JLabel();
        XFunc4 = new javax.swing.JLabel();
        XFunc2 = new javax.swing.JLabel();
        spacer = new javax.swing.JLabel();
        fullScreenCamera = new javax.swing.JLabel();
        ManualEntryPanel = new javax.swing.JPanel();
        closeButton1 = new javax.swing.JButton();
        jLabel5 = new javax.swing.JLabel();
        manualEntryTime = new com.github.lgooddatepicker.components.TimePicker();
        manualEntryDate = new datechooser.beans.DateChooserCombo();
        Create = new javax.swing.JButton();
        manualEntryPlate = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        SearchPanel = new javax.swing.JPanel();
        SearchDisplayPanel = new javax.swing.JPanel();
        closeButton = new javax.swing.JButton();
        SearchDateTo = new datechooser.beans.DateChooserCombo();
        SearchTimeTo = new com.github.lgooddatepicker.components.TimePicker();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        SearchTimeFrom = new com.github.lgooddatepicker.components.TimePicker();
        SearchDateFrom = new datechooser.beans.DateChooserCombo();
        jButton1 = new javax.swing.JButton();
        PasswordPanel = new javax.swing.JPanel();
        PWORDlbl = new javax.swing.JLabel();
        PWORDInput2 = new javax.swing.JLabel();
        LoginPanelX = new javax.swing.JPanel();
        LoginLbl = new javax.swing.JLabel();
        LogUsercode2 = new javax.swing.JTextField();
        LoginLbl1 = new javax.swing.JLabel();
        LogPassword2 = new javax.swing.JPasswordField();
        LoginLbl2 = new javax.swing.JLabel();
        LoginButton2 = new javax.swing.JButton();
        LogoutPanelX = new javax.swing.JPanel();
        LogoutLbl = new javax.swing.JLabel();
        LogUsercode1 = new javax.swing.JTextField();
        LogoutLbl1 = new javax.swing.JLabel();
        LogPassword1 = new javax.swing.JPasswordField();
        LogoutLbl2 = new javax.swing.JLabel();
        LoginButton1 = new javax.swing.JButton();
        MasterCardPanel = new javax.swing.JPanel();
        MasterCardLbl = new javax.swing.JLabel();
        MasterCardInput2 = new javax.swing.JLabel();
        reprintButton = new javax.swing.JButton();
        BGPanel = new javax.swing.JPanel();
        NorthPanel = new javax.swing.JPanel();
        ProductName = new javax.swing.JLabel();
        ClientName = new javax.swing.JLabel();
        LeftMsgPanel = new javax.swing.JPanel();
        Labels = new javax.swing.JPanel();
        ServerInfo1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        Status = new javax.swing.JPanel();
        ServerStatus = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        slotsPanel = new javax.swing.JPanel();
        slotsType = new javax.swing.JLabel();
        carsMinusbtn = new javax.swing.JLabel();
        carsNum = new javax.swing.JTextField();
        carsPlusbtn = new javax.swing.JLabel();
        sep = new javax.swing.JLabel();
        slotsType2 = new javax.swing.JLabel();
        motorMinusbtn = new javax.swing.JLabel();
        motorNum = new javax.swing.JTextField();
        motorPlusbtn = new javax.swing.JLabel();
        SouthPanel = new javax.swing.JPanel();
        version = new javax.swing.JLabel();
        RDatePanel = new javax.swing.JPanel();
        DateLabel = new javax.swing.JLabel();
        datedisplay = new javax.swing.JLabel();
        daydisplay = new javax.swing.JLabel();
        TimeLabel = new javax.swing.JLabel();
        timedisplay = new javax.swing.JLabel();
        spacer1 = new javax.swing.JLabel();
        RDatePanel1 = new javax.swing.JPanel();
        spacer4 = new javax.swing.JLabel();
        SentinelIDlbl = new javax.swing.JLabel();
        TerminalIDlbl = new javax.swing.JLabel();
        spacer2 = new javax.swing.JLabel();
        spacer3 = new javax.swing.JLabel();
        CenterPanel = new javax.swing.JPanel();
        newMidPanel = new javax.swing.JPanel();
        inputPanel = new javax.swing.JPanel();
        CardNumberlbl = new javax.swing.JLabel();
        CardInput2 = new javax.swing.JLabel();
        PlateNumberlbl = new javax.swing.JLabel();
        PlateInput2 = new javax.swing.JLabel();
        MainPanel = new javax.swing.JPanel();
        amntlbl1 = new javax.swing.JLabel();
        amntlbl = new javax.swing.JLabel();
        AMOUNTdisplay = new javax.swing.JLabel();
        AmtTendered = new javax.swing.JTextField();
        amntlbl2 = new javax.swing.JLabel();
        amntlbl3 = new javax.swing.JLabel();
        ChangeDisplay = new javax.swing.JLabel();
        LeftMIDMsgPanel = new javax.swing.JPanel();
        SysMessage1 = new javax.swing.JLabel();
        SysMessage11 = new javax.swing.JLabel();
        SysMessage2 = new javax.swing.JLabel();
        SysMessage12 = new javax.swing.JLabel();
        SysMessage3 = new javax.swing.JLabel();
        SysMessage13 = new javax.swing.JLabel();
        SysMessage4 = new javax.swing.JLabel();
        SysMessage14 = new javax.swing.JLabel();
        SysMessage5 = new javax.swing.JLabel();
        SysMessage15 = new javax.swing.JLabel();
        SysMessage6 = new javax.swing.JLabel();
        SysMessage16 = new javax.swing.JLabel();
        SysMessage7 = new javax.swing.JLabel();
        SysMessage17 = new javax.swing.JLabel();
        SysMessage8 = new javax.swing.JLabel();
        SysMessage18 = new javax.swing.JLabel();
        SysMessage9 = new javax.swing.JLabel();
        SysMessage19 = new javax.swing.JLabel();
        SysMessage10 = new javax.swing.JLabel();
        SysMessage20 = new javax.swing.JLabel();
        RightMsgPanellbl = new javax.swing.JPanel();
        ParkerInfo11 = new javax.swing.JLabel();
        ParkerInfo1 = new javax.swing.JLabel();
        ParkerInfo12 = new javax.swing.JLabel();
        ParkerInfo2 = new javax.swing.JLabel();
        ParkerInfo13 = new javax.swing.JLabel();
        ParkerInfo3 = new javax.swing.JLabel();
        ParkerInfo14 = new javax.swing.JLabel();
        ParkerInfo4 = new javax.swing.JLabel();
        ParkerInfo15 = new javax.swing.JLabel();
        ParkerInfo5 = new javax.swing.JLabel();
        ParkerInfo16 = new javax.swing.JLabel();
        ParkerInfo6 = new javax.swing.JLabel();
        ParkerInfo17 = new javax.swing.JLabel();
        ParkerInfo7 = new javax.swing.JLabel();
        ParkerInfo18 = new javax.swing.JLabel();
        ParkerInfo8 = new javax.swing.JLabel();
        ParkerInfo19 = new javax.swing.JLabel();
        ParkerInfo9 = new javax.swing.JLabel();
        ParkerInfo20 = new javax.swing.JLabel();
        ParkerInfo10 = new javax.swing.JLabel();
        LowerLeftPanelX = new javax.swing.JPanel();
        LowerLeftPanel = new javax.swing.JPanel();
        CollectionDate = new datechooser.beans.DateChooserCombo();
        cashColLbl = new javax.swing.JLabel();
        printCollLbl = new javax.swing.JLabel();
        printCollectionBtn = new javax.swing.JLabel();
        searchLbl = new javax.swing.JLabel();
        searchCollectionBtn = new javax.swing.JLabel();
        XReadingListField = new javax.swing.JScrollPane();
        xreadList = new javax.swing.JList<>();
        ZReadingPanel = new javax.swing.JPanel();
        ZReadingDate = new datechooser.beans.DateChooserCombo();
        zreadLbl = new javax.swing.JLabel();
        printCollLbl1 = new javax.swing.JLabel();
        printZReadingBtn = new javax.swing.JLabel();
        LowerRightPanel = new javax.swing.JPanel();
        SettPanel = new javax.swing.JPanel();
        PWDoscaID = new javax.swing.JTextField();
        oscaName = new javax.swing.JTextField();
        oscaAddr = new javax.swing.JTextField();
        oscaTIN = new javax.swing.JTextField();
        oscaBusStyle = new javax.swing.JTextField();
        settLbl3 = new javax.swing.JLabel();
        settLbl5 = new javax.swing.JLabel();
        settLbl7 = new javax.swing.JLabel();
        settLbl4 = new javax.swing.JLabel();
        settLbl6 = new javax.swing.JLabel();
        LostPanel = new javax.swing.JPanel();
        lostLbl = new javax.swing.JLabel();
        lostLbl1 = new javax.swing.JLabel();
        lostLbl2 = new javax.swing.JLabel();
        lostDateIN = new datechooser.beans.DateChooserCombo();
        goLostBtn = new javax.swing.JButton();
        lostTimeIN = new com.github.lgooddatepicker.components.TimePicker();
        ReprintPanel = new javax.swing.JPanel();
        reprintLbl1 = new javax.swing.JLabel();
        reprintPlate = new javax.swing.JTextField();
        reprintLbl2 = new javax.swing.JLabel();
        reprintBtn = new javax.swing.JButton();
        reprintOut = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        jList1 = new javax.swing.JList<>();
        RefundPanel = new javax.swing.JPanel();
        voidLabel = new javax.swing.JLabel();
        voidSelection = new javax.swing.JTextField();
        refundBtn = new javax.swing.JButton();
        refundOut = new javax.swing.JButton();
        voidTypeSelection = new javax.swing.JComboBox<>();
        transactions4Void = new javax.swing.JScrollPane();
        trans4Void = new javax.swing.JList<>();
        fullKeyBoard = new javax.swing.JPanel();
        keypadPanel = new javax.swing.JPanel();
        LettersPad = new javax.swing.JPanel();
        KeyLabel17 = new javax.swing.JLabel();
        KeyLabel23 = new javax.swing.JLabel();
        KeyLabel5 = new javax.swing.JLabel();
        KeyLabel18 = new javax.swing.JLabel();
        KeyLabel20 = new javax.swing.JLabel();
        KeyLabel25 = new javax.swing.JLabel();
        KeyLabel21 = new javax.swing.JLabel();
        KeyLabel9 = new javax.swing.JLabel();
        KeyLabel15 = new javax.swing.JLabel();
        KeyLabel16 = new javax.swing.JLabel();
        KeyLabel1 = new javax.swing.JLabel();
        KeyLabel19 = new javax.swing.JLabel();
        KeyLabel4 = new javax.swing.JLabel();
        KeyLabel6 = new javax.swing.JLabel();
        KeyLabel7 = new javax.swing.JLabel();
        KeyLabel8 = new javax.swing.JLabel();
        KeyLabel10 = new javax.swing.JLabel();
        KeyLabel11 = new javax.swing.JLabel();
        KeyLabel12 = new javax.swing.JLabel();
        KeyLabel28 = new javax.swing.JLabel();
        KeyLabel26 = new javax.swing.JLabel();
        KeyLabel24 = new javax.swing.JLabel();
        KeyLabel3 = new javax.swing.JLabel();
        KeyLabel22 = new javax.swing.JLabel();
        KeyLabel2 = new javax.swing.JLabel();
        KeyLabel14 = new javax.swing.JLabel();
        KeyLabel13 = new javax.swing.JLabel();
        KeyLabel27 = new javax.swing.JLabel();
        KeyLabel29 = new javax.swing.JLabel();
        ButtonPad = new javax.swing.JPanel();
        Button17 = new javax.swing.JLabel();
        Button23 = new javax.swing.JLabel();
        Button5 = new javax.swing.JLabel();
        Button18 = new javax.swing.JLabel();
        Button20 = new javax.swing.JLabel();
        Button25 = new javax.swing.JLabel();
        Button21 = new javax.swing.JLabel();
        Button9 = new javax.swing.JLabel();
        Button15 = new javax.swing.JLabel();
        Button16 = new javax.swing.JLabel();
        Button1 = new javax.swing.JLabel();
        Button19 = new javax.swing.JLabel();
        Button4 = new javax.swing.JLabel();
        Button6 = new javax.swing.JLabel();
        Button7 = new javax.swing.JLabel();
        Button8 = new javax.swing.JLabel();
        Button10 = new javax.swing.JLabel();
        Button11 = new javax.swing.JLabel();
        Button12 = new javax.swing.JLabel();
        Button28 = new javax.swing.JLabel();
        Button26 = new javax.swing.JLabel();
        Button24 = new javax.swing.JLabel();
        Button3 = new javax.swing.JLabel();
        Button22 = new javax.swing.JLabel();
        Button2 = new javax.swing.JLabel();
        Button14 = new javax.swing.JLabel();
        Button13 = new javax.swing.JLabel();
        Button27 = new javax.swing.JLabel();
        Button29 = new javax.swing.JLabel();
        ButtonPadBG = new javax.swing.JPanel();
        Q = new javax.swing.JLabel();
        W = new javax.swing.JLabel();
        E = new javax.swing.JLabel();
        R = new javax.swing.JLabel();
        T = new javax.swing.JLabel();
        Y = new javax.swing.JLabel();
        U = new javax.swing.JLabel();
        I = new javax.swing.JLabel();
        O = new javax.swing.JLabel();
        P = new javax.swing.JLabel();
        A = new javax.swing.JLabel();
        S = new javax.swing.JLabel();
        D = new javax.swing.JLabel();
        F = new javax.swing.JLabel();
        G = new javax.swing.JLabel();
        H = new javax.swing.JLabel();
        J = new javax.swing.JLabel();
        K = new javax.swing.JLabel();
        L = new javax.swing.JLabel();
        ENTER = new javax.swing.JLabel();
        Z = new javax.swing.JLabel();
        X = new javax.swing.JLabel();
        C = new javax.swing.JLabel();
        V = new javax.swing.JLabel();
        B = new javax.swing.JLabel();
        N = new javax.swing.JLabel();
        M = new javax.swing.JLabel();
        DASH = new javax.swing.JLabel();
        BACKSPACE = new javax.swing.JLabel();
        numpadPanel = new javax.swing.JPanel();
        NumLabelPad = new javax.swing.JPanel();
        NumPadlbl7 = new javax.swing.JLabel();
        NumPadlbl8 = new javax.swing.JLabel();
        NumPadlbl9 = new javax.swing.JLabel();
        NumPadlbl4 = new javax.swing.JLabel();
        NumPadlbl5 = new javax.swing.JLabel();
        NumPadlbl6 = new javax.swing.JLabel();
        NumPadlbl1 = new javax.swing.JLabel();
        NumPadlbl2 = new javax.swing.JLabel();
        NumPadlbl3 = new javax.swing.JLabel();
        NumPadlbl10 = new javax.swing.JLabel();
        NumPadlbl11 = new javax.swing.JLabel();
        NumPadlblAC = new javax.swing.JLabel();
        NumButtonPad = new javax.swing.JPanel();
        NumButton7 = new javax.swing.JLabel();
        NumButton8 = new javax.swing.JLabel();
        NumButton9 = new javax.swing.JLabel();
        NumButton4 = new javax.swing.JLabel();
        NumButton5 = new javax.swing.JLabel();
        NumButton6 = new javax.swing.JLabel();
        NumButton1 = new javax.swing.JLabel();
        NumButton2 = new javax.swing.JLabel();
        NumButton3 = new javax.swing.JLabel();
        NumButton10 = new javax.swing.JLabel();
        NumButton11 = new javax.swing.JLabel();
        NumButtonAC = new javax.swing.JLabel();
        NumButtonBG = new javax.swing.JPanel();
        num7 = new javax.swing.JLabel();
        num8 = new javax.swing.JLabel();
        num9 = new javax.swing.JLabel();
        num4 = new javax.swing.JLabel();
        num5 = new javax.swing.JLabel();
        num6 = new javax.swing.JLabel();
        num1 = new javax.swing.JLabel();
        num2 = new javax.swing.JLabel();
        num3 = new javax.swing.JLabel();
        num0 = new javax.swing.JLabel();
        numBlank = new javax.swing.JLabel();
        AllClear = new javax.swing.JLabel();
        WestPanel = new javax.swing.JPanel();
        MainFuncPad = new javax.swing.JPanel();
        Funcbutton1 = new javax.swing.JLabel();
        XFunc5 = new javax.swing.JLabel();
        XFunc7 = new javax.swing.JLabel();
        XFunc8 = new javax.swing.JLabel();
        MasterFun = new javax.swing.JLabel();
        jSeparator1 = new javax.swing.JSeparator();
        XFunc11 = new javax.swing.JLabel();
        XFunc12 = new javax.swing.JLabel();
        SecretFuncPad = new javax.swing.JPanel();
        XFunc9 = new javax.swing.JLabel();
        XFunc10 = new javax.swing.JLabel();
        jSeparator2 = new javax.swing.JSeparator();
        EastPanel = new javax.swing.JPanel();
        adminPanel = new javax.swing.JPanel();
        admin1 = new javax.swing.JLabel();
        admin2 = new javax.swing.JLabel();
        admin3 = new javax.swing.JLabel();
        admin4 = new javax.swing.JLabel();
        admin5 = new javax.swing.JLabel();
        admin6 = new javax.swing.JLabel();
        admin7 = new javax.swing.JLabel();
        admin8 = new javax.swing.JLabel();
        errorPanels = new javax.swing.JPanel();
        error1 = new javax.swing.JLabel();
        error2 = new javax.swing.JLabel();
        error3 = new javax.swing.JLabel();
        error4 = new javax.swing.JLabel();
        errorMsg5 = new javax.swing.JLabel();
        errorMsg6 = new javax.swing.JLabel();
        errorMsg7 = new javax.swing.JLabel();
        errorMsg8 = new javax.swing.JLabel();
        ExitFuncPad = new javax.swing.JPanel();
        CamPanel = new javax.swing.JPanel();
        entryCamera = new javax.swing.JLabel();
        exitCamera = new javax.swing.JLabel();
        background = new javax.swing.JLabel();
        BG = new javax.swing.JLabel();

        Switch.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        Switch.setForeground(new java.awt.Color(255, 255, 255));
        Switch.setLabelFor(switchbutton1);
        Switch.setText("SWITCH"); // NOI18N
        Switch.setFocusable(false);
        Switch.setInheritsPopupMenu(false);
        Switch.setRequestFocusEnabled(false);
        Switch.setVerifyInputWhenFocusTarget(false);

        Mode.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        Mode.setForeground(new java.awt.Color(255, 255, 255));
        Mode.setLabelFor(switchbutton1);
        Mode.setText("MODE"); // NOI18N
        Mode.setFocusable(false);
        Mode.setInheritsPopupMenu(false);
        Mode.setRequestFocusEnabled(false);
        Mode.setVerifyInputWhenFocusTarget(false);

        switchbutton1.setLabelFor(Switch);
        switchbutton1.setIconTextGap(2);
        switchbutton1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                SwitchModes(evt);
            }
        });
        switchbutton1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                Funckeymouseover(evt.getSource());
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                Funckeymouseoff(evt.getSource());
            }
        });

        Special.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        Special.setForeground(new java.awt.Color(255, 255, 255));
        Special.setLabelFor(switchbutton2);
        Special.setText("Special"); // NOI18N
        Special.setFocusable(false);
        Special.setInheritsPopupMenu(false);
        Special.setRequestFocusEnabled(false);
        Special.setVerifyInputWhenFocusTarget(false);

        Parkers.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        Parkers.setForeground(new java.awt.Color(255, 255, 255));
        Parkers.setLabelFor(switchbutton2);
        Parkers.setText("Parkers"); // NOI18N
        Parkers.setFocusable(false);
        Parkers.setInheritsPopupMenu(false);
        Parkers.setRequestFocusEnabled(false);
        Parkers.setVerifyInputWhenFocusTarget(false);

        switchbutton2.setLabelFor(Special);
        switchbutton2.setIconTextGap(2);
        switchbutton2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                goSpecial(evt);
            }
        });
        switchbutton2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                Funckeymouseover(evt.getSource());
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                Funckeymouseoff(evt.getSource());
            }
        });

        SUBMIT.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        SUBMIT.setForeground(new java.awt.Color(255, 255, 255));
        SUBMIT.setLabelFor(switchbutton2);
        SUBMIT.setText("SUBMIT"); // NOI18N
        SUBMIT.setFocusable(false);
        SUBMIT.setInheritsPopupMenu(false);
        SUBMIT.setRequestFocusEnabled(false);
        SUBMIT.setVerifyInputWhenFocusTarget(false);

        ticketsPanel.setEnabled(false);
        ticketsPanel.setOpaque(false);

        exitTicket.setBackground(new java.awt.Color(137, 188, 211));
        exitTicket.setFont(new java.awt.Font("DejaVu LGC Sans", 1, 11)); // NOI18N
        exitTicket.setIcon(new javax.swing.ImageIcon(getClass().getResource("/hybrid/resources/button1-sup.png"))); // NOI18N
        exitTicket.setBorder(null);
        exitTicket.setBorderPainted(false);
        exitTicket.setContentAreaFilled(false);
        exitTicket.setFocusable(false);
        exitTicket.setName("exitTicket"); // NOI18N
        exitTicket.setRolloverIcon(new javax.swing.ImageIcon(getClass().getResource("/hybrid/resources/button1-sdown.png"))); // NOI18N
        exitTicket.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                exitTicketMousePressed(evt);
            }
        });
        ticketsPanel.add(exitTicket);

        MidMsgPanel.setFocusable(false);
        MidMsgPanel.setOpaque(false);
        MidMsgPanel.setLayout(new java.awt.GridLayout(3, 2));

        ServerInfo10.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        ServerInfo10.setForeground(new java.awt.Color(255, 255, 255));
        ServerInfo10.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        ServerInfo10.setText("ENTRY SERVED"); // NOI18N
        MidMsgPanel.add(ServerInfo10);

        ServedNo.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        ServedNo.setForeground(new java.awt.Color(255, 204, 0));
        ServedNo.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        ServedNo.setText("-"); // NOI18N
        MidMsgPanel.add(ServedNo);

        ServerInfo11.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        ServerInfo11.setForeground(new java.awt.Color(255, 255, 255));
        ServerInfo11.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        ServerInfo11.setText("EXIT SERVED"); // NOI18N
        MidMsgPanel.add(ServerInfo11);

        ServedExit.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        ServedExit.setForeground(new java.awt.Color(255, 204, 0));
        ServedExit.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        ServedExit.setText("-"); // NOI18N
        MidMsgPanel.add(ServedExit);

        Cnamelbl.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        Cnamelbl.setForeground(new java.awt.Color(255, 255, 255));
        Cnamelbl.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        Cnamelbl.setText("Cashier Name:"); // NOI18N
        MidMsgPanel.add(Cnamelbl);

        TellerName.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        TellerName.setForeground(new java.awt.Color(255, 255, 255));
        TellerName.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        TellerName.setText("-"); // NOI18N
        MidMsgPanel.add(TellerName);

        ticketsPanel.add(MidMsgPanel);

        TicketMsgPanel.setFocusable(false);
        TicketMsgPanel.setOpaque(false);
        TicketMsgPanel.setLayout(new java.awt.GridLayout(2, 1));

        Ticketslbl.setFont(new java.awt.Font("DejaVu LGC Sans", 0, 8)); // NOI18N
        Ticketslbl.setForeground(new java.awt.Color(254, 247, 247));
        Ticketslbl.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        Ticketslbl.setText("TICKETS");
        TicketMsgPanel.add(Ticketslbl);

        jPanel1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 255, 255)));
        jPanel1.setFocusable(false);
        jPanel1.setOpaque(false);
        jPanel1.setLayout(new java.awt.GridLayout(2, 0));

        EntryTickets.setForeground(new java.awt.Color(247, 217, 101));
        EntryTickets.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        EntryTickets.setText("-");
        jPanel1.add(EntryTickets);

        ExitTickets.setForeground(new java.awt.Color(247, 217, 101));
        ExitTickets.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        ExitTickets.setText("-");
        jPanel1.add(ExitTickets);

        TicketMsgPanel.add(jPanel1);

        ticketsPanel.add(TicketMsgPanel);

        TicketPlusOne.setBackground(new java.awt.Color(137, 188, 211));
        TicketPlusOne.setFont(new java.awt.Font("DejaVu LGC Sans", 1, 11)); // NOI18N
        TicketPlusOne.setIcon(new javax.swing.ImageIcon(getClass().getResource("/hybrid/resources/button1+sup.png"))); // NOI18N
        TicketPlusOne.setBorder(null);
        TicketPlusOne.setBorderPainted(false);
        TicketPlusOne.setContentAreaFilled(false);
        TicketPlusOne.setFocusable(false);
        TicketPlusOne.setRolloverIcon(new javax.swing.ImageIcon(getClass().getResource("/hybrid/resources/button1+sdown.png"))); // NOI18N
        TicketPlusOne.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                TicketPlusOneMousePressed(evt);
            }
        });
        ticketsPanel.add(TicketPlusOne);

        CouponPanel.setOpaque(false);
        CouponPanel.setLayout(null);

        CouponNolbl.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        CouponNolbl.setForeground(new java.awt.Color(255, 255, 51));
        CouponNolbl.setText("COUPON No:"); // NOI18N
        CouponNolbl.setFocusable(false);
        CouponNolbl.setInheritsPopupMenu(false);
        CouponNolbl.setRequestFocusEnabled(false);
        CouponNolbl.setVerifyInputWhenFocusTarget(false);
        CouponPanel.add(CouponNolbl);
        CouponNolbl.setBounds(10, 20, 160, 15);

        PrepaidInput2.setBackground(new java.awt.Color(255, 255, 204));
        PrepaidInput2.setFont(new java.awt.Font("Arial Narrow", 0, 38)); // NOI18N
        PrepaidInput2.setForeground(new java.awt.Color(167, 122, 6));
        PrepaidInput2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        PrepaidInput2.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        PrepaidInput2.setOpaque(true);
        CouponPanel.add(PrepaidInput2);
        PrepaidInput2.setBounds(130, 0, 187, 48);

        HybridPanel.setEnabled(false);
        HybridPanel.setOpaque(false);
        HybridPanel.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.CENTER, 0, 0));

        Switch_Entry.setText("Entry");
        Switch_Entry.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                Switch_EntryMousePressed(evt);
            }
        });
        HybridPanel.add(Switch_Entry);

        Switch_Exit.setText("Exit");
        Switch_Exit.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                Switch_ExitMousePressed(evt);
            }
        });
        HybridPanel.add(Switch_Exit);

        settTIN.setEditable(false);
        settTIN.setBackground(new java.awt.Color(204, 204, 204));
        settTIN.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        settTIN.setForeground(new java.awt.Color(255, 255, 102));
        settTIN.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        settTIN.setOpaque(false);

        settLbl2.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        settLbl2.setForeground(new java.awt.Color(255, 255, 204));
        settLbl2.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        settLbl2.setText("TIN:   ");
        settLbl2.setFocusable(false);
        settLbl2.setInheritsPopupMenu(false);
        settLbl2.setRequestFocusEnabled(false);
        settLbl2.setVerifyInputWhenFocusTarget(false);

        settLbl1.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        settLbl1.setForeground(new java.awt.Color(255, 255, 204));
        settLbl1.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        settLbl1.setText("Addr:   ");
        settLbl1.setFocusable(false);
        settLbl1.setInheritsPopupMenu(false);
        settLbl1.setRequestFocusEnabled(false);
        settLbl1.setVerifyInputWhenFocusTarget(false);

        settName.setEditable(false);
        settName.setBackground(new java.awt.Color(204, 204, 204));
        settName.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        settName.setForeground(new java.awt.Color(255, 255, 102));
        settName.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        settName.setOpaque(false);

        settLbl.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        settLbl.setForeground(new java.awt.Color(255, 255, 204));
        settLbl.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        settLbl.setText("Name:   ");
        settLbl.setFocusable(false);
        settLbl.setInheritsPopupMenu(false);
        settLbl.setRequestFocusEnabled(false);
        settLbl.setVerifyInputWhenFocusTarget(false);

        CameraPanel.setOpaque(false);
        CameraPanel.setLayout(null);

        Camera.setIcon(new javax.swing.ImageIcon(getClass().getResource("/hybrid/resources/coke_zero.jpg"))); // NOI18N
        Camera.setText("jLabel3");
        CameraPanel.add(Camera);
        Camera.setBounds(334, 280, 540, 320);

        logcancelbutton.setText("jLabel3");

        LogUsercode.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        LogUsercode.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N

        LogPassword.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        LogPassword.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N

        LoginButton.setText("Login");
        LoginButton.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        LoginButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                LoginButtonMouseClicked(evt);
            }
        });

        Funcbutton7.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        Funcbutton7.setIcon(new javax.swing.ImageIcon(getClass().getResource("/hybrid/resources/keypad.png"))); // NOI18N
        Funcbutton7.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        Funcbutton7.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);
        Funcbutton7.setVerticalTextPosition(javax.swing.SwingConstants.TOP);
        Funcbutton7.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                FB7MousePressed(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                FB7MouseReleased(evt);
            }
        });

        Funcbutton3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/hybrid/resources/retail.png"))); // NOI18N
        Funcbutton3.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                FB3MousePressed(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                FB3MouseReleased(evt);
            }
        });

        XFunc6.setIcon(new javax.swing.ImageIcon(getClass().getResource("/hybrid/resources/delivery.png"))); // NOI18N
        XFunc6.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                XFunc6MousePressed(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                XFunc6MouseReleased(evt);
            }
        });

        Funcbutton6.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        Funcbutton6.setIcon(new javax.swing.ImageIcon(getClass().getResource("/hybrid/resources/motorcycle.png"))); // NOI18N
        Funcbutton6.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        Funcbutton6.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);
        Funcbutton6.setMaximumSize(new java.awt.Dimension(149, 81));
        Funcbutton6.setVerticalTextPosition(javax.swing.SwingConstants.TOP);
        Funcbutton6.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                FB6MousePressed(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                FB6MouseReleased(evt);
            }
        });

        Funcbutton4.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        Funcbutton4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/hybrid/resources/senior.png"))); // NOI18N
        Funcbutton4.setVerticalAlignment(javax.swing.SwingConstants.BOTTOM);
        Funcbutton4.setFont(new java.awt.Font("SansSerif", 1, 12)); // NOI18N
        Funcbutton4.setForeground(new java.awt.Color(255, 255, 255));
        Funcbutton4.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                FB4MousePressed(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                FB4MouseReleased(evt);
            }
        });

        XFunc4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/hybrid/resources/bpo_car.png"))); // NOI18N
        XFunc4.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                XFunc4MousePressed(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                XFunc4MouseReleased(evt);
            }
        });

        XFunc2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/hybrid/resources/bpo_motor.png"))); // NOI18N
        XFunc2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                XFunc2MousePressed(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                XFunc2MouseReleased(evt);
            }
        });

        spacer.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        spacer.setIcon(new javax.swing.ImageIcon(getClass().getResource("/hybrid/resources/logo.png"))); // NOI18N

        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        setBackground(new java.awt.Color(255, 255, 255));
        setForeground(java.awt.Color.white);
        addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                formFocusLost(evt);
            }
        });
        addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                formKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                formKeyReleased(evt);
            }
        });
        getContentPane().setLayout(null);

        fullScreenCamera.setVerticalAlignment(javax.swing.SwingConstants.BOTTOM);
        fullScreenCamera.setFocusable(false);
        fullScreenCamera.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        fullScreenCamera.setForeground(new java.awt.Color(255, 255, 0));
        fullScreenCamera.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        fullScreenCamera.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        fullScreenCamera.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                fullScreenCameraMouseClicked(evt);
            }
        });
        getContentPane().add(fullScreenCamera);
        fullScreenCamera.setBounds(0, 0, 71, 30);

        ManualEntryPanel.setBackground(new java.awt.Color(255, 255, 255));
        ManualEntryPanel.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        ManualEntryPanel.setLayout(null);

        closeButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/hybrid/resources/close window.png"))); // NOI18N
        closeButton1.setOpaque(false);
        closeButton1.setPreferredSize(new java.awt.Dimension(50, 50));
        closeButton1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                closeButton1MouseClicked(evt);
            }
        });
        ManualEntryPanel.add(closeButton1);
        closeButton1.setBounds(0, 0, 60, 50);

        jLabel5.setText("Plate Number");
        jLabel5.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(0, 153, 153));
        ManualEntryPanel.add(jLabel5);
        jLabel5.setBounds(80, 0, 130, 20);

        manualEntryTime.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        ManualEntryPanel.add(manualEntryTime);
        manualEntryTime.setBounds(220, 80, 130, 30);

        manualEntryDate.setFieldFont(new java.awt.Font("Tahoma", java.awt.Font.BOLD, 12));
        manualEntryDate.setBehavior(datechooser.model.multiple.MultyModelBehavior.SELECT_SINGLE);
        ManualEntryPanel.add(manualEntryDate);
        manualEntryDate.setBounds(80, 80, 130, 30);

        Create.setText("Submit");
        Create.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        Create.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                CreateMouseClicked(evt);
            }
        });
        ManualEntryPanel.add(Create);
        Create.setBounds(360, 70, 120, 50);
        ManualEntryPanel.add(manualEntryPlate);
        manualEntryPlate.setBounds(80, 20, 130, 30);

        jLabel6.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(255, 255, 255));
        jLabel6.setText("Entry Time");
        ManualEntryPanel.add(jLabel6);
        jLabel6.setBounds(220, 60, 140, 20);

        jLabel8.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel8.setForeground(new java.awt.Color(255, 255, 255));
        jLabel8.setText("Entry Date");
        ManualEntryPanel.add(jLabel8);
        jLabel8.setBounds(80, 60, 140, 20);

        jLabel7.setBackground(new java.awt.Color(0, 153, 255));
        jLabel7.setForeground(new java.awt.Color(0, 153, 255));
        jLabel7.setOpaque(true);
        ManualEntryPanel.add(jLabel7);
        jLabel7.setBounds(10, 60, 490, 70);

        getContentPane().add(ManualEntryPanel);
        ManualEntryPanel.setBounds(470, 310, 510, 140);

        SearchPanel.setBackground(new java.awt.Color(255, 255, 255));
        SearchPanel.setLayout(null);

        SearchDisplayPanel.setBackground(new java.awt.Color(0, 153, 255));
        SearchDisplayPanel.setFocusable(false);
        SearchDisplayPanel.setAutoscrolls(true);
        java.awt.FlowLayout flowLayout2 = new java.awt.FlowLayout(java.awt.FlowLayout.LEADING);
        flowLayout2.setAlignOnBaseline(true);
        SearchDisplayPanel.setLayout(flowLayout2);
        SearchPanel.add(SearchDisplayPanel);
        SearchDisplayPanel.setBounds(0, 70, 1090, 650);

        closeButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/hybrid/resources/close window.png"))); // NOI18N
        closeButton.setOpaque(false);
        closeButton.setPreferredSize(new java.awt.Dimension(50, 50));
        closeButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                closeButtonMouseClicked(evt);
            }
        });
        SearchPanel.add(closeButton);
        closeButton.setBounds(0, 0, 70, 70);

        SearchDateTo.setFieldFont(new java.awt.Font("Tahoma", java.awt.Font.BOLD, 12));
        SearchDateTo.setBehavior(datechooser.model.multiple.MultyModelBehavior.SELECT_SINGLE);
        SearchPanel.add(SearchDateTo);
        SearchDateTo.setBounds(380, 30, 130, 30);

        SearchTimeTo.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        SearchPanel.add(SearchTimeTo);
        SearchTimeTo.setBounds(520, 30, 130, 30);

        jLabel3.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(0, 153, 153));
        jLabel3.setText("Search From");
        SearchPanel.add(jLabel3);
        jLabel3.setBounds(80, 4, 140, 20);

        jLabel4.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(0, 153, 153));
        jLabel4.setText("To");
        SearchPanel.add(jLabel4);
        jLabel4.setBounds(380, 4, 50, 20);

        SearchTimeFrom.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        SearchPanel.add(SearchTimeFrom);
        SearchTimeFrom.setBounds(220, 30, 130, 30);

        SearchDateFrom.setFieldFont(new java.awt.Font("Tahoma", java.awt.Font.BOLD, 12));
        SearchDateFrom.setBehavior(datechooser.model.multiple.MultyModelBehavior.SELECT_SINGLE);
        SearchPanel.add(SearchDateFrom);
        SearchDateFrom.setBounds(80, 30, 130, 30);

        jButton1.setText("Search");
        jButton1.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jButton1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jButton1MouseClicked(evt);
            }
        });
        SearchPanel.add(jButton1);
        jButton1.setBounds(660, 10, 120, 50);

        getContentPane().add(SearchPanel);
        SearchPanel.setBounds(320, 110, 860, 570);

        PasswordPanel.setFocusable(false);
        PasswordPanel.setOpaque(false);
        PasswordPanel.setLayout(new java.awt.GridLayout(1, 5, 1, 0));

        PWORDlbl.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        PWORDlbl.setText("PASSWORD:"); // NOI18N
        PWORDlbl.setFocusable(false);
        PWORDlbl.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        PWORDlbl.setForeground(new java.awt.Color(24, 91, 0));
        PWORDlbl.setRequestFocusEnabled(false);
        PWORDlbl.setVerifyInputWhenFocusTarget(false);
        PasswordPanel.add(PWORDlbl);

        PWORDInput2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        PWORDInput2.setBackground(new java.awt.Color(207, 207, 106));
        PWORDInput2.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 2, true));
        PWORDInput2.setFont(new java.awt.Font("Arial Narrow", 1, 24)); // NOI18N
        PWORDInput2.setForeground(new java.awt.Color(24, 91, 0));
        PWORDInput2.setOpaque(true);
        PasswordPanel.add(PWORDInput2);

        getContentPane().add(PasswordPanel);
        PasswordPanel.setBounds(480, 350, 310, 60);

        LoginPanelX.setFocusable(false);
        LoginPanelX.setOpaque(false);
        LoginPanelX.setLayout(new java.awt.GridLayout(3, 3, 1, 0));

        LoginLbl.setText("Usercode:");
        LoginLbl.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        LoginLbl.setForeground(new java.awt.Color(255, 255, 255));
        LoginPanelX.add(LoginLbl);

        LogUsercode2.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        LogUsercode2.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        LoginPanelX.add(LogUsercode2);

        LoginLbl1.setText("Password:");
        LoginLbl1.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        LoginLbl1.setForeground(new java.awt.Color(255, 255, 255));
        LoginPanelX.add(LoginLbl1);

        LogPassword2.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        LogPassword2.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        LoginPanelX.add(LogPassword2);

        LoginLbl2.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        LoginLbl2.setForeground(new java.awt.Color(255, 255, 255));
        LoginPanelX.add(LoginLbl2);

        LoginButton2.setText("Login");
        LoginButton2.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        LoginButton2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                LoginButton2MouseClicked(evt);
            }
        });
        LoginButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                LoginButton2ActionPerformed(evt);
            }
        });
        LoginPanelX.add(LoginButton2);

        getContentPane().add(LoginPanelX);
        LoginPanelX.setBounds(440, 280, 420, 160);

        LogoutPanelX.setFocusable(false);
        LogoutPanelX.setOpaque(false);
        LogoutPanelX.setLayout(new java.awt.GridLayout(3, 3, 1, 0));

        LogoutLbl.setText("Usercode:");
        LogoutLbl.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        LogoutLbl.setForeground(new java.awt.Color(255, 255, 255));
        LogoutPanelX.add(LogoutLbl);

        LogUsercode1.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        LogUsercode1.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        LogoutPanelX.add(LogUsercode1);

        LogoutLbl1.setText("Password:");
        LogoutLbl1.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        LogoutLbl1.setForeground(new java.awt.Color(255, 255, 255));
        LogoutPanelX.add(LogoutLbl1);

        LogPassword1.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        LogPassword1.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        LogoutPanelX.add(LogPassword1);

        LogoutLbl2.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        LogoutLbl2.setForeground(new java.awt.Color(255, 255, 255));
        LogoutPanelX.add(LogoutLbl2);

        LoginButton1.setText("Logout");
        LoginButton1.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        LoginButton1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                LoginButton1MouseClicked(evt);
            }
        });
        LogoutPanelX.add(LoginButton1);

        getContentPane().add(LogoutPanelX);
        LogoutPanelX.setBounds(440, 280, 420, 160);

        MasterCardPanel.setFocusable(false);
        MasterCardPanel.setOpaque(false);
        MasterCardPanel.setLayout(new java.awt.GridLayout(1, 5, 1, 0));

        MasterCardLbl.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        MasterCardLbl.setText("MASTERCARD:"); // NOI18N
        MasterCardLbl.setFocusable(false);
        MasterCardLbl.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        MasterCardLbl.setForeground(new java.awt.Color(255, 255, 51));
        MasterCardLbl.setRequestFocusEnabled(false);
        MasterCardLbl.setVerifyInputWhenFocusTarget(false);
        MasterCardPanel.add(MasterCardLbl);

        MasterCardInput2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        MasterCardInput2.setBackground(new java.awt.Color(255, 255, 204));
        MasterCardInput2.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 2, true));
        MasterCardInput2.setFont(new java.awt.Font("Arial Narrow", 1, 24)); // NOI18N
        MasterCardInput2.setForeground(new java.awt.Color(51, 25, 25));
        MasterCardInput2.setOpaque(true);
        MasterCardPanel.add(MasterCardInput2);

        reprintButton.setText("Reprint X Read");
        reprintButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                reprintButtonMousePressed(evt);
            }
        });
        MasterCardPanel.add(reprintButton);

        getContentPane().add(MasterCardPanel);
        MasterCardPanel.setBounds(480, 350, 310, 60);

        BGPanel.setOpaque(false);
        BGPanel.setLayout(new java.awt.BorderLayout());

        NorthPanel.setOpaque(false);
        NorthPanel.setLayout(new java.awt.BorderLayout());

        ProductName.setIcon(new javax.swing.ImageIcon(getClass().getResource("/hybrid/resources/small SilverLogo.png"))); // NOI18N
        ProductName.setText("PARKING AREA SENTINEL SYSTEM");
        ProductName.setFont(new java.awt.Font("Calibri", 1, 14)); // NOI18N
        ProductName.setForeground(new java.awt.Color(255, 255, 255));
        NorthPanel.add(ProductName, java.awt.BorderLayout.WEST);
        ProductName.getAccessibleContext().setAccessibleName("PARKING AREA SENTINEL SYSTEM\nTheoretics Inc");

        ClientName.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        ClientName.setText("WELCOME TO PAYSTATION PARKING");
        ClientName.setFont(new java.awt.Font("Calibri", 1, 24)); // NOI18N
        ClientName.setForeground(new java.awt.Color(255, 255, 255));
        NorthPanel.add(ClientName, java.awt.BorderLayout.NORTH);

        LeftMsgPanel.setPreferredSize(new java.awt.Dimension(280, 34));
        LeftMsgPanel.setOpaque(false);
        LeftMsgPanel.setLayout(new java.awt.GridLayout(1, 2));

        Labels.setOpaque(false);
        Labels.setLayout(new java.awt.GridLayout(2, 2));

        ServerInfo1.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        ServerInfo1.setForeground(new java.awt.Color(204, 204, 204));
        ServerInfo1.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        ServerInfo1.setText("SERVER ="); // NOI18N
        ServerInfo1.setMinimumSize(null);
        ServerInfo1.setPreferredSize(null);
        Labels.add(ServerInfo1);

        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setText("created by");
        jLabel2.setMinimumSize(null);
        jLabel2.setPreferredSize(null);
        Labels.add(jLabel2);

        LeftMsgPanel.add(Labels);

        Status.setFocusable(false);
        Status.setMinimumSize(new java.awt.Dimension(200, 34));
        Status.setOpaque(false);
        Status.setPreferredSize(new java.awt.Dimension(200, 20));
        Status.setLayout(new java.awt.GridLayout(2, 2));

        ServerStatus.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        ServerStatus.setForeground(new java.awt.Color(110, 5, 10));
        ServerStatus.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        ServerStatus.setText("checking..."); // NOI18N
        Status.add(ServerStatus);

        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("THEORETICS INC.");
        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jLabel1.setVerticalTextPosition(javax.swing.SwingConstants.TOP);
        Status.add(jLabel1);
        jLabel1.getAccessibleContext().setAccessibleName("jLabel1");

        LeftMsgPanel.add(Status);

        NorthPanel.add(LeftMsgPanel, java.awt.BorderLayout.EAST);

        slotsPanel.setOpaque(false);

        slotsType.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        slotsType.setForeground(new java.awt.Color(255, 255, 255));
        slotsType.setText("CARS");
        slotsPanel.add(slotsType);

        carsMinusbtn.setIcon(new javax.swing.ImageIcon(getClass().getResource("/hybrid/resources/buttonMinus1.png"))); // NOI18N
        carsMinusbtn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                carsMinusbtnMouseClicked(evt);
            }
        });
        slotsPanel.add(carsMinusbtn);

        carsNum.setEditable(false);
        carsNum.setHorizontalAlignment(javax.swing.JTextField.LEFT);
        carsNum.setText("120");
        carsNum.setBackground(new java.awt.Color(0, 0, 0));
        carsNum.setFont(new java.awt.Font("DSEG14 Modern", 0, 24)); // NOI18N
        carsNum.setForeground(new java.awt.Color(255, 255, 255));
        carsNum.setMargin(new java.awt.Insets(10, 10, 10, 10));
        carsNum.setMinimumSize(new java.awt.Dimension(76, 76));
        carsNum.setName(""); // NOI18N
        slotsPanel.add(carsNum);

        carsPlusbtn.setIcon(new javax.swing.ImageIcon(getClass().getResource("/hybrid/resources/buttonPlus1.png"))); // NOI18N
        carsPlusbtn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                carsPlusbtnMouseClicked(evt);
            }
        });
        slotsPanel.add(carsPlusbtn);

        sep.setText("                        ");
        slotsPanel.add(sep);

        slotsType2.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        slotsType2.setForeground(new java.awt.Color(255, 255, 255));
        slotsType2.setText("Motorcycle");
        slotsPanel.add(slotsType2);

        motorMinusbtn.setIcon(new javax.swing.ImageIcon(getClass().getResource("/hybrid/resources/buttonMinus1.png"))); // NOI18N
        motorMinusbtn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                motorMinusbtnMouseClicked(evt);
            }
        });
        slotsPanel.add(motorMinusbtn);

        motorNum.setEditable(false);
        motorNum.setHorizontalAlignment(javax.swing.JTextField.LEFT);
        motorNum.setText("120");
        motorNum.setBackground(new java.awt.Color(0, 0, 0));
        motorNum.setFont(new java.awt.Font("DSEG14 Modern", 0, 24)); // NOI18N
        motorNum.setForeground(new java.awt.Color(255, 255, 255));
        motorNum.setMargin(new java.awt.Insets(10, 10, 10, 10));
        motorNum.setMinimumSize(new java.awt.Dimension(76, 76));
        motorNum.setName(""); // NOI18N
        slotsPanel.add(motorNum);

        motorPlusbtn.setIcon(new javax.swing.ImageIcon(getClass().getResource("/hybrid/resources/buttonPlus1.png"))); // NOI18N
        motorPlusbtn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                motorPlusbtnMouseClicked(evt);
            }
        });
        slotsPanel.add(motorPlusbtn);

        NorthPanel.add(slotsPanel, java.awt.BorderLayout.CENTER);

        BGPanel.add(NorthPanel, java.awt.BorderLayout.NORTH);

        SouthPanel.setOpaque(false);
        SouthPanel.setLayout(new java.awt.BorderLayout());

        version.setIcon(new javax.swing.ImageIcon(getClass().getResource("/hybrid/resources/startButton.PNG"))); // NOI18N
        version.setText("V *"); // NOI18N
        version.setVerticalAlignment(javax.swing.SwingConstants.BOTTOM);
        version.setFocusable(false);
        version.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        version.setForeground(new java.awt.Color(255, 255, 204));
        version.setRequestFocusEnabled(false);
        version.setVerifyInputWhenFocusTarget(false);
        version.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                startButtonPressed(evt);
            }
        });
        SouthPanel.add(version, java.awt.BorderLayout.WEST);

        RDatePanel.setMinimumSize(new java.awt.Dimension(300, 48));
        RDatePanel.setPreferredSize(new java.awt.Dimension(400, 40));
        RDatePanel.setOpaque(false);
        RDatePanel.setLayout(new java.awt.GridLayout(2, 4));

        DateLabel.setText("DATE:"); // NOI18N
        DateLabel.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        DateLabel.setForeground(new java.awt.Color(255, 255, 255));
        DateLabel.setMaximumSize(new java.awt.Dimension(10, 14));
        DateLabel.setMinimumSize(new java.awt.Dimension(10, 14));
        DateLabel.setPreferredSize(new java.awt.Dimension(10, 14));
        RDatePanel.add(DateLabel);

        datedisplay.setText("*"); // NOI18N
        datedisplay.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        datedisplay.setForeground(new java.awt.Color(255, 255, 255));
        datedisplay.setMaximumSize(new java.awt.Dimension(27, 22));
        datedisplay.setMinimumSize(new java.awt.Dimension(27, 22));
        datedisplay.setName(""); // NOI18N
        datedisplay.setPreferredSize(new java.awt.Dimension(27, 22));
        RDatePanel.add(datedisplay);

        daydisplay.setText("*"); // NOI18N
        daydisplay.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        daydisplay.setForeground(new java.awt.Color(255, 255, 255));
        daydisplay.setName(""); // NOI18N
        RDatePanel.add(daydisplay);

        TimeLabel.setText("TIME:"); // NOI18N
        TimeLabel.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        TimeLabel.setForeground(new java.awt.Color(255, 255, 255));
        TimeLabel.setMaximumSize(new java.awt.Dimension(10, 14));
        TimeLabel.setMinimumSize(new java.awt.Dimension(10, 14));
        TimeLabel.setPreferredSize(new java.awt.Dimension(10, 14));
        RDatePanel.add(TimeLabel);

        timedisplay.setText("*"); // NOI18N
        timedisplay.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        timedisplay.setForeground(new java.awt.Color(255, 255, 255));
        timedisplay.setMaximumSize(new java.awt.Dimension(27, 22));
        timedisplay.setMinimumSize(new java.awt.Dimension(27, 22));
        timedisplay.setName(""); // NOI18N
        timedisplay.setPreferredSize(new java.awt.Dimension(27, 22));
        RDatePanel.add(timedisplay);

        spacer1.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        spacer1.setForeground(new java.awt.Color(255, 255, 255));
        RDatePanel.add(spacer1);

        SouthPanel.add(RDatePanel, java.awt.BorderLayout.EAST);

        RDatePanel1.setMinimumSize(new java.awt.Dimension(300, 48));
        RDatePanel1.setPreferredSize(new java.awt.Dimension(400, 40));
        RDatePanel1.setOpaque(false);
        RDatePanel1.setLayout(new java.awt.GridLayout(1, 2));
        RDatePanel1.add(spacer4);

        SentinelIDlbl.setText("Sentinel ID:"); // NOI18N
        SentinelIDlbl.setBackground(new java.awt.Color(255, 255, 200));
        SentinelIDlbl.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        SentinelIDlbl.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        SentinelIDlbl.setForeground(new java.awt.Color(255, 255, 255));
        RDatePanel1.add(SentinelIDlbl);

        TerminalIDlbl.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        TerminalIDlbl.setText("ID"); // NOI18N
        TerminalIDlbl.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        TerminalIDlbl.setFont(new java.awt.Font("Arial", 1, 16)); // NOI18N
        TerminalIDlbl.setForeground(new java.awt.Color(255, 255, 0));
        RDatePanel1.add(TerminalIDlbl);
        RDatePanel1.add(spacer2);
        RDatePanel1.add(spacer3);

        SouthPanel.add(RDatePanel1, java.awt.BorderLayout.CENTER);

        BGPanel.add(SouthPanel, java.awt.BorderLayout.SOUTH);

        CenterPanel.setOpaque(false);
        CenterPanel.setLayout(new java.awt.BorderLayout());

        newMidPanel.setFocusable(false);
        newMidPanel.setOpaque(false);
        newMidPanel.setLayout(new java.awt.GridLayout(3, 3));

        inputPanel.setMinimumSize(new java.awt.Dimension(116, 125));
        inputPanel.setPreferredSize(new java.awt.Dimension(116, 125));
        inputPanel.setOpaque(false);
        inputPanel.setLayout(new java.awt.GridLayout(4, 0));

        CardNumberlbl.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        CardNumberlbl.setText("CARD NUMBER:"); // NOI18N
        CardNumberlbl.setFocusable(false);
        CardNumberlbl.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        CardNumberlbl.setForeground(java.awt.Color.white);
        CardNumberlbl.setRequestFocusEnabled(false);
        CardNumberlbl.setVerifyInputWhenFocusTarget(false);
        inputPanel.add(CardNumberlbl);

        CardInput2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        CardInput2.setFont(new java.awt.Font("Arial Narrow", 0, 38)); // NOI18N
        CardInput2.setForeground(new java.awt.Color(255, 255, 0));
        inputPanel.add(CardInput2);

        PlateNumberlbl.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        PlateNumberlbl.setText("PLATE NUMBER:"); // NOI18N
        PlateNumberlbl.setFocusable(false);
        PlateNumberlbl.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        PlateNumberlbl.setForeground(java.awt.Color.white);
        PlateNumberlbl.setRequestFocusEnabled(false);
        PlateNumberlbl.setVerifyInputWhenFocusTarget(false);
        inputPanel.add(PlateNumberlbl);

        PlateInput2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        PlateInput2.setBackground(new java.awt.Color(0, 0, 102));
        PlateInput2.setFont(new java.awt.Font("Arial Black", 0, 40)); // NOI18N
        PlateInput2.setForeground(new java.awt.Color(0, 130, 255));
        PlateInput2.setMinimumSize(null);
        PlateInput2.setPreferredSize(null);
        inputPanel.add(PlateInput2);

        newMidPanel.add(inputPanel);

        MainPanel.setOpaque(false);
        MainPanel.setLayout(null);

        amntlbl1.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        amntlbl1.setText("AMOUNT DUE:");
        amntlbl1.setFocusable(false);
        amntlbl1.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        amntlbl1.setForeground(new java.awt.Color(255, 255, 204));
        amntlbl1.setRequestFocusEnabled(false);
        amntlbl1.setVerifyInputWhenFocusTarget(false);
        MainPanel.add(amntlbl1);
        amntlbl1.setBounds(0, 0, 200, 50);

        amntlbl.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        amntlbl.setForeground(new java.awt.Color(255, 255, 204));
        amntlbl.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        amntlbl.setFocusable(false);
        amntlbl.setRequestFocusEnabled(false);
        amntlbl.setVerifyInputWhenFocusTarget(false);
        MainPanel.add(amntlbl);
        amntlbl.setBounds(0, 90, 90, 80);

        AMOUNTdisplay.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        AMOUNTdisplay.setFont(new java.awt.Font("Arial Narrow", 0, 38)); // NOI18N
        AMOUNTdisplay.setForeground(new java.awt.Color(102, 255, 153));
        MainPanel.add(AMOUNTdisplay);
        AMOUNTdisplay.setBounds(80, 40, 440, 70);

        AmtTendered.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        AmtTendered.setEnabled(false);
        AmtTendered.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        AmtTendered.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                AmtTenderedKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                AmtTenderedKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                AmtTenderedKeyTyped(evt);
            }
        });
        MainPanel.add(AmtTendered);
        AmtTendered.setBounds(240, 110, 100, 60);

        amntlbl2.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        amntlbl2.setForeground(new java.awt.Color(255, 255, 204));
        amntlbl2.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        amntlbl2.setText("TENDERED:");
        amntlbl2.setFocusable(false);
        amntlbl2.setRequestFocusEnabled(false);
        amntlbl2.setVerifyInputWhenFocusTarget(false);
        MainPanel.add(amntlbl2);
        amntlbl2.setBounds(0, 110, 200, 60);

        amntlbl3.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        amntlbl3.setText("CHANGE:");
        amntlbl3.setFocusable(false);
        amntlbl3.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        amntlbl3.setForeground(new java.awt.Color(255, 255, 204));
        amntlbl3.setRequestFocusEnabled(false);
        amntlbl3.setVerifyInputWhenFocusTarget(false);
        MainPanel.add(amntlbl3);
        amntlbl3.setBounds(340, 110, 80, 60);

        ChangeDisplay.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        ChangeDisplay.setFocusable(false);
        ChangeDisplay.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        ChangeDisplay.setForeground(new java.awt.Color(255, 255, 204));
        ChangeDisplay.setRequestFocusEnabled(false);
        ChangeDisplay.setVerifyInputWhenFocusTarget(false);
        MainPanel.add(ChangeDisplay);
        ChangeDisplay.setBounds(430, 110, 90, 60);

        newMidPanel.add(MainPanel);

        LeftMIDMsgPanel.setOpaque(false);
        LeftMIDMsgPanel.setLayout(new java.awt.GridLayout(10, 0));

        SysMessage1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        SysMessage1.setFont(new java.awt.Font("Tahoma", 1, 16)); // NOI18N
        SysMessage1.setForeground(new java.awt.Color(255, 255, 204));
        LeftMIDMsgPanel.add(SysMessage1);

        SysMessage11.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        SysMessage11.setFont(new java.awt.Font("Tahoma", 1, 16)); // NOI18N
        SysMessage11.setForeground(new java.awt.Color(255, 255, 204));
        LeftMIDMsgPanel.add(SysMessage11);

        SysMessage2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        SysMessage2.setFont(new java.awt.Font("Tahoma", 1, 16)); // NOI18N
        SysMessage2.setForeground(new java.awt.Color(255, 255, 255));
        LeftMIDMsgPanel.add(SysMessage2);

        SysMessage12.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        SysMessage12.setFont(new java.awt.Font("Tahoma", 1, 16)); // NOI18N
        SysMessage12.setForeground(new java.awt.Color(255, 255, 255));
        LeftMIDMsgPanel.add(SysMessage12);

        SysMessage3.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        SysMessage3.setFont(new java.awt.Font("Tahoma", 1, 16)); // NOI18N
        SysMessage3.setForeground(new java.awt.Color(255, 255, 255));
        LeftMIDMsgPanel.add(SysMessage3);

        SysMessage13.setFont(new java.awt.Font("Tahoma", 1, 16)); // NOI18N
        SysMessage13.setForeground(new java.awt.Color(255, 255, 255));
        SysMessage13.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        LeftMIDMsgPanel.add(SysMessage13);

        SysMessage4.setFont(new java.awt.Font("Tahoma", 1, 16)); // NOI18N
        SysMessage4.setForeground(new java.awt.Color(255, 255, 255));
        SysMessage4.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        LeftMIDMsgPanel.add(SysMessage4);

        SysMessage14.setFont(new java.awt.Font("Tahoma", 1, 16)); // NOI18N
        SysMessage14.setForeground(new java.awt.Color(255, 255, 255));
        SysMessage14.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        LeftMIDMsgPanel.add(SysMessage14);

        SysMessage5.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        SysMessage5.setFont(new java.awt.Font("Tahoma", 1, 16)); // NOI18N
        SysMessage5.setForeground(new java.awt.Color(255, 255, 255));
        LeftMIDMsgPanel.add(SysMessage5);

        SysMessage15.setFont(new java.awt.Font("Tahoma", 1, 16)); // NOI18N
        SysMessage15.setForeground(new java.awt.Color(255, 255, 255));
        SysMessage15.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        LeftMIDMsgPanel.add(SysMessage15);

        SysMessage6.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        SysMessage6.setFont(new java.awt.Font("Tahoma", 1, 16)); // NOI18N
        SysMessage6.setForeground(new java.awt.Color(255, 255, 255));
        LeftMIDMsgPanel.add(SysMessage6);

        SysMessage16.setFont(new java.awt.Font("Tahoma", 1, 16)); // NOI18N
        SysMessage16.setForeground(new java.awt.Color(255, 255, 255));
        SysMessage16.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        LeftMIDMsgPanel.add(SysMessage16);

        SysMessage7.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        SysMessage7.setFont(new java.awt.Font("Tahoma", 1, 16)); // NOI18N
        SysMessage7.setForeground(new java.awt.Color(255, 255, 255));
        LeftMIDMsgPanel.add(SysMessage7);

        SysMessage17.setFont(new java.awt.Font("Tahoma", 1, 16)); // NOI18N
        SysMessage17.setForeground(new java.awt.Color(255, 255, 255));
        SysMessage17.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        LeftMIDMsgPanel.add(SysMessage17);

        SysMessage8.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        SysMessage8.setFont(new java.awt.Font("Tahoma", 1, 16)); // NOI18N
        SysMessage8.setForeground(new java.awt.Color(255, 255, 255));
        LeftMIDMsgPanel.add(SysMessage8);

        SysMessage18.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        SysMessage18.setFont(new java.awt.Font("Tahoma", 1, 16)); // NOI18N
        SysMessage18.setForeground(new java.awt.Color(255, 255, 255));
        LeftMIDMsgPanel.add(SysMessage18);

        SysMessage9.setFont(new java.awt.Font("Tahoma", 1, 16)); // NOI18N
        SysMessage9.setForeground(new java.awt.Color(255, 255, 255));
        SysMessage9.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        LeftMIDMsgPanel.add(SysMessage9);

        SysMessage19.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        SysMessage19.setFont(new java.awt.Font("Tahoma", 1, 16)); // NOI18N
        SysMessage19.setForeground(new java.awt.Color(255, 255, 255));
        LeftMIDMsgPanel.add(SysMessage19);

        SysMessage10.setFont(new java.awt.Font("Tahoma", 1, 16)); // NOI18N
        SysMessage10.setForeground(new java.awt.Color(255, 255, 255));
        SysMessage10.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        LeftMIDMsgPanel.add(SysMessage10);

        SysMessage20.setFont(new java.awt.Font("Tahoma", 1, 16)); // NOI18N
        SysMessage20.setForeground(new java.awt.Color(255, 255, 255));
        SysMessage20.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        LeftMIDMsgPanel.add(SysMessage20);

        newMidPanel.add(LeftMIDMsgPanel);

        RightMsgPanellbl.setBackground(new java.awt.Color(255, 255, 255));
        RightMsgPanellbl.setForeground(new java.awt.Color(255, 255, 255));
        RightMsgPanellbl.setOpaque(false);
        RightMsgPanellbl.setLayout(new java.awt.GridLayout(10, 3));

        ParkerInfo11.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        ParkerInfo11.setText("Entrance From:  "); // NOI18N
        ParkerInfo11.setAlignmentX(1.0F);
        ParkerInfo11.setAlignmentY(1.0F);
        ParkerInfo11.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 10));
        ParkerInfo11.setFocusable(false);
        ParkerInfo11.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        ParkerInfo11.setForeground(new java.awt.Color(255, 255, 0));
        ParkerInfo11.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        RightMsgPanellbl.add(ParkerInfo11);

        ParkerInfo1.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        ParkerInfo1.setForeground(new java.awt.Color(255, 255, 255));
        RightMsgPanellbl.add(ParkerInfo1);

        ParkerInfo12.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        ParkerInfo12.setText("Card Number  "); // NOI18N
        ParkerInfo12.setAlignmentX(1.0F);
        ParkerInfo12.setAlignmentY(1.0F);
        ParkerInfo12.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 10));
        ParkerInfo12.setFocusable(false);
        ParkerInfo12.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        ParkerInfo12.setForeground(new java.awt.Color(255, 255, 0));
        ParkerInfo12.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        RightMsgPanellbl.add(ParkerInfo12);

        ParkerInfo2.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        ParkerInfo2.setForeground(new java.awt.Color(255, 255, 255));
        RightMsgPanellbl.add(ParkerInfo2);

        ParkerInfo13.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        ParkerInfo13.setText("Plate Number  "); // NOI18N
        ParkerInfo13.setAlignmentX(1.0F);
        ParkerInfo13.setAlignmentY(1.0F);
        ParkerInfo13.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 10));
        ParkerInfo13.setFocusable(false);
        ParkerInfo13.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        ParkerInfo13.setForeground(new java.awt.Color(255, 255, 0));
        ParkerInfo13.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        RightMsgPanellbl.add(ParkerInfo13);

        ParkerInfo3.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        ParkerInfo3.setForeground(new java.awt.Color(255, 255, 255));
        RightMsgPanellbl.add(ParkerInfo3);

        ParkerInfo14.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        ParkerInfo14.setText("Transaction Type  "); // NOI18N
        ParkerInfo14.setAlignmentX(1.0F);
        ParkerInfo14.setAlignmentY(1.0F);
        ParkerInfo14.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 10));
        ParkerInfo14.setFocusable(false);
        ParkerInfo14.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        ParkerInfo14.setForeground(new java.awt.Color(255, 255, 0));
        ParkerInfo14.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        RightMsgPanellbl.add(ParkerInfo14);

        ParkerInfo4.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        ParkerInfo4.setForeground(new java.awt.Color(255, 255, 255));
        RightMsgPanellbl.add(ParkerInfo4);

        ParkerInfo15.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        ParkerInfo15.setText("Timestamp  "); // NOI18N
        ParkerInfo15.setAlignmentX(1.0F);
        ParkerInfo15.setAlignmentY(1.0F);
        ParkerInfo15.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 10));
        ParkerInfo15.setFocusable(false);
        ParkerInfo15.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        ParkerInfo15.setForeground(new java.awt.Color(255, 255, 0));
        ParkerInfo15.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        RightMsgPanellbl.add(ParkerInfo15);

        ParkerInfo5.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        ParkerInfo5.setForeground(new java.awt.Color(255, 255, 255));
        RightMsgPanellbl.add(ParkerInfo5);

        ParkerInfo16.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        ParkerInfo16.setAlignmentX(1.0F);
        ParkerInfo16.setAlignmentY(1.0F);
        ParkerInfo16.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 10));
        ParkerInfo16.setFocusable(false);
        ParkerInfo16.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        ParkerInfo16.setForeground(new java.awt.Color(255, 255, 0));
        ParkerInfo16.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        RightMsgPanellbl.add(ParkerInfo16);

        ParkerInfo6.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        ParkerInfo6.setForeground(new java.awt.Color(255, 255, 255));
        RightMsgPanellbl.add(ParkerInfo6);

        ParkerInfo17.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        ParkerInfo17.setText("Date IN  "); // NOI18N
        ParkerInfo17.setAlignmentX(1.0F);
        ParkerInfo17.setAlignmentY(1.0F);
        ParkerInfo17.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 10));
        ParkerInfo17.setFocusable(false);
        ParkerInfo17.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        ParkerInfo17.setForeground(new java.awt.Color(255, 255, 0));
        ParkerInfo17.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        RightMsgPanellbl.add(ParkerInfo17);

        ParkerInfo7.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        ParkerInfo7.setForeground(new java.awt.Color(255, 255, 255));
        RightMsgPanellbl.add(ParkerInfo7);

        ParkerInfo18.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        ParkerInfo18.setText("Time IN  "); // NOI18N
        ParkerInfo18.setAlignmentX(1.0F);
        ParkerInfo18.setAlignmentY(1.0F);
        ParkerInfo18.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 10));
        ParkerInfo18.setFocusable(false);
        ParkerInfo18.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        ParkerInfo18.setForeground(new java.awt.Color(255, 255, 0));
        ParkerInfo18.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        RightMsgPanellbl.add(ParkerInfo18);

        ParkerInfo8.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        ParkerInfo8.setForeground(new java.awt.Color(255, 255, 255));
        RightMsgPanellbl.add(ParkerInfo8);

        ParkerInfo19.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        ParkerInfo19.setAlignmentX(1.0F);
        ParkerInfo19.setAlignmentY(1.0F);
        ParkerInfo19.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 10));
        ParkerInfo19.setFocusable(false);
        ParkerInfo19.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        ParkerInfo19.setForeground(new java.awt.Color(255, 255, 0));
        ParkerInfo19.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        RightMsgPanellbl.add(ParkerInfo19);

        ParkerInfo9.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        ParkerInfo9.setForeground(new java.awt.Color(255, 255, 255));
        RightMsgPanellbl.add(ParkerInfo9);

        ParkerInfo20.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        ParkerInfo20.setAlignmentX(1.0F);
        ParkerInfo20.setAlignmentY(1.0F);
        ParkerInfo20.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 10));
        ParkerInfo20.setFocusable(false);
        ParkerInfo20.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        ParkerInfo20.setForeground(new java.awt.Color(255, 255, 0));
        ParkerInfo20.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        RightMsgPanellbl.add(ParkerInfo20);

        ParkerInfo10.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        ParkerInfo10.setForeground(new java.awt.Color(255, 255, 255));
        RightMsgPanellbl.add(ParkerInfo10);

        newMidPanel.add(RightMsgPanellbl);

        LowerLeftPanelX.setOpaque(false);
        LowerLeftPanelX.setLayout(null);

        LowerLeftPanel.setEnabled(false);
        LowerLeftPanel.setOpaque(false);
        LowerLeftPanel.setLayout(null);

        CollectionDate.setNothingAllowed(false);
        CollectionDate.setFieldFont(new java.awt.Font("Tahoma", java.awt.Font.BOLD, 16));
        CollectionDate.setNavigateFont(new java.awt.Font("Tahoma", java.awt.Font.PLAIN, 14));
        LowerLeftPanel.add(CollectionDate);
        CollectionDate.setBounds(20, 40, 190, 40);

        cashColLbl.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        cashColLbl.setText("Collection Date:"); // NOI18N
        cashColLbl.setFocusable(false);
        cashColLbl.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        cashColLbl.setForeground(new java.awt.Color(255, 255, 0));
        cashColLbl.setRequestFocusEnabled(false);
        cashColLbl.setVerifyInputWhenFocusTarget(false);
        LowerLeftPanel.add(cashColLbl);
        cashColLbl.setBounds(20, 0, 190, 40);

        printCollLbl.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        printCollLbl.setText("Print X"); // NOI18N
        printCollLbl.setFocusable(false);
        printCollLbl.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        printCollLbl.setRequestFocusEnabled(false);
        printCollLbl.setVerifyInputWhenFocusTarget(false);
        LowerLeftPanel.add(printCollLbl);
        printCollLbl.setBounds(250, 120, 50, 40);

        printCollectionBtn.setIcon(new javax.swing.ImageIcon(getClass().getResource("/hybrid/resources/Keybutton.png"))); // NOI18N
        printCollectionBtn.setIconTextGap(2);
        printCollectionBtn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                printCollectionBtnMouseClicked(evt);
            }
        });
        LowerLeftPanel.add(printCollectionBtn);
        printCollectionBtn.setBounds(240, 120, 70, 40);
        logcancelbutton.addMouseListener(new java.awt.event.MouseAdapter() {             public void mousePressed(java.awt.event.MouseEvent evt) {                 Funckeymouseover(evt.getSource());             }             public void mouseReleased(java.awt.event.MouseEvent evt) {                 Funckeymouseoff(evt.getSource());             }         });

        searchLbl.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        searchLbl.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        searchLbl.setText("Search"); // NOI18N
        searchLbl.setFocusable(false);
        searchLbl.setRequestFocusEnabled(false);
        searchLbl.setVerifyInputWhenFocusTarget(false);
        LowerLeftPanel.add(searchLbl);
        searchLbl.setBounds(250, 40, 50, 40);

        searchCollectionBtn.setIcon(new javax.swing.ImageIcon(getClass().getResource("/hybrid/resources/Keybutton.png"))); // NOI18N
        searchCollectionBtn.setIconTextGap(2);
        searchCollectionBtn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                searchCollectionBtnMouseClicked(evt);
            }
        });
        LowerLeftPanel.add(searchCollectionBtn);
        searchCollectionBtn.setBounds(240, 40, 70, 40);
        logcancelbutton.addMouseListener(new java.awt.event.MouseAdapter() {             public void mousePressed(java.awt.event.MouseEvent evt) {                 Funckeymouseover(evt.getSource());             }             public void mouseReleased(java.awt.event.MouseEvent evt) {                 Funckeymouseoff(evt.getSource());             }         });

        XReadingListField.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N

        xreadList.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        xreadList.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        XReadingListField.setViewportView(xreadList);

        LowerLeftPanel.add(XReadingListField);
        XReadingListField.setBounds(20, 90, 210, 70);

        LowerLeftPanelX.add(LowerLeftPanel);
        LowerLeftPanel.setBounds(0, 0, 540, 180);

        ZReadingPanel.setEnabled(false);
        ZReadingPanel.setOpaque(false);
        ZReadingPanel.setLayout(null);

        ZReadingDate.setNothingAllowed(false);
        ZReadingDate.setFieldFont(new java.awt.Font("Tahoma", java.awt.Font.BOLD, 16));
        ZReadingDate.setNavigateFont(new java.awt.Font("Tahoma", java.awt.Font.PLAIN, 14));
        ZReadingDate.setBehavior(datechooser.model.multiple.MultyModelBehavior.SELECT_SINGLE);
        ZReadingPanel.add(ZReadingDate);
        ZReadingDate.setBounds(200, 80, 190, 40);

        zreadLbl.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        zreadLbl.setText("Z Reading Date:"); // NOI18N
        zreadLbl.setFocusable(false);
        zreadLbl.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        zreadLbl.setForeground(new java.awt.Color(255, 255, 0));
        zreadLbl.setRequestFocusEnabled(false);
        zreadLbl.setVerifyInputWhenFocusTarget(false);
        ZReadingPanel.add(zreadLbl);
        zreadLbl.setBounds(200, 10, 190, 70);

        printCollLbl1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        printCollLbl1.setText("Print Z"); // NOI18N
        printCollLbl1.setFocusable(false);
        printCollLbl1.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        printCollLbl1.setRequestFocusEnabled(false);
        printCollLbl1.setVerifyInputWhenFocusTarget(false);
        ZReadingPanel.add(printCollLbl1);
        printCollLbl1.setBounds(430, 80, 50, 40);

        printZReadingBtn.setIcon(new javax.swing.ImageIcon(getClass().getResource("/hybrid/resources/Keybutton.png"))); // NOI18N
        printZReadingBtn.setIconTextGap(2);
        printZReadingBtn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                printZReadingBtnMouseClicked(evt);
            }
        });
        ZReadingPanel.add(printZReadingBtn);
        printZReadingBtn.setBounds(420, 80, 70, 40);
        logcancelbutton.addMouseListener(new java.awt.event.MouseAdapter() {             public void mousePressed(java.awt.event.MouseEvent evt) {                 Funckeymouseover(evt.getSource());             }             public void mouseReleased(java.awt.event.MouseEvent evt) {                 Funckeymouseoff(evt.getSource());             }         });

        LowerLeftPanelX.add(ZReadingPanel);
        ZReadingPanel.setBounds(-1, -1, 540, 170);

        newMidPanel.add(LowerLeftPanelX);

        LowerRightPanel.setOpaque(false);
        LowerRightPanel.setLayout(null);

        SettPanel.setOpaque(false);
        SettPanel.setLayout(null);

        PWDoscaID.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        PWDoscaID.setBackground(new java.awt.Color(51, 51, 255));
        PWDoscaID.setCaretColor(new java.awt.Color(255, 255, 0));
        PWDoscaID.setCursor(new java.awt.Cursor(java.awt.Cursor.CROSSHAIR_CURSOR));
        PWDoscaID.setFont(new java.awt.Font("Tahoma", 1, 20)); // NOI18N
        PWDoscaID.setForeground(new java.awt.Color(255, 255, 51));
        PWDoscaID.setNextFocusableComponent(oscaName);
        PWDoscaID.setOpaque(false);
        PWDoscaID.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                PWDoscaIDKeyPressed(evt);
            }
        });
        SettPanel.add(PWDoscaID);
        PWDoscaID.setBounds(200, 0, 170, 40);

        oscaName.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        oscaName.setBackground(new java.awt.Color(51, 51, 255));
        oscaName.setCaretColor(new java.awt.Color(255, 255, 0));
        oscaName.setCursor(new java.awt.Cursor(java.awt.Cursor.CROSSHAIR_CURSOR));
        oscaName.setFont(new java.awt.Font("Tahoma", 1, 20)); // NOI18N
        oscaName.setForeground(new java.awt.Color(255, 255, 51));
        oscaName.setNextFocusableComponent(oscaAddr);
        oscaName.setOpaque(false);
        oscaName.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                oscaNameActionPerformed(evt);
            }
        });
        oscaName.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                oscaNameKeyPressed(evt);
            }
        });
        SettPanel.add(oscaName);
        oscaName.setBounds(80, 60, 170, 40);

        oscaAddr.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        oscaAddr.setBackground(new java.awt.Color(51, 51, 255));
        oscaAddr.setCaretColor(new java.awt.Color(255, 255, 0));
        oscaAddr.setCursor(new java.awt.Cursor(java.awt.Cursor.CROSSHAIR_CURSOR));
        oscaAddr.setFont(new java.awt.Font("Tahoma", 1, 20)); // NOI18N
        oscaAddr.setForeground(new java.awt.Color(255, 255, 51));
        oscaAddr.setNextFocusableComponent(oscaTIN);
        oscaAddr.setOpaque(false);
        oscaAddr.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                oscaAddrKeyPressed(evt);
            }
        });
        SettPanel.add(oscaAddr);
        oscaAddr.setBounds(80, 120, 170, 40);

        oscaTIN.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        oscaTIN.setBackground(new java.awt.Color(51, 51, 255));
        oscaTIN.setCaretColor(new java.awt.Color(255, 255, 0));
        oscaTIN.setCursor(new java.awt.Cursor(java.awt.Cursor.CROSSHAIR_CURSOR));
        oscaTIN.setFont(new java.awt.Font("Tahoma", 1, 20)); // NOI18N
        oscaTIN.setForeground(new java.awt.Color(255, 255, 51));
        oscaTIN.setNextFocusableComponent(oscaBusStyle);
        oscaTIN.setOpaque(false);
        oscaTIN.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                oscaTINKeyPressed(evt);
            }
        });
        SettPanel.add(oscaTIN);
        oscaTIN.setBounds(310, 60, 170, 40);

        oscaBusStyle.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        oscaBusStyle.setBackground(new java.awt.Color(51, 51, 255));
        oscaBusStyle.setCaretColor(new java.awt.Color(255, 255, 0));
        oscaBusStyle.setCursor(new java.awt.Cursor(java.awt.Cursor.CROSSHAIR_CURSOR));
        oscaBusStyle.setFont(new java.awt.Font("Tahoma", 1, 20)); // NOI18N
        oscaBusStyle.setForeground(new java.awt.Color(255, 255, 51));
        oscaBusStyle.setNextFocusableComponent(PWDoscaID);
        oscaBusStyle.setOpaque(false);
        oscaBusStyle.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                oscaBusStyleActionPerformed(evt);
            }
        });
        oscaBusStyle.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                oscaBusStyleKeyPressed(evt);
            }
        });
        SettPanel.add(oscaBusStyle);
        oscaBusStyle.setBounds(310, 120, 170, 40);

        settLbl3.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        settLbl3.setForeground(new java.awt.Color(204, 204, 204));
        settLbl3.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        settLbl3.setText("SC/PWD/OSCA ID");
        settLbl3.setFocusable(false);
        settLbl3.setRequestFocusEnabled(false);
        settLbl3.setVerifyInputWhenFocusTarget(false);
        SettPanel.add(settLbl3);
        settLbl3.setBounds(0, 0, 190, 30);

        settLbl5.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        settLbl5.setForeground(new java.awt.Color(204, 204, 204));
        settLbl5.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        settLbl5.setText("NAME:");
        settLbl5.setFocusable(false);
        settLbl5.setRequestFocusEnabled(false);
        settLbl5.setVerifyInputWhenFocusTarget(false);
        SettPanel.add(settLbl5);
        settLbl5.setBounds(0, 30, 120, 40);

        settLbl7.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        settLbl7.setForeground(new java.awt.Color(204, 204, 204));
        settLbl7.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        settLbl7.setText("TIN:");
        settLbl7.setFocusable(false);
        settLbl7.setRequestFocusEnabled(false);
        settLbl7.setVerifyInputWhenFocusTarget(false);
        SettPanel.add(settLbl7);
        settLbl7.setBounds(190, 30, 120, 40);

        settLbl4.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        settLbl4.setText("ADDRESS:");
        settLbl4.setFocusable(false);
        settLbl4.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        settLbl4.setForeground(new java.awt.Color(204, 204, 204));
        settLbl4.setRequestFocusEnabled(false);
        settLbl4.setVerifyInputWhenFocusTarget(false);
        SettPanel.add(settLbl4);
        settLbl4.setBounds(0, 90, 120, 40);

        settLbl6.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        settLbl6.setForeground(new java.awt.Color(204, 204, 204));
        settLbl6.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        settLbl6.setText("BUS.STYLE:");
        settLbl6.setFocusable(false);
        settLbl6.setRequestFocusEnabled(false);
        settLbl6.setVerifyInputWhenFocusTarget(false);
        SettPanel.add(settLbl6);
        settLbl6.setBounds(190, 90, 120, 40);

        LowerRightPanel.add(SettPanel);
        SettPanel.setBounds(0, 0, 540, 200);

        LostPanel.setOpaque(false);
        LostPanel.setLayout(null);

        lostLbl.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        lostLbl.setForeground(new java.awt.Color(204, 204, 204));
        lostLbl.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lostLbl.setText("LOST CARD:");
        lostLbl.setFocusable(false);
        lostLbl.setRequestFocusEnabled(false);
        lostLbl.setVerifyInputWhenFocusTarget(false);
        LostPanel.add(lostLbl);
        lostLbl.setBounds(0, 10, 130, 40);

        lostLbl1.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        lostLbl1.setForeground(new java.awt.Color(204, 204, 204));
        lostLbl1.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lostLbl1.setText("DATE IN:");
        lostLbl1.setFocusable(false);
        lostLbl1.setRequestFocusEnabled(false);
        lostLbl1.setVerifyInputWhenFocusTarget(false);
        LostPanel.add(lostLbl1);
        lostLbl1.setBounds(0, 50, 120, 40);

        lostLbl2.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lostLbl2.setText("TIME IN:");
        lostLbl2.setFocusable(false);
        lostLbl2.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        lostLbl2.setForeground(new java.awt.Color(204, 204, 204));
        lostLbl2.setRequestFocusEnabled(false);
        lostLbl2.setVerifyInputWhenFocusTarget(false);
        LostPanel.add(lostLbl2);
        lostLbl2.setBounds(0, 90, 120, 40);

        lostDateIN.setNothingAllowed(false);
        lostDateIN.setFieldFont(new java.awt.Font("Tahoma", java.awt.Font.BOLD, 16));
        lostDateIN.setNavigateFont(new java.awt.Font("Tahoma", java.awt.Font.PLAIN, 14));
        lostDateIN.setBehavior(datechooser.model.multiple.MultyModelBehavior.SELECT_SINGLE);
        LostPanel.add(lostDateIN);
        lostDateIN.setBounds(130, 50, 190, 40);

        goLostBtn.setText("Go");
        goLostBtn.setFont(new java.awt.Font("Tahoma", 1, 21)); // NOI18N
        goLostBtn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                goLostBtnMouseClicked(evt);
            }
        });
        LostPanel.add(goLostBtn);
        goLostBtn.setBounds(333, 40, 140, 100);
        LostPanel.add(lostTimeIN);
        lostTimeIN.setBounds(130, 93, 190, 40);

        LowerRightPanel.add(LostPanel);
        LostPanel.setBounds(0, 0, 540, 170);

        ReprintPanel.setOpaque(false);
        ReprintPanel.setLayout(null);

        reprintLbl1.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        reprintLbl1.setForeground(new java.awt.Color(204, 204, 204));
        reprintLbl1.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        reprintLbl1.setText("REPRINT");
        reprintLbl1.setFocusable(false);
        reprintLbl1.setRequestFocusEnabled(false);
        reprintLbl1.setVerifyInputWhenFocusTarget(false);
        ReprintPanel.add(reprintLbl1);
        reprintLbl1.setBounds(0, 0, 120, 30);

        reprintPlate.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        reprintPlate.setBackground(new java.awt.Color(51, 51, 255));
        reprintPlate.setFont(new java.awt.Font("Tahoma", 1, 20)); // NOI18N
        reprintPlate.setForeground(new java.awt.Color(255, 255, 51));
        reprintPlate.setOpaque(false);
        ReprintPanel.add(reprintPlate);
        reprintPlate.setBounds(129, 25, 190, 50);

        reprintLbl2.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        reprintLbl2.setForeground(new java.awt.Color(204, 204, 204));
        reprintLbl2.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        reprintLbl2.setText("Plate No.:");
        reprintLbl2.setFocusable(false);
        reprintLbl2.setRequestFocusEnabled(false);
        reprintLbl2.setVerifyInputWhenFocusTarget(false);
        ReprintPanel.add(reprintLbl2);
        reprintLbl2.setBounds(0, 30, 120, 40);

        reprintBtn.setText("Search");
        reprintBtn.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        reprintBtn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                reprintBtnMousePressed(evt);
            }
        });
        ReprintPanel.add(reprintBtn);
        reprintBtn.setBounds(330, 23, 130, 50);

        reprintOut.setText("Print");
        reprintOut.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        reprintOut.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                reprintOutMousePressed(evt);
            }
        });
        ReprintPanel.add(reprintOut);
        reprintOut.setBounds(330, 83, 130, 80);

        jScrollPane1.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N

        jList1.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jList1.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        jScrollPane1.setViewportView(jList1);

        ReprintPanel.add(jScrollPane1);
        jScrollPane1.setBounds(20, 80, 300, 80);

        LowerRightPanel.add(ReprintPanel);
        ReprintPanel.setBounds(0, 0, 520, 170);

        RefundPanel.setOpaque(false);
        RefundPanel.setLayout(null);

        voidLabel.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        voidLabel.setText("VOID/REFUND");
        voidLabel.setFocusable(false);
        voidLabel.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        voidLabel.setForeground(new java.awt.Color(204, 204, 204));
        voidLabel.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        voidLabel.setRequestFocusEnabled(false);
        voidLabel.setVerifyInputWhenFocusTarget(false);
        RefundPanel.add(voidLabel);
        voidLabel.setBounds(0, 0, 190, 30);

        voidSelection.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        voidSelection.setBackground(new java.awt.Color(51, 51, 255));
        voidSelection.setFont(new java.awt.Font("Tahoma", 1, 20)); // NOI18N
        voidSelection.setForeground(new java.awt.Color(255, 255, 51));
        voidSelection.setOpaque(false);
        voidSelection.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                voidSelectionKeyTyped(evt);
            }
        });
        RefundPanel.add(voidSelection);
        voidSelection.setBounds(189, 25, 130, 50);

        refundBtn.setText("Search");
        refundBtn.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        refundBtn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                refundBtnMousePressed(evt);
            }
        });
        RefundPanel.add(refundBtn);
        refundBtn.setBounds(330, 23, 130, 50);

        refundOut.setText("Print");
        refundOut.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        refundOut.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                refundOutMousePressed(evt);
            }
        });
        refundOut.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                refundOutActionPerformed(evt);
            }
        });
        RefundPanel.add(refundOut);
        refundOut.setBounds(330, 83, 130, 80);

        voidTypeSelection.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "by Card #", "by Plate #", "by Receipt #" }));
        voidTypeSelection.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        voidTypeSelection.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                voidTypeSelectionItemStateChanged(evt);
            }
        });
        RefundPanel.add(voidTypeSelection);
        voidTypeSelection.setBounds(20, 30, 160, 30);

        transactions4Void.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N

        trans4Void.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        trans4Void.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        transactions4Void.setViewportView(trans4Void);

        RefundPanel.add(transactions4Void);
        transactions4Void.setBounds(20, 80, 300, 80);

        LowerRightPanel.add(RefundPanel);
        RefundPanel.setBounds(0, 0, 520, 170);

        newMidPanel.add(LowerRightPanel);

        CenterPanel.add(newMidPanel, java.awt.BorderLayout.CENTER);

        fullKeyBoard.setMinimumSize(new java.awt.Dimension(0, 0));
        fullKeyBoard.setPreferredSize(new java.awt.Dimension(0, 200));
        fullKeyBoard.setOpaque(false);
        fullKeyBoard.setLayout(new java.awt.BorderLayout());

        keypadPanel.setOpaque(false);
        keypadPanel.setLayout(null);

        LettersPad.setMinimumSize(new java.awt.Dimension(100, 100));
        LettersPad.setPreferredSize(new java.awt.Dimension(100, 100));
        LettersPad.setOpaque(false);
        LettersPad.setLayout(new java.awt.GridLayout(3, 3, 1, 1));

        KeyLabel17.setFont(KeyLabel17.getFont().deriveFont(KeyLabel17.getFont().getStyle() | java.awt.Font.BOLD, KeyLabel17.getFont().getSize()+14));
        KeyLabel17.setForeground(new java.awt.Color(153, 153, 255));
        KeyLabel17.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        KeyLabel17.setLabelFor(Q);
        KeyLabel17.setText("Q"); // NOI18N
        KeyLabel17.setFocusable(false);
        KeyLabel17.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        KeyLabel17.setRequestFocusEnabled(false);
        KeyLabel17.setVerifyInputWhenFocusTarget(false);
        LettersPad.add(KeyLabel17);

        KeyLabel23.setFont(KeyLabel23.getFont().deriveFont(KeyLabel23.getFont().getStyle() | java.awt.Font.BOLD, KeyLabel23.getFont().getSize()+14));
        KeyLabel23.setForeground(new java.awt.Color(153, 153, 255));
        KeyLabel23.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        KeyLabel23.setLabelFor(W);
        KeyLabel23.setText("W"); // NOI18N
        KeyLabel23.setFocusable(false);
        KeyLabel23.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        KeyLabel23.setRequestFocusEnabled(false);
        KeyLabel23.setVerifyInputWhenFocusTarget(false);
        LettersPad.add(KeyLabel23);

        KeyLabel5.setFont(KeyLabel5.getFont().deriveFont(KeyLabel5.getFont().getStyle() | java.awt.Font.BOLD, KeyLabel5.getFont().getSize()+14));
        KeyLabel5.setForeground(new java.awt.Color(153, 153, 255));
        KeyLabel5.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        KeyLabel5.setLabelFor(E);
        KeyLabel5.setText("E"); // NOI18N
        KeyLabel5.setFocusable(false);
        KeyLabel5.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        KeyLabel5.setRequestFocusEnabled(false);
        KeyLabel5.setVerifyInputWhenFocusTarget(false);
        LettersPad.add(KeyLabel5);

        KeyLabel18.setFont(KeyLabel18.getFont().deriveFont(KeyLabel18.getFont().getStyle() | java.awt.Font.BOLD, KeyLabel18.getFont().getSize()+14));
        KeyLabel18.setForeground(new java.awt.Color(153, 153, 255));
        KeyLabel18.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        KeyLabel18.setLabelFor(R);
        KeyLabel18.setText("R"); // NOI18N
        KeyLabel18.setFocusable(false);
        KeyLabel18.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        KeyLabel18.setRequestFocusEnabled(false);
        KeyLabel18.setVerifyInputWhenFocusTarget(false);
        LettersPad.add(KeyLabel18);

        KeyLabel20.setFont(KeyLabel20.getFont().deriveFont(KeyLabel20.getFont().getStyle() | java.awt.Font.BOLD, KeyLabel20.getFont().getSize()+14));
        KeyLabel20.setForeground(new java.awt.Color(153, 153, 255));
        KeyLabel20.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        KeyLabel20.setLabelFor(T);
        KeyLabel20.setText("T"); // NOI18N
        KeyLabel20.setFocusable(false);
        KeyLabel20.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        KeyLabel20.setRequestFocusEnabled(false);
        KeyLabel20.setVerifyInputWhenFocusTarget(false);
        LettersPad.add(KeyLabel20);

        KeyLabel25.setFont(KeyLabel25.getFont().deriveFont(KeyLabel25.getFont().getStyle() | java.awt.Font.BOLD, KeyLabel25.getFont().getSize()+14));
        KeyLabel25.setForeground(new java.awt.Color(153, 153, 255));
        KeyLabel25.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        KeyLabel25.setLabelFor(Y);
        KeyLabel25.setText("Y"); // NOI18N
        KeyLabel25.setFocusable(false);
        KeyLabel25.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        KeyLabel25.setRequestFocusEnabled(false);
        KeyLabel25.setVerifyInputWhenFocusTarget(false);
        LettersPad.add(KeyLabel25);

        KeyLabel21.setFont(KeyLabel21.getFont().deriveFont(KeyLabel21.getFont().getStyle() | java.awt.Font.BOLD, KeyLabel21.getFont().getSize()+14));
        KeyLabel21.setForeground(new java.awt.Color(153, 153, 255));
        KeyLabel21.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        KeyLabel21.setLabelFor(U);
        KeyLabel21.setText("U"); // NOI18N
        KeyLabel21.setFocusable(false);
        KeyLabel21.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        KeyLabel21.setRequestFocusEnabled(false);
        KeyLabel21.setVerifyInputWhenFocusTarget(false);
        LettersPad.add(KeyLabel21);

        KeyLabel9.setFont(KeyLabel9.getFont().deriveFont(KeyLabel9.getFont().getStyle() | java.awt.Font.BOLD, KeyLabel9.getFont().getSize()+14));
        KeyLabel9.setForeground(new java.awt.Color(153, 153, 255));
        KeyLabel9.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        KeyLabel9.setLabelFor(I);
        KeyLabel9.setText("I"); // NOI18N
        KeyLabel9.setFocusable(false);
        KeyLabel9.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        KeyLabel9.setRequestFocusEnabled(false);
        KeyLabel9.setVerifyInputWhenFocusTarget(false);
        LettersPad.add(KeyLabel9);

        KeyLabel15.setFont(KeyLabel15.getFont().deriveFont(KeyLabel15.getFont().getStyle() | java.awt.Font.BOLD, KeyLabel15.getFont().getSize()+14));
        KeyLabel15.setForeground(new java.awt.Color(153, 153, 255));
        KeyLabel15.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        KeyLabel15.setLabelFor(O);
        KeyLabel15.setText("O"); // NOI18N
        KeyLabel15.setFocusable(false);
        KeyLabel15.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        KeyLabel15.setRequestFocusEnabled(false);
        KeyLabel15.setVerifyInputWhenFocusTarget(false);
        LettersPad.add(KeyLabel15);

        KeyLabel16.setFont(KeyLabel16.getFont().deriveFont(KeyLabel16.getFont().getStyle() | java.awt.Font.BOLD, KeyLabel16.getFont().getSize()+14));
        KeyLabel16.setForeground(new java.awt.Color(153, 153, 255));
        KeyLabel16.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        KeyLabel16.setLabelFor(P);
        KeyLabel16.setText("P"); // NOI18N
        KeyLabel16.setFocusable(false);
        KeyLabel16.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        KeyLabel16.setRequestFocusEnabled(false);
        KeyLabel16.setVerifyInputWhenFocusTarget(false);
        LettersPad.add(KeyLabel16);

        KeyLabel1.setFont(KeyLabel1.getFont().deriveFont(KeyLabel1.getFont().getStyle() | java.awt.Font.BOLD, KeyLabel1.getFont().getSize()+14));
        KeyLabel1.setForeground(new java.awt.Color(153, 153, 255));
        KeyLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        KeyLabel1.setLabelFor(A);
        KeyLabel1.setText("A"); // NOI18N
        KeyLabel1.setFocusable(false);
        KeyLabel1.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        KeyLabel1.setRequestFocusEnabled(false);
        KeyLabel1.setVerifyInputWhenFocusTarget(false);
        LettersPad.add(KeyLabel1);

        KeyLabel19.setFont(KeyLabel19.getFont().deriveFont(KeyLabel19.getFont().getStyle() | java.awt.Font.BOLD, KeyLabel19.getFont().getSize()+14));
        KeyLabel19.setForeground(new java.awt.Color(153, 153, 255));
        KeyLabel19.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        KeyLabel19.setLabelFor(S);
        KeyLabel19.setText("S"); // NOI18N
        KeyLabel19.setFocusable(false);
        KeyLabel19.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        KeyLabel19.setRequestFocusEnabled(false);
        KeyLabel19.setVerifyInputWhenFocusTarget(false);
        LettersPad.add(KeyLabel19);

        KeyLabel4.setFont(KeyLabel4.getFont().deriveFont(KeyLabel4.getFont().getStyle() | java.awt.Font.BOLD, KeyLabel4.getFont().getSize()+14));
        KeyLabel4.setForeground(new java.awt.Color(153, 153, 255));
        KeyLabel4.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        KeyLabel4.setLabelFor(D);
        KeyLabel4.setText("D"); // NOI18N
        KeyLabel4.setFocusable(false);
        KeyLabel4.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        KeyLabel4.setRequestFocusEnabled(false);
        KeyLabel4.setVerifyInputWhenFocusTarget(false);
        LettersPad.add(KeyLabel4);

        KeyLabel6.setFont(new java.awt.Font("Tahoma", 1, 30)); // NOI18N
        KeyLabel6.setForeground(new java.awt.Color(153, 153, 255));
        KeyLabel6.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        KeyLabel6.setLabelFor(F);
        KeyLabel6.setText("F"); // NOI18N
        KeyLabel6.setFocusable(false);
        KeyLabel6.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        KeyLabel6.setRequestFocusEnabled(false);
        KeyLabel6.setVerifyInputWhenFocusTarget(false);
        LettersPad.add(KeyLabel6);

        KeyLabel7.setFont(KeyLabel7.getFont().deriveFont(KeyLabel7.getFont().getStyle() | java.awt.Font.BOLD, KeyLabel7.getFont().getSize()+14));
        KeyLabel7.setForeground(new java.awt.Color(153, 153, 255));
        KeyLabel7.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        KeyLabel7.setLabelFor(G);
        KeyLabel7.setText("G"); // NOI18N
        KeyLabel7.setFocusable(false);
        KeyLabel7.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        KeyLabel7.setRequestFocusEnabled(false);
        KeyLabel7.setVerifyInputWhenFocusTarget(false);
        LettersPad.add(KeyLabel7);

        KeyLabel8.setFont(KeyLabel8.getFont().deriveFont(KeyLabel8.getFont().getStyle() | java.awt.Font.BOLD, KeyLabel8.getFont().getSize()+14));
        KeyLabel8.setForeground(new java.awt.Color(153, 153, 255));
        KeyLabel8.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        KeyLabel8.setLabelFor(H);
        KeyLabel8.setText("H"); // NOI18N
        KeyLabel8.setFocusable(false);
        KeyLabel8.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        KeyLabel8.setRequestFocusEnabled(false);
        KeyLabel8.setVerifyInputWhenFocusTarget(false);
        LettersPad.add(KeyLabel8);

        KeyLabel10.setFont(KeyLabel10.getFont().deriveFont(KeyLabel10.getFont().getStyle() | java.awt.Font.BOLD, KeyLabel10.getFont().getSize()+14));
        KeyLabel10.setForeground(new java.awt.Color(153, 153, 255));
        KeyLabel10.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        KeyLabel10.setLabelFor(J);
        KeyLabel10.setText("J"); // NOI18N
        KeyLabel10.setFocusable(false);
        KeyLabel10.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        KeyLabel10.setRequestFocusEnabled(false);
        KeyLabel10.setVerifyInputWhenFocusTarget(false);
        LettersPad.add(KeyLabel10);

        KeyLabel11.setFont(KeyLabel11.getFont().deriveFont(KeyLabel11.getFont().getStyle() | java.awt.Font.BOLD, KeyLabel11.getFont().getSize()+14));
        KeyLabel11.setForeground(new java.awt.Color(153, 153, 255));
        KeyLabel11.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        KeyLabel11.setLabelFor(K);
        KeyLabel11.setText("K"); // NOI18N
        KeyLabel11.setFocusable(false);
        KeyLabel11.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        KeyLabel11.setRequestFocusEnabled(false);
        KeyLabel11.setVerifyInputWhenFocusTarget(false);
        LettersPad.add(KeyLabel11);

        KeyLabel12.setFont(KeyLabel12.getFont().deriveFont(KeyLabel12.getFont().getStyle() | java.awt.Font.BOLD, KeyLabel12.getFont().getSize()+14));
        KeyLabel12.setForeground(new java.awt.Color(153, 153, 255));
        KeyLabel12.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        KeyLabel12.setLabelFor(L);
        KeyLabel12.setText("L"); // NOI18N
        KeyLabel12.setFocusable(false);
        KeyLabel12.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        KeyLabel12.setRequestFocusEnabled(false);
        KeyLabel12.setVerifyInputWhenFocusTarget(false);
        LettersPad.add(KeyLabel12);

        KeyLabel28.setFont(KeyLabel28.getFont().deriveFont(KeyLabel28.getFont().getStyle() | java.awt.Font.BOLD, KeyLabel28.getFont().getSize()+10));
        KeyLabel28.setForeground(new java.awt.Color(153, 153, 255));
        KeyLabel28.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        KeyLabel28.setLabelFor(ENTER);
        KeyLabel28.setText("Enter"); // NOI18N
        KeyLabel28.setFocusable(false);
        KeyLabel28.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        KeyLabel28.setRequestFocusEnabled(false);
        KeyLabel28.setVerifyInputWhenFocusTarget(false);
        LettersPad.add(KeyLabel28);

        KeyLabel26.setFont(KeyLabel26.getFont().deriveFont(KeyLabel26.getFont().getStyle() | java.awt.Font.BOLD, KeyLabel26.getFont().getSize()+14));
        KeyLabel26.setForeground(new java.awt.Color(153, 153, 255));
        KeyLabel26.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        KeyLabel26.setLabelFor(Z);
        KeyLabel26.setText("Z"); // NOI18N
        KeyLabel26.setFocusable(false);
        KeyLabel26.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        KeyLabel26.setRequestFocusEnabled(false);
        KeyLabel26.setVerifyInputWhenFocusTarget(false);
        LettersPad.add(KeyLabel26);

        KeyLabel24.setFont(KeyLabel24.getFont().deriveFont(KeyLabel24.getFont().getStyle() | java.awt.Font.BOLD, KeyLabel24.getFont().getSize()+14));
        KeyLabel24.setForeground(new java.awt.Color(153, 153, 255));
        KeyLabel24.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        KeyLabel24.setLabelFor(X);
        KeyLabel24.setText("X"); // NOI18N
        KeyLabel24.setFocusable(false);
        KeyLabel24.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        KeyLabel24.setRequestFocusEnabled(false);
        KeyLabel24.setVerifyInputWhenFocusTarget(false);
        LettersPad.add(KeyLabel24);

        KeyLabel3.setFont(KeyLabel3.getFont().deriveFont(KeyLabel3.getFont().getStyle() | java.awt.Font.BOLD, KeyLabel3.getFont().getSize()+14));
        KeyLabel3.setForeground(new java.awt.Color(153, 153, 255));
        KeyLabel3.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        KeyLabel3.setLabelFor(C);
        KeyLabel3.setText("C"); // NOI18N
        KeyLabel3.setFocusable(false);
        KeyLabel3.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        KeyLabel3.setRequestFocusEnabled(false);
        KeyLabel3.setVerifyInputWhenFocusTarget(false);
        LettersPad.add(KeyLabel3);

        KeyLabel22.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        KeyLabel22.setLabelFor(V);
        KeyLabel22.setText("V"); // NOI18N
        KeyLabel22.setFocusable(false);
        KeyLabel22.setFont(KeyLabel22.getFont().deriveFont(KeyLabel22.getFont().getStyle() | java.awt.Font.BOLD, KeyLabel22.getFont().getSize()+14));
        KeyLabel22.setForeground(new java.awt.Color(153, 153, 255));
        KeyLabel22.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        KeyLabel22.setRequestFocusEnabled(false);
        KeyLabel22.setVerifyInputWhenFocusTarget(false);
        LettersPad.add(KeyLabel22);

        KeyLabel2.setFont(KeyLabel2.getFont().deriveFont(KeyLabel2.getFont().getStyle() | java.awt.Font.BOLD, KeyLabel2.getFont().getSize()+14));
        KeyLabel2.setForeground(new java.awt.Color(153, 153, 255));
        KeyLabel2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        KeyLabel2.setLabelFor(B);
        KeyLabel2.setText("B"); // NOI18N
        KeyLabel2.setFocusable(false);
        KeyLabel2.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        KeyLabel2.setRequestFocusEnabled(false);
        KeyLabel2.setVerifyInputWhenFocusTarget(false);
        LettersPad.add(KeyLabel2);

        KeyLabel14.setFont(KeyLabel14.getFont().deriveFont(KeyLabel14.getFont().getStyle() | java.awt.Font.BOLD, KeyLabel14.getFont().getSize()+14));
        KeyLabel14.setForeground(new java.awt.Color(153, 153, 255));
        KeyLabel14.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        KeyLabel14.setLabelFor(N);
        KeyLabel14.setText("N"); // NOI18N
        KeyLabel14.setFocusable(false);
        KeyLabel14.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        KeyLabel14.setRequestFocusEnabled(false);
        KeyLabel14.setVerifyInputWhenFocusTarget(false);
        LettersPad.add(KeyLabel14);

        KeyLabel13.setFont(KeyLabel13.getFont().deriveFont(KeyLabel13.getFont().getStyle() | java.awt.Font.BOLD, KeyLabel13.getFont().getSize()+14));
        KeyLabel13.setForeground(new java.awt.Color(153, 153, 255));
        KeyLabel13.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        KeyLabel13.setLabelFor(M);
        KeyLabel13.setText("M"); // NOI18N
        KeyLabel13.setFocusable(false);
        KeyLabel13.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        KeyLabel13.setRequestFocusEnabled(false);
        KeyLabel13.setVerifyInputWhenFocusTarget(false);
        LettersPad.add(KeyLabel13);

        KeyLabel27.setFont(KeyLabel27.getFont().deriveFont(KeyLabel27.getFont().getStyle() | java.awt.Font.BOLD, KeyLabel27.getFont().getSize()+14));
        KeyLabel27.setForeground(new java.awt.Color(153, 153, 255));
        KeyLabel27.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        KeyLabel27.setLabelFor(DASH);
        KeyLabel27.setText("-"); // NOI18N
        KeyLabel27.setFocusable(false);
        KeyLabel27.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        KeyLabel27.setRequestFocusEnabled(false);
        KeyLabel27.setVerifyInputWhenFocusTarget(false);
        LettersPad.add(KeyLabel27);

        KeyLabel29.setFont(KeyLabel29.getFont().deriveFont(KeyLabel29.getFont().getStyle() | java.awt.Font.BOLD, KeyLabel29.getFont().getSize()+14));
        KeyLabel29.setForeground(new java.awt.Color(153, 153, 255));
        KeyLabel29.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        KeyLabel29.setLabelFor(BACKSPACE);
        KeyLabel29.setText("<--"); // NOI18N
        KeyLabel29.setFocusable(false);
        KeyLabel29.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        KeyLabel29.setRequestFocusEnabled(false);
        KeyLabel29.setVerifyInputWhenFocusTarget(false);
        KeyLabel29.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                Backspaced(evt);
            }
        });
        LettersPad.add(KeyLabel29);

        keypadPanel.add(LettersPad);
        LettersPad.setBounds(0, 0, 650, 200);

        ButtonPad.setFocusable(false);
        ButtonPad.setMinimumSize(new java.awt.Dimension(100, 100));
        ButtonPad.setOpaque(false);
        ButtonPad.setPreferredSize(new java.awt.Dimension(100, 100));
        ButtonPad.setLayout(new java.awt.GridLayout(3, 3, 20, 30));

        Button17.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        Button17.setLabelFor(KeyLabel17);
        ButtonPad.add(Button17);
        Button17.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                anykeymouseover(evt.getSource());
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                anykeymouseoff(evt.getSource());
            }
        });

        Button23.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        Button23.setLabelFor(KeyLabel23);
        ButtonPad.add(Button23);
        Button23.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                anykeymouseover(evt.getSource());
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                anykeymouseoff(evt.getSource());
            }
        });

        Button5.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        Button5.setLabelFor(KeyLabel5);
        ButtonPad.add(Button5);
        Button5.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                anykeymouseover(evt.getSource());
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                anykeymouseoff(evt.getSource());
            }
        });

        Button18.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        Button18.setLabelFor(KeyLabel18);
        ButtonPad.add(Button18);
        Button18.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                anykeymouseover(evt.getSource());
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                anykeymouseoff(evt.getSource());
            }
        });

        Button20.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        Button20.setLabelFor(KeyLabel20);
        ButtonPad.add(Button20);
        Button20.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                anykeymouseover(evt.getSource());
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                anykeymouseoff(evt.getSource());
            }
        });

        Button25.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        Button25.setLabelFor(KeyLabel25);
        ButtonPad.add(Button25);
        Button25.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                anykeymouseover(evt.getSource());
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                anykeymouseoff(evt.getSource());
            }
        });

        Button21.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        Button21.setLabelFor(KeyLabel21);
        ButtonPad.add(Button21);
        Button21.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                anykeymouseover(evt.getSource());
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                anykeymouseoff(evt.getSource());
            }
        });

        Button9.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        Button9.setLabelFor(KeyLabel9);
        ButtonPad.add(Button9);
        Button9.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                anykeymouseover(evt.getSource());
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                anykeymouseoff(evt.getSource());
            }
        });

        Button15.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        Button15.setLabelFor(KeyLabel15);
        ButtonPad.add(Button15);
        Button15.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                anykeymouseover(evt.getSource());
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                anykeymouseoff(evt.getSource());
            }
        });

        Button16.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        Button16.setLabelFor(KeyLabel16);
        ButtonPad.add(Button16);
        Button16.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                anykeymouseover(evt.getSource());
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                anykeymouseoff(evt.getSource());
            }
        });

        Button1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        Button1.setLabelFor(KeyLabel1);
        ButtonPad.add(Button1);
        Button1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                anykeymouseover(evt.getSource());
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                anykeymouseoff(evt.getSource());
            }
        });

        Button19.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        Button19.setLabelFor(KeyLabel19);
        ButtonPad.add(Button19);
        Button19.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                anykeymouseover(evt.getSource());
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                anykeymouseoff(evt.getSource());
            }
        });

        Button4.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        Button4.setLabelFor(KeyLabel4);
        ButtonPad.add(Button4);
        Button4.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                anykeymouseover(evt.getSource());
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                anykeymouseoff(evt.getSource());
            }
        });

        Button6.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        Button6.setLabelFor(KeyLabel6);
        ButtonPad.add(Button6);
        Button6.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                anykeymouseover(evt.getSource());
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                anykeymouseoff(evt.getSource());
            }
        });

        Button7.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        Button7.setLabelFor(KeyLabel7);
        ButtonPad.add(Button7);
        Button7.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                anykeymouseover(evt.getSource());
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                anykeymouseoff(evt.getSource());
            }
        });

        Button8.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        Button8.setLabelFor(KeyLabel8);
        ButtonPad.add(Button8);
        Button8.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                anykeymouseover(evt.getSource());
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                anykeymouseoff(evt.getSource());
            }
        });

        Button10.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        Button10.setLabelFor(KeyLabel10);
        ButtonPad.add(Button10);
        Button10.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                anykeymouseover(evt.getSource());
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                anykeymouseoff(evt.getSource());
            }
        });

        Button11.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        Button11.setLabelFor(KeyLabel11);
        ButtonPad.add(Button11);
        Button11.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                anykeymouseover(evt.getSource());
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                anykeymouseoff(evt.getSource());
            }
        });

        Button12.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        Button12.setLabelFor(KeyLabel12);
        ButtonPad.add(Button12);
        Button12.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                anykeymouseover(evt.getSource());
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                anykeymouseoff(evt.getSource());
            }
        });

        Button28.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        Button28.setLabelFor(KeyLabel28);
        Button28.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                ManualEnter(evt);
            }
        });
        ButtonPad.add(Button28);

        Button26.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        Button26.setLabelFor(KeyLabel26);
        ButtonPad.add(Button26);
        Button26.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                anykeymouseover(evt.getSource());
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                anykeymouseoff(evt.getSource());
            }
        });

        Button24.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        Button24.setLabelFor(KeyLabel24);
        ButtonPad.add(Button24);
        Button24.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                anykeymouseover(evt.getSource());
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                anykeymouseoff(evt.getSource());
            }
        });

        Button3.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        Button3.setLabelFor(KeyLabel3);
        ButtonPad.add(Button3);
        Button3.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                anykeymouseover(evt.getSource());
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                anykeymouseoff(evt.getSource());
            }
        });

        Button22.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        Button22.setLabelFor(KeyLabel22);
        ButtonPad.add(Button22);
        Button22.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                anykeymouseover(evt.getSource());
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                anykeymouseoff(evt.getSource());
            }
        });

        Button2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        Button2.setLabelFor(KeyLabel2);
        ButtonPad.add(Button2);
        Button2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                anykeymouseover(evt.getSource());
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                anykeymouseoff(evt.getSource());
            }
        });

        Button14.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        Button14.setLabelFor(KeyLabel14);
        ButtonPad.add(Button14);
        Button14.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                anykeymouseover(evt.getSource());
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                anykeymouseoff(evt.getSource());
            }
        });

        Button13.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        Button13.setLabelFor(KeyLabel13);
        ButtonPad.add(Button13);
        Button13.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                anykeymouseover(evt.getSource());
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                anykeymouseoff(evt.getSource());
            }
        });

        Button27.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        Button27.setLabelFor(KeyLabel27);
        ButtonPad.add(Button27);

        Button29.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        ButtonPad.add(Button29);
        Button27.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                anykeymouseover(evt.getSource());
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                anykeymouseoff(evt.getSource());
            }
        });

        keypadPanel.add(ButtonPad);
        ButtonPad.setBounds(0, 0, 650, 200);

        ButtonPadBG.setFocusable(false);
        ButtonPadBG.setMinimumSize(new java.awt.Dimension(100, 100));
        ButtonPadBG.setOpaque(false);
        ButtonPadBG.setPreferredSize(new java.awt.Dimension(100, 100));
        ButtonPadBG.setLayout(new java.awt.GridLayout(3, 3));

        Q.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        Q.setIcon(new javax.swing.ImageIcon(getClass().getResource("/hybrid/resources/qwerty.png"))); // NOI18N
        Q.setLabelFor(KeyLabel17);
        Q.setMaximumSize(new java.awt.Dimension(90, 50));
        Q.setMinimumSize(new java.awt.Dimension(90, 50));
        Q.setPreferredSize(new java.awt.Dimension(90, 50));
        ButtonPadBG.add(Q);

        W.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        W.setIcon(new javax.swing.ImageIcon(getClass().getResource("/hybrid/resources/qwerty.png"))); // NOI18N
        W.setLabelFor(KeyLabel23);
        ButtonPadBG.add(W);

        E.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        E.setIcon(new javax.swing.ImageIcon(getClass().getResource("/hybrid/resources/qwerty.png"))); // NOI18N
        E.setLabelFor(KeyLabel5);
        ButtonPadBG.add(E);

        R.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        R.setIcon(new javax.swing.ImageIcon(getClass().getResource("/hybrid/resources/qwerty.png"))); // NOI18N
        R.setLabelFor(KeyLabel18);
        ButtonPadBG.add(R);

        T.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        T.setIcon(new javax.swing.ImageIcon(getClass().getResource("/hybrid/resources/qwerty.png"))); // NOI18N
        T.setLabelFor(KeyLabel20);
        ButtonPadBG.add(T);

        Y.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        Y.setIcon(new javax.swing.ImageIcon(getClass().getResource("/hybrid/resources/qwerty.png"))); // NOI18N
        Y.setLabelFor(KeyLabel25);
        ButtonPadBG.add(Y);

        U.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        U.setIcon(new javax.swing.ImageIcon(getClass().getResource("/hybrid/resources/qwerty.png"))); // NOI18N
        U.setLabelFor(KeyLabel21);
        ButtonPadBG.add(U);

        I.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        I.setIcon(new javax.swing.ImageIcon(getClass().getResource("/hybrid/resources/qwerty.png"))); // NOI18N
        I.setLabelFor(KeyLabel9);
        ButtonPadBG.add(I);

        O.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        O.setIcon(new javax.swing.ImageIcon(getClass().getResource("/hybrid/resources/qwerty.png"))); // NOI18N
        O.setLabelFor(KeyLabel15);
        ButtonPadBG.add(O);

        P.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        P.setIcon(new javax.swing.ImageIcon(getClass().getResource("/hybrid/resources/qwerty.png"))); // NOI18N
        P.setLabelFor(KeyLabel16);
        ButtonPadBG.add(P);

        A.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        A.setIcon(new javax.swing.ImageIcon(getClass().getResource("/hybrid/resources/qwerty.png"))); // NOI18N
        A.setLabelFor(KeyLabel1);
        ButtonPadBG.add(A);

        S.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        S.setIcon(new javax.swing.ImageIcon(getClass().getResource("/hybrid/resources/qwerty.png"))); // NOI18N
        S.setLabelFor(KeyLabel19);
        ButtonPadBG.add(S);

        D.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        D.setIcon(new javax.swing.ImageIcon(getClass().getResource("/hybrid/resources/qwerty.png"))); // NOI18N
        D.setLabelFor(KeyLabel4);
        ButtonPadBG.add(D);

        F.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        F.setIcon(new javax.swing.ImageIcon(getClass().getResource("/hybrid/resources/qwerty.png"))); // NOI18N
        F.setLabelFor(KeyLabel6);
        ButtonPadBG.add(F);

        G.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        G.setIcon(new javax.swing.ImageIcon(getClass().getResource("/hybrid/resources/qwerty.png"))); // NOI18N
        G.setLabelFor(KeyLabel7);
        ButtonPadBG.add(G);

        H.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        H.setIcon(new javax.swing.ImageIcon(getClass().getResource("/hybrid/resources/qwerty.png"))); // NOI18N
        H.setLabelFor(KeyLabel8);
        ButtonPadBG.add(H);

        J.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        J.setIcon(new javax.swing.ImageIcon(getClass().getResource("/hybrid/resources/qwerty.png"))); // NOI18N
        J.setLabelFor(KeyLabel10);
        ButtonPadBG.add(J);

        K.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        K.setIcon(new javax.swing.ImageIcon(getClass().getResource("/hybrid/resources/qwerty.png"))); // NOI18N
        K.setLabelFor(KeyLabel11);
        ButtonPadBG.add(K);

        L.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        L.setIcon(new javax.swing.ImageIcon(getClass().getResource("/hybrid/resources/qwerty.png"))); // NOI18N
        L.setLabelFor(KeyLabel12);
        ButtonPadBG.add(L);

        ENTER.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        ENTER.setIcon(new javax.swing.ImageIcon(getClass().getResource("/hybrid/resources/qwerty.png"))); // NOI18N
        ENTER.setLabelFor(KeyLabel28);
        ENTER.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                ENTERManualEnter(evt);
            }
        });
        ButtonPadBG.add(ENTER);

        Z.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        Z.setIcon(new javax.swing.ImageIcon(getClass().getResource("/hybrid/resources/qwerty.png"))); // NOI18N
        Z.setLabelFor(KeyLabel26);
        ButtonPadBG.add(Z);

        X.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        X.setIcon(new javax.swing.ImageIcon(getClass().getResource("/hybrid/resources/qwerty.png"))); // NOI18N
        X.setLabelFor(KeyLabel24);
        ButtonPadBG.add(X);

        C.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        C.setIcon(new javax.swing.ImageIcon(getClass().getResource("/hybrid/resources/qwerty.png"))); // NOI18N
        C.setLabelFor(KeyLabel3);
        ButtonPadBG.add(C);

        V.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        V.setIcon(new javax.swing.ImageIcon(getClass().getResource("/hybrid/resources/qwerty.png"))); // NOI18N
        V.setLabelFor(KeyLabel22);
        ButtonPadBG.add(V);

        B.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        B.setIcon(new javax.swing.ImageIcon(getClass().getResource("/hybrid/resources/qwerty.png"))); // NOI18N
        B.setLabelFor(KeyLabel2);
        ButtonPadBG.add(B);

        N.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        N.setIcon(new javax.swing.ImageIcon(getClass().getResource("/hybrid/resources/qwerty.png"))); // NOI18N
        N.setLabelFor(KeyLabel14);
        ButtonPadBG.add(N);

        M.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        M.setIcon(new javax.swing.ImageIcon(getClass().getResource("/hybrid/resources/qwerty.png"))); // NOI18N
        M.setLabelFor(KeyLabel13);
        ButtonPadBG.add(M);

        DASH.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        DASH.setIcon(new javax.swing.ImageIcon(getClass().getResource("/hybrid/resources/qwerty.png"))); // NOI18N
        DASH.setLabelFor(KeyLabel27);
        ButtonPadBG.add(DASH);

        BACKSPACE.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        BACKSPACE.setIcon(new javax.swing.ImageIcon(getClass().getResource("/hybrid/resources/qwerty.png"))); // NOI18N
        ButtonPadBG.add(BACKSPACE);

        keypadPanel.add(ButtonPadBG);
        ButtonPadBG.setBounds(0, 0, 650, 200);

        fullKeyBoard.add(keypadPanel, java.awt.BorderLayout.CENTER);

        numpadPanel.setOpaque(false);
        numpadPanel.setLayout(new javax.swing.OverlayLayout(numpadPanel));

        NumLabelPad.setFocusable(false);
        NumLabelPad.setOpaque(false);
        NumLabelPad.setLayout(new java.awt.GridLayout(4, 3));

        NumPadlbl7.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        NumPadlbl7.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        NumPadlbl7.setLabelFor(num7);
        NumPadlbl7.setText("7"); // NOI18N
        NumPadlbl7.setEnabled(false);
        NumPadlbl7.setFocusable(false);
        NumPadlbl7.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        NumPadlbl7.setRequestFocusEnabled(false);
        NumPadlbl7.setVerifyInputWhenFocusTarget(false);
        NumLabelPad.add(NumPadlbl7);

        NumPadlbl8.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        NumPadlbl8.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        NumPadlbl8.setLabelFor(num8);
        NumPadlbl8.setText("8"); // NOI18N
        NumPadlbl8.setEnabled(false);
        NumPadlbl8.setFocusable(false);
        NumPadlbl8.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        NumPadlbl8.setRequestFocusEnabled(false);
        NumPadlbl8.setVerifyInputWhenFocusTarget(false);
        NumLabelPad.add(NumPadlbl8);

        NumPadlbl9.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        NumPadlbl9.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        NumPadlbl9.setLabelFor(num9);
        NumPadlbl9.setText("9"); // NOI18N
        NumPadlbl9.setEnabled(false);
        NumPadlbl9.setFocusable(false);
        NumPadlbl9.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        NumPadlbl9.setRequestFocusEnabled(false);
        NumPadlbl9.setVerifyInputWhenFocusTarget(false);
        NumLabelPad.add(NumPadlbl9);

        NumPadlbl4.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        NumPadlbl4.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        NumPadlbl4.setLabelFor(num4);
        NumPadlbl4.setText("4"); // NOI18N
        NumPadlbl4.setEnabled(false);
        NumPadlbl4.setFocusable(false);
        NumPadlbl4.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        NumPadlbl4.setRequestFocusEnabled(false);
        NumPadlbl4.setVerifyInputWhenFocusTarget(false);
        NumLabelPad.add(NumPadlbl4);

        NumPadlbl5.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        NumPadlbl5.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        NumPadlbl5.setLabelFor(num5);
        NumPadlbl5.setText("5"); // NOI18N
        NumPadlbl5.setEnabled(false);
        NumPadlbl5.setFocusable(false);
        NumPadlbl5.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        NumPadlbl5.setRequestFocusEnabled(false);
        NumPadlbl5.setVerifyInputWhenFocusTarget(false);
        NumLabelPad.add(NumPadlbl5);

        NumPadlbl6.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        NumPadlbl6.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        NumPadlbl6.setLabelFor(num6);
        NumPadlbl6.setText("6"); // NOI18N
        NumPadlbl6.setEnabled(false);
        NumPadlbl6.setFocusable(false);
        NumPadlbl6.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        NumPadlbl6.setRequestFocusEnabled(false);
        NumPadlbl6.setVerifyInputWhenFocusTarget(false);
        NumLabelPad.add(NumPadlbl6);

        NumPadlbl1.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        NumPadlbl1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        NumPadlbl1.setLabelFor(num1);
        NumPadlbl1.setText("1"); // NOI18N
        NumPadlbl1.setEnabled(false);
        NumPadlbl1.setFocusable(false);
        NumPadlbl1.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        NumPadlbl1.setRequestFocusEnabled(false);
        NumPadlbl1.setVerifyInputWhenFocusTarget(false);
        NumLabelPad.add(NumPadlbl1);

        NumPadlbl2.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        NumPadlbl2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        NumPadlbl2.setLabelFor(num2);
        NumPadlbl2.setText("2"); // NOI18N
        NumPadlbl2.setEnabled(false);
        NumPadlbl2.setFocusable(false);
        NumPadlbl2.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        NumPadlbl2.setRequestFocusEnabled(false);
        NumPadlbl2.setVerifyInputWhenFocusTarget(false);
        NumLabelPad.add(NumPadlbl2);

        NumPadlbl3.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        NumPadlbl3.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        NumPadlbl3.setLabelFor(num3);
        NumPadlbl3.setText("3"); // NOI18N
        NumPadlbl3.setEnabled(false);
        NumPadlbl3.setFocusable(false);
        NumPadlbl3.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        NumPadlbl3.setRequestFocusEnabled(false);
        NumPadlbl3.setVerifyInputWhenFocusTarget(false);
        NumLabelPad.add(NumPadlbl3);

        NumPadlbl10.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        NumPadlbl10.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        NumPadlbl10.setLabelFor(num0);
        NumPadlbl10.setText("0"); // NOI18N
        NumPadlbl10.setEnabled(false);
        NumPadlbl10.setFocusable(false);
        NumPadlbl10.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        NumPadlbl10.setRequestFocusEnabled(false);
        NumPadlbl10.setVerifyInputWhenFocusTarget(false);
        NumLabelPad.add(NumPadlbl10);

        NumPadlbl11.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        NumPadlbl11.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        NumPadlbl11.setEnabled(false);
        NumPadlbl11.setFocusable(false);
        NumPadlbl11.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        NumPadlbl11.setRequestFocusEnabled(false);
        NumPadlbl11.setVerifyInputWhenFocusTarget(false);
        NumLabelPad.add(NumPadlbl11);

        NumPadlblAC.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        NumPadlblAC.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        NumPadlblAC.setLabelFor(AllClear);
        NumPadlblAC.setText("Clear"); // NOI18N
        NumPadlblAC.setEnabled(false);
        NumPadlblAC.setFocusable(false);
        NumPadlblAC.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        NumPadlblAC.setRequestFocusEnabled(false);
        NumPadlblAC.setVerifyInputWhenFocusTarget(false);
        NumLabelPad.add(NumPadlblAC);

        numpadPanel.add(NumLabelPad);

        NumButtonPad.setFocusable(false);
        NumButtonPad.setOpaque(false);
        NumButtonPad.setLayout(new java.awt.GridLayout(4, 3, 20, 10));

        NumButton7.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        NumButton7.setLabelFor(NumPadlbl7);
        NumButtonPad.add(NumButton7);
        NumButton7.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                Numkeymouseover(evt.getSource());
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                Numkeymouseoff(evt.getSource());
            }
        });

        NumButton8.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        NumButton8.setLabelFor(NumPadlbl8);
        NumButtonPad.add(NumButton8);
        NumButton8.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                Numkeymouseover(evt.getSource());
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                Numkeymouseoff(evt.getSource());
            }
        });

        NumButton9.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        NumButton9.setLabelFor(NumPadlbl9);
        NumButtonPad.add(NumButton9);
        NumButton9.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                Numkeymouseover(evt.getSource());
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                Numkeymouseoff(evt.getSource());
            }
        });

        NumButton4.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        NumButton4.setLabelFor(NumPadlbl4);
        NumButtonPad.add(NumButton4);
        NumButton4.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                Numkeymouseover(evt.getSource());
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                Numkeymouseoff(evt.getSource());
            }
        });

        NumButton5.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        NumButton5.setLabelFor(NumPadlbl5);
        NumButtonPad.add(NumButton5);
        NumButton5.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                Numkeymouseover(evt.getSource());
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                Numkeymouseoff(evt.getSource());
            }
        });

        NumButton6.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        NumButton6.setLabelFor(NumPadlbl6);
        NumButtonPad.add(NumButton6);
        NumButton6.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                Numkeymouseover(evt.getSource());
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                Numkeymouseoff(evt.getSource());
            }
        });

        NumButton1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        NumButton1.setLabelFor(NumPadlbl1);
        NumButtonPad.add(NumButton1);
        NumButton1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                Numkeymouseover(evt.getSource());
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                Numkeymouseoff(evt.getSource());
            }
        });

        NumButton2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        NumButton2.setLabelFor(NumPadlbl2);
        NumButtonPad.add(NumButton2);
        NumButton2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                Numkeymouseover(evt.getSource());
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                Numkeymouseoff(evt.getSource());
            }
        });

        NumButton3.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        NumButton3.setLabelFor(NumPadlbl3);
        NumButtonPad.add(NumButton3);
        NumButton3.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                Numkeymouseover(evt.getSource());
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                Numkeymouseoff(evt.getSource());
            }
        });

        NumButton10.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        NumButton10.setLabelFor(NumPadlbl10);
        NumButtonPad.add(NumButton10);
        NumButton10.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                Numkeymouseover(evt.getSource());
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                Numkeymouseoff(evt.getSource());
            }
        });

        NumButton11.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        NumButtonPad.add(NumButton11);

        NumButtonAC.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        NumButtonAC.setLabelFor(NumPadlblAC);
        NumButtonAC.setMaximumSize(new java.awt.Dimension(100, 38));
        NumButtonAC.setMinimumSize(new java.awt.Dimension(100, 38));
        NumButtonAC.setPreferredSize(new java.awt.Dimension(100, 38));
        NumButtonPad.add(NumButtonAC);
        NumButtonAC.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                trtype = "R";
                PlateInput2.setText("");
                Cardinput.delete(0, CardInput2.toString().length());
                CardInput2.setText("");
                clearRightPanel();
                clearLeftMIDMsgPanel();
                ResetCARDPLATE();
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                Numkeymouseoff(evt.getSource());
            }
        });

        numpadPanel.add(NumButtonPad);

        NumButtonBG.setFocusable(false);
        NumButtonBG.setOpaque(false);
        NumButtonBG.setLayout(new java.awt.GridLayout(4, 3));

        num7.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        num7.setIcon(new javax.swing.ImageIcon(getClass().getResource("/hybrid/resources/Numpad_midGreen.png"))); // NOI18N
        NumButtonBG.add(num7);

        num8.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        num8.setIcon(new javax.swing.ImageIcon(getClass().getResource("/hybrid/resources/Numpad_midGreen.png"))); // NOI18N
        NumButtonBG.add(num8);

        num9.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        num9.setIcon(new javax.swing.ImageIcon(getClass().getResource("/hybrid/resources/Numpad_midGreen.png"))); // NOI18N
        NumButtonBG.add(num9);

        num4.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        num4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/hybrid/resources/Numpad_midGreen.png"))); // NOI18N
        NumButtonBG.add(num4);

        num5.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        num5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/hybrid/resources/Numpad_midGreen.png"))); // NOI18N
        NumButtonBG.add(num5);

        num6.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        num6.setIcon(new javax.swing.ImageIcon(getClass().getResource("/hybrid/resources/Numpad_midGreen.png"))); // NOI18N
        NumButtonBG.add(num6);

        num1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        num1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/hybrid/resources/Numpad_midGreen.png"))); // NOI18N
        NumButtonBG.add(num1);

        num2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        num2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/hybrid/resources/Numpad_midGreen.png"))); // NOI18N
        NumButtonBG.add(num2);

        num3.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        num3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/hybrid/resources/Numpad_midGreen.png"))); // NOI18N
        NumButtonBG.add(num3);

        num0.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        num0.setIcon(new javax.swing.ImageIcon(getClass().getResource("/hybrid/resources/Numpad_midGreen.png"))); // NOI18N
        NumButtonBG.add(num0);

        numBlank.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        numBlank.setIcon(new javax.swing.ImageIcon(getClass().getResource("/hybrid/resources/Numpad_midGreen.png"))); // NOI18N
        NumButtonBG.add(numBlank);

        AllClear.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        AllClear.setIcon(new javax.swing.ImageIcon(getClass().getResource("/hybrid/resources/Numpad_midGreen.png"))); // NOI18N
        AllClear.setMaximumSize(new java.awt.Dimension(100, 38));
        AllClear.setMinimumSize(new java.awt.Dimension(100, 38));
        AllClear.setPreferredSize(new java.awt.Dimension(100, 38));
        NumButtonBG.add(AllClear);

        numpadPanel.add(NumButtonBG);

        fullKeyBoard.add(numpadPanel, java.awt.BorderLayout.EAST);

        CenterPanel.add(fullKeyBoard, java.awt.BorderLayout.PAGE_END);

        BGPanel.add(CenterPanel, java.awt.BorderLayout.CENTER);

        WestPanel.setPreferredSize(new java.awt.Dimension(350, 720));
        WestPanel.setOpaque(false);

        MainFuncPad.setFocusable(false);
        MainFuncPad.setPreferredSize(new java.awt.Dimension(370, 720));
        MainFuncPad.setRequestFocusEnabled(false);
        MainFuncPad.setOpaque(false);
        MainFuncPad.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 6, 6));

        Funcbutton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/hybrid/resources/cancel.png"))); // NOI18N
        Funcbutton1.setIconTextGap(2);
        Funcbutton1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                FB1MousePressed(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                FB1MouseReleased(evt);
            }
        });
        MainFuncPad.add(Funcbutton1);

        XFunc5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/hybrid/resources/login_logout.png"))); // NOI18N
        XFunc5.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                XFunc5MousePressed(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                XFunc5MouseReleased(evt);
            }
        });
        MainFuncPad.add(XFunc5);

        XFunc7.setIcon(new javax.swing.ImageIcon(getClass().getResource("/hybrid/resources/settlement.png"))); // NOI18N
        XFunc7.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                XFunc7MousePressed(evt);
            }
        });
        MainFuncPad.add(XFunc7);

        XFunc8.setIcon(new javax.swing.ImageIcon(getClass().getResource("/hybrid/resources/reprint.png"))); // NOI18N
        XFunc8.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                XFunc8MousePressed(evt);
            }
        });
        MainFuncPad.add(XFunc8);

        MasterFun.setIcon(new javax.swing.ImageIcon(getClass().getResource("/hybrid/resources/mastercard.png"))); // NOI18N
        MasterFun.setText("F10");
        MasterFun.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        MasterFun.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                MasterFunMouseClicked(evt);
            }
        });
        MainFuncPad.add(MasterFun);
        MainFuncPad.add(jSeparator1);

        XFunc11.setIcon(new javax.swing.ImageIcon(getClass().getResource("/hybrid/resources/plate_searchie.png"))); // NOI18N
        XFunc11.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                XFunc11MouseClicked(evt);
            }
            public void mousePressed(java.awt.event.MouseEvent evt) {
                XFunc11MousePressed(evt);
            }
        });
        MainFuncPad.add(XFunc11);

        XFunc12.setIcon(new javax.swing.ImageIcon(getClass().getResource("/hybrid/resources/manual-entry.png"))); // NOI18N
        XFunc12.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                XFunc12MouseClicked(evt);
            }
        });
        MainFuncPad.add(XFunc12);

        WestPanel.add(MainFuncPad);

        SecretFuncPad.setFocusable(false);
        SecretFuncPad.setPreferredSize(new java.awt.Dimension(370, 720));
        SecretFuncPad.setRequestFocusEnabled(false);
        SecretFuncPad.setOpaque(false);
        SecretFuncPad.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 6, 6));

        XFunc9.setIcon(new javax.swing.ImageIcon(getClass().getResource("/hybrid/resources/reprintX.png"))); // NOI18N
        XFunc9.setText("Reprint XRead");
        XFunc9.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                XFunc9MousePressed(evt);
            }
        });
        SecretFuncPad.add(XFunc9);

        XFunc10.setIcon(new javax.swing.ImageIcon(getClass().getResource("/hybrid/resources/reprintZ.png"))); // NOI18N
        XFunc10.setText("Reprint Z");
        XFunc10.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                XFunc10MousePressed(evt);
            }
        });
        SecretFuncPad.add(XFunc10);
        SecretFuncPad.add(jSeparator2);

        WestPanel.add(SecretFuncPad);

        BGPanel.add(WestPanel, java.awt.BorderLayout.WEST);

        EastPanel.setOpaque(false);
        EastPanel.setLayout(new java.awt.GridLayout(4, 0));

        adminPanel.setEnabled(false);
        adminPanel.setOpaque(false);
        adminPanel.setLayout(new java.awt.GridLayout(8, 1));

        admin1.setFont(new java.awt.Font("Luxi Sans", 0, 8)); // NOI18N
        admin1.setForeground(java.awt.Color.white);
        admin1.setPreferredSize(null);
        adminPanel.add(admin1);

        admin2.setFont(new java.awt.Font("Luxi Sans", 0, 8)); // NOI18N
        admin2.setForeground(java.awt.Color.lightGray);
        admin2.setPreferredSize(null);
        adminPanel.add(admin2);

        admin3.setFont(new java.awt.Font("Luxi Sans", 0, 8)); // NOI18N
        admin3.setForeground(java.awt.Color.white);
        admin3.setPreferredSize(null);
        adminPanel.add(admin3);

        admin4.setFont(new java.awt.Font("Luxi Sans", 0, 8)); // NOI18N
        admin4.setForeground(java.awt.Color.lightGray);
        admin4.setPreferredSize(null);
        adminPanel.add(admin4);

        admin5.setFont(new java.awt.Font("Luxi Sans", 0, 8)); // NOI18N
        admin5.setForeground(java.awt.Color.white);
        admin5.setPreferredSize(null);
        adminPanel.add(admin5);

        admin6.setFont(new java.awt.Font("Luxi Sans", 0, 8)); // NOI18N
        admin6.setForeground(java.awt.Color.lightGray);
        admin6.setPreferredSize(null);
        adminPanel.add(admin6);

        admin7.setFont(new java.awt.Font("Luxi Sans", 0, 8)); // NOI18N
        admin7.setForeground(java.awt.Color.white);
        admin7.setPreferredSize(null);
        adminPanel.add(admin7);

        admin8.setFont(new java.awt.Font("Luxi Sans", 0, 8)); // NOI18N
        admin8.setForeground(java.awt.Color.lightGray);
        admin8.setPreferredSize(null);
        adminPanel.add(admin8);

        EastPanel.add(adminPanel);

        errorPanels.setEnabled(false);
        errorPanels.setOpaque(false);
        errorPanels.setLayout(new java.awt.GridLayout(8, 1));

        error1.setPreferredSize(null);
        errorPanels.add(error1);

        error2.setPreferredSize(null);
        errorPanels.add(error2);

        error3.setPreferredSize(null);
        errorPanels.add(error3);

        error4.setPreferredSize(null);
        errorPanels.add(error4);

        errorMsg5.setPreferredSize(null);
        errorPanels.add(errorMsg5);

        errorMsg6.setPreferredSize(null);
        errorPanels.add(errorMsg6);

        errorMsg7.setPreferredSize(null);
        errorPanels.add(errorMsg7);

        errorMsg8.setPreferredSize(null);
        errorPanels.add(errorMsg8);

        EastPanel.add(errorPanels);

        ExitFuncPad.setOpaque(false);
        ExitFuncPad.setLayout(null);
        EastPanel.add(ExitFuncPad);

        BGPanel.add(EastPanel, java.awt.BorderLayout.EAST);

        getContentPane().add(BGPanel);
        BGPanel.setBounds(0, 0, 1430, 870);

        CamPanel.setFocusable(false);
        CamPanel.setRequestFocusEnabled(false);
        CamPanel.setEnabled(false);
        CamPanel.setOpaque(false);
        java.awt.FlowLayout flowLayout1 = new java.awt.FlowLayout(java.awt.FlowLayout.LEADING);
        flowLayout1.setAlignOnBaseline(true);
        CamPanel.setLayout(flowLayout1);

        entryCamera.setText("ENTRY");
        entryCamera.setVerticalAlignment(javax.swing.SwingConstants.BOTTOM);
        entryCamera.setFocusable(false);
        entryCamera.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        entryCamera.setForeground(new java.awt.Color(255, 255, 0));
        entryCamera.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        entryCamera.setRequestFocusEnabled(false);
        entryCamera.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        CamPanel.add(entryCamera);

        exitCamera.setText("EXIT");
        exitCamera.setVerticalAlignment(javax.swing.SwingConstants.BOTTOM);
        exitCamera.setFocusable(false);
        exitCamera.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        exitCamera.setForeground(new java.awt.Color(255, 255, 0));
        exitCamera.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        exitCamera.setRequestFocusEnabled(false);
        exitCamera.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        exitCamera.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                exitCameraMouseClicked(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                exitCameraMouseExited(evt);
            }
            public void mousePressed(java.awt.event.MouseEvent evt) {
                exitCameraMousePressed(evt);
            }
        });
        CamPanel.add(exitCamera);

        getContentPane().add(CamPanel);
        CamPanel.setBounds(0, 360, 970, 460);

        background.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        background.setAlignmentY(0.0F);
        background.setBackground(new java.awt.Color(0, 0, 0));
        background.setEnabled(false);
        background.setFocusable(false);
        background.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        getContentPane().add(background);
        background.setBounds(0, 0, 0, 768);

        BG.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        BG.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        getContentPane().add(BG);
        BG.setBounds(0, 0, 0, 0);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void goSpecial(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_goSpecial
        MainFuncPad.setVisible(false);
    }//GEN-LAST:event_goSpecial

    private void SwitchModes(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_SwitchModes

        if (switchmode.compareToIgnoreCase("entrance") == 0) {
            //background.setIcon(new javax.swing.ImageIcon(getClass().getResource("/hybrid/resources/light_contrast.jpg")));
            switchmode = "exit";
            PlateNumberlbl.setVisible(false);
            PlateInput2.setVisible(false);
            CardNumberlbl.setForeground(new java.awt.Color(0, 197, 60));
            CardInput2.setForeground(new java.awt.Color(0, 0, 60));
        } else if (switchmode.compareToIgnoreCase("exit") == 0) {
            //background.setIcon(new javax.swing.ImageIcon(getClass().getResource("/hybrid/resources/New_menu_blank.jpg")));
            switchmode = "entrance";
            PlateNumberlbl.setVisible(true);
            PlateInput2.setVisible(true);
            CardNumberlbl.setForeground(new java.awt.Color(255, 255, 100));
            CardInput2.setForeground(new java.awt.Color(210, 197, 60));
        }
}//GEN-LAST:event_SwitchModes

    private void Backspaced(java.awt.event.MouseEvent evt) {
        //PlateInput2.setText(PlateInput2.getText().substring(0, PlateInput2.getText().length() - 1));
        backspacing();
    }

    private void formFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_formFocusLost
        this.trapExit();
    }//GEN-LAST:event_formFocusLost

private void FB4MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_FB4MousePressed
    this.resetAllOverrides();
    QCSeniorFunction();
    ExitAPI ea = new ExitAPI(this);
    if (printer.compareToIgnoreCase("enabled") == 0) {
        PrinterEnabled = true;
    } else {
        PrinterEnabled = false;
    }
    if (ea.InitiateExit(new Date(), firstscan, currenttype, PrinterEnabled) == true) {
        firstscan = true;

    }
}//GEN-LAST:event_FB4MousePressed

private void FB4MouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_FB4MouseReleased
    Funcbutton4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/hybrid/resources/senior.png")));
    //SlotsInput.setVisible(true);
}//GEN-LAST:event_FB4MouseReleased

private void FB3MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_FB3MousePressed
    this.resetAllOverrides();
    PrivateCarsFunction();
    ExitAPI ea = new ExitAPI(this);
    if (printer.compareToIgnoreCase("enabled") == 0) {
        PrinterEnabled = true;
    } else {
        PrinterEnabled = false;
    }
    if (ea.InitiateExit(new Date(), firstscan, currenttype, PrinterEnabled) == true) {
        firstscan = true;

    }
}//GEN-LAST:event_FB3MousePressed

private void FB3MouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_FB3MouseReleased
    Funcbutton3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/hybrid/resources/retail.png")));
    /*
    if (slotsPanel.isVisible() == true) {
        slotsPanel.setVisible(false);
    } else {
        slotsPanel.setVisible(true);
    }
     */
}//GEN-LAST:event_FB3MouseReleased

private void FB1MouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_FB1MouseReleased


}//GEN-LAST:event_FB1MouseReleased

private void FB6MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_FB6MousePressed
    this.resetAllOverrides();
    MotorcycleFunction();
    ExitAPI ea = new ExitAPI(this);
    if (printer.compareToIgnoreCase("enabled") == 0) {
        PrinterEnabled = true;
    } else {
        PrinterEnabled = false;
    }
    if (ea.InitiateExit(new Date(), firstscan, currenttype, PrinterEnabled) == true) {
        firstscan = true;

    }
}//GEN-LAST:event_FB6MousePressed

private void FB6MouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_FB6MouseReleased
    Funcbutton6.setIcon(new javax.swing.ImageIcon(getClass().getResource("/hybrid/resources/motorcycle.png")));
}//GEN-LAST:event_FB6MouseReleased

private void FB7MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_FB7MousePressed
    Funcbutton7.setIcon(new javax.swing.ImageIcon(getClass().getResource("/hybrid/resources/keypad_press.png")));
}//GEN-LAST:event_FB7MousePressed

private void FB7MouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_FB7MouseReleased
    Funcbutton7.setIcon(new javax.swing.ImageIcon(getClass().getResource("/hybrid/resources/keypad.png")));
    if (fullKeyBoard.isVisible() == true) {
        fullKeyBoard.setVisible(false);
    } else {
        fullKeyBoard.setVisible(true);
    }
}//GEN-LAST:event_FB7MouseReleased

    private void VIPpress() {
        this.resetAllOverrides();
        VIPFunction();
        ExitAPI ea = new ExitAPI(this);
        if (printer.compareToIgnoreCase("enabled") == 0) {
            PrinterEnabled = true;
        } else {
            PrinterEnabled = false;
        }
        if (ea.InitiateExit(new Date(), firstscan, currenttype, PrinterEnabled) == true) {
            firstscan = true;

        }
    }

    private void GenericButtonPress(String trtype, String trname, String trdesc) {
        this.resetAllOverrides();
        currenttype = trtype;
        GenericCarsFunction(trtype, trname, trdesc);
        ExitAPI ea = new ExitAPI(this);
        if (printer.compareToIgnoreCase("enabled") == 0) {
            PrinterEnabled = true;
        } else {
            PrinterEnabled = false;
        }
        if (ea.InitiateExit(new Date(), firstscan, currenttype, PrinterEnabled) == true) {
            firstscan = true;

        }
    }

    private void settlementFormKeyPressed(java.awt.event.KeyEvent evt) {
        int code = evt.getKeyCode();
        switch (code) {
            case 27: //"Escape"
                PreviousCard = "";
                resetAllOverrides();
                if (currentmode.compareToIgnoreCase("logout") == 0) {
                    //resetLoginout();
                    currentmode = "";
                }
                if (Password == true) {
                    resetPassword();
                    Password = false;
                } else if (currentmode.compareToIgnoreCase("CheckMPP") == 0) {
                    currentmode = "";
                    resetMPPcheck();
                } else if (currentmode.compareToIgnoreCase("CheckLOST") == 0) {
                    currentmode = "";
                    //preCheckLCEP = false;
                    this.resetLostCard();
                } else if (currentmode.compareToIgnoreCase("CheckPREPAID") == 0) {
                    currentmode = "";
                    CouponPanel.setVisible(false);
                    Prepaidinput.delete(0, Prepaidinput.length() + 1);
                } else if (currentmode.compareToIgnoreCase("CheckVIP") == 0) {
                    currentmode = "";
                    resetVIP();
                } else if (currentmode.compareToIgnoreCase("CheckLCEP") == 0) {
                    currentmode = "";
                    //preCheckLCEP = false;
                } else if (currentmode.compareToIgnoreCase("invalidflatrate") == 0) {
                    CardNumberlbl.setText("INVALID FLATRATE - DONT PRESS ESC");

                }

                currentmode = "";
                Cardinput.delete(0, Cardinput.length() + 1);    //plus 1 because of the /n or Enter
                Loginput.delete(0, Loginput.length() + 1);
                CardInput2.setText("");
                this.clearLeftMIDMsgPanel();
                this.clearRightPanel();
                //--------entrance escape
                Cardinput.delete(0, Cardinput.length() + 1); //due to the unseen enter character [chr(13)] with every barcodecard scan
                CardInput2.setText("");
                currentmode = "backingout";
                resetBackout();
                this.clearLeftMIDMsgPanel();
                this.clearRightPanel();
                //ResetLogIN();
                lostEnabled = false;
                LostPanel.setVisible(lostEnabled);
                XFunc7.setIcon(new javax.swing.ImageIcon(getClass().getResource("/hybrid/resources/settlement.png")));
                settlementEnabled = false;
                //settToggle.setEnabled(settlementEnabled);
                //settName.setEditable(settlementEnabled);
                //settAddr.setEditable(settlementEnabled);
                PWDoscaID.setEditable(settlementEnabled);
                SettPanel.setVisible(settlementEnabled);
                settLbl3.setForeground(new Color(204, 204, 204));
                settLbl4.setForeground(new Color(204, 204, 204));
                this.repaint();
                this.requestFocus();
                this.validate();
                break;
            case 10: //ENTER button
                if (isEnterPressed == false || PrevPlate.compareToIgnoreCase(Plateinput.toString()) != 0 ) {
                    PrevPlate = Plateinput.toString();
                    goEnter();
                }
        }
    }

    /**
     * Used for ALT - HOTKEYS Shortcut keys
     *
     * @param evt
     */
    private void altKeyReleased(java.awt.event.KeyEvent evt) {
        int code = evt.getKeyCode();
        log.info("HOT code:" + code);
        switch (code) {
            case 65:    //a
                log.info("HOT Key:" + evt.getKeyChar());
                break;
            case 68:   //d
                log.info("Delivery HOT Key:" + evt.getKeyChar());
                DeliveryFunction();
                break;
            case 76:   //l
                log.info("Lost HOT Key:" + evt.getKeyChar());
                LostFunction();
                break;
            case 77:   //m
                log.info("Motorcycle HOT Key:" + evt.getKeyChar());
                MotorcycleFunction();
                break;
            case 80:   //p
                log.info("Private HOT Key:" + evt.getKeyChar());
                PrivateCarsFunction();
                break;
            case 81:   //q
                log.info("QC Senior HOT Key:" + evt.getKeyChar());
                QCSeniorFunction();
                break;
            case 112:   //F1
                log.info("Settlement HOT Key:" + evt.getKeyChar());
                SettlementFunction();
                break;
            case 48:   //0
                log.info("BPO Motor HOT Key:" + evt.getKeyChar());
                BPOMotorFunction();
                break;
            case 49:   //1
                log.info("BPO Car HOT Key:" + evt.getKeyChar());
                BPOCarFunction();
                break;
            case 86:   //v
                log.info("VIP HOT Key:" + evt.getKeyChar());
                VIPFunction();
                break;
            case 13:   //v
                log.info("ALT TAB Key:" + evt.getKeyChar());
                trapExit();
                break;
            default:
                //log.info("key Released:" + code);
                //SysMessage10.setText("Alt [" + evt.getKeyChar() + "]");
                break;
        }
        altIsPressed = false;
    }

private void formKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_formKeyPressed
    try {
        int code = evt.getKeyCode();
        //int code = evt.getKeyCode();
        //altIsPressed = false;
        //log.info("keypresseed:" + evt.getKeyChar());
        //log.info("keycode presseed:" + code);

        //log.info("altIsPressed:" + altIsPressed);
        switch (code) {
            case 18:
                //if (debugMode) {
                //log.info("Alt key pressed");
                altIsPressed = true;
                evt.consume();
                //    evt.setKeyCode(30);
                return;
            //} else {
            //    break;
            //}
            case 35:
                evt.setKeyCode(49);
                evt.setKeyChar('1');
                altIsPressed = false;
                break;
            case 225:
                evt.setKeyCode(50);
                evt.setKeyChar('2');
                altIsPressed = false;
                break;
            case 34:
                evt.setKeyCode(51);
                evt.setKeyChar('3');
                altIsPressed = false;
                break;
            case 226:
                evt.setKeyCode(52);
                evt.setKeyChar('4');
                altIsPressed = false;
                break;
            case 65368:
                evt.setKeyCode(53);
                evt.setKeyChar('5');
                altIsPressed = false;
                break;
            case 227:
                evt.setKeyCode(54);
                evt.setKeyChar('6');
                altIsPressed = false;
                break;
            case 36:
                evt.setKeyCode(55);
                evt.setKeyChar('7');
                altIsPressed = false;
                break;
            case 224:
                evt.setKeyCode(56);
                evt.setKeyChar('8');
                altIsPressed = false;
                break;
            case 33:
                evt.setKeyCode(57);
                evt.setKeyChar('9');
                altIsPressed = false;
                break;
            case 155:
                evt.setKeyCode(58);
                evt.setKeyChar('0');
                altIsPressed = false;
                break;
        }
        if (altIsPressed == false) {
            KeyAccepted = dumpInfo("Pressed", evt);
        }
        int i = 0;
        String s = "", asterisks = "";
        //promptINCPlate();
//        if (MasterIN == true) {
//            for (i = 0; i < MasterCardinput.length(); i++) {
//                asterisks = "*" + asterisks;
//            }
//            MasterCardInput2.setText(asterisks);
//            //MasterCardInput2.setText("********");  //(MasterCardinput.toString());
//        } else 
        if (Password == true) {
            for (i = 0; i < PWORDinput.length(); i++) {
                asterisks = "*" + asterisks;
            }
            PWORDInput2.setText(asterisks);
            //PWORDInput2.setText(PWORDinput.toString());
        } else if (currentmode.compareToIgnoreCase("CheckMPP") == 0) {
            if (KeyAccepted == true) {
                if (Plateinput.length() <= 6) {
                    PlateInput2.setText(Plateinput.toString().toUpperCase());
                }
            }
        } else if (currentmode.compareToIgnoreCase("CheckPREPAID") == 0) {
            if (KeyAccepted == true) {
                if (Prepaidinput.length() <= 6) {
                    PrepaidInput2.setText(Prepaidinput.toString().toUpperCase());
                }
            }
        } else if (searchparker == true) {
            if (KeyAccepted == true) {
                if (Plateinput.length() <= 6) {
                    PlateInput2.setText(Plateinput.toString().toUpperCase());
                }
            }
        } else if (currentmode.compareToIgnoreCase("CheckLOST") == 0) {
            if (KeyAccepted == true) {
                if (Plateinput.length() <= 6) {
                    PlateInput2.setText(Plateinput.toString().toUpperCase());
                }
            }
        } else if (InvalidFlatRate == true) {
            if (KeyAccepted == true) {
                if (Plateinput.length() <= 6) {
                    PlateInput2.setText(Plateinput.toString().toUpperCase());
                }
            }
        } else if (KeyAccepted == true) {
            //if (Cardinput.length() <= CardDigits) {
            //this.clearLeftMIDMsgPanel();
            //this.clearRightPanel();
            //CardInput2.setText(Cardinput.toStriCng().toUpperCase());
            PlateInput2.setText(Plateinput.toString().toUpperCase());
//                    } else if (Cardinput.length() == CardDigits) {
//                        try {
//                            this.InitiateExit();
//                        } catch (Exception ex) {
//                            log.error(ex.getMessage());
//                        }
            //}
        }
    } catch (Exception ex) {
        ex.printStackTrace();
        log.error(ex.getMessage());
    }

    }//GEN-LAST:event_formKeyPressed

private void ManualEnter(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_ManualEnter
    goEnter();
}//GEN-LAST:event_ManualEnter

private void exitTicketMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_exitTicketMousePressed
    ParkersAPI SP = new ParkersAPI();
    SP.UpdateCarSlots(EX_SentinelID, slotsmode);
    SP.UpdateExitTicketServed();
    busyStamp = new Date();
    this.SysMessage1.setText("Parking Slots added");
    this.SysMessage3.setText("due to Tickets");
//            String curr = SP.GetCarSlots(EX_SentinelID);
//            this.SysMessage5.setText("Exit Ticket Current Count: " + curr);
//            this.SysMessage7.setText("- Please wait -");
    ExitTickets.setText(SP.getExitTicketsServed());
}//GEN-LAST:event_exitTicketMousePressed

private void TicketPlusOneMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_TicketPlusOneMousePressed

    try {
        this.clearLeftMIDMsgPanel();
        NOSfiles nf = new NOSfiles();
        //nf.UpdateCarSlots(EN_SentinelID, slotsmode);
        nf.UpdateEntryTicketServed();
        busyStamp = new Date();
        this.SysMessage1.setText("Parking Slots subtracted");
        this.SysMessage3.setText("due to Tickets");
//            String curr = nf.GetCarSlots(EN_SentinelID);
//            this.SysMessage5.setText("Entry Tickets Current Count: " + curr);
//            this.SysMessage7.setText("- Please wait -");
        EntryTickets.setText(nf.getEntryTicketsServed());
    } catch (IOException ex) {
        log.error(ex.getMessage());
    }
}//GEN-LAST:event_TicketPlusOneMousePressed

private void Switch_EntryMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_Switch_EntryMousePressed
    OverrideSwitch_set2Exit(false);
}//GEN-LAST:event_Switch_EntryMousePressed

private void Switch_ExitMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_Switch_ExitMousePressed
    OverrideSwitch_set2Exit(true);
}//GEN-LAST:event_Switch_ExitMousePressed

private void ENTERManualEnter(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_ENTERManualEnter

}//GEN-LAST:event_ENTERManualEnter

    private void FB1MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_FB1MousePressed
        //OverrideSwitch_set2Exit(false);
        CamPanel.setVisible(true);
        WestPanel.setVisible(false);
        MainFuncPad.setVisible(false);
        SecretFuncPad.setVisible(false);
    }//GEN-LAST:event_FB1MousePressed

    private void startButtonPressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_startButtonPressed
        WestPanel.setVisible(true);
        CamPanel.setVisible(false);
        MainFuncPad.setVisible(true);
        SecretFuncPad.setVisible(false);
    }//GEN-LAST:event_startButtonPressed

    private void XFunc5MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_XFunc5MousePressed
        XFunc5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/hybrid/resources/login_logout_press.png")));
        currentmode = "logout";
        clearLeftMIDMsgPanel();
        clearRightPanel();
        ResetCARDPLATE();
        this.repaint();
        this.requestFocus();
        this.validate();
        LoginMOD lm = new LoginMOD();
        try {
            String cashier = lm.getCashierID();
            if (cashier.compareToIgnoreCase("") == 0) {
                LoginPanelX.setVisible(true);
                LoginPanelX.setVisible(false);
                LoginPanelX.setVisible(true);
                LoginPanelX.transferFocus();
                LoginPanelX.requestFocus();
                LogUsercode2.requestFocus();
                LogUsercode2.validate();
            } else {
                LoginPanelX.setVisible(false);
                LogoutPanelX.setVisible(true);
                LogoutPanelX.transferFocus();
                LogoutPanelX.requestFocus();
                LogUsercode1.requestFocus();
                LogUsercode1.validate();
            }
            //LogInput2.requestFocus();
            //StartLogOut();
        } catch (Exception ex) {
            log.error(ex.getMessage());
        }
    }//GEN-LAST:event_XFunc5MousePressed

    private void XFunc5MouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_XFunc5MouseReleased
        XFunc5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/hybrid/resources/login_logout.png")));
    }//GEN-LAST:event_XFunc5MouseReleased

    private void XFunc4MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_XFunc4MousePressed
        this.resetAllOverrides();
        BPOCarFunction();
        ExitAPI ea = new ExitAPI(this);
        if (printer.compareToIgnoreCase("enabled") == 0) {
            PrinterEnabled = true;
        } else {
            PrinterEnabled = false;
        }
        if (ea.InitiateExit(new Date(), firstscan, currenttype, PrinterEnabled) == true) {
            firstscan = true;

        }
    }//GEN-LAST:event_XFunc4MousePressed

    private void XFunc1Pressed() {
        this.resetAllOverrides();
        LostFunction();
        ExitAPI ea = new ExitAPI(this);
        if (printer.compareToIgnoreCase("enabled") == 0) {
            PrinterEnabled = true;
        } else {
            PrinterEnabled = false;
        }
        if (ea.InitiateExit(new Date(), firstscan, currenttype, PrinterEnabled) == true) {
            firstscan = true;

        }
    }

    private void XFunc2MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_XFunc2MousePressed
        this.resetAllOverrides();
        BPOMotorFunction();
        ExitAPI ea = new ExitAPI(this);
        if (printer.compareToIgnoreCase("enabled") == 0) {
            PrinterEnabled = true;
        } else {
            PrinterEnabled = false;
        }
        if (ea.InitiateExit(new Date(), firstscan, currenttype, PrinterEnabled) == true) {
            firstscan = true;

        }
    }//GEN-LAST:event_XFunc2MousePressed

    private void XFunc4MouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_XFunc4MouseReleased
        XFunc4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/hybrid/resources/bpo_car.png")));
    }//GEN-LAST:event_XFunc4MouseReleased

    private void XFunc2MouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_XFunc2MouseReleased
        XFunc2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/hybrid/resources/bpo_motor.png")));
    }//GEN-LAST:event_XFunc2MouseReleased

    private void XFunc6MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_XFunc6MousePressed
        this.resetAllOverrides();
        DeliveryFunction();
        ExitAPI ea = new ExitAPI(this);
        if (printer.compareToIgnoreCase("enabled") == 0) {
            PrinterEnabled = true;
        } else {
            PrinterEnabled = false;
        }
        if (ea.InitiateExit(new Date(), firstscan, "", PrinterEnabled) == true) {
            firstscan = true;

        }
    }//GEN-LAST:event_XFunc6MousePressed

    private void XFunc6MouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_XFunc6MouseReleased
        XFunc6.setIcon(new javax.swing.ImageIcon(getClass().getResource("/hybrid/resources/delivery.png")));
    }//GEN-LAST:event_XFunc6MouseReleased

    private void XFunc7MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_XFunc7MousePressed
        SettlementFunction();
    }//GEN-LAST:event_XFunc7MousePressed

    private void PWDoscaIDKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_PWDoscaIDKeyPressed
        settlementFormKeyPressed(evt);
    }//GEN-LAST:event_PWDoscaIDKeyPressed

    private void goLostBtnMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_goLostBtnMouseClicked
        DateConversionHandler dch = new DateConversionHandler();
        DataBaseHandler dbh = new DataBaseHandler();

        lostTimeIN.setFont(new java.awt.Font("Tahoma", 1, 22));
        String ampm = lostTimeIN.getText().toString().substring(lostTimeIN.getText().toString().length() - 2);

        Date d;
        if (ampm.compareToIgnoreCase("am") == 0) {
            d = dch.convertStr2Date(lostDateIN.getText().trim(), lostTimeIN.getText().trim());
        } else {
            int HourIN = lostTimeIN.getTime().getHour();
            int MinIN = lostTimeIN.getTime().getMinute();
            d = dch.convertStr2Date(lostDateIN.getText().trim(), HourIN + ":" + MinIN + "PM");
        }
        this.clearLeftMIDMsgPanel();

        log.info("LOST TIME IN:" + d.toString());
        Long ts = dch.convertJavaDate2UnixTimeDB(d);
        Long ts2 = dch.convertJavaDate2UnixTime4Card(d);
        //mifare.writeManualEntrance(ts.toString() + "");
        if (mifare == null || CardInput2.getText().toString() == "") {
            return;
        }
        if (mifare.readUID() != null || mifare.readUID() != "") {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-M-dd H:mm:ss.S");
            String d2 = sdf.format(d);
            long timeStampIN = dch.convertJavaDate2UnixTime(d);
            if (dbh.writeManualEntrance(CardInput2.getText().toString(), "R", d2, timeStampIN, true)) {
                SysMessage1.setText("Manual Card");
                SysMessage4.setText("Successfully Created");
                SysMessage6.setText(d2);
                dbh.saveLog("LC", CashierID, CardInput2.getText().toString());
            } else {
                SysMessage2.setText("Cannot Create Manual Card");
                SysMessage4.setText("Card is still in Use");
            }
        }
        lostEnabled = false;
        LostPanel.setVisible(lostEnabled);

        this.repaint();
        this.requestFocus();
        this.validate();
    }//GEN-LAST:event_goLostBtnMouseClicked

    private void formKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_formKeyReleased
        int code = evt.getKeyCode();
        switch (code) {
            case 18:
                //log.info("HOT Key:" + evt.getKeyChar());
                break;
            default:
                if (altIsPressed == true) {
                    altKeyReleased(evt);
                }
                break;
        }
    }//GEN-LAST:event_formKeyReleased

    private void printCollectionBtnMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_printCollectionBtnMouseClicked
        LoginMOD lm = new LoginMOD();
        try {
            DateConversionHandler dch = new DateConversionHandler();
            String dateColl = xreadList.getSelectedValue().trim();
//            dateColl = dch.convertDate2DBbase(dateColl);

            lm.printCollectReceiptFromDB(EX_SentinelID, loginID, dateColl, mcardOwner2);
        } catch (Exception ex) {
            log.error(ex.getMessage());
        }

        LowerLeftPanel.setVisible(false);

    }//GEN-LAST:event_printCollectionBtnMouseClicked

    private void LoginButtonMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_LoginButtonMouseClicked
        StartLogInX();
    }//GEN-LAST:event_LoginButtonMouseClicked

    private void XFunc8MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_XFunc8MousePressed
        if (reprintEnabled) {
            ReprintPanel.setVisible(!reprintEnabled);
            LostPanel.setVisible(false);
            SettPanel.setVisible(false);
            reprintEnabled = false;
            this.requestFocus();
        } else {
            ReprintPanel.setVisible(!reprintEnabled);
            LostPanel.setVisible(false);
            SettPanel.setVisible(false);
            reprintEnabled = true;
            reprintPlate.requestFocus();
        }


    }//GEN-LAST:event_XFunc8MousePressed

    private void reprintBtnMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_reprintBtnMousePressed
        ParkersAPI pa = new ParkersAPI();
//        pa.retrieveReceipt(reprintPlate.getText().toUpperCase());
        jList1.setModel(new javax.swing.AbstractListModel<String>() {
            String[] dates = pa.retrieveReceipt(reprintPlate.getText().toUpperCase());
            //String[] dates = pa.retrieveReceipt(reprintPlate.getText().toUpperCase());

            public int getSize() {
                return dates.length;
            }

            public String getElementAt(int i) {
                return dates[i];
            }
        });
    }//GEN-LAST:event_reprintBtnMousePressed

    private void reprintOutMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_reprintOutMousePressed
        ParkersAPI pa = new ParkersAPI();
        pa.reprintOut(firstRun, reprintPlate.getText().toUpperCase(), jList1.getSelectedValue());
    }//GEN-LAST:event_reprintOutMousePressed

    private void LoginButton1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_LoginButton1MouseClicked
        if (this.isLogoutValidfromDB() == true) {
            //currentmode = "logout";
        }
        //StartLogOut();
    }//GEN-LAST:event_LoginButton1MouseClicked

    private void LoginButton2MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_LoginButton2MouseClicked
        StartLogInX();
    }//GEN-LAST:event_LoginButton2MouseClicked

    private void carsMinusbtnMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_carsMinusbtnMouseClicked
        DataBaseHandler dbh = new DataBaseHandler();
        dbh.Slotsminus1("car");
        int car = dbh.getSlotAvail("car");
        carsNum.setText(String.valueOf(car));

    }//GEN-LAST:event_carsMinusbtnMouseClicked

    private void carsPlusbtnMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_carsPlusbtnMouseClicked
        DataBaseHandler dbh = new DataBaseHandler();
        dbh.Slotsplus1("car");
        int car = dbh.getSlotAvail("car");
        carsNum.setText(String.valueOf(car));
    }//GEN-LAST:event_carsPlusbtnMouseClicked

    private void motorPlusbtnMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_motorPlusbtnMouseClicked
        DataBaseHandler dbh = new DataBaseHandler();
        dbh.Slotsplus1("motor");
        int motor = dbh.getSlotAvail("motor");
        motorNum.setText(String.valueOf(motor));
    }//GEN-LAST:event_motorPlusbtnMouseClicked

    private void motorMinusbtnMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_motorMinusbtnMouseClicked
        DataBaseHandler dbh = new DataBaseHandler();
        dbh.Slotsminus1("motor");
        int motor = dbh.getSlotAvail("motor");
        motorNum.setText(String.valueOf(motor));
    }//GEN-LAST:event_motorMinusbtnMouseClicked

    private void LoginButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_LoginButton2ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_LoginButton2ActionPerformed

    private void printZReadingBtnMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_printZReadingBtnMouseClicked
        LoginMOD lm = new LoginMOD();
        try {
            DateConversionHandler dch = new DateConversionHandler();
            //lm.printCollectReceipt(stn.EX_SentinelID);
            String zdate = ZReadingDate.getText().trim();
            zdate = dch.convertDate2DBbase(zdate);

            lm.printZReadFromDB(EX_SentinelID, loginID, zdate, mcardOwner2);
        } catch (Exception ex) {
            log.error(ex.getMessage());
        }

        ZReadingPanel.setVisible(false);
    }//GEN-LAST:event_printZReadingBtnMouseClicked

    private void refundBtnMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_refundBtnMousePressed
        ParkersAPI pa = new ParkersAPI();
        String[] refundDates = pa.retrieveReceiptByCategory(voidTypeSelection.getSelectedIndex(), voidSelection.getText().toUpperCase());
        trans4Void.setModel(new javax.swing.AbstractListModel<String>() {
            public int getSize() {
                return refundDates.length;
            }

            public String getElementAt(int i) {
                return refundDates[i];
            }
        });
        voidSelection.setText(trans4Void.getModel().getElementAt(0));

    }//GEN-LAST:event_refundBtnMousePressed

    private void refundOutMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_refundOutMousePressed
        ParkersAPI pa = new ParkersAPI();

        switch (voidTypeSelection.getSelectedIndex()) {
            case 0:
                pa.reprintRefundByCard(firstRun, voidSelection.getText().toUpperCase(), trans4Void.getSelectedValue(), this.loginID);
                break;
            case 1:
                pa.reprintRefundByPlate(firstRun, voidSelection.getText().toUpperCase(), trans4Void.getSelectedValue(), this.loginID);
                break;
            case 2:
                pa.reprintRefundByReceipt(firstRun, voidSelection.getText().toUpperCase(), trans4Void.getSelectedValue(), this.loginID);
                break;
            default:
        }

        voidSelection.setText("");
        trans4Void.removeAll();
        String[] blank = {""};
        trans4Void.setModel(new javax.swing.AbstractListModel<String>() {
            public int getSize() {
                return blank.length;
            }

            public String getElementAt(int i) {
                return blank[i];
            }
        });
        SettPanel.setVisible(true);
        RefundPanel.setVisible(false);
        clearLeftMIDMsgPanel();
        clearRightPanel();
    }//GEN-LAST:event_refundOutMousePressed

    private void voidSelectionKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_voidSelectionKeyTyped
        // TODO add your handling code here:
    }//GEN-LAST:event_voidSelectionKeyTyped

    private void voidTypeSelectionItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_voidTypeSelectionItemStateChanged
        System.out.println(voidTypeSelection.getSelectedIndex());
        voidSelection.setText("");
        String[] blank = {""};
        trans4Void.setModel(new javax.swing.AbstractListModel<String>() {
            public int getSize() {
                return blank.length;
            }

            public String getElementAt(int i) {
                return blank[i];
            }
        });
    }//GEN-LAST:event_voidTypeSelectionItemStateChanged

    private void oscaNameKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_oscaNameKeyPressed
        settlementFormKeyPressed(evt);
    }//GEN-LAST:event_oscaNameKeyPressed

    private void oscaAddrKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_oscaAddrKeyPressed
        settlementFormKeyPressed(evt);
    }//GEN-LAST:event_oscaAddrKeyPressed

    private void oscaNameActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_oscaNameActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_oscaNameActionPerformed

    private void oscaTINKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_oscaTINKeyPressed
        settlementFormKeyPressed(evt);
    }//GEN-LAST:event_oscaTINKeyPressed

    private void oscaBusStyleActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_oscaBusStyleActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_oscaBusStyleActionPerformed

    private void oscaBusStyleKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_oscaBusStyleKeyPressed
        settlementFormKeyPressed(evt);
    }//GEN-LAST:event_oscaBusStyleKeyPressed

    private void XFunc9MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_XFunc9MousePressed
        resetPassword();
        clearLeftMIDMsgPanel();
        LowerLeftPanel.setVisible(true);
        Date printdate = new Date();
        logthis.setLog(EX_SentinelID, "Printing Summary Collection at " + printdate);

    }//GEN-LAST:event_XFunc9MousePressed

    private void XFunc10MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_XFunc10MousePressed
        resetPassword();
        clearLeftMIDMsgPanel();
        ZReadingPanel.setVisible(true);
        Date printdate = new Date();
        logthis.setLog(EX_SentinelID, "Printing ZReading at " + printdate);
    }//GEN-LAST:event_XFunc10MousePressed

    private void MasterFunMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_MasterFunMouseClicked
        startMasterCard();
    }//GEN-LAST:event_MasterFunMouseClicked

    private void searchCollectionBtnMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_searchCollectionBtnMouseClicked
        try {
            DateFormat formatter = new SimpleDateFormat("MM/dd/yy");
            Date date = (Date) formatter.parse(CollectionDate.getText());

            formatter = new SimpleDateFormat("yyyy-MM-dd");
            String dOut = formatter.format(date);

            ParkersAPI pa = new ParkersAPI();
            xreadList.setModel(new javax.swing.AbstractListModel<String>() {
                String[] dates = pa.retrieveXRead(dOut);

                public int getSize() {
                    return dates.length;
                }

                public String getElementAt(int i) {
                    return dates[i];
                }
            });
        } catch (ParseException ex) {
            java.util.logging.Logger.getLogger(HybridPanelUI.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_searchCollectionBtnMouseClicked

    private void refundOutActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_refundOutActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_refundOutActionPerformed

    private void AmtTenderedKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_AmtTenderedKeyPressed
        int code = evt.getKeyCode();
        switch (code) {
            case 10:
                if (isEnterPressed == false || PrevPlate.compareToIgnoreCase(Plateinput.toString()) != 0 ) {
                    PrevPlate = Plateinput.toString();
                    goEnter();
                }
                break;
            case 27:
                break;
            default:
        }
    }//GEN-LAST:event_AmtTenderedKeyPressed

    private void AmtTenderedKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_AmtTenderedKeyTyped

        char ch = evt.getKeyChar();
        if (!((ch == KeyEvent.VK_BACK_SPACE) || (ch == KeyEvent.VK_DELETE)
                || (ch == KeyEvent.VK_ENTER) || (ch == KeyEvent.VK_PERIOD) || (ch == KeyEvent.VK_TAB)
                || (Character.isDigit(ch)))) {
            evt.consume();
        }


    }//GEN-LAST:event_AmtTenderedKeyTyped

    private void AmtTenderedKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_AmtTenderedKeyReleased
        try {
            KeyAccepted = dumpInfo("Pressed", evt);
            if (KeyAccepted = false) {      //special keys
                //AmtTendered.requestFocus(false);
            }
            String amount = AMOUNTdisplay.getText();
            amount = amount.replace("P", "");
            if (amount.compareTo("") != 0) {
                float amt = Float.parseFloat(amount);
                String tend = AmtTendered.getText();
                if (null == tend || tend.compareToIgnoreCase("") == 0) {
                    tend = "0";
                }
                float tendered = Float.parseFloat(tend);
                float change = tendered - amt;
                DecimalFormat df2 = new DecimalFormat("#.00");
                
                if (change > 0) {
                    ChangeDisplay.setText("" + df2.format(change));
                } else {
                    ChangeDisplay.setText("0.00");
                }
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }//GEN-LAST:event_AmtTenderedKeyReleased

    private void XFunc11MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_XFunc11MousePressed
        // TODO add your handling code here:
    }//GEN-LAST:event_XFunc11MousePressed

    private void XFunc11MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_XFunc11MouseClicked
        SearchPanel.setVisible(true);
        SearchTimeTo.setTimeToNow();
    }//GEN-LAST:event_XFunc11MouseClicked

    private void closeButtonMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_closeButtonMouseClicked
        SearchPanel.setVisible(false);
    }//GEN-LAST:event_closeButtonMouseClicked

    private void jButton1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton1MouseClicked
        startLostCardSearch();
    }//GEN-LAST:event_jButton1MouseClicked

    private void exitCameraMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_exitCameraMouseClicked
        if (exitCamPressed) {
            exitCamPressed = false;
        } else {
            exitCamPressed = true;
        }
    }//GEN-LAST:event_exitCameraMouseClicked

    private void exitCameraMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_exitCameraMouseExited
        
    }//GEN-LAST:event_exitCameraMouseExited

    private void fullScreenCameraMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_fullScreenCameraMouseClicked
        if (exitCamPressed) {
            exitCamPressed = false;
        } else {
            exitCamPressed = true;
        }
    }//GEN-LAST:event_fullScreenCameraMouseClicked

    private void exitCameraMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_exitCameraMousePressed
        //exitCamPressed = true;
    }//GEN-LAST:event_exitCameraMousePressed

    private void closeButton1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_closeButton1MouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_closeButton1MouseClicked

    private void CreateMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_CreateMouseClicked
        EntranceAPI na = new EntranceAPI(this);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        manualEntryDate.setDateFormat(sdf);
        SimpleDateFormat sdtf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                
        String dateManuallyCreated = manualEntryDate.getText() + " " + manualEntryTime.getTime();
        
        na.createManualEntry(trtype, dateManuallyCreated, manualEntryPlate.getText());
        ManualEntryPanel.setVisible(false);
    }//GEN-LAST:event_CreateMouseClicked

    private void XFunc12MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_XFunc12MouseClicked
        ManualEntryPanel.setVisible(true);
        manualEntryPlate.requestFocus();
        manualEntryTime.setTimeToNow();
    }//GEN-LAST:event_XFunc12MouseClicked

    private void reprintButtonMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_reprintButtonMousePressed
        MasterCardPanel.setVisible(false);
        MainFuncPad.setVisible(false);
        SecretFuncPad.setVisible(true);
    }//GEN-LAST:event_reprintButtonMousePressed

    private void OverrideSwitch_set2Exit(boolean setExit) {
//        this.clearLeftMIDMsgPanel();
//        this.clearRightPanel(); 
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

        if (setExit == false) {
            //Switch_Entry.setIcon(new javax.swing.ImageIcon(getClass().getResource("/hybrid/resources/SwitchEntryA.png")));
            //Switch_Exit.setIcon(new javax.swing.ImageIcon(getClass().getResource("/hybrid/resources/SwitchExit.png")));
            //background.setIcon(new javax.swing.ImageIcon(getClass().getResource("/hybrid/resources/NewHybridDesign3a.jpg")));
            BG.setIcon(new ImageIcon(new ImageIcon(getClass()
                    .getResource("/hybrid/resources/parkingBG.jpg"))
                    .getImage()
                    .getScaledInstance(screenSize.width, screenSize.height, Image.SCALE_DEFAULT)));

            this.RightMsgPanellbl.setVisible(false);
            switchmode = "entrance";
            PlateNumberlbl.setVisible(true);
            PlateInput2.setVisible(true);
            CardNumberlbl.setForeground(new java.awt.Color(55, 255, 100));
            CardInput2.setForeground(new java.awt.Color(0, 67, 0));
            this.setEntryMode(!setExit);
            ExitSwitch = false;
        } else {
            //Switch_Entry.setIcon(new javax.swing.ImageIcon(getClass().getResource("/hybrid/resources/SwitchEntry.png")));
            //Switch_Exit.setIcon(new javax.swing.ImageIcon(getClass().getResource("/hybrid/resources/SwitchExitA.png")));
            //background.setIcon(new javax.swing.ImageIcon(getClass().getResource("/hybrid/resources/NewHybridDesign3b.jpg")));
            BG.setIcon(new ImageIcon(new ImageIcon(getClass()
                    .getResource("/hybrid/resources/parkingExit.jpg"))
                    .getImage()
                    .getScaledInstance(screenSize.width, screenSize.height, Image.SCALE_DEFAULT)));

            this.RightMsgPanellbl.setVisible(true);
            switchmode = "exit";
            PlateNumberlbl.setVisible(true);
            PlateInput2.setVisible(true);
            CardNumberlbl.setForeground(new java.awt.Color(0, 197, 60));
            CardInput2.setForeground(new java.awt.Color(34, 85, 255));
            this.setEntryMode(!setExit);
            ExitSwitch = true;
        }

    }

    private void setEntryMode(boolean mode) {
        /**
         * this.PlateNumberlbl.setVisible(mode);
         * this.PlateInput2.setVisible(mode); if
         * (switchmode.compareTo("entrance") == 0) {
         * this.CardNumberlbl.setLocation(10, 90);
         * this.CardInput2.setLocation(20, 105);
         * this.PlateNumberlbl.setLocation(10, 10);
         * this.PlateInput2.setLocation(20, 25); } else {
         * this.CardNumberlbl.setLocation(10, 50);
         * this.CardInput2.setLocation(20, 65); }
         *
         */
    }

    private void StartVIP() {
        VIPOverride = true;
        this.clearLeftMIDMsgPanel();
        this.clearRightPanel();
        SysMessage5.setText("  VIP Parker");
        SysMessage6.setText("< Scan  Card >");
        SysMessage9.setText("Press Esc to Cancel");
        this.PlateNumberlbl.setVisible(false);
    }

    private void StartClearCollect() {
        PasswordMOD pwdmod = new PasswordMOD();
        pwdmod.clearCollectReceipt(EX_SentinelID);
        this.clearLeftMIDMsgPanel();
        this.SysMessage5.setText("-Collection Report-");
        this.SysMessage6.setText("--Deleted--");
    }

    private void StartLostCard() {
        clearLeftMIDMsgPanel();
        clearRightPanel();
        Plateinput.delete(0, Plateinput.length());
        CardInput2.setVisible(false);
        PlateNumberlbl.setVisible(true);
    }

    private void StartPassword() {
        PasswordPanel.setVisible(true);
        Password = true;
    }

    public void StartComparingCard2DB() {
        DataBaseHandler dbh = new DataBaseHandler();
        DateConversionHandler dch = new DateConversionHandler();
        ParkersAPI SP = new ParkersAPI();
        Date crdEnDate = null;
        Date crdExDate = new Date();
        if (CardInput2.getText() != "") {
            try {
                String CardCheck = CardInput2.getText().substring(0, CardDigits);
//                SP.retrieveCRDPLTFromDB(CardCheck, null, true);
//                SP.startDataSplit();
//                String Entrypoint = SP.getSysID();
//                String Cardno = SP.getCardID();
//                String Plateno = SP.getPlateID();  //Always Blank
//                String ParkerType = SP.getTRID();
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-M-dd hh:mm:ss.S");
                String entry = dbh.getEntDatefromCard(CardCheck);
                if (entry.compareToIgnoreCase("") != 0) {
                    crdEnDate = sdf.parse(entry);
                    SysMessage2.setHorizontalAlignment(SwingConstants.LEFT);
                    SysMessage3.setHorizontalAlignment(SwingConstants.LEFT);
                    SysMessage4.setHorizontalAlignment(SwingConstants.LEFT);
                    SysMessage5.setHorizontalAlignment(SwingConstants.LEFT);
                    SysMessage1.setText("CARD CONTENTS");
                    SysMessage2.setText("Parking  ID: " + "P1");
                    SysMessage3.setText("Entrance ID: " + "EN01");
                    SysMessage4.setText("Date IN    : " + dch.convertInt2base(crdEnDate.getMonth() + 1) + "/" + dch.convertInt2base(crdEnDate.getDate()) + "/20" + (crdEnDate.getYear() - 100));
                    SysMessage5.setText("Time IN    : " + dch.convertInt2base(crdEnDate.getHours()) + ":" + dch.convertInt2base(crdEnDate.getMinutes()));
                    String exit = dbh.getExtDatefromCard(CardCheck);
                    if (exit.compareToIgnoreCase("") != 0) {
                        crdExDate = sdf.parse(exit);
                        SysMessage11.setText("Please PROCEED to Exit");
                        SysMessage12.setText("Parking  ID: " + "P1");
                        SysMessage13.setText("Exit  ID: " + "EN01");
                        //if (extdata.substring(9, 11).compareToIgnoreCase("--") != 0) {
                        SysMessage14.setText("Date OUT    : " + dch.convertInt2base(crdExDate.getMonth() + 1) + "/" + dch.convertInt2base(crdExDate.getDate()) + "/20" + (crdExDate.getYear() - 100));
                        //}
                        SysMessage15.setText("Time OUT    : " + dch.convertInt2base(crdExDate.getHours()) + ":" + dch.convertInt2base(crdExDate.getMinutes()));

                    } else {
                        //if (extdata.substring(4, 5).compareToIgnoreCase("P") == 0) {
                        SysMessage11.setText("CARD is Still ACTIVE");
                        SysMessage12.setText("Parking  ID: " + "--");
                        SysMessage13.setText("Exit  ID: " + "----");
                        //if (extdata.substring(9, 11).compareToIgnoreCase("--") != 0) {
                        SysMessage14.setText("Date OUT    : " + dch.convertInt2base(crdExDate.getMonth() + 1) + "/" + dch.convertInt2base(crdExDate.getDate()) + "/20" + (crdExDate.getYear() - 100));
                        //}
                        SysMessage15.setText("Time OUT    : " + dch.convertInt2base(crdExDate.getHours()) + ":" + dch.convertInt2base(crdExDate.getMinutes()));
                        //}
                    }
                    //Date elapsedDate = dch.getElapsedTimeFromUnixTime(entdata.substring(6, 16));
//                Date elapsedDate = dch.convertJavaUnixTime2Date(elapsed);
//                log.info(elapsed + " Elapsed DATA: " + elapsedDate.toString());
                    long timeStampIN;
                    //long nextDueTimeStamp;
                    timeStampIN = dch.convertJavaDate2UnixTime(crdEnDate);
                    long today = dch.convertJavaDate2UnixTime(crdExDate);
                    log.info("IN : " + timeStampIN);
                    log.info("TODAY : " + today);
                    Long elapsed = today - timeStampIN;
                    SysMessage17.setText(" Elapsed Hour: " + dch.getHoursfromseconds(elapsed));
                    SysMessage18.setText(" Elapsed Mins: " + dch.getRemainingMinutesfromseconds(elapsed));
                } else {    //Transacted Card has Exit DATA   --- They Need to Proceed to Exit
                    String entryFrExt = dbh.getEntDatefromExitCardDB(CardCheck);
                    if (entryFrExt.compareToIgnoreCase("") != 0) {
                        crdEnDate = sdf.parse(entryFrExt);
                        SysMessage1.setText("CARD CONTENTS");
                        SysMessage2.setText("Parking  ID: " + "P1");
                        SysMessage3.setText("Entrance ID: " + "EN01");
                        SysMessage4.setText("Date IN    : " + dch.convertInt2base(crdEnDate.getMonth() + 1) + "/" + dch.convertInt2base(crdEnDate.getDate()) + "/20" + (crdEnDate.getYear() - 100));
                        SysMessage5.setText("Time IN    : " + dch.convertInt2base(crdEnDate.getHours()) + ":" + dch.convertInt2base(crdEnDate.getMinutes()));
                    }
                    String exit = dbh.getExtDatefromCard(CardCheck);
                    if (exit.compareToIgnoreCase("") != 0) {
                        crdExDate = sdf.parse(exit);
                        SysMessage11.setText("Please PROCEED to Exit");
                        SysMessage12.setText("Parking  ID: " + "P1");
                        SysMessage13.setText("Exit  ID: " + "EN01");
                        //if (extdata.substring(9, 11).compareToIgnoreCase("--") != 0) {
                        SysMessage14.setText("Date OUT    : " + dch.convertInt2base(crdExDate.getMonth() + 1) + "/" + dch.convertInt2base(crdExDate.getDate()) + "/20" + (crdExDate.getYear() - 100));
                        //}
                        SysMessage15.setText("Time OUT    : " + dch.convertInt2base(crdExDate.getHours()) + ":" + dch.convertInt2base(crdExDate.getMinutes()));
                        check4Succeeding();
                    } else {
                        SysMessage1.setText("CARD CONTENTS");
                        SysMessage11.setText("::EMPTY::");

                        SysMessage3.setText("PARKER TYPE");
                        SysMessage13.setText(trtype.toUpperCase());

                    }

                }
            } catch (Exception ex) {
                log.error(ex.getMessage());
            }

        }

    }

    private void StartReadingMIFARECard() {
        try {
            ReadMIFARE mifare = new ReadMIFARE();
            DateConversionHandler dch = new DateConversionHandler();
            if (mifare.terminal.isCardPresent()) {
                mifare.startWaiting4CardPresent();
                mifare.authenticateBlock((byte) 0x04);
                mifare.authenticateBlock((byte) 0x05);
                String entdata = mifare.readDecoded16BytesbyBlockNum((byte) 0x04);
                log.info("Entrance DATA:" + entdata);
                String extdata = mifare.readDecoded16BytesbyBlockNum((byte) 0x05);
                log.info(extdata);
                Date crdEntDate = dch.convertJavaUnixTime2Date(entdata.substring(6, 16));
                Date crdExtDate = new Date();
                if (extdata.substring(0, 1).compareToIgnoreCase("-") != 0) {
                    crdExtDate = dch.convertJavaUnixTime2Date(extdata.substring(6, 16));
                }
                Long in = dch.convertJavaDate2UnixTime(crdEntDate);
                Long today = dch.convertJavaDate2UnixTime(crdExtDate);
                log.info("IN : " + in);
                log.info("TODAY : " + today);
                Long elapsed = today - in;
                log.info("elapsed : " + elapsed);
                Date elapsedD = new Date(crdExtDate.getTime() - crdEntDate.getTime());
                log.info("elapsedDate : " + elapsedD);

                SysMessage2.setHorizontalAlignment(SwingConstants.LEFT);
                SysMessage3.setHorizontalAlignment(SwingConstants.LEFT);
                SysMessage4.setHorizontalAlignment(SwingConstants.LEFT);
                SysMessage5.setHorizontalAlignment(SwingConstants.LEFT);
                SysMessage1.setText("CARD CONTENTS");
                SysMessage2.setText("Parking  ID: " + entdata.substring(0, 2));
                SysMessage3.setText("Entrance ID: " + entdata.substring(2, 4));
                SysMessage4.setText("Date IN    : " + dch.convertInt2base(crdEntDate.getMonth() + 1) + "/" + dch.convertInt2base(crdEntDate.getDate()) + "/20" + (crdEntDate.getYear() - 100));
                SysMessage5.setText("Time IN    : " + dch.convertInt2base(crdEntDate.getHours()) + ":" + dch.convertInt2base(crdEntDate.getMinutes()));

                if (extdata.substring(4, 5).compareToIgnoreCase("P") == 0) {
                    SysMessage11.setText("Please Proceed to Exit");
                } else {
                    SysMessage11.setText("Card is Still ACTIVE");
                }
                //if (extdata.substring(4, 5).compareToIgnoreCase("P") == 0) {
                SysMessage12.setText("Parking  ID: " + extdata.substring(0, 2));
                SysMessage13.setText("Exit ID    : " + extdata.substring(2, 4));
                //if (extdata.substring(9, 11).compareToIgnoreCase("--") != 0) {
                SysMessage14.setText("Date OUT    : " + dch.convertInt2base(crdExtDate.getMonth() + 1) + "/" + dch.convertInt2base(crdExtDate.getDate()) + "/20" + (crdExtDate.getYear() - 100));
                //}
                SysMessage15.setText("Time OUT    : " + dch.convertInt2base(crdExtDate.getHours()) + ":" + dch.convertInt2base(crdExtDate.getMinutes()));
                //}
                //Date elapsedDate = dch.getElapsedTimeFromUnixTime(entdata.substring(6, 16));
//                Date elapsedDate = dch.convertJavaUnixTime2Date(elapsed);
//                log.info(elapsed + " Elapsed DATA: " + elapsedDate.toString());
                SysMessage17.setText(" Elapsed Hour: " + dch.getHoursfromseconds(elapsed));
                SysMessage18.setText(" Elapsed Mins: " + dch.getRemainingMinutesfromseconds(elapsed));

                check4Succeeding();
            }
        } catch (CardException ex) {
            log.error(ex.getMessage());
        }
    }

    private void StartLogInX() {
        //RightMsgPanel.setVisible(false);
        DataBaseHandler dbh = new DataBaseHandler();
        SaveCollData scd = new SaveCollData();
        RightMsgPanellbl.setVisible(false);
        newMidPanel.setVisible(false);
        LoginPanelX.setVisible(true);
        String logname = "";
        LoginMOD lm = new LoginMOD();
        Date logStamp = new Date();

        try {
            String tempID = lm.getCashierID();
            if (tempID.compareToIgnoreCase("") == 0) {
                //LoginLbl.setText("LOGIN");
                //LoginLbl.setForeground(new java.awt.Color(122, 181, 251));
            } else {
                LoginLbl.setText("LOGOUT");
                LoginLbl.setForeground(new java.awt.Color(255, 255, 0));
            }
            if (LogUsercode2.getText().compareToIgnoreCase("") != 0) {
                logname = lm.getLOGINDATcashiernameFromDB(LogUsercode2.getText().toString(), LogPassword2.getText().toString());
                if (logname.compareToIgnoreCase("") == 0) {
                    SysMessage1.setText("LOGCODE INVALID");
                    SysMessage3.setText(LogUsercode.getText());
                } else {
                    clearLeftMIDMsgPanel();
                    CashierID = LogUsercode2.getText().toString();
                    String logID = logStamp.getYear() + logStamp.getMonth() + logStamp.getDate() + LogUsercode2.getText() + logStamp.getHours() + logStamp.getMinutes() + logStamp.getSeconds();
                    //lm.printLoginSTUB(logname, LogUsercode2.getText(), EX_SentinelID);
                    //lm.saveLogintoFile(logname, LogUsercode2.getText());
                    lm.printLoginUSBSTUB(logStamp, LogUsercode2.getText(), logname, EX_SentinelID);
                    dbh.saveLog("L1", LogUsercode2.getText());
                    lm.saveLogintoFile(logStamp, logID, LogUsercode2.getText(), logname);
                    currentmode = "";
                    //REFRESH
                    this.loginID = logID;
                    this.startEntranceTransacting();
                    String lastTransaction = dbh.getLastTransaction(EX_SentinelID);
                    if (null == lastTransaction) {
                        lastTransaction = "0000000000000001";
                    }
                    dbh.saveLogin(logID, CashierID, logname, EX_SentinelID);
                    scd.saveZRead(logID, EX_SentinelID, lastTransaction, LogUsercode2.getText());
                }

                CashierName = lm.getCashierName();
                TellerName.setText(CashierName);
                LogUsercode.setText("");
                LoginLbl.setText("");
            }
        } catch (Exception ex) {
            log.error(ex.getMessage());
        }
    }

    private void StartLogInOld() {
        //RightMsgPanel.setVisible(false);
        RightMsgPanellbl.setVisible(false);
        newMidPanel.setVisible(false);
        LoginPanelX.setVisible(true);
        String logname = "";
        LoginMOD lm = new LoginMOD();
        try {
            String tempID = lm.getCashierID();
            if (tempID.compareToIgnoreCase("") == 0) {
                LoginLbl.setText("LOGIN");
                LoginLbl.setForeground(new java.awt.Color(122, 181, 251));
            } else {
                LoginLbl.setText("LOGOUT");
                LoginLbl.setForeground(new java.awt.Color(255, 255, 0));
            }
            if (Loginput.toString().compareToIgnoreCase("") != 0) {
                logname = lm.getLOGINDATcashiername(Loginput.toString());
                if (logname.compareToIgnoreCase("") == 0) {
                    SysMessage1.setText("LOGCODE INVALID");
                    SysMessage3.setText(Loginput.toString());
                } else {
                    clearLeftMIDMsgPanel();
                    //lm.printLoginSTUB(Loginput.toString(), logname, EX_SentinelID);
                    //lm.saveLogintoFile(Loginput.toString(), logname);
                    currentmode = "";
                    CashierID = Loginput.toString();
                    this.startEntranceTransacting();
                    DataBaseHandler dbh = new DataBaseHandler();
                    //dbh.saveLogin(CashierID, logname, EX_SentinelID);
                }
                TellerName.setText(lm.getCashierName());
                Loginput.delete(0, Loginput.length());
                LoginLbl.setText("");
            }
        } catch (Exception ex) {
            log.error(ex.getMessage());
        }
    }

    private void StartLogOut() {
        //RightMsgPanel.setVisible(false);
        RightMsgPanellbl.setVisible(false);
        newMidPanel.setVisible(false);
        LoginPanelX.setVisible(true);
        //Loginout_lbl.setVisible(true);
        //Password_lbl.setVisible(true);
        LoginMOD lm = new LoginMOD();
        try {
            String tempID = lm.getCashierID();
            if (tempID.compareToIgnoreCase("") == 0) {
                LoginLbl.setText("LOGIN");
                LoginLbl.setForeground(new java.awt.Color(24, 91, 0));
            } else {
                LoginLbl.setText("LOGOUT");
                LoginLbl.setForeground(new java.awt.Color(204, 255, 0));
            }
        } catch (Exception ex) {
            log.error(ex.getMessage());
        }
    }

    private void StartPrepaid() {
        this.clearLeftMIDMsgPanel();
        this.clearRightPanel();
        Prepaidinput.delete(0, Prepaidinput.length() + 1);
        CouponPanel.setVisible(true);
        SysMessage1.setText(".:: Prepaid Coupon ::.");
        SysMessage5.setText("input coupon no. first");
        SysMessage6.setText("then scan card");
    }

    private void StartPrepaidCheck() {
        this.clearLeftMIDMsgPanel();

        //if found, Override Prepaid Type here
        ParkersAPI cp = new ParkersAPI();

        if (cp.checkPC(Prepaidinput.toString()) == 2) {
            SysMessage1.setText(".::Coupon Parker::.");
            SysMessage3.setText("coupon# " + Prepaidinput.toString());
            SysMessage4.setText("is VALID");
            SysMessage6.setText("Scan Card");
            PrepaidOverride = true;
            Prepaid2Save = Prepaidinput.toString();
        } else if (cp.checkPC(Prepaidinput.toString()) == 1) {
            SysMessage2.setText("DUPLICATE COUPON");
            SysMessage4.setText("coupon# " + Prepaidinput.toString());
            SysMessage6.setText("Scan Card");
            PrepaidOverride = true;
            Prepaid2Save = Prepaidinput.toString();
        } else {
            SysMessage5.setText(".::Coupon Parker::.");
            SysMessage6.setText("INVALID");
        }
    }

    private void StartLCEP() {
        this.clearLeftMIDMsgPanel();
        this.clearRightPanel();
        ParkerInfo8.setText("Plateinput" + LCEPtemp);
        LCEPOverride = true;
        CardInput2.setText("000000");
        //Cardinput.append("000000");
        //this.preCheckLCEP = false;
        currentmode = "";
    }

    public void StartInvalidFlatRate() {
        clearLeftMIDMsgPanel();
        clearRightPanel();
        Plateinput.delete(0, Plateinput.length());
        CardNumberlbl.setText("INVALID FLATRATE");
        InvalidFlatRate = true;
        CardInput2.setVisible(false);
//        CardInput2.setText("");
        currentmode = "invalidflatrate";
        PlateNumberlbl.setVisible(true);
        PlateInput2.setVisible(true);
        this.CardNumberlbl.setLocation(10, 90);
        this.CardInput2.setLocation(20, 105);         //Bottom Bar
        this.PlateNumberlbl.setLocation(10, 10);
        this.PlateInput2.setLocation(20, 25);        //Top Bar
    }

    private void startMasterCard() {
        this.clearLeftMIDMsgPanel();
        MasterCardPanel.setVisible(true);
        PasswordPanel.setVisible(false);
        LoginPanelX.setVisible(false);
        MasterIN = true;
        this.toFront();
        this.requestFocus();
        this.validate();
    }

    private void StartPrintDUPReceipt() {
        DupReceiptAPI dr = new DupReceiptAPI();
        dr.printDUPreceipt();
    }

    private void SpclParkerModeSet(boolean on) {
        this.PlateInput2.setVisible(on);
//        this.LettersPad.setVisible(on);
//        this.ButtonPad.setVisible(on);
        this.setEntryMode(on);
    }

    private boolean PreCheckExit() {
        SystemStatus ss = new SystemStatus();
        if (checkPING(serverIP) == true) {
            if (ss.checkOnline() == true) {
                return true;
            }
        }
        this.SysMessage3.setText("Server OFFLINE");
        this.SysMessage5.setText("Server OFFLINE");
        return false;
    }

    public void startEntranceTransacting() {
        //XFunc1.setVisible(true);
        XFunc2.setVisible(true);
        //RightMsgPanel.setVisible(true);
//        LettersPad.setVisible(true);
        MainFuncPad.setVisible(true);
        //ExitFuncPad_lbls.setVisible(true);
        LoginPanelX.setVisible(false);
        newMidPanel.setVisible(true);
        //Loglbl.setText("LOGOUT");
    }

    public void stopEntranceTransacting()//mustLogin()
    {
        //ExitFuncPad_lbls.setVisible(false);
        //XFunc1.setVisible(false);
        XFunc2.setVisible(false);
        //RightMsgPanel.setVisible(false);
        MainFuncPad.setVisible(false);
//        LettersPad.setVisible(false);
        LoginPanelX.setVisible(true);
        LoginPanelX.transferFocus();
        LoginPanelX.requestFocus();
        LogUsercode2.requestFocus();
        LogUsercode2.validate();
        newMidPanel.setVisible(false);
        //Loglbl.setText("LOGIN");
        //Loglbl.setForeground(new java.awt.Color(205, 205, 215));
        currentmode = "logout";
    }

    private void promptINCPlate() {
        if (PlateInput2.getText().length() < PlateDigits && PlateInput2.getText().length() >= 1) {
            //SysMessage7.setText("Plate Number");
            //SysMessage8.setText("[" + PlateInput2.getText().length() + "]");
            //SysMessage10.setText("Incomplete");
        } else {
            //this.clearLeftMIDMsgPanel();
        }
    }
//    private void setCardIn_Lbls_Loc(boolean entrymode)
//    {
//        if (entrymode == true)
//        {
//            CardNumberlbl.setLocation(10, 90);
//            this.CardInput2.setLocation(20,105);
//        }
//        else
//        {
//            this.CardNumberlbl.setLocation(10, 50);
//            this.CardInput2.setLocation(20,65);
//        }
//    }

    private void showHelp() {
        clearLeftMIDMsgPanel();
        SysMessage1.setText("F1 : Show HELP");
        SysMessage2.setText("");
        SysMessage3.setText("F3 : Entrance Mode");
        SysMessage4.setText("");
        //SysMessage5.setText("F5 : Login");
        //SysMessage6.setText("F6 : Logout");
        SysMessage7.setText("");
        SysMessage8.setText("F8 : Exit Mode");
        SysMessage9.setText("");
        SysMessage10.setText("F10 : MasterCard");
    }

    private void CheckMPPButton() {
        try {
            currentmode = "CheckMPP";        //F7
            clearLeftMIDMsgPanel();
            clearRightPanel();
            PlateNumberlbl.setVisible(true);
        } catch (Exception ex) {
            log.error(ex.getMessage());
        }
    }

    private boolean isLogoutValidfromDB() {
        try {
            LoginMOD lm = new LoginMOD();
            String compcode = "";
            compcode = lm.getCashierID();
            Date logStamp = new Date();
            if (compcode.compareToIgnoreCase(LogUsercode1.getText().toString()) == 0 && lm.getCashierPassword(LogUsercode1.getText().toString(), LogPassword1.getText().toString())) {//Startlog out if code is valid
                SaveCollData scd = new SaveCollData();
                DataBaseHandler dbh = new DataBaseHandler();
                dbh.saveLog("L0", LogUsercode1.getText().toString());
                //print out logout form first
                //lm.printLogoutReceiptFromDB(EX_SentinelID, false);
                lm.printHEADER(EX_SentinelID);
                lm.epsonPrintLogoutReceiptFromDB(EX_SentinelID, false);
                //---------------------------
                lm.saveLogintoFile(logStamp, "", "", "");
                SysMessage1.setText("LOGOUT Successful");
                SysMessage3.setText("LOGCODE Found");
                SysMessage4.setText("-Please Login again-");
                Loginput.delete(0, Loginput.length());

                LogUsercode1.setText("");
                LogPassword1.setText("");
                LogUsercode2.setText("");
                LogPassword2.setText("");
                LogoutPanelX.setVisible(false);
                LoginPanelX.setVisible(true);

                CashierID = "";
                CashierName = "";
                //String logID, String Exitpoint, String lastTransaction, String logcode                
                String lastTransaction = dbh.getLastTransaction(EX_SentinelID);
                Float totalCollected = dbh.getImptAmount("totalAmount", loginID);
                Float discountCollected = dbh.getImptAmount("discountAmount", loginID);
                Float vatsaleAmount = dbh.getImptAmount("vatsaleAmount", loginID);
                Float vat12Amount = dbh.getImptAmount("vat12Amount", loginID);
                Float vatExemptAmount = dbh.getImptAmount("vatExemptAmount", loginID);                
                
                //Double Sale12Vat = (double) totalCollected * 0.12;
                //Double Sale12Vat = (double) (totalCollected / 1.12) * 0.12f;
                //Double vatSale = totalCollected - Sale12Vat;
                scd.updateZRead(loginID, EX_SentinelID, lastTransaction, compcode, String.valueOf(totalCollected), String.valueOf(vatsaleAmount), String.valueOf(vat12Amount), String.valueOf(vatExemptAmount), String.valueOf(discountCollected));
                scd.ResetCarServed();
                scd.ResetExitCarServed();
                scd.ResetEntryTicketsServed();
                scd.ResetExitTicketsServed();
                ServedNo.setText(scd.getCarServed());
                ServedExit.setText(scd.getExitCarServed());
                EntryTickets.setText(scd.getEntryTicketsServed());
                ExitTickets.setText(scd.getExitTicketsServed());
                LoginMOD la = new LoginMOD();
                la.CheckValidCashierStamp(this);
                this.StartLogInX();
                return true;
            } else {//reset codeinputbox
                LogUsercode1.setText("");
                LogPassword1.setText("");
                SysMessage1.setText("LOGCODE INVALID");
                //SysMessage3.setText(Loginput.toString());
                SysMessage5.setText("Cannot Logout");
                SysMessage6.setText("Input correct Logcode");
                Loginput.delete(0, Loginput.length());
                //LogInput2.setText("");
                LogoutPanelX.setVisible(false);

            }
            return false;
        } catch (Exception ex) {
            log.error(ex.getMessage());
        }
        return false;
    }

    //----display methods
    public void clearRightPanel() {
        ParkerInfo1.setText("");
        ParkerInfo2.setText("");
        ParkerInfo3.setText("");
        ParkerInfo4.setText("");
        ParkerInfo5.setText("");
        ParkerInfo6.setText("");
        ParkerInfo7.setText("");
        ParkerInfo8.setText("");
        ParkerInfo9.setText("");
        ParkerInfo10.setText("");
        ParkerInfo11.setText("");
        ParkerInfo12.setText("");
        ParkerInfo13.setText("");
        ParkerInfo14.setText("");
        ParkerInfo15.setText("");
        ParkerInfo16.setText("");
        ParkerInfo17.setText("");
        ParkerInfo18.setText("");
        ParkerInfo19.setText("");
        ParkerInfo20.setText("");
        AMOUNTdisplay.setText("");
    }

    public void clearLeftMIDMsgPanel() {
        SysMessage1.setText("");
        SysMessage2.setText("");
        SysMessage3.setText("");
        SysMessage4.setText("");
        SysMessage5.setText("");
        SysMessage6.setText("");
        SysMessage7.setText("");
        SysMessage8.setText("");
        SysMessage9.setText("");
        SysMessage10.setText("");
        SysMessage11.setText("");
        SysMessage12.setText("");
        SysMessage13.setText("");
        SysMessage14.setText("");
        SysMessage15.setText("");
        SysMessage16.setText("");
        SysMessage17.setText("");
        SysMessage18.setText("");
        SysMessage19.setText("");
        SysMessage20.setText("");
        //AMOUNTdisplay.setText("");
    }

    public void processLeftPanelMsgs(String[] MSG2process) {
        LeftMIDMsgPanel.setVisible(true);
        SysMessage1.setForeground(new java.awt.Color(255, 255, 0));
        SysMessage1.setText(MSG2process[0]);
        SysMessage2.setForeground(new java.awt.Color(255, 255, 0));
        SysMessage2.setText(MSG2process[1]);
        SysMessage3.setForeground(new java.awt.Color(255, 255, 0));
        SysMessage3.setText(MSG2process[2]);
        SysMessage4.setText(MSG2process[3]);
        SysMessage5.setText(MSG2process[4]);
        SysMessage6.setText(MSG2process[5]);
        SysMessage7.setText(MSG2process[6]);
        SysMessage8.setText(MSG2process[7]);
        SysMessage9.setText(MSG2process[8]);
        SysMessage10.setText(MSG2process[9]);
        SysMessage11.setText(MSG2process[10]);
        SysMessage12.setText(MSG2process[11]);
        SysMessage13.setText(MSG2process[12]);
        SysMessage14.setText(MSG2process[13]);
        SysMessage15.setText(MSG2process[14]);
        SysMessage16.setText(MSG2process[15]);
        SysMessage17.setText(MSG2process[16]);
        SysMessage18.setText(MSG2process[17]);
        SysMessage19.setText(MSG2process[18]);
        SysMessage20.setText(MSG2process[19]);
    }

    public void processRightPanelMsgs(String[] MSG2process) {
        RightMsgPanellbl.setVisible(true);
        ParkerInfo1.setText(MSG2process[0]);
        ParkerInfo2.setText(MSG2process[1]);
        ParkerInfo3.setText(MSG2process[2]);
        ParkerInfo4.setText(MSG2process[3]);
        ParkerInfo5.setText(MSG2process[4]);
        ParkerInfo6.setText(MSG2process[5]);
        ParkerInfo7.setText(MSG2process[6]);
        ParkerInfo8.setText(MSG2process[7]);
        ParkerInfo9.setText(MSG2process[8]);
        ParkerInfo10.setText(MSG2process[10]);
        ParkerInfo11.setText(MSG2process[11]);
        ParkerInfo12.setText(MSG2process[12]);
        ParkerInfo13.setText(MSG2process[13]);
        ParkerInfo14.setText(MSG2process[14]);
        ParkerInfo15.setText(MSG2process[15]);
        ParkerInfo16.setText(MSG2process[16]);
        ParkerInfo17.setText(MSG2process[17]);
        ParkerInfo18.setText(MSG2process[18]);
        ParkerInfo19.setText(MSG2process[19]);
        ParkerInfo20.setText(MSG2process[20]);

    }

    public void ResetFuncButtons() {
        //SclParkerPad.setVisible(true);
        //FuncPadButton.setVisible(true);
        LoginPanelX.setVisible(false);
    }

    public void ResetCARDPLATE() {
        Cardinput.delete(0, Cardinput.length() + 1);
        CardInput2.setText("");
        Plateinput.delete(0, Plateinput.length());
        PlateInput2.setText("");
    }

    private void resetBackout() {
        PlateNumberlbl.setForeground(new java.awt.Color(255, 255, 100));
        PlateNumberlbl.setText("PLATE NUMBER:");
        PlateInput2.setVisible(true);
        PlateInput2.setText("");
        Cardinput.delete(0, Cardinput.length() + 1);
        CardInput2.setText("");
    }

    public void resetPassword() {
        PasswordPanel.setVisible(false);
        PWORDinput.delete(0, PWORDinput.length());
        PWORDInput2.setText("");
        MasterCard1 = false;
        MasterCard2 = false;
    }

    public void resetMasterCard() {
        MasterCardPanel.setVisible(false);
        MasterCardinput.delete(0, MasterCardinput.length());
        MasterCardInput2.setText("");
    }

    private void resetSearchCard() {
        Plateinput.delete(0, Cardinput.length() + 1);
        CardInput2.setText("");
        currentmode = "";
        //clearLeftMIDMsgPanel();
        CardNumberlbl.setText("CARD NUMBER:");
        CardInput2.setVisible(true);
        Plateinput.delete(0, Plateinput.length());
        PlateInput2.setText("");
        PlateNumberlbl.setVisible(false);
        searchparker = false;
    }

    private void resetMPPcheck() {
        PlateInput2.setText("");
        if (switchmode.compareTo("entrance") == 0) {
            PlateNumberlbl.setVisible(true);
            CardNumberlbl.setVisible(true);
            CardInput2.setVisible(true);
        } else {
            PlateNumberlbl.setVisible(false);
            CardNumberlbl.setVisible(true);
            CardInput2.setVisible(true);
        }
        //this.clearLeftMIDMsgPanel();
        Plateinput.delete(0, Plateinput.length());
        XFunc2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/hybrid/resources/backout.png")));

    }

    private void resetMPPbutton() {
        XFunc2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/hybrid/resources/backout.png")));
        this.PlateInput2.setText("");
        this.Plateinput.delete(0, Plateinput.length());
        this.CardInput2.setText("");
        this.Cardinput.delete(0, Plateinput.length());
        if (switchmode.compareTo("exit") == 0) {
            //this.setCardIn_Lbls_Loc(false);
            this.setEntryMode(false);
            this.PlateNumberlbl.setVisible(false);
            this.PlateInput2.setVisible(false);
        }
        this.CardNumberlbl.setVisible(true);
        this.CardInput2.setVisible(true);
        PlateInput2.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        PlateInput2.setOpaque(false);
    }

    private void resetLostCard() {
        Plateinput.delete(0, Cardinput.length() + 1);
        CardInput2.setText("");
        currentmode = "";
        //clearLeftMIDMsgPanel();
        CardNumberlbl.setText("CARD NUMBER:");
        CardInput2.setVisible(true);
        Plateinput.delete(0, Plateinput.length());
        PlateInput2.setText("");
        PlateNumberlbl.setVisible(false);
        //LostCard_lbl.setText("Lost Card");
        this.CardInput2.setText("");
        this.Cardinput.delete(0, Plateinput.length());
        //LostCard_lbl.setForeground(new java.awt.Color(255, 255, 204));

        //XFunc1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/hybrid/resources/backout.png")));
    }

    private void resetLCEP() {
        Plateinput.delete(0, Plateinput.length());
        PlateInput2.setText("");
        Cardinput.delete(0, Cardinput.length());
        CardInput2.setText("");
        //this.LostCard_lbl.setText("Lost Card");
    }

    private void resetPrepaid() {
        Prepaidinput.delete(0, Prepaidinput.length() + 1);
        this.PrepaidInput2.setText("");
        CouponPanel.setVisible(false);
    }

    private void resetVIP() {
        //XFunc5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/hybrid/resources/backout.png")));
        clearLeftMIDMsgPanel();
        currentmode = "";
        VIPOverride = false;
        VIPinput.delete(0, VIPinput.length() + 1);
    }

    private void resetAdmin() {
        admin1.setText("");
        admin2.setText("");
        admin3.setText("");
        admin4.setText("");
        admin5.setText("");
        admin6.setText("");
        admin7.setText("");
        admin8.setText("");
    }

    private void ResetTicket() {
        this.CardNumberlbl.setText("CARD NUMBER:");
        TicketInput = false;
        //Ticketlbl.setForeground(new java.awt.Color(255, 255, 255));
    }

    private void ResetManualCard() {
        this.CardNumberlbl.setText("CARD NUMBER:");
        ManualCard = false;
        //ManualCardlbl.setForeground(new java.awt.Color(255, 255, 255));
    }

    public void resetInvalidFlatRates() {
        Plateinput.delete(0, Plateinput.length());
        CardInput2.setText("");
        CardNumberlbl.setText("CARD NUMBER:");
        CardInput2.setVisible(true);
        Cardinput.delete(0, Cardinput.length());
        this.setEntryMode(ExitSwitch);
        PlateInput2.setText("");
        PlateNumberlbl.setVisible(false);
        trtype = "R";
    }

    public void resetAllOverrides() {
        LostOverride = false;
        PrepaidOverride = false;
        LCEPOverride = false;
        VIPOverride = false;
        QCSeniorOverride = false;
        DeliveryOverride = false;
        MotorOverride = false;
        BPOCarOverride = false;
        BPOMotorOverride = false;
        //settName.setText("");
        //settAddr.setText("");
        //settTIN.setText("");
        PWDoscaID.setText("");
        oscaName.setText("");
        oscaAddr.setText("");
        oscaTIN.setText("");
        oscaBusStyle.setText("");

        //settToggle.setEnabled(true);
        //settName.setEditable(false);
        //settAddr.setEditable(false);
        //settTIN.setEditable(false);
        PWDoscaID.setEditable(false);
        //trtype = "R";
        currenttype = "";
        resetVIP();
    }

    private void backspacing() {
        try {
            if (MasterIN == true) {
                MasterCardinput.deleteCharAt(MasterCardinput.length() - 1);
                String asterisks = "";
                for (int i = 0; i < MasterCardinput.length(); i++) {
                    asterisks = "*" + asterisks;
                }
                MasterCardInput2.setText(asterisks);
                //MasterCardInput2.setText(MasterCardinput.toString());
            } else if (Password == true) {
                PWORDinput.deleteCharAt(PWORDinput.length() - 1);
                String asterisks = "";
                for (int i = 0; i < PWORDinput.length(); i++) {
                    asterisks = "*" + asterisks;
                }
                PWORDInput2.setText(asterisks);
                //PWORDInput2.setText(PWORDinput.toString());
            } else if (CashierID.compareToIgnoreCase("") == 0) {
                Loginput.deleteCharAt(Loginput.length() - 1);
                LoginLbl.setText(Loginput.toString());
            } else if (currentmode.compareToIgnoreCase("logout") == 0) {
                Loginput.deleteCharAt(Loginput.length() - 1);
                LoginLbl.setText(Loginput.toString());
            } else if (currentmode.compareToIgnoreCase("CheckMPP") == 0) {
                Plateinput.deleteCharAt(Plateinput.length() - 1);
                PlateInput2.setText(Plateinput.toString());
            } else if (currentmode.compareToIgnoreCase("CheckLOST") == 0) {
                Plateinput.deleteCharAt(Plateinput.length() - 1);
                PlateInput2.setText(Plateinput.toString());
            } else if (currentmode.compareToIgnoreCase("CheckLCEP") == 0) {
                Plateinput.deleteCharAt(Plateinput.length() - 1);
                PlateInput2.setText(Plateinput.toString());
            } else if (currentmode.compareToIgnoreCase("CheckVIP") == 0) {
                Cardinput.deleteCharAt(Cardinput.length() - 1);
                CardInput2.setText(Cardinput.toString());
            } else if (currentmode.compareToIgnoreCase("CheckPREPAID") == 0) {
                Prepaidinput.deleteCharAt(Prepaidinput.length() - 1);
                PrepaidInput2.setText(Prepaidinput.toString());
            } //          else if (searchparker == true){
            //              Plateinput.deleteCharAt(Plateinput.length()-1);
            //              PlateInput2.setText(Plateinput.toString());
            //          }
            else if (InvalidFlatRate == true) {
                Plateinput.deleteCharAt(Plateinput.length() - 1);
                PlateInput2.setText(Plateinput.toString());
            } else if (ExitSwitch == true) {
                Cardinput.deleteCharAt(Cardinput.length() - 1);
                CardInput2.setText(Cardinput.toString());
            } else {
                Plateinput.deleteCharAt(Plateinput.length() - 1);
                PlateInput2.setText(Plateinput.toString());
            }
        } catch (StringIndexOutOfBoundsException err) {
            //err.printStackTrace();
        }
    }

    private void startMIFAREReader() {
        mifare = new ReadMIFARE();
        try {
            if (null != mifare.terminal) {
                mifare.terminal.waitForCardPresent(0);
            }
        } catch (Exception ex) {
            log.error(ex.getMessage());
        }
    }

    private void GenericCarsFunction(String tr, String trname, String trdesc) {
        clearLeftMIDMsgPanel();
        resetAllOverrides();
        trtype = tr;
        currenttype = tr;
        MainFuncPad.setVisible(true);
        clearRightPanel();
        SysMessage1.setText(trname);
        SysMessage2.setText(trdesc);
    }

    private void PrivateCarsFunction() {
        clearLeftMIDMsgPanel();
        Funcbutton3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/hybrid/resources/retail_press.png")));
        trtype = "R";
        resetAllOverrides();
        MainFuncPad.setVisible(true);
        clearRightPanel();
        SysMessage1.setText("Private Parkers");
        SysMessage2.setText("Flat Rate P50");
    }

    private void MotorcycleFunction() {
        clearLeftMIDMsgPanel();
        Funcbutton6.setIcon(new javax.swing.ImageIcon(getClass().getResource("/hybrid/resources/motorcycle_press.png")));
        trtype = "M";
        MotorOverride = true;
        MainFuncPad.setVisible(true);
        clearRightPanel();
        SysMessage1.setText("MOTORCYCLE");
        SysMessage2.setText("Flat Rate P35");
        //ParkerInfo3.setText("Schedule [M-T-W-TH-F]");
    }

    private void QCSeniorFunction() {
        clearLeftMIDMsgPanel();
        Funcbutton4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/hybrid/resources/senior_press.png")));
        trtype = "Q";
        QCSeniorOverride = true;
        MainFuncPad.setVisible(true);
        clearRightPanel();
        SysMessage1.setText("QC Senior Citizen");
        SysMessage2.setText("Flat Rate P0.00");
    }

    private void BPOCarFunction() {
        clearLeftMIDMsgPanel();
        XFunc4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/hybrid/resources/bpo_car_press.png")));
        trtype = "BC";
        BPOCarOverride = true;
        MainFuncPad.setVisible(true);
        clearRightPanel();
        SysMessage1.setText("BPO Car");
        SysMessage2.setText("Flat Rate P50");
        SysMessage3.setText("1st Extended and");
        SysMessage4.setText("Overnight waived");
    }

    private void GenericButtonFunction() {
        clearLeftMIDMsgPanel();
        XFunc2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/hybrid/resources/generic_press.png")));
        trtype = "";
        BPOMotorOverride = true;
        MainFuncPad.setVisible(true);
        clearRightPanel();
        SysMessage1.setText("Description");
        SysMessage2.setText("Here");
    }

    private void BPOMotorFunction() {
        clearLeftMIDMsgPanel();
        XFunc2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/hybrid/resources/bpo_motor_press.png")));
        trtype = "BM";
        BPOMotorOverride = true;
        MainFuncPad.setVisible(true);
        clearRightPanel();
        SysMessage1.setText("BPO Motor");
        SysMessage2.setText("Flat Rate P35");
        SysMessage3.setText("1st Extended and");
        SysMessage4.setText("Overnight waived");
    }

    private void LostFunction() {
        if (lostEnabled == true) {
            //XFunc1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/hybrid/resources/lostcard.png")));
            lostEnabled = false;
            settLbl3.setForeground(new Color(204, 204, 204));
            settLbl4.setForeground(new Color(204, 204, 204));
            //settToggle.setEnabled(settlementEnabled);
            //settName.setEditable(settlementEnabled);
            //settAddr.setEditable(settlementEnabled);
            PWDoscaID.setEditable(false);
            //settRef.setBackground(new Color(255, 255, 255));
            //settRef.setForeground(new Color(255, 255, 255));
            //settRef.setEditable(lostEnabled);
            //SettPanel.setVisible(settlementEnabled);
            SettPanel.setVisible(true);
            LostPanel.setVisible(false);
            this.repaint();
            this.requestFocus();
            this.validate();
        } else {
            //XFunc1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/hybrid/resources/lostcard_press.png")));
            lostTimeIN.setText("");
            //clearLeftMIDMsgPanel();
            ///clearRightPanel();
            lostEnabled = true;
            settLbl3.setForeground(new Color(255, 255, 204));
            settLbl4.setForeground(new Color(255, 255, 204));
            SettPanel.setVisible(false);
            LostPanel.setVisible(true);
            lostTimeIN.requestFocus();
            //settRef.setOpaque(lostEnabled);
            //settRef.setBackground(new Color(255, 255, 204));
            //settRef.setForeground(new Color(155, 155, 155));
            //SettPanel.setVisible(settlementEnabled);
            //settToggle.setEnabled(settlementEnabled);
            //settAddr.setEditable(settlementEnabled);
            //settTIN.setEditable(settlementEnabled);
            //settRef.requestFocus();
            PWDoscaID.setEditable(false);
        }

    }

    private void DeliveryFunction() {
        clearLeftMIDMsgPanel();
        XFunc6.setIcon(new javax.swing.ImageIcon(getClass().getResource("/hybrid/resources/delivery_press.png")));
        trtype = "D";
        DeliveryOverride = true;
        MainFuncPad.setVisible(true);
        clearRightPanel();
        SysMessage1.setText("Delivery Parker");
        SysMessage2.setText("Free 1st Hour");
        SysMessage3.setText("P100 Succeeding Hour");
        SysMessage4.setText("Lost Card = P200");
    }

    private void SettlementFunction() {
        if (settlementEnabled == true) {
            XFunc7.setIcon(new javax.swing.ImageIcon(getClass().getResource("/hybrid/resources/settlement.png")));
            settlementEnabled = false;
            settLbl3.setForeground(new Color(204, 204, 204));
            settLbl4.setForeground(new Color(204, 204, 204));
            //settToggle.setEnabled(settlementEnabled);
            //settName.setEditable(settlementEnabled);
            //settAddr.setEditable(settlementEnabled);
            //SettPanel.setVisible(false);
            //LostPanel.setVisible(true);
            PWDoscaID.setOpaque(settlementEnabled);
            PWDoscaID.setBackground(new Color(255, 255, 255));
            PWDoscaID.setForeground(new Color(255, 255, 255));
            PWDoscaID.setEditable(settlementEnabled);
            SettPanel.setVisible(settlementEnabled);
            this.repaint();
            this.requestFocus();
            this.validate();
        } else {
            XFunc7.setIcon(new javax.swing.ImageIcon(getClass().getResource("/hybrid/resources/settlement_press.png")));
            settlementEnabled = true;
            settLbl3.setForeground(new Color(255, 255, 204));
            settLbl4.setForeground(new Color(255, 255, 204));
            PWDoscaID.setOpaque(settlementEnabled);
            PWDoscaID.setBackground(new Color(255, 255, 204));
            PWDoscaID.setForeground(new Color(155, 155, 155));
            //SettPanel.setVisible(settlementEnabled);
            //settToggle.setEnabled(settlementEnabled);
            SettPanel.setVisible(true);
            LostPanel.setVisible(false);
            PWDoscaID.setEditable(settlementEnabled);
            //settAddr.setEditable(settlementEnabled);
            //settTIN.setEditable(settlementEnabled);
            PWDoscaID.requestFocus();
        }
    }

    private void VIPFunction() {
        clearLeftMIDMsgPanel();
        //Funcbutton5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/hybrid/resources/vip_exit_press.png")));
        trtype = "V";
        VIPOverride = true;
        MainFuncPad.setVisible(true);
        clearRightPanel();
        SysMessage1.setText("VIP");
        SysMessage2.setText("Free Parking");
    }

    private void initLoadParkerTypes() {
        DataBaseHandler dbh = new DataBaseHandler();
        dbh.manualOpen();
        ResultSet rs = dbh.getAllActivePtypes();
        String[] strings = new String[0];
        try {
            while (rs.next()) {
                String parkertype = rs.getString("parkertype");
                String ptypename = rs.getString("ptypename");
                String ptypedesc = rs.getString("ptypedesc");
                String btnimg = rs.getString("btnimg");

                //log.info("ptypeID:" + parkertype + " ptypename:" + ptypename);
                /**
                 * ********************************
                 *
                 */
                if (parkertype.compareToIgnoreCase("G") != 0 && parkertype.compareToIgnoreCase("L") != 0 && parkertype.compareToIgnoreCase("V") != 0) {
                    JLabel Funcbuttonx = new JLabel();
                    //Funcbuttonx.setIcon(new javax.swing.ImageIcon(getClass().getResource("/hybrid/resources/delivery.png"))); // NOI18N
                    Funcbuttonx.setIcon(new javax.swing.JLabel() {
                        public javax.swing.Icon getIcon() {
                            try {
                                return new javax.swing.ImageIcon(
                                        new java.net.URL(btnimg)
                                );
                            } catch (java.net.MalformedURLException e) {
                            }
                            return null;
                        }
                    }.getIcon());
                    Funcbuttonx.addMouseListener(new java.awt.event.MouseAdapter() {
                        public void mousePressed(java.awt.event.MouseEvent evt) {
                            GenericButtonPress(parkertype, ptypename, ptypedesc);
                        }

                        public void mouseReleased(java.awt.event.MouseEvent evt) {
                            //GenericButtonPress('');
                        }
                    });
                    MainFuncPad.add(Funcbuttonx);
                    Funcbuttonx = null;
                } else if (parkertype.compareToIgnoreCase("V") == 0) {
                    // Add VIP
                    javax.swing.JLabel Funcbutton5 = new javax.swing.JLabel();
                    Funcbutton5.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
                    Funcbutton5.setVerticalAlignment(javax.swing.SwingConstants.TOP);
                    Funcbutton5.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);
//                    Funcbutton5.setMaximumSize(new java.awt.Dimension(100, 100));
//                    Funcbutton5.setMinimumSize(new java.awt.Dimension(100, 100));
//                    Funcbutton5.setPreferredSize(new java.awt.Dimension(100, 100));
                    Funcbutton5.setVerticalTextPosition(javax.swing.SwingConstants.TOP);
                    Funcbutton5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/hybrid/resources/vip_exit.png"))); // NOI18N
                    Funcbutton5.addMouseListener(new java.awt.event.MouseAdapter() {
                        public void mousePressed(java.awt.event.MouseEvent evt) {
                            VIPpress();
                        }
                    });
                    MainFuncPad.add(Funcbutton5);
                } else if (parkertype.compareToIgnoreCase("L") == 0) {
                    // Add Lost
                    javax.swing.JLabel XFunc1 = new javax.swing.JLabel();
                    XFunc1.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
                    XFunc1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/hybrid/resources/lostcard.png"))); // NOI18N
                    XFunc1.setVerticalAlignment(javax.swing.SwingConstants.TOP);
                    XFunc1.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);
//                    XFunc1.setMaximumSize(new java.awt.Dimension(100, 100));
//                    XFunc1.setMinimumSize(new java.awt.Dimension(100, 100));
//                    XFunc1.setPreferredSize(new java.awt.Dimension(100, 100));
                    XFunc1.setVerticalTextPosition(javax.swing.SwingConstants.TOP);
                    XFunc1.addMouseListener(new java.awt.event.MouseAdapter() {
                        public void mousePressed(java.awt.event.MouseEvent evt) {
                            XFunc1Pressed();
                        }
                    });
                    MainFuncPad.add(XFunc1);
                }
                /**
                 * ********
                 *
                 */
            }
        } catch (SQLException ex) {
            log.error(ex.getMessage());
        }
        dbh.manualClose();

    }

    private void initLoadSlots() {
        DataBaseHandler dbh = new DataBaseHandler();
        int motor = dbh.getSlotAvail("motor");
        int car = dbh.getSlotAvail("car");
        carsNum.setText(String.valueOf(car));
        motorNum.setText(String.valueOf(motor));
    }

    private void check4Succeeding() {
        ExitAPI ea = new ExitAPI(this);
        if (printer.compareToIgnoreCase("enabled") == 0) {
            PrinterEnabled = true;
        } else {
            PrinterEnabled = false;
        }
        if (ea.InitiateExit(new Date(), false, "", PrinterEnabled) == true) {
            firstscan = true;
        }
    }

    private void testShowCameraOnScreen() {
        dbh.insertImageFromURLToDB(entryIPCamera, "admin", "admin888888");
        BufferedImage buf = dbh.GetImageFromDB("CA034E2D");
        entryCamera.setIcon(new ImageIcon(buf));
    }

    private void startLostCardSearch() {        
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        SearchDateFrom.setDateFormat(sdf);
        SearchDateTo.setDateFormat(sdf);
        String DateFrom = SearchDateFrom.getText();
        String DateTo = SearchDateTo.getText();
        
        int x = dbh.GetImageCountFromDB_byDate(DateFrom + " " + SearchTimeFrom, DateTo + " " + SearchTimeTo);
        BufferedImage[] buf = dbh.GetImageFromDB_byDate(DateFrom + " " + SearchTimeFrom, DateTo + " " + SearchTimeTo);
        for (int i = 0; i < x; i++) {
            javax.swing.JLabel Picture = new javax.swing.JLabel();
            Picture.setIcon(new ImageIcon(buf[i]));
            SearchDisplayPanel.add(Picture);
        }        
    }

    //-----------------Threads------------------------
    class ShowExitCamera implements Runnable {

        @Override
        public void run() {
            while (true) {
                //System.out.println(new Date());
                try {

                    //dbh.insertImageFromURLToDB();
                    BufferedImage buf = dbh.getImageFromCamera(exitIPCamera, "admin", "user1234");
                    if (null != buf) {
                        //entryCamera.setIcon(new ImageIcon(buf));       
                        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
                        
                        if (exitCamPressed) {
                            Image img = getScaledImage(buf, screenSize.width, screenSize.height);
                            fullScreenCamera.setIcon(new ImageIcon(new ImageIcon(buf)
                            .getImage()
                            .getScaledInstance(screenSize.width, screenSize.height, Image.SCALE_DEFAULT)));
                            fullScreenCamera.setBounds(0, 0, screenSize.width, screenSize.height);                            
                        } else {
                            Image img = getScaledImage(buf, screenSize.width / 4 + 100, screenSize.height / 3);
                            fullScreenCamera.setIcon(null);
                            fullScreenCamera.setText("");
                            fullScreenCamera.setBounds(0, 0, 0, 0);      
                            exitCamera.setIcon(new ImageIcon(img));
                            exitCamera.setText("EXIT");
                        }
                    }
                    waitMultiplier = 1;
                } catch (Exception ex) {
                    waitMultiplier++;
                    exitCamera.setText("");
                    //log.error(ex.getMessage());
                    try {
                        Thread.sleep(5000 * waitMultiplier);
                    } catch (InterruptedException ex1) {
                        //log.error(ex1.getMessage());
                    }
                }
            }
        }

    }
    
    private Image getScaledImage(Image srcImg, int w, int h) {
        BufferedImage resizedImg = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = resizedImg.createGraphics();

        g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g2.drawImage(srcImg, 0, 0, w, h, null);
        g2.dispose();

        return resizedImg;
    }
    class DigitalClock implements Runnable {

        @Override
        public void run() {

            try {
                while (true) {
                    Date theTime = new Date();
                    ///if (theTime.getTime() >= busyStamp.getTime() + 8000 && theTime.getTime() < busyStamp.getTime() + 9500) {
                    //    ParkerInfo10.setText(AMOUNTdisplay.getText());
                    //    clearLeftMIDMsgPanel();
                    //}
                    //DateFormat df = DateFormat.getDateInstance(DateFormat.LONG);
                    SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
                    datedisplay.setText(sdf.format(theTime));
                    int day = theTime.getDay();
                    switch (day) {
                        case 0:
                            daydisplay.setText("-Sunday-");
                            break;
                        case 1:
                            daydisplay.setText("-Monday-");
                            break;
                        case 2:
                            daydisplay.setText("-Tuesday-");
                            break;
                        case 3:
                            daydisplay.setText("-Wednesday-");
                            break;
                        case 4:
                            daydisplay.setText("-Thursday-");
                            break;
                        case 5:
                            daydisplay.setText("-Friday-");
                            break;
                        case 6:
                            daydisplay.setText("-Saturday-");
                            break;
                    }
                    if (HolidayOverride == true) {
                        daydisplay.setText("-Holiday-");
                    }
                    DateFormat tf = DateFormat.getTimeInstance(DateFormat.MEDIUM);
                    timedisplay.setText(tf.format(theTime));
                    //npd.displayDate.setText(df.format(theTime));
                    //npd.displayTime.setText(tf.format(theTime));
                    //if (SlotsResetEnabled.compareToIgnoreCase("enabled") == timedisplay.getText().compareToIgnoreCase(sResetTime)) {
                    //    new SlotsStatus().StartResetAllSlots();
                    //}
                    Thread.sleep(1000);
                    if (timedisplay.getText().compareToIgnoreCase("4:30:00 AM") == 0) {
                        //ParkerDataHandler pdh = new ParkerDataHandler();
                        //pdh.copyParkerTrans2Server(EN_SentinelID);
                        //pdh.uploadLog(EN_SentinelID);

                    }
                    if (timedisplay.getText().compareToIgnoreCase("2:20:00 AM") == 0) {
                        //ParkerDataHandler pdh = new ParkerDataHandler();
                        //pdh.copyParkerTrans2Server(EN_SentinelID);

                    }
                    if (timedisplay.getText().compareToIgnoreCase("11:55:00 PM") == 0) {
                        //ParkerDataHandler pdh = new ParkerDataHandler();
                        //pdh.uploadLog(EN_SentinelID);
                    }
                }
            } catch (Exception ex) {
                log.error(ex.getMessage());
            }
        }

    }

    class SubServerCopier implements Runnable {

        @Override
        public void run() {

            try {
                while (true) {

                    Runtime r = Runtime.getRuntime();
                    Process p = r.exec("cp -uf `find /SYSTEMS -mmin -50` /SUBSYSTEMS");//u - update f - force
                    Thread.sleep(30000);
                }
            } catch (Exception ex) {
                log.error(ex.getMessage());
            }
        }

    }

    class OnlineUpdater implements Runnable {

        @Override
        public void run() {
            try {
                while (true) {
                    Thread.sleep(10000);
                    ServerDataHandler sdh = new ServerDataHandler();
                    SystemStatus ss = new SystemStatus();
                    DataBaseHandler DBH = new DataBaseHandler();
                    //online = ss.checkPING(BackupMainServer);
                    if (ss.checkPING(BackupMainServer) == true) {
                        //sdh.UpdateLOGIN();
                        //sdh.UpdateMPP();
//                        DBH.copyCRDPLTfromServer("crdplt.main", "crdplt.main");
//                        DBH.copyExitTransfromLocal("carpark.exit_trans", "carpark.exit_trans");
//                        DBH.copyColltrainfromLocal("colltrain.main", "colltrain.main");
//                        DBH.copyZReadfromLocal("zread.main", "zread.main");
                    }
                    Thread.sleep(10000);
                }
            } catch (Exception ex) {
                log.error(ex.getMessage());
            }
        }
    }

    class OnlineQuickUpdater implements Runnable {

        @Override
        public void run() {
            try {
                while (true) {
                    Thread.sleep(5000);
                    SystemStatus ss = new SystemStatus();
                    online = checkPING(serverIP);//LINUX USE ONLY - also check your root password
                    if (online == true) {
                        if (ss.checkOnline() == true)//if true then you can read online.aaa
                        {
                            ss.updateServerCRDPLT();    //Transacted Entrances not passed to Server
                            ss.deleteOfflineCRDPLT(serverIP);   //For Exit Cards transacted by not deleted
                            ss.updateCoupons(serverIP);
                        }
                    }
                    Thread.sleep(5000);
                }
            } catch (Exception ex) {
                log.error(ex.getMessage());
            }
        }
    }

    class SlotsComputer implements Runnable {

        boolean runwait = false;
        boolean runslotscomputation = false;
        boolean Globalrunwait = false;
        SlotsStatus ss = new SlotsStatus();

        public SlotsComputer(boolean compute) {
            runslotscomputation = compute;
        }

        @Override
        public void run() {
            try {

                while (true) {
                    initLoadSlots();
                    Thread.sleep(5000);
                    if (runwait == false && Globalrunwait == false) {
                        runwait = true;
                        Thread.sleep(2000);
                        online = checkPING(serverIP);//LINUX USE ONLY - also check your root password
                        if (online == true) {
                            if (ss.checkOnline() == true)//if true then you can read online.aaa
                            {
                                if (runslotscomputation == true) {
                                    ss.computeSlots(slotsmode, SlotsID, ParkingArea, computeEntry, computeExit, numofentrances, numofexits);
                                }
                                processSlotsdisplay();
                                Thread.sleep(3000);
                                String slot = ss.checkAvailableSlots(SlotsID);
                                //SlotsStatus.setText(slot);
                                runwait = false;
                                //ss.checkReset(EN_SentinelID);
                                ss.checkReset(EX_SentinelID);
                            } else {
                                runwait = false;
                            }
                        } else {
                            runwait = false;
                        }
                    } else {
                        //SlotsStatus.setText("PAUSED");
                    }

                }
            } catch (Exception ex) {
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException ex2) {
                    log.error(ex2.getMessage());
                }
//                LogManager.getLogger(TouchPanelUI.class.getName()).log(Level.SEVERE, null, ex);
                log.error(ex.getMessage());
                runwait = false;
                this.run();
            }
        }

        private void processSlotsdisplay() {
            for (int x = 0; x < numofentrances; x++) {
                errorMsg[x].setText("EN: " + ss.getENSERVdata(x));
            }

            for (int x = 0; x < numofexits; x++) {
                errorMsg[x + 4].setText("EX: " + ss.getEXSERVdata(x));
            }
        }

        public void pausethis() {
            Globalrunwait = true;
        }

        public void resumethis() {
            Globalrunwait = false;
        }
    }

    class NetworkClock implements Runnable {

        @Override
        public void run() {

            try {
                while (true) {
                    SystemStatus ss = new SystemStatus();
                    online = checkPING(serverIP);//LINUX USE ONLY - also check your root password
                    if (online == true) {
                        if (ss.checkOnline() == true)//if true then you can read online.aaa
                        {
                            ServerStatus.setText("ONLINE");
                            ServerStatus.setForeground(new java.awt.Color(0, 155, 0));
                            //ss.updateServerCRDPLT();
//                        ss.deleteOfflineCRDPLT(serverIP);
//                        ss.updateCoupons(serverIP);
                        } else if (ss.checkOnline() == false) {
                            ServerStatus.setForeground(new java.awt.Color(255, 120, 0));
                            ServerStatus.setText("OFFLINE");
                            //admin1.setText("DO NOT REBOOT OR RESTART");
                            //admin5.setText("please wait");
                            //admin7.setText("SYSTEM WILL AUTO-CHECK SAMBA");                            //proc = rt.exec("sudo umount -fl -t smbfs /SYSTEMS");//maybe unneeded                             proc = rt.exec("sudo umount -fl -t cifs /SYSTEMS");//maybe unneeded
                            //proc = rt.exec("C://JTerminals/netunboot.sh");
                            //proc.waitFor();
                            //proc = rt.exec("sudo mount -t smbfs //"+serverIP+"/SYSTEMS /SYSTEMS -o username=root,password="+rootpasswd+",dmask=777,fmask=777");
                            //proc = rt.exec("mount -t cifs //"+serverIP+"/SYSTEMS /SYSTEMS -o user=root,password="+rootpasswd+",dir_mode=0777,file_mode=0777,soft,rw");//FC7
                            //proc = rt.exec("C://JTerminals/netboot.sh");
                            //proc.waitFor();
                            //proc = rt.exec("sudo mount -t cifs //"+serverIP+"/SYSTEMS /SYSTEMS -o username=root,password="+rootpasswd+",dmask=777,fmask=777,security mask=777,socket options=IPTOS_LOWDELAY TCP_NODELAY,strict sync=yes,sync always=yes,sync locking=yes");//REDHAT
                            Thread.sleep(5000);
                        }
                    } else if (online == false) {
                        ServerStatus.setForeground(new java.awt.Color(255, 0, 0));
                        ServerStatus.setText("OFFLINE");
                        //admin1.setText("DO NOT REBOOT OR RESTART");
                        //admin3.setText("please wait");
                        //admin7.setText("SYSTEM WILL AUTO-CHECK NETWORK");
                        //proc = rt.exec("sudo /JTerminals/netunboot.sh");
                        //proc = rt.exec("sudo umount -t smbfs /SYSTEMS");//maybe unneeded
                    }
                    Thread.sleep(1000);
                    //resetAdmin();
                    Thread.sleep(2000);
                }
            } catch (Exception ex) {
                log.error(ex.getMessage());
            }
        }

    }

    class MIFAREpolling implements Runnable {

        HybridPanelUI ui;

        private MIFAREpolling(HybridPanelUI aThis) {
            ui = aThis;
        }

        @Override
        public void run() {

            while (true) {

                if (null != mifare.terminal) {
                    try {
                        if (mifare.terminal.waitForCardPresent(0)) {
                            Card card = mifare.terminal.connect("T=1");
                            mifare.channel = card.getBasicChannel();
                            String s = mifare.readUID();
//                            log.info(s);
                            if (MasterIN == true) {
                                MasterCardinput.append(s);
                                MasterCardInput2.setText("**********");
                            } else {
                                if (s.compareToIgnoreCase(mastercard1) != 0 && s.compareToIgnoreCase(mastercard2) != 0
                                        && s.compareToIgnoreCase(mastercard3) != 0 && s.compareToIgnoreCase(mastercard4) != 0) {
                                    Cardinput.delete(0, Cardinput.length());
                                    CardInput2.setText(s);
                                    Cardinput.append(s);
                                    if (ExitSwitch == true) {
                                        ExitAPI ea = new ExitAPI(ui);
                                        if (printer.compareToIgnoreCase("enabled") == 0) {
                                            PrinterEnabled = true;
                                        } else {
                                            PrinterEnabled = false;
                                        }
                                        if (ea.InitiateExit(new Date(), firstscan, currenttype, PrinterEnabled) == true) {
                                            firstscan = true;
                                        }
                                    }
                                } else if (s.compareToIgnoreCase(mastercard1) == 0) {
                                    if (CashierName.compareToIgnoreCase("") == 0) {
                                        MasterCardinput.append(mastercard1);
                                        MasterCardInput2.setText("**********");
                                        WestPanel.setVisible(true);
                                        MainFuncPad.setVisible(true);
                                        SecretFuncPad.setVisible(false);
//                                        CenterPanel.setVisible(false);
                                    }
                                } else if (s.compareToIgnoreCase(mastercard2) == 0) {
                                    WestPanel.setVisible(true);
                                    MainFuncPad.setVisible(false);
                                    SecretFuncPad.setVisible(true);
                                }
                                //this.repaint();
                                //this.validate();
                            }

                            if (mifare.terminal.waitForCardAbsent(0)) {
                                isEnterPressed = false;
                                if (MasterIN == true) {
                                    resetMasterCard();
                                    MasterIN = false;
                                } else if (CashierName.compareToIgnoreCase("") == 0) {
                                    WestPanel.setVisible(false);
                                    CamPanel.setVisible(true);
                                    MainFuncPad.setVisible(false);
                                    SecretFuncPad.setVisible(false);
                                    LoginPanelX.setVisible(true);
//                                    CenterPanel.setVisible(true);
                                } else {
                                    Cardinput.delete(0, CardInput2.toString().length());
                                    CardInput2.setText("");
                                    clearLeftMIDMsgPanel();
                                    clearRightPanel();
                                    resetAllOverrides();
                                    Plateinput.delete(0, Plateinput.length());
                                    PlateInput2.setText("");
                                    MainFuncPad.setVisible(true);
                                    SecretFuncPad.setVisible(false);
                                    entryCamera.setIcon(new ImageIcon(""));
                                    entryCamera.setText("");
                                    AmtTendered.setText("");
                                    ChangeDisplay.setText("");
                                }
                            }
                        }
                        Thread.sleep(1000);
                    } catch (Exception ex) {
                        log.error(ex.getMessage());
                        mifare = null;
                        mifare = new ReadMIFARE();
                        try {
                            Thread.sleep(2000);
                        } catch (InterruptedException ex2) {
                            mifare = null;
                            mifare = new ReadMIFARE();
                            log.error(ex2.getMessage());
                        }

                    }
                } else {
                    mifare = null;
                    mifare = new ReadMIFARE();
                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException ex) {
                        mifare = null;
                        mifare = new ReadMIFARE();
                        log.error(ex.getMessage());
                    }
                }

            }

        }

    }

    private static boolean isReachable(String addr, int openPort, int timeOutMillis) {
        // Any Open port on other machine
        // openPort =  22 - ssh, 80 or 443 - webserver, 25 - mailserver etc.
        addr = "http://192.168.15.12";
        try {
            try (Socket soc = new Socket()) {
                soc.connect(new InetSocketAddress(addr, openPort), timeOutMillis);
            }
            return true;
        } catch (IOException ex) {
            return false;
        }
    }

    private boolean checkPING(String ipStr) {

        //log.info(inputLine);
        /*
        try {
            //String pingCmd = "sudo mount -t cifs //192.168.1.10/SYSTEMS /SYSTEMS -o user=root,password=sssigsc";
            String pingCmd = "ping " + ip + "";
            Runtime r = Runtime.getRuntime();
            Process p = r.exec(pingCmd);
            try {
                p.waitFor();
            } catch (Exception ex) {
                //admin1.setText(ex.getMessage());
                LogManager.getLogger(HybridPanelUI.class.getName()).log(Level.SEVERE, null, ex);
            }
            BufferedReader in = new BufferedReader(new InputStreamReader(p.getInputStream()));
            String inputLine;
            inputLine = in.readLine();////
            //inputLine = in.readLine();/////
            //this.admin2.setText(inputLine);/////

            inputLine = in.readLine(); //LINUX ONLY - needs the second line as the result of the ping
            //log.info(inputLine);
            //this.admin4.setText(inputLine);
            inputLine = in.readLine();
            inputLine = in.readLine();
            //log.info(inputLine);
            //this.admin6.setText(inputLine);
            if (inputLine != null && inputLine.length() >= 4) {
                //log.info(inputLine);
                if (inputLine != null && inputLine.substring(0, 4).compareToIgnoreCase("From") == 0) {
                    //log.info(inputLine);
                    //log.info("Offline");
                    //this.admin6.setText("Offline" + inputLine);
                    in.close();
                    return false;
                } else if (inputLine != null && inputLine.substring(0, 4).compareToIgnoreCase("Dest") == 0) {
                    //log.info(inputLine);
                    //log.info("Offline");
                    //this.admin6.setText("Offline" + inputLine);
                    in.close();
                    return false;
                } else if (inputLine != null && inputLine.substring(0, 1).compareToIgnoreCase("-") == 0) {
                    //log.info(inputLine);
                    //log.info("Offline");
                    //this.admin6.setText("Offline" + inputLine);
                    in.close();
                    return false;
                }
                //log.info("Online");
                //log.info(inputLine);
                //this.admin8.setText(inputLine);
                in.close();
                return true;
            }
            in.close();
            return false;
        } catch (Exception ex) {
            //admin1.setText(ex.getMessage());
            LogManager.getLogger(HybridPanelUI.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
         */
        return true;
    }
    //-------------------------------------------------

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel A;
    public javax.swing.JLabel AMOUNTdisplay;
    private javax.swing.JLabel AllClear;
    public javax.swing.JTextField AmtTendered;
    private javax.swing.JLabel B;
    private javax.swing.JLabel BACKSPACE;
    private javax.swing.JLabel BG;
    private javax.swing.JPanel BGPanel;
    private javax.swing.JLabel Button1;
    private javax.swing.JLabel Button10;
    private javax.swing.JLabel Button11;
    private javax.swing.JLabel Button12;
    private javax.swing.JLabel Button13;
    private javax.swing.JLabel Button14;
    private javax.swing.JLabel Button15;
    private javax.swing.JLabel Button16;
    private javax.swing.JLabel Button17;
    private javax.swing.JLabel Button18;
    private javax.swing.JLabel Button19;
    private javax.swing.JLabel Button2;
    private javax.swing.JLabel Button20;
    private javax.swing.JLabel Button21;
    private javax.swing.JLabel Button22;
    private javax.swing.JLabel Button23;
    private javax.swing.JLabel Button24;
    private javax.swing.JLabel Button25;
    private javax.swing.JLabel Button26;
    private javax.swing.JLabel Button27;
    private javax.swing.JLabel Button28;
    private javax.swing.JLabel Button29;
    private javax.swing.JLabel Button3;
    private javax.swing.JLabel Button4;
    private javax.swing.JLabel Button5;
    private javax.swing.JLabel Button6;
    private javax.swing.JLabel Button7;
    private javax.swing.JLabel Button8;
    private javax.swing.JLabel Button9;
    private javax.swing.JPanel ButtonPad;
    private javax.swing.JPanel ButtonPadBG;
    private javax.swing.JLabel C;
    private javax.swing.JPanel CamPanel;
    private javax.swing.JLabel Camera;
    private javax.swing.JPanel CameraPanel;
    public javax.swing.JLabel CardInput2;
    private javax.swing.JLabel CardNumberlbl;
    private javax.swing.JPanel CenterPanel;
    public javax.swing.JLabel ChangeDisplay;
    private javax.swing.JLabel ClientName;
    private javax.swing.JLabel Cnamelbl;
    private datechooser.beans.DateChooserCombo CollectionDate;
    private javax.swing.JLabel CouponNolbl;
    private javax.swing.JPanel CouponPanel;
    private javax.swing.JButton Create;
    private javax.swing.JLabel D;
    private javax.swing.JLabel DASH;
    private javax.swing.JLabel DateLabel;
    private javax.swing.JLabel E;
    private javax.swing.JLabel ENTER;
    private javax.swing.JPanel EastPanel;
    public javax.swing.JLabel EntryTickets;
    private javax.swing.JPanel ExitFuncPad;
    public javax.swing.JLabel ExitTickets;
    private javax.swing.JLabel F;
    private javax.swing.JLabel Funcbutton1;
    private javax.swing.JLabel Funcbutton3;
    private javax.swing.JLabel Funcbutton4;
    private javax.swing.JLabel Funcbutton6;
    private javax.swing.JLabel Funcbutton7;
    private javax.swing.JLabel G;
    private javax.swing.JLabel H;
    private javax.swing.JPanel HybridPanel;
    private javax.swing.JLabel I;
    private javax.swing.JLabel J;
    private javax.swing.JLabel K;
    private javax.swing.JLabel KeyLabel1;
    private javax.swing.JLabel KeyLabel10;
    private javax.swing.JLabel KeyLabel11;
    private javax.swing.JLabel KeyLabel12;
    private javax.swing.JLabel KeyLabel13;
    private javax.swing.JLabel KeyLabel14;
    private javax.swing.JLabel KeyLabel15;
    private javax.swing.JLabel KeyLabel16;
    private javax.swing.JLabel KeyLabel17;
    private javax.swing.JLabel KeyLabel18;
    private javax.swing.JLabel KeyLabel19;
    private javax.swing.JLabel KeyLabel2;
    private javax.swing.JLabel KeyLabel20;
    private javax.swing.JLabel KeyLabel21;
    private javax.swing.JLabel KeyLabel22;
    private javax.swing.JLabel KeyLabel23;
    private javax.swing.JLabel KeyLabel24;
    private javax.swing.JLabel KeyLabel25;
    private javax.swing.JLabel KeyLabel26;
    private javax.swing.JLabel KeyLabel27;
    private javax.swing.JLabel KeyLabel28;
    private javax.swing.JLabel KeyLabel29;
    private javax.swing.JLabel KeyLabel3;
    private javax.swing.JLabel KeyLabel4;
    private javax.swing.JLabel KeyLabel5;
    private javax.swing.JLabel KeyLabel6;
    private javax.swing.JLabel KeyLabel7;
    private javax.swing.JLabel KeyLabel8;
    private javax.swing.JLabel KeyLabel9;
    private javax.swing.JLabel L;
    private javax.swing.JPanel Labels;
    private javax.swing.JPanel LeftMIDMsgPanel;
    private javax.swing.JPanel LeftMsgPanel;
    private javax.swing.JPanel LettersPad;
    private javax.swing.JPasswordField LogPassword;
    private javax.swing.JPasswordField LogPassword1;
    private javax.swing.JPasswordField LogPassword2;
    private javax.swing.JTextField LogUsercode;
    private javax.swing.JTextField LogUsercode1;
    private javax.swing.JTextField LogUsercode2;
    private javax.swing.JButton LoginButton;
    private javax.swing.JButton LoginButton1;
    private javax.swing.JButton LoginButton2;
    private javax.swing.JLabel LoginLbl;
    private javax.swing.JLabel LoginLbl1;
    private javax.swing.JLabel LoginLbl2;
    private javax.swing.JPanel LoginPanelX;
    private javax.swing.JLabel LogoutLbl;
    private javax.swing.JLabel LogoutLbl1;
    private javax.swing.JLabel LogoutLbl2;
    private javax.swing.JPanel LogoutPanelX;
    private javax.swing.JPanel LostPanel;
    public javax.swing.JPanel LowerLeftPanel;
    public javax.swing.JPanel LowerLeftPanelX;
    private javax.swing.JPanel LowerRightPanel;
    private javax.swing.JLabel M;
    private javax.swing.JPanel MainFuncPad;
    private javax.swing.JPanel MainPanel;
    private javax.swing.JPanel ManualEntryPanel;
    private javax.swing.JLabel MasterCardInput2;
    private javax.swing.JLabel MasterCardLbl;
    private javax.swing.JPanel MasterCardPanel;
    private javax.swing.JLabel MasterFun;
    private javax.swing.JPanel MidMsgPanel;
    private javax.swing.JLabel Mode;
    private javax.swing.JLabel N;
    private javax.swing.JPanel NorthPanel;
    private javax.swing.JLabel NumButton1;
    private javax.swing.JLabel NumButton10;
    private javax.swing.JLabel NumButton11;
    private javax.swing.JLabel NumButton2;
    private javax.swing.JLabel NumButton3;
    private javax.swing.JLabel NumButton4;
    private javax.swing.JLabel NumButton5;
    private javax.swing.JLabel NumButton6;
    private javax.swing.JLabel NumButton7;
    private javax.swing.JLabel NumButton8;
    private javax.swing.JLabel NumButton9;
    private javax.swing.JLabel NumButtonAC;
    private javax.swing.JPanel NumButtonBG;
    private javax.swing.JPanel NumButtonPad;
    private javax.swing.JPanel NumLabelPad;
    private javax.swing.JLabel NumPadlbl1;
    private javax.swing.JLabel NumPadlbl10;
    private javax.swing.JLabel NumPadlbl11;
    private javax.swing.JLabel NumPadlbl2;
    private javax.swing.JLabel NumPadlbl3;
    private javax.swing.JLabel NumPadlbl4;
    private javax.swing.JLabel NumPadlbl5;
    private javax.swing.JLabel NumPadlbl6;
    private javax.swing.JLabel NumPadlbl7;
    private javax.swing.JLabel NumPadlbl8;
    private javax.swing.JLabel NumPadlbl9;
    private javax.swing.JLabel NumPadlblAC;
    private javax.swing.JLabel O;
    private javax.swing.JLabel P;
    private javax.swing.JTextField PWDoscaID;
    private javax.swing.JLabel PWORDInput2;
    private javax.swing.JLabel PWORDlbl;
    public javax.swing.JLabel ParkerInfo1;
    public javax.swing.JLabel ParkerInfo10;
    private javax.swing.JLabel ParkerInfo11;
    private javax.swing.JLabel ParkerInfo12;
    private javax.swing.JLabel ParkerInfo13;
    private javax.swing.JLabel ParkerInfo14;
    private javax.swing.JLabel ParkerInfo15;
    private javax.swing.JLabel ParkerInfo16;
    private javax.swing.JLabel ParkerInfo17;
    private javax.swing.JLabel ParkerInfo18;
    private javax.swing.JLabel ParkerInfo19;
    public javax.swing.JLabel ParkerInfo2;
    private javax.swing.JLabel ParkerInfo20;
    public javax.swing.JLabel ParkerInfo3;
    public javax.swing.JLabel ParkerInfo4;
    public javax.swing.JLabel ParkerInfo5;
    public javax.swing.JLabel ParkerInfo6;
    public javax.swing.JLabel ParkerInfo7;
    public javax.swing.JLabel ParkerInfo8;
    public javax.swing.JLabel ParkerInfo9;
    private javax.swing.JLabel Parkers;
    private javax.swing.JPanel PasswordPanel;
    public javax.swing.JLabel PlateInput2;
    private javax.swing.JLabel PlateNumberlbl;
    private javax.swing.JLabel PrepaidInput2;
    private javax.swing.JLabel ProductName;
    private javax.swing.JLabel Q;
    private javax.swing.JLabel R;
    private javax.swing.JPanel RDatePanel;
    private javax.swing.JPanel RDatePanel1;
    public javax.swing.JPanel RefundPanel;
    private javax.swing.JPanel ReprintPanel;
    private javax.swing.JPanel RightMsgPanellbl;
    private javax.swing.JLabel S;
    private javax.swing.JLabel SUBMIT;
    private datechooser.beans.DateChooserCombo SearchDateFrom;
    private datechooser.beans.DateChooserCombo SearchDateTo;
    private javax.swing.JPanel SearchDisplayPanel;
    private javax.swing.JPanel SearchPanel;
    private com.github.lgooddatepicker.components.TimePicker SearchTimeFrom;
    private com.github.lgooddatepicker.components.TimePicker SearchTimeTo;
    private javax.swing.JPanel SecretFuncPad;
    private javax.swing.JLabel SentinelIDlbl;
    public javax.swing.JLabel ServedExit;
    public javax.swing.JLabel ServedNo;
    private javax.swing.JLabel ServerInfo1;
    private javax.swing.JLabel ServerInfo10;
    private javax.swing.JLabel ServerInfo11;
    private javax.swing.JLabel ServerStatus;
    public javax.swing.JPanel SettPanel;
    private javax.swing.JPanel SouthPanel;
    private javax.swing.JLabel Special;
    private javax.swing.JPanel Status;
    private javax.swing.JLabel Switch;
    private javax.swing.JLabel Switch_Entry;
    private javax.swing.JLabel Switch_Exit;
    public javax.swing.JLabel SysMessage1;
    public javax.swing.JLabel SysMessage10;
    public javax.swing.JLabel SysMessage11;
    public javax.swing.JLabel SysMessage12;
    public javax.swing.JLabel SysMessage13;
    public javax.swing.JLabel SysMessage14;
    public javax.swing.JLabel SysMessage15;
    public javax.swing.JLabel SysMessage16;
    public javax.swing.JLabel SysMessage17;
    public javax.swing.JLabel SysMessage18;
    public javax.swing.JLabel SysMessage19;
    public javax.swing.JLabel SysMessage2;
    public javax.swing.JLabel SysMessage20;
    public javax.swing.JLabel SysMessage3;
    public javax.swing.JLabel SysMessage4;
    public javax.swing.JLabel SysMessage5;
    public javax.swing.JLabel SysMessage6;
    public javax.swing.JLabel SysMessage7;
    public javax.swing.JLabel SysMessage8;
    public javax.swing.JLabel SysMessage9;
    private javax.swing.JLabel T;
    public javax.swing.JLabel TellerName;
    private javax.swing.JLabel TerminalIDlbl;
    private javax.swing.JPanel TicketMsgPanel;
    private javax.swing.JButton TicketPlusOne;
    private javax.swing.JLabel Ticketslbl;
    private javax.swing.JLabel TimeLabel;
    private javax.swing.JLabel U;
    private javax.swing.JLabel V;
    private javax.swing.JLabel W;
    private javax.swing.JPanel WestPanel;
    private javax.swing.JLabel X;
    private javax.swing.JLabel XFunc10;
    private javax.swing.JLabel XFunc11;
    private javax.swing.JLabel XFunc12;
    private javax.swing.JLabel XFunc2;
    private javax.swing.JLabel XFunc4;
    private javax.swing.JLabel XFunc5;
    private javax.swing.JLabel XFunc6;
    private javax.swing.JLabel XFunc7;
    private javax.swing.JLabel XFunc8;
    private javax.swing.JLabel XFunc9;
    private javax.swing.JScrollPane XReadingListField;
    private javax.swing.JLabel Y;
    private javax.swing.JLabel Z;
    private datechooser.beans.DateChooserCombo ZReadingDate;
    public javax.swing.JPanel ZReadingPanel;
    public javax.swing.JLabel admin1;
    public javax.swing.JLabel admin2;
    public javax.swing.JLabel admin3;
    public javax.swing.JLabel admin4;
    public javax.swing.JLabel admin5;
    public javax.swing.JLabel admin6;
    public javax.swing.JLabel admin7;
    public javax.swing.JLabel admin8;
    private javax.swing.JPanel adminPanel;
    private javax.swing.JLabel amntlbl;
    private javax.swing.JLabel amntlbl1;
    private javax.swing.JLabel amntlbl2;
    private javax.swing.JLabel amntlbl3;
    private javax.swing.JLabel background;
    private javax.swing.JLabel carsMinusbtn;
    private javax.swing.JTextField carsNum;
    private javax.swing.JLabel carsPlusbtn;
    private javax.swing.JLabel cashColLbl;
    private javax.swing.JButton closeButton;
    private javax.swing.JButton closeButton1;
    private javax.swing.JLabel datedisplay;
    private javax.swing.JLabel daydisplay;
    public javax.swing.JLabel entryCamera;
    private javax.swing.JLabel error1;
    private javax.swing.JLabel error2;
    private javax.swing.JLabel error3;
    private javax.swing.JLabel error4;
    private javax.swing.JLabel errorMsg5;
    private javax.swing.JLabel errorMsg6;
    private javax.swing.JLabel errorMsg7;
    private javax.swing.JLabel errorMsg8;
    private javax.swing.JPanel errorPanels;
    private javax.swing.JLabel exitCamera;
    private javax.swing.JButton exitTicket;
    private javax.swing.JPanel fullKeyBoard;
    private javax.swing.JLabel fullScreenCamera;
    private javax.swing.JButton goLostBtn;
    private javax.swing.JPanel inputPanel;
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JList<String> jList1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JPanel keypadPanel;
    private javax.swing.JLabel logcancelbutton;
    private datechooser.beans.DateChooserCombo lostDateIN;
    private javax.swing.JLabel lostLbl;
    private javax.swing.JLabel lostLbl1;
    private javax.swing.JLabel lostLbl2;
    private com.github.lgooddatepicker.components.TimePicker lostTimeIN;
    private datechooser.beans.DateChooserCombo manualEntryDate;
    private javax.swing.JTextField manualEntryPlate;
    private com.github.lgooddatepicker.components.TimePicker manualEntryTime;
    private javax.swing.JLabel motorMinusbtn;
    private javax.swing.JTextField motorNum;
    private javax.swing.JLabel motorPlusbtn;
    private javax.swing.JPanel newMidPanel;
    private javax.swing.JLabel num0;
    private javax.swing.JLabel num1;
    private javax.swing.JLabel num2;
    private javax.swing.JLabel num3;
    private javax.swing.JLabel num4;
    private javax.swing.JLabel num5;
    private javax.swing.JLabel num6;
    private javax.swing.JLabel num7;
    private javax.swing.JLabel num8;
    private javax.swing.JLabel num9;
    private javax.swing.JLabel numBlank;
    private javax.swing.JPanel numpadPanel;
    private javax.swing.JTextField oscaAddr;
    private javax.swing.JTextField oscaBusStyle;
    private javax.swing.JTextField oscaName;
    private javax.swing.JTextField oscaTIN;
    private javax.swing.JLabel printCollLbl;
    private javax.swing.JLabel printCollLbl1;
    private javax.swing.JLabel printCollectionBtn;
    private javax.swing.JLabel printZReadingBtn;
    private javax.swing.JButton refundBtn;
    private javax.swing.JButton refundOut;
    private javax.swing.JButton reprintBtn;
    private javax.swing.JButton reprintButton;
    private javax.swing.JLabel reprintLbl1;
    private javax.swing.JLabel reprintLbl2;
    private javax.swing.JButton reprintOut;
    private javax.swing.JTextField reprintPlate;
    private javax.swing.JLabel searchCollectionBtn;
    private javax.swing.JLabel searchLbl;
    private javax.swing.JLabel sep;
    private javax.swing.JLabel settLbl;
    private javax.swing.JLabel settLbl1;
    private javax.swing.JLabel settLbl2;
    private javax.swing.JLabel settLbl3;
    private javax.swing.JLabel settLbl4;
    private javax.swing.JLabel settLbl5;
    private javax.swing.JLabel settLbl6;
    private javax.swing.JLabel settLbl7;
    private javax.swing.JTextField settName;
    private javax.swing.JTextField settTIN;
    private javax.swing.JPanel slotsPanel;
    private javax.swing.JLabel slotsType;
    private javax.swing.JLabel slotsType2;
    private javax.swing.JLabel spacer;
    private javax.swing.JLabel spacer1;
    private javax.swing.JLabel spacer2;
    private javax.swing.JLabel spacer3;
    private javax.swing.JLabel spacer4;
    private javax.swing.JLabel switchbutton1;
    private javax.swing.JLabel switchbutton2;
    private javax.swing.JPanel ticketsPanel;
    private javax.swing.JLabel timedisplay;
    private javax.swing.JList<String> trans4Void;
    private javax.swing.JScrollPane transactions4Void;
    private javax.swing.JLabel version;
    private javax.swing.JLabel voidLabel;
    private javax.swing.JTextField voidSelection;
    private javax.swing.JComboBox<String> voidTypeSelection;
    private javax.swing.JList<String> xreadList;
    private javax.swing.JLabel zreadLbl;
    // End of variables declaration//GEN-END:variables

    private void goSetSlots(String setnos) {
        NOSfiles nf = new NOSfiles();
        nf.calculateOverride(setnos, SlotsID, ParkingArea);
        nf.logSet(setnos, SlotsID, ParkingArea, CashierName);
    }

    //-----------------Scanner Methods
    public boolean goEnter() {

        isEnterPressed = true;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        DataBaseHandler dbh = new DataBaseHandler();
        Date lastZread = dbh.getZReadLastDate(EX_SentinelID);

        if (dbh.isZreadActive(EX_SentinelID, sdf.format(lastZread)) == false) {
            SysMessage1.setText("ZREAD has been printed");
            SysMessage2.setText(sdf.format(lastZread));
            SysMessage3.setText("");
            SysMessage4.setText("POS Deactivated");
            SysMessage5.setText("");
            SysMessage6.setText("");
            dbh.saveLog("XE", CashierID);
            return false;
        }

        /*if (SlotsInput.isVisible() == true) {
            SlotsInput.setVisible(false);
            if (SlotsInput.getText().compareToIgnoreCase("") != 0) {
            goSetSlots(SlotsInput.getText());
            }
            }*/
        settlementRef = PWDoscaID.getText();
        settlementName = oscaName.getText();
        settlementAddr = oscaAddr.getText();
        settlementTIN = oscaTIN.getText();
        settlementBusStyle = oscaBusStyle.getText();

        if (MasterIN == true) {
            MasterCardAPI mca = new MasterCardAPI();
            if (mca.isMasterCardValid(this) == true) {
                this.StartPassword();
                MasterIN = false;
            }
            this.processLeftPanelMsgs(mca.SysMsg);
        } else if (Password == true) {
            PasswordMOD pw = new PasswordMOD();
            if (pw.isPasswordValid(this) == true) {
                Password = false;
            } else {
                Password = false;
            }
            this.processLeftPanelMsgs(pw.SysMsg);
        } else if (CashierID.compareToIgnoreCase("") == 0) //ENTER button
        {
            this.StartLogInX();
        } else if (currentmode.compareToIgnoreCase("logout") == 0) {
//            if (this.isLogoutValid() == true) {
//                currentmode = "logout";
//            }
        } else if (searchparker == true) {
            ParkersAPI pc = new ParkersAPI();
            pc.getPdata(this.PlateInput2.getText());
            this.processLeftPanelMsgs(pc.SysMsg);
            resetSearchCard();
        } else if (currentmode.compareToIgnoreCase("CheckMPP") == 0) {
            currentmode = "";
            try {
                MPPchecker mpc = new MPPchecker();
                mpc.checkMPPRec(serverIP, this.PlateInput2.getText());
                processLeftPanelMsgs(mpc.SysMsg);

            } catch (Exception ex) {
                this.SysMessage1.setText("MPP Not Found $!$");
                log.info(ex);
            }
            this.ResetCARDPLATE();
            resetMPPcheck();
        } else if (currentmode.compareToIgnoreCase("CheckPREPAID") == 0) {
            StartPrepaidCheck();
            resetPrepaid();
            currentmode = "";
        } else if (currentmode.compareToIgnoreCase("CheckLOST") == 0) {
            LostCheckAPI lc = new LostCheckAPI();
            ExitAPI ea = new ExitAPI(this);
            if (lc.StartLOSTcheck(this) == true) {
                this.processLeftPanelMsgs(ea.SysMsg);
                clearLeftMIDMsgPanel();
                //npd.Greeter.setVisible(true);
                //npd.PlateNo.setText("");
                if (printer.compareToIgnoreCase("enabled") == 0) {
                    PrinterEnabled = true;
                } else {
                    PrinterEnabled = false;
                }
                if (ea.InitiateExit(new Date(), firstscan, currenttype, PrinterEnabled) == true) {
//                              this.ThrDigitalClock.sleep(3000);
//                              this.ThrNetworkClock.sleep(5000);
//                              this.ThrUpdaterClock.sleep(6000);
//                              this.ThrQuickUpdaterClock.sleep(4000);
                    Thread xit = new Thread(ea);
                    xit.start();
                    resetAllOverrides();
                }
                //ea.ValidPartII();
                //log.info("End Lost Card");
                //this.processRightPanelMsgs(ea.SysMsg);
                currentmode = "";
                //OverrideSwitch_set2Exit(true);
                this.resetLostCard();
            } else {
                modal = true;
                currentmode = "";
                //LostCard_lbl.setText("LCEP");
                CardInput2.setVisible(true);
                this.CardInput2.setText("");
                this.Cardinput.delete(0, Plateinput.length());
                this.processLeftPanelMsgs(ea.SysMsg);
                SysMessage1.setText("LOST CARD FAILED");
                SysMessage4.setText("Press LOST CARD/LCEP Again");
                currentmode = "preCheckLCEP";
                //this.processRightPanelMsgs(ea.SysMsg);
            }

        } else if (currentmode.compareToIgnoreCase("preCheckLCEP") == 0) {
            modal = true;
        } else if (InvalidFlatRate == true) {
            ExitAPI ea = new ExitAPI(this);
            PreviousCard = CardInput2.getText();
            if (ea.InitiateInvalids() == true) {
                this.repaint();
                this.validate();
//                        this.ThrDigitalClock.sleep(3000);
//                        this.ThrNetworkClock.sleep(5000);
//                        this.ThrUpdaterClock.sleep(6000);
//                        this.ThrQuickUpdaterClock.sleep(4000);
                Thread xit = new Thread(ea);
                xit.start();
                resetAllOverrides();
            }
//                           InvalidFlatRate = false;
//                          IFROverride = false;
//                          resetInvalidFlatRates();
        } else if (ExitSwitch == true) {
            busyStamp = new Date();
            
            if (PreCheckExit() == false) {
                this.SysMessage5.setText("System");
                this.SysMessage6.setText("OFFLINE");

                this.SysMessage7.setText("Please retry");
                this.SysMessage8.setText("again later");
                return false;
            }
            ExitAPI ea = new ExitAPI(this);
            if (printer.compareToIgnoreCase("enabled") == 0) {
                PrinterEnabled = true;
            } else {
                PrinterEnabled = false;
            }
            if (PrinterEnabled && printerType.compareToIgnoreCase("serial") == 0) {
                SerialEpsonHandler eh = new SerialEpsonHandler();
                eh.closePrinter();
                eh.openPrinter();
                if (eh.checkPrinterOFFstatus() == true) {
                    ea.SysMsg[2] = "PRINTER OFFLINE";
                    ea.SysMsg[5] = "PRINTER OFFLINE";
                    ea.SysMsg[7] = "Please Call";
                    ea.SysMsg[8] = "Service Engineers";
                    ea.SysMsg[9] = "Head Office will be alerted";
                    Date logtime = new Date();
                    CardInput2.setText("");
                    Cardinput.delete(0, Cardinput.length() + 1);
                    logthis.setLog(EX_SentinelID, logtime + ":. PRINTER WAS TURNED OFF by " + this.TellerName.getText());
                    this.processLeftPanelMsgs(ea.SysMsg);
                    this.repaint();
                    this.requestFocus();
                    this.validate();
                    return false;
                }
            } else {
                USBEpsonHandler eh = new USBEpsonHandler();
                eh.closePrinter();
                eh.openPrinter();
                //CHECK if USB Printer is Connected Here

            }
            //eh.closePrinter();

//            if (PrinterEnabled == true) {
        //if (isEnterPressed == true || PrevPlate.compareToIgnoreCase(Plateinput.toString()) != 0 ) {
            //PrevPlate = Plateinput.toString();
            if (PreviousCard.compareToIgnoreCase(Cardinput.toString()) != 0) {
                //********This prevents from scanning the card again.
                PreviousCard = CardInput2.getText();  //Uncomment if you want to Recheck the CARD upon exit
                //This is for Paystation Only 
                //if (this.Plateinput.length() >= 6) {
                //    this.PlateInput2.setText(Plateinput.toString());
                SysMessage8.setText("[Enter] Pressed:");
                firstscan = false;
                if (ea.InitiateExit(new Date(), firstscan, currenttype, PrinterEnabled) == true) {
                    this.repaint();
                    this.validate();
                    //                           if(ThrDisplayPole.isInterrupted()==false)
                    //                           {ThrDisplayPole.yield();
                    //                            ThrDisplayPole.interrupt();}

                    Thread xit = new Thread(ea);
                    xit.setPriority(1);
                    xit.start();
                    resetAllOverrides();
                    //                        ea.ValidPartII();
                    this.resetAllOverrides();
                    firstscan = true;
                }
                //} else {
                //    ea.SysMsg[11] = "No PLate Number";
                //    ea.SysMsg[13] = "Please input";
                //    ea.SysMsg[14] = "Plate Number";
                //    ea.SysMsg[15] = "first";
                //    ea.SysMsg[16] = "to proceed ";
                //}

            }/*
                else {
                    Date logtime = new Date();
                    CardInput2.setText("");
                    Cardinput.delete(0, Cardinput.length() + 1);
                    logthis.setLog(EX_SentinelID, logtime + ":. " + PreviousCard + "  scanned twice by " + this.TellerName.getText());
                    this.SysMessage4.setText(PreviousCard);
                    firstscan = false;
                    this.SysMessage5.setText("Duplicate Scan");
                    ea.SysMsg[0] = PreviousCard;
                    ea.SysMsg[6] = "Duplicate Scan";
                    ea.SysMsg[7] = "Press F5";
                    ea.SysMsg[8] = "to scan contents";
                    ea.SysMsg[9] = "Exit ";
                }*/
//            } 
//            else {
//                //eh.closePrinter();
//                //this.AMOUNTdisplay.setText("Printer OFFLINE");
//                ea.SysMsg[2] = "PRINTER OFFLINE";
//                ea.SysMsg[5] = "PRINTER OFFLINE";
//                ea.SysMsg[7] = "Call Service Engineers";
//                ea.SysMsg[8] = "in 10 seconds";
//                ea.SysMsg[9] = "Head Office will be alerted";
//                Date logtime = new Date();
//                CardInput2.setText("");
//                Cardinput.delete(0, Cardinput.length() + 1);
//                logthis.setLog(EX_SentinelID, logtime + ":. PRINTER WAS TURNED OFF by " + this.TellerName.getText());
//            }
            //eh.closePrinter();
            //ea.ValidPartII();
//}
            this.processLeftPanelMsgs(ea.SysMsg);
            this.repaint();
            this.requestFocus();
            this.validate();
            //isEnterPressed = false;
        } //End Exit
        else if (ExitSwitch == false) {  //Entrance Function

            if (TicketInput == true) {
                if (this.Cardinput.length() < 6) {
                    while (Cardinput.length() != 6) {
                        Cardinput.insert(0, "0");
                    }
                    this.CardInput2.setText(Cardinput.toString());
                }
            } else if (currentmode.compareToIgnoreCase("backingout") == 0) {
                this.clearRightPanel();
                this.clearLeftMIDMsgPanel();
                PreEntranceAPI PRna = new PreEntranceAPI();
                if (PRna.StartBackout(this) == true) {
                    currentmode = "";
                    resetBackout();
                    ServedNo.setText(PRna.servStatus);
                    this.processRightPanelMsgs(PRna.PrkrMsg);
                } else {
                    currentmode = "";
                    resetBackout();
                    this.processRightPanelMsgs(PRna.PrkrMsg);
                }
            } else if (ManualCard == true) {
                if (this.Cardinput.length() < 8) {
                    while (Cardinput.length() != 8) {
                        Cardinput.insert(0, "0");
                    }
                    this.CardInput2.setText(Cardinput.toString());
                }
            }
            //promptINCPlate();
            EntranceAPI na = new EntranceAPI(this);
            na.InitiateEntry(trtype);
        }
        if (TicketInput == true) {
            ResetTicket();
        } else if (ManualCard == true) {
            ResetManualCard();
        }  //End Entrance
        return true;
    }

    @SuppressWarnings("static-access")
    private boolean dumpInfo(String s, KeyEvent evt) throws Exception {
        //Card input usage only -- because card input is by keyboard wedge barcode readers
        //Plate input goes directly to anykey_mouseover and numkey_mouseover
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int code = evt.getKeyCode();
        switch (code) {
            case 65:
            case 66:
            case 67:
            case 68:
            case 69:
            case 70:
            case 71:
            case 72:
            case 73:
            case 74:
            case 75:
            case 76:
            case 77:
            case 78:
            case 79:
            case 80:
            case 81:
            case 82:
            case 83:
            case 84:
            case 85:
            case 86:
            case 87:
            case 88:
            case 90:    //z
//                if (MasterIN == true || Password == true) {
//                    validatedInput(evt);
//                    return true;
//                }
//                    else if (CheckMPP == true)
//                    {validatedInput(evt);}
//                    else if (CheckLOST == true)
//                    {validatedInput(evt);}
//                    break;
            case 89:    //y
                if (this.awaitClearCollect == true) {
                    this.StartClearCollect();
                    this.awaitClearCollect = false;
                    break;
                }
//                    if (MasterIN == true || Password == true)
//                    {secret = 3;validatedInput(evt);}
//                    else if (CheckLOST == true)
//                    {validatedInput(evt);}

            case 48: //0
            case 49: //1
            case 50: //2
            case 51: //3
            case 52: //4
            case 53: //5
            case 54: //6
            case 55: //7
            case 56: //8
            case 57: //9
            case 58: //0
            case 96: //NumPad[0]
            case 97: //NumPad[1]
            case 98: //NumPad[2]
            case 99: //NumPad[3]
            case 100: //NumPad[4]
            case 101: //NumPad[5]
            case 102: //NumPad[6]
            case 103: //NumPad[7]
            case 104: //NumPad[8]
            case 105: //NumPad[9]
            case 45: //dash -
            case 109: //dash -
                validatedInput(evt);
                //preCheckLCEP = false;
                return true;
            //accepts all numbered keyin only from 48 - 57;96 - 105

            case 10: //ENTER button
                return goEnter();
            case 27: //"Escape"
                PreviousCard = "";
                exitCamPressed = false;
                resetAllOverrides();
                CamPanel.setVisible(true);
                if (MasterIN == true) {
                    resetMasterCard();
                    MasterIN = false;
                }
                if (currentmode.compareToIgnoreCase("logout") == 0) {
                    //resetLoginout();
                    currentmode = "";
                }
                if (Password == true) {
                    resetPassword();
                    Password = false;
                } else if (currentmode.compareToIgnoreCase("CheckMPP") == 0) {
                    currentmode = "";
                    resetMPPcheck();
                } else if (currentmode.compareToIgnoreCase("CheckLOST") == 0) {
                    currentmode = "";
                    //preCheckLCEP = false;
                    this.resetLostCard();
                } else if (currentmode.compareToIgnoreCase("CheckPREPAID") == 0) {
                    currentmode = "";
                    CouponPanel.setVisible(false);
                    Prepaidinput.delete(0, Prepaidinput.length() + 1);
                } else if (currentmode.compareToIgnoreCase("CheckVIP") == 0) {
                    currentmode = "";
                    resetVIP();
                } else if (currentmode.compareToIgnoreCase("CheckLCEP") == 0) {
                    currentmode = "";
                    //preCheckLCEP = false;
                } else if (currentmode.compareToIgnoreCase("invalidflatrate") == 0) {
                    CardNumberlbl.setText("INVALID FLATRATE - DONT PRESS ESC");
                    //currentmode = "";
                    return false;
                    //preCheckLCEP = false;
                }

//                      CheckLOST = false;
                currentmode = "";
                //preCheckLCEP = false;
                Cardinput.delete(0, Cardinput.length() + 1);    //plus 1 because of the /n or Enter
                Loginput.delete(0, Loginput.length() + 1);
                CardInput2.setText("");
                this.clearLeftMIDMsgPanel();
//                      secret = 0;
                this.clearRightPanel();
                //--------entrance escape
                Cardinput.delete(0, Cardinput.length() + 1); //due to the unseen enter character [chr(13)] with every barcodecard scan
                CardInput2.setText("");
                PlateInput2.setText("");
                Plateinput.delete(0, Plateinput.length());
                Plateinput.append("");
                //logging = false;
                currentmode = "backingout";
                resetBackout();
                this.clearLeftMIDMsgPanel();
                this.clearRightPanel();
                //ResetFuncButtons();
                //ResetLogIN();
                settlementEnabled = false;
                PWDoscaID.setEditable(settlementEnabled);
                settLbl3.setForeground(new Color(204, 204, 204));
                settLbl4.setForeground(new Color(204, 204, 204));
                //SettPanel.setVisible(settlementEnabled);
//                      if (CashierID.compareToIgnoreCase("") == 0)
//                      {
//                      //  newMidPanel.setVisible(false);
//                      }
                break;

            //FUNCTION KEYS
            case 112: //F1
                showHelp();
                break;
            case 113: //F2
                if (AmtTendered.hasFocus() == false) {
                    AmtTendered.setEnabled(true);
                    AmtTendered.requestFocus();
                }
//                } else {
//                    AmtTendered.setEnabled(false);
//                    AmtTendered.requestFocus(false);
//                    BG.requestFocus();
//                }
                break;
            case 114: //F3
                log.info("Entry Mode");
                trtype = "R";
                if (payuponentry.compareToIgnoreCase("disabled") == 0) {
                    Plateinput.delete(0, Plateinput.length() + 1);
                    PlateInput2.setText("");
                    Cardinput.delete(0, Cardinput.length() + 1);
                    CardInput2.setText("");
                    this.clearLeftMIDMsgPanel();
                    this.clearRightPanel();
//                      //ResetFuncButtons();
//                      ResetLogINOUT();
//                      if (CashierID.compareToIgnoreCase("") == 0)
//                      {
//                        newMidPanel.setVisible(false);
//                      }

                    width = screenSize.width;
                    height = screenSize.height + 500;
                    OverrideSwitch_set2Exit(false);
                    BG.setIcon(new ImageIcon(new ImageIcon(getClass()
                            .getResource("/hybrid/resources/parkingBG.jpg"))
                            .getImage()
                            .getScaledInstance(screenSize.width, screenSize.height, Image.SCALE_DEFAULT)));
                }
                return false;

//            case 116:
//                this.clearLeftMIDMsgPanel();
//                this.clearRightPanel();
//                //ParkerInfo9.setText("Card Contents");            //F5
//                StartComparingCard2DB();
//                //StartReadingMIFARECard();
//                return false;
//          case 117 : ParkerInfo9.setText("Logout");           //F6
//                       CashierID = "";
//                       StartLogOut();
//                       return false;
            case 119:
                //F8  Exit Mode
                log.info("Exit Mode");
                Cardinput.delete(0, Cardinput.length() + 1);
                CardInput2.setText("");
                this.clearLeftMIDMsgPanel();
                this.clearRightPanel();
                //Dimension frameSize = getSize();
                width = screenSize.width;
                height = screenSize.height + 500;
                BG.setIcon(new ImageIcon(new ImageIcon(getClass()
                        .getResource("/hybrid/resources/parkingExit.jpg"))
                        .getImage()
                        .getScaledInstance(screenSize.width, screenSize.height, Image.SCALE_DEFAULT)));
                OverrideSwitch_set2Exit(true);

                return false;
            case 120:
                //F9
                if (ExitSwitch == false) {
                    Cardinput.delete(0, Cardinput.length() + 1);
                    Cardinput.append(getRandomString(8, characterSet));
                    CardInput2.setText(getRandomString(8, characterSet));
                } else {
                    //FOR Debugging ONLY
                    Cardinput.delete(0, Cardinput.length() + 1);
                    String randomExitCard = new DataBaseHandler().getRandomCard();
                    Cardinput.append(randomExitCard);
                    CardInput2.setText(randomExitCard);
//                    Cardinput.append(getRandomString(8, characterSet));
//                    CardInput2.setText(getRandomString(8, characterSet));
                }
                return false;
            case 121:
                startMasterCard();
                return false;
            case 122:
                startLostCardSearch();
                return false;

            case 524:
                this.validate();
                return false;
            default:
            //ParkerInfo10.setText(String.valueOf(code));
        }
        return false;
    }

    private void validatedInput(KeyEvent evt) {
        if (MasterIN == true) {
            //MasterCardinput.append(evt.getKeyChar());
        } else if (Password == true) {
            PWORDinput.append(evt.getKeyChar());
        } else if (CashierID.compareToIgnoreCase("") == 0) {
            Loginput.append(evt.getKeyChar());
        } else if (currentmode.compareToIgnoreCase("logout") == 0) {
            Loginput.append(evt.getKeyChar());
        } else if (currentmode.compareToIgnoreCase("CheckMPP") == 0) {
            if (Plateinput.length() < 7) {
                Plateinput.append(evt.getKeyChar());
            }
        } else if (currentmode.compareToIgnoreCase("CheckLOST") == 0) {
            if (Plateinput.length() < 7) {
                Plateinput.append(evt.getKeyChar());
            }
        } else if (currentmode.compareToIgnoreCase("CheckLCEP") == 0) {
            Plateinput.append(evt.getKeyChar());
        } else if (currentmode.compareToIgnoreCase("CheckVIP") == 0) {
            Cardinput.append(evt.getKeyChar());
        } else if (currentmode.compareToIgnoreCase("CheckPREPAID") == 0) {
            Prepaidinput.append(evt.getKeyChar());
        } else if (Plateinput.length() < 7) {
            Plateinput.append(evt.getKeyChar());
        }
    }

    //----------------------------------------------------
    public String getRandomString(int length, char[] characterSet) {
        StringBuilder sb = new StringBuilder();

        for (int loop = 0; loop < length; loop++) {
            int index = new Random().nextInt(characterSet.length);
            sb.append(characterSet[index]);
        }

        String nonce = sb.toString();
        return nonce;
    }

    //----keyboard functions
    private void Funckeymouseover(Object anyobj) {
        JLabel any = (JLabel) anyobj;
        any.setIcon(new javax.swing.ImageIcon(getClass().getResource("/hybrid/resources/backout_press.png")));
    }

    private void Funckeymouseoff(Object anyobj) {
        JLabel any = (JLabel) anyobj;
        any.setIcon(new javax.swing.ImageIcon(getClass().getResource("/hybrid/resources/backout.png")));
    }

    private void Numkeymouseover(Object anyobj) {
        if (CashierID.compareToIgnoreCase("") != 0) {
            if (currentmode.compareToIgnoreCase("CheckLOST") == 0 && ExitSwitch == true && InvalidFlatRate == false) {
                JLabel any = (JLabel) anyobj;
                JLabel myS = (JLabel) any.getLabelFor();
                JLabel bg = (JLabel) myS.getLabelFor();

                ImageIcon imageIcon = new ImageIcon(getClass().getResource("/hybrid/resources/Numpad_midBlue.png")); // load the image to a imageIcon
                Image image = imageIcon.getImage(); // transform it
                Image newimg = image.getScaledInstance((int) Math.round(width * buttonSize), (int) Math.round(height * buttonSize), java.awt.Image.SCALE_SMOOTH); // scale it the smooth way
                imageIcon = new ImageIcon(newimg);  // transform it back
                bg.setIcon(imageIcon);
                if (Plateinput.length() < PlateDigits) {
                    Plateinput.append(myS.getText());
                    PlateInput2.setText(PlateInput2.getText() + myS.getText());
                }
            }
            if (InvalidFlatRate == true) {
                JLabel any = (JLabel) anyobj;
                JLabel myS = (JLabel) any.getLabelFor();
                JLabel bg = (JLabel) myS.getLabelFor();
                ImageIcon imageIcon = new ImageIcon(getClass().getResource("/hybrid/resources/Numpad_midBlue.png")); // load the image to a imageIcon
                Image image = imageIcon.getImage(); // transform it
                Image newimg = image.getScaledInstance((int) Math.round(width * buttonSize), (int) Math.round(height * buttonSize), java.awt.Image.SCALE_SMOOTH); // scale it the smooth way
                imageIcon = new ImageIcon(newimg);  // transform it back
                bg.setIcon(imageIcon);
                if (Plateinput.length() < PlateDigits) {
                    Plateinput.append(myS.getText());
                    PlateInput2.setVisible(true);
                    this.PlateInput2.setLocation(20, 25);
                    PlateInput2.setText(PlateInput2.getText() + myS.getText());
                    //npd.PlateNo.setText(PlateInput2.getText());
                }
            }
            if (MasterIN == true && InvalidFlatRate == false) {
                JLabel any = (JLabel) anyobj;
                JLabel myS = (JLabel) any.getLabelFor();
                JLabel bg = (JLabel) myS.getLabelFor();
                ImageIcon imageIcon = new ImageIcon(getClass().getResource("/hybrid/resources/Numpad_midBlue.png")); // load the image to a imageIcon
                Image image = imageIcon.getImage(); // transform it
                Image newimg = image.getScaledInstance((int) Math.round(width * buttonSize), (int) Math.round(height * buttonSize), java.awt.Image.SCALE_SMOOTH); // scale it the smooth way
                imageIcon = new ImageIcon(newimg);  // transform it back
                bg.setIcon(imageIcon);
                String asterisks = "";
                if (MasterCardinput.length() <= 8) {
                    MasterCardinput.append(myS.getText());
                }

                for (int i = 0; i < MasterCardinput.length(); i++) {
                    asterisks = "*" + asterisks;
                }
                MasterCardInput2.setText(asterisks);
            } else if (Password == true && InvalidFlatRate == false) {
                JLabel any = (JLabel) anyobj;
                JLabel myS = (JLabel) any.getLabelFor();
                JLabel bg = (JLabel) myS.getLabelFor();
                ImageIcon imageIcon = new ImageIcon(getClass().getResource("/hybrid/resources/Numpad_midBlue.png")); // load the image to a imageIcon
                Image image = imageIcon.getImage(); // transform it
                Image newimg = image.getScaledInstance((int) Math.round(width * buttonSize), (int) Math.round(height * buttonSize), java.awt.Image.SCALE_SMOOTH); // scale it the smooth way
                imageIcon = new ImageIcon(newimg);  // transform it back
                bg.setIcon(imageIcon);
                String asterisks = "";
                if (this.PWORDinput.length() <= 8) {
                    PWORDinput.append(myS.getText());
                }

                for (int i = 0; i < PWORDinput.length(); i++) {
                    asterisks = "*" + asterisks;
                }
                PWORDInput2.setText(asterisks);
            } else if (currentmode.compareToIgnoreCase("CheckMPP") == 0 && InvalidFlatRate == false && PlateInput2.getText().length() < PlateDigits)//MPP
            {
                JLabel any = (JLabel) anyobj;
                JLabel myS = (JLabel) any.getLabelFor();
                JLabel bg = (JLabel) myS.getLabelFor();
                ImageIcon imageIcon = new ImageIcon(getClass().getResource("/hybrid/resources/Numpad_midBlue.png")); // load the image to a imageIcon
                Image image = imageIcon.getImage(); // transform it
                Image newimg = image.getScaledInstance((int) Math.round(width * buttonSize), (int) Math.round(height * buttonSize), java.awt.Image.SCALE_SMOOTH); // scale it the smooth way
                imageIcon = new ImageIcon(newimg);  // transform it back
                bg.setIcon(imageIcon);
                if (Plateinput.length() < 6) {
                    Plateinput.append(myS.getText());
                    PlateInput2.setText(PlateInput2.getText() + myS.getText());
                    //npd.PlateNo.setText(PlateInput2.getText());
                }
            } else if (ExitSwitch == true)//Exit Procedure
            {
                if (currentmode.compareToIgnoreCase("CheckPREPAID") == 0) {
                    JLabel any = (JLabel) anyobj;
                    JLabel myS = (JLabel) any.getLabelFor();
                    JLabel bg = (JLabel) myS.getLabelFor();
                    ImageIcon imageIcon = new ImageIcon(getClass().getResource("/hybrid/resources/Numpad_midBlue.png")); // load the image to a imageIcon
                    Image image = imageIcon.getImage(); // transform it
                    Image newimg = image.getScaledInstance((int) Math.round(width * buttonSize), (int) Math.round(height * buttonSize), java.awt.Image.SCALE_SMOOTH); // scale it the smooth way
                    imageIcon = new ImageIcon(newimg);  // transform it back
                    bg.setIcon(imageIcon);
                    if (Prepaidinput.length() < 6) {
                        Prepaidinput.append(myS.getText());
                        PrepaidInput2.setText(PrepaidInput2.getText() + myS.getText());
                    }
                } else {
                    JLabel any = (JLabel) anyobj;
                    JLabel myS = (JLabel) any.getLabelFor();
                    JLabel bg = (JLabel) myS.getLabelFor();
                    ImageIcon imageIcon = new ImageIcon(getClass().getResource("/hybrid/resources/Numpad_midBlue.png")); // load the image to a imageIcon
                    Image image = imageIcon.getImage(); // transform it
                    Image newimg = image.getScaledInstance((int) Math.round(width * buttonSize), (int) Math.round(height * buttonSize), java.awt.Image.SCALE_SMOOTH); // scale it the smooth way
                    imageIcon = new ImageIcon(newimg);  // transform it back
                    bg.setIcon(imageIcon);
                    if (Cardinput.length() < CardDigits) {
                        Cardinput.append(myS.getText());
                        CardInput2.setText(CardInput2.getText() + myS.getText());
                    }
                }
            } else if (ExitSwitch == false && InvalidFlatRate == false && PlateInput2.getText().length() < PlateDigits)//Entrance Only Procedures
            {
                JLabel any = (JLabel) anyobj;
                JLabel myS = (JLabel) any.getLabelFor();
                JLabel bg = (JLabel) myS.getLabelFor();
                ImageIcon imageIcon = new ImageIcon(getClass().getResource("/hybrid/resources/Numpad_midBlue.png")); // load the image to a imageIcon
                Image image = imageIcon.getImage(); // transform it
                Image newimg = image.getScaledInstance((int) Math.round(width * buttonSize), (int) Math.round(height * buttonSize), java.awt.Image.SCALE_SMOOTH); // scale it the smooth way
                imageIcon = new ImageIcon(newimg);  // transform it back
                bg.setIcon(imageIcon);
                if (Plateinput.length() < 6) {
                    Plateinput.append(myS.getText());
                    PlateInput2.setText(PlateInput2.getText() + myS.getText());
                    //npd.PlateNo.setText(PlateInput2.getText());
                }
            }

        } else {
            if (MasterIN == true && InvalidFlatRate == false) {
                JLabel any = (JLabel) anyobj;
                JLabel myS = (JLabel) any.getLabelFor();
                JLabel bg = (JLabel) myS.getLabelFor();
                ImageIcon imageIcon = new ImageIcon(getClass().getResource("/hybrid/resources/Numpad_midBlue.png")); // load the image to a imageIcon
                Image image = imageIcon.getImage(); // transform it
                Image newimg = image.getScaledInstance((int) Math.round(width * buttonSize), (int) Math.round(height * buttonSize), java.awt.Image.SCALE_SMOOTH); // scale it the smooth way
                imageIcon = new ImageIcon(newimg);  // transform it back
                bg.setIcon(imageIcon);
                String asterisks = "";
                if (MasterCardinput.length() <= 8) {
                    MasterCardinput.append(myS.getText());
                }

                for (int i = 0; i < MasterCardinput.length(); i++) {
                    asterisks = "*" + asterisks;
                }
                MasterCardInput2.setText(asterisks);
            } else if (Password == true && InvalidFlatRate == false) {
                JLabel any = (JLabel) anyobj;
                JLabel myS = (JLabel) any.getLabelFor();
                JLabel bg = (JLabel) myS.getLabelFor();
                ImageIcon imageIcon = new ImageIcon(getClass().getResource("/hybrid/resources/Numpad_midBlue.png")); // load the image to a imageIcon
                Image image = imageIcon.getImage(); // transform it
                Image newimg = image.getScaledInstance((int) Math.round(width * buttonSize), (int) Math.round(height * buttonSize), java.awt.Image.SCALE_SMOOTH); // scale it the smooth way
                imageIcon = new ImageIcon(newimg);  // transform it back
                bg.setIcon(imageIcon);
                String asterisks = "";
                if (this.PWORDinput.length() <= 8) {
                    PWORDinput.append(myS.getText());
                }

                for (int i = 0; i < PWORDinput.length(); i++) {
                    asterisks = "*" + asterisks;
                }
                PWORDInput2.setText(asterisks);
            }
            SysMessage1.setText("Log IN");
            SysMessage3.setText("- first -");
        }
    }

    private void Numkeymouseoff(Object anyobj) {
        clearLeftMIDMsgPanel();
        if (this.trtype.compareToIgnoreCase("M") != 0) {
            this.clearRightPanel();
        }
        JLabel any = (JLabel) anyobj;
        JLabel templetter = (JLabel) any.getLabelFor();

        JLabel bg = (JLabel) templetter.getLabelFor();
        ImageIcon imageIcon = new ImageIcon(getClass().getResource("/hybrid/resources/Numpad_midGreen.png")); // load the image to a imageIcon
        Image image = imageIcon.getImage(); // transform it
        Image newimg = image.getScaledInstance((int) Math.round(width * buttonSize), (int) Math.round(height * buttonSize), java.awt.Image.SCALE_SMOOTH); // scale it the smooth way
        imageIcon = new ImageIcon(newimg);  // transform it back
        bg.setIcon(imageIcon);

        int PlateLen = PlateInput2.getText().length();// - 296;
        //this.LostCard_lbl.setText("Lost Card");
        if (PlateLen == this.PlateDigits) {
            PreEntranceAPI PRea = new PreEntranceAPI();
//            if (CheckLOST==true)
//              {
//                  LostCheckAPI lc = new LostCheckAPI();
//                  ExitAPI xa = new ExitAPI(this);
//                  if (lc.StartLOSTcheck(this)==true)
//                  {
//                      this.processLeftPanelMsgs(ea.SysMsg);
//                      clearLeftMIDMsgPanel();
//                      npd.Greeter.setVisible(true);
//                      npd.PlateNo.setText("");
//                      if (xa.InitiateExit()==true)
//                              {
//                              Thread xit = new Thread(xa);
//                              xit.start();}
//
//                      this.processLeftPanelMsgs(xa.SysMsg);
//                  }
//                  else
//                  {
//                      this.processLeftPanelMsgs(xa.SysMsg);
//                      SysMessage1.setText("LOST CARD FAILED");
//                      SysMessage2.setText("Press LCEP Button");
//                      preCheckLCEP = true;
//                      this.LostCard_lbl.setText("LCEP");
//                      //this.processLeftPanelMsgs(ea.SysMsg);
//                  }
//                  busyStamp = new Date();
//            }
            if (currentmode.compareToIgnoreCase("CheckMPP") == 0) {
                SysMessage5.setText("Checking MPP Record");
                SysMessage7.setText("Press ENTER");
            } else if (PRea.PlateComplete(this) == true)// && CheckMPP == false)
            {
                PRea.SPCLPlateComplete2(this);
            }
        }
    }

    private void anykeymouseover(Object anyobj) {
        JLabel tempAny = (JLabel) anyobj;
        JLabel templetter = (JLabel) tempAny.getLabelFor();
        if (this.awaitClearCollect == true && templetter.getText().compareToIgnoreCase("Y") == 0) {
            this.StartClearCollect();
            this.awaitClearCollect = false;
        }
        if (CashierID.compareToIgnoreCase("") != 0) {
            if (currentmode.compareToIgnoreCase("CheckLOST") == 0 && ExitSwitch == true && InvalidFlatRate == false) {
                JLabel any = (JLabel) anyobj;
                JLabel bg = (JLabel) any.getLabelFor();
                ImageIcon imageIcon = new ImageIcon(getClass().getResource("/hybrid/resources/qwerty_press.png")); // load the image to a imageIcon
                Image image = imageIcon.getImage(); // transform it
                Image newimg = image.getScaledInstance((int) Math.round(width * buttonSize), (int) Math.round(height * buttonSize), java.awt.Image.SCALE_SMOOTH); // scale it the smooth way
                imageIcon = new ImageIcon(newimg);  // transform it back
                bg.setIcon(imageIcon);
                JLabel myS = (JLabel) any.getLabelFor();
                if (Plateinput.length() < PlateDigits) {
                    Plateinput.append(myS.getText());
                    PlateInput2.setText(PlateInput2.getText() + myS.getText());
                    //npd.PlateNo.setText(PlateInput2.getText());
                }
            }
            if (InvalidFlatRate == true) {
                JLabel any = (JLabel) anyobj;
                JLabel bg = (JLabel) any.getLabelFor();
                ImageIcon imageIcon = new ImageIcon(getClass().getResource("/hybrid/resources/qwerty_press.png")); // load the image to a imageIcon
                Image image = imageIcon.getImage(); // transform it
                Image newimg = image.getScaledInstance((int) Math.round(width * buttonSize), (int) Math.round(height * buttonSize), java.awt.Image.SCALE_SMOOTH); // scale it the smooth way
                imageIcon = new ImageIcon(newimg);  // transform it back
                bg.setIcon(imageIcon);
                JLabel myS = (JLabel) any.getLabelFor();
                if (Plateinput.length() < PlateDigits) {
                    Plateinput.append(myS.getText());
                    PlateInput2.setVisible(true);
                    PlateInput2.setLocation(20, 25);
                    PlateInput2.setText(PlateInput2.getText() + myS.getText());
                    //npd.PlateNo.setText(PlateInput2.getText());
                }
            }
            if (MasterIN == true && InvalidFlatRate == false) {
                JLabel any = (JLabel) anyobj;
                JLabel bg = (JLabel) any.getLabelFor();
                ImageIcon imageIcon = new ImageIcon(getClass().getResource("/hybrid/resources/qwerty_press.png")); // load the image to a imageIcon
                Image image = imageIcon.getImage(); // transform it
                Image newimg = image.getScaledInstance((int) Math.round(width * buttonSize), (int) Math.round(height * buttonSize), java.awt.Image.SCALE_SMOOTH); // scale it the smooth way
                imageIcon = new ImageIcon(newimg);  // transform it back
                bg.setIcon(imageIcon);
                JLabel myS = (JLabel) any.getLabelFor();
                if (MasterCardinput.length() <= 8) {
                    MasterCardinput.append(myS.getText());
                }
                String asterisks = "";
                for (int i = 0; i < MasterCardinput.length(); i++) {
                    asterisks = "*" + asterisks;
                }
                MasterCardInput2.setText(asterisks);
            } else if (Password == true && InvalidFlatRate == false) {
                JLabel any = (JLabel) anyobj;
                JLabel bg = (JLabel) any.getLabelFor();
                ImageIcon imageIcon = new ImageIcon(getClass().getResource("/hybrid/resources/qwerty_press.png")); // load the image to a imageIcon
                Image image = imageIcon.getImage(); // transform it
                Image newimg = image.getScaledInstance((int) Math.round(width * buttonSize), (int) Math.round(height * buttonSize), java.awt.Image.SCALE_SMOOTH); // scale it the smooth way
                imageIcon = new ImageIcon(newimg);  // transform it back
                bg.setIcon(imageIcon);
                JLabel myS = (JLabel) any.getLabelFor();
                String asterisks = "";
                if (this.PWORDinput.length() <= 8) {
                    PWORDinput.append(myS.getText());
                }
                for (int i = 0; i < PWORDinput.length(); i++) {
                    asterisks = "*" + asterisks;
                }
                PWORDInput2.setText(asterisks);
            } else if (currentmode.compareToIgnoreCase("CheckMPP") == 0 && InvalidFlatRate == false && PlateInput2.getText().length() < PlateDigits)//MPP
            {
                JLabel any = (JLabel) anyobj;
                JLabel bg = (JLabel) any.getLabelFor();
                ImageIcon imageIcon = new ImageIcon(getClass().getResource("/hybrid/resources/qwerty_press.png")); // load the image to a imageIcon
                Image image = imageIcon.getImage(); // transform it
                Image newimg = image.getScaledInstance((int) Math.round(width * buttonSize), (int) Math.round(height * buttonSize), java.awt.Image.SCALE_SMOOTH); // scale it the smooth way
                imageIcon = new ImageIcon(newimg);  // transform it back
                bg.setIcon(imageIcon);
                JLabel myS = (JLabel) any.getLabelFor();
                if (Plateinput.length() < 6) {
                    Plateinput.append(myS.getText());
                    PlateInput2.setText(PlateInput2.getText() + myS.getText());
                    //npd.PlateNo.setText(PlateInput2.getText());
                }
            } else if (ExitSwitch == true && InvalidFlatRate == false) {
                JLabel any = (JLabel) anyobj;
                JLabel bg = (JLabel) any.getLabelFor();
                ImageIcon imageIcon = new ImageIcon(getClass().getResource("/hybrid/resources/qwerty_press.png")); // load the image to a imageIcon
                Image image = imageIcon.getImage(); // transform it
                Image newimg = image.getScaledInstance((int) Math.round(width * buttonSize), (int) Math.round(height * buttonSize), java.awt.Image.SCALE_SMOOTH); // scale it the smooth way
                imageIcon = new ImageIcon(newimg);  // transform it back
                bg.setIcon(imageIcon);
                JLabel myS = (JLabel) any.getLabelFor();
                if (Cardinput.length() < CardDigits) {
                    Cardinput.append(myS.getText());
                    CardInput2.setText(CardInput2.getText() + myS.getText());
                }
            } else if (ExitSwitch == false && InvalidFlatRate == false && PlateInput2.getText().length() < PlateDigits)//Entrance Only Procedures
            {
                JLabel any = (JLabel) anyobj;
                JLabel bg = (JLabel) any.getLabelFor();
                ImageIcon imageIcon = new ImageIcon(getClass().getResource("/hybrid/resources/qwerty_press.png")); // load the image to a imageIcon
                Image image = imageIcon.getImage(); // transform it
                Image newimg = image.getScaledInstance((int) Math.round(width * buttonSize), (int) Math.round(height * buttonSize), java.awt.Image.SCALE_SMOOTH); // scale it the smooth way
                imageIcon = new ImageIcon(newimg);  // transform it back
                bg.setIcon(imageIcon);
                JLabel myS = (JLabel) any.getLabelFor();
                if (Plateinput.length() < 6) {
                    Plateinput.append(myS.getText());
                    PlateInput2.setText(PlateInput2.getText() + myS.getText());
                    //npd.PlateNo.setText(PlateInput2.getText());
                }
            }
        } else {
            if (MasterIN == true && InvalidFlatRate == false) {
                JLabel any = (JLabel) anyobj;
                JLabel bg = (JLabel) any.getLabelFor();
                ImageIcon imageIcon = new ImageIcon(getClass().getResource("/hybrid/resources/qwerty_press.png")); // load the image to a imageIcon
                Image image = imageIcon.getImage(); // transform it
                Image newimg = image.getScaledInstance((int) Math.round(width * buttonSize), (int) Math.round(height * buttonSize), java.awt.Image.SCALE_SMOOTH); // scale it the smooth way
                imageIcon = new ImageIcon(newimg);  // transform it back
                bg.setIcon(imageIcon);
                JLabel myS = (JLabel) any.getLabelFor();
                if (MasterCardinput.length() <= 8) {
                    MasterCardinput.append(myS.getText());
                }
                String asterisks = "";
                for (int i = 0; i < MasterCardinput.length(); i++) {
                    asterisks = "*" + asterisks;
                }
                MasterCardInput2.setText(asterisks);
            } else if (Password == true && InvalidFlatRate == false) {
                JLabel any = (JLabel) anyobj;
                JLabel bg = (JLabel) any.getLabelFor();
                ImageIcon imageIcon = new ImageIcon(getClass().getResource("/hybrid/resources/qwerty_press.png")); // load the image to a imageIcon
                Image image = imageIcon.getImage(); // transform it
                Image newimg = image.getScaledInstance((int) Math.round(width * buttonSize), (int) Math.round(height * buttonSize), java.awt.Image.SCALE_SMOOTH); // scale it the smooth way
                imageIcon = new ImageIcon(newimg);  // transform it back
                bg.setIcon(imageIcon);
                JLabel myS = (JLabel) any.getLabelFor();
                String asterisks = "";
                if (this.PWORDinput.length() <= 8) {
                    PWORDinput.append(myS.getText());
                }
                for (int i = 0; i < PWORDinput.length(); i++) {
                    asterisks = "*" + asterisks;
                }
                PWORDInput2.setText(asterisks);
            }
            SysMessage1.setText("Log IN");
            SysMessage3.setText("- first -");
        }
    }

    private void anykeymouseoff(Object anyobj) {
        clearLeftMIDMsgPanel();
        if (this.trtype.compareToIgnoreCase("M") != 0) {
            this.clearRightPanel();
        }
        JLabel any = (JLabel) anyobj;
        JLabel bg = (JLabel) any.getLabelFor();
        JLabel templetter = (JLabel) any.getLabelFor();
        ImageIcon imageIcon = new ImageIcon(getClass().getResource("/hybrid/resources/qwerty.png")); // load the image to a imageIcon
        Image image = imageIcon.getImage(); // transform it
        Image newimg = image.getScaledInstance((int) Math.round(width * buttonSize), (int) Math.round(height * buttonSize), java.awt.Image.SCALE_SMOOTH); // scale it the smooth way
        imageIcon = new ImageIcon(newimg);  // transform it back
        bg.setIcon(imageIcon);
        int PlateLen = PlateInput2.getText().length();// - 296;
        //this.LostCard_lbl.setText("Lost Card");

        if (PlateLen == this.PlateDigits) {
            PreEntranceAPI prea = new PreEntranceAPI();
            if (currentmode.compareToIgnoreCase("CheckMPP") == 0) {
                SysMessage5.setText("Checking MPP Record");
            } else if (prea.PlateComplete(this) == true)// && CheckMPP == false)
            {
                prea.SPCLPlateComplete2(this);
            }

        }
    }
    //--------------

    @Override
    public void windowGainedFocus(WindowEvent e) {
        this.trapExit();
    }

    @Override
    public void windowLostFocus(WindowEvent e) {
        this.trapExit();
    }

    private void trapExit() {
        this.setAlwaysOnTop(debugMode);
        if (debugMode) {
            this.toFront();
            this.requestFocus();
            this.validate();
        }
    }

}
