package com.girish.noty.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by Girish on 19/06/16.
 */
public class DataHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "mydb.notes";
    public static final String NOTES_TABLE_NAME = "notes";
    public static final String NOTES_COLUMN_ID = "id";
    public static final String NOTES_COLUMN_CREATED_AT = "created_at";
    public static final String NOTES_COLUMN_TITLE = "title";
    public static final String NOTES_COLUMN_DESCRIPTION = "description";

    private static DataHelper mDataHelper;

    private DataHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    public static DataHelper getInstance(Context context) {
        if (mDataHelper == null) {
            mDataHelper = new DataHelper(context);
        }
        return mDataHelper;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(
                "create table " +
                        NOTES_TABLE_NAME + " (" +
                        NOTES_COLUMN_ID + " integer primary key, " +
                        NOTES_COLUMN_CREATED_AT + " text," +
                        NOTES_COLUMN_TITLE + " text," +
                        NOTES_COLUMN_DESCRIPTION + " text)"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + NOTES_TABLE_NAME);
        onCreate(db);
    }

    private String getDateTime() {
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "yyyy-MM-dd", Locale.getDefault());
        Date date = new Date();
        return dateFormat.format(date);
    }

    public long insertNote(String title, String description) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(NOTES_COLUMN_TITLE, title);
        contentValues.put(NOTES_COLUMN_DESCRIPTION, description);
        contentValues.put(NOTES_COLUMN_CREATED_AT, getDateTime());
        long id = db.insert(NOTES_TABLE_NAME, null, contentValues);
        return id;
    }

    public List<NoteItem> getAllNotes() {
        List<NoteItem> list = new ArrayList<NoteItem>();

        //hp = new HashMap();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from " + NOTES_TABLE_NAME, null);
        res.moveToFirst();

        while (res.isAfterLast() == false) {
            NoteItem noteItem = new NoteItem();
            noteItem.setId(res.getInt(res.getColumnIndex(NOTES_COLUMN_ID)));
            noteItem.setTitle(res.getString(res.getColumnIndex(NOTES_COLUMN_TITLE)));
            noteItem.setDescription(res.getString(res.getColumnIndex(NOTES_COLUMN_DESCRIPTION)));
            noteItem.setTimeStamp(res.getString(res.getColumnIndex(NOTES_COLUMN_CREATED_AT)));
            list.add(noteItem);
            res.moveToNext();
        }
        return list;
    }

    public boolean updateNote(long id, String title, String description) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(NOTES_COLUMN_TITLE, title);
        contentValues.put(NOTES_COLUMN_DESCRIPTION, description);
        db.update(NOTES_TABLE_NAME, contentValues, "id = ? ", new String[]{Long.toString(id)});
        return true;
    }

    public int deleteNote(long id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(NOTES_TABLE_NAME,
                "id = ? ",
                new String[]{Long.toString(id)});
    }
}
