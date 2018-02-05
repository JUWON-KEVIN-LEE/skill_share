package com.immymemine.kevin.skillshare.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.immymemine.kevin.skillshare.R;


import java.util.List;

/**
 * Created by JisangYou on 2017-11-29.
 */

//public class ProfileRecyclerViewAdapter extends RecyclerView.Adapter<ProfileRecyclerViewAdapter.Holder> {
//    List<TeachingClass> teachingClasses;
//    Context context;
//
//    public ProfileRecyclerViewAdapter(List<TeachingClass> teachingClasses, Context context) {
//        this.teachingClasses = teachingClasses;
//        this.context = context;
//    }
//
//    @Override
//    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
//        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_view_item_tutor, parent, false);
//        return new Holder(view);
//    }
//
//    @Override
//    public void onBindViewHolder(Holder holder, int position) {
//        if (teachingClasses != null && teachingClasses.size() != 0) {
//            TeachingClass teachingClass = teachingClasses.get(position);
//            holder.text_view_time.setText(teachingClass.getDuration());
//            holder.text_view_title.setText(teachingClass.getTeachingClassTitle());
//            holder.text_view_user_name.setText(teachingClass.getTutorName());
//            Glide.with(context).load(teachingClass.getTeachingClassImageUrl())
//                    .apply(RequestOptions.centerCropTransform())
//                    .into(holder.image_view_teachingClass);
//        }
//
//    }
//
//    @Override
//    public int getItemCount() {
//        return teachingClasses.size();
//    }
//
//
//
//    public class Holder extends RecyclerView.ViewHolder {
//        TextView text_view_time, text_view_title, text_view_user_name;
//        ImageView image_view_teachingClass;
//
//        public Holder(View itemView) {
//            super(itemView);
//            text_view_time = itemView.findViewById(R.id.text_view_time);
//            text_view_title = itemView.findViewById(R.id.text_view_title);
//            text_view_user_name = itemView.findViewById(R.id.text_view_user_name);
//            image_view_teachingClass = itemView.findViewById(R.id.image_view_teachingClass);
//
//        }
//    }
//}
