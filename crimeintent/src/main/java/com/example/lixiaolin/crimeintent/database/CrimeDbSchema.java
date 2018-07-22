package com.example.lixiaolin.crimeintent.database;

/**
 * Created by lixiaolin on 15/10/21.
 */
public class CrimeDbSchema {
    public static final class CrimeTable{
        public static final String NAME = "crimes";
    }
    public static final class Columns{
        public static final String UUID = "uuid";
        public static final String TITLE = "title";
        public static final String DATE = "date";
        public static final String SOLVED = "solved";
        public static final String SUSPECT = "suspect";
    }

    public static final class Cols{
        public static final int UUID=1;
        public static final int TITLE=2;
        public static final int DATE=3;
        public static final int SOLVED=4;
        public static final int SUSPECT=5;
    }
}
