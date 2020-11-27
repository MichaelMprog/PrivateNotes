package com.example.privatenotes;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class NoteDatabase extends SQLiteOpenHelper {
    // database values
    private static final int DATABASE_VERSION = 2;
    private static final String DATABASE_NAME = "notesdbs";     // aka database name
    private static final String DATABASE_TABLE = "notesTables"; // aka table name

    // column names for database table
    private static final String KEY_ID = "id";
    private static final String KEY_TITLE = "title";
    private static final String KEY_CONTENT = "content";
    private static final String KEY_DATE = "date";
    private static final String KEY_TIME = "time";
    private static final String KEY_CATEGORY = "category";

    // constructor
    public NoteDatabase(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);

    }

    // creates database
    @Override
    public void onCreate(SQLiteDatabase db) {
        String  query = "CREATE TABLE " + DATABASE_TABLE + "(" +
                KEY_ID + " INTEGER PRIMARY KEY," +
                KEY_TITLE + " TEXT," +
                KEY_CONTENT + " TEXT," +
                KEY_DATE + " TEXT," +
                KEY_TIME + " TEXT," +
                KEY_CATEGORY + " TEXT" + ")";
        db.execSQL(query);
    }

    // upgrade database if older version exists
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion >= newVersion)
            return;

        db.execSQL("DROP TABLE IF EXISTS " + DATABASE_TABLE);
        onCreate(db);
    }

    // add note to database
    public void addNote (Note note) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues c = new ContentValues();
        c.put(KEY_TITLE, note.getTitle());
        c.put(KEY_CONTENT, note.getContent());
        c.put(KEY_DATE, note.getDate());
        c.put(KEY_TIME, note.getTime());
        c.put(KEY_CATEGORY, note.getCategory());

        // inserting data into db
        db.insert(DATABASE_TABLE, null, c);
    }

    // returns a note
    public Note getNote(int id) {
        // select from databaseTable where id=1
        SQLiteDatabase db = this.getWritableDatabase();
        // cursor points to the specific row in the database column
        // need to add KEY_CATEGORY
        String[] query = new String[]{KEY_ID, KEY_TITLE, KEY_CONTENT, KEY_DATE, KEY_TIME, KEY_CATEGORY};
        Cursor cursor = db.query(DATABASE_TABLE, query, KEY_ID + "=?", new String[]{String.valueOf(id)},
                null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        Note note = new Note(
                Integer.parseInt(cursor.getString(0)),
                cursor.getString(1),
                cursor.getString(2),
                cursor.getString(3),
                cursor.getString(4),
                cursor.getString(5)
        );
        return note;
    }

    // returns the list of notes
    public List<Note> getAllNotes() {
        List<Note> allNotes = new ArrayList<>();

        String query = "SELECT * FROM " + DATABASE_TABLE+" ORDER BY "+KEY_ID+" DESC";
        /*
        if (MainActivity.filter == 0) {
            query = "SELECT * FROM " + DATABASE_TABLE+" ORDER BY "+KEY_DATE+" DESC";
        }
        else if (MainActivity.filter == 1){
            query = "SELECT * FROM " + DATABASE_TABLE+" ORDER BY "+KEY_TITLE+" DESC";
        }
        else if (MainActivity.filter == 2){
            query = "SELECT * FROM " + DATABASE_TABLE+" ORDER BY "+KEY_CATEGORY+" DESC";
        }
        */
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        // if not null, retrieve all note data
        if (cursor.moveToFirst()) {
            do{
                Note note = new Note();
                note.setID(Integer.parseInt(cursor.getString(0)));
                note.setTitle(cursor.getString(1));
                note.setContent(cursor.getString(2));
                note.setDate(cursor.getString(3));
                note.setTime(cursor.getString(4));
                note.setCategory(cursor.getString(5));
                allNotes.add(note);
            }while(cursor.moveToNext());
        }
        return allNotes;
    }

    public int editNote (Note note) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues c = new ContentValues();
        Log.d("Edited", "Edited Title: -> " + note.getTitle() + "\n ID -> " + note.getID());
        c.put(KEY_TITLE, note.getTitle());
        c.put(KEY_CONTENT, note.getContent());
        c.put(KEY_DATE, note.getDate());
        c.put(KEY_TIME, note.getTime());
        c.put(KEY_CATEGORY, note.getCategory());
        return db.update(DATABASE_TABLE, c, KEY_ID+"=?", new String[]{String.valueOf(note.getID())});
    }

    // take id of note to delete, and delete it
    void deleteNote (int id){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(DATABASE_TABLE, KEY_ID+"=?", new String[]{String.valueOf(id)});
        db.close();
    }

    /*
    THIS FUNCTION COULD BE USED TO SORT THE NOTE DATABASE WHEN SORT BUTTON IS PRESSED
    public String sortNotes(int sortKey)
    {
        String query;
        query = "SELECT * FROM " + DATABASE_TABLE + " ORDER BY " + sortKey + "DESC";
        return query;
    }

     */
}
