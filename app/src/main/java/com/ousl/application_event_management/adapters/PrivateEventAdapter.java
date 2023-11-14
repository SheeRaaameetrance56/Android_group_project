package com.ousl.application_event_management.adapters;

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

public class PrivateEventAdapter extends RecyclerView.Adapter<PrivateEventAdapter.MyViewHolder>{
    private Context context;
    private List<PrivateEvents> privateEventList;


    public PrivateEventAdapter(Context context){
        this.context = context;
        privateEventList = new ArrayList<>();
    }

    public void addEvent(PrivateEvents privateEvent){
        privateEventList.add(privateEvent);
        notifyDataSetChanged();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        private TextView title;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.card_view_title);

        }
    }

    @NonNull
    @Override
    public PrivateEventAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_card_view, parent, false);
        return new PrivateEventAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        PrivateEvents privateEvent = privateEventList.get(position);
        holder.title.setText(privateEvent.getTitle());
    }


    @Override
    public int getItemCount() {
        return privateEventList.size();
    }
}