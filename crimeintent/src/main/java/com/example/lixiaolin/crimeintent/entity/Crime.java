package com.example.lixiaolin.crimeintent.entity;

import java.util.Date;
import java.util.UUID;

/**
 * Created by lixiaolin on 15/10/18.
 */
public class Crime {
    private UUID mUUID;
    private String mTitle;
    private Date mDate;
    private boolean isSolved;
    private String mSuspect;

    public Crime() {
        this(UUID.randomUUID());
    }

    public Crime(UUID uuid) {
        mUUID=uuid;
        mDate = new Date();
    }

    public UUID getUUID() {
        return mUUID;
    }
    public String getTitle() {
        return mTitle;
    }


    public void setTitle(String title) {
        mTitle = title;
    }

    public Date getDate() {
        return mDate;
    }

    public void setDate(Date date) {
        mDate = date;
    }

    public boolean isSolved() {
        return isSolved;
    }

    public void setIsSolved(boolean isSolved) {
        this.isSolved = isSolved;
    }

    public String getSuspect() {
        return mSuspect;
    }

    public void setSuspect(String suspect) {
        mSuspect = suspect;
    }

    public String  getFileName() {
        return "IMG_" + getUUID() + ".jpg";
    }
}
