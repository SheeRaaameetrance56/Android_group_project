package com.ousl.application_event_management.views.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ousl.application_event_management.R;
import com.ousl.application_event_management.models.PrivateEvents;

import java.util.ArrayList;
import java.util.List;

public class PrivateEventAdapter extends RecyclerView.Adapter<PrivateEventAdapter.MyViewHolder>{
    private Context context;
    private List<PrivateEvents> privateEventList;

    private PrivateEventAdapter.OnItemClickListener onItemClickListener;


    public PrivateEventAdapter(Context context){
        this.context = context;
        privateEventList = new ArrayList<>();
    }

    public void addEvent(PrivateEvents privateEvent){
        privateEventList.add(privateEvent);
        notifyDataSetChanged();
    }

    // Interface for item click handling
    public interface OnItemClickListener {
        void onItemClick(PrivateEvents event, String eventId, String userId);
    }

    public void setOnItemClickListener(PrivateEventAdapter.OnItemClickListener listener) {
        this.onItemClickListener = listener;

    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        private TextView title, date;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.private_card_view_title);
            date = itemView.findViewById(R.id.private_card_view_date);

            itemView.setOnClickListener(view -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION && onItemClickListener != null) {
                    PrivateEvents clickedEvent = privateEventList.get(position);
                    onItemClickListener.onItemClick(clickedEvent, clickedEvent.getEventId(), clickedEvent.getUserId());
                }
            });

        }
    }

    @NonNull
    @Override
    public PrivateEventAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_private_card_view, parent, false);
        return new PrivateEventAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        PrivateEvents privateEvent = privateEventList.get(position);
        holder.title.setText(privateEvent.getTitle());
        holder.date.setText(privateEvent.getDate());
    }


    @Override
    public int getItemCount() {
        return privateEventList.size();
    }
}
