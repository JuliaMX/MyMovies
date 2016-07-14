package photosides.juliamaksimkin;

public class Movie {
    public int id;
    public String _id;
    public String subject;
    public String body;
    public String url;
    public float rating;
    public boolean watched;

    public Movie() {}

    public Movie(String subject, String body, String url, boolean watched) {
        this.subject = subject;
        this.body = body;
        this.url = url;
        this.watched = watched;
    }

    public Movie(String subject,  boolean watched) {
        this.subject = subject;
    }

    public Movie(String _id, String subject) {
        this._id = _id;
        this.subject = subject;
    }

    public Movie(String _id, String subject, String body, String url,  boolean watched) {
        this._id = _id;
        this.subject = subject;
        this.body = body;
        this.url = url;
        this.watched = watched;
    }

    public Movie(int id, String _id, String subject, String body, String url,  boolean watched) {
        this.id = id;
        this._id = _id;
        this.subject = subject;
        this.body = body;
        this.url = url;
        this.watched = watched;
    }

    public Movie(String _id, String subject, String body, String url, float rating,  boolean watched) {
        this._id = _id;
        this.subject = subject;
        this.body = body;
        this.url = url;
        this.rating = rating;
        this.watched = watched;
    }

    public Movie(int id, String _id, String subject, String body, String url, float rating,  boolean watched) {
        this.id = id;
        this._id = _id;
        this.subject = subject;
        this.body = body;
        this.url = url;
        this.rating = rating;
        this.watched = watched;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String get_Id() {
        return _id;
    }

    public void set_Id(String _id) {
        this._id = _id;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    public boolean isWatched() {
        return watched;
    }

    public void setWatched(boolean watched) {
        this.watched = watched;
    }

    public String toString() {
        return subject;
    }
}