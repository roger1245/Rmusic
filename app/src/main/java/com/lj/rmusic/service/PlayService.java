package com.lj.rmusic.service;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;
import android.widget.SeekBar;

import java.io.IOException;

public class PlayService extends Service implements MediaPlayer.OnPreparedListener, MediaPlayer.OnCompletionListener{

    public static final String TAG = "PlayService";
    private LocalService mBinder = new LocalService();
    private MediaPlayer mPlayer = null;
    private PlayManager playManager;


    @Override
    public void onCreate() {
        super.onCreate();
        playManager = PlayManager.getInstance();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mPlayer != null) mPlayer.release();
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        mp.start();
//        playManager.setDuration(mp.getDuration());
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        playManager.next();
    }


//    @Override
//    public boolean onError(MediaPlayer mp, int what, int extra) {
//        ensurePlayer();
//        playManager.play();
//        return false;
//    }


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
            mPlayer.setOnCompletionListener(this);
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

//    public int getDuration() {
//        if (mPlayer != null && mPlayer.isPlaying()) {
//            return mPlayer.getDuration();
//        }
//        return 0;
//    }
    public boolean isPlaying() {
        if (mPlayer != null) {
            return mPlayer.isPlaying();
        }
        return false;
    }
    public void play() {
        mPlayer.start();
    }
    public void pause() {
        mPlayer.pause();
//        Log.d(TAG, "this");
    }

//    private void seekTo(int millsecs) {
//        if (mPlayer != null) {
//            mPlayer.seekTo(millsecs);
//        }
//    }
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
        } else {
            mPlayer.reset();
        }
    }
}











