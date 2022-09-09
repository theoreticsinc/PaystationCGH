/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package UserInteface;

import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import javax.swing.ImageIcon;
import javax.swing.text.AbstractDocument;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;
import misc.DataBaseHandler;
import misc.ImgResizer;

/**
 *
 * @author Theoretics
 */
public class IdleEncoding extends javax.swing.JFrame {

    BufferedImage buf;
    String cardCode;
    int position = 1;

    /**
     * Creates new form IdleEncoding
     */
    public IdleEncoding() {
        initComponents();
//        sliderWidth.setVisible(false);
        sliderHeight.setVisible(false);
    }

    public void initialize() {
        DataBaseHandler dbh = new DataBaseHandler();
        cardCode = dbh.getPlateReady4Encoding();
        loadAnImage();
        DocumentFilter f = new UppercaseJTextField();
        AbstractDocument doc = (AbstractDocument) plateField.getDocument();
        doc.setDocumentFilter(f);
    }

    class UppercaseJTextField extends DocumentFilter {

        @Override
        public void insertString(DocumentFilter.FilterBypass fb, int offset, String text, AttributeSet attr) throws BadLocationException {
            fb.insertString(offset, text.toUpperCase(), attr);
        }

        @Override
        public void replace(DocumentFilter.FilterBypass fb, int offset, int length, String text, AttributeSet attrs) throws BadLocationException {
            fb.replace(offset, length, text.toUpperCase(), attrs);
        }
    }

    private void loadAnImage() {
        DataBaseHandler dbh = new DataBaseHandler();
//        cardCode = dbh.getPlateReady4Encoding();
        if (cardCode.compareTo("") != 0) {
            buf = dbh.GetImageFromDB(cardCode);
            if (null != buf) {
                reloadCameras();
                zoomEntryCamRecord.setText("");
            }
        } else {
            entryCamRecord.setIcon(null);
            entryCamRecord.setText("");
            zoomEntryCamRecord.setIcon(null);
            zoomEntryCamRecord.setText("No more Records without PlateNumbers");
        }
    }

    private void reloadCameras() {
        BufferedImage zoomBuff = null;

        int H = sliderHeight.getValue();
        int W = sliderWidth.getValue();
        int startx = sliderStartX.getValue();
        int starty = sliderStartY.getValue();

        //buf = ImgResizer.cropImage(buf, buf.getWidth() / zoom - (zoom * 10), buf.getHeight() / zoom - (zoom * 10), (buf.getWidth() / zoom) * (zoom - 1), (buf.getHeight() / zoom) * (zoom - 1));
        //buf = ImgResizer.cropImage(buf, 150, 50, (buf.getWidth() / zoom) * (zoom - 1), (buf.getHeight() / zoom) * (zoom - 1));
        if (null != buf) {
            try {
                zoomBuff = ImgResizer.cropImage(buf, startx, starty, W, H);
            } catch (Exception ex) {
//                ex.printStackTrace();
                try {
                    zoomBuff = ImgResizer.cropImage(buf, startx / 2, starty / 2, W / 2, H / 2);
                } catch (Exception exx) {
                    zoomBuff = buf;
                }
            }
            Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
            Image img = null;
            if (null != buf) {
                img = getScaledImage(buf, screenSize.width / 4 + 50, screenSize.height / 3 + 20);

                entryCamRecord.setIcon(new ImageIcon(img));
//            entryCamRecord.setText("ENTRY");
            }
            if (null != zoomBuff) {
                //Image img = getScaledImage(buf, screenSize.width / 4 + 100, screenSize.height / 3);
                img = getScaledImage(zoomBuff, screenSize.width / 4 + 150, screenSize.height / 3 + 120);

                zoomEntryCamRecord.setIcon(new ImageIcon(img));
//            zoomEntryCamRecord.setText("ZOOM");
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

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        closeButton = new javax.swing.JLabel();
        zoomEntryCamRecord = new javax.swing.JLabel();
        plateField = new javax.swing.JTextField();
        entryCamRecord = new javax.swing.JLabel();
        sliderWidth = new javax.swing.JSlider();
        sliderHeight = new javax.swing.JSlider();
        sliderStartX = new javax.swing.JSlider();
        sliderStartY = new javax.swing.JSlider();
        nextButton = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        prevButton = new javax.swing.JButton();
        updateBtn = new javax.swing.JLabel();
        BG = new javax.swing.JLabel();

        setBackground(new java.awt.Color(51, 51, 51));
        setBounds(new java.awt.Rectangle(175, 45, 0, 0));
        setUndecorated(true);
        setPreferredSize(new java.awt.Dimension(975, 600));
        setResizable(false);
        getContentPane().setLayout(null);

        closeButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/hybrid/resources/close-window-white.png"))); // NOI18N
        closeButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                closeButtonMousePressed(evt);
            }
        });
        getContentPane().add(closeButton);
        closeButton.setBounds(0, 0, 50, 60);

        zoomEntryCamRecord.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        zoomEntryCamRecord.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        getContentPane().add(zoomEntryCamRecord);
        zoomEntryCamRecord.setBounds(10, 70, 520, 408);

        plateField.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        plateField.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        plateField.setToolTipText("PLATE");
        getContentPane().add(plateField);
        plateField.setBounds(580, 350, 145, 47);

        entryCamRecord.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        getContentPane().add(entryCamRecord);
        entryCamRecord.setBounds(580, 40, 360, 260);

        sliderWidth.setMajorTickSpacing(10);
        sliderWidth.setMaximum(400);
        sliderWidth.setMinimum(100);
        sliderWidth.setPaintTicks(true);
        sliderWidth.setValue(240);
        sliderWidth.setInverted(true);
        sliderWidth.setOpaque(false);
        sliderWidth.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                sliderWidthMouseReleased(evt);
            }
        });
        getContentPane().add(sliderWidth);
        sliderWidth.setBounds(10, 544, 550, 31);

        sliderHeight.setMajorTickSpacing(10);
        sliderHeight.setMaximum(400);
        sliderHeight.setMinimum(100);
        sliderHeight.setPaintLabels(true);
        sliderHeight.setPaintTicks(true);
        sliderHeight.setValue(200);
        sliderHeight.setInverted(true);
        sliderHeight.setOpaque(false);
        getContentPane().add(sliderHeight);
        sliderHeight.setBounds(10, 593, 1028, 45);

        sliderStartX.setMajorTickSpacing(10);
        sliderStartX.setMaximum(1000);
        sliderStartX.setMinimum(300);
        sliderStartX.setMinorTickSpacing(10);
        sliderStartX.setPaintTicks(true);
        sliderStartX.setSnapToTicks(true);
        sliderStartX.setValue(450);
        sliderStartX.setOpaque(false);
        sliderStartX.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                sliderStartXMouseReleased(evt);
            }
        });
        getContentPane().add(sliderStartX);
        sliderStartX.setBounds(10, 470, 538, 31);

        sliderStartY.setMajorTickSpacing(10);
        sliderStartY.setMaximum(300);
        sliderStartY.setMinimum(100);
        sliderStartY.setOrientation(javax.swing.JSlider.VERTICAL);
        sliderStartY.setPaintTicks(true);
        sliderStartY.setSnapToTicks(true);
        sliderStartY.setValue(200);
        sliderStartY.setInverted(true);
        sliderStartY.setOpaque(false);
        sliderStartY.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                sliderStartYMouseReleased(evt);
            }
        });
        getContentPane().add(sliderStartY);
        sliderStartY.setBounds(530, 70, 31, 386);

        nextButton.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        nextButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/hybrid/resources/next_track.png"))); // NOI18N
        nextButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                nextButtonMouseClicked(evt);
            }
        });
        getContentPane().add(nextButton);
        nextButton.setBounds(670, 535, 90, 30);

        jLabel2.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setText("Zoom Out");
        getContentPane().add(jLabel2);
        jLabel2.setBounds(18, 521, 63, 17);

        jLabel3.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(255, 255, 255));
        jLabel3.setText("Zoom In");
        getContentPane().add(jLabel3);
        jLabel3.setBounds(500, 520, 52, 17);

        jLabel1.setBackground(new java.awt.Color(102, 102, 102));
        jLabel1.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText(" IDLE Manual Encoding (PLATE NUMBERS)");
        jLabel1.setFocusable(false);
        getContentPane().add(jLabel1);
        jLabel1.setBounds(120, 0, 476, 58);

        jLabel4.setBackground(new java.awt.Color(102, 102, 102));
        jLabel4.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(255, 255, 255));
        jLabel4.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel4.setText("ENTER PLATE");
        jLabel4.setOpaque(true);
        getContentPane().add(jLabel4);
        jLabel4.setBounds(580, 310, 145, 29);

        prevButton.setBackground(new java.awt.Color(51, 51, 51));
        prevButton.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        prevButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/hybrid/resources/left_track.png"))); // NOI18N
        prevButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                prevButtonMouseClicked(evt);
            }
        });
        getContentPane().add(prevButton);
        prevButton.setBounds(560, 535, 100, 30);

        updateBtn.setIcon(new javax.swing.ImageIcon(getClass().getResource("/hybrid/resources/update.png"))); // NOI18N
        updateBtn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                updateBtnMousePressed(evt);
            }
        });
        getContentPane().add(updateBtn);
        updateBtn.setBounds(580, 410, 150, 40);

        BG.setBackground(new java.awt.Color(102, 102, 102));
        BG.setOpaque(true);
        getContentPane().add(BG);
        BG.setBounds(-6, -10, 1290, 650);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void sliderStartYMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_sliderStartYMouseReleased
        reloadCameras();
    }//GEN-LAST:event_sliderStartYMouseReleased

    private void sliderStartXMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_sliderStartXMouseReleased
        reloadCameras();
    }//GEN-LAST:event_sliderStartXMouseReleased

    private void nextButtonMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_nextButtonMouseClicked
        position++;
        DataBaseHandler dbh = new DataBaseHandler();
        cardCode = dbh.getNextPlateReady4Encoding(position);
        loadAnImage();
        plateField.setText("");

        if (cardCode.compareTo("") == 0) {
            position = 1;
        }
    }//GEN-LAST:event_nextButtonMouseClicked

    private void sliderWidthMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_sliderWidthMouseReleased
        sliderHeight.setValue(sliderWidth.getValue());
        reloadCameras();
    }//GEN-LAST:event_sliderWidthMouseReleased

    private void prevButtonMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_prevButtonMouseClicked
        DataBaseHandler dbh = new DataBaseHandler();
        cardCode = dbh.getPlateReady4Encoding();
        loadAnImage();
        plateField.setText("");
    }//GEN-LAST:event_prevButtonMouseClicked

    private void closeButtonMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_closeButtonMousePressed
        this.setVisible(false);
    }//GEN-LAST:event_closeButtonMousePressed

    private void updateBtnMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_updateBtnMousePressed
        DataBaseHandler dbh = new DataBaseHandler();
        dbh.updateEncodedPlate(plateField.getText(), cardCode);
        plateField.setSelectionStart(0);
        plateField.setSelectionEnd(plateField.getText().length());
        plateField.requestFocus();
        plateField.validate();
    }//GEN-LAST:event_updateBtnMousePressed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(IdleEncoding.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(IdleEncoding.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(IdleEncoding.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(IdleEncoding.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new IdleEncoding().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel BG;
    private javax.swing.JLabel closeButton;
    private javax.swing.JLabel entryCamRecord;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JButton nextButton;
    private javax.swing.JTextField plateField;
    private javax.swing.JButton prevButton;
    private javax.swing.JSlider sliderHeight;
    private javax.swing.JSlider sliderStartX;
    private javax.swing.JSlider sliderStartY;
    private javax.swing.JSlider sliderWidth;
    private javax.swing.JLabel updateBtn;
    private javax.swing.JLabel zoomEntryCamRecord;
    // End of variables declaration//GEN-END:variables
}
