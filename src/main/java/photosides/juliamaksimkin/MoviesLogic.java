package photosides.juliamaksimkin;

import android.app.Activity;
import android.content.ContentValues;
import android.database.Cursor;

import java.util.ArrayList;

public class MoviesLogic extends BaseLogic {

    public MoviesLogic(Activity activity) {
        super(activity);
    }

    public long addMovie(Movie movie) {
        ContentValues contentValues = new ContentValues();

        contentValues.put(DB.Movies._ID, movie.get_Id());
        contentValues.put(DB.Movies.SUBJECT, movie.getSubject());
        contentValues.put(DB.Movies.BODY, movie.getBody());
        contentValues.put(DB.Movies.URL, movie.getUrl());
        contentValues.put(DB.Movies.RATING, movie.getRating());
        contentValues.put(DB.Movies.WATCHED, String.valueOf(movie.isWatched()));

        long createdId = dal.insert(DB.Movies.TABLE_NAME, contentValues);
        return createdId;
    }

    public long updateMovie(Movie movie) {

        ContentValues contentValues = new ContentValues();

        contentValues.put(DB.Movies._ID, movie.get_Id());
        contentValues.put(DB.Movies.SUBJECT, movie.getSubject());
        contentValues.put(DB.Movies.BODY, movie.getBody());
        contentValues.put(DB.Movies.URL, movie.getUrl());
        contentValues.put(DB.Movies.RATING, movie.getRating());
        contentValues.put(DB.Movies.WATCHED, String.valueOf(movie.isWatched()));

        String where = DB.Movies.ID + "=" + movie.getId();

        long affectedRows = dal.update(DB.Movies.TABLE_NAME, contentValues, where);
        return affectedRows;
    }

    public long deleteMovie(Movie movie) {
        String where = DB.Movies.ID + "=" + movie.getId();
        long affectedRows = dal.delete(DB.Movies.TABLE_NAME, where);
        return affectedRows;
    }

    public long deleteAllMovies(){
        long affectedRows = dal.deleteAll(DB.Movies.TABLE_NAME);
        return affectedRows;
    }

    private ArrayList<Movie> getMovies(String where) {

        ArrayList<Movie> movies = new ArrayList<>();

        Cursor cursor = dal.getTable(DB.Movies.TABLE_NAME, DB.Movies.ALL_COLUMNS, where);

        while (cursor.moveToNext()) {
            Movie movie = new Movie();
            movie.setId(cursor.getInt(cursor.getColumnIndex(DB.Movies.ID)));
            movie.set_Id(cursor.getString(cursor.getColumnIndex(DB.Movies._ID)));
            movie.setSubject(cursor.getString(cursor.getColumnIndex(DB.Movies.SUBJECT)));
            movie.setBody(cursor.getString(cursor.getColumnIndex(DB.Movies.BODY)));
            movie.setUrl(cursor.getString(cursor.getColumnIndex(DB.Movies.URL)));
            movie.setRating(cursor.getFloat(cursor.getColumnIndex(DB.Movies.RATING)));
            movie.setWatched(Boolean.parseBoolean(cursor.getString(cursor.getColumnIndex(DB.Movies.WATCHED))));

            movies.add(movie);
        }
        cursor.close();

        return movies;
    }

    public ArrayList<Movie> getAllMovies() {
        return getMovies(null);
    }

    public Movie getMovieById(int id) {
        String where = DB.Movies.ID + "=" + id;
        return getMovies(where).get(0);
    }
}