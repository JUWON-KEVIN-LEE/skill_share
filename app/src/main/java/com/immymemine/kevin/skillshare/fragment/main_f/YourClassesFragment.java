package com.immymemine.kevin.skillshare.fragment.main_f;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.immymemine.kevin.skillshare.R;
import com.immymemine.kevin.skillshare.activity.SavedActivity;
import com.immymemine.kevin.skillshare.model.user.SubscribedClass;
import com.immymemine.kevin.skillshare.model.user.User;
import com.immymemine.kevin.skillshare.utility.StateUtil;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class YourClassesFragment extends Fragment {

    Context context;

    TextView textViewSubscribedCount;
    ImageView imageViewThumbnail;
    StateUtil state;
    User user;
    List<SubscribedClass> subscribedClasses;
    Intent intent;
    int size;

    public YourClassesFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_your_classes, container, false);

        context = getActivity();

        textViewSubscribedCount = view.findViewById(R.id.text_view_subscribed_count);
        imageViewThumbnail = view.findViewById(R.id.image_view_profile);


        dataSetting();


        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        dataSetting();


    }

    public void dataSetting() {
        state = StateUtil.getInstance();
        if (state.getState()) {
            user = state.getUserInstance();
            if (user.getSubscribedClasses() != null) {
                subscribedClasses = user.getSubscribedClasses();
                size = subscribedClasses.size();
                textViewSubscribedCount.setText(size + " Classes");
                if (subscribedClasses.size() != 0) {
                    Glide.with(context).load(subscribedClasses.get(size - 1).getImageUrl())
                            .apply(RequestOptions.centerCropTransform())
                            .into(imageViewThumbnail);
                }

                imageViewThumbnail.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        intent = new Intent(context, SavedActivity.class);
                        startActivity(intent);
                    }
                });
            }
        }
    }


    public void clickDownload(View view) {
        // download page //
    }

    public void clickSubscribedClasses(View view) {
        // see all activity //
    }
}
