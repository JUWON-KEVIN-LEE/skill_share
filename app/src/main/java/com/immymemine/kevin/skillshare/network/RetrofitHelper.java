package com.immymemine.kevin.skillshare.network;

import com.immymemine.kevin.skillshare.network.interceptor.AuthenticationInterceptor;
import com.immymemine.kevin.skillshare.utility.ValidationUtil;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by quf93 on 2017-11-30.
 */

public class RetrofitHelper {

    public static final String BASE_URL = "http://192.168.0.9:8079/";

    // OkHttpClient
    private static OkHttpClient.Builder httpClient = new OkHttpClient.Builder()
            .addInterceptor(getLoggingInterceptor())
            .connectTimeout(15, TimeUnit.SECONDS)
            .writeTimeout(15, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS);

    // Retrofit Builder
    private static Retrofit.Builder builder =new Retrofit.Builder()
                                                         .client(httpClient.build())
                                                         .baseUrl(BASE_URL)
                                                         .addConverterFactory(GsonConverterFactory.create())
                                                         .addCallAdapterFactory(RxJava2CallAdapterFactory.create());
    // Retrofit
    private static Retrofit retrofit = builder.build();

    public static <T> T createApi(Class<T> service){
        return createApi(service, null);
    }

    public static <T> T createApi(Class<T> service, String authToken) {
        if(!ValidationUtil.isEmpty(authToken)) {
            AuthenticationInterceptor interceptor = new AuthenticationInterceptor(authToken);

            if(!httpClient.interceptors().contains(interceptor)) {
                httpClient.addInterceptor(interceptor);

                builder.client(httpClient.build());
                retrofit = builder.build();
            }
        }
        return retrofit.create(service);
    }

    private static HttpLoggingInterceptor loggingInterceptor;

    private static HttpLoggingInterceptor getLoggingInterceptor() {
        if(loggingInterceptor == null) {
            loggingInterceptor = new HttpLoggingInterceptor();
            loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        }
        return loggingInterceptor;
    }
}