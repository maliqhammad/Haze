package com.hammad.iphoneringtones.ui.ringtones;

import java.io.Serializable;

public class SongItem implements Serializable {

    String songTitle = "";
    int songDuration = 0;
    int songUrl = 0;
    int songBg = 0;
    int songPlayImage = 0;

    public SongItem(String songTitle, int songDuration, int songUrl, int songBg, int songPlayImage) {
        this.songTitle = songTitle;
        this.songDuration = songDuration;
        this.songUrl = songUrl;
        this.songBg = songBg;
        this.songPlayImage = songPlayImage;
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

    public int getSongBg() {
        return songBg;
    }

    public void setSongBg(int songBg) {
        this.songBg = songBg;
    }

    public int getSongPlayImage() {
        return songPlayImage;
    }

    public void setSongPlayImage(int songPlayImage) {
        this.songPlayImage = songPlayImage;
    }
}
