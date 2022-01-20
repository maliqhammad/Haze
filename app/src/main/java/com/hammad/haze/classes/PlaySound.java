package com.hammad.haze.classes;

import android.content.Context;
import android.media.MediaPlayer;

import java.util.HashSet;

public final class PlaySound {
    private static final HashSet<MediaPlayer> mpSet = new HashSet<>();

    public static void play(Context context, int resId) {
        MediaPlayer mp = MediaPlayer.create(context, resId);
        mp.setOnCompletionListener(mp1 -> {
            mpSet.remove(mp1);
            mp1.stop();
            mp1.release();
        });
        mp.start();
    }

    public static void stop() {
        for (MediaPlayer mp : mpSet) {
            if (mp != null) {
                mp.stop();
                mp.release();
            }
        }
        mpSet.clear();
    }
}
