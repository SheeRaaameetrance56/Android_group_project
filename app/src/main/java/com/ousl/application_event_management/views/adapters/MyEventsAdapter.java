package com.ousl.application_event_management.views.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ousl.application_event_management.R;
import com.ousl.application_event_management.models.PrivateEvents;
import com.ousl.application_event_management.models.PublicEvent;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class MyEventsAdapter extends RecyclerView.Adapter<MyEventsAdapter.MyViewHolder>{

    private Context context;
    private List<PrivateEvents> myEventList;


    public MyEventsAdapter(Context context){
        this.context = context;
        myEventList = new ArrayList<>();
    }

    public void addEvent(PrivateEvents privateEvents){
        myEventList.add(privateEvents);
        notifyDataSetChanged();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        private TextView title, date;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.private_card_view_title);
            date = itemView.findViewById(R.id.private_card_view_date);
        }
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_private_card_view, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        PrivateEvents myEvent = myEventList.get(position);
        holder.title.setText(myEvent.getTitle());
        holder.date.setText(myEvent.getDate());
    }

    @Override
    public int getItemCount() {
        return myEventList.size();
    }


}
