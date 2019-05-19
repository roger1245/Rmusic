package com.lj.rmusic.widget.lrc;

import android.text.TextUtils;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class LrcRow implements Comparable<LrcRow> {
    private String endTime;

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    private static final String TAG = "LrcRow";

    /**
     * 开始时间 为00:10:00
     ***/
    private String timeStr;
    /**
     * 开始时间 毫米数  00:10:00  为10000
     **/
    private int time;
    /**
     * 歌词内容
     **/
    private String content;
    /**
     * 该行歌词显示的总时间
     **/
    private int totalTime;

    public long getTotalTime() {
        return totalTime;
    }

    public void setTotalTime(int totalTime) {
        this.totalTime = totalTime;
    }

    public String getTimeStr() {
        return timeStr;
    }

    public void setTimeStr(String timeStr) {
        this.timeStr = timeStr;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public LrcRow() {
        super();
    }

    public LrcRow(String timeStr, int time, String content) {
        super();
        this.timeStr = timeStr;
        this.time = time;
        this.content = content;
    }

    /**
     * 将歌词文件中的某一行 解析成一个List<LrcRow>
     * 因为一行中可能包含了多个LrcRow对象
     * 比如  [03:33.02][00:36.37]当鸽子不再象征和平  ，就包含了2个对象
     *
     * @param lrcLine
     * @return
     */
    public static final List<LrcRow> createRows(String lrcLine, String preLineTime) {
//        Log.d(TAG, preLineTime + "   " + lrcLine);
//        if (!lrcLine.startsWith("[") || lrcLine.indexOf("]") != 10) {
//            return null;
//        }
        //最后一个"]"
        int lastIndexOfRightBracket = lrcLine.lastIndexOf("]");
        //歌词内容
        String content = lrcLine.substring(lastIndexOfRightBracket + 1, lrcLine.length());
        //截取出歌词时间，并将"[" 和"]" 替换为"-"   [offset:0]
//        System.out.println("lrcLine=" + lrcLine);
        // -03:33.02--00:36.37-

        String nexTime = lrcLine.substring(0, lastIndexOfRightBracket + 1).replace("[", "-").replace("]", "-");
        String preTime = preLineTime;
        String nnexTime = nexTime.substring(0, 9) + "-";
        String npreTime = preTime.substring(0, 9) + "-";
        String times = (nnexTime).replace("[", "-").replace("]", "-");;


        times = times.substring(1, times.length() - 1);
        Log.d(TAG, times);
//        String[] timesArray = times.split("-");
        List<LrcRow> lrcRows = new ArrayList<LrcRow>();
        LrcRow lrcRow = new LrcRow(times, formatTime(times), content);
        lrcRows.add(lrcRow);
//        for (String tem : timesArray) {
//            if (TextUtils.isEmpty(tem.trim())) {
//                continue;
//            }
////            Log.d(TAG, tem);
//            //
//            try {
//                LrcRow lrcRow = new LrcRow(tem, formatTime(tem), content);
//                Log.d(TAG, lrcRow.toString());
//                lrcRows.add(lrcRow);
//            } catch (Exception e) {
////                Log.w("LrcRow", e.getMessage());
//            }

        return lrcRows;
    }

    /****
     * 把歌词时间转换为毫秒值  如 将00:10.00  转为10000
     *
     * @param tem
     * @return
     */
    private static int formatTime(String timeStr) {
        timeStr = timeStr.replace('.', ':');
        String[] times = timeStr.split(":");

        return Integer.parseInt(times[0]) * 60 * 1000
                + Integer.parseInt(times[1]) * 1000
                + Integer.parseInt(times[2]);
    }

    @Override
    public int compareTo(LrcRow anotherLrcRow) {
        return (int) (this.time - anotherLrcRow.time);
    }

    @Override
    public String toString() {
        return "LrcRow [timeStr=" + timeStr + ", time=" + time + ", content="
                + content + "]";
    }


}
