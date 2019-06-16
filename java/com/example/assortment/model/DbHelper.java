package com.example.assortment.model;


import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

public class DbHelper extends SQLiteOpenHelper {

    public DbHelper(Context context, String name, SQLiteDatabase.CursorFactory factory,
                    int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL( "CREATE TABLE MISE_TBL (_id INTEGER PRIMARY KEY AUTOINCREMENT, item TEXT,root TEXT);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public void insert(String item,String root) {
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL( "INSERT INTO MISE_TBL VALUES(null, " +
                "'" + item + "','"+root+"');");
        db.close();
    }



    public void delete(String item) {
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("DELETE FROM MISE_TBL WHERE item='" + item + "';");
        db.close();
    }
    public void delete(int idx) {
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("DELETE FROM MISE_TBL WHERE _id =" + idx + ";");
        db.close();
    }
    public ArrayList<MiseModel> getResult() {
        SQLiteDatabase db = getReadableDatabase();
        String result = "";

        Cursor cursor = db.rawQuery("SELECT * FROM MISE_TBL", null);
        ArrayList<MiseModel> mise = new ArrayList<>();
        while (cursor.moveToNext()) {
            MiseModel miseModel = new MiseModel(cursor.getInt(0),cursor.getString(1),cursor.getString(2));
            mise.add(miseModel);
            Log.d("<TAG>>",miseModel.item +  " ,  " + miseModel.getRoot());
        }

        return mise;
    }

    public class MiseModel{
        int idx;
        String item;
        String root;

        public MiseModel(int idx, String item, String root) {
            this.idx = idx;
            this.item = item;
            this.root = root;
        }

        public String getRoot() {
            return root;
        }

        public void setRoot(String root) {
            this.root = root;
        }

        public MiseModel(int idx, String item) {
            this.idx = idx;
            this.item = item;
        }

        public int getIdx() {
            return idx;
        }

        public void setIdx(int idx) {
            this.idx = idx;
        }

        public String getItem() {
            return item;
        }

        public void setItem(String item) {
            this.item = item;
        }
    }
}

