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
import com.ousl.application_event_management.models.PublicEvent;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class MyEventsAdapter extends RecyclerView.Adapter<MyEventsAdapter.MyViewHolder>{

    private Context context;
    private List<PublicEvent> myEventList;


    public MyEventsAdapter(Context context){
        this.context = context;
        myEventList = new ArrayList<>();
    }

    public void addEvent(PublicEvent publicEvent){
        myEventList.add(publicEvent);
        notifyDataSetChanged();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        private TextView title, date;
        private ImageView image;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.card_view_title);
            date = itemView.findViewById(R.id.card_view_date);
            image = itemView.findViewById(R.id.card_view_image);
        }
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_card_view, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        PublicEvent myEvent = myEventList.get(position);
        holder.title.setText(myEvent.getTitle());
        Picasso.get().load(myEvent.getImageUrl()).into(holder.image);
    }

    @Override
    public int getItemCount() {
        return myEventList.size();
    }


}
