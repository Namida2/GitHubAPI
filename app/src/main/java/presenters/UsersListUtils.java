package presenters;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.View;

import com.example.testapi.ErrorAlertDialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.util.List;

import model.GitUsers;

import static com.example.testapi.MainActivity.adapter;
import static com.example.testapi.MainActivity.fragmentManager;

public class UsersListUtils {

    public static synchronized void fillInUsersList(Activity activity, String httpResponse) throws JSONException {
        JSONObject jsonHttpResponse = new JSONObject(httpResponse);
        JSONArray jsonGitUsers = jsonHttpResponse.getJSONArray("items");
        if(jsonGitUsers.length() == 0) {
            ErrorAlertDialog dialog = ErrorAlertDialog.getNewInstance(ErrorAlertDialog.RESPONSE_EMPTY);
            activity.runOnUiThread(() -> {
                dialog.show(fragmentManager, "");
            });
            return;
        }

        for (int i = 0; i < jsonGitUsers.length(); ++i) {
            JSONObject object = jsonGitUsers.getJSONObject(i);
            GitUsers gitUser = new GitUsers(
                    object.getString("login"),
                    object.getString("avatar_url"),
                    object.getString("html_url")
            );
            GitUsers.addToListUsers(gitUser);
            Log.d("myLogs", "Size = " + Integer.toString( GitUsers.getUsersList().size()));
        }
        adapter.setUsersList(GitUsers.getUsersList());
    }

    public static synchronized void loadAvatars(Activity activity, int startIndex, List<GitUsers> usersList) throws IOException {
        activity.runOnUiThread(() -> adapter.setLoadingVisibility(View.GONE, true));
        for (int i = startIndex; i < usersList.size(); ++i) {
            URL avatarUrl = new URL(usersList.get(i).getUserAvatarURL());
            Bitmap bitmap = BitmapFactory.decodeStream(avatarUrl.openConnection().getInputStream());
            adapter.setAvatarForUser(bitmap, i);
            int finalI = i + 2;
            activity.runOnUiThread(() -> {
                adapter.notifyItemChanged(finalI);
            });
            Log.d("avatar", "Loaded Avatar For: " + finalI);
        }
    }

}
