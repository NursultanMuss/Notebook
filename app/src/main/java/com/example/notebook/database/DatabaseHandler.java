package com.example.notebook.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.Nullable;
import android.util.Log;

import com.example.notebook.models.Note;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHandler extends SQLiteOpenHelper {
    private static final String TAG = "DatabaseHandler";
    private static final int DATABASE_VERSION = 4;
    private static final String DATABASE_NAME = "notes_manager";
    private static final String TABLE_NAME = "notes";

    //Column Names
    private static final String KEY_ID = "id";
    private static final String KEY_TITLE = "title";
    private static final String KEY_NOTE = "note";
    private static final String KEY_NOTEBOOK = "notebook";
    private static final String KEY_TIMESTAMP = "time";

    //Column Combinations
    private static final String[] COLS_ID_TITLE_NOTE = new String[] {KEY_ID, KEY_TITLE, KEY_NOTE, KEY_TIMESTAMP, KEY_NOTEBOOK};


    public DatabaseHandler(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_NOTES_TABLE = "CREATE TABLE " + TABLE_NAME + " ( "
                + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT"+", "
                + KEY_TITLE + " TEXT NOT NULL"+ ", "
                + KEY_NOTE + " TEXT NOT NULL" + ", "
                + KEY_NOTEBOOK + " TEXT NOT NULL" + ", "
                + KEY_TIMESTAMP + " INTEGER "
                + ")";
        Log.d(TAG,CREATE_NOTES_TABLE);

        db.execSQL(CREATE_NOTES_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String DROP_TABLE = "DROP TABLE IF EXISTS "+ TABLE_NAME;

        Log.d(TAG,DROP_TABLE);

        db.execSQL(DROP_TABLE);

        onCreate(db);
    }

    //CRUD OPERATIONS

    public void addNote(Note note) {

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_TITLE, note.getTitle());
        values.put(KEY_NOTE, note.getNote());
        values.put(KEY_NOTEBOOK, note.getNotebook());
        values.put(KEY_TIMESTAMP, note.getTimestamp());


        db.insert(TABLE_NAME,null,values);
        db.close();
    }

    public Note getNote(int id){
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor c = db.query(TABLE_NAME,COLS_ID_TITLE_NOTE,KEY_ID +"=?",new String[]{String.valueOf(id)}
        ,null,null,null,null);
        if(c != null){
            c.moveToFirst();
        }
        db.close();

        Log.d(TAG,"Get Note Result "+ c.getString(0)+","+c.getString(1)+","+c.getString(2));
        Note note = new Note(Integer.parseInt(c.getString(0)),c.getString(1)
                ,c.getString(2), c.getString(3), c.getInt(4));
        return note;
    }

    public List<Note> getAllNotes(){
        SQLiteDatabase db = this.getReadableDatabase();

        List<Note> noteList = new ArrayList<>();

        Cursor cursor = db.query(TABLE_NAME,COLS_ID_TITLE_NOTE,null,null,null,null,null);


        if(cursor!= null && cursor.moveToFirst()){

            do{
                Note note = new Note();
                note.setId(Integer.parseInt(cursor.getString(0)));
                note.setTitle(cursor.getString(1));
                note.setNote(cursor.getString(2));
                note.setNotebook(cursor.getString(3));
                note.setTimestamp(cursor.getInt(4));
                noteList.add(note);

            }while (cursor.moveToNext());


        }
        db.close();
        return noteList;

    }

}
