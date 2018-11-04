package com.smartdevsolutions.ilottoandroid.Data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.HashMap;

public class DBHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "GCL_LOTTO.db";
    public static final String CONFIGURATION_TABLE = "configuration";
    public static final String CONTACTS_COLUMN_ID = "id";
    public static final String CONFIGURATION_KEY = "key";
    public static final String CONFIGURATION_DESCRIPTION = "description";
    public static final String CONFIGURATION_VALUE = "value";

    private HashMap hp;

    public DBHelper(Context context) {
        super(context, DATABASE_NAME , null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // TODO Auto-generated method stub
        db.execSQL(
                "create table configuration " +
                        "(id integer primary key,[key] text,description text,value text)"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO Auto-generated method stub
        db.execSQL("DROP TABLE IF EXISTS configuration");
        onCreate(db);
    }

    public boolean insertConfiguration (String key, String value, String description) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        if(iskeyavailable(key)==  false) {
            contentValues.put("[key]", key);
            contentValues.put("description", description);
            contentValues.put("value", value);

            db.insert("configuration", null, contentValues);
            return true;

        }
        else {
            return false;
        }
    }


    public boolean iskeyavailable(String key) {
        Cursor res = getData(key);
        res.moveToFirst();
       return !res.isAfterLast() ;
    }

    public Cursor getData(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from configuration where id="+id+"", null );
        return res;
    }

    public Cursor getData(String key) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from configuration where [key]='"+key+"'", null );
        return res;
    }

    public int numberOfRows(){
        SQLiteDatabase db = this.getReadableDatabase();
        int numRows = (int) DatabaseUtils.queryNumEntries(db, CONFIGURATION_TABLE);
        return numRows;
    }

    public boolean updateConfiguration (String key, String value, String description) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        Cursor findexistingrecord = getData(key);
        if (findexistingrecord!= null ) {
            //contentValues.put("key", key);
            if(description!= null && !description.isEmpty())
                contentValues.put("description", description);
            contentValues.put("value", value);

            db.update("configuration", contentValues, "[key] = ? ", new String[]{key});
            return true;
        }
        else{
            return false;
        }
    }

    public int deleteConfigurataion (String key) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete("configuration",
                "[key] = ? ",
                new String[] { key });
    }

    public ArrayList<String> getAllConfigurations() {
        ArrayList<String> array_list = new ArrayList<String>();

        //hp = new HashMap();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from configuration", null );
        res.moveToFirst();

        while(res.isAfterLast() == false){
            array_list.add(res.getString(res.getColumnIndex(CONFIGURATION_KEY)));
            res.moveToNext();
        }
        return array_list;
    }
}
