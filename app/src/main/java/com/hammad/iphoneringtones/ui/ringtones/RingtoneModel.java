package com.hammad.iphoneringtones.ui.ringtones;

import java.io.Serializable;

public class RingtoneModel implements Serializable {

    String ringtoneTitle = "";
    String ringtoneURL = "";
    boolean isPlaying = false;
    int songDuration = 0;
    int songUrl = 0;

    public RingtoneModel(String ringtoneTitle, int songDuration, int songUrl) {
        this.ringtoneTitle = ringtoneTitle;
        this.songDuration = songDuration;
        this.songUrl = songUrl;
    }

    public RingtoneModel(String ringtoneTitle, String ringtoneURL) {
        this.ringtoneTitle = ringtoneTitle;
        this.ringtoneURL = ringtoneURL;
    }

    public String getRingtoneTitle() {
        return ringtoneTitle;
    }

    public void setRingtoneTitle(String ringtoneTitle) {
        this.ringtoneTitle = ringtoneTitle;
    }

    public int getSongDuration() {
        return songDuration;
    }

    public void setSongDuration(int songDuration) {
        this.songDuration = songDuration;
    }

    public int getSongUrl() {
        return songUrl;
    }

    public void setSongUrl(int songUrl) {
        this.songUrl = songUrl;
    }

    public String getRingtoneURL() {
        return ringtoneURL;
    }

    public void setRingtoneURL(String ringtoneURL) {
        this.ringtoneURL = ringtoneURL;
    }

    public boolean isPlaying() {
        return isPlaying;
    }

    public void setPlaying(boolean playing) {
        isPlaying = playing;
    }
}
