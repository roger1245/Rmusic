package com.lj.rmusic.util;

import android.content.Context;
import android.util.Log;

import com.lj.rmusic.R;

public class TextUtil {
    private static final String TAG = "TextUtil";
    public static String makeShortTimeString(int millsec) {
        long mins, secs;
        Log.d(TAG, "" + millsec);

        mins = millsec /1000 / 60;
        secs = millsec / 1000 % 60;
        Log.d(TAG, mins + "   " + secs);
        final String durationFormat = "%02d:%02d";
        return String.format(durationFormat,mins, secs);
    }
}
