package com.immymemine.kevin.skillshare.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.exoplayer2.C;
import com.immymemine.kevin.skillshare.R;
import com.immymemine.kevin.skillshare.model.m_class.Lessons;
import com.immymemine.kevin.skillshare.model.m_class.Tutor;
import com.immymemine.kevin.skillshare.model.user.User;
import com.immymemine.kevin.skillshare.network.RetrofitHelper;
import com.immymemine.kevin.skillshare.network.api.ClassService;
import com.immymemine.kevin.skillshare.network.api.UserService;
import com.immymemine.kevin.skillshare.utility.ConstantUtil;
import com.immymemine.kevin.skillshare.utility.StateUtil;
import com.immymemine.kevin.skillshare.utility.ValidationUtil;
import com.immymemine.kevin.skillshare.view.ViewFactory;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class ProfileActivity extends AppCompatActivity {

    LinearLayout profileContainer;
    ImageView meImage;
    TextView meName, meNickname, meFollowers, meFollowing;
    ImageButton meButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        initiateView();

        Intent intent = getIntent();
        String userId = intent.getStringExtra(ConstantUtil.USER_ID_FLAG);

        RetrofitHelper.createApi(UserService.class)
                .getUser(userId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::handleResponse, this::handleError);
    }

    private void initiateView() {
        profileContainer = findViewById(R.id.profile_container);
        meImage = findViewById(R.id.me_image);
        meName = findViewById(R.id.me_name);
        meNickname = findViewById(R.id.me_nickname);
        meFollowers = findViewById(R.id.me_followers);
        meFollowing = findViewById(R.id.me_following);

        meButton = findViewById(R.id.me_button); // sign out button
    }

    private void handleResponse(User user) {
        ViewFactory viewFactory = ViewFactory.getInstance(this);

        // TODO user setting
        if( !ValidationUtil.isEmpty(user.getImageUrl()) ) {
            Glide.with(this)
                    .load(user.getImageUrl())
                    .apply(RequestOptions.circleCropTransform())
                    .into(meImage);
        }
        meName.setText(user.getName());
        if( !ValidationUtil.isEmpty(user.getNickname()) ) {
            meNickname.setText(user.getNickname());
        }

        if(user.getFollowers() != null) {
            meFollowers.setText(user.getFollowers().size() + " Followers");
        }
        if(user.getFollowing() != null) {
            meFollowing.setText("Following " + user.getFollowing().size());
        }

        if(user.getFollowingSkills() != null && user.getFollowingSkills().size() > 0) {
            View meSkillView = viewFactory.getMeSkillView(user.getFollowingSkills());
            profileContainer.addView(meSkillView);
        }
    }

    private void handleError(Throwable error) {

    }
}
