package com.immymemine.kevin.skillshare.fragment.class_f;


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
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.immymemine.kevin.skillshare.R;
import com.immymemine.kevin.skillshare.activity.ProfileActivity;
import com.immymemine.kevin.skillshare.adapter.class_adapter.LessonsAdapter;
import com.immymemine.kevin.skillshare.model.m_class.Lessons;
import com.immymemine.kevin.skillshare.model.m_class.Tutor;
import com.immymemine.kevin.skillshare.model.user.Following;
import com.immymemine.kevin.skillshare.model.user.User;
import com.immymemine.kevin.skillshare.network.RetrofitHelper;
import com.immymemine.kevin.skillshare.network.api.ClassService;
import com.immymemine.kevin.skillshare.network.api.UserService;
import com.immymemine.kevin.skillshare.utility.ConstantUtil;
import com.immymemine.kevin.skillshare.utility.DialogUtil;
import com.immymemine.kevin.skillshare.utility.StateUtil;
import com.immymemine.kevin.skillshare.utility.TimeUtil;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * A simple {@link Fragment} subclass.
 */
public class LessonsFragment extends Fragment {

    // view
    ScrollView scrollView;
    TextView textViewTitle, textViewTime, textViewReview, textViewSubscriberCount;
    ToggleButton buttonFollow;
    TextView textViewTutor, textViewFollowersCount;
    ImageView imageViewTutor;

    // RecyclerView / adapter
    RecyclerView recyclerViewLessons;
    LessonsAdapter adapter;

    // context
    Context context;

    // follow button
    int followers;

    Tutor tutor;
    String tutorId;
    String classId;

    public LessonsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_lessons, container, false);

        context = getActivity();

        initiateView(view);

        classId = getArguments().getString(ConstantUtil.ID_FLAG);

        RetrofitHelper.createApi(ClassService.class)
                .getLessons(classId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::handleResponse, this::handleError);
        // TODO progress bar

        return view;
    }

    private void initiateView(View v) {
        scrollView = v.findViewById(R.id.scroll_view_lesson);
        // class 정보
        textViewTitle = v.findViewById(R.id.text_view_title);
        textViewTime = v.findViewById(R.id.text_view_time);
        textViewReview = v.findViewById(R.id.text_view_review);
        textViewSubscriberCount = v.findViewById(R.id.text_view_subscriber_count);

        // tutor 정보
        textViewTutor = v.findViewById(R.id.text_view_user_name);
        textViewFollowersCount = v.findViewById(R.id.text_view_followers);
        imageViewTutor = v.findViewById(R.id.image_view_group);
        imageViewTutor.setOnClickListener(view -> {
            Intent intent = new Intent(context, ProfileActivity.class);

//            intent.putExtra(ConstantUtil.TUTOR_CHECK, tutorId);

            intent.putExtra(ConstantUtil.USER_ID_FLAG, tutorId);
            startActivity(intent);
        });

        // follow 버튼
        buttonFollow = v.findViewById(R.id.button_follow);

        // lessons recycler view
        recyclerViewLessons = v.findViewById(R.id.recycler_view_lessons);
        recyclerViewLessons.setNestedScrollingEnabled(false);

        adapter = new LessonsAdapter(context);
        recyclerViewLessons.setLayoutManager(new LinearLayoutManager(context));
        recyclerViewLessons.setAdapter(adapter);
    }


    private void handleResponse(Lessons lessons) {
        // class 정보
        textViewTitle.setText(lessons.getTitle());
        textViewTime.setText(TimeUtil.calculateVideoTime(lessons.getTotalDuration()));
        textViewReview.setText(lessons.getReviewPercent() + "%");

        textViewSubscriberCount.setText(lessons.getSubscriberCount());

        // tutor 정보
        tutor = lessons.getTutor();
        tutorId = tutor.getTutorId();



        if (StateUtil.getInstance().getState()) {
            List<Following> followings = StateUtil.getInstance().getUserInstance().getFollowing();
            for (Following following : followings) {
                if (following.getUserId().equals(tutorId)) {
                    buttonFollow.setChecked(true);
                    buttonFollow.setTextColor(getResources().getColor(R.color.white));
                    break;
                }
            }

            buttonFollow.setOnCheckedChangeListener((buttonView, isChecked) -> {
                if (isChecked) {
                    buttonView.setTextColor(getResources().getColor(R.color.white));
                    followers++;
                    toggleFollow();
                } else {
                    buttonView.setTextColor(getResources().getColor(R.color.IcActive));
                    followers--;
                    toggleFollow();
                }

                textViewFollowersCount.setText(followers + " Followers");
            });

        } else {
            buttonFollow.setOnCheckedChangeListener((buttonView, isChecked) ->
                    {
                        buttonView.setChecked(false);
                        DialogUtil.showSignDialog(context);
                    }
            );
        }

        textViewTutor.setText(tutor.getName());
        followers = Integer.parseInt(tutor.getFollowers());
        textViewFollowersCount.setText(followers + " Followers");
        Glide.with(context)
                .load(tutor.getImageUrl())
                .apply(RequestOptions.circleCropTransform())
                .into(imageViewTutor);
        // TODO onClickListener

        // video data 전달
        adapter.update(lessons.getVideos());
        // TODO hide progress bar
    }


    private void handleError(Throwable error) {

    }

    private void toggleFollow() {
        User user = StateUtil.getInstance().getUserInstance();

        RetrofitHelper.createApi(UserService.class)
                .follow(user.get_id(), tutor)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        (Following following) -> {
                            List<Following> followings = user.getFollowing();

                            if (followings.contains(following)) {
                                followings.remove(following); //?
                            } else {
                                followings.add(following);
                            }
                        }, (Throwable error) -> {

                        }
                );
    }
}
