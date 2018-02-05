package com.immymemine.kevin.skillshare.fragment.class_f;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ScrollView;
import android.widget.TextView;

import com.immymemine.kevin.skillshare.R;
import com.immymemine.kevin.skillshare.activity.SeeAllActivity;
import com.immymemine.kevin.skillshare.adapter.class_adapter.ProjectAdapter;
import com.immymemine.kevin.skillshare.adapter.class_adapter.RelatedClassAdapter;
import com.immymemine.kevin.skillshare.adapter.class_adapter.ReviewAdapter;
import com.immymemine.kevin.skillshare.adapter.class_adapter.SubscribersAdapter;
import com.immymemine.kevin.skillshare.model.m_class.About;
import com.immymemine.kevin.skillshare.model.m_class.Project;
import com.immymemine.kevin.skillshare.model.m_class.RelatedClass;
import com.immymemine.kevin.skillshare.model.m_class.Review;
import com.immymemine.kevin.skillshare.model.m_class.Subscriber;
import com.immymemine.kevin.skillshare.model.see_all.SeeAll;
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
public class AboutFragment extends Fragment {

    ScrollView scrollView;
    // project
    TextView textViewProjectCount, textViewProjectSeeAll;
    RecyclerView recyclerViewProject;
    // review
    TextView textViewReviewPercent, textViewReviewSeeAll;
    RecyclerView recyclerViewReview;
    // student
    TextView textViewSubscriberNum, textViewSubscriberSeeAll;
    RecyclerView recyclerViewSubscribers;
    // related class
    RecyclerView recyclerViewRelatedClass;
    // adapter
    ProjectAdapter projectAdapter;
    SubscribersAdapter subscribersAdapter;
    RelatedClassAdapter relatedClassAdapter;
    ReviewAdapter reviewAdapter;

    Context context;
    // class id
    String classId;
    public AboutFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_about, container, false);

        context = getActivity();
        classId = getArguments().getString(ConstantUtil.ID_FLAG);

        initiateView(view);

        // empty constructor >>> recycler view 초기화
        projectAdapter = new ProjectAdapter(context);
        recyclerViewProject.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
        recyclerViewProject.setAdapter(projectAdapter);

        reviewAdapter = new ReviewAdapter(context);
        recyclerViewReview.setLayoutManager(new LinearLayoutManager(context));
        recyclerViewReview.setAdapter(reviewAdapter);

        subscribersAdapter = new SubscribersAdapter(context);
        recyclerViewSubscribers.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
        recyclerViewSubscribers.setAdapter(subscribersAdapter);

        relatedClassAdapter = new RelatedClassAdapter(context);
        recyclerViewRelatedClass.setLayoutManager(new LinearLayoutManager(context));
        recyclerViewRelatedClass.setAdapter(relatedClassAdapter);

        RetrofitHelper.createApi(ClassService.class)
                .getAbout(classId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::handleResponse, this::handleError);

        return view;
    }

    private void handleResponse(About about) {
        // project
        List<Project> projects = about.getProjects();
        int projectsSize = projects.size();
        if(projectsSize == 0) {
            textViewProjectCount.setText("Subscriber Project");
            textViewProjectSeeAll.setVisibility(View.GONE);
        } else
            textViewProjectCount.setText(projectsSize + " Subscriber Projects");

        projectAdapter.update(projects); // <<< data 전달 & notifyDataSetChanged


        String a = "SEO Today: Strategies to Earn Trust, Rank High, and Stand Out";

        textViewProjectSeeAll.setOnClickListener(view -> {
            Intent intent = new Intent(context, SeeAllActivity.class);
            intent.putExtra(ConstantUtil.SEE_ALL_FLAG, ConstantUtil.PROJECT_ITEM);
//            intent.putExtra(ConstantUtil.ID_FLAG, classId);
            intent.putExtra("a",a);

            context.startActivity(intent);

        });

        // review
        List<Review> reviews = about.getReviews();
        int size = reviews.size();
        if(size != 0) {
            int likeCount = 0;
            for(Review review : reviews) {
                if( "like".equals(review.getLikeOrNot()) )
                    likeCount++;
            }
            textViewReviewPercent.setText((likeCount * 100 / size)+"% Positive Reviews");
            reviewAdapter.updateData(reviews.get(size-1));

            textViewReviewSeeAll.setOnClickListener(view -> {
                Intent intent = new Intent(context, SeeAllActivity.class);
                intent.putExtra(ConstantUtil.SEE_ALL_FLAG, ConstantUtil.REVIEW_ITEM);
                intent.putExtra(ConstantUtil.ID_FLAG, classId);
                intent.putParcelableArrayListExtra(ConstantUtil.REVIEWS_FLAG, (ArrayList<? extends Parcelable>) reviews); // 굳이?
                Log.e("Parcelable reviews","check ======= "+ reviews);
                context.startActivity(intent);
            });
        } else {
            textViewReviewPercent.setText("No Reviews");
            textViewReviewSeeAll.setVisibility(View.GONE);
        }

        // subscriber
        List<Subscriber> subscribers = about.getSubscribers();
        int count = subscribers.size();
        if(count != 0) {
            textViewSubscriberNum.setVisibility(View.VISIBLE);
            textViewSubscriberNum.setText(count + " Subscribers");

            textViewSubscriberSeeAll.setOnClickListener(view -> {
                Intent intent = new Intent(context, SeeAllActivity.class);
                intent.putExtra(ConstantUtil.SEE_ALL_FLAG, ConstantUtil.SUBSCRIBER_ITEM);
                intent.putExtra(ConstantUtil.ID_FLAG, classId);
                intent.putExtra(ConstantUtil.TOOLBAR_TITLE_FLAG, textViewSubscriberNum.getText().toString());
                intent.putParcelableArrayListExtra(ConstantUtil.SUBSCRIBERS_FLAG, (ArrayList<? extends Parcelable>) subscribers); // 굳이? 어차피 seeAll에서 한번 통신을 해야하는데?
                context.startActivity(intent);
            });
        } else
            textViewSubscriberNum.setVisibility(View.GONE);
        subscribersAdapter.update(subscribers);

        // related class
        List<RelatedClass> relatedClasses = about.getRelatedClasses();
        relatedClassAdapter.update(relatedClasses);
    }

    private void handleError(Throwable error) {

    }

    private void initiateView(View view) {
        // scroll view
        scrollView = view.findViewById(R.id.scroll_view);
        // project
        textViewProjectCount = view.findViewById(R.id.text_view_project_count);
        textViewProjectSeeAll = view.findViewById(R.id.text_view_project_see_all);
        recyclerViewProject = view.findViewById(R.id.recycler_view_project);
        textViewProjectSeeAll.setOnClickListener(v -> {
            Intent intent = new Intent(context, SeeAllActivity.class);
            intent.putExtra(ConstantUtil.SEE_ALL_FLAG, ConstantUtil.PROJECT_ITEM);
            intent.putExtra(ConstantUtil.ID_FLAG, classId);
            context.startActivity(intent);
        });

        // review
        textViewReviewPercent = view.findViewById(R.id.text_view_review_percent);
        textViewReviewSeeAll = view.findViewById(R.id.text_view_review_see_all);
        recyclerViewReview = view.findViewById(R.id.recycler_view_review);
        // student
        textViewSubscriberNum = view.findViewById(R.id.text_view_subscriber_num);
        textViewSubscriberSeeAll = view.findViewById(R.id.text_view_subscriber_see_all);
        recyclerViewSubscribers = view.findViewById(R.id.recycler_view_subscribers);
        // related class
        recyclerViewRelatedClass = view.findViewById(R.id.recycler_view_related_class);
    }
}