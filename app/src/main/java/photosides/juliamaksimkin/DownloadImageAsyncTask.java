package photosides.juliamaksimkin;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.io.BufferedInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;

public class DownloadImageAsyncTask extends AsyncTask<String, String, Bitmap>  {
    ImageView movieImage;
//        private ProgressDialog dialog;
//        private Activity activity;
    private Activity activity;
    private ProgressBar progressBarLoad;
    private TextView textViewLoad;
    private static final String TAG = "myLogs";

    public DownloadImageAsyncTask(Activity activity) {
            this.activity = activity;
    }


    protected void onPreExecute() {
        progressBarLoad = (ProgressBar)activity.findViewById(R.id.progressBarLoad);
        textViewLoad = (TextView)activity.findViewById(R.id.textViewLoad);
        movieImage = (ImageView)activity.findViewById(R.id.imageViewPicture);
    }

    protected Bitmap doInBackground(String... urls) {
        String urldisplay = urls[0];
        Bitmap mIcon11 = null;
        Bitmap picture = null;
        int count;

        try {
            int lenghtOfFile = new java.net.URL(urldisplay).openConnection().getContentLength();

            Log.d(TAG, "Size of image:" + lenghtOfFile);

            InputStream input = new java.net.URL(urldisplay).openStream();

            OutputStream output = new FileOutputStream("/sdcard/tmp_picture.tmp");


            byte data[] = new byte[1024];
            long total = 0;
            while ((count = input.read(data)) != -1) {
                total += count;
                // publishing the progress....
                // After this onProgressUpdate will be called

                int percentDone = (int)(((long)total * 100) / lenghtOfFile);

                publishProgress(Integer.toString(percentDone));

                output.write(data, 0, count);

            }

            output.flush();
            // closing streams
            output.close();
            input.close();

            Log.d(TAG, "before decode stream");

            picture = BitmapFactory.decodeFile("/sdcard/tmp_picture.tmp");
//            mIcon11 = BitmapFactory.decodeStream(input);
            Log.d(TAG, "after decode stream");
        } catch (Exception e) {
            Log.e("Error", e.getMessage());
            e.printStackTrace();
        }

        return picture;
    }

    protected void onProgressUpdate(String... values) {
        String percentDone = values[0];
        progressBarLoad.setProgress(Integer.parseInt(percentDone));
        textViewLoad.setText(percentDone + " %");
        Log.d(TAG, "progress update");
    }

    protected void onPostExecute(Bitmap result) {
//            dialog.dismiss();
        Log.d(TAG, "postexec");
        movieImage.setImageBitmap(result);
    }
}