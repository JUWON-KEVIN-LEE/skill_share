package com.immymemine.kevin.skillshare.utility;

import com.immymemine.kevin.skillshare.model.user.User;

/**
 * Created by quf93 on 2017-12-21.
 */

public class StateUtil {

    private User user;
    private static StateUtil stateUtil;

    // singleton
    private StateUtil() {
        user = new User();
    }

    public static StateUtil getInstance() {
        if(stateUtil == null)
            stateUtil = new StateUtil();

        return stateUtil;
    }

    // get sign in state
    private static boolean state = false;

    public boolean getState() {
        return state;
    }

    public void setState(boolean isSignIn) {
        state = isSignIn;
    }

    public void setUserInstance(User user) {
        this.user = user;
    }

    // get user state
    public User getUserInstance() {
        return user;
    }
}