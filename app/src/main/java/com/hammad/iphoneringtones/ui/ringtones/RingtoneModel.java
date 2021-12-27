package com.hammad.iphoneringtones.ui.ringtones;

import java.io.Serializable;

public class RingtoneModel implements Serializable {

    String songTitle = "";
    int songDuration = 0;
    int songUrl = 0;
    String ringtoneURL = "";

    public RingtoneModel(String songTitle, int songDuration, int songUrl) {
        this.songTitle = songTitle;
        this.songDuration = songDuration;
        this.songUrl = songUrl;
    }

    public RingtoneModel(String songTitle, String ringtoneURL) {
        this.songTitle = songTitle;
        this.ringtoneURL = ringtoneURL;
    }

    public String getSongTitle() {
        return songTitle;
    }

    public void setSongTitle(String songTitle) {
        this.songTitle = songTitle;
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
}
