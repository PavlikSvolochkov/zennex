package ru.zennex.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DBHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "CAT_DB";
    public static final String TABLE_NAME = "CAT_TABLE";
    public static final String COLUMN_NAME = "CAT_NAME";

    private final String[] catNames = new String[]{"Рыжик", "Барсик", "Мурзик",
            "Мурка", "Васька", "Томасина", "Кристина", "Пушок", "Дымка",
            "Кузя", "Китти", "Масяня", "Симба"};

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.d("LOG", "--- onCreate database ---");
        db.execSQL("create table " + TABLE_NAME + "("
                + "id integer primary key autoincrement, "
                + COLUMN_NAME + " text);");
//        createData();
    }

    public void createData() {

        ContentValues contentValues = new ContentValues();

        for (String catName : catNames) {
            contentValues.put("catName", catName);
            getWritableDatabase().insert(TABLE_NAME, null, contentValues);
            Log.d("LOG", contentValues.toString());
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}