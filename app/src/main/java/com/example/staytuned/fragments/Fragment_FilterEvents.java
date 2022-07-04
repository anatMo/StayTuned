package com.example.staytuned.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;

import com.example.staytuned.R;

public class Fragment_FilterEvents extends Fragment {

    private Button filter_BTN_allEvents;
    private Button filter_BTN_myEvents;

    public Fragment_FilterEvents() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_filter_events, container, false);
        findViews(view);

        filter_BTN_allEvents.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment_All_Events allEvents = new Fragment_All_Events();
                getParentFragmentManager().beginTransaction().replace(R.id.panel_FRAME_content, allEvents).commit();
            }
        });

        filter_BTN_myEvents.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment_My_Events myEvents = new Fragment_My_Events();
                getParentFragmentManager().beginTransaction().replace(R.id.panel_FRAME_content, myEvents).commit();

            }
        });

        return view;
    }

    private void findViews(View view) {
        filter_BTN_allEvents = view.findViewById(R.id.filter_BTN_allEvents);
        filter_BTN_myEvents = view.findViewById(R.id.filter_BTN_myEvents);
    }
}