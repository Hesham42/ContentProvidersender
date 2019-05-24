package com.example.contentproviderenter;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import org.jetbrains.annotations.Nullable;

public class DBHelper extends SQLiteOpenHelper {

    //Constants for db name and version
    private static final String DATABASE_NAME = "contacts.db";
    private static final int DATABASE_VERSION = 1;

    //Constants for table and columns
    public static final String TABLE_CONTACTS = "contacts";
    public static final String CONTACT_ID = "_id";
    public static final String CONTACT_NAME = "myName";
    public static final String CONTACT_AGE = "myAge";
    public static final String CONTACT_CREATED_ON = "contactCreationTimeStamp";

    public static final String[] ALL_COLUMNS =
            {CONTACT_ID,CONTACT_NAME,CONTACT_AGE,CONTACT_CREATED_ON};

    //Create Table
    private static final String CREATE_TABLE =
            "CREATE TABLE " + TABLE_CONTACTS + " (" +
                    CONTACT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    CONTACT_NAME + " TEXT, " +
                    CONTACT_AGE + " INTEGER, " +
                    CONTACT_CREATED_ON + " TEXT default CURRENT_TIMESTAMP" +
                    ")";

    public DBHelper(Context context, String name,SQLiteDatabase.CursorFactory factory, int version) {
        super(context, DATABASE_NAME, factory, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
//    db.execSQL("create table myTable(myName text,myAge number)");
        db.execSQL(CREATE_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+ TABLE_CONTACTS);
        onCreate(db);
    }
}
