package com.immymemine.kevin.skillshare.adapter.main_adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.immymemine.kevin.skillshare.R;
import com.immymemine.kevin.skillshare.activity.SeeAllActivity;
import com.immymemine.kevin.skillshare.model.home.Class;
import com.immymemine.kevin.skillshare.utility.ConstantUtil;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by quf93 on 2017-12-25.
 */

public class HomeRecyclerViewAdapter extends RecyclerView.Adapter<HomeRecyclerViewAdapter.Holder> {

    Context context;
    List<Map<String, List<Class>>> classes;
    Map<String, List<Class>> map;


    public HomeRecyclerViewAdapter(Context context) {
        this.context = context;
    }

    public void update(List<Map<String, List<Class>>> classes) {
        this.classes = classes;
        notifyDataSetChanged();
    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.general_view, parent, false);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(Holder holder, int position) {
        map = classes.get(position);

        for (String title : map.keySet()) {
            holder.textViewTitle.setText(title);
            holder.generalRecyclerView.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
            holder.generalRecyclerView.setAdapter(new GeneralRecyclerViewAdapter(context, map.get(title)));

        }

    }

    @Override
    public int getItemCount() {
        if (classes == null)
            return 0;
        return classes.size();
    }

    class Holder extends RecyclerView.ViewHolder {

        TextView textViewTitle, buttonSeeAll;
        RecyclerView generalRecyclerView;

        public Holder(View view) {
            super(view);
            textViewTitle = view.findViewById(R.id.text_view_title);
            generalRecyclerView = view.findViewById(R.id.general_recycler_view);
            buttonSeeAll = view.findViewById(R.id.button_see_all);

            buttonSeeAll.setOnClickListener(
                    v -> {

                        Intent intent = new Intent(context, SeeAllActivity.class);
                        intent.putExtra(ConstantUtil.SEE_ALL_FLAG, ConstantUtil.CLASS_ITEM);
                        intent.putExtra(ConstantUtil.TOOLBAR_TITLE_FLAG, textViewTitle.getText().toString()); // map 형식을 seeAll로 보내면, seeAll에서 해당 맵에 대한 정보를 받아 recyclerView로 세팅함.
                        context.startActivity(intent);
                    }
            );
        }
    }

}
