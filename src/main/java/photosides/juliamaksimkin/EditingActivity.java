package photosides.juliamaksimkin;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;

public class EditingActivity extends Activity {

    private MoviesLogic moviesLogic;

    private Uri imageUri;
    private static final int TAKE_PICTURE = 115;
    private RatingBar ratingBar;

    private static String action;
    private int currentId;
    private ImageView imageViewPicture;
    private EditText editTextSubject;
    private EditText editTextBody;
    private EditText editTextURL;
    private CheckBox checkBox;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editing);
        moviesLogic = new MoviesLogic(this);
        moviesLogic.open();

        imageViewPicture = (ImageView) findViewById(R.id.imageViewPicture);
        editTextSubject = (EditText) findViewById(R.id.editTextSubject);
        editTextBody = (EditText) findViewById(R.id.editTextBody);
        editTextURL = (EditText) findViewById(R.id.editTextURL);
        checkBox = (CheckBox) findViewById(R.id.checkBox);
        ratingBar = (RatingBar) findViewById(R.id.ratingBar);
        listenerForRating();

        Intent intent = getIntent();
        action = intent.getExtras().getString("action");
        if (action.equals("edit") || action.equals("addFromSearch")) {
            int id = intent.getExtras().getInt("id");
            String _id = intent.getExtras().getString("_id");
            String subject = intent.getExtras().getString("subject");
            String body = intent.getExtras().getString("body");
            String url = intent.getExtras().getString("url");
            float rating = intent.getExtras().getFloat("rating");
            boolean watched = intent.getExtras().getBoolean("watched");

            currentId = id;

            editTextSubject.setText(subject);
            editTextBody.setText(body);
            editTextURL.setText(url);
            ratingBar.setRating(rating);
            checkBox.setChecked(watched);

            int ln = url.split("/").length;
            String filename = url.split("/")[ln - 1];

            File sdCard = Environment.getExternalStorageDirectory();
            File dir = new File (sdCard.getAbsolutePath() + "/tmp/mymovie/cache");
            File imgFile = new  File(dir, filename);

            if(imgFile.exists()){
                Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                imageViewPicture.setImageBitmap(myBitmap);
            } else {
                imageViewPicture.setBackgroundResource(R.drawable.no_poster_img);
            }
        }
    }

    @Override
    protected void onResume(){
        super.onResume();
    }

    @Override
    protected void onPause(){
        super.onPause();
    }

    public void buttonOK_onClick(View view) {

        Movie movie = new Movie();
        movie.setSubject(editTextSubject.getText().toString());
        movie.setBody(editTextBody.getText().toString());
        movie.setUrl(editTextURL.getText().toString());
        movie.setRating(ratingBar.getRating());
        movie.setWatched(checkBox.isChecked());

        if (movie.getSubject().equals("") || movie.getBody().equals("")) {
            Toast toast = Toast.makeText(this, "Error! Please enter a full information!", Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
        } else {
            if (action.equals("add") || action.equals("addFromSearch")) {
                long createdId = moviesLogic.addMovie(movie);
            } else {
                movie.setId(currentId);
                long affectedRows = moviesLogic.updateMovie(movie);
            }

            Intent intent = new Intent();
            setResult(RESULT_OK, intent);
            finish();
        }

    }

    public void buttonCancel_onClick(View view) {
        Intent intent = new Intent();
        setResult(RESULT_CANCELED, intent);
        finish();
    }

    public void buttonShow_onClick(View view) {

        String url = editTextURL.getText().toString();
        if (url.equals("N/A") || url.equals("")) {
            imageViewPicture.setBackgroundResource(R.drawable.no_poster_img);
        } else {
            try {
                new DownloadImageAsyncTask(this)
                        .execute(url);
            } catch (Exception e) {
            }
        }
    }

    public void imageButtonPhoto_onClick(View view) {
        Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
        File photoFile = new File(Environment.getExternalStorageDirectory(), "Photo.png");
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photoFile));
        imageUri = Uri.fromFile(photoFile);
        startActivityForResult(intent, TAKE_PICTURE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case TAKE_PICTURE:
                if (resultCode == RESULT_OK) {
                    Uri selectedImageUri = imageUri;
                    imageViewPicture.setImageURI(selectedImageUri);
                    Bitmap bitmap = ((BitmapDrawable)imageViewPicture.getDrawable()).getBitmap();
                    int ln = selectedImageUri.toString().split("/").length;
                    String filename = selectedImageUri.toString().split("/")[ln - 1];
                    File sdCard = Environment.getExternalStorageDirectory();
                    File dir = new File (sdCard.getAbsolutePath() + "/tmp/mymovie/cache");
                    dir.mkdirs();
                    File file = new File(dir, currentId + filename);

                    try {
                        FileOutputStream out = new FileOutputStream(file);
                        bitmap.compress(Bitmap.CompressFormat.PNG, 90, out);
                        out.flush();
                        out.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    editTextURL.setText(dir.toString() + "/" + currentId + filename);
                    Bitmap myBitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
                    imageViewPicture.setImageBitmap(myBitmap);
                }
        }
    }

    public void listenerForRating() {

        ratingBar.setOnRatingBarChangeListener(
            new RatingBar.OnRatingBarChangeListener() {

                @Override
                public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {}
            }
        );
    }
}