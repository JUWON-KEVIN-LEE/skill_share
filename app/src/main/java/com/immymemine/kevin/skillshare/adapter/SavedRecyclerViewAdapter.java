package com.immymemine.kevin.skillshare.adapter;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import com.immymemine.kevin.skillshare.R;
import com.immymemine.kevin.skillshare.model.user.SubscribedClass;
import com.immymemine.kevin.skillshare.model.user.User;
import com.immymemine.kevin.skillshare.network.Response;
import com.immymemine.kevin.skillshare.network.RetrofitHelper;
import com.immymemine.kevin.skillshare.network.api.UserService;
import com.immymemine.kevin.skillshare.network.user.SubscribeResponse;
import com.immymemine.kevin.skillshare.network.user.UserResponse;
import com.immymemine.kevin.skillshare.utility.ConstantUtil;
import com.immymemine.kevin.skillshare.utility.StateUtil;
import com.immymemine.kevin.skillshare.utility.TimeUtil;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by JisangYou on 2017-12-14.
 */

public class SavedRecyclerViewAdapter extends RecyclerView.Adapter<SavedRecyclerViewAdapter.ViewHolder> {

    List<SubscribedClass> subscribedList;
    Context context;


    public SavedRecyclerViewAdapter(Context context, List<SubscribedClass> subscribedList) {
        this.context = context;
        this.subscribedList = subscribedList;
    }

    public void setData(List<SubscribedClass> subscribedList) {
        this.subscribedList = subscribedList;
        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {

        if (subscribedList.size() == 0)
            return ConstantUtil.NO_ITEM;
        else
            return super.getItemViewType(position);
    }

    @Override
    public SavedRecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        if (viewType == ConstantUtil.NO_ITEM)
            view = LayoutInflater.from(context).inflate(R.layout.recycler_view_item_no_saved, parent, false);
        else
            view = LayoutInflater.from(context).inflate(R.layout.recycler_view_item_saved, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(SavedRecyclerViewAdapter.ViewHolder holder, int position) {

        if (subscribedList.size() != 0) {
            SubscribedClass subscribedClass = subscribedList.get(position);
            holder.title.setText(subscribedClass.getTitle());
            holder.tutorName.setText(subscribedClass.getTutorName());
            holder.duration.setText(TimeUtil.calculateVideoTime(subscribedClass.getTotalDuration()));
            holder.thumbUpPercent.setText(subscribedClass.getReviewPercent());
            holder.attendance.setText(subscribedClass.getSubscriberCount());
            Glide.with(context).load(Uri.parse(subscribedClass.getImageUrl())).into(holder.imageUrl);
            holder.dots.setImageResource(R.drawable.image_triple_dot);
        }

    }

    @Override
    public int getItemCount() {

        if (subscribedList.size() == 0)
            return 1;
        else
            return subscribedList.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView title, tutorName, duration, thumbUpPercent, attendance;
        ImageView imageUrl, dots;

        public ViewHolder(View v) {
            super(v);

            User user = StateUtil.getInstance().getUserInstance();


            if (subscribedList.size() != 0) {
                title = v.findViewById(R.id.text_view_title);
                tutorName = v.findViewById(R.id.text_view_tutor3);
                duration = v.findViewById(R.id.text_view_duration_s);
                thumbUpPercent = v.findViewById(R.id.text_view_thumbUp_s);
                attendance = v.findViewById(R.id.text_view_attendanceStudents_s);
                imageUrl = v.findViewById(R.id.image_view_profile);
                dots = v.findViewById(R.id.image_view_dots);

                dots.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View vi) {
                        PopupMenu popupMenu = new PopupMenu(vi.getContext(), vi);
                        ((Activity) context).getMenuInflater().inflate(R.menu.pop_up, popupMenu.getMenu());
                        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                            @Override
                            public boolean onMenuItemClick(MenuItem item) {
                                int position = getLayoutPosition();

                                if (item.getItemId() == R.id.delete) {

                                    subscribedList.remove(position);
                                    setData(subscribedList);
                                    user.setSubscribedClasses(subscribedList);
                                    Log.e("setSubscribedClasses", "setSubscribedClasses == " + user.getSubscribedClasses().size());

                                    RetrofitHelper.createApi(UserService.class)
                                            .deleteSubscribeClass(user.get_id(), user.getSubscribedClasses())
                                            .subscribeOn(Schedulers.io())
                                            .observeOn(AndroidSchedulers.mainThread())
                                            .subscribe(
                                                    (SubscribeResponse subscribeResponse) -> {
                                                        Log.e("user SubscribedClasses", "SubscribedClasses == " + user.getSubscribedClasses().size());
                                                        // TODO 지상
                                                        // rx를 활용해서 여기서 YourClassesFrament를 control하고 싶은데....시간이 없음.
                                                    }, (Throwable error) -> {
                                                        Log.e("error", "error == " + error.getMessage());
                                                    });

                                } else if (item.getItemId() == R.id.download) {
                                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://f1.media.brightcove.com/12/3695997568001/3695997568001_4607435803001_4204665411001.mp4?pubId=3695997568001&videoId=4204665411001"));
                                    context.startActivity(intent);
                                }

                                return true;
                            }
                        });
                        popupMenu.show();
                    }
                });


            }
        }
    }


}
