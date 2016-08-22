package com.developer.stoyanov.imageuploader.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.util.Log;

public class DataHandler extends SQLiteOpenHelper implements ActionsDataHandler {

    private static final String TAG = "DataHandler";

    private static final int DATABASE_VERSION = 2;
    private static final String DATABASE_NAME = "statements";
    private static final String TABLE_STATEMENT = "statement";
    private static final String KEY_ID = "id";
    private static final String KEY_STATEMENT = "statement_";
    private static final String KEY_FILEPATH = "file_path";
    private static final String KEY_RESPONSE = "response";
    private static final String KEY_LANGUAGE = "language";
    private static final String KEY_DEFAULT_LANGUAGE = "default_language";
    private static final String ID_1 = "id=1";


    public DataHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        Log.i(TAG, "DataHandler: Constructor has such args - DATABASE_VERSION = " + DATABASE_VERSION
                + ", DATABASE_NAME = " + DATABASE_NAME);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String CREATE_TABLE = "CREATE TABLE " + TABLE_STATEMENT + "("
                + KEY_ID + " INTEGER PRIMARY KEY," + KEY_STATEMENT + " TEXT,"
                + KEY_RESPONSE + " TEXT," + KEY_FILEPATH + " TEXT," + KEY_LANGUAGE + " TEXT," +
                KEY_DEFAULT_LANGUAGE + " TEXT)";
        Log.i(TAG, "onCreate: Create table. SQL query = " + CREATE_TABLE);
        sqLiteDatabase.execSQL(CREATE_TABLE);

        ContentValues values = new ContentValues();
        values.put(KEY_STATEMENT, 0);
        values.put(KEY_FILEPATH, "");
        values.put(KEY_RESPONSE, "");
        values.put(KEY_LANGUAGE, "");
        values.put(KEY_DEFAULT_LANGUAGE, "");

        sqLiteDatabase.insert(TABLE_STATEMENT, null, values);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_STATEMENT);
        onCreate(sqLiteDatabase);
    }

    @Override
    public void setStatement(int statement) {
        Log.d(TAG, "setStatement: statement - " + statement);
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            ContentValues values = new ContentValues();
            values.put(KEY_STATEMENT, statement);

            int i = db.update(TABLE_STATEMENT, values, ID_1, null);
            Log.i(TAG, "setStatement: the number of rows affected = " + i);
        } finally {
            db.close();
        }
    }

    @Override
    public int getStatement() {
        SQLiteDatabase DBConnect = null;
        int resultStatement;
        try {
            DBConnect = this.getReadableDatabase();
            Cursor cursor = null;
            try {
                cursor = DBConnect.query(TABLE_STATEMENT, null, null, null, null, null, null);
                if (cursor.moveToFirst()) {
                    resultStatement = cursor.getInt(1);
                    return resultStatement;
                }
            } finally {
                if (cursor != null) cursor.close();
            }
        } finally {
            if (DBConnect != null) DBConnect.close();
        }
        return 0;
    }

    @Override
    public void setData(Bundle bundle) {
        SQLiteDatabase DBConnect = null;
        try {
            DBConnect = this.getWritableDatabase();

            ContentValues values = new ContentValues();
            values.put(KEY_FILEPATH, bundle.getString("filePath", ""));
            values.put(KEY_RESPONSE, bundle.getString("response", ""));

            long result = DBConnect.update(TABLE_STATEMENT, values, "id=1", null);
            Log.i(TAG, "setStatement: the number of rows affected = " + result);
        } finally {
            if (DBConnect != null) DBConnect.close();
        }
    }

    @Override
    public Bundle getData() {
        SQLiteDatabase DBConnect = null;
        try {
            DBConnect = this.getWritableDatabase();
            Cursor cursor = null;
            try {
                cursor = DBConnect.query(TABLE_STATEMENT, null, null, null, null, null, null);
                int filePathColIndex = cursor.getColumnIndex("file_path");
                int responseColIndex = cursor.getColumnIndex("response");
                if (cursor.moveToFirst()) {
                    Bundle bundle = new Bundle();
                    String filePath = cursor.getString(filePathColIndex);
                    String response = cursor.getString(responseColIndex);
                    Log.i(TAG, "getData: Bundle packed with values -> filePath = " + filePath
                            + ", response = " + response);
                    bundle.putString("filePath", filePath);
                    bundle.putString("response", response);
                    return bundle;
                }
            } finally {
                if (cursor != null) cursor.close();
            }
        } finally {
            if (DBConnect != null) DBConnect.close();
        }
        return null;
    }

    @Override
    public void setLanguage(String lang) {
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            ContentValues values = new ContentValues();
            values.put(KEY_LANGUAGE, lang);
            Log.i(TAG, "setLanguage: language = " + lang);
            int i = db.update(TABLE_STATEMENT, values, ID_1, null);
            Log.i(TAG, "setStatement: the number of rows affected = " + i);
        } finally {
            db.close();
        }
    }

    @Override
    public String getLanguage() {
        SQLiteDatabase DBConnect = null;
        try {
            DBConnect = this.getReadableDatabase();
            Cursor cursor = null;
            try {
                cursor = DBConnect.query(TABLE_STATEMENT, null, null, null, null, null, null);
                int languageColIndex = cursor.getColumnIndex(KEY_LANGUAGE);
                if (cursor.moveToFirst()) {
                    return cursor.getString(languageColIndex);
                }
            } finally {
                if (cursor != null) cursor.close();
            }
        } finally {
            if (DBConnect != null) DBConnect.close();
        }
        return null;
    }

    @Override
    public void setDefaultLanguage(String lang) {
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            ContentValues values = new ContentValues();
            values.put(KEY_LANGUAGE, lang);
            Log.i(TAG, "setLanguage: language = " + lang);
            int i = db.update(TABLE_STATEMENT, values, ID_1, null);
            Log.i(TAG, "setStatement: the number of rows affected = " + i);
        } finally {
            db.close();
        }
    }

    @Override
    public String getDefaultLanguage() {
        SQLiteDatabase DBConnect = null;
        try {
            DBConnect = this.getReadableDatabase();
            Cursor cursor = null;
            try {
                cursor = DBConnect.query(TABLE_STATEMENT, null, null, null, null, null, null);
                int languageColIndex = cursor.getColumnIndex(KEY_LANGUAGE);
                if (cursor.moveToFirst()) {
                    return cursor.getString(languageColIndex);
                }
            } finally {
                if (cursor != null) cursor.close();
            }
        } finally {
            if (DBConnect != null) DBConnect.close();
        }
        return null;
    }
}
