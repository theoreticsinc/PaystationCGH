/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package UserInteface;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

/**
 *
 * @author Theoretics
 */
public class AudioClass {
    
    AudioInputStream welcomeAudioIn = null;
    
    Clip welcomeClip = null;
    
    
    private void testAudio() {
        AudioInputStream welcomeAudioIn = null;
        try {
            welcomeAudioIn = AudioSystem.getAudioInputStream(AudioClass.class.getResource("/sounds/welcome2baguio.wav"));
            welcomeClip = AudioSystem.getClip();
            welcomeClip.open(welcomeAudioIn);

            try {
                if (welcomeClip.isActive() == false) {
                    welcomeClip.setFramePosition(0);
                    welcomeClip.start();
                    System.out.println("Welcome Message OK");
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    
        public static void main(String[] args) throws InterruptedException {
            AudioClass ac = new AudioClass();
            ac.testAudio();
        }
        
}
