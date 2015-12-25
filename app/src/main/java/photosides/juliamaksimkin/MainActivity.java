package photosides.juliamaksimkin;

import android.app.ActionBar;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.ContextMenu;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends ActionBarActivity implements PopupMenu.OnMenuItemClickListener {

    //!!!!!!!!!!!!!!!!добавила + поставила extends ActionBar вместо ListView
    ListView listView;


    private final static int REQUEST_CODE_ADDMOVIE = 1;
    private final static int REQUEST_CODE_FINDMOVIE = 2;
    private final static int REQUEST_CODE_EDITMOVIE = 3;

    private final static String TAG = "myLogs";

    //    private ArrayList<String> moviesList;
    private ArrayList<Movie> mainMoviesList;
    RelativeLayout relativeLayoutMain;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

/*
        ActionBar actionBar = getActionBar();
        actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#f78710")));
*/
        /*actionBar.setTitle("My Movies");*/

        refreshMovieList();

    }


    public boolean deleteMovie(Movie movie) {

        Log.d(TAG, "Start delete movie");

        DBHelper dbHelper = new DBHelper(this);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Log.d(TAG, "opened DB");

        String whereClause = "id" + "=?";
        String[] whereArgs = new String[]{String.valueOf(movie.getId())};

        int result = db.delete("movietable1", whereClause, whereArgs);
        db.close();
        Log.d(TAG, "close DB and cursor");
        if (result != 1) {
            return false;
        } else {
            return true;
        }

    }


    public void refreshMovieList() {

        Log.d(TAG, "Start refresh");

        DBHelper dbHelper = new DBHelper(this);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Log.d(TAG, "opened DB");

        //moviesList = new ArrayList<>();
        mainMoviesList = new ArrayList<>();

        Cursor c = db.query("movietable1", null, null, null, null, null, null);
        Log.d(TAG, "query done");

        while (c.moveToNext()) {
            Movie movie = new Movie();
            movie.setId(c.getInt(c.getColumnIndex("id")));
            movie.set_Id(c.getString(c.getColumnIndex("_id")));
            movie.setSubject(c.getString(c.getColumnIndex("subject")));
            movie.setBody(c.getString(c.getColumnIndex("body")));
            movie.setUrl(c.getString(c.getColumnIndex("url")));
            movie.setRating(c.getFloat(c.getColumnIndex("rating")));
            Log.d(TAG, "get result from cursor:" + movie.toString());

            mainMoviesList.add(movie);
            Log.d(TAG, "Added movie to arrayList");

        }

        c.close();
        db.close();
        Log.d(TAG, "close DB and cursor");









        //!!!!!!!!!! вот это добавила
        listView = (ListView) findViewById(R.id.listView);
        Log.d(TAG, "listView was found");


        MainMovieAdapter listAdapter = new MainMovieAdapter(this, mainMoviesList);
        Log.d(TAG, "listAdapter was created");

        listView.setAdapter(listAdapter);
        Log.d(TAG, "Adapter was given");

        registerForContextMenu(listView);
        Log.d(TAG, "Start to context menu");

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            public void onItemClick(AdapterView<?> arg0,
                                    View arg1, int position, long arg3) {
                Toast.makeText(MainActivity.this, "LIST ITEM POSITION " + position, Toast.LENGTH_SHORT).show();


                Movie clickedMovie = mainMoviesList.get(position);

                Intent intent = new Intent(MainActivity.this, EditingActivity.class);
                intent.putExtra("action", "edit");

                intent.putExtra("id", clickedMovie.id);
                intent.putExtra("_id", clickedMovie._id);
                intent.putExtra("subject", clickedMovie.subject);
                intent.putExtra("body", clickedMovie.body);
                intent.putExtra("url", clickedMovie.url);
                intent.putExtra("rating", clickedMovie.rating);

                Log.d(TAG, "try to start editing activity");
                startActivityForResult(intent, REQUEST_CODE_EDITMOVIE);



            }
        });




        return;








       /* MainMovieAdapter adapter = new MainMovieAdapter(this, mainMoviesList);
        setListAdapter(adapter);
        registerForContextMenu(getListView());
        return;*/
    }


    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;


        // было getListAdapter() вместо listView.getAdapter()
        String title = listView.getAdapter().getItem(info.position).toString();
        menu.setHeaderTitle(title);

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_context, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();

        Log.d(TAG, "long click id:" + item.getTitle() + " item from adapter:" + info.position);
        Movie clickedMovie = mainMoviesList.get(info.position);

        switch (item.getTitle().toString()) {
            case "Edit":

                Log.d(TAG, "context edit clickedMovie " + clickedMovie.toString());

                Intent intent = new Intent(this, EditingActivity.class);
                intent.putExtra("action", "edit");
                intent.putExtra("id", clickedMovie.id);
                intent.putExtra("_id", clickedMovie._id);
                intent.putExtra("subject", clickedMovie.subject);
                intent.putExtra("body", clickedMovie.body);
                intent.putExtra("url", clickedMovie.url);

                Log.d(TAG, "try to start editing activity from context");
                startActivityForResult(intent, REQUEST_CODE_EDITMOVIE);
                return true;

            case "Delete":
                Log.d(TAG, "context delete clickedMovie " + clickedMovie.toString());
                deleteMovie(clickedMovie);
                refreshMovieList();
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }


    //Нажимаем клавишу "+"
    public void imageButtonAdd_onClick(View view) {

        PopupMenu popup = new PopupMenu(this, view);
        popup.setOnMenuItemClickListener(this);
        popup.inflate(R.menu.menu_popup);
        popup.show();

    }

    //Открываем EditingActivity для добавления информации о новом кино
    public void addMovie() {

        Intent intent = new Intent(this, EditingActivity.class);
        intent.putExtra("action", "add");
        startActivityForResult(intent, REQUEST_CODE_ADDMOVIE);
        Log.d(TAG, "Send add intent to EditingActivity");

    }

    //Возвращаем информацию в ArrayList
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d(TAG, "activity result code=" + resultCode);
//        if (requestCode != RESULT_CANCELED){
        refreshMovieList();
        Log.d(TAG, "refreshing");

//        }
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

                Log.d(TAG, "Send intent to SearchActivity");
                return true;
            default:
                return false;

        }

    }

    //Жмем на клавишу "Опции" в верхнем правом углу
    /*public void imageButtonOptions_onClick(View view) {

    ImageButton imageButtonOptions = (ImageButton) findViewById(R.id.imageButtonOptions);
      imageButtonOptions.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
              Log.d(TAG, "button");
              openOptionsMenu();


          }
      });

    }*/

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
    public boolean onCreateOptionsMenu(Menu menu) {
        //return super.onCreateOptionsMenu(menu);

        getMenuInflater().inflate(R.menu.menu_options, menu);
        return true;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //

        switch (item.getItemId()) {

            case R.id.deleteAllMovies:

                Toast.makeText(this, "Delete All Movies!", Toast.LENGTH_SHORT).show();

                DBHelper dbHelper = new DBHelper(this);
                SQLiteDatabase db = dbHelper.getWritableDatabase();
                Log.d(TAG, "delete");
                int result = db.delete("movietable1", "1", null);
                db.close();
                Log.d(TAG, "close DB and cursor" + result);
                refreshMovieList();

                break;

            case R.id.exit:

                Toast.makeText(this, "Exit!", Toast.LENGTH_SHORT).show();
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);

    }







    // Код из интернета для Hard Button

    public boolean onKeyUp(int keyCode, KeyEvent event) {
        Log.d(TAG, "Hard button!" + keyCode);

        if (keyCode == KeyEvent.KEYCODE_MENU) {
            // ........
            return true;
        }
        return super.onKeyUp(keyCode, event);
    }



    /*@Override
    public boolean onKeyDown(int keycode, KeyEvent e) {

        Log.d(TAG, "Hard button!" + keycode);
        switch (keycode) {
            case KeyEvent.KEYCODE_MENU:



                return true;
        }

        return super.onKeyDown(keycode, e);
    }*/


    //для короткого нажатия



















    /*@Override*/
   /* protected void onListItemClick(ListView l, View v, int position, long id) {

        Movie clickedMovie = mainMoviesList.get(position);

        Log.d(TAG, "clickedMovie " + clickedMovie.toString());

        *//*Movie movie = getMovieFromDb(clickedMovie);*//*


        Intent intent = new Intent(this, EditingActivity.class);
        intent.putExtra("action", "edit");

        intent.putExtra("id", clickedMovie.id);
        intent.putExtra("_id", clickedMovie._id);
        intent.putExtra("subject", clickedMovie.subject);
        intent.putExtra("body", clickedMovie.body);
        intent.putExtra("url", clickedMovie.url);

        Log.d(TAG, "try to start editing activity");
        startActivityForResult(intent, REQUEST_CODE_EDITMOVIE);

    }*/
}