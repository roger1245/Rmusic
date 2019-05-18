package com.lj.rmusic.interfaceO;

import com.lj.rmusic.bean.PlayList;

public interface ISongView {
    void showSongs(PlayList playList);
    void onMoodChange();
}
