package com.example.staytuned.activities;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.staytuned.R;
import com.example.staytuned.models.UserModel;
import com.example.staytuned.utils.MyFirebaseDB;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

public class AccountActivity extends Activity {
    // Constants
    private final String TAG = "logtagAccountActivity";
    private Uri DEFAULT_AVATAR_RES_URI;
    private String DEFAULT_AVATAR_STORAGE_STRING = "avatar2.png";

//    private final Uri DEFAULT_AVATAR_STORAGE_URI = Uri.parse(DEFAULT_AVATAR_STORAGE_STRING);


    // Firebase
    private FirebaseStorage firebaseStorage;
    private FirebaseUser firebaseUser;
    private FirebaseFirestore firebaseFirestore;
    private CollectionReference usersCollectionRef;

    // Views
    private Button buttonChangeProfilePic;
    private ImageView profileImage;
    private ImageView register_ICN_logo;
    private Button account_BTN_save;
    private Button account_BTN_cancel;
    private Button account_BTN_delete;
    private EditText account_EDTXT_nickName;
    private ProgressBar progressBar;

    // Data
    private String userID;
    private String userEmail;
    private String userName;
    private boolean didComeFromRegister;
    private String initialUserProfilePicURI;
    private String updatedUserProfilePicURI;
    private Uri uploadedPhoto;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "in onCreate");


        setContentView(R.layout.activity_account);
        initializeFields();
        didComeFromRegister =
                getIntent().getBooleanExtra(getString(R.string.arg_did_come_from_register_intent)
                        , true);
        if (didComeFromRegister) {
            initializeFromRegister();

        } else {
            initializeFromUpdate();
        }

    }

    @Override
    public void onBackPressed() {
        usersCollectionRef.document(userID).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(AccountActivity.this,"please add name to use", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("AccountActivity.this", String.format("failed deleted " +
                        "account with email %s", userEmail));
            }
        });

    }


    private void initializeFields() {
        Log.d(TAG, "in initializeFields");

        initializeViews();
        initializeFirebaseFields();
        initializeDataFields();
    }

    private void initializeViews() {
        register_ICN_logo = findViewById(R.id.register_ICN_logo);
        account_BTN_save = findViewById(R.id.account_BTN_save);
        account_BTN_cancel = findViewById(R.id.account_BTN_cancel);
        account_BTN_delete = findViewById(R.id.account_BTN_delete);
        account_EDTXT_nickName = findViewById(R.id.account_EDTXT_nickName);
        setButtonDeleteAccountOnClick();
        setSaveButtonOnClick();
    }


    private void initializeFirebaseFields() {
        Log.d(TAG, "in initializeFirebaseFields");

//        myAssert(FirebaseAuth.getInstance().getCurrentUser() != null, "User in null upon account " +
//                "activity");
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        firebaseFirestore = FirebaseFirestore.getInstance();
        usersCollectionRef = firebaseFirestore.collection(getString(R.string.ff_Users));
        firebaseStorage = FirebaseStorage.getInstance();

    }

    private void initializeDataFields() {

        Log.d(TAG, "in initializeDataFields");

        userID = firebaseUser.getUid();
        userEmail = firebaseUser.getEmail();
        userName = "";

        DEFAULT_AVATAR_RES_URI = new Uri.Builder()
                .scheme(ContentResolver.SCHEME_ANDROID_RESOURCE)
                .authority(getResources().getResourcePackageName(R.drawable.avatar2))
                .appendPath(getResources().getResourceTypeName(R.drawable.avatar2))
                .appendPath(getResources().getResourceEntryName(R.drawable.avatar2))
                .build();
    }


    private void initializeFromRegister() {
        Log.d(TAG, "in initializeFromRegister");
//        buttonCancel.setVisibility(View.INVISIBLE);
//        buttonDeleteAccount.setVisibility(View.INVISIBLE);
//        buttonChangeProfilePic.setText("Add profile picture");
//        initialUserProfilePicURI = DEFAULT_AVATAR_STORAGE_STRING;
//        setChangeProfilePicButtonOnClick();
    }


    private void initializeFromUpdate() {
        Log.d(TAG, "in initializeFromUpdate");

        // TODO show progress bar
        account_BTN_cancel.setVisibility(View.VISIBLE);
        account_BTN_delete.setVisibility(View.VISIBLE);
//        buttonChangeProfilePic.setText("Change profile picture");
//        usersCollectionRef.document(userID).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
//            @Override
//            public void onSuccess(DocumentSnapshot documentSnapshot) {
//                userName = documentSnapshot.getString(getString(R.string.ff_Users_userName));
//                initialUserProfilePicURI = documentSnapshot.getString(getString(R.string.ff_Users_userProfilePicURL));
//                showName();
//                showProfilePic();
//
//            }
//        }).addOnFailureListener(new OnFailureListener() {
//            @Override
//            public void onFailure(@NonNull Exception e) {
//                Toast.makeText(AccountActivity.this, "Failed retrieving data", Toast.LENGTH_SHORT).show();
//                gotoMainActivity();
//            }
//        });
//        setChangeProfilePicButtonOnClick();
        setCancelButtonOnClick();

    }

//    private void setChangeProfilePicButtonOnClick() {
//        Log.d(TAG, "in setChangeProfilePicButtonOnClick");
//        buttonChangeProfilePic.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent openGalleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//                startActivityForResult(openGalleryIntent, 1000);
//            }
//        });
//    }


    private void showName() {
        Log.d(TAG, "in showName with name: " + userName);

        account_EDTXT_nickName.setText(userName);
    }

    private void showProfilePic() {
        Log.d(TAG, "in showProfilePic");
        StorageReference userStorageRef = firebaseStorage.getReference().child(initialUserProfilePicURI);

        userStorageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.get().load(uri).into(profileImage);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(AccountActivity.this,
                        "Loading profile pic failed with error: " + e.getMessage(),
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setSaveButtonOnClick() {
        Log.d(TAG, "in setSaveButtonOnClick");

        account_BTN_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (areFieldsValid()) {
                    if (didComeFromRegister) {
                        saveNewUser();
                    } else {
                        updateExistingUser();
                    }
                }
                gotoMainActivity();
            }
        });
    }

    private void setButtonDeleteAccountOnClick() {
        account_BTN_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // TODO: handle deleting users from events theyre invited to
                // delete user from Users collection
                usersCollectionRef.document(userID).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {

                        // delete user from Events collection
                        // delete user from authentication
                        firebaseUser.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Log.d(TAG, "Successfully deleted user");
                                Toast.makeText(AccountActivity.this, "Successfully deleted user", Toast.LENGTH_SHORT).show();
                                gotoLoginActivity();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(AccountActivity.this, "Failed to delete user", Toast.LENGTH_SHORT).show();
                            }
                        });

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                        Toast.makeText(AccountActivity.this, String.format("Failed to delete " +
                                        "account with email %s, error:", userEmail, e.getMessage()),
                                Toast.LENGTH_SHORT).show();
                    }
                });



            }
        });

    }

    private void saveNewUser() {
        UserModel userModel = new UserModel();
        userModel.setUserID(firebaseUser.getUid());
        userModel.setUserName(account_EDTXT_nickName.getText().toString());
        userModel.setUserEmail(firebaseUser.getEmail());
        MyFirebaseDB.getMe().CreateUser(userModel);
    }

    private void updateExistingUser() {
        MyFirebaseDB.getMe().updateUserName(account_EDTXT_nickName.getText().toString(), firebaseUser.getUid());
    }


    private void onUserSaveSuccess() {
        Toast.makeText(AccountActivity.this, "User created successfully",
                Toast.LENGTH_SHORT).show();
        gotoMainActivity();
    }

    private void onUserSaveFailure(Exception e) {
        Toast.makeText(AccountActivity.this, "User could not be created, " +
                "error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
    }

    private boolean areFieldsValid() {
        boolean isNameEmpty = TextUtils.isEmpty(account_EDTXT_nickName.getText());
//        boolean isProfilePicURLEmpty = TextUtils.isEmpty(initialUserProfilePicURI);
        return !isNameEmpty;
    }


    private void setCancelButtonOnClick() {
        account_BTN_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gotoMainActivity();

            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1000) {
            if (resultCode == Activity.RESULT_OK) {
                uploadedPhoto = data.getData();
                Picasso.get().load(uploadedPhoto).into(profileImage);

            }
        }
    }


    private void saveProfilePicToStorage(Uri profilePicToUpload) {
        Log.d(TAG, "in saveProfilePicToStorage");
        firebaseStorage.getReference().child(userID).putFile(profilePicToUpload).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                updatedUserProfilePicURI = userID;
                Toast.makeText(AccountActivity.this, "Profile picture uploaded",
                        Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(AccountActivity.this, "Profile Picture couldn't upload", Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void gotoLoginActivity() {
        Intent loginIntent = new Intent(AccountActivity.this, LoginActivity.class);
        startActivity(loginIntent);
        // prevent option to back-click back to here
        finish();
    }

    private void gotoMainActivity() {
        Intent mainIntent = new Intent(AccountActivity.this, MainActivity.class);
        startActivity(mainIntent);
        // prevent option to back-click back to here
        finish();
    }


}
