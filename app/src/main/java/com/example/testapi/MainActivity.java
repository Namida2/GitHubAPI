package com.example.testapi;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;

import android.text.Editable;
import android.text.SpannableStringBuilder;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

import com.example.testapi.adapters.UsersRecyclerViewAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.jakewharton.rxbinding4.widget.RxTextView;

import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.ObservableEmitter;
import io.reactivex.rxjava3.core.ObservableOnSubscribe;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.functions.BiFunction;
import io.reactivex.rxjava3.internal.observers.DisposableLambdaObserver;
import io.reactivex.rxjava3.observers.DisposableObserver;
import io.reactivex.rxjava3.schedulers.Schedulers;
import io.reactivex.rxjava3.subscribers.DisposableSubscriber;
import model.GitHttpRequest;
import model.GitUsers;
import presenters.RecyclerViewUtils;


public class MainActivity extends AppCompatActivity{

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

        recyclerView = findViewById(R.id.listRecyclerView);
        adapter = new UsersRecyclerViewAdapter(this, GitUsers.getUsersList());
        recyclerView.setAdapter(adapter);
        //RecyclerViewUtils.prepareRecyclerView(recyclerView, adapter);

        EditText editText = findViewById(R.id.search);

//        fragmentManager = getSupportFragmentManager();
//        floatingActionButton = findViewById(R.id.fba);
//        floatingActionButton.setOnClickListener(v -> {
//            adapter.setLoadingVisibility(View.GONE, false);
//            recyclerView.getLayoutManager().scrollToPosition(0);
//            Anim.Companion.hideView(floatingActionButton);
//        });
//        recyclerView = findViewById(R.id.listRecyclerView);
//        adapter = new UsersRecyclerViewAdapter(this, GitUsers.getUsersList());
//        recyclerView.setAdapter(adapter);
//        RecyclerViewUtils.prepareRecyclerView(recyclerView, adapter);
    }

}



