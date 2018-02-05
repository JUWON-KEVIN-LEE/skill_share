package com.immymemine.kevin.skillshare.utility;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.immymemine.kevin.skillshare.R;
import com.immymemine.kevin.skillshare.activity.SignInActivity;
import com.immymemine.kevin.skillshare.activity.SignUpActivity;
import com.immymemine.kevin.skillshare.model.user.User;
import com.immymemine.kevin.skillshare.network.Response;
import com.immymemine.kevin.skillshare.network.RetrofitHelper;
import com.immymemine.kevin.skillshare.network.api.UserService;
import com.jakewharton.rxbinding2.widget.RxTextView;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

import static android.app.Activity.RESULT_OK;

/**
 * Created by quf93 on 2017-11-26.
 */

public class DialogUtil {
    // TODO custom dialog
    private void showAlertDialog(Context context, String title, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.setPositiveButton("Ok", (dialog, which) -> dialog.dismiss());
        builder.show();
    }

    public static View showCreateGroupDialog(Context context, Fragment fragment) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_create_group, null, false);
        int limit = 40;
        ImageButton toolbarCloseButton = view.findViewById(R.id.toolbar_close_button);
        EditText editTextGroupName = view.findViewById(R.id.edit_text_group_name);
        TextView textViewCharactersLeft = view.findViewById(R.id.text_view_characters_left);
        Button buttonCreateGroup = view.findViewById(R.id.button_create_group);
        Button imageButtonGallery = view.findViewById(R.id.image_button_gallery);


        RxTextView.textChanges(editTextGroupName)
                .subscribe(charSequence -> {
                    int left = limit - charSequence.toString().length();
                    textViewCharactersLeft.setText(left + " characters left");

                    if (left < 40) {
                        // button enabled
                        buttonCreateGroup.setEnabled(true);
                        buttonCreateGroup.setBackgroundColor(context.getResources().getColor(R.color.IcActive));
                    } else {
                        // disabled
                        buttonCreateGroup.setEnabled(false);
                        buttonCreateGroup.setBackgroundColor(context.getResources().getColor(R.color.IcDisabled));
                    }
                });

        builder.setView(view);
        AlertDialog dialog = builder.show();

        buttonCreateGroup.setOnClickListener(
                v -> {


                    dialog.dismiss();

                }
        );

        imageButtonGallery.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_PICK);
            intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
            fragment.startActivityForResult(intent, ConstantUtil.GALLERY_REQUEST_CODE_FROM_DIALOG);
        });



        toolbarCloseButton.setOnClickListener(
                v -> {
                    dialog.dismiss();
                }
        );

        return view;
    }


//    private void showCreateGroupDialog(Context context) {
//        AlertDialog.Builder builder = new AlertDialog.Builder(context);
//        View view = LayoutInflater.from(context).inflate(R.layout.dialog_create_group, null, false);
//        int limit = 40;
//        ImageButton toolbarCloseButton = view.findViewById(R.id.toolbar_close_button);
//        EditText editTextGroupName = view.findViewById(R.id.edit_text_group_name);
//        TextView textViewCharactersLeft = view.findViewById(R.id.text_view_characters_left);
//
//        RxTextView.textChanges(editTextGroupName)
//                .subscribe(charSequence -> {
//                    int left = limit - charSequence.toString().length();
//                    textViewCharactersLeft.setText(left + " characters left");
//
//                    if(left < 40) {
//                        // button enabled
//                    } else {
//                        // disabled
//                    }
//                });
//
//        builder.setView(view);
//        builder.show();
//    }

    public static AlertDialog showSettingNicknameDialog(Context context) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_setting_nickname, null, false);

        AlertDialog dialog = builder.setView(view).show();

        view.findViewById(R.id.image_button_close).setOnClickListener(
                v -> dialog.dismiss()
        );

        EditText editTextNickname = view.findViewById(R.id.edit_text_nickname);
        TextView buttonSubmit = view.findViewById(R.id.button_submit);

        RxTextView.textChanges(editTextNickname).subscribe(
                (CharSequence c) -> {
                    if (c.length() > 0) {
                        buttonSubmit.setTextColor(context.getResources().getColor(R.color.white));
                        buttonSubmit.setBackgroundColor(context.getResources().getColor(R.color.IcActive));
                    } else {
                        buttonSubmit.setTextColor(Color.parseColor("#c8d9d9d9"));
                        buttonSubmit.setBackgroundColor(context.getResources().getColor(R.color.IcInactive));
                    }
                }
        );

        buttonSubmit.setOnClickListener(v -> {
            String nickname = editTextNickname.getText().toString();

            if (nickname.length() > 0) {
                User user = StateUtil.getInstance().getUserInstance();

                RetrofitHelper.createApi(UserService.class)
                        .setNickname(user.get_id(), nickname)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                                (Response response) -> {
                                    if (ConstantUtil.SUCCESS.equals(response.getResult())) {
                                        StateUtil.getInstance().getUserInstance().setNickname(nickname);
                                        dialog.dismiss();
                                    }
                                }, (Throwable error) -> {
                                    Log.d("JUWONLEE", "set nickname error : " + error.getMessage());
                                }
                        );
            }
        });

        return dialog;
    }

    public static void showSignDialog(Context context) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_sign, null, false);

        view.findViewById(R.id.button_sign_in).setOnClickListener(
                v -> context.startActivity(new Intent(context, SignInActivity.class))
        );

        view.findViewById(R.id.button_sign_up).setOnClickListener(
                v -> context.startActivity(new Intent(context, SignUpActivity.class))
        );

        builder.setView(view).show();
    }


}
