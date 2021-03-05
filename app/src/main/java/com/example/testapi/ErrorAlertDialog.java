package com.example.testapi;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

public class ErrorAlertDialog extends DialogFragment {

    public static ErrorAlertDialog getNewInstance () {
        ErrorAlertDialog errorAlertDialog = new ErrorAlertDialog();
        return errorAlertDialog;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext(), R.style.test);
        builder.setView(View.inflate(getActivity(), R.layout.error_dialog, null));
        return builder.create();
    }

}
