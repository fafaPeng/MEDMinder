package com.example.medminder;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class ReminderEntryDBHelper extends SQLiteOpenHelper {
    //db info (file name, version, table name)
    private static final String DATABASE_NAME = "reminder_entries.db";
    private static final int DATABASE_VERSION = 3;
    public static final String TABLE_NAME_ENTRIES = "entry";

    //keys for db columns
    public static final String COL_ROW = "_id";
    public static final String COL_REMINDER_TYPE = "reminder_type";
    public static final String COL_MEDICATION_TYPE = "medication_type";
    public static final String COL_DATE_TIME = "date_time";
    public static final String COL_NAME = "medication_name";
    public static final String COL_QUANTITY = "quantity";
    public static final String COL_NOTES = "notes";
    public static final String COL_CONFIRM = "confirmed_dosage";


    private SQLiteDatabase database;

    //db table schema
    public static final String CREATE_TABLE_ENTRIES = "CREATE TABLE IF NOT EXISTS "
            + TABLE_NAME_ENTRIES + " ("
            + COL_ROW + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + COL_REMINDER_TYPE + " INTEGER NOT NULL, "
            + COL_MEDICATION_TYPE + " INTEGER NOT NULL, "
            + COL_DATE_TIME + " DATETIME NOT NULL, "
            + COL_NAME + " TEXT, "
            + COL_QUANTITY + " TEXT, "
            + COL_NOTES + " TEXT, "
            + COL_CONFIRM + " INTEGER);";

    public ReminderEntryDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    /**
     * Execute the db creation
     */
    @Override
    public void onCreate(SQLiteDatabase database) {
        database.execSQL(CREATE_TABLE_ENTRIES);
    }

    /**
     * Handle table upgrades
     *
     * @param db         database
     * @param oldVersion old version number
     * @param newVersion new version number
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_ENTRIES);
        onCreate(db);
    }

    /**
     * Insert an ExerciseEntry into db
     */
    public long insertEntry(ReminderEntry entry) {
        //put all the column values into a new row
        ContentValues values = new ContentValues();
        values.put(COL_REMINDER_TYPE, entry.getmReminderType());
        values.put(COL_MEDICATION_TYPE, entry.getmMReminderMedicationType());
        values.put(COL_DATE_TIME, entry.getmDateTime());
        values.put(COL_NAME, entry.getmMedicationName());
        values.put(COL_QUANTITY, entry.getmMedicationQuantity());
        values.put(COL_NOTES, entry.getmMedicationNotes());
        values.put(COL_CONFIRM, entry.getmConfirmed());

        //insert new row into db
//        SQLiteDatabase database = getWritableDatabase();
        database = getWritableDatabase();
        long insertedId = database.insert(TABLE_NAME_ENTRIES, null, values);
        //close database
        database.close();
        return insertedId;
    }

    /**
     * Remove an entry by its id in the db
     */
    public void removeEntry(long id) {
//        SQLiteDatabase database = getWritableDatabase();
        database = getWritableDatabase();
        database.delete(TABLE_NAME_ENTRIES,
                COL_ROW + " = " + id, null);
        //close database
        database.close();

    }

    /**
     * Update an entry by its id in the db
     */
    public void updateEntry(ReminderEntry entry) {
        database = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_REMINDER_TYPE, entry.getmReminderType());
        values.put(COL_MEDICATION_TYPE, entry.getmMReminderMedicationType());
        values.put(COL_DATE_TIME, entry.getmDateTime());
        values.put(COL_NAME, entry.getmMedicationName());
        values.put(COL_QUANTITY, entry.getmMedicationQuantity());
        values.put(COL_NOTES, entry.getmMedicationNotes());
        values.put(COL_CONFIRM, entry.getmConfirmed());

        String[] id = new String[]{entry.getId().toString()};
        int updatedId = database.update(TABLE_NAME_ENTRIES, values, "_id = ?", id);
        Log.d("Update", "Rows affected: "+updatedId);
    }

    /**
     * Query a specific entry by its id.
     * @param rowId     id of the entry to fetch
     * @return          the ExerciseEntry object at rowId
     */
    public ReminderEntry fetchEntryByIndex(long rowId) {
//        SQLiteDatabase database = getWritableDatabase();
        database = getWritableDatabase();
        Cursor cursor = database.query(TABLE_NAME_ENTRIES, null,
                COL_ROW + " = " + rowId, null, null,
                null, null);
        cursor.moveToFirst();
        ReminderEntry entry = covertCursorToEntry(cursor);

        //close cursor and database
        cursor.close();
        database.close();

        return entry;
    }

    /**
     * Query the entire table, return all rows
     * @return  all ExerciseEntry objects in the db in a list
     */
    public ArrayList<ReminderEntry> fetchAllEntries() {
        Log.d("fetch all", "fetchAllEntries: here");
//                SQLiteDatabase database = getWritableDatabase();
        database = getWritableDatabase();
        ArrayList<ReminderEntry> entries = new ArrayList<>();
        Cursor cursor = database.query(TABLE_NAME_ENTRIES,
                null, null, null, null, null, null);

        cursor.moveToFirst();
        //loop through all entries
        while (!cursor.isAfterLast()) {
            ReminderEntry comment = covertCursorToEntry(cursor);
            entries.add(comment);
            cursor.moveToNext();
        }
        //close cursor and database
        cursor.close();
        return entries;
    }

    /**
     * Get the ExerciseEntry at the Cursor
     * @param cursor    Cursor
     * @return          ExerciseEntry at the cursor
     */
    public ReminderEntry covertCursorToEntry(Cursor cursor) {
        //make an entry and fill in its values
        ReminderEntry entry = new ReminderEntry();
        entry.setId(cursor.getLong(0));
        entry.setmReminderType(cursor.getInt(1));
        entry.setmMReminderMedicationType(cursor.getInt(2));
        entry.setmDateTime(cursor.getLong(3));
        entry.setmMedicationName(cursor.getString(4));
        entry.setmMedicationQuantity(cursor.getString(5));
        entry.setmMedicationNotes(cursor.getString(6));
        entry.setmConfirmed(cursor.getInt(7));
        return entry;
    }
}
