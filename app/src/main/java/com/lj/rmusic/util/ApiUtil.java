package com.lj.rmusic.util;

public class ApiUtil {
    public static final int HAPPY = 0;
    public static final int UNHAPPY = 1;
    public static final int CLAM = 2;
    public static final int EXCITING = 3;

    public static final String baseUrl = "http://elf.egos.hosigus.com";
    public static final String baseMusic = "http://elf.egos.hosigus.com/music";
    //需要param
    public static final String moodSongs = "http://elf.egos.hosigus.com/getSongListID.php";

    public static final String recommendSongs = baseUrl + "/getRecommendID.php";

    public static String getMoodSongs(int mood) {
        String url = null;
        switch (mood) {
            case HAPPY:
                url =  moodSongs + "?type=HAPPY";
                break;
            case UNHAPPY:
                url =  moodSongs + "?type=UNHAPPY";
                break;
            case CLAM:
                url =  moodSongs + "?type=CLAM";
                break;
            case EXCITING:
                url =  moodSongs + "?type=EXCITING";
                break;
            default:
                break;
        }
        return url;
    }
    public static String getInform(int songId) {
        return baseMusic + "/playlist/detail" + "?id=" + songId;
    }
    public static String getSongFile(int id) {
        return "http://music.163.com/song/media/outer/url?id=" + id + ".mp3";
    }

    public static String getLyric(int id) {
        return baseMusic + "/lyric" + "?id=" + id;
    }
}
