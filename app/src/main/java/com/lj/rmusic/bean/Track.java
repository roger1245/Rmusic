package com.lj.rmusic.bean;

import java.util.List;

public class Track {
    private String name;
    private int id;
    private List<Ar> ars;

    @Override
    public String toString() {
        return "Track{" +
                "name='" + name + '\'' +
                ", id=" + id +
                ", ars=" + ars +
                ", al=" + al +
                '}';
    }

    public Track(String name, int id, List<Ar> ars, Al al) {
        this.name = name;
        this.id = id;
        this.ars = ars;
        this.al = al;
    }

    public List<Ar> getArs() {
        return ars;
    }

    public void setArs(List<Ar> ars) {
        this.ars = ars;
    }

    private Al al;



    public Al getAl() {
        return al;
    }

    public void setAl(Al al) {
        this.al = al;
    }

    public String getName() {
        return name;
    }



    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public static class Ar {
        @Override
        public String toString() {
            return "Ar{" +
                    "id=" + id +
                    ", name='" + name + '\'' +
                    '}';
        }

        public int getId() {
            return id;

        }

        public void setId(int id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        private int id;
        private String name;

    }

    public static class Al {
        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getPicUrl() {
            return picUrl;
        }

        public void setPicUrl(String picUrl) {
            this.picUrl = picUrl;
        }

        private int id;
        //所在专辑名字
        private String name;
        private String picUrl;

        @Override
        public String toString() {
            return "Al{" +
                    "id=" + id +
                    ", name='" + name + '\'' +
                    ", picUrl='" + picUrl + '\'' +
                    '}';
        }
    }
}
