package com.immymemine.kevin.skillshare.network.api;

import com.immymemine.kevin.skillshare.model.group.Group;
import com.immymemine.kevin.skillshare.model.m_class.Tutor;
import com.immymemine.kevin.skillshare.model.user.Following;
import com.immymemine.kevin.skillshare.model.user.SubscribedClass;
import com.immymemine.kevin.skillshare.model.user.User;
import com.immymemine.kevin.skillshare.network.Response;
import com.immymemine.kevin.skillshare.network.user.SignUpRequestBody;
import com.immymemine.kevin.skillshare.network.user.SubscribeResponse;
import com.immymemine.kevin.skillshare.network.user.UserResponse;

import java.util.List;

import io.reactivex.Observable;
import okhttp3.MultipartBody;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by quf93 on 2017-11-30.
 */

public interface UserService {

    @POST("user/sign-up")
    Observable<UserResponse> signUp(@Body SignUpRequestBody body);

    @GET("users/sign-in")
    Observable<UserResponse> signIn(@Query("email") String email, @Query("password") String password);

    @GET("user/authorization")
    Observable<UserResponse> getMyInfo(@Header("authorization") String token);

    @GET("user/{id}")
    Observable<User> getUser(@Path("id") String id);

    @PUT("user/follow/{userId}")
    Observable<Following> follow(@Path("userId") String userId, @Body Tutor tutor);

    @POST("user/joinGroup")
    Observable<Response> joinGroup(@Body Group group, @Query("userId") String userId);

    @POST("user/followSkills")
    Observable<Response> followSkills(@Query("userId") String userId, @Query("skills") List<String> skills);

    @POST("user/subscribeClass")
    Observable<SubscribeResponse> subscribeClass(@Query("userId") String userId, @Query("classId") String classId);

    @POST("user/unsubscribeClass")
    Observable<SubscribeResponse> unsubscribeClass(@Query("userId") String userId, @Query("classId") String classId);

    @Multipart
    @POST("user/uploadImageFile")
    Observable<Response> uploadImageFile(@Query("userId") String userId, @Part MultipartBody.Part image);

    @GET("user/imageFile/{userId}")
    Observable<User> downloadImage(@Path("userId")String userId);

    @POST("user/setNickname")
    Observable<Response> setNickname(@Query("userId") String userId, @Query("nickname") String nickname);

    @POST("user/DeletesubscribedClass")
    Observable<SubscribeResponse> deleteSubscribeClass(@Query("userId") String userId, @Body List<SubscribedClass> deleteSubscribeClass);

//    @GET("user/downloadVideo/{}")

    //    @PUT("user/imageUrl/{userId}/{imageUrl}")
//    Observable<User> putImageUrl(@Path("userId") String userId, @Path("imageUrl") String imageUrl);
}
