package model;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.View;

import com.example.testapi.ErrorAlertDialog;

import org.json.JSONException;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

import presenters.RecyclerViewUtils;
import presenters.UsersListUtils;

import static com.example.testapi.MainActivity.adapter;
import static com.example.testapi.MainActivity.fragmentManager;


public class GitHttpRequest extends Thread {
    private static URL url;
    private static String httpResponse;
    private final Activity activity;
    private static int lastPositionForLoadAvatar = 0;
    private int responseCode;

    public GitHttpRequest(Context activity, URL url) {
        this.activity = (Activity) activity;
        GitHttpRequest.url = url;
    }

    @Override
    public void run() {
        try {
            httpResponse = makeRequest();
            UsersListUtils.fillInUsersList(activity, httpResponse);
            UsersListUtils.loadAvatars(activity, lastPositionForLoadAvatar, GitUsers.getUsersList());
            lastPositionForLoadAvatar = GitUsers.getUsersList().size();
        } catch (IOException | JSONException ioException) {
            if(responseCode == 403){
                adapter.setLoadingVisibility(View.GONE, false);
                if(!ErrorAlertDialog.isExist()) {
                    ErrorAlertDialog dialog = ErrorAlertDialog.getNewInstance(ErrorAlertDialog.RESPONSE_403);
                    activity.runOnUiThread(() -> {
                        dialog.show(fragmentManager, "");
                    });
                }
            }
        }
        synchronized (this) {
            activity.runOnUiThread(() -> { adapter.setLoadingVisibility(View.GONE, true); });
            notify();
        }
    }

    private synchronized String makeRequest() throws IOException {
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        responseCode = connection.getResponseCode();
        Log.d("myLogs", "Response = " + responseCode);
        Scanner scanner;
        try {
            InputStream inputStream = connection.getInputStream();
            scanner = new Scanner(inputStream);
            scanner.useDelimiter("\\A");
            return scanner.hasNext() ? scanner.next() : null;
        } finally {
            connection.disconnect();
        }
    }
    public static void resetLastPositionForLoadAvatar () {
        lastPositionForLoadAvatar = 0;
    }
}


