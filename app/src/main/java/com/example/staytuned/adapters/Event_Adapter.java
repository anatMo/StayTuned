package com.example.staytuned.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.example.staytuned.R;
import com.example.staytuned.models.EventModel;
import com.example.staytuned.utils.MyFireBaseAuthontication;
import com.example.staytuned.utils.MyFirebaseDB;

import java.util.ArrayList;

public class Event_Adapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Activity activity;
    private ArrayList<EventModel> events = new ArrayList<>();
    private Context context;
    private Event_Adapter(Context context) {
        this.context = context;
    }
    int i = 0;



    private static Event_Adapter me;

    public static Event_Adapter getMe() {
        return me;
    }

    public static Event_Adapter initHelper(Context context) {
        if (me == null) {
            me = new Event_Adapter(context);
        }
        return me;
    }

    public Event_Adapter setActivity(Activity activity) {
        this.activity = activity;
        return this;
    }

    public void setEvents(ArrayList<EventModel> events){
        this.events = events;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_event_item, parent, false);
        EventHolder eventHolder = new EventHolder(view);
        return eventHolder;
    }


    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {

        final EventHolder holder = (EventHolder) viewHolder;
        EventModel event = getItem(position);

        holder.singleEvent_TXT_eventName.setText(event.getEventName());
        holder.singleEvent_TXT_description.setText(event.getEventDescription());
        holder.singleEvent_TXT_createdBy.setText(event.getEventCreatorName());
        holder.singleEvent_TXT_date.setText(event.getEventTimeStarting().toString());

        String uid = MyFireBaseAuthontication.getMe().getFireBaseUser().getUid();

        if(event.getUsersAttending() != null){
            if(event.getUsersAttending().containsKey(uid)){
                holder.singleEvent_BTN_goToEvent.setVisibility(View.GONE);
            }else {
                holder.singleEvent_BTN_goToEvent.setVisibility(View.VISIBLE);

            }
        }else {
            holder.singleEvent_BTN_goToEvent.setVisibility(View.VISIBLE);
        }

    }

    @Override
    public int getItemCount() {
        return events.size();
    }

    public EventModel getItem(int position) {
        return events.get(position);
    }



    class EventHolder extends RecyclerView.ViewHolder {

        private TextView singleEvent_TXT_eventName;
        private TextView singleEvent_TXT_description;
        private TextView singleEvent_TXT_createdBy;
        private TextView singleEvent_TXT_date;
        private Button singleEvent_BTN_goToEvent;
        public EventHolder(View itemView) {
            super(itemView);
            singleEvent_TXT_eventName = itemView.findViewById(R.id.singleEvent_TXT_eventName);
            singleEvent_TXT_description = itemView.findViewById(R.id.singleEvent_TXT_description);
            singleEvent_TXT_createdBy = itemView.findViewById(R.id.singleEvent_TXT_createdBy);
            singleEvent_TXT_date = itemView.findViewById(R.id.singleEvent_TXT_date);
            singleEvent_BTN_goToEvent = itemView.findViewById(R.id.singleEvent_BTN_goToEvent);

            singleEvent_BTN_goToEvent.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(context,"GOING",Toast.LENGTH_LONG).show();
                    MyFirebaseDB.getMe().setGoingToEvent(getItem(getAdapterPosition()).getEventID(), MyFireBaseAuthontication.getMe().getFireBaseUser().getUid());
                    singleEvent_BTN_goToEvent.setVisibility(View.GONE);


                }
            });



        }

    }

}
