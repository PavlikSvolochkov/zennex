package ru.zennex.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import ru.zennex.common.Cat;

public class CatDAO {

    private DBHelper dbHelper;
    private SQLiteDatabase database;

    public CatDAO(Context context) {
        dbHelper = new DBHelper(context);
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    public Cat createCat(String name) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(DBHelper.COLUMN_NAME, name);
        Long insertId = database.insert(DBHelper.TABLE_NAME, null, contentValues);
        Cursor cursor = database.query(DBHelper.TABLE_NAME, null, "id=" + insertId, null, null, null, null);
        cursor.moveToFirst();
        Cat cat = cursorToCat(cursor);
        cursor.close();
        return cat;
    }

    public List<Cat> getAllCats() {
        List<Cat> catList = new ArrayList<>();
        Cursor cursor = database.query(DBHelper.TABLE_NAME, null, null, null, null, null, null);
        cursor.moveToFirst();

        while (!cursor.isAfterLast()) {
            Cat cat = cursorToCat(cursor);
            catList.add(cat);
            cursor.moveToNext();
        }
        cursor.close();
        return catList;
    }

    public Cat getCat(Long id) {
        Cursor cursor = database.query(DBHelper.TABLE_NAME, null, null, null, null, null, null);
        cursor.moveToFirst();
        Cat cat = cursorToCat(cursor);
        cursor.close();
        return cat;
    }

    public void updateCat(Cat cat) {
        String byId = "id=" + cat.getId();
        ContentValues contentValues = new ContentValues();
        contentValues.put(DBHelper.COLUMN_NAME, cat.getName());
        database.update(DBHelper.TABLE_NAME, contentValues, byId, null);
    }

    public void deleteCat(Long id) {
        String byId = "id=" + id;
        database.delete(DBHelper.TABLE_NAME, byId, null);
    }

    private Cat cursorToCat(Cursor cursor) {
        Cat cat = new Cat();
        cat.setId(cursor.getLong(0));
        cat.setName(cursor.getString(1));
        return cat;
    }
}
