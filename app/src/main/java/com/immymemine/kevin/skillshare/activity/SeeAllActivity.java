package com.immymemine.kevin.skillshare.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.immymemine.kevin.skillshare.R;
import com.immymemine.kevin.skillshare.adapter.main_adapter.GeneralRecyclerViewAdapter;
import com.immymemine.kevin.skillshare.adapter.see_all_adapter.DiscussionSeeAllRecyclerViewAdapter;
import com.immymemine.kevin.skillshare.adapter.see_all_adapter.ProjectSeeAllRecyclerViewAdapter;
import com.immymemine.kevin.skillshare.adapter.see_all_adapter.ReviewSeeAllRecyclerViewAdapter;
import com.immymemine.kevin.skillshare.adapter.see_all_adapter.SubscriberSeeAllRecyclerViewAdapter;
import com.immymemine.kevin.skillshare.model.home.Class;
import com.immymemine.kevin.skillshare.model.m_class.Reply;
import com.immymemine.kevin.skillshare.model.m_class.Review;
import com.immymemine.kevin.skillshare.model.m_class.Subscriber;
import com.immymemine.kevin.skillshare.model.see_all.Project;
import com.immymemine.kevin.skillshare.model.user.User;
import com.immymemine.kevin.skillshare.network.RetrofitHelper;

import com.immymemine.kevin.skillshare.network.api.SeeAllService;
import com.immymemine.kevin.skillshare.utility.ConstantUtil;
import com.immymemine.kevin.skillshare.utility.StateUtil;
import com.immymemine.kevin.skillshare.utility.ValidationUtil;
import com.immymemine.kevin.skillshare.utility.communication_util.Bus;
import com.jakewharton.rxbinding2.widget.RxTextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class SeeAllActivity extends AppCompatActivity implements SubscriberSeeAllRecyclerViewAdapter.OnLoadMoreListener {

    // intent
    Intent intent;
    String type;
    int position;
    String id;

    // class 리스트
    List<Class> classList;

    // project
    List<Project> projectList;

    // api
    SeeAllService service;

    // recycler view
    RecyclerView recyclerView;
//    RecyclerView.Adapter adapter;

    // discussion
    EditText editTextReply;
    TextView textViewSendReply;
    List<Reply> replies;

    // Review
    TextView textViewReviewPercent, textViewTestimonials, textViewPositiveCount, textViewNegativeCount;

    // Toolbar
    TextView textViewToolbarTitle;
    String title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 1. 인텐트 받아옴
        intent = getIntent();
        // 각 프래그먼트에서 받아온 type
        type = intent.getStringExtra(ConstantUtil.SEE_ALL_FLAG);
        // review 부분에서 받아온 것같음.
        position = intent.getIntExtra("position", -1);
        // 각 fragment에서 id를 전달
        id = intent.getStringExtra(ConstantUtil.ID_FLAG);
        // toobar별로 받아온 것 같음
        title = intent.getStringExtra(ConstantUtil.TOOLBAR_TITLE_FLAG); // home부분에서 고치기.받아온거 리팩토링 필요함.

        // 통신 모듈화
        service = RetrofitHelper.createApi(SeeAllService.class);

        // 넘어온 type별로 정리
        if (type.equals(ConstantUtil.REVIEW_ITEM)) {
            setContentView(R.layout.activity_see_all_review);
        } else if (type.equals(ConstantUtil.DISCUSSION_ITEM)) {
            setContentView(R.layout.activity_see_all_discussion);
        } else if (type.equals(ConstantUtil.CLASS_ITEM)) {
            setContentView(R.layout.activity_see_all);
        } else if (type.equals(ConstantUtil.PROJECT_ITEM)) {
            setContentView(R.layout.activity_see_all);
        } else if (type.equals(ConstantUtil.SUBSCRIBER_ITEM)) {
            setContentView(R.layout.activity_see_all);
        }


        initiateView(title);

////        networking(id);
    }
//
//    private void networking(String id) {
//        //retrofit...
//        switch (type) {
//            case ConstantUtil.CLASS_ITEM:
//                break;
//            case ConstantUtil.DISCUSSION_ITEM:
//                break;
//            case ConstantUtil.PROJECT_ITEM:
//                break;
//        }
//    }

    @Override
    public void onBackPressed() {
        if (type.equals(ConstantUtil.DISCUSSION_ITEM)) {
            Map<Integer, List<Reply>> data = new HashMap<>();
            replies.remove(0);
            Collections.reverse(replies);
            data.put(position, replies);
            Bus.getInstance().post(data);
        }
        super.onBackPressed();
    }


    private void initiateView(@Nullable String title) {
        // toolbar title
        textViewToolbarTitle = findViewById(R.id.toolbar_title);

        if (title != null)
            textViewToolbarTitle.setText(title);

        // back button
        findViewById(R.id.toolbar_button_back).setOnClickListener(
                v -> {
                    if (type.equals(ConstantUtil.DISCUSSION_ITEM)) {
                        Map<Integer, List<Reply>> data = new HashMap<>();
                        replies.remove(0);
                        data.put(position, replies);
                        Bus.getInstance().post(data);
                    }
                    finish();
                }
        );

//        // recycler view
//        recyclerView = findViewById(R.id.recycler_view_see_all);
//        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        switch (type) {

            case ConstantUtil.CLASS_ITEM:

                classSeeAll();

                break;
            case ConstantUtil.DISCUSSION_ITEM:

                discussionSeeAll();

                break;
            case ConstantUtil.PROJECT_ITEM:

                aboutProjectSeeAll();

                break;
            case ConstantUtil.REVIEW_ITEM:

                aboutReviewsSeeAll();

                break;
            case ConstantUtil.SUBSCRIBER_ITEM:

                aboutSubscribeSeeAll();

                break;
        }
    }


    private void classSeeAll() {

        classList = new ArrayList<>();
        recyclerView = findViewById(R.id.recycler_view_see_all);
        textViewToolbarTitle.setText(title);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        GeneralRecyclerViewAdapter generalRecyclerViewAdapter = new GeneralRecyclerViewAdapter(this, classList);
        recyclerView.setAdapter(generalRecyclerViewAdapter);

        service.getSeeAllHome(title)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        (Map<String, List<Class>> classes) -> {
                            classes.get(title);
                            generalRecyclerViewAdapter.update(classes.get(title));
                        }, (Throwable error) -> {
                            Log.e("error", error.getMessage());
                        });
    }

    private void aboutProjectSeeAll() {
        //TODO 지상
        String test = intent.getStringExtra("a");
        projectList = new ArrayList<>();
        textViewToolbarTitle.setText("Subscriber Projects");
//        intent.getParcelableArrayListExtra(ConstantUtil.ID_FLAG);
        recyclerView = findViewById(R.id.recycler_view_see_all);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        ProjectSeeAllRecyclerViewAdapter projectSeeAllRecyclerViewAdapter = new ProjectSeeAllRecyclerViewAdapter(this, projectList);
        recyclerView.setAdapter(projectSeeAllRecyclerViewAdapter);

        service.getSeeAllProject(test).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(
                (List<Project> projects) -> {
                    projectList = projects;
                    projectSeeAllRecyclerViewAdapter.update(projectList);

                }, (Throwable error) -> {
                    Log.e("error", error.getMessage());
                }
        );

    }

    private void aboutReviewsSeeAll() {
        textViewToolbarTitle.setText("Class Reviews");
        List<Review> reviews = intent.getParcelableArrayListExtra(ConstantUtil.REVIEWS_FLAG);

        // review
        textViewTestimonials = findViewById(R.id.text_view_testimonials);
        textViewReviewPercent = findViewById(R.id.text_view_review_percent);
        textViewPositiveCount = findViewById(R.id.text_view_positive_count);
        textViewNegativeCount = findViewById(R.id.text_view_negative_count);
        recyclerView = findViewById(R.id.recycler_view_seeAll);

        int size = reviews.size();
        int likeCount = 0;
        for (Review review : reviews) {
            if ("like".equals(review.getLikeOrNot()))
                likeCount++;
        }

        textViewReviewPercent.setText((likeCount * 100 / size) + "% Positive Reviews");
        textViewPositiveCount.setText(likeCount + "");
        textViewNegativeCount.setText(size - likeCount + "");
        if (size > 1)
            textViewTestimonials.setText(size + " Testimonials");
        else
            textViewTestimonials.setText(size + " Testimonial");

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        ReviewSeeAllRecyclerViewAdapter reviewSeeAllRecyclerViewAdapter = new ReviewSeeAllRecyclerViewAdapter(this, reviews);
        recyclerView.setAdapter(reviewSeeAllRecyclerViewAdapter);

    }

    SubscriberSeeAllRecyclerViewAdapter subscriberSeeAllRecyclerViewAdapter;

    private void aboutSubscribeSeeAll() {

        textViewToolbarTitle.setText("Subscribers");

        List<Subscriber> subscribers = intent.getParcelableArrayListExtra(ConstantUtil.SUBSCRIBERS_FLAG);

        recyclerView = findViewById(R.id.recycler_view_see_all);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        subscriberSeeAllRecyclerViewAdapter = new SubscriberSeeAllRecyclerViewAdapter(this, subscribers);
        recyclerView.setAdapter(subscriberSeeAllRecyclerViewAdapter);

        LinearLayoutManager llManager = (LinearLayoutManager) recyclerView.getLayoutManager();
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if (llManager.findLastCompletelyVisibleItemPosition()
                        == (subscriberSeeAllRecyclerViewAdapter.getItemCount() - 4)) {
                    subscriberSeeAllRecyclerViewAdapter.setMore(true);
                    subscriberSeeAllRecyclerViewAdapter.showLoading();
                }
                super.onScrolled(recyclerView, dx, dy);
            }
        });
    }

    @Override
    public void onLoadMore() {
        if (type.equals(ConstantUtil.SUBSCRIBER_ITEM)) {
            RetrofitHelper.createApi(SeeAllService.class)
                    .getSeeAllSubscribers(id, subscriberSeeAllRecyclerViewAdapter.getItemCount())
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(
                            (List<Subscriber> partOfSubscribers) -> {
                                subscriberSeeAllRecyclerViewAdapter.dismissLoading();
                                subscriberSeeAllRecyclerViewAdapter.addItemMore(partOfSubscribers);
                                if (partOfSubscribers.size() < 20) {
                                    recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
                                        @Override
                                        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                                            super.onScrolled(recyclerView, dx, dy);
                                        }
                                    });
                                }
                            }, (Throwable error) -> {

                            }
                    );
        }
    }

    DiscussionSeeAllRecyclerViewAdapter discussionSeeAllRecyclerViewAdapter;
    User user;

    private void discussionSeeAll() {
        user = StateUtil.getInstance().getUserInstance();

        replies = intent.getParcelableArrayListExtra(ConstantUtil.DISCUSSION_ITEM);

        recyclerView = findViewById(R.id.recycler_view_discussion_see_all);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        discussionSeeAllRecyclerViewAdapter = new DiscussionSeeAllRecyclerViewAdapter(this, replies);
        recyclerView.setAdapter(discussionSeeAllRecyclerViewAdapter);

        editTextReply = findViewById(R.id.edit_text_reply);
        textViewSendReply = findViewById(R.id.text_view_send_reply);

        if (!StateUtil.getInstance().getState()) {

            findViewById(R.id.linear_layout_discussion).setVisibility(View.GONE);
            findViewById(R.id.linear_layout_sign_message).setVisibility(View.VISIBLE);

            findViewById(R.id.sign_in).setOnClickListener(
                    view -> startActivity(new Intent(SeeAllActivity.this, SignInActivity.class))
            );

            findViewById(R.id.sign_up).setOnClickListener(
                    view -> startActivity(new Intent(SeeAllActivity.this, SignUpActivity.class))
            );

        } else {

            findViewById(R.id.linear_layout_discussion).setVisibility(View.VISIBLE);
            findViewById(R.id.linear_layout_sign_message).setVisibility(View.GONE);

            RxTextView.textChanges(editTextReply).subscribe(
                    charSequence -> {
                        if (!ValidationUtil.isEmpty(charSequence.toString())) {
                            textViewSendReply.setEnabled(true);
                            textViewSendReply.setTextColor(getResources().getColor(R.color.IcActive));
                        } else {
                            textViewSendReply.setEnabled(false);
                            textViewSendReply.setTextColor(getResources().getColor(R.color.IcDisabled));
                        }
                    }
            );


            textViewSendReply.setOnClickListener(
                    view -> {
                        Reply reply = new Reply(
                                user.getName(),
                                user.getImageUrl(),
                                editTextReply.getText().toString(),
                                System.currentTimeMillis() + ""
                        );

                        service.addReply(reply, id)
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(
                                        (List<Reply> replies) -> {
                                            Reply discussion = this.replies.get(0);

                                            Collections.reverse(replies);
                                            this.replies.clear();
                                            this.replies.add(0, discussion);
                                            this.replies.addAll(replies);

                                            discussionSeeAllRecyclerViewAdapter.update(this.replies);

                                            textViewToolbarTitle.setText(this.replies.size() - 1 + " Replies");
                                        },
                                        (Throwable error) -> {
                                            Log.e("JUWONLEE", "add reply error : " + error.getMessage());
                                        }
                                );

                        // Reply 입력칸 비우기
                        editTextReply.setText("");
                    }
            );
        }

    }
}