package photosides.juliamaksimkin;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

public class DownloadImageAsyncTask extends AsyncTask<String, String, Bitmap>  {
    ImageView movieImage;
    private Activity activity;
    private ProgressBar progressBarLoad;
    private TextView textViewLoad;
    private static final String TAG = "myLogs";
    private String url;
    public DownloadImageAsyncTask(Activity activity) {
            this.activity = activity;
    }

    protected void onPreExecute() {
        progressBarLoad = (ProgressBar)activity.findViewById(R.id.progressBarLoad);
        textViewLoad = (TextView)activity.findViewById(R.id.textViewLoad);
        movieImage = (ImageView)activity.findViewById(R.id.imageViewPicture);
    }

    protected Bitmap doInBackground(String... urls) {
        url = urls[0];
        Bitmap picture = null;
        int count;

        try {
            int lengthOfFile = new java.net.URL(url).openConnection().getContentLength();
            InputStream input = new java.net.URL(url).openStream();
            OutputStream output = new FileOutputStream("/sdcard/tmp_picture.tmp");

            byte data[] = new byte[1024];
            long total = 0;
            while ((count = input.read(data)) != -1) {
                total += count;
                // publishing the progress....
                // After this onProgressUpdate will be called
                int percentDone = (int)(((long)total * 100) / lengthOfFile);
                publishProgress(Integer.toString(percentDone));
                output.write(data, 0, count);
            }

            output.flush();
            output.close();
            input.close();

            picture = BitmapFactory.decodeFile("/sdcard/tmp_picture.tmp");
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

        if(Integer.parseInt(percentDone) == 100) {
            textViewLoad.setText("100 %");
            textViewLoad.setText("");
        }
    }

    protected void onPostExecute(Bitmap result) {

        int ln = url.split("/").length;
        String filename = url.split("/")[ln - 1];
        File sdCard = Environment.getExternalStorageDirectory();
        File dir = new File (sdCard.getAbsolutePath() + "/tmp/mymovie/cache");
        dir.mkdirs();
        File file = new File(dir, filename);

        try {
            FileOutputStream out = new FileOutputStream(file);
            result.compress(Bitmap.CompressFormat.PNG, 90, out);
            out.flush();
            out.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

        movieImage.setImageBitmap(result);
    }
}