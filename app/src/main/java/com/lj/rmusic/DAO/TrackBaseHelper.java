package com.lj.rmusic.DAO;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.lj.rmusic.bean.Track;

public class TrackBaseHelper extends SQLiteOpenHelper {
    private static final int VERSION = 1;
    private static final String DATABASE_NAME  = "trackBase.db";
    public TrackBaseHelper(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + "tracks" + "(" +
                "id integer primary key autoincrement, " + "song text, " + "singer text)"
                );
        db.execSQL("create table " + "trackslike" + "(" +
                "id integer primary key autoincrement, " + "song text, " + "singer text)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
//
//    public static ContentValues getContentValues(Track track) {
//        ContentValues values = new ContentValues();
//        values.put("song", track.getName());
//        values.put("singer", track.getArs().get(0).getName());
//        return values;
//    }
}
