package com.lxl.yuer.advance.photopicker;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2015/12/10.
 * 相片目录
 */
public class PhotoDirectory {
    private String id;
    private String name;
    private String coverPath;
    private List<Photo> mPhotos = new ArrayList<>();

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCoverPath() {
        return coverPath;
    }

    public void setCoverPath(String coverPath) {
        this.coverPath = coverPath;
    }

    public List<Photo> getPhotos() {
        return mPhotos;
    }

    public void setPhotos(List<Photo> photos) {
        mPhotos = photos;
    }

    public void addPhoto(Photo photo) {
        mPhotos.add(photo);
    }
}
