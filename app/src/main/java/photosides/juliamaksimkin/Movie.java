package photosides.juliamaksimkin;


public class Movie {
    public int id;
    public String _id;
    public String subject;
    public String body;
    public String url;
    public float rating;


    public Movie() {
    }

    public Movie(String subject, String body, String url) {
        this.subject = subject;
        this.body = body;
        this.url = url;
    }

    public Movie(String subject) {
        this.subject = subject;
    }

    public Movie(String _id, String subject) {
        this._id = _id;
        this.subject = subject;
    }


    public Movie(String _id, String subject, String body, String url) {
        this._id = _id;
        this.subject = subject;
        this.body = body;
        this.url = url;
    }

    public Movie(int id, String _id, String subject, String body, String url) {
        this.id = id;
        this._id = _id;
        this.subject = subject;
        this.body = body;
        this.url = url;

    }

    public Movie(String _id, String subject, String body, String url, float rating) {
        this._id = _id;
        this.subject = subject;
        this.body = body;
        this.url = url;
        this.rating = rating;
    }

    public Movie(int id, String _id, String subject, String body, String url, float rating) {
        this.id = id;
        this._id = _id;
        this.subject = subject;
        this.body = body;
        this.url = url;
        this.rating = rating;
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


    public String toString() {
        return subject;
    }

}
