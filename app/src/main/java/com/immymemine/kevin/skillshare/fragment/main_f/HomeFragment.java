package com.immymemine.kevin.skillshare.fragment.main_f;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.immymemine.kevin.skillshare.R;
import com.immymemine.kevin.skillshare.activity.SearchActivity;
import com.immymemine.kevin.skillshare.adapter.main_adapter.HomeRecyclerViewAdapter;
import com.immymemine.kevin.skillshare.model.home.Class;
import com.immymemine.kevin.skillshare.network.RetrofitHelper;
import com.immymemine.kevin.skillshare.network.api.HomeService;
import com.immymemine.kevin.skillshare.utility.StateUtil;
import com.immymemine.kevin.skillshare.view.ViewFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * A simple {@link Fragment} subclass.
 */

public class HomeFragment extends Fragment {

    Context context;
    HomeRecyclerViewAdapter adapter;

    FrameLayout welcomeViewContainer;
    View welcomeView;

    Map<String, List<Class>> addFollowSkills;
    List<String> followSkills;

    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        context = getActivity();

        view.findViewById(R.id.toolbar_button_search)
                .setOnClickListener(v -> context.startActivity(new Intent(context, SearchActivity.class)));

        welcomeViewContainer = view.findViewById(R.id.welcome_view_container);

        if (!StateUtil.getInstance().getState()) {
            Log.d("JUWONLEE", "sign in welcome view");
            if(getArguments() == null) {
                Log.d("JUWONLEE", "arguments null");
                welcomeView = ViewFactory.getInstance(context).getWelcomeView();
                welcomeView.findViewById(R.id.close_button).setOnClickListener(
                        v -> welcomeViewContainer.setVisibility(View.GONE)
                );
                welcomeViewContainer.addView(welcomeView);
            }
        } else {
            welcomeViewContainer.setVisibility(View.GONE);
        }



        if(StateUtil.getInstance().getState()) {
            if(StateUtil.getInstance().getUserInstance().getFollowingSkills() != null) {
                followSkills = StateUtil.getInstance().getUserInstance().getFollowingSkills();

            } else {
                followSkills = new ArrayList<>();
            }
        } else {
            followSkills = new ArrayList<>();
        }


        RetrofitHelper.createApi(HomeService.class)
                .getHomeClasses(followSkills)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::handleResponse, this::handleError);

        // RecyclerView setting
        RecyclerView recyclerView = view.findViewById(R.id.recycler_view_home);
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        adapter = new HomeRecyclerViewAdapter(context);
        recyclerView.setAdapter(adapter);

        return view;
    }


    private void handleResponse(List<Map<String, List<Class>>> classes) {
        adapter.update(classes);
    }

    private void handleError(Throwable error) {

    }
}
