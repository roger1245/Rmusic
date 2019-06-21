package com.lj.rmusic.activity;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Intent;
import android.graphics.Outline;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewOutlineProvider;
import android.view.animation.LinearInterpolator;
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
import com.lj.rmusic.widget.SpringActionMenu.ActionButtonItems;
import com.lj.rmusic.widget.SpringActionMenu.ActionMenu;
import com.lj.rmusic.widget.SpringActionMenu.OnActionItemClickListener;
import com.lj.rmusic.widget.jingyuntexiao.DiffuseView;

public class MainActivity extends BaseActivity<ISongView, BasePresenter<ISongView>> implements ISongView {

    private static final String TAG = "MainActivity";
    private DrawerLayout mDrawerLayout;
    private PlayManager playManager;
    private ImageView roundImage;
    private ImageView music_Detail;
    private TextView albumName;
    private TextView singer;
    private TextView songName;
    private DiffuseView diffuseView;
    private ActionMenu actionMenu;

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
        diffuseView = findViewById(R.id.front_diffuseview);
        diffuseView.start();
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

        actionMenu = findViewById(R.id.action_menu);
        actionMenu.addView(R.drawable.ic_mood_clam);
        actionMenu.addView(R.drawable.ic_mood_exciting);
        actionMenu.addView(R.drawable.ic_mood_happy);
        actionMenu.addView(R.drawable.ic_mood_unhappy);
        actionMenu.setItemClickListener(new OnActionItemClickListener() {
            @Override
            public void onItemClick(int index) {
                ActionButtonItems actionButtonItems = (ActionButtonItems)actionMenu.getChildAt(0);
                switch (index) {
                    case 1:
                        actionButtonItems.setBitmapIcon(R.drawable.ic_mood_clam);
                        break;
                    case 2:
                        actionButtonItems.setBitmapIcon(R.drawable.ic_mood_exciting);
                        break;
                    case 3:
                        actionButtonItems.setBitmapIcon(R.drawable.ic_mood_happy);
                        break;
                    case 4:
                        actionButtonItems.setBitmapIcon(R.drawable.ic_mood_unhappy);
                        break;
                    default:
                        break;
                }
            }

            @Override
            public void onAnimationEnd(boolean isOpen) {

            }
        });


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
            Log.d(TAG, playManager.getNowSong().getAl().getPicUrl());
            if (Build.VERSION.SDK_INT >= 21) {
                //裁剪
                roundImage.setOutlineProvider(new ViewOutlineProvider() {
                    @Override
                    public void getOutline(View view, Outline outline) {
                        int width = roundImage.getWidth();
                        int height = roundImage.getHeight();
                        outline.setOval(0, 0, width, height);
                    }
                });
                roundImage.setClipToOutline(true);

                //属性动画让roundImage旋转起来
                ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(roundImage, "rotation", 0, 360);
                objectAnimator.setDuration(15000);
                objectAnimator.setRepeatMode(ValueAnimator.RESTART);
                objectAnimator.setInterpolator(new LinearInterpolator());
                objectAnimator.setRepeatCount(-1);
                objectAnimator.start();
            }
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
