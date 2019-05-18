package com.lj.rmusic.activity;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.media.Image;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.lj.ljimageloader.ImageLoader;
import com.lj.rmusic.R;
import com.lj.rmusic.service.PlayManager;
import com.lj.rmusic.service.PlayService;

public class PlayingActivity extends AppCompatActivity {
    private PlayManager playManager;
    private ImageView image;
    private ImageView star;
    private ImageView like;
    private ImageView button;
    private ImageView move_back;
    private ImageView move_forward;
    private TextView singer;


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

    }
    private void initData() {
        ImageLoader.build(this).bindBitmap(playManager.getNowSong().getAl().getPicUrl(), image);
        singer.setText(playManager.getNowSong().getArs().get(0).getName());
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
