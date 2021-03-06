package com.example.testapi;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

public class ErrorAlertDialog extends DialogFragment {
    private static boolean isExist = false;

    public static boolean isExist() {
        return isExist;
    }

    public static synchronized ErrorAlertDialog getNewInstance () {
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
        Button button = view.findViewById(R.id.ok);
        button.setOnClickListener( v -> {
            dismiss();
            isExist = false;
        });
        builder.setView(view);
        return builder.create();
    }

}
