package model;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import org.json.JSONException;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

import presenters.UsersListUtils;


public class GitHttpRequest extends Thread {
    private static URL url;
    private static String httpResponse;
    private final Activity activity;
    private static int lastPositionForLoadAvatar = 0;

    public GitHttpRequest(Context activity, URL url) {
        this.activity = (Activity) activity;
        GitHttpRequest.url = url;
    }
    @Override
    public void run() {
        try {
            httpResponse = getGitUsers();
            UsersListUtils.fillInUsersList(activity, httpResponse);
            UsersListUtils.loadAvatars(activity, lastPositionForLoadAvatar, GitUsers.getUsersList());
            lastPositionForLoadAvatar = GitUsers.getUsersList().size();
            synchronized (this) {
                notify();
            }
        } catch (IOException | JSONException ioException) { }
    }
    private static synchronized String getGitUsers () throws IOException {
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        Log.d("myLogs", "Response = " + connection.getResponseCode());
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

    public static void resetLastPositionForLoadAvatar()
    {
        lastPositionForLoadAvatar = 0;
    }
}


