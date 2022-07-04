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
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.staytuned.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class LoginActivity extends Activity {
    private final String TAG = "tagLoginActivity";
    private Button login_BTN_login;
    private Button register_IMG_showHidePassword;
    private TextView login_BTN_register;
    private EditText login_EDTXT_email;
    private EditText login_EDTXT_password;
    private ProgressBar progressBar;
    private boolean isHidden = true;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initializeFields();
        initializeListeners();

    }

    private void initializeFields() {
        login_BTN_login = findViewById(R.id.login_BTN_login);
        login_BTN_register = findViewById(R.id.login_BTN_register);
        login_EDTXT_email = findViewById(R.id.login_EDTXT_email);
        login_EDTXT_password = findViewById(R.id.login_EDTXT_password);
//        progressBar = findViewById(R.id.login_progressBar);
        firebaseAuth = FirebaseAuth.getInstance();
        register_IMG_showHidePassword = findViewById(R.id.register_IMG_showHidePassword);
        register_IMG_showHidePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isHidden) {
                    login_EDTXT_password.setTransformationMethod(null);
                    register_IMG_showHidePassword.setBackgroundResource(R.drawable.ic_baseline_remove_red_eye_24);
                    isHidden = false;
                } else {
                    isHidden = true;
                    login_EDTXT_password.setTransformationMethod(new PasswordTransformationMethod());
                    register_IMG_showHidePassword.setBackgroundResource(R.drawable.ic_eye_visibility_off_24);
                }
            }
        });
    }

    private void initializeListeners() {
        login_BTN_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String sUserEmail = login_EDTXT_email.getText().toString();
                String sUserPassword = login_EDTXT_password.getText().toString();

                if (isLoginFieldsValid(sUserEmail, sUserPassword)) {
                    //show progress
//                    progressBar.setVisibility(View.VISIBLE);
                    firebaseAuth.signInWithEmailAndPassword(sUserEmail, sUserPassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            // login successful
                            if (task.isSuccessful()) {

                                Toast.makeText(LoginActivity.this, "User Logged in successfully",
                                        Toast.LENGTH_SHORT).show();
                                gotoMainActivity();
                            } else {
                                // show error to user
                                String errorMessage = task.getException().getMessage();
                                Toast.makeText(LoginActivity.this, "Error: " + errorMessage,
                                        Toast.LENGTH_SHORT).show();
                            }
                            // after progress we don't want to see progress bar
//                            progressBar.setVisibility(View.INVISIBLE);
                        }
                    });
                }

                else{
                    Toast.makeText(LoginActivity.this, "please fill out email and password", Toast.LENGTH_SHORT).show();
                }
            }
        });

        login_BTN_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gotoRegister();

            }
        });
    }

    /*
    returns true if the login fields are valid (e.g. if they are not empty etc.)
     */
    private boolean isLoginFieldsValid(String userEmail, String userPassword) {
        boolean isEmailEmpty = TextUtils.isEmpty(userEmail);
        boolean isPasswordEmpty = TextUtils.isEmpty(userPassword);
        return !isEmailEmpty && !isPasswordEmpty;
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
                        gotoMainActivity();
                    }
                }

            });
        }
    }

    private void gotoMainActivity() {
        Intent mainIntent = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(mainIntent);
        // prevent option to back-click back to here
        finish();
    }

    private void gotoRegister() {
        Intent registerIntent = new Intent(LoginActivity.this, RegisterActivity.class);
        startActivity(registerIntent);

    }


}
