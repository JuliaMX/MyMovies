package photosides.juliamaksimkin;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.Toast;

import java.io.File;

public class EditingActivity extends AppCompatActivity {


    // Photo
    Uri imageUri;
    private static final int TAKE_PICTURE = 115;

    private RatingBar ratingBar;

    private static String action;
    private int currentId;
    private String current_Id = "";
    private ImageView imageViewPicture;

    private static final String TAG = "myLogs";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editing);

        imageViewPicture = (ImageView) findViewById(R.id.imageViewPicture);

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

            Log.d(TAG, "Starting editActivity with action:" + action + " and item " + subject);

            //Saving current ID of movie for successful update SQL Table in future
            currentId = id;
            //Saving IMDB_ID
            current_Id = _id;

            EditText editTextSubject = (EditText) findViewById(R.id.editTextSubject);
            EditText editTextBody = (EditText) findViewById(R.id.editTextBody);
            EditText editTextURL = (EditText) findViewById(R.id.editTextURL);

            //Fill text fields
            editTextSubject.setText(subject);
            editTextBody.setText(body);
            editTextURL.setText(url);
            ratingBar.setRating(rating);

        }
    }


    public void buttonOK_onClick(View view) {
        EditText editTextSubject = (EditText) findViewById(R.id.editTextSubject);
        EditText editTextBody = (EditText) findViewById(R.id.editTextBody);
        EditText editTextURL = (EditText) findViewById(R.id.editTextURL);


        Log.d(TAG, "pressed button OK, action is: " + action);

        DBHelper dbHelper = new DBHelper(this);

        // создаем объект для данных
        ContentValues cv = new ContentValues();

        // получаем данные из полей ввода
        String subject = editTextSubject.getText().toString();
        String body = editTextBody.getText().toString();
        String url = editTextURL.getText().toString();
        float rating = ratingBar.getRating();


        //Делаем проверку все ли поля ввода заполнены

        if (subject.equals("") || body.equals("")) {
            Toast.makeText(this, "Error! Please enter a full information!", Toast.LENGTH_SHORT).show();
        } else {

            // подключаемся к БД
            SQLiteDatabase db = dbHelper.getWritableDatabase();

            // подготовим данные для вставки в виде пар: наименование столбца - значение

            cv.put("subject", subject);
            cv.put("body", body);
            cv.put("url", url);
            cv.put("_id", current_Id);
            cv.put("rating", rating);

            if (action.equals("add") || action.equals("addFromSearch")) {
                Log.d(TAG, "--- Insert in movietable: ---");
                // вставляем запись и получаем ее ID
                long rowID = db.insert("movietable1", null, cv);
                Log.d(TAG, "row inserted, ID = " + rowID);

            } else {

                Log.d(TAG, "Need to update row ID = " + currentId);
                int updCount = db.update("movietable1", cv, "id = ?",
                        new String[]{Integer.toString(currentId)});

                Log.d(TAG, "Update done, updCount= " + updCount);
            }

            // закрываем подключение к БД

            dbHelper.close();

            Intent intent = new Intent();
            //intent.putExtra("Subject", subject);
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
        EditText editTextURL = (EditText) findViewById(R.id.editTextURL);
        String url = editTextURL.getText().toString();

        if(url.equals("N/A") || url.equals("")) {

           imageViewPicture.setBackgroundResource(R.drawable.no_poster);
        } else {
            try {
                new DownloadImageAsyncTask(this)
                        .execute(url);
            }catch (Exception e) {

                Log.d(TAG, "Bad request");

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

                    ImageView imageViewPicture = (ImageView) findViewById(R.id.imageViewPicture);
                    imageViewPicture.setImageURI(selectedImageUri);

                    EditText editTextURL = (EditText) findViewById(R.id.editTextURL);
                    editTextURL.setText(selectedImageUri.toString());

                    Log.d(TAG, "URI: " + selectedImageUri);
                }
        }
    }

    public void listenerForRating() {


        ratingBar.setOnRatingBarChangeListener(
                new RatingBar.OnRatingBarChangeListener() {

                    @Override
                    public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {

                        Toast.makeText(EditingActivity.this, "Rating: " + String.valueOf(rating), Toast.LENGTH_SHORT).show();

                    }
                }
        );

    }

}
