package com.lj.rmusic.widget.lrc;

import android.content.Context;

public class CommonUtils {
    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }
}
