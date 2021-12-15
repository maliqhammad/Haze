package com.hammad.iphoneringtones.ui.slideshow

import android.content.ContentResolver
import android.content.Context
import android.content.res.Resources
import android.media.MediaMetadataRetriever
import android.net.Uri
import android.util.Log
import androidx.annotation.AnyRes
import com.hammad.iphoneringtones.R
import java.lang.Exception
import java.util.ArrayList

object SongProvider {

    fun getAllSongs(context: Context): ArrayList<SongItem> {
        val songs = ArrayList<SongItem>()
        songs.add(
            SongItem(
                context.getString(R.string.song_title1),
                getRingDuration(context, getUriToResource(context, R.raw.bismilla)).toInt(),
                R.raw.bismilla,
                R.drawable.bg_gradient1,
                R.drawable.ic_play
            )
        )
        songs.add(
            SongItem(
                context.getString(R.string.song_title2),
                getRingDuration(context, getUriToResource(context, R.raw.azan_abu_dhabi_1)).toInt(),
                R.raw.azan_abu_dhabi_1,
                R.drawable.bg_gradient2,
                R.drawable.ic_play
            )
        )
        songs.add(
            SongItem(
                context.getString(R.string.song_title3),
                getRingDuration(
                    context,
                    getUriToResource(context, R.raw.azan_abu_dhabi_2)
                ).toInt(),
                R.raw.azan_abu_dhabi_2,
                R.drawable.bg_gradient3,
                R.drawable.ic_play
            )
        )
        songs.add(
            SongItem(
                context.getString(R.string.song_title4),
                getRingDuration(
                    context,
                    getUriToResource(context, R.raw.azan_allah_akbar)
                ).toInt(),
                R.raw.azan_allah_akbar,
                R.drawable.bg_gradient4,
                R.drawable.ic_play
            )
        )
        songs.add(
            SongItem(
                context.getString(R.string.song_title5),
                getRingDuration(
                    context,
                    getUriToResource(context, R.raw.azan_ashhaduallah)
                ).toInt(),
                R.raw.azan_ashhaduallah,
                R.drawable.bg_gradient5,
                R.drawable.ic_play
            )
        )
        songs.add(
            SongItem(
                context.getString(R.string.song_title6),
                getRingDuration(context, getUriToResource(context, R.raw.azan_egypt)).toInt(),
                R.raw.azan_egypt,
                R.drawable.bg_gradient6,
                R.drawable.ic_play
            )
        )
        songs.add(
            SongItem(
                context.getString(R.string.song_title7),
                getRingDuration(context, getUriToResource(context, R.raw.azan_in_islam)).toInt(),
                R.raw.azan_in_islam,
                R.drawable.bg_gradient7,
                R.drawable.ic_play
            )
        )
        songs.add(
            SongItem(
                context.getString(R.string.song_title8),
                getRingDuration(
                    context,
                    getUriToResource(context, R.raw.azan_qoba)
                ).toInt(),
                R.raw.azan_qoba,
                R.drawable.bg_gradient8,
                R.drawable.ic_play
            )
        )
        songs.add(
            SongItem(
                context.getString(R.string.song_title9),
                getRingDuration(
                    context,
                    getUriToResource(context, R.raw.azan_raad_kurdi_1)
                ).toInt(),
                R.raw.azan_raad_kurdi_1,
                R.drawable.bg_gradient9,
                R.drawable.ic_play
            )
        )
        songs.add(
            SongItem(
                context.getString(R.string.song_title10),
                getRingDuration(
                    context,
                    getUriToResource(context, R.raw.azan_raad_kurdi_2)
                ).toInt(),
                R.raw.azan_raad_kurdi_2,
                R.drawable.bg_gradient10,
                R.drawable.ic_play
            )
        )
        songs.add(
            SongItem(
                context.getString(R.string.song_title11),
                getRingDuration(
                    context,
                    getUriToResource(context, R.raw.azan_ringtone1)
                ).toInt(),
                R.raw.azan_ringtone1,
                R.drawable.bg_gradient11,
                R.drawable.ic_play
            )
        )
        songs.add(
            SongItem(
                context.getString(R.string.song_title12),
                getRingDuration(
                    context,
                    getUriToResource(context, R.raw.azan_ringtone2)
                ).toInt(),
                R.raw.azan_ringtone2,
                R.drawable.bg_gradient12,
                R.drawable.ic_play
            )
        )
        songs.add(
            SongItem(
                context.getString(R.string.song_title13),
                getRingDuration(
                    context,
                    getUriToResource(context, R.raw.azan_ringtone3)
                ).toInt(),
                R.raw.azan_ringtone3,
                R.drawable.bg_gradient13,
                R.drawable.ic_play
            )
        )
        songs.add(
            SongItem(
                context.getString(R.string.song_title14),
                getRingDuration(context, getUriToResource(context, R.raw.azan_ringtone4)).toInt(),
                R.raw.azan_ringtone4,
                R.drawable.bg_gradient14,
                R.drawable.ic_play
            )
        )
        songs.add(
            SongItem(
                context.getString(R.string.song_title15),
                getRingDuration(context, getUriToResource(context, R.raw.azan_ringtone5)).toInt(),
                R.raw.azan_ringtone5,
                R.drawable.bg_gradient15,
                R.drawable.ic_play
            )
        )
        songs.add(
            SongItem(
                context.getString(R.string.song_title16),
                getRingDuration(
                    context,
                    getUriToResource(context, R.raw.azan_ringtone6)
                ).toInt(),
                R.raw.azan_ringtone6,
                R.drawable.bg_gradient16,
                R.drawable.ic_play
            )
        )
        songs.add(
            SongItem(
                context.getString(R.string.song_title17),
                getRingDuration(context, getUriToResource(context, R.raw.azan_uzbek)).toInt(),
                R.raw.azan_uzbek,
                R.drawable.bg_gradient17,
                R.drawable.ic_play
            )
        )
        songs.add(
            SongItem(
                context.getString(R.string.song_title18),
                getRingDuration(context, getUriToResource(context, R.raw.best_azan)).toInt(),
                R.raw.best_azan,
                R.drawable.bg_gradient18,
                R.drawable.ic_play
            )
        )
        songs.add(
            SongItem(
                context.getString(R.string.song_title19),
                getRingDuration(context, getUriToResource(context, R.raw.best_azan_tone)).toInt(),
                R.raw.best_azan_tone,
                R.drawable.bg_gradient19,
                R.drawable.ic_play
            )
        )
        songs.add(
            SongItem(
                context.getString(R.string.song_title20),
                getRingDuration(context, getUriToResource(context, R.raw.call_to_prayer)).toInt(),
                R.raw.call_to_prayer,
                R.drawable.bg_gradient20,
                R.drawable.ic_play
            )
        )

        songs.add(
            SongItem(
                context.getString(R.string.song_title21),
                getRingDuration(context, getUriToResource(context, R.raw.fajr_alarm_mecca)).toInt(),
                R.raw.fajr_alarm_mecca,
                R.drawable.bg_gradient1,
                R.drawable.ic_play
            )
        )

        songs.add(
            SongItem(
                context.getString(R.string.song_title22),
                getRingDuration(context, getUriToResource(context, R.raw.india_azan)).toInt(),
                R.raw.india_azan,
                R.drawable.bg_gradient2,
                R.drawable.ic_play
            )
        )

        songs.add(
            SongItem(
                context.getString(R.string.song_title23),
                getRingDuration(context, getUriToResource(context, R.raw.islamic_azan_1)).toInt(),
                R.raw.islamic_azan_1,
                R.drawable.bg_gradient3,
                R.drawable.ic_play
            )
        )

        songs.add(
            SongItem(
                context.getString(R.string.song_title24),
                getRingDuration(context, getUriToResource(context, R.raw.islamic_azan_2)).toInt(),
                R.raw.islamic_azan_2,
                R.drawable.bg_gradient4,
                R.drawable.ic_play
            )
        )

        songs.add(
            SongItem(
                context.getString(R.string.song_title25),
                getRingDuration(context, getUriToResource(context, R.raw.istanbul_azan)).toInt(),
                R.raw.istanbul_azan,
                R.drawable.bg_gradient5,
                R.drawable.ic_play
            )
        )

        songs.add(
            SongItem(
                context.getString(R.string.song_title26),
                getRingDuration(context, getUriToResource(context, R.raw.sweet_azan)).toInt(),
                R.raw.sweet_azan,
                R.drawable.bg_gradient6,
                R.drawable.ic_play
            )
        )

        return songs
    }

    @Throws(Resources.NotFoundException::class)
    fun getUriToResource(context: Context, @AnyRes resId: Int): Uri {
        val res: Resources = context.resources

        val uri = Uri.parse(
            ContentResolver.SCHEME_ANDROID_RESOURCE +
                    "://" + res.getResourcePackageName(resId)
                    + '/' + res.getResourceTypeName(resId)
                    + '/' + res.getResourceEntryName(resId)
        )
        Log.i("myInformation", getRingDuration(context, uri))
        return uri
    }


    private fun getRingDuration(context: Context, uri: Uri): String {
        var result = "29074"
        try {
            val mediaMetadataRetriever = MediaMetadataRetriever()
            mediaMetadataRetriever.setDataSource(context, uri)
            result =
                mediaMetadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION)!!

        } catch (e: Exception) {

        }

        return result
    }

    fun getDurationMilliToSec(duration: Int): String {
        val durSec: Int = duration / 1000
        var mString = ""
        mString = if (durSec < 59) {
            if (durSec > 9) "00:$durSec"
            else "00:0$durSec"
        } else {
            val dummyMin = durSec / 60
            val dummySec = durSec % 60
            if (dummySec > 9) "0$dummyMin:$dummySec" else "0$dummyMin:0$dummySec"
        }

        return mString

    }
}