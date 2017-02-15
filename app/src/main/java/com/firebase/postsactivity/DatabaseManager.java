package com.firebase.postsactivity;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by eunoiatechnologies on 15/09/16.
 */

public class DatabaseManager extends SQLiteOpenHelper implements Constants {
    private static DatabaseManager instance;
    private static SQLiteOpenHelper mDatabaseHelper;
    private static String TAG = "DatabaseManager";
    private SQLiteDatabase mDatabase;
    private Context context;
    private AtomicInteger mOpenCounter = new AtomicInteger();

    public DatabaseManager(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "CREATE TABLE IF NOT EXISTS " + TABLE_POSTS + " ("+ COLUMN_POST_TITLE + " TEXT, "+COLUMN_POST_IMG+" TEXT ,"+COLUMN_POST_DESCRIPTION+" TEXT,"+COLUMN_POST_TIME+" TEXT )";
        db.execSQL(sql);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
    public static synchronized DatabaseManager getInstance(Context context) {
        if (instance == null) {
            instance = new DatabaseManager(context);
        }
        return instance;
    }
    public synchronized SQLiteDatabase openDatabase() {
        if(mOpenCounter.incrementAndGet() == 1) {
            try{
                mDatabase = instance.getWritableDatabase();
            }catch(Exception e){
                Log.e(TAG,""+e);
            }
        }
        if (!mDatabase.isOpen())
        {
            try{
                mDatabase = instance.getWritableDatabase();
            }catch(Exception e){
                Log.e(TAG,""+e);
            }
        }
        return mDatabase;
    }

    public synchronized void closeDatabase() {
        if(mOpenCounter.decrementAndGet() == 0&&mDatabase.isOpen()) {
            try{
                mDatabase.close();
            }catch(Exception e){
                Log.e(TAG,""+e);
            }
        }
        //HLog.i(TAG,"After Closing Database "+mOpenCounter.get());
    }

    public synchronized void deleteDatabase(){
        //HLog.i(TAG,"Before Deleting Database "+mOpenCounter.get());
        try{
            context.deleteDatabase(DATABASE_NAME);
            mOpenCounter.set(0);
        }catch (Exception e) {

        }
        //HLog.i(TAG,"After Deleting Database "+mOpenCounter.get());
    }
}