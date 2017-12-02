package com.lxl.yuer.advance.photopicker;

/**
 * Created by Administrator on 2015/12/10.
 */
public class Photo {
    private int id;
    private String path;

    public Photo(int id, String path) {
        this.id = id;
        this.path = path;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}
