package presenters;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import com.example.testapi.ErrorAlertDialog;
import com.example.testapi.MyException;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.observers.DisposableObserver;
import io.reactivex.rxjava3.schedulers.Schedulers;
import model.GitUsers;

import static com.example.testapi.MainActivity.adapter;
import static com.example.testapi.MainActivity.fragmentManager;

public class EditTextUtils {
//    private static String currentText = "";
//    public static void eddTextSettings (EditText editText) {
//        editText.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//                currentText = s.toString();
//            }
//            @Override
//            public void afterTextChanged(Editable s) { }
//        });
//
//        editText.setOnKeyListener((v, keyCode, event) -> {
//            if(event.getAction() == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER) {
//                if ((gitHttpRequest == null || !gitHttpRequest.isAlive())) {
//                    adapter.setLoadingVisibility(View.VISIBLE, false);
//                    GitHttpRequest.resetLastPositionForLoadAvatar();
//                    adapter.setUsersList(new ArrayList<>());
//                    adapter.notifyDataSetChanged();
//                    NetworkUtils.resetCurrentPage();
//                    gitHttpRequest = new GitHttpRequest(v.getContext(),
//                            NetworkUtils.getURL(editText.getText().toString()));
//                    GitUsers.clearUsersList();
//                    hideKeyboard((Activity) v.getContext());
//                    gitHttpRequest.start();
//                } else
//                    Toast.makeText(v.getContext(), "Wait!", Toast.LENGTH_SHORT).show();
//            }
//
//            if (keyCode == KeyEvent.KEYCODE_DEL) {
//                if (editText.getText().toString().length() > 1) {
//                    editText.setText(editText.getText().toString().substring(0, editText.getText().length() - 1));
//                } else
//                    editText.setText("");
//                editText.setSelection(editText.getText().toString().length());
//            }
//            return true;
//        });
//    }
//
//    public static void hideKeyboard(Activity activity)
//    {
//        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
//        View view = activity.getCurrentFocus();
//        if (view == null) {
//            view = new View(activity);
//        }
//        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
//    }
//
//    public static String getCurrentText() {
//        return currentText;
//    }
//

    private static final String JSON_ARRAY_NAME = "items";
    private static final String JSON_LOGIN = "login";
    private static final String JSON_AVATAR_URL = "avatar_url";
    private static final String JSON_HTML_URL = "html_url";
    private static final AtomicBoolean processAlreadyExist =  new AtomicBoolean(false);
    private static final AtomicBoolean needToStopTheLoading = new AtomicBoolean(false);

    private static final AtomicInteger waitingThreads = new AtomicInteger(0);

    public static void makeRequest(String name) {
        URL url = NetworkUtils.getURL(name);
        waitingThreads.getAndIncrement();
        if(processAlreadyExist.get()) needToStopTheLoading.set(true);
        Disposable dis = getRequestObservable(url)
                .delay(1200, TimeUnit.MILLISECONDS)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .flatMap(EditTextUtils::fillInUsersList)
                .map(usersList -> { adapter.setUsersList(usersList); return usersList; })
                .flatMap(usersList -> loadAvatars(usersList)
                        .subscribeOn(Schedulers.newThread())
                        .observeOn(AndroidSchedulers.mainThread()))
                .subscribe( nextInt -> {
                    final int position = nextInt + adapter.ADDITIONAL_SIZE;
                    adapter.notifyItemChanged(position);

                }, error -> {
                    ErrorAlertDialog errorAlertDialog = null;
                    switch (Objects.requireNonNull(error.getMessage())) {
                        case MyException.RESPONSE_403_MESSAGE:
                            errorAlertDialog = ErrorAlertDialog.getNewInstance(ErrorAlertDialog.RESPONSE_403); break;
                        case MyException.RESPONSE_EMPTY_MESSAGE:
                            errorAlertDialog = ErrorAlertDialog.getNewInstance(ErrorAlertDialog.RESPONSE_EMPTY); break;
                        case MyException.INTERNET_CONNECTION_MESSAGE:
                            errorAlertDialog = ErrorAlertDialog.getNewInstance(ErrorAlertDialog.INTERNET_CONNECTION); break;
                        case MyException.SSLHandshakeExceptionChainValidationFailed:
                            errorAlertDialog = ErrorAlertDialog.getNewInstance(ErrorAlertDialog.SSLHandshakeExceptionChainValidationFailed); break;
                        default: break;
                    }
                    if(errorAlertDialog != null)  errorAlertDialog.show(fragmentManager, "");
                    Log.d( "myLogs", Objects.requireNonNull(error.getMessage()));
                    if(waitingThreads.get() <= 1) processAlreadyExist.set(false);
                    waitingThreads.getAndDecrement();
                }, () -> {
                    Log.d("myLogs", "COMPLETE!");
                    if(waitingThreads.get() <= 1) processAlreadyExist.set(false);
                    waitingThreads.getAndDecrement();
                });
    }

    private static Observable<Integer> loadAvatars(List<GitUsers> usersList) {
        return Observable.create(emitter -> {
            Log.d("myLogs", "Thread: " + Thread.currentThread().getName());
            synchronized (EditTextUtils.class) {
                for (int i = 0; i < usersList.size(); ++i) {
                    if (needToStopTheLoading.get()) {
                        needToStopTheLoading.set(false);
                        Log.d("myLogs", "Loading is stopped.");
                        break;
                    }
                    URL avatarUrl = new URL(usersList.get(i).getUserAvatarURL());
                    Bitmap bitmap = BitmapFactory.decodeStream(avatarUrl.openConnection().getInputStream());
                    adapter.setAvatarForUser(bitmap, i);
                    Log.d("myLogs", "Loaded avatar For: " + i);
                    emitter.onNext(i);
                }
            }
            emitter.onComplete();
        });
    }

    private static Observable<List<GitUsers>> fillInUsersList (String httpResponse) {
        return Observable.create(emitter -> {
            synchronized (EditTextUtils.class) {
                JSONObject jsonHttpResponse = new JSONObject(httpResponse);
                JSONArray jsonArray = jsonHttpResponse.getJSONArray(JSON_ARRAY_NAME);
                if (jsonArray.length() == 0) {
                    emitter.onError(new MyException(MyException.RESPONSE_EMPTY_MESSAGE));
                } else {
                    List<GitUsers> listGitUser = new ArrayList<>();
                    for (int i = 0; i < jsonArray.length(); ++i) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        GitUsers gitUsers = new GitUsers(
                                jsonObject.getString(JSON_LOGIN),
                                jsonObject.getString(JSON_AVATAR_URL),
                                jsonObject.getString(JSON_HTML_URL)
                        );
                        listGitUser.add(gitUsers);
                    }
                    emitter.onNext(listGitUser);
                    Log.d("myLogs", "Size listGitUsers: " + listGitUser.size());
                }
                Log.d("myLogs", "------------------------------------");
                Log.d("myLogs", "Waiting thread: " + waitingThreads.get());
                Log.d("myLogs", "needToStopTheLoading: " + needToStopTheLoading.get());
                Log.d("myLogs", "processAlreadyExist: " + processAlreadyExist.get());
                Log.d("myLogs", "-------------------------------------");
                if (waitingThreads.get() > 1 && !needToStopTheLoading.get() && processAlreadyExist.get())
                    needToStopTheLoading.set(true);
                Log.d("myLogs", "------------------------------------");
                Log.d("myLogs", "Waiting thread: " + waitingThreads.get());
                Log.d("myLogs", "needToStopTheLoading: " + needToStopTheLoading.get());
                Log.d("myLogs", "processAlreadyExist: " + processAlreadyExist.get());
                Log.d("myLogs", "-------------------------------------");
                emitter.onComplete();

            }
        });
    }

    private static  Observable<String> getRequestObservable(URL url) {
        return Observable.create(emitter -> {
            synchronized (EditTextUtils.class) {
                processAlreadyExist.set(true);
                HttpURLConnection connection = null;
                int responseCode;
                Scanner scanner;
                String next;
                try {
                    connection = (HttpURLConnection) url.openConnection();
                    responseCode = connection.getResponseCode();
                    Log.d("myLogs", "Response code: " + responseCode);
                    if (responseCode == 403){
                        emitter.onError(new MyException(MyException.RESPONSE_403_MESSAGE));
                        return;
                    }
                    InputStream inputStream = connection.getInputStream();
                    scanner = new Scanner(inputStream);
                    scanner.useDelimiter("\\A");
                    next = scanner.hasNext() ? scanner.next() : "";
                    emitter.onNext(next);
                }
                catch (Throwable ex){
                    emitter.onError(new MyException(MyException.INTERNET_CONNECTION_MESSAGE));
                }
                finally {
                    //assert connection != null;
                    connection.disconnect();
                }
            }
            emitter.onComplete();
        });
    }

}
