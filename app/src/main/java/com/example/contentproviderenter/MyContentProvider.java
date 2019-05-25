package com.example.contentproviderenter;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

public class MyContentProvider extends ContentProvider {

    private DBHelper helper;
    private static final String AUTHORITY = "com.guinness.own.PROVIDER";
    private static final String BASE_PATH = "contacts";
    public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + BASE_PATH );

    private static final int CONTACTS = 1;
    private static final int CONTACT_ID = 2;
    private static final UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
    static {
        uriMatcher.addURI(AUTHORITY,BASE_PATH, CONTACTS);
        uriMatcher.addURI(AUTHORITY,BASE_PATH + "/#",CONTACT_ID);
    }
    private SQLiteDatabase database;


    public MyContentProvider() {
    }


    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        int delCount = 0;
        switch (uriMatcher.match(uri)) {
            case CONTACTS:
                delCount =  database.delete(DBHelper.TABLE_CONTACTS,selection,selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("This is an Unknown URI " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return delCount;
    }

    @Override
    public String getType(Uri uri) {
        switch (uriMatcher.match(uri)) {
            case CONTACTS:
                return "vnd.android.cursor.dir/contacts";
            default:
                throw new IllegalArgumentException("This is an Unknown URI " + uri);
        }

    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        long id = database.insert(DBHelper.TABLE_CONTACTS,
                null,
                values);
        if (id > 0) {
            Uri _uri = ContentUris.withAppendedId(CONTENT_URI, id);
            sort();
            getContext().getContentResolver().notifyChange(_uri, null);
            return _uri;
        }
        throw new SQLException("Insertion Failed for URI :" + uri);

    }

    @Override
    public boolean onCreate() {
        // TODO: Implement this to initialize your content provider on startup.
          helper = new DBHelper(getContext(),null,null,1);
          database = helper.getWritableDatabase();
            sort();
          return true;
    }

    private void sort() {
        database.rawQuery( "select * from "+DBHelper.TABLE_CONTACTS+" ORDER BY _id DESC", null);

    }

    
    @Override
    public Cursor query(Uri uri,
                        String[] projection,
                        String selection,
                        String[] selectionArgs,
                        String sortOrder ) {

            Cursor cursor;

            switch (uriMatcher.match(uri)) {
                case CONTACTS:
                    cursor = database.query(
                            DBHelper.TABLE_CONTACTS,
                            DBHelper.ALL_COLUMNS,
                            selection, null, null, null,
                            DBHelper.CONTACT_NAME + " ASC");
                    break;
                default:
                    throw new IllegalArgumentException("This is an Unknown URI " + uri);

            }
        cursor.setNotificationUri(getContext().getContentResolver(),uri);
//        helper.getReadableDatabase().query(DBHelper.TABLE_CONTACTS,projection,selection,selectionArgs,null,null,sortOrder);

        return cursor;
    }


    @Override
    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        int updCount = 0;
        switch (uriMatcher.match(uri)) {
            case CONTACTS:
                updCount =  database.
                        update(
                        DBHelper.TABLE_CONTACTS,
                        values,
                        selection,
                        selectionArgs);
                sort();
                break;
            default:
                throw new IllegalArgumentException("This is an Unknown URI " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return updCount;
        }

    }

