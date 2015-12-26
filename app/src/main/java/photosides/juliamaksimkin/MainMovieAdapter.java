package photosides.juliamaksimkin;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;

public class MainMovieAdapter extends ArrayAdapter<Movie> {

    private Activity activity;
    private ArrayList<Movie> movies;
    private LayoutInflater layoutInflater;

    private final static String TAG = "myLogs";

    public MainMovieAdapter(Activity activity, ArrayList<Movie> movies) {
        super(activity, 0, movies);
        this.activity = activity;
        Log.d(TAG, "Activity was found");

        this.movies = movies;
        Log.d(TAG, "Movies was found");

        layoutInflater = (LayoutInflater) activity.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        Log.d(TAG, "layoutInflater");

    }

    public View getView(int position, View convertView, ViewGroup parent) {

        Movie movie = movies.get(position);
        View itemLayout = layoutInflater.inflate(R.layout.item_main_movie, null);

        TextView textViewMainMovieItem = (TextView) itemLayout.findViewById(R.id.textViewMainMovieItem);
        textViewMainMovieItem.setText(movie.getSubject());

        ImageView imageViewPicture = (ImageView) itemLayout.findViewById(R.id.imageViewPicture);

        //Try to get image from cache
        int ln = movie.url.split("/").length;
        String filename = movie.url.split("/")[ln - 1];

        File sdCard = Environment.getExternalStorageDirectory();
        File dir = new File (sdCard.getAbsolutePath() + "/tmp/mymovie/cache");
        File imgFile = new  File(dir, filename);

        if(imgFile.exists() && !filename.equals("")){
            Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
            imageViewPicture.setImageBitmap(myBitmap);
            Log.d(TAG, "image exist" + filename);
        } else {
            Log.d(TAG, "image not exist" + filename);
            imageViewPicture.setBackgroundResource(R.drawable.no_poster);
        }


        RatingBar ratingBar = (RatingBar) itemLayout.findViewById(R.id.ratingBar);
        ratingBar.setRating(movie.getRating());

        return itemLayout;
    }
}
