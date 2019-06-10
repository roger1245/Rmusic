package com.lj.rmusic.service;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.util.Log;

import com.lj.rmusic.bean.PlayList;
import com.lj.rmusic.bean.Track;
import com.lj.rmusic.interfaceO.OnPlayEventListener;
import com.lj.rmusic.util.ApiUtil;

import java.util.ArrayList;
import java.util.List;

public class PlayManager {
    private static final String TAG = PlayManager.class.getSimpleName();
    private static PlayManager sManager = null;
    private PlayService.LocalService mLocalService;
    private PlayService playService;
    private List<OnPlayEventListener> listeners = new ArrayList<>();
    private Handler handler = new Handler(Looper.getMainLooper());

    public Handler getHandler2() {
        return handler2;
    }

    private Handler handler2;


    private int position = 0;

    public synchronized static PlayManager getInstance() {
        if (sManager == null) {
            sManager = new PlayManager();
        }
        return sManager;
    }

    public void setHandler2(Handler handler2) {
        this.handler2 = handler2;
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
        handler.post(mPublishRunnable);
    }

    public void bindService(Context context) {
        context.bindService(new Intent(context, PlayService.class), connection, Context.BIND_AUTO_CREATE);
    }

    //    public void bindService(Context context, ServiceCallback callBackListener) {
//        context.bindService(new Intent(context, PlayService.class), connection, Context.BIND_AUTO_CREATE);
//        this.callBackListener = callBackListener;
//    }
    public void startPlayService(Context context) {
        context.startService(new Intent(context, PlayService.class));
    }

    public void unBindService(Context context) {
        context.unbindService(connection);
//        this.callBackListener = null;
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

    public void setDuration(int millsecs) {
//        millsecs
        duration = millsecs;
        Log.d(TAG, "setDUratio");
//        if (callBackListener != null) {
//            callBackListener.duration(millsecs);
//        }

    }

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
        handler.post(mPublishRunnable);
        for (OnPlayEventListener listener : listeners) {
            listener.publish(0);
        }
    }

    public void pause() {
        Log.d(TAG, "Pause" + String.valueOf(position));
        playService.pause();
        handler.removeCallbacks(mPublishRunnable);
    }

    public boolean isPlaying() {
        return playService.isPlaying();
    }

    public void addOnPlayEventListener(OnPlayEventListener listener) {
        if (!listeners.contains(listener)) {
            listeners.add(listener);
        }
    }

    public void removeOnPlayEventListener(OnPlayEventListener listener) {
        listeners.remove(listener);
    }

    private Runnable mPublishRunnable = new Runnable() {
        @Override
        public void run() {
            if (isPlaying()) {
                for (OnPlayEventListener listener : listeners) {
                    listener.publish(playService.getCurrentPosition());
                }
            }
            handler.postDelayed(this, 1000);
        }
    };

    public void seekTo(int progress) {
        playService.seekTo(progress);
    }

//    public void getLrc(CallBackListener callBackListener) {
//        SongPresenter.fetchLrc(songs.get(position).getId(), callBackListener);
//    }

}
