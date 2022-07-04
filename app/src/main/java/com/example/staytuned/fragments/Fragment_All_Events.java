package com.example.staytuned.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.staytuned.R;
import com.example.staytuned.adapters.Event_Adapter;
import com.example.staytuned.models.EventModel;
import com.example.staytuned.utils.MyFirebaseDB;

import java.util.ArrayList;


public class Fragment_All_Events extends Fragment {

    private RecyclerView events_LST_items;


    public Fragment_All_Events() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_events, container, false);
        findViews(view);
        initCallbacks();
        MyFirebaseDB.getMe().getAllEvents();

        return view;
    }

    private void initCallbacks() {
        MyFirebaseDB.getMe().setCallback_getEvents(callback_getEvents);
    }

    private void findViews(View view) {
        events_LST_items = view.findViewById(R.id.events_LST_items);
    }

    MyFirebaseDB.Callback_getEvents callback_getEvents = new MyFirebaseDB.Callback_getEvents() {
        @Override
        public void eventsReturned(ArrayList<EventModel> events) {
            Event_Adapter.getMe().setEvents(events);
            events_LST_items.setLayoutManager(new LinearLayoutManager(getContext()));
            events_LST_items.setHasFixedSize(true);
            events_LST_items.setAdapter(Event_Adapter.getMe());

        }
    };
}