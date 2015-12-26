package photosides.juliamaksimkin;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.util.Log;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class RequestAsyncTask extends AsyncTask<URL, Void, String> {
    private Activity activity;
    private ProgressDialog dialog;
    private static final String TAG = "myLogs";
    public AsyncResponse delegate = null;

    public RequestAsyncTask(Activity activity) {
        this.activity = activity;
    }

    protected void onPreExecute() {
        dialog = new ProgressDialog(activity);
        dialog.setTitle("Connecting...");
        dialog.setMessage("Please Wait...");
        dialog.show();
    }

    protected String doInBackground(URL... params) {
        try {
            URL url = params[0];
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            int httpStatusCode = connection.getResponseCode();
            if (httpStatusCode == HttpURLConnection.HTTP_BAD_REQUEST) {
                return "No Such Symbol";
            }
            if (httpStatusCode != HttpURLConnection.HTTP_OK) {
                return "Error Code: " + httpStatusCode + "\nError Message: " + connection.getResponseMessage();
            }

            InputStream inputStream = connection.getInputStream();
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

            String result = "";
            String oneLine = bufferedReader.readLine();

            while (oneLine != null) {
                result += oneLine + "\n";
                oneLine = bufferedReader.readLine();
            }

            return result;
        } catch (Exception ex) {
            return "Error: " + ex.getMessage();
        }
    }

    @Override
    protected void onPostExecute(String response) {
        dialog.dismiss();
        delegate.processFinish(response);
    }
}