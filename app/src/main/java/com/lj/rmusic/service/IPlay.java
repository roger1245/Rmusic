package com.lj.rmusic.service;

public interface IPlay {
    void play(String path);
    void pause();
    void stop();
    int getProgress();
    int getDuration();
}
