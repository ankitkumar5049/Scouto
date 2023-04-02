package com.example.scouto.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class Database extends SQLiteOpenHelper {

    public static final String Dbname = "Login.db";

    public Database(@Nullable Context context) {
        super(context, "Login.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase MyDB) {
        MyDB.execSQL("CREATE TABLE users(username TEXT, mobile TEXT PRIMARY KEY, email TEXT , password TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase MyDB, int oldVersion, int newVersion) {
        MyDB.execSQL("drop table if exists users");
    }

    public Boolean insertData(String username, String mobile, String email, String password){
        SQLiteDatabase MyDB = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("username",username);
        contentValues.put("mobile",mobile);
        contentValues.put("email",email);
        contentValues.put("password",password);
        long results = MyDB.insert("users",null,contentValues);

        if(results==-1)
            return  false;
        else
            return true;

    }

    public Boolean checkUserMobile(String mobile){
        SQLiteDatabase MyDB = this.getWritableDatabase();
        Cursor cursor = MyDB.rawQuery("Select * from users where mobile = ?", new String[] {mobile});
        if ((cursor.getCount()>0))
            return true;
        else
            return false;
    }

    public Boolean checkUserEmail(String email){
        SQLiteDatabase MyDB = this.getWritableDatabase();
        Cursor cursor = MyDB.rawQuery("Select * from users where email = ?", new String[] {email});
        if ((cursor.getCount()>0))
            return true;
        else
            return false;
    }

    public Boolean checkUserPass2(String email,String password){
        SQLiteDatabase MyDB = this.getWritableDatabase();
        Cursor cursor = MyDB.rawQuery("Select * from users where email = ? and password = ?", new String[] {email,password});
        if ((cursor.getCount()>0))
            return true;
        else
            return false;
    }

    public Boolean checkUserPass1(String mobile, String password){
        SQLiteDatabase MyDB = this.getWritableDatabase();
        Cursor cursor = MyDB.rawQuery("Select * from users where mobile = ? and password = ?", new String[] {mobile,password});
        if ((cursor.getCount()>0))
            return true;
        else
            return false;
    }

    public Cursor getData(String mobile){
        SQLiteDatabase db = this.getWritableDatabase();
        String sql = "Select * from users where mobile="+mobile;
        Cursor cursor = db.rawQuery(sql, null);
        return cursor;
    }


}
