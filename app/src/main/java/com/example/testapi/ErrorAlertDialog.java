package com.example.testapi;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

public class ErrorAlertDialog extends DialogFragment {
    public static final int RESPONSE_403 = 0;
    public static final int RESPONSE_EMPTY = 1;
    private static int dialogType;
    private static boolean isExist = false;
    public static boolean isExist() {
        return isExist;
    }

    public static synchronized ErrorAlertDialog getNewInstance (int dialogType) {
        ErrorAlertDialog.dialogType = dialogType;
        ErrorAlertDialog errorAlertDialog = new ErrorAlertDialog();
        errorAlertDialog.setCancelable(false);
        isExist = true;
        return errorAlertDialog;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext(), R.style.test);
        View view = View.inflate(getActivity(), R.layout.error_dialog, null);
        TextView responseCode = view.findViewById(R.id.responseCode);
        TextView text = view.findViewById(R.id.text);
        Button button = view.findViewById(R.id.ok);
        button.setOnClickListener( v -> {
            dismiss();
            isExist = false;
        });
        if (dialogType == RESPONSE_EMPTY) {
            responseCode.setText(R.string.empty_result_title);
            text.setText(R.string.empty_result_text);
        }
        builder.setView(view);
        return builder.create();
    }

}
