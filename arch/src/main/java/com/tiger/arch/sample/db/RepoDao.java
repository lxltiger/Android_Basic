package com.tiger.arch.sample.db;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import com.tiger.arch.sample.vo.Repo;

import java.util.List;

@Dao
public interface RepoDao {


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertRepo(List<Repo> list);

    @Query("select * from Repo")
    LiveData<List<Repo>> loadRepos();
}
