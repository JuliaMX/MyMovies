package photosides.juliamaksimkin;

public class DB {
    public static final String NAME = "Movie_DB";
    public static final int VERSION = 1;

    public static class Movies {
        public static final String TABLE_NAME = "Movies";
        public static final String ID = "id";
        public static final String _ID = "_id";
        public static final String SUBJECT = "subject";
        public static final String BODY = "body";
        public static final String URL = "url";
        public static final String RATING = "rating";
        public static final String WATCHED = "watched";
        public static final String[] ALL_COLUMNS = new String[] {ID, _ID, SUBJECT, BODY, URL, RATING, WATCHED};

        public static final String CREATION_STATEMENT = "CREATE TABLE " + TABLE_NAME +
                " ( " + ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                _ID + " TEXT, " +
                SUBJECT + " TEXT, " +
                BODY + " TEXT, " +
                URL + " TEXT, " +
                RATING + " NUMERIC, " +
                WATCHED + " TEXT )";

        public static final String DELETION_STATEMENT = "DROP TABLE IF EXIST " + TABLE_NAME;

    }
}
