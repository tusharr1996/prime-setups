package com.tushar.primesetups.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.tushar.primesetups.models.Result;


import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {


    public static String DATABASE = "setupdb.db";
    public static String TABLE ="setups";
    public static String id ="id";
    public static String setup_name ="setup_name";
    public static String image_url ="image_url";
    public static String wall_name ="wall_name";
    public static String wall_url ="wall_url";
    public static String icon_name ="icon_name";
    public static String icon_url ="icon_url";
    public static String launcher_name ="launcher_name";
    public static String launcher_url ="launcher_url";
    public static String wid_name ="wid_name";
    public static String wid_url ="wid_url";
    public static String bc_url ="bc_url";
    public static String dev_name ="dev_name";
    public static String down_count ="down_count";
    public static String notes ="note";
    String br;

    public DatabaseHelper(Context context) {
        super(context, DATABASE, null, 1);

    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        br = "CREATE TABLE "+TABLE+"("+id+ " integer primary key autoincrement , "+setup_name+ " Text, "+image_url+ " Text, "+wall_name+ " Text, "+wall_url+ " Text, "+icon_name+ " Text, "+icon_url+ " Text, "+launcher_name+ " Text, "+launcher_url+ " Text, "+wid_name+ " Text, "+wid_url+ " Text, "+bc_url+ " Text, "+dev_name+ " Text, "+down_count+ " Text, "+notes+ " Text);";
        db.execSQL(br);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+TABLE+" ;");
    }

    public void droptable(){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DROP TABLE IF EXISTS "+TABLE+" ;");
        br = "CREATE TABLE "+TABLE+"("+id+ " integer primary key autoincrement , "+setup_name+ " Text, "+image_url+ " Text, "+wall_name+ " Text, "+wall_url+ " Text, "+icon_name+ " Text, "+icon_url+ " Text, "+launcher_name+ " Text, "+launcher_url+ " Text, "+wid_name+ " Text, "+wid_url+ " Text, "+bc_url+ " Text, "+dev_name+ " Text, "+down_count+ " Text, "+notes+ " Text);";
        db.execSQL(br);
    }

    public void insertdata(String name,String company ,String city,String country){
        System.out.print("Hello "+br);
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues= new ContentValues();


        //contentValues.put(NAME, name);
        //contentValues.put(COMPANY, company);
      //  contentValues.put(CITY,city);
    //    contentValues.put(COUNTRY,country);
        db.insert(TABLE,null,contentValues);


    }

  public  void addJson(JSONObject json) throws JSONException {
        SQLiteDatabase db = this.getReadableDatabase();
        ContentValues values = new ContentValues();
        try {
            values.put(setup_name, json.getString(setup_name));
            values.put(image_url, json.getString(image_url));
            values.put(wall_name, json.getString(wall_name));
            values.put(wall_url, json.getString(wall_url));
            values.put(icon_name, json.getString(icon_name));
            values.put(icon_url, json.getString(icon_url));
            values.put(launcher_name, json.getString(launcher_name));
            values.put(launcher_url, json.getString(launcher_url));
            values.put(wid_name, json.getString(wid_name));
            values.put(wid_url, json.getString(wid_url));
            values.put(bc_url, json.getString(bc_url));
            values.put(dev_name, json.getString(dev_name));
            values.put(down_count, json.getString(down_count));
            values.put(notes, json.getString(notes));



        } catch (JSONException e) {
            // TODO Auto-generated catch block
            System.out.println("JsonException........");
            e.printStackTrace();
        }

    db.insert(TABLE, null, values);
    db.close();

//        db.insertOrThrow(TABLE,null,values);

    }

//public StringBuffer getdata(){
//    SQLiteDatabase db = this.getWritableDatabase();
//    Cursor cursor = db.rawQuery("select * from "+TABLE+" ;",null);
//    StringBuffer stringBuffer = new StringBuffer();
//    String a;
//    if (cursor.moveToFirst()) {
//        do {
//            stringBuffer.append(cursor.getString(cursor.getColumnIndexOrThrow("setup_name")));
//        } while (cursor.moveToNext());
//    }
//
//
//
//        return stringBuffer;
//}

    public List<Result> getdata(){

        List<Result> data=new ArrayList<>();
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("select * from "+TABLE+" ;",null);
        StringBuffer stringBuffer = new StringBuffer();
        Result Result = null;
        ColumnIndexCache cache = new ColumnIndexCache();
        while (cursor.moveToNext()) {
            Result= new Result();

            Result.setId(cursor.getString(cursor.getColumnIndexOrThrow(id)));
            Result.setSetup_name(cursor.getString(cursor.getColumnIndexOrThrow(setup_name)));
            Result.setImage_url(cursor.getString(cursor.getColumnIndexOrThrow(image_url)));
            Result.setWall_name(cursor.getString(cursor.getColumnIndexOrThrow(wall_name)));
            Result.setWall_url(cursor.getString(cursor.getColumnIndexOrThrow(wall_url)));
            Result.setIcon_name(cursor.getString(cursor.getColumnIndexOrThrow(icon_name)));
            Result.setIcon_url(cursor.getString(cursor.getColumnIndexOrThrow(icon_url)));
            Result.setLauncher_name(cursor.getString(cursor.getColumnIndexOrThrow(launcher_name)));
            Result.setLauncher_url(cursor.getString(cursor.getColumnIndexOrThrow(launcher_url)));
            Result.setWid_name(cursor.getString(cursor.getColumnIndexOrThrow(wid_name)));
            Result.setWid_url(cursor.getString(cursor.getColumnIndexOrThrow(wid_url)));
            Result.setBc_url(cursor.getString(cursor.getColumnIndexOrThrow(bc_url)));
            Result.setDev_name(cursor.getString(cursor.getColumnIndexOrThrow(dev_name)));

            Result.setNote(cursor.getString(cursor.getColumnIndexOrThrow(notes)));
            stringBuffer.append(Result);
            // stringBuffer.append(dataModel);
            data.add(Result);
        }

//        for (Result mo:data ) {
//
//            Log.i("Hellomo",""+mo.getSetup_name());
//        }

        //

        return data;
    }



}
