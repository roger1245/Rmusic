package com.lj.rmusic.util;

import com.lj.rmusic.bean.MoodSong;
import com.lj.rmusic.bean.PlayList;
import com.lj.rmusic.bean.Track;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ParseUtil {

    public static MoodSong parseMoodSong(String response) {
        MoodSong song = null;
        try {
            JSONObject jsonObject = new JSONObject(response);
            int status = jsonObject.getInt("status");
            String message = jsonObject.getString("message");
            JSONObject data = jsonObject.getJSONObject("data");
            MoodSong.Data mData = new MoodSong.Data(data.getInt("id"));
            song = new MoodSong(status, message, mData);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return song;
    }

    public static PlayList parsePlayList(String response) {
        PlayList playList = new PlayList();
        try {
            JSONObject playListDetail = new JSONObject(response);
            JSONObject playListRaw = playListDetail.getJSONObject("playlist");
            JSONArray tracks = playListRaw.getJSONArray("tracks");
            List<Track> trackList = new ArrayList<>();
            for (int i = 0; i < tracks.length(); i++) {
                Track track = parseTrack(tracks.getJSONObject(i));
                trackList.add(track);
            }
            playList.setTracks(trackList);
            String coverImage = playListRaw.getString("coverImgUrl");
            String name = playListRaw.getString("name");
            String descr = playListRaw.getString("description");
            playList.setCoverImgUrl(coverImage);
            playList.setName(name);
            playList.setDescription(descr);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return playList;
    }

    public static Track parseTrack(JSONObject jsonObject) throws Exception {
        String name = jsonObject.getString("name");
        int id = jsonObject.getInt("id");
        List<Track.Ar> arList = new ArrayList<>();
        JSONArray ars = jsonObject.getJSONArray("ar");
        for (int i = 0; i < ars.length(); i++) {
            JSONObject object = ars.getJSONObject(i);
            Track.Ar ar = parseAr(object);
            arList.add(ar);
        }

        Track.Al al = parseAl(jsonObject.getJSONObject("al"));

        return new Track(name, id, arList, al);
    }

    public static Track.Ar parseAr(JSONObject ar) throws Exception {
        Track.Ar arr = new Track.Ar();
        arr.setId(ar.getInt("id"));
        arr.setName(ar.getString("name"));
        return arr;
    }

    public static Track.Al parseAl(JSONObject al) throws Exception {
        Track.Al al1 = new Track.Al();
        al1.setId(al.getInt("id"));
        al1.setName(al.getString("name"));
        al1.setPicUrl(al.getString("picUrl"));
        return al1;
    }
}
