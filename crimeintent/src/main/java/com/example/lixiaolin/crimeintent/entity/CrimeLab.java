package com.example.lixiaolin.crimeintent.entity;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;

import com.example.lixiaolin.crimeintent.database.CrimeDataBaseHelper;
import com.example.lixiaolin.crimeintent.database.CrimeDbSchema;
import com.example.lixiaolin.crimeintent.database.CrimeDbSchema.Columns;
import com.example.lixiaolin.crimeintent.database.CrimeDbSchema.CrimeTable;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by lixiaolin on 15/10/18.
 */
public class CrimeLab {
    private static CrimeLab mCrimeLab;
    private SQLiteDatabase mSQLiteDatabase;
    private Context mContext;
    private CrimeLab(Context context) {
        mContext=context.getApplicationContext();
        mSQLiteDatabase = new CrimeDataBaseHelper(context.getApplicationContext()).getWritableDatabase();
    }

    public static CrimeLab getInstance(Context context) {
        if (mCrimeLab == null) {
            mCrimeLab = new CrimeLab(context.getApplicationContext());

        }
        return mCrimeLab;
    }

    private static ContentValues getContentValues(Crime crime) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(Columns.UUID, crime.getUUID().toString());
        contentValues.put(Columns.TITLE, crime.getTitle());
        contentValues.put(Columns.DATE, crime.getDate().getTime());
        contentValues.put(Columns.SOLVED, crime.isSolved() ? 1 : 0);
        contentValues.put(Columns.SUSPECT, crime.getSuspect());
        return contentValues;
    }

    public void addCrime(Crime crime) {
        ContentValues contentValues = getContentValues(crime);
        mSQLiteDatabase.insert(CrimeTable.NAME, null, contentValues);
    }

    public void updateCrime(Crime crime) {
        ContentValues contentValues = getContentValues(crime);
        mSQLiteDatabase.update(CrimeTable.NAME, contentValues, Columns.UUID +
                "=?", new String[]{crime.getUUID().toString()});

    }

    public CrimeCursorWrapper queryCrime(String whereClause, String[] whereArg) {
        Cursor cursor = mSQLiteDatabase.query(CrimeTable.NAME, null, whereClause, whereArg, null, null, null);
        return new CrimeCursorWrapper(cursor);
    }

    public Crime getCrime(UUID uuid) {
        CrimeCursorWrapper cursorWrapper = queryCrime(Columns.UUID + "=?", new String[]{uuid.toString()});
        try {
            if (cursorWrapper.getCount() == 0) {
                return null;
            }
            cursorWrapper.moveToFirst();
            return cursorWrapper.getCrime();
        } finally {
            cursorWrapper.close();
        }
    }

    public List<Crime> getCrime() {
        ArrayList<Crime> crimes = new ArrayList<>();
        CrimeCursorWrapper cursorWrapper = queryCrime(null, null);
        try {
            cursorWrapper.moveToFirst();
            while (!cursorWrapper.isAfterLast()) {
                crimes.add(cursorWrapper.getCrime());
                cursorWrapper.moveToNext();
            }
        } finally {
            cursorWrapper.close();
        }
        return crimes;
    }

    public File getPhotoFile(Crime crime) {
        //get the external storage directory
        File externalFilesDir = mContext.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
//        return null if the directory does not exist
        if (externalFilesDir == null) {
            return null;
        }
        // this does not create real file ,but a file object pointing to the right position
        return new File(externalFilesDir, crime.getFileName());
    }
}
