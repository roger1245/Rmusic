package com.lj.rmusic.base;

import com.lj.rmusic.interfaceO.ISongView;

import java.lang.ref.Reference;
import java.lang.ref.WeakReference;

public abstract class BasePresenter <T extends ISongView>{
    protected Reference<T> mViewRef;

    public void attachView(T view) {
        mViewRef = new WeakReference<T>(view);
    }

    public T getView() {
        return mViewRef.get();
    }
    public boolean isViewAttached() {
        return mViewRef !=  null && mViewRef.get() != null;
    }

    public void detachView() {
        if (mViewRef != null) {
            mViewRef.clear();
            mViewRef = null;
        }
    }
    public abstract void fetchMoodSongs(int mood);
}
