package photosides.juliamaksimkin;

import android.app.Dialog;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.PopupMenu;

import java.util.ArrayList;

public class MainActivity extends ActionBarActivity implements PopupMenu.OnMenuItemClickListener {


    private MoviesLogic moviesLogic;
    private ArrayList<Movie> mainMoviesList;
    ListView listView;
    private final static int REQUEST_CODE_ADDMOVIE = 1;
    private final static int REQUEST_CODE_FINDMOVIE = 2;
    private final static int REQUEST_CODE_EDITMOVIE = 3;
    private final static String TAG = "myLogs";

    private Dialog dialogDeleteMovie;
    private Dialog dialogDeleteAllMovies;
    private Movie clickedMovie;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        moviesLogic = new MoviesLogic(this);
        moviesLogic.open();
        refreshMovieList();
    }


    @Override
    protected void onResume(){
        super.onResume();
/*        moviesLogic.open();*/
        refreshMovieList();
    }

    @Override
    protected void onPause(){
        super.onPause();
     /*   moviesLogic.close();*/
    }

    public void deleteMovie(Movie movie) {
        long affectedRows = moviesLogic.deleteMovie(movie);
    }

    public void refreshMovieList() {
        mainMoviesList = moviesLogic.getAllMovies();
        listView = (ListView) findViewById(R.id.listView);
        MainMovieAdapter listAdapter = new MainMovieAdapter(this, mainMoviesList);
        listView.setAdapter(listAdapter);
        registerForContextMenu(listView);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            public void onItemClick(AdapterView<?> arg0,
                                    View arg1, int position, long arg3) {
                clickedMovie = mainMoviesList.get(position);
                Intent intent = new Intent(MainActivity.this, EditingActivity.class);
                intent.putExtra("action", "edit");
                intent.putExtra("id", clickedMovie.id);
                intent.putExtra("_id", clickedMovie._id);
                intent.putExtra("subject", clickedMovie.subject);
                intent.putExtra("body", clickedMovie.body);
                intent.putExtra("url", clickedMovie.url);
                intent.putExtra("rating", clickedMovie.rating);
                intent.putExtra("watched", clickedMovie.watched);
                startActivityForResult(intent, REQUEST_CODE_EDITMOVIE);
            }
        });
        return;
    }


    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;
        String title = listView.getAdapter().getItem(info.position).toString();
        menu.setHeaderTitle(title);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_context, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        clickedMovie = mainMoviesList.get(info.position);

        switch (item.getTitle().toString()) {
            case "Edit":
                Intent intent = new Intent(this, EditingActivity.class);
                intent.putExtra("action", "edit");
                intent.putExtra("id", clickedMovie.id);
                intent.putExtra("_id", clickedMovie._id);
                intent.putExtra("subject", clickedMovie.subject);
                intent.putExtra("body", clickedMovie.body);
                intent.putExtra("url", clickedMovie.url);
                intent.putExtra("rating", clickedMovie.rating);
                intent.putExtra("watched", clickedMovie.watched);
                startActivityForResult(intent, REQUEST_CODE_EDITMOVIE);
                return true;

            case "Delete":
                dialogDeleteMovie = new Dialog(this);
                dialogDeleteMovie.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialogDeleteMovie.setContentView(R.layout.dialog_delete_movie);
                dialogDeleteMovie.setCancelable(false);
                dialogDeleteMovie.show();
                return true;

            case "Check as Watched":
                if (clickedMovie.isWatched()) {
                    clickedMovie.setWatched(false);
                }else {
                    clickedMovie.setWatched(true);
                }
                long affectedRows = moviesLogic.updateMovie(clickedMovie);
                refreshMovieList();
                return true;

            default:
                return super.onContextItemSelected(item);
        }
    }

    public void imageButtonAdd_onClick(View view) {

        PopupMenu popup = new PopupMenu(this, view);
        popup.setOnMenuItemClickListener(this);
        popup.inflate(R.menu.menu_popup);
        popup.show();

    }

    public void addMovie() {

        Intent intent = new Intent(this, EditingActivity.class);
        intent.putExtra("action", "add");
        startActivityForResult(intent, REQUEST_CODE_ADDMOVIE);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        refreshMovieList();
        Log.d(TAG, "refreshing");
    }


    // Создаем и выводим на экран Popup Menu
    @Override
    public boolean onMenuItemClick(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.menuAddMovie:
                addMovie();
                return true;
            case R.id.menuFindMovie:
                Intent intent = new Intent(this, SearchActivity.class);
                startActivityForResult(intent, REQUEST_CODE_FINDMOVIE);
                return true;
            default:
                return false;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_options, menu);
        return true;
    }

    public void openOptionsMenu() {
        super.openOptionsMenu();
        Configuration config = getResources().getConfiguration();
        if ((config.screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) > Configuration.SCREENLAYOUT_SIZE_LARGE) {
            int originalScreenLayout = config.screenLayout;
            config.screenLayout = Configuration.SCREENLAYOUT_SIZE_LARGE;
            super.openOptionsMenu();
            config.screenLayout = originalScreenLayout;
        } else {
            super.openOptionsMenu();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case R.id.deleteAllMovies:
                dialogDeleteAllMovies = new Dialog(this);
                dialogDeleteAllMovies.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialogDeleteAllMovies.setContentView(R.layout.dialog_delete_all_movies);
                dialogDeleteAllMovies.setCancelable(false);
                dialogDeleteAllMovies.show();
                break;

            case R.id.exit:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public void buttonDeleteMovie_onClick(View view) {

        deleteMovie(clickedMovie);
        refreshMovieList();
        dialogDeleteMovie.dismiss();
    }

    public void buttonDeleteMovieCancel_onClick(View view) {
        dialogDeleteMovie.dismiss();
    }

    public void buttonDeleteAllMovies_onClick(View view) {
        dialogDeleteAllMovies.dismiss();
        moviesLogic.deleteAllMovies();
        refreshMovieList();
    }

    public void buttonDeleteAllMoviesCancel_onClick(View view) {
        dialogDeleteAllMovies.dismiss();
    }
}