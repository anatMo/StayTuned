package com.example.staytuned.activities;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.example.staytuned.R;
import com.example.staytuned.fragments.Fragment_GoogleMap;
import com.example.staytuned.models.EventModel;
import com.example.staytuned.models.InvitedInEventUserModel;
import com.example.staytuned.utils.MyFirebaseDB;
import com.example.staytuned.utils.MyGoogleMapsPlaces;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

//import com.example.staytuned.utils.map_utils.CustomMapTileProvider;
//import com.example.staytuned.utils.map_utils.OfflineTileProvider;

public class CreateEventActivity extends AppCompatActivity {
    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;
    private final String TAG = "tagCreateEventActivity";

    // Views
    private EditText create_EDTXT_eventName;
    private EditText create_EDTXT_eventDesc;

    private Button create_BTN_createEvent;
    private ProgressBar create_PGB_bar;
    private CardView create_CDV_locationSearch;
    private ListView listViewInvitedUsers;
    private AutoCompleteTextView autoCompleteInvitedUsers;


    private TextView create_TXTV_time;
    private TextView create_TXTV_date;
    private TimePicker create_timePicker;
    private DatePicker create_datePicker;


    // Utils
    private ArrayAdapter<String> autoCompleteInvitedUsersAdapter;
    private ArrayList<String> autoCompleteInvitedUsersList;
    private ArrayAdapter<String> listViewInvitedUsersAdapter;
    private ArrayList<String> listViewInvitedUsersList;


    // Firebase
    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;
    private DocumentReference currUserDocumentRef;
    private FirebaseFirestore firebaseFirestore;
    private CollectionReference usersCollectionRef;
    private CollectionReference eventsCollectionRef;
    // Fields for storing data to push to FirebaseFirestore (ff)
    private String ffEventName;
    private String ffEventDescription;
    private String ffEventLocationName;
    private String ffEventCreatorUID;
    private String ffEventCreatorName;
    private Date ffEventTimeCreated;
    private Date ffEventTimeStarting;
    private List<InvitedInEventUserModel> ffInvitedUserInEventModels;

    private boolean didFinishSettingCreatorName;
    private boolean didFinishSettingUsers;
    private ArrayList<String> ffEventInvitedUserIDs;

    private Fragment_GoogleMap fragment_googlemap;

    private double currentLat;
    private double currentLng;


    private void googlePlacesAutoComplete() {

        MyGoogleMapsPlaces.getMe().initPlaces();

        AutocompleteSupportFragment autocompleteFragment = (AutocompleteSupportFragment)
                getSupportFragmentManager().findFragmentById(R.id.create_FRG_fragment);

        // Specify the types of place data to return.
        autocompleteFragment.setPlaceFields(Arrays.asList(Place.Field.LAT_LNG, Place.Field.NAME));

        // Set up a PlaceSelectionListener to handle the response.
        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(@NonNull Place place) {

                fragment_googlemap.setMapLocation(place.getLatLng().latitude, place.getLatLng().longitude);
                currentLat = place.getLatLng().latitude;
                currentLng = place.getLatLng().longitude;
                ffEventLocationName = place.getName();
            }

            @Override
            public void onError(@NonNull Status status) {
                Log.i("pttt", "An error occurred: " + status);
            }
        });
    }

    private void initializeFields() {

        // Views
        create_EDTXT_eventName = findViewById(R.id.create_EDTXT_eventName);
        create_EDTXT_eventDesc = findViewById(R.id.create_EDTXT_eventDesc);
        create_BTN_createEvent = findViewById(R.id.create_BTN_createEvent);
        create_PGB_bar = findViewById(R.id.create_PGB_bar);
        create_CDV_locationSearch = findViewById(R.id.create_CDV_locationSearch);
        create_timePicker = findViewById(R.id.create_timePicker);
        create_datePicker = findViewById(R.id.create_datePicker);
        create_timePicker.setIs24HourView(true);


        // Firebase
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        firebaseFirestore = FirebaseFirestore.getInstance();
        usersCollectionRef = firebaseFirestore.collection(getString(R.string.ff_Users));
        eventsCollectionRef = firebaseFirestore.collection(getString(R.string.ff_Events));
        currUserDocumentRef = usersCollectionRef.document(firebaseUser.getUid());

        initializeTimeAndDate();

        ffEventInvitedUserIDs = new ArrayList<>();

        setEventCreatorUID();
        setEventCreatorName();

    }

    private void initializeTimeAndDate() {
        Context createContext = this;


        initializeTimePicker(createContext);
        initializeDatePicker(createContext);

    }

    private void updateCurrentDate() {
        create_TXTV_date = findViewById(R.id.create_TXTV_date);
        Date currDate = Calendar.getInstance().getTime();
        String day = (String) DateFormat.format("dd", currDate); // 20
        String monthNumber = (String) DateFormat.format("MM", currDate); // 06
        String year = (String) DateFormat.format("yyyy", currDate); // 2013
        create_TXTV_date.setText(day + "/" + monthNumber + "/" + year);
    }


    private void initializeDatePicker(Context createContext) {
        updateCurrentDate();

        create_TXTV_date.setOnClickListener(new View.OnClickListener() {
            //TODO: Hide soft keyboard when choosing items in craeteevent
            @Override
            public void onClick(View v) {
                String date = create_TXTV_date.getText().toString();
                int day = -1;
                int month = -1;
                int year = -1;
                int idx = 0;
                for (int i = 0; i < date.length(); i++) {
                    if (date.charAt(i) == '/') {
                        if (day == -1) {
                            day = Integer.parseInt(date.substring(0, i));
                            idx = i;
                        } else if (month == -1) {
                            month = Integer.parseInt(date.substring(idx + 1, i));
                            year = Integer.parseInt(date.substring(i + 1));
                        }
                    }
                }
                create_TXTV_date.setText(day + "/" + month + "/" + year);

                DatePickerDialog mDatePicker;
//                int resTheme = R.style.SpinnerTimePicker;

//                int resTheme = DatePickerDialog.THEME_HOLO_LIGHT;
                mDatePicker = new DatePickerDialog(createContext, R.style.DialogTheme,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int selectedYear, int selectedMonth, int selectedDayOfMonth) {
//                                int correctMonth = selectedMonth + 1;
                                create_TXTV_date.setText(selectedDayOfMonth + "/" + selectedMonth + "/" + selectedYear);
                            }
                        }, year, month, day);//Yes 24 hour time
                mDatePicker.setTitle("Select Date");
                mDatePicker.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                mDatePicker.show();


            }

        });
    }

    private void updateTime() {
        create_TXTV_time = findViewById(R.id.create_TXTV_time);
        Calendar cal = Calendar.getInstance();
        String time = cal.getTime().toString();
        String hour = "";
        String minute = "";
        for (int i = 0; i < time.length(); i++) {
            if (time.charAt(i) == ':') {
                hour = time.substring(i - 2, i);
                minute = time.substring(i + 1, i + 3);
                break;
            }
        }
        create_TXTV_time.setText(hour + ":" + minute);
    }

    private void initializeTimePicker(Context createContext) {
        updateTime();
        create_TXTV_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int hour = -1;
                int minute = -1;
                String time = create_TXTV_time.getText().toString();
                for (int i = 0; i < time.length(); i++) {
                    if (time.charAt(i) == ':') {
                        hour = Integer.parseInt(time.substring(i - 2, i));
                        minute = Integer.parseInt(time.substring(i + 1, i + 3));
                        break;
                    }
                }
                TimePickerDialog mTimePicker;
//                int resTheme = TimePickerDialog.THEME_HOLO_LIGHT;
                mTimePicker = new TimePickerDialog(createContext, R.style.TimePickerTheme,
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                                String viewMinute = String.format("%s%s", selectedMinute < 10 ? "0" : "",
                                        selectedMinute);

                                create_TXTV_time.setText(selectedHour + ":" + viewMinute);
                            }
                        }, hour, minute, true);//Yes 24 hour time
                mTimePicker.setTitle("Select Time");
                mTimePicker.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                mTimePicker.show();

            }

        });
    }


    private void initializeListeners() {
        initializeCreateEventListener();
    }

    private void initializeCreateEventListener() {
        create_BTN_createEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showProgressBar();

                boolean didSetFields = retrieveAndSetEventFields();
                if (didSetFields) {
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            {
                                createEvent();
                                gotoEvents();
//                        loadingDialog.dismiss();//dismiss the dialog box once data is retreived
//                        tvUserName.setText(u_name);
                            }
                        }
                    }, 2000);
                } else {
                }// 2000 milliseconds = 2seconds

            }
        });
    }

    private void gotoEvents() {
        Intent eventsIntent = new Intent(CreateEventActivity.this, MainActivity.class);
        startActivity(eventsIntent);
        // don't allow going back to creating event
        hideProgressBar();
        finish();

    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fragment_googlemap = new Fragment_GoogleMap();
        getSupportFragmentManager().beginTransaction().add(R.id.create_MAP_mapView,fragment_googlemap).commit();
        setContentView(R.layout.activity_create_event);

        initializeFields();
        initializeListeners();

        googlePlacesAutoComplete();
    }



    private EventModel createNewEventModel() {
        EventModel createdEvent = new EventModel();
        createdEvent.setEventName(ffEventName);
        createdEvent.setEventDescription(ffEventDescription);
        createdEvent.setEventLocationName(ffEventLocationName);
        createdEvent.setEventTimeCreated(ffEventTimeCreated);
        createdEvent.setEventTimeStarting(ffEventTimeStarting);
        createdEvent.setEventCreatorUID(ffEventCreatorUID);
        createdEvent.setEventCreatorName(ffEventCreatorName);
        return createdEvent;
    }


    private void showProgressBar() {
        create_BTN_createEvent.setText("");
        create_PGB_bar.setVisibility(View.VISIBLE);
    }

    private void hideProgressBar() {
        create_PGB_bar.setVisibility(View.INVISIBLE);
        create_BTN_createEvent.setText("Create Event");

    }

    private void createEvent() {
        EventModel newEventModel = createNewEventModel();
        MyFirebaseDB.getMe().CreateEvent(newEventModel);


    }


    private boolean retrieveAndSetEventFields() {
        String toastErrorMessage = "";


        if (!setEventName(create_EDTXT_eventName.getText().toString())) {
            toastErrorMessage = "Fix event name";
        } else if (!setEventDescription(create_EDTXT_eventDesc.getText().toString())) {
            toastErrorMessage = "Fix event description";


        } else if (ffEventLocationName == null || ffEventLocationName.length() == 0) {
            toastErrorMessage = "Please enter event location";
            // ffEventLocation should be full since it is retrieved from the search query
        } else if (!setEventTimeCreated()) {
            toastErrorMessage = "Error setting creation time";
            // TODO: Time starting not working (giving weird times)
            // TODO: Fix time to be taken from time picker dialog
        } else if (!setEventTimeStarting()) {
            toastErrorMessage = "Fix time starting";
        }
        if (!TextUtils.isEmpty(toastErrorMessage)) {
            Toast.makeText(this, toastErrorMessage, Toast.LENGTH_SHORT).show();
            Log.d(TAG, toastErrorMessage);
            hideProgressBar();
            return false;
        } else {
            return true;
        }
    }

    private boolean setEventName(String eventName) {
        boolean nameNotEmpty = !TextUtils.isEmpty(eventName);
        if (nameNotEmpty) {
            this.ffEventName = eventName;
            return true;
        } else {
            return false;
        }
    }

    private boolean setEventDescription(String eventDescription) {
        boolean descriptionNotEmpty = !TextUtils.isEmpty(eventDescription);
        if (descriptionNotEmpty) {
            this.ffEventDescription = eventDescription;
            return true;
        } else {
            return false;
        }
    }

    private boolean setEventCreatorUID() {
        this.ffEventCreatorUID = this.firebaseUser.getUid();
        return true;
    }

    private boolean setEventCreatorName() {
        currUserDocumentRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                ffEventCreatorName =
                        documentSnapshot.getString(getString(R.string.ff_Users_userName));
                Log.d("create", "event creator name is " + ffEventCreatorName);
                didFinishSettingCreatorName = true;
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

                Log.d("CreateEventActivity", String.format("Error getting creator" +
                        " user name: %s", e.getMessage()));
            }


        }).addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
            }
        });
        return true;
    }

    private boolean setEventTimeCreated() {
        this.ffEventTimeCreated = Timestamp.now().toDate();
        return true;
    }

    private boolean setEventTimeStarting() {
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy,HH:mm", Locale.ENGLISH);
        Date date;
        try {
            date = formatter.parse(String.format("%s,%s", create_TXTV_date.getText(),
                    create_TXTV_time.getText()));
            this.ffEventTimeStarting = date;
            return true;
        } catch (ParseException e) {
            e.printStackTrace();
            return false;
        }

    }

}

