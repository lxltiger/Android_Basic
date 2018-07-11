package com.tiger.arch.sample.db;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import com.tiger.arch.sample.vo.Repo;

@Database(entities = {Repo.class}, exportSchema = false, version = 2)
public abstract class GithubDatabase extends RoomDatabase {

    private static GithubDatabase db;

    public abstract RepoDao repo();

    public static GithubDatabase instance(Context contex) {
        if (db == null) {
            synchronized (GithubDatabase.class) {
                if (db == null) {
                    db =Room.databaseBuilder(contex.getApplicationContext(), GithubDatabase.class, "github.db")
                            .allowMainThreadQueries().build();
                }
            }
        }

        return db;

    }
}
