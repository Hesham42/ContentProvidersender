package com.example.contentproviderenter;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    DBHelper helper;
    EditText Name,Age,SearchAge;
    TextView txv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Name = (EditText) findViewById(R.id.Name);
        Age = (EditText)  findViewById(R.id.Age);
        SearchAge= (EditText)  findViewById(R.id.search);
        txv= (TextView) findViewById(R.id.textDBInfo);
        helper = new DBHelper(this,null,null,1);

    }




    public void btnInsert(View view) {
        InsertURI();
//        InsertQuereDb();
    }

    private void InsertQuereDb() {
        SQLiteDatabase db = helper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DBHelper.CONTACT_NAME,getName());
        values.put(DBHelper.CONTACT_AGE,getAge());
        db.insert(DBHelper.TABLE_CONTACTS,null,values);
        db.close();

    }

    private void InsertURI() {
        ContentValues values = new ContentValues();
        values.put(DBHelper.CONTACT_NAME,getName());
        values.put(DBHelper.CONTACT_AGE,getAge());
        Uri contactUri  = getContentResolver().insert(MyContentProvider.CONTENT_URI,values);
        Toast.makeText(this,"Created Contact " + getName(),Toast.LENGTH_LONG).show();

    }




    public void btnUpdate(View view) {
        ContentValues values = new ContentValues();
        values.put(DBHelper.CONTACT_NAME,getName());
        values.put(DBHelper.CONTACT_AGE,getAge());
        String whereClause= DBHelper.CONTACT_AGE +"= ?";
        String[] whereArgs={String.valueOf(getSearchAge())};

       int t  = getContentResolver().update(MyContentProvider.CONTENT_URI,values,
                whereClause,whereArgs);

       if (t==0){
            Log.i("guinness","Not Update Faild");

       }else {
           Log.i("guinness","Update done ");

       }
    }

    public void btnDelete(View view) {
//        DELETEQURE();
        DELETEURI();


    }

    private void DELETEURI() {
        String whereClause= DBHelper.CONTACT_AGE +"= ?";
        String[] whereArgs={String.valueOf(getSearchAge())};
        getContentResolver().delete(MyContentProvider.CONTENT_URI,
                whereClause,whereArgs);

        Toast.makeText(this,"All Contacts Deleted",Toast.LENGTH_LONG).show();

    }

    private void DELETEQURE() {
        SQLiteDatabase db = helper.getReadableDatabase();
        String table = DBHelper.TABLE_CONTACTS;
        String whereClause= DBHelper.CONTACT_AGE +"= ?";
        String[] whereArgs={String.valueOf(getSearchAge())};
        db.delete(table,whereClause,whereArgs);
        db.close();
    }

    public void btnDisplay(View view) {
        SQLiteDatabase db = helper.getReadableDatabase();
        String table = DBHelper.TABLE_CONTACTS;
        String[] columns = null;
        String selection= null;
        String[] selectionArgs= null;
        String groupBy= null;
        String having= null;
        String orderBy= null;
         Cursor cursor = db.query(
                table,
                columns,
                selection,
                selectionArgs,
                groupBy,
                having,
                orderBy
        );
        List<String> strings = new ArrayList<>();
        while (cursor.moveToNext()){
           String myName = cursor.getString(cursor.getColumnIndex(DBHelper.CONTACT_NAME));
           Integer myAge = cursor.getInt(cursor.getColumnIndex(DBHelper.CONTACT_AGE));
            Log.i("guinness","Name = "+myName +"  "+ "Age = "+myAge);
            strings.add("\n "+"Name = "+myName +" \n "+ "Age = "+myAge);
        }
        txv.setText(" "+strings.toString()+"\n ");


        db.close();

    }

    public void btnSearch(View view) {
        SQLiteDatabase db = helper.getReadableDatabase();

        String table = DBHelper.TABLE_CONTACTS;
        String[] columns = {DBHelper.CONTACT_NAME};
        String selection= DBHelper.CONTACT_AGE +"= ?";
        String[] selectionArgs= {""+getSearchAge()};
        String groupBy= null;
        String having= null;
        String orderBy= null;
        Cursor cursor = db.query(
                table,
                columns,
                selection,
                selectionArgs,
                groupBy,
                having,
                orderBy
        );
        List<String> strings = new ArrayList<>();
        while (cursor.moveToNext()){
            String myName = cursor.getString(cursor.getColumnIndex(DBHelper.CONTACT_NAME));
            Log.i("guinness","Name = "+myName +"  ");
            strings.add(myName);
        }
        txv.setText("Name = "+strings.toString()+"  ");


        db.close();
    }
    private Integer getAge() {
        return Integer.parseInt(Age.getText().toString());
    }

    private Integer getSearchAge() {
        return Integer.parseInt(SearchAge.getText().toString());
    }

    private String getName() {

        return Name.getText().toString();
    }


}
