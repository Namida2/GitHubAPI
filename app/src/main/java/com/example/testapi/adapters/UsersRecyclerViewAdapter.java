package com.example.testapi.adapters;

import android.app.Activity;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.example.testapi.Anim;
import com.example.testapi.R;
import com.example.testapi.MyBottomSheetDialog;
import com.jakewharton.rxbinding4.widget.RxTextView;

import java.security.Key;
import java.util.List;
import java.util.Objects;

import io.reactivex.rxjava3.disposables.Disposable;
import model.GitUsers;
import presenters.EditTextUtils;

import static com.example.testapi.MainActivity.fragmentManager;
import static com.example.testapi.MainActivity.gitHttpRequest;
import static com.example.testapi.MainActivity.recyclerView;

public class UsersRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final int ADDITIONAL_SIZE = 2;
    private int loadingState = View.GONE;

    private List<GitUsers> usersList;
    private Activity activity;

    public UsersRecyclerViewAdapter(Activity activity, List<GitUsers> usersList) {
        this.activity = activity;
        this.usersList = usersList;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
    @Override
    public int  getItemCount() {
        return usersList.size() + ADDITIONAL_SIZE;
    }
    @Override
    public int  getItemViewType(int position) {
        switch (position){
            case 0: return 0;
            case 1: return 1;
        }
        if (position == usersList.size() + ADDITIONAL_SIZE - 1){
            setLoadingVisibility(View.VISIBLE, false);
            return 1;
        }
        setLoadingVisibility(View.GONE, false);
        return 2;
    }
    public void setAvatarForUser(Bitmap avatar, int position) {
        usersList.get(position).setUserAvatar(avatar);
    }
    public void setUsersList(List<GitUsers> usersList) {
        this.usersList = usersList;
    }
    public List<GitUsers> getUsersList() {
        return usersList;
    }

    static class ViewHolderHead extends RecyclerView.ViewHolder {
        private final EditText searchBar;
        public ViewHolderHead(@NonNull View itemView) {
            super(itemView);
            searchBar = itemView.findViewById(R.id.search);
            Disposable dis = RxTextView.editorActionEvents(searchBar)
                    .map(item -> item.getView().getText().toString())
                    .subscribe(EditTextUtils::makeRequest);
        }
    }

    static class ViewHolderItems extends RecyclerView.ViewHolder {
        private final ImageView userAvatar;
        private final Button userName;
        private final View currentView;
        public ViewHolderItems(@NonNull View itemView) {
            super(itemView);
            currentView = itemView;
            userAvatar = itemView.findViewById(R.id.userAvatar);
            userAvatar.setClipToOutline(true);
            userName = itemView.findViewById(R.id.userName);
        }
    }

    static class ViewHolderLoading extends RecyclerView.ViewHolder {
        ProgressBar progressBar;
        public ViewHolderLoading(@NonNull View itemView) {
            super(itemView);
            progressBar = itemView.findViewById(R.id.progress);
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view;
        switch (viewType) {
            case 0:
                view = inflater.inflate(R.layout.head_recucler_view, parent, false);
                return new ViewHolderHead(view);
            case 1:
                view = inflater.inflate(R.layout.loading, parent, false);
                return new ViewHolderLoading(view);
            case 2:
                view = inflater.inflate(R.layout.user_card, parent, false);
                return new ViewHolderItems(view);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        switch (holder.getItemViewType()) {
            case 0:
                //((ViewHolderHead) holder).searchBar.setText(EditTextUtils.getCurrentText());
                //((ViewHolderHead) holder).searchBar.setSelection(EditTextUtils.getCurrentText().length());
                break;
            case 1:
                Log.d("visibility", "visibility: " + loadingState);
                if(loadingState == View.VISIBLE) {
                    ((ViewHolderLoading) holder).progressBar.setVisibility(loadingState);
                    Anim.Companion.showView(((ViewHolderLoading) holder).progressBar);
                }
                else if (loadingState == View.GONE) {
                    Anim.Companion.hideView(((ViewHolderLoading) holder).progressBar);
                    ((ViewHolderLoading) holder).progressBar.setVisibility(loadingState);
                }
                break;
            case 2:
                position = position - ADDITIONAL_SIZE;
                Log.d("avatar", "Notify: " + position);
                if (usersList.get(position) != null) {
                    if (usersList.get(position).getUserAvatar() == null) {
                        ((ViewHolderItems) holder).userAvatar.setImageResource(R.drawable.ic_launcher_foreground);
                        Anim.Companion.startAnim(((ViewHolderItems)holder).currentView);
                    }
                    else
                        ((ViewHolderItems) holder).userAvatar.setImageBitmap(usersList.get(position).getUserAvatar());
                    ((ViewHolderItems) holder).userName.setText(usersList.get(position).getUserName());
                    setOnClick(((ViewHolderItems)holder).userName, position);
                }
        }
    }

    public void setLoadingVisibility(int visibility, Boolean notify){
        loadingState = visibility;
        if (notify) this.notifyItemChanged(1);
    }

    private void setOnClick (Button button, int position) {
        button.setOnClickListener(v -> {
            if(usersList.get(position).getUserAvatar() != null) {
                MyBottomSheetDialog dialog = new MyBottomSheetDialog();
                dialog.setUserData(
                        usersList.get(position).getUserAvatar(),
                        usersList.get(position).getUserName(),
                        usersList.get(position).getUserHtmlURL());
                dialog.show(fragmentManager, "");
            }
            else
                Toast.makeText(v.getContext(), "Wait for the avatar to load", Toast.LENGTH_SHORT).show();
            }
        );
    }
}
