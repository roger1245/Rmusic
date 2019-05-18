package com.lj.rmusic.service;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import java.io.IOException;

public class PlayService extends Service implements MediaPlayer.OnPreparedListener {

    public static final String TAG = "PlayService";
    private LocalService mBinder = new LocalService();
    private MediaPlayer mPlayer = null;

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mPlayer != null) mPlayer.release();
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        mp.start();
    }


    public class LocalService extends Binder {
        public PlayService getService() {
            return PlayService.this;
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    public void startPlayer(String path) {
        //releasePlayer();
        ensurePlayer();
        try {
            mPlayer.setDataSource(path);
            mPlayer.setOnPreparedListener(this);

//            onPrepare();
//            setPlayerState(STATE_INITIALIZED);
            mPlayer.prepareAsync();
//            setPlayerState(STATE_PREPARING);
//            Log
//            mPlayer.start();
        } catch (IOException e) {
            e.printStackTrace();
//            releasePlayer();
        }
    }
//    public void onComplete(MediaPlayer.OnCompletionListener listener) {
//        mPlayer.setOnCompletionListener(listener);
//    }
//    public void onPrepare(MediaPlayer.OnPreparedListener listener) {
//        mPlayer.setOnPreparedListener(listener);
//    }

//
////    @Override
//    public void play(String path) {
//        startPlayer(path);
//    }

    private void ensurePlayer () {
        if (mPlayer == null) {
            mPlayer = new MediaPlayer();
        }
    }
}











