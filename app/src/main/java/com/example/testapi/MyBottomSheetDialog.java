package com.example.testapi;


import android.app.Dialog;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;


import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;


public class MyBottomSheetDialog extends BottomSheetDialogFragment {

    private Bitmap avatar;
    private String name;
    private String url;
    private Boolean needDismiss = false;
    public void setUserData(Bitmap avatar, String name, String url) {
        this.avatar = avatar;
        this.name = name;
        this.url = url;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        BottomSheetDialog dialog = new BottomSheetDialog(getActivity(), R.style.dialogStyle);
        View contentView = View.inflate(getContext(), R.layout.dialog_fragment, null);
        dialog.setContentView(contentView);
        dialog.setOnShowListener(dialog1 -> {
            FrameLayout frameLayout = ((BottomSheetDialog) dialog1).findViewById(R.id.design_bottom_sheet);
            BottomSheetBehavior.from(frameLayout).setState(BottomSheetBehavior.STATE_EXPANDED);
            BottomSheetBehavior.from(frameLayout).addBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
                @Override
                public void onStateChanged(@NonNull View bottomSheet, int newState) {
                    if(newState == BottomSheetBehavior.STATE_SETTLING ) {
                        BottomSheetBehavior.from(frameLayout).setState(BottomSheetBehavior.STATE_HIDDEN);
                        needDismiss = true;
                    }

                    if (newState == BottomSheetBehavior.STATE_COLLAPSED || (newState == BottomSheetBehavior.STATE_EXPANDED && needDismiss))
                        dismiss();
                }
                @Override
                public void onSlide(@NonNull View bottomSheet, float slideOffset) { }
            });
        });
        return dialog;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.dialog_fragment, container, false);
        ImageView avatar = layout.findViewById(R.id.avatar);
        avatar.setImageBitmap(this.avatar);
        avatar.setClipToOutline(true);
        ((TextView) layout.findViewById(R.id.userName)).setText(this.name);
        ((TextView) layout.findViewById(R.id.url)).setText(this.url);
        setOnClickGoToAccount(layout.findViewById(R.id.goToAccountButton));
        setOnClickGoToSearch(layout.findViewById(R.id.goToSearchButton));
        return layout;
    }

    private void setOnClickGoToAccount(Button button){
        button.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(this.url));
            startActivity(intent);
        });
    }
    private void setOnClickGoToSearch(Button button) {
        button.setOnClickListener(v -> {
            this.dismiss();
        });
    }

//    @Override
//    public void setupDialog(@NonNull Dialog dialog, int style) {
//        View contentView = View.inflate(getContext(), R.layout.dialog_fragment, null);
//        dialog.setContentView(contentView);
//    }
}
