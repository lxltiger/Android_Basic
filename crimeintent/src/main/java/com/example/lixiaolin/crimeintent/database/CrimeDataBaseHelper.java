package com.example.lixiaolin.crimeintent.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.example.lixiaolin.crimeintent.database.CrimeDbSchema.CrimeTable;
import com.example.lixiaolin.crimeintent.database.CrimeDbSchema.Columns;
/**
 * Created by lixiaolin on 15/10/21.
 */
public class CrimeDataBaseHelper extends SQLiteOpenHelper{
    private static final String DATABASE_NAME = "crimeBase.db";
    private static final int VERSION = 2;
    private static final String sql = "create table "+ CrimeTable.NAME+
            "( _id integer primary key autoincrement,"+
            Columns.UUID+" ,"+
            Columns.TITLE+" ,"+
            Columns.DATE+" ,"+
            Columns.SOLVED+" ,"+
            Columns.SUSPECT+")";

    public CrimeDataBaseHelper(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion != newVersion) {
            db.execSQL("drop table if exists "+CrimeTable.NAME);
            onCreate(db);
        }
    }
}
