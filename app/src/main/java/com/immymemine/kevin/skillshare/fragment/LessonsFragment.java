package com.immymemine.kevin.skillshare.fragment;


import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.immymemine.kevin.skillshare.R;
import com.immymemine.kevin.skillshare.adapter.fragment_adapter.LessonsAdapter;
import com.immymemine.kevin.skillshare.model.m_class.Lessons;
import com.immymemine.kevin.skillshare.model.m_class.Tutor;
import com.immymemine.kevin.skillshare.model.m_class.Video;
import com.immymemine.kevin.skillshare.network.RetrofitHelper;
import com.immymemine.kevin.skillshare.network.api.ClassService;
import com.immymemine.kevin.skillshare.utility.ConstantUtil;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * A simple {@link Fragment} subclass.
 */
public class LessonsFragment extends Fragment implements LessonsAdapter.FragmentAndRecyclerViewInteractionInterface {

    // view
    ScrollView scrollView;
    TextView textViewTitle, textViewTime, textViewReview, textViewSubscriberCount;
    ToggleButton buttonFollow;
    TextView textViewTutor, textViewFollowersCount;
    ImageView imageViewTutor;

    // RecyclerView / adapter
    RecyclerView recyclerViewLessons;
    LessonsAdapter adapter;
    LinearLayoutManager layoutManager;

    // context
    Context context;


    public LessonsFragment() {
        // Required empty public constructor
    }
    /* TODO 지상
    프래그먼트에서는 빈 생성자가 필수라고 함. 없으면 튕길 수 있음.
     */

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_lessons, container, false);
        context = getActivity();
        initiateView(view);
        followedAndFollowing();

        RetrofitHelper.createApi(ClassService.class)
                .getLessons(getArguments().getString("_id"))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::handleResponse, this::handleError);
        // TODO progress bar

        // test ==============================================================
        Tutor tutor = new Tutor("Tutor ID", "Yuko Shimizu", "23593", "https://static.skillshare.com/uploads/users/3110168/user-image-medium.jpg?43101635");

        List<Video> videos = new ArrayList<>();
        videos.add(new Video("Video ID", "1", "Introduction", "1:08", "http://s3.amazonaws.com/skillshare/uploads/parentClasses/2f4f5efd1d503e7131249c94cf2ed7bc/681a4bd7"));
        videos.add(new Video("Video ID", "2", "Brushes and Nibs", "8:57", "https://static.skillshare.com/uploads/project/95045c8c57d1227a6cfb442bd5d3661d/219967c6"));
        videos.add(new Video("Video ID", "3", "Ink and Paper", "8:27", "https://static.skillshare.com/uploads/project/d00cdd4401224eb969fc135174b89135/b6adbd89"));
        videos.add(new Video("Video ID", "4", "Bonus Video : Da Vinci Artist Supply", "1:50", "https://static.skillshare.com/uploads/project/91698/cover_800_28f0ed9b189297e1a13c6bc6cb444eca.jpg"));
        videos.add(new Video("Video ID", "5", "Setting Up Your Materials", "3:41", "https://static.skillshare.com/uploads/project/d00cdd4401224eb969fc135174b89135/3bbd9b77"));
        videos.add(new Video("Video ID", "6", "BrushStroke and Nib Techniques", "8:47", "https://static.skillshare.com/uploads/project/61144/cover_800_e10d97be1e6045c496651c90efd59572.jpg"));

        Lessons lessons = new Lessons(
                "Lessons ID",
                "Ink Drawing Techniques: Brush, Nib, and Pen Style",
                "1h 32m",
                "99%",
                "26324",
                tutor,
                videos
        );

        handleResponse(lessons);
        // test ==============================================================

        return view;
    }
    /* TODO 지상
       getLenssons레트로핏 인터페이스에서 액티비티로 전달 받은 아이디를 참조해 데이터를 불러온다.
       성공적으로 불러왔으면 handelResponse로 전달함.
       실패했으면 handleError로 전달함.
     */

    private void handleResponse(Lessons lessons) {

        // class 정보
        textViewTitle.setText(lessons.getTitle());
        textViewTime.setText(lessons.getTime());
        textViewReview.setText(lessons.getReviewPercent());
        textViewSubscriberCount.setText(lessons.getSubscriberCount());

        // tutor 정보
        Tutor tutor = lessons.getTutor();
        textViewTutor.setText(tutor.getName());
        textViewFollowersCount.setText(tutor.getFollowers());
        Glide.with(context)
                .load(tutor.getPictureUrl())
                .apply(RequestOptions.circleCropTransform())
                .into(imageViewTutor);

        // video data 전달
        adapter.update(lessons.getVideos());
        // TODO hide progress bar
    }
    /* TODO 지상

        Tutor tutor = lessons.getTutor()
        -> Tutor 객체를 lessons의 tutor로 정의함
         ☆ 개인적으로 그동안 헷갈렸던 부분
     */

    private void handleError(Throwable error) {

    }

    public void followedAndFollowing() {


        buttonFollow.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (buttonFollow.isChecked()) {
                      buttonFollow.setTextColor(getResources().getColor(R.color.white));

                    /* TODO 지상
                    로직 작성
                     */


                }else
                    buttonFollow.setTextColor(getResources().getColor(R.color.IcActive));

                /* TODO 지상
                    로직 작성
                */


            }
        });
    }


    private void initiateView(View v) {
        scrollView = v.findViewById(R.id.scroll_view_lesson);
        // class 정보
        textViewTitle = v.findViewById(R.id.text_view_title);
        textViewTime = v.findViewById(R.id.text_view_time);
        textViewReview = v.findViewById(R.id.text_view_review);
        textViewSubscriberCount = v.findViewById(R.id.text_view_subscriber_count);

        // tutor 정보
        textViewTutor = v.findViewById(R.id.text_view_profile);
        textViewFollowersCount = v.findViewById(R.id.text_view_followers_count);
        imageViewTutor = v.findViewById(R.id.image_view_tutor);

        // follow 버튼
        buttonFollow = v.findViewById(R.id.button_follow);

        // lessons recycler view
        recyclerViewLessons = v.findViewById(R.id.recycler_view_lessons);
        recyclerViewLessons.setNestedScrollingEnabled(false);

        adapter = new LessonsAdapter(context, this);
        layoutManager = new LinearLayoutManager(context);
        recyclerViewLessons.setLayoutManager(layoutManager);
        recyclerViewLessons.setAdapter(adapter);
    }

    @Override
    public void focus(int position, int height) {
        scrollView.scrollTo(0, height * position);
    }
}
