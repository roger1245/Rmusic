package com.lj.rmusic.service;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.util.Log;

import com.lj.rmusic.bean.PlayList;
import com.lj.rmusic.bean.Track;
import com.lj.rmusic.util.ApiUtil;

import java.util.ArrayList;
import java.util.List;

public class PlayManager {
    private static final String TAG = PlayManager.class.getSimpleName();

    private static PlayManager sManager = null;
    private PlayService.LocalService mLocalService;
    private PlayService playService;



    private int position = 0;

    public synchronized static PlayManager getInstance() {
        if (sManager == null) {
            sManager = new PlayManager();
        }
        return sManager;
    }

    private ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mLocalService = (PlayService.LocalService) service;
            playService = mLocalService.getService();


        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            mLocalService = null;
            playService = null;
        }
    };
    private List<Track> songs = new ArrayList<>();


    public void add(PlayList playListList) {
        songs.addAll(playListList.getTracks());
    }
    public void play(int i) {
        playService.startPlayer(ApiUtil.getSongFile(songs.get(i).getId()));
    }

    public void bindService(Context context) {
        context.bindService(new Intent(context, PlayService.class), connection, Context.BIND_AUTO_CREATE);
    }
    public void startPlayService (Context context) {
        context.startService(new Intent(context, PlayService.class));
    }
    public void unBindService(Context context) {
        context.unbindService(connection);
    }
    public void stopPlayService(Context context) {
        context.stopService(new Intent(context, PlayService.class));
    }
    public Track getNowSong() {
        if (position < songs.size()) {
            return songs.get(position);
        }
        return null;
    }
    public boolean next() {
        if (position + 1 < songs.size()) {
            position++;
            play(position);
            Log.d(TAG, "next" + position);
            return true;
        } else {
            position = 0;
            play(position);
            return false;
        }

    }

    public boolean pre() {
        if (position - 1 >= 0) {
            position--;
            play(position);
            Log.d(TAG, "next" + position);
            return true;
        } else {
            position = songs.size() - 1;
            play(position);
            return false;
        }
    }
    private int duration = 0;
    public int getDuration() {
        return duration;
    }
//    public void setDuration(int millsecs) {
////        millsecs
//        duration = millsecs;
//    }

//    public void playOrPause() {
//        try {
//            if (playService != null) {
//                if (playService.isPlaying()) {
//                    playService.pause();
//                } else {
//                    playService.play();
//                }
//            }
//        } catch (final Exception ignored) {
//        }
//    }
    public void play() {
        Log.d(TAG, "Play" + String.valueOf(position));
        playService.play();
    }
    public void pause() {
        Log.d(TAG, "Pause" + String.valueOf(position));
        playService.pause();
    }

    public boolean isPlaying() {
        return playService.isPlaying();
    }

//    @Override
//    public void onCompletion(MediaPlayer mp) {
//        if (mp == null) {
//            mp = new MediaPlayer();
//        }
//        try {
//            mp.reset();
//            mp.setDataSource(ApiUtil.getSongFile(songs.get(position).getId()));
//            mp.setOnPreparedListener(this);
//            mp.prepareAsync();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//    @Override
//    public void onPrepared(MediaPlayer mp) {
//        mp.start();
//    }
}
