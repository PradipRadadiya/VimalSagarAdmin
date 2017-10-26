package com.example.grapes_pradip.vimalsagaradmin.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

@SuppressWarnings("ALL")
class DBAdapter {
    private static final String DATABASE_NAME = "mydb_emp";
    private static final int DATABASE_VERSION = 1;
    private static final String TABLE_NAME = "employee";
    private static final String ID = "id";
    private static final String FIRSTNAME = "firstname";
    private static final String LASTNAME = "lastname";
    private static final String EMAIL = "email";
    private static final String MOBILE = "mobile";
    private static final String PROFILE_IMAGE = "pro_image";

    private SQLiteDatabase mydb_emp;
    private final DBHelper dbHelper;

    public DBAdapter(Context context) {
        dbHelper = new DBHelper(context);
    }

    //OPEN DATABASE
    DBAdapter openDatabase() {
        mydb_emp = dbHelper.getWritableDatabase();
        return this;
    }

    //CLOSE DATABASE
    public void closeDatabase() {
        dbHelper.close();
    }


    //INSERT VALUES
    public void addValues(String fnm, String lnm, String emails, String mo, byte[] imageBytes) {
        ContentValues values = new ContentValues();
        values.put(FIRSTNAME, fnm);
        values.put(LASTNAME, lnm);
        values.put(EMAIL, emails);
        values.put(MOBILE, mo);
        values.put(PROFILE_IMAGE, imageBytes);
        mydb_emp.insert(TABLE_NAME, null, values);

        Log.e("addvaluespppp", "addvalues error");
    }

    //Get all values
    public Cursor getAllValues() {
        String[] columns = {ID, FIRSTNAME, LASTNAME, EMAIL, MOBILE, PROFILE_IMAGE};
        Cursor c = mydb_emp.query(true, TABLE_NAME, columns, null, null, ID, null, null, null);
        c.moveToNext();
        return c;

    }

    @SuppressWarnings("unused")
    class DBHelper extends SQLiteOpenHelper {

        public DBHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            try {
                db.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " (id INTEGER PRIMARY KEY AUTOINCREMENT, "
                        + FIRSTNAME + " TEXT, "
                        + LASTNAME + " TEXT, "
                        + EMAIL + " TEXT, "
                        + MOBILE + " TEXT, "
                        + PROFILE_IMAGE + " BLOB ); ");
                Log.e("......table :.......:", "Table created");
                //+ PROFILE_IMAGE + "BLOB);");
            } catch (SQLException e) {
                Log.e("error se :.......:", e.toString());
            }

        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        }
    }
}
