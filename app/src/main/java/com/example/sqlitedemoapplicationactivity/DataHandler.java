package com.example.sqlitedemoapplicationactivity;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import android.content.Context;


import java.util.ArrayList;
import java.util.List;

public class DataHandler extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "StudentsDB.db";
    public static final String TABLE_NAME = "Students";
    public static final String COLUMN_ID = "StudentID";
    public static final String COLUMN_NAME = "StudentName";


    public DataHandler(Context context, String name,
                       SQLiteDatabase.CursorFactory factory, int version) {
        super(context, DATABASE_NAME, factory, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);

        String CREATE_STUDENTS_TABLE = "CREATE TABLE " +
                TABLE_NAME + "("
                + COLUMN_ID + " INTEGER PRIMARY KEY," +
                COLUMN_NAME + " TEXT )";

        db.execSQL(CREATE_STUDENTS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion,
                          int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);

        onCreate(db);
    }

    public String loadDataHandler() {
        String result = "";

        String query = "SELECT* FROM " + TABLE_NAME;

        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.rawQuery(query, null);

        while (cursor.moveToNext()) {

            int result_0 = cursor.getInt(0);

            String result_1 = cursor.getString(1);

            result += String.valueOf(result_0) + " " + result_1 +
                    System.getProperty("line.separator");
        }

        cursor.close();

        db.close();
        return result;
    }

    public void addDataHandler(Student student) {

        ContentValues values = new ContentValues();

        values.put(COLUMN_ID, student.getStudentID());
        values.put(COLUMN_NAME, student.getStudentName());
        SQLiteDatabase db = this.getWritableDatabase();

        db.insert(TABLE_NAME, null, values);
        db.close();
    }

    public Student findFisrtDataHandler(String studentname) {


        String query = "Select * FROM " + TABLE_NAME
                + " WHERE " + COLUMN_NAME + " = "
                + "'" + studentname + "'";
        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.rawQuery(query, null);
        Student student = new Student();

        if (cursor.moveToFirst()) {
            cursor.moveToFirst();
            student.setStudentID(Integer.parseInt(cursor.getString(0)));
            student.setStudentName(cursor.getString(1));
            cursor.close();
        } else {
            student = null;
        }
        db.close();

        return student;
    }

    public List<Student> findAllDataHandler(String studentname) {

        String query = "Select * FROM " + TABLE_NAME
                + " WHERE " + COLUMN_NAME + " = "
                + "'" + studentname + "'";
        SQLiteDatabase db = this.getWritableDatabase();

        List<Student> lst =  new ArrayList<Student>();

        Cursor cursor = db.rawQuery(query, null);

        if(cursor.moveToFirst()) {
            do {
                Student student = new Student();
                student.setStudentID(Integer.parseInt(cursor.getString(0)));
                student.setStudentName(cursor.getString(1));
                lst.add(student);
            }while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        return lst;
    }
    public boolean deleteDataHandler(int ID) {
        boolean result = false;
        String query = "Select * FROM " + TABLE_NAME + " WHERE "
                + COLUMN_ID + " = '" + String.valueOf(ID) + "'";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        Student student = new Student();
        if (cursor.moveToFirst()) {
            student.setStudentID(Integer.parseInt(cursor.getString(0)));
            db.delete(TABLE_NAME, COLUMN_ID + "=?",
                    new String[] {
                            String.valueOf(student.getStudentID())
                    });
            cursor.close();
            result = true;
        }
        db.close();
        return result;
    }
    public boolean updateDataHandler(int ID, String name) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues args = new ContentValues();
        args.put(COLUMN_ID, ID);
        args.put(COLUMN_NAME, name);
        return db.update(TABLE_NAME, args, COLUMN_ID + " = " + ID, null) > 0;
    }

}