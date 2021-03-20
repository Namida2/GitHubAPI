package presenters;

import android.accounts.NetworkErrorException;
import android.app.Activity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Scanner;

import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.observers.DisposableObserver;
import io.reactivex.rxjava3.schedulers.Schedulers;
import model.GitHttpRequest;
import model.GitUsers;

import static com.example.testapi.MainActivity.adapter;
import static com.example.testapi.MainActivity.floatingActionButton;
import static com.example.testapi.MainActivity.gitHttpRequest;

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

    public static void makeRequest(String name) {
        URL url = NetworkUtils.getURL(name);
        getRequestObservable(url)
                .subscribeOn(Schedulers.newThread())
                .subscribe(new DisposableObserver<String>() {
                    @Override
                    public void onNext(@NonNull String s) {

                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        Log.d("myLogs", e.toString());
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    private static Observable<String> getRequestObservable(URL url) {
        return Observable.create(emitter -> {
            HttpURLConnection connection = null;
            Scanner scanner;
            String next;
            try {
                connection = (HttpURLConnection) url.openConnection();
                Log.d("myLogs", "Response code: " + connection.getResponseCode());
                InputStream inputStream = connection.getInputStream();
                scanner = new Scanner(inputStream);
                scanner.useDelimiter("\\A");
                next = scanner.hasNext() ? scanner.next() : "";
                emitter.onNext(next);
            }
            catch (Throwable ex){
                emitter.onError(ex);
            }
            finally {
                connection.disconnect();
            }

        });
    }
}
