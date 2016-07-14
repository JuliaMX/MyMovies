package photosides.juliamaksimkin;

import android.app.Activity;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DAL extends SQLiteOpenHelper{

    private SQLiteDatabase database;

    public DAL (Activity activity) {
        super(activity, DB.NAME, null, DB.VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(DB.Movies.CREATION_STATEMENT);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(DB.Movies.DELETION_STATEMENT);
        db.execSQL(DB.Movies.CREATION_STATEMENT);
    }

    public void open() {
        database = getWritableDatabase();
    }

    public void close() {
        super.close();
    }

    public long insert (String tableName, ContentValues values) {
        long createdId = database.insert(tableName, null, values);
        return createdId;
    }

    public Cursor getTable(String tableName, String[] columns) {
        return database.query(tableName, columns, null, null, null, null, null);
    }

    public Cursor getTable(String tableName, String[] columns, String where) {
        return database.query(tableName, columns, where, null, null, null, null);
    }

    public long update(String tableName, ContentValues values, String where) {
        long affectedRows =  database.update(tableName, values, where, null);
        return affectedRows;
    }

    public long delete(String tableName, String where) {
        return database.delete(tableName, where, null);
    }

    public long deleteAll(String tableName) {
        return database.delete(tableName, null, null);
    }
}