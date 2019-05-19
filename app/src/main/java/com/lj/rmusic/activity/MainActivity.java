package com.lj.rmusic.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.lj.ljimageloader.ImageLoader;
import com.lj.rmusic.R;
import com.lj.rmusic.SongPresenter;
import com.lj.rmusic.base.BaseActivity;
import com.lj.rmusic.base.BasePresenter;
import com.lj.rmusic.bean.PlayList;
import com.lj.rmusic.interfaceO.ISongView;
import com.lj.rmusic.service.PlayManager;
import com.lj.rmusic.util.ApiUtil;

public class MainActivity extends BaseActivity<ISongView, BasePresenter<ISongView>> implements ISongView {
    private DrawerLayout mDrawerLayout;
    private PlayManager playManager;
    private ImageView roundImage;
    private ImageView music_Detail;
    private TextView albumName;
    private TextView singer;
    private TextView songName;

//    private PlayService.LocalService mLocalService;
//    private PlayService playService;
//    private ServiceConnection connection = new ServiceConnection() {
//        @Override
//        public void onServiceConnected(ComponentName name, IBinder service) {
//            mLocalService = (PlayService.LocalService) service;
//            playService = mLocalService.getService();
//
//        }
//
//        @Override
//        public void onServiceDisconnected(ComponentName name) {
//            mLocalService = null;
//            playService = null;
//        }
//    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        playManager = PlayManager.getInstance();
        init();
        mPresenter.fetchMoodSongs(ApiUtil.CLAM);

    }

    private void init() {
        singer = findViewById(R.id.front_song_singer);
        songName = findViewById(R.id.front_song_name);
        roundImage = findViewById(R.id.front_image_view);
        music_Detail = findViewById(R.id.music_detail);
        music_Detail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, PlayingActivity.class);
                startActivity(intent);

            }
        });
        playManager.startPlayService(this);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.ic_setting);
        }


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        playManager.stopPlayService(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        playManager.unBindService(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        playManager.bindService(this);

    }

    @Override
    protected void onResume() {
        super.onResume();
        upDateUI();
    }

    @Override
    protected BasePresenter<ISongView> createPresenter() {
        return new SongPresenter();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);
                break;
            default:
        }
        return true;
    }
    private void upDateUI() {
        if (playManager.getNowSong() != null) {
            songName.setText(playManager.getNowSong().getName());
            singer.setText(playManager.getNowSong().getArs().get(0).getName());
            ImageLoader.build(this).bindBitmap(playManager.getNowSong().getAl().getPicUrl(), roundImage);
        }
    }

    @Override
    public void showSongs(PlayList playList) {
//        PlayManager.getInstance(this).play(ApiUtil.getSongFile(playList.getTracks().get(0).getId()));
        playManager.add(playList);
        playManager.play(0);
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                upDateUI();
            }
        });


    }

    @Override
    public void onMoodChange() {

    }


}
