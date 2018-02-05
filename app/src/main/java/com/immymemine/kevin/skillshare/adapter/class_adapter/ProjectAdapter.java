package com.immymemine.kevin.skillshare.adapter.class_adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.immymemine.kevin.skillshare.R;
import com.immymemine.kevin.skillshare.model.m_class.Project;
import com.immymemine.kevin.skillshare.utility.ConstantUtil;

import java.util.List;

/**
 * Created by quf93 on 2017-12-06.
 */

public class ProjectAdapter extends RecyclerView.Adapter<ProjectAdapter.ProjectHolder> {

    Context context;
    List<Project> projects;

    public ProjectAdapter(Context context) {
        this.context = context;
    }

    public void update(List<Project> projects) {
        this.projects = projects;
        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        if( projects == null || projects.size() == 0 ) {
            return ConstantUtil.NO_ITEM;
        } else {
            return super.getItemViewType(position);
        }
    }

    @Override
    public ProjectHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;

        if( viewType == ConstantUtil.NO_ITEM ) {
            view = LayoutInflater.from(context).inflate(R.layout.recycler_view_item_no_project, parent, false);
        } else {
            view = LayoutInflater.from(context).inflate(R.layout.recycler_view_item_project, parent, false);
        }

        return new ProjectHolder(view);
    }

    @Override
    public void onBindViewHolder(ProjectHolder holder, int position) {
        if(projects != null && projects.size() != 0) {
            if(position >= projects.size())
                return;

            Project project = projects.get(position);
            holder.projectId = project.get_id();

            if(project.getProjectPictureUrl() != null) {
                Glide.with(context).load(project.getProjectPictureUrl())
                        .apply(RequestOptions.centerCropTransform())
                        .into(holder.projectImageView);
            }
        }
    }

    @Override
    public int getItemCount() {
        if(projects == null || projects.size() == 0)
            return 1;
        else
            return 3;
    }

    class ProjectHolder extends RecyclerView.ViewHolder {

        ImageView projectImageView;
        String projectId;

        public ProjectHolder(View view) {
            super(view);
            if( projects != null ) {
                projectImageView = view.findViewById(R.id.image_view_project);
                projectImageView.setOnClickListener(v -> {
                    // project class 이동
                });
            }
        }
    }
}
