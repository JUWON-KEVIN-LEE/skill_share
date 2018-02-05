package com.immymemine.kevin.skillshare.adapter.class_adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.immymemine.kevin.skillshare.R;
import com.immymemine.kevin.skillshare.activity.SeeAllActivity;
import com.immymemine.kevin.skillshare.model.m_class.Discussion;
import com.immymemine.kevin.skillshare.model.m_class.Reply;
import com.immymemine.kevin.skillshare.model.user.User;
import com.immymemine.kevin.skillshare.network.Response;
import com.immymemine.kevin.skillshare.network.RetrofitHelper;
import com.immymemine.kevin.skillshare.network.api.ClassService;
import com.immymemine.kevin.skillshare.network.user.LikeRequestBody;
import com.immymemine.kevin.skillshare.utility.ConstantUtil;
import com.immymemine.kevin.skillshare.utility.DialogUtil;
import com.immymemine.kevin.skillshare.utility.StateUtil;
import com.immymemine.kevin.skillshare.utility.TimeUtil;
import com.immymemine.kevin.skillshare.view.ExpandableTextView;

import net.colindodd.toggleimagebutton.ToggleImageButton;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by JisangYou on 2017-11-22.
 */

public class DiscussionsAdapter extends RecyclerView.Adapter<DiscussionsAdapter.Holder> {

    Context context;
    List<Discussion> discussions;

    public DiscussionsAdapter(Context context) {
        this.context = context;
    }

    public void initiateData(List<Discussion> discussions) {
        this.discussions = discussions;
        notifyDataSetChanged();
    }

    // TODO DiffUtil 개선
    public void updateData(List<Discussion> discussions) {

//        DiscussionDiffCallback callback = new DiscussionDiffCallback(this.discussions, discussions);
//        DiffUtil.DiffResult result = DiffUtil.calculateDiff(callback);
//
//        this.discussions.clear();
//        this.discussions.addAll(discussions);
//        result.dispatchUpdatesTo(this);

        this.discussions = discussions;
        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        if (discussions == null)
            return ConstantUtil.NO_ITEM;
        else
            return super.getItemViewType(position);
    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;

        if (viewType == ConstantUtil.NO_ITEM)
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_view_item_no_discussions, parent, false);
        else
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_view_item_discussions, parent, false);

        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(Holder holder, int position) {
        if (discussions != null) {
            Discussion discussion = discussions.get(position);
            // identifier
            holder.discussionId = discussion.get_id();
            holder.resId = discussion.getResId();
            holder.position = position;

            // profile

//            if (RetrofitHelper.BASE_URL + discussion.getImageUrl() != null) {
            Glide.with(context).load(RetrofitHelper.BASE_URL + discussion.getImageUrl())
                    .apply(RequestOptions.circleCropTransform())
                    .into(holder.imageViewProfile);
//            }
            Log.e("onBindViewHolder", "check discussionImageUrl" + discussion.getImageUrl());

            holder.textViewProfile.setText(discussion.getName());

            // content
            holder.expandableTextView.setText(discussion.getContent(), TextView.BufferType.NORMAL);

            // time
            holder.time = discussion.getTime();
            holder.textViewTime.setText(TimeUtil.calculateTime(holder.time));

            // like
            if (StateUtil.getInstance().getState()) {
                if (discussion.getLikeUsersIds() != null && discussion.getLikeUsersIds().size() != 0) {
                    String userId = StateUtil.getInstance().getUserInstance().get_id();
                    if (discussion.getLikeUsersIds().contains(userId))
                        holder.imageButtonLike.setChecked(true);
                }

                User user = StateUtil.getInstance().getUserInstance();
                holder.imageButtonLike.setOnCheckedChangeListener(
                        (buttonView, isChecked) -> {
                            if (isChecked) {
                                RetrofitHelper.createApi(ClassService.class)
                                        .like(new LikeRequestBody(holder.discussionId, user.get_id(), user.getName(), holder.resId))
                                        .subscribeOn(Schedulers.io())
                                        .observeOn(AndroidSchedulers.mainThread())
                                        .subscribe(
                                                (Response response) -> {
                                                    String likeCount = response.getResult();
                                                    discussion.setLikeCount(likeCount);
                                                    holder.textViewLikeCount.setText(likeCount);
                                                }, (Throwable error) -> {
                                                    Log.d("JUWON LEE", "error : " + error.getMessage());
                                                }
                                        );
                            } else {
                                RetrofitHelper.createApi(ClassService.class)
                                        .unLike(new LikeRequestBody(holder.discussionId, user.get_id(), user.getName(), holder.resId))
                                        .subscribeOn(Schedulers.io())
                                        .observeOn(AndroidSchedulers.mainThread())
                                        .subscribe(
                                                (Response response) -> {
                                                    String likeCount = response.getResult();
                                                    discussion.setLikeCount(likeCount);
                                                    holder.textViewLikeCount.setText(likeCount);
                                                }, (Throwable error) -> {
                                                    Log.d("JUWON LEE", "error : " + error.getMessage());
                                                }
                                        );
                            }
                        }
                );
            } else {
                holder.imageButtonLike.setOnCheckedChangeListener(
                        (buttonView, isChecked) -> {
                            buttonView.setChecked(false);
                            DialogUtil.showSignDialog(context);
                        }
                );
            }
            holder.textViewLikeCount.setText(discussion.getLikeCount());

            // reply
            if (discussion.getReplies() != null) {
                holder.setReply(discussion.getReplies());
            } else
                holder.setReply(new ArrayList<>());
        }
    }

    @Override
    public int getItemCount() {
        if (discussions == null)
            return 1;
        return discussions.size();
    }

    public class Holder extends RecyclerView.ViewHolder implements View.OnClickListener {
        // id
        String discussionId;
        String resId;
        int position;

        // profile
        ImageView imageViewProfile;
        TextView textViewProfile;
        // content
        ExpandableTextView expandableTextView;
        // info / reply
        ImageButton imageButtonReply;
        TextView textViewTime;
        List<Reply> replies;
        String time;

        // like
        ToggleImageButton imageButtonLike;
        TextView textViewLikeCount;

        Reply reply;


        public Holder(View v) {
            super(v);
            if (discussions != null) {
                // profile
                imageViewProfile = v.findViewById(R.id.image_view_profile);
                imageViewProfile.setOnClickListener(view -> {
                    // profile activity 이동
                });
                textViewProfile = v.findViewById(R.id.text_view_user_name);
                textViewProfile.setOnClickListener(view -> {
                    // profile activity 이동
                });
                // content
                expandableTextView = v.findViewById(R.id.expandable_text_view);
                expandableTextView.setTrimLength(8);

                // info / reply
                imageButtonReply = v.findViewById(R.id.image_button_reply);
                imageButtonReply.setOnClickListener(this); // 클릭시 seeAllActivity로 이동함.

                textViewTime = v.findViewById(R.id.text_view_time);
                // like
                imageButtonLike = v.findViewById(R.id.image_button_like);
                textViewLikeCount = v.findViewById(R.id.text_view_like_count);

                // reply
                textViewReplies = v.findViewById(R.id.text_view_replies);
                imageViewReplyProfile = v.findViewById(R.id.image_view_reply_profile);
                textViewReplyProfile = v.findViewById(R.id.text_view_reply_profile);
                expandableTextViewReply = v.findViewById(R.id.expandable_text_view_reply);
                textViewTimeReply = v.findViewById(R.id.text_view_time_reply);
            }
        }

        TextView textViewReplies, textViewReplyProfile, textViewTimeReply;
        ImageView imageViewReplyProfile;
        ExpandableTextView expandableTextViewReply;


        public void setReply(List<Reply> replies) {

            this.replies = replies;

            if (replies != null && replies.size() != 0) {
                textViewReplyProfile.setVisibility(View.VISIBLE);
                textViewTimeReply.setVisibility(View.VISIBLE);
                imageViewReplyProfile.setVisibility(View.VISIBLE);
                expandableTextViewReply.setVisibility(View.VISIBLE);

                int size = replies.size();
                if (size >= 2) {
                    textViewReplies.setText("See all " + replies.size() + " replies");
                    textViewReplies.setVisibility(View.VISIBLE);
                    textViewReplies.setOnClickListener(this);
                }

                reply = replies.get(replies.size() - 1);
                textViewReplyProfile.setText(reply.getName());
                textViewTimeReply.setText(TimeUtil.calculateTime(reply.getTime()));


//                if (reply.getImageUrl() != null)
                Glide.with(context).load(RetrofitHelper.BASE_URL + reply.getImageUrl())
                        .apply(RequestOptions.circleCropTransform())
                        .into(imageViewReplyProfile);
                Log.e("setReply", "check replyImageUrl" + reply.getImageUrl());

                expandableTextViewReply.setTrimLength(4);
                expandableTextViewReply.setText(reply.getContent(), TextView.BufferType.NORMAL);
            } else {
                textViewReplyProfile.setVisibility(View.GONE);
                textViewTimeReply.setVisibility(View.GONE);
                imageViewReplyProfile.setVisibility(View.GONE);
                expandableTextViewReply.setVisibility(View.GONE);
                textViewReplies.setVisibility(View.GONE);
            }
        }

        @Override
        public void onClick(View v) {

            Intent intent = new Intent(context, SeeAllActivity.class);
            intent.putExtra(ConstantUtil.SEE_ALL_FLAG, ConstantUtil.DISCUSSION_ITEM);
            intent.putExtra(ConstantUtil.ID_FLAG, discussionId);
            intent.putExtra("position", position);

            intent.putExtra(ConstantUtil.TOOLBAR_TITLE_FLAG, replies.size() + " Replies");

            Collections.reverse(replies);

            Reply newReply = new Reply(
                    textViewProfile.getText().toString(),
                    reply.getImageUrl(),
                    expandableTextView.getText().toString(),
                    time);

            replies.add(0, newReply);

            intent.putParcelableArrayListExtra(ConstantUtil.DISCUSSION_ITEM, (ArrayList) replies);
            context.startActivity(intent);
        }
    }
}
