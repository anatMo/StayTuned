package com.example.staytuned.utils;

import android.content.Context;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.staytuned.models.EventModel;
import com.example.staytuned.models.UserModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.UUID;

public class MyFirebaseDB {
    private Context context;

    private MyFirebaseDB(Context context) {
        this.context = context;
    }

    private FirebaseDatabase database = FirebaseDatabase.getInstance();

    private static MyFirebaseDB me;

    private Callback_getEvents callback_getEvents;

    private Callback_getMyEvents callback_getMyEvents;

    public static MyFirebaseDB getMe() {
        return me;
    }

    public static MyFirebaseDB initHelper(Context context) {
        if (me == null) {
            me = new MyFirebaseDB(context);
        }
        return me;
    }

    public void updateUserName(String name, String uid) {
        DatabaseReference
                ref =  database.getReference()
                .child("Users")
                .child(uid).child("userName");

        ref.setValue(name);
    }

    public void setGoingToEvent(String eventID, String uid) {
        DatabaseReference
                ref =  database.getReference()
                .child("Events")
                .child(eventID)
                .child("usersAttending").child(uid);

        ref.setValue(uid);


        ref =  database.getReference()
                .child("Users")
                .child(uid)
                .child("events").child(eventID);

        ref.setValue(eventID);
    }



    public interface Callback_getEvents {
        void eventsReturned(ArrayList<EventModel> events);
    }

    public MyFirebaseDB setCallback_getMyEvents(Callback_getMyEvents callback_getMyEvents) {
        this.callback_getMyEvents = callback_getMyEvents;
        return this;
    }

    public interface Callback_getMyEvents {
        void myEventsReturned(ArrayList<EventModel> events);
    }

    public MyFirebaseDB setCallback_getEvents(Callback_getEvents callback_getEvents) {
        this.callback_getEvents = callback_getEvents;
        return this;
    }



    public void CreateEvent(EventModel eventModel) {
        eventModel.setEventID(UUID.randomUUID().toString());

        DatabaseReference
                ref =  database.getReference()
                .child("Events")
                .child(eventModel.getEventID());

        ref.setValue(eventModel);
    }

    public void getAllEvents() {
        database.getReference()
                .child("Events")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot snapshot) {
                        ArrayList<EventModel> events = new ArrayList<>();
                        for(DataSnapshot ds : snapshot.getChildren()) {
                            EventModel event = ds.getValue(EventModel.class);
                            events.add(event);
                            Toast.makeText(context, "getAllEvents", Toast.LENGTH_SHORT).show();
                        }
                        callback_getEvents.eventsReturned(events);
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }
    public void CreateUser(UserModel user) {
        DatabaseReference
                ref =  database.getReference()
                .child("Users")
                .child(user.getUserID());

        ref.setValue(user);
    }

    public void getMyEvents() {
        DatabaseReference
                ref =  database.getReference();

        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                ArrayList<EventModel> events = new ArrayList<>();
                DataSnapshot ds = snapshot.child("Users").child(MyFireBaseAuthontication.getMe().getFireBaseUser().getUid());
                UserModel user = ds.getValue(UserModel.class);

                if (user.getEvents() != null){
                    for (String eventId: user.getEvents().keySet()){
                        EventModel event = snapshot.child("Events").child(eventId).getValue(EventModel.class);
                        if(event != null){
                            events.add(event);
                        }
                    }
                }

                callback_getMyEvents.myEventsReturned(events);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
