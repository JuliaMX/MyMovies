package photosides.juliamaksimkin;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class SearchActivity extends ListActivity implements AsyncResponse {

    private ArrayList<Movie> moviesList = new ArrayList<>();
    private EditText editTextSearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        refreshList();
        editTextSearch = (EditText) findViewById(R.id.editTextSearch);
    }

    public void buttonSearchCancel_onClick(View view) {
        finish();
    }

    public void buttonGo_onClick(View view) {
        try {
            URL url = new URL("http://www.omdbapi.com/?s="+editTextSearch.getText().toString().replace(" ", "+")+"&r=json");
            sendRequest(url);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        Movie clickedMovie = moviesList.get(position);
        try {
            URL url = new URL("http://www.omdbapi.com/?i="+clickedMovie.get_Id()+"&r=json");
            sendRequest(url);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    private void sendRequest(URL url) {

        RequestAsyncTask requestAsyncTask = new RequestAsyncTask(this);
        requestAsyncTask.delegate = this;

        requestAsyncTask.execute(url);
    }

    @Override
    public void processFinish(String response) {
        JSONObject json = null;
        try {
            json = new JSONObject(response);
            if (json.has("Search")){
                JSONArray search = json.getJSONArray("Search");
                moviesList.clear();

                for (int i = 0; i < search.length(); i++) {
                    String _id = search.getJSONObject(i).getString("imdbID");
                    String subject = search.getJSONObject(i).getString("Title");
                    moviesList.add(new Movie(_id, subject));
                }
                refreshList();

            }else{
                Movie movie = new Movie(
                        json.getString("imdbID"),
                        json.getString("Title"),
                        json.getString("Plot"),
                        json.getString("Poster"),
                        Float.parseFloat(json.getString("imdbRating")),
                        false);

                Intent intent = new Intent(this, EditingActivity.class);
                intent.putExtra("action", "addFromSearch");
                intent.putExtra("_id", movie._id);
                intent.putExtra("subject", movie.subject);
                intent.putExtra("body", movie.body);
                intent.putExtra("url", movie.url);
                intent.putExtra("rating", movie.rating);
                startActivity(intent);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void refreshList (){
        MovieAdapter adapter = new MovieAdapter(this, moviesList);
        setListAdapter(adapter);
    }
}