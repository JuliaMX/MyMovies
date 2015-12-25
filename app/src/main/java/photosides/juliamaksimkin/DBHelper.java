package photosides.juliamaksimkin;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Julia on 12/9/2015.
 */
public class DBHelper extends SQLiteOpenHelper {

    public DBHelper(Context context) {
        // конструктор суперкласса
        super(context, "MoviesDB1", null, 1);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        // создаем таблицу с полями
        db.execSQL("create table movietable1 ("
                + "id integer primary key autoincrement,"
                + "_id text,"
                + "subject text,"
                + "body text,"
                + "url text,"
                + "rating numeric" + ");");

    }




    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {


    }
}
