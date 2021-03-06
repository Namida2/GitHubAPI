package com.example.testapi;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModel;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;

import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;

import com.example.testapi.adapters.UsersRecyclerViewAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import model.GitHttpRequest;
import model.GitUsers;
import presenters.RecyclerViewUtils;

public class MainActivity extends AppCompatActivity {

    @SuppressLint("StaticFieldLeak")
    public static volatile GitHttpRequest gitHttpRequest;
    @SuppressLint("StaticFieldLeak")
    public static UsersRecyclerViewAdapter adapter;
    public static FragmentManager fragmentManager;

    public static RecyclerView recyclerView;
    public static FloatingActionButton floatingActionButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        fragmentManager = getSupportFragmentManager();
        floatingActionButton = findViewById(R.id.fba);
        floatingActionButton.setOnClickListener(v -> {
            adapter.setLoadingVisibility(View.GONE, false);
            recyclerView.getLayoutManager().scrollToPosition(0);
            Anim.Companion.hideView(floatingActionButton);
        });
        recyclerView = findViewById(R.id.listRecyclerView);
        adapter = new UsersRecyclerViewAdapter(this, GitUsers.getUsersList());
        recyclerView.setAdapter(adapter);
        RecyclerViewUtils.prepareRecyclerView(recyclerView, adapter);
    }
}



