package photosides.juliamaksimkin;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class MovieAdapter extends ArrayAdapter<Movie> {

    private Activity activity;
    private ArrayList<Movie> movies;
    private LayoutInflater layoutInflater;

    public MovieAdapter(Activity activity, ArrayList<Movie> movies) {
        super(activity, 0, movies);
        this.activity = activity;
        this.movies = movies;
        layoutInflater = (LayoutInflater)activity.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
    }

    public View getView(int position, View convertView, ViewGroup parent){

        Movie movie = movies.get(position);
        View itemLayout = layoutInflater.inflate(R.layout.item_movie, null);
        TextView textViewMovieItem = (TextView)itemLayout.findViewById(R.id.textViewMovieItem);
        textViewMovieItem.setText(movie.getSubject());

        return itemLayout;
    }
}
