package objects;


import javazoom.jlgui.basicplayer.BasicPlayer;
import javazoom.jlgui.basicplayer.BasicPlayerException;

import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MP3Player {
    private BasicPlayer player = new BasicPlayer();
    private String currentFileName;
    private double currentVolumeValue;
    private static Logger logger = Logger.getLogger(MP3Player.class.getName());

    public void play(String fileName) {
        try {

            if (currentFileName != null && currentFileName.equals(fileName) && player.getStatus() == BasicPlayer.PAUSED) {
                player.resume();
                return;
            }
            currentFileName = fileName;
            player.open(new File(fileName));
            player.play();
            player.setGain(currentVolumeValue);

        } catch (BasicPlayerException e) {
            logger.log(Level.SEVERE, null, e);
        }
    }

    public void stop() {
        try {
            player.stop();
        } catch (BasicPlayerException e) {
            logger.log(Level.SEVERE, null, e);
        }
    }

    public void pause() {
        try {
            player.pause();
        } catch (BasicPlayerException e) {
            logger.log(Level.SEVERE, null, e);
        }
    }

    public void setVolume(int currentValue, int maximumValue) {
        currentVolumeValue = currentValue;
        try {
            if (currentValue == 0) {
                player.setGain(0);
            } else {
                player.setGain(calcVolume(currentValue, maximumValue));
            }

        } catch (BasicPlayerException e) {
            logger.log(Level.SEVERE, null, e);
        }

    }

    private double calcVolume(int currentValue, int maximumValue) {
        currentVolumeValue = (double) currentValue / maximumValue;
        return currentVolumeValue;
    }


}
