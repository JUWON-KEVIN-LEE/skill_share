package com.immymemine.kevin.skillshare.adapter.see_all_adapter;

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
import com.immymemine.kevin.skillshare.model.see_all.Project;

import java.util.List;

/**
 * Created by quf93 on 2017-12-10.
 */

public class ProjectSeeAllRecyclerViewAdapter extends RecyclerView.Adapter<ProjectSeeAllRecyclerViewAdapter.Holder> {

    Context context;
    List<Project> projects;

    public ProjectSeeAllRecyclerViewAdapter(Context context, List<Project> projects) {
        this.context = context;
        this.projects = projects;
    }

    public void update(List<Project> projects){
        this.projects = projects;
        notifyDataSetChanged();
    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.recycler_view_item_see_all_projects, parent, false);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(Holder holder, int position) {
            Project project = projects.get(position);

            Glide.with(context).load(project.getProjectPictureUrl())
                    .apply(RequestOptions.centerCropTransform())
                    .into(holder.imageViewProject);
            holder.textViewLikeCount.append(project.getProjectLikeCount());
            holder.textViewTitle.setText(project.getProjectTitle());
            holder.textViewSubscriber.setText(project.getSubscriberName());
    }

    @Override
    public int getItemCount() {
        return projects.size();
    }

    class Holder extends RecyclerView.ViewHolder {
        ImageView imageViewProject;
        TextView textViewLikeCount, textViewTitle, textViewSubscriber;

        public Holder(View itemView) {
            super(itemView);

            imageViewProject = itemView.findViewById(R.id.image_view_project);
            textViewLikeCount = itemView.findViewById(R.id.text_view_reply);
            textViewTitle = itemView.findViewById(R.id.text_view_title);
            textViewSubscriber = itemView.findViewById(R.id.text_view_subscriber);
        }
    }
}
