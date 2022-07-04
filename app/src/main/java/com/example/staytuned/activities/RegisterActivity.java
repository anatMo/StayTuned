package com.example.staytuned.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.staytuned.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class RegisterActivity extends Activity {
    private final String TAG = "logtagRegisterActivity";


    Button register_BTN_backToLogin;
    Button register_BTN_register;
    EditText editTextName;
    EditText register_EDTXT_email;
    EditText register_EDTXT_password;
    ProgressBar progressBar;
    boolean isHidden = true;
    private Button register_IMG_showHidePassword;
    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        //

        initializeFields();
        initializeListeners();
    }

    private void initializeFields() {
        register_BTN_backToLogin = findViewById(R.id.register_BTN_backToLogin);
        register_BTN_register = findViewById(R.id.register_BTN_register);
        register_EDTXT_email = findViewById(R.id.register_EDTXT_email);
        register_EDTXT_password = findViewById(R.id.register_EDTXT_password);
//        progressBar = findViewById(R.id.register_progressBar);
        firebaseAuth = FirebaseAuth.getInstance();
        register_IMG_showHidePassword = findViewById(R.id.register_IMG_showHidePassword);
        register_IMG_showHidePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isHidden) {
                    register_EDTXT_password.setTransformationMethod(null);
                    register_IMG_showHidePassword.setBackgroundResource(R.drawable.ic_baseline_remove_red_eye_24);
                    isHidden = false;
                } else {
                    isHidden = true;
                    register_EDTXT_password.setTransformationMethod(new PasswordTransformationMethod());
                    register_IMG_showHidePassword.setBackgroundResource(R.drawable.ic_eye_visibility_off_24);
                }
            }
        });
    }


    private void initializeListeners() {
        register_BTN_backToLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // we call finish because the previous activity on the stack is the login activity
                finish();
            }
        });
        register_BTN_register.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                String sUserEmail = register_EDTXT_email.getText().toString();
                String sUserPassword = register_EDTXT_password.getText().toString();
                if (isRegisterFieldsValid(sUserEmail, sUserPassword)) {
                    //show progress
//                    progressBar.setVisibility(View.VISIBLE);

                    firebaseAuth.createUserWithEmailAndPassword(sUserEmail, sUserPassword).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                        @Override
                        public void onSuccess(AuthResult authResult) {
                            gotoAccountActivity();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(RegisterActivity.this, "Error creating username: " + e.getMessage(),
                                    Toast.LENGTH_SHORT).show();
                        }
                    });

//                    progressBar.setVisibility(View.INVISIBLE);
                } else {
                    Toast.makeText(RegisterActivity.this, "One or more of the fields is invalid",
                            Toast.LENGTH_SHORT).show();

                }
            }
        });
    }

    private void gotoAccountActivity() {
        Log.d(TAG, "in gotoAccountActivity");
        Intent accountIntent = new Intent(RegisterActivity.this, AccountActivity.class);
        accountIntent.putExtra(getString(R.string.arg_did_come_from_register_intent), true);

        startActivity(accountIntent);
        // prevent option to back-click back to here
        finish();
    }


    @Override
    protected void onStart() {
        super.onStart();

        checkIfUserExistsAndAct();

    }

    private void checkIfUserExistsAndAct() {
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();
        // if user does not exist in auth, goto login
        if (currentUser != null) {

            // if he does exist in auth, he might still posses token (remains for one hour after
            // accout is deleted from auth
            String uid = currentUser.getUid();
            DocumentReference userRef =
                    FirebaseFirestore.getInstance().collection(getString(R.string.ff_Users)).document(uid);
            userRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    if (documentSnapshot.exists()) {
                        Log.d(TAG, String.format("User with email %s is logged in", currentUser.getEmail()));
//                        gotoMainActivity();
                    }
                }

            });
        }
    }


//    private void gotoMainActivity() {
//        Intent mainIntent = new Intent(RegisterActivity.this, MainActivity.class);
//        startActivity(mainIntent);
//        // prevent option to back-click back to here
//        finish();
//    }

//    private void gotoAccountActivity() {
//        Log.d(TAG, "in gotoAccountActivity");
//        Intent accountIntent = new Intent(RegisterActivity.this, AccountActivity.class);
//        accountIntent.putExtra(getString(R.string.arg_did_come_from_register_intent), true);
//
//        startActivity(accountIntent);
//        // prevent option to back-click back to here
//        finish();
//    }

    /*
    returns true if the login fields are valid (e.g. if they are not empty etc.)
     */
    private boolean isRegisterFieldsValid(String userEmail, String userPassword) {
        boolean isEmailEmpty = TextUtils.isEmpty(userEmail);
        boolean isPasswordEmpty = TextUtils.isEmpty(userPassword);
        return !isEmailEmpty && !isPasswordEmpty;
    }
}
