package com.example.staytuned.utils;

import android.content.Context;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MyFireBaseAuthontication {

    private static final Integer RESULT_OK = -1;
    private Context context;
    private FirebaseUser firebaseUser;


    private MyFireBaseAuthontication(Context context) {
        this.context = context;
    }

    private static MyFireBaseAuthontication me;

    public static MyFireBaseAuthontication getMe() {
        return me;
    }

    public static MyFireBaseAuthontication initHelper(Context context) {
        if (me == null) {
            me = new MyFireBaseAuthontication(context);
        }
        return me;
    }


    public FirebaseUser getFireBaseUser(){
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        return firebaseUser;
    }

}
