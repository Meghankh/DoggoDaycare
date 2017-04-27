package com.doggo.doggydaycare.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class StepCountDB extends SQLiteOpenHelper
{
    private static final String DATABASE_NAME = "steps.db";
    private static final int DATABASE_VERSION = 1;

    // creates a table for the local database that will save steps
    // TODO: look inside DBConstants and create a string that will create a table with the correct datatypes.
    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + DBConstants.TABLE_NAME + " (\n" +
                    "    " + DBConstants.COLUMN_NAME_ENTRY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,\n" +
                    "    " + DBConstants.COLUMN_NAME_ENTRY_DATE + " TEXT NOT NULL,\n" +
                    "    " + DBConstants.COLUMN_NAME_ENTRY_DATE_EPOCH + " BIGINT,\n" +
                    "    " + DBConstants.COLUMN_NAME_TOTAL_VALUE + " INT,\n" +
                    "    " + DBConstants.COLUMN_NAME_INCREMENT_VALUE + " SMALLINT,\n" +
                    "    " + DBConstants.COLUMN_NAME_STATUS + " BOOLEAN\n" +
                    ");";

    // This constructor requires Context
    public StepCountDB(Context context)
    {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // this method will execute SQL_CREATE_ENTRIES to create the table with all the rows
    @Override
    public void onCreate(SQLiteDatabase db)
    {
        db.execSQL(SQL_CREATE_ENTRIES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        // we don't wanna do upgrades for this simple db
    }
}
