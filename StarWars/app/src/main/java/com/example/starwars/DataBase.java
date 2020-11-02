package com.example.starwars;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

public class DataBase extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "PlanetsDB.db";
    public static final String TABLE_NAME = "planets";
    public static final String COLUMN_ID = "idPlanet";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_GROUND = "ground";
    public static final String COLUMN_CLIMATE = "climate";


    public DataBase(Context context) {
        super(context, DATABASE_NAME , null, 1);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL( String.format("create table %s (%s integer primary key, %s text,%s text,%s text)",TABLE_NAME,COLUMN_ID,COLUMN_NAME,COLUMN_GROUND,COLUMN_CLIMATE));
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL( String.format("DROP TABLE IF EXISTS %s",TABLE_NAME));
        onCreate(db);
    }

    public boolean insertContact (Planetas p) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_NAME, p.getPlanete());
        contentValues.put(COLUMN_GROUND, p.getGround());
        contentValues.put(COLUMN_CLIMATE, p.getClimate());
        db.insert(TABLE_NAME, null, contentValues);
        return true;
    }

    public Cursor getData(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( String.format("select * from %s where id=%s", COLUMN_ID ), null);
        return res;
    }

    public Integer deleteContact (Integer id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_NAME,
                "id = ? ",
                new String[] { Integer.toString(id) });
    }

    public Integer deleteAll () {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_NAME,
                null,
                null);
    }

    public ArrayList<Planetas> getAllContacts() {
        ArrayList<Planetas> array_list = new ArrayList<Planetas>();

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( String.format("select * from %s",TABLE_NAME ),null );
        res.moveToFirst();

        while(res.isAfterLast() == false){
            array_list.add(new Planetas(res.getString(res.getColumnIndex(COLUMN_NAME)),res.getString(res.getColumnIndex(COLUMN_CLIMATE)),res.getString(res.getColumnIndex(COLUMN_GROUND))));
            res.moveToNext();
        }
        return array_list;
    }
}
