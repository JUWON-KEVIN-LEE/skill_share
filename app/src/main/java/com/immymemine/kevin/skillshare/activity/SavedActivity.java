package com.immymemine.kevin.skillshare.activity;


import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.ImageButton;

import com.immymemine.kevin.skillshare.R;
import com.immymemine.kevin.skillshare.adapter.SavedRecyclerViewAdapter;
import com.immymemine.kevin.skillshare.model.user.SubscribedClass;
import com.immymemine.kevin.skillshare.model.user.User;
import com.immymemine.kevin.skillshare.utility.StateUtil;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Array;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class SavedActivity extends AppCompatActivity {

    RecyclerView savedRecyclerView;
    List<SubscribedClass> subscribedList;
    ImageButton toolbar_button_back;
    SavedRecyclerViewAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saved);

        initiateView();

        dataSetting();

        initRecyclerView();



    }

    private void dataSetting() {

        User user = StateUtil.getInstance().getUserInstance();
        subscribedList = new ArrayList();
        subscribedList = user.getSubscribedClasses();

    }

    private void initiateView() {
        savedRecyclerView = findViewById(R.id.recycler_view_saved);
        toolbar_button_back = findViewById(R.id.toolbar_button_back);
        toolbar_button_back.setOnClickListener(v -> finish());

    }

    private void initRecyclerView() {
        savedRecyclerView.setHasFixedSize(true);
        adapter = new SavedRecyclerViewAdapter(this, subscribedList);
        savedRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        savedRecyclerView.setAdapter(adapter);


    }









//    @Override
//    protected void onResume() {
//        super.onResume();
//        adapter.setData(subscribedList);
//
//    }
}


