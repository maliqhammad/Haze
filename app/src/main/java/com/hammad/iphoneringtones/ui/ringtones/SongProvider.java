package com.hammad.iphoneringtones.ui.ringtones;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.Resources.NotFoundException;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.util.Log;

import com.hammad.iphoneringtones.R;

import java.util.ArrayList;

import kotlin.jvm.internal.Intrinsics;

public final class SongProvider {

    public static ArrayList<RingtoneModel> getAllSongs(Context context) {
        Intrinsics.checkNotNullParameter(context, "context");
        ArrayList<RingtoneModel> songs = new ArrayList<>();
        songs.add(new RingtoneModel(
                context.getString(R.string.song_title1),
                Integer.parseInt(getRingDuration(context, getUriToResource(context, R.raw.bismilla))),
                R.raw.bismilla)
        );
        songs.add(new RingtoneModel(
                context.getString(R.string.song_title2),
                Integer.parseInt(getRingDuration(context, getUriToResource(context, R.raw.azan_abu_dhabi_1))),
                R.raw.azan_abu_dhabi_1)
        );
        songs.add(new RingtoneModel(
                context.getString(R.string.song_title3),
                Integer.parseInt(getRingDuration(context, getUriToResource(context, R.raw.azan_abu_dhabi_2))),
                R.raw.azan_abu_dhabi_2)
        );
        songs.add(new RingtoneModel(
                context.getString(R.string.song_title4),
                Integer.parseInt(getRingDuration(context, getUriToResource(context, R.raw.azan_allah_akbar))),
                R.raw.azan_allah_akbar)
        );
        songs.add(new RingtoneModel(
                context.getString(R.string.song_title5),
                Integer.parseInt(getRingDuration(context, getUriToResource(context, R.raw.azan_ashhaduallah))),
                R.raw.azan_ashhaduallah)
        );
        songs.add(new RingtoneModel(
                context.getString(R.string.song_title6),
                Integer.parseInt(getRingDuration(context, getUriToResource(context, R.raw.azan_egypt))),
                R.raw.azan_egypt)
        );
        songs.add(new RingtoneModel(
                context.getString(R.string.song_title7),
                Integer.parseInt(getRingDuration(context, getUriToResource(context, R.raw.azan_in_islam))),
                R.raw.azan_in_islam)
        );
        songs.add(new RingtoneModel(
                context.getString(R.string.song_title8),
                Integer.parseInt(getRingDuration(context, getUriToResource(context, R.raw.azan_qoba))),
                R.raw.azan_qoba)
        );
        songs.add(new RingtoneModel(
                context.getString(R.string.song_title9),
                Integer.parseInt(getRingDuration(context, getUriToResource(context, R.raw.azan_raad_kurdi_1))),
                R.raw.azan_raad_kurdi_1)
        );
        songs.add(new RingtoneModel(
                context.getString(R.string.song_title10),
                Integer.parseInt(getRingDuration(context, getUriToResource(context, R.raw.azan_raad_kurdi_2))),
                R.raw.azan_raad_kurdi_2)
        );
        songs.add(new RingtoneModel(
                context.getString(R.string.song_title11),
                Integer.parseInt(getRingDuration(context, getUriToResource(context, R.raw.azan_ringtone1))),
                R.raw.azan_ringtone1)
        );
        songs.add(new RingtoneModel(
                context.getString(R.string.song_title12),
                Integer.parseInt(getRingDuration(context, getUriToResource(context, R.raw.azan_ringtone2))),
                R.raw.azan_ringtone2)
        );
        songs.add(new RingtoneModel(
                context.getString(R.string.song_title13),
                Integer.parseInt(getRingDuration(context, getUriToResource(context, R.raw.azan_ringtone3))),
                R.raw.azan_ringtone3)
        );
        songs.add(new RingtoneModel(
                context.getString(R.string.song_title14),
                Integer.parseInt(getRingDuration(context, getUriToResource(context, R.raw.azan_ringtone4))),
                R.raw.azan_ringtone4)
        );
        songs.add(new RingtoneModel(
                context.getString(R.string.song_title15),
                Integer.parseInt(getRingDuration(context, getUriToResource(context, R.raw.azan_ringtone5))),
                R.raw.azan_ringtone5)
        );
        songs.add(new RingtoneModel(
                context.getString(R.string.song_title16),
                Integer.parseInt(getRingDuration(context, getUriToResource(context, R.raw.azan_ringtone6))),
                R.raw.azan_ringtone6)
        );
        songs.add(new RingtoneModel(
                context.getString(R.string.song_title17),
                Integer.parseInt(getRingDuration(context, getUriToResource(context, R.raw.azan_uzbek))),
                R.raw.azan_uzbek)
        );
        songs.add(new RingtoneModel(
                context.getString(R.string.song_title18),
                Integer.parseInt(getRingDuration(context, getUriToResource(context, R.raw.best_azan))),
                R.raw.best_azan)
        );
        songs.add(new RingtoneModel(
                context.getString(R.string.song_title19),
                Integer.parseInt(getRingDuration(context, getUriToResource(context, R.raw.best_azan_tone))),
                R.raw.best_azan_tone)
        );
        songs.add(new RingtoneModel(
                context.getString(R.string.song_title20),
                Integer.parseInt(getRingDuration(context, getUriToResource(context, R.raw.call_to_prayer))),
                R.raw.call_to_prayer)
        );
        songs.add(new RingtoneModel(
                context.getString(R.string.song_title21),
                Integer.parseInt(getRingDuration(context, getUriToResource(context, R.raw.fajr_alarm_mecca))),
                R.raw.fajr_alarm_mecca)
        );
        songs.add(new RingtoneModel(
                context.getString(R.string.song_title22),
                Integer.parseInt(getRingDuration(context, getUriToResource(context, R.raw.india_azan))),
                R.raw.india_azan)
        );
        songs.add(new RingtoneModel(
                context.getString(R.string.song_title23),
                Integer.parseInt(getRingDuration(context, getUriToResource(context, R.raw.islamic_azan_1))),
                R.raw.islamic_azan_1)
        );
        songs.add(new RingtoneModel(
                context.getString(R.string.song_title24),
                Integer.parseInt(getRingDuration(context, getUriToResource(context, R.raw.islamic_azan_2))),
                R.raw.islamic_azan_2)
        );
        songs.add(new RingtoneModel(
                context.getString(R.string.song_title25),
                Integer.parseInt(getRingDuration(context, getUriToResource(context, R.raw.istanbul_azan))),
                R.raw.istanbul_azan)
        );
        songs.add(new RingtoneModel(
                context.getString(R.string.song_title26),
                Integer.parseInt(getRingDuration(context, getUriToResource(context, R.raw.sweet_azan))),
                R.raw.sweet_azan)
        );
        return songs;
    }

    public static Uri getUriToResource(Context context, int resId) throws NotFoundException {
        Intrinsics.checkNotNullParameter(context, "context");
        Resources resources = context.getResources();
        Intrinsics.checkNotNullExpressionValue(resources, "context.resources");
        Uri uri = Uri.parse("android.resource://" + resources.getResourcePackageName(resId) + '/' + resources.getResourceTypeName(resId) + '/' + resources.getResourceEntryName(resId));
        Intrinsics.checkNotNullExpressionValue(uri, "uri");
        Log.i("myInformation", getRingDuration(context, uri));
        return uri;
    }

    private static String getRingDuration(Context context, Uri uri) {
        String result = "29074";
        try {
            MediaMetadataRetriever mediaMetadataRetriever = new MediaMetadataRetriever();
            mediaMetadataRetriever.setDataSource(context, uri);
            String var10000 = mediaMetadataRetriever.extractMetadata(9);
            Intrinsics.checkNotNull(var10000);
            result = var10000;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }

    public static String getDurationMilliToSec(int duration) {
        int durSec = duration / 1000;
        String mString;
        if (durSec < 59) {
            mString = durSec > 9 ? "00:" + durSec : "00:0" + durSec;
        } else {
            int dummyMin = durSec / 60;
            int dummySec = durSec % 60;
            mString = dummySec > 9 ? "" + '0' + dummyMin + ':' + dummySec : "" + '0' + dummyMin + ":0" + dummySec;
        }
        return mString;
    }

}

