package api;

import javax.media.bean.playerbean.*;

public class VideoPlayerPanelAPI extends javax.swing.JPanel {

    public void VideoPlay(String file2play) {
        setLayout(null);
        //setSize(800,600);
        mediaPlayer1.setBounds(0, -45, 820, 615);
        mediaPlayer1.setControlPanelVisible(false);
        mediaPlayer1.setPlaybackLoop(true);
        mediaPlayer1.setMediaLocation(new java.lang.String(file2play));
        mediaPlayer1.start();
        add(mediaPlayer1);
    }

    public void stop() {
        if (mediaPlayer1 != null) {
            mediaPlayer1.stop();
        }

    }

    public void destroy() {
        if (mediaPlayer1 != null) {
            mediaPlayer1.deallocate();
        }

    }

    //{{DECLARE_CONTROLS
    javax.media.bean.playerbean.MediaPlayer mediaPlayer1 = new javax.media.bean.playerbean.MediaPlayer();
    //}}
}
