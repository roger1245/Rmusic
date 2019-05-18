package com.lj.rmusic;

import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.lj.rmusic.base.BaseActivity;
import com.lj.rmusic.base.BasePresenter;
import com.lj.rmusic.interfaceO.ISongView;
import com.lj.rmusic.util.ApiUtil;

public class MainActivity extends BaseActivity<ISongView, BasePresenter<ISongView>> implements ISongView {
    private DrawerLayout mDrawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        init();
        mPresenter.fetchMoodSongs(ApiUtil.CLAM);
    }
    private void init() {
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.ic_setting);
        }
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

    @Override
    public void showSongs() {

    }

    @Override
    public void onMoodChange() {

    }
}
