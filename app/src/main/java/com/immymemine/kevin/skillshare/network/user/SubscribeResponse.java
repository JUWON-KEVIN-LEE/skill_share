package com.immymemine.kevin.skillshare.network.user;

import com.immymemine.kevin.skillshare.model.user.SubscribedClass;

import java.util.List;

/**
 * Created by quf93 on 2018-01-20.
 */

public class SubscribeResponse {
    String result;
    String message;
    SubscribedClass data;
    List<SubscribedClass> datas;

    public String getResult() {
        return result;
    }

    public String getMessage() {
        return message;
    }

    public SubscribedClass getData() {
        return data;
    }

    public List<SubscribedClass> getDatas() {
        return datas;
    }

}
