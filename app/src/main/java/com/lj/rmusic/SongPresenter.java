package com.lj.rmusic;

import android.util.Log;

import com.lj.rmusic.base.BasePresenter;
import com.lj.rmusic.bean.MoodSong;
import com.lj.rmusic.bean.PlayList;
import com.lj.rmusic.util.ApiUtil;
import com.lj.rmusic.util.HttpUtil;
import com.lj.rmusic.util.ParseUtil;

public class SongPresenter extends BasePresenter {
    private MoodSong moodSong;
    private PlayList playList;




    public static final String TAG = "SongPresenter";

    private void fetchSongsList(final String url) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpUtil.sendHttpRequest(url, true, new CallBackListener() {
                    @Override
                    public void showResponse(String response) {
//                        parseJsonMoodSong(response);
                        playList = parseJsonSongList(response);
                        getView().showSongs(playList);
                    }
                });
            }
        }).start();
    }

    private PlayList parseJsonSongList(String response) {

        return ParseUtil.parsePlayList(response);
    }


    public void  fetchMoodSongs(final int mood) {
        final String url = ApiUtil.getMoodSongs(mood);
        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpUtil.sendHttpRequest(url, true, new CallBackListener() {
                    @Override
                    public void showResponse(String response) {
                        MoodSong moodSong = parseMoodSong(response);
                        Log.d(TAG, moodSong.toString());
                        //获取歌单
                        fetchSongsList(ApiUtil.getInform(moodSong.getData().getId()));

                    }
                });
            }
        }).start();
    }
    private MoodSong parseMoodSong(String response) {
       return ParseUtil.parseMoodSong(response);
    }
}
