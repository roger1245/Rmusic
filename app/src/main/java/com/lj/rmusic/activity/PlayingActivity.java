package com.lj.rmusic.activity;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.media.Image;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.lj.ljimageloader.ImageLoader;
import com.lj.rmusic.R;
import com.lj.rmusic.service.PlayManager;
import com.lj.rmusic.service.PlayService;
import com.lj.rmusic.util.TextUtil;

public class PlayingActivity extends AppCompatActivity {

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
    private boolean isDraggingProgress;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_playing);
        playManager = PlayManager.getInstance();
        init();
        initData();
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
    }
    private void initData() {
        upDateUI();
//        sbSeekBar.setOnSeekBarChangeListener(this);
        move_forward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playManager.next();
                upDateUI();
            }
        });
        move_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playManager.pre();
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
    }
    private void upDateUI() {
//        Log.d(TAG, playManager.getNowSong().getName());
        ImageLoader.build(this).bindBitmap(playManager.getNowSong().getAl().getPicUrl(), image);
        singer.setText(playManager.getNowSong().getArs().get(0).getName());
//        Log.d(TAG, String.valueOf(playManager.getDuration()));
        maxTime.setText(TextUtil.makeShortTimeString(playManager.getDuration()));
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




}
