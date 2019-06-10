package com.lj.rmusic.activity;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.lj.ljimageloader.ImageLoader;
import com.lj.rmusic.DAO.TrackBaseHelper;
import com.lj.rmusic.R;
import com.lj.rmusic.SongPresenter;
import com.lj.rmusic.interfaceO.CallBackListener;
import com.lj.rmusic.interfaceO.OnPlayEventListener;
import com.lj.rmusic.service.PlayManager;
import com.lj.rmusic.util.ParseUtil;
import com.lj.rmusic.util.TextUtil;
import com.lj.rmusic.widget.lrc.LrcRow;
import com.lj.rmusic.widget.lrc.LrcView;

import java.lang.ref.WeakReference;
import java.util.List;

public class PlayingActivity extends AppCompatActivity implements SeekBar.OnSeekBarChangeListener, OnPlayEventListener {

    private static final String TAG = "PlayingActivity";
    private PlayManager playManager;
    private ImageView image;
    private ImageView star;
    private ImageView like;
    private ImageView button;
    private ImageView move_back;
    private ImageView move_forward;
    private TextView singer;
    private SeekBar sbSeekBar;
    private TextView nowTime;
    private TextView maxTime;
    private boolean isDraggingProgress = false;
    private SQLiteDatabase database;
    private int lastProgress;
    private LrcView mLrcView;
    private MyHandler handler;

    private boolean isFirstCreate = true;

    private static class MyHandler extends Handler {
        private final WeakReference<PlayingActivity> mActivity;

        public MyHandler(PlayingActivity activity) {
            mActivity = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            PlayingActivity activity = mActivity.get();
            if (activity != null) {
//                Log.d(TAG, activity.playManager.getDuration() + "");
                activity.sbSeekBar.setMax(activity.playManager.getDuration());
                activity.maxTime.setText(TextUtil.makeShortTimeString(activity.playManager.getDuration()));
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_playing);
        playManager = PlayManager.getInstance();

        database = new TrackBaseHelper(this).getWritableDatabase();
        init();
        initData();
        initLrcView();
        playManager.addOnPlayEventListener(this);
    }

    private void init() {
        image = findViewById(R.id.play_image_view);
        star = findViewById(R.id.play_star);
        like = findViewById(R.id.play_like);
        move_back = findViewById(R.id.play_left);
        move_forward = findViewById(R.id.play_right);
        button = findViewById(R.id.play_play);
        singer = findViewById(R.id.play_singer);
        sbSeekBar = findViewById(R.id.play_seek_bar);
        nowTime = findViewById(R.id.play_now_time);
        maxTime = findViewById(R.id.play_max_time);
        mLrcView = findViewById(R.id.play_lrc_view);
    }

    private void initLrcView() {

        mLrcView.setOnSeekToListener(onSeekToListener);
//        mLrcView.setOnLrcClickListener(this);
    }

    LrcView.OnSeekToListener onSeekToListener = new LrcView.OnSeekToListener() {

        @Override
        public void onSeekTo(int progress) {
            if (playManager.isPlaying()) {

                playManager.seekTo(progress);
            }
        }
    };


    private void initData() {

        handler = new MyHandler(this);
        playManager.setHandler2(handler);


        upDateUI();
//        sbSeekBar.setOnSeekBarChangeListener(this);
        move_forward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playManager.next();
                button.setImageResource(R.drawable.ic_play_running);
                upDateUI();
            }
        });
        move_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playManager.pre();
                button.setImageResource(R.drawable.ic_play_running);
                upDateUI();
            }
        });
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (playManager.isPlaying()) {
                    button.setImageResource(R.drawable.ic_play_pause);
                    playManager.pause();
                } else {
                    button.setImageResource(R.drawable.ic_play_running);
                    playManager.play();

                }
                upDateUI();
            }
        });
        star.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Cursor cursor = queryTracks("tracks", "song = ?", new String[]{playManager.getNowSong().getName()});
                if (cursor.getCount() >= 1) {
                    star.setImageResource(R.drawable.ic_star_off);
                    database.delete("tracks", "song = ?", new String[]{playManager.getNowSong().getName()});

                } else {
                    star.setImageResource(R.drawable.ic_star_on);
                    ContentValues contentValues = new ContentValues();
                    contentValues.put("song", playManager.getNowSong().getName());
                    contentValues.put("singer", playManager.getNowSong().getArs().get(0).getName());
                    database.insert("tracks", null, contentValues);
                }
                cursor.close();
            }
        });
        like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Cursor cursor = queryTracks("trackslike", "song = ?", new String[]{playManager.getNowSong().getName()});
                if (cursor.getCount() >= 1) {
                    like.setImageResource(R.drawable.ic_like_off);
                    database.delete("trackslike", "song = ?", new String[]{playManager.getNowSong().getName()});

                } else {
                    like.setImageResource(R.drawable.ic_like_on);
                    ContentValues contentValues = new ContentValues();
                    contentValues.put("song", playManager.getNowSong().getName());
                    contentValues.put("singer", playManager.getNowSong().getArs().get(0).getName());
                    database.insert("trackslike", null, contentValues);
                }
                cursor.close();
            }
        });
        sbSeekBar.setOnSeekBarChangeListener(this);
    }

    private void upDateUI() {


        if (isFirstCreate) {
            sbSeekBar.setMax(playManager.getDuration());
            maxTime.setText(TextUtil.makeShortTimeString(playManager.getDuration()));
            isFirstCreate = false;
        }

        Cursor cursor = queryTracks("tracks", "song = ?", new String[]{playManager.getNowSong().getName()});
        if (cursor.getCount() >= 1) {
            star.setImageResource(R.drawable.ic_star_on);
        } else {
            star.setImageResource(R.drawable.ic_star_off);
        }
        cursor.close();
        Cursor cursor2 = queryTracks("trackslike", "song = ?", new String[]{playManager.getNowSong().getName()});
        if (cursor2.getCount() >= 1) {
            like.setImageResource(R.drawable.ic_like_on);
        } else {
            like.setImageResource(R.drawable.ic_like_off);
        }
        cursor2.close();

        ImageLoader.build(this).bindBitmap(playManager.getNowSong().getAl().getPicUrl(), image);
        singer.setText(playManager.getNowSong().getArs().get(0).getName());
        SongPresenter.fetchLrc(playManager.getNowSong().getId(), new CallBackListener() {
            @Override
            public void showResponse(String response) {
                String re = ParseUtil.parseLrc(response);
                updateLrc(re);

            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        playManager.bindService(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        playManager.unBindService(this);
    }


    private Cursor queryTracks(String table, String where, String[] args) {
        Cursor cursor = database.query(table, new String[]{"song"}, where, args, null, null, null);
        return cursor;
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        playManager.setHandler2(null);
        playManager.removeOnPlayEventListener(this);

    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        if (Math.abs(progress - lastProgress) >= DateUtils.SECOND_IN_MILLIS) {
            nowTime.setText((TextUtil.makeShortTimeString(progress)));
            lastProgress = progress;
        }
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
        isDraggingProgress = true;
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        int progress = seekBar.getProgress();
        if (playManager.isPlaying()) {

            playManager.seekTo(progress);
        }
        isDraggingProgress = false;
    }


    @Override
    public void publish(int progress) {

//        Log.d(TAG, "" + progress);
        this.lastProgress = progress;
        if (!isDraggingProgress) {
//            Log.d(TAG, "this");
            sbSeekBar.setProgress(progress);
            mLrcView.seekTo(progress, true, true);
        }
        nowTime.setText(TextUtil.makeShortTimeString(progress));
    }

    public void updateLrc(String response) {

        List<LrcRow> list = ParseUtil.getLrcRows(response);
//        Log.d(TAG, list.size() + "this is size");
//        Log.d(TAG, list.get(0).toString());
        if (list != null && list.size() > 0) {
            mLrcView.setLrcRows(list);
        }
    }

}
