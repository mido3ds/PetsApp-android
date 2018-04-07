package com.example.android.pets.Data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class PetOpenHelper extends SQLiteOpenHelper {
    private final static String NAME = "pets.db";
    private final static int VERSION = 2;

    private final static String SQLITE_CREATE_TABLE = "CREATE TABLE " +
            PetContract.PetEntry.TABLE_NAME +
            " (" +
            PetContract.PetEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
            PetContract.PetEntry.COLUMN_PET_NAME + " TEXT NOT NULL," +
            PetContract.PetEntry.COLUMN_PET_BREED + " TEXT," +
            PetContract.PetEntry.COLUMN_PET_GENDER + " INTEGER NOT NULL," +
            PetContract.PetEntry.COLUMN_PET_WEIGHT + " INTEGER NOT NULL" +
            ");";
    private final static String SQLIT_DROP_TABLE = "DROP TABLE " + PetContract.PetEntry.TABLE_NAME + ";";

    public PetOpenHelper(Context context) {
        super(context, NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(SQLITE_CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL(SQLIT_DROP_TABLE);
        onCreate(sqLiteDatabase);
    }
}
