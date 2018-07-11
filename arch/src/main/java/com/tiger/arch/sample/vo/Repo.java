package com.tiger.arch.sample.vo;

import android.arch.persistence.room.Embedded;
import android.arch.persistence.room.Entity;
import android.support.annotation.NonNull;

import com.google.gson.annotations.SerializedName;
import com.tiger.arch.sample.utils.Objects;


/**
 * SerializedName 对应json里面的字段
 */
@Entity(primaryKeys = {"name", "owner_login"})
public class Repo {

    private int id;
    @NonNull
    private String name;
    @SerializedName("full_name")
    private String fullName;
    private String description;
    private String url;
    @SerializedName("stargazers_count")
    private int stars;
    @NonNull
    @Embedded(prefix = "owner_")
    private Owner owner;

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

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public Owner getOwner() {
        return owner;
    }

    public void setOwner(Owner owner) {
        this.owner = owner;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getStars() {
        return stars;
    }

    public void setStars(int stars) {
        this.stars = stars;
    }

    public static class Owner {

        @NonNull
        private String login;
        private String url;
        public String getLogin() {
            return login;
        }

        public void setLogin(String login) {
            this.login = login;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }



         @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Owner owner = (Owner) o;
            return Objects.equals(login, owner.login) &&
                    Objects.equals(url, owner.url);
        }

        @Override
        public int hashCode() {
            return Objects.hash(login, url);
        }
    }


}