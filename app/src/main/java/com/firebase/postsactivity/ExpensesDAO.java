package com.firebase.postsactivity;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by eunoiatechnologies on 15/09/16.
 */

public class ExpensesDAO extends AbstaractDAO implements Constants {
    public ExpensesDAO(Context context) {
        super(context);
    }

    private String TAG = "ExpensesDAO.java";

    public long insert(PostsModel postsModel) {
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put(COLUMN_POST_TITLE, postsModel.getTitle());
            values.put(COLUMN_POST_IMG, postsModel.getImgUrl());
            values.put(COLUMN_POST_DESCRIPTION,postsModel.getDescription());
            long id = db.insert(TABLE_POSTS, null, values);
            closeDatabase();
            return id;
        } catch (Exception e) {

            closeDatabase();
            return 0;
        }
    }


    public List<PostsModel> getData()
    {
        try
        {
            String sql = "select * from " + TABLE_POSTS;
            Log.i(TAG,"sql"+sql);
            SQLiteDatabase db = this.getReadableDatabase();
            Cursor cursor = db.rawQuery(sql,null);
            Log.i(TAG,"cursor size"+cursor.getCount());
            List<PostsModel> mapList =new ArrayList<>();
            if (cursor.moveToFirst()) {

                do {

                    PostsModel postsModel = new PostsModel();
                    postsModel.setTitle(cursor.getString(cursor.getColumnIndex(COLUMN_POST_TITLE)));
                    postsModel.setImgUrl(cursor.getString(cursor.getColumnIndex(COLUMN_POST_IMG)));
                    postsModel.setDescription(cursor.getString(cursor.getColumnIndex(COLUMN_POST_DESCRIPTION)));
                    postsModel.setPostedtime(cursor.getString(cursor.getColumnIndex(COLUMN_POST_TIME)));
                    mapList.add(postsModel);
                }while (cursor.moveToNext());
            }
            cursor.close();
            closeDatabase();
            return mapList;
        }catch (Exception e) {

            closeDatabase();
            return null;
        }

    }


    public String getCurrentDate()
    {
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        //get current date time with Date()
        Date date = new Date();
        System.out.println(dateFormat.format(date));
        String result = dateFormat.format(date);
        return result;
    }




}