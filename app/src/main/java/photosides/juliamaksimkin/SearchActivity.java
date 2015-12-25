package photosides.juliamaksimkin;

import android.app.ListActivity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class SearchActivity extends ListActivity implements AsyncResponse {

    private static final String TAG = "myLogs";
    private ArrayList<Movie> moviesList = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        Log.d(TAG, "OnCreate, ArrayList = "+ moviesList.toString());
        refreshList();
    }

    public void buttonSearchCancel_onClick(View view) {
        finish();
    }

    public void buttonGo_onClick(View view) {
        EditText editTextSearch = (EditText) findViewById(R.id.editTextSearch);


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

        Log.d(TAG, "clickedMovie " + clickedMovie.toString());

        try {
            URL url = new URL("http://www.omdbapi.com/?i="+clickedMovie.get_Id()+"&r=json");
            sendRequest(url);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }


    private void sendRequest(URL url) {

        RequestTask requestTask = new RequestTask(this);
        requestTask.delegate = this;

        //execute the async task
        requestTask.execute(url);
        Log.d(TAG, "started requestTask");

    }


    @Override
    public void processFinish(String response) {
        Log.d(TAG, "RESULT in search activity: " + response);

        JSONObject json = null;

        try {
            json = new JSONObject(response);

            if (json.has("Search")){
                Log.d(TAG, "SEARCH responce");
                JSONArray search = json.getJSONArray("Search");
                Log.d(TAG, "lenght = "+ search.length());
                moviesList.clear();

                for (int i = 0; i < search.length(); i++) {
                    String _id = search.getJSONObject(i).getString("imdbID");
                    String subject = search.getJSONObject(i).getString("Title");
                    Log.d(TAG, "responce i = " + i + " _id=" + _id +  " title " + subject + " rating");
                    moviesList.add(new Movie(_id, subject));
                }

                Log.d(TAG, "moviesList:" + moviesList.toString());
                refreshList();

            }else{
                Log.d(TAG, "DETAIL responce");

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

                Log.d(TAG, "try to start editing activity");
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
