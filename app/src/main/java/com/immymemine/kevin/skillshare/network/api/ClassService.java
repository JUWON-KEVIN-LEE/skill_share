package com.immymemine.kevin.skillshare.network.api;

import com.immymemine.kevin.skillshare.model.discover.SearchClass;
import com.immymemine.kevin.skillshare.model.m_class.About;
import com.immymemine.kevin.skillshare.model.m_class.Discussion;
import com.immymemine.kevin.skillshare.model.m_class.Lessons;
import com.immymemine.kevin.skillshare.network.Response;
import com.immymemine.kevin.skillshare.network.user.LikeRequestBody;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by quf93 on 2017-12-02.
 */

public interface ClassService {

    @GET("class/{id}")
    Observable<Lessons> getVideo(@Path("id") String id);

    @GET("class/lessons/{id}")
    Observable<Lessons> getLessons(@Path("id") String id);

    @GET("class/about/{id}")
    Observable<About> getAbout(@Path("id") String id);

    @GET("class/discussions/{id}")
    Observable<List<Discussion>> getDiscussions(@Path("id") String id);

    @POST("class/sendDiscussion")
    Observable<List<Discussion>> sendDiscussion(@Body Discussion discussion, @Query("classId") String classId);

    @POST("discussions/like")
    Observable<Response> like(@Body LikeRequestBody likeBody);

    @POST("discussions/unlike")
    Observable<Response> unLike(@Body LikeRequestBody likeBody);

    @GET("class/search/{content}")
    Observable<List<SearchClass>> search(@Path("content") String searchContent);


}
