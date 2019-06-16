package com.example.assortment;

import android.app.Application;

import com.example.assortment.model.DbHelper;

public class BaseApp extends Application {
    private DbHelper dbHelper;

    @Override
    public void onCreate() {
        super.onCreate();
        dbHelper = new DbHelper(this,getPackageName(),null,1);
    }

    public DbHelper getDbHelper() {
        return dbHelper;
    }

    public void setDbHelper(DbHelper dbHelper) {
        this.dbHelper = dbHelper;
    }
}
