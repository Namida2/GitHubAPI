package model;

import android.graphics.Bitmap;

import java.util.ArrayList;
import java.util.List;

public class GitUsers {

    private final String userName;
    private Bitmap userAvatar = null;
    private final String userAvatarURL;
    private final String userHtmlURL;

    private static List<GitUsers> usersList = new ArrayList<>();

    public GitUsers(String userName, String userAvatarURL, String userURL) {
        this.userName = userName;
        this.userAvatarURL = userAvatarURL;
        this.userHtmlURL = userURL;
    }
    public void setUserAvatar(Bitmap userAvatar) {
        this.userAvatar = userAvatar;
    }
    public Bitmap getUserAvatar()                {
        return userAvatar;
    }
    public String getUserName() {
        return userName;
    }
    public static void addToListUsers(GitUsers gitUser) {
        usersList.add(gitUser);
    }
    public static void clearUsersList ()
    {
        GitUsers.usersList = new ArrayList<>();
    }
    public static List<GitUsers> getUsersList ()
    {
       return usersList;
    }
    public String getUserAvatarURL() {
        return userAvatarURL;
    }
    public String getUserHtmlURL() {
        return userHtmlURL;
    }
}
