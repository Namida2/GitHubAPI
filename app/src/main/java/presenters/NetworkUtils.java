package presenters;

import android.net.Uri;
import android.util.Log;

import java.net.MalformedURLException;
import java.net.URL;

public class NetworkUtils {

    private static final String BASE_GITHUB_URL = "https://api.github.com/";
    private static final String SEARCH = "search/users";
    private static final String PARAM = "q";
    private static final String USER_AGENT = "User-Agent";
    private static final String REQUEST = "request";
    private static final String PAGE = "page";

    private static int currentPage = 1;

    public static URL getURL(String name) {
        URL url = null;
        Log.d("myLogs", "Current page = " + currentPage);
        Uri generateURI = Uri.parse(BASE_GITHUB_URL + SEARCH)
                .buildUpon()
                .appendQueryParameter(PARAM, name)
                .appendQueryParameter(USER_AGENT, REQUEST)
                .appendQueryParameter(PAGE, Integer.toString(currentPage++))
                .build();
        try {
            url = new URL(generateURI.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        return url;
    }

    public static void resetCurrentPage()
    {
        currentPage = 1;
    }
    public static int getCurrentPage()
    {
        return NetworkUtils.currentPage;
    }
}


