package com.immymemine.kevin.skillshare.fragment.main_f;


import android.Manifest;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.flexbox.FlexboxLayoutManager;
import com.immymemine.kevin.skillshare.R;
import com.immymemine.kevin.skillshare.adapter.main_adapter.SkillsRecyclerViewAdapter;
import com.immymemine.kevin.skillshare.model.user.User;
import com.immymemine.kevin.skillshare.network.Response;
import com.immymemine.kevin.skillshare.network.RetrofitHelper;
import com.immymemine.kevin.skillshare.network.api.UserService;
import com.immymemine.kevin.skillshare.utility.ConstantUtil;
import com.immymemine.kevin.skillshare.utility.StateUtil;
import com.immymemine.kevin.skillshare.view.ViewFactory;

import java.io.File;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 */
public class MeFragment extends Fragment {

    ImageView meImage;
    TextView meName, meNickname, meFollowers, meFollowing;
    ImageButton meButton;
    LinearLayout container;

    Button mePersonaize;
    View selectSkillView;
    View meFragment;
    RecyclerView recyclerViewSkills;
    SkillsRecyclerViewAdapter adapter;
    List<String> skills;
    FlexboxLayoutManager layoutManager;

    StateUtil state;
    User user;

    Context context;

    public MeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        meFragment = inflater.inflate(R.layout.fragment_me, container, false);
        context = getActivity();
//        selectSkillView = LayoutInflater.from(context).inflate(R.layout.me_skill_view, container, false);

        initiateView(meFragment);
//        initiateMeSkillView(selectSkillView);

        state = StateUtil.getInstance();
        user = state.getUserInstance();

        if (user.getImageUrl() != null) {
            String imageUrl = RetrofitHelper.BASE_URL+user.getImageUrl();

            Glide.with(context)
                    .load(imageUrl)
                    .apply(RequestOptions.circleCropTransform())
                    .into(meImage);
        }

        meName.setText(user.getName());
        if (user.getNickname() != null)
            meNickname.setText("@" + user.getNickname());

        meFollowers.setText(user.getFollowers().size() + " Followers");
        meFollowing.setText("Following " + user.getFollowing().size());
//        Glide.with(context).load(user.getImageUrl()).apply(RequestOptions.circleCropTransform()).into(meImage);

//        recyclerViewSetting();

        return meFragment;
    }

    private void initiateView(View view) {
        meImage = view.findViewById(R.id.me_image);
        meImage.setOnClickListener(v -> {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                checkPermission();
            } else {
                getImageFile();
            }
        });
        meName = view.findViewById(R.id.me_name);
        meNickname = view.findViewById(R.id.me_nickname);
        meFollowers = view.findViewById(R.id.me_followers);
        meFollowing = view.findViewById(R.id.me_following);
        meButton = view.findViewById(R.id.me_button); // sign out button
        meButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO refresh !!
                StateUtil.getInstance().setUserInstance(null);

            }
        });
        container = view.findViewById(R.id.container);
        container.addView(ViewFactory.getInstance(context).getMeSkillView(user.getFollowingSkills()));
    }

//    public void initiateMeSkillView(View meSkillView) {
//        mePersonaize = meSkillView.findViewById(R.id.personalize);
//        mePersonaize.setOnClickListener(v -> {
//            Intent intent = new Intent(context, SelectSkillsActivity.class);
//            startActivityForResult(intent, ConstantUtil.SELECT_SKILLS_REQUEST_CODE);
//        });
//        recyclerViewSkills = meSkillView.findViewById(R.id.recycler_view_skills);
//        container.addView(meSkillView);
//    }

    public void uploadImageFile(String path) {
        File imageFile = new File(path);

        RequestBody requestFile =
                RequestBody.create(
                        MediaType.parse("image/*"),
                        imageFile
                );

        MultipartBody.Part body =
                MultipartBody.Part.createFormData(
                        "image",
                        imageFile.getName(),
                        requestFile
                );

        RetrofitHelper.createApi(UserService.class)
                .uploadImageFile(user.get_id(), body)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe((Response response) -> {

                }, (Throwable error) -> {

                });
    }

    public void getImageFile() {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");

        startActivityForResult(intent, ConstantUtil.GALLERY_REQUEST_CODE);
    }

    private String getPathFromUri(Uri uri) {
        try (
                Cursor cursor =
                     context.getContentResolver().query(uri, new String[]{MediaStore.Images.Media.DATA},
                             null, null, null)
        ) {
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        } catch(Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == ConstantUtil.GALLERY_REQUEST_CODE && resultCode == RESULT_OK) {
            if(data != null && data.getData() != null) {
                Uri imageUri = data.getData();
                String imagePath = getPathFromUri(imageUri);

                Glide.with(context)
                        .load(new File(imagePath))
                        .apply(RequestOptions.circleCropTransform())
                        .into(meImage);

                uploadImageFile(imagePath);
            }
        } else if (requestCode == ConstantUtil.SELECT_SKILLS_REQUEST_CODE && resultCode == RESULT_OK) {
            skills = data.getStringArrayListExtra(ConstantUtil.SKILLS_FLAG);

            user.setFollowingSkills(skills);

//            recyclerViewSetting();

            RetrofitHelper.createApi(UserService.class).followSkills(user.get_id(), user.getFollowingSkills())
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(
                            (User user) -> {
                                Log.e("handleResponse", "USER" + user.getFollowingSkills());
                            }, (Throwable error) -> {
                                Log.e("handleError", "throwable message" + error.getMessage());
                            }
                    );
        }
    }


    @TargetApi(Build.VERSION_CODES.M)
    private void checkPermission() {
        // 1. 권한이 있는지 여부 확인
        if (context.checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_GRANTED) {
            getImageFile();
            // 진행 허용 처리

            // 2. 권한이 없으면 권한을 요청
        } else {
            // 2.1 요청할 권한을 정의
            String permissions[] = {
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
            };
            // 2.2 권한 요청
            requestPermissions(permissions, ConstantUtil.REQ_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        // 3. 권한 승인 여부 체크
        switch (requestCode) {
            case ConstantUtil.REQ_CODE:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // 진행 허용 처리
                    getImageFile();
                }
                break;
        }
    }

//    public void recyclerViewSetting() {
//        layoutManager = new FlexboxLayoutManager(context);
//        layoutManager.setFlexDirection(FlexDirection.ROW);
//        recyclerViewSkills.setLayoutManager(layoutManager);
//        recyclerViewSkills.setAdapter(new SkillsRecyclerViewAdapter(context, user.getFollowingSkills()));
//    }

}