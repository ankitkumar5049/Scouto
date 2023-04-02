package com.example.scouto.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.example.scouto.model.Car;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {
    public DatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table cars(name varchar(250), model varchar(250))");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("drop table if exists cars");
        onCreate(db);
    }
    public long saveCarData(String name, String model){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("name", name);
        cv.put("model", model);
        return db.insert("cars",null, cv);
    }
    public long deleteCarData(String make){
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete("cars", "name=?", new String[]{make});
    }
    public ArrayList<Car> getRecords(){
        ArrayList<Car> list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("Select distinct * from cars", null);
        while(cursor.moveToNext()){
            String name = cursor.getString(0);
            String model = cursor.getString(1);
            list.add(new Car(name, model));
        }
        return list;
    }
}
