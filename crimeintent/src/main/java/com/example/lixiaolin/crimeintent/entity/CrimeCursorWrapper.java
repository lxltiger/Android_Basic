package com.example.lixiaolin.crimeintent.entity;

import android.database.Cursor;
import android.database.CursorWrapper;

import com.example.lixiaolin.crimeintent.database.CrimeDbSchema.Cols;

import java.util.Date;
import java.util.UUID;

/**
 * Created by lixiaolin on 15/10/21.
 */
public class CrimeCursorWrapper extends CursorWrapper{
    /**
     * Creates a cursor wrapper.
     *
     * @param cursor The underlying cursor to wrap.
     */
    public CrimeCursorWrapper(Cursor cursor) {
        super(cursor);
    }

    public Crime getCrime() {
        String uuid=getString(Cols.UUID);
        String title=getString(Cols.TITLE);
        long date=getLong(Cols.DATE);
        int solved=getInt(Cols.SOLVED);
        Crime crime = new Crime(UUID.fromString(uuid));
        crime.setTitle(title);
        crime.setDate(new Date(date));
        crime.setIsSolved(solved != 0);
        crime.setSuspect(getString(Cols.SUSPECT));
        return crime;

    }
}
