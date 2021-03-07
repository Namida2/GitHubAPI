package presenters;

import android.app.Activity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;

import model.GitHttpRequest;
import model.GitUsers;

import static com.example.testapi.MainActivity.adapter;
import static com.example.testapi.MainActivity.gitHttpRequest;

public class EditTextUtils {
    private static String currentText = "";
    public static void eddTextSettings (EditText editText)
    {
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                currentText = s.toString();
            }
            @Override
            public void afterTextChanged(Editable s) { }
        });

        editText.setOnKeyListener((v, keyCode, event) -> {
            if(event.getAction() == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER) {
                if ((gitHttpRequest == null || !gitHttpRequest.isAlive())) {
                    adapter.setLoadingVisibility(View.VISIBLE, false);
                    GitHttpRequest.resetLastPositionForLoadAvatar();
                    adapter.setUsersList(new ArrayList<>());
                    adapter.notifyDataSetChanged();
                    NetworkUtils.resetCurrentPage();
                    gitHttpRequest = new GitHttpRequest(v.getContext(),
                            NetworkUtils.getURL(editText.getText().toString()));
                    GitUsers.clearUsersList();
                    hideKeyboard((Activity) v.getContext());
                    gitHttpRequest.start();
                } else
                    Toast.makeText(v.getContext(), "Wait!", Toast.LENGTH_SHORT).show();
            }

            if (keyCode == KeyEvent.KEYCODE_DEL) {
                if (editText.getText().toString().length() > 1) {
                    editText.setText(editText.getText().toString().substring(0, editText.getText().length() - 1));
                } else
                    editText.setText("");
                editText.setSelection(editText.getText().toString().length());
            }
            return true;
        });
    }
    public static void hideKeyboard(Activity activity)
    {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        View view = activity.getCurrentFocus();
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    public static String getCurrentText() {
        return currentText;
    }

}
