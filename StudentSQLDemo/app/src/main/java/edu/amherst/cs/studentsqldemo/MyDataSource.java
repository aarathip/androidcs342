package edu.amherst.cs.studentsqldemo;

/**
 * Created by aarathi on 9/17/15.
 * Factory class for connecting to the physical data source.
 */

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class MyDataSource {

    // Database fields
    private SQLiteDatabase database;
    private MySQLiteHelper dbHelper;
    private String[] allColumns = { MySQLiteHelper.COLUMN_ID,
            MySQLiteHelper.COLUMN_NAME,
            MySQLiteHelper.COLUMN_MAJOR,
            MySQLiteHelper.COLUMN_YEAR};



    public MyDataSource(Context context) {
        dbHelper = new MySQLiteHelper(context);
    }

    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    public boolean isOpen()
    {
        return dbHelper.getWritableDatabase().isOpen();
    }
    
    public void close() {
        dbHelper.close();
    }

    //Adding a new student record to the StudentSQLDemo table
    public StudentDemo addStudent(String name, String major, int year) {

        //Adding the key-value pairs
        ContentValues values = new ContentValues();
        values.put(MySQLiteHelper.COLUMN_NAME, name);
        values.put(MySQLiteHelper.COLUMN_MAJOR, major);
        values.put(MySQLiteHelper.COLUMN_YEAR, year);

        //Insert the new record
        long insertId = database.insert(MySQLiteHelper.TABLE_DEMO, null,
                values);

        //Query and return the record with the insertId as its id
        Cursor cursor = database.query(MySQLiteHelper.TABLE_DEMO,
                allColumns, MySQLiteHelper.COLUMN_ID + " = " + insertId, null,
                null, null, null);

        //Move to the first record
        cursor.moveToFirst();

        //Load values from cursor into the StudentDemo object
        StudentDemo student = cursorToKey(cursor);

        //Dont forget to close teh cursor
        cursor.close();

        return student;
    }

    //If you want to delete a row
    public void deleteStudent(StudentDemo student) {
        long id = student.getId();
        System.out.println("Student deleted with id: " + id);
        database.delete(MySQLiteHelper.TABLE_DEMO, MySQLiteHelper.COLUMN_ID
                + " = " + id, null);
    }

    /*
        Return all student records in the table.
     */

    public List<StudentDemo> getAllRecords() {
        List<StudentDemo> allRecords = new ArrayList<StudentDemo>();

        //Generate a database query with no selection filters


        //Loop through all the cursor records as follows
        /*
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            StudentDemo s = cursorToKey(cursor);
            allRecords.add(s);
            cursor.moveToNext();
        }
         */

        // make sure to close the cursor

        return allRecords;
    }


    //Each value in the cursor is used to set the attributes of the StudentDemo object
    private StudentDemo cursorToKey(Cursor cursor) {
        StudentDemo s = new StudentDemo();
        s.setId(cursor.getLong(0));
        s.setName(cursor.getString(1));
        s.setMajor(cursor.getString(2));
        s.setYear(cursor.getInt(3));
        return s;
    }
}

